package com.lanking.uxb.service.user.resource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.DifficultyType;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;
import com.lanking.uxb.service.zuoye.convert.ZyQuestionCollectionConvert3;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VQuestionCollection;
import com.lanking.uxb.service.zuoye.value.VTeaCollectionTextbook;

/**
 * 教师好题本相关接口
 * 
 * @since yoomath(mobile) V1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月7日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/qcollection")
public class ZyMTeaQuestionCollectionController {

	@Autowired
	private ZyQuestionCollectionService zyQuestionCollectionService;
	@Autowired
	private ZyQuestionCollectionConvert3 zyQuestionCollectConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	protected TextbookService tbService;
	@Autowired
	protected TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SearchService searchService;

	/**
	 * 收藏题目
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "collect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value collect(long questionId) {
		zyQuestionCollectionService.collect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 取消收藏题目
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "cancelCollect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cancelCollect(long questionId) {
		zyQuestionCollectionService.cancelCollect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 切换教材
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryByTextBook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByTextBook(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		long collectionCount = 0;

		// 教材列表
		List<VTextbook> textbooks = tbConvert.to(
				tbService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(), teacher.getSubjectCode()));
		List<VTeaCollectionTextbook> teaCollectTextbooks = new ArrayList<>();
		if (teacher.getTextbookCategoryCode() != null) {
			Map<Integer, Integer> collectCountMap = zyQuestionCollectionService
					.statisTextbookCollect(teacher.getId(), teacher.getTextbookCategoryCode());
			if (collectCountMap.size() > 0) {
				for (VTextbook v : textbooks) {
					if (collectCountMap.containsKey(v.getCode())) {
						collectionCount += collectCountMap.get(v.getCode());
						teaCollectTextbooks.add(new VTeaCollectionTextbook(v, collectCountMap.get(v.getCode())));
					}
				}
			} else {
				for (VTextbook v : textbooks) {
					teaCollectTextbooks.add(new VTeaCollectionTextbook(v));
				}
			}
		} else {
			for (VTextbook v : textbooks) {
				teaCollectTextbooks.add(new VTeaCollectionTextbook(v));
			}
		}
		data.put("collectionTextbooks", teaCollectTextbooks);
		data.put("collectionCount", collectionCount);

		// 不传值取最后一条收藏题目的textbookCode
		if (textbookCode == null) {
			Map<String, BigInteger> codeMap = zyQuestionCollectionService.getLastTextbookCode(Security.getUserId(),
					teacher.getTextbookCategoryCode());
			if (codeMap == null) {
				textbookCode = textbooks.get(0).getCode();
			} else {
				textbookCode = codeMap.get("textbook_code").intValue();
			}

		}
		data.put("textbookCode", textbookCode);

		// 展示有错题的章节,通过collectCount识别
		List<Section> sectionList = sectionService.findByTextbookCode(textbookCode);
		List<VSection> vsections = sectionConvert.to(sectionList);
		// 查询错题本中section数据
		Map<Long, Long> sectionCollectList = zyQuestionCollectionService.querySectionCode(Security.getUserId(),
				textbookCode);
		// 过滤数据
		for (VSection v : vsections) {
			if (sectionCollectList.containsKey(v.getCode())) {
				v.setCollectCount(sectionCollectList.get(v.getCode()));
			}
		}

		data.put("sections", sectionConvert.assemblySectionTree(vsections));

		// 当前教材下收藏数量
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("teacherId", Security.getUserId()));
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
				Lists.newArrayList(textbookCode)));
		data.put("total", searchService.count(IndexType.USER_QUESTION_COLLECT, qb));
		return new Value(data);
	}

	/**
	 * 查询收藏列表
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * 
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(String questionType, long cursor, @RequestParam(value = "size", defaultValue = "20") int size,
			DifficultyType difficultyType, Integer textbookCode, Long sectionCode) {
		VCursorPage<VQuestionCollection> vp = new VCursorPage<VQuestionCollection>();
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());

		QuestionQueryForm query = new QuestionQueryForm();
		query.setUserId(Security.getUserId());
		// 题型 选择 填空 解答
		if (questionType != null) {
			Set<Long> typeList = Sets.newHashSet();
			if (teacher.getSubjectCode() == SubjectService.PHASE_2_MATH) {
				if ("CHOICE".equals(questionType)) {
					typeList.add(20201L);
					typeList.add(20206L);
				} else if (Type.FILL_BLANK.name().equals(questionType)) {
					typeList.add(20203L);
				} else if (Type.QUESTION_ANSWERING.name().equals(questionType)) {
					typeList.add(20202L);
					typeList.add(20204L);
					typeList.add(20205L);
				}
			} else if (teacher.getSubjectCode() == SubjectService.PHASE_3_MATH) {
				if ("CHOICE".equals(questionType)) {
					typeList.add(30201L);
					typeList.add(30206L);
				} else if (Type.FILL_BLANK.name().equals(questionType)) {
					typeList.add(30203L);
				} else if (Type.QUESTION_ANSWERING.name().equals(questionType)) {
					typeList.add(30202L);
					typeList.add(30204L);
					typeList.add(30205L);
				}
			}
			query.setTypeCodes(typeList);
		}
		// 难度
		if (difficultyType != null) {
			if (difficultyType == DifficultyType.BASIS) {
				query.setLeDifficulty(BigDecimal.valueOf(0.8));
				query.setReDifficulty(BigDecimal.valueOf(1));
				query.setLeftOpen(false);
				query.setRightOpen(false);
			} else if (difficultyType == DifficultyType.IMPROVE) {
				query.setLeDifficulty(BigDecimal.valueOf(0.4));
				query.setReDifficulty(BigDecimal.valueOf(0.8));
				query.setLeftOpen(false);
				query.setRightOpen(true);
			} else if (difficultyType == DifficultyType.HARD) {
				query.setReDifficulty(BigDecimal.valueOf(0.4));
				query.setRightOpen(true);
			}
		}
		// 章节
		query.setTextbookCode(textbookCode);
		if (sectionCode != null) {
			// 教师端v1.3.0 当前进度为“本章综合测试”时，好题本展示的是“该大章下所有的好题”
			Section integrateSection = sectionService.findIntegrateSectionCode(sectionCode);
			if (integrateSection != null) {
				sectionCode = integrateSection.getPcode();
			}

			List<Long> sectionCodes = new ArrayList<>();
			sectionCodes.add(sectionCode);
			List<Long> childrenCodes = sectionService.findSectionChildren(sectionCode);
			if (CollectionUtils.isNotEmpty(childrenCodes)) {
				sectionCodes.addAll(childrenCodes);
			}
			query.setSectionCodes(sectionCodes);
		}

		query.setSearchInSection(true);

		CursorPage<Long, QuestionCollection> collectPage = zyQuestionCollectionService.queryCollection2(query,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(size, 20)));
		if (collectPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			vp.setCursor(collectPage.getNextCursor());
			List<VQuestionCollection> vqList = zyQuestionCollectConvert.to(collectPage.getItems());
			vp.setItems(vqList);
			long total = zyQuestionCollectionService.queryCollectionCount(query);
			vp.setTotal(total);
		}
		return new Value(vp);
	}
}
