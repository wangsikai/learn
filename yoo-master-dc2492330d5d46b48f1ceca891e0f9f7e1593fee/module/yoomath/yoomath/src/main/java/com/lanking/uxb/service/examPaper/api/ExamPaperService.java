package com.lanking.uxb.service.examPaper.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;

/**
 * 中央资源库！试卷管理相关Service
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
public interface ExamPaperService {

	/**
	 * 获取试卷.
	 * 
	 * @param id
	 * @return
	 */
	ExamPaper get(long id);

	/**
	 * 批量获取试卷.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, ExamPaper> mget(Collection<Long> ids);

	/**
	 * 查询试卷
	 *
	 * @param queryForm
	 *            查询试卷条件form
	 * @return {@link Page}
	 */
	Page<ExamPaper> queryExam(ExamQueryForm queryForm);

	/**
	 * 查询现在试卷库中所存在的区
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科码
	 * @return 地区码列表
	 */
	List<Long> getDistricts(Integer phaseCode, Integer subjectCode);

	/**
	 * 查询现在试卷库中所存在的区(精品试卷组卷列表)
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科码
	 * @return 地区码列表
	 */
	List<Long> getDistrictsByGoods(Integer phaseCode, Integer subjectCode);

	/**
	 * 查询现在试卷库中所存在的区（我的收藏）
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科码
	 * @return 地区码列表
	 */
	List<Long> getDistrictsByFavorite(Integer phaseCode, Integer subjectCode, Long createId);

	/**
	 * 查询现在试卷库中所存在的区（我的收藏）
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科码
	 * @param limit
	 *            分页数目
	 * @param nDay
	 *            试卷点击周期
	 * @return
	 */
	List<ExamPaper> findRecommendByNdayHot(Integer subjectCode, Integer phaseCode, Integer limit, boolean isPutaway,
			int nDay);

	/**
	 * 最新上架试卷
	 *
	 * @param phaseCode
	 *            阶段
	 * @param subjectCode
	 *            学科码
	 * @param limit
	 * @return
	 */
	List<ExamPaper> findNewPaper(Integer subjectCode, Integer phaseCode, Integer limit);

}
