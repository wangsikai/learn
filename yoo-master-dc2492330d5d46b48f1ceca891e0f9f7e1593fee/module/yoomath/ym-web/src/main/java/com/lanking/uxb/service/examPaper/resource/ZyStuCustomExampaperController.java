package com.lanking.uxb.service.examPaper.resource;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStudent;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.CustomExampaperStudentService;
import com.lanking.uxb.service.examPaper.cache.CustomExampaperStudentNoticeCacheService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperStudentConvert;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperStudentConvertOption;
import com.lanking.uxb.service.examPaper.ex.CustomExampaperException;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuery;
import com.lanking.uxb.service.examPaper.form.CustomExampaperStudentQuestionStatisticForm;
import com.lanking.uxb.service.examPaper.value.VCustomExampaperStudent;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 学生组卷服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月12日
 */
@RestController
@RequestMapping(value = "zy/s/ep")
public class ZyStuCustomExampaperController {

	@Autowired
	private CustomExampaperStudentNoticeCacheService customExampaperStudentNoticeCacheService;
	@Autowired
	private CustomExampaperStudentService customExampaperStudentService;
	@Autowired
	private CustomExampaperStudentConvert customExampaperStudentConvert;

	/**
	 * 列表首页.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "listIndex", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listIndex() {
		return new Value();
	}

	/**
	 * 获得NEW提醒.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getNotice() {
		boolean needNotice = false;
		long studentId = Security.getUserId();
		Long readTimestamp = customExampaperStudentNoticeCacheService.getTimestamp(studentId);
		if (readTimestamp != null) {
			needNotice = customExampaperStudentService.countNewDatas(studentId, readTimestamp) > 0;
		}
		return new Value(needNotice ? 1 : 0);
	}

	/**
	 * 查询学生组卷列表.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(CustomExampaperStudentQuery query) {
		try {
			long studentId = Security.getUserId();
			query.setStudentId(studentId);
			Page<CustomExampaperStudent> page = customExampaperStudentService.query(query,
					P.offset((query.getPage() - 1) * query.getPageSize(), query.getPageSize()));

			// 读取标记.
			Long readTimestamp = customExampaperStudentNoticeCacheService.getTimestamp(studentId);

			CustomExampaperStudentConvertOption option = new CustomExampaperStudentConvertOption();
			option.setHasCustomExampaper(true);
			option.setHasClazz(true);
			List<VCustomExampaperStudent> vos = customExampaperStudentConvert.to(page.getItems(), option);
			if (readTimestamp != null) {
				for (VCustomExampaperStudent vo : vos) {
					vo.setRead(readTimestamp > vo.getCreateAt().getTime());
				}
			}

			VPage<VCustomExampaperStudent> vpage = new VPage<VCustomExampaperStudent>();
			vpage.setCurrentPage(query.getPage());
			vpage.setItems(vos);
			vpage.setPageSize(query.getPageSize());
			vpage.setTotal(page.getTotalCount());
			vpage.setTotalPage(page.getPageCount());

			// 清除读取标记
			customExampaperStudentNoticeCacheService.clear(studentId);
			return new Value(vpage);
		} catch (CustomExampaperException e) {
			return new Value(e);
		}
	}

	/**
	 * 获得组卷详情.
	 * 
	 * @param id
	 *            学生组卷ID.
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "detail", method = { RequestMethod.GET, RequestMethod.POST })
	public Value detail(Long id) {
		if (id == null) {
			return new Value(new MissingArgumentException());
		}
		try {
			CustomExampaperStudent customExampaperStudent = customExampaperStudentService.get(id);
			CustomExampaperStudentConvertOption option = new CustomExampaperStudentConvertOption();
			option.setHasCustomExampaper(true);
			option.setHasClazz(true);
			option.setHasQuestion(true);
			VCustomExampaperStudent vo = customExampaperStudentConvert.to(customExampaperStudent, option);
			return new Value(vo);
		} catch (CustomExampaperException e) {
			return new Value(e);
		}
	}

	/**
	 * 提交学生组卷统计.
	 * 
	 * @param customExampaperStudentId
	 *            学生组卷ID
	 * @param statisticst
	 *            统计结果
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "submitStatistic", method = { RequestMethod.GET, RequestMethod.POST })
	public Value submitStatistic(Long customExampaperStudentId, String statistics) {
		if (customExampaperStudentId == null || StringUtils.isBlank(statistics)) {
			return new Value(new MissingArgumentException());
		}
		try {
			List<CustomExampaperStudentQuestionStatisticForm> forms = JSON.parseArray(statistics,
					CustomExampaperStudentQuestionStatisticForm.class);

			// 提交统计
			customExampaperStudentService.submitStatistic(customExampaperStudentId, forms);
			return new Value();
		} catch (CustomExampaperException e) {
			return new Value(e);
		}

	}
}
