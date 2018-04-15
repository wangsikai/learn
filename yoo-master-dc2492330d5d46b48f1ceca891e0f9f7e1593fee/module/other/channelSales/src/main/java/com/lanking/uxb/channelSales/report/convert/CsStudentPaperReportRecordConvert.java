package com.lanking.uxb.channelSales.report.convert;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.stat.StudentPaperReportRecord;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.channelSales.report.value.VStudentPaperReportRecord;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

@Component
public class CsStudentPaperReportRecordConvert
		extends Converter<VStudentPaperReportRecord, StudentPaperReportRecord, Long> {

	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;

	@Override
	protected Long getId(StudentPaperReportRecord s) {
		return s.getId();
	}

	@Override
	protected VStudentPaperReportRecord convert(StudentPaperReportRecord s) {
		if (s == null) {
			return null;
		}
		VStudentPaperReportRecord v = new VStudentPaperReportRecord();
		v.setId(s.getId());
		v.setClassId(s.getClassId());
		v.setCreateAt(s.getCreateAt());
		v.setDownloadCount(s.getDownloadCount());
		v.setEndDate(s.getEndDate());
		v.setOperator(s.getOperator());
		v.setStartDate(s.getStartDate());
		v.setStatus(s.getStatus());
		v.setAllStudent(s.isAllStudent());
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		v.setSingleMonth(format.format(s.getStartDate()).equals(format.format(s.getEndDate())));
		return v;
	}

	@Override
	protected void initAssemblers(@SuppressWarnings("rawtypes") List<ConverterAssembler> assemblers) {
		ZyHomeworkClassConvertOption option = new ZyHomeworkClassConvertOption();
		option.setInitTeacher(true);

		// 转换学校名称
		assemblers.add(
				new ConverterAssembler<VStudentPaperReportRecord, StudentPaperReportRecord, Long, VHomeworkClazz>() {

					@Override
					public boolean accept(StudentPaperReportRecord s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentPaperReportRecord s, VStudentPaperReportRecord d) {
						return s.getClassId();
					}

					@Override
					public void setValue(StudentPaperReportRecord s, VStudentPaperReportRecord d,
							VHomeworkClazz value) {
						d.setHomeworkClazz(value);
					}

					@Override
					public VHomeworkClazz getValue(Long key) {
						return homeworkClazzConvert.to(homeworkClassService.get(key), option);
					}

					@Override
					public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
						Map<Long, VHomeworkClazz> homeworkClazzMap = homeworkClazzConvert
								.to(homeworkClassService.mget(keys), option);
						return homeworkClazzMap;
					}
				});
	}
}
