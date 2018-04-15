package com.lanking.uxb.rescon.exam.api;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.domain.type.CheckStatus;

import java.util.Collection;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since 2.0.3
 */
public interface ResconExamPaperStatisticService {
	/**
	 * 统计各阶段各学科的试卷总数
	 *
	 * @return 统计数据
	 */
	Map<String, Map<String, Long>> countStatistic();

	/**
	 * 统计各状态试卷数量
	 *
	 * @param subjectCode
	 *            学科码
	 * @return 学科码对应的数量
	 */
	Map<ExamPaperStatus, Long> countBySubject(int subjectCode);

	/**
	 * 查询一个试卷中所含题状态的数量
	 *
	 * @param id
	 *            试卷id
	 * @return 校验状态对应数量
	 */
	Map<CheckStatus, Integer> countExamQuestionCheckStatusById(long id);

	/**
	 * 查询多个试卷中所含题状态的数量
	 *
	 * @param ids
	 *            试卷id列表
	 * @return 校验状态对应数量
	 */
	Map<Long, Map<CheckStatus, Integer>> countExamQuestionCheckStatusByIds(Collection<Long> ids);
}
