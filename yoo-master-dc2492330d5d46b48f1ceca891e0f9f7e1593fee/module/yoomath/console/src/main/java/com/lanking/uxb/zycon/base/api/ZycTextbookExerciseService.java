package com.lanking.uxb.zycon.base.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExercise;
import com.lanking.cloud.domain.common.resource.textbookExercise.TextbookExerciseQuestion;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 教材习题相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年8月31日 上午11:12:56
 */
public interface ZycTextbookExerciseService {

	/**
	 * 创建textbookexercise
	 * 
	 * @param sectionCode
	 *            章节code
	 * @param textBookCode
	 *            教材code
	 * @param uid
	 *            用户
	 * @param name
	 *            姓名
	 * @param isDefaultExercise
	 *            是否是默认
	 *
	 */
	TextbookExercise add(Integer textBookCode, Long sectionCode, Long uid, String name, Boolean isDefaultExercise);

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
	 * 预置习题
	 * 
	 * @param qIds
	 *            习题IDs 默认ID 已经按从简单到难顺序排好
	 */
	void setQuestions(List<Long> qIds, Long exerciseId);

	/**
	 * 对习题进行禁用、启用、删除操作
	 * 
	 * @param textbookExerciseId
	 * @param status
	 */
	void operateByStaus(long textbookExerciseId, Status status);

	/**
	 * 更新习题
	 * 
	 * @param textbookExerciseId
	 * @param name
	 */
	TextbookExercise updateExercise(long textbookExerciseId, String name);

	/**
	 * 当前章节码此名称是否已经出现过
	 * 
	 * @param sectionCode
	 * @param name
	 * @return
	 */
	Long getExerciseNameCount(Long sectionCode, String name);
}
