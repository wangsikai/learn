package com.lanking.uxb.service.imperial.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExamination2Question;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;

/**
 * 科举活动预置作业题目接口.
 * 
 * @author peng.zhao
 *
 * @version 2017年11月15日
 */
public interface ImperialExamination2QuestionService {

	/**
	 * 根据指定查询条件查询数据
	 */
	List<ImperialExamination2Question> getQuestions(Long code, ImperialExaminationType type,
			ImperialExaminationGrade grade, Integer textbookCategoryCode, Integer tag);

	/**
	 * 保存数据
	 * 
	 * @param question
	 */
	void save(ImperialExamination2Question question);
}
