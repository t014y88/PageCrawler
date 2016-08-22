package com.pagecrawler.test.common;

import java.util.List;

import org.junit.Assert;
import org.mockito.Mockito;

import com.google.common.collect.Lists;
import com.pagecrawler.storage.Storage;
import com.pagecrawler.urlprovider.UrlProvider;

public class TestConstants {
	public static final List<String> URI_LIST = Lists.newArrayList("http://www.yahoo.com", "http://www.google.com",
			"http://www.msn.com", "http://www.github.com");

	public static void expectNumberOfUris(int number, UrlProvider mockedUriProvider) {

		Assert.assertTrue("The number of URI you want to test exceeds the number of URIs avalible to test",
				number <= TestConstants.URI_LIST.size());

		Boolean intialValue = false;
		List<Boolean> nextValues = Lists.newArrayList(false);

		String intialUri = "";
		List<String> nextUris = Lists.newArrayList();

		for (int i = 0; i < number; i++) {
			String currentUri = TestConstants.URI_LIST.get(i);
			if (i == 0) {
				intialValue = true;
				intialUri = currentUri;
			} else {
				nextValues.add(0, true);
				nextUris.add(currentUri);
			}
		}

		if (number > 1) {
			nextValues.set(number - 1, false);
		}

		Mockito.when(mockedUriProvider.hasNextUrl()).thenReturn(intialValue, nextValues.toArray(new Boolean[0]));
		Mockito.when(mockedUriProvider.nextUrl()).thenReturn(intialUri, nextUris.toArray(new String[0]));
	}

	public static void verifyAll(int numberOfUrls, Storage mockedStorage) {
		for (int i = 0; i < numberOfUrls; i++) {
			Mockito.verify(mockedStorage).save(Mockito.matches(TestConstants.URI_LIST.get(i)),
					Mockito.anyMapOf(String.class, Integer.class));
		}
	}
}
