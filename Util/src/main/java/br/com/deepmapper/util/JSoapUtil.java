package br.com.deepmapper.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JSoapUtil {
	private static final Logger logger = LogManager.getLogger(JSoapUtil.class);
	public Document toDocument(HtmlPage page){
		logger.trace("toDocument()");
		
		logger.trace("Using jsoap parser.");
		return Jsoup.parse(page.asXml().toString());
	}
}
