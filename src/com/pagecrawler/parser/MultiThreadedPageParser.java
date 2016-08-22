package com.pagecrawler.parser;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.LogFactoryImpl;

import com.google.api.client.util.Sets;
import com.pagecrawler.storage.Storage;
import com.pagecrawler.urlprovider.UrlProvider;

public class MultiThreadedPageParser extends PageParser implements Runnable {
	private static final Log LOGGER = LogFactoryImpl.getLog(MultiThreadedPageParser.class);
	private static final Object POISON_PILL = new Object();
	private volatile boolean isRunning = true;
	private int numberOfThreads = 0;

	public MultiThreadedPageParser(UrlProvider urlProvider, Storage storage) {
		super(urlProvider, storage);
	}

	private MultiThreadedPageParser() {
		super(urlProvider, storage);
	}

	@Override
	public void processUrls() {
		if (numberOfThreads < 1) {
			throw new IllegalArgumentException("You must provided a positive number of threads to process the URLs");
		}

		Collection<Thread> threads = Sets.newHashSet();
		for (int i = 0; i < numberOfThreads; i++) {
			Thread t = new Thread(new MultiThreadedPageParser());
			t.start();
			threads.add(t);
		}
		for (Thread t : threads) {
			try {
				t.join();
			} catch (InterruptedException e) {
				LOGGER.warn("Thread was interrupted", e);
			}
		}
	}

	@Override
	public void run() {
		while (isRunning) {
			Object item = getNextUrl();
			if (item == POISON_PILL) {
				continue;
			}
			String url = (String) item;

			Map<String, Integer> wordCounts = parseUrl(url);

			storage.save(url, wordCounts);
		}
	}

	private synchronized Object getNextUrl() {
		if (urlProvider.hasNextUrl()) {
			return urlProvider.nextUrl();
		} else {
			finish();
			return POISON_PILL;
		}
	}

	public void finish() {
		isRunning = false;
	}

	public void setNumberOfThreads(int numberOfThreads) {
		this.numberOfThreads = numberOfThreads;
	}
}