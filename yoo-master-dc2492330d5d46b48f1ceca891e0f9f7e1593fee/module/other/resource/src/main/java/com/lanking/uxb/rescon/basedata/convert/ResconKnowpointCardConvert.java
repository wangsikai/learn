package com.lanking.uxb.rescon.basedata.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.basedata.value.VResconKnowpointCard;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;

/**
 * 知识卡片convert
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2016年1月12日 上午10:42:14
 */
@Component
public class ResconKnowpointCardConvert extends Converter<VResconKnowpointCard, KnowpointCard, Long> {

	@Autowired
	private ResconQuestionConvert questionConvert;

	@Override
	protected Long getId(KnowpointCard knowpointCard) {
		return knowpointCard.getId();
	}

	@Override
	protected VResconKnowpointCard convert(KnowpointCard card) {
		VResconKnowpointCard v = new VResconKnowpointCard();
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
		assemblers.add(new ConverterAssembler<VResconKnowpointCard, KnowpointCard, List<Long>, List<VQuestion>>() {
			@Override
			public boolean accept(KnowpointCard s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Long> getKey(KnowpointCard s, VResconKnowpointCard d) {
				return s.getExamples();
			}

			@Override
			public void setValue(KnowpointCard s, VResconKnowpointCard d, List<VQuestion> value) {
				if (value != null) {
					d.setExampleQuestions(value);
				}
			}

			@Override
			public List<VQuestion> getValue(List<Long> key) {
				return questionConvert.mgetList(key);
			}

			@Override
			public Map<List<Long>, List<VQuestion>> mgetValue(Collection<List<Long>> keys) {
				throw new NotImplementedException();
			}

		});
	}

}
