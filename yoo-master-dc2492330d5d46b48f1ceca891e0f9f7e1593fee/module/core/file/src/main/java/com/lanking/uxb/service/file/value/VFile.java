package com.lanking.uxb.service.file.value;

import java.io.Serializable;

import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.uxb.service.file.util.FileUtil;

public class VFile implements Serializable {

	private static final long serialVersionUID = -6845071382033638678L;

	private String id;
	private String name;
	private long size;
	private int duration;
	private int width;
	private int height;
	private FileType type;
	private String ext;
	private String url;
	private String webThumbUrl;
	private String webMidThumbUrl;
	private long screenshotId;
	private String screenshotUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		setExt(FileUtil.getSimpleExt(name));
		this.name = name;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public FileType getType() {
		return type;
	}

	public void setType(FileType type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebThumbUrl() {
		return webThumbUrl;
	}

	public void setWebThumbUrl(String webThumbUrl) {
		this.webThumbUrl = webThumbUrl;
	}

	public long getScreenshotId() {
		return screenshotId;
	}

	public void setScreenshotId(long screenshotId) {
		this.screenshotId = screenshotId;
	}

	public String getScreenshotUrl() {
		return screenshotUrl;
	}

	public void setScreenshotUrl(String screenshotUrl) {
		this.screenshotUrl = screenshotUrl;
	}

	public String getWebMidThumbUrl() {
		return webMidThumbUrl;
	}

	public void setWebMidThumbUrl(String webMidThumbUrl) {
		this.webMidThumbUrl = webMidThumbUrl;
	}

}
