package com.pagecrawler.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pagecrawler.storage.Storage;
import com.pagecrawler.test.common.TestConstants;
import com.pagecrawler.urlprovider.UrlProvider;

public class MultiThreadedParserTest {

	@Mock
	private UrlProvider mockedUriProvider;
	@Mock
	private Storage mockedStorage;

	private MultiThreadedPageParser multiThreadedPageParser;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		multiThreadedPageParser = new MultiThreadedPageParser(mockedUriProvider, mockedStorage);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroTheads() {
		multiThreadedPageParser.setNumberOfThreads(0);
		multiThreadedPageParser.processUrls();
	}

	@Test
	public void singleUrlSingleThread() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(1);
		int numberOfUrls = 1;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		multiThreadedPageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}

	@Test
	public void manyUrlsSingleThread() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(1);
		int numberOfUrls = 3;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		multiThreadedPageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}

	@Test
	public void moreUrlsThenThreads() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(2);
		int numberOfUrls = 3;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		multiThreadedPageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}

	@Test
	public void moreTheadsThenUrls() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(5);
		int numberOfUrls = 3;

		TestConstants.expectNumberOfUris(numberOfUrls, mockedUriProvider);

		multiThreadedPageParser.processUrls();

		TestConstants.verifyAll(numberOfUrls, mockedStorage);
	}
}
