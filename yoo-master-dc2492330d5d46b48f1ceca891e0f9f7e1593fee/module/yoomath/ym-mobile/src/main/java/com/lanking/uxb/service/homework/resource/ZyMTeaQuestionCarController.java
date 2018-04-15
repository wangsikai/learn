package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;

/**
 * 客户端教师作业篮子Controller
 *
 * @author xinyu.zhou
 * @since 3.0.0
 */
@RestController
@RequestMapping(value = "zy/m/t/qc")
public class ZyMTeaQuestionCarController {
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyQuestionCarService questionCarService;

	/**
	 * 获得现在篮子中题目数量
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getAmount() {
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("count", questionCarService.countQuestions(Security.getUserId()));
		return new Value(retMap);
	}

	/**
	 * 获得作业篮子中的题目数据
	 *
	 * @return 题目列表
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getQuestions() {
		// 先取按顺序排序的id，没有的话从hash取
		List<Long> questionCarIds = questionCarService.getQuestionCarIds(Security.getUserId());
		List<Long> sortCarIds = questionCarService.getQuestionIds(Security.getUserId());
		List<Long> carQuestions = Lists.newArrayList();
		if (CollectionUtils.isEmpty(questionCarIds)) {
			return new Value();
		}

		// @since 2017/8/25 SSS-2006作业篮子重复题
		if (CollectionUtils.isEmpty(sortCarIds)) {
			carQuestions = questionCarIds;
		} else {
			for (Long sortId : sortCarIds) {
				for (Long id : questionCarIds) {
					if (sortId.longValue() == id.longValue() && !carQuestions.contains(sortId)) {
						carQuestions.add(sortId);
						break;
					}
				}
			}
		}

		if (CollectionUtils.isNotEmpty(questionCarIds) && CollectionUtils.isNotEmpty(sortCarIds)) {
			if (questionCarIds.size() != sortCarIds.size()) {
				questionCarService.addQuestionIds(Security.getUserId(), questionCarIds);
			}
		}

		int predictTime = 0;
		List<Question> questions = questionService.mgetList(Lists.newArrayList(carQuestions));
		List<Question> sortQuestions = Lists.newArrayList();
		for (Long carId : carQuestions) {
			for (Question q : questions) {
				if (carId.longValue() == q.getId().longValue()) {
					sortQuestions.add(q);
				}
			}
		}

		Collections.sort(sortQuestions, new Comparator<Question>() {
			@Override
			public int compare(Question q1, Question q2) {
				if (q1.getType().getValue() < q2.getType().getValue()) {
					return -1;
				} else if (q1.getType().getValue() > q2.getType().getValue()) {
					return 1;
				}
				// @since 教师端v1.3.0 删掉按难度排序
				// else {
				// if (q1.getDifficulty() > q2.getDifficulty()) {
				// return -1;
				// } else if (q1.getDifficulty() < q2.getDifficulty()) {
				// return 1;
				// }
				// }
				return 0;
			}
		});

		// 计算预估时间
		for (Question q : sortQuestions) {
			predictTime += questionService.calPredictTime(q);
		}

		// @since 教师端v1.3.0
		QuestionConvertOption option = new QuestionConvertOption(false, true, true, null);
		option.setInitPublishCount(true);
		option.setInitQuestionSimilarCount(true);
		option.setInitExamination(true);
		option.setInitQuestionTag(true); // 标签
		option.setCollect(true);

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("questions", questionConvert.to(sortQuestions, option));
		retMap.put("predictTime", predictTime);

		return new Value(retMap);
	}

	/**
	 * 添加题目至作业篮子
	 *
	 * @param questionIds
	 *            题目id列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "addToCar", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addToCar(String questionIds) {
		if (StringUtils.isBlank(questionIds)) {
			return new Value(new IllegalArgException());
		}

		String[] idStrs = questionIds.split(",");
		List<Long> addQuestionIds = new ArrayList<Long>(idStrs.length);
		List<Long> inCarIds = questionCarService.getQuestionIds(Security.getUserId());
		if (CollectionUtils.isEmpty(inCarIds)) {
			inCarIds = questionCarService.getQuestionCarIds(Security.getUserId());
		}

		// List<Long> inCarIds =
		// questionCarService.getQuestionCarIds(Security.getUserId());
		for (String idStr : idStrs) {
			Long id = Long.valueOf(idStr);
			if (inCarIds.contains(id)) {
				continue;
			}

			addQuestionIds.add(id);
		}

		inCarIds.addAll(addQuestionIds);

		if (inCarIds.size() > 25) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_ADDTOCAR_FULL));
		}

		List<Question> questions = questionService.mgetList(addQuestionIds);
		// @since 教师端v1.3.0 修改排序
		List<Question> sorts = Lists.newArrayList();
		for (Long id : addQuestionIds) {
			for (Question q : questions) {
				if (id.longValue() == q.getId().longValue()) {
					sorts.add(q);
				}
			}
		}

		long userId = Security.getUserId();
		for (Question q : sorts) {
			questionCarService.addQuestion2Car(userId, q.getId(), q.getDifficulty(), q.getType());
		}

		// 按选择顺序添加排序
		questionCarService.addQuestionIds(userId, inCarIds);
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("count", questionCarService.countQuestions(userId));

		return new Value(retMap);
	}

	/**
	 * 移除题目
	 *
	 * @param questionIds
	 *            题目列表id
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "remove", method = { RequestMethod.GET, RequestMethod.POST })
	public Value remove(String questionIds) {
		if (StringUtils.isBlank(questionIds)) {
			return new Value(new IllegalArgException());
		}

		String[] idStrs = questionIds.split(",");
		long userId = Security.getUserId();
		for (String idStr : idStrs) {
			questionCarService.removeFromCar(userId, Long.valueOf(idStr));
		}

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("count", questionCarService.countQuestions(userId));
		List<Long> qIds = questionCarService.getQuestionCarIds(userId);
		List<Question> questions = questionService.mgetList(qIds);
		int predictTime = 0;
		for (Question q : questions) {
			predictTime += questionService.calPredictTime(q);
		}
		retMap.put("predictTime", predictTime);

		return new Value(retMap);
	}

}
