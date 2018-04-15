package com.lanking.uxb.service.pointcard.resource;

import java.math.BigDecimal;
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

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.convert.ExaminationPointConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointCardConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VKnowledgePointCard;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.examPaper.convert.KnowledgePointTreeNodeConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgeSystemTreeNodeConvert;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 学生知识卡片Controller
 *
 * @author xinyu.zhou
 * @since 2.6.0
 */
@RestController
@RequestMapping(value = "zy/s/kpc")
public class ZyStuKnowledgePointCardController {
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private KnowledgePointCardConvert knowledgePointCardConvert;
	@Autowired
	private ExaminationPointKnowledgePointService ekService;
	@Autowired
	private ExaminationPointService examinationPointService;
	@Autowired
	private ExaminationPointConvert examinationPointConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private DiagnosticStudentClassKnowpointService diaStuKnowpointService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;

	/**
	 * 学生知识卡片模块首页接口
	 *
	 * @param code
	 *            知识点code
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(long code) {

		Map<String, Object> retMap = new HashMap<String, Object>(5);

		List<ExaminationPointKnowledgePoint> es = ekService.findByKnowledgePoint(code);
		List<Long> examinationCodes = new ArrayList<Long>(es.size());
		for (ExaminationPointKnowledgePoint e : es) {
			examinationCodes.add(e.getExaminationPointId());
		}
		// 知识点关联的考点列表
		if (CollectionUtils.isNotEmpty(examinationCodes)) {
			List<ExaminationPoint> examinationPoints = examinationPointService.mgetList(examinationCodes);

			List<VExaminationPoint> evs = examinationPointConvert.to(examinationPoints);

			retMap.put("examinationPoints", evs);
		}

		List<VKnowledgePointCard> relativeCards = null;
		// 相关知识卡片
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findByPcode(code / 100);
		if (knowledgePoints.size() > 1) {
			List<KnowledgePointCard> cards = Lists.newArrayList();
			for (KnowledgePoint kp : knowledgePoints) {
				if (!kp.getCode().equals(code)) {
					cards.addAll(knowledgePointCardService.findByKnowledgePoint(kp.getCode()));
				}
			}
			relativeCards = new ArrayList<VKnowledgePointCard>(cards.size());
			for (KnowledgePointCard kpc : cards) {
				VKnowledgePointCard v = new VKnowledgePointCard();
				v.setId(kpc.getId());
				v.setName(kpc.getName());
				VKnowledgePoint vpoint = new VKnowledgePoint();
				vpoint.setCode(kpc.getKnowpointCode());
				v.setKnowledgePoint(vpoint);
				relativeCards.add(v);
			}

		}

		List<KnowledgePointCard> cards = knowledgePointCardService.findByKnowledgePoint(code);
		List<VKnowledgePointCard> vcards = new ArrayList<VKnowledgePointCard>(cards.size());
		List<Long> questionIds = new ArrayList<Long>();
		for (KnowledgePointCard c : cards) {
			if (CollectionUtils.isNotEmpty(c.getQuestions())) {
				questionIds.addAll(c.getQuestions());
			}
			VKnowledgePointCard v = knowledgePointCardConvert.to(c);
			v.setQuestionIds(c.getQuestions());
			vcards.add(v);
		}

		List<Question> questions = questionService.mgetList(questionIds);
		QuestionConvertOption option = new QuestionConvertOption(true, true, true, null);
		List<VQuestion> vquestions = questionConvert.to(questions, option);

		Map<Long, VQuestion> vquestionMap = new HashMap<Long, VQuestion>(vquestions.size());
		for (VQuestion v : vquestions) {
			vquestionMap.put(v.getId(), v);
		}

		for (VKnowledgePointCard v : vcards) {
			v.setRelated(relativeCards);
			if (CollectionUtils.isEmpty(v.getQuestionIds())) {
				continue;
			}
			List<Long> qIds = v.getQuestionIds();
			List<Object> cardQuestions = new ArrayList<Object>(qIds.size());
			for (Long id : qIds) {
				cardQuestions.add(vquestionMap.get(id));
			}

			v.setQuestions(cardQuestions);
		}

		// 本知识点下的知识卡片列表
		retMap.put("knowledgePointCards", vcards);

		// 学生知识点掌握情况
		List<DiagnosticStudentClassKnowpoint> studentClassKnowpoints = diaStuKnowpointService.queryByKnowledge(code,
				Security.getUserId());

		if (CollectionUtils.isNotEmpty(studentClassKnowpoints)) {
			int doTotal = 0;
			int rightCount = 0;
			for (DiagnosticStudentClassKnowpoint k : studentClassKnowpoints) {
				doTotal += k.getDoCount();
				rightCount += k.getRightCount();
			}

			BigDecimal completeRate = new BigDecimal((rightCount + 1) * 100d / (doTotal + 2)).setScale(0,
					BigDecimal.ROUND_HALF_UP);

			retMap.put("completeRate", completeRate);
		}

		List<KnowledgeSystem> knowledgeSystems = knowledgeSystemService.mgetList(knowledgeSystemService
				.findAllCodeByKPoint(code));

		retMap.put("knowledgeSystems", knowledgeSystemConvert.to(knowledgeSystems));
		retMap.put("knowledgePoint", knowledgePointConvert.to(knowledgePointService.get(code)));

		return new Value(retMap);
	}

	/**
	 * 根据知识点列表查询数据列表
	 *
	 * @param codes
	 *            知识点code列表
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getKnowledgeByCodes", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowledgeByCodes(@RequestParam(value = "codes") List<Long> codes) {
		if (CollectionUtils.isEmpty(codes)) {
			return new Value(new IllegalArgException());
		}

		List<Long> kcodes = knowledgePointCardService.findHasCardKnowledgePoint(codes);

		if (CollectionUtils.isEmpty(kcodes)) {
			return new Value(Collections.EMPTY_LIST);
		}

		List<KnowledgePoint> points = knowledgePointService.mgetList(kcodes);
		return new Value(knowledgePointConvert.to(points));
	}

	/**
	 * 新知识点数集合并且返回每个知识点下对应的卡片数量
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "allKnowledgeCard", method = { RequestMethod.GET, RequestMethod.POST })
	public Value allKnowledgeCard(long code) {
		// 判断当前传进来的知识点是哪个阶段的
		KnowledgePoint kp = knowledgePointService.get(code);
		List<KnowledgePoint> points = knowledgePointService.findBySubject(kp.getSubjectCode());
		List<Long> codes = new ArrayList<Long>();
		for (KnowledgePoint k : points) {
			codes.add(k.getCode());
		}
		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeTreeNode> nodes = knowledgePointTreeNodeConvert.to(points);
		Map<Long, Long> cardMap = knowledgePointCardService.statisByPoints(codes);
		for (VKnowledgeTreeNode n : nodes) {
			n.setKnowCardCount(cardMap.get(n.getCode()) == null ? 0 : cardMap.get(n.getCode()));
		}
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("tree", knowledgeSystemTreeNodeConvert.assemblyPointTreeFilterNoCard(newList));
		return new Value(retMap);
	}
}
