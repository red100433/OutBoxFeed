package com.fever.remains.serivce;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fever.remains.model.OutboxContent;
import com.fever.remains.request.DeleteContent;
import com.fever.remains.request.WriteContent;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Iterators;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FeedboxService {
	@Autowired
	private FeedboxClient feedboxClient;

	static final Long DEFAULT_PAGE_SIZE = 20L;

	public List<OutboxContent> getContentPage(String serviceName, List<FollowingChannel> followingChannels,
		PageRequest pageRequest) //
		throws FeverThriftException {
		List<OutboxFeed> outboxFeedList = new ArrayList<OutboxFeed>();
		//		GetContentByChannelResponse response;
		//		GetContentByChannelRequest request;

		//TODO ehcache, hashmap차이, cache 의 변천사 캐싱.
		//ehcache Bean 설정, 익스파이어 값 알기
		Stopwatch stopWatchContent = Stopwatch.createStarted();

		/* 1번
		 * 20개씩 짜르는 로직*/
//		for (FollowingChannel channel : followingChannels) {
//			GetContentByChannelRequest request = new GetContentByChannelRequest(serviceName,
//				channel.getChannel(), pageRequest);
//			GetContentByChannelResponse response = this.feedboxClient.getContentByChannel(request);
//			//TODO : 속도 측정, stream concat 할때마다 sorting 하는 속도와 전체를 다 가지고 와서 sorting하는 속도 차이
//			outboxFeedList = Stream.concat(outboxFeedList.stream(), response.getOutboxFeedPage().getContents().stream())
//				.parallel().sorted(Comparator.comparing(OutboxFeed::getFeedId).reversed())
//				.limit(DEFAULT_PAGE_SIZE)
//				.collect(Collectors.toList());
//		}

		/* 2번
		 * 20개씩 짜르는 로직*/
		//		for (FollowingChannel channel : followingChannels) {
		//			GetContentByChannelRequest request = new GetContentByChannelRequest(serviceName,
		//				channel.getChannel(), pageRequest);
		//			GetContentByChannelResponse response = this.feedboxClient.getContentByChannel(request);

		//			outboxFeedList = mergeContentList(outboxFeedList, outboxFeedList.size(),
		//				response.getOutboxFeedPage().getContents(), response.getOutboxFeedPage().getPageSize());
		//		}

		/*3번
		 * 20개씩 짜르는 로직*/
		for (FollowingChannel channel : followingChannels) {
			GetContentByChannelRequest request = new GetContentByChannelRequest(serviceName,
				channel.getChannel(), pageRequest);
			GetContentByChannelResponse response = this.feedboxClient.getContentByChannel(request);

			outboxFeedList = StreamSupport.stream(Spliterators.spliteratorUnknownSize(Iterators.mergeSorted(
				Arrays.asList(outboxFeedList.iterator(), response.getOutboxFeedPage().getContents().iterator()),
				(o1, o2) -> o2.getFeedId().compareTo(o1.getFeedId())), Spliterator.ORDERED), true)
				.limit(DEFAULT_PAGE_SIZE).collect(Collectors.toList());

		}

		/*
		 * 전부 다 더하는 로직*/
		//		for (FollowingChannel channel : followingChannels) {
		//			PageRequest page = PageRequest.fromFirst();
		//			do {
		//				request = new GetContentByChannelRequest(serviceName,
		//					channel.getChannel(), page);
		//				response = this.feedboxClient.getContentByChannel(request);
		//				//TODO : 속도 측정, stream concat 할때마다 sorting 하는 속도와 전체를 다 가지고 와서 sorting하는 속도 차이
		//				outboxFeedList.addAll(response.getOutboxFeedPage().getContents().stream().parallel()
		//					.collect(Collectors.toList()));
		//				page = response.getOutboxFeedPage().nextPageRequest(20);
		//
		//			} while (response.getOutboxFeedPage().hasNext());
		//		}

		stopWatchContent.stop();

		//				Stopwatch stopWatchSort = Stopwatch.createStarted();
		//						outboxFeedList.sort((o1, o2) -> o2.getFeedId().compareTo(o1.getFeedId()));
		//				stopWatchSort.stop();

		Stopwatch stopWatchMakeContent = Stopwatch.createStarted();
		List<OutboxContent> a = outboxFeedList.stream().parallel()
			.map(outboxFeed -> getContent(serviceName, outboxFeed))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
		stopWatchMakeContent.stop();

		log.info("Content : {}", stopWatchContent.toString());
		//				log.info("Sorting : {}", stopWatchSort.toString());
		log.info("make outboxContent : {}", stopWatchMakeContent.toString());
		log.info("total : {}", a.size());
		return a;
	}

	public List<OutboxContent> getChannelContentList(String serviceName, Channel channel,
		PageRequest pageRequest)
		throws FeverThriftException {
		Stopwatch stopWatch = Stopwatch.createStarted();

		List<OutboxContent> content = this.feedboxClient.getContentByChannel(new GetContentByChannelRequest(serviceName,
			channel, pageRequest)).getOutboxFeedPage().getContents().stream()
			.map(outboxFeed -> getContent(serviceName, outboxFeed))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
		stopWatch.stop();
		log.info("channelContents : {}", stopWatch.toString());

		return content;
	}

	private Optional<OutboxContent> getContent(String serviceName, OutboxFeed outboxFeed) {
		try {
			GetContentRequest getContentRequest = new GetContentRequest(serviceName, outboxFeed.getContentId());
			GetContentResponse content = feedboxClient.getContent(getContentRequest);
			return Optional.of(OutboxContent.of(content, outboxFeed.getFeedId()));
		} catch (FeverThriftException e) {
			return Optional.empty();
		}
	}

	private List<OutboxFeed> mergeContentList(List<OutboxFeed> o1, int o1Size, List<OutboxFeed> o2, int o2Size) {
		List<OutboxFeed> result = new ArrayList<OutboxFeed>();
		int o1_num = 0, o2_num = 0;

		while (!(o1_num == o1Size || o2_num == o2Size)) {
			if (o1.get(o1_num).getFeedId() > o2.get(o2_num).getFeedId()) {
				result.add(o1.get(o1_num));
				o1_num++;
			} else {
				result.add(o2.get(o2_num));
				o2_num++;
			}

		}

		if (o1_num == o1Size) {
			for (int i = o2_num; i < o2Size; i++) {
				result.add(o2.get(i));
			}
		} else if (o2_num == o2Size) {
			for (int i = o1_num; i < o1Size; i++) {
				result.add(o1.get(i));
			}
		}

		return result.stream().limit(20).collect(Collectors.toList());
	}

	//TODO return 값, uuid
	public void writeNewContent(WriteContent writeContent)
		throws FeverThriftException {
		String contentId = "content" + System.currentTimeMillis();
		log.info("contentId + " + contentId);
		WriteNewContentRequest writeNewContentRequest = new WriteNewContentRequest(writeContent.getServiceName(),
			Channel.of(writeContent.getChannelGroup(), writeContent.getChannelId()), contentId,
			writeContent.getContentBody(), LocalDateTime.now());
		feedboxClient.writeNewContent(writeNewContentRequest);
	}

	public void deleteContent(DeleteContent deleteContent) throws FeverThriftException {
		DeleteContentRequest request = new DeleteContentRequest(deleteContent.getServiceName(),
			deleteContent.getContentId());
		feedboxClient.deleteContent(request);
	}

}