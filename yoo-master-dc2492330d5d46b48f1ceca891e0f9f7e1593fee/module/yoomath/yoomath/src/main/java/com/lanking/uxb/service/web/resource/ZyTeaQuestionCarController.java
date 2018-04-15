package com.lanking.uxb.service.web.resource;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.api.ExerciseQuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 题目篮子相关接口(类似于购物车)
 * 
 * @since yoomathV1.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年9月6日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/qcar")
@RolesAllowed(userTypes = { "TEACHER" })
public class ZyTeaQuestionCarController {

	@Autowired
	private ZyQuestionCarService zyQcarService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ExerciseQuestionService eqService;

	/**
	 * 添加问题至篮子
	 *
	 * @param id
	 *            问题的id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "addQuestion2Car", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value addQuestion2Car(@RequestParam(value = "id") Long id,
			@RequestParam(value = "difficult") double difficult,
			@RequestParam(value = "type", required = false) Question.Type type) {
		// 增加校验 by zhaopeng
		List<Long> inCarIds = zyQcarService.getQuestionIds(Security.getUserId());
		if (CollectionUtils.isEmpty(inCarIds)) {
			inCarIds = zyQcarService.getQuestionCarIds(Security.getUserId());
		}
		if (inCarIds.size() > 25) {
			return new Value(new IllegalArgException());
		}

		try {
			zyQcarService.addQuestion2Car(Security.getUserId(), id, difficult, type);

			// 按选择顺序添加排序
			inCarIds.add(id);
			zyQcarService.addQuestionIds(Security.getUserId(), inCarIds);
		} catch (ZuoyeException e) {
			// 已经添加过题目了
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 添加习题页的题目至篮子
	 *
	 * @param exerciseId
	 *            习题页的id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "addQuestions2Car", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value addQuestions2Car(@RequestParam(value = "exerciseId") long exerciseId) {
		// 增加校验 by zhaopeng
		List<Long> inCarIds = zyQcarService.getQuestionIds(Security.getUserId());
		if (CollectionUtils.isEmpty(inCarIds)) {
			inCarIds = zyQcarService.getQuestionCarIds(Security.getUserId());
		}
		if (inCarIds.size() > 25) {
			return new Value(new IllegalArgException());
		}

		List<Question> questionList = questionService.mgetList(eqService.getQuestion(exerciseId));
		List<Long> qList = zyQcarService.addQuestions2Car(Security.getUserId(), questionList);

		inCarIds.addAll(qList);
		// 按选择顺序添加排序
		zyQcarService.addQuestionIds(Security.getUserId(), inCarIds);

		return new Value(questionConvert.mget(qList));
	}

	/**
	 * 从篮子中去除题目
	 *
	 * @param id
	 *            题目的id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "removeQuestionFromCar", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value removeQuestionFromCar(@RequestParam(value = "id") Long id) {
		zyQcarService.removeFromCar(Security.getUserId(), id);
		return new Value();
	}

	/**
	 * 批量移除作业篮子里的题目
	 *
	 * @param ids
	 *            String类型的id组合"id,id,id"
	 * @return Value
	 */
	@RequestMapping(value = "removeQuestionsFromCar", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value removeQuestionsFromCar(@RequestParam(value = "ids") String ids) {
		if (StringUtils.isBlank(ids)) {
			return new Value(new IllegalArgException());
		}

		zyQcarService.removeAll(Security.getUserId());

		return new Value();
	}

	/**
	 * 预览题目
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "preview", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value preview() {
		List<Question> questions = zyQcarService.getQuestions(Security.getUserId());
		return new Value(questionConvert.to(questions, new QuestionConvertOption(false, true, true, null)));
	}

	/**
	 * 添加的各种难度题目的数量
	 *
	 * 作业篮子更改为可保存题目类型
	 *
	 * @return Value
	 * @since yoomath V1.4
	 */
	@RequestMapping(value = "carCondition", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value carCondition(@RequestParam(value = "flag", defaultValue = "false") Boolean flag) {
		if (flag) {
			return new Value(zyQcarService.mgetList(Security.getUserId()));
		}
		return new Value(zyQcarService.getCarCondition(Security.getUserId()));
	}

	@RequestMapping(value = "car_count", method = { RequestMethod.GET, RequestMethod.POST })
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value carCount() {
		return new Value(zyQcarService.countQuestions(Security.getUserId()));
	}

	/**
	 * 作业预览.
	 *
	 * @param qids
	 * @return
	 */
	@RequestMapping(value = "carQuestionsView")
	@RolesAllowed(userTypes = { "TEACHER" })
	public Value carQuestionsView(Long[] qids) {
		if (null == qids || qids.length == 0) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> data = new HashMap<String, Object>(2);

		List<Question> questions = questionService.mgetList(Lists.newArrayList(qids));
		Collections.sort(questions, new Comparator<Question>() {
			@Override
			public int compare(Question q1, Question q2) {
				if (q1.getType().getValue() < q2.getType().getValue()) {
					return -1;
				} else if (q1.getType().getValue() > q2.getType().getValue()) {
					return 1;
				} else {
					if (q1.getDifficulty().doubleValue() > q2.getDifficulty().doubleValue()) {
						return -1;
					} else if (q1.getDifficulty().doubleValue() < q2.getDifficulty().doubleValue()) {
						return 1;
					}
				}
				return 0;
			}
		});
		List<VQuestion> qvs = questionConvert.to(questions, new QuestionConvertOption(true, true, true, null));
		data.put("questions", qvs);

		// 获取知识点
		Map<Integer, VMetaKnowpoint> vmks = new HashMap<Integer, VMetaKnowpoint>();
		for (VQuestion question : qvs) {
			if (question.getMetaKnowpoints() != null) {
				for (VMetaKnowpoint vmk : question.getMetaKnowpoints()) {
					if (vmks.get(vmk.getCode()) == null) {
						vmks.put(vmk.getCode(), vmk);
					}
				}
			}
		}
		data.put("metaKnowpoints", vmks.values());
		return new Value(data);
	}

	/**
	 * 存储排序后的题目
	 *
	 * @param ids
	 *            排序后的题目id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "store_sorted_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value storeSortedQuestions(Long[] ids) {
		List<Long> paramIds = Lists.newArrayList();
		Collections.addAll(paramIds, ids);

		List<Long> carIds = zyQcarService.getQuestionIds(Security.getUserId());
		if (CollectionUtils.isEmpty(carIds)) {
			carIds = zyQcarService.getQuestionCarIds(Security.getUserId());
		}
		// 和手机端保持数据一直
		List<Long> sortedIds = Lists.newArrayList();
		for (Long id : paramIds) {
			if (carIds.contains(id)) {
				sortedIds.add(id);
			}
		}

		zyQcarService.addSortedQuestions(Security.getUserId(), sortedIds);
		zyQcarService.addQuestionIds(Security.getUserId(), sortedIds);

		return new Value();
	}

	/**
	 * 预览题目
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "get_sorted_questions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSortedQuestions(@RequestParam(value = "v", defaultValue = "1") Integer version) {
		List<Long> sortedList = zyQcarService.getSortedQuestions(Security.getUserId());
		boolean flag = false;
		List<Long> carQuestions = zyQcarService.getQuestionCarIds(Security.getUserId());
		// 已经有排序队列的情况
		if (CollectionUtils.isEmpty(sortedList)) {
			if (carQuestions.size() == 0) {
				return new Value();
			}
			flag = true;
		} else if (CollectionUtils.isEmpty(carQuestions)) {
			return new Value();
		}
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<Question> questions = null;
		// flag = true,说明原来没有排序队列，需要对原始的进行一次排序操作。
		if (flag) {
			questions = questionService.mgetList(Lists.newArrayList(carQuestions));
			Collections.sort(questions, new Comparator<Question>() {
				@Override
				public int compare(Question q1, Question q2) {
					if (q1.getType().getValue() < q2.getType().getValue()) {
						return -1;
					} else if (q1.getType().getValue() > q2.getType().getValue()) {
						return 1;
					} else {
						if (q1.getDifficulty().doubleValue() > q2.getDifficulty().doubleValue()) {
							return -1;
						} else if (q1.getDifficulty().doubleValue() < q2.getDifficulty().doubleValue()) {
							return 1;
						}
					}
					return 0;
				}
			});

			// 对于首次添加应该先排序完成之后，再加入至排序列表里
			carQuestions = Lists.newArrayList();
			for (Question q : questions) {
				carQuestions.add(q.getId());
			}
			for (Long c : carQuestions) {
				sortedList.add(c);
			}
			zyQcarService.addSortedQuestions(Security.getUserId(), sortedList);
		} else {
			questions = questionService.mgetList(Lists.newArrayList(sortedList));
		}

		// @since 3.0.2 转换增加考点数据
		Map<Long, VQuestion> qvm = questionConvert.toMap(questions,
		        new QuestionConvertOption(false, true, true, false, true, null));
		List<VQuestion> qvs = Lists.newArrayList();
		for (Long id : sortedList) {
			qvs.add(qvm.get(id));
		}
		data.put("questions", qvs);

		switch (version) {
			case 1:
				// 获取知识点
				Map<Integer, VMetaKnowpoint> vmks = new HashMap<Integer, VMetaKnowpoint>();
				for (VQuestion question : qvs) {
					if (CollectionUtils.isNotEmpty(question.getMetaKnowpoints())) {
						for (VMetaKnowpoint vmk : question.getMetaKnowpoints()) {
							if (vmks.get(vmk.getCode()) == null) {
								vmks.put(vmk.getCode(), vmk);
							}
						}
					}
				}
				data.put("metaKnowpoints", vmks.values());
				break;
			case 2:
				Map<Long, VKnowledgePoint> vks = new HashMap<Long, VKnowledgePoint>();
				for (VQuestion v : qvs) {
					if (CollectionUtils.isNotEmpty(v.getNewKnowpoints())) {
						for (VKnowledgePoint vkp : v.getNewKnowpoints()) {
							vks.put(vkp.getCode(), vkp);
						}
					}
				}
				data.put("metaKnowpoints", vks);
				break;
		}

		return new Value(data);
	}

}
