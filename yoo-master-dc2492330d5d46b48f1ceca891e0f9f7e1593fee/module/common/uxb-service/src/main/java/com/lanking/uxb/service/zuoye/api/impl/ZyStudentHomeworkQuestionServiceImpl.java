package com.lanking.uxb.service.zuoye.api.impl;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswerImage;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerImageService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.cache.CalculateRightRateCacheService;
import com.lanking.uxb.service.zuoye.form.TeaCorrectQuestionForm2;

@Transactional(readOnly = true)
@Service
public class ZyStudentHomeworkQuestionServiceImpl implements ZyStudentHomeworkQuestionService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("StudentHomeworkQuestionRepo")
	Repo<StudentHomeworkQuestion, Long> stuHkQRepo;
	@Autowired
	@Qualifier("StudentHomeworkAnswerRepo")
	Repo<StudentHomeworkAnswer, Long> stuHkAnswerRepo;
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> stuHkRepo;
	@Autowired
	@Qualifier("QuestionRepo")
	Repo<Question, Long> questionRepo;
	@Autowired
	private StudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private CalculateRightRateCacheService calculateRightRateCacheService;
	@Autowired
	private StudentHomeworkAnswerImageService studentAnswerImageService;

	@Override
	public Map<Long, StudentHomeworkQuestion> mget(List<Long> stuHKQIds) {
		return stuHkQRepo.mget(stuHKQIds);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, List<Long>> findHomeworkWrongQuestion(long homeworkId) {
		Map<Long, List<Long>> map = Maps.newHashMap();
		List<Map> list = stuHkQRepo.find("$zyFindHomeworkWrongQuestion", Params.param("homeworkId", homeworkId))
				.list(Map.class);
		for (Map m : list) {
			long studentId = ((BigInteger) m.get("student_id")).longValue();
			long questionId = ((BigInteger) m.get("question_id")).longValue();
			List<Long> qList = map.get(studentId);
			if (qList == null) {
				qList = Lists.newArrayList();
			}
			qList.add(questionId);
			map.put(studentId, qList);
		}
		return map;
	}

	@Override
	public List<Long> getCorrectQuestions(long stuHkId) {
		return stuHkQRepo.find("$zyGetCorrectQuestions", Params.param("stuHkId", stuHkId)).list(Long.class);
	}

	@Override
	public List<StudentHomeworkQuestion> queryCorrectQuestions(long homeworkId, long questionId, Question.Type type,
			boolean isLastCommit) {
		return stuHkQRepo.find("$zyListUncorrectStu", Params.param("homeworkId", homeworkId)
				.put("questionId", questionId).put("questionType", type.getValue()))
				.list();
	}

	@Override
	@Transactional
	public int saveNotation(long id, long srcId, long generateId, String notationImg, String notationPoints,
			Integer rightRate, HomeworkAnswerResult result) {
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
		return stuHkQRepo.execute("$zyUpdateNotation", params);
	}

	@Override
	public String getNotation(long id, Integer index) {
		if (index == null) {
			return stuHkQRepo.get(id).getAnswerNotation();
		} else {
			List<StudentHomeworkAnswerImage> list = studentAnswerImageService.findByStuHkQuestion(id);
			if (CollectionUtils.isNotEmpty(list)) {
				return list.get(index).getAnswerNotation();
			}
		}
		return "";
	}

	@Override
	public List<Question> findNeedCorrectQuestion(long hkId) {
		return questionRepo.find("$queryNeedCorrectQuestion", Params.param("hkId", hkId)).list();
	}

	@Override
	public List<Long> findNeedCorrectQuestions(long hkId) {
		return questionRepo.find("$queryNeedCorrectQuestions", Params.param("hkId", hkId)).list(Long.class);
	}

	@Override
	public List<Question> findNeedCorrectQuestionAll(long hkId) {
		return questionRepo.find("$queryNeedCorrectQuestionAll", Params.param("hkId", hkId)).list();
	}

	@Override
	public List<Long> findNeedCorrectQuestionsAll(long hkId) {
		return questionRepo.find("$queryNeedCorrectQuestionsAll", Params.param("hkId", hkId)).list(Long.class);
	}

	@Override
	public boolean isQuestionAnsweringDone(long studentHomeworkId) {
		return stuHkRepo.find("$zyIsQuestionAnsweringDone", Params.param("studentHomeworkId", studentHomeworkId))
				.count() > 0;
	}

	@Override
	@Transactional
	public void updateAnswerImage(long id, long image) {
		stuHkQRepo.execute("$zyUpdateAnswerImage", Params.param("id", id).put("image", image));
	}

	@Override
	public StudentHomeworkQuestion get(Long id) {
		return stuHkQRepo.get(id);
	}

	@Override
	public List<Long> listCorrectQuestions(long hkId) {
		return stuHkQRepo.find("$listCorrectQuestions", Params.param("hkId", hkId)).list(Long.class);
	}

	@Override
	public List<StudentHomeworkQuestion> listByQuestionId(long homeworkId, long questionId) {
		return stuHkQRepo
				.find("$zyFindByQuestionId", Params.param("homeworkId", homeworkId).put("questionId", questionId))
				.list();
	}

	@Override
	@Transactional
	public void updateDoQuestionTime(long studentHomeworkQuestionId, int dotime) {
		StudentHomeworkQuestion studentHomeworkQuestion = stuHkQRepo.get(studentHomeworkQuestionId);
		studentHomeworkQuestion.setDotime(dotime);
		stuHkQRepo.save(studentHomeworkQuestion);
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
		return stuHkQRepo.execute("$zyUpdateMultiNotation", params);
	}

	@Override
	public List<Map> findStuQuestionMapByOldCodes(long studentHomeworkQuestionId, List<Long> codes) {
		Params params = Params.param("studentHomeworkQuestionId", studentHomeworkQuestionId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("codes", codes);
		}
		return stuHkQRepo.find("$findStuQuestionMapByOldCodes", params).list(Map.class);
	}

	@Override
	public List<Map> findStuQuestionMapByNewCodes(long studentHomeworkQuestionId, List<Long> codes) {
		Params params = Params.param("studentHomeworkQuestionId", studentHomeworkQuestionId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("codes", codes);
		}
		return stuHkQRepo.find("$findStuQuestionMapByNewCodes", params).list(Map.class);
	}

	@Override
	@Transactional
	public void saveNotation(TeaCorrectQuestionForm2 form) {
		if (form.getAnswerImgId() != null) {
			saveNotation(form.getStuHkQuestionId(), form.getAnswerImgId(), form.getNotationImageId(),
					form.getNotation(), null, null, null);
			if (form.getAnswerImgIds() != null) {
				for (int i = 0; i < form.getAnswerImgIds().size(); i++) {
					this.saveMultiNotation(form.getStuHkQuestionId(), form.getAnswerImgIds().get(i),
							form.getAnswerImgIds().get(i), form.getNotationImageIds().get(i),
							form.getNotations().get(i), null);
				}
			} else {
				// 一张图的也需要更新student_homework_answer_image
				this.saveMultiNotation(form.getStuHkQuestionId(), form.getAnswerImgId(), form.getAnswerImgId(),
						form.getNotationImageId(), form.getNotation(), null);
			}
		}

	}

	@Override
	@Transactional
	public void updateVoice(int voiceTime, String fileKey, long shqId) {
		stuHkQRepo.execute("$zyUpdateVoice",
				Params.param("shqId", shqId).put("voiceTime", voiceTime).put("fileKey", fileKey));
	}

	@Override
	public List<Map> findWrongAndcorrectionQuestion(Collection<Long> stuHomeworkIds) {
		return stuHkQRepo.find("$findWrongAndcorrectionQuestion", Params.param("studentHomeworkIds", stuHomeworkIds))
				.list(Map.class);
	}

	@Override
	public List<Map> findCorrectedAndcorrectingQuestion(Collection<Long> stuHomeworkIds) {
		return stuHkQRepo
				.find("$findCorrectingAndCorrectedQuestion", Params.param("studentHomeworkIds", stuHomeworkIds))
				.list(Map.class);
	}

	@Override
	public List<Long> getNewCorrectQuestions(long stuHkId) {
		return stuHkQRepo.find("$zyGetNewCorrectQuestions", Params.param("stuHkId", stuHkId)).list(Long.class);
	}

	@Override
	public List<StudentHomeworkQuestion> listByStuHomeworkIdAndQuestionId(long stuHomeworkId, long questionId) {
		return stuHkQRepo
				.find("$listByStuHomeworkIdAndQuestionId", Params.param("stuHomeworkId", stuHomeworkId).put("questionId", questionId))
				.list();
	}

	@Override
	public List<Map> staticToBeCorrectedQuestionCount(Collection<Long> stuHomeworkIds) {
		return stuHkQRepo.find("$staticToBeCorrectedQuestionCount", Params.param("stuHomeworkIds", stuHomeworkIds))
				.list(Map.class);
	}
}
