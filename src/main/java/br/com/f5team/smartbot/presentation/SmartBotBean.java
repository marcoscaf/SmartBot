package br.com.f5team.smartbot.presentation;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.json.JSONArray;
import org.json.JSONObject;
import org.primefaces.context.RequestContext;

import br.com.f5team.smartbot.mail.JavaMailApp;
import br.com.f5team.smartbot.rest.JTricksRESTClient;
import br.com.f5team.smartbot.rest.SendMessage;

@ViewScoped
@ManagedBean
public class SmartBotBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String text;
	public String textBot;
	public String userName;
	public String userEmail;
	private JSONObject conversationContext;

	@PostConstruct
	private void initConversation() {
		text = "oi";
		try {
			setTextBot(processMessage(getText()));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.execute("document.getElementsByClassName('fire_bot')[0].click()");
		text = null;

	}

	public String getTextBot() {
		return textBot;
	}

	public void setTextBot(String textBot) {
		this.textBot = textBot;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void sendMessage(ActionEvent e) {

		try {
			String processMess = processMessage(getText());
			setTextBot(processMess);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		RequestContext requestContext = RequestContext.getCurrentInstance();
		requestContext.execute("document.getElementsByClassName('fire_bot')[0].click()");

	}

	/**
	 * Processa a mensagem na API do Watson e retorna a resposta para o usu�rio
	 *
	 * @param userMessage
	 *            the user message
	 * @return the string
	 * @throws IOException
	 */
	private String processMessage(String message) throws IOException {

		JSONObject jsonResp = SendMessage.sendMessageWatson(message, conversationContext);

		try {
			conversationContext = ((JSONObject) jsonResp.get("context"));
		} catch (Exception e) {

		}

		JTricksRESTClient jiraCreate = new JTricksRESTClient();
		String jiraURL = "";
		if (conversationContext.toString().contains("jiraerro")) {
			jiraURL = jiraCreate.createJiraIssue("PROBLEMA", "PROBLEMA", "Problema");
		} else if (conversationContext.toString().contains("jiratarefa")) {
			jiraURL = jiraCreate.createJiraIssue("Solicita��o de servi�o", "Solicita��o de servi�o",
					"Solicita��o de servi�o");
		}

		JSONArray messageArray = ((JSONArray) ((JSONObject) jsonResp.get("output")).get("text"));

		ArrayList<String> list = new ArrayList<String>();
		JSONArray jsonArray = (JSONArray) messageArray;
		if (jsonArray != null) {
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.get(i).toString());
			}
		}
		if (!jiraURL.isEmpty()) {
			list.add("O n�mero do seu chamado: <a href=\""+jiraURL+"\" target=\"_blank\">"+jiraURL+"</a>");	
			list.add("Enviei um email para voc� com as inforam��es do seu chamado.");
			JavaMailApp mailApp = new JavaMailApp();
			
			//mailApp.senEmail(userName, email, issueNumber, issueDescription);
			
			
		}
		jiraURL = "";

		return getMessages(list);

	}

	private String getMessages(List<String> messCollection) {
		StringBuffer sb = new StringBuffer();
		for (String mess : messCollection) {
			sb.append(mess).append("\n");
		}
		return sb.toString();

	}

}
