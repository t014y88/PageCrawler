package com.pagecrawler.storage;

import java.util.Map;

public interface Storage {

	// Map<word, count>
	boolean save(String url, Map<String, Integer> wordCounts);
}
