package com.lanking.uxb.service.file.api;

import java.io.IOException;

import com.lanking.cloud.domain.base.file.File;

public interface OFileService {
	/**
	 * 旋转已有图片并生成新的图片
	 * 
	 * @param id
	 *            已有图片ID
	 * @param degree
	 *            旋转的角度
	 * @return 新的图片对象
	 */
	File rotate(long id, int degree) throws IOException;
}
