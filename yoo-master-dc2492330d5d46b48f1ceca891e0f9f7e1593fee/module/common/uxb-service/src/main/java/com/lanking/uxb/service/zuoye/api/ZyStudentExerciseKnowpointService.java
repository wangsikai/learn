package com.lanking.uxb.service.zuoye.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;

/**
 * 得到知识点完成情况
 *
 * @author xinyu.zhou
 * @since yoomath V1.6
 * @since yoomath v2.1.2 新知识点
 */
public interface ZyStudentExerciseKnowpointService {

	/**
	 * 根据知识点获得Map对象
	 *
	 * @param codes
	 *            知识点代码
	 * @return Map
	 */
	Map<Integer, StudentExerciseKnowpoint> mgetByCodes(Collection<Integer> codes, long studentId);

	/**
	 * 根据新知识点获得Map对象
	 *
	 * @param codes
	 *            知识点代码
	 * @return Map
	 */
	Map<Long, StudentExerciseKnowpoint> mgetNewByCodes(Collection<Long> codes, long studentId);

	/**
	 * 根据知识点获得掌握情况
	 *
	 * @param code
	 *            知识点代码
	 * @param studentId
	 *            学生的id
	 * @return {@link StudentExerciseKnowpoint}
	 */
	StudentExerciseKnowpoint getByCode(int code, long studentId);

	/**
	 * 根据班级查找学生知识点掌握情况
	 *
	 * @param codes
	 *            知识点代表列表
	 * @param classId
	 *            班级id
	 * @return 一个班级中所有学生掌握的知识点情况
	 */
	Map<Integer, List<StudentExerciseKnowpoint>> getByCodeAndClass(List<Integer> codes, long classId);
}
