package com.lanking.uxb.zycon.operation.value;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.lanking.cloud.sdk.bean.Status;

/**
 * versionLog vo
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月9日 下午4:03:34
 */
public class CVersionLog implements Serializable {

	private static final long serialVersionUID = 3464884046240675091L;

	private long id;
	private Date publishDate;
	private String version;
	private List<String> desc;
	private Status status;
	private String statusName;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getPublishDate() {
		return publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getDesc() {
		return desc;
	}

	public void setDesc(List<String> desc) {
		this.desc = desc;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
