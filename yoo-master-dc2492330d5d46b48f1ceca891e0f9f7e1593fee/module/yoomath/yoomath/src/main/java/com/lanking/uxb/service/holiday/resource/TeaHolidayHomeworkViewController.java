package com.lanking.uxb.service.holiday.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.common.convert.QuestionBaseConvert;
import com.lanking.uxb.service.common.convert.QuestionBaseConvertOption;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkItemConvert;
import com.lanking.uxb.service.holiday.convert.HolidayHomeworkItemQuestionConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvert;
import com.lanking.uxb.service.holiday.convert.HolidayQuestionConvertOption;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemConvert;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkItemQuestionConvert;
import com.lanking.uxb.service.holiday.value.VHolidayHomeworkItem;
import com.lanking.uxb.service.holiday.value.VHolidayHomeworkStuStatPage;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomework;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItem;
import com.lanking.uxb.service.holiday.value.VHolidayStuHomeworkItemQuestion;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VStuWrong;

/**
 * 老师假日作业查看相关rest API
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/holiday/view")
public class TeaHolidayHomeworkViewController {
	@Autowired
	private HolidayStuHomeworkItemQuestionConvert stuItemQuestionConvert;
	@Autowired
	private HolidayHomeworkItemService hdHomeworkItemService;
	@Autowired
	private HolidayHomeworkItemConvert hdHomeworkItemConvert;
	@Autowired
	private HolidayHomeworkService hdHomeworkService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private HolidayStuHomeworkService hdStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert hdStuHomeworkConvert;
	@Autowired
	private HolidayStuHomeworkItemService hdStuHomeworkItemService;
	@Autowired
	private HolidayStuHomeworkItemConvert hdStuHomeworkItemConvert;
	@Autowired
	private HolidayHomeworkItemQuestionService hdHomeworkItemQuestionService;
	@Autowired
	private HolidayHomeworkItemQuestionConvert hdHkItemQuestionConvert;
	@Autowired
	private ZyHomeworkClassService clazzService;
	@Autowired
	private ZyHomeworkClazzConvert clazzConvert;
	@Autowired
	private HolidayHomeworkConvert hdHomeworkConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionBaseConvert<VHolidayQuestion> questionBaseConvert;
	@Autowired
	private HolidayQuestionConvert holidayQuestionConvert;
	@Autowired
	private HolidayStuHomeworkItemQuestionService shiQservice;
	@Autowired
	private ZyQuestionCarService zyQcarService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private AccountService accountService;

	/**
	 * 教师查看作业统计 专项统计
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "itemsStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value itemsStat(long holidayHomeworkId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<HolidayHomeworkItem> hdItems = hdHomeworkItemService.listHdItemById(holidayHomeworkId);
		if (hdItems.isEmpty()) {
			return new Value(new IllegalArgException());
		}
		data.put("itemList", hdHomeworkItemConvert.to(hdItems));
		data.put("homework", hdHomeworkConvert.to(hdHomeworkService.get(holidayHomeworkId)));
		return new Value(data);
	}

	/**
	 * 教师查看作业统计 学生统计
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "studentStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentStat(@RequestParam(value = "holidayHomeworkId") long holidayHomeworkId,
			@RequestParam(value = "pageSize", defaultValue = "60") int pageSize,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		VHolidayHomeworkStuStatPage vPage = new VHolidayHomeworkStuStatPage();
		HolidayHomework holidayHomework = hdHomeworkService.get(holidayHomeworkId);
		vPage.setClazz(clazzConvert.to(clazzService.get(holidayHomework.getHomeworkClassId())));
		vPage.setHolidayHomework(hdHomeworkConvert.to(holidayHomework));
		// 获取班级学生列表
		Page<HomeworkStudentClazz> stuPage = homeworkStuClazzService.query(holidayHomework.getHomeworkClassId(),
				P.index(page, pageSize));
		List<Long> stuIds = Lists.newArrayList();
		for (HomeworkStudentClazz stuClazz : stuPage.getItems()) {
			stuIds.add(stuClazz.getStudentId());
		}

		List<VHolidayStuHomework> hdStuHkList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(stuIds)) {
			hdStuHkList = hdStuHomeworkConvert.to(hdStuHomeworkService.queryStuHomework(holidayHomeworkId, stuIds));
		}
		vPage.setPageSize(pageSize);
		vPage.setCurrentPage(page);
		vPage.setTotalPage(stuPage.getPageCount());
		vPage.setTotal(stuPage.getTotalCount());
		vPage.setItems(hdStuHkList);
		return new Value(vPage);
	}

	/**
	 * 教师查看作业统计 学生统计
	 * 
	 * @param holidayHomeworkId
	 *            假期作业ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "listStuItems", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listStuItems(@RequestParam(value = "holidayStuHomeworkId") long holidayStuHomeworkId) {
		List<HolidayStuHomeworkItem> hdStuItems = hdStuHomeworkItemService.queryStuHkItems(null, null,
				holidayStuHomeworkId, null);
		return new Value(hdStuHomeworkItemConvert.to(hdStuItems));
	}

	/**
	 * 专项作业分发给的学生作业列表(用于老师查看专项作业详情里面的左侧列表)
	 * 
	 * @since 1.9
	 * @param hkId
	 *            专项作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "stuhks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworks(long hkItemId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		HolidayHomeworkItem homeworkItem = hdHomeworkItemService.get(hkItemId);
		if (homeworkItem.getCreateId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}
		VHolidayHomeworkItem vHomework = hdHomeworkItemConvert.to(homeworkItem);
		data.put("homework", vHomework);
		if (vHomework.getStatus() == HomeworkStatus.INIT) {
			// 待分发,查询出当前班级的学生
			List<Long> studentList = hkStuClazzService.listClassStudents(homeworkItem.getHomeworkClassId());
			List<VUser> userList = userConvert.mgetList(studentList);
			List<VStudentHomework> vshs = new ArrayList<VStudentHomework>();
			for (VUser user : userList) {
				VStudentHomework v = new VStudentHomework();
				v.setUser(user);
				vshs.add(v);
			}
			data.put("studentHomeworks", vshs);
		} else {
			List<HolidayStuHomeworkItem> shs = hdStuHomeworkItemService.listByHomeworkItem(hkItemId);
			List<VHolidayStuHomeworkItem> vshs = hdStuHomeworkItemConvert.to(shs);
			// if (homeworkItem.getHomeworkClassId() != null ||
			// homeworkItem.getHomeworkClassId() > 0) {
			// List<VHomeworkStudentClazz> vhkStuClazzs =
			// hkStuClazzConvert.to(hkStuClazzService.list(homeworkItem
			// .getHomeworkClassId()));
			// Map<Long, VHomeworkStudentClazz> vhkStuClazzMap = new
			// HashMap<Long, VHomeworkStudentClazz>(
			// vhkStuClazzs.size());
			// for (VHolidayStuHkView v : vshs) {
			// v.setStudentClazz(vhkStuClazzMap.get(v.getStudentId()));
			// }
			// }
			data.put("studentHomeworks", vshs);
		}
		return new Value(data);
	}

	/**
	 * 作业总体的结果列表
	 * 
	 * @since 1.9
	 * @param hkItemId
	 *            作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "hk_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value homeworkQuestions(long hkItemId) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		HolidayHomeworkItem homeworkItem = hdHomeworkItemService.get(hkItemId);
		map.put("homework", hdHomeworkItemConvert.to(homeworkItem));
		List<HolidayHomeworkItemQuestion> hqs = hdHomeworkItemQuestionService.getHomeworkQuestion(homeworkItem.getId());
		List<Long> qIds = new ArrayList<Long>(hqs.size());
		for (HolidayHomeworkItemQuestion item : hqs) {
			qIds.add(item.getQuestionId());
		}
		List<Question> qs = questionService.mgetList(qIds);
		List<VHolidayQuestion> vqs = questionBaseConvert
				.to(qs, new QuestionBaseConvertOption(false, true, true, false));
		map.put("homeworkQuestions", holidayQuestionConvert.to(vqs, new HolidayQuestionConvertOption(null, hkItemId)));
		return new Value(map);
	}

	/**
	 * 学生作业题目列表(查看用)<br>
	 * 
	 * @since 1.9
	 * @param stuHomeworkItemId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "stuhk_questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworkquestions(long stuHomeworkItemId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		HolidayStuHomeworkItem hdStuItemHomework = hdStuHomeworkItemService.get(stuHomeworkItemId);
		data.put("studentHomework", hdStuHomeworkItemConvert.to(hdStuItemHomework));
		List<Long> qIds = hdHomeworkItemQuestionService.queryQuestions(hdStuItemHomework.getHolidayHomeworkItemId());
		List<Question> qs = questionService.mgetList(qIds);
		List<VHolidayQuestion> vqs = questionBaseConvert
				.to(qs, new QuestionBaseConvertOption(false, true, true, false));
		data.put("questions", holidayQuestionConvert.to(vqs, new HolidayQuestionConvertOption(stuHomeworkItemId)));
		return new Value(data);
	}

	/**
	 * 查看一次作业的某题目做错的学生列表
	 * 
	 * @since yoomath V1.4.1
	 * @param homeworkId
	 *            作业ID
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "listWrongStu", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listWrongStu(long homeworkItemId, long questionId) {
		Map<String, Object> data = new HashMap<String, Object>(1);
		List<Map> resultMapLists = hdHomeworkItemQuestionService.listWrongStu(homeworkItemId, questionId);
		List<VStuWrong> vsList = new ArrayList<VStuWrong>();
		List<Long> stuIds = new ArrayList<Long>();
		List<Long> stuHKQIds = new ArrayList<Long>();
		for (Map map : resultMapLists) {
			Long studentId = Long.parseLong(map.get("student_id").toString());
			Long studentHkQId = Long.parseLong(map.get("holiday_stu_homework_item_qid").toString());
			if (!stuIds.contains(studentId)) {
				stuIds.add(studentId);
			}
			if (!stuHKQIds.contains(studentId)) {
				stuHKQIds.add(studentHkQId);
			}
		}
		// key 为 stundetHKQID
		Map<Long, HolidayStuHomeworkItemQuestion> shqMap = shiQservice.mget(stuHKQIds);
		// key 为userID
		Map<Long, HolidayStuHomeworkItemQuestion> shqMap2 = Maps.newHashMap();
		for (Map map : resultMapLists) {
			shqMap2.put(Long.parseLong(map.get("student_id").toString()),
					shqMap.get(Long.parseLong(map.get("holiday_stu_homework_item_qid").toString())));
		}
		Map<Long, VUser> userMap = userConvert.mget(stuIds);
		for (Long key : userMap.keySet()) {
			VStuWrong vs = new VStuWrong();
			List<VStudentHomeworkAnswer> vsTemp = new ArrayList<VStudentHomeworkAnswer>();
			String name = userMap.get(key).getName();
			for (Map userQesMap : resultMapLists) {
				Long studentId = Long.parseLong(userQesMap.get("student_id").toString());
				VStudentHomeworkAnswer vsa = new VStudentHomeworkAnswer();
				if (studentId.equals(key)) {
					if (userQesMap.get("content_ascii") != null) {
						vsa.setContentAscii((userQesMap.get("content_ascii").toString()));
					} else {
						vsa.setContentAscii("");
					}
					if (Integer.valueOf(userQesMap.get("result").toString()) == 1) {
						vsa.setResult(HomeworkAnswerResult.RIGHT);
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 2) {
						vsa.setResult(HomeworkAnswerResult.WRONG);
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 3) {
						vsa.setResult(HomeworkAnswerResult.UNKNOW);
					} else if (Integer.valueOf(userQesMap.get("result").toString()) == 0) {
						vsa.setResult(HomeworkAnswerResult.INIT);
					}
				}
				if (vsa.getResult() != null) {
					vsTemp.add(vsa);
				}
			}

			HolidayStuHomeworkItemQuestion shk = shqMap2.get(key);
			VHolidayStuHomeworkItemQuestion temp = stuItemQuestionConvert.to(shk);
			vs.setAnswerImgs(temp.getAnswerImgs());
			vs.setNotationAnswerImgs(temp.getNotationAnswerImgs());

			vs.setName(name);
			vs.setVsTemp(vsTemp);
			vsList.add(vs);
		}
		data.put("data", vsList);
		return new Value(data);
	}

	/**
	 * 添加指定题目IDs到作业篮子
	 * 
	 * @param 题目ID
	 *            集合
	 * @return
	 */
	@RequestMapping(value = "addQuestions2Car", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value addQuestions2Car(long homeworkItemId) {
		List<Long> qIds = hdHomeworkItemQuestionService.queryQuestions(homeworkItemId);
		if (qIds.isEmpty()) {
			return new Value(new IllegalArgException());
		}
		List<Question> questionList = questionService.mgetList(qIds);
		List<Long> qList = zyQcarService.addQuestions2Car(Security.getUserId(), questionList);
		return new Value(questionConvert.mget(qList));
	}

	/**
	 * 删除假期作业
	 * 
	 * @since yoomath V1.4
	 * @param id
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(String password, long id) {
		// check password
		Account account = accountService.getAccountByUserId(Security.getUserId());
		if (!account.getPassword().equals(Codecs.md5Hex(password))) {
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}
		int upt = hdHomeworkService.delete(Security.getUserId(), id);
		if (upt == 0) {
			return new Value(new NoPermissionException());
		}
		return new Value();
	}
}
