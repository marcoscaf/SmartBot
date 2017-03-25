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
	private StringBuffer conversasionLog = new StringBuffer();
	private JSONObject conversationContext;
	private String summary;
	private String description;

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
	 * Processa a mensagem na API do Watson e retorna a resposta para o usuário
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

			this.userName = conversationContext.get("username").toString();

			this.userEmail = conversationContext.get("useremail").toString();

		} catch (Exception e) {

		}

		if (checkErrorTaskIntent(jsonResp.getJSONArray("intents"))) {
			summary = createSummary(jsonResp, getTool(jsonResp));
			description = createDescription(jsonResp, getTool(jsonResp));
		}

		JTricksRESTClient jiraCreate = new JTricksRESTClient();
		String jiraURL = "";
		if (conversationContext.toString().contains("jiraerro")) {
			jiraURL = jiraCreate.createJiraIssue(summary, description, "Incidente");
		} else if (conversationContext.toString().contains("jiratarefa")) {
			jiraURL = jiraCreate.createJiraIssue(summary, description, "Solicitação de serviço");
		} else if (conversationContext.toString().contains("tarefafechada")) {
			jiraURL = jiraCreate.createJiraIssue(summary, description, "Solicitação de serviço");
			String[] parts = jiraURL.split("/");
			jiraURL = jiraCreate.closeJiraIssue(parts[4]);
		}
		JSONArray messageArray = ((JSONArray) ((JSONObject) jsonResp.get("output")).get("text"));

		ArrayList<String> list = new ArrayList<String>();
		JSONArray jsonArray = (JSONArray) messageArray;
		if (jsonArray != null) {
			int len = jsonArray.length();
			for (int i = 0; i < len; i++) {
				list.add(jsonArray.get(i).toString());
				conversasionLog.append(jsonArray.get(i).toString());
			}
		}
		if (!jiraURL.isEmpty()) {
			list.add("Este é o número do seu chamado: <a href=\"" + jiraURL + "\" target=\"_blank\">" + jiraURL + "</a>");
			conversasionLog.append(
					"O número do seu chamado: <a href=\"" + jiraURL + "\" target=\"_blank\">" + jiraURL + "</a>");
			list.add("Estou enviando um email com essas informações...");

			String keyjira = jiraURL.split("https://jira.cpqd.com.br/browse/")[1];
			JavaMailApp mailApp = new JavaMailApp();
			mailApp.senEmail(getUserName(), getUserEmail(), keyjira, conversasionLog.toString());

		}
		jiraURL = "";

		return getMessages(list);

	}

	private String getTool(JSONObject jsonResp) {
		String ferramenta = null;
		JSONArray entities = jsonResp.getJSONArray("entities");
		for (int i = 0; i < entities.length(); i++) {
			JSONObject entity = entities.getJSONObject(i);
			ferramenta = entity.get("value").toString();
		}
		return ferramenta;
	}

	private boolean checkErrorTaskIntent(JSONArray intent) {
		for (int i = 0; i < intent.length(); i++) {
			JSONObject obj = intent.getJSONObject(i);
			if (obj.get("intent").toString().trim().equals("tarefa")
					|| obj.get("intent").toString().trim().equals("erro"))
				return true;
		}
		return false;
	}

	private String createDescription(JSONObject jsonResp, String ferramenta) {
		StringBuilder sb = new StringBuilder();

		return sb.append("Dados do solicitante:\n").append("Nome: ")
				.append(conversationContext.get("username").toString()).append("\n").append("Email: ")
				.append(conversationContext.get("useremail").toString()).append("\n")
				.append(conversationContext.get("username").toString())
				.append(" solicitou a instalação da ferramenta: ").append(ferramenta).toString();
	}

	private String createSummary(JSONObject jsonResp, String ferramenta) {
		StringBuilder sb = new StringBuilder();
		String summ = sb.append("[").append(ferramenta).append("]").append(" Realizar instalação")
				.toString();

		return summ;
	}

	private String getMessages(List<String> messCollection) {
		StringBuffer sb = new StringBuffer();
		for (String mess : messCollection) {
			sb.append(mess).append("\n");
		}
		return sb.toString();

	}

}
