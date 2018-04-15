package com.lanking.uxb.rescon.basedata.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconKnowpointCardService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowpointService;
import com.lanking.uxb.rescon.basedata.api.ResconMetaKnowpointService;
import com.lanking.uxb.rescon.basedata.api.ResconPointService;
import com.lanking.uxb.rescon.basedata.api.ResconPointType;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowpointCardConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowpointConvert;
import com.lanking.uxb.rescon.basedata.convert.ResconMetaKnowpointConvert2;
import com.lanking.uxb.rescon.basedata.form.ResconPointCardForm;
import com.lanking.uxb.rescon.basedata.form.ResconPointForm;
import com.lanking.uxb.rescon.basedata.value.VResconPoint;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "rescon/point")
public class ResconPointController {

	@Autowired
	private ResconKnowpointService knowpointService;
	@Autowired
	private ResconMetaKnowpointService metaKnowpointService;
	@Autowired
	private ResconKnowpointConvert knowpointConvert;
	@Autowired
	private ResconMetaKnowpointConvert2 metaKnowpointConvert2;
	@Autowired
	private ResconPointService pointService;
	@Autowired
	private ResconKnowpointCardService cardService;
	@Autowired
	private ResconKnowpointCardConvert cardConvert;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;

	/**
	 * 初始化tree
	 *
	 * @param subjectCode
	 *            学科代码
	 * @param phaseCode
	 *            阶段代码
	 * @return {@link Value}
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseCode") Integer phaseCode) {
		List<Integer> knowpointCodes = Lists.newArrayList();
		List<Knowpoint> knowpoints = knowpointService.find(null, subjectCode, phaseCode);
		List<VResconPoint> vResconPoints = Lists.newArrayList();
		for (Knowpoint k : knowpoints) {
			knowpointCodes.add(k.getCode());
		}
		vResconPoints = knowpointConvert.to(knowpoints);
		List<MetaKnowpoint> metaKnowpoints = metaKnowpointService.findAll(knowpointCodes);
		List<VResconPoint> metaResconVPoints = metaKnowpointConvert2.to(metaKnowpoints);
		vResconPoints.addAll(metaResconVPoints);

		return new Value(vResconPoints);
	}

	/**
	 * 保存
	 *
	 * @param formStr
	 *            提交的form string
	 * @return {@link Value}
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(@RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}
		ResconPointForm form = JSONObject.parseObject(formStr, ResconPointForm.class);
		// 必须需要type类型
		if (form.getType() == null) {
			return new Value(new IllegalArgException());
		}
		if (form.getIsAdd()) {
			if (form.getLevel() != null && form.getLevel() == 2) {
				return new Value(new NoPermissionException());
			}
			pointService.save(form);
		} else {
			pointService.update(form);
		}
		return new Value();
	}

	/**
	 * 同步数据
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "syncData", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncData() {
		pointService.syncData();
		return new Value();
	}

	/**
	 * 更新知识点排序值
	 *
	 * @param code
	 *            知识点编码
	 * @param sequence
	 *            排序值
	 * @param type
	 *            知识点类型
	 * @return {@link Value}
	 */
	@RequestMapping(value = "updateSequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateSequence(@RequestParam(value = "code") int code, @RequestParam(value = "sequence") int sequence,
			@RequestParam(value = "type") ResconPointType type) {
		pointService.updateSequence(code, sequence, type);

		return new Value();
	}

	/**
	 * 将待启用的节点启用
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "turnOn", method = { RequestMethod.GET, RequestMethod.POST })
	public Value turnOn() {
		pointService.turnOn();
		return new Value();
	}

	/**
	 * 启用并同步
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "turnOnAndSync", method = { RequestMethod.GET, RequestMethod.POST })
	public Value turnOnAndSync() {
		pointService.turnOn();
		pointService.syncData();

		return new Value();
	}

	/**
	 * 获取知识点对应 知识卡片
	 * 
	 * @param knowpointCode
	 * @return
	 */
	@RequestMapping(value = "getCard", method = RequestMethod.POST)
	public Value create(long knowpointCode) {
		return new Value(cardConvert.to(cardService.getByCode(knowpointCode)));
	}

	@RequestMapping(value = "getQuestionByCode", method = RequestMethod.POST)
	public Value getQuestionByCode(String questionCode, int subjectCode, int phaseCode) {
		List<Question> qList = questionManage.findQuestionByCode(Lists.newArrayList(questionCode), null);
		if (CollectionUtils.isEmpty(qList) || qList.size() != 1) {
			return new Value(new IllegalArgException());
		}
		Question question = qList.get(0);
		if (question.getSubjectCode() != subjectCode || question.getPhaseCode() != phaseCode) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.SUBJECT_PHASE_NOT_MATCH_KNOWPOINT));
		}
		if (question.getSchoolId() > 0) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.SCHOOL_QUESTION_ERROR));
		}
		if (question.getStatus() != CheckStatus.PASS) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.QUESTION_NOT_PASS));
		}
		return new Value(questionConvert.to(question));
	}

	/**
	 * 保存/编辑知识卡片
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "savePointCard", method = RequestMethod.POST)
	public Value create(String json) {
		ResconPointCardForm pointCardForm = JSON.parseObject(json, ResconPointCardForm.class);
		pointCardForm.setDate(new Date());
		pointCardForm.setUserId(Security.getUserId());
		KnowpointCard card = cardService.save(pointCardForm);
		return new Value(cardConvert.to(card));
	}
}
