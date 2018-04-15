package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementFreeEditContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementFreeEditContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementFreeEditContent -> VTeachAssistElementFreeEditContent
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementFreeEditContentConvert extends
		Converter<VTeachAssistElementFreeEditContent, TeachAssistElementFreeEditContent, Long> {
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(TeachAssistElementFreeEditContent teachAssistElementFreeEditContent) {
		return teachAssistElementFreeEditContent.getId();
	}

	@Override
	protected VTeachAssistElementFreeEditContent convert(
			TeachAssistElementFreeEditContent teachAssistElementFreeEditContent) {
		VTeachAssistElementFreeEditContent v = new VTeachAssistElementFreeEditContent();
		v.setContent(teachAssistElementFreeEditContent.getContent());
		v.setSequence(teachAssistElementFreeEditContent.getSequence());
		v.setFreeEditId(teachAssistElementFreeEditContent.getFreeEditId());
		v.setId(teachAssistElementFreeEditContent.getId());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementFreeEditContent, TeachAssistElementFreeEditContent, Long, VQuestion>() {

					@Override
					public boolean accept(TeachAssistElementFreeEditContent teachAssistElementFreeEditContent) {
						return teachAssistElementFreeEditContent.getQuestionId() != null
								&& teachAssistElementFreeEditContent.getQuestionId() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementFreeEditContent teachAssistElementFreeEditContent,
							VTeachAssistElementFreeEditContent vTeachAssistElementFreeEditContent) {
						return teachAssistElementFreeEditContent.getQuestionId();
					}

					@Override
					public void setValue(TeachAssistElementFreeEditContent teachAssistElementFreeEditContent,
							VTeachAssistElementFreeEditContent vTeachAssistElementFreeEditContent, VQuestion value) {
						vTeachAssistElementFreeEditContent.setQuestion(value);
					}

					@Override
					public VQuestion getValue(Long key) {
						return questionConvert.to(questionManage.get(key));
					}

					@Override
					public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Collections.EMPTY_MAP;
						}
						return questionConvert.to(questionManage.mget(keys));
					}
				});
	}
}
