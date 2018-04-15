package com.lanking.cloud.domain.yoo.thirdparty;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.lanking.cloud.domain.type.Biz;

/**
 * 第三方分享记录
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "share_log")
public class ShareLog implements Serializable {

	private static final long serialVersionUID = 3359491105359835430L;

	@Id
	private Long id;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 分享类型
	 */
	@Column(name = "type", precision = 3)
	private ShareType type;

	/**
	 * 关联业务对象类型
	 */
	@Column(name = "biz", precision = 3)
	private Biz biz;

	/**
	 * 关联业务对象ID
	 */
	@Column(name = "biz_id")
	private long bizId;

	/**
	 * 标题
	 */
	@Column(length = 512)
	private String title;

	/**
	 * 内容
	 */
	@Column(length = 512)
	private String body;

	/**
	 * url
	 */
	@Lob
	@org.hibernate.annotations.Type(type = "text")
	@Column
	private String url;

	/**
	 * 额外记录页面里面需要的内容,如：分享报告的报告数据
	 */
	@Column(length = 4000)
	private String content;

	/**
	 * 附加字段
	 */
	@Column(length = 128)
	private String p0;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public ShareType getType() {
		return type;
	}

	public void setType(ShareType type) {
		this.type = type;
	}

	public Biz getBiz() {
		return biz;
	}

	public void setBiz(Biz biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
