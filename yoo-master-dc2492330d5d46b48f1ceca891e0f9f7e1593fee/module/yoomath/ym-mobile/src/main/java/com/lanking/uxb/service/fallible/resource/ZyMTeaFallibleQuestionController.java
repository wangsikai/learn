package com.lanking.uxb.service.fallible.resource;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.fallible.cache.TeacherFallibleCacheService;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.DifficultyType;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.OrderType;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.UpdateRange;
import com.lanking.uxb.service.fallible.value.VTeaFalliblePage;
import com.lanking.uxb.service.fallible.value.VTeaFallibleTextbook;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyTeacherFallibleQuestionConvert;

/**
 * 教师端的错题本相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月4日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/fallible")
public class ZyMTeaFallibleQuestionController extends ZyMBaseController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyTeacherFallibleQuestionService tfqService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private ZyTeacherFallibleQuestionConvert tfqConvert;
	@Autowired
	private SearchService searchService;
	@Autowired
	private TeacherFallibleCacheService teacherFallibleCacheService;
	@Autowired
	private IndexService indexService;

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryByTextBook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByTextBook(Integer textbookCode) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> data = new HashMap<String, Object>(3);
		long fallibleCount = 0;
		// 教材列表每次都返回
		List<VTextbook> textbooks = tbConvert.to(
				tbService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(), teacher.getSubjectCode()));
		List<VTeaFallibleTextbook> teaFallibleTextbooks = new ArrayList<VTeaFallibleTextbook>(textbooks.size());
		if (teacher.getTextbookCategoryCode() != null) {
			Map<Integer, Integer> fallCountMap = tfqService.statisTextbookFallible(teacher.getId(),
					teacher.getTextbookCategoryCode());
			if (fallCountMap.size() > 0) {
				for (VTextbook v : textbooks) {
					if (fallCountMap.containsKey(v.getCode())) {
						fallibleCount += fallCountMap.get(v.getCode());
						teaFallibleTextbooks.add(new VTeaFallibleTextbook(v, fallCountMap.get(v.getCode())));
					}
				}
			} else {
				for (VTextbook v : textbooks) {
					teaFallibleTextbooks.add(new VTeaFallibleTextbook(v));
				}
			}
		} else {
			for (VTextbook v : textbooks) {
				teaFallibleTextbooks.add(new VTeaFallibleTextbook(v));
			}
		}
		data.put("fallibleTextbooks", teaFallibleTextbooks);
		data.put("fallibleCount", fallibleCount);

		// textbookCode为空的时候表示第一次请求
		if (textbookCode == null) {
			if (teaFallibleTextbooks.size() > 0 && teacher.getTextbookCategoryCode() != null) {
				int latestTextbook = teacherFallibleCacheService.getTextbookCurrent(Security.getUserId(),
						teacher.getTextbookCategoryCode());
				boolean useCache = false;
				for (VTeaFallibleTextbook v : teaFallibleTextbooks) {
					if (v.getTextbook().getCode() == latestTextbook) {
						useCache = true;
						break;
					}
				}
				if (useCache) {
					textbookCode = latestTextbook;
				} else {
					textbookCode = teaFallibleTextbooks.get(0).getTextbook().getCode();
				}
			} else {
				textbookCode = textbooks.get(0).getCode();
			}
		}

		data.put("textbookCode", textbookCode);

		// 展示有错题的章节,通过fallibleCount识别
		List<Section> sectionList = sectionService.findByTextbookCode(textbookCode);
		// 查询错题本中section数据
		Map<Long, Long> sectionFallList = tfqService.querySectionCode(Security.getUserId(), textbookCode);
		// 过滤数据
		List<VSection> vsections = sectionConvert.to(sectionList);
		for (VSection v : vsections) {
			if (sectionFallList.containsKey(v.getCode())) {
				v.setFallibleCount(sectionFallList.get(v.getCode()));
			}
		}

		data.put("sections", sectionConvert.assemblySectionTree(vsections));
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		qb.must(QueryBuilders.termQuery("teacherId", Security.getUserId()));
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes",
				Lists.newArrayList(textbookCode)));
		qb.must(QueryBuilders.rangeQuery("rightRate").lte(BigDecimal.valueOf(50).doubleValue()));
		data.put("total", searchService.count(IndexType.TEACHER_FALLIBLE_QUESTION, qb));
		return new Value(data);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryByFilter", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByFilter(TeaFallibleFilterForm form) {
		if (form.getOrderType() == null || form.getPageNo() == null || form.getPageNo() < 1
				|| form.getTextbookCode() == null || form.getTextbookCode() < 0) {
			return new Value(new IllegalArgException());
		}
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 封装查询条件
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		query.setTeacherId(Security.getUserId());
		query.setTextbookCode(form.getTextbookCode());
		if (form.getOrderType() == OrderType.RIGHT_RATE_ASC) {
			query.setIsRightRateDesc(false);
		} else if (form.getOrderType() == OrderType.RIGHT_RATE_DESC) {
			query.setIsRightRateDesc(true);
		} else if (form.getOrderType() == OrderType.UPDATE_TIME_ASC) {
			query.setIsUpdateAtDesc(false);
		} else if (form.getOrderType() == OrderType.UPDATE_TIME_DESC) {
			query.setIsUpdateAtDesc(true);
		}
		if (form.getSectionCode() != null) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCode());
			List<Long> childrenCodes = sectionService.findSectionChildren(form.getSectionCode());
			if (CollectionUtils.isNotEmpty(childrenCodes)) {
				sectionCodes.addAll(childrenCodes);
			}
			query.setSectionCodes(sectionCodes);
		}
		if (form.getMinRightRate() != null) {
			query.setLeRightRate(BigDecimal.valueOf(form.getMinRightRate()));
			if (form.getMinRightRate().intValue() == 30) {
				query.setRateleftOpen(false);
			} else if (form.getMinRightRate().intValue() == 40) {
				query.setRateleftOpen(false);
			}
		}
		if (form.getMaxRightRate() != null) {
			query.setReRightRate(BigDecimal.valueOf(form.getMaxRightRate()));
			if (form.getMaxRightRate().intValue() == 30) {
				query.setRaterightOpen(true);
			} else if (form.getMaxRightRate().intValue() == 40) {
				query.setRaterightOpen(true);
			} else if (form.getMaxRightRate().intValue() == 50) {
				query.setRaterightOpen(false);
			}
		} else {
			query.setReRightRate(BigDecimal.valueOf(50));
			query.setRaterightOpen(false);
		}
		if (form.getUpdateRange() != null) {
			if (form.getUpdateRange() == UpdateRange.A_WEEK) {
				query.setTimeRange(1);
			} else if (form.getUpdateRange() == UpdateRange.A_MONTH) {
				query.setTimeRange(2);
			}
		}
		if (form.getQuestionType() != null) {
			List<Long> typeList = new ArrayList<Long>();
			if (teacher.getSubjectCode() == SubjectService.PHASE_2_MATH) {
				if (form.getQuestionType() == Type.SINGLE_CHOICE) {
					typeList.add(20201L);
				} else if (form.getQuestionType() == Type.MULTIPLE_CHOICE) {
					typeList.add(20206L);
				} else if (form.getQuestionType() == Type.FILL_BLANK) {
					typeList.add(20203L);
				} else if (form.getQuestionType() == Type.TRUE_OR_FALSE) {
					typeList.add(20207L);
				} else if (form.getQuestionType() == Type.QUESTION_ANSWERING) {
					typeList.add(20202L);
					typeList.add(20204L);
					typeList.add(20205L);
				}
			} else if (teacher.getSubjectCode() == SubjectService.PHASE_3_MATH) {
				if (form.getQuestionType() == Type.SINGLE_CHOICE) {
					typeList.add(30201L);
				} else if (form.getQuestionType() == Type.MULTIPLE_CHOICE) {
					typeList.add(30206L);
				} else if (form.getQuestionType() == Type.FILL_BLANK) {
					typeList.add(30203L);
				} else if (form.getQuestionType() == Type.TRUE_OR_FALSE) {
					typeList.add(30207L);
				} else if (form.getQuestionType() == Type.QUESTION_ANSWERING) {
					typeList.add(30202L);
					typeList.add(30204L);
					typeList.add(30205L);
				}
			}
			query.setTypeCodes(typeList);
		}
		if (form.getDifficultyType() != null) {
			if (form.getDifficultyType() == DifficultyType.BASIS) {
				query.setLeDifficulty(BigDecimal.valueOf(0.8));
				query.setReDifficulty(BigDecimal.valueOf(1));
				query.setLeftOpen(false);
				query.setRightOpen(false);
			} else if (form.getDifficultyType() == DifficultyType.IMPROVE) {
				query.setLeDifficulty(BigDecimal.valueOf(0.4));
				query.setReDifficulty(BigDecimal.valueOf(0.8));
				query.setLeftOpen(false);
				query.setRightOpen(true);
			} else if (form.getDifficultyType() == DifficultyType.HARD) {
				query.setReDifficulty(BigDecimal.valueOf(0.4));
				query.setRightOpen(true);
			}
		}
		Long endTime = form.getEndTime() == null ? System.currentTimeMillis() : form.getEndTime();
		Page<TeacherFallibleQuestion> page = tfqService.queryFaliableQuestion3(teacher.getSubjectCode(), query,
				form.getPageNo(), 20);
		VTeaFalliblePage vpage = new VTeaFalliblePage();
		vpage.setPageSize(20);
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(tfqConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPageNo());
		vpage.setEndTime(endTime);
		return new Value(vpage);
	}

	/**
	 * 删除错题
	 * 
	 * @since 2.1.0
	 * @param id
	 *            错题ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(long id) {
		try {
			indexService.syncDelete(IndexType.TEACHER_FALLIBLE_QUESTION, id);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ignore) {
			}
			tfqService.deleteFailQuestion(id, Security.getUserId());
		} catch (AbstractException e) {
			indexService.syncUpdate(IndexType.TEACHER_FALLIBLE_QUESTION, id);
			return new Value(new YoomathMobileException(e.getCode()));
		}
		return new Value();
	}

}
