package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.uxb.rescon.basedata.form.ResconKnowledgePointCardForm;

/**
 * (新)知识点卡片接口
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
public interface ResconKnowledgePointCardService {

	/**
	 * 保存知识点卡片
	 *
	 * @param form
	 *            {@link ResconKnowledgePointCardForm}
	 * @param userId
	 *            操作人id
	 * @return 变动的知识卡片题目id列表
	 */
	List<Long> save(ResconKnowledgePointCardForm form, long userId);

	/**
	 * 根据知识点代码得到相应的知识点卡片
	 *
	 * @param code
	 *            知识点代码
	 * @return {@link List}
	 */
	List<KnowledgePointCard> findByKnowpointCode(long code);

	/**
	 * 根据知识点和校验状态查找数据
	 *
	 * @param code
	 *            知识点代码
	 * @param cardStatus
	 *            校验状态
	 * @return {@link List}
	 */
	List<KnowledgePointCard> findByKnowpointCode(long code, CardStatus cardStatus);

	/**
	 * 根据多个知识点查找知识卡片
	 *
	 * @param codes
	 *            知识点列表
	 * @param cardStatus
	 *            卡片较验状态
	 * @return {@link List}
	 */
	List<KnowledgePointCard> findByKnowpointCode(Collection<Long> codes, CardStatus cardStatus);

	/**
	 * 获得知识卡片
	 *
	 * @param id
	 *            id
	 * @return {@link KnowledgePointCard}
	 */
	KnowledgePointCard get(long id);

	/**
	 * 启用
	 *
	 * @param id
	 *            知识卡片id
	 * @return 启用后需要同步索引的题目
	 */
	List<Long> use(long id);

	/**
	 * 禁用
	 *
	 * @param id
	 *            id
	 * @return 禁用后需要同步索引的题目
	 */
	List<Long> forbid(long id);

	/**
	 * 删除知识卡片
	 *
	 * @param id
	 *            id
	 */
	void delete(long id);

	/**
	 * 更新知识点卡片状态
	 * 
	 * @param id
	 *            id
	 * @param status
	 *            状态
	 * @return 需要同步索相的题目
	 */
	List<Long> updateCardStatus(long id, CardStatus status);

	/**
	 * 学科代码
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return 查询题目数量
	 */
	long questionCount(int subjectCode);

	/**
	 * 各种状态题目数量统计
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return {@link Map}
	 */
	Map<CardStatus, Long> statusCount(int subjectCode);

}
