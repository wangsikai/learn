package com.lanking.uxb.rescon.basedata.api;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.ExaminationPointCard;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointCardForm;

import java.util.Map;

/**
 * 考点卡片Service
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
public interface ResconExaminationPointCardService {
	/**
	 * 保存考点卡片
	 *
	 * @param form
	 *            {@link ResconExaminationPointCardForm}
	 * @param userId
	 *            操作人id
	 * @return {@link ExaminationPointCard}
	 */
	ExaminationPointCard save(ResconExaminationPointCardForm form, long userId);

	/**
	 * 根据考点id得到知识卡片(2.0.1设计一个考点只可以对应一张卡片)
	 *
	 * @param epId
	 *            考点id
	 * @return {@link ExaminationPointCard}
	 */
	ExaminationPointCard findByExaminationPoint(long epId);

	/**
	 * 获取考点卡片
	 * 
	 * @param id
	 * @return
	 */
	ExaminationPointCard get(long id);

	/**
	 * 更新考点卡片状态
	 * 
	 * @param id
	 * @param status
	 */
	void updateCardStatus(long id, CardStatus status);

	/**
	 * 考点例题数量
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return 例题数量
	 */
	long questionCount(int subjectCode);

	/**
	 * 各种状态题目数量统计
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return {@link Map}
	 */
	Map<CardStatus, Long> statusCount(int subjectCode);
}
