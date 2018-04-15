package com.lanking.uxb.channelSales.channel.value;

import java.io.Serializable;
import java.util.Date;

/**
 * 渠道资料VO.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月3日
 */
public class VChannelFile implements Serializable {
	private static final long serialVersionUID = 7674671533572803662L;

	private Long id;
	private String name;
	private long coverId;
	private long fileId;
	private String uri;
	private Long createId;
	private Date createAt;
	private Long updateId;
	private Date updateAt;

	/**
	 * 图片地址.
	 */
	private String coverUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getCoverId() {
		return coverId;
	}

	public void setCoverId(long coverId) {
		this.coverId = coverId;
	}

	public long getFileId() {
		return fileId;
	}

	public void setFileId(long fileId) {
		this.fileId = fileId;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Long getUpdateId() {
		return updateId;
	}

	public void setUpdateId(Long updateId) {
		this.updateId = updateId;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

}
