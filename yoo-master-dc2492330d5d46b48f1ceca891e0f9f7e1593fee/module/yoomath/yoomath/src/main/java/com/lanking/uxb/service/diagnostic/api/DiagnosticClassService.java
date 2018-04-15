package com.lanking.uxb.service.diagnostic.api;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassService {
	/**
	 * 根据班级及教材码查询数据
	 *
	 * @param textbookCode
	 *            教材码
	 * @return {@link DiagnosticClass}
	 */
	DiagnosticClass getByTextbook(int textbookCode, long classId);
}
