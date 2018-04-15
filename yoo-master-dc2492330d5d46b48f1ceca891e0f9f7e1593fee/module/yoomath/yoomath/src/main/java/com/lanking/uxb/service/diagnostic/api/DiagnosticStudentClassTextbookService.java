package com.lanking.uxb.service.diagnostic.api;

import java.util.List;

public interface DiagnosticStudentClassTextbookService {
	/**
	 * 查询对应的教材
	 * 
	 * @param classId
	 * @param studentId
	 * @param sort
	 *            是否按code 排序
	 * @return
	 */
	List<Integer> queryTextBookList(Long classId, Long studentId, boolean sort, Integer categoryCode);

}
