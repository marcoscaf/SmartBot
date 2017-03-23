package br.com.f5team.smartbot.presentation;



import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

@SessionScoped
@ManagedBean(name="smartBotBean")
public class SmartBotBean implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String text;
	public String textBot;
	
	
	
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
			setTextBot(processMessage(getText()));
			RequestContext requestContext = RequestContext.getCurrentInstance();
			Thread.sleep(500);
			requestContext.execute("document.getElementsByClassName('fire_bot')[0].click()");

		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	
	/**
	 * Processa a mensagem na API do Watson e retorna a resposta para o usuário
	 *
	 * @param userMessage the user message
	 * @return the string
	 */
	private String processMessage(String userMessage){
		
		// TODO implementar serviço Rest com o Watson da IBM
		
		if(userMessage.toLowerCase().contains("olá") ||
			userMessage.toLowerCase().contains("oi")||
			userMessage.toLowerCase().contains("ola")||
			userMessage.toLowerCase().contains("bom dia")||
			userMessage.toLowerCase().contains("boa tarde")||
			userMessage.toLowerCase().contains("boa noite")
			
		){
				return "Olá! ";
		} else if(userMessage.toLowerCase().contains("tudo bem ?") ||
				userMessage.toLowerCase().contains("tudo bem?")){
				return "Melhor agora falando com você!";
			
		}		
		else{
			return "Não compreendi sua dúvida ";
		}
		
	}
	

}
