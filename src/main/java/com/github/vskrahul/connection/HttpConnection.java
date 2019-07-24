package com.github.vskrahul.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;

public class HttpConnection {

	private HttpURLConnection connection;
	
	public HttpConnection() {
		
	}
	
	public HttpResponse execute(HttpRequest request) throws IOException {
		
		HttpResponse response = null;
		try {
			openConnection(request.url());
			
			request.getHeader().get()
								.entrySet()
								.stream()
								.forEach(e -> {
									this.connection.addRequestProperty(e.getKey(), e.getValue());
								});
			
			if(request.getMethod() != HttpMethod.GET) body(request.getBody(), request);
			
			this.connection.connect();
			
			StringBuilder sb = new StringBuilder();
			
			sb.append("Content-Encoding=").append(this.connection.getContentEncoding());
			sb.append("\nContent-Length=").append(this.connection.getContentLength());
			sb.append("\nContent-Type=").append(this.connection.getContentType());
			sb.append("\nContent=").append(this.connection.getContent());
			sb.append("\nResponseMessage=").append(this.connection.getResponseMessage());
			sb.append("\nResponseCode=").append(this.connection.getResponseCode());
			sb.append("\nHeader=").append(this.connection.getHeaderFields());
			sb.append("\nPermission=").append(this.connection.getPermission().getActions());
			
			
			String body = body(this.connection.getInputStream());
			sb.append("\nresponse=").append(body);
			
			if(request.traceFlag())
				System.out.println(sb.toString());
			
			response = new HttpResponse(body
							,this.connection.getHeaderFields()
							,this.connection.getResponseCode()
							,this.connection.getResponseMessage());
			return response;
		} catch(IOException e) {
			String errorBody = body(this.connection.getErrorStream());
			response = new HttpResponse(errorBody
					,this.connection.getHeaderFields()
					,this.connection.getResponseCode()
					,this.connection.getResponseMessage());
		} finally {
			this.connection.disconnect();
		}
		return response;
	}
	
	private void body(String body, HttpRequest request) throws IOException {
		
		this.connection.setDoOutput(true);
		OutputStream os = null;
		try {
			os = this.connection.getOutputStream();
			os.write(body.getBytes(Charset.forName("UTF-8")));
			os.flush();
		} catch(IOException e) {
			throw e;
		} finally {
			try {if(os!=null) os.close();} catch(IOException e) {}
		}
		
	}
	
	private String body(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder(); 
		try {
			byte[] data = new byte[1024];
			int i = -1;
			while((i = is.read(data)) > 0) {
				sb.append(new String(data, 0, i, Charset.forName("UTF-8")));
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		return sb.toString();
	}
	
	private HttpConnection openConnection(String url) throws IOException {
		try {
			URL _url = new URL(url);
			this.connection = (HttpURLConnection)_url.openConnection();
		} catch(IOException e) {
			throw e;
		}
		
		return this;
	}
}