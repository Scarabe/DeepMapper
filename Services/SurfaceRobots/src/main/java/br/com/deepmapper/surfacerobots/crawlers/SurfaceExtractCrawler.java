package br.com.deepmapper.surfacerobots.crawlers;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.constans.GoogleConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;
import br.com.deepmapper.surfacerobots.test.SurfaceTest;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;

public class SurfaceExtractCrawler {
	private static final Logger logger = LogManager.getLogger(SurfaceTest.class);
	private UnitUtil unitUtil = new UnitUtil();
	private RegexUtil regexUtil = new RegexUtil();
	private MongoUtil dbUtil = new MongoUtil();

	public void googleRuningPages() {
		logger.trace(getClass());
		
		ExecutorService executor = Executors.newFixedThreadPool( 2 );

		HtmlPage gPage = unitUtil.googleAcess(GoogleConstants.serchContent);
		String searchUrl = gPage.getUrl().toString() + GoogleConstants.googleLinkPlusPage;

		for (int searchResult = 0; searchResult <= GoogleConstants.googleMaxSearch; searchResult += 10) {
			final int searchFinal = searchResult;
			
			executor.submit((Runnable & Serializable) () -> {
				logger.trace("*******************************************************");
				logger.trace("Starting google page" + GoogleConstants.googleLinkPlusPage + searchFinal);
				try {
					HtmlPage searchPage = gPage.getWebClient().getPage(searchUrl + searchFinal);
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
			});
		}
	}

	public void googlePgCrawler(HtmlPage gPage) {	
		logger.trace(getClass());

		List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();

		HtmlDivision linksTable = (HtmlDivision) gPage.getElementById(GoogleConstants.linksTable);
		@SuppressWarnings("unchecked")
		List<HtmlElement> linksList = (List<HtmlElement>) linksTable.getByXPath("//h3[@class='r']/a");

		for (HtmlElement link : linksList) {
			try {
				logger.trace("Starting extraction from link: " + link.asText());
				HtmlPage surfaceLinkPage = link.click();
				noClassList = regexUtil.rxHtmlSurfApp(surfaceLinkPage, noClassList);
			} catch (Exception e) {
				logger.error("The link: " + link.asText() + " has a problem. \n" + e);
			}
		}
		;

		dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
	}
}
