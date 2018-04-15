package com.lanking.cloud.job;

import org.springframework.beans.factory.annotation.Autowired;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordFileService;
import com.lanking.cloud.job.paperReport.service.StudentPaperReportRecordService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PaperReportJob implements SimpleJob {

	@Autowired
	private StudentPaperReportRecordService recordService;
	@Autowired
	private StudentPaperReportRecordFileService recordFileService;

	@Override
	public void execute(ShardingContext shardingContext) {
		log.info("开始");
		int productSize = recordService.productData();
		while (productSize > 0) {
			recordFileService.productFile();
			productSize = recordService.productData();
		}
		log.info("结束");
	}

}
