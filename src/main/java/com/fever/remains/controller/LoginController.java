package com.fever.remains.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fever.remains.request.UserRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/remains/login")
public class LoginController {

	@GetMapping()
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("message", "Welcome to UGC");
		mv.setViewName("login");

		return mv;
	}

	@PostMapping()
	public String loginProcessing(UserRequest userRequest, HttpServletResponse response) {

		Cookie userId = new Cookie("userId", userRequest.getUserId());
		Cookie serviceName = new Cookie("serviceName", userRequest.getServiceName());

		response.addCookie(userId);
		response.addCookie(serviceName);

		return "redirect:/remains/myfeed";

	}

}
