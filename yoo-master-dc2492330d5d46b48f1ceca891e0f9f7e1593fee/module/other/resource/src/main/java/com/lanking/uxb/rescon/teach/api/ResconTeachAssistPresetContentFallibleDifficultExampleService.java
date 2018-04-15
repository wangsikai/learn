package com.lanking.uxb.rescon.teach.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficultExample;

/**
 * 易错难题例题,需求定的只有一个例题
 * 
 * @author wangsenhao
 *
 */
public interface ResconTeachAssistPresetContentFallibleDifficultExampleService {
	/**
	 * 通过易错难点查询对应的例题,目前需求定的只有一个
	 * 
	 * @param id
	 * @return
	 */
	List<TeachAssistPresetContentFallibleDifficultExample> findListByFallId(Long id);

	/**
	 * 通过易错难点id删除对应例题
	 * 
	 * @param id
	 */
	void delByFallId(Long id);

	/**
	 * 根据多个易错点查询对应的例题数据
	 *
	 * @param ids
	 *            易错点id列表
	 * @return {@link List}
	 */
	List<TeachAssistPresetContentFallibleDifficultExample> findListByFallIds(Collection<Long> ids);
}
