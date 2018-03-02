package com.fever.remains.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fever.remains.serivce.FollowService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@EnableAutoConfiguration
@RequestMapping("/remains/tservice")
public class ChannelController {
	@Autowired
	private FollowService followService;

	@Value("${channel.group}")
	private String channelGroup;

	@GetMapping()
	public ModelAndView home(@CookieValue("userId") String userId, @CookieValue("serviceName") String serviceName)
		throws FeverThriftException {

		FollowingListResponse followingChannels = followService.followingList(serviceName,
			channelGroup, userId, PageRequest.fromFirst());

		ModelAndView mv = new ModelAndView("channelManage");

		mv.addObject("userId", userId);
		mv.addObject("serviceName", serviceName);
		mv.addObject("followingChannels", followingChannels.getChannels().getContents());
		mv.addObject("next", followingChannels.getChannels().getNext());
		return mv;
	}


	@GetMapping("/addChannel")
	public ModelAndView addChannel(@CookieValue("userId") String userId,
		@CookieValue("serviceName") String serviceName) {
		ModelAndView mv = new ModelAndView();

		mv.addObject("userId", userId);
		mv.addObject("serviceName", serviceName);
		mv.setViewName("addFollow");
		return mv;
	}

}
