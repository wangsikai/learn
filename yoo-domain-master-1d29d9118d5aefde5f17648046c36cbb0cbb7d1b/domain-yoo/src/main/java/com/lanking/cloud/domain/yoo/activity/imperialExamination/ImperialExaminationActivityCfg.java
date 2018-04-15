package com.lanking.cloud.domain.yoo.activity.imperialExamination;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

import lombok.Getter;
import lombok.Setter;

/**
 * 科举活动配置
 * 
 * <pre>
 * 存储活动的相关配置及规则:
 * 1.代码(必须)
 * 2.考场
 * 3.可以参与的版本
 * 4.支持的年级
 * 5.阶段流程时间对象
 * 6.消息发送时间列表
 * 
 * 7.奖品列表（一期中使用的）
 * 8.评比标准（一期中使用的）
 * 
 * </pre>
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 */
@Setter
@Getter
public final class ImperialExaminationActivityCfg implements Serializable {

	private static final long serialVersionUID = -8522413327981435771L;

	/**
	 * 活动code
	 */
	private Integer code;

	/**
	 * 考场列表（包含了考场的个性化配置）
	 */
	private List<ImperialExaminationRoom> rooms;

	/**
	 * 支持的所有版本
	 */
	private List<Integer> textbookCategoryCodes;

	/**
	 * 支持的年级
	 */
	private List<ImperialExaminationGrade> grades;

	/**
	 * 阶段流程时间对象
	 */
	private List<ImperialExaminationProcessTime> timeList;

	/**
	 * 消息发送时间列表
	 */
	private List<ImperialExaminationMessageTime> messageList;

	/**
	 * 奖品列表
	 */
	@Deprecated
	private List<ImperialExaminationAwardType> awardList;

	/**
	 * 评比标准
	 */
	@Deprecated
	private List<ImperialExaminationCompositeIndex> indexList;

	@JSONField(serialize = false)
	public ImperialExaminationProcessTime getCurretStage() {
		Date nowTime = new Date();
		for (ImperialExaminationProcessTime t : timeList) {
			if (t.getEndTime() != null && t.getStartTime() != null) {
				if (nowTime.before(t.getEndTime()) && nowTime.after(t.getStartTime())) {
					return t;
				}
			}
		}
		return null;
	}
}
