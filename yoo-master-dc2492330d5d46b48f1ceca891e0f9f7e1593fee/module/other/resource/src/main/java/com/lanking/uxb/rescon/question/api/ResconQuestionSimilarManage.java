package com.lanking.uxb.rescon.question.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.question.form.SimilarQuestionsForm;

/**
 * 相似题处理接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月17日
 */
public interface ResconQuestionSimilarManage {

	/**
	 * 获得相似题组.
	 * 
	 * @param id
	 *            题组ID
	 * @return
	 */
	QuestionSimilar getQuestionSimilar(long id);

	/**
	 * 题组设置保存.
	 * 
	 * @param form
	 */
	void saveSimilarQuestions(SimilarQuestionsForm form, long vendorId);

	/**
	 * 相似题组统计数据.
	 * 
	 * @return
	 */
	Map<String, Object> similarCounts(long vendorId);

	/**
	 * 查询某个题目的相似题
	 * 
	 * @param qid
	 *            题目ID.
	 * @param size
	 *            取值个数
	 * @return
	 */
	List<Question> listSimilarQuestionsForWeb(long qid, int size);

	/**
	 * 查询旧库数据（重建相似题库使用）.
	 * 
	 * @param pageable
	 * @return
	 */
	Page<QuestionSimilar> queryOldDatas(Pageable pageable);

	/**
	 * 转换新的数据（重建相似题库使用）.
	 * 
	 * @param oldDatas
	 *            旧数据
	 */
	void buildNewDatas(List<QuestionSimilar> oldDatas);

	/**
	 * 获取指定的题型个数.
	 * 
	 * @param qids
	 *            习题集合
	 * @param types
	 *            题型集合
	 * @return
	 */
	long geyQuestionTypesCount(List<Long> qids, List<Integer> types);
}
