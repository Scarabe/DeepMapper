package br.com.deepmapper.deeprobots;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.constans.TextConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;

public class TorExtractCrawler {
	private static final Logger logger = LogManager.getLogger(TorExtractCrawler.class);
	private UnitUtil unitUtil = new UnitUtil();
	private MongoUtil dbUtil = new MongoUtil();
	private RegexUtil regexUtil = new RegexUtil();

	@Test
	public void torAcess() {
		logger.trace("torAcess()");
		
		final WebClient webClient = unitUtil.newWebConfigure(TextConstants.deepConfig);
		MongoCollection<Document> setedColl = dbUtil.getMongoColl(DBConstants.noClassColl);
		FindIterable<Document> cursor = setedColl.find();
		List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();

		for (Document onionLink : cursor) {
			final String urlOnion = onionLink.getString(DBConstants.onionID);

			try {
				HtmlPage onionPage = webClient.getPage(urlOnion);
				noClassList = regexUtil.rxHtmlSurfApp(onionPage, noClassList);
				dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
			} catch (FailingHttpStatusCodeException e) {
				logger.error("Http Status " + e + ".");
			} catch (MalformedURLException e) {
				logger.error("Malformed Url " + e + ".");
			} catch (IOException e) {
				logger.error("IO " + e + ".");
			}
		}
	}
}
