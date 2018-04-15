package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;

@Transactional(readOnly = true)
@Service
public class ZyStudentHomeworkServiceImpl implements ZyStudentHomeworkService {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> stuHkRepo;

	@Autowired
	private StudentHomeworkService stuHkService;
	@Autowired
	private ZyStudentHomeworkStatService stuHkStatService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private ZyStudentHomeworkAnswerService zyStuHkAnswerService;
	@Autowired
	private MqSender mqSender;

	@Override
	public Page<StudentHomework> query(ZyStudentHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.isCourse()) {// 课程作业
			if (query.getCourseId() != null) {
				params.put("courseId", query.getCourseId());
				params.put("fromCourse", "1");
			}
		} else {// 班级作业
			if (query.getClassId() != null) {
				params.put("classId", query.getClassId());
				params.put("fromClass", "1");
			}
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = Sets.newHashSet();
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			if (statusVals.size() == 1) {
				params.put("status", statusVals.iterator().next());
			} else {
				params.put("statuses", statusVals);
			}
		}
		return stuHkRepo.find("$zyQuery", params).fetch(pageable);
	}

	@Override
	public CursorPage<Long, StudentHomework> query(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.isCourse()) {// 课程作业
			if (query.getCourseId() != null) {
				params.put("courseId", query.getCourseId());
				params.put("fromCourse", "1");
			}
		} else {// 班级作业
			if (query.getClassId() != null) {
				params.put("classId", query.getClassId());
				params.put("fromClass", "1");
			}
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = Sets.newHashSet();
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			if (statusVals.size() == 1) {
				params.put("status", statusVals.iterator().next());
			} else {
				params.put("statuses", statusVals);
			}
		}
		return stuHkRepo.find("$zyQueryCursor", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryUnionHolidayStuHk(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable)

	{
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getStatus().size() > 1) {
			Set<Integer> statusSet = new HashSet<Integer>(query.getStatus().size());
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusSet.add(s.getValue());
			}
			params.put("statuses", statusSet);
		} else {
			params.put("status", query.getStatus().iterator().next().getValue());
		}
		if (query.isMobileIndex()) {
			params.put("isMobileIndex", 1);
		} else {
			params.put("isMobileIndex", 0);
		}
		if (query.getClassId() != null) {
			params.put("clazzId", query.getClassId());
		}
		if ("startTime".equals(query.getCursorType())) {
			if (pageable.getCursor() != null && pageable.getCursor().longValue() < Long.MAX_VALUE) {
				params.put("startTime", new Date(pageable.getCursor()));
			}
			return stuHkRepo.find("$queryUnionHolidayStuHkByStartTimeCursor", params).fetch(pageable,

					Map.class, new CursorGetter<Long, Map>() {
						@Override
						public Long getCursor(Map bean) {
							return ((Date) bean.get("start_time")).getTime();
						}
					});
		} else {
			return stuHkRepo.find("$queryUnionHolidayStuHkByCursor", params).fetch(pageable, Map.class,
					new CursorGetter<Long, Map>() {
						@Override
						public Long getCursor(Map bean) {
							return Long.parseLong(String.valueOf(bean.get("id")));
						}
					});
		}

	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryUnionHolidayStuHk(ZyStudentHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.isCourse()) {// 课程作业
			if (query.getCourseId() != null) {
				params.put("courseId", query.getCourseId());
				params.put("fromCourse", "1");
			}
		} else {// 班级作业
			if (query.getClassId() != null) {
				params.put("classId", query.getClassId());
				params.put("fromClass", "1");
			}
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = Sets.newHashSet();
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			if (statusVals.size() == 1) {
				params.put("status", statusVals.iterator().next());
			} else {
				params.put("statuses", statusVals);
			}
		}
		return stuHkRepo.find("$queryUnionHolidayStuHk", params).fetch(pageable, Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryUnionHolidayStuHkNew(ZyStudentHomeworkQuery query) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.isCourse()) {// 课程作业
			if (query.getCourseId() != null) {
				params.put("courseId", query.getCourseId());
				params.put("fromCourse", "1");
			}
		} else {// 班级作业
			if (query.getClassId() != null) {
				params.put("classId", query.getClassId());
			}
			params.put("fromClass", "1");
		}
		if (CollectionUtils.isNotEmpty(query.getStatus())) {
			Set<Integer> statusVals = Sets.newHashSet();
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusVals.add(s.getValue());
			}
			if (statusVals.size() == 1) {
				params.put("status", statusVals.iterator().next());
			} else {
				params.put("statuses", statusVals);
			}
		}
		return stuHkRepo.find("$queryUnionHolidayStuHkNew", params).list(Map.class);
	}

	@Transactional
	@Override
	public long updateHomeworkTime(long id, long studentId, int homeworkTime, Double completionRate) {
		Params params = Params.param("id", id).put("studentId", studentId).put("homeworkTime", homeworkTime);
		if (completionRate != null) {
			params.put("completionRate", completionRate);
		}
		Date now = new Date();
		params.put("updateAt", now);
		stuHkRepo.execute("$zyUpdateHomeworkTime", params);

		return now.getTime();
	}

	@Transactional
	@Override
	public long commitHomework(long homeworkId, long studentId) {
		Homework hk = stuHkService.commitHomework(homeworkId, studentId);
		if (hk == null) {
			return 0;
		} else {
			// 下面的操作靠上面的操作异常阻止
			stuHkStatService.updateAfterCommitHomework(studentId, hk.getHomeworkClassId());
			// 更新最后一个学生作业提交的时间
			if (hk.getDistributeCount().longValue() == stuHkService.countCommit(homeworkId)) {
				zyHkService.updateLastCommitAt(homeworkId, 0);

				// 作业已截止 @since 小悠快批，2018-2-27
				hkService.updateStatus(homeworkId, HomeworkStatus.NOT_ISSUE);
			}
			return hk.getStudentHomeworkId();
		}
	}

	@Transactional
	@Override
	public int commitHomework(long homeworkId) {
		List<StudentHomework> studentHomeworks = stuHkService.listByHomework(homeworkId);
		List<Long> notCommit = Lists.newArrayList();
		int commitCount = 0;
		for (StudentHomework studentHomework : studentHomeworks) {
			if (studentHomework.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
				notCommit.add(studentHomework.getStudentId());
				studentHomework.setSubmitAt(new Date());
				// 如果被自动提交的时候学生已经答题了，则算提交，否则不算提交
				if (zyStuHkAnswerService.isDoHomework(studentHomework.getId())) {
					studentHomework.setStuSubmitAt(studentHomework.getSubmitAt());
					JSONObject jo = new JSONObject();
					jo.put("studentId", studentHomework.getStudentId());
					jo.put("studentHomeworkId", studentHomework.getId());
					mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
							MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_COMMIT,

							MQ.builder().data(jo).build());
					commitCount++;
				} else {
					studentHomework.setStuSubmitAt(null);
				}
				studentHomework.setStatus(StudentHomeworkStatus.SUBMITED);
				stuHkRepo.save(studentHomework);
			}
		}
		// 更新最后一个学生作业提交的时间
		zyHkService.updateLastCommitAt(homeworkId, commitCount);
		Homework hk = hkService.get(homeworkId);
		for (Long studentId : notCommit) {
			stuHkStatService.updateAfterCommitHomework(studentId, hk.getHomeworkClassId());
		}
		return notCommit.size();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> statisticLatestHomework(long classId, long studentId, int limit) {
		List<Map> list = stuHkRepo.find("$zyStatisticLatestHomework",
				Params.param("classId", classId).put("studentId", studentId).put("limit",

						limit))
				.list(Map.class);
		if (CollectionUtils.isNotEmpty(list)) {
			Collections.reverse(list);
		}
		return list;
	}

	@Override
	public long countHomeworks(long studentId, Set<StudentHomeworkStatus> status) {
		Params params = Params.param("studentId", studentId);
		if (status.size() > 0) {
			Set<Integer> statusSet = new HashSet<Integer>(status.size());
			for (StudentHomeworkStatus s : status) {
				statusSet.add(s.getValue());
			}
			params.put("statuses", statusSet);
		} else {
			params.put("status", status.iterator().next().getValue());
		}
		return stuHkRepo.find("$zyCountHomeworks", params).count();
	}

	@Override
	public long countAllHomeworks(long studentId, Set<StudentHomeworkStatus> status) {
		Params params = Params.param("studentId", studentId);
		if (status.size() > 0) {
			Set<Integer> statusSet = new HashSet<Integer>(status.size());
			for (StudentHomeworkStatus s : status) {
				statusSet.add(s.getValue());
			}
			params.put("statuses", statusSet);
		} else {
			params.put("status", status.iterator().next().getValue());
		}
		return stuHkRepo.find("$countAllHomeworks", params).count();
	}

	@Override
	public List<Long> listNotCommitStudent(long homeworkId) {
		return stuHkRepo.find("$zyListNotCommitStudent", Params.param("hkId", homeworkId)).list(Long.class);
	}

	@Override
	public List<StudentHomework> listTop5ByHomework(long homeworkId, Integer limit) {
		Params params = Params.param("homeworkId", homeworkId);
		if (limit != null) {
			params.put("limit", limit);
		}
		return stuHkRepo.find("$findStudentHomeworkTop", params).list();
	}

	@Override
	public List<Long> getMostImprovedStu(Long classId, int limit) {
		// 最近下发的两次作业
		List<Homework> hkList = zyHkService.getLatestIssuedHomeWorks(classId, 2);

		// 没有两次下发的作业
		if (hkList.size() != 2) {
			return null;
		}
		// 判断两份作业中，有效提交是否均大于20
		for (Homework homework : hkList) {
			if (homework.getCommitCount() < 20) {
				return null;
			}
		}

		// 早一点
		List<StudentHomework> shListlate = stuHkService.listByHomework(hkList.get(0).getId());
		// 晚一点
		List<StudentHomework> shListEarly = stuHkService.listByHomework(hkList.get(1).getId());
		Map<Long, Integer> improvedMap = Maps.newHashMap();
		// 找到进步学生map
		for (StudentHomework studentHomeworkLate : shListlate) {
			for (StudentHomework studentHomeworkEarly : shListEarly) {
				if (studentHomeworkEarly.getStudentId().equals(studentHomeworkLate.getStudentId())
						&& studentHomeworkLate.getRank() != null && studentHomeworkEarly.getRank

						() != null && studentHomeworkLate.getRank() >= studentHomeworkEarly.getRank()) {
					if (studentHomeworkLate.getRank() - studentHomeworkEarly.getRank() != 0) {
						improvedMap.put(studentHomeworkEarly.getStudentId(), studentHomeworkLate.getRank() -

								studentHomeworkEarly.getRank());
					}
				}
			}
		}
		if (improvedMap.isEmpty()) {
			return null;
		}
		List<Map.Entry<Long, Integer>> list_Data = new ArrayList<Map.Entry<Long, Integer>>(improvedMap.entrySet

		());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(list_Data, new Comparator<Map.Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
				return (o1.getValue() - o2.getValue());
			}
		});
		List<Long> userIds = Lists.newArrayList();
		if (list_Data.size() > limit) {
			for (int i = 0; i < limit; i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		} else {
			for (int i = 0; i < list_Data.size(); i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		}
		return userIds;
	}

	@Override
	public Map<String, Map<Long, Integer>> getMostImprovedMap(Long classId, int limit) {
		// 最近下发的两次作业
		List<Homework> hkList = zyHkService.getLatestIssuedHomeWorks(classId, 2);

		// 没有两次下发的作业
		if (hkList.size() != 2) {
			return null;
		}
		// 判断两份作业中，有效提交是否均大于20
		for (Homework homework : hkList) {
			if (homework.getCommitCount() < 20) {
				return null;
			}
		}

		// 早一点
		List<StudentHomework> shListlate = stuHkService.listByHomework(hkList.get(0).getId());
		// 晚一点
		List<StudentHomework> shListEarly = stuHkService.listByHomework(hkList.get(1).getId());
		Map<Long, Integer> improvedMap = Maps.newHashMap();
		Map<Long, Integer> rankMap = Maps.newHashMap();
		// 找到进步学生map
		for (StudentHomework studentHomeworkLate : shListlate) {
			for (StudentHomework studentHomeworkEarly : shListEarly) {
				if (studentHomeworkEarly.getStudentId().equals(studentHomeworkLate.getStudentId())
						&& studentHomeworkLate.getRank() != null && studentHomeworkEarly.getRank

						() != null && studentHomeworkLate.getRank() >= studentHomeworkEarly.getRank()) {
					if (studentHomeworkLate.getRank() - studentHomeworkEarly.getRank() != 0) {
						improvedMap.put(studentHomeworkEarly.getStudentId(), studentHomeworkLate.getRank() -

								studentHomeworkEarly.getRank());
					}
					rankMap.put(studentHomeworkEarly.getStudentId(), studentHomeworkEarly.getRank());
				}
			}
		}
		if (improvedMap.isEmpty()) {
			return null;
		}
		List<Map.Entry<Long, Integer>> list_Data = new ArrayList<Map.Entry<Long, Integer>>(improvedMap.entrySet

		());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(list_Data, new Comparator<Map.Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});
		List<Long> userIds = Lists.newArrayList();
		if (list_Data.size() > limit) {
			for (int i = 0; i < limit; i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		} else {
			for (int i = 0; i < list_Data.size(); i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		}
		Map<String, Map<Long, Integer>> map = new HashMap<String, Map<Long, Integer>>();
		if (CollectionUtils.isNotEmpty(userIds)) {
			// 进步了多少名
			Map<Long, Integer> map1 = new HashMap<Long, Integer>();
			// 本次对应的排名
			Map<Long, Integer> map2 = new HashMap<Long, Integer>();
			for (Long userId : userIds) {
				map1.put(userId, improvedMap.get(userId));
				map2.put(userId, rankMap.get(userId));
			}
			map.put("improveRankMap", map1);
			map.put("rankMap", map2);
		}
		return map;
	}

	@Override
	public long countNotCorrect(long hkId) {
		return stuHkRepo.find("$zyCountNotCorrect", Params.param("hkId", hkId)).count();
	}

	@Override
	public List<Long> getMostBackwardStu(Long classId, int limit) {
		// 最近下发的两次作业
		List<Homework> hkList = zyHkService.getLatestIssuedHomeWorks(classId, 2);
		// 没有两次下发的作业

		if (hkList.size() != 2) {
			return null;
		} // 判断两份作业中，有效提交是否均大于20
		for (Homework homework : hkList) {
			if (homework.getCommitCount() < 20) {
				return null;
			}
		}

		List<StudentHomework> shListlate = stuHkService.listByHomework(hkList.get(0).getId());
		List<StudentHomework> shListEarly = stuHkService.listByHomework(hkList.get(1).getId());
		Map<Long, Integer> backwardMap = Maps.newHashMap();
		// 找到进步学生map
		for (StudentHomework studentHomeworkLate : shListlate) {
			for (StudentHomework studentHomeworkEarly : shListEarly) {
				if (studentHomeworkEarly.getStudentId().equals(studentHomeworkLate.getStudentId())
						&& studentHomeworkLate.getRank() != null && studentHomeworkEarly.getRank

						() != null && studentHomeworkLate.getRank() <= studentHomeworkEarly.getRank()) {
					backwardMap.put(studentHomeworkEarly.getStudentId(),
							studentHomeworkEarly.getRank() - studentHomeworkLate.getRank());
				}
			}
		}
		if (backwardMap.isEmpty()) {
			return null;
		}
		List<Map.Entry<Long, Integer>> list_Data = new ArrayList<Map.Entry<Long, Integer>>(backwardMap.entrySet

		());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(list_Data, new Comparator<Map.Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
				return (o1.getValue() - o2.getValue());
			}
		});
		List<Long> userIds = Lists.newArrayList();
		// 取出limit+1个进步最快的学生
		Integer newlimit = limit + 1;
		if (list_Data.size() > limit) {
			for (int i = 0; i < newlimit; i++) {
				userIds.add(list_Data.get(i).getKey());
			}
			Integer improve1 = backwardMap.get(newlimit - 1);
			Integer improve2 = backwardMap.get(newlimit);
			// 取出进步同样多前limit的学生
			if (improve1 == improve2) {
				for (Long id : backwardMap.keySet()) {
					if (backwardMap.get(id) == improve1) {
						if (!userIds.contains(id)) {
							userIds.add(id);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < list_Data.size(); i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		}
		return userIds;
	}

	@Override
	public List<Long> findNotSubmitStus(Long homeworkId) {
		return stuHkRepo.find("$findNotSubmitStus", Params.param("homeworkId", homeworkId)).list(Long.class);
	}

	@Override
	@Transactional
	public void updateViewStatus(long id) {
		stuHkRepo.execute("$updateViewStatus", Params.param("id", id));
	}

	@Override
	public List<StudentHomework> findByRank(long homeworkId, Integer rank) {
		return stuHkRepo.find("$findByRank", Params.param("homeworkId", homeworkId).put("rank", rank)).list();
	}

	/**
	 * 新作业查询.
	 */
	@Override
	public Page<StudentHomework> queryHomeworkWeb(ZyStudentHomeworkQuery query, Pageable pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getClassId() != null) {
			params.put("homeworkClassId", query.getClassId());
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("keys", "%" + query.getKey().replace(" ", "") + "%");
		}
		if (query.getBeginTime() != null && query.getEndTime() != null) {
			params.put("bt", query.getBeginTime());
			params.put("et", query.getEndTime());
		}
		if (StringUtils.isNotBlank(query.getSectionName())) {
			params.put("secname", "%" + query.getSectionName() + "%");
		}
		if (query.getHomeworkStatus() != null) {
			params.put("status", query.getHomeworkStatus());
		}
		return stuHkRepo.find("$queryHomeworkWeb2", params).fetch(pageable);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Date getFirstStartAt(Long studentId) {
		Params params = Params.param("studentId", studentId);
		// List<HomeworkClazz> clazzs =
		// zyHkClassService.listCurrentClazzs(teacherId);
		// Set<Long> clazzIds = Sets.newHashSet();
		// for (HomeworkClazz clazz : clazzs) {
		// clazzIds.add(clazz.getId());
		// }
		// if (clazzIds.size() > 0) {
		// params.put("homeworkClassIds", clazzIds);
		// }
		List<Map> list = stuHkRepo.find("$getFirstStartAt", params).list(Map.class);
		return list.size() == 0 ? null : (Date) list.get(0).get("ct");
	}

	@Override
	public Map<String, Map<Long, Integer>> getMostBackwardMap(Long classId, int limit) {
		// 最近下发的两次作业
		List<Homework> hkList = zyHkService.getLatestIssuedHomeWorks(classId, 2);
		// 没有两次下发的作业

		if (hkList.size() != 2) {
			return null;
		} // 判断两份作业中，有效提交是否均大于20
		for (Homework homework : hkList) {
			if (homework.getCommitCount() < 20) {
				return null;
			}
		}

		List<StudentHomework> shListlate = stuHkService.listByHomework(hkList.get(0).getId());
		List<StudentHomework> shListEarly = stuHkService.listByHomework(hkList.get(1).getId());
		Map<Long, Integer> backwardMap = Maps.newHashMap();
		// 本次的名次
		Map<Long, Integer> backRankMap = Maps.newHashMap();
		for (StudentHomework studentHomeworkLate : shListlate) {
			for (StudentHomework studentHomeworkEarly : shListEarly) {
				if (studentHomeworkEarly.getStudentId().equals(studentHomeworkLate.getStudentId())
						&& studentHomeworkLate.getRank() != null && studentHomeworkEarly.getRank

						() != null && studentHomeworkLate.getRank() <= studentHomeworkEarly.getRank()) {
					if (studentHomeworkEarly.getRank() - studentHomeworkLate.getRank() != 0) {
						backwardMap.put(studentHomeworkEarly.getStudentId(), studentHomeworkEarly.getRank() -

								studentHomeworkLate.getRank());
					}
					backRankMap.put(studentHomeworkEarly.getStudentId(),

							studentHomeworkEarly.getRank());
				}
			}
		}
		if (backwardMap.isEmpty()) {
			return null;
		}
		List<Map.Entry<Long, Integer>> list_Data = new ArrayList<Map.Entry<Long, Integer>>(backwardMap.entrySet

		());
		// 通过Collections.sort(List I,Comparator c)方法进行排序
		Collections.sort(list_Data, new Comparator<Map.Entry<Long, Integer>>() {
			@Override
			public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
				return (o1.getValue() - o2.getValue());
			}
		});
		List<Long> userIds = Lists.newArrayList();
		// 取出limit+1个进步最快的学生
		Integer newlimit = limit + 1;
		if (list_Data.size() > limit) {
			for (int i = 0; i < newlimit; i++) {
				userIds.add(list_Data.get(i).getKey());
			}
			Integer improve1 = backwardMap.get(newlimit - 1);
			Integer improve2 = backwardMap.get(newlimit);
			// 取出进步同样多前limit的学生
			if (improve1 == improve2) {
				for (Long id : backwardMap.keySet()) {
					if (backwardMap.get(id) == improve1) {
						if (!userIds.contains(id)) {
							userIds.add(id);
						}
					}
				}
			}
		} else {
			for (int i = 0; i < list_Data.size(); i++) {
				userIds.add(list_Data.get(i).getKey());
			}
		}
		Map<String, Map<Long, Integer>> map = new HashMap<String, Map<Long, Integer>>();
		if (CollectionUtils.isNotEmpty(userIds)) {
			// 退步了多少名
			Map<Long, Integer> map1 = new HashMap<Long, Integer>();
			// 本次对应的排名
			Map<Long, Integer> map2 = new HashMap<Long, Integer>();
			for (Long userId : userIds) {
				map1.put(userId, backwardMap.get(userId));
				map2.put(userId, backRankMap.get(userId));
			}
			map.put("backRankMap", map1);
			map.put("rankMap", map2);
		}
		return map;
	}

	@Override
	public Integer countToDoHk(Long studentId) {
		return stuHkRepo.find("$countToDoHk", Params.param("studentId", studentId)).get(Integer.class);
	}

	@Override
	public CursorPage<Long, Map> queryStuHk(ZyStudentHomeworkQuery query, CursorPageable<Long> pageable) {
		Params params = Params.param("studentId", query.getStudentId());
		if (query.getStatus().size() > 1) {
			Set<Integer> statusSet = new HashSet<Integer>(query.getStatus().size());
			for (StudentHomeworkStatus s : query.getStatus()) {
				statusSet.add(s.getValue());
			}
			params.put("statuses", statusSet);
		} else {
			params.put("status", query.getStatus().iterator().next().getValue());
		}
		if (query.isMobileIndex()) {
			params.put("isMobileIndex", 1);
		} else {
			params.put("isMobileIndex", 0);
		}
		if (query.getClassId() != null) {
			params.put("clazzId", query.getClassId());
		}
		if ("startTime".equals(query.getCursorType())) {
			if (pageable.getCursor() != null && pageable.getCursor().longValue() < Long.MAX_VALUE) {
				params.put("startTime", new Date(pageable.getCursor()));
			}
			return stuHkRepo.find("$queryStuHkByStartTimeCursor", params).fetch(pageable,

					Map.class, new CursorGetter<Long, Map>() {
						@Override
						public Long getCursor(Map bean) {
							return ((Date) bean.get("start_time")).getTime();
						}
					});
		} else {
			return stuHkRepo.find("$queryStuHkByCursor", params).fetch(pageable, Map.class,
					new CursorGetter<Long, Map>() {
						@Override
						public Long getCursor(Map bean) {
							return Long.parseLong(String.valueOf(bean.get("id")));
						}
					});
		}
	}

	@Override
	public long countAllHomeworksNoHoliday(long studentId, Set<StudentHomeworkStatus> status) {
		Params params = Params.param("studentId", studentId);
		if (status.size() > 0) {
			Set<Integer> statusSet = new HashSet<Integer>(status.size());
			for (StudentHomeworkStatus s : status) {
				statusSet.add(s.getValue());
			}
			params.put("statuses", statusSet);
		} else {
			params.put("status", status.iterator().next().getValue());
		}
		return stuHkRepo.find("$countAllHomeworksNoHoliday", params).count();
	}

	@Override
	public CursorPage<Long, StudentHomework> queryNotCompleteStudentHomework(CursorPageable<Long> pageable) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, -30);
		Date bt = cal.getTime(); // 提交30分钟仍处于未批改或者自动批改中的作业
		cal.add(Calendar.HOUR_OF_DAY, -2);
		Date et = cal.getTime(); // 处理2小时内的数据
		return stuHkRepo.find("$queryNotCompleteStudentHomework", Params.param("bt", bt).put("et", et)).fetch(pageable);
	}
}
