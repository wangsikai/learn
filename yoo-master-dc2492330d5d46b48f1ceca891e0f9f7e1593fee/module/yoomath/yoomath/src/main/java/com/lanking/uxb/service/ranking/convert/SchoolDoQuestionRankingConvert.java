package com.lanking.uxb.service.ranking.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.ranking.value.VSchoolDoQuestionRanking;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

@Component
public class SchoolDoQuestionRankingConvert extends Converter<VSchoolDoQuestionRanking, DoQuestionSchoolStat, Long> {

	@Autowired
	private ZyHomeworkClassService hcService;
	@Autowired
	private ZyHomeworkClazzConvert hcConvert;

	@Override
	protected Long getId(DoQuestionSchoolStat s) {
		return s.getId();
	}

	@Override
	protected VSchoolDoQuestionRanking convert(DoQuestionSchoolStat s) {
		VSchoolDoQuestionRanking v = new VSchoolDoQuestionRanking();
		v.setId(s.getId());
		v.setQuestionCount(s.getDoCount());
		v.setRightRate(s.getRightRate());
		v.setRightRateTitle(String.valueOf(s.getRightRate().setScale(0, BigDecimal.ROUND_HALF_UP).intValue()) + "%");
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 班级信息
		assemblers.add(new ConverterAssembler<VSchoolDoQuestionRanking, DoQuestionSchoolStat, Long, VHomeworkClazz>() {

			@Override
			public boolean accept(DoQuestionSchoolStat s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(DoQuestionSchoolStat s, VSchoolDoQuestionRanking d) {
				return s.getClassId();
			}

			@Override
			public void setValue(DoQuestionSchoolStat s, VSchoolDoQuestionRanking d, VHomeworkClazz value) {
				if (value != null) {
					d.setClazz(value);
				}
			}

			@Override
			public VHomeworkClazz getValue(Long key) {
				if (key == null) {
					return null;
				}
				return hcConvert.to(hcService.get(key), new ZyHomeworkClassConvertOption(false, false, false));
			}

			@Override
			public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return hcConvert.to(hcService.mget(keys), new ZyHomeworkClassConvertOption(false, false, false));
			}

		});

	}

}
