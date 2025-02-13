package com.flatmate.services;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;

	public void sendMail(String to, String subject, String body) {
		System.out.println("Sending mail to " + to);
		MimeMessage message = emailSender.createMimeMessage();
		try {
			message.setSubject(subject);
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("noreply@ezyrental.com");
			helper.setTo(to);
			helper.setText(body, true);
			emailSender.send(message);
		} catch (Exception ex) {
			System.out.println("Error " + ex.getMessage());
		}
	}
}
