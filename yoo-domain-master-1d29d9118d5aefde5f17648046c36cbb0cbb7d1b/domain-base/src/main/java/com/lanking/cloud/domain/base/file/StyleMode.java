package com.lanking.cloud.domain.base.file;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 文件处理模式
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public enum StyleMode implements Valueable {
	/**
	 * 限定缩略图的长边最多为<Width>，短边最多为<Height>，进行等比缩放，不裁剪。如果只指定 w 参数则表示限定长边（短边自适应），只指定
	 * h 参数则表示限定短边（长边自适应）。
	 */
	MAX_EDGE(0),
	/**
	 * 限定缩略图的宽最少为<Width>，高最少为<Height>，进行等比缩放，居中裁剪。转后的缩略图通常恰好是 <Width>x<Height>
	 * 的大小（有一个边缩放的时候会因为超出矩形框而被裁剪掉多余部分）。如果只指定 w 参数或只指定 h 参数，代表限定为长宽相等的正方图。
	 */
	MAX_W_H_CUT(1),
	/**
	 * 限定缩略图的宽最多为<Width>，高最多为<Height>，进行等比缩放，不裁剪。如果只指定 w 参数则表示限定长边（短边自适应），只指定 h
	 * 参数则表示限定短边 （长边自适应）。它和模式0类似，区别只是限定宽和高，不是限定长边和短边。从应用场景来说，
	 * 模式0适合移动设备上做缩略图，模式2适合PC上做缩略图。
	 */
	MAX_W_H(2),
	/**
	 * 质量压缩
	 */
	QUALITY_COMPRESS(3);

	private int value;

	StyleMode(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static StyleMode findByValue(int value) {
		switch (value) {
		case 0:
			return MAX_EDGE;
		case 1:
			return MAX_W_H_CUT;
		case 2:
			return MAX_W_H;
		case 3:
			return QUALITY_COMPRESS;
		default:
			return null;
		}
	}

}
