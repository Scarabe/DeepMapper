package br.com.deepmapper.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import br.com.deepmapper.constans.GoogleConstants;

public class UnitUtil {
	private GoogleConstants googleConstants = new GoogleConstants();
	private static final Logger logger = LogManager.getLogger(UnitUtil.class);

	@SuppressWarnings("resource")
	public HtmlPage googleAcess(String serchString) {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		logger.trace(getClass());

		WebClient webClient = new WebClient();
		HtmlPage googlePage = null;

		webClient.getOptions().setJavaScriptEnabled(false);

		try {
			googlePage = webClient.getPage(googleConstants.getGoogleLink());
		} catch (FailingHttpStatusCodeException e) {
			logger.error("Http Status " + e + ".");
		} catch (MalformedURLException e) {
			logger.error("Malformed Url " + e + ".");
		} catch (IOException e) {
			logger.error("IO " + e + ".");
		}
		HtmlInput serchInput = googlePage.getElementByName(googleConstants.getInputSerchName());
		serchInput.setValueAttribute(serchString);

		HtmlSubmitInput submitSerch = googlePage.getElementByName(googleConstants.getSerchBtnName());

		try {
			logger.trace(
					"Submiting serch to " + googleConstants.getGoogleLink() + " serching for " + serchString + ".");
			googlePage = submitSerch.click();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.trace("Returning serched page.");
		return googlePage;
	}
}
