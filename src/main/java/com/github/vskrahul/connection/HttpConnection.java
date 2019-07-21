package com.github.vskrahul.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import com.github.vskrahul.method.Method;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;

public class HttpConnection implements Connection {

	private HttpURLConnection connection;
	
	private HttpRequest request;
	
	public HttpConnection() {
		
	}
	
	public HttpConnection get(String url) throws IOException {
		this.request = new HttpRequest(Method.GET);
		openConnection(url);
		return this;
	}
	
	public HttpConnection post(String url) throws IOException {
		this.request = new HttpRequest(Method.POST);
		openConnection(url);
		return this;
	}
	
	public HttpConnection header(String key, String value) {
		this.connection.addRequestProperty(key, value);
		return this;
	}
	
	public HttpConnection header(Map<String, String> headers) {
		headers.entrySet().stream().forEach(e -> this.connection.addRequestProperty(e.getKey(), e.getValue()));
		return this;
	}
	
	public HttpConnection body(String body) throws IOException {
		this.request.setBody(body);
		
		this.connection.setDoOutput(true);
		OutputStream os = null;
		try {
			os = this.connection.getOutputStream();
			os.write(body.getBytes());
			os.flush();
		} catch(IOException e) {
			throw e;
		} finally {
			try {if(os!=null) os.close();} catch(IOException e) {}
		}
		
		return this;
	}
	
	@Override
	public HttpResponse execute() throws IOException {
		
		try {
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
			sb.append("\nbody=").append(body);
			
			System.out.println(sb.toString());
			
			return new HttpResponse(body
							,this.connection.getHeaderFields()
							,this.connection.getResponseCode()
							,this.connection.getResponseMessage());
			
		} catch(IOException e) {
			throw e;
		} finally {
			this.connection.disconnect();
		}
	}
	
	private String body(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder(); 
		try {
			byte[] data = new byte[1024];
			is.read(data);
			sb.append(new String(data));
		} catch(IOException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		return sb.toString();
	}
	
	private Connection openConnection(String url) throws IOException {
		try {
			URL _url = new URL(url);
			this.connection = (HttpURLConnection)_url.openConnection();
		} catch(IOException e) {
			throw e;
		}
		
		return this;
	}
}