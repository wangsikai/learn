package com.lanking.uxb.service.question.api;

import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.stat.KnowledgeQuestionStat;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

/**
 * 知识点统计
 * 
 * @since 2.6.0
 * @author zemin.song
 *
 */
public interface KnowledgeQuestionCountStatService {

	/**
	 * 获取知识点统计数据
	 */
	List<Map> getCountData(List<Integer> codes);

	/**
	 * 获取新知识点统计数据
	 * 
	 */
	List<Map> getCountNewData(List<Long> codes);

	/**
	 * 存储知识点统计数据
	 * 
	 * @param kqsList
	 */
	void saveKnowledgeQuestionStat(List<KnowledgeQuestionStat> kqsList);

	/**
	 * 分页游标查询知识点
	 * 
	 * @param pageable
	 * @return
	 */
	CursorPage<Integer, MetaKnowpoint> queryKnowpoint(CursorPageable<Integer> pageable);

	/**
	 * 分页游标查询知识点
	 * 
	 * @param pageable
	 * @return
	 */
	CursorPage<Long, KnowledgePoint> queryKnowledgePoint(CursorPageable<Long> pageable);

	/**
	 * 删除历史统计数据
	 */
	void deleteKnowpoint();

}
