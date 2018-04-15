package com.lanking.cloud.domain.base.file.api;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片类型
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public enum ImageType {
	JPG("jpg", "jpeg"), PNG("png"), GIF("gif"), BMP("bmp");

	private String[] exts;
	private static Map<String, ImageType> MAPPING = new HashMap<String, ImageType>();

	static {
		for (ImageType it : values()) {
			for (String ext : it.exts) {
				MAPPING.put(ext, it);
			}
		}
	}

	private ImageType(String... exts) {
		this.exts = exts;
	}

	public String getExt() {
		return exts[0];
	}

	public static ImageType from(String fileName) {
		return fromExt(AbstractFileUtil.getExt(fileName));
	}

	public static ImageType fromExt(String ext) {
		return MAPPING.get(ext.toLowerCase());
	}
}
