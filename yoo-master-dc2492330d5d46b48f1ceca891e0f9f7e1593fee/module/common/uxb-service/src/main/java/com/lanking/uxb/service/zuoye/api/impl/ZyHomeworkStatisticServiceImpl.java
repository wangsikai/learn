package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDataRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathInteractionRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

@SuppressWarnings("unchecked")
@Service
@Transactional(readOnly = true)
public class ZyHomeworkStatisticServiceImpl extends ZyHomeworkStatistic2ServiceImpl
		implements ZyHomeworkStatisticService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private ZyStudentHomeworkService stuHomeworkService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Transactional(readOnly = true)
	boolean staticOK(long homeworkId) {
		boolean ok = true;
		List<StudentHomework> shs = stuHkService.listByHomework(homeworkId);
		for (StudentHomework sh : shs) {
			if (sh.getSubmitAt() != null && sh.getStuSubmitAt() != null && sh.getRank() == null) {
				ok = false;
				break;
			}
		}
		return ok;
	}

	void asyncMq(Homework hk) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				long sleep = 0;
				while (sleep < 120000) {
					boolean ok = staticOK(hk.getId());
					if (ok) {
						break;
					} else {
						try {
							Thread.sleep(10000);
						} catch (InterruptedException ignore) {
						}
						sleep += 10000;
					}
				}
				try {
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("homeworkId", hk.getId());
					jsonObject.put("classId", hk.getHomeworkClassId());
					mqSender.send(MqYoomathInteractionRegistryConstants.EX_YM_INTERACTION,
							MqYoomathInteractionRegistryConstants.RK_YM_INTERACTION,
							MQ.builder().data(jsonObject).build());

					// 发送统计消息
					JSONObject object = new JSONObject();
					object.put("classId", hk.getHomeworkClassId());
					object.put("homeworkId", hk.getId());
					mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
							MqYoomathDataRegistryConstants.RK_YM_DATA_HOMEWORKSTUDENTCLAZZSTAT,
							MQ.builder().data(object).build());
					mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
							MqYoomathDataRegistryConstants.RK_YM_DATA_HKCLAZZKNOWPOINTSTAT,
							MQ.builder().data(object).build());
					mqSender.send(MqYoomathDataRegistryConstants.EX_YM_DATA,
							MqYoomathDataRegistryConstants.RK_YM_DATA_HKSTUCLAZZKNOWPOINTSTAT,
							MQ.builder().data(object).build());

					// 师生互动,班级前五
					jsonObject.put("type", InteractionType.CLASS_HOMEWORK_TOP5);
					mqSender.send(MqYoomathInteractionRegistryConstants.EX_YM_INTERACTION,
							MqYoomathInteractionRegistryConstants.RK_YM_INTERACTION,
							MQ.builder().data(jsonObject).build());

					// 教学诊断
					mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
							MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_CLASS_HOMEWORK,
							MQ.builder().data(jsonObject).build());

					// 学习诊断
					mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
							MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK,
							MQ.builder().data(jsonObject).build());
					mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
							MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_HOMEWORK_ALL,
							MQ.builder().data(jsonObject).build());
				} catch (Exception e) {
					logger.error("issue homework mq send error:", e);
				}
			}
		});
	}

	@Transactional
	@Async
	@Override
	public void asyncStaticHomework(long homeworkId) {
		Homework homework = hkService.get(homeworkId);
		try {
			updateStudentHomeworkStat(homeworkId);
			updateTeacherHomeworkStat(homeworkId);

			// 注意该统计必须在updateStudentHomeworkStat之后
			rankingStudentHomework(homeworkId);
			rankingStudent(homework);
		} catch (Exception e) {
			logger.error("issue homework async static error:", e);
		}

		try {
			// 单次作业排名前五
			coinsService.earn(CoinsAction.ONE_HOMEWORK_TOP5, null, 0, Biz.HOMEWORK, homeworkId);
			// 单次作业进步最快
			coinsService.earn(CoinsAction.MOST_IMPROVED_STU, null, 0, Biz.CLASS, homework.getHomeworkClassId());

			// 师生互动,单次排名前五,单次作业进步最快,单次作业退步最快,三次没有提交
			// 单次作业排名前5（班级）用户任务进度 -- 20170604 senhao.wang
			List<StudentHomework> stuHs = stuHomeworkService.listTop5ByHomework(homeworkId, null);
			for (StudentHomework sh : stuHs) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("userId", sh.getStudentId());
				Map<String, Object> params = new HashMap<String, Object>(1);
				params.put("commitCount", homework.getCommitCount().intValue());
				params.put("rank", sh.getRank());
				params.put("stuHkId", sh.getId().longValue());
				messageObj.put("params", params);

				// 单次作业排名前5（班级）
				messageObj.put("taskCode", 101020012);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

				// 近3次作业排名前5（班级）
				messageObj.put("taskCode", 101020013);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());

				// 近5次作业排名前5（班级）
				messageObj.put("taskCode", 101020014);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}

			// 获取进步最快的学生
			Map<String, Map<Long, Integer>> map = stuHomeworkService.getMostImprovedMap(homework.getHomeworkClassId(),
					1);
			if (CollectionUtils.isNotEmpty(map)) {
				JSONObject messageObj = new JSONObject();
				messageObj.put("userId", map.get("improveRankMap").keySet().iterator().next());
				messageObj.put("taskCode", 101020015);
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			}

		} catch (Exception e) {
			logger.error("issue homework coins earn error:", e);
		}
		asyncMq(homework);
	}

	@Transactional
	@Async
	@Override
	public void asyncDeleteHomework(long homeworkId) {
		updateStudentHomeworkStat(homeworkId);
		updateTeacherHomeworkStat(homeworkId);
	}

	@Transactional
	@Override
	@Deprecated
	public void staticAfterLastCommit(long homeworkId) {
		Homework homework = hkService.get(homeworkId);
		List<StudentHomework> shs = stuHkService.listByHomework(homeworkId);
		for (StudentHomework sh : shs) {
			if (sh.getSubmitAt() == null || sh.getStuSubmitAt() == null) {// 如果不是有效提交则不统计正确率
				continue;
			}
			List<StudentHomeworkQuestion> studentHomeworkQuestions = stuHkQuestionService.find(sh.getId());
			int rightCount = 0;
			int wrongCount = 0;
			int halfWrongCount = 0;

			int totalPercent = 0;
			int totalDenominator = 0;

			for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
				if (studentHomeworkQuestion.getSubFlag()) {
					continue;
				}
				if (studentHomeworkQuestion.isCorrect()) {
					if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT
							|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.UNKNOW) {
						rightCount = 0;
						wrongCount = 0;
						halfWrongCount = 0;
						break;
					}
				} else {
					if (studentHomeworkQuestion.getType() == Type.QUESTION_ANSWERING) {// 简答题
						totalDenominator += 2;
						if (studentHomeworkQuestion.getRightRate() != null) {
							int rightRate = studentHomeworkQuestion.getRightRate().intValue();
							totalPercent += rightRate * 2;
							if (rightRate == 100) {
								rightCount++;
							} else if (rightRate == 0) {
								wrongCount++;
							} else {
								halfWrongCount++;
							}
						}
					} else if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) {// 填空题
						totalDenominator += 1;
						if (studentHomeworkQuestion.getRightRate() == null) {
							if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.RIGHT) {
								rightCount++;
								totalPercent += 100;
							} else if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG) {
								wrongCount++;
								totalPercent += 0;
							}
						} else {
							int rightRate = studentHomeworkQuestion.getRightRate().intValue();
							totalPercent += rightRate;
							if (rightRate == 100) {
								rightCount++;
							} else if (rightRate == 0) {
								wrongCount++;
							} else {
								halfWrongCount++;
							}

						}
					} else {
						totalDenominator += 1;
						if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.RIGHT) {
							rightCount++;
							totalPercent += 100;
						} else if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG) {
							wrongCount++;
							totalPercent += 0;
						}
					}
				}
			}
			if (rightCount + wrongCount + halfWrongCount == homework.getQuestionCount()) {
				sh.setRightCount(rightCount);
				sh.setWrongCount(wrongCount);
				sh.setHalfWrongCount(halfWrongCount);
				sh.setRightRate(
						BigDecimal.valueOf(totalPercent * 1f / totalDenominator).setScale(0, BigDecimal.ROUND_HALF_UP));
				stuHkRepo.save(sh);
			}
		}
	}

	@Transactional
	@Override
	@Deprecated
	public void staticAfterAutoCorrect(long stuHkId) {
		StudentHomework sh = stuHkService.get(stuHkId);
		if (sh.getSubmitAt() == null || sh.getStuSubmitAt() == null) {// 如果不是有效提交则不统计正确率
			return;
		}

		List<StudentHomeworkQuestion> studentHomeworkQuestions = stuHkQuestionService.find(sh.getId());
		int rightCount = 0;
		int wrongCount = 0;
		int halfWrongCount = 0;

		int totalPercent = 0;
		int totalDenominator = 0;

		boolean doStatic = true;// 是否要统计正确率
		boolean autoManualAllCorrected = true;// 是否不需要后台处理了

		for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
			if (studentHomeworkQuestion.getSubFlag()) {
				continue;
			}
			if (doStatic) {
				if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.RIGHT) {
					// 可以计算
				} else if (studentHomeworkQuestion.isManualCorrect()
						&& studentHomeworkQuestion.getResult() != HomeworkAnswerResult.UNKNOW) {
					// 可以计算
				} else {
					doStatic = false;
				}
			}
			if (doStatic && !studentHomeworkQuestion.isCorrect()) {// 计算正确率的相关统计(非订正题)
				if (studentHomeworkQuestion.getType() == Type.QUESTION_ANSWERING) {// 简答题
					totalDenominator += 2;
					if (studentHomeworkQuestion.getRightRate() != null) {
						int rightRate = studentHomeworkQuestion.getRightRate().intValue();
						totalPercent += rightRate * 2;
						if (rightRate == 100) {
							rightCount++;
						} else if (rightRate == 0) {
							wrongCount++;
						} else {
							halfWrongCount++;
						}
					}
				} else if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) {// 填空题
					totalDenominator += 1;
					if (studentHomeworkQuestion.getRightRate() == null) {
						if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.RIGHT) {
							rightCount++;
							totalPercent += 100;
						} else if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG) {
							wrongCount++;
							totalPercent += 0;
						}
					} else {
						int rightRate = studentHomeworkQuestion.getRightRate().intValue();
						totalPercent += rightRate;
						if (rightRate == 100) {
							rightCount++;
						} else if (rightRate == 0) {
							wrongCount++;
						} else {
							halfWrongCount++;
						}
					}
				} else {
					totalDenominator += 1;
					if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.RIGHT) {
						rightCount++;
						totalPercent += 100;
					} else if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG) {
						wrongCount++;
						totalPercent += 0;
					}
				}
			}
			if (autoManualAllCorrected && !studentHomeworkQuestion.isManualCorrect()) {
				autoManualAllCorrected = false;
			}
		}
		if (doStatic) {
			sh.setRightCount(rightCount);
			sh.setWrongCount(wrongCount);
			sh.setHalfWrongCount(halfWrongCount);
			sh.setRightRate(
					BigDecimal.valueOf(totalPercent * 1f / totalDenominator).setScale(0, BigDecimal.ROUND_HALF_UP));
			sh.setAutoManualAllCorrected(autoManualAllCorrected);
			stuHkRepo.save(sh);
		} else if (autoManualAllCorrected) {
			sh.setAutoManualAllCorrected(autoManualAllCorrected);
			stuHkRepo.save(sh);
		}
	}

	/**
	 * @since 教师端 v1.3.0 2017-7-5 学生退出班级不再参与班级的整体统计
	 */
	@Transactional
	@Override
	public void updateTeacherHomeworkStat(long homeworkId) {
		Homework homework = hkRepo.get(homeworkId);
		Params params = Params.param();
		if (homework.getHomeworkClassId() != null) {
			params.put("classId", homework.getHomeworkClassId());
		}
		Map<String, Object> map = stuHkRepo.find("$zyStaticAvgHomeworkTimeForTeaStat", params).get(Map.class);

		HomeworkStat stat = hkStatService.findOne(homework.getCreateId(), homework.getHomeworkClassId());

		if (stat != null) {
			if (map != null && map.get("avg_time") != null && map.get("avg_rate") != null) {
				stat.setHomeworkTime(((BigDecimal) map.get("avg_time")).intValue());
				stat.setRightRate(((BigDecimal) map.get("avg_rate")).setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			Params p = Params.param("createId", homework.getCreateId());
			if (homework.getHomeworkClassId() != null) {
				p.put("classId", homework.getHomeworkClassId());
			}
			stat.setHomeWorkNum(hkRepo.find("$zyCountHomework", p).count());
			stat.setDoingNum(hkRepo.find("$zyCountDoingHomework", p).count());
			// 计算完成率
			BigDecimal completionRate = null;
			Map<String, Object> completionRateMap = hkRepo.find("$zyCountIssuedHomeworkCompletionRate", params)
					.get(Map.class);
			if (completionRateMap != null && completionRateMap.get("distribute_count") != null
					&& completionRateMap.get("commit_count") != null) {
				BigDecimal distributeCount = (BigDecimal) completionRateMap.get("distribute_count");
				BigDecimal commitCount = (BigDecimal) completionRateMap.get("commit_count");
				if (distributeCount.intValue() == 0 || commitCount.intValue() == 0) {
					completionRate = BigDecimal.valueOf(0);
				} else {
					completionRate = BigDecimal.valueOf(commitCount.intValue() * 100f / distributeCount.intValue())
							.setScale(0, BigDecimal.ROUND_HALF_UP);
				}
			}
			stat.setCompletionRate(completionRate);
			hkStatRepo.save(stat);
		}
	}

	@Transactional
	@Override
	public void updateTeacherHomeworkStatByClass(long clazzId) {
		HomeworkClazz homeworkClazz = homeworkClassService.get(clazzId);
		if (homeworkClazz == null) {
			return;
		}

		Params params = Params.param();
		params.put("classId", clazzId);
		Map<String, Object> map = stuHkRepo.find("$zyStaticAvgHomeworkTimeForTeaStat", params).get(Map.class);

		HomeworkStat stat = hkStatService.findOne(homeworkClazz.getTeacherId(), clazzId);
		if (stat != null) {
			if (map != null && map.get("avg_time") != null && map.get("avg_rate") != null) {
				stat.setHomeworkTime(((BigDecimal) map.get("avg_time")).intValue());
				stat.setRightRate(((BigDecimal) map.get("avg_rate")).setScale(2, BigDecimal.ROUND_HALF_UP));
			} else {
				stat.setHomeworkTime(0);
				stat.setRightRate(null);
			}
			Params p = Params.param("createId", homeworkClazz.getTeacherId());
			p.put("classId", clazzId);
			stat.setHomeWorkNum(hkRepo.find("$zyCountHomework", p).count());
			stat.setDoingNum(hkRepo.find("$zyCountDoingHomework", p).count());
			// 计算完成率
			BigDecimal completionRate = null;
			Map<String, Object> completionRateMap = hkRepo.find("$zyCountIssuedHomeworkCompletionRate", params)
					.get(Map.class);
			if (completionRateMap != null && completionRateMap.get("distribute_count") != null
					&& completionRateMap.get("commit_count") != null) {
				BigDecimal distributeCount = (BigDecimal) completionRateMap.get("distribute_count");
				BigDecimal commitCount = (BigDecimal) completionRateMap.get("commit_count");
				if (distributeCount.intValue() == 0 || commitCount.intValue() == 0) {
					completionRate = BigDecimal.valueOf(0);
				} else {
					completionRate = BigDecimal.valueOf(commitCount.intValue() * 100f / distributeCount.intValue())
							.setScale(0, BigDecimal.ROUND_HALF_UP);
				}
			}
			stat.setCompletionRate(completionRate);
			hkStatRepo.save(stat);
		}
	}

	@SuppressWarnings("rawtypes")
	@Transactional
	@Override
	public void updateStudentHomeworkStat(long homeworkId) {
		Homework homework = hkRepo.get(homeworkId);
		Params params = Params.param();
		if (homework.getHomeworkClassId() != null) {
			params.put("classId", homework.getHomeworkClassId());
		}
		List<Map> list = stuHkRepo.find("$zyStaticAvgHomeworkTimeForStuStat", params).list(Map.class);

		// 批量获取班级学生作业统计
		Map<Long, StudentHomeworkStat> studentHomeworkStatMap = stuHkStatService
				.findByClazzId(homework.getHomeworkClassId());

		Set<Long> stuIds = Sets.newHashSet();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map map : list) {
				long studentId = ((BigInteger) map.get("student_id")).longValue();
				StudentHomeworkStat stat = studentHomeworkStatMap.get(studentId);
				if (stat != null) {
					stuIds.add(studentId);
					Params p = Params.param("studentId", studentId);
					p.put("classId", homework.getHomeworkClassId());
					stat.setHomeworkTime(((BigDecimal) map.get("avg_time")).intValue());
					if (map.get("avg_rate") == null) {
						stat.setRightRate(BigDecimal.ZERO);
					} else {
						stat.setRightRate(((BigDecimal) map.get("avg_rate")).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					stat.setHomeWorkNum(stuHkRepo.find("$zyCountHomework", p).count());
					stat.setTodoNum(stuHkRepo.find("$zyCountToDoHomework", p).count());
					stat.setOverdueNum(stuHkRepo.find("$zyCountOverdueHomework", p).count());
					stuHkStatRepo.save(stat);
				}
			}
		}

		for (Long stuId : studentHomeworkStatMap.keySet()) {
			if (!stuIds.contains(stuId)) {
				StudentHomeworkStat stat = studentHomeworkStatMap.get(stuId);
				if (stat != null) {
					Params p = Params.param("studentId", stuId);
					if (homework.getHomeworkClassId() != null) {
						p.put("classId", homework.getHomeworkClassId());
					}
					stat.setHomeWorkNum(stuHkRepo.find("$zyCountHomework", p).count());
					stat.setTodoNum(stuHkRepo.find("$zyCountToDoHomework", p).count());
					stat.setOverdueNum(stuHkRepo.find("$zyCountOverdueHomework", p).count());
					stuHkStatRepo.save(stat);
				}
			}

		}
		// 金币服务
		coinsService.earn(CoinsAction.STU_HOMEWORK_RESULT, null, 0, Biz.HOMEWORK, homeworkId);
	}

	@Transactional
	@Override
	public void rankingStudentHomework(long homeworkId) {
		List<StudentHomework> studentHomeworks = stuHkRepo
				.find("$zyStaticRightRateRank", Params.param("homeworkId", homeworkId)).list();
		if (CollectionUtils.isNotEmpty(studentHomeworks)) {
			int index = 0;
			StudentHomework preStuHk = null;
			for (StudentHomework studentHomework : studentHomeworks) {
				if (studentHomework.getSubmitAt() == null || studentHomework.getStuSubmitAt() == null) {
					continue;
				}
				if (preStuHk == null) {
					studentHomework.setRank(1);
				} else {
					if (studentHomework.getRightRate().doubleValue() == preStuHk.getRightRate().doubleValue()) {
						studentHomework.setRank(preStuHk.getRank());
					} else {
						studentHomework.setRank(index + 1);
					}
				}
				stuHkRepo.save(studentHomework);
				preStuHk = studentHomework;
				index++;
			}
		}
	}

	@Transactional
	@Override
	public void rankingStudent(Homework homework) {
		Params params = Params.param();
		if (homework.getHomeworkClassId() != null) {
			params.put("classId", homework.getHomeworkClassId());
		}
		List<StudentHomeworkStat> stuHkStats = stuHkStatRepo.find("$zyStaticRightRateRank", params).list();
		if (CollectionUtils.isNotEmpty(stuHkStats)) {
			int index = 0;
			StudentHomeworkStat preStuHkStat = null;
			for (StudentHomeworkStat stuHkStat : stuHkStats) {
				if (preStuHkStat == null) {
					stuHkStat.setRank(1);
				} else {
					if (preStuHkStat.getRightRate() != null && stuHkStat.getRightRate() == null) {
						stuHkStat.setRank(index + 1);
					} else if (preStuHkStat.getRightRate() == null && stuHkStat.getRightRate() == null) {
						stuHkStat.setRank(preStuHkStat.getRank());
					} else if (preStuHkStat.getRightRate().compareTo(stuHkStat.getRightRate()) == 0) {
						stuHkStat.setRank(preStuHkStat.getRank());
					} else {
						stuHkStat.setRank(index + 1);
					}
				}
				stuHkStatRepo.save(stuHkStat);
				preStuHkStat = stuHkStat;
				index++;
			}
		}
	}

}
