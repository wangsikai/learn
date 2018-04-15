package com.lanking.uxb.rescon.basedata.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointCardService;
import com.lanking.uxb.rescon.basedata.api.ResconExaminationPointService;
import com.lanking.uxb.rescon.basedata.convert.ResconExaminationPointCardConvert;
import com.lanking.uxb.rescon.basedata.form.ResconExaminationPointCardForm;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.rescon.question.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 考点卡片Controller
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
@RestController
@RequestMapping(value = "/rescon/ep/")
public class ResconExaminationPointCardController {

	@Autowired
	private ResconExaminationPointCardService cardService;
	@Autowired
	private ResconExaminationPointCardConvert cardConvert;
	@Autowired
	private ResconExaminationPointService pointService;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ResconQuestionManage questionManage;

	/**
	 * 保存考点卡片
	 *
	 * @param form
	 *            {@link ResconExaminationPointCardForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(ResconExaminationPointCardForm form) {
		cardService.save(form, Security.getUserId());

		return new Value();
	}

	/**
	 * 根据考点查找考点卡片
	 *
	 * @param code
	 *            考点代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getByExaminationCode", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getByExaminationCode(long code) {
		ExaminationPoint examinationPoint = pointService.get(code);
		if (examinationPoint == null) {
			return new Value(new IllegalArgException());
		}

		List<VQuestion> questions = new ArrayList<VQuestion>(examinationPoint.getQuestions().size());
		if (examinationPoint.getQuestions().size() > 0) {
			Map<Long, VQuestion> questionMap = questionConvert.to(questionManage.mget(examinationPoint.getQuestions()));
			for (Long questionId : examinationPoint.getQuestions()) {
				questions.add(questionMap.get(questionId));
			}
		}

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("questions", questions);
		retMap.put("card", cardConvert.to(cardService.findByExaminationPoint(code)));
		return new Value(retMap);
	}

	/**
	 * 得到当前学科下的卡片统计
	 *
	 * @param subjectCode
	 *            学科代码
	 * @return {@links Value}
	 */
	@RequestMapping(value = "statistic", method = { RequestMethod.GET, RequestMethod.POST })
	public Value statistic(int subjectCode) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		long questionCount = cardService.questionCount(subjectCode);
		retMap.put("questionCount", questionCount);
		retMap.put("statusCount", cardService.statusCount(subjectCode));

		return new Value(retMap);
	}

	@RequestMapping(value = "get", method = { RequestMethod.GET, RequestMethod.POST })
	public Value get(long id) {
		return new Value(cardConvert.to(cardService.get(id)));
	}
}
