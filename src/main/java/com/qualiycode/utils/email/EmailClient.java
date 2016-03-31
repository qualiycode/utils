package com.qualiycode.utils.email;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * This class is an Email client class.
 * 
 * @author Eli Rozenfeld
 *
 */
public class EmailClient{
	protected String accountName = null; 
	protected String accountPassword = null; 
	protected EmailAccountType accountType = EmailAccountType.GMAIL;
	protected String from = null; 
	protected String to = null; 
	protected String cc = null;
	protected String subject = null; 
	protected String body = null; 
	protected String charset = null; 
	protected ContentType contentType = ContentType.TXT; 
	protected String attachment = null;
	protected Properties serverConnectionProperties;
	
	/**
	 * @param accountName - the email account name
	 * @param accountPassword - the email account password
	 * @param accountType - the email account type
	 */
	public EmailClient(String accountName, String accountPassword, EmailAccountType accountType){
		this.accountName = accountName;
		this.accountPassword = accountPassword;
		this.accountType = accountType;
		serverConnectionProperties = new Properties();
	}
	
	/**
	 * Send email to configured recipients
	 * @throws Exception
	 */
	public void sendEmail() throws Exception{
		sendEmail(from, to, subject, body);
	}
	
	/**
	 * Send email using specific parameters
	 * @param from - email from
	 * @param to - email to
	 * @param subject - email subject
	 * @param body - email body
	 * @throws Exception
	 */
	public void sendEmail(String from, String to, String subject, String body) throws Exception{
		
		if(accountName == null || accountPassword == null || accountType == null){
			throw new Exception("Email credentials are missing, You must set: account name, account password and account type");
		}
		
		if(from == null || to == null || subject == null || body == null){
			throw new Exception("Email parameters are missing, You must set: from, to, subject and body");
		}
		
		switch (accountType) {
		case GMAIL:
			serverConnectionProperties.put("mail.smtp.auth", "true");
			serverConnectionProperties.put("mail.smtp.starttls.enable", "true");
			serverConnectionProperties.put("mail.smtp.host", "smtp.gmail.com");
			serverConnectionProperties.put("mail.smtp.port", "587");
			
			break;

		default:
			break;
		}
		
		Session session = Session.getInstance(serverConnectionProperties, new javax.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(accountName, accountPassword);
			}
		  });

		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		if(cc != null){
			message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		}
		message.setSubject(subject);
		
		Multipart mp = new MimeMultipart();
		MimeBodyPart messagePart = new MimeBodyPart();
		messagePart.setContent(body, contentType.getContentType());
        mp.addBodyPart(messagePart);
        
        if(attachment != null){
        	MimeBodyPart attachmentPart = new MimeBodyPart();
        	File attachmentFile = new File(attachment);
        	DataSource source = new FileDataSource(attachmentFile.getAbsolutePath());
        	attachmentPart.setDataHandler(new DataHandler(source));
        	attachmentPart.setFileName(attachmentFile.getName());
            mp.addBodyPart(attachmentPart);
        }
        
        message.setContent(mp);
        
        Transport.send(message);
	}

	/**
	 * @return the email body charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @param charset - the email body charset or null to use the operating system default 
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the email body content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * @param contentType - the email body content type
	 */
	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the email attachment
	 */
	public String getAttachment() {
		return attachment;
	}

	/**
	 * @param attachment - the email attachment file name and location
	 */
	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	/**
	 * @return the email account name
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName - the email account name
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the email account password
	 */
	public String getAccountPassword() {
		return accountPassword;
	}

	/**
	 * @param accountPassword - the email account password
	 */
	public void setAccountPassword(String accountPassword) {
		this.accountPassword = accountPassword;
	}

	/**
	 * @return the email account type
	 */
	public EmailAccountType getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType - the email account type
	 */
	public void setAccountType(EmailAccountType accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the email from
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * @param from - the email from 
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * @return the email to
	 */
	public String getTo() {
		return to;
	}

	/**
	 * @param to - the email to 
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * @return the email CC
	 */
	public String getCc() {
		return cc;
	}

	/**
	 * @param cc - the email to 
	 */
	public void setCc(String cc) {
		this.cc = cc;
	}

	/**
	 * @return the email subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject - the email subject 
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the email body
	 */
	public String getBody() {
		return body;
	}

	/**
	 * @param body - the email body 
	 */
	public void setBody(String body) {
		this.body = body;
	}
}
