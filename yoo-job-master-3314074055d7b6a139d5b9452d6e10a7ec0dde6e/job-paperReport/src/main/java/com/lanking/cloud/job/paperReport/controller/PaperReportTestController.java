package com.lanking.cloud.job.paperReport.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordFileService;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordService;
import com.lanking.cloud.sdk.value.Value;

/**
 * 测试使用，测试通过删除.
 * 
 * @author wlche
 *
 */
@RestController
@RequestMapping(value = "pr/test")
public class PaperReportTestController {

	@Autowired
	private StudentPaperReportRecordService recordService;

	@Autowired
	private StudentPaperReportRecordFileService recordFileService;

	@RequestMapping(value = "data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value data() {
		recordService.productData();
		return new Value();
	}

	@RequestMapping(value = "file", method = { RequestMethod.GET, RequestMethod.POST })
	public Value file() {
		recordFileService.productFile();
		return new Value();
	}
}
