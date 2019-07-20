package com.github.vskrahul.response;

import com.github.vskrahul.header.HttpHeader;

public class HttpResponse implements Response {

	private String body;
	
	private HttpHeader header;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public HttpHeader getHeader() {
		return header;
	}

	public void setHeader(HttpHeader header) {
		this.header = header;
	}
}