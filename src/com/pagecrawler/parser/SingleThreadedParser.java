package com.pagecrawler.parser;

import java.util.Map;

import com.pagecrawler.storage.Storage;
import com.pagecrawler.urlprovider.UrlProvider;

public class SingleThreadedParser extends PageParser {

	protected SingleThreadedParser(UrlProvider uriProvider, Storage storage) {
		super(uriProvider, storage);
	}

	@Override
	public void processUrls() {
		while (urlProvider.hasNextUrl()) {
			String url = urlProvider.nextUrl();
			Map<String, Integer> wordCounts = parseUrl(url);
			storage.save(url, wordCounts);
		}
	}
}
