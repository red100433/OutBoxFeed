package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Stopwatch;

import com.naver.fever.boot.client.exception.ClientInitializeException;
import com.naver.fever.boot.client.exception.NameServerException;
import com.naver.fever.boot.config.envronment.Profile;
import com.naver.fever.boot.model.exception.FeverThriftException;
import com.naver.fever.boot.model.field.Channel;
import com.naver.fever.boot.model.field.PageRequest;
import com.naver.fever.relation.RelationClient;
import com.naver.fever.relation.model.request.follow.FollowRequest;
import com.naver.fever.relation.model.request.follow.FollowingListRequest;
import com.naver.fever.relation.model.request.follow.UnfollowRequest;
import com.naver.fever.relation.model.response.FollowingChannel;
import com.naver.fever.relation.model.response.follow.FollowResponse;
import com.naver.fever.relation.model.response.follow.FollowingListResponse;
import com.naver.fever.relation.model.response.follow.UnfollowResponse;

public class FollowserviceTest {
	static RelationClient relationClient;
	static final String USER_ID = "user1";
	static final String GROUP = "InternGroup";
	static final String SERVICE_NAME = "Intern";
	static final String SITE_ID = "testChannel";
	static final int DEFAULT_PAGE = 20;
	static final Channel CHANNEL = Channel.of(GROUP, SITE_ID);

	@BeforeClass
	public static void setUpBeforeClass() throws NameServerException, ClientInitializeException {
		relationClient = new RelationClient(Profile.DEV);
	}

	@Ignore
	@Test
	public void unFollowTest() throws FeverThriftException {

		String siteId = SITE_ID;

		UnfollowRequest request = new UnfollowRequest(SERVICE_NAME, USER_ID, Channel.of(GROUP, siteId));
		UnfollowResponse response = relationClient.unfollow(request);

	}

	@Ignore
	@Test
	public void followTest() throws FeverThriftException {

		String siteId = SITE_ID;

		FollowRequest request = new FollowRequest(SERVICE_NAME, USER_ID, Channel.of(GROUP, siteId));
		FollowResponse response = relationClient.follow(request);
		Assert.assertEquals(response.getFollowChannel(), Channel.of(GROUP, siteId));

	}

	@Test
	public void followingList() throws FeverThriftException {
		PageRequest pageRequest = PageRequest.fromFirst();

		FollowingListRequest request;
		FollowingListResponse response;

		List<FollowingChannel> followingList = new ArrayList<FollowingChannel>();
		Stopwatch stopwatch = Stopwatch.createStarted();
		//		do {
		//			request = new FollowingListRequest(SERVICE_NAME, USER_ID, Arrays.asList(GROUP), pageRequest);
		//			response = relationClient.followingList(request);
		//
		//			response.getChannels().getContents().stream()
		//				.forEach(getFollowingChannel -> followingList.add(getFollowingChannel));
		//			pageRequest = response.getChannels().nextPageRequest(DEFAULT_PAGE);
		//		} while (response.getChannels().hasNext());

		do {
			request = new FollowingListRequest(SERVICE_NAME, USER_ID, Arrays.asList(GROUP), pageRequest);
			response = relationClient.followingList(request);

			followingList.addAll(response.getChannels().getContents());
			pageRequest = response.getChannels().nextPageRequest(DEFAULT_PAGE);
		} while (response.getChannels().hasNext());
	}
}
