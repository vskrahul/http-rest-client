package com.github.vskrahul.request;

import com.github.vskrahul.header.HttpHeader;
import com.github.vskrahul.method.Method;

public class HttpRequest implements Request {

	private String body;
	
	private HttpHeader header;
	
	private Method method;
	
	public HttpRequest(Method method) {
		this.method = method;
	}

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

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
}