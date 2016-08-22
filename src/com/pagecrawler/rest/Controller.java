package com.pagecrawler.rest;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pagecrawler.parser.PageParser;
import com.pagecrawler.urlprovider.UrlProvider;

@RestController
public class Controller {
	private UrlProvider urlProvider;
	private PageParser parser;

	public Controller(UrlProvider urlProvider, PageParser parser) {
		this.urlProvider = urlProvider;
		this.parser = parser;
	}

	@RequestMapping(value = "/addUrls", method = RequestMethod.POST)
	public boolean addUrls(@RequestBody List<String> urls) {
		urlProvider.addUrls(urls);

		return true;
	}

	@RequestMapping(value = "/processUrls")
	public boolean processUrls() {
		parser.processUrls();

		return true;
	}
}
