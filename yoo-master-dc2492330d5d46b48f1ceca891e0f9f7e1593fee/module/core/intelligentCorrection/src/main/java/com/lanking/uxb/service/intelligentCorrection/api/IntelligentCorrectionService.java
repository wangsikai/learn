package com.lanking.uxb.service.intelligentCorrection.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 智能批改相关接口
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月8日
 */
public interface IntelligentCorrectionService {

	/**
	 * 单个批改
	 * 
	 * @param queryId
	 *            批改目标ID,通过此参数获取对应的批改结果
	 * @param answerId
	 *            对应库里面的答案ID
	 * @param target
	 *            正确的答案
	 * @param query
	 *            需要批改的答案
	 * @return 批改结果 {@link CorrectResult}
	 * @throws IntelligentCorrectionException
	 */
	CorrectResult correct(Long queryId, Long answerId, String target, String query)
			throws IntelligentCorrectionException, IllegalArgException;

	/**
	 * 批量批改
	 * 
	 * @param queryIds
	 *            批改目标ID,通过此参数获取对应的批改结果
	 * @param answerIds
	 *            对应库里面的答案ID
	 * @param targets
	 *            正确的答案
	 * @param querys
	 *            需要批改的答案
	 * @return 批改结果 {@link CorrectResult}
	 * @throws IntelligentCorrectionException
	 */
	Map<Long, CorrectResult> correct(List<Long> queryIds, List<Long> answerIds, List<String> targets,
			List<String> querys) throws IntelligentCorrectionException, IllegalArgException;
}
