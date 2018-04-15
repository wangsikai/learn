package com.lanking.uxb.service.examPaper.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTextbookService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassKnowpointConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassKnowpoint;
import com.lanking.uxb.service.examPaper.api.SmartPaperService;
import com.lanking.uxb.service.examPaper.form.SmartExamPaperForm;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * 智能组卷
 * 
 * @since yoomath 2.0.6
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/t/ep")
public class ZyTeaSmartExamPaperController {

	@Autowired
	private DiagnosticClassKnowpointService diagnosticKpService;
	@Autowired
	private DiagnosticClassKnowpointConvert diagnosticKpConvert;
	@Autowired
	private KnowledgeSectionService knowSectionService;
	@Autowired
	private KnowledgePointService kpService;
	@Autowired
	private SmartPaperService smartPaperService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private DiagnosticClassTextbookService classTextbookService;
	@Autowired
	private QuestionConvert questionConvert;

	/**
	 * 查询可以智能组卷的班级列表
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "smart/queryClassList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClassList() {
		List<HomeworkClazz> clazzs = homeworkClassService.listCurrentClazzs(Security.getUserId());
		if (clazzs == null || clazzs.size() <= 0) {
			return new Value();
		}
		return new Value(homeworkClazzConvert.to(clazzs, new ZyHomeworkClassConvertOption(false, true, false)));
	}

	/**
	 * 查询智能组卷有数据的教材
	 * 
	 * @param classId
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "smart/querySmartTextbookList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySmartTextbookList(Long classId) {
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		if (teacher.getTextbookCategoryCode() == null) {
			throw new IllegalArgException();
		}
		List<VTextbook> list = textbookConvert.mgetList(classTextbookService.getClassTextbooks(classId,
				teacher.getTextbookCategoryCode()));
		return new Value(list);
	}

	/**
	 * 根据三种不同的考察方式查询对应的知识点列表
	 * 
	 * @param form
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "smart/queryKnowledgeList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryKnowledgeList(SmartExamPaperForm form) {
		List<DiagnosticClassKnowpoint> list = new ArrayList<DiagnosticClassKnowpoint>();
		// 薄弱知识点
		if (form.getTestMode() == 1) {
			list = diagnosticKpService.smartWeakDatas(form.getClassId(), form.getTextbookCode(), 6);
		} else if (form.getTestMode() == 2) {
			// 平衡性训练，取已练数量最少的6个知识点，且练习数大于0
			list = diagnosticKpService.smartBalanceDatas(form.getClassId(), form.getTextbookCode(), 6);
		} else if (form.getTestMode() == 3) {
			// 自定义，所有当前教材下知识点
			// 先查询出当前教材下对应的知识点
			List<Long> allList = knowSectionService.getByTextbook(form.getTextbookCode());
			List<VDiagnosticClassKnowpoint> newKpList = new ArrayList<VDiagnosticClassKnowpoint>();
			if (CollectionUtils.isNotEmpty(allList)) {
				Map<Long, KnowledgePoint> pointMap = kpService.mget(allList);
				// 做过的知识点集合
				List<DiagnosticClassKnowpoint> doKpList = diagnosticKpService.smartBalanceDatas(form.getClassId(),
						form.getTextbookCode(), null);
				List<VDiagnosticClassKnowpoint> vDoKpList = diagnosticKpConvert.to(doKpList);
				Map<Long, VDiagnosticClassKnowpoint> map = new HashMap<Long, VDiagnosticClassKnowpoint>();
				for (VDiagnosticClassKnowpoint v : vDoKpList) {
					map.put(v.getKnowledgeCode(), v);
				}
				for (Long code : allList) {
					if (map.get(code) != null) {
						newKpList.add(map.get(code));
					} else {
						if (pointMap.get(code) != null) {
							VDiagnosticClassKnowpoint v = new VDiagnosticClassKnowpoint();
							v.setDoCount(0);
							v.setKnowledgeCode(code);
							v.setKnowpointName(pointMap.get(code).getName());
							newKpList.add(v);
						}
					}
				}
			}
			return new Value(newKpList);
		}
		return new Value(diagnosticKpConvert.to(list));
	}

	/**
	 * 拉取题目
	 * 
	 * @param form
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "smart/findQuestionList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findQuestionList(SmartExamPaperForm form) {
		Map<String, Object> data = new HashMap<String, Object>();
		/**
		 * 当用户选择的数量实际大于符合条件数据库存在题目数时给出提示
		 */
		if (form.getChoiceNum() > 0) {
			form.setQuestionType(Question.Type.SINGLE_CHOICE.getValue());
			form.setSize(form.getChoiceNum());
			List<Long> choiceIds = smartPaperService.query(form);
			if (choiceIds.size() < form.getChoiceNum()) {
				data.put("realChoiceSize", choiceIds.size());
				return new Value(data);
			}
		}
		if (form.getFillBlankNum() > 0) {
			form.setQuestionType(Question.Type.FILL_BLANK.getValue());
			form.setSize(form.getFillBlankNum());
			List<Long> fillBlankIds = smartPaperService.query(form);
			if (fillBlankIds.size() < form.getFillBlankNum()) {
				data.put("realFillSize", fillBlankIds.size());
				return new Value(data);
			}
		}
		if (form.getAnswerNum() > 0) {
			form.setQuestionType(Question.Type.QUESTION_ANSWERING.getValue());
			form.setSize(form.getAnswerNum());
			List<Long> qaIds = smartPaperService.query(form);
			if (qaIds.size() < form.getAnswerNum()) {
				data.put("realAnswerSize", qaIds.size());
				return new Value(data);
			}
		}
		List<Long> ids = smartPaperService.queryQuestionsByIndex(form);
		return new Value(questionConvert.mgetList(ids));
	}
}
