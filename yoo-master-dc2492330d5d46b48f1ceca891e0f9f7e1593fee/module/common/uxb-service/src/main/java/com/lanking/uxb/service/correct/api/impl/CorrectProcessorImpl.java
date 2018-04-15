package com.lanking.uxb.service.correct.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkType;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkCorrectStatus;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.intercomm.yoocorrect.service.CorrectQuestionDatawayService;
import com.lanking.uxb.service.correct.api.AutoCorrectingService;
import com.lanking.uxb.service.correct.api.CommonCorrectService;
import com.lanking.uxb.service.correct.api.CorrectHomeworkService;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.api.CorrectStatService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkService;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;

import lombok.extern.slf4j.Slf4j;

/**
 * 悠数学批改流程控制接口实现.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
@Service
@Slf4j
public class CorrectProcessorImpl implements CorrectProcessor {
	@Autowired
	private CorrectHomeworkService homeworkService;
	@Autowired
	private CorrectStudentHomeworkService studentHomeworkService;
	@Autowired
	private CorrectStudentHomeworkQuestionService studentHomeworkQuestionService;
	@Autowired
	private AutoCorrectingService autoCorrectingService;
	@Autowired
	private CommonCorrectService commonCorrectService;
	@Autowired
	private CorrectStatService correctStatService;
	@Autowired
	private CorrectQuestionDatawayService correctQuestionDatawayService;
	@Autowired
	private ZyHomeworkStatisticService hkStatisticService;

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	/**
	 * 学生提交作业之后的批改流程.
	 */
	@Override
	public void afterStudentCommitHomeworkAsync(long studentHomeworkId) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				afterStudentCommitHomeworkThread(studentHomeworkId);

				// 判断homework是否全部批改完成
				StudentHomework studentHomework = studentHomeworkService.getStudentHomework(studentHomeworkId);
				homeworkService.checkAndSaveHomeworkCorrectComplete(studentHomework.getHomeworkId());
			}
		});
	}

	/**
	 * 服务端强制提交作业之后的批改流程.
	 */
	@Override
	public void afterForceCommitHomework(long homeworkId) {
		List<StudentHomework> studentHomeworks = studentHomeworkService.listByHomework(homeworkId);
		for (StudentHomework studentHomework : studentHomeworks) {
			if (studentHomework.getCorrectStatus() == null
					|| studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.DEFAULT) {
				this.afterStudentCommitHomeworkThread(studentHomework.getId());
			}
		}
		// 判断homework是否全部批改完成
		homeworkService.checkAndSaveHomeworkCorrectComplete(homeworkId);
	}

	/**
	 * 学生订正习题之后的批改流程.
	 */
	@Override
	public void afterStudentCorrectQuestion(long studentHomeworkQuestionId, boolean done) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				if (done) {
					afterStudentCorrectQuestionThread(studentHomeworkQuestionId);
				} else {
					afterStudentNoCorrectQuestionThread(studentHomeworkQuestionId);
				}
			}
		});
	}

	private void afterStudentCommitHomeworkThread(long studentHomeworkId) {
		StudentHomework studentHomework = studentHomeworkService.getStudentHomework(studentHomeworkId);
		List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
				.getAllStudentHomeworkQuestions(studentHomeworkId);
		Homework homework = homeworkService.get(studentHomework.getHomeworkId());

		if (studentHomework.getSubmitAt() != null && studentHomework.getStuSubmitAt() == null) {
			// 被强制提交且学生未做答，直接生成订正题
			List<Long> studentHomeworkQuestionIds = new ArrayList<Long>(studentHomeworkQuestions.size());
			for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
				studentHomeworkQuestionIds.add(shq.getId());
			}
			studentHomeworkQuestionService.createStudentHomeworkCorrectQuestion(studentHomeworkQuestionIds);
			return;
		}

		// 开始自动批改
		studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomeworkId,
				StudentHomeworkCorrectStatus.AUTO_CORRECTING);

		// 自动批改流程
		Map<Question.Type, Integer> notAutoCount = autoCorrectingService.autoCorrecting(homework,
				studentHomeworkQuestions);
		int notFQAutoCount = notAutoCount.get(Type.FILL_BLANK); // 不能自动批改的填空题个数
		int notAQAutoCount = notAutoCount.get(Type.QUESTION_ANSWERING); // 不能自动批改的解答题个数

		if (notFQAutoCount == 0 && notAQAutoCount == 0) {
			// 全部都是自动批改并完成
			studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomeworkId,
					StudentHomeworkCorrectStatus.RIGHT_RATE_STAT);

			// 统计正确率，对错个数
			studentHomeworkService.staticStudentHomeworkRightRate(studentHomeworkId);

			log.info("[-COMPLETE-][w1] 批改完成: studentHomework = " + JSON.toJSONString(studentHomework));

			// 批改完成
			studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomeworkId,
					StudentHomeworkCorrectStatus.COMPLETE);
			homeworkService.setTobeCorrected(homework.getId(), false);
			return;
		} else if (notAQAutoCount > 0 && !homework.isAnswerQuestionAutoCorrect()) {
			// 有解答题不能自动批改并且作业设置了教师自行批改

			// 统计答题数量
			studentHomeworkService.staticStudentHomeworkRightRate(studentHomeworkId);

			studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomeworkId,
					StudentHomeworkCorrectStatus.TOBE_CORRECTED);
			homeworkService.setTobeCorrected(homework.getId(), true);
		} else {
			// 剩下有需要人工（小悠快批）批改的习题

			// 统计答题数量
			studentHomeworkService.staticStudentHomeworkRightRate(studentHomeworkId);
			studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomeworkId,
					StudentHomeworkCorrectStatus.CORRECTING);
		}

		// 剩下有需要人工（小悠快批）批改的习题

		// 小悠快批通信
		List<StudentHomeworkQuestion> studentHomeworkQuestions2 = studentHomeworkQuestionService
				.getStudentHomeworkQuestions(studentHomeworkId);
		List<Long> studentHomeworkQuestionIds = Lists.newArrayList();
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions2) {
			if (shq.getResult() != null && shq.getResult() == HomeworkAnswerResult.UNKNOW) {
				if (shq.getType() == Type.FILL_BLANK
						|| (homework.isAnswerQuestionAutoCorrect() && shq.getType() == Type.QUESTION_ANSWERING)) {
					studentHomeworkQuestionIds.add(shq.getId());
				}
			}
		}
		if (studentHomeworkQuestionIds.size() > 0) {
			correctQuestionDatawayService.sendCorrectQuestion(studentHomework.getStudentId(),
					studentHomeworkQuestionIds, false);
		}
	}

	/**
	 * 手动批改学生单条作业习题.
	 */
	@Override
	public void correctStudentHomeworkQuestion(long corrector, CorrectorType correctorType,
			QuestionCorrectObject questionCorrectObject) {
		this.correctStudentHomeworkQuestions(corrector, correctorType, questionCorrectObject.getStudentHomeworkId(),
				Lists.newArrayList(questionCorrectObject));
	}

	/**
	 * 手动批改学生多条作业（需要是同一份作业）.
	 */
	@Override
	public void correctStudentHomeworkQuestions(long corrector, CorrectorType correctorType, long studentHomeworkId,
			Collection<QuestionCorrectObject> questionCorrectObjects) {

		// 记录批改日志
		List<QuestionCorrectLog> logs = Lists.newArrayList();

		// 记录错题
		List<Long> fallibleStudentHomeworkQuestionIds = Lists.newArrayList();

		boolean changeResult = false;
		for (QuestionCorrectObject questionCorrectObject : questionCorrectObjects) {
			StudentHomeworkQuestion old = studentHomeworkQuestionService
					.get(questionCorrectObject.getStuHomeworkQuestionId());

			// 批改单个题目
			HomeworkAnswerResult homeworkAnswerResult = studentHomeworkQuestionService.correctSigleQuestion(corrector,
					correctorType, questionCorrectObject);

			if (!old.isCorrect() && !old.isNewCorrect() && old.getResult() != homeworkAnswerResult) {
				// 有改变原习题批改结果
				changeResult = true;
			}

			// 异步生成订正题
			if (homeworkAnswerResult == HomeworkAnswerResult.WRONG) {
				fallibleStudentHomeworkQuestionIds.add(questionCorrectObject.getStuHomeworkQuestionId());
				commonCorrectService
						.asyncCreateStudentHomeworkCorrectQuestion(questionCorrectObject.getStuHomeworkQuestionId());
			}

			QuestionCorrectLog log = new QuestionCorrectLog();
			if (correctorType == CorrectorType.PG_USER) {
				log.setCorrectType(QuestionCorrectType.CONSOLE_CORRECT);
			} else if (correctorType == CorrectorType.TEACHER) {
				log.setCorrectType(QuestionCorrectType.TEACHER_CORRECT);
			} else if (correctorType == CorrectorType.Y_CORRECTOR) {
				log.setCorrectType(QuestionCorrectType.YOO_CORRECT);
			}
			log.setCreateAt(new Date());
			log.setHomeworkType(HomeworkType.HOMEWORK);
			log.setUserId(corrector);
			log.setStudentHomeworkQuestionId(questionCorrectObject.getStuHomeworkQuestionId());
			log.setResult(homeworkAnswerResult);
			logs.add(log);
		}

		// 异步保存批改日志
		if (logs.size() > 0) {
			commonCorrectService.asyncCreateCorrectLog(logs);
		}

		// 异步处理错题
		if (fallibleStudentHomeworkQuestionIds.size() > 0) {
			correctStatService.aysncFallibleQuestionHandler(fallibleStudentHomeworkQuestionIds);
		}

		// 统计学生作业正确率
		studentHomeworkService.staticStudentHomeworkRightRate(studentHomeworkId);

		// 判断当前作业的待批改状态
		studentHomeworkService.checkStudentHomeworkCorrectStatus(studentHomeworkId);

		// 判断整份作业的状态
		StudentHomework studentHomework = studentHomeworkService.getStudentHomework(studentHomeworkId);
		homeworkService.checkAndSaveHomeworkCorrectComplete(studentHomework.getHomeworkId());

		// 有改变原批改结果，重新统计作业相关数据
		if (changeResult) {
			// 只有原作业已经有正确率了之后再重新统计
			Homework homework = homeworkService.get(studentHomework.getHomeworkId());
			if (homework.getRightRate() != null) {
				executor.execute(new Runnable() {
					@Override
					public void run() {
						hkStatisticService.staticAfterIssue(studentHomework.getHomeworkId());
					}
				});
			}
		}
	}

	/**
	 * 学生订正答案未做答（不会做）
	 * 
	 * @param studentHomeworkQuestionId
	 */
	private void afterStudentNoCorrectQuestionThread(long studentHomeworkQuestionId) {
		StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionService.get(studentHomeworkQuestionId);
		if (!studentHomeworkQuestion.isNewCorrect() || !studentHomeworkQuestion.isRevised()
				|| (studentHomeworkQuestion.getResult() != null
						&& studentHomeworkQuestion.getResult() != HomeworkAnswerResult.INIT)) {
			// 必须是新的订正题，并且已订正未做答
			return;
		}

		// 统计订正后的正确率
		studentHomeworkService.staticStudentHomeworkRightRate(studentHomeworkQuestion.getStudentHomeworkId());
	}

	/**
	 * 学生订正答案.
	 * 
	 * @param studentHomeworkQuestionId
	 */
	private void afterStudentCorrectQuestionThread(long studentHomeworkQuestionId) {
		StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionService.get(studentHomeworkQuestionId);
		if (!studentHomeworkQuestion.isNewCorrect() || !studentHomeworkQuestion.isRevised()) {
			// 必须是新的订正题，并且已订正已做答
			return;
		}

		StudentHomework studentHomework = studentHomeworkService
				.getStudentHomework(studentHomeworkQuestion.getStudentHomeworkId());
		Homework homework = homeworkService.get(studentHomework.getHomeworkId());

		// 自动批改流程
		Map<Question.Type, Integer> notAutoCount = autoCorrectingService.autoCorrecting(homework,
				Lists.newArrayList(studentHomeworkQuestion));
		int notFQAutoCount = notAutoCount.get(Type.FILL_BLANK); // 不能自动批改的填空题个数
		int notAQAutoCount = notAutoCount.get(Type.QUESTION_ANSWERING); // 不能自动批改的解答题个数

		if (notFQAutoCount == 0 && notAQAutoCount == 0) {
			// 若全部都是自动批改并完成，开始正确率统计

			// 统计订正后的正确率
			studentHomeworkService.staticStudentHomeworkRightRate(studentHomework.getId());

			return;
		} else if (notAQAutoCount > 0 && !homework.isAnswerQuestionAutoCorrect()) {
			// 有解答题不能自动批改并且作业设置了教师自行批改

			studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomework.getId(),
					StudentHomeworkCorrectStatus.TOBE_CORRECTED);
			homeworkService.setTobeCorrected(homework.getId(), true);
			studentHomeworkQuestionService.saveQuestionCorrectType(studentHomeworkQuestionId,
					QuestionCorrectType.TEACHER_CORRECT);
			return;
		}

		// 小悠快批通信
		studentHomeworkQuestionService.saveQuestionCorrectType(studentHomeworkQuestionId,
				QuestionCorrectType.YOO_CORRECT);
		correctQuestionDatawayService.sendCorrectQuestion(studentHomework.getStudentId(),
				Lists.newArrayList(studentHomeworkQuestionId), false);
	}

	@Override
	public void notCompleteStudentHomeworkHandle(Collection<StudentHomework> studentHomeworks) {
		for (StudentHomework studentHomework : studentHomeworks) {
			if (studentHomework.getCorrectStatus() == null
					|| studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.DEFAULT) {
				// 未批改的作业
				this.afterStudentCommitHomeworkThread(studentHomework.getId());
			} else if (studentHomework.getCorrectStatus() == StudentHomeworkCorrectStatus.AUTO_CORRECTING) {
				// 处于批改中的作业
				List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
						.getAllStudentHomeworkQuestions(studentHomework.getId());

				boolean hasUnknown = false;
				boolean hasAQUnknown = false;
				List<StudentHomeworkQuestion> notCompleteStudentHomeworkQuestions = Lists.newArrayList();
				for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
					if (shq.getResult() == HomeworkAnswerResult.INIT) {
						notCompleteStudentHomeworkQuestions.add(shq);
					} else if (shq.getResult() == HomeworkAnswerResult.UNKNOW) {
						hasUnknown = true;
						if (shq.getType() == Type.QUESTION_ANSWERING) {
							hasAQUnknown = true;
						}
					}
				}

				// 自动批改流程
				Homework homework = homeworkService.get(studentHomework.getHomeworkId());
				int notFQAutoCount = 0;
				int notAQAutoCount = 0;
				if (notCompleteStudentHomeworkQuestions.size() > 0) {
					Map<Question.Type, Integer> notAutoCount = autoCorrectingService.autoCorrecting(homework,
							notCompleteStudentHomeworkQuestions);
					notFQAutoCount = notAutoCount.get(Type.FILL_BLANK); // 不能自动批改的填空题个数
					notAQAutoCount = notAutoCount.get(Type.QUESTION_ANSWERING); // 不能自动批改的解答题个数
				}

				if (notFQAutoCount == 0 && notAQAutoCount == 0 && !hasUnknown) {
					// 全部都是自动批改并完成
					studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomework.getId(),
							StudentHomeworkCorrectStatus.RIGHT_RATE_STAT);

					// 统计正确率，对错个数
					studentHomeworkService.staticStudentHomeworkRightRate(studentHomework.getId());

					log.info("[-COMPLETE-][w4] 批改完成: studentHomework = " + JSON.toJSONString(studentHomework));

					// 批改完成
					studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomework.getId(),
							StudentHomeworkCorrectStatus.COMPLETE);
					homeworkService.setTobeCorrected(homework.getId(), false);
					return;
				} else if ((notAQAutoCount > 0 || hasAQUnknown) && !homework.isAnswerQuestionAutoCorrect()) {
					// 有解答题不能自动批改并且作业设置了教师自行批改

					// 统计答题数量
					studentHomeworkService.staticStudentHomeworkRightRate(studentHomework.getId());

					studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomework.getId(),
							StudentHomeworkCorrectStatus.TOBE_CORRECTED);
					homeworkService.setTobeCorrected(homework.getId(), true);
				} else {
					// 剩下有需要人工（小悠快批）批改的习题

					// 统计答题数量
					studentHomeworkService.staticStudentHomeworkRightRate(studentHomework.getId());
					studentHomeworkService.setStudentHomeworkCorrectStatus(studentHomework.getId(),
							StudentHomeworkCorrectStatus.CORRECTING);
				}

				// 小悠快批通信
				if (notCompleteStudentHomeworkQuestions.size() > 0) {
					List<StudentHomeworkQuestion> studentHomeworkQuestions2 = studentHomeworkQuestionService
							.getStudentHomeworkQuestions(studentHomework.getId());
					List<Long> studentHomeworkQuestionIds = Lists.newArrayList();
					for (StudentHomeworkQuestion shq : studentHomeworkQuestions2) {
						if (shq.getResult() != null && shq.getResult() == HomeworkAnswerResult.UNKNOW) {
							if (shq.getType() == Type.FILL_BLANK || (homework.isAnswerQuestionAutoCorrect()
									&& shq.getType() == Type.QUESTION_ANSWERING
									&& notCompleteStudentHomeworkQuestions.contains(shq.getId()))) {
								studentHomeworkQuestionIds.add(shq.getId());
							}
						}
					}
					if (studentHomeworkQuestionIds.size() > 0) {
						correctQuestionDatawayService.sendCorrectQuestion(studentHomework.getStudentId(),
								studentHomeworkQuestionIds, false);
					}
				}
			}

			// 判断homework是否全部批改完成
			homeworkService.checkAndSaveHomeworkCorrectComplete(studentHomework.getHomeworkId());
		}
	}
}
