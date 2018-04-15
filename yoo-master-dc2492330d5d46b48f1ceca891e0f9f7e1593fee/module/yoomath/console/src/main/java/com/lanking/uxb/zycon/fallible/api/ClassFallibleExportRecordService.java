package com.lanking.uxb.zycon.fallible.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecord;
import com.lanking.cloud.domain.yoomath.fallible.ClassFallibleExportRecordStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

public interface ClassFallibleExportRecordService {

	/**
	 * 保存记录.
	 * 
	 * @param record
	 */
	void save(ClassFallibleExportRecord record);

	/**
	 * 分页查询所有记录.
	 * @param pageable
	 * @return
	 */
	Page<ClassFallibleExportRecord> query(Pageable pageable);

	/**
	 * 根据记录id查询导出记录信息
	 * 
	 * @param id
	 *            记录ID.
	 * @return
	 */
	ClassFallibleExportRecord get(long id);


	/**
	 * 更新状态.
	 * @param id
	 * @param noQuestionStudentNames(没有错题记录的学生)
	 * @param status
	 */
	void updateStatus(long id,List<String> noQuestionStudentNames, ClassFallibleExportRecordStatus status);
	
	/**
	 * 根据clazzId和status查询导出记录
	 * @param clazzId
	 * @param status
	 * @return
	 */
	ClassFallibleExportRecord findByClassIdandStatus(Long clazzId,int status);

}
