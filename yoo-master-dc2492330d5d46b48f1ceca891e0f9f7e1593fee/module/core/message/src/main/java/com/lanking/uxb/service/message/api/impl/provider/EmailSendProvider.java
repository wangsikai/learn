package com.lanking.uxb.service.message.api.impl.provider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.message.EmailTemplate;
import com.lanking.cloud.domain.base.message.api.EmailPacket;
import com.lanking.cloud.domain.base.message.api.MessagePacket;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.message.api.impl.email.MailSender;
import com.lanking.uxb.service.message.api.impl.email.Mailer;
import com.lanking.uxb.service.message.ex.MessageException;
import com.lanking.uxb.service.message.type.Constants;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Component
public class EmailSendProvider extends AbstractSendProvider implements InitializingBean {

	private Logger logger = LoggerFactory.getLogger(EmailSendProvider.class);

	@Autowired
	private MailSender mailSender;
	@Autowired
	private FreeMarkerConfigurationFactory freeMarkerConfigurationFactory;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	private Configuration configuration;

	private Mailer mailer;

	@Override
	public boolean accept(MessageType messageType) {
		return messageType == getType();
	}

	@Override
	public MessageType getType() {
		return MessageType.EMAIL;
	}

	@Override
	String getTitleTemplate(int code) throws Exception {
		EmailTemplate emailTemplate = messageTemplateCacheService.getEmailTemplate(code);
		if (emailTemplate == null) {
			emailTemplate = messageTemplateService.getEmailTemplate(code);
			if (emailTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putEmailTemplate(emailTemplate);
		}
		return emailTemplate.getTitle();
	}

	@Override
	String getBodyTemplate(int code) throws Exception {
		EmailTemplate emailTemplate = messageTemplateCacheService.getEmailTemplate(code);
		if (emailTemplate == null) {
			emailTemplate = messageTemplateService.getEmailTemplate(code);
			if (emailTemplate == null) {
				throw new Exception();
			}
			messageTemplateCacheService.putEmailTemplate(emailTemplate);
		}
		return emailTemplate.getBody();
	}

	@Override
	boolean isMock(int code) throws Exception {
		throw new Exception();
	}

	@Override
	public void send(MessagePacket packet) throws MessageException {
		try {
			EmailPacket emailPacket = (EmailPacket) packet;
			logger.info("receive mq packet:{}", emailPacket.toString());
			if (emailPacket.getMessageTemplateCode() != null && emailPacket.getMessageTemplateBodyParams() != null) {
				emailPacket.setTitle(
						getTitle(emailPacket.getMessageTemplateCode(), emailPacket.getMessageTemplateTitleParams()));
				emailPacket.setBody(
						getBody(emailPacket.getMessageTemplateCode(), emailPacket.getMessageTemplateBodyParams()));
			} else {
				Map<String, Object> params = emailPacket.getMessageTemplateBodyParams();
				if (params != null && (params.get(Constants.EMAIL_FTL_KEY) != null
						&& Boolean.valueOf(params.get(Constants.EMAIL_FTL_KEY).toString()))) {
					Template template = null;
					if (params.get(Constants.EMAIL_FTL_NAME_KEY).toString().contains(".ftl")) {
						template = configuration.getTemplate(params.get(Constants.EMAIL_FTL_NAME_KEY).toString(),
								Charsets.UTF8);
					} else {
						template = configuration.getTemplate(params.get(Constants.EMAIL_FTL_NAME_KEY) + ".ftl",
								Charsets.UTF8);
					}
					Map<String, String> ps = Maps.newHashMap();
					for (String key : params.keySet()) {
						if (Constants.EMAIL_FTL_KEY.equals(key) || Constants.EMAIL_FTL_NAME_KEY.equals(key)) {
							continue;
						}
						ps.put(key, String.valueOf(params.get(key)));
					}
					emailPacket.setBody(FreeMarkerTemplateUtils.processTemplateIntoString(template, ps));
				}
			}
			if (CollectionUtils.isNotEmpty(emailPacket.getTargets())) {
				emailPacket.setRet(mailSender.sender(mailer, emailPacket.getTargets(), emailPacket.getTitle(),
						emailPacket.getBody(), false));
			} else {
				emailPacket.setRet(mailSender.sender(mailer, emailPacket.getTarget(), emailPacket.getTitle(),
						emailPacket.getBody(), false));
			}
			afterSend(emailPacket);
		} catch (Exception e) {
			logger.error("send email fail:", e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mailer = new Mailer();
		mailer.setUsername(Env.getString("mail.sender"));
		mailer.setPassword(Env.getString("mail.pwd"));
		mailer.setSmtp(Env.getString("mail.smtp"));
		mailer.setPort(StringUtils.isBlank(Env.getString("mail.port")) ? 0 : Env.getInt("mail.port"));
		// init ftl Configuration
		configuration = freeMarkerConfigurationFactory.createConfiguration();
		final File baseFileDir = new File(Env.getString("email.template.destPath"));
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		configuration.setTemplateLoader(new FileTemplateLoader(baseFileDir));
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
					Resource[] resources = resolver
							.getResources("classpath*:" + Env.getString("email.template.srcPath") + "/*.ftl");
					if (resources != null) {
						for (Resource resource : resources) {
							String fileName = resource.getFilename();
							File ftlFile = new File(baseFileDir, fileName);
							if (ftlFile.exists()) {
								ftlFile.delete();
							}
							ftlFile.createNewFile();
							FileWriter fw = new FileWriter(ftlFile);
							BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
							String s = "";
							while ((s = br.readLine()) != null) {
								fw.write(s + "\n");
							}
							br.close();
							fw.close();
						}
					}
				} catch (Exception e) {
					logger.error("load email template fail:", e);
				}
			}
		});
	}
}
