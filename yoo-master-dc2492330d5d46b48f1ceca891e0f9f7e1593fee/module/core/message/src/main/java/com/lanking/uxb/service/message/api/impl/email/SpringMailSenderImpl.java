package com.lanking.uxb.service.message.api.impl.email;

import java.util.Collection;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.StringUtils;

@Component
@ConditionalOnExpression("${email.spring}")
public class SpringMailSenderImpl implements MailSender {

	private Logger logger = LoggerFactory.getLogger(SpringMailSenderImpl.class);

	@Override
	public int sender(Mailer mailer, String target, String subject, String body, boolean simple) {
		return sender(mailer, Lists.newArrayList(target), subject, body, simple);
	}

	@Override
	public int sender(Mailer mailer, Collection<String> targets, String subject, String body, boolean simple) {
		JavaMailSenderImpl javaMailSenderImpl = new JavaMailSenderImpl();
		javaMailSenderImpl.setHost(mailer.getSmtp());
		javaMailSenderImpl.setUsername(mailer.getUsername());
		javaMailSenderImpl.setPassword(mailer.getPassword());
		javaMailSenderImpl.setDefaultEncoding("UTF-8");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.auth", true);
		javaMailProperties.put("mail.smtp.starttls.enable", true);

		javaMailProperties.put("mail.smtp.port", mailer.getPort());
		javaMailProperties.put("mail.transport.protocol", "smtp");
		javaMailProperties.put("mail.smtp.host", mailer.getSmtp());
		javaMailProperties.put("mail.smtp.ssl.enable", true);

		javaMailSenderImpl.setJavaMailProperties(javaMailProperties);

		String[] tos = new String[targets.size()];
		try {
			if (simple) {
				SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
				simpleMailMessage.setFrom(javaMailSenderImpl.getUsername());
				simpleMailMessage.setTo(targets.toArray(tos));
				simpleMailMessage.setSubject(subject == null ? StringUtils.EMPTY : subject);
				simpleMailMessage.setText(body == null ? StringUtils.EMPTY : body);
				javaMailSenderImpl.send(simpleMailMessage);
			} else {

				MimeMessage mimeMessage = javaMailSenderImpl.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, Charsets.UTF8);
				helper.setFrom(javaMailSenderImpl.getUsername());
				helper.setTo(targets.toArray(tos));
				helper.setSubject(subject == null ? StringUtils.EMPTY : subject);
				helper.setText(body, true);
				javaMailSenderImpl.send(mimeMessage);
			}
			return 0;
		} catch (Exception e) {
			logger.error("send email error:", e);
			return -1;
		}
	}
}
