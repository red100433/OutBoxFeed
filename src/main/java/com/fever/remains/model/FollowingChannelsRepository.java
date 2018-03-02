package com.fever.remains.model;

import java.util.List;

public interface FollowingChannelsRepository {
	FollowingChannels getChannels(String userId, List<FollowingChannel> getChannels);
	void refresh(String name);
}
