package com.lanking.uxb.service.message.api.impl.email;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月1日
 *
 */
public class Mailer {
	private String username;
	private String password;
	private String smtp;
	private int port;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtp() {
		return smtp;
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
