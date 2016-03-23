package br.com.deepmapper.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Random;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;

import br.com.deepmapper.constans.GoogleConstants;
import br.com.deepmapper.constans.TextConstants;

public class UnitUtil {
	private static final Logger logger = LogManager.getLogger(UnitUtil.class);

	public UnitUtil() {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
				"org.apache.commons.logging.impl.NoOpLog");
		java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.OFF);
		java.util.logging.Logger.getLogger("org.apache.commons.httpclient").setLevel(Level.OFF);
		logger.trace(getClass());
	}

	/**
	 * Method description: Configuring web client for surface or deepweb
	 *
	 * @since 16 de mar de 2016 08:46:37
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param int
	 *            clientType: 1 = Surface, 2 = Deep
	 * @return WebClient webClient
	 */
	public WebClient newWebConfigure(int clientType) {
		logger.trace("webConfigure()");
		WebClient webClient = null;

		if (clientType == 1) {
			webClient = alterNavi();
		} else if (clientType == 2) {
			webClient = configuringWebClient(new WebClient());
			ProxyConfig prc = new ProxyConfig(TextConstants.proxyIP, 9150, true);
			webClient.getOptions().setProxyConfig(prc);
		}

		return webClient;
	}

	/**
	 * Method description: Google point of acess and search.
	 *
	 * @since 15 de mar de 2016 07:59:25
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param String
	 *            serchString;
	 * @return HtmlPage googlePage;
	 */
	public HtmlPage googleAcess(String serchString) {
		logger.trace("googleAcess()");

		HtmlPage googlePage = null;

		WebClient webClient = newWebConfigure(TextConstants.surfaceConfig);

		try {
			googlePage = webClient.getPage(GoogleConstants.googleLink);
		} catch (FailingHttpStatusCodeException e) {
			logger.error("Http Status " + e + ".");
		} catch (MalformedURLException e) {
			logger.error("Malformed Url " + e + ".");
		} catch (IOException e) {
			logger.error("IO " + e + ".");
		}
		HtmlInput serchInput = googlePage.getElementByName(GoogleConstants.inputSerchName);
		serchInput.setValueAttribute(serchString);

		HtmlSubmitInput submitSerch = googlePage.getElementByName(GoogleConstants.searchBtnName);

		try {
			logger.trace("Submiting serch to " + GoogleConstants.googleLink + " serching for " + serchString + ".");
			googlePage = submitSerch.click();
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.trace("Returning serched page.");
		return googlePage;
	}

	/**
	 * Method description: Webclient configuration
	 *
	 * @since 15 de mar de 2016 07:59:02
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 */
	private static WebClient configuringWebClient(WebClient webClient) {
		logger.trace("WebClient()");
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.setCssErrorHandler(new SilentCssErrorHandler());

		return webClient;
	}

	/**
	 * Method description: Randomize webclient
	 *
	 * @since 17 de mar de 2016 08:07:01
	 * @author Guilherme Scarabelo <gui_fernando@hotmail.com>
	 * @version 1.0
	 * @param Webclient
	 *            wb
	 * @return
	 */
	@SuppressWarnings({ "deprecation" })
	public static WebClient alterNavi() {
		logger.trace("alterNavi()");
		WebClient wb = null;
		Random randomGenerator = new Random();
		int randonInt = randomGenerator.nextInt(3);;
		

		if (randonInt == 0) {
			wb = new WebClient(BrowserVersion.FIREFOX_38, TextConstants.proxyIP, TextConstants.proxyPort);
			logger.info("Alter navi to " + BrowserVersion.FIREFOX_38.getNickname());

		} else if (randonInt == 1) {
			wb = new WebClient(BrowserVersion.EDGE, TextConstants.proxyIP, TextConstants.proxyPort);
			logger.info("Alter navi to " + BrowserVersion.EDGE.getNickname());

		} else if (randonInt == 2) {
			wb = new WebClient(BrowserVersion.INTERNET_EXPLORER_11, TextConstants.proxyIP, TextConstants.proxyPort);
			logger.info("Alter navi to " + BrowserVersion.INTERNET_EXPLORER_11.getNickname());

		} else if (randonInt == 3){
			wb = new WebClient(BrowserVersion.CHROME, TextConstants.proxyIP, TextConstants.proxyPort);
			logger.info("Alter navi to " + BrowserVersion.CHROME.getNickname());
		}

		wb = configuringWebClient(wb);

		return wb;
	}

}
