package com.fever.remains.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@EnableAutoConfiguration
@RequestMapping("/remains")
public class RequestController {

	@Value("${channel.group}")
	String channelGroup;

	@GetMapping()
	public String welcome() {
		return "redirect:/remains/login";
	}

	@GetMapping("/logout")
	public String logout(HttpServletResponse response){
		Cookie userId = new Cookie("userId", null);
		Cookie serviceName = new Cookie("serviceName", null);
		response.addCookie(userId);
		response.addCookie(serviceName);

		return "redirect:/remains/login";
	}
}