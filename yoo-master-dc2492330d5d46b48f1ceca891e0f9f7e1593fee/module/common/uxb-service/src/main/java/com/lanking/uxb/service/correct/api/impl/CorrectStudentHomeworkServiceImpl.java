package com.lanking.uxb.service.correct.api.impl;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.correct.api.CorrectHomeworkService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CorrectStudentHomeworkServiceImpl implements CorrectStudentHomeworkService {

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Autowired
	private CorrectStudentHomeworkQuestionService studentHomeworkQuestionService;
	@Autowired
	private CorrectHomeworkService correctHomeworkService;

	@Override
	@Transactional
	public StudentHomework getStudentHomework(long studentHomeworkId) {
		return studentHomeworkRepo.get(studentHomeworkId);
	}

	@Override
	@Transactional(readOnly = true)
	public List<StudentHomework> mgetStudentHomework(Collection<Long> studentHomeworkIds) {
		return studentHomeworkRepo.mgetList(studentHomeworkIds);
	}

	@Override
	@Transactional
	public List<StudentHomework> listByHomework(long homeworkId) {
		return studentHomeworkRepo.find("$findStudentHomework", Params.param("homeworkId", homeworkId)).list();
	}

	@Override
	@Transactional
	public void setStudentHomeworkCorrectStatus(long studentHomeworkId, StudentHomeworkCorrectStatus correctStatus) {
		studentHomeworkRepo.execute("$setStudentHomeworkCorrectStatus",
				Params.param("studentHomeworkId", studentHomeworkId).put("correctStatus", correctStatus.getValue()));
	}

	/**
	 * 统计学生作业正确率.
	 */
	@Override
	@Transactional
	public void staticStudentHomeworkRightRate(long studentHomeworkId) {
		StudentHomework studentHomework = studentHomeworkRepo.get(studentHomeworkId);
		List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
				.getAllStudentHomeworkQuestions(studentHomeworkId);

		if (studentHomework.getSubmitAt() == null || studentHomework.getStuSubmitAt() == null) {
			// 如果不是有效提交则不统计正确率，仅统计订正后的正确率（后台强制提交时，学生一题未答，不算有效提交）
			this.staticStudentHomeworkCorrectRightRate(studentHomeworkId, studentHomeworkQuestions);
			return;
		}

		// 首先将新的订正题过滤出来，非订正题有init、unknown的题目不计算正确率
		boolean hasNoResult = false;
		Map<Long, StudentHomeworkQuestion> reviseQuestions = Maps.newHashMap();
		for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
			if (studentHomeworkQuestion.isNewCorrect()) {
				reviseQuestions.put(studentHomeworkQuestion.getQuestionId(), studentHomeworkQuestion);
			}
		}

		int rightCount = 0;
		int wrongCount = 0;
		int halfWrongCount = 0;

		int totalPercent = 0;
		int totalDenominator = 0;
		int totalPercent_correct = 0; // 订正后的数据
		int totalDenominator_correct = 0; // 订正后的数据

		// 旧的订正题都是上份作业的，不参与计算，但是新的订正题都是本次作业的，需要参与计算订正后的正确率
		// 首先统计原始正确率
		for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
			if (!studentHomeworkQuestion.isCorrect() && !studentHomeworkQuestion.isNewCorrect()) {
				if (studentHomeworkQuestion.getResult() == null
						|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT
						|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.UNKNOW) {
					hasNoResult = true;
					continue;
				}
				if (studentHomeworkQuestion.getType() == Type.QUESTION_ANSWERING) { // 简答题
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
				} else if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) { // 填空题
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

		// 统计订正后的正确率
		if (reviseQuestions.size() > 0) {
			for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
				if (!studentHomeworkQuestion.isCorrect() && !studentHomeworkQuestion.isNewCorrect()) {
					StudentHomeworkQuestion reviseQuestion = reviseQuestions
							.get(studentHomeworkQuestion.getQuestionId());
					if (reviseQuestion != null && reviseQuestion.getResult() != HomeworkAnswerResult.INIT
							&& reviseQuestion.getResult() != HomeworkAnswerResult.UNKNOW
							&& reviseQuestion.getRightRate() != null) {
						studentHomeworkQuestion = reviseQuestion; // 本题对应的订正题
					}

					Integer rightRate = studentHomeworkQuestion.getRightRate();
					HomeworkAnswerResult result = studentHomeworkQuestion.getResult();
					if (studentHomeworkQuestion.getResult() == null
							|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT) {
						if (studentHomeworkQuestion.isNewCorrect() && studentHomeworkQuestion.isRevised()) {
							// 订正题不会做，认为错
							rightRate = 0;
							result = HomeworkAnswerResult.WRONG;
						} else {
							continue;
						}
					} else if (studentHomeworkQuestion.getResult() == HomeworkAnswerResult.UNKNOW) {
						continue;
					}

					if (studentHomeworkQuestion.getType() == Type.QUESTION_ANSWERING) { // 简答题
						totalDenominator_correct += 2;
						if (rightRate != null) {
							totalPercent_correct += rightRate * 2;
						}
					} else if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) { // 填空题
						totalDenominator_correct += 1;
						if (rightRate == null) {
							if (result == HomeworkAnswerResult.RIGHT) {
								totalPercent_correct += 100;
							} else if (result == HomeworkAnswerResult.WRONG) {
								totalPercent_correct += 0;
							}
						} else {
							totalPercent_correct += rightRate;
						}
					} else {
						totalDenominator_correct += 1;
						if (result == HomeworkAnswerResult.RIGHT) {
							totalPercent_correct += 100;
						} else if (result == HomeworkAnswerResult.WRONG) {
							totalPercent_correct += 0;
						}
					}
				}
			}
		}

		Date correctCompleteTime = null; // 学生作业首次批改完成的时间
		if (!hasNoResult && studentHomework.getRightRate() == null) {
			// 首次批改完成，记录批改完成的学生作业数量
			correctHomeworkService.addCorrectingCount(studentHomework.getHomeworkId());
			correctCompleteTime = new Date();
		}
		Params params = Params.param("studentHomeworkId", studentHomeworkId).put("rightCount", rightCount)
				.put("wrongCount", wrongCount).put("halfWrongCount", halfWrongCount);
		if (!hasNoResult) {
			params.put("rightRate",
					BigDecimal.valueOf(totalPercent * 1f / totalDenominator).setScale(0, BigDecimal.ROUND_HALF_UP));
			if (totalDenominator_correct > 0) {
				params.put("rightRateCorrect", BigDecimal.valueOf(totalPercent_correct * 1f / totalDenominator_correct)
						.setScale(0, BigDecimal.ROUND_HALF_UP));
			}
			if (correctCompleteTime != null) {
				params.put("correctCompleteTime", correctCompleteTime);
			}
		}
		studentHomeworkRepo.execute("$updateStudentHomeworkRightRate", params);
	}

	/**
	 * 统计订正后的作业正确率（非有效提交作业的情况）.
	 */
	private void staticStudentHomeworkCorrectRightRate(long studentHomeworkId,
			List<StudentHomeworkQuestion> studentHomeworkQuestions) {
		int correctQuestionResultCount = 0; // 订正题有批改结果个数
		int correctQuestionCount = 0; // 订正题的个数
		List<StudentHomeworkQuestion> reviseQuestions = Lists.newArrayList();
		for (StudentHomeworkQuestion studentHomeworkQuestion : studentHomeworkQuestions) {
			if (studentHomeworkQuestion.isNewCorrect()) {
				reviseQuestions.add(studentHomeworkQuestion);
				if (studentHomeworkQuestion.getResult() != null
						&& studentHomeworkQuestion.getResult() != HomeworkAnswerResult.INIT
						&& studentHomeworkQuestion.getResult() != HomeworkAnswerResult.UNKNOW) {
					correctQuestionResultCount++;
				}
				if (studentHomeworkQuestion.isNewCorrect() && (studentHomeworkQuestion.getResult() == null
						|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT)) {
					// 学生不会做
					correctQuestionResultCount++;
				}
				correctQuestionCount++;
			}
		}
		// 对于一题未答被动提交的作业，只有全部订正题都有批改结果后再计算订正后的正确率
		if (correctQuestionResultCount > 0 && correctQuestionResultCount == correctQuestionCount) {
			int totalPercent_correct = 0; // 订正后的数据
			int totalDenominator_correct = 0; // 订正后的数据
			for (StudentHomeworkQuestion studentHomeworkQuestion : reviseQuestions) {

				Integer rightRate = studentHomeworkQuestion.getRightRate();
				HomeworkAnswerResult result = studentHomeworkQuestion.getResult();
				if (studentHomeworkQuestion.isNewCorrect() && (studentHomeworkQuestion.getResult() == null
						|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT)) {
					// 如果学生不会做，认为错，正确率为0正确率为0
					rightRate = 0;
					result = HomeworkAnswerResult.WRONG;
				}

				if (studentHomeworkQuestion.getType() == Type.QUESTION_ANSWERING) { // 简答题
					totalDenominator_correct += 2;
					if (rightRate != null) {
						totalPercent_correct += rightRate * 2;
					}
				} else if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) { // 填空题
					totalDenominator_correct += 1;
					if (rightRate == null) {
						if (result == HomeworkAnswerResult.RIGHT) {
							totalPercent_correct += 100;
						} else if (result == HomeworkAnswerResult.WRONG) {
							totalPercent_correct += 0;
						}
					} else {
						totalPercent_correct += rightRate;
					}
				} else {
					totalDenominator_correct += 1;
					if (result == HomeworkAnswerResult.RIGHT) {
						totalPercent_correct += 100;
					} else if (result == HomeworkAnswerResult.WRONG) {
						totalPercent_correct += 0;
					}
				}
			}
			BigDecimal rightRateCorrect = BigDecimal.valueOf(totalPercent_correct * 1f / totalDenominator_correct)
					.setScale(0, BigDecimal.ROUND_HALF_UP);
			studentHomeworkRepo.execute("$updateStudentHomeworkRightRateCorrect",
					Params.param("studentHomeworkId", studentHomeworkId).put("rightRateCorrect", rightRateCorrect));
		}
	}

	@Override
	@Transactional
	public void checkStudentHomeworkCorrectStatus(long studentHomeworkId) {
		StudentHomework studentHomework = studentHomeworkRepo.get(studentHomeworkId);
		if (studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.TOBE_CORRECTED) {
			// 教师待批改状态
			List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
					.getAllStudentHomeworkQuestions(studentHomeworkId);
			boolean hasBlankUnknowCount = false;
			int completeCount = 0;
			for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
				if (shq.getType() == Type.QUESTION_ANSWERING && shq.getResult() == HomeworkAnswerResult.UNKNOW) {
					// 如果作业中仍有解答题没有批改结果，则学生作业待批改状态不改
					return;
				}
				if (!shq.isCorrect() && !shq.isNewCorrect() && shq.getType() == Type.FILL_BLANK
						&& shq.getResult() == HomeworkAnswerResult.UNKNOW) {
					hasBlankUnknowCount = true;
				}
				if (shq.isCorrect() || shq.getResult() == HomeworkAnswerResult.RIGHT
						|| shq.getResult() == HomeworkAnswerResult.WRONG
						|| (shq.isNewCorrect() && shq.getResult() == HomeworkAnswerResult.INIT)
						|| (studentHomework.getStuSubmitAt() == null && !shq.isNewCorrect())) {
					// 判断已批改完成的题目：1、旧的订正题；2、有正确、错误结果的习题；3、学生一题未答被动提交，所有的原题。
					completeCount++;
				}
			}
			if (hasBlankUnknowCount) {
				// 如果作业中仍有非订正填空题批改结果是未知状态，恢复作业状态为批改中
				studentHomeworkRepo.execute("update student_homework set correct_status=:correctStatus where id=:id",
						Params.param("correctStatus", StudentHomeworkCorrectStatus.CORRECTING.getValue()).put("id",
								studentHomeworkId));
			} else if (completeCount == studentHomeworkQuestions.size()) {
				// 全部批改完成且有结果

				log.info("[-COMPLETE-][w2] 批改完成: studentHomework = " + JSON.toJSONString(studentHomework));

				studentHomeworkRepo.execute("update student_homework set correct_status=:correctStatus where id=:id",
						Params.param("correctStatus", StudentHomeworkCorrectStatus.COMPLETE.getValue()).put("id",
								studentHomeworkId));
			}

			// 去除作业待批改标签
			// 判断除了该学生作业之外其他学生作业的待批改状态
			long toBeCount = studentHomeworkRepo.find(
					"select count(*) from student_homework where homework_id=:homeworkId and id!=:id and correct_status=3",
					Params.param("homeworkId", studentHomework.getHomeworkId()).put("id", studentHomework.getId()))
					.count();
			if (toBeCount == 0) {
				// 只有所有学生作业都不存在待批改时才取消Homework待批改状态
				correctHomeworkService.setTobeCorrected(studentHomework.getHomeworkId(), false);
			}
		} else if (studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.CORRECTING) {
			// 批改中
			List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
					.getAllStudentHomeworkQuestions(studentHomeworkId);
			int completeCount = 0;
			for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
				if (shq.isCorrect() || shq.getResult() == HomeworkAnswerResult.RIGHT
						|| shq.getResult() == HomeworkAnswerResult.WRONG
						|| (shq.isNewCorrect() && shq.getResult() == HomeworkAnswerResult.INIT)) {
					completeCount++;
				}
			}
			if (completeCount == studentHomeworkQuestions.size()) {
				// 全部批改完成且有结果

				log.info("[-COMPLETE-][w3] 批改完成: studentHomework = " + JSON.toJSONString(studentHomework));

				studentHomeworkRepo.execute("update student_homework set correct_status=:correctStatus where id=:id",
						Params.param("correctStatus", StudentHomeworkCorrectStatus.COMPLETE.getValue()).put("id",
								studentHomeworkId));
			}
		}
	}

	@Override
	@Transactional
	public void setStudentHomeworkRightRateCorrect(long studentHomeworkId, int rightRateCorrect) {
		studentHomeworkRepo.execute("update student_homework set right_rate_correct=:rrc where id=:id",
				Params.param("id", studentHomeworkId).put("rrc", rightRateCorrect));
	}
}
