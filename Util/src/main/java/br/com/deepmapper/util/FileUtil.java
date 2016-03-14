package br.com.deepmapper.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class FileUtil {
	private static final Logger logger = LogManager.getLogger(FileUtil.class);

	public void getHtmlPage(String html, String fileName, String Path) {
		logger.trace("getHtmlPage()");

		File file = new File(Path + "/" + fileName + ".html");
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter(file));
			output.write(html);
			output.close();
		} catch (IOException e) {
			logger.error("IO " + e);
		}
		logger.trace("Generating file on: " + Path + "/" + fileName + ".html.");
	}
}
