package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.value.VQuestionCollection;

/**
 * 错题练习题目列表用convert 删除不需要的initStudentQuestionCount
 * 
 * @since sprint-69
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年5月25日
 */
@Component
public class ZyQuestionCollectionConvert3 extends Converter<VQuestionCollection, QuestionCollection, Long> {

	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ZyQuestionCarService questionCarService;

	@Override
	protected Long getId(QuestionCollection s) {
		return s.getId();
	}

	@Override
	protected VQuestionCollection convert(QuestionCollection s) {
		VQuestionCollection vq = new VQuestionCollection();
		vq.setCollectId(s.getId());
		vq.setCollectTime(s.getCreateAt());
		return vq;
	}

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VQuestionCollection, QuestionCollection, Long, VQuestion>() {

			@Override
			public boolean accept(QuestionCollection s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionCollection s, VQuestionCollection d) {
				return s.getQuestionId();
			}

			@Override
			public void setValue(QuestionCollection s, VQuestionCollection d, VQuestion value) {
				d.setvQuestion(value);

			}

			@Override
			public VQuestion getValue(Long key) {
				QuestionConvertOption qco = new QuestionConvertOption();
				qco.setAnalysis(true);
				qco.setAnswer(true);
				qco.setCollect(true);
				qco.setInitSub(true);
				qco.setInitExamination(true);
				qco.setInitStudentQuestionCount(false);
				qco.setInitTextbookCategory(true);
				qco.setInitMetaKnowpoint(true);
				qco.setInitPhase(true);
				qco.setInitSubject(false);
				qco.setInitQuestionType(true);
				qco.setInitKnowledgePoint(true);
				qco.setInitPublishCount(true);
				qco.setInitQuestionTag(true);
				qco.setInitQuestionSimilarCount(true);

				// 设置题目是否被加入作业篮子
				VQuestion vq = questionConvert.to(questionService.get(key), qco);
				List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
				if (vq.getType() != Type.COMPOSITE) {
					vq.setInQuestionCar(carQuestions.contains(vq.getId()));
				}

				return vq;
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				QuestionConvertOption qco = new QuestionConvertOption();
				qco.setAnalysis(true);
				qco.setAnswer(true);
				qco.setCollect(true);
				qco.setInitSub(true);
				qco.setInitExamination(true);
				qco.setInitStudentQuestionCount(false);
				qco.setInitTextbookCategory(true);
				qco.setInitMetaKnowpoint(true);
				qco.setInitPhase(true);
				qco.setInitSubject(false);
				qco.setInitQuestionType(true);
				qco.setInitKnowledgePoint(true);
				qco.setInitPublishCount(true);
				qco.setInitQuestionTag(true);
				qco.setInitQuestionSimilarCount(true);

				// 设置题目是否被加入作业篮子
				Map<Long, VQuestion> map = questionConvert.to(questionService.mget(keys), qco);
				List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
				for (Long qId : map.keySet()) {
					VQuestion v = map.get(qId);

					if (v.getType() != Type.COMPOSITE) {
						v.setInQuestionCar(carQuestions.contains(v.getId()));
						map.put(qId, v);
					}
				}
				return map;
			}
		});
	}
}
