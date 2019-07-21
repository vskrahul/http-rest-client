package com.github.vskrahul.connection;

import java.io.IOException;

import com.github.vskrahul.response.HttpResponse;

public interface Connection {

	HttpResponse execute() throws IOException;
}