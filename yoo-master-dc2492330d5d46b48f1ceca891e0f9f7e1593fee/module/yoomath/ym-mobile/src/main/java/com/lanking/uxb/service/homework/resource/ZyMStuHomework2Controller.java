package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.correct.vo.AnswerCorrectResult;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 悠数学移动端(学生作业相关接口)
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/hk/2")
public class ZyMStuHomework2Controller {

	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private StudentHomeworkService stuHomeworkService;
	@Autowired
	private HomeworkQuestionService hqService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyStudentHomeworkQuestionService zyStuHkQuestionService;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagStuKpService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private HomeworkService hkService;

	/**
	 * 作业首页数据接口(历史作业返回已提交未下发的作业)
	 * 
	 * @since yoomath(mobile) V1.1.0
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
			dataMap.put("historyCount", stuHkService.countHomeworks(Security.getUserId(),
					Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED)));
			// 历史作业
			VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
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
	 * 作业首页历史作业数据接口(历史作业返回已提交未下发的作业)
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory(long cursor, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
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
		Map<String, Object> data = new HashMap<String, Object>(3);
		// 学生作业数据
		StudentHomework studentHomework = stuHomeworkService.get(stuHkId);
		// 过滤不存在
		if (studentHomework == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}
		
		// 作业数据
		Homework homework = hkService.get(studentHomework.getHomeworkId());
				
		StudentHomeworkConvertOption stuHkOption = new StudentHomeworkConvertOption();
		stuHkOption.setInitStuHomeworkWrongAndCorrect(true);
		stuHkOption.setInitHomework(true);
		stuHkOption.setInitMessages(true);
		VStudentHomework vstudentHomework = stuHkConvert.to(studentHomework, stuHkOption);
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
		
		for (VQuestion v : vs) {
			//只要答案错了就设置颜色
			if (v.getType() == Type.FILL_BLANK) {
				int size = v.getStudentHomeworkAnswers().size();
				for (int i = 0; i < size; i++) {
					HomeworkAnswerResult result = v.getStudentHomeworkAnswers().get(i).getResult();
					if(result == HomeworkAnswerResult.RIGHT|| result == HomeworkAnswerResult.WRONG) {
						boolean rightAnswer = (result == HomeworkAnswerResult.RIGHT);
						v.getStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils
								.process(v.getStudentHomeworkAnswers().get(i).getContent(), rightAnswer, true));
					}
				}
				
				int correctedSize = v.getCorrectStudentHomeworkAnswers().size();
				for (int i = 0; i < correctedSize; i++) {
					HomeworkAnswerResult result = v.getCorrectStudentHomeworkAnswers().get(i).getResult();
					if(result == HomeworkAnswerResult.RIGHT|| result == HomeworkAnswerResult.WRONG) {
						boolean rightAnswer = (result == HomeworkAnswerResult.RIGHT);
						v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils
								.process(v.getCorrectStudentHomeworkAnswers().get(i).getContent(), rightAnswer, true));
					}
				}
			}
			
			// 顺便处理下申述
			/**
			 * 处理本题是否可申述. <br>
			 * 1.填空题任意一空被批改错误的，有申诉入口 <br>
			 * 2.解答题"小优快批"批改的，且被批改为完全错误的，有申诉入口 <br>
			 * 3.其他情况下，无申诉入口
			 */
			VStudentHomeworkQuestion vStudentHomeworkQuestion = v.getStudentHomeworkQuestion();
			getAppeal(homework, v, vStudentHomeworkQuestion);
		}
		
		// 作业下发作业状态并且是学生主动提交（做的过程中下发作业时，不处理）的时候设置薄弱知识点
		if (studentHomework.getRightRate() != null && studentHomework.getStuSubmitAt() != null) {
			// 返回此学生薄弱知识点
			List<VKnowledgePoint> knowledgePoints = vstudentHomework.getHomework().getKnowledgePoints();
			if (CollectionUtils.isNotEmpty(knowledgePoints)) {
				// 新知识点
				Set<Long> codes = new HashSet<Long>(knowledgePoints.size());
				for (VKnowledgePoint knowledgePoint : knowledgePoints) {
					codes.add(knowledgePoint.getCode());
				}

				// 与web端保持一致
				List<DiagnosticStudentClassKnowpoint> list = diagStuKpService.queryHistoryWeakListByCodes(
						studentHomework.getStudentId(), vstudentHomework.getHomework().getHomeworkClazzId(), codes);
				if (list.size() > 0) {
					Set<Long> weakCodes = new HashSet<Long>(knowledgePoints.size());
					for (DiagnosticStudentClassKnowpoint dsck : list) {
						weakCodes.add(dsck.getKnowpointCode());
					}
					List<KnowledgePoint> weakKnowledges = knowledgePointService.mgetList(weakCodes);
					data.put("weakKnowpoints", knowledgePointConvert.to(weakKnowledges));
				} else {
					data.put("weakKnowpoints", Lists.newArrayList());
				}
			}
		} else if (studentHomework.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
			int predictTime = 0;
			for (VQuestion v : vs) {
				predictTime += questionService.calPredictTime(v.getType(), v.getDifficulty(), v.getSubject().getCode());
			}
			data.put("predictTime", predictTime);
		}
		data.put("questions", vs);
		// 倒计时时间
		Date d = vstudentHomework.getHomework().getDeadline();
		long deadline = d.getTime() - new Date().getTime();
		data.put("deadline", deadline < 10000 ? 0 : deadline - 10000);
		return new Value(data);
	}
	
	
	/**
	 * 获取单个题目
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param sHkQuestionId
	 *            学生作业题目ID
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "viewQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value viewQuestion(long sHkQuestionId) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		
		StudentHomeworkQuestion studentHomeworkQuestion = shqService.get(sHkQuestionId);
		if (studentHomeworkQuestion == null) {
			return new Value(new IllegalArgException());
		}
		
		Question question = questionService.get(studentHomeworkQuestion.getQuestionId());
		
		MemberType memberType = SecurityContext.getMemberType();
		
		// 设置不查询无用的初始化数据
		QuestionConvertOption option = new QuestionConvertOption();
		option.setStudentHomeworkId(studentHomeworkQuestion.getStudentHomeworkId());
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

		VQuestion v = questionConvert.to(question, option);
		
		//如果是老订正题
		if(studentHomeworkQuestion.isCorrect()) {
			v.setCorrectQuestion(true);
		}
		
		// 学生作业数据
		StudentHomework studentHomework = stuHomeworkService.get(studentHomeworkQuestion.getStudentHomeworkId());
		
		// 作业数据
		Homework homework = hkService.get(studentHomework.getHomeworkId());
		
		//只要答案错了就设置颜色
		if (v.getType() == Type.FILL_BLANK) {
			int size = v.getStudentHomeworkAnswers().size();
			for (int i = 0; i < size; i++) {
				HomeworkAnswerResult result = v.getStudentHomeworkAnswers().get(i).getResult();
				if(result == HomeworkAnswerResult.RIGHT|| result == HomeworkAnswerResult.WRONG) {
					boolean rightAnswer = (result == HomeworkAnswerResult.RIGHT);
					v.getStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils
							.process(v.getStudentHomeworkAnswers().get(i).getContent(), rightAnswer, true));
				}
			}
			
			int correctedSize = v.getCorrectStudentHomeworkAnswers().size();
			for (int i = 0; i < correctedSize; i++) {
				HomeworkAnswerResult result = v.getCorrectStudentHomeworkAnswers().get(i).getResult();
				if(result == HomeworkAnswerResult.RIGHT|| result == HomeworkAnswerResult.WRONG) {
					boolean rightAnswer = (result == HomeworkAnswerResult.RIGHT);
					v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils
							.process(v.getCorrectStudentHomeworkAnswers().get(i).getContent(), rightAnswer, true));
				}
			}
		}
		
		// 顺便处理下申述
		/**
		 * 处理本题是否可申述. <br>
		 * 1.填空题任意一空被批改错误的，有申诉入口 <br>
		 * 2.解答题"小优快批"批改的，且被批改为完全错误的，有申诉入口 <br>
		 * 3.其他情况下，无申诉入口
		 */
		VStudentHomeworkQuestion vStudentHomeworkQuestion = v.getStudentHomeworkQuestion();
		getAppeal(homework, v, vStudentHomeworkQuestion);
		
		data.put("question", v);
		
		return new Value(data);
	}

	private void getAppeal(Homework homework, VQuestion v, VStudentHomeworkQuestion vStudentHomeworkQuestion) {
		if ((v.getType() == Question.Type.FILL_BLANK && vStudentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG)
				|| (v.getType() == Question.Type.QUESTION_ANSWERING
						&& homework.isAnswerQuestionAutoCorrect()
						&& vStudentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG && vStudentHomeworkQuestion.getRightRate() == 0)) {
			Boolean canAppeal = true;
			Boolean allBlank = true;
			
			// 填空题如果一个空都没有作答，不能申述
			if(v.getType() == Question.Type.FILL_BLANK) {
				for (VStudentHomeworkAnswer sha : v.getStudentHomeworkAnswers()) {
					if (StringUtils.isNotBlank(sha.getContent()) && sha.getResult() == HomeworkAnswerResult.WRONG) {
						allBlank = false;
					}
				}
				
				if(allBlank) {
					canAppeal = false;
				}
			}
					
			// 解答题 如果没有作答，也不能申述
			if (v.getType() == Question.Type.QUESTION_ANSWERING && (vStudentHomeworkQuestion.getAnswerImg() == null || vStudentHomeworkQuestion.getAnswerImgId() <= 0)) {
				canAppeal = false;
			}
			
			vStudentHomeworkQuestion.setCanAppeal(canAppeal);
		}
	}
}
