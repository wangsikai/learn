package com.lanking.uxb.zycon.homework.resource;

import java.math.BigInteger;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppeal;
import com.lanking.cloud.domain.yoomath.homework.QuestionAppealStatus;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.correct.api.CorrectLogService;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.resources.api.QuestionAppealService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionAppealRecordService;
import com.lanking.uxb.zycon.homework.api.ZycQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkService;
import com.lanking.uxb.zycon.homework.convert.ZycQuestionConvert;
import com.lanking.uxb.zycon.homework.form.QuestionAppealRecordForm;
import com.lanking.uxb.zycon.homework.value.VZycQuestion;

/**
 * 后台批改申述记录接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@RestController
@RequestMapping(value = "zyc/correctAppeal/")
public class ZycCorrectAppealRecordController {
	@Autowired
	private ZycQuestionAppealRecordService zycQuestionAppealService;
	@Autowired
	@Qualifier(value = "hzycQuestionConvert")
	private ZycQuestionConvert questionConvert;
	@Autowired
	private ZycStudentHomeworkQuestionService zycStudentHomeworkQuestionService;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private CorrectLogService logService;
	@Autowired
	private StudentHomeworkAnswerService shaService;
	@Autowired
	private QuestionAppealService qaService;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private ZycHomeworkService homeworkService;
	@Autowired
	private ZycStudentHomeworkService studentHomeworkService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 查询现在作业列表(非下发)
	 *
	 * @return Value
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "100") int size, QuestionAppealRecordForm form) {
		Pageable pageable = P.index(page, size);
		Page<Map> pageValue = zycQuestionAppealService.page(pageable, form);

		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (pageValue.getTotalCount() + size - 1) / size;
		vp.setPageSize(size);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(pageValue.getTotalCount());
		for (Map map : pageValue.getItems()) {
			Long id = ((BigInteger) map.get("stuhkqid")).longValue();
			map.put("stuhkqid", id);

			Long creator = ((BigInteger) map.get("creator")).longValue();
			map.put("creator", creator);
		}
		vp.setItems(pageValue.getItems());

		return new Value(vp);
	}

	/**
	 * 查询申述的问题和答案
	 *
	 * @param stuHkQId
	 *            学生申述的题目id
	 * @param creator
	 *            申述人的id
	 * @return Value
	 */
	@RequestMapping(value = "queryStuQuestion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryStuQuestion(@RequestParam(value = "stuHkQId") Long stuHkQId,
			@RequestParam(value = "creator") Long creator) {
		if (stuHkQId == null) {
			return new Value(new IllegalArgException());
		}
		StudentHomeworkQuestion studentHomeworkQuestion = zycStudentHomeworkQuestionService.get(stuHkQId);

		if (studentHomeworkQuestion == null) {
			return new Value(new IllegalArgException());
		}

		Long questionId = studentHomeworkQuestion.getQuestionId();

		Question question = questionService.zycFindQuestion(questionId);
		question.setStudentHomeworkId(studentHomeworkQuestion.getStudentHomeworkId());
		VZycQuestion vs = questionConvert.to(question);
		vs.setStudentHomeworkId(studentHomeworkQuestion.getStudentHomeworkId());
		vs.setCreator(creator);

		QuestionAppeal appeal = qaService.getAppeal(stuHkQId);

		if (appeal != null) {
			Boolean hasCorrected = logService.hasCorrectd(stuHkQId, QuestionCorrectType.YOO_CORRECT,
					appeal.getCreateAt());
			Boolean riviseHasCorrectd = false;
			if (vs.getCorrectStudentHomeworkQuestion() != null) {
				riviseHasCorrectd = logService.hasCorrectd(vs.getCorrectStudentHomeworkQuestion().getId(),
						QuestionCorrectType.YOO_CORRECT, appeal.getCreateAt());
			}
			vs.setCanSubmitAppeal(hasCorrected || riviseHasCorrectd);
		}

		return new Value(vs);
	}

	/**
	 * 申述确认接口
	 *
	 * @since yoomath V1.9.2
	 *
	 * @param stuHkQId
	 *            学生作业题目id
	 * @param creator
	 *            申诉人的id
	 * @param type
	 *            {@link com.lanking.cloud.domain.common.resource.question.Question.Type}
	 * @return {@link Value}
	 */
	@Deprecated
	@RequestMapping(value = "confirmAppeal", method = { RequestMethod.GET, RequestMethod.POST })
	public Value confirmAppeal(long stuHkQId, @RequestParam(value = "creator") Long creator, Question.Type type) {

		QuestionAppeal appeal = qaService.getAppeal(stuHkQId);

		// 查看申述的状态是否需要处理
		if (appeal != null && appeal.getStatus() == QuestionAppealStatus.INIT) {
			// 申述处理，跟原来的申述时的结果比较，看要怎么处理
			// processAppeal(stuHkQId, appeal, type);
		}

		return new Value();
	}

}
