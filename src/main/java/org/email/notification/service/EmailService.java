package org.email.notification.service;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.email.notification.model.EmailTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class  EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${email.address}")
	private String attchEmailAddr;
	

	public void sendTextEmail(EmailTemplate emailTemplate) {

		SimpleMailMessage msg = new SimpleMailMessage();
		try {
			if (emailTemplate.getSendTo().contains(",")) {
				String[] emails = emailTemplate.getSendTo().split(",");
				int receipantSize = emails.length;
				for (int i = 0; i < receipantSize; i++) {

					msg.setTo(emails[i]);
					msg.setSubject(emailTemplate.getSubject());
					msg.setText(emailTemplate.getBody());
					javaMailSender.send(msg);
				}

			} else {
				msg.setTo(emailTemplate.getSendTo());
				msg.setSubject(emailTemplate.getSubject());
				msg.setText(emailTemplate.getBody());
				javaMailSender.send(msg);
			}

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void sendEmailWithAttachment(MultipartFile multipartFile) throws MessagingException, IOException {

		MimeMessage msg = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);

		try {
			if (attchEmailAddr.contains(",")) {
				String[] emails = attchEmailAddr.split(",");
				int receipantSize = emails.length;				
				for (int i = 0; i < receipantSize; i++) {
					helper.setTo(emails[i]);
					helper.setSubject("Attachment File !");
					helper.setText("<h1>" + "Find the Attachment file" + "</h1>", true);
					InputStreamSource attachment = new ByteArrayResource(multipartFile.getBytes());

					helper.addAttachment(multipartFile.getOriginalFilename(), attachment);
					javaMailSender.send(msg);
				}

			} else {
				helper.setTo(attchEmailAddr);
				helper.setSubject("Attachment File !");
				// default = text/plain
				// true = text/html
				helper.setText("<h1>" + "Find the Attachment file" + "</h1>", true);
				InputStreamSource attachment = new ByteArrayResource(multipartFile.getBytes());

				helper.addAttachment(multipartFile.getOriginalFilename(), attachment);
				javaMailSender.send(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
