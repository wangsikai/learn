package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Knowpoint;

/**
 * 提供知识点相关接口
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年1月14日
 */
public interface KnowpointService {

	Knowpoint get(Integer code);

	Map<Integer, Knowpoint> mget(Collection<Integer> codes);

	List<Knowpoint> mgetList(Collection<Integer> codes);

	List<Knowpoint> findByPcode(int pcode);

	Knowpoint getParent(int code);

	List<Knowpoint> listBySubject(Integer subjectCode);

	/**
	 * 获取所有知识点列表（包括以knowpoint形式返回关联的metaknowpoint,无层次结构）
	 * 
	 * @param subjectCode
	 *            学科代码
	 * @return 知识点集合
	 */
	List<Knowpoint> listAllBySubject(Integer subjectCode);
}
