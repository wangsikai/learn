package com.lanking.uxb.rescon.basedata.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointCardService;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointCardConvert;
import com.lanking.uxb.rescon.basedata.form.ResconKnowledgePointCardForm;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 知识卡片Controller
 *
 * @author xinyu.zhou
 * @since 2.0.1
 */
@RestController
@RequestMapping(value = "/rescon/kpc/")
public class ResconKnowledgePointCardController {
	@Autowired
	private ResconKnowledgePointCardService cardService;
	@Autowired
	private ResconKnowledgePointCardConvert cardConvert;
	@Autowired
	private IndexService indexService;

	/**
	 * 保存知识卡片
	 *
	 * @param form
	 *            {@link ResconKnowledgePointCardForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(ResconKnowledgePointCardForm form) {
		List<Long> questionIds = cardService.save(form, Security.getUserId());
		if (CollectionUtils.isNotEmpty(questionIds)) {
			indexService.update(IndexType.QUESTION, questionIds);
		}
		return new Value();
	}

	/**
	 * 删除知识卡片
	 *
	 * @param id
	 *            知识卡片id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(long id) {
		cardService.delete(id);
		return new Value();
	}

	/**
	 * 启用知识卡片
	 *
	 * @param id
	 *            知识卡片id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "use", method = { RequestMethod.GET, RequestMethod.POST })
	public Value use(long id) {
		List<Long> needUpdateQuestions = cardService.use(id);
		if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
			indexService.update(IndexType.QUESTION, needUpdateQuestions);
		}

		return new Value();
	}

	/**
	 * 禁用知识卡片
	 *
	 * @param id
	 *            id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "forbid", method = { RequestMethod.GET, RequestMethod.POST })
	public Value forbid(long id) {
		List<Long> needUpdateQuestions = cardService.forbid(id);

		if (CollectionUtils.isNotEmpty(needUpdateQuestions)) {
			indexService.update(IndexType.QUESTION, needUpdateQuestions);
		}

		return new Value();
	}

	/**
	 * 获得单个知识卡片信息
	 *
	 * @param id
	 *            知识卡片id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "get", method = { RequestMethod.GET, RequestMethod.POST })
	public Value get(long id) {
		KnowledgePointCard card = cardService.get(id);
		if (null == card) {
			return new Value(new IllegalArgException());
		}
		return new Value(cardConvert.to(card));
	}

	/**
	 * 得到当前学科下卡片统计数据
	 *
	 * @param subjectCode
	 *            学科编码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "statistic", method = { RequestMethod.GET, RequestMethod.POST })
	public Value statistic(int subjectCode) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		long questionCount = cardService.questionCount(subjectCode);
		retMap.put("questionCount", questionCount);
		retMap.put("statusCount", cardService.statusCount(subjectCode));

		return new Value(retMap);
	}

	/**
	 * 根据知识点查找知识卡片
	 *
	 * @param code
	 *            知识点代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getByKnowpoint", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getByKnowpoint(long code) {
		return new Value(cardConvert.to(cardService.findByKnowpointCode(code)));
	}
}
