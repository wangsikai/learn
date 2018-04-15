package com.lanking.uxb.service.diagnostic.resource;

import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassStudentService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassStudentConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassStudent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 学生掌握情况Controller
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
@RestController
@RequestMapping(value = "zy/t/dia/stu")
public class ZyTeaDiagnosticStuController {

	@Autowired
	private DiagnosticClassStudentService studentService;
	@Autowired
	private DiagnosticClassStudentConvert studentConvert;

	/**
	 * 分页查询学生掌握情况列表<br/>
	 *
	 * <p>
	 * 各参数说明: day0: 0 -> 查询全部区间的列表 7 -> 最新一周的排名情况 30 -> 最近一个月的排名情况 90 ->
	 * 最近三个月的排名情况 orderBy: 0 -> 按照正确率DESC排 1 -> 按照floatRank DESC 排 2 ->
	 * 按照floatRank ASC 排 3 -> 按照homeworkCount DESC 排
	 * </p>
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            每页大小
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "10") int size,
			@RequestParam(value = "day0", defaultValue = "7") int day0,
			@RequestParam(value = "orderBy", defaultValue = "0") int order,
			@RequestParam(value = "classId") long classId) {

		Pageable pageable = P.index(page, size);

		Page<DiagnosticClassStudent> studentPage = studentService.query(pageable, day0, order, classId);

		VPage<VDiagnosticClassStudent> vPage = new VPage<VDiagnosticClassStudent>();
		List<VDiagnosticClassStudent> vs = studentConvert.to(studentPage.getItems());
		vPage.setCurrentPage(page);
		vPage.setPageSize(size);
		vPage.setItems(vs);
		vPage.setTotal(studentPage.getTotalCount());
		vPage.setTotalPage(studentPage.getPageCount());

		return new Value(vPage);
	}
}
