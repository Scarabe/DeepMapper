package br.com.deepmapper.surfacerobots.controlers;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.deepmapper.constans.GoogleConstants;
import br.com.deepmapper.surfacerobots.crawlers.GoogleExtractCrawler;

@RestController
@RequestMapping("/surface")
public class GoogleSurfaceDataService {

	@RequestMapping(value = "/google", method = { RequestMethod.POST })
	public void getGoogleLinks(@RequestParam( value = "searchstring" ) String searchString){
		
		GoogleConstants googleConstants = new GoogleConstants();
		
		if(searchString!= null && StringUtils.isNotBlank(searchString)){
			googleConstants.setSerchContent(searchString);
		}	
		
		GoogleExtractCrawler extractor = new GoogleExtractCrawler();
		extractor.googleRuningPages(googleConstants);
	}

}