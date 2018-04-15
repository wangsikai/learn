package com.lanking.uxb.rescon.basedata.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointForm;

/**
 * 考点.
 * 
 * @author wlche
 * @since v2.0.1
 */
public interface ResconExaminationPointService {

	/**
	 * 考点集合.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * 
	 */
	List<ExaminationPoint> list(Integer phaseCode, Integer subjectCode, Status status);

	/**
	 * 获取考点列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param pcode
	 * @return
	 */
	List<ExaminationPoint> findPoint(Integer phaseCode, Integer subjectCode, Long pcode, Status status);

	/**
	 * 通过第二层小专项code获取对应的考点集合
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param knowpointCode
	 *            第二层专项code
	 * @return
	 */
	List<ExaminationPoint> listBySmallSpecailCode(Integer phaseCode, Integer subjectCode, Long knowpointCode);

	/**
	 * 查询已启用的考点
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @return 查询出的数据
	 */
	List<ExaminationPoint> listUse(Integer phaseCode, Integer subjectCode);

	/**
	 * 根据知识体系-知识专项获取考点集合.
	 * 
	 * @return Map<Long, List<ExaminationPoint>>
	 */
	Map<Long, List<ExaminationPoint>> queryBySpecialKnowledgePoints(Integer phaseCode, Integer subjectCode);

	/**
	 * 获取考点
	 * 
	 * @param id
	 * @return
	 */
	ExaminationPoint get(long id);

	/**
	 * 获取当前考点对应的目录层级描述<br>
	 * 例:数与式 > 方程与不等式 > 解一元一次方程 > 解一元一次方程
	 * 
	 * @param code
	 * @return
	 */
	String getLevelDesc(long code);

	/**
	 * 保存前台考点.
	 * 
	 * @param form
	 *            考点表单.
	 */
	void save(ResconExaminationPointForm form);

	/**
	 * 保存状态.
	 * 
	 * @param id
	 *            考点ID.
	 * @param status
	 *            状态
	 * @return 需要更新的题目列表
	 */
	List<Long> saveStatus(Long id, Status status);

	/**
	 * 保存状态.
	 *
	 * @return 需要更新的题目列表
	 */
	List<Long> saveAll();

	/**
	 * 删除考点.
	 * 
	 * @param id
	 *            考点ID.
	 */
	void delete(Long id);

	/**
	 * 增加考点例题.
	 * 
	 * @param id
	 *            考点ID.
	 * @param questionId
	 *            例题ID
	 */
	void addQuestion(Long id, Long questionId);

	/**
	 * 删除考点例题.
	 * 
	 * @param id
	 *            考点ID.
	 * @param questionId
	 *            例题ID
	 */
	void deleteQuestion(Long id, Long questionId);

	/**
	 * 通过考点获取例题集合
	 * 
	 * @param examIds
	 *            考点
	 * @return
	 */
	List<Long> queryQuestionsByExamIds(List<Long> examIds);

	/**
	 * 保存习题.
	 * 
	 * @param id
	 *            考点
	 * @param questionIds
	 *            习题ID集合
	 */
	void saveQuestions(Long id, List<Long> questionIds);

	/**
	 * 获取统计数据.
	 * 
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科
	 * @return
	 */
	Map<String, Integer> queryCounts(Integer phaseCode, Integer subjectCode);

	/**
	 * 更新引用考点的习题索引.
	 * 
	 * @param id
	 *            考点Id
	 */
	void asyncUpdateQuestionIndexByExaminationPoint(long id);
}
