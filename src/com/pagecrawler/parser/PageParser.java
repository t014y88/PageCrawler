package com.pagecrawler.parser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.springframework.scheduling.annotation.Async;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import com.pagecrawler.storage.Storage;
import com.pagecrawler.urlprovider.UrlProvider;

public abstract class PageParser {
	private static final Log LOGGER = LogFactoryImpl.getLog(PageParser.class);
	protected static UrlProvider urlProvider;
	protected static Storage storage;

	protected PageParser(UrlProvider uriProvider, Storage storage) {
		PageParser.urlProvider = uriProvider;
		PageParser.storage = storage;
	}

	@Async
	public abstract void processUrls();

	protected Map<String, Integer> parseUrl(String url) {
		String page;
		Map<String, Integer> wordCounts = Maps.newHashMap();
		try {
			page = getPage(url);
			wordCounts = parsePage(page);
		} catch (IOException e) {
			LOGGER.warn("Problem parsing the page.", e);
		}

		return wordCounts;
	}

	private String getPage(String url) throws IOException {
		HttpClient client = new DefaultHttpClient();
		HttpUriRequest request = new HttpGet(url);
		HttpResponse response = client.execute(request);
		return CharStreams.toString(new InputStreamReader(response.getEntity().getContent(), Charsets.UTF_8));
	}

	private Map<String, Integer> parsePage(String pageContent) {
		String parsedPage = Jsoup.parse(pageContent).text();

		String[] words = parsedPage.split(" ");

		Map<String, Integer> wordCounts = Maps.newHashMap();

		for (String word : words) {
			int currentCount = 0;
			if (wordCounts.containsKey(word)) {
				currentCount = wordCounts.get(word);
			}
			wordCounts.put(word, ++currentCount);
		}
		return wordCounts;
	}
}
