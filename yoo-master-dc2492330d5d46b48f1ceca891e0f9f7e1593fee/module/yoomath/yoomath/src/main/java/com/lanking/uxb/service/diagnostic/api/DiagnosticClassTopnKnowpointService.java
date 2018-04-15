package com.lanking.uxb.service.diagnostic.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTopnKnowpoint;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassTopnKnowpointService {
	/**
	 * 查询班级前10名相对应的知识点正确率
	 *
	 * @param classId
	 *            班级id
	 * @param codes
	 *            知识点代码列表
	 * @return {@link List}
	 */
	Map<Long, DiagnosticClassTopnKnowpoint> query(long classId, Collection<Long> codes);
}
