package com.pagecrawler.parser;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import com.pagecrawler.storage.FileStorage;
import com.pagecrawler.storage.Storage;
import com.pagecrawler.test.common.TestConstants;
import com.pagecrawler.urlprovider.InMemoryProvider;
import com.pagecrawler.urlprovider.UrlProvider;

public class EndToEndMultiThreadedParserTest {
	private static final String FILE_SAVE_LOCATION = "saveFile.txt";

	private UrlProvider uriProvider;
	private Storage storage;

	private MultiThreadedPageParser multiThreadedPageParser;

	@Before
	public void init() throws FileNotFoundException {
		uriProvider = new InMemoryProvider();
		storage = new FileStorage(FILE_SAVE_LOCATION);
		multiThreadedPageParser = new MultiThreadedPageParser(uriProvider, storage);

		uriProvider.addUrls(TestConstants.URI_LIST);
	}

	@Test(expected = IllegalArgumentException.class)
	public void zeroTheads() {
		multiThreadedPageParser.setNumberOfThreads(0);
		multiThreadedPageParser.processUrls();
	}

	@Test
	public void singleThread() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(1);
		multiThreadedPageParser.processUrls();
	}

	@Test
	public void manyThreads() throws Exception {
		multiThreadedPageParser.setNumberOfThreads(5);
		multiThreadedPageParser.processUrls();
	}
}
