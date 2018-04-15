package com.lanking.uxb.rescon.exam.controller;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.rescon.exam.api.ResconExamManage;
import com.lanking.uxb.rescon.exam.api.ResconExamPaperStatisticService;
import com.lanking.uxb.rescon.exam.convert.ResconExamConvert;
import com.lanking.uxb.rescon.exam.convert.ResconExamOption;
import com.lanking.uxb.rescon.exam.form.QueryForm;
import com.lanking.uxb.rescon.exam.value.VExam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 试卷统计Controller
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@RestController
@RequestMapping(value = "rescon/ep/statistic")
public class ResconExamPaperStatisticController {
	@Autowired
	private ResconExamPaperStatisticService statisticService;
	@Autowired
	private ResconExamManage resconExamManage;
	@Autowired
	private ResconExamConvert examConvert;

	/**
	 * 统计初中及高中的试卷数量
	 *
	 * 例: { "初中": { "语文": 0, "数学": 2, .... }, "高中": { "语文": 0, "数学": 20, .... }
	 * }
	 *
	 * @return 数据格式如上
	 */
	@RequestMapping(value = "count", method = { RequestMethod.GET, RequestMethod.POST })
	public Value count() {
		return new Value(statisticService.countStatistic());
	}

	/**
	 * 查询试卷列表
	 *
	 * @param queryForm
	 *            {@link QueryForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(QueryForm queryForm) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		if (queryForm.getNeedCount()) {
			retMap.put("statistic", statisticService.countBySubject(queryForm.getSubjectCode()));
		}
		Page<ExamPaper> page = resconExamManage.queryResconExam(queryForm);
		VPage<VExam> vpage = new VPage<VExam>();
		vpage.setPageSize(queryForm.getPageSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(examConvert.to(page.getItems(), new ResconExamOption(false, false, false, true)));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(queryForm.getPage());

		retMap.put("page", vpage);
		return new Value(retMap);
	}
}
