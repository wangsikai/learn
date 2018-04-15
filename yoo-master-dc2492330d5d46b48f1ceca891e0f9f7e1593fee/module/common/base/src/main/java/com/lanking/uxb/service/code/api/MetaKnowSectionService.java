package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;

/**
 * 章节知识点
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月10日
 */
public interface MetaKnowSectionService {

	List<Integer> findBySectionCode(long sectionCode);

	List<Integer> findBySectionCodes(Collection<Long> sectionCodes);

	List<Long> findByMetaknowCodes(Collection<Integer> metaknowCodes);

	/**
	 * 根据章节code 获取所有子章节知识点
	 * 
	 * @param code
	 *            章节CODE
	 * @return
	 */
	List<Integer> getKnowCodesByCode(long code);
}
