package com.lanking.uxb.service.fallible.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.DifficultyType;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.UpdateRange;
import com.lanking.uxb.service.fallible.value.VTeaFalliblePage;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyTeacherFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VTeacherFallibleQuestion;

/**
 * 教师端的错题本V2
 * 
 * @since 2.0.3
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月10日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/fallible/2")
public class ZyMTeaFallibleQuestionController2 extends ZyMBaseController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyTeacherFallibleQuestionService tfqService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyTeacherFallibleQuestionConvert tfqConvert;

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "queryByFilter", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByFilter2(TeaFallibleFilterForm form) {
		if (form.getCursor() == null || form.getTextbookCode() == null || form.getTextbookCode() < 0) {
			return new Value(new IllegalArgException());
		}
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		// 封装查询条件
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		query.setTeacherId(Security.getUserId());
		query.setTextbookCode(form.getTextbookCode());
		query.setIsUpdateAtDesc(true);
		if (form.getSectionCode() != null) {
			// 教师端v1.3.0 当前进度为“本章综合测试”时，好题本展示的是“该大章下所有的好题”
			Section integrateSection = sectionService.findIntegrateSectionCode(form.getSectionCode());
			if (integrateSection != null) {
				form.setSectionCode(integrateSection.getPcode());
			}

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
		// 游标
		query.setCursor(form.getCursor() == 0 ? Long.MAX_VALUE : form.getCursor());

		Page<TeacherFallibleQuestion> page = tfqService.queryFaliableQuestion4(teacher.getSubjectCode(), query, 20);
		VTeaFalliblePage vpage = new VTeaFalliblePage();
		vpage.setPageSize(20);
		long nextCursor = 0l;
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
			nextCursor = System.currentTimeMillis();
		} else {
			List<VTeacherFallibleQuestion> items = tfqConvert.to(page.getItems());

			nextCursor = items.get(items.size() - 1).getUpdateAt().getTime();
			vpage.setItems(items);
		}

		// 错题总数需要重新查询
		query.setCursor(Long.MAX_VALUE);
		Page<TeacherFallibleQuestion> totalPage = tfqService.queryFaliableQuestion4(teacher.getSubjectCode(), query,
				20);
		vpage.setTotal(totalPage.getTotalCount());
		// 游标
		vpage.setCursor(nextCursor - 1);

		return new Value(vpage);
	}
}
