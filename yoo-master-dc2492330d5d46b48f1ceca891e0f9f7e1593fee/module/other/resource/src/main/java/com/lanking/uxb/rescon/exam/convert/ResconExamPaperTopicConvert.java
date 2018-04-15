package com.lanking.uxb.rescon.exam.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.exam.value.VExamPaperTopic;

/**
 * 试卷题目类型 convert
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月22日 上午9:09:31
 */
@Component
public class ResconExamPaperTopicConvert extends Converter<VExamPaperTopic, ExamPaperTopic, Long> {
	@Override
	protected Long getId(ExamPaperTopic s) {
		return s.getId();
	}

	@Override
	protected VExamPaperTopic convert(ExamPaperTopic s) {
		VExamPaperTopic v = new VExamPaperTopic();
		v.setName(s.getName());
		v.setId(s.getId());
		v.setExamId(s.getExamPaperId());
		v.setSequence(s.getSequence());
		v.setType(s.getType());
		return v;
	}
}
