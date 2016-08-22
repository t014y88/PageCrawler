package com.pagecrawler.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

public class FileStorage implements Storage {
	private static final Log LOGGER = LogFactoryImpl.getLog(FileStorage.class);
	private File file;

	public FileStorage(String fileLocation) {
		this.file = new File(fileLocation);
		file.delete();
	}

	@Override
	public boolean save(String url, Map<String, Integer> wordCounts) {
		try (FileWriter fw = new FileWriter(file, true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw)) {
			for (Entry<String, Integer> wordCount : wordCounts.entrySet()) {
				out.println(url + "," + wordCount.getKey() + "," + wordCount.getValue());
			}
			return true;
		} catch (IOException e) {
			LOGGER.warn("There was an error writing to SQL", e);
			return false;
		}
	}

}
