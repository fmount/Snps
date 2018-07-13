package org.osgi.demo.actuator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
//import java.net.URLConnection;


public class HttpRequestHandler {

	private  final String url;

	public HttpRequestHandler(String url)
	{
		this.url = url;
	}
	
	@SuppressWarnings("unused")
	public void execute(String command, String led, String on_of)
	{
		try {
			String completeCommand = url+ command + led + "=" +on_of;
			System.out.println("HttpRequestHandler Execute command: " + completeCommand);
			URL url =  new URL(completeCommand);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream inputStream = connection.getInputStream();
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
