package com.fever.remains.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteContent {
	private String serviceName;
	private String channelGroup;
	private String channelId;
	private String contentBody;

}
