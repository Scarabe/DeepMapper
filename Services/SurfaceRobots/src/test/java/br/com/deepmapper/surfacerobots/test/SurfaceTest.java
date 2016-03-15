package br.com.deepmapper.surfacerobots.test;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.surfacerobots.crawlers.GoogleExtractCrawler;
import br.com.deepmapper.util.FileUtil;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;
public class SurfaceTest {
	private static final Logger logger = LogManager.getLogger(SurfaceTest.class);

	@Test
	public void testSurface() {

		/*logger.trace("Starting googleAcess.");
		HtmlPage gPage = unitUtil.googleAcess("Onion links");
		
		fileUtil.getHtmlPage(gPage.asXml(), "TesteGoogle", TextConstants.myDesktop);

		logger.trace("Starting to get no classified links from any source.");
		List<NoClassifiedLinksDto> noClassList = regexUtil.rxHtmlSurfApp(gPage);

		logger.trace("Starting getMongoColl.");
		dbUtil.insertNoClass(noClassList, noClassCol);

		logger.trace("Ending of processing.");
		*/
		GoogleExtractCrawler Crawler = new GoogleExtractCrawler();
		Crawler.googleRuningPages();
	}
}
