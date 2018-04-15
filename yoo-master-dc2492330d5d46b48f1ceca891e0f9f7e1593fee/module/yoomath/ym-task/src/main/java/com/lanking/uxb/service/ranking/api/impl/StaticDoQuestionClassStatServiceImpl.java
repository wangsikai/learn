package com.lanking.uxb.service.ranking.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionStudentStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.push.type.OpenPath;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.ranking.api.StaticDoQuestionClassStatService;
import com.lanking.uxb.service.session.api.DeviceService;

@Transactional(readOnly = true)
@Service
public class StaticDoQuestionClassStatServiceImpl implements StaticDoQuestionClassStatService {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> clazzRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> stuClazzRepo;

	@Autowired
	@Qualifier("DoQuestionClassStatRepo")
	private Repo<DoQuestionClassStat, Long> doQuestionClassStatRepo;

	@Autowired
	@Qualifier("DoQuestionStudentStatRepo")
	private Repo<DoQuestionStudentStat, Long> doQuestionStudentStatRepo;

	@Autowired
	@Qualifier("DoQuestionSchoolStatRepo")
	private Repo<DoQuestionSchoolStat, Long> doQuestionSchoolStatRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	private Repo<Teacher, Long> teacherRepo;

	@Autowired
	private MessageSender messageSender;
	@Autowired
	private DeviceService deviceService;

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryClassId(CursorPageable<Long> pageable) {
		return clazzRepo.find("$taskQueryClass").fetch(pageable, Map.class, new CursorGetter<Long, Map>() {
			@Override
			public Long getCursor(Map bean) {
				return Long.parseLong(String.valueOf(bean.get("id")));
			}
		});
	}

	@Override
	public Map<Long, DoQuestionClassStat> listOneClass(long classId) {
		List<DoQuestionClassStat> stats = doQuestionClassStatRepo.find("$taskListOneClass",
				Params.param("classId", classId)).list();
		if (CollectionUtils.isEmpty(stats)) {
			return Collections.EMPTY_MAP;
		} else {
			Map<Long, DoQuestionClassStat> statMap = new HashMap<Long, DoQuestionClassStat>(stats.size());
			for (DoQuestionClassStat doQuestionClassStat : stats) {
				statMap.put(doQuestionClassStat.getUserId(), doQuestionClassStat);
			}
			return statMap;
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false)
	@Override
	public void staticDoQuestionClassStat(int day, List<Long> classIds, List<Long> teacherIds) {
		List<DoQuestionClassStat> classStats = Lists.newArrayList();
		List<DoQuestionSchoolStat> schoolStats = Lists.newArrayList();
		Map<Long, Teacher> teachers = teacherRepo.mget(teacherIds);
		int idx = 0;
		for (Long classId : classIds) {
			Teacher teacher = teachers.get(teacherIds.get(idx));
			long schoolId = 0;
			int phaseCode = 0;
			if (teacher != null) {
				schoolId = teacher.getSchoolId() == null ? 0 : teacher.getSchoolId();
				phaseCode = teacher.getPhaseCode() == null ? 0 : teacher.getPhaseCode();
			}
			List<Map> list = stuClazzRepo.find("$taskListStudent", Params.param("classId", classId)).list(Map.class);
			if (CollectionUtils.isNotEmpty(list)) {
				List<Long> studentIds = new ArrayList<Long>(list.size());
				for (Map map : list) {
					long studentId = ((BigInteger) map.get("student_id")).longValue();
					studentIds.add(studentId);
				}
				List<DoQuestionStudentStat> studentStats = doQuestionStudentStatRepo.find(
						"$taskQueryStudentInOneClass", Params.param("day0", day).put("userIds", studentIds)).list();
				int rank = 1;

				List<Long> pushStudentIds = new ArrayList<Long>(studentIds.size());
				Map<Long, DoQuestionClassStat> listOneClassLatestStats = Collections.EMPTY_MAP;
				if (day == 7) {
					listOneClassLatestStats = listOneClass(classId);
				}

				if (CollectionUtils.isNotEmpty(studentStats)) {
					for (DoQuestionStudentStat studentStat : studentStats) {
						DoQuestionClassStat classStat = new DoQuestionClassStat();
						classStat.setClassId(classId);
						classStat.setDay(day);
						classStat.setDoCount(studentStat.getDoCount());
						classStat.setRightCount(studentStat.getRightCount());
						classStat.setRightRate(studentStat.getRightRate());
						classStat.setStatus(Status.DISABLED);
						classStat.setUserId(studentStat.getUserId());
						classStat.setSchoolId(schoolId);
						classStat.setPhaseCode(phaseCode);
						classStat.setRank(rank);
						classStats.add(classStat);

						if (day == 7 && rank <= 10) {
							DoQuestionClassStat latestStat = listOneClassLatestStats.get(studentStat.getUserId());
							if (latestStat == null || latestStat.getRank() > 10) {
								pushStudentIds.add(studentStat.getUserId());
							}
						}
						rank++;
					}
				} else {
					// 如果学生都没有练习题目则表示此班级的练习题目也为0,向do_question_school_stat存入此班级的记录
					DoQuestionSchoolStat schoolStat = new DoQuestionSchoolStat();
					schoolStat.setDoCount(0);
					schoolStat.setRightCount(0);
					schoolStat.setStatus(Status.DISABLED);
					schoolStat.setRightRate(BigDecimal.valueOf(0));
					schoolStat.setSchoolId(schoolId);
					schoolStat.setClassId(classId);
					schoolStat.setPhaseCode(phaseCode);
					schoolStat.setDay(day);
					schoolStats.add(schoolStat);
				}
				// 进入排行榜推送
				if (day == 7 && pushStudentIds.size() > 0) {
					List<String> tokens = deviceService.findTokenByUserIds(pushStudentIds, Product.YOOMATH);
					if (CollectionUtils.isNotEmpty(tokens)) {
						messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_STUDENT, tokens, 12000000,
								ValueMap.value("p", OpenPath.RANKING_CLASS.getName()).put("classId",
										String.valueOf(classId)), YmPushUrls.url(YooApp.MATH_STUDENT,
										OpenPath.RANKING_CLASS, ValueMap.value("classId", String.valueOf(classId)))));
					}
				}
			}
			idx++;
		}
		if (CollectionUtils.isNotEmpty(classStats)) {
			doQuestionClassStatRepo.save(classStats);
		}
		if (CollectionUtils.isNotEmpty(schoolStats)) {
			doQuestionSchoolStatRepo.save(schoolStats);
		}
	}

	@Transactional(readOnly = false)
	@Override
	public void refreshDoQuestionClassStat() {
		doQuestionClassStatRepo.execute("$taskDeleteOldDoQuestionClassStat");
		doQuestionClassStatRepo.execute("$taskEnableNewDoQuestionClassStat");
	}

}
