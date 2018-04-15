package com.lanking.uxb.rescon.basedata.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;

/**
 * 元知识点Service
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconMetaKnowpointService {

	/**
	 * 查找某个知识点下的所有元知识点
	 *
	 * @param knowPointCode
	 *            知识点的Code
	 * @return 元知识点列表
	 */
	List<MetaKnowpoint> findAll(Integer knowPointCode);

	/**
	 * 同步数据
	 */
	void syncData();

	/**
	 * 根据多个知识点Code，查找到其对应的元知识点
	 *
	 * @param knowpointCodes
	 *            知识点Code列表
	 * @return 元知识点
	 */
	List<MetaKnowpoint> findAll(Collection<Integer> knowpointCodes);
}
