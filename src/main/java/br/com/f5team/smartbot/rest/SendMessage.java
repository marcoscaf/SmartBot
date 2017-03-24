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

public class SendMessage {

	private static final String BASE_URL = "https://watson-api-explorer.mybluemix.net/conversation/api/v1/workspaces/7abcfb33-118e-4771-9272-8e6ea0d6e324/message?version=2017-02-03";

	public static JSONObject sendMessageWatson(String message, JSONObject conversationContext) {
		String auth = new String(Base64.encode("22ed1ef0-0cb8-4b5b-bd52-ba57b86b90d6:hXB3xyAykVpx"));
		String returnMessage = null;
		JSONObject issueObj = null;
		try {
			// //Get Projects
			// String projects = invokeGetMethod(auth,
			// BASE_URL+"/rest/api/2/project");
			// System.out.println(projects);
			// JSONArray projectArray = new JSONArray(projects);
			// for (int i = 0; i < projectArray.length(); i++) {
			// JSONObject proj = projectArray.getJSONObject(i);
			// System.out.println("Key:"+proj.getString("key")+",
			// Name:"+proj.getString("name"));
			// }

			// Create Issue
			String createIssueData = getJSonBody(message, conversationContext);
			String issue = invokePostMethod(auth, BASE_URL, createIssueData);
			System.out.println(issue);
			issueObj = new JSONObject(issue);

			// System.out.println(newKey);

			// //Update Issue
			// String editIssueData =
			// "{\"fields\":{\"assignee\":{\"name\":\"test\"}}}";
			// invokePutMethod(auth, BASE_URL+"/rest/api/2/issue/"+newKey,
			// editIssueData);

			// invokeDeleteMethod(auth, BASE_URL+"/rest/api/2/issue/DEMO-13");

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

		return issueObj;

	}

	private static String invokeGetMethod(String auth, String url)
			throws AuthenticationException, ClientHandlerException {
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

	private static String invokePostMethod(String auth, String url, String data)
			throws AuthenticationException, ClientHandlerException {
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

	private static void invokePutMethod(String auth, String url, String data)
			throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").put(ClientResponse.class, data);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}

	private static void invokeDeleteMethod(String auth, String url)
			throws AuthenticationException, ClientHandlerException {
		Client client = Client.create();
		WebResource webResource = client.resource(url);
		ClientResponse response = webResource.header("Authorization", "Basic " + auth).type("application/json")
				.accept("application/json").delete(ClientResponse.class);
		int statusCode = response.getStatus();
		if (statusCode == 401) {
			throw new AuthenticationException("Invalid Username or Password");
		}
	}

	private static String getJSonBody(String message, JSONObject conversationContext) {
		JSONObject text = null;
		String json;

		if (conversationContext == null) {
			json = Json.createObjectBuilder().add("input", Json.createObjectBuilder().add("text", message)).build().toString();

		} else {
			json = Json.createObjectBuilder().add("input", Json.createObjectBuilder().add("text", message)).build().toString();
		}
		
		return json;

	}

}
