package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZyStudentHomeworkAnswerServiceImpl implements ZyStudentHomeworkAnswerService {

	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> stuHkAnswerRepo;
	@Autowired
	@Qualifier("StudentHomeworkAnswerImageRepo")
	Repo<StudentHomeworkAnswerImage, Long> stuHkAnswerImgRepo;

	@Autowired
	private StudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private QuestionService questionService;

	@Transactional
	@Override
	public void doQuestion(long studentHomeworkQuestionId, List<String> answers, List<String> answerAsciis,
			Long solvingImg, long studentId) {
		stuHkQuestionService.updateAnswerImage(studentHomeworkQuestionId, solvingImg == null ? 0 : solvingImg);

		stuHkAnswerImgRepo.execute("$deleteByStuHkQuestion", Params.param("id", studentHomeworkQuestionId));
		if (solvingImg != null && solvingImg > 0) {
			StudentHomeworkAnswerImage img = new StudentHomeworkAnswerImage();
			img.setAnswerImg(solvingImg);
			img.setStudentHomeworkQuestionId(studentHomeworkQuestionId);

			stuHkAnswerImgRepo.save(img);
		}

		StudentHomeworkQuestion studentHomeworkQuestion = stuHkQuestionService.get(studentHomeworkQuestionId);
		Question question = questionService.get(studentHomeworkQuestion.getQuestionId());
		List<StudentHomeworkAnswer> list = stuHkAnswerService.find(studentHomeworkQuestionId);

		int index = 0;
		for (StudentHomeworkAnswer studentHomeworkAnswer : list) {
			if (question.getType() == Type.QUESTION_ANSWERING) {
				studentHomeworkAnswer.setContent(null);
				studentHomeworkAnswer.setContentAscii(null);
				studentHomeworkAnswer.setAnswerAt(new Date());
				studentHomeworkAnswer.setAnswerId(studentId);
			} else {
				if (index > answers.size() - 1) {
					studentHomeworkAnswer.setContent(null);
					studentHomeworkAnswer.setContentAscii(null);
					studentHomeworkAnswer.setAnswerAt(null);
					studentHomeworkAnswer.setAnswerId(null);
				} else {
					String answer = answers.get(index);
					if (StringUtils.isBlank(answer)) {
						studentHomeworkAnswer.setContent(answer);
					} else {
						studentHomeworkAnswer.setContent(answer.replaceAll("\\\\text\\{ \\}", ""));
					}
					studentHomeworkAnswer.setContentAscii(answerAsciis.get(index));
					studentHomeworkAnswer.setAnswerAt(new Date());
					studentHomeworkAnswer.setAnswerId(studentId);
				}
			}
			index++;
			stuHkAnswerRepo.save(studentHomeworkAnswer);
		}
		if (list.size() < answers.size()) {
			// 一般为多选题会有此情况
			for (int i = index; i < answers.size(); i++) {
				StudentHomeworkAnswer studentHomeworkAnswer = new StudentHomeworkAnswer();
				studentHomeworkAnswer.setStudentHomeworkQuestionId(studentHomeworkQuestionId);
				studentHomeworkAnswer.setSequence(index + 1);
				studentHomeworkAnswer.setContent(answers.get(i));
				studentHomeworkAnswer.setContentAscii(answerAsciis.get(i));
				studentHomeworkAnswer.setAnswerAt(new Date());
				studentHomeworkAnswer.setAnswerId(studentId);
				stuHkAnswerRepo.save(studentHomeworkAnswer);
			}
		}
	}

	@Transactional
	@Override
	public void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis, Long solvingImg,
			long studentId) {
		for (Long studentHomeworkQuestionId : answers.keySet()) {
			doQuestion(studentHomeworkQuestionId, answers.get(studentHomeworkQuestionId),
					answerAsciis.get(studentHomeworkQuestionId), solvingImg, studentId);
		}
	}

	@Override
	@Transactional
	public void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis,
			Map<Long, Long> solvingImgs, Map<Long, Question.Type> questionTypes, long studentId) {
		Date answerAt = new Date();
		for (Map.Entry<Long, List<String>> e : answers.entrySet()) {

			Long shqId = e.getKey();

			Question.Type questionType = questionTypes.get(shqId);
			Params params = Params.param();
			params.put("shqId", shqId);

			int sequence = 1;
			Long imageId = solvingImgs.get(shqId) == null ? 0L : solvingImgs.get(shqId);
			stuHkQuestionService.updateAnswerImage(shqId, imageId);

			stuHkAnswerImgRepo.execute("$deleteByStuHkQuestion", Params.param("id", shqId));
			if (imageId > 0) {
				StudentHomeworkAnswerImage img = new StudentHomeworkAnswerImage();
				img.setAnswerImg(imageId);
				img.setStudentHomeworkQuestionId(shqId);

				stuHkAnswerImgRepo.save(img);
			}

			if (questionType == Type.QUESTION_ANSWERING) {
				params.put("sequence", sequence);
				params.put("answerAt", answerAt);
				params.put("content", null);
				params.put("answerId", studentId);
				params.put("contentAscii", null);

				stuHkAnswerRepo.execute("$updateUserAnswer", params);
			} else if (questionType != Type.MULTIPLE_CHOICE) {
				// 非多选题情况
				List<String> latexAnswers = answers.get(shqId);
				List<String> asciiAnswers = answerAsciis.get(shqId);

				for (String answer : latexAnswers) {
					params.put("sequence", sequence);
					if (sequence > latexAnswers.size()) {
						params.put("answerAt", null);
						params.put("content", null);
						params.put("contentAscii", null);
						params.put("answerId", null);
						stuHkAnswerRepo.execute("$updateUserAnswer", params);
					} else {
						if (StringUtils.isBlank(answer)) {
							params.put("content", answer);
						} else {
							params.put("content", answer.replaceAll("\\\\text\\{ \\}", ""));
						}

						params.put("contentAscii", asciiAnswers.get(sequence - 1));
						params.put("answerAt", answerAt);
						params.put("answerId", studentId);
						stuHkAnswerRepo.execute("$updateUserAnswer", params);
					}

					sequence++;
				}
			} else {
				// 多选题首先将原答案清除，再进行保存操作
				stuHkAnswerRepo.execute("$deleteUserAnswer", Params.param("shqId", shqId));
				List<String> latexAnswers = answers.get(shqId);
				List<String> asciiAnswers = answerAsciis.get(shqId);
				for (String content : latexAnswers) {
					StudentHomeworkAnswer answer = new StudentHomeworkAnswer();
					String contentAscii = asciiAnswers.get(sequence - 1);
					answer.setAnswerAt(answerAt);
					answer.setAnswerId(studentId);
					answer.setSequence(sequence);
					answer.setContent(content);
					answer.setContentAscii(contentAscii);
					answer.setSequence(sequence);
					answer.setStudentHomeworkQuestionId(shqId);

					// 处理丢失标签的问题
					if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(contentAscii)) {
						Pattern p = Pattern.compile("<ux-mth>(.+?)</ux-mth>");
						Matcher m = p.matcher(contentAscii);
						if (m.find() && content.indexOf("<ux-mth>") == -1) {
							answer.setContent("<ux-mth>" + content + "</ux-mth>");
						}
					}

					stuHkAnswerRepo.save(answer);
					sequence++;
				}
			}

		}
	}

	@Override
	public List<StudentHomeworkAnswer> find(long homeworkId, long questionId) {
		return stuHkAnswerRepo.find("$zyFind", Params.param("homeworkId", homeworkId).put("questionId", questionId))
				.list();
	}

	@Override
	public boolean isDoHomework(long studentHomeworkId) {
		boolean isDo = stuHkAnswerRepo.find("$zyIsDoHomework", Params.param("stuHkId", studentHomeworkId)).count() > 0;
		if (!isDo) {// 如果题目答案都为空,则判断有没有解答题答过题的
			isDo = stuHkQuestionService.isQuestionAnsweringDone(studentHomeworkId);
		}
		return isDo;
	}

	@Override
	@Transactional
	public void saveAnswerResult(long id, HomeworkAnswerResult result) {
		stuHkAnswerRepo.execute("$zyCorrectById", Params.param("id", id).put("result", result.getValue()));
	}

	@Override
	public List<StudentHomeworkAnswer> listByQuestionId(long homeworkId, long questionId) {
		return stuHkAnswerRepo
				.find("$zyFindByQuestionId", Params.param("homeworkId", homeworkId).put("questionId", questionId))
				.list();
	}

	@Override
	@Transactional
	public void doQuestion(Map<Long, List<String>> answers, Map<Long, List<String>> answerAsciis,
			Map<Long, List<Long>> solvingImgs, Map<Long, Type> questionTypes, long studentId,
			Map<Long, List<String>> handWritings, Map<Long, Integer> doTime) {

		if (handWritings == null) {
			handWritings = Collections.EMPTY_MAP;
		}
		if (doTime == null) {
			doTime = Collections.EMPTY_MAP;
		}

		Date answerAt = new Date();
		for (Map.Entry<Long, List<String>> e : answers.entrySet()) {

			Long shqId = e.getKey();

			Integer time = doTime.get(shqId) == null ? 0 : doTime.get(shqId);
			stuHkQuestionService.updateDoQuestionTime(shqId, time);

			Question.Type questionType = questionTypes.get(shqId);
			Params params = Params.param();
			params.put("shqId", shqId);

			int sequence = 1;
			Long imageId;
			List<Long> solvingImgList = solvingImgs.get(shqId);
			imageId = CollectionUtils.isEmpty(solvingImgList) ? 0L : solvingImgList.get(0);

			stuHkAnswerImgRepo.execute("$deleteByStuHkQuestion", Params.param("id", shqId));

			List<String> handWriting = handWritings.get(shqId);
			int index = 0;
			// 保存答案数据
			if (CollectionUtils.isNotEmpty(solvingImgList)) {
				for (Long imgId : solvingImgList) {
					StudentHomeworkAnswerImage img = new StudentHomeworkAnswerImage();
					if (handWriting != null) {
						img.setHandWriting(handWriting.get(index));
					}
					img.setAnswerImg(imgId);
					img.setStudentHomeworkQuestionId(shqId);

					stuHkAnswerImgRepo.save(img);
					index++;
				}
			}

			stuHkQuestionService.updateAnswerImage(shqId, imageId);

			if (questionType == Type.QUESTION_ANSWERING) {
				params.put("sequence", sequence);
				params.put("answerAt", answerAt);
				params.put("content", null);
				params.put("answerId", studentId);
				params.put("contentAscii", null);

				stuHkAnswerRepo.execute("$updateUserAnswer", params);
			} else if (questionType != Type.MULTIPLE_CHOICE) {
				// 非多选题情况
				List<String> latexAnswers = answers.get(shqId);
				List<String> asciiAnswers = answerAsciis.get(shqId);

				for (String answer : latexAnswers) {
					params.put("sequence", sequence);
					if (sequence > latexAnswers.size()) {
						params.put("answerAt", null);
						params.put("content", null);
						params.put("contentAscii", null);
						params.put("answerId", null);
						stuHkAnswerRepo.execute("$updateUserAnswer", params);
					} else {
						if (StringUtils.isBlank(answer)) {
							params.put("content", answer);
						} else {
							params.put("content", answer.replaceAll("\\\\text\\{ \\}", ""));
						}

						params.put("contentAscii", asciiAnswers.get(sequence - 1));
						params.put("answerAt", answerAt);
						params.put("answerId", studentId);
						stuHkAnswerRepo.execute("$updateUserAnswer", params);
					}

					sequence++;
				}
			} else {
				// 多选题首先将原答案清除，再进行保存操作
				stuHkAnswerRepo.execute("$deleteUserAnswer", Params.param("shqId", shqId));
				List<String> latexAnswers = answers.get(shqId);
				List<String> asciiAnswers = answerAsciis.get(shqId);
				for (String content : latexAnswers) {
					StudentHomeworkAnswer answer = new StudentHomeworkAnswer();
					String contentAscii = asciiAnswers.get(sequence - 1);

					answer.setAnswerAt(answerAt);
					answer.setAnswerId(studentId);
					answer.setSequence(sequence);
					answer.setContent(content);
					answer.setContentAscii(contentAscii);
					answer.setSequence(sequence);
					answer.setStudentHomeworkQuestionId(shqId);

					// 处理丢失标签的问题
					if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(contentAscii)) {
						Pattern p = Pattern.compile("<ux-mth>(.+?)</ux-mth>");
						Matcher m = p.matcher(contentAscii);
						if (m.find() && content.indexOf("<ux-mth>") == -1) {
							answer.setContent("<ux-mth>" + content + "</ux-mth>");
						}
					}

					stuHkAnswerRepo.save(answer);
					sequence++;
				}
			}

		}

	}

}
