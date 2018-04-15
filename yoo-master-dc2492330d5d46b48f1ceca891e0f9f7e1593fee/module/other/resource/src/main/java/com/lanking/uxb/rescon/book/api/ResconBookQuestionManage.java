package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.rescon.book.form.BookQuestionQueryForm;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;

/**
 * 书本习题接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月22日
 */
public interface ResconBookQuestionManage {

	/**
	 * 添加已有题目至书本中.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookVersionId
	 *            书本ID
	 * @param bookCatalogId
	 *            目录ID
	 * @param createId
	 *            操作人ID
	 * @throws VBookException
	 */
	void addQuestion(long questionId, long bookVersionId, long bookCatalogId, long createId)
			throws ResourceConsoleException;

	/**
	 * 保存书本题目.
	 * 
	 * @param bookQuestion
	 *            书本题目.
	 * @throws VBookException
	 */
	void addQuestion(BookQuestion bookQuestion) throws ResourceConsoleException;

	/**
	 * 批量添加已有题目至书本中.
	 * 
	 * @param questionIds
	 * @param bookVersionId
	 * @param bookCatalogId
	 * @param createId
	 * @throws VBookException
	 */
	void addQuestions(Collection<BookQuestion> bookQuestions) throws ResourceConsoleException;

	/**
	 * 获取目录中对应的题目.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookCatalogId
	 *            目录ID
	 * @return
	 */
	BookQuestion getQuestionFromCatalog(long questionId, long bookCatalogId);

	/**
	 * 获取书本中对应的题目.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookVersionId
	 *            版本ID
	 * @return
	 */
	BookQuestion getQuestionFromVersion(long questionId, long bookVersionId);

	/**
	 * 移动题目.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookCatalogId
	 *            书本目录ID
	 * @param flag
	 *            移动位置
	 * @throws VBookException
	 */
	void moveQuestion(long questionId, long bookCatalogId, int flag, long createId) throws ResourceConsoleException;

	/**
	 * 移动题目.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookCatalogId
	 *            书本目录ID
	 * @throws VBookException
	 */
	void moveQuestionTo(long bookVersionId, long questionId, long bookCatalogId, long createId)
			throws ResourceConsoleException;

	/**
	 * 删除题目.
	 * 
	 * @param questionId
	 *            习题ID
	 * @param bookCatalogId
	 *            书本目录ID
	 * @throws VBookException
	 */
	void deleteQuestion(long questionId, long bookCatalogId, long createId) throws ResourceConsoleException;

	/**
	 * 判断是否已经包含部分题目.
	 * 
	 * @param bookVersionId
	 *            版本ID
	 * @param questionCodes
	 *            题目集合.
	 * @return 已包含的题目编号
	 */
	List<String> hasQuestion(long bookVersionId, Collection<String> questionCodes);

	/**
	 * 获得某个目录下的最大序号.
	 * 
	 * @param bookCatalogId
	 *            目录ID
	 * @return
	 */
	Integer getMaxSequence(long bookCatalogId);

	/**
	 * 分页获取书本题目.
	 * 
	 * @param form
	 *            查询参数
	 * @param catalogIds
	 *            子章节集合
	 * @return
	 */
	Page<Question> query(BookQuestionQueryForm form, Pageable pageable);

	/**
	 * 获得书本中各个状态的题目数量统计.
	 * 
	 * @param bookVersionId
	 *            书本版本ID.
	 * @return
	 */
	Map<CheckStatus, Integer> getBookCounts(long bookVersionId);

	/**
	 * 获得书本中各个状态的题目数量统计.
	 * 
	 * @param bookVersionIds
	 *            书本版本ID集合
	 * @return List<Map> Map为按照传入书本版本ID顺序的状态统计集合
	 */
	List<Map<CheckStatus, Integer>> getBookCounts(Collection<Long> bookVersionIds);

	/**
	 * 分页获得书本题目集合.
	 * 
	 * @param bookVersionId
	 *            书本版本
	 * @return
	 */
	Page<BookQuestion> query(long bookVersionId, Pageable pageable);

	/**
	 * 根据习题找到对应的书本题目（主要用于草稿习题的搜索）
	 * 
	 * @since rescon v1.2.7
	 * @param questionId
	 * @return
	 */
	List<BookQuestion> queryByQuestionId(long questionId);

	/**
	 * 替换习题.
	 * 
	 * @since rescon v1.3.3
	 * @param bookVersionId
	 *            版本ID
	 * @param oldQuestionId
	 *            旧习题ID
	 * @param newQuestionId
	 *            新习题ID
	 */
	void changeQuestion(long bookVersionId, long oldQuestionId, long newQuestionId);
	
	/**
	 * 获得某个目录下的题目数量.
	 * 
	 * @param bookCatalogId
	 *            目录ID
	 * @return
	 */
	Integer countCatalog(long bookCatalogId);
}
