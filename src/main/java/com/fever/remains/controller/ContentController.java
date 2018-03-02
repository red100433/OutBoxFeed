package com.fever.remains.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fever.remains.model.FollowingChannels;
import com.fever.remains.model.FollowingChannelsRepository;
import com.fever.remains.model.OutboxContent;
import com.fever.remains.request.DeleteContent;
import com.fever.remains.request.WriteContent;
import com.fever.remains.serivce.FeedboxService;
import com.fever.remains.serivce.FollowService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/remains/myfeed/content")
public class ContentController {
	@Autowired
	private FollowService followService;
	@Autowired
	private FeedboxService feedboxService;
	@Autowired
	private FollowingChannelsRepository followingChannelsRepository;
	private static final int DEFAULT_PAGE_SIZE = 20;

	@PostMapping("/writeContent")
	public void addContent(HttpServletResponse response, @ModelAttribute WriteContent writeContent)
		throws FeverThriftException, IOException {

		feedboxService.writeNewContent(writeContent);
		response.sendRedirect("/remains/myfeed/channelContent/" + writeContent.getChannelId());
	}

	@PostMapping("/deleteContent/{serviceName}/{contentId}")
	public void removeContent(@ModelAttribute DeleteContent deleteContent) throws FeverThriftException {

		feedboxService.deleteContent(deleteContent);
	}

	@PostMapping("/contentList/{serviceName}/{channelGroup}/{userId}/{next}")
	public List<OutboxContent> getOutboxContentList(@PathVariable String serviceName, @PathVariable String channelGroup,
		@PathVariable String userId, @PathVariable Long next)
		throws FeverThriftException {
		return feedboxService.getContentPage(serviceName, new FollowingChannels(userId,
			followService.followingListAll(serviceName, channelGroup, userId)).getFollowingChannels(),
			new PageRequest(null, next, DEFAULT_PAGE_SIZE));
	}

	@PostMapping("/channelContent/{serviceName}/{channelGroup}/{channelId}/{next}")
	public List<OutboxContent> getChannelContentList(@PathVariable String serviceName,
		@PathVariable String channelGroup, @PathVariable String channelId, @PathVariable Long next)
		throws FeverThriftException {

		return feedboxService.getChannelContentList(serviceName, Channel.of(channelGroup, channelId),
			new PageRequest(null, next, DEFAULT_PAGE_SIZE));
	}

}