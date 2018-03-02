package com.fever.remains.request;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FollowingRequest {
	private String userId;
	private String serviceName;
	private String channelId;
	private String channelGroup;

}
