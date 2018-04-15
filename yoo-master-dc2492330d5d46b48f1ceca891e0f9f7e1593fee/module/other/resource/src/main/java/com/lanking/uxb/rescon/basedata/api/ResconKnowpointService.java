package com.lanking.uxb.rescon.basedata.api;

import java.util.List;

import com.lanking.cloud.domain.common.baseData.Knowpoint;

/**
 * 知识点Service
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconKnowpointService {

	/**
	 * 查找所有的Knowpoint
	 *
	 * @param pcode
	 *            父code
	 * @param subjectCode
	 *            subject code
	 * @param phaseCode
	 *            phase code
	 * @return Knowpoint
	 */
	List<Knowpoint> find(Integer pcode, Integer subjectCode, Integer phaseCode);

	/**
	 * 同步知识点
	 */
	void syncData();
}
