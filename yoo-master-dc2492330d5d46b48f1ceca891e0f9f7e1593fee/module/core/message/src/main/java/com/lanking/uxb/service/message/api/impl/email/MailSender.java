package com.lanking.uxb.service.message.api.impl.email;

import java.util.Collection;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月1日
 *
 */
public interface MailSender {

	int sender(Mailer mailer, String target, String subject, String body, boolean simple);

	int sender(Mailer mailer, Collection<String> targets, String subject, String body, boolean simple);

}
