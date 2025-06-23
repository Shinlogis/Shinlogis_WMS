package com.shinlogis.wms.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import com.shinlogis.wms.common.Exception.EmailException;

import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.Message;

public class MailSender {
	
	String account_user="apdkfldbwls@gmail.com"; //구글의 이메일 계정 
	String app_pwd="desn zhje pjyl sjls"; //내가 받은 앱비밀번호
	Session session;
	
	
	public MailSender() {
		Properties props=new Properties();
		props.put("mail.smtp.auth" , "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com"); 
		props.put("mail.smtp", "587");
		
		session=Session.getInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(account_user, app_pwd);
			}
		});
	}
	
	public void send(String targetMail, String title, String content) throws EmailException{
		
		try {
			//메일의 내용 구성하기 
			Message message  = new MimeMessage(session);
			message.setFrom(new InternetAddress(account_user));
			message.setRecipients(Message.RecipientType.TO , InternetAddress.parse(targetMail));
			message.setSubject(title);
			message.setText(content); 
			
			//메일 전송 
			Transport.send(message);
		} catch (AddressException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		}
		
	}
	
	public void sendHtml(String targetMail, String title, String content) throws EmailException {
			
		try {
			//메일의 내용 구성하기 
			Message message  = new MimeMessage(session);
			message.setFrom(new InternetAddress(account_user, "wms")); //보내는 사람 메일 
			message.setRecipients(Message.RecipientType.TO , InternetAddress.parse(targetMail) ); //받을 사람 
			message.setSubject(title);//메일 제목 
			
			
			message.setContent(content, "text/html; charset=utf-8");
			Transport.send(message);
			
		} catch (AddressException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new EmailException("메일 발송 실패", e);
		}
	}

}
