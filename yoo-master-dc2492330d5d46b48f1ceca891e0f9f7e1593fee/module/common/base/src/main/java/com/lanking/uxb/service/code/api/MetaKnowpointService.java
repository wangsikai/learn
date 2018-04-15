package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;

/**
 * 提供元知识点相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface MetaKnowpointService {

	MetaKnowpoint get(Integer code);

	Map<Integer, MetaKnowpoint> mget(Collection<Integer> codes);

	List<MetaKnowpoint> mgetList(Collection<Integer> codes);

	/**
	 * 根据科目查询元知识点.
	 * 
	 * @param subjectCode
	 * @param key
	 * @return
	 */
	List<MetaKnowpoint> listBySubject(int subjectCode, String key);

	/**
	 * 根据科目查询元知识点.
	 * 
	 * @param subjectCode
	 * @return
	 */
	List<Map> listBySubject2(int subjectCode);

	/**
	 * 根据知识点查询元知识点
	 *
	 * @param knowPointCode
	 *            知识点代码
	 * @return 元知识点
	 */
	List<MetaKnowpoint> listByKnowPoint(Integer knowPointCode);
}
