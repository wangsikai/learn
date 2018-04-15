package com.lanking.uxb.service.honor.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLogType;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthRule;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.value.VGrowthLog;

/**
 * 用户成长值日志 convert
 * 
 * @author wangsenhao
 * @since yoomath V1.8
 *
 */
@Component
public class GrowthLogConvert extends Converter<VGrowthLog, GrowthLog, Long> {

	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private UserTaskService userTaskService;

	@Override
	protected Long getId(GrowthLog s) {
		return s.getId();
	}

	@Override
	protected VGrowthLog convert(GrowthLog s) {
		VGrowthLog vGrowthLog = new VGrowthLog();
		vGrowthLog.setCreateAt(s.getCreateAt());
		vGrowthLog.setGrowth(s.getGrowthValue());
		return vGrowthLog;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 普通成长值转换
		assemblers.add(new ConverterAssembler<VGrowthLog, GrowthLog, Integer, GrowthRule>() {

			@Override
			public boolean accept(GrowthLog s) {
				return s.getType() == GrowthLogType.GROWTH_RULE;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(GrowthLog s, VGrowthLog d) {
				return s.getRuleCode();
			}

			@Override
			public void setValue(GrowthLog s, VGrowthLog d, GrowthRule value) {
				d.setDescription(value.getDescription());
			}

			@Override
			public GrowthRule getValue(Integer key) {
				return growthRuleService.getByCode(key);
			}

			@Override
			public Map<Integer, GrowthRule> mgetValue(Collection<Integer> keys) {
				return growthRuleService.mgetGrowthRule(keys);
			}

		});

		// 通过任务获得的成长值
		assemblers.add(new ConverterAssembler<VGrowthLog, GrowthLog, Integer, UserTask>() {

			@Override
			public boolean accept(GrowthLog growthLog) {
				return growthLog.getType() == GrowthLogType.USER_TASK;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(GrowthLog growthLog, VGrowthLog vGrowthLog) {
				return growthLog.getRuleCode();
			}

			@Override
			public void setValue(GrowthLog growthLog, VGrowthLog vGrowthLog, UserTask value) {
				if (value != null) {
					vGrowthLog.setDescription(value.getName());
				}
			}

			@Override
			public UserTask getValue(Integer key) {
				return userTaskService.get(key);
			}

			@Override
			public Map<Integer, UserTask> mgetValue(Collection<Integer> keys) {
				return userTaskService.mget(keys);
			}
		});
	}
}
