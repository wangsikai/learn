package com.lanking.uxb.service.question.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 习题WordML预处理服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月4日
 */
public interface QuestionWordMLService {

	/**
	 * 添加缓存.
	 * 
	 * @param question
	 *            习题
	 * @param answers
	 *            答案
	 * @param host
	 *            本地服务地址
	 */
	void asyncAdd(Question question, List<Answer> answers, String host);

	/**
	 * 批量添加缓存.
	 * 
	 * @param questions
	 * @param answers
	 * @param host
	 */
	void batchAdd(List<Question> questions, Map<Long, List<Answer>> answers, String host);

	/**
	 * 获得习题缓存.
	 * 
	 * @param id
	 * @return
	 */
	QuestionWordMLData get(long id);

	/**
	 * 批量获取习题缓存.
	 * 
	 * @param ids
	 * @return
	 */
	Map<Long, QuestionWordMLData> mget(Collection<Long> ids);

	/**
	 * 分页查询库表中的缓存数据.
	 * 
	 * @param pageable
	 * @return
	 */
	Page<QuestionWordMLData> queryAllFromTable(Pageable pageable);
}
