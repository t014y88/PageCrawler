package com.pagecrawler.urlprovider;

import java.util.List;
import java.util.Queue;
import java.util.UUID;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.google.api.client.util.Lists;
import com.google.common.collect.Queues;

public class SqsUriProvider implements UrlProvider {
	private AmazonSQS sqs;
	private String queueUrl;
	private Queue<String> nextUrls = Queues.newConcurrentLinkedQueue();

	public SqsUriProvider(AmazonSQS sqs, String queueUrl) {
		this.sqs = sqs;
		this.queueUrl = queueUrl;
	}

	@Override
	public boolean hasNextUrl() {
		ReceiveMessageResult receiveMessageResult = sqs.receiveMessage(queueUrl);
		for (Message message : receiveMessageResult.getMessages()) {
			nextUrls.add(message.getBody());
		}

		if (nextUrls.isEmpty()) {
			return false;
		}
		return true;
	}

	@Override
	public String nextUrl() {
		return nextUrls.remove();
	}

	@Override
	public boolean addUrls(List<String> newUrls) {
		List<SendMessageBatchRequestEntry> entries = Lists.newArrayList();

		for (String url : newUrls) {
			SendMessageBatchRequestEntry request = new SendMessageBatchRequestEntry(UUID.randomUUID().toString(), url);
			entries.add(request);
		}

		sqs.sendMessageBatch(queueUrl, entries);
		return true;
	}
}
