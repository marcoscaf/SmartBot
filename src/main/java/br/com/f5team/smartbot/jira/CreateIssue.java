package br.com.f5team.smartbot.jira;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import javax.json.Json;
import javax.json.JsonObject;
import javax.security.sasl.AuthenticationException;

import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.org.apache.xml.internal.security.utils.Base64;
 
public class CreateIssue {
    public static void main(String[] args) {
    	
    	String url = "https://watson-api-explorer.mybluemix.net/conversation/api/v1/workspaces/7abcfb33-118e-4771-9272-8e6ea0d6e324/message?version=2017-02-03";
    	try {
    	
            URL jiraREST_URL = new URL(url);
            URLConnection urlConnection = jiraREST_URL.openConnection();
            urlConnection.setDoInput(true);
 
            HttpURLConnection conn = (HttpURLConnection) jiraREST_URL.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
 
            String encodedData = URLEncoder.encode(getJSonBody(),"UTF-8");
 
            System.out.println(getJSonBody() + "/" + encodedData);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Basic " + Base64.encode("22ed1ef0-0cb8-4b5b-bd52-ba57b86b90d6:hXB3xyAykVpx".getBytes(), 0));
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", String.valueOf(encodedData.length()));
            conn.getOutputStream().write(encodedData.getBytes());
 
            try {
                InputStream inputStream = conn.getInputStream();
                
                String theString = IOUtils.toString(inputStream, encodedData); 
                
                System.out.println(theString);
                
                
            }
            catch (IOException e){
                System.out.println(e.getMessage());
            }
 
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private static String getJSonBody(){
  
    	
    	
    	JsonObject createIssueJiraHelpDesk = Json.createObjectBuilder()
        		.add("text", "oi")
        		.add("requestTypeId", "158")
        		.add("requestFieldValues",Json.createObjectBuilder()
        				.add("summary","Criando jira via REST - F5Team")
        				.add("description", "Criando jira via REST - F5Team"))
                .build();
    	
   	
	
        JsonObject createIssueJira
        = Json.createObjectBuilder()
                .add("input",Json.createObjectBuilder()
        		.add("text","oi")).build();
 
        return createIssueJira.toString();
 
    }
}