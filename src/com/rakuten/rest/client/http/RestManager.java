package com.rakuten.rest.client.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

public class RestManager {
	
	public static String callGet(String url, String accept) {
	
		StringBuffer respTxt = new StringBuffer();
		try {
			if(accept == null || accept.isEmpty()) {
				accept="application/json";
			}
			HttpClient httpClient = HttpClients.createDefault();
			HttpGet reqGet = new HttpGet(url);
			reqGet.addHeader("accept", accept);
			HttpResponse response = httpClient.execute(reqGet);
			BufferedReader bReader = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));		
			String line;
			while ((line = bReader.readLine()) != null) {
				respTxt.append(line);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return respTxt.toString();
	}
}
