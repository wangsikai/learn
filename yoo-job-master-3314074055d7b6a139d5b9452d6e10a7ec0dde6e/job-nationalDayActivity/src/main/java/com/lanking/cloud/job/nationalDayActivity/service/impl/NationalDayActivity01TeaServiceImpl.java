package com.lanking.cloud.job.nationalDayActivity.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Homework;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.job.nationalDayActivity.DAO.HomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01DAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01HomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.NationalDayActivity01TeaDAO;
import com.lanking.cloud.job.nationalDayActivity.DAO.StudentHomeworkDAO;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01Service;
import com.lanking.cloud.job.nationalDayActivity.service.NationalDayActivity01TeaService;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class NationalDayActivity01TeaServiceImpl implements NationalDayActivity01TeaService {

	@Autowired
	@Qualifier("nda01NationalDayActivity01HomeworkDAO")
	private NationalDayActivity01HomeworkDAO nationalDayActivity01HomeworkDAO;
	@Autowired
	@Qualifier("nda01HomeworkDAO")
	private HomeworkDAO homeworkDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01TeaDAO")
	private NationalDayActivity01TeaDAO nationalDayActivity01TeaDAO;
	@Autowired
	@Qualifier("nda01NationalDayActivity01DAO")
	private NationalDayActivity01DAO nationalDayActivity01DAO;
	@Autowired
	@Qualifier("nda01StudentHomeworkDAO")
	private StudentHomeworkDAO nda01StudentHomework;

	@Transactional(readOnly = false)
	@Override
	public void saveTea(List<Long> teacherIds, NationalDayActivity01 activity) {
		// 根据teacherId查询homework数据
		Map<Long, List<NationalDayActivity01Homework>> homeworkMap = nationalDayActivity01HomeworkDAO
				.getHomeworkByUsers(teacherIds);
		if (!homeworkMap.isEmpty()) {
			for (Map.Entry<Long, List<NationalDayActivity01Homework>> entry : homeworkMap.entrySet()) {
				long teacherId = entry.getKey();
				List<Homework> homeworks = getHomework(entry.getValue(), activity);

				// 计算提交率要根据学生作业来算
				int submitedCount = getStudentHomeworks(homeworks, activity);

				// 计算提交率=作业提交数量/作业分发数量 数据库记录int类型
				int subRate = getHomeworkSubRate(homeworks, Double.valueOf(submitedCount));
				// 计算综合得分=作业数量*提交率/10,提交率记录为*100,所以这里要/10
				int homeworkCount = homeworks.size();
				int score = getTeacherScore(homeworkCount, subRate);

				NationalDayActivity01Tea activityTea = nationalDayActivity01TeaDAO.get(teacherId);
				if (activityTea == null) {
					activityTea = new NationalDayActivity01Tea();
					activityTea.setUserId(teacherId);
					activityTea.setHomeworkCount(homeworkCount);
					activityTea.setCommitRate(subRate);
					activityTea.setScore(score);
				} else {
					activityTea.setHomeworkCount(homeworkCount);
					activityTea.setCommitRate(subRate);
					activityTea.setScore(score);
				}

				nationalDayActivity01TeaDAO.save(activityTea);
			}
		}
	}

	/**
	 * 查询作业信息
	 * 
	 * @param nHomework
	 * @return
	 */
	private List<Homework> getHomework(List<NationalDayActivity01Homework> nHomework, NationalDayActivity01 activity) {
		List<Homework> homeworks = Lists.newArrayList();
		if (CollectionUtils.isEmpty(nHomework)) {
			return homeworks;
		}

		List<Long> ids = nHomework.stream().map(p -> p.getHomeworkId()).collect(Collectors.toList());
		Map<Long, Homework> homeworkMap = homeworkDAO.mget(ids);


		for (Map.Entry<Long, Homework> entry : homeworkMap.entrySet()) {
			// 根据活动结束时间过滤下作业，超过的不加入处理
			homeworks.add(entry.getValue());
		}

		return homeworks;
	}

	/**
	 * 计算作业提交率
	 * 
	 * @param homeworks
	 * @return subRate
	 */
	private int getHomeworkSubRate(List<Homework> homeworks, double commitCount) {
		if (CollectionUtils.isEmpty(homeworks)) {
			return 0;
		}

		double distributeCount = 0L;
		for (Homework value : homeworks) {
			distributeCount = value.getDistributeCount() + distributeCount;
		}

		Double rateDouble = new BigDecimal(commitCount / distributeCount).setScale(2, BigDecimal.ROUND_HALF_UP)
				.doubleValue() * 100;

		return rateDouble.intValue();
	}

	/**
	 * 计算综合得分
	 * 
	 * @param homeworkCount
	 * @param subRate
	 * @return 得分
	 */
	private int getTeacherScore(int homeworkCount, int subRate) {
		// 作业数量*提交率/10 四舍五入
		double count = homeworkCount;
		double rate = subRate;

		Double d = new BigDecimal(count * rate / 10).setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();

		return d.intValue();
	}

	@Override
	public List<Long> getTeacherIdsSpecify(int startindex, int size) {
		return nationalDayActivity01HomeworkDAO.getTeacherIdsSpecify(startindex, size);
	}

	@Override
	public NationalDayActivity01 getActivity() {
		return nationalDayActivity01DAO.get(NationalDayActivity01Service.NATIONAL_DAY_ACTIVITY_ID);
	}

	/**
	 * 查询学生已提交的作业数量
	 * 
	 * @param homeworks
	 * @param activity
	 */
	private int getStudentHomeworks(List<Homework> homeworks, NationalDayActivity01 activity) {
		List<Long> homeworkIds = Lists.newArrayList();
		for (Homework value : homeworks) {
			homeworkIds.add(value.getId());
		}

		List<StudentHomework> sHomwrorks = nda01StudentHomework.getSubmitedIssuedHomework(homeworkIds,
				activity.getStartTime(), activity.getEndTime());
		if (CollectionUtils.isEmpty(sHomwrorks)) {
			return 0;
		} else {
			return sHomwrorks.size();
		}
	}
}
