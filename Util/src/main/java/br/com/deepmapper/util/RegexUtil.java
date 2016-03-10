package br.com.deepmapper.util;

import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.deepmapper.constans.RegexConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;

public class RegexUtil {
	private static final Logger logger = LogManager.getLogger(MongoUtil.class);
	private JSoapUtil jsoapUtil = new JSoapUtil();
	
	public List<NoClassifiedLinksDto> rxHtmlSurfApp(HtmlPage serchPage, List<NoClassifiedLinksDto> noClassList) {
		logger.trace(getClass());

		URL sourcePage = serchPage.getUrl();
		logger.trace("Converting to jsoap document.");
		Document jsoupReturn = jsoapUtil.toDocument(serchPage);
		
		logger.trace("Applyng regex pattern: "+RegexConstants.onionRegex+".");
		Pattern rxPatter = Pattern.compile(RegexConstants.onionRegex, Pattern.MULTILINE);
		
		logger.trace("Applyng regex matcher.");
		Matcher rxMatcher = rxPatter.matcher(jsoupReturn.text().replace(" ", ""));
				
		while (rxMatcher.find()) {
			NoClassifiedLinksDto linksDto = new NoClassifiedLinksDto();
			
			linksDto.setSourceLink(sourcePage.toString());
			linksDto.setOnionLink(rxMatcher.group());

			noClassList.add(linksDto);
		}

		return noClassList;
	}
}
