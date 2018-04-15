package com.lanking.uxb.service.diagnostic.api.teacher;

import java.util.List;
import java.util.Map;

/**
 * 班级-最近n次作业薄弱知识点服务.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
public interface TaskDiagnosticClassLatestHomeworkKnowpointService {

	/**
	 * 删除某个班级的最近N次作业薄弱知识点.
	 * 
	 * @param classId
	 */
	void deleteByClassId(long classId);

	/**
	 * 统计最近n次作业知识点相关做题数据.
	 * 
	 * @param homeworkClassId
	 *            班级ID
	 * @param hkQuestion
	 *            习题数据
	 * @param questionKnowledgeMap
	 *            习题知识点对应关系（已经根据教师教材版本过滤过的）
	 * @param times
	 *            最近n次
	 */
	void doKnowledgeStat(long homeworkClassId, @SuppressWarnings("rawtypes") List<Map> hkQuestion,
			Map<Long, List<Long>> questionKnowledgeMap, int times);
}
