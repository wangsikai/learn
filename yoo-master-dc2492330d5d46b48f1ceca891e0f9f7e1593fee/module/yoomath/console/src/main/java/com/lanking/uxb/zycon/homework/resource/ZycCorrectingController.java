package com.lanking.uxb.zycon.homework.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.support.console.common.HomeworkCorrectLogType;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.intercomm.yoocorrect.client.CorrectQuestionDatawayClient;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.correct.api.CorrectProcessor;
import com.lanking.uxb.service.correct.vo.CorrectorType;
import com.lanking.uxb.service.correct.vo.QuestionCorrectObject;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.holiday.api.ZycHolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycAutoCorrectingService;
import com.lanking.uxb.zycon.homework.api.ZycCorrectingService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkCorrectLogService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkService;
import com.lanking.uxb.zycon.homework.convert.ZycHomeworkConvert;
import com.lanking.uxb.zycon.homework.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.homework.form.CorrectQuestionForm;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectForm;
import com.lanking.uxb.zycon.homework.form.HomeworkCorrectLogForm;
import com.lanking.uxb.zycon.homework.form.HomeworkQueryForm;
import com.lanking.uxb.zycon.homework.value.VZycHomework;
import com.lanking.uxb.zycon.homework.value.VZycQuestion;

/**
 * 后台批改作业接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@RestController
@RequestMapping(value = "zyc/correcting/")
public class ZycCorrectingController {
	@Autowired
	private ZycCorrectingService zycCorrectingService;
	@Autowired
	private ZycHomeworkConvert zycHomeworkConvert;
	@Autowired
	private ZycStudentHomeworkQuestionService zycStudentHomeworkQuestionService;
	@Autowired
	@Qualifier(value = "hzycQuestionConvert")
	private ZycQuestionConvert questionConvert;
	@Autowired
	private com.lanking.uxb.zycon.base.convert.ZycQuestionConvert questionConvert2;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycStudentHomeworkService zycStudentHomeworkService;
	@Autowired
	private ZycAutoCorrectingService zycAutoCorrectingService;
	@Autowired
	private ZycHomeworkCorrectLogService correctLogService;
	@Autowired
	private ZycHolidayStuHomeworkItemQuestionService holidayStuHomeworkItemQuestionService;
	@Autowired
	private CorrectProcessor correctProcessor;
	@Autowired
	private CorrectQuestionDatawayClient correctDatawayClient;
	

	/**
	 * 同步是否有批改消息
	 *
	 * 增加假期作业未提交的题目数量 hkNotCorrectCount -> 正常作业未批改题目数量 holidayHkNotCorrectCount
	 * -> 假期作业未批改题目数量
	 *
	 * @return Value
	 */
	@RequestMapping(value = "sync_message")
	public Value syncMessage() {
		Value value = correctDatawayClient.correctQuestionsCount();
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		
		retMap.put("notCorrectCount", value.getRet());
		
		return new Value(retMap);
	}

	/**
	 * 查询现在作业列表(非下发)
	 *
	 * @return Value
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(HomeworkQueryForm form) {
		// int offset = (page - 1) * size;

		// Pageable pageable = P.offset(offset, size);
		Page<Homework> pageValue = zycCorrectingService.page(form);
		VPage<VZycHomework> vPage = new VPage<VZycHomework>();
		List<VZycHomework> vs = zycHomeworkConvert.to(pageValue.getItems());
		vPage.setItems(vs);
		vPage.setCurrentPage(form.getPage());
		vPage.setTotalPage(pageValue.getPageCount());
		vPage.setPageSize(form.getSize());
		vPage.setTotal(pageValue.getTotalCount());

		return new Value(vPage);
	}

	/**
	 * 得到作业下已提交学生的题目
	 *
	 * @param size
	 *            一次性加载的题目
	 * @return Value
	 */
	@RequestMapping(value = "query_stu_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryStuQuestions(@RequestParam(value = "size", defaultValue = "5") int size) {
		List<Question> questions = questionService.zycFindStuHkQuestions(size);
		List<VZycQuestion> vs = questionConvert.to(questions);
		return new Value(vs);
	}

	/**
	 * 新批改接口
	 * 
	 * @param formStr
	 *            {@link HomeworkCorrectForm}
	 *
	 * @since V1.9.2
	 * @return {@link Value}
	 */
	@RequestMapping(value = "3/correct", method = { RequestMethod.GET, RequestMethod.POST })
	public Value correct3(@RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		HomeworkCorrectForm form = JSONObject.parseObject(formStr, HomeworkCorrectForm.class);
		List<HomeworkCorrectLogForm> forms = new ArrayList<HomeworkCorrectLogForm>(form.getSqIds().size());

		// 作业题归类
		Map<Long, StudentHomeworkQuestion> studentHomeworkQuestionMap = zycStudentHomeworkQuestionService
				.mget(form.getSqIds());
		Map<Long, List<QuestionCorrectObject>> questionCorrectObjectMap = new HashMap<Long, List<QuestionCorrectObject>>();
		for (int i = 0; i < form.getSqIds().size(); i++) {
			if (form.getCountDownTime().get(i) != null && form.getCountDownTime().get(i) <= 0) {
				continue;
			}
			long studentHomeworkId = form.getStuHkIds().get(i);
			long studentHomeworkQuestionId = form.getSqIds().get(i);
			StudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionMap.get(studentHomeworkQuestionId);
			List<QuestionCorrectObject> questionCorrectObjects = questionCorrectObjectMap.get(studentHomeworkId);
			if (questionCorrectObjects == null) {
				questionCorrectObjects = Lists.newArrayList();
				questionCorrectObjectMap.put(studentHomeworkId, questionCorrectObjects);
			}
			QuestionCorrectObject questionCorrectObject = new QuestionCorrectObject();
			questionCorrectObject.setStudentHomeworkId(studentHomeworkId);
			questionCorrectObject.setStuHomeworkQuestionId(form.getSqIds().get(i));
			questionCorrectObject.setQuestionType(studentHomeworkQuestion.getType());
			questionCorrectObject.setQuestionResult(form.getResults().get(i));
			if (form.getRightRates().get(i) != null) {
				questionCorrectObject.setQuestionRightRate(form.getRightRates().get(i).intValue());
			}
			if (studentHomeworkQuestion.getType() == Type.FILL_BLANK) {
				questionCorrectObject.setAnswerResultMap(form.getAnswerResults().get(i));
			}
			questionCorrectObjects.add(questionCorrectObject);
		}

		// 根据作业分类调用新的批改流程
		for (Entry<Long, List<QuestionCorrectObject>> entry : questionCorrectObjectMap.entrySet()) {
			correctProcessor.correctStudentHomeworkQuestions(Security.getUserId(), CorrectorType.PG_USER,
					entry.getKey(), entry.getValue());
		}

		for (int i = 0; i < form.getSqIds().size(); i++) {
			if (form.getCountDownTime().get(i) != null && form.getCountDownTime().get(i) <= 0) {
				continue;
			}
			zycAutoCorrectingService.asyncAutoCheck(form.getStuHkIds().get(i), form.getSqIds().get(i),
					form.getResults().get(i));

			HomeworkCorrectLogForm logForm = new HomeworkCorrectLogForm(form.getSqIds().get(i),
					form.getResults().get(i));
			forms.add(logForm);
		}

		correctLogService.save(Security.getUserId(), forms, HomeworkCorrectLogType.HOMEWORK);

		return new Value();
	}

	/**
	 * 移除推送的questions
	 *
	 * @param questionIds
	 *            question的ids
	 * @return Value
	 */
	@RequestMapping(value = "remove_push_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removePushQuestions(@RequestParam(value = "questionIds") String questionIds,
			@RequestParam(value = "homeworkId") Long homeworkId) {
		if (StringUtils.isBlank(questionIds)) {
			return new Value(new IllegalArgException());
		}

		List<Long> ids = Lists.newArrayList();
		for (String id : questionIds.split(",")) {
			ids.add(Long.valueOf(id));
		}
		zycStudentHomeworkQuestionService.removePushedId(homeworkId, ids);

		return new Value();
	}

	/**
	 * 接口变更 >>> 现前端推送无序的题目给别人批改
	 *
	 * @param formStr
	 *            {@link CorrectQuestionForm} JSON序列化后的form str
	 * @return {@link Value}
	 */
	@RequestMapping(value = "remove_push_questions2", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removePushQuestions2(@RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		List<CorrectQuestionForm> forms = JSONObject.parseArray(formStr, CorrectQuestionForm.class);
		Map<Long, List<Long>> map = Maps.newHashMap();
		for (CorrectQuestionForm f : forms) {
			List<Long> list = map.get(f.getHomeworkId());
			if (null == list) {
				list = Lists.newArrayList();
				list.add(f.getStudentHomeworkQuestionId());
				map.put(f.getHomeworkId(), list);
			} else {
				list.add(f.getStudentHomeworkQuestionId());
				map.put(f.getHomeworkId(), list);
			}
		}

		for (Map.Entry<Long, List<Long>> entry : map.entrySet()) {
			zycStudentHomeworkQuestionService.removePushedId(entry.getKey(), entry.getValue());
		}

		return new Value();
	}

	/**
	 * 得到作业下布置的所有题目
	 *
	 * @param homeworkId
	 *            作业id
	 * @return 数据
	 */
	@RequestMapping(value = "query_hk_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryHKQuestions(@RequestParam(value = "homeworkId") Long homeworkId) {
		List<Question> questions = questionService.zycFindHKQuestions(homeworkId);
		return new Value(questionConvert2.to(questions, false, true, false, null));
	}

	/**
	 * 导出操作
	 *
	 * form 中导出传入的size固定由前端传入进来
	 *
	 * @param response
	 *            {@link HttpServletResponse}
	 * @param form
	 *            {@link HomeworkQueryForm}
	 */
	@RequestMapping(value = "export", method = { RequestMethod.GET })
	public void export(HttpServletResponse response, HomeworkQueryForm form) {
		Page<Homework> pageValue = zycCorrectingService.page(form);
		VPage<VZycHomework> vPage = new VPage<VZycHomework>();
		List<VZycHomework> vs = zycHomeworkConvert.to(pageValue.getItems());

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet();
		HSSFRow row = sheet.createRow(0);
		row.createCell(0).setCellValue("省");
		row.createCell(1).setCellValue("市");
		row.createCell(2).setCellValue("学校");
		row.createCell(3).setCellValue("教师");
		row.createCell(4).setCellValue("班级/小组");
		row.createCell(5).setCellValue("作业名称");
		row.createCell(6).setCellValue("答题时间");
		row.createCell(7).setCellValue("已提交人数");
		row.createCell(8).setCellValue("状态");

		int i = 1;
		SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
		for (VZycHomework v : vs) {
			row = sheet.createRow(i);
			row.createCell(0).setCellValue(v.getClazz().getProvince());
			row.createCell(1).setCellValue(v.getClazz().getCity());
			row.createCell(2).setCellValue(v.getClazz().getSchoolName());
			row.createCell(3).setCellValue(v.getClazz().getTeacherName());
			String name = "";
			if(StringUtils.isNoneBlank(v.getClazz().getGroupName())) {
				name = v.getClazz().getName() + "/" + v.getClazz().getGroupName();
			} else {
				name = v.getClazz().getName();
			}
			row.createCell(4).setCellValue(name);
			row.createCell(5).setCellValue(v.getName());
			row.createCell(6).setCellValue(format.format(v.getStartTime()) + "~" + format.format(v.getDeadline()));
			row.createCell(7).setCellValue(v.getCommitCount() + "/" + v.getDistributeCount());

			String statusName = "";
			switch (v.getType()) {
			case FINISH:
				statusName = "已截止";
				break;
			case WORKING:
				statusName = "批改中";
				break;
			case INIT:
				statusName = "作业中";
				break;
			}
			row.createCell(8).setCellValue(statusName);
			i++;
		}

		try {
			response.reset();
			response.setContentType("bin");
			response.addHeader("Content-Disposition",
					"attachment; filename=\"" + new String("作业列表.xls".getBytes("UTF-8"), "ISO-8859-1") + "\"");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
		}

	}
}
