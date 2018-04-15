package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.fallible.TeacherFallibleQuestion;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyTeacherFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyTeacherFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.form.TeaFallibleQuestionForm;
import com.lanking.uxb.service.zuoye.value.VExerciseSection;
import com.lanking.uxb.service.zuoye.value.VTeacherFallibleQuestion;

/**
 * 悠作业老师错题本接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/fallible")
public class ZyTeaFallibleQuestionController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyTeacherFallibleQuestionService tfqService;
	@Autowired
	private ZyTeacherFallibleQuestionConvert tfqConvert;

	/**
	 * 首页数据
	 * 
	 * @since 2.1
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@SuppressWarnings("deprecation")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		if (textbookCode == null) {
			Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
			List<VTextbookCategory> categories = tbcConvert.to(tbcService.getAll());
			data.put("textbookCategories", categories);
			List<VTextbook> textbooks = tbConvert.to(tbService.find(teacher.getPhaseCode(), null,
					teacher.getSubjectCode()));
			data.put("textbooks", textbooks);
			for (VTextbook v : textbooks) {
				if (v.getCategoryCode() == categories.get(0).getCode()) {
					textbookCode = v.getCode();
					break;
				}
			}
		}
		if (textbookCode == null) {
			data.put("sections", new ArrayList<VExerciseSection>(0));
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = tfqService.staticFallibleCount(Security.getUserId(), textbookCode);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setFallibleCount(countMap.get(v.getCode()));
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
			data.put("total", total);
		}
		return new Value(data);
	}

	/**
	 * 查询错题列表
	 * 
	 * @since 2.1
	 * @param form
	 *            查询条件
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(TeaFallibleQuestionForm form) {
		ZyTeacherFallibleQuestionQuery query = new ZyTeacherFallibleQuestionQuery();
		query.setTeacherId(Security.getUserId());
		query.setLeRightRate(form.getLeRightRate());
		query.setReRightRate(form.getReRightRate());
		query.setLeDifficulty(form.getLeDifficulty());
		query.setReDifficulty(form.getReDifficulty());
		query.setTextbookCode(form.getTextbookCode());
		query.setIsRightRateDesc(form.getIsRightRateDesc());
		if (CollectionUtils.isNotEmpty(form.getSectionCodes())) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCodes());
			if (sectionCodes.size() == 1) {
				query.setSectionCode(sectionCodes.iterator().next());
			} else {
				query.setSectionCodes(sectionCodes);
			}
		}
		query.setIsCreateAtDesc(form.getIsCreateAtDesc());
		query.setIsUpdateAtDesc(form.getIsUpdateAtDesc());
		query.setIsDifficultyDesc(form.getIsDifficultyDesc());
		query.setTimeRange(form.getTimeRange());

		Page<TeacherFallibleQuestion> page = tfqService.query(query, P.index(form.getPageNo(), form.getSize()));
		VPage<VTeacherFallibleQuestion> vpage = new VPage<VTeacherFallibleQuestion>();
		vpage.setPageSize(form.getSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(tfqConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPageNo());
		return new Value(vpage);
	}
}
