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
    	
    	String url = "https://jira.cpqd.com.br/rest/api/2/issue/";
    	
    	//https://jira.cpqd.com.br/rest/api/2/issue/createmeta?projectKeys=HACK&issuetypeNames=Incidente&expand=projects.issuetypes.fields
    	//String url = "https://jira.cpqd.com.br/rest/servicedeskapi/request";
        try {
        	
        	
        	
        	
    		
    		
    		String auth = new String(Base64.encode("marcosaf:;%25lindEN".getBytes(), 0));
    		
    		Client client = Client.create();
    		WebResource webResource = client.resource("https://jira.cpqd.com.br/rest/api/2/issue/");
    		
    		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json").accept("application/json").get(ClientResponse.class);
    		
    		int statusCode = response.getStatus();
    		if (statusCode == 401) {
    		    throw new AuthenticationException("Invalid Username or Password");
    		}
    		
    		
    		
    		String response = response.getEntity(String.class);
    		
    		
        	
        	
        	
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
  
    	
    	
    	JsonObject createIssueJiraHelpDesk = Json.createObjectBuilder()
        		.add("serviceDeskId", "34")
        		.add("requestTypeId", "158")
        		.add("requestFieldValues",Json.createObjectBuilder()
        				.add("summary","Criando jira via REST - F5Team")
        				.add("description", "Criando jira via REST - F5Team"))
                .build();
    	
    	
//    	{
//    	    "fields": {
//    	       "project":
//    	       { 
//    	          "key": "HACK"
//    	       },
//    	       "summary": "REST ye merry gentlemen.",
//    	       "description": "Creating of an issue using project keys and issue type names using the REST API",
//    	       "issuetype": {
//    	          "name": "Bug"
//    	       }
//    	   }
//    	}

    	
    	
        JsonObject createIssueJira
        = Json.createObjectBuilder()
                .add("fields",Json.createObjectBuilder()
        		.add("project",Json.createObjectBuilder().add("key","HACK"))
                .add("summary", "TESTE JIRA")
                .add("description", "TESTE JIRA")
                .add("issuetype", Json.createObjectBuilder().add("name", "Incidente"))
                ).build();
 
        return createIssueJira.toString();
 
    }
}