package com.github.vskrahul.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.github.vskrahul.exception.HttpException;
import com.github.vskrahul.method.HttpMethod;
import com.github.vskrahul.request.HttpRequest;
import com.github.vskrahul.response.HttpResponse;
import com.github.vskrahul.status.HttpStatus;

public class HttpConnection {

	private HttpURLConnection connection;
	
	public HttpConnection() {
		
	}
	
	public HttpResponse execute(HttpRequest request) {
		
		HttpResponse response = null;
		StringBuilder sb = new StringBuilder();
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
			
			sb.append("Content-Encoding=").append(this.connection.getContentEncoding());
			sb.append("\nContent-Length=").append(this.connection.getContentLength());
			sb.append("\nContent-Type=").append(this.connection.getContentType());
			//sb.append("\nContent=").append(this.connection.getContent());
			sb.append("\nResponseMessage=").append(this.connection.getResponseMessage());
			sb.append("\nResponseCode=").append(this.connection.getResponseCode());
			sb.append("\nHeader=").append(this.connection.getHeaderFields());
			sb.append("\nPermission=").append(this.connection.getPermission().getActions());
			
			
			String body = body(this.connection.getInputStream());
			sb.append("\nresponse=").append(body);
			
			response = new HttpResponse(body
							,this.connection.getHeaderFields()
							,this.connection.getResponseCode()
							,this.connection.getResponseMessage());
			return response;
		} catch(IOException e) {
			try {
				String errorBody = body(this.connection.getErrorStream());
				sb.append("\nresponse=").append(errorBody);
				response = new HttpResponse(errorBody
						,this.connection.getHeaderFields()
						,this.connection.getResponseCode()
						,this.connection.getResponseMessage());
			} catch(IOException ex) {
				sb.append("\nresponse=").append(e.getMessage());
				response = new HttpResponse(e.getMessage(), null, HttpStatus.INTERNAL_SERVER_ERROR.statusCode()
																	,HttpStatus.INTERNAL_SERVER_ERROR.status());
			}
		} finally {
			this.connection.disconnect();
			if(request.traceFlag())
				System.out.println(sb.toString());
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
	
	private String body(InputStream is) {
		StringBuilder sb = new StringBuilder(); 
		try {
			byte[] data = new byte[1024];
			int i = -1;
			while((i = is.read(data)) > 0) {
				sb.append(new String(data, 0, i, Charset.forName("UTF-8")));
			}
		} catch(IOException e) {
			System.err.println(e.getMessage());
			throw new HttpException(e.getMessage());
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