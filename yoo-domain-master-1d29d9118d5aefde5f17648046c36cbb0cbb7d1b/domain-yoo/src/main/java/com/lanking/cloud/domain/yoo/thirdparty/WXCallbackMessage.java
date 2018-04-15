package com.lanking.cloud.domain.yoo.thirdparty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 微信回调消息存储
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "wx_callback_message")
public class WXCallbackMessage implements Serializable {
	private static final long serialVersionUID = -2194706945610898721L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 产品
	 */
	@Column(name = "product", precision = 3, nullable = false)
	private Product product = Product.YOOMATH;

	/**
	 * 微信回调消息类型
	 */
	@Column(name = "type", length = 100)
	private String messageType;

	/**
	 * 事件类型
	 */
	@Column(name = "event", length = 50)
	private String event;

	/**
	 * 自定义的KEY值，用于定位消息
	 */
	@Column(name = "key0", length = 100)
	private String key;

	/**
	 * 图文客服消息
	 */
	@Column(name = "media_id", length = 100)
	private String mediaId;

	/**
	 * 消息内容
	 */
	@Column(name = "message", length = 4000)
	private String message;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
