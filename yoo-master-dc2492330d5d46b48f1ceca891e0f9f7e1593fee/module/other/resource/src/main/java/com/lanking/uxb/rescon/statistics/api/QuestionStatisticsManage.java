package com.lanking.uxb.rescon.statistics.api;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.uxb.rescon.statistics.form.QuestionStatisForm;
import com.lanking.uxb.rescon.statistics.value.VQuestionStatis;
import com.lanking.uxb.rescon.statistics.value.VStatisKnowpoint;

public interface QuestionStatisticsManage {
	/**
	 * 获取对应章节的问题集合
	 * 
	 * @param list
	 * @return
	 */
	List<Map> getQuestionBySections(List<Long> list, int version);

	/**
	 * 获取各章节对应的统计数据
	 * 
	 * @param sectionList
	 *            章节列表
	 * @param questionList
	 *            问题列表
	 * @return
	 */
	List<VQuestionStatis> getSectionStatis(List<Section> sectionList, List<Map> questionList);

	Map<String, Map<Long, Integer>> getKnowPoint(QuestionStatisForm form);

	List<Section> findByTextbookCodeAndName(int textBookCode, String name);

	/**
	 * 通过科目,关键字查询对应知识点
	 * 
	 * @param p
	 * @param form
	 * @return
	 */
	List<Knowpoint> listBySubjectAndKey(QuestionStatisForm form);

	/**
	 * 通过科目,关键字查询对应新知识点
	 * 
	 * @param p
	 * @param form
	 * @return
	 */
	List<KnowledgeSystem> newlistBySubjectAndKey(QuestionStatisForm form);

	/**
	 * 查询出章节对应的统计
	 * 
	 * @param form
	 * @return
	 */
	List<VQuestionStatis> querySectionStatis(QuestionStatisForm form);

	/**
	 * 导出按知识点统计
	 * 
	 * @param form
	 * @return
	 */
	List<VStatisKnowpoint> queryKnowpointStatis(QuestionStatisForm form);

	/**
	 * 导出按章节统计
	 * 
	 * @param list
	 * @return
	 */
	HSSFWorkbook exportBySection(List<VQuestionStatis> list);

	/**
	 * 导出按知识点统计
	 * 
	 * @param list
	 * @return
	 */
	HSSFWorkbook exportByKnowpoint(List<VStatisKnowpoint> list);

}
