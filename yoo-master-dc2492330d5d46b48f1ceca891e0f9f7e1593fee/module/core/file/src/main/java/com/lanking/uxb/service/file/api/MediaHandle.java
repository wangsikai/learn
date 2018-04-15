package com.lanking.uxb.service.file.api;

import java.io.File;
import java.util.Map;

import com.lanking.cloud.domain.base.file.FileType;

/**
 * 定义对音频的操作接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月3日
 *
 */
public interface MediaHandle {

	int getDuration(File file);

	void screenshots(File file, int w, int h, int s);

	Map<Integer, Integer> getWH(File file);

	void convert(String srcFilePath, String destFilePath, FileType type);

	void segment(String srcFilePath, String destFilePath, FileType type, int start, int end);
}
