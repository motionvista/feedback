package com.feedback.common;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.feedback.vo.SettingVO;

public class EmailUtil {

public boolean sendEmail(SettingVO setting,String subject,String msg) throws Exception{
	boolean flag = true;
	String host =setting.getEmailHost();
	final String from =setting.getEmailFrom();
	String to = setting.getEmailTo();
	try {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
       	props.put("mail.smtp.port", "587");
	    props.put("mail.smtp.auth", "true");
	    
	 //gmail:
	    /* 	props.put("mail.smtp.starttls.enable", "true"); 
	        Authenticator authenticator = new Authenticator() {
	    	private PasswordAuthentication authentication;
	     
	    	{
	    		authentication = new PasswordAuthentication("eid", "pass");
	    	}
	     
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    		return authentication;
	    	}
	    };*/
	    
	    Authenticator authenticator = new Authenticator() {
	    	private PasswordAuthentication authentication;
	     
	    	{
	    		authentication = new PasswordAuthentication(from, "rushikeshbagul");
	    	}
	     
	    	protected PasswordAuthentication getPasswordAuthentication() {
	    		return authentication;
	    	}
	    };
	    
		javax.mail.Session session = javax.mail.Session.getInstance(props, authenticator);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from)); 
		StringTokenizer stk = new StringTokenizer(to,","); 
		InternetAddress[] toAddress = new InternetAddress[stk.countTokens()];
		int index = 0;
		while(stk.hasMoreTokens()){
			toAddress[index] = new InternetAddress(stk.nextToken().trim());
			index++;
		}
		
		message.setRecipients(Message.RecipientType.TO, toAddress);
		message.setSubject(subject);
		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setText(msg); 
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);
		message.setContent(msg, "text/html"); 
		//Transport.send(message);
		//code added from here..
		Transport transport = session.getTransport("smtp");
		transport.connect();
		transport.sendMessage(message, toAddress);
		transport.close();

	}catch (Exception ex) {
		flag = false;
		throw ex;
	} 

	return flag;
	}
}