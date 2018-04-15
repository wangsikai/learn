package com.lanking.uxb.service.zuoye.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.zuoye.value.VKnowpointCard;

/**
 * 知识卡片convert
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:42:14
 */
@Component
public class ZyKnowpointCardConvert extends Converter<VKnowpointCard, KnowpointCard, Long> {

	@Autowired
	private QuestionService questionService;

	@Autowired
	private QuestionConvert questionConvert;

	@Override
	protected Long getId(KnowpointCard knowpointCard) {
		return knowpointCard.getId();
	}

	@Override
	protected VKnowpointCard convert(KnowpointCard card) {
		VKnowpointCard v = new VKnowpointCard();
		v.setId(card.getId());
		v.setDescription(card.getDescription());
		v.setHints(card.getHints());
		v.setKnowpointCode(card.getKnowpointCode());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 例题
		assemblers.add(new ConverterAssembler<VKnowpointCard, KnowpointCard, List<Long>, List<VQuestion>>() {
			@Override
			public boolean accept(KnowpointCard s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Long> getKey(KnowpointCard s, VKnowpointCard d) {
				return s.getExamples();
			}

			@Override
			public void setValue(KnowpointCard s, VKnowpointCard d, List<VQuestion> value) {
				if (value != null) {
					d.setExampleQuestions(value);
				}
			}

			@Override
			public List<VQuestion> getValue(List<Long> key) {
				QuestionConvertOption option = new QuestionConvertOption();
				option.setAnalysis(true);
				option.setAnswer(true);
				Map<Long, VQuestion> map = questionConvert.to(questionService.mget(key), option);
				List<VQuestion> list = new ArrayList<VQuestion>(map.size());
				for (Long id : key) {
					if (map.get(id) != null) {
						list.add(map.get(id));
					}
				}
				return list;
			}

			@Override
			public Map<List<Long>, List<VQuestion>> mgetValue(Collection<List<Long>> keys) {
				throw new NotImplementedException();
			}

		});
	}

}
