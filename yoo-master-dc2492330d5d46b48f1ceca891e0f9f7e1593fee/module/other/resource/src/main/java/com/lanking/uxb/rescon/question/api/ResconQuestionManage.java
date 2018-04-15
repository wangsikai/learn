package com.lanking.uxb.rescon.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.AsciiStatus;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.form.CheckForm;
import com.lanking.uxb.rescon.question.form.QuestionForm;
import com.lanking.uxb.rescon.question.value.VAnswer;

/**
 * 习题服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年3月4日
 */
public interface ResconQuestionManage {

	/**
	 * 获取一个习题
	 * 
	 * @param id
	 *            习题ID
	 * @return 习题对象
	 */
	Question get(long id);

	/**
	 * 获取子题
	 * 
	 * @param id
	 *            父题ID
	 * @return 子题集合
	 */
	List<Question> getSubQuestions(long id);

	/**
	 * 批量获取习题
	 * 
	 * @param id
	 *            习题IDs
	 * @return 习题对象
	 */
	Map<Long, Question> mget(Collection<Long> ids);

	/**
	 * 批量获取习题
	 * 
	 * @param id
	 *            习题IDs
	 * @return 习题对象
	 */
	List<Question> mgetList(Collection<Long> ids);

	/**
	 * 创建题目.
	 * 
	 * @param form
	 *            题目表单
	 * @param vendorUser
	 *            创建人
	 * @return Map 数据中包含题目和原题目状态
	 * @throws ResourceConsoleException
	 */
	Map<String, Object> saveQuestion(QuestionForm form, VendorUser vendorUser) throws ResourceConsoleException;

	/**
	 * 删除习题.
	 * 
	 * @param questionId
	 * @throws ResourceConsoleException
	 */
	void delete(long questionId) throws ResourceConsoleException;

	/**
	 * 保存习题编号.
	 * 
	 * @param questionId
	 * @param code
	 * @throws ResourceConsoleException
	 */
	void saveCode(long questionId, String code) throws ResourceConsoleException;

	/**
	 * 批量保存习题编号.
	 * 
	 * @param codeMap
	 * @throws ResourceConsoleException
	 */
	void saveCode(Map<Long, String> codeMap) throws ResourceConsoleException;

	/**
	 * 获得最大编号.
	 * 
	 * @param prefix
	 * @return
	 */
	public String getMaxCode(String prefix);

	/**
	 * 搜索无编号题目.
	 * 
	 * @param query
	 * @param pageable
	 * @return
	 */
	Page<Question> queryNoCodeQuestions(Pageable pageable);

	/**
	 * 不同状态的题目个数
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	List<Map> getCountByCheckStaus(Long vendorId);

	/**
	 * 获取校验题目.
	 * 
	 * @param vendorUserId
	 *            校验员ID
	 * @param vendorId
	 *            供应商ID
	 * @param hasCheckOne
	 *            是否有一校权限
	 * @param hasCheckTwo
	 *            是否有二校权限
	 * @param questionIds
	 *            需要排除的题目
	 * @param checkForm
	 *            校验题目限定条件
	 * @return
	 */
	Question getCheckQuestions(Long vendorUserId, Long vendorId, boolean hasCheckOne, boolean hasCheckTwo,
			Collection<Long> questionIds, CheckForm checkForm, Pageable pageable);

	/**
	 * 更新校验题目时的题目状态.
	 * 
	 * @param questionId
	 * @param status
	 */
	void updateDoCheckStatus(long questionId, CheckStatus status, Long vendorUserId, String nopassContent,
			List<Long> nopassImages);

	/**
	 * 打回重新校验.
	 * 
	 * @param questionId
	 */
	void backCheckQuestion(long questionId);

	/**
	 * 批量获取题目子题
	 * 
	 * @since yoomathV1.2
	 * @param ids
	 *            父题IDs
	 * @return Map
	 */
	Map<Long, List<Question>> mgetSubQuestions(Collection<Long> ids);

	/**
	 * 根据题目code 找习题
	 * 
	 * @param qCodes
	 * @param status
	 *            题目状态
	 * @return 题目list
	 */
	List<Question> findQuestionByCode(List<String> qCodes, Integer status);

	/**
	 * 判断是否对整本书完成了 通过校验.
	 * 
	 * @param bookId
	 *            书本的ID
	 * @return
	 */
	boolean checkBookComplete(Long bookId);

	/**
	 * 判断是否对整个试卷完成了 通过校验.
	 * 
	 * @param paperId
	 *            试卷的ID
	 * @return
	 */
	boolean checkPaperComplete(Long paperId);

	/**
	 * 判断是否对整个训练完成了 通过校验
	 * 
	 * @param trainId
	 *            训练ID
	 * @return
	 */
	boolean checkTrainComplete(Long trainId);

	/**
	 * 保存题目的学校.
	 * 
	 * @param schoolId
	 *            学校ID
	 */
	void saveQuestionSchool(long questionId, long schoolId);

	/**
	 * 更新学校题目统计.
	 * 
	 * @param schoolId
	 *            学校ID
	 * @param num
	 *            数量
	 */
	void updateQuestionSchoolCount(long schoolId, int num);

	/**
	 * 回去需要转换的题目（只可能是填空题以及复合含有填空题的题目）
	 * 
	 * @param status
	 *            题目状态
	 * @param questionId
	 *            不包含的题目ID（已经被查询过且没有释放）
	 * @return
	 */
	Question getConvertQuestion(AsciiStatus status, List<Long> questionId);

	/**
	 * 获取已经完成转化和转化玩但需要校验的题目数量
	 * 
	 * @return
	 */
	Map<String, Long> getConvertQuestionCount();

	/**
	 * 校验通过习题答案.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param answers
	 *            答案集合
	 * @param checkFlag
	 *            是否为校验
	 */
	void checkAnswer(long questionId, List<VAnswer> answers, boolean checkFlag);

	/**
	 * 保存习题新知识点.
	 * 
	 * @param questionId
	 *            习题Id
	 * @param knowledgePoints
	 *            知识点集合
	 * @throws ResourceConsoleException
	 */
	void saveQuestionKnowledgePoint(Long questionId, List<Long> knowledgePoints) throws ResourceConsoleException;

	/**
	 * 保存习题新考点.
	 * 
	 * @param questionId
	 *            习题Id
	 * @param examinationPoints
	 *            考点集合
	 * @throws ResourceConsoleException
	 */
	void saveQuestionExaminationPoint(Long questionId, List<Long> examinationPoints) throws ResourceConsoleException;

	/**
	 * 更新习题新知识点属性.
	 * 
	 * @param questionId
	 *            习题Id
	 * @param vendorUserId
	 *            操作用户ID.
	 * @throws ResourceConsoleException
	 */
	void updateKnowledgeCreator(Long questionId, Long vendorUserId) throws ResourceConsoleException;

	/**
	 * 获得新旧知识点页面统计.
	 * 
	 * @param vendorId
	 *            供应商ID
	 * @param vendorUserId
	 *            操作用户ID.
	 * @param phaseCode
	 *            阶段
	 * @return
	 */
	Map<String, Integer> calNewKnowledgeDatas(Long vendorId, Long vendorUserId, int phaseCode);

	/**
	 * 获取旧知识点下未处理题目个数（第一级）.
	 * 
	 * @param subjectCode
	 *            学科阶段
	 * @param vendorId
	 *            供应商
	 * @return
	 */
	Map<Integer, Integer> calNoKnowledgeL1(int subjectCode, long vendorId);

	/**
	 * 获取新知识点已未处理题目个数（第一级）.
	 * 
	 * @param subjectCode
	 *            学科阶段
	 * @param vendorId
	 *            供应商
	 * @return
	 */
	Map<Integer, Integer> calHasKnowledgeL1(int subjectCode, long vendorId);

	/**
	 * 保存校验时的相关题目数据.
	 * 
	 * @param questionId
	 *            题目ID
	 * @param checkKnowledgePoints
	 *            人工选取知识点
	 * @param allKnowledgePoints
	 *            合并知识点
	 * @param sysKnowledgePoints
	 *            系统知识点
	 * @param checkKnowledgeSyncs
	 *            同步知识点
	 * @param checkKnowledgeReviews
	 *            复习知识点
	 * @param vendorUserId
	 *            更人操作员
	 */
	void saveCheckQuestionDatas(long questionId, Double difficulty, List<Long> checkKnowledgePoints,
			List<Long> allKnowledgePoints, List<Long> sysKnowledgePoints, List<Long> checkKnowledgeSyncs,
			List<Long> checkKnowledgeReviews, long vendorUserId);

	/**
	 * 相似题习题拷贝（校验使用）
	 * 
	 * @param question
	 *            原习题
	 * @param similarQuestion
	 *            相似题
	 * @param operator
	 *            当前操作校验员
	 * @return
	 */
	Question copySimilarQuestion(Question question, Question similarQuestion, long operator);

	/**
	 * 打回一校.
	 * 
	 * @param questionId
	 *            习题
	 * @param nopassContent
	 *            未通过理由
	 * @param nopassImages
	 *            未通过图片
	 * @param vendorUserId
	 *            操作用户
	 */
	void checkRefund(Long questionId, String nopassContent, Long[] nopassImages, long vendorUserId);

	/**
	 * 保存题目分类.
	 * 
	 * @param questionId
	 * @param questionCategorys
	 * @throws ResourceConsoleException
	 */
	void saveQuestionCategorys(Long questionId, List<Long> questionCategorys) throws ResourceConsoleException;

	/**
	 * 保存习题标签.
	 * 
	 * @param questionId
	 * @param questionTags
	 * @throws ResourceConsoleException
	 */
	void saveQuestionTags(Long questionId, List<Long> questionTags) throws ResourceConsoleException;

	/**
	 * 查询需要重建习题章节的习题列表
	 * 
	 * @param pageable
	 * @return
	 */
	Page<Question> queryReQuestionSection(Pageable pageable);

	/**
	 * 保存需要重建习题章节的习题列表
	 * 
	 * @param questions
	 */
	void saveReQuestionSection(List<Question> questions);

	/**
	 * 校验题目是否符合katex规则用接口,只更新指定内容
	 * 
	 * @param question
	 *            旧题目
	 * @param form
	 *            更新content,answer,hint,analysis
	 * @param vendorUser
	 *            没有值的话不更新questions表的update_id
	 */
	Question updateQuestionByCheckKatex(QuestionForm form, VendorUser vendorUser);
}
