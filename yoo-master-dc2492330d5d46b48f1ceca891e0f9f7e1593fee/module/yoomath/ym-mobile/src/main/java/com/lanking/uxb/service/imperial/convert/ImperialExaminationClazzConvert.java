package com.lanking.uxb.service.imperial.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.imperial.value.VImperialExaminationClazz;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

@Component
public class ImperialExaminationClazzConvert extends Converter<VImperialExaminationClazz, Homework, Long> {

	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	@Override
	protected Long getId(Homework s) {
		return s.getId();
	}

	@Override
	protected VImperialExaminationClazz convert(Homework s) {
		VImperialExaminationClazz v = new VImperialExaminationClazz();
		v.setClassId(s.getHomeworkClassId());
		v.setCommitCount(s.getCommitCount());
		v.setStatus(s.getStatus());
		v.setHomeworkId(s.getId());
		v.setDistributeCount(s.getDistributeCount());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VImperialExaminationClazz, Homework, Long, HomeworkClazz>() {

			@Override
			public boolean accept(Homework s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework s, VImperialExaminationClazz d) {
				return s.getHomeworkClassId();
			}

			@Override
			public void setValue(Homework s, VImperialExaminationClazz d, HomeworkClazz value) {
				d.setClassName(value.getName());
			}

			@Override
			public HomeworkClazz getValue(Long key) {
				return homeworkClassService.get(key);
			}

			@Override
			public Map<Long, HomeworkClazz> mgetValue(Collection<Long> keys) {
				return homeworkClassService.mget(keys);
			}
		});
	}

}
