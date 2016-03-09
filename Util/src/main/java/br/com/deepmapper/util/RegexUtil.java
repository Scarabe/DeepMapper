package br.com.deepmapper.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.gargoylesoftware.htmlunit.html.HtmlPage;

import br.com.deepmapper.constans.RegexConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;

public class RegexUtil {
	private static final Logger logger = LogManager.getLogger(MongoUtil.class);

	public List<NoClassifiedLinksDto> rxHtmlSurfApp(HtmlPage serchPage) {
		logger.trace(getClass());

		List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();
		URL sourcePage = serchPage.getUrl();

		Pattern rxPatter = Pattern.compile(RegexConstants.onionRegex);
		Matcher rxMatcher = rxPatter.matcher(serchPage.asXml());
		while (rxMatcher.find()) {
			NoClassifiedLinksDto linksDto = new NoClassifiedLinksDto();

			linksDto.setSourceLink(sourcePage.toString());
			linksDto.setOnionLink(rxMatcher.group());

			noClassList.add(linksDto);
		}

		return noClassList;
	}
}
