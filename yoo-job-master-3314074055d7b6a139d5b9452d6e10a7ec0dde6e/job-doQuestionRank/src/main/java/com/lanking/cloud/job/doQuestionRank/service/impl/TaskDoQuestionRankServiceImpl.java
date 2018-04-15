package com.lanking.cloud.job.doQuestionRank.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.constants.MqJobServiceRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.job.doQuestionRank.DAO.DoQuestionClassRankDAO;
import com.lanking.cloud.job.doQuestionRank.DAO.DoQuestionSchoolRankDAO;
import com.lanking.cloud.job.doQuestionRank.DAO.HomeworkStudentClazzDAO;
import com.lanking.cloud.job.doQuestionRank.DAO.StudentDAO;
import com.lanking.cloud.job.doQuestionRank.DAO.StudentQuestionAnswerDAO;
import com.lanking.cloud.job.doQuestionRank.DAO.UserDAO;
import com.lanking.cloud.job.doQuestionRank.service.TaskDoQuestionRankService;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;

import httl.util.CollectionUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(readOnly = true)
@Service
public class TaskDoQuestionRankServiceImpl implements TaskDoQuestionRankService {

	@Qualifier("doQuestionRankUserDAO")
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private StudentQuestionAnswerDAO studentQuestionAnswerDAO;
	@Autowired
	private HomeworkStudentClazzDAO homeworkStudentClazzDAO;
	@Autowired
	private DoQuestionClassRankDAO doQuestionClassRankDAO;
	@Autowired
	@Qualifier("doQuestionRankStudentDAO")
	private StudentDAO studentDAO;
	@Autowired
	private DoQuestionSchoolRankDAO doQuestionSchoolRankDAO;
	@Autowired
	private MqSender mqSender;

	@Override
	@Transactional(readOnly = false)
	public void doQuestionStudentRank(int startInt, int endInt, Date startDate, Date endDate, List<Long> userIds) {
		List<StudentQuestionAnswer> answerList = studentQuestionAnswerDAO.taskStaticDoQuestionStudent(startDate,
				endDate, userIds, HomeworkAnswerResult.RIGHT);
		if (CollectionUtils.isNotEmpty(answerList)) {
			// 班级榜
			doQuestionClassRank(startInt, endInt, formatAnswer(answerList));
			// 校级榜
			doQuestionSchoolRank(startInt, endInt, formatAnswer(answerList));
		}

		log.info("doQuestionStudentRank end.");
	}

	/**
	 * 统计班级维度
	 * 
	 * @param startInt
	 * @param endInt
	 * @param answerMap
	 *            key:userId value:做题次数
	 */
	private void doQuestionClassRank(int startInt, int endInt, Map<Long, Long> answerMap) {
		if (answerMap == null) {
			return;
		}

		List<Long> userIds = Lists.newArrayList();
		for (Long key : answerMap.keySet()) {
			userIds.add(key);
		}

		// 取用户所在的班级
		Map<Long, List<HomeworkStudentClazz>> clazzMap = homeworkStudentClazzDAO.taskListclazz(userIds);

		for (Map.Entry<Long, Long> map : answerMap.entrySet()) {
			long userId = map.getKey();
			List<HomeworkStudentClazz> clazzs = clazzMap.get(userId);
			if (CollectionUtils.isEmpty(clazzs)) {
				continue;
			}
			// 查询DoQuestionClassRank表中数据是否存在
			for (HomeworkStudentClazz v : clazzs) {
				DoQuestionClassRank rank = doQuestionClassRankDAO.findStudentInClassRank(v.getClassId(), startInt,
						endInt, userId);
				if (rank == null) {
					rank = new DoQuestionClassRank();
					rank.setClassId(v.getClassId());
					rank.setEndDate(endInt);
					rank.setPraiseCount(0L);
					rank.setRightCount(map.getValue());
					rank.setRightCount0(map.getValue());
					rank.setStartDate(startInt);
					rank.setUserId(userId);
					rank.setPushStatus(Status.DISABLED);
				} else {
					rank.setRightCount0(map.getValue());
				}

				// 保存数据
				doQuestionClassRankDAO.save(rank);
			}
		}
	}

	/**
	 * 统计校级维度
	 * 
	 * @param startInt
	 * @param endInt
	 * @param answerMap
	 *            key:userId value:做题次数
	 */
	private void doQuestionSchoolRank(int startInt, int endInt, Map<Long, Long> answerMap) {
		if (answerMap == null) {
			return;
		}

		List<Long> userIds = Lists.newArrayList();
		for (Long key : answerMap.keySet()) {
			userIds.add(key);
		}

		// 根据userId取student信息
		Map<Long, Student> schoolMap = studentDAO.taskListStudent(userIds);
		for (Map.Entry<Long, Long> map : answerMap.entrySet()) {
			long userId = map.getKey();
			Student student = schoolMap.get(userId);
			if (student == null) {
				continue;
			}

			// 查询DoQuestionSchoolRank表中数据是否存在
			if (student.getSchoolId() == null || student.getSchoolId() <= 0) {
				continue;
			}

			DoQuestionSchoolRank rank = doQuestionSchoolRankDAO.findStudentInSchoolRank(student.getSchoolId(), startInt,
					endInt, userId);
			if (rank == null) {
				rank = new DoQuestionSchoolRank();
				rank.setSchoolId(student.getSchoolId());
				rank.setEndDate(endInt);
				rank.setPraiseCount(0L);
				rank.setRightCount(map.getValue());
				rank.setRightCount0(map.getValue());
				rank.setStartDate(startInt);
				rank.setUserId(userId);
				rank.setPhaseCode(student.getPhaseCode() == null ? 0 : student.getPhaseCode());
			} else {
				rank.setRightCount0(map.getValue());
			}

			// 保存数据
			doQuestionSchoolRankDAO.save(rank);
		}
	}

	/**
	 * 格式化answer对象
	 * 
	 * @param answerList
	 * @return map key:userId value:做题次数
	 */
	private Map<Long, Long> formatAnswer(List<StudentQuestionAnswer> answerList) {
		Map<Long, Long> data = new HashMap<>();
		for (StudentQuestionAnswer v : answerList) {
			Long key = v.getStudentId();
			if (data.containsKey(key)) {
				data.put(key, data.get(key) + 1);
			} else {
				data.put(key, 1L);
			}
		}

		return data;
	}

	@Override
	@Transactional(readOnly = false)
	public CursorPage<Long, DoQuestionClassRank> refreshClassData(int startInt, int endInt, long cursor,
			int fetchCount) {
		CursorPage<Long, DoQuestionClassRank> rankPage = doQuestionClassRankDAO.getAllRankPraiseByCursor(startInt,
				endInt, CP.cursor(cursor, fetchCount));
		if (rankPage.isNotEmpty()) {
			List<DoQuestionClassRank> rankList = rankPage.getItems();
			for (DoQuestionClassRank v : rankList) {
				v.setRank(v.getRank0());
				v.setRightCount(v.getRightCount0());
			}

			doQuestionClassRankDAO.saves(rankList);
		}

		return rankPage;
	}

	@Override
	@Transactional(readOnly = false)
	public Map<Long, String> statisClassRank(int startInt, int endInt) {
		// 先取班级
		int start = 0;
		int size = 20;

		List<Long> classIds = doQuestionClassRankDAO.getClassIds(startInt, endInt, start, size);
		Map<Long, String> pushInfo = new HashMap<>(); // 推送队列
		while (CollectionUtils.isNotEmpty(classIds)) {
			List<DoQuestionClassRank> ranks = doQuestionClassRankDAO.findStudentInClassRanks(classIds, startInt,
					endInt);
			if (CollectionUtils.isNotEmpty(ranks)) {
				Map<Long, List<DoQuestionClassRank>> rankMap = new HashMap<>();

				// 格式化数据
				for (DoQuestionClassRank v : ranks) {
					if (rankMap.containsKey(v.getClassId())) {
						List<DoQuestionClassRank> temp = rankMap.get(v.getClassId());
						temp.add(v);
						rankMap.put(v.getClassId(), temp);
					} else {
						List<DoQuestionClassRank> temp = Lists.newArrayList();
						temp.add(v);
						rankMap.put(v.getClassId(), temp);
					}
				}

				// 排序
				for (Map.Entry<Long, List<DoQuestionClassRank>> map : rankMap.entrySet()) {
					List<DoQuestionClassRank> classRanks = map.getValue();
					Collections.sort(classRanks, (x, y) -> y.getRightCount0().compareTo(x.getRightCount0()));

					List<Long> pushUsers = Lists.newArrayList(); // 推送userId
					for (int i = 0; i < classRanks.size(); i++) {
						classRanks.get(i).setRank0(i + 1);
						// 班级的top10要发推送
						// 查询pushStatus如果是Status.DISABLED说明没有推送过，发推送并且把pushStatus设置成Status.ENABLED
						if (classRanks.get(i).getRank0() <= 10
								&& classRanks.get(i).getPushStatus() == Status.DISABLED) {
							pushUsers.add(classRanks.get(i).getUserId());
							classRanks.get(i).setPushStatus(Status.ENABLED);
						}
					}

					// 推送,发送mq到uxb中处理
					if (CollectionUtils.isNotEmpty(pushUsers)) {
						List<String> users = Lists.newArrayList();
						for (Long v : pushUsers) {
							users.add(String.valueOf(v));
						}
						pushInfo.put(map.getKey(), String.join(",", users));
					}

					// 保存数据
					doQuestionClassRankDAO.saves(classRanks);
				}
			}

			// 取下一部分数据
			start = start + size;
			classIds = doQuestionClassRankDAO.getClassIds(startInt, endInt, start, size);
		}

		return pushInfo;
	}

	@Override
	@Transactional(readOnly = false)
	public List<Long> statisSchoolRank(int startInt, int endInt, int start, int size) {
		List<Long> schoolIds = getSchoolIds(startInt, endInt, start, size);
		if (CollectionUtils.isNotEmpty(schoolIds)) {
			for (Long schoolId : schoolIds) {
				List<DoQuestionSchoolRank> rankList = Lists.newArrayList();
				// 用游标取
				CursorPage<Long, DoQuestionSchoolRank> rankPage = getSchoolRankPraiseBySchoolId(startInt, endInt,
						schoolId, size, Long.MAX_VALUE);
				while (rankPage.isNotEmpty()) {
					rankList.addAll(rankPage.getItems());
					// 取下一页数据
					rankPage = getSchoolRankPraiseBySchoolId(startInt, endInt, schoolId, size,
							rankPage.getNextCursor());
				}

				// 处理数据
				// 排序
				Collections.sort(rankList, (x, y) -> y.getRightCount0().compareTo(x.getRightCount0()));
				for (int i = 0; i < rankList.size(); i++) {
					rankList.get(i).setRank0(i + 1);
					// 保存数据
					doQuestionSchoolRankDAO.save(rankList.get(i));
				}
			}
		}

		return schoolIds;
	}

	@Override
	@Transactional(readOnly = false)
	public CursorPage<Long, DoQuestionSchoolRank> refreshSchoolData(int startDate, int endDate, int fetchCount,
			long cursor) {
		CursorPage<Long, DoQuestionSchoolRank> rankPage = getSchoolRankPraiseBySchoolId(startDate, endDate, null,
				fetchCount, cursor);

		List<DoQuestionSchoolRank> rankList = rankPage.getItems();
		for (DoQuestionSchoolRank v : rankList) {
			v.setRank(v.getRank0());
			v.setRightCount(v.getRightCount0());
		}
		doQuestionSchoolRankDAO.saves(rankList);

		return rankPage;
	}

	@Override
	public void sendClassRankingPush(Map<Long, String> pushMap) {
		if (pushMap == null) {
			return;
		}
		for (Map.Entry<Long, String> map : pushMap.entrySet()) {
			JSONObject messageObj = new JSONObject();
			messageObj.put("classId", map.getKey());
			messageObj.put("userIds", map.getValue());
			mqSender.send(MqJobServiceRegistryConstants.EX_JOBS, MqJobServiceRegistryConstants.RK_JOBS_RANKING_PUSH,
					MQ.builder().data(messageObj).build());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryUserId(int fetchCount, long cursor) {
		return userDAO.queryUserId(CP.cursor(cursor, fetchCount));
	}

	@Override
	public CursorPage<Long, DoQuestionClassRank> getAllRankPraiseByCursor(int startDate, int endDate, int fetchCount,
			long cursor) {
		return doQuestionClassRankDAO.getAllRankPraiseByCursor(startDate, endDate, CP.cursor(cursor, fetchCount));
	}

	@Override
	public List<Long> getSchoolIds(int startDate, int endDate, int start, int size) {
		return doQuestionSchoolRankDAO.getSchoolIds(startDate, endDate, start, size);
	}

	@Override
	public CursorPage<Long, DoQuestionSchoolRank> getSchoolRankPraiseBySchoolId(int startDate, int endDate,
			Long schoolId, int fetchCount, long cursor) {
		return doQuestionSchoolRankDAO.getSchoolRankPraiseBySchoolId(startDate, endDate, schoolId,
				CP.cursor(cursor, fetchCount));
	}

}
