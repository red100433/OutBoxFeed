package com.fever.remains.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FollowingChannelFactory {

	private FollowingChannel followingChannel;

	public FollowingChannelFactory(FollowingChannel getFollowingChannel) {
		this.followingChannel = getFollowingChannel;
	}
}
