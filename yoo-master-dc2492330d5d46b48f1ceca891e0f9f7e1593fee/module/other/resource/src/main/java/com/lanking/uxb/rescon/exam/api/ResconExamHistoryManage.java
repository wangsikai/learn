package com.lanking.uxb.rescon.exam.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperHistory.OperateType;

/**
 * 提供试卷操作历史相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月21日 下午3:56:14
 */
public interface ResconExamHistoryManage {

	/**
	 * 获取试卷操作状态的数据集
	 * 
	 * @param examId
	 *            试卷ID
	 * @param operateTypes
	 *            操作者ID
	 * @param limit
	 *            限制
	 * @return
	 */
	List<ExamPaperHistory> getByOperate(Long examId, List<OperateType> operateTypes, Integer limit);

	/**
	 * 获取试卷所有某个状态的操作者的Id(批量)
	 * 
	 * @param examIds
	 *            试卷IDs
	 * @return
	 */
	Map<Long, List<ExamPaperHistory>> mgetByOperate(List<Long> examIds, List<OperateType> arrayList);

	/**
	 * 保存操作 记录操作
	 * 
	 * @param userId
	 *            用户Id
	 * @param examId
	 *            试卷Id
	 * @param edit
	 *            编辑
	 * 
	 */
	void save(long userId, Long examId, OperateType type);

}
