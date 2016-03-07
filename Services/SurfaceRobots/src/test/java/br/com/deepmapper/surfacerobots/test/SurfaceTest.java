package br.com.deepmapper.surfacerobots.test;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.constans.TextConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;
import br.com.deepmapper.util.FileUtil;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;

public class SurfaceTest {
	private static final Logger logger = LogManager.getLogger(SurfaceTest.class);
	private FileUtil fileUtil = new FileUtil();
	private MongoUtil dbUtil = new MongoUtil();
	private UnitUtil unitUtil = new UnitUtil();
	private TextConstants textConstants = new TextConstants();
	private RegexUtil regexUtil = new RegexUtil();
	private DBConstants dbConstants = new DBConstants();

	@Test
	public void testSurface() {
		String noClassCol = dbConstants.getNoClassColl();

		logger.trace("Starting googleAcess.");
		HtmlPage gPage = unitUtil.googleAcess("Onion links");

		fileUtil.getHtmlPage(gPage.asXml(), "TesteGoogle", textConstants.getMyDesktop());

		logger.trace("Starting to get no classified links from any source.");
		List<NoClassifiedLinksDto> noClassList = regexUtil.rxHtmlSurfApp(gPage);

		logger.trace("Starting getMongoColl.");
		dbUtil.insertNoClass(noClassList, noClassCol);

		logger.trace("FINALIZADO PORRA!");
	}
}
