package com.lanking.uxb.service.web.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.common.value.VQuestionBase;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyTeacherCorrectQuestionConvert;
import com.lanking.uxb.service.zuoye.form.TeaCorrectQuestionForm2;
import com.lanking.uxb.service.zuoye.value.VTeacherCorrectQuestion;

/**
 * 教师批改题目
 *
 * @author xinyu.zhou
 * @since yoomath V1.9.1
 */
@RestController
@RequestMapping(value = "zy/t/hk/correct")
public class ZyTeaHomeworkCorrectController {

	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ZyStudentHomeworkQuestionService zyStuQuestionService;
	@Autowired
	private ZyTeacherCorrectQuestionConvert teacherCorrectQuestionConvert;
	@Autowired
	private QuestionBaseConvert<VQuestionBase> questionConvert;
	@Autowired
	private ZyStudentHomeworkAnswerService answerService;
	@Autowired
	private ZyStudentHomeworkService zyStudentHomeworkService;
	@Autowired
	private StudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private CorrectProcessor correctProcessor;

	/**
	 * 首页进入批改
	 *
	 * @param homeworkId
	 *            作业id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index(long homeworkId, @RequestParam(value = "questionId", required = false) Long questionId) {
		Homework hk = homeworkService.get(homeworkId);
		if (null == hk) {
			return new Value(new IllegalArgException());
		}
		// 作业未分发及作业已下发则不可以进行批改操作
		if (HomeworkStatus.INIT == hk.getStatus()) {
			return new Value(new NoPermissionException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(4);

//		boolean isLastCommit = false;
//		if (hk.getLastCommitAt() != null) {
//			isLastCommit = System.currentTimeMillis() > hk.getLastCommitAt().getTime()
//					+ Env.getInt("homework.allcommit.then") * 60 * 1000;
//		} else {
//			isLastCommit = hk.getDeadline().getTime() + Env.getInt("homework.allcommit.then") * 60 * 1000 < System
//					.currentTimeMillis();
//		}
		long notCorrectCount = zyStudentHomeworkService.countNotCorrect(homeworkId);
		retMap.put("notCorrect", notCorrectCount);
		if (notCorrectCount <= 0) {
			return new Value(retMap);
		}
		List<Question> questions = null;

//		if (isLastCommit) {
//			questions = zyStuQuestionService.findNeedCorrectQuestionAll(homeworkId);
//		} else {
//			questions = zyStuQuestionService.findNeedCorrectQuestion(homeworkId);
//		}
		questions = zyStuQuestionService.findNeedCorrectQuestionAll(homeworkId);

		if (CollectionUtils.isNotEmpty(questions)) {
			// 将待批改列表中第一条题目待批改的学生列表返回回去
			Question question = null;
			if (questionId != null) {
				for (Question q : questions) {
					if (q.getId().equals(questionId)) {
						question = q;
						break;
					}
				}
			} else {
				question = questions.get(0);
			}

			if (question == null) {
				return new Value(new IllegalArgException());
			}
			List<StudentHomeworkQuestion> studentHomeworkQuestions = zyStuQuestionService
					.queryCorrectQuestions(homeworkId, question.getId(), question.getType(), true);

			List<VTeacherCorrectQuestion> vs = teacherCorrectQuestionConvert.to(studentHomeworkQuestions);
			retMap.put("hkQuestions", vs);
		}

		retMap.put("questions", questionConvert.to(questions, new QuestionBaseConvertOption(true, true, true, false)));
		retMap.put("lastCommit", true);
		return new Value(retMap);
	}

	/**
	 * 查询作业中的题有哪些学生待批改
	 *
	 * @param homeworkId
	 *            作业id
	 * @param questionId
	 *            作业题目id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(long homeworkId, long questionId, Question.Type type) {
		Homework hk = homeworkService.get(homeworkId);
		if (null == hk) {
			return new Value(new IllegalArgException());
		}
		// 作业未分发及作业已下发则不可以进行批改操作
		if (HomeworkStatus.INIT == hk.getStatus()) {
			return new Value(new NoPermissionException());
		}
		boolean isLastCommit = false;
//		if (hk.getLastCommitAt() != null) {
//			isLastCommit = System.currentTimeMillis() > hk.getLastCommitAt().getTime()
//					+ Env.getInt("homework.allcommit.then") * 60 * 1000;
//		} else {
//			isLastCommit = hk.getDeadline().getTime() + Env.getInt("homework.allcommit.then") * 60 * 1000 < System
//					.currentTimeMillis();
//		}

		Map<String, Object> retMap = new HashMap<String, Object>(1);

		List<StudentHomeworkQuestion> studentHomeworkQuestions = zyStuQuestionService.queryCorrectQuestions(homeworkId,
				questionId, type, isLastCommit);

		List<VTeacherCorrectQuestion> vs = teacherCorrectQuestionConvert.to(studentHomeworkQuestions);

		retMap.put("hkQuestions", vs);

		return new Value(retMap);
	}

	/**
	 * 保存教师批改轨迹
	 *
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "saveAnnotate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveAnnotate(@RequestBody List<String> notations) {
		if (CollectionUtils.isEmpty(notations)) {
			return new Value(new IllegalArgException());
		}

		String generateImageId = notations.get(notations.size() - 3);
		String srcImageId = notations.get(notations.size() - 2);
		String stuHkQuestionId = notations.get(notations.size() - 1);
		notations.remove(notations.size() - 3);
		notations.remove(notations.size() - 2);
		notations.remove(notations.size() - 1);

		String jsonStr = JSON.toJSONString(notations);
		zyStuQuestionService.saveNotation(Long.parseLong(stuHkQuestionId), Long.parseLong(srcImageId),
				Long.parseLong(generateImageId), jsonStr, null, null, null);
		return new Value();
	}

	/**
	 * 查找题目老师批改轨迹
	 *
	 * @param stuHkQuestionId
	 *            学生作业题目id
	 * @param index
	 *            可以为空，说明是单图的，不为空说明多图
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "findNotation", method = { RequestMethod.POST, RequestMethod.GET })
	public Value findNotation(long stuHkQuestionId, Integer index) {
		return new Value(zyStuQuestionService.getNotation(stuHkQuestionId, index));
	}

	/**
	 * 
	 * @since 小悠快批，2018-2-13，注意新流程仅支持单题批改，不要批量题目批改提交！若原有批量题目提交的场景，请修改。
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save2(TeaCorrectQuestionForm2 form) {
		if (form.getAnswerResults() == null && form.getResult() == null && form.getRightRate() == null
				&& form.getNotationImageId() == null) {
			return new Value(new IllegalArgException());
		}
		try {
			// 调用新的批改流程
			QuestionCorrectObject questionCorrectObject = new QuestionCorrectObject();
			questionCorrectObject.setStudentHomeworkId(form.getStuHkId());
			questionCorrectObject.setStuHomeworkQuestionId(form.getStuHkQuestionId());
			questionCorrectObject.setQuestionType(form.getType());
			questionCorrectObject.setQuestionRightRate(form.getRightRate());
			questionCorrectObject.setQuestionResult(form.getResult());

			// 填空题
			if (CollectionUtils.isNotEmpty(form.getAnswerMap())) {
				questionCorrectObject.setAnswerResultMap(form.getAnswerMap());
			}
			questionCorrectObject.setNotation(form.getNotation());
			questionCorrectObject.setNotationImageId(form.getNotationImageId());
			questionCorrectObject.setNotationImageIds(form.getNotationImageIds());
			questionCorrectObject.setNotations(form.getNotations());
			questionCorrectObject.setAnswerImgId(form.getAnswerImgId());
			questionCorrectObject.setAnswerImgIds(form.getAnswerImgIds());
			correctProcessor.correctStudentHomeworkQuestion(Security.getUserId(), CorrectorType.TEACHER,
					questionCorrectObject);

		} catch (Exception e) {
			return new Value(new ServerException());
		}

		return new Value();
	}

	/**
	 * 新增查询界面批注的保存
	 * 
	 * wangsenhao 2017-1-10
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "saveInQuery", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveInQuery(TeaCorrectQuestionForm2 form) {
		if (form.getAnswerResults() == null && form.getResult() == null && form.getRightRate() == null
				&& form.getNotationImageId() == null) {
			return new Value(new IllegalArgException());
		}
		try {
			zyStuQuestionService.saveNotation(form);
		} catch (Exception e) {
			return new Value(new ServerException());
		}
		return new Value();
	}

	/**
	 * 若图片进行旋转,保存接口
	 *
	 * @param id
	 *            学生题目id
	 * @param image
	 *            旋转后的图片id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateAnswerImage", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateAnswerImage(long id, long image) {
		zyStuQuestionService.updateAnswerImage(id, image);
		return new Value();
	}

	/**
	 * 对学生答案进行批改
	 *
	 * 例如: { 'id1': 'WRONG', 'id2': 'RIGHT' }
	 *
	 * @param formStr
	 *            更新答案id->结果
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "saveAnswerResult")
	public Value saveAnswerResult(@RequestParam("formStr") String formStr) {

		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		JSONObject jsonObject = JSON.parseObject(formStr);
		for (String key : jsonObject.keySet()) {
			answerService.saveAnswerResult(Long.valueOf(key), HomeworkAnswerResult.valueOf(jsonObject.getString(key)));
		}

		return new Value();
	}
}
