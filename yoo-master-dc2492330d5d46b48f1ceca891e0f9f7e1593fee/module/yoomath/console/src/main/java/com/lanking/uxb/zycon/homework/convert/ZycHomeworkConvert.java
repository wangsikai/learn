package com.lanking.uxb.zycon.homework.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryType;
import com.lanking.uxb.zycon.homework.value.VZycHomework;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkClazz;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Component
public class ZycHomeworkConvert extends Converter<VZycHomework, Homework, Long> {
	@Autowired
	private ZycHomeworkClazzConvert zycHomeworkClazzConvert;
	
	@Autowired
	private ZyHomeworkClassGroupService zyHomeworkClassGroupService;

	@Override
	protected Long getId(Homework homework) {
		return homework.getId();
	}

	@Override
	protected VZycHomework convert(Homework homework) {
		VZycHomework v = new VZycHomework();
		v.setId(homework.getId());
		v.setDeadline(homework.getDeadline());
		v.setId(homework.getId());
		v.setStartTime(homework.getStartTime());
		v.setStatus(homework.getStatus());
		v.setName(homework.getName());
		if (homework.getLastCommitAt() != null) {// 最后一个已经提交
			v.setAllCommitMement(System.currentTimeMillis() > homework.getLastCommitAt().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000);
		}
		v.setCommitCount(homework.getCommitCount());
		v.setDistributeCount(homework.getDistributeCount());
		v.setDelStatus(homework.getDelStatus());
		v.setManStatus(homework.getManStatus());
		v.setAllCorrectComplete(homework.isAllCorrectComplete());
		
		if (v.getStatus() == HomeworkStatus.NOT_ISSUE) {
			v.setType(HomeworkQueryType.FINISH);
		} else if (v.getStatus() == HomeworkStatus.PUBLISH){
			v.setType(HomeworkQueryType.INIT);
		}
		
		//是否正在批改中
		if(v.getCommitCount() > 0 && homework.getCorrectingCount() < v.getCommitCount()) {
			v.setCorrectIng(true);
		} else {
			v.setCorrectIng(false);
		}
		
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycHomework, Homework, Long, VZycHomeworkClazz>() {

			@Override
			public boolean accept(Homework homework) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework homework, VZycHomework vZycHomework) {
				return homework.getHomeworkClassId();
			}

			@Override
			public void setValue(Homework homework, VZycHomework vZycHomework, VZycHomeworkClazz value) {
				if (value != null) {
					vZycHomework.setClazz(value);
				}
			}

			@Override
			public VZycHomeworkClazz getValue(Long key) {
				if (null == key)
					return null;
				return zycHomeworkClazzConvert.get(key);
			}

			@Override
			public Map<Long, VZycHomeworkClazz> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				return zycHomeworkClazzConvert.mget(keys);
			}
		});
		
		assemblers.add(new ConverterAssembler<VZycHomework, Homework, Long, HomeworkClazzGroup>() {

			@Override
			public boolean accept(Homework homework) {
				return homework.getHomeworkClassGroupId() != null && homework.getHomeworkClassGroupId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Homework homework, VZycHomework vZycHomework) {
				return homework.getHomeworkClassGroupId();
			}

			@Override
			public void setValue(Homework homework, VZycHomework vZycHomework, HomeworkClazzGroup value) {
				if (value != null) {
					vZycHomework.getClazz().setGroupName(value.getName());
				}
			}

			@Override
			public HomeworkClazzGroup getValue(Long key) {
				if (null == key)
					return null;
				return zyHomeworkClassGroupService.get(key);
			}

			@Override
			public Map<Long, HomeworkClazzGroup> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				return zyHomeworkClassGroupService.mget(keys);
			}
		});
		
	}
}
