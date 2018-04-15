package com.lanking.uxb.service.diagnostic.api.teacher;

import java.util.List;
import java.util.Map;

/**
 * 班级-知识点维度统计服务.
 * 
 * @author wlche
 * 
 * @since 教师端 v1.3.0，2017-6-30 数据跟随相关整理
 *
 */
public interface TaskDiagnosticClassKnowpointService {

	/**
	 * 统计班级知识点维度数据.
	 * 
	 * @param homeworkClassId
	 *            班级ID
	 * @param hkQuestion
	 *            习题数据集合
	 * @param questionKnowledgeMap
	 *            习题知识点对应关系（已经根据教师教材版本过滤过的）
	 * @param reduce
	 *            是否是减量
	 */
	void doKnowledgeStat(Long homeworkClassId, @SuppressWarnings("rawtypes") List<Map> hkQuestion,
			Map<Long, List<Long>> questionKnowledgeMap, boolean reduce);

	/**
	 * 判断是否是初次运行
	 *
	 * @return true表示有数据 false 表示无数据
	 */
	boolean hasClassKpData();
}
