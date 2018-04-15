package com.lanking.uxb.service.diagnostic.api;

import java.util.List;
import java.util.Map;

/**
 * 教师学情分析中的知识点相关的统计方法封装
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface StaticDiagnosticKpService {
	/**
	 * 统计知识点的数据
	 *
	 * hkQuestion -> <br/>
	 * right_count: 正确的数量<br/>
	 * wrong_count: 错误的数量<br/>
	 * question_id: 题目id<br/>
	 * difficulty: 题目的难度<br/>
	 *
	 * @param homeworkClassId
	 *            班级id
	 * @param teacherId
	 *            教师id
	 * @param hkQuestion
	 *            作业各题目完成情况数据列表
	 * @param times
	 *            次数
	 */
	void doKnowledgeStat(Long homeworkClassId, long teacherId, List<Map> hkQuestion, boolean init, int times);

	/**
	 * 处理班级知识点相关数据
	 *
	 * @param rightCount
	 *            正确数量
	 * @param doCount
	 *            练习量
	 * @param code
	 *            知识点代码
	 * @param classId
	 *            班级id
	 * @param textbookCode
	 *            教材码
	 */
	void doClassLatestKpStat(Long rightCount, Long doCount, Long code, Long classId, Integer textbookCode, int times);
}
