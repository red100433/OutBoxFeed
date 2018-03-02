package com.fever.remains.serivce;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fever.remains.request.FollowingRequest;
import com.google.common.base.Stopwatch;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FollowService {
	@Autowired
	private RelationClient relationClient;

	public boolean follow(FollowingRequest followingRequest) {

		return this.relationClient
			.follow(new FollowRequest(followingRequest.getServiceName(), followingRequest.getUserId(),
				Channel.of(followingRequest.getChannelGroup(), followingRequest.getChannelId())))
			.getFollowChannel() != null;
	}

	public boolean unfollow(FollowingRequest followingRequest) {

		return (this.relationClient
			.unfollow(new UnfollowRequest(followingRequest.getServiceName(), followingRequest.getUserId(),
				Channel.of(followingRequest.getChannelGroup(), followingRequest.getChannelId())))
			.getUnfollowChannel() != null);
	}

	public FollowingListResponse followingList(String serviceName, String channelGroup, String userId,
		PageRequest pageRequest)
		throws FeverThriftException {

		return relationClient.followingList(new FollowingListRequest(serviceName, userId, Arrays.asList(channelGroup),
			pageRequest));
	}

	public List<FollowingChannel> followingListAll(String serviceName, String channelGroup, String userId)
		throws FeverThriftException {
		PageRequest pageRequest = PageRequest.fromFirst();

		FollowingListRequest request;
		FollowingListResponse response;
		List<FollowingChannel> followingList = new ArrayList<FollowingChannel>();

		Stopwatch stopWatch = Stopwatch.createStarted();
		do {
			request = new FollowingListRequest(serviceName, userId, Arrays.asList(channelGroup), pageRequest);
			response = relationClient.followingList(request);

			//			followingList.addAll(response.getChannels().getContents());

			followingList.addAll(response.getChannels().getContents().stream().parallel()
				.map(followingChannel -> getFollowingChannel(followingChannel))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList()));
			pageRequest = response.getChannels().nextPageRequest(20);
		} while (response.getChannels().hasNext());

		stopWatch.stop();
		log.info("followingList All : {}", stopWatch.toString());
		return followingList;
	}

	private Optional<FollowingChannel> getFollowingChannel(FollowingChannel followingChannel) {
		try {
			return Optional.of(followingChannel);
		} catch (Exception e) {

			return Optional.empty();
		}

	}
}
