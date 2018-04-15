package com.lanking.uxb.service.youngyedu.homework.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.homework.form.HomeworkQuestionForm;
import com.lanking.uxb.service.homework.resource.ZyMStuHomework2Controller;
import com.lanking.uxb.service.homework.resource.ZyMStuHomeworkController;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.youngyedu.homework.form.YoungHomeworkQuestionForm;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 融捷作业相关接口
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
@RestController
@RequestMapping(value = "router/youngyedu/ym/s/homework")
public class ZyMYoungyeduStuHomeworkController {

	@Autowired
	private ZyStudentHomeworkService zyStudentHomeworkService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private StudentHomeworkConvert studentHomeworkConvert;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private StudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private ZyStudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	private ZyMStuHomeworkController stuHomeworkController;
	@Autowired
	private ZyMStuHomework2Controller stuHomework2Controller;
	@Autowired
	private CredentialService credentialService;

	/**
	 * 查询学习作业作业中状态列表
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "todo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value todo(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "8") int size) {
		Pageable pageable = P.index(page, size);

		// 待批改作业
		ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
		todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
		todoQuery.setCourse(false);
		todoQuery.setStudentId(Security.getUserId());
		todoQuery.setCursorType("startTime");
		Page<Map> todoPage = zyStudentHomeworkService.queryUnionHolidayStuHk(todoQuery, pageable);
		VPage<VStudentHomework> vpage = new VPage<VStudentHomework>();
		if (todoPage.isEmpty()) {
			return new Value(vpage);
		} else {
			int todoSize = todoPage.getItemSize();
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
			List<Long> ids = new ArrayList<Long>(todoSize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = todoPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(studentHomeworkConvert.to(map, false, true, false, false));
			}
			if (stuHolidayHkIds.size() > 0) {
				Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
				vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}

			vpage.setItems(items);
		}

		vpage.setCurrentPage(page);
		vpage.setPageSize(size);
		vpage.setTotal(todoPage.getTotalCount());
		vpage.setTotalPage(todoPage.getPageCount());

		return new Value(vpage);
	}

	/**
	 * 查询作业记录列表
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "history", method = { RequestMethod.GET, RequestMethod.POST })
	public Value history(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "8") int size) {
		VPage<VStudentHomework> vpage = new VPage<VStudentHomework>();
		Pageable pageable = P.index(page, size);
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		historyQuery.setCursorType("startTime");
		Page<Map> historyPage = zyStudentHomeworkService.queryUnionHolidayStuHk(historyQuery, pageable);
		if (historyPage.isEmpty()) {
			return new Value(vpage);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(size);
			List<Long> ids = new ArrayList<Long>(size);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(size);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(studentHomeworkConvert.to(map, false, true, false, false));
			}
			if (stuHolidayHkIds.size() > 0) {
				Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
				vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vpage.setItems(items);
		}

		vpage.setTotal(historyPage.getTotalCount());
		vpage.setTotalPage(historyPage.getPageCount());
		vpage.setCurrentPage(page);
		vpage.setPageSize(size);

		return new Value(vpage);
	}

	/**
	 * 提交作业
	 *
	 * @param hkId
	 *            学生作业id
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value commit(long hkId) {
		return stuHomeworkController.commit(hkId);
	}

	/**
	 * 查看作业
	 *
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "view", method = { RequestMethod.GET, RequestMethod.POST })
	public Value view(long stuHkId) {
		return stuHomework2Controller.view(stuHkId);
	}

	/**
	 * 墨水屏保存学生答案
	 *
	 * @param form
	 *            {@link HomeworkQuestionForm}
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "do", method = { RequestMethod.GET, RequestMethod.POST })
	public Value $do(YoungHomeworkQuestionForm form) {
		if (form == null || form.getType() != 1 || form.getHomeworkId() <= 0
		        || form.getStuHkId() <= 0 || form.getQuestionId() <= 0
		        || form.getStuHkQuestionId() <= 0
		        || (StringUtils.isBlank(form.getAsciimathAnswers())
		                && StringUtils.isBlank(form.getHandWriting()))
		        || form.getTime() <= 0) {
			return new Value(new IllegalArgException());
		}

		if (StringUtils.isBlank(form.getAsciimathAnswers())) {
			form.setAsciimathAnswerList(Collections.EMPTY_LIST);
		} else {
			form.setAsciimathAnswerList(JSONArray.parseArray(form.getAsciimathAnswers(), String.class));
		}

		StudentHomework studentHomework = studentHomeworkService.get(form.getStuHkId());
		if (studentHomework == null || !studentHomework.getStudentId().equals(Security.getUserId())) {
			return new Value(new IllegalArgException());
		}
		if (studentHomework.getStatus() != StudentHomeworkStatus.NOT_SUBMIT) {
			if (studentHomework.getStatus() == StudentHomeworkStatus.SUBMITED) {
				if (studentHomework.getStuSubmitAt() == null) {
					// 作业被自动提交了
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_AUTO_SUBMITTED));
				} else {
					// 此作业学生已经主动提交过,不能再做此作业的题目了
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_SUBMITTED));
				}
			} else if (studentHomework.getStatus() == StudentHomeworkStatus.ISSUED) {
				if (!(studentHomework.getSubmitAt() == null && studentHomework.getStuSubmitAt() == null)) {
					// 作业已经被下发不能答题
					return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_ISSUED));
				}
			}
		}
		Question question = questionService.get(form.getQuestionId());

		stuHkService.updateHomeworkTime(form.getStuHkId(), Security.getUserId(), form.getTime(),
				form.getCompletionRate());

		Map<Long, List<String>> answerData = new HashMap<Long, List<String>>(1);
		Map<Long, List<String>> answerAsciiData = new HashMap<Long, List<String>>(1);
		Map<Long, List<Long>> solvingImgs = new HashMap<Long, List<Long>>(1);
		Map<Long, Question.Type> questionTypes = new HashMap<Long, Question.Type>(1);
		Map<Long, List<String>> handWritings = new HashMap<Long, List<String>>(1);

		Long shqId = form.getStuHkQuestionId();
		questionTypes.put(shqId, question.getType());
		// 墨水屏非客观题可以上传解题过程
		if (question.getType() == Question.Type.SINGLE_CHOICE || question.getType() == Question.Type.MULTIPLE_CHOICE
				|| question.getType() == Question.Type.TRUE_OR_FALSE) {
			List<String> answerContents = JSONArray.parseArray(form.getAsciimathAnswers(), String.class);

			answerData.put(shqId, answerContents);
			answerAsciiData.put(shqId, answerContents);
			handWritings.put(shqId, JSONArray.parseArray(form.getHandWriting(), String.class));
		} else {
			handWritings.put(shqId, JSONArray.parseArray(form.getHandWriting(), String.class));
			answerData.put(shqId, Collections.EMPTY_LIST);
			answerAsciiData.put(shqId, Collections.EMPTY_LIST);
		}

		if (CollectionUtils.isNotEmpty(form.getImages())) {
			solvingImgs.put(shqId, form.getImages());
		}

		// 保存答案数据
		stuHkAnswerService.doQuestion(answerData, answerAsciiData, solvingImgs, questionTypes, Security.getUserId(),
				handWritings, null);

		question.setCorrectQuestion(stuHkQuestionService.get(form.getStuHkQuestionId()).isCorrect());
		return new Value(questionConvert.to(question,
				new QuestionConvertOption(false, false, true, true, form.getStuHkId())));
	}

	/**
	 * 获得待完成作业数量
	 *
	 * @param userId
	 *            用户id
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getTodoCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getTodoCount(String userId) {
		if (StringUtils.isBlank(userId)) {
			return new Value(0);
		}
		Credential credential = credentialService.getCredentialByPersonId(Product.YOOMATH,
				CredentialType.YOUNGY_EDU, userId);
		if (credential == null) {
			return new Value(0);
		}

		long uId = credential.getUserId();
		Pageable pageable = P.index(1, 1);
		ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
		todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
		todoQuery.setCourse(false);
		todoQuery.setStudentId(uId);
		todoQuery.setCursorType("startTime");
		Page<Map> todoPage = zyStudentHomeworkService.queryUnionHolidayStuHk(todoQuery, pageable);

		return new Value(todoPage.getTotalCount());
	}

}
