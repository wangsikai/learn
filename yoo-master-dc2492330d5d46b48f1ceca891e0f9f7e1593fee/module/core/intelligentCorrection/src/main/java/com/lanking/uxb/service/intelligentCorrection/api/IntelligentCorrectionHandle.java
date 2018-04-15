package com.lanking.uxb.service.intelligentCorrection.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 智能批改处理handle
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月8日
 */
public interface IntelligentCorrectionHandle {

	void handle(List<Long> queryIds, List<Long> answerIds, List<String> targets, List<String> querys,
			Map<Long, CorrectResult> results) throws IntelligentCorrectionException, IllegalArgException;

}
