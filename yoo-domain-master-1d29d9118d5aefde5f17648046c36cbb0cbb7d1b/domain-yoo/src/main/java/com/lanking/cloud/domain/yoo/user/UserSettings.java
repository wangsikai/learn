package com.lanking.cloud.domain.yoo.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用户设置
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_settings")
public class UserSettings implements Serializable {

	private static final long serialVersionUID = 2533835667332014498L;

	/**
	 * 同用户ID
	 */
	@Id
	private Long id;

	/**
	 * 此人作业是否产生短信
	 */
	@Column(name = "homework_sms", columnDefinition = "bit default 1")
	private boolean homeworkSms = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isHomeworkSms() {
		return homeworkSms;
	}

	public void setHomeworkSms(boolean homeworkSms) {
		this.homeworkSms = homeworkSms;
	}

}
