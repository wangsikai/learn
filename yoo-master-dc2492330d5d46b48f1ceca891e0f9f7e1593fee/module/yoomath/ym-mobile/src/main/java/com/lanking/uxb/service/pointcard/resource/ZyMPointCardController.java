package com.lanking.uxb.service.pointcard.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.card.KnowledgePointCard;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.code.api.ExaminationPointService;
import com.lanking.uxb.service.code.api.KnowledgePointCardService;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.convert.ExaminationPointConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointCardConvert;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.value.VExaminationPoint;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VKnowledgePointCard;
import com.lanking.uxb.service.code.value.VKnowledgeSystem;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 知识点卡片Controller
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
@RestController
@RequestMapping(value = "zy/m/pc")
public class ZyMPointCardController {
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private KnowledgePointCardService knowledgePointCardService;
	@Autowired
	private ExaminationPointKnowledgePointService ekService;
	@Autowired
	private ExaminationPointService examinationPointService;
	@Autowired
	private ExaminationPointConvert examinationPointConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private KnowledgePointCardConvert knowledgePointCardConvert;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private KnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private ParameterService parameterService;

	/**
	 * 查看全部的时候得到大专题以及小专题树型结构数据
	 *
	 * @since 学生客户端 v1.4.3 2017-5-27 当前客户端已无此接口调用，若后期使用，请删除 Deprecated 注解
	 * 
	 * @param pointCode
	 *            知识点code
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@Deprecated
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "getKnowledgeSystem", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowledgeSystem(Long pointCode, Integer subjectCode) {
		if (pointCode == null && subjectCode == null) {
			return new Value(new IllegalArgException());
		}
		List<KnowledgePoint> points = null;
		if (pointCode != null) {
			KnowledgePoint kp = knowledgePointService.get(pointCode);
			if (kp == null) {
				return new Value(new IllegalArgException());
			}
			subjectCode = kp.getSubjectCode();
		}
		points = knowledgePointService.findBySubject(subjectCode);

		if (CollectionUtils.isEmpty(points)) {
			return new Value(new IllegalArgException());
		}
		List<Long> codes = new ArrayList<Long>();
		Map<Long, VKnowledgePoint> knowledgePointMap = new HashMap<Long, VKnowledgePoint>(points.size());
		List<VKnowledgePoint> vKnowledgePoints = knowledgePointConvert.to(points);
		for (VKnowledgePoint k : vKnowledgePoints) {
			k.setLevel(4);
			codes.add(k.getCode());
			knowledgePointMap.put(k.getCode(), k);
		}

		List<KnowledgePointCard> cards = knowledgePointCardService.findByKnowledgePoints(codes);

		// 首先批量获取 Parameter值
		List<String[]> argsList = new ArrayList<String[]>(cards.size());
		for (int i = 0; i < cards.size(); i++) {
			argsList.add(new String[] { String.valueOf(cards.get(i).getId()) });
		}
		List<String> paramValues = parameterService.mgetValueList(Product.YOOMATH, "h5.knowledge-card.url", argsList);

		for (int i = 0; i < cards.size(); i++) {
			VKnowledgePointCard v = new VKnowledgePointCard();
			KnowledgePointCard c = cards.get(i);
			v.setId(c.getId());
			v.setName(c.getName());
			v.setKnowpointCode(c.getKnowpointCode());
			v.setH5PageUrl(paramValues == null ? null : paramValues.get(i));

			List<VKnowledgePointCard> knowledgePointCards = knowledgePointMap.get(c.getKnowpointCode()).getCards();
			if (CollectionUtils.isEmpty(knowledgePointCards)) {
				knowledgePointCards = Lists.newArrayList();
			}

			knowledgePointCards.add(v);
			knowledgePointMap.get(c.getKnowpointCode()).setCards(knowledgePointCards);
		}

		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeSystem> vsystems = knowledgeSystemConvert.to(systems);
		List<VKnowledgeSystem> knowledges = new ArrayList<VKnowledgeSystem>(systems.size() + vKnowledgePoints.size());
		knowledges.addAll(vsystems);
		knowledges.addAll(vKnowledgePoints);

		List<VKnowledgeSystem> tree = knowledgeSystemConvert.assembleTree2(knowledges);

		return new Value(tree);
	}

	/**
	 * 根据知识卡片查询卡片相关数据
	 *
	 * @param id
	 *            知识卡片id
	 * @param code
	 *            新知识点code值
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(Long id, Long code) {

		if (id == null && code == null) {
			return new Value(new IllegalArgException());
		}

		// 若传递了知识点卡片的id则取此知识卡片对应的知识点
		if (id != null && id > 0) {
			KnowledgePointCard knowledgePointCard = knowledgePointCardService.get(id);
			// 表明传递参数据不正确
			if (knowledgePointCard == null) {
				return new Value(new IllegalArgException());
			}
			code = knowledgePointCard.getKnowpointCode();
		}
		Map<String, Object> retMap = new HashMap<String, Object>(4);

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

		// 相关知识卡片
		List<VKnowledgePointCard> relativeCards = null;
		List<KnowledgePoint> knowledgePoints = knowledgePointService.findByPcode(code / 100);
		if (knowledgePoints.size() > 1) {
			List<KnowledgePointCard> cards = Lists.newArrayList();
			List<Long> codes = new ArrayList<Long>(knowledgePoints.size());
			for (KnowledgePoint kp : knowledgePoints) {
				if (!kp.getCode().equals(code)) {
					codes.add(kp.getCode());
				}
			}
			if (codes.size() > 0) {
				cards = knowledgePointCardService.findByKnowledgePoints(codes);
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
		List<VKnowledgePointCard> vcards = knowledgePointCardConvert.to(cards);

		List<Long> questionIds = new ArrayList<Long>();
		for (KnowledgePointCard c : cards) {
			if (CollectionUtils.isNotEmpty(c.getQuestions())) {
				questionIds.addAll(c.getQuestions());
			}
		}

		List<Question> questions = questionService.mgetList(questionIds);
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnalysis(true);
		option.setAnswer(true);
		option.setInitExamination(false);
		option.setInitKnowledgePoint(false);
		option.setInitMetaKnowpoint(false);
		option.setInitPhase(false);
		option.setInitSubject(false);
		option.setInitTextbookCategory(false);
		option.setInitQuestionTag(true); // 标签
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
			for (Long qId : qIds) {
				cardQuestions.add(vquestionMap.get(qId));
			}

			v.setQuestions(cardQuestions);
		}

		// 本知识点下的知识卡片列表
		retMap.put("knowledgePointCards", vcards);
		retMap.put("knowledgePoint", knowledgePointConvert.to(knowledgePointService.get(code)));

		return new Value(retMap);
	}

}
