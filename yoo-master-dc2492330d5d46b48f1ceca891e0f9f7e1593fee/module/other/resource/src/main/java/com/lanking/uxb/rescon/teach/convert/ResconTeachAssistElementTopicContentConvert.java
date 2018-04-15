package com.lanking.uxb.rescon.teach.convert;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementTopicContent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElementTopicContent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * TeachAssistElementTopicContent -> VTeachAssistElementTopicContent
 * 
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
public class ResconTeachAssistElementTopicContentConvert extends
		Converter<VTeachAssistElementTopicContent, TeachAssistElementTopicContent, Long> {
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(TeachAssistElementTopicContent teachAssistElementTopicContent) {
		return teachAssistElementTopicContent.getId();
	}

	@Override
	protected VTeachAssistElementTopicContent convert(TeachAssistElementTopicContent teachAssistElementTopicContent) {
		VTeachAssistElementTopicContent v = new VTeachAssistElementTopicContent();
		v.setId(teachAssistElementTopicContent.getId());
		v.setName(teachAssistElementTopicContent.getName());
		v.setContent(teachAssistElementTopicContent.getContent());
		v.setQuestion1Strategy(teachAssistElementTopicContent.getQuestion1Strategy());
		v.setQuestion2Strategy(teachAssistElementTopicContent.getQuestion2Strategy());
		v.setQuestion3Strategy(teachAssistElementTopicContent.getQuestion3Strategy());
		v.setSequence(teachAssistElementTopicContent.getSequence());
		v.setTopicId(teachAssistElementTopicContent.getTopicId());
		return v;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementTopicContent, TeachAssistElementTopicContent, Long, VQuestion>() {

					@Override
					public boolean accept(TeachAssistElementTopicContent teachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion1() != null
								&& teachAssistElementTopicContent.getQuestion1() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion1();
					}

					@Override
					public void setValue(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent, VQuestion value) {
						vTeachAssistElementTopicContent.setQuestion1(value);
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
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementTopicContent, TeachAssistElementTopicContent, Long, VQuestion>() {

					@Override
					public boolean accept(TeachAssistElementTopicContent teachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion2() != null
								&& teachAssistElementTopicContent.getQuestion2() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion2();
					}

					@Override
					public void setValue(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent, VQuestion value) {
						vTeachAssistElementTopicContent.setQuestion2(value);
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
		assemblers
				.add(new ConverterAssembler<VTeachAssistElementTopicContent, TeachAssistElementTopicContent, Long, VQuestion>() {

					@Override
					public boolean accept(TeachAssistElementTopicContent teachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion3() != null
								&& teachAssistElementTopicContent.getQuestion3() > 0;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent) {
						return teachAssistElementTopicContent.getQuestion3();
					}

					@Override
					public void setValue(TeachAssistElementTopicContent teachAssistElementTopicContent,
							VTeachAssistElementTopicContent vTeachAssistElementTopicContent, VQuestion value) {
						vTeachAssistElementTopicContent.setQuestion3(value);
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
