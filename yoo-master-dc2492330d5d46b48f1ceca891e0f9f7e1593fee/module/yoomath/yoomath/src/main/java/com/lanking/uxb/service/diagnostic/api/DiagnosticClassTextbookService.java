package com.lanking.uxb.service.diagnostic.api;

import java.util.List;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassTextbookService {
	/**
	 * 得到班级有效的教材(按教材加入时间排序)
	 *
	 * @param classId
	 *            班级id
	 * @param textbookCategory
	 *            版本码
	 * @return {@link List}
	 */
	List<Integer> getClassTextbooks(long classId, int textbookCategory);

	/**
	 * 得到班级有效的教材(按教材自身顺序排序)
	 * 
	 * @param classId
	 * @param textbookCategory
	 * @return
	 */
	List<Integer> getClassSortTextbooks(long classId, int textbookCategory);
}
