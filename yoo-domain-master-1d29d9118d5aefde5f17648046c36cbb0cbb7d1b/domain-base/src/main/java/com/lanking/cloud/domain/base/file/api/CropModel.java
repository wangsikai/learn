package com.lanking.cloud.domain.base.file.api;

/**
 * 图片切割模型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class CropModel {
	/**
	 * 切图左上角距图片左边缘尺寸
	 */
	private Integer left;
	/**
	 * 切图左上角距图片左边缘尺寸
	 */
	private Integer top;
	/**
	 * 目标width
	 */
	private Integer w;
	/**
	 * 目标height
	 */
	private Integer h;

	public CropModel() {
		super();
	}

	public CropModel(Integer left, Integer top, Integer w, Integer h) {
		super();
		this.left = left;
		this.top = top;
		this.w = w;
		this.h = h;
	}

	/**
	 * 判断参数合法性
	 * 
	 * @return true|false
	 */
	public boolean isLegal() {
		if (left == null || top == null || (left != null && left <= 0) || (top != null && top <= 0)
				|| (w == null && h == null) || (w != null && w <= 0) || (h != null && h <= 0)) {
			return false;
		}
		if (w == null) {
			w = h;
		}
		if (h == null) {
			h = w;
		}
		return true;
	}

	public Integer getLeft() {
		return left;
	}

	public void setLeft(Integer left) {
		this.left = left;
	}

	public Integer getTop() {
		return top;
	}

	public void setTop(Integer top) {
		this.top = top;
	}

	public Integer getW() {
		return w;
	}

	public void setW(Integer w) {
		this.w = w;
	}

	public Integer getH() {
		return h;
	}

	public void setH(Integer h) {
		this.h = h;
	}

}
