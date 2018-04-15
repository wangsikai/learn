package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
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
import com.google.common.collect.Maps;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.examPaper.api.SmartPaperService;
import com.lanking.uxb.service.examPaper.form.SmartExamPaperForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 学生专题练习
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "zy/s/topic")
public class ZyStuTopicPracticeController {

	@Autowired
	private SmartPaperService smartPaperService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private StudentQuestionAnswerService stuQuestionAnswerService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private KnowledgePointConvert pointConvert;
	@Autowired
	private ZyStudentFallibleQuestionService stuFallService;
	@Autowired
	private ZyStudentFallibleQuestionConvert stuFallConvert;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 拉取题目<br>
	 * 选择题 4 题、填空题 6 题<br>
	 * 对每个题型而言，难度[0.8,1]的80%，难度[0.4,0.8)的20%
	 * 
	 * @param knowledgeCodes
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "findQuestionList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findQuestionList(@RequestParam(value = "knowledgeCodes") List<Long> knowledgeCodes) {
		SmartExamPaperForm form = new SmartExamPaperForm();
		form.setKnowledgeCodes(knowledgeCodes);
		form.setChoiceNum(4);
		form.setFillBlankNum(6);
		form.setBasePercent(80);
		form.setRaisePercent(20);
		form.setSprintPercent(0);
		List<Long> questionIds = smartPaperService.queryQuestionsByIndex(form);
		List<Question> questionList = questionService.mgetList(questionIds);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("questionList",
				questionConvert.to(questionList, new QuestionConvertOption(false, true, true, true, true, null)));
		Map<Long, VKnowledgePoint> vkpMap = pointConvert.mget(knowledgeCodes);
		List<Long> kcodes = knowledgePointCardService.findHasCardKnowledgePoint(knowledgeCodes);
		for (Long code : vkpMap.keySet()) {
			if (kcodes.contains(code)) {
				vkpMap.get(code).setHasCard(true);
			}
		}
		map.put("kPoint", vkpMap);
		Map<Long, VStudentFallibleQuestion> stuFallMap = new HashMap<Long, VStudentFallibleQuestion>();
		List<VStudentFallibleQuestion> vlist = stuFallConvert
				.to(stuFallService.mgetQuestionList(questionIds, Security.getUserId()));
		for (VStudentFallibleQuestion s : vlist) {
			stuFallMap.put(s.getQuestionId(), s);
		}
		map.put("questionStat", stuFallMap);
		return new Value(map);
	}

	/**
	 * 提交错题练习,暂时不提交答案,只记录练习次数
	 * 
	 * @since 2.1
	 * @param id
	 *            错题集记录ID
	 * @param questionId
	 *            题目ID
	 * @param answer
	 *            答案JSON格式 <br>
	 * 
	 *            <pre>
	 *            [
	 *            	{qid:题目ID,answers:[]},
	 *            	{qid:子题ID,answers:[]},
	 *            	{qid:子题ID,answers:[]}
	 *            ]
	 *            </pre>
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "exercise_commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value exerciseCommit(long id, Long questionId, String answer, HomeworkAnswerResult result, Long answerImg,
			Integer rightRate, @RequestParam(required = false) List<HomeworkAnswerResult> itemResults) {
		Map<Long, List<String>> latex_answers = Maps.newHashMap();
		Map<Long, List<String>> asciimath_answers = Maps.newHashMap();
		if (answer != null) {
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				long qid = answerObject.getLong("qid");
				JSONArray array = answerObject.getJSONArray("answers");
				List<String> latexs = new ArrayList<String>(array.size());
				List<String> asciis = new ArrayList<String>(array.size());
				for (Object object : array) {
					latexs.add(((JSONObject) object).getString("content"));
					asciis.add(((JSONObject) object).getString("contentAscii"));
				}
				latex_answers.put(qid, latexs);
				asciimath_answers.put(qid, asciis);
			}
		}
		List<Long> answerImgs = null;
		if (answerImg != null && answerImg > 0) {
			answerImgs = new ArrayList<Long>(1);
			answerImgs.add(answerImg);
		}
		stuQuestionAnswerService.create(Security.getUserId(), questionId, latex_answers, asciimath_answers, answerImgs,
				itemResults, rightRate, result == null ? HomeworkAnswerResult.UNKNOW : result,
				StudentQuestionAnswerSource.TOPIC_PRACTICE, new Date());

		// 薄弱知识项练习，题量任务
		JSONObject obj = new JSONObject();
		obj.put("taskCode", 101020016);
		obj.put("userId", Security.getUserId());
		obj.put("isClient", Security.isClient()); // web端
		Map<String, Object> pms = new HashMap<String, Object>(1);
		pms.put("questionCount", 1);
		obj.put("params", pms);
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(obj).build());

		return new Value();
	}
}
