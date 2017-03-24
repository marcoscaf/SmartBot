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

import org.apache.commons.io.IOUtils;

import com.sun.org.apache.xml.internal.security.utils.Base64;
 
public class CreateIssue {
    public static void main(String[] args) {
    	
    	String url = "https://jira.cpqd.com.br/rest/servicedeskapi/request";
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
            conn.setRequestProperty("Authorization", "Basic " + Base64.encode("marcosaf:;%25lindEN".getBytes(), 0));
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
  
    	
    	
    	JsonObject createIssueJira = Json.createObjectBuilder()
                .add("fields", Json.createObjectBuilder()
        		.add("serviceDeskId", "34")
        		.add("requestTypeId", "158")
        		.add("requestFieldValues",Json.createObjectBuilder()
        				.add("summary","Criando jira via REST - F5Team")
        				.add("description", "Criando jira via REST - F5Team"))
                ).build();
    	
    	
    	
    	
    	
        JsonObject createIssue = Json.createObjectBuilder()
                .add("fields",Json.createObjectBuilder()
        		.add("serviceDeskId", "34")
        		.add("requestTypeId", "158")
        		.add("project",Json.createObjectBuilder().add("key","HACK-4"))
                .add("summary", "Usuário não consegue executar operação XPTO")
                .add("description", "Usuário não consegue executar operação XPTO")
                .add("issuetype", Json.createObjectBuilder().add("id", "1"))
                ).build();
 
        return createIssueJira.toString();
 
    }
}