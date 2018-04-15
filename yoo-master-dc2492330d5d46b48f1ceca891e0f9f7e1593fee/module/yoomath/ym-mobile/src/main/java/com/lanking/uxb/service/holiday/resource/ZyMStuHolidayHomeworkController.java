package com.lanking.uxb.service.holiday.resource;

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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHolidayHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.StatusCode;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvertOption;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvertOption;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemConvertOption;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkQuestionForm;
import com.lanking.uxb.service.holiday.form.HolidayHomeworkQuestionSaveForm;
import com.lanking.uxb.service.holiday.form.StuItemAnswerForm;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItem;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.latex.api.LatexService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseKnowpointService;

/**
 * 学生假期作业相关接口
 * 
 * @since yoomath mobile V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月19日
 */
@RestController
@RequestMapping("zy/m/s/holiday")
public class ZyMStuHolidayHomeworkController {
	@Autowired
	private HolidayStuHomeworkItemService holidayStuHomeworkItemService;
	@Autowired
	private HolidayStuHomeworkItemConvert holidayStuHomeworkItemConvert;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayHomeworkConvert holidayHomeworkConvert;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private HolidayHomeworkItemQuestionService holidayHomeworkItemQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseConvert<VHolidayQuestion> questionBaseConvert;
	@Autowired
	private HolidayStuHomeworkItemAnswerService holidayStuHomeworkItemAnswerService;
	@Autowired
	private HolidayStuHomeworkItemService itemService;
	@Autowired
	private HolidayQuestionConvert holidayQuestionConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private StuHolidayHomeworkController stuHolidayHomeworkController;
	@Autowired
	private LatexService latexService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyStudentExerciseKnowpointService knowpointService;

	/**
	 * 获取寒假作业项列表
	 * 
	 * @since yoomath mobile V1.1.0
	 * @param holidayStuHkId
	 *            学生寒假作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "items", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(Long holidayStuHkId) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		HolidayStuHomework holidayStuHomework = holidayStuHomeworkService.get(holidayStuHkId);
		if (holidayStuHomework == null) {
			return new Value(new EntityNotFoundException());
		}
		List<HolidayStuHomeworkItem> itemList = holidayStuHomeworkItemService
				.queryStuHkItems(holidayStuHomework.getHolidayHomeworkId(), Security.getUserId(), null, null);
		HolidayStuHomeworkItemConvertOption option = new HolidayStuHomeworkItemConvertOption();
		option.setInitUser(false);
		List<VHolidayStuHomeworkItem> items = holidayStuHomeworkItemConvert.to(itemList, option);
		for (VHolidayStuHomeworkItem item : items) {
			if (item.getStatus() == StudentHomeworkStatus.NOT_SUBMIT
					&& item.getHolidayHomeworkItem().getStatus() == HomeworkStatus.NOT_ISSUE) {
				item.setStatus(StudentHomeworkStatus.SUBMITED);
			}
		}
		data.put("items", items);
		HolidayStuHomeworkConvertOption option1 = new HolidayStuHomeworkConvertOption();
		option1.setInitUser(false);
		data.put("holidayStuHomework", holidayStuHomeworkConvert.to(holidayStuHomework, option1));
		return new Value(data);
	}

	/**
	 * 获取寒假作业专项题目
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param holidayStuHkItemId
	 *            学生寒假作业专项ID
	 * @return {@link Value}
	 */
	@MemberAllowed
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "view", method = { RequestMethod.POST, RequestMethod.GET })
	public Value view(long holidayStuHkItemId) {
		Map<String, Object> data = new HashMap<String, Object>(5);
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHkItemId);
		HolidayHomework holidayHomework = holidayHomeworkService.get(holidayStuHomeworkItem.getHolidayHomeworkId());
		List<Long> questionIds = holidayHomeworkItemQuestionService
				.queryQuestions(holidayStuHomeworkItem.getHolidayHomeworkItemId());
		List<Question> qs = questionService.mgetList(questionIds);
		MemberType memberType = SecurityContext.getMemberType();
		QuestionBaseConvertOption option = new QuestionBaseConvertOption(false, false, true, true, true);
		option.setAnalysis(memberType != null && memberType == MemberType.VIP);
		option.setInitMetaKnowpoint(false);
		option.setInitPhase(false);
		option.setInitTextbookCategory(false);
		List<VHolidayQuestion> vqs = questionBaseConvert.to(qs, option);
		List<VHolidayQuestion> vs = holidayQuestionConvert.to(vqs,
				new HolidayQuestionConvertOption(holidayStuHkItemId));
		int predictTime = 0;
		List<VKnowledgePoint> knowledgePoints = new ArrayList<VKnowledgePoint>();
		for (VHolidayQuestion v : vs) {
			if (holidayStuHomeworkItem.getStatus() == StudentHomeworkStatus.SUBMITED) {// 作业下发作业状态的时候设置答案颜色
				if (v.getType() == Type.FILL_BLANK) {
					int size = v.getHolidayStuHomeworkItemAnswers().size();
					for (int i = 0; i < size; i++) {
						boolean rightAnswer = v.getHolidayStuHomeworkItemAnswers().get(i)
								.getResult() == HomeworkAnswerResult.RIGHT;
						v.getHolidayStuHomeworkItemAnswers().get(i).setImageContent(QuestionUtils
								.process(v.getHolidayStuHomeworkItemAnswers().get(i).getContent(), rightAnswer, true));
					}
				}
				knowledgePoints.addAll(v.getNewKnowpoints());
			}
			predictTime += questionService.calPredictTime(v.getType(), v.getDifficulty(), v.getSubject().getCode());

		}
		if (CollectionUtils.isNotEmpty(knowledgePoints)) {
			// 新知识点
			List<VKnowledgePoint> weakKnowledgePoints = new ArrayList<VKnowledgePoint>();
			// 过滤重复的
			List<VKnowledgePoint> knowledgePoints2 = new ArrayList<VKnowledgePoint>();
			Set<Long> codes = new HashSet<Long>(knowledgePoints.size());
			for (VKnowledgePoint knowledgePoint : knowledgePoints) {
				if (!codes.contains(knowledgePoint.getCode())) {
					knowledgePoints2.add(knowledgePoint);
				}
				codes.add(knowledgePoint.getCode());
			}
			Map<Long, StudentExerciseKnowpoint> knowledgePointMap = null;
			if (codes.size() > 0) {
				knowledgePointMap = knowpointService.mgetNewByCodes(codes, Security.getUserId());
			} else {
				knowledgePointMap = Collections.EMPTY_MAP;
			}
			for (VKnowledgePoint knowledgePoint : knowledgePoints2) {
				StudentExerciseKnowpoint studentExerciseKnowpoint = knowledgePointMap.get(knowledgePoint.getCode());
				if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
					// 做过的题数量>20
					double rightRate;
					if (studentExerciseKnowpoint.getDoCount() > 20) {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / studentExerciseKnowpoint.getDoCount();
					} else {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount())
								* 100d) / 20;
					}
					if (rightRate <= 50) {
						weakKnowledgePoints.add(knowledgePoint);
					}
				}
			}
			data.put("weakKnowpoints", weakKnowledgePoints);
		}
		data.put("predictTime", predictTime);
		data.put("questions", vs);
		Date d = holidayHomework.getDeadline();
		long deadline = d.getTime() - new Date().getTime();
		data.put("deadline", deadline < 10000 ? 0 : deadline - 10000);
		VHolidayStuHomeworkItem item = holidayStuHomeworkItemConvert.to(holidayStuHomeworkItem);
		if (item.getStatus() == StudentHomeworkStatus.NOT_SUBMIT) {
			if (item.getHolidayHomeworkItem().getStatus() == HomeworkStatus.NOT_ISSUE || deadline == 0) {
				item.setStatus(StudentHomeworkStatus.SUBMITED);
			}
		}
		data.put("holidayStuHomeworkItem", item);
		return new Value(data);
	}

	/**
	 * 提交寒假作业专项
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param holidayHkItemId
	 *            寒假作业专项ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value commit(long holidayStuHkItemId) {
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHkItemId);
		if (holidayStuHomeworkItem.getStatus() == StudentHomeworkStatus.SUBMITED) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOLIDAY_HOMEWORK_SUBMITTED));
		}
		itemService.updateStudentHkStatus(holidayStuHkItemId, 100);
		itemService.correctHolidayStuHk(holidayStuHkItemId);
		holidayStuHomeworkService.uptStuHomeworkCompleteRate(holidayStuHomeworkItem.getHolidayStuHomeworkId());
		// 更新假期作业表总的完成率
		holidayHomeworkService.uptHolidayHomeworkCompleRate(itemService.get(holidayStuHkItemId).getHolidayHomeworkId());
		Map<String, Object> data = new HashMap<String, Object>(1);
		// GrowthLog growthlog =
		// growthService.grow(GrowthAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId(), true);
		// CoinsLog coinslog =
		// coinsService.earn(CoinsAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId());
		// if (growthlog.getHonor() != null) {
		// data.put("userReward",
		// new VUserReward(growthlog.getHonor().getUpRewardCoins(),
		// growthlog.getHonor().isUpgrade(),
		// growthlog.getHonor().getLevel(), growthlog.getGrowthValue(),
		// coinslog.getCoinsValue()));
		// }

		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101010002);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("holidayStuHomeworkItemId", holidayStuHkItemId);
		jsonObject.put("studentId", Security.getUserId());
		mqSender.send(MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_COMMIT,
				MqYoomathHolidayHomeworkRegistryConstants.QUEUE_YM_HOLIDAYHOMEWORK_COMMIT,
				MQ.builder().data(jsonObject).build());
		return new Value(data);
	}

	/**
	 * 做作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "do", method = { RequestMethod.POST, RequestMethod.GET })
	public Value $do(HolidayHomeworkQuestionForm form) {
		if (form == null || form.getType() != 2 || form.getHolidayHkItemId() <= 0 || form.getHolidayStuHkItemId() <= 0
				|| form.getQuestionId() <= 0 || form.getHolidayStuHkItemQuestionId() <= 0
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

		StuItemAnswerForm stuItemAnswerForm = new StuItemAnswerForm();
		stuItemAnswerForm.setQuestionId(form.getQuestionId());
		stuItemAnswerForm.setHomeworkTime(form.getTime());
		stuItemAnswerForm.setUpdateData(true);
		stuItemAnswerForm.setType(question.getType());
		stuItemAnswerForm.setHolidayStuHomeworkItemId(form.getHolidayStuHkItemId());
		stuItemAnswerForm.setCompletionRate(form.getCompletionRate());
		// 组装答案的json格式
		JSONArray answerArray = new JSONArray();
		JSONObject answerObject = new JSONObject();
		answerObject.put("stuQuestionId", form.getHolidayStuHkItemQuestionId());
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
		stuItemAnswerForm.setAnswer(answerArray.toString());
		Value value = stuHolidayHomeworkController.doOne(stuItemAnswerForm);
		if (value.getRet_code() != StatusCode.SUCCEED) {// 有异常转换异常码
			return new Value(new YoomathMobileException(
					YoomathMobileException.YOOMATH_MOBILE_CANNOTDO_SUBMITTED_HOLIDAY_HOMEWORK));
		}
		stuHolidayHomeworkController.updateComplete(form.getHolidayStuHkItemId(), form.getCompletionRate());
		return value;
	}

	/**
	 * 新版提交假期作业接口
	 *
	 * @param form
	 *            {@link HolidayHomeworkQuestionSaveForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "2/commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value commit2(HolidayHomeworkQuestionSaveForm form) {
		try {
			save(form);
		} catch (AbstractException e) {
			return new Value(e);
		}
		Long holidayStuHkItemId = form.getHolidayStuHkItemId();
		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(holidayStuHkItemId);
		if (holidayStuHomeworkItem.getStatus() == StudentHomeworkStatus.SUBMITED) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOLIDAY_HOMEWORK_SUBMITTED));
		}
		itemService.updateStudentHkStatus(holidayStuHkItemId, 100);
		itemService.correctHolidayStuHk(holidayStuHkItemId);
		holidayStuHomeworkService.uptStuHomeworkCompleteRate(holidayStuHomeworkItem.getHolidayStuHomeworkId());
		// 更新假期作业表总的完成率
		holidayHomeworkService.uptHolidayHomeworkCompleRate(itemService.get(holidayStuHkItemId).getHolidayHomeworkId());
		Map<String, Object> data = new HashMap<String, Object>(1);
		// GrowthLog growthlog =
		// growthService.grow(GrowthAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId(), true);
		// CoinsLog coinslog =
		// coinsService.earn(CoinsAction.FINISH_HOLIDAY_HOMEWORK,
		// Security.getUserId());
		// if (growthlog.getHonor() != null) {
		// data.put("userReward",
		// new VUserReward(growthlog.getHonor().getUpRewardCoins(),
		// growthlog.getHonor().isUpgrade(),
		// growthlog.getHonor().getLevel(), growthlog.getGrowthValue(),
		// coinslog.getCoinsValue()));
		// }

		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101010002);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("holidayStuHomeworkItemId", holidayStuHkItemId);
		jsonObject.put("studentId", Security.getUserId());

		mqSender.send(MqYoomathHolidayHomeworkRegistryConstants.EX_YM_HOLIDAYHOMEWORK,
				MqYoomathHolidayHomeworkRegistryConstants.RK_YM_HOLIDAYHOMEWORK_COMMIT,
				MQ.builder().data(jsonObject).build());
		return new Value(data);
	}

	/**
	 * 学生假期作业做作业保存接口
	 *
	 * @param form
	 *            {@link HolidayHomeworkQuestionSaveForm}
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "saveDo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveDo(HolidayHomeworkQuestionSaveForm form) {
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
	private long save(HolidayHomeworkQuestionSaveForm form) {
		if (form == null
				|| form.getHolidayHkItemId() <= 0 && form.getHolidayStuHkItemId() <= 0 && form.getTime() <= 0) {
			throw new IllegalArgException();
		}

		HolidayStuHomeworkItem holidayStuHomeworkItem = itemService.get(form.getHolidayStuHkItemId());
		if (holidayStuHomeworkItem.getStudentId() != Security.getUserId()) {
			throw new IllegalArgException();
		}

		if (holidayStuHomeworkItem.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
			throw new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CANNOTDO_SUBMITTED_HOLIDAY_HOMEWORK);
		}

		List<HolidayHomeworkQuestionForm> forms = form.getForms();
		List<Long> questionIds = new ArrayList<Long>(forms.size());
		List<Long> itemQuestionIds = new ArrayList<Long>(forms.size());
		// Map<Long, Long> sovlingImgs = new HashMap<Long,
		// Long>(questionIds.size());

		Map<Long, List<Long>> sovlingImgs = new HashMap<Long, List<Long>>(questionIds.size());
		for (HolidayHomeworkQuestionForm f : forms) {
			questionIds.add(f.getQuestionId());
			itemQuestionIds.add(f.getHolidayStuHkItemQuestionId());
			// 目前客户端暂时不存在上传答题过程
			if (f.getImage() != null) {
				List<Long> imgs = new ArrayList<Long>(1);
				imgs.add(f.getImage());
				sovlingImgs.put(f.getHolidayStuHkItemQuestionId(), imgs);
			} else {
				sovlingImgs.put(f.getHolidayStuHkItemQuestionId(), f.getImages());
			}
		}

		Map<Long, Question> questionMap = questionService.mget(questionIds);
		// latex 下的 HolidayStudentHomeworkItemQuestion.id -> 答案列表
		Map<Long, List<String>> latexAnswerMap = new HashMap<Long, List<String>>(questionMap.size());
		// asciimath 下的 HolidayStudentHomeworkItemQuestion.id -> 答案列表
		Map<Long, List<String>> asciiAnswerMap = new HashMap<Long, List<String>>(questionMap.size());
		// 下的 HolidayStudentHomeworkItemQuestion.id -> 题目类型
		Map<Long, Type> questionTypeMap = new HashMap<Long, Type>(questionMap.size());

		for (HolidayHomeworkQuestionForm f : forms) {

			Long hoStuHomeworkQuestionId = f.getHolidayStuHkItemQuestionId();

			Question question = questionMap.get(f.getQuestionId());
			questionTypeMap.put(hoStuHomeworkQuestionId, question.getType());

			String asciiAnswers = f.getAsciimathAnswers();
			if (StringUtils.isBlank(asciiAnswers)) {
				asciiAnswerMap.put(hoStuHomeworkQuestionId, Collections.EMPTY_LIST);
			} else {
				List<String> asciiAnswerList = JSONArray.parseArray(f.getAsciimathAnswers(), String.class);

				if (question.getType() == Type.FILL_BLANK) {
					List<String> tmpList = new ArrayList<String>(asciiAnswerList.size());

					for (String a : asciiAnswerList) {
						a = "<ux-mth>" + a + "</ux-mth>";
						tmpList.add(a);
					}

					asciiAnswerMap.put(hoStuHomeworkQuestionId, tmpList);

					if (asciiAnswerMap.get(hoStuHomeworkQuestionId).size() != question.getAnswerNumber()) {
						throw new IllegalArgException();
					}

					String mmlMathStr = f.getMathmlAnswers();
					if (StringUtils.isBlank(mmlMathStr)) {
						latexAnswerMap.put(hoStuHomeworkQuestionId, Collections.EMPTY_LIST);
					} else {
						JSONArray mmlArray = JSONArray.parseArray(mmlMathStr);
						List<String> mmls = new ArrayList<String>(mmlArray.size());
						for (int i = 0; i < mmlArray.size(); i++) {
							mmls.add(mmlArray.getString(i));
						}

						List<String> latexs = latexService.multiMml2Latex(mmls);
						latexAnswerMap.put(hoStuHomeworkQuestionId, latexs);
					}
				} else {
					asciiAnswerMap.put(hoStuHomeworkQuestionId, asciiAnswerList);
					latexAnswerMap.put(hoStuHomeworkQuestionId, asciiAnswerList);
				}

			}
		}

		HolidayStuHomeworkItem item = holidayStuHomeworkItemService.get(form.getHolidayStuHkItemId());
		holidayStuHomeworkItemService.updateHomeworkTime(form.getHolidayStuHkItemId(), form.getTime());

		// 批量保存学生答案
		holidayStuHomeworkItemAnswerService.saveAnswer(itemQuestionIds, latexAnswerMap, asciiAnswerMap, sovlingImgs,
				Security.getUserId(), questionTypeMap, item.getHolidayHomeworkId(), item.getHolidayHomeworkItemId(),
				item.getHolidayStuHomeworkId(), item.getId());

		long time = itemService.updateStuItemCompleteRate(form.getHolidayStuHkItemId(), form.getCompletionRate());
		holidayStuHomeworkService.uptStuHomeworkCompleteRate(item.getHolidayStuHomeworkId());

		return time;
	}
}
