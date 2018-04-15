package com.lanking.uxb.service.youngyedu.fallible.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.fallible.form.StuFallibleFilterForm;
import com.lanking.uxb.service.fallible.value.VStuFallibleTextbook;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.resource.ZyStuFallibleQuestion2Controller;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestion2Form;

/**
 * 融捷错题本相关接口
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
@RestController
@RequestMapping(value = "router/youngyedu/ym/s/fallible")
public class ZyMYoungyeduStuFallibleQuestionController {
	@Autowired
	private ZyStuFallibleQuestion2Controller zyStuFallibleQuestion2Controller;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private SectionConvert sectionConvert;

	/**
	 * 获取查询错题本数据
	 *
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "filterConditions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value filterConditions() {
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Integer categoryCode = null;
		Teacher teacher = null;
		Student student = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			for (HomeworkStudentClazz c : clazzs) {
				Long teacherId = zyHkClassService.get(c.getClassId()).getTeacherId();
				if (teacherId == null) {
					continue;
				}
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
				if (teacher != null) {
					categoryCode = teacher.getTextbookCategoryCode();
					break;
				}
			}

			if (teacher == null || categoryCode == null) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_NOCATEGORYCODE));
			}

		} else {
			student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
			categoryCode = student.getTextbookCategoryCode();
		}

		Map<String, Object> retMap = new HashMap<String, Object>(5);
		List<Map> res = sfqService.getStudentFallTSCount(Security.getUserId(), categoryCode);

		// 处理不同教材下的错题等
		if (res == null || res.size() == 0) {
			retMap.put("TEXTBOOK", Collections.EMPTY_LIST);
		} else {
			Map<Long, Long> sections = new HashMap<Long, Long>(res.size());
			Set<Integer> textbookCodes = new HashSet<Integer>(res.size());
			Set<Long> sectionCodes = new HashSet<Long>(res.size());
			Map<Integer, List<Long>> textbookSections = new HashMap<Integer, List<Long>>(res.size());

			for (Map m : res) {
				Integer textbookCode = ((BigInteger) m.get("textbook_code")).intValue();
				Long sectionCode = ((BigInteger) m.get("section_code")).longValue();
				Long count = ((BigInteger) m.get("amount")).longValue();

				Long firstLevelSectionCode = Long.parseLong(sectionCode.toString().substring(0, 10));
				textbookCodes.add(textbookCode);

				Long regionSectionCount = sections.get(firstLevelSectionCode) == null ? 0L
						: sections.get(firstLevelSectionCode);

				regionSectionCount += count;
				sections.put(firstLevelSectionCode, regionSectionCount);

				sectionCodes.add(firstLevelSectionCode);
				sectionCodes.add(sectionCode);
				sections.put(sectionCode, count);

				List<Long> textbookSectionCodes = textbookSections.get(textbookCode);
				if (CollectionUtils.isEmpty(textbookSectionCodes)) {
					textbookSectionCodes = Lists.newArrayList();
				}

				if (!textbookSectionCodes.contains(firstLevelSectionCode)) {
					textbookSectionCodes.add(firstLevelSectionCode);
				}
				textbookSections.put(textbookCode, textbookSectionCodes);
			}

			List<Textbook> textbookList = textbookService.mgetList(textbookCodes);
			List<Section> sectionList = sectionService.mgetList(sectionCodes);
			List<VSection> vSections = sectionConvert.to(sectionList);
			for (VSection v : vSections) {
				v.setFallibleCount(sections.get(v.getCode()));
			}
			List<VSection> treeSection = sectionConvert.assemblySectionTree(vSections);
			Map<Long, VSection> sectionMap = new HashMap<Long, VSection>(treeSection.size());
			for (VSection v : treeSection) {
				sectionMap.put(v.getCode(), v);
			}

			List<VStuFallibleTextbook> vts = new ArrayList<VStuFallibleTextbook>(textbookList.size());

			for (Textbook t : textbookList) {
				VStuFallibleTextbook v = new VStuFallibleTextbook();
				v.setCode(t.getCode());
				v.setName(t.getName());

				List<VSection> children = new ArrayList<VSection>(textbookSections.get(t.getCode()).size());

				long count = 0;
				for (Long sCode : textbookSections.get(t.getCode())) {
					VSection vSection = sectionMap.get(sCode);
					count += vSection.getFallibleCount();

					children.add(vSection);
				}

				v.setCount(count);
				v.setSections(children);

				vts.add(v);
			}

			retMap.put("TEXTBOOK", vts);
		}

		List<Long> allList = new ArrayList<Long>(4);
		Date endDate = new Date();
		Date month1Date = DateUtils.addDays(endDate, -30);
		Date month2Date = DateUtils.addDays(endDate, -30 * 3);
		Date month3Date = DateUtils.addDays(endDate, -30 * 6);

		Long allCount = sfqService.countByDate(Security.getUserId(), categoryCode, null, null);
		Long month1Count = sfqService.countByDate(Security.getUserId(), categoryCode, month1Date, endDate);
		Long month2Count = sfqService.countByDate(Security.getUserId(), categoryCode, month2Date, endDate);
		Long month3Count = sfqService.countByDate(Security.getUserId(), categoryCode, month3Date, endDate);

		allList.add(allCount == null ? 0 : allCount);
		allList.add(month1Count == null ? 0 : month1Count);
		allList.add(month2Count == null ? 0 : month2Count);
		allList.add(month3Count == null ? 0 : month3Count);

		retMap.put("ALL", allList);

		retMap.put("OCR", sfqService.countOcr(Security.getUserId(), categoryCode));
		retMap.put("OTHER", sfqService.countOther(Security.getUserId(), categoryCode));
		retMap.put("categoryCode", categoryCode);

		return new Value(retMap);
	}

	/**
	 * 查询学生错题本数据
	 *
	 * @param form
	 *            {@link StuFallibleFilterForm}
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @param key
	 *            查询关键字
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(StuFallibleFilterForm form, @RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "8") int size, String key) {
		if (StringUtils.isBlank(form.getType())) {
			return new Value(new IllegalArgException());
		}
		StuFallibleQuestion2Form queryForm = new StuFallibleQuestion2Form();
		queryForm.setPage(page);
		queryForm.setPageSize(size);
		queryForm.setKey(key);
		queryForm.setNewKeyQuery(true);
		queryForm.setUserId(Security.getUserId());
		switch (form.getType()) {
		case "ALL":
			queryForm.setOther(false);
			break;
		case "OCR":
			queryForm.setOther(false);
			queryForm.setSource(StudentQuestionAnswerSource.OCR.getValue());
			break;
		case "OTHER":
			queryForm.setOther(true);
			List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			Integer categoryCode = null;
			Teacher teacher = null;
			Student student = null;
			if (CollectionUtils.isNotEmpty(clazzs)) {
				for (HomeworkStudentClazz c : clazzs) {
					Long teacherId = zyHkClassService.get(c.getClassId()).getTeacherId();
					if (teacherId == null) {
						continue;
					}
					teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
					if (teacher != null) {
						categoryCode = teacher.getTextbookCategoryCode();
						break;
					}
				}

				if (teacher == null || categoryCode == null) {
					return new Value(
							new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_NOCATEGORYCODE));
				}

			} else {
				student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
				categoryCode = student.getTextbookCategoryCode();
			}

			queryForm.setCategoryCode(categoryCode);
			break;
		case "TEXTBOOK":
			queryForm.setOther(false);
			if (form.getSectionCode() != null && form.getSectionCode() > 0) {
				List<Long> sectionCodes = new ArrayList<Long>(1);
				sectionCodes.add(form.getSectionCode());
				queryForm.setSectionCodes(sectionCodes);
			}
			if (form.getTextbookCode() != null && form.getTextbookCode() > 0) {
				queryForm.setTextbookCode(form.getTextbookCode());
			}
			break;
		}
		queryForm.setOrderByCreateAt(false);

		return zyStuFallibleQuestion2Controller.query(queryForm);
	}
}
