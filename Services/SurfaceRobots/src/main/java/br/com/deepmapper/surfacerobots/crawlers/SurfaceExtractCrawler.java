package br.com.deepmapper.surfacerobots.crawlers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
	
	public void googlePgCrawler(){
		logger.trace(getClass());
		logger.trace("Starting googleAcess.");
		HtmlPage gPage = unitUtil.googleAcess(GoogleConstants.serchContent);
		
		 List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();	
		
		HtmlDivision linksTable = (HtmlDivision) gPage.getElementById(GoogleConstants.linksTable);
		List<HtmlElement> linksList = linksTable.getElementsByTagName("a");
		
		
		for(HtmlElement link : linksList){
			try {
				logger.trace("Starting extraction from link: "+link.asText());
				HtmlPage surfaceLinkPage = link.click();
				noClassList= regexUtil.rxHtmlSurfApp(surfaceLinkPage, noClassList);
			} catch (Exception e) {
				logger.error("The link: "+link.asText()+" has a problem. \n"+e);
			}
		};
		
		
		dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
		
		logger.trace("Finished.");
		//HtmlDivision navBar = (HtmlDivision) gPage.getElementById(GoogleConstants.naviBar);
		//List<HtmlElement> gPages = linksTable.getElementsByTagName("a");	
	}
}
