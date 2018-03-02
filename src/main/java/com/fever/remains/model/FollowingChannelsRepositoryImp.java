package com.fever.remains.model;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
public class FollowingChannelsRepositoryImp implements FollowingChannelsRepository {

	@Override
	@Cacheable(value = "getChannels", key = "#userId")
	public FollowingChannels getChannels(String userId, List<FollowingChannel> getChannels) {
		return new FollowingChannels(userId, getChannels);
	}

	@Override
	@CacheEvict(value = "getChannels", key = "#userId")
	public void refresh(String userId) {

	}
}