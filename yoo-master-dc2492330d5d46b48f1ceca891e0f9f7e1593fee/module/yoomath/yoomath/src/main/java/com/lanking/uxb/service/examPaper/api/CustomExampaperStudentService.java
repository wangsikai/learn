package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudent;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudentQuestion;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuery;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuestionStatisticForm;

/**
 * 学生组卷服务
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface CustomExampaperStudentService {

	/**
	 * 开卷
	 *
	 * @param homeworkStudentClazzs
	 *            学生id列表
	 * @param paperId
	 *            试卷id
	 */
	void open(Collection<HomeworkStudentClazz> homeworkStudentClazzs, long paperId);

	/**
	 * 分页查询学生组卷.
	 * 
	 * @param query
	 *            查询参数.
	 * @param pageable
	 * @return
	 */
	Page<CustomExampaperStudent> query(CustomExampaperStudentQuery query, Pageable pageable);

	/**
	 * 获得未读组卷数量.
	 * 
	 * @param studentID
	 *            学生ID
	 * @param timestamp
	 *            截止时间戳
	 * @return
	 */
	long countNewDatas(long studentID, long timestamp);

	/**
	 * 通过学生组卷ID找到组卷题型分类集合.
	 * 
	 * @param customExampaperStudentID
	 *            学生组卷ID.
	 * @return
	 */
	List<CustomExampaperTopic> findCustomExampaperTopicByStudent(long customExampaperStudentID);

	/**
	 * 获得学生组卷.
	 * 
	 * @param id
	 *            组卷ID
	 * @return
	 */
	CustomExampaperStudent get(long id);

	/**
	 * 根据学生组卷ID获得学生习题
	 * 
	 * @param customExampaperStudentID
	 *            学生组卷ID
	 * @return
	 */
	List<CustomExampaperStudentQuestion> listStudentPaperQuestionByCustomExampaperStudentID(
			long customExampaperStudentID);

	/**
	 * 根据学生组卷ID获得组卷习题
	 * 
	 * @param customExampaperStudentID
	 * @return
	 */
	List<CustomExampaperQuestion> listPaperQuestionByCustomExampaperStudentID(long customExampaperStudentID);

	/**
	 * 统计学生组卷.
	 * 
	 * @param customExampaperStudentId
	 *            学生组卷ID
	 * @param statistics
	 *            统计数据
	 */
	void submitStatistic(Long customExampaperStudentId, List<CustomExampaperStudentQuestionStatisticForm> statistics);
}
