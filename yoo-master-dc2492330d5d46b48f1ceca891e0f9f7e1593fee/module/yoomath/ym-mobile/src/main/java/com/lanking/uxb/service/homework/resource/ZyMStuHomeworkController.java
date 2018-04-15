package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.StatusCode;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.homework.form.HomeworkQuestionForm;
import com.lanking.uxb.service.homework.form.SaveHomeworkQuestionForm;
import com.lanking.uxb.service.latex.api.LatexService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.web.resource.ZyStuHomeworkController;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 悠数学移动端(学生作业相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/hk")
public class ZyMStuHomeworkController {

	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyStuHomeworkController stuHomeworkController;
	@Autowired
	private StudentHomeworkService stuHomeworkService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyStudentHomeworkQuestionService zyStuHkQuestionService;
	@Autowired
	private StudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private LatexService latexService;
	@Autowired
	private ZyStudentHomeworkAnswerService zyStuHkAnswerService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 作业首页数据接口
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param historySize
	 *            获取历史记录的条数
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			dataMap.put("historyCount",
					stuHkService.countHomeworks(Security.getUserId(), Sets.newHashSet(StudentHomeworkStatus.ISSUED)));
			// 历史作业
			VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.ISSUED));
			historyQuery.setCourse(false);
			historyQuery.setStudentId(Security.getUserId());
			CursorPage<Long, StudentHomework> historyPage = stuHkService.query(historyQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
			if (historyPage.isEmpty()) {
				vp.setCursor(Long.MAX_VALUE);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				vp.setCursor(historyPage.getNextCursor());
				vp.setItems(stuHkConvert.to(historyPage.getItems(), false, true, false, false));
			}
			dataMap.put("history", vp);
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			CursorPage<Long, StudentHomework> todoPage = stuHkService.query(todoQuery, CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				dataMap.put("todo", Collections.EMPTY_LIST);
			} else {
				dataMap.put("todo", stuHkConvert.to(todoPage.getItems(), false, true, false, false));
			}
		}
		return new Value(dataMap);
	}

	/**
	 * 作业首页历史作业数据接口
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory(long cursor, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		CursorPage<Long, StudentHomework> historyPage = stuHkService.query(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));
		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(stuHkConvert.to(historyPage.getItems(), false, true, false, false));
		}
		return new Value(vp);
	}

	/**
	 * 获取作业题目
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param stuHkId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value view(long stuHkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 学生作业数据
		StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
		studentHomework.setInitHomework(true);
		VStudentHomework vstudentHomework = stuHkConvert.to(studentHomework, false, true, false, false);
		data.put("studentHomework", vstudentHomework);
		// 题目列表
		List<Long> qid = hqService.getQuestion(studentHomework.getHomeworkId());
		List<Long> correctQIds = zyStuHkQuestionService.getCorrectQuestions(stuHkId);
		qid.addAll(correctQIds);
		List<Question> qs = new ArrayList<Question>(qid.size());
		Map<Long, Question> qsMap = questionService.mget(qid);
		for (Long id : qid) {
			if (correctQIds.contains(id)) {
				qsMap.get(id).setCorrectQuestion(true);
			}
			qs.add(qsMap.get(id));
		}
		MemberType memberType = SecurityContext.getMemberType();

		// 设置不查询无用的初始化数据
		QuestionConvertOption option = new QuestionConvertOption();
		option.setStudentHomeworkId(stuHkId);
		option.setInitSub(true);
		option.setAnalysis(memberType != null && memberType == MemberType.VIP); // 解析
		option.setAnswer(true); // 答案
		option.setCollect(true); // 收藏
		option.setInitExamination(true); // 考点
		option.setInitKnowledgePoint(true); // 新知识点
		option.setInitMetaKnowpoint(false);
		option.setInitPhase(false);
		option.setInitSub(false);
		option.setInitQuestionType(false);
		option.setInitTextbookCategory(false);
		List<VQuestion> vs = questionConvert.to(qs, option);
		boolean hasQuestionAnswering = false;
		if (studentHomework.getStatus() == StudentHomeworkStatus.ISSUED) {// 作业下发作业状态的时候设置答案颜色
			for (VQuestion v : vs) {
				if (v.getType() == Type.FILL_BLANK) {
					boolean rightAnswer = v.getStudentHomeworkQuestion().getResult() == HomeworkAnswerResult.RIGHT;
					int size = v.getStudentHomeworkAnswers().size();
					for (int i = 0; i < size; i++) {
						v.getStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils
								.process(v.getStudentHomeworkAnswers().get(i).getContent(), rightAnswer, true));
					}
				}
				if (v.getType() == Type.QUESTION_ANSWERING && !hasQuestionAnswering) {
					hasQuestionAnswering = true;
				}
			}
		} else {
			for (VQuestion v : vs) {
				if (v.getType() == Type.QUESTION_ANSWERING && !hasQuestionAnswering) {
					hasQuestionAnswering = true;
				}
			}
		}
		if (hasQuestionAnswering) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_NOT_SUPPORT));
		} else {
			data.put("questions", vs);
			// 倒计时时间
			Date d = vstudentHomework.getHomework().getDeadline();
			long deadline = d.getTime() - new Date().getTime();
			data.put("deadline", deadline < 10000 ? 0 : deadline - 10000);
			return new Value(data);
		}
	}

	/**
	 * 提交作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param hkId
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(long hkId) {
		Value value = stuHomeworkController.commit(hkId);
		if (value.getRet_code() != StatusCode.SUCCEED) {// 有异常转换异常码
			if (value.getRet_code() == ZuoyeException.ZUOYE_ISSUED_INADVANCE) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_ISSUED_INADVANCE));
			} else {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED));
			}
		}
		// 用户行为动作
		userActionService.asyncAction(UserAction.SUBMIT_HOMEWORK, Security.getUserId(), null);
		return value;
	}

	/**
	 * 做作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "do", method = { RequestMethod.POST, RequestMethod.GET })
	public Value $do(HomeworkQuestionForm form) {
		if (form == null || form.getType() != 1 || form.getHomeworkId() <= 0 || form.getStuHkId() <= 0
				|| form.getQuestionId() <= 0 || form.getStuHkQuestionId() <= 0
				|| StringUtils.isBlank(form.getAsciimathAnswers()) || form.getTime() <= 0) {
			return new Value(new IllegalArgException());
		}

		if (StringUtils.isBlank(form.getAsciimathAnswers())) {
			form.setAsciimathAnswerList(Collections.EMPTY_LIST);
		} else {
			form.setAsciimathAnswerList(JSONArray.parseArray(form.getAsciimathAnswers(), String.class));
		}

		Question question = questionService.get(form.getQuestionId());
		if (question.getType() == Type.FILL_BLANK) {
			if (form.getAsciimathAnswerList().size() != question.getAnswerNumber().intValue()) {
				return new Value(new IllegalArgException());
			}
			if (StringUtils.isBlank(form.getMathmlAnswers())) {
				form.setLatexAnswerList(Collections.EMPTY_LIST);
			} else {
				JSONArray mmlArray = JSONArray.parseArray(form.getMathmlAnswers());
				List<String> mmls = new ArrayList<String>(mmlArray.size());
				for (int i = 0; i < mmlArray.size(); i++) {
					mmls.add(mmlArray.getString(i));
				}
				List<String> latexs = latexService.multiMml2Latex(mmls);
				form.setLatexAnswerList(latexs);
			}
		} else {
			form.setLatexAnswerList(form.getAsciimathAnswerList());
		}

		JSONArray answerArray = new JSONArray();
		JSONObject answerObject = new JSONObject();
		answerObject.put("stuQuestionId", form.getStuHkQuestionId());
		JSONArray answerContentArray = new JSONArray();
		for (int i = 0; i < form.getLatexAnswerList().size(); i++) {
			JSONObject answerContentObject = new JSONObject();
			String asciimath = form.getAsciimathAnswerList().get(i);
			String latex = form.getLatexAnswerList().get(i);
			answerContentObject.put("sequence", i + 1);
			if (StringUtils.isNotBlank(asciimath)) {
				if (question.getType() == Type.FILL_BLANK) {
					answerContentObject.put("contentAscii", "<ux-mth>" + asciimath + "</ux-mth>");
					answerContentObject.put("content", latex);
				} else {
					answerContentObject.put("contentAscii", asciimath);
					answerContentObject.put("content", latex);
				}
			} else {
				answerContentObject.put("contentAscii", StringUtils.EMPTY);
				answerContentObject.put("content", StringUtils.EMPTY);
			}
			answerContentArray.add(answerContentObject);
		}
		answerObject.put("answers", answerContentArray);
		answerArray.add(answerObject);
		Value value = stuHomeworkController.doOneQuestion(form.getQuestionId(), form.getStuHkId(),
				answerArray.toString(), form.getImage(), form.getTime(), false, form.getCompletionRate(), false);
		if (value.getRet_code() != StatusCode.SUCCEED) {// 有异常转换异常码
			if (value.getRet_code() == ZuoyeException.ZUOYE_AUTO_COMMITED) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED));
			} else {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_SUBMITTED));
			}
		}
		question.setCorrectQuestion(stuHkQuestionService.get(form.getStuHkQuestionId()).isCorrect());
		return new Value(
				questionConvert.to(question, new QuestionConvertOption(false, false, true, true, form.getStuHkId())));
	}

	/**
	 * 提交作业，在提交作业之前会先对答案进行保存。
	 *
	 * @since 3.0.0
	 * @param form
	 *            {@link SaveHomeworkQuestionForm} 作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "2/commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit2(SaveHomeworkQuestionForm form) {
		try {
			// 先进行答案的保存
			save(form);
		} catch (AbstractException e) {
			return new Value(e);
		}
		Value value = stuHomeworkController.commit(form.getHomeworkId());
		if (value.getRet_code() != StatusCode.SUCCEED) {// 有异常转换异常码
			if (value.getRet_code() == ZuoyeException.ZUOYE_ISSUED_INADVANCE) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_ISSUED_INADVANCE));
			} else {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED));
			}
		}

		// 用户行为动作
		userActionService.asyncAction(UserAction.SUBMIT_HOMEWORK, Security.getUserId(), null);
		// 延迟处理
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 保存答案
	 *
	 * 答案提交格式:详见Wiki文档
	 *
	 * @param form
	 *            {@link SaveHomeworkQuestionForm}
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "saveDo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveDo(SaveHomeworkQuestionForm form) {
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		try {
			long timestamp = save(form);
			retMap.put("timestamp", timestamp);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value(retMap);
	}

	@SuppressWarnings("unchecked")
	private long save(SaveHomeworkQuestionForm form) {
		if (null == form || form.getTime() <= 0 || form.getHomeworkId() <= 0 || form.getStuHkId() <= 0) {
			throw new IllegalArgException();
		}

		List<HomeworkQuestionForm> forms = form.getHomeworkQuestionForms();
		if (CollectionUtils.isEmpty(forms)) {
			throw new IllegalArgException();
		}

		StudentHomework studentHomework = stuHomeworkService.get(form.getStuHkId());
		// 请求的学生作业不存在或者是此份作业不属于此学生
		if (studentHomework == null || studentHomework.getStudentId() != Security.getUserId()) {
			throw new IllegalArgException();
		}

		if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
			if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
				// 作业被自动提交
				if (studentHomework.getStuSubmitAt() == null) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED);
				} else {
					// 自己已经提交
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_SUBMITTED);
				}
			} else {
				// 作业被下发
				if (!(studentHomework.getSubmitAt() == null && studentHomework.getStuSubmitAt() == null)) {
					throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_ISSUED);
				}
			}
		}

		// 对应的答案Map StudentHomeworkQuestion.id -> 答案列表
		Map<Long, List<String>> asciiQuestionMathMap = new HashMap<Long, List<String>>(forms.size());
		Map<Long, List<String>> latexQuestionMathMap = new HashMap<Long, List<String>>(forms.size());
		// 处理学生答题中的图片
		Map<Long, List<Long>> solvingImgs = new HashMap<Long, List<Long>>(forms.size());
		Map<Long, Integer> doTimes = new HashMap<Long, Integer>(forms.size());

		List<Long> questionIds = new ArrayList<Long>(forms.size());
		for (HomeworkQuestionForm f : forms) {
			questionIds.add(f.getQuestionId());
			if (f.getImage() != null && f.getImage() > 0) {
				// 老版本上传一张图片
				List<Long> imgs = new ArrayList<Long>(1);
				imgs.add(f.getImage());
				solvingImgs.put(f.getStuHkQuestionId(), imgs);
			} else {
				solvingImgs.put(f.getStuHkQuestionId(), f.getImages());
			}

			doTimes.put(f.getStuHkQuestionId(), f.getDoTime() == null ? 0 : f.getDoTime());
		}

		Map<Long, Question> questionMap = questionService.mget(questionIds);
		// 组成StudentHomeworkQuestion.id -> Question.Type的Map
		Map<Long, Type> questionTypeMap = new HashMap<Long, Type>(questionMap.size());

		// 处理学生提交的答案
		for (HomeworkQuestionForm f : forms) {
			Long stuHkQuestionId = f.getStuHkQuestionId();
			Question q = questionMap.get(f.getQuestionId());
			questionTypeMap.put(stuHkQuestionId, q.getType());

			if (StringUtils.isBlank(f.getAsciimathAnswers())) {
				asciiQuestionMathMap.put(stuHkQuestionId, Collections.EMPTY_LIST);
			} else {
				List<String> answerContents = JSONArray.parseArray(f.getAsciimathAnswers(), String.class);
				if (q.getType() == Type.FILL_BLANK) {
					List<String> tmpAnswerContents = new ArrayList<String>(answerContents.size());
					for (String answerContent : answerContents) {
						answerContent = "<ux-mth>" + answerContent + "</ux-mth>";
						tmpAnswerContents.add(answerContent);
					}
					asciiQuestionMathMap.put(stuHkQuestionId, tmpAnswerContents);

					if (asciiQuestionMathMap.get(stuHkQuestionId).size() != q.getAnswerNumber()) {
						throw new IllegalArgException();
					}

					String mmlMathStr = f.getMathmlAnswers();
					if (StringUtils.isBlank(mmlMathStr)) {
						latexQuestionMathMap.put(stuHkQuestionId, Collections.EMPTY_LIST);
					} else {
						JSONArray mmlArray = JSONArray.parseArray(mmlMathStr);
						List<String> mmls = new ArrayList<String>(mmlArray.size());
						for (int i = 0; i < mmlArray.size(); i++) {
							mmls.add(mmlArray.getString(i));
						}

						List<String> latexs = latexService.multiMml2Latex(mmls);

						latexQuestionMathMap.put(stuHkQuestionId, latexs);
					}
				} else {
					asciiQuestionMathMap.put(stuHkQuestionId, answerContents);
					latexQuestionMathMap.put(stuHkQuestionId, answerContents);
				}
			}
		}

		zyStuHkAnswerService.doQuestion(latexQuestionMathMap, asciiQuestionMathMap, solvingImgs, questionTypeMap,
				Security.getUserId(), null, doTimes);

		// 更新答题时间
		return stuHkService.updateHomeworkTime(form.getStuHkId(), Security.getUserId(), form.getTime(),
				form.getCompletionRate());
	}
}
