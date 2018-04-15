package com.lanking.uxb.service.correct.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkType;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectLog;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.correct.api.AutoCorrectingService;
import com.lanking.uxb.service.correct.api.CommonCorrectService;
import com.lanking.uxb.service.correct.api.CorrectAnswerService;
import com.lanking.uxb.service.correct.api.CorrectStatService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkAnswerService;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.correct.vo.AnswerCorrectResult;
import com.lanking.uxb.service.correct.vo.QuestionCorrectResult;
import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionService;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 自动批改接口实现.
 * 
 * @author wanlong.che
 * @since 小优快批
 */
@Service
public class AutoCorrectingServiceImpl implements AutoCorrectingService {

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	@Autowired
	private CorrectAnswerService answerService;
	@Autowired
	private CorrectStudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private IntelligentCorrectionService intelligentCorrectionService;
	@Autowired
	private CorrectStudentHomeworkQuestionService studentHomeworkQuestionService;
	@Autowired
	private CommonCorrectService commonCorrectService;
	@Autowired
	private CorrectStatService correctStatService;

	/**
	 * 自动批改.
	 */
	@Override
	public Map<Question.Type, Integer> autoCorrecting(Homework homework,
			List<StudentHomeworkQuestion> studentHomeworkQuestions) {
		Map<Question.Type, Integer> notAutoCount = new HashMap<Question.Type, Integer>(); // 无法自动批改的习题个数集合
		notAutoCount.put(Question.Type.FILL_BLANK, 0);
		notAutoCount.put(Question.Type.QUESTION_ANSWERING, 0);

		Set<Long> questionIds = new HashSet<Long>(studentHomeworkQuestions.size());
		Set<Long> studentHomeworkQuestionIds = new HashSet<Long>(studentHomeworkQuestions.size());
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
			if (shq.getResult() != HomeworkAnswerResult.INIT) {
				continue;
			}
			questionIds.add(shq.getQuestionId());
			studentHomeworkQuestionIds.add(shq.getId());
		}

		// 批改日志集合
		List<QuestionCorrectLog> logs = new ArrayList<QuestionCorrectLog>();

		// 习题正确答案集合
		Map<Long, List<Answer>> answers = answerService.getQuestionAnswers(questionIds);

		// 学生习题答案集合
		Map<Long, List<StudentHomeworkAnswer>> sanswers = studentHomeworkAnswerService.find(studentHomeworkQuestionIds);

		// 自动批改结果集合，统一保存
		Map<Long, QuestionCorrectType> questionCorrectTypes = new HashMap<Long, QuestionCorrectType>(); // 预期批改方式
		List<QuestionCorrectResult> questionFinalResults = new ArrayList<QuestionCorrectResult>(); // 最终批改方式
		List<AnswerCorrectResult> allAnswerResults = new ArrayList<AnswerCorrectResult>(); // 所有题答案批改结果
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
			// 批改日志
			QuestionCorrectLog log = new QuestionCorrectLog();
			log.setCorrectType(QuestionCorrectType.AUTO_CORRECT);
			log.setHomeworkType(HomeworkType.HOMEWORK);
			log.setStudentHomeworkQuestionId(shq.getId());

			List<Answer> as = answers.get(shq.getQuestionId()); // 正确答案
			List<StudentHomeworkAnswer> shas = sanswers.get(shq.getId()); // 学生答案
			List<AnswerCorrectResult> answerResults = new ArrayList<AnswerCorrectResult>(); // 单题答案批改结果

			// 调用远程核心自动批改功能的数据集合
			List<Map<String, Object>> remoteAnswers = new ArrayList<Map<String, Object>>();

			QuestionCorrectResult questionCorrectResult = null;
			if (shq.getType() == Type.SINGLE_CHOICE || shq.getType() == Type.TRUE_OR_FALSE) {
				// 单选、判断题
				String asContent = as.get(0).getContent();
				String shasContent = shas.get(0).getContent();
				AnswerCorrectResult answerCorrectResult = null;
				if (StringUtils.isNotBlank(shasContent)
						&& asContent.toLowerCase().trim().equals(shasContent.toLowerCase().trim())) {
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.RIGHT,
							QuestionCorrectType.AUTO_CORRECT, 100);
					answerCorrectResult = new AnswerCorrectResult(shas.get(0).getId(), null,
							HomeworkAnswerResult.RIGHT);
				} else {
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.WRONG,
							QuestionCorrectType.AUTO_CORRECT, 0);
					answerCorrectResult = new AnswerCorrectResult(shas.get(0).getId(), null,
							HomeworkAnswerResult.WRONG);
				}
				answerResults.add(answerCorrectResult);
				questionCorrectTypes.put(shq.getId(), QuestionCorrectType.AUTO_CORRECT);

				log.setCreateAt(new Date());
				log.setAutoCorrectMethods(Lists.newArrayList(QuestionAutoCorrectMethod.AUTO_CHECK));
				log.setResult(questionCorrectResult.getResult());
				logs.add(log);
			} else if (shq.getType() == Type.MULTIPLE_CHOICE) {
				// 多选题
				List<String> ras = Lists.newArrayList();
				for (Answer a : as) {
					ras.add(a.getContent().trim().toLowerCase());
				}
				int rightCount = 0;
				int wrongCount = 0;
				for (StudentHomeworkAnswer sha : shas) {
					AnswerCorrectResult answerCorrectResult = null;
					if (StringUtils.isNotBlank(sha.getContent())) {
						if (ras.contains(sha.getContent().trim().toLowerCase())) {
							answerCorrectResult = new AnswerCorrectResult(sha.getId(), null,
									HomeworkAnswerResult.RIGHT);
							ras.remove(sha.getContent().trim().toLowerCase());
							rightCount++;
						} else {
							answerCorrectResult = new AnswerCorrectResult(sha.getId(), null,
									HomeworkAnswerResult.WRONG);
							wrongCount++;
						}
					} else {
						answerCorrectResult = new AnswerCorrectResult(sha.getId(), null, HomeworkAnswerResult.WRONG);
					}
					answerResults.add(answerCorrectResult);
				}
				if (ras.size() == 0 && as.size() == rightCount && wrongCount == 0) {
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.RIGHT,
							QuestionCorrectType.AUTO_CORRECT, 100);
				} else {
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.WRONG,
							QuestionCorrectType.AUTO_CORRECT, 0);
				}
				questionCorrectTypes.put(shq.getId(), QuestionCorrectType.AUTO_CORRECT);

				log.setCreateAt(new Date());
				log.setAutoCorrectMethods(Lists.newArrayList(QuestionAutoCorrectMethod.AUTO_CHECK));
				log.setResult(questionCorrectResult.getResult());
				logs.add(log);
			} else if (shq.getType() == Type.FILL_BLANK) {
				// 填空题
				int i = 0;
				int rightAnswerSize = as.size();
				for (StudentHomeworkAnswer sha : shas) {
					AnswerCorrectResult answerCorrectResult = null;
					if (StringUtils.isBlank(sha.getContent())) {
						answerCorrectResult = new AnswerCorrectResult(sha.getId(), null, HomeworkAnswerResult.WRONG);
					} else {
						if (i + 1 > rightAnswerSize) {
							answerCorrectResult = new AnswerCorrectResult(sha.getId(), null,
									HomeworkAnswerResult.WRONG);
						} else {
							// 需要调用远程批改的数据
							Map<String, Object> am = new HashMap<String, Object>();
							am.put("studentHomeworkQuestionId", shq.getId());
							am.put("studentHomeworkAnswerId", sha.getId());
							am.put("buildAnswerId", as.get(i).getId());
							am.put("answer", as.get(i).getContent());
							am.put("stuAnswer", sha.getContent());
							remoteAnswers.add(am);
						}
					}
					i++;
					if (answerCorrectResult != null) {
						answerResults.add(answerCorrectResult);
					}
				}
				if (answerResults.size() == shas.size()) {
					// 全部答案批改结果为“错”
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.WRONG,
							QuestionCorrectType.AUTO_CORRECT, 0);
					questionCorrectTypes.put(shq.getId(), QuestionCorrectType.AUTO_CORRECT);

					log.setCreateAt(new Date());
					log.setAutoCorrectMethods(Lists.newArrayList(QuestionAutoCorrectMethod.AUTO_CHECK));
					log.setResult(questionCorrectResult.getResult());
					logs.add(log);
				} else {
					questionCorrectTypes.put(shq.getId(), QuestionCorrectType.YOO_CORRECT);
				}
			} else if (shq.getType() == Question.Type.QUESTION_ANSWERING) {
				// 解答题
				if (shq.getAnswerImg() == null || shq.getAnswerImg() <= 0) {
					// 解答题没有答题的情况直接判错
					AnswerCorrectResult answerCorrectResult = new AnswerCorrectResult(shas.get(0).getId(), null,
							HomeworkAnswerResult.WRONG);
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.WRONG,
							QuestionCorrectType.AUTO_CORRECT, 0);
					answerResults.add(answerCorrectResult);

					questionCorrectTypes.put(shq.getId(), QuestionCorrectType.AUTO_CORRECT);
					log.setCreateAt(new Date());
					log.setAutoCorrectMethods(Lists.newArrayList(QuestionAutoCorrectMethod.AUTO_CHECK));
					log.setResult(questionCorrectResult.getResult());
					logs.add(log);
				} else {
					// 非自动批改的解答题批改结果设为UNKNOW
					AnswerCorrectResult answerCorrectResult = new AnswerCorrectResult(shas.get(0).getId(), null,
							HomeworkAnswerResult.UNKNOW);
					questionCorrectResult = new QuestionCorrectResult(shq.getId(), HomeworkAnswerResult.UNKNOW,
							QuestionCorrectType.AUTO_CORRECT, null);
					answerResults.add(answerCorrectResult);

					if (homework.isAnswerQuestionAutoCorrect()) {
						// 设置解答题自动批改（小悠快批）
						questionCorrectTypes.put(shq.getId(), QuestionCorrectType.YOO_CORRECT);
					} else {
						// 教师自行批改
						questionCorrectTypes.put(shq.getId(), QuestionCorrectType.TEACHER_CORRECT);
					}
					notAutoCount.put(Type.QUESTION_ANSWERING, notAutoCount.get(Type.QUESTION_ANSWERING) + 1);
				}
			}

			// 处理远程自动批改填空题
			if (remoteAnswers.size() > 0) {
				Map<Long, CorrectResult> rets = batchAutoCheckAnswer(remoteAnswers);
				List<QuestionAutoCorrectMethod> autoMethods = Lists.newArrayList();
				for (Map<String, Object> one : remoteAnswers) {
					Long studentHomeworkAnswerId = (Long) one.get("studentHomeworkAnswerId");
					CorrectResult oneRet = rets.get(studentHomeworkAnswerId);
					AnswerCorrectResult answerCorrectResult = new AnswerCorrectResult(studentHomeworkAnswerId, null,
							oneRet.getResult());
					autoMethods.add(oneRet.getMethod());
					answerResults.add(answerCorrectResult);
				}
				// 习题的批改结果
				int rightCount = 0;
				int wrongCount = 0;
				for (AnswerCorrectResult answerCorrectResult : answerResults) {
					if (answerCorrectResult.getResult() == HomeworkAnswerResult.RIGHT) {
						rightCount++;
					} else if (answerCorrectResult.getResult() == HomeworkAnswerResult.WRONG) {
						wrongCount++;
					}
				}
				Integer rightRate = null;
				HomeworkAnswerResult result = HomeworkAnswerResult.UNKNOW;

				// 有可能一道填空题某几个空有结果，某几个空是UNKNOW，整道题就是UNKNOW
				if (rightCount + wrongCount == answerResults.size()) {
					rightRate = BigDecimal.valueOf(rightCount * 100f / answerResults.size())
							.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
					result = rightRate == 100 ? HomeworkAnswerResult.RIGHT : HomeworkAnswerResult.WRONG;
				} else {
					notAutoCount.put(Type.FILL_BLANK, answerResults.size() - rightCount - wrongCount);
				}
				questionCorrectResult = new QuestionCorrectResult(shq.getId(), result, QuestionCorrectType.AUTO_CORRECT,
						rightRate);

				log.setCreateAt(new Date());
				log.setAutoCorrectMethods(autoMethods);
				log.setResult(questionCorrectResult.getResult());
				logs.add(log);
			}

			if (questionCorrectResult != null) {
				questionFinalResults.add(questionCorrectResult);
			}

			allAnswerResults.addAll(answerResults);
		}

		// 记录错题
		List<Long> fallibleStudentHomeworkQuestionIds = Lists.newArrayList();

		// 保存习题答案的自动批改结果
		if (allAnswerResults.size() > 0) {
			studentHomeworkAnswerService.saveCorrectResults(allAnswerResults);
		}
		if (questionFinalResults.size() > 0) {

			// 保存习题的自动批改结果
			studentHomeworkQuestionService.saveCorrectResults(questionFinalResults);

			// 异步创建学生作业订正题
			List<Long> studentHomeworkCorrectQuestionIds = Lists.newArrayList();
			for (QuestionCorrectResult result : questionFinalResults) {
				if (result.getResult() == HomeworkAnswerResult.WRONG) {
					fallibleStudentHomeworkQuestionIds.add(result.getStudentHomeworkQuestionId());
					studentHomeworkCorrectQuestionIds.add(result.getStudentHomeworkQuestionId());
				}
			}
			if (studentHomeworkCorrectQuestionIds.size() > 0) {
				commonCorrectService.asyncCreateStudentHomeworkCorrectQuestion(studentHomeworkCorrectQuestionIds);
			}
		}
		if (questionCorrectTypes.size() > 0) {
			// 保存预期批改方式
			studentHomeworkQuestionService.saveQuestionCorrectType(questionCorrectTypes);
		}

		// 异步保存批改日志
		if (logs.size() > 0) {
			commonCorrectService.asyncCreateCorrectLog(logs);
		}

		// 异步保存错题
		if (fallibleStudentHomeworkQuestionIds.size() > 0) {
			correctStatService.aysncFallibleQuestionHandler(studentHomeworkQuestionIds);
		}

		return notAutoCount;
	}

	/**
	 * 批量调用远程自动批改.
	 * 
	 * @param answers
	 *            需要远程批改的学生答案集合
	 * @return
	 */
	public Map<Long, CorrectResult> batchAutoCheckAnswer(List<Map<String, Object>> answers) {
		if (CollectionUtils.isNotEmpty(answers)) {
			int size = answers.size();
			List<Long> queryIds = new ArrayList<Long>(size);
			List<Long> answerIds = new ArrayList<Long>(size);
			List<String> targets = new ArrayList<String>(size);
			List<String> querys = new ArrayList<String>(size);
			for (Map<String, Object> map : answers) {
				long queryId = (Long) map.get("studentHomeworkAnswerId");
				long answerId = (Long) map.get("buildAnswerId");
				String target = map.get("answer").toString();
				String query = map.get("stuAnswer").toString();
				queryIds.add(queryId);
				answerIds.add(answerId);
				targets.add(target);
				querys.add(query);
			}
			return intelligentCorrectionService.correct(queryIds, answerIds, targets, querys);
		}
		return new HashMap<Long, CorrectResult>(0);
	}
}
