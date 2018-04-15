package com.lanking.uxb.zycon.operation.api;

import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;

/**
 * 学生导出错题记录接口
 * 
 * @author wangsenhao
 * @since 2.5.0
 */
public interface ZycStudentFallibleExportRecordService {
	/**
	 * 通过代打印订单ID查询对应的学生导出错题记录
	 * 
	 * @param fallibleQuestionPrintOrderId
	 * @return
	 */
	StudentFallibleExportRecord getByOrderId(Long fallibleQuestionPrintOrderId);

	/**
	 * 通过代打印订单ID批量获取
	 * 
	 * @param fallibleQuestionPrintOrderIds
	 * @return
	 */
	Map<Long, StudentFallibleExportRecord> mgetByOrderIds(Collection<Long> fallibleQuestionPrintOrderIds);
}
