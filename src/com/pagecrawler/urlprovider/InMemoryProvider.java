package com.pagecrawler.urlprovider;

import java.util.List;
import java.util.Queue;

import com.google.common.collect.Queues;

public class InMemoryProvider implements UrlProvider {
	private Queue<String> nextUrls = Queues.newConcurrentLinkedQueue();

	@Override
	public boolean hasNextUrl() {
		return nextUrls.isEmpty() ? false : true;
	}

	@Override
	public String nextUrl() {
		return nextUrls.remove();
	}

	@Override
	public boolean addUrls(List<String> newUrls) {
		nextUrls.addAll(newUrls);
		return true;
	}

}
