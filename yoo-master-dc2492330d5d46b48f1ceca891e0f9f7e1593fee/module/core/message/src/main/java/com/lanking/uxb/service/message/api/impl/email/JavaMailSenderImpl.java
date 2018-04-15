package com.lanking.uxb.service.message.api.impl.email;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

/**
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version V1.0.0,2014年12月2日
 *
 */
@Component
@ConditionalOnExpression("!${email.spring}")
public class JavaMailSenderImpl implements MailSender {
	private Logger logger = LoggerFactory.getLogger(JavaMailSenderImpl.class);

	@Override
	public int sender(Mailer mailer, String target, String subject, String body, boolean simple) {
		List<String> to = Lists.newArrayList(target);
		return sender(mailer, to, subject, body, simple);
	}

	@Override
	public int sender(Mailer mailer, Collection<String> targets, String subject, String body, boolean simple) {
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", mailer.getSmtp());
		props.put("mail.smtp.port", mailer.getPort());
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.ssl.enable", true);
		// 创建Session实例对象
		Session session = Session.getDefaultInstance(props);
		// 创建MimeMessage实例对象
		MimeMessage message = new MimeMessage(session);
		try {
			// 设置发件人
			message.setFrom(new InternetAddress(mailer.getUsername()));
			// 设置邮件主题
			message.setSubject(subject);
			// 创建邮件的接收者地址，并设置到邮件消息中
			Address[] tos = null;
			Iterator<String> it = targets.iterator();
			while (it.hasNext()) {
				tos = new InternetAddress[targets.size()];
				for (int i = 0; i < targets.size(); i++) {
					tos[i] = new InternetAddress(it.next());
				}
			}
			// 设置收件人
			message.setRecipients(RecipientType.TO, tos);
			// 设置发送时间
			message.setSentDate(new Date());
			// 设置纯文本内容为邮件正文
			message.setText(body);
			// 保存并生成最终的邮件内容
			message.saveChanges();
			// 获得Transport实例对象
			Transport transport = session.getTransport();
			// 打开连接
			transport.connect(mailer.getUsername(), mailer.getPassword());
			// 将message对象传递给transport对象，将邮件发送出去
			transport.sendMessage(message, message.getAllRecipients());
			// 关闭连接
			transport.close();
			return 0;
		} catch (Exception e) {
			logger.error("send email error:", e);
			return -1;
		}
	}
}
