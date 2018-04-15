package com.lanking.uxb.service.honor.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLogType;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.value.VCoinsLog;

/**
 * 用户金币值日志 convert
 * 
 * @author wangsenhao
 * @since yoomath V1.8
 *
 */
@Component
public class CoinsLogConvert extends Converter<VCoinsLog, CoinsLog, Long> {

	@Autowired
	private CoinsRuleService coinsRuleService;
	@Autowired
	private UserTaskService userTaskService;

	@Override
	protected Long getId(CoinsLog s) {
		return s.getId();
	}

	@Override
	protected VCoinsLog convert(CoinsLog s) {
		VCoinsLog vCoinsLog = new VCoinsLog();
		vCoinsLog.setCreateAt(s.getCreateAt());
		vCoinsLog.setCoins(s.getCoinsValue());
		return vCoinsLog;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VCoinsLog, CoinsLog, Integer, CoinsRule>() {

			@Override
			public boolean accept(CoinsLog s) {
				return s.getType() == CoinsLogType.COINS_RULE;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(CoinsLog s, VCoinsLog d) {
				return s.getRuleCode();
			}

			@Override
			public void setValue(CoinsLog s, VCoinsLog d, CoinsRule value) {
				// 单份作业完成率达%s以上/作业正确率高于%s
				if (value.getAction() == CoinsAction.TEA_HOMEOWORK_RESULT
						|| value.getAction() == CoinsAction.STU_HOMEWORK_RESULT) {
					if (s.getCoinsValue() == 10) {
						d.setDescription(String.format(value.getDescription(), "80%"));
					}
					if (s.getCoinsValue() == 20) {
						d.setDescription(String.format(value.getDescription(), "90%"));
					}
					if (s.getCoinsValue() == 30) {
						d.setDescription(String.format(value.getDescription(), "100%"));
					}
				} else {
					d.setDescription(value.getDescription());
				}
			}

			@Override
			public CoinsRule getValue(Integer key) {
				return coinsRuleService.getByCode(key);
			}

			@Override
			public Map<Integer, CoinsRule> mgetValue(Collection<Integer> keys) {
				return coinsRuleService.mgetCoinsRule(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VCoinsLog, CoinsLog, Integer, UserTask>() {
			@Override
			public boolean accept(CoinsLog coinsLog) {
				return coinsLog.getType() == CoinsLogType.USER_TASK;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(CoinsLog coinsLog, VCoinsLog vCoinsLog) {
				return coinsLog.getRuleCode();
			}

			@Override
			public void setValue(CoinsLog coinsLog, VCoinsLog vCoinsLog, UserTask value) {
				if (null != value) {
					vCoinsLog.setDescription(value.getName());
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
