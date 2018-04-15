package com.lanking.uxb.service.zuoye.api;

import java.util.List;

/**
 * 学生错题OCR相关服务
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface ZyStudentFallibleQuestionOCRService {
	/**
	 * 学生拍照识别相关接口
	 *
	 * @param fileId
	 *            文件id
	 * @return 识别出来的题目id列表
	 */
	List<Long> ocr(long fileId);
}
