package com.fever.remains.exception;

public class ChannelException extends RuntimeException {

	public ChannelException(String message) {
		super(message);
	}

	public ChannelException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ChannelException(Throwable throwable) {
		super(throwable);
	}
}
