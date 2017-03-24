package br.com.f5team.smartbot.rest;



import javax.json.Json;
import javax.json.JsonObject;
import javax.naming.AuthenticationException;

import org.json.JSONException;
import org.json.JSONObject;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

public class JTricksRESTClient {
	
	private static final String BASE_URL = "https://jira.cpqd.com.br";
	
	public void createJiraIssue(String summary, String description, String issueType){
		String auth = new String(Base64.encode("marcosaf:SENHA"));
		
		try {
//			//Get Projects
//			String projects = invokeGetMethod(auth, BASE_URL+"/rest/api/2/project");
//			System.out.println(projects);
//			JSONArray projectArray = new JSONArray(projects);
//			for (int i = 0; i < projectArray.length(); i++) {
//				JSONObject proj = projectArray.getJSONObject(i);
//				System.out.println("Key:"+proj.getString("key")+", Name:"+proj.getString("name"));
//			}
			
			//Create Issue
			String createIssueData = getJSonBody(summary, description, issueType);
			String issue = invokePostMethod(auth, BASE_URL+"/rest/api/2/issue", createIssueData);
			System.out.println(issue);
			JSONObject issueObj = new JSONObject(issue);
			String newKey = issueObj.getString("key");
			System.out.println("Key:"+newKey);
			
//			//Update Issue
//			String editIssueData = "{\"fields\":{\"assignee\":{\"name\":\"test\"}}}";
//			invokePutMethod(auth, BASE_URL+"/rest/api/2/issue/"+newKey, editIssueData);
			
			//invokeDeleteMethod(auth, BASE_URL+"/rest/api/2/issue/DEMO-13");
			
		} catch (AuthenticationException e) {
			System.out.println("Username or Password wrong!");
			e.printStackTrace();
		} catch (ClientHandlerException e) {
			System.out.println("Error invoking REST method");
			e.printStackTrace();
		} catch (JSONException e) {
			System.out.println("Invalid JSON output");
			e.printStackTrace();
		}

	}

	private static String invokeGetMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").get(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}
	
	private static String invokePostMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").post(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
		return response.getEntity(String.class);
	}
	
	private static void invokePutMethod(String auth, String url, String data) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}
	
	private static void invokeDeleteMethod(String auth, String url) throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").delete(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}
	
	
    private static String getJSonBody(String summary, String description, String issueType){
  
    	
     	
        JsonObject createIssueJira
        = Json.createObjectBuilder()
                .add("fields",Json.createObjectBuilder()
        		.add("project",Json.createObjectBuilder().add("key","HACK"))
                .add("summary", summary)
                .add("description", description)
                .add("requestTypeId", "158")
                .add("serviceDeskId", "34")
                .add("issuetype", Json.createObjectBuilder().add("name", issueType))
                ).build();
 
        return createIssueJira.toString();
 
    }
    
    public static void main(String[] args) {
		new JTricksRESTClient().createJiraIssue("F5", "F5", "Insidente");
	}

}
