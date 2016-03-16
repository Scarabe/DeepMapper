package br.com.deepmapper.deeprobots;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;

import br.com.deepmapper.constans.DBConstants;
import br.com.deepmapper.constans.TextConstants;
import br.com.deepmapper.dto.NoClassifiedLinksDto;
import br.com.deepmapper.util.MongoUtil;
import br.com.deepmapper.util.RegexUtil;
import br.com.deepmapper.util.UnitUtil;

public class TorExtractCrawler {
	private static final Logger logger = LogManager.getLogger(TorExtractCrawler.class);
	private UnitUtil unitUtil = new UnitUtil();
	private MongoUtil dbUtil = new MongoUtil();
	private RegexUtil regexUtil = new RegexUtil();
	
	@Test
	public void torAcess(){
		final WebClient webClient = unitUtil.webConfigure(TextConstants.deepConfig);
		CompletionService<Boolean> executor = new ExecutorCompletionService<Boolean>(Executors.newFixedThreadPool(TextConstants.maxThreadPool));
		MongoCollection<Document> setedColl = dbUtil.getMongoColl(DBConstants.noClassColl);
		FindIterable<Document> cursor = setedColl.find();
		List<Future<Boolean>> threads = new ArrayList<>();
		
		for(Document onionLink : cursor){
			final String urlOnion = onionLink.getString(DBConstants.onionID);;

			Future<Boolean> thread = executor.submit((Callable<Boolean> & Serializable) () -> {
				List<NoClassifiedLinksDto> noClassList = new ArrayList<NoClassifiedLinksDto>();
				
				try {
					HtmlPage onionPage = webClient.getPage(urlOnion);
					noClassList = regexUtil.rxHtmlSurfApp(onionPage, noClassList);
					System.out.println(1);
				} catch (FailingHttpStatusCodeException e) {
					logger.error("Http Status " + e + ".");
				} catch (MalformedURLException e) {
					logger.error("Malformed Url " + e + ".");
				} catch (IOException e) {
					logger.error("IO " + e + ".");
				}
				
				dbUtil.insertNoClass(noClassList, DBConstants.noClassColl);
				return true;
			});
			
			threads.add(thread);
		}
		
		threads.stream().forEach(thread -> {
			try {
				thread.get();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
