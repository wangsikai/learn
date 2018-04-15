package com.lanking.uxb.zycon.qs.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.qs.api.ZycQuestionSchoolService;
import com.lanking.uxb.zycon.qs.convert.ZycQuestionSchoolConverter;
import com.lanking.uxb.zycon.qs.convert.ZycSchoolConverter;
import com.lanking.uxb.zycon.qs.value.VZycQuestionSchool;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@RestController
@RequestMapping(value = "zyc/qs")
public class ZycQuestionSchoolController {

	@Autowired
	private ZycQuestionSchoolService questionSchoolService;
	@Autowired
	private ZycQuestionSchoolConverter questionSchoolConverter;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ZycSchoolConverter schoolConvert;

	/**
	 * 分页获取校本数据
	 *
	 * @param size
	 *            每页数据
	 * @param page
	 *            查询第几页
	 * @param name
	 *            查询名称
	 * @return {@link VZycQuestionSchool}
	 */
	@RequestMapping(value = "page", method = { RequestMethod.GET, RequestMethod.POST })
	public Value page(@RequestParam(value = "size", defaultValue = "20") int size,
			@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "name", required = false) String name) {
		Pageable pageable = P.index(page, size);
		Page<QuestionSchool> p = questionSchoolService.page(pageable, name);
		List<VZycQuestionSchool> vs = questionSchoolConverter.to(p.getItems());
		VPage<VZycQuestionSchool> vPage = new VPage<VZycQuestionSchool>();
		vPage.setTotal(p.getTotalCount());
		vPage.setPageSize(size);
		vPage.setCurrentPage(page);
		vPage.setTotalPage(p.getPageCount());
		vPage.setItems(vs);

		return new Value(vPage);
	}

	/**
	 * 更新学校的状态 {@link Status}
	 *
	 * @param schoolId
	 *            学校的id
	 * @param status
	 *            状态
	 * @return {@link VZycQuestionSchool}
	 */
	@RequestMapping(value = "update_status", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(@RequestParam(value = "schoolId") long schoolId,
			@RequestParam(value = "status") Status status) {
		QuestionSchool school = questionSchoolService.update(schoolId, status);
		if (null == school) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.QUESTION_SCHOOL_NOT_EXISTS));
		}
		return new Value(questionSchoolConverter.to(school));
	}

	/**
	 * 创建校本学校
	 *
	 * @param schoolId
	 *            school id
	 * @return {@link QuestionSchool}
	 */
	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(@RequestParam(value = "schoolId") long schoolId) {
		QuestionSchool school = questionSchoolService.create(schoolId);
		if (school == null) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.QUESTION_SCHOOL_ADD_EXISTS));
		}
		return new Value(questionSchoolConverter.to(school));
	}

	/**
	 * 添加学校时进行学校查询
	 *
	 * @param name
	 *            学校名
	 * @return {@link VSchool}
	 */
	@RequestMapping(value = "search_school_by_name", method = { RequestMethod.GET, RequestMethod.POST })
	public Value searchSchoolByName(@RequestParam(value = "name", required = true) String name) {
		if (StringUtils.isBlank(name))
			return new Value(new YoomathConsoleException(YoomathConsoleException.QUESTION_SCHOOL_ADD_NAME_IS_NULL));

		List<School> schools = schoolService.getSchoolByNameLike(name);
		return new Value(schoolConvert.to(schools));
	}

	/**
	 * 更新校本学校可以免费录入的题目数量
	 * 
	 * @param schoolId
	 * @param count
	 *            页面设置的免费可以录入的题目数量
	 * @return
	 */
	@RequestMapping(value = "updateRecordQuestionCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateRecordQuestionCount(Long schoolId, Long count) {
		questionSchoolService.updateRecordQuestionCount(schoolId, count);
		return new Value();
	}
}
