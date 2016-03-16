package br.com.deepmapper.surfacerobots.crawlers;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.constans.GoogleConstants;
import br.com.deepmapper.constans.TextConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;
import br.com.deepmapper.surfacerobots.test.SurfaceTest;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;

public class GoogleExtractCrawler {
	private static final Logger logger = LogManager.getLogger(SurfaceTest.class);
	private UnitUtil unitUtil = new UnitUtil();
	private RegexUtil regexUtil = new RegexUtil();
	private MongoUtil dbUtil = new MongoUtil();

	/**
	 * Method description: Main method of google crawler, run on threads.
	 *
	 * @since 15 de mar de 2016 08:15:25
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 */
	public void googleRuningPages() {
		logger.trace("googleRuningPages()");

		CompletionService<Boolean> executor = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(TextConstants.maxThreadPool));

		HtmlPage gPage = unitUtil.googleAcess(GoogleConstants.serchContent);
		String searchUrl = gPage.getUrl().toString() + GoogleConstants.googleLinkPlusPage;

		List<Future<Boolean>> threads = new ArrayList<>();
		for (int searchResult = 0; searchResult <= GoogleConstants.googleMaxSearch; searchResult += 10) {
			final int searchFinal = searchResult;

			Future<Boolean> thread = executor.submit((Callable<Boolean> & Serializable) () -> {
				List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();
				
				logger.trace("*******************************************************");
				logger.trace("Starting google page" + GoogleConstants.googleLinkPlusPage + searchFinal);
				try {
					HtmlPage searchPage = gPage.getWebClient().getPage(searchUrl + searchFinal);
					noClassList = regexUtil.rxHtmlSurfApp(searchPage, noClassList);
					dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
					googlePgCrawler(searchPage);
				} catch (FailingHttpStatusCodeException e) {
					logger.error("Http Status " + e + ".");
				} catch (MalformedURLException e) {
					logger.error("Malformed Url " + e + ".");
				} catch (IOException e) {
					logger.error("IO " + e + ".");
				}
				logger.trace("Finished google page " + GoogleConstants.googleLinkPlusPage + searchFinal);
				logger.trace("*******************************************************");
				return true;
			});

			threads.add(thread);
		}

		threads.stream().forEach(thread -> {
			try {
				thread.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	/**
	 * Method description: Run all google pages and apply the regex.
	 *
	 * @since 15 de mar de 2016 08:16:37
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param HtmlPage
	 *            gPage
	 */

	public void googlePgCrawler(HtmlPage gPage) {
		logger.trace("googlePgCrawler()");

		List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();

		HtmlDivision linksTable = (HtmlDivision) gPage.getElementById(GoogleConstants.linksTable);

		if (linksTable != null) {
			@SuppressWarnings("unchecked")
			List<HtmlElement> linksList = (List<HtmlElement>) linksTable.getByXPath(GoogleConstants.xpathGLinks);

			for (HtmlElement link : linksList) {
				try {
					logger.trace("Starting extraction from link: " + link.asText());
					HtmlPage surfaceLinkPage = link.click();
					noClassList = regexUtil.rxHtmlSurfApp(surfaceLinkPage, noClassList);
				} catch (Exception e) {
					logger.error("The link: " + link.asText() + " has a problem. \n" + e);
				}
			}

			dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
		} else {
			logger.error("Google captcha.");
			// TODO MAKE CAPTCHA BREAKER
		}
	}
}
