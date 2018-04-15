package com.lanking.uxb.service.diagnostic.api.teacher;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 班级-教材维度统计服务.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
public interface TaskDiagnosticClassService {

	/**
	 * 统计班级教材相关做题数据.
	 * 
	 * @param hkQuestion
	 *            所有题目
	 * @param textbookCategoryCode
	 *            教师教材版本
	 * @param questionKnowledgeMap
	 *            题目知识点对应关系（已经根据教师教材版本过滤过的）
	 * @param knowledgeTextbookMap
	 *            知识点对应教材（已经根据教师教材版本过滤过的）
	 * @param classId
	 *            班级ID
	 * @param reduce
	 *            是否为减量
	 */
	@SuppressWarnings("rawtypes")
	void doDiagnosticClassStat(List<Map> hkQuestion, int textbookCategoryCode,
			Map<Long, List<Long>> questionKnowledgeMap, Map<Long, Set<Integer>> knowledgeTextbookMap, long classId,
			boolean reduce);

	/**
	 * ClassTextbook 重新统计.
	 * 
	 * @param textbookCategoryCode
	 *            当前教师版本
	 * @param classId
	 *            班级
	 */
	void doClassTextbookStat(long classId);
}
