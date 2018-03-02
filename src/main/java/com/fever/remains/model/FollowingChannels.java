package com.fever.remains.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowingChannels {
	private String userId;
	private List<FollowingChannel> followingChannels;

	public FollowingChannels(String userId, List<FollowingChannel> followingChannels) {
		this.userId = userId;
		this.followingChannels = followingChannels;
	}
}