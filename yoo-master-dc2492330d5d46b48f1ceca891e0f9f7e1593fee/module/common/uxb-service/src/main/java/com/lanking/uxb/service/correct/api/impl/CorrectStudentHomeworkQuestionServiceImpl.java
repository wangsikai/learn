package com.lanking.uxb.service.correct.api.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.correct.api.CorrectStudentHomeworkQuestionService;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.correct.vo.QuestionCorrectResult;

@Service
public class CorrectStudentHomeworkQuestionServiceImpl implements CorrectStudentHomeworkQuestionService {

	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> studentHomeworkQuestionRepo;

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> studentHomeworkAnswerRepo;

	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;

	@Override
	@Transactional
	public StudentHomeworkQuestion get(long id) {
		return studentHomeworkQuestionRepo.get(id);
	}

	@Override
	@Transactional
	public List<StudentHomeworkQuestion> mgetList(Collection<Long> studentHomeworkQuestionIds) {
		return studentHomeworkQuestionRepo.mgetList(studentHomeworkQuestionIds);
	}

	@Override
	@Transactional
	public List<StudentHomeworkQuestion> getStudentHomeworkQuestions(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("$findByStudentHomeworkIdAndQuestionId", Params.param("studentHomeworkId", studentHomeworkId))
				.list();
	}

	@Override
	@Transactional
	public List<StudentHomeworkQuestion> getAllStudentHomeworkQuestions(long studentHomeworkId) {
		return studentHomeworkQuestionRepo
				.find("select * from student_homework_question where student_homework_id=:studentHomeworkId",
						Params.param("studentHomeworkId", studentHomeworkId))
				.list();
	}

	@Override
	@Transactional
	public void saveCorrectResults(List<QuestionCorrectResult> questionResults) {
		if (CollectionUtils.isNotEmpty(questionResults)) {
			Date correctAt = new Date();
			List<StudentHomeworkQuestion> questions = new ArrayList<StudentHomeworkQuestion>();
			for (QuestionCorrectResult result : questionResults) {
				StudentHomeworkQuestion question = studentHomeworkQuestionRepo
						.get(result.getStudentHomeworkQuestionId());
				if (result.getCorrectType() == QuestionCorrectType.AUTO_CORRECT) {
					question.setAutoCorrect(true);
					question.setManualCorrect(false);
				} else {
					question.setAutoCorrect(false);
					question.setManualCorrect(true);
				}
				question.setCorrectFinalType(result.getCorrectType()); // 最终的批改方式
				question.setResult(result.getResult());
				question.setRightRate(result.getRightRate());
				question.setCorrectAt(correctAt);
				questions.add(question);
			}
			studentHomeworkQuestionRepo.save(questions);
		}
	}

	@Override
	@Transactional
	public void saveQuestionCorrectType(Map<Long, QuestionCorrectType> questionCorrectTypes) {
		if (CollectionUtils.isNotEmpty(questionCorrectTypes)) {
			List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionRepo
					.mgetList(questionCorrectTypes.keySet());
			for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
				shq.setCorrectType(questionCorrectTypes.get(shq.getId()));
			}
			studentHomeworkQuestionRepo.save(studentHomeworkQuestions);
		}
	}

	@Override
	@Transactional
	public void saveQuestionCorrectType(long studentHomeworkQuestionId, QuestionCorrectType correctType) {
		studentHomeworkQuestionRepo.execute(
				"update student_homework_question set correct_type=:correctType where id=:id",
				Params.param("id", studentHomeworkQuestionId).put("correctType", correctType.getValue()));
	}

	@Override
	@Transactional
	public HomeworkAnswerResult correctSigleQuestion(long corrector, CorrectorType correctorType,
			QuestionCorrectObject questionCorrectObject) {
		HomeworkAnswerResult questionResult = questionCorrectObject.getQuestionResult(); // 习题批改结果
		Integer questionRightRate = questionCorrectObject.getQuestionRightRate(); // 习题正确率

		QuestionCorrectType questionCorrectType = null; // 习题批改方式
		if (correctorType == CorrectorType.TEACHER) {
			questionCorrectType = QuestionCorrectType.TEACHER_CORRECT;
		} else if (correctorType == CorrectorType.PG_USER) {
			questionCorrectType = QuestionCorrectType.CONSOLE_CORRECT;
		} else if (correctorType == CorrectorType.Y_CORRECTOR) {
			questionCorrectType = QuestionCorrectType.YOO_CORRECT;
		}

		Date now = new Date();
		if (questionCorrectObject.getQuestionType() == Type.FILL_BLANK) {
			// 填空题
			for (Map.Entry<Long, HomeworkAnswerResult> answerEntry : questionCorrectObject.getAnswerResultMap()
					.entrySet()) {
				studentHomeworkAnswerRepo.execute("$zyCorrectById",
						Params.param("result", answerEntry.getValue().getValue()).put("id", answerEntry.getKey())
								.put("correctAt", now));
			}

			// 计算正确率
			if (questionRightRate == null || questionResult == null) {
				questionRightRate = 0;
				questionResult = HomeworkAnswerResult.RIGHT;
				List<StudentHomeworkAnswer> answers = studentHomeworkAnswerRepo
						.find("$zyFindByStuHkQuestion",
								Params.param("stuHkQuestionId", questionCorrectObject.getStuHomeworkQuestionId()))
						.list();
				for (StudentHomeworkAnswer answer : answers) {
					if (answer.getResult() == HomeworkAnswerResult.RIGHT) {
						questionRightRate += 100;
					} else if (answer.getResult() == HomeworkAnswerResult.WRONG) {
						questionResult = HomeworkAnswerResult.WRONG;
					} else {
						// 如果仍有答案未批改完成，处于unknown状态，则无法统计习题结果
						questionRightRate = null;
						questionResult = HomeworkAnswerResult.UNKNOW;
						break;
					}
				}
				if (questionRightRate != null) {
					questionRightRate = BigDecimal.valueOf(questionRightRate / answers.size())
							.setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
				}
			}

			if (questionResult != HomeworkAnswerResult.UNKNOW) {
				studentHomeworkQuestionRepo.execute("$zyCorrect",
						Params.param("id", questionCorrectObject.getStuHomeworkQuestionId())
								.put("result", questionResult.getValue()).put("questionRate", questionRightRate)
								.put("correctAt", now).put("correctType", questionCorrectType.getValue()));
			}
		} else if (questionCorrectObject.getQuestionType() == Type.QUESTION_ANSWERING) {
			// 解答题
			if (questionRightRate != null) {
				if (questionRightRate == 100) {
					questionResult = HomeworkAnswerResult.RIGHT;
				} else {
					questionResult = HomeworkAnswerResult.WRONG;
				}
			} else if (questionResult == null) {
				StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionRepo
						.get(questionCorrectObject.getStuHomeworkQuestionId());
				questionResult = studentHomeworkQuestion.getResult();
			}

			// 轨迹处理、答图处理
			int ret = 0;
			if (questionCorrectObject.getAnswerImgId() != null) {
				ret = this.saveNotation(questionCorrectObject.getStuHomeworkQuestionId(),
						questionCorrectObject.getAnswerImgId(), questionCorrectObject.getNotationImageId(),
						questionCorrectObject.getNotation(), null, questionRightRate, questionResult, now,
						questionCorrectType);
				if (questionCorrectObject.getAnswerImgIds() != null) {
					for (int i = 0; i < questionCorrectObject.getAnswerImgIds().size(); i++) {
						this.saveMultiNotation(questionCorrectObject.getStuHomeworkQuestionId(),
								questionCorrectObject.getAnswerImgIds().get(i),
								questionCorrectObject.getAnswerImgIds().get(i),
								questionCorrectObject.getNotationImageIds().get(i),
								questionCorrectObject.getNotations().get(i), null);
					}
				} else {
					// 一张图的也需要更新student_homework_answer_image
					this.saveMultiNotation(questionCorrectObject.getStuHomeworkQuestionId(),
							questionCorrectObject.getAnswerImgId(), questionCorrectObject.getAnswerImgId(),
							questionCorrectObject.getNotationImageId(), questionCorrectObject.getNotation(), null);
				}
			} else {
				ret = studentHomeworkQuestionRepo.execute("$zyUpdateResultAndRightRate",
						Params.param("id", questionCorrectObject.getStuHomeworkQuestionId())
								.put("rightRate", questionRightRate).put("result", questionResult.getValue())
								.put("correctAt", now).put("correctType", questionCorrectType.getValue()));
			}

			if (ret > 0) {
				studentHomeworkAnswerRepo.execute("$zyCorrect",
						Params.param("stuHkQId", questionCorrectObject.getStuHomeworkQuestionId())
								.put("result", questionResult.getValue()).put("correctAt", now));
			}
		} else {
			// 客观题
			questionRightRate = questionResult == HomeworkAnswerResult.RIGHT ? 100 : 0;
			int ret = studentHomeworkQuestionRepo.execute("$zyCorrect",
					Params.param("id", questionCorrectObject.getStuHomeworkQuestionId())
							.put("result", questionResult.getValue()).put("questionRate", questionRightRate)
							.put("correctAt", now).put("correctType", questionCorrectType.getValue()));

			if (ret > 0) {
				studentHomeworkAnswerRepo.execute("$zyCorrect",
						Params.param("stuHkQId", questionCorrectObject.getStuHomeworkQuestionId()).put("result",
								questionResult.getValue()));
			}
		}

		return questionResult;
	}

	@Override
	@Transactional
	public int saveNotation(long id, long srcId, long generateId, String notationImg, String notationPoints,
			Integer rightRate, HomeworkAnswerResult result, Date correctAt, QuestionCorrectType correctType) {
		Params params = Params.param("id", id).put("srcId", srcId).put("generateId", generateId).put("notationImg",
				notationImg);
		if (notationImg == null) {
			params.put("notationPoints", notationPoints);
		}
		if (notationPoints == null) {
			params.put("notationImg", notationImg);
		}
		if (result != null) {
			params.put("result", result.getValue());
		}
		if (rightRate != null) {
			params.put("rightRate", rightRate);
		}
		if (correctAt != null) {
			params.put("correctAt", correctAt);
		}
		if (correctType != null) {
			params.put("correctType", correctType.getValue());
		}
		return studentHomeworkQuestionRepo.execute("$zyUpdateNotation", params);
	}

	@Override
	@Transactional
	public int saveMultiNotation(long id, long oriSrcId, long srcId, long generateId, String notationImg,
			String notationPoints) {
		Params params = Params.param("id", id).put("oriSrcId", oriSrcId).put("srcId", srcId).put("generateId",
				generateId);
		if (notationPoints != null) {
			params.put("notationPoints", notationPoints);
		}
		if (notationImg != null) {
			params.put("notationImg", notationImg);
		}
		return studentHomeworkQuestionRepo.execute("$zyUpdateMultiNotation", params);
	}

	@Override
	@Transactional
	public void createStudentHomeworkCorrectQuestion(long studentHomeworkQuestionId) {
		StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionRepo.get(studentHomeworkQuestionId);
		if (studentHomeworkQuestion.isNewCorrect()) {
			// 本身是订正题，不再生成
			return;
		}
		StudentHomeworkQuestion newCorrectQuestion = this.getNewCorrectQuestion(
				studentHomeworkQuestion.getStudentHomeworkId(), studentHomeworkQuestion.getQuestionId());
		if (newCorrectQuestion != null) {
			// 已有订正题，不再生成
			return;
		}
		if (!studentHomeworkQuestion.isCorrect() && !studentHomeworkQuestion.isNewCorrect()
				&& studentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG) {
			List<StudentHomeworkAnswer> studentHomeworkAnswers = studentHomeworkAnswerRepo
					.find("$zyFindByStuHkQuestion", Params.param("stuHkQuestionId", studentHomeworkQuestionId)).list();

			// 错题产生订正题
			StudentHomeworkQuestion cq = new StudentHomeworkQuestion();
			cq.setStudentHomeworkId(studentHomeworkQuestion.getStudentHomeworkId());
			cq.setQuestionId(studentHomeworkQuestion.getQuestionId());
			cq.setResult(HomeworkAnswerResult.INIT);
			cq.setType(studentHomeworkQuestion.getType());
			cq.setNewCorrect(true);
			studentHomeworkQuestionRepo.save(cq);

			for (StudentHomeworkAnswer studentHomeworkAnswer : studentHomeworkAnswers) {
				StudentHomeworkAnswer ca = new StudentHomeworkAnswer();
				ca.setStudentHomeworkQuestionId(cq.getId());
				ca.setResult(HomeworkAnswerResult.INIT);
				ca.setAutoResult(HomeworkAnswerResult.INIT);
				ca.setSequence(studentHomeworkAnswer.getSequence());
				studentHomeworkAnswerRepo.save(ca);
			}
		}
	}

	@Override
	@Transactional
	public void createStudentHomeworkCorrectQuestion(Collection<Long> studentHomeworkQuestionIds) {
		List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionRepo
				.mgetList(studentHomeworkQuestionIds);
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
			if (!shq.isCorrect() && !shq.isNewCorrect()) {
				List<StudentHomeworkAnswer> studentHomeworkAnswers = studentHomeworkAnswerRepo
						.find("$zyFindByStuHkQuestion", Params.param("stuHkQuestionId", shq.getId())).list();

				// 错题产生订正题
				StudentHomeworkQuestion cq = new StudentHomeworkQuestion();
				cq.setStudentHomeworkId(shq.getStudentHomeworkId());
				cq.setQuestionId(shq.getQuestionId());
				cq.setResult(HomeworkAnswerResult.INIT);
				cq.setType(shq.getType());
				cq.setNewCorrect(true);
				studentHomeworkQuestionRepo.save(cq);

				for (StudentHomeworkAnswer studentHomeworkAnswer : studentHomeworkAnswers) {
					StudentHomeworkAnswer ca = new StudentHomeworkAnswer();
					ca.setStudentHomeworkQuestionId(cq.getId());
					ca.setSequence(studentHomeworkAnswer.getSequence());
					ca.setResult(HomeworkAnswerResult.INIT);
					ca.setAutoResult(HomeworkAnswerResult.INIT);
					studentHomeworkAnswerRepo.save(ca);
				}
			}
		}
	}

	@Override
	@Transactional
	public StudentHomeworkQuestion getNewCorrectQuestion(long studentHomeworkId, long questionId) {
		List<StudentHomeworkQuestion> shqs = studentHomeworkQuestionRepo.find("$getNewCorrectQuestion",
				Params.param("studentHomeworkId", studentHomeworkId).put("questionId", questionId)).list();
		return shqs.size() > 0 ? shqs.get(0) : null;
	}
}
