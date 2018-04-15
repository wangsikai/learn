package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.zuoye.value.VStudentQuestionAnswer;

@Component
public class ZyStudentQuestionAnswerConvert extends Converter<VStudentQuestionAnswer, StudentQuestionAnswer, Long> {

	@Override
	protected Long getId(StudentQuestionAnswer s) {
		return s.getId();
	}

	@Override
	protected VStudentQuestionAnswer convert(StudentQuestionAnswer s) {
		VStudentQuestionAnswer v = new VStudentQuestionAnswer();
		v.setId(s.getId());
		v.setQuestionId(s.getQuestionId());
		v.setAnswers(s.getAnswers());
		v.setAnswersAscii(s.getAnswersAscii());
		v.setCreateAt(s.getCreateAt());
		if (CollectionUtils.isNotEmpty(s.getAnswerImgs())) {
			v.setAnswerImg(FileUtil.getUrl(s.getAnswerImgs().get(0)));
			for (Long answerImg : s.getAnswerImgs()) {
				v.getAnswerImgs().add(FileUtil.getUrl(answerImg));
			}
		}
		v.setItemResults(s.getItemResults());
		v.setResult(s.getResult());
		return v;
	}
}
