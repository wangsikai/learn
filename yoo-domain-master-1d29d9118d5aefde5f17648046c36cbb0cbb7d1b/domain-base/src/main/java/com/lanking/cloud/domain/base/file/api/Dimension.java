package com.lanking.cloud.domain.base.file.api;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 尺寸信息
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class Dimension implements Serializable {

	private static final long serialVersionUID = 151428284803135503L;

	private int width;
	private int height;

	public Dimension() {
	}

	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
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

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
