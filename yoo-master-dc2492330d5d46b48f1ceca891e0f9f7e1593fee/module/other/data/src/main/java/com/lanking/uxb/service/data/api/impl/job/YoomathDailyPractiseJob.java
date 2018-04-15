package com.lanking.uxb.service.data.api.impl.job;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.service.data.api.StudentDailyPractisePushService;
import com.lanking.uxb.service.user.api.StudentService;

/**
 * 每日练习推送定时服务
 *
 * @author xinyu.zhou
 * @since yoomath(mobile) V1.0.0
 */
public class YoomathDailyPractiseJob implements SimpleJob {

	private static final int FETCH_SIZE = 100;

	@Autowired
	private StudentService studentService;
	@Autowired
	private StudentDailyPractisePushService studentDailyPractisePushService;

	/**
	 * 每天定时推送,学生的教材设置不为空的则进行推送操作
	 */
	public void execute(ShardingContext shardingContext) {
		List<Student> students = null;

		for (int i = 1;; i++) {
			Pageable pageable = P.index(i, FETCH_SIZE);
			students = studentService.queryStudentByTextbookCode(pageable).getItems();
			if (students == null || students.size() == 0) {
				break;
			}
			studentDailyPractisePushService.push(students);
		}
	}
}
