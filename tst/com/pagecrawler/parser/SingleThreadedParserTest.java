package com.pagecrawler.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagecrawler.storage.Storage;
import com.pagecrawler.test.common.TestConstants;
import com.pagecrawler.urlprovider.UrlProvider;

public class SingleThreadedParserTest {

	@Mock
	private UrlProvider mockedUriProvider;
	@Mock
	private Storage mockedStorage;

	private PageParser pageParser;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		pageParser = new SingleThreadedParser(mockedUriProvider, mockedStorage);
	}

	@Test
	public void singleUrl() throws Exception {
		int numberOfUrls = 1;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		pageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}

	@Test
	public void manyUrls() throws Exception {
		int numberOfUrls = 3;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		pageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}
}
