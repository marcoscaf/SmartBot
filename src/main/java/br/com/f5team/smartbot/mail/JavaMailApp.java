package br.com.f5team.smartbot.mail;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaMailApp {

	public void senEmail(String userName, String email, String issueNumber, String issueDescription) {

		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("f5team2017@gmail.com", "melhortime");
			}
		});

		/** Ativa Debug para sessão */
		session.setDebug(true);

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("f5team2017@gmail.com")); // Remetente

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(email);

			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Help Desk Hackathon chamado: "+issueDescription);// Assunto
			message.setText("Sr(a) "+
						userName
					+ ". Segue o número do seu chamado:  "+issueNumber
					+ "\nAgradecemos o contato, ele é muito importante para nós"
					+ "\n\nAtenciosamente."
					+ "\n\nHelp Desk Hackathon'");

			/** Método para enviar a mensagem criada */
			Transport.send(message);


		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		new JavaMailApp().senEmail("Marcos Cristiano", "marcos.cristiano.af@gmail.com", "JIRA-XXXX", "Instalar o Git");
	}
}
