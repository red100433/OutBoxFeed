package com.fever.remains.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fever.remains.request.FollowingRequest;
import com.fever.remains.serivce.FollowService;

@RestController
@RequestMapping("/remains/tservice/follows")
public class FollowingController {

	@Autowired
	private FollowService followService;
	private static final int DEFAULT_PAGE_SIZE = 20;
	@PostMapping("/follow")
	public void follow(HttpServletResponse response, FollowingRequest followingRequest) {
		this.followService.follow(followingRequest);
		response.sendRedirect("/remains/tservice");
	}

	@PostMapping("/unfollow/{serviceName}/{channelGroup}/{channelId}/{userId}")
	public void unfollow(@ModelAttribute FollowingRequest followingRequest) {

		this.followService.unfollow(followingRequest);
		//return "redirect:/demo/tservice";
	}

	@GetMapping("/followList/{serviceName}/{channelGroup}/{userId}/{next}")
	public FollowingListResponse getFollowingList(@PathVariable String serviceName, @PathVariable String channelGroup,
		@PathVariable String userId, @PathVariable Long next) {

		return followService.followingList(serviceName, channelGroup, userId, new PageRequest(null,next,DEFAULT_PAGE_SIZE));
	}
}
