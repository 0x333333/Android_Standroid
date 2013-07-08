package com.jesusjzp.stan;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Log;

public class GetData {
	
	public String getConnect(String value) {
		String res = "";
		String httpUrl = "http://www.reseau-stan.com/monitoring/index.asp?rub_code=67&line="+value; 
		Log.v("httpUrl:", httpUrl);
		URL url = null;
		
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e) {
			Log.e("GetData.java", "MalformedURLException");
		}
		
		if(url != null) {
			try {
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "utf-8"));
				String inputLine = null;
				while (((inputLine = reader.readLine()) != null)) {
					res += inputLine + "\n";
				}
				reader.close();
				urlConn.disconnect();
			} catch (IOException e)	{
				return "";
			}
		}
		
		return res;
	}
	
	public int needUpdate() {
		String res = "";
		String httpUrl = "http://jesusjzp.webege.com/projects/standroid/stan_version.html";
		Log.v("httpUrl:", httpUrl);
		URL url = null;
		
		try {
			url = new URL(httpUrl);
		} catch (MalformedURLException e) {
			Log.e("GetData.java", "MalformedURLException");
		}
		
		if(url != null) {
			try {
				HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
				urlConn.connect();
				BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "8859-1"));
				String inputLine = null;
				while (((inputLine = reader.readLine()) != null)) {
					res += inputLine + "\n";
				}
				reader.close();
				urlConn.disconnect();
			} catch (IOException e)	{
				return 0;
			}
		}
		
		int online_version;
		int index = res.indexOf("jesusjzp");
		online_version = Integer.parseInt(res.substring(0, index));
		
		Log.v("res[0]", online_version+"");
		return online_version;
	}

}
