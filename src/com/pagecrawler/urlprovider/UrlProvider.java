package com.pagecrawler.urlprovider;

import java.util.List;

public interface UrlProvider {

	boolean hasNextUrl();

	String nextUrl();

	boolean addUrls(List<String> newUrls);
}
