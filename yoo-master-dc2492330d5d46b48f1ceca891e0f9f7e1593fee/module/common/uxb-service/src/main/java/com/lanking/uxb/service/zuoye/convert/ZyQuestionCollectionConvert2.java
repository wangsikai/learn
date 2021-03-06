package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.zuoye.value.VQuestionCollection;

/**
 * 错题练习题目列表用convert 删除不需要的initStudentQuestionCount
 * 
 * @since sprint-69
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年5月25日
 */
@Component
public class ZyQuestionCollectionConvert2 extends Converter<VQuestionCollection, QuestionCollection, Long> {

	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;

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
				qco.setAnalysis(false);
				qco.setAnswer(false);
				qco.setCollect(false);
				qco.setInitSub(true);
				qco.setInitExamination(false);
				// 设置不查询无用的初始化数据
				qco.setInitStudentQuestionCount(false);
				qco.setInitTextbookCategory(false);
				qco.setInitMetaKnowpoint(false);
				qco.setInitPhase(false);
				qco.setInitSubject(false);
				qco.setInitQuestionType(false);
				qco.setInitKnowledgePoint(false);
				return questionConvert.to(questionService.get(key), qco);
			}

			@Override
			public Map<Long, VQuestion> mgetValue(Collection<Long> keys) {
				QuestionConvertOption qco = new QuestionConvertOption();
				qco.setAnalysis(false);
				qco.setAnswer(false);
				qco.setCollect(true);
				qco.setInitSub(true);
				qco.setInitExamination(false);
				// 设置不查询无用的初始化数据
				qco.setInitStudentQuestionCount(false);
				qco.setInitTextbookCategory(false);
				qco.setInitMetaKnowpoint(false);
				qco.setInitPhase(false);
				qco.setInitSubject(false);
				qco.setInitQuestionType(false);
				qco.setInitKnowledgePoint(false);
				return questionConvert.to(questionService.mget(keys), qco);
			}

		});
	}
}
