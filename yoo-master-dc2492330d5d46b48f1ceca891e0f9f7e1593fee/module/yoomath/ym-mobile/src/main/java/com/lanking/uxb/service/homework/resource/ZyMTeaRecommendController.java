package com.lanking.uxb.service.homework.resource;

import java.util.Collections;
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
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.common.resource.question.QuestionSimilar;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.recommend.api.QuestionTeaRecommendService;
import com.lanking.uxb.service.recommend.form.ResourceRecommendForm;
import com.lanking.uxb.service.recommend.type.RecommendType;
import com.lanking.uxb.service.recommend.value.VRecommend;
import com.lanking.uxb.service.resources.api.QuestionSimilarService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;

/**
 * 教师端推荐相关接口
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月21日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/recommend")
public class ZyMTeaRecommendController {

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyQuestionCarService questionCarService;
	@Autowired
	private QuestionSimilarService questionSimilarService;
	@Autowired
	private TeacherOperationCacheService operationCacheService;
	@Autowired
	private QuestionTeaRecommendService questionTeaRecommendService;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private TeacherOperationCacheService teacherOperationCacheService;
	@Autowired
	private KnowledgePointService kpService;
	@Autowired
	private KnowledgePointConvert kpConvert;
	@Autowired
	private SectionService sectionService;

	/**
	 * 推荐题目首页
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(Integer textbookCode, Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 防止取到旧缓存
		if (textbookCode != null && sectionCode != null) {
			operationCacheService.setProgress(Security.getUserId(), sectionCode, TeacherOperationCacheService.PROGRESS);
			operationCacheService.setProgress(Security.getUserId(), Long.valueOf(textbookCode),
					TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		}

		// 取用户设置，没有的话全部推荐
		// 第一次进入推荐页可以通过下面的缓存来判断
		List<Integer> sources = operationCacheService.getRecommendSource(Security.getUserId());
		List<Integer> allSources = Lists.newArrayList();
		allSources.add(RecommendType.HOT_QUESTION.getValue());
		allSources.add(RecommendType.COMMON_BOOK.getValue());
		allSources.add(RecommendType.WEAK_KNOWLEDGEPOINT.getValue());
		allSources.add(RecommendType.EXAMINATION_POINT.getValue());
		allSources.add(RecommendType.COLLECTION_QUESTION.getValue());
		allSources.add(RecommendType.FALLIBLE_QUESTION.getValue());
		if (CollectionUtils.isEmpty(sources)) {
			// 记录缓存
			operationCacheService.setRecommendSource(Security.getUserId(), allSources);

			data.put("sources", getSelectRecommend(allSources));
		} else {
			data.put("sources", getSelectRecommend(sources));
		}

		List<Question> questions = questionTeaRecommendService.getQuestionsFromCache(Security.getUserId());

		QuestionConvertOption questionOption = new QuestionConvertOption();
		questionOption.setInitPhase(false);
		questionOption.setInitSub(false);
		questionOption.setInitSubject(false);
		questionOption.setInitTextbookCategory(false);
		questionOption.setInitQuestionType(false);
		questionOption.setInitMetaKnowpoint(false);
		questionOption.setInitStudentQuestionCount(false);
		questionOption.setCollect(false);

		questionOption.setInitPublishCount(true); // 布置作业次数
		questionOption.setInitQuestionSimilarCount(true); // 相似题个数
		questionOption.setInitQuestionTag(true); // 标签
		questionOption.setInitKnowledgePoint(true); // 知识点
		questionOption.setInitExamination(true); // 考点
		questionOption.setAnalysis(true); // 解析
		questionOption.setAnswer(true); // 答案

		List<VQuestion> vquestions = questionConvert.to(questions, questionOption);
		// 设置题目是否被加入作业篮子
		List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
		for (VQuestion q : vquestions) {
			if (q.getType() != Type.COMPOSITE) {
				q.setInQuestionCar(carQuestions.contains(q.getId()));
			}
		}
		data.put("questions", vquestions);

		return new Value(data);
	}

	/**
	 * 组装recommend
	 * 
	 * @param sources
	 */
	private List<VRecommend> getSelectRecommend(List<Integer> sources) {
		List<VRecommend> vList = Lists.newArrayList();
		for (int i = 0; i < 6; i++) {
			VRecommend v = new VRecommend();
			v.setRecommendType(RecommendType.findByValue(i).name());
			vList.add(v);
		}

		for (VRecommend v : vList) {
			for (Integer source : sources) {
				if (v.getRecommendType().equals(RecommendType.findByValue(source).name())) {
					v.setSelected(true);
				}
			}
		}

		return vList;
	}

	/**
	 * 相似题
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "similarQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value similarQuestions(Long id) {
		// 校验
		if (id == null) {
			throw new IllegalArgException();
		}
		Map<String, Object> data = new HashMap<String, Object>();

		QuestionSimilar questionSimilar = questionSimilarService.getByQuestion(id);
		if (questionSimilar != null) {
			List<Long> ids = questionSimilar.getLikeQuestions();
			QuestionConvertOption option = new QuestionConvertOption(false, true, true, true, null);
			option.setInitPublishCount(true);
			option.setInitExamination(true);
			option.setInitQuestionTag(true); // 标签
			Map<Long, Question> questions = questionService.mget(ids);
			List<Question> questionList = Lists.newArrayList();
			for (long qid : ids) {
				if (id == qid) {
					// 去掉本题
					continue;
				}
				Question question = questions.get(qid);
				if (question.getType() != Question.Type.SINGLE_CHOICE && question.getType() != Question.Type.FILL_BLANK
						&& question.getType() != Question.Type.QUESTION_ANSWERING) {
					// 仅保留单选、填空、解答题
					continue;
				}
				questionList.add(question);
			}

			List<VQuestion> vList = questionConvert.to(questionList, option);

			List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
			for (VQuestion q : vList) {
				if (q.getType() != Type.COMPOSITE) {
					q.setInQuestionCar(carQuestions.contains(q.getId()));
				}
			}
			data.put("items", vList);
		} else {
			data.put("items", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}

	/**
	 * 选择推荐来源
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "chooseSource", method = { RequestMethod.POST, RequestMethod.GET })
	public Value chooseSource(String sources) {
		if (sources == null) {
			throw new IllegalArgException();
		}
		String[] split = sources.split(",");
		List<Integer> resourceList = Lists.newArrayList();
		for (int i = 0; i < split.length; i++) {
			resourceList.add(RecommendType.valueOf(split[i]).getValue());
		}
		// 记录缓存
		operationCacheService.setRecommendSource(Security.getUserId(), resourceList);

		// 刷新题目
		questionTeaRecommendService.refreshNewQuestions(Security.getUserId(), null);

		return new Value();
	}

	/**
	 * 切换题目
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "changeQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value changeQuestions(ResourceRecommendForm form) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Question> questions = questionTeaRecommendService.refreshNewQuestions(Security.getUserId(), form);

		QuestionConvertOption option = new QuestionConvertOption(false, true, true, true, null);
		option.setInitPublishCount(true);
		option.setInitQuestionSimilarCount(true);
		option.setInitExamination(true);
		option.setInitQuestionTag(true); // 标签

		List<VQuestion> vquestions = questionConvert.to(questions, option);
		// 设置题目是否被加入作业篮子
		List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
		for (VQuestion q : vquestions) {
			if (q.getType() != Type.COMPOSITE) {
				q.setInQuestionCar(carQuestions.contains(q.getId()));
			}
		}
		data.put("questions", vquestions);

		return new Value(data);
	}

	/**
	 * 获取老师章节下的知识点列表
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getKps", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getKps() {
		// 获取教师当前设置的进度
		Long sectionCode = teacherOperationCacheService.getProgress(Security.getUserId(),
				TeacherOperationCacheService.PROGRESS);
		Map<String, Object> data = new HashMap<String, Object>();
		if (sectionCode == null) {
			return new Value(data);
		}
		// 判断当前章节是否是本章综合练习
		String name = sectionService.get(sectionCode).getName();
		if (name.equals("本章综合与测试")) {
			String sc = sectionCode.toString().substring(0, sectionCode.toString().length() - 2);
			sectionCode = Long.parseLong(sc);
		}
		List<Long> kpCodes = knowledgeSectionService.getBySection(sectionCode);
		if (CollectionUtils.isNotEmpty(kpCodes)) {
			data.put("kpList", kpConvert.to(kpService.mgetList(kpCodes)));
		}
		return new Value(data);
	}

}
