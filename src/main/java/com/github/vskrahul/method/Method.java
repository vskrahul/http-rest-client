package com.github.vskrahul.method;

public enum Method {

	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	PATCH("PATCH"),
	DELETE("DELETE"),
	COPY("COPY"),
	HEAD("HEAD"),
	OPTIONS("OPTIONS"),
	LINK("LINK"),
	UNLINK("UNLINK"),
	PURGE("PURGE"),
	LOCK("LOCK"),
	UNLOCK("UNLOCK"),
	PROPFIND("PROPFIND"),
	VIEW("VIEW");
	
	private String value;
	
	private Method(String value) {
		this.value = value;
	}
	
	public String value() {
		return this.value;
	}
}