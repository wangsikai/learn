package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 知识点相关接口
 * 
 * @author wangsenhao
 * @since 2.0.1
 */
public interface ResconKnowledgePointService {

	/**
	 * 获取知识点列表
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @param pcode
	 * @return
	 */
	List<KnowledgePoint> findPoint(Integer phaseCode, Integer subjectCode, Long pcode, Status status);

	/**
	 * 获取子知识点总数
	 * 
	 * @param phaseCode
	 * @param subjectCode
	 * @return
	 */
	Long getPointCount(Integer phaseCode, Integer subjectCode);

	/**
	 * 获取知识点
	 * 
	 * @param code
	 * @return
	 */
	KnowledgePoint get(long code);

	/**
	 * 获取当前子知识点对应的目录层级描述<br>
	 * 例:数与式 > 方程与不等式 > 解一元一次方程 > 解一元一次方程
	 * 
	 * @param code
	 * @return
	 */
	String getLevelDesc(long code);

	/**
	 * 获取小专项对应的目录
	 * 
	 * @param knowCode
	 * @return
	 */
	String getSmallSpecialCataLog(long knowCode);

	/**
	 * 获取所有的知识点
	 * 
	 * @return
	 */
	List<KnowledgePoint> findAll();

	/**
	 * 获取所有的知识点
	 * 
	 * @return
	 */
	List<KnowledgePoint> findAllByStatus(Integer phaseCode, Integer subjectCode, Status status);

	/**
	 * 获取小专项下面所有的知识点
	 * 
	 * @return
	 */
	List<KnowledgePoint> findAll(Integer phaseCode, Integer subjectCode, Long knowpointCode);

	/**
	 * 批量获取知识点.
	 * 
	 * @param ids
	 *            知识点ID集合
	 * @return
	 */
	List<KnowledgePoint> mgetList(List<Long> ids);

	/**
	 * 批量获取知识点.
	 * 
	 * @param ids
	 *            知识点ID集合
	 * @return
	 */
	Map<Long, KnowledgePoint> mget(Collection<Long> ids);

	/**
	 * 模糊匹配
	 * 
	 * @param code
	 * @return
	 */
	List<KnowledgePoint> findByCode(Long code);
}
