package com.lanking.uxb.service.smartExam.convert;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.smartExamPaper.SmartExamPaper;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.smartExam.value.VPaper;

/**
 * 试卷VO
 * 
 * @since yoomath(mobile) V1.0.0
 * @author wangsenhao
 *
 */
@Component
public class PaperConvert extends Converter<VPaper, SmartExamPaper, Long> {

	@Override
	protected Long getId(SmartExamPaper s) {
		return s.getId();
	}

	@Override
	protected VPaper convert(SmartExamPaper s) {
		VPaper vPaper = new VPaper();
		vPaper.setCommitAt(s.getCommitAt());
		vPaper.setStar(s.getSmartExamPaperDifficulty().getStar());
		vPaper.setRightRate(s.getRightRate() == null ? null : s.getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP));
		vPaper.setTitle(s.getName());
		vPaper.setPaperId(s.getId());
		vPaper.setHomeworkTime(s.getHomeworkTime());
		vPaper.setAvgDifficulty(s.getDifficulty());
		return vPaper;
	}

}
