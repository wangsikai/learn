package com.lanking.uxb.service.diagnostic.api;

import java.util.List;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
public interface DiagnosticClassStudentService {
	/**
	 * 分页查询班级学生掌握情况
	 *
	 * @param pageable
	 *            {@link Pageable}
	 * @param day0
	 *            统计时间范围
	 * @param orderBy
	 *            根据如何进行排序
	 * @param classId
	 *            班级id
	 * @return {@link Page}
	 */
	Page<DiagnosticClassStudent> query(Pageable pageable, int day0, int orderBy, long classId);

	/**
	 * 不分页查询班级学生掌握情况
	 *
	 * @param day0
	 *            统计时间范围
	 * @param orderBy
	 *            0.正确率DESC(名次榜)<br>
	 *            1.floatRank DESC(进步榜)<br>
	 *            2.floatRank ASC(退步榜)<br>
	 *            3.作业数量 DESC(勤奋榜)<br>
	 * @param classId
	 *            班级id
	 * @return {@link Page}
	 */
	List<DiagnosticClassStudent> query(int day0, int orderBy, long classId);
}
