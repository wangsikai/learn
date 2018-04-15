package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPracticeComment;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPracticeComment;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementPracticeComment -> VTeachAssistElementPracticeComment
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPracticeCommentConvert extends
		Converter<VTeachAssistElementPracticeComment, TeachAssistElementPracticeComment, Long> {

	@Override
	protected Long getId(TeachAssistElementPracticeComment teachAssistElementPracticeComment) {
		return teachAssistElementPracticeComment.getId();
	}

	@Override
	protected VTeachAssistElementPracticeComment convert(
			TeachAssistElementPracticeComment teachAssistElementPracticeComment) {
		VTeachAssistElementPracticeComment v = new VTeachAssistElementPracticeComment();
		v.setId(teachAssistElementPracticeComment.getId());
		v.setSequence(teachAssistElementPracticeComment.getSequence());
		v.setType(teachAssistElementPracticeComment.getType());
		return v;
	}
}
