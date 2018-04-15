package com.lanking.uxb.service.question.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;

/**
 * 习题二维码相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年2月8日
 */
public interface QuestionQRCodeService {

	/**
	 * 异步添加习题二维码图片.
	 * 
	 * @param question
	 */
	void asyncMakeQRCodeImage(Question question);

	/**
	 * 批量添加习题二维码图片.
	 * 
	 * @param questions
	 */
	void batchMakeQRCodeImage(List<Question> questions);
}
