package com.lanking.uxb.service.thirdparty.value;

import java.io.Serializable;
import java.util.Date;

import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.thirdparty.ShareType;

public class VShareLog implements Serializable {

	private static final long serialVersionUID = 8498000935727862221L;

	private long id;
	private ShareType type;
	private Biz biz;
	private long bizId;
	private String p0;
	private String content;
	private Date createAt;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

}
