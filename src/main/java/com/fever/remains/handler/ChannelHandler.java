package com.fever.remains.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fever.remains.exception.ChannelException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ChannelHandler {

	@ExceptionHandler(ChannelException.class)
	public void channelException(ChannelException channelError) {
		log.error(channelError.getMessage());
	}

	@ExceptionHandler(value = Exception.class)
	public void handleException(Exception error) {
		log.error(error.getMessage());
	}
}