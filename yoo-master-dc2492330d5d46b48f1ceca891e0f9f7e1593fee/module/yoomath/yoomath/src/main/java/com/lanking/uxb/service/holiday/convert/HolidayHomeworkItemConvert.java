package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.holiday.value.VHolidayHomeworkItem;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 作业专项转换
 * 
 * @since 2.8
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年12月22日 下午4:50:38
 */
@Component
public class HolidayHomeworkItemConvert extends Converter<VHolidayHomeworkItem, HolidayHomeworkItem, Long> {

	@Autowired
	private MetaKnowpointService metaKnowpointService;
	@Autowired
	private MetaKnowpointConvert metaKnowpointConvert;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;

	@Override
	protected Long getId(HolidayHomeworkItem s) {
		return s.getId();
	}

	@Override
	protected VHolidayHomeworkItem convert(HolidayHomeworkItem s) {
		VHolidayHomeworkItem homeworkItem = new VHolidayHomeworkItem();
		homeworkItem.setId(s.getId());
		homeworkItem.setCommitCount(s.getCommitCount() == null ? 0 : s.getCommitCount());
		homeworkItem.setDifficulty(s.getDifficulty() == null ? BigDecimal.valueOf(0) : s.getDifficulty());
		homeworkItem.setDistributeCount(s.getDistributeCount() == null ? 0 : s.getDistributeCount());
		homeworkItem.setHolidayHomeworkId(s.getHolidayHomeworkId());
		homeworkItem.setName(s.getName());
		homeworkItem.setRightRate(s.getRightRate() == null ? BigDecimal.valueOf(-1) : s.getRightRate());
		homeworkItem.setHomeworkTime(s.getHomeworkTime());
		if (s.getDistributeCount() != 0) {
			homeworkItem.setFinishRate(BigDecimal.valueOf(Math.round(s.getCommitCount().doubleValue() * 100
					/ s.getDistributeCount())));
			homeworkItem.setFinishRateTitle(homeworkItem.getFinishRate() + "%");
		} else {
			homeworkItem.setFinishRate(BigDecimal.valueOf(0));
			homeworkItem.setFinishRateTitle("0%");
		}

		homeworkItem.setCompletionRate(homeworkItem.getFinishRate());

		homeworkItem.setCreateAt(s.getCreateAt());
		homeworkItem.setQuestionCount(s.getQuestionCount() == null ? 0 : s.getQuestionCount());
		homeworkItem.setStartTime(s.getStartTime());
		homeworkItem.setDeadline(s.getDeadline());
		homeworkItem.setStatus(s.getStatus());
		// init知识点
		List<Integer> metaKnowpointCodes = Lists.newArrayList();
		List<Long> metaKnowpoints = s.getMetaKnowpoints();
		if (metaKnowpoints != null) {
			for (Long code : metaKnowpoints) {
				metaKnowpointCodes.add(code.intValue());
			}
		}
		homeworkItem.setMetaKnowpoints(metaKnowpointConvert.to(metaKnowpointService.mgetList(metaKnowpointCodes)));

		if (CollectionUtils.isEmpty(s.getKnowledgePoints())) {
			homeworkItem.setKnowledgePoints(Collections.EMPTY_LIST);
		} else {
			List<KnowledgePoint> knowledgePoints = knowledgePointService.mgetList(s.getKnowledgePoints());
			homeworkItem.setKnowledgePoints(knowledgePointConvert.to(knowledgePoints));
		}

		return homeworkItem;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 班级信息
		assemblers.add(new ConverterAssembler<VHolidayHomeworkItem, HolidayHomeworkItem, Long, VHomeworkClazz>() {

			@Override
			public boolean accept(HolidayHomeworkItem s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayHomeworkItem s, VHolidayHomeworkItem d) {
				return s.getHomeworkClassId();
			}

			@Override
			public void setValue(HolidayHomeworkItem s, VHolidayHomeworkItem d, VHomeworkClazz value) {
				if (value != null) {
					d.setClazz(value);
				}
			}

			@Override
			public VHomeworkClazz getValue(Long key) {
				if (key == null) {
					return null;
				}
				HomeworkClazz clazz = homeworkClassService.get(key);
				return homeworkClazzConvert.to(clazz);
			}

			@Override
			public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return homeworkClazzConvert.toMap(Lists.newArrayList(homeworkClassService.mget(keys).values()));
			}
		});

	}
}
