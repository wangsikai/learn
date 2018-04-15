package com.lanking.cloud.domain.yoo.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 账号密保设置
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "account_password_question")
public class AccountPasswordQuestion implements Serializable {

	private static final long serialVersionUID = -6244455695944912282L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联账号ID
	 */
	@Column(name = "account_id")
	private long accountId;

	/**
	 * 关联密保问题代码
	 */
	@Column(name = "password_question_code")
	private Integer passwordQuestionCode;

	/**
	 * 用户设置的密保答案
	 */
	@Column(name = "answer", length = 100)
	private String answer;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	@Transient
	private boolean initAnswer;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public Integer getPasswordQuestionCode() {
		return passwordQuestionCode;
	}

	public void setPasswordQuestionCode(Integer passwordQuestionCode) {
		this.passwordQuestionCode = passwordQuestionCode;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public boolean isInitAnswer() {
		return initAnswer;
	}

	public void setInitAnswer(boolean initAnswer) {
		this.initAnswer = initAnswer;
	}

}
