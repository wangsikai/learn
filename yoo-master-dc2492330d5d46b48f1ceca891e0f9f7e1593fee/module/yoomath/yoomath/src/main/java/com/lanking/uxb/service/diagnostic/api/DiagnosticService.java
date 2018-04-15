package com.lanking.uxb.service.diagnostic.api;

import com.lanking.cloud.domain.yoomath.diagnostic.Diagnostic;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticService {
	/**
	 * 根据教材码查找数据
	 *
	 * @param textbookCode
	 *            教材码
	 * @return {@link Diagnostic}
	 */
	Diagnostic findByTextbook(int textbookCode);
}
