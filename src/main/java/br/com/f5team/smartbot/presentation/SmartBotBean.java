package br.com.f5team.smartbot.presentation;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;

import br.com.f5team.smartbot.rest.SendMessage;

@SessionScoped
@ManagedBean
public class SmartBotBean implements Serializable {

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
		} catch (IOException e1) {
			// TODO Auto-generated catch block
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

		// TODO implementar serviço Rest com o Watson da IBM
		SendMessage sendMessage = new SendMessage();
		String botMessage = SendMessage.sendMessageWatson(message);
		/*
		 * CrunchifyCallUrlAndGetResponse principal = new
		 * CrunchifyCallUrlAndGetResponse(); principal.callURL(myURL);
		 */
		/*
		 * Teste2 t2 = new Teste2(); t2.executa();
		 */
		/*
		 * Teste teste = new Teste(); teste.executa();
		 */
		return botMessage;

	}

}
