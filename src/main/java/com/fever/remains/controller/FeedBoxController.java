package com.fever.remains.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fever.remains.model.FollowingChannels;
import com.fever.remains.model.FollowingChannelsRepository;
import com.fever.remains.model.OutboxContent;
import com.fever.remains.serivce.FeedboxService;
import com.fever.remains.serivce.FollowService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@EnableCaching
@Controller
@RequestMapping("/remains/myfeed")
public class FeedBoxController {
	@Autowired
	private FeedboxService feedboxService;
	@Autowired
	private FollowService followService;
	@Autowired
	private FollowingChannelsRepository followingChannelsRepository;

	@Value("${channel.group}")
	private String channelGroup;
	private static final int DEFAULT_PAGE_SIZE = 20;

	@GetMapping()
	public ModelAndView feed(@CookieValue("userId") String userId, @CookieValue("serviceName") String serviceName)
		throws FeverThriftException {

		List<OutboxContent> contentList = feedboxService.getContentPage(serviceName,
			new FollowingChannels(userId, followService.followingListAll(serviceName, channelGroup, userId))
				.getFollowingChannels(),
			new PageRequest(null, Long.MAX_VALUE, DEFAULT_PAGE_SIZE));

		ModelAndView mv = new ModelAndView("feed");
		mv.addObject("serviceName", serviceName);
		mv.addObject("userId", userId);
		mv.addObject("channelGroup", channelGroup);
		mv.addObject("getContentResponse", contentList);
		mv.addObject("next", contentList.size() == 0 ? 0 : contentList.get(contentList.size() - 1).getFeedId());

		return mv;
	}

	@GetMapping("/channelContent/{channelId}")
	public ModelAndView channelContent(@CookieValue("serviceName") String serviceName, @PathVariable String channelId)
		throws FeverThriftException {
		ModelAndView mv = new ModelAndView("channelContent");
		List<OutboxContent> contentList = feedboxService.getChannelContentList(serviceName,
			Channel.of(channelGroup, channelId), PageRequest.fromFirst());
		mv.addObject("channelId", channelId);
		mv.addObject("serviceName", serviceName);
		mv.addObject("channelGroup", channelGroup);
		mv.addObject("channelContentList", contentList);
		mv.addObject("next", contentList.size() == 0 ? 0 : contentList.get(contentList.size() - 1).getFeedId());

		return mv;
	}

	@GetMapping("/refresh")
	public String hometest(@CookieValue("userId") String userId) {
		followingChannelsRepository.refresh(userId);
		return "redirect:/remains";
	}
}