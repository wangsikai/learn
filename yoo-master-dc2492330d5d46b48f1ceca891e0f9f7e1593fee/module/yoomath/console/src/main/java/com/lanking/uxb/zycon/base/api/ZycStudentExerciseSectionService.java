package com.lanking.uxb.zycon.base.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentExerciseSection;

/**
 * 学生加强练习接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
public interface ZycStudentExerciseSectionService {

	/**
	 * 根据id获得数据
	 *
	 * @param id
	 *            id
	 * @return {@link StudentExerciseSection}
	 */
	StudentExerciseSection get(long id);

	/**
	 * 根据Section获得数据
	 *
	 * @param userId
	 *            学生id
	 * @param sectionCode
	 *            章节码
	 * @return {@link StudentExerciseSection}
	 */
	StudentExerciseSection getBySection(long userId, long sectionCode);

	/**
	 * mget　by 章节码
	 *
	 * @param userId
	 *            学生id
	 * @param sectionCodes
	 *            章节码
	 * @param lastMonth
	 *            对应的上个月如 201602
	 * @return {@link StudentExerciseSection}
	 */
	Map<Long, StudentExerciseSection> mgetBySection(long userId, Collection<Long> sectionCodes, Long lastMonth);

	/**
	 * 查找一个班级学生平均掌握情况
	 * 从1.9开始，寒假作业功能中需要查看班级平均掌握情况，此时传入的sectionCode为教材下的<strong>一级目录</strong>
	 *
	 * @since yoomath V1.9
	 * @param classId
	 *            班级id
	 * @param sectionCode
	 *            章节码
	 * @param lastMonth
	 *            月份 如 201602
	 * @return {@link StudentExerciseSection}
	 */
	List<StudentExerciseSection> findByClassIdAndSectionCode(long classId, long sectionCode, Long lastMonth);
}
