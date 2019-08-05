package com.github.vskrahul.exception;

public class HttpException extends RuntimeException {

	private static final long serialVersionUID = 1948500227046977066L;

	public HttpException(String message) {
		super(message);
	}
}