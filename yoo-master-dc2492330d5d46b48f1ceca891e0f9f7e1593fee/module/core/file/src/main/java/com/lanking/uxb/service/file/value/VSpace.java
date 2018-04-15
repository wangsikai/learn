package com.lanking.uxb.service.file.value;

import java.io.Serializable;

public class VSpace implements Serializable {
	private static final long serialVersionUID = -3893787153289813059L;

	private long id;
	private String name;
	private long fileCount;
	private long maxFileSize;
	private long size;
	private long usedSize;
	private boolean authorization;
	private boolean preview;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFileCount() {
		return fileCount;
	}

	public void setFileCount(long fileCount) {
		this.fileCount = fileCount;
	}

	public long getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public long getUsedSize() {
		return usedSize;
	}

	public void setUsedSize(long usedSize) {
		this.usedSize = usedSize;
	}

	public boolean isAuthorization() {
		return authorization;
	}

	public void setAuthorization(boolean authorization) {
		this.authorization = authorization;
	}

	public boolean isPreview() {
		return preview;
	}

	public void setPreview(boolean preview) {
		this.preview = preview;
	}

}
