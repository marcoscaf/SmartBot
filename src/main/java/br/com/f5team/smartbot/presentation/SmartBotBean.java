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

		JSONObject jsonResp = SendMessage.sendMessageWatson(message,conversationContext);

		conversationContext = ((JSONObject)jsonResp.get("context"));

		JSONArray messageArray = ((JSONArray)((JSONObject) jsonResp.get("output")).get("text"));
		
		ArrayList<String> list = new ArrayList<String>();     
		JSONArray jsonArray = (JSONArray)messageArray; 
		if (jsonArray != null) { 
		   int len = jsonArray.length();
		   for (int i=0;i<len;i++){ 
		    list.add(jsonArray.get(i).toString());
		   } 
		} 
		
		return  getMessages(list);

	}
	
	private String getMessages(List<String> messCollection){
		StringBuffer sb = new StringBuffer();
		for (String mess : messCollection) {
			sb.append(mess).append("\n");
		}	
		return sb.toString();
		
	}

}
