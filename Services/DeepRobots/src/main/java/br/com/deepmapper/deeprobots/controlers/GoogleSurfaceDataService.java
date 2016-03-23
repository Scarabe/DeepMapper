package br.com.deepmapper.deeprobots.controlers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.deepmapper.deeprobots.crawlers.TorExtractCrawler;

@RestController
@RequestMapping("/deep")
public class GoogleSurfaceDataService {

	@RequestMapping(value = "/tor", method = { RequestMethod.POST })
	public void getGoogleLinks(){
		
		
		TorExtractCrawler torExtractor = new TorExtractCrawler();
		torExtractor.torAcess();
	}

}