package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementPrepareComment;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementPrepareComment;

import org.springframework.stereotype.Component;

/**
 * TeachAssistElementPrepareComment -> VTeachAssistElementPrepareComment
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementPrepareCommentConvert extends
		Converter<VTeachAssistElementPrepareComment, TeachAssistElementPrepareComment, Long> {
	@Override
	protected Long getId(TeachAssistElementPrepareComment teachAssistElementPrepareComment) {
		return teachAssistElementPrepareComment.getId();
	}

	@Override
	protected VTeachAssistElementPrepareComment convert(
			TeachAssistElementPrepareComment teachAssistElementPrepareComment) {
		VTeachAssistElementPrepareComment v = new VTeachAssistElementPrepareComment();
		v.setId(teachAssistElementPrepareComment.getId());
		v.setSequence(teachAssistElementPrepareComment.getSequence());
		v.setType(teachAssistElementPrepareComment.getType());
		return v;
	}
}
