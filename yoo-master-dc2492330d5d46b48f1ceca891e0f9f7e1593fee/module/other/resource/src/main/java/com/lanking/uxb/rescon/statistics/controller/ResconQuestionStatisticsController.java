package com.lanking.uxb.rescon.statistics.controller;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.statistics.api.QuestionStatisticsManage;
import com.lanking.uxb.rescon.statistics.form.QuestionStatisForm;
import com.lanking.uxb.rescon.statistics.value.VQuestionStatis;
import com.lanking.uxb.rescon.statistics.value.VStatisKnowpoint;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;

/**
 * 题目统计相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年8月19日
 */
@RestController
@RequestMapping("rescon/statis/question")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconQuestionStatisticsController {
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private QuestionStatisticsManage questionStatisticsManage;
	@Autowired
	private KnowpointService knowPointService;

	/**
	 * 章节统计
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "getSectionStatis", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getSectionStatis(QuestionStatisForm form) {
		List<VQuestionStatis> qs = questionStatisticsManage.querySectionStatis(form);
		return new Value(qs);
	}

	/**
	 * 知识点统计
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "getKnowPoint", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowPoint(QuestionStatisForm form) {
		List<VStatisKnowpoint> list = questionStatisticsManage.queryKnowpointStatis(form);
		return new Value(list);
	}

	/**
	 * 导出章节题目统计
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportBySection")
	public void exportBySection(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QuestionStatisForm form = new QuestionStatisForm();
		String textbookCode = request.getParameter("textbookCode");
		String key = request.getParameter("key");
		String version = request.getParameter("version");
		form.setTextbookCode(Integer.parseInt(textbookCode));
		form.setKey(key);
		form.setVersion(Integer.parseInt(version));
		List<VQuestionStatis> qs = questionStatisticsManage.querySectionStatis(form);
		HSSFWorkbook wb = questionStatisticsManage.exportBySection(qs);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=sectionStat.xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}

	/**
	 * 导出知识点题目统计
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportByKnowpoint")
	public void exportByKnowpoint(HttpServletRequest request, HttpServletResponse response) throws Exception {
		QuestionStatisForm form = new QuestionStatisForm();
		String subjectCode = request.getParameter("subjectCode");
		String key = request.getParameter("key");
		int version = Integer.parseInt(request.getParameter("version"));
		form.setSubjectCode(Integer.parseInt(subjectCode));
		form.setKey(key);
		form.setVersion(version);
		List<VStatisKnowpoint> list = questionStatisticsManage.queryKnowpointStatis(form);
		HSSFWorkbook wb = questionStatisticsManage.exportByKnowpoint(list);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=knowpointStat.xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
}