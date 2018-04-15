package com.lanking.uxb.service.code.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.MetaKnowKnow;

/**
 * 提供元知识点和知识点关系的相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月15日
 */
public interface MetaKnowKnowService {

	/**
	 * 根据知识点code获取MetaKnowKnow
	 * 
	 * @param code
	 *            知识点code
	 * @return MetaKnowKnow集合
	 */
	List<MetaKnowKnow> findByKnowpoint(int code);

	/**
	 * 根据元知识点code获取MetaKnowKnow
	 * 
	 * @param code
	 *            元知识点code
	 * @return MetaKnowKnow集合
	 */
	List<MetaKnowKnow> findByMetaKnowpoint(int code);

	void save(MetaKnowKnow metaKnowKnow);
}
