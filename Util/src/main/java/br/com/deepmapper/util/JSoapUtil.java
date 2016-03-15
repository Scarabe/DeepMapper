package br.com.deepmapper.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class JSoapUtil {
	private static final Logger logger = LogManager.getLogger(JSoapUtil.class);
	
	/**
	 * Method description: Convertnig one HtmlPage a Document with JSoap
	 *
	 * @since 15 de mar de 2016 08:10:21
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param HtmlPage page
	 * @return Document
	 */
	public Document toDocument(HtmlPage page){
		logger.trace("toDocument()");
		
		logger.trace("Using jsoap parser.");
		return Jsoup.parse(page.asXml().toString());
	}
}
