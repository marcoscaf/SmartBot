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

	public void senEmail(String userName, String email, String issueNumber, String conversasionLog) {

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

			Message messageToUser = new MimeMessage(session);
			messageToUser.setFrom(new InternetAddress("f5team2017@gmail.com")); // Remetente

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(email);

			messageToUser.setRecipients(Message.RecipientType.TO, toUser);
			messageToUser.setSubject("Help Desk Hackathon chamado: " + issueNumber);// Assunto
			messageToUser.setText("Sr(a) " + userName + ". Segue o número do seu chamado:  " + issueNumber
					+ " e você pode acompanhar ele na página: https://jira.cpqd.com.br/browse/" + issueNumber
					+ "\nAgradecemos o contato, ele é muito importante para nós" + "\n\nAtenciosamente."
					+ "\n\nHelp Desk Hackathon'");

			/** Método para enviar a mensagem criada */
			sendEmail(messageToUser);
		
		
			Message messageToHelpDesk = new MimeMessage(session);
			messageToHelpDesk.setFrom(new InternetAddress("f5team2017@gmail.com")); // Remetente

			Address[] toHD = InternetAddress // Destinatário(s)
					.parse("f5team2017@gmail.com");

			messageToHelpDesk.setRecipients(Message.RecipientType.TO, toHD);
			messageToHelpDesk.setSubject("Help Desk Hackathon chamado: " + issueNumber);// Assunto
			messageToHelpDesk.setText("Usuário: " + userName + ".\nSolicitou o chamado: " + issueNumber
					+ "\nLink: https://jira.cpqd.com.br/browse/" + issueNumber);

			/** Método para enviar a mensagem criada */

			sendEmail(messageToHelpDesk);
	

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		new JavaMailApp().senEmail("Marcos Cristiano", "marcos.cristiano.af@gmail.com", "JIRA-XXXX", "Instalar o Git");
	}
	
	
	private void sendEmail(Message message) {
		new Thread() {
			@Override
			public void run() {
				try {
					Transport.send(message);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
}
