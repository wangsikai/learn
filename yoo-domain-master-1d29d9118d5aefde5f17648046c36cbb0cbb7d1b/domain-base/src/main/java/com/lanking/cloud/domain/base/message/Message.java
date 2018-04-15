package com.lanking.cloud.domain.base.message;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 消息表基类
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@MappedSuperclass
public abstract class Message implements Serializable {

	private static final long serialVersionUID = -9198813031790731007L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 消息接受者
	 */
	@Column(name = "target", length = 64)
	private String target;

	/**
	 * 模板代码
	 */
	@Column(name = "template_code")
	private Integer templateCode;

	/**
	 * 消息体
	 */
	@Column(name = "body", length = 4000)
	private String body;

	/**
	 * 发起时间(创建时间)
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 发送时间
	 */
	@Column(name = "send_at", columnDefinition = "datetime(3)")
	private Date sendAt;

	/**
	 * 发送结果
	 */
	@Column(name = "ret")
	private Integer ret;

	/**
	 * 保存时间
	 */
	@Column(name = "save_at", columnDefinition = "datetime(3)")
	private Date saveAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(Integer templateCode) {
		this.templateCode = templateCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getSendAt() {
		return sendAt;
	}

	public void setSendAt(Date sendAt) {
		this.sendAt = sendAt;
	}

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Date getSaveAt() {
		return saveAt;
	}

	public void setSaveAt(Date saveAt) {
		this.saveAt = saveAt;
	}

}
