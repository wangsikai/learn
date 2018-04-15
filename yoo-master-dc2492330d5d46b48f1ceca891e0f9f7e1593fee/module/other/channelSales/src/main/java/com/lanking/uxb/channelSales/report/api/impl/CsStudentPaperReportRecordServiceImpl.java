package com.lanking.uxb.channelSales.report.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportStatus;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.channelSales.report.api.CsStudentPaperReportRecordService;
import com.lanking.uxb.channelSales.report.form.StudentReportPaperForm;

/**
 * 学情纸质报告记录相关接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class CsStudentPaperReportRecordServiceImpl implements CsStudentPaperReportRecordService {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("StudentPaperReportRecordRepo")
	private Repo<StudentPaperReportRecord, Long> studentPaperReportRecordRepo;

	@Override
	public StudentPaperReportRecord get(long id) {
		return studentPaperReportRecordRepo.get(id);
	}

	@Override
	public StudentPaperReportRecord getLatestRecord(long classId) {
		return studentPaperReportRecordRepo.find("$getLatestRecord", Params.param("classId", classId)).get();
	}

	@Transactional
	@Override
	public void createStudentReportPaper(StudentReportPaperForm form, long operator) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		StudentPaperReportRecord record = new StudentPaperReportRecord();
		record.setClassId(form.getClassId());
		record.setAllStudent(form.isAllStudent());
		record.setCreateAt(now);
		try {
			Date st = format.parse(form.getStartDate() + "-01");
			Date et = format.parse(form.getEndDate() + "-01");
			Calendar cal = Calendar.getInstance();
			cal.setTime(et);
			cal.add(Calendar.MONTH, +1);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			now = format.parse(format.format(now));
			if (st.compareTo(now) >= 0) {
				throw new ChannelSalesConsoleException(
						ChannelSalesConsoleException.CHANNELSALES_STUREPORT_MONTHSELECT_ERROR);
			} else if (cal.getTime().compareTo(now) > 0) {
				cal.setTime(now);
				cal.add(Calendar.DAY_OF_YEAR, -1);
			}
			record.setStartDate(st);
			record.setEndDate(cal.getTime());
		} catch (ParseException e) {
			logger.error("createStudentReportPaper error", e);
		}
		record.setOperator(operator);
		record.setStatus(StudentPaperReportStatus.DATA_PRODUCING);
		record.setStudentIdList(
				JSONArray.toJSONString(form.getStudentIds() == null ? Lists.newArrayList() : form.getStudentIds()));
		studentPaperReportRecordRepo.save(record);
	}

	@Override
	public long countChannelNotRead(int channelCode) {
		return studentPaperReportRecordRepo.find("$countChannelNotRead", Params.param("channelCode", channelCode))
				.count();
	}

	@Override
	@Transactional
	public Page<StudentPaperReportRecord> queryRecords(int channelCode, Pageable pageable) {
		Page<StudentPaperReportRecord> page = studentPaperReportRecordRepo
				.find("$queryRecords", Params.param("channelCode", channelCode)).fetch(pageable);
		studentPaperReportRecordRepo.execute("$readRecords", Params.param("channelCode", channelCode));
		return page;
	}

	@Override
	@Transactional
	public void dowloadRecord(long recordId) {
		studentPaperReportRecordRepo.execute("$dowloadRecord", Params.param("recordId", recordId));
	}
}
