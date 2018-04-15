package com.lanking.cloud.domain.base.file;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 文件类型
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月13日
 */
public enum FileType implements Valueable {
	/**
	 * 二进制文件
	 */
	BIN(0),
	/**
	 * 图片
	 */
	IMAGE(1),
	/**
	 * 音频
	 */
	AUDIO(2),
	/**
	 * 视频
	 */
	VIDEO(3);

	private int value;

	FileType(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return this.value;
	}

	public static FileType findByValue(int value) {
		switch (value) {
		case 0:
			return BIN;
		case 1:
			return IMAGE;
		case 2:
			return AUDIO;
		case 3:
			return VIDEO;
		default:
			return null;
		}
	}

}
