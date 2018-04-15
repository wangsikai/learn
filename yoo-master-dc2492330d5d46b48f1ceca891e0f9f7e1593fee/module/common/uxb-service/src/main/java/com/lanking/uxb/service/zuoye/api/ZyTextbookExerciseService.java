package com.lanking.uxb.service.zuoye.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseQuestion;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseType;

/**
 * 教材习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月15日
 */
public interface ZyTextbookExerciseService {

	/**
	 * 通过教材代码获取预置练习
	 * 
	 * @since 2.1
	 * @param textbookCode
	 *            教材版本
	 * @return 预置习题
	 */
	List<TextbookExercise> findByTextbook(int textbookCode);

	/**
	 * 通过预置练习ID拉取题目列表
	 * 
	 * @since 2.1
	 * @param textbookExerciseId
	 *            预置练习ID
	 * @return List
	 */
	List<TextbookExerciseQuestion> listQuestions(long textbookExerciseId);

	/**
	 * 获取单个预置练习
	 * 
	 * @since 2.1
	 * @param id
	 *            ID
	 * @return {@link TextbookExercise}
	 */
	TextbookExercise get(long id);

	/**
	 * 根据章节Code 获取章节下的练习题列表
	 * 
	 * @param sectionCodes
	 * @return
	 */
	List<TextbookExercise> getTbeListBySectioCode(Long sectionCode, TextbookExerciseType type);

	/**
	 * 创建textbookexercicse，并根据qIDs预置习题
	 * 
	 * @param textbookCode
	 *            textbookCode
	 * @param sectionCode
	 *            章节code
	 * @param userId
	 *            创建人
	 * @param name
	 *            章节名称
	 * @param isdefalut
	 *            是否是默认
	 * @param qIds
	 *            题目ID集合
	 * @return
	 */
	TextbookExercise create(Integer textbookCode, Long sectionCode, long userId, String name, boolean isDefalut,
			List<Long> qIds);
}
