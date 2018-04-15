package com.lanking.cloud.domain.base.file.api;

import java.util.Set;

import com.google.common.collect.Sets;

/**
 * 文件扩展名(使用常量,不在配置文件里面配置)
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
public class FileExt {
	public static final Set<String> IMAGE = Sets.newHashSet("gif", "bmp", "jpg", "jpeg", "png");
	public static final Set<String> DOC = Sets.newHashSet("doc", "docx", "rtf", "xls", "xlsx", "ppt", "pptx", "pdf",
			"pps", "pot", "dps", "et", "wps");
	public static final Set<String> TEXT = Sets.newHashSet("txt");
	public static final Set<String> VIDEO = Sets.newHashSet("mpg", "mpeg", "mpe", "avi", "mov", "asf", "mp4", "wmv",
			"flv", "3gp", "rm", "rmvb");
	public static final Set<String> AUDIO = Sets.newHashSet("mp3", "wav", "wma", "ogg", "ape", "flac", "aif", "pcm",
			"aac", "acc", "amr");
	public static final Set<String> ZIP = Sets.newHashSet("zip", "rar", "7z", "jar", "gz", "tgz", "bz2", "tar", "iso");

	public static final String SEPARATOR = ".";
	public static final String JPG = "jpg";
	public static final String PNG = "png";
	public static final String MP3 = "mp3";
	public static final String MP4 = "mp4";
	public static final String PDF = "pdf";
	public static final String SWF = "swf";
	public static final String GIF = "gif";
}
