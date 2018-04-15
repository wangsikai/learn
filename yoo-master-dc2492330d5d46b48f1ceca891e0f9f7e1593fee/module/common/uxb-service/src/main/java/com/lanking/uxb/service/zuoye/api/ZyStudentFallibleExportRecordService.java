package com.lanking.uxb.service.zuoye.api;

import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * 学生导出错题本记录服务.
 * 
 * @author wlche
 *
 */
public interface ZyStudentFallibleExportRecordService {

	/**
	 * 保存记录.
	 * 
	 * @param record
	 */
	void save(StudentFallibleExportRecord record);

	/**
	 * 分页查询所有记录.
	 * 
	 * @param studentId
	 *            学生ID
	 * @param pageable
	 * @return
	 */
	Page<StudentFallibleExportRecord> query(long studentId, Pageable pageable);

	/**
	 * 获得兑换记录.
	 * 
	 * @param id
	 *            记录ID.
	 * @return
	 */
	StudentFallibleExportRecord get(long id);

	/**
	 * 兑换.
	 * 
	 * @param id
	 */
	void buy(long id);

	/**
	 * 更新状态.
	 * 
	 * @param id
	 */
	void updateStatus(long id, Status status);

	/**
	 * 根据hash获得学生兑换记录.
	 * 
	 * @param studentId
	 * @param hash
	 * @return
	 */
	StudentFallibleExportRecord findByHash(long studentId, int hash);
}
