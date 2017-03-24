package br.com.f5team.smartbot.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmartBotRest {

	public void callWatsonSpeak() throws IOException {

		URL url = new URL("http://http://correiosapi.apphb.com/cep/13100231");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Accept", "application/json");

		if (conn.getResponseCode() != 200) {
			throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
		}

		BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

		String output;
		System.out.println("Output from Server .... \n");
		while ((output = br.readLine()) != null) {
			System.out.println(output);
		}

		conn.disconnect();

	}
	
	 public static void main(String[] args) {
		try {
			new SmartBotRest().callWatsonSpeak();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
