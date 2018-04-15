package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleExportRecord;

@Component
public class ZyStudentFallibleExportRecordConvert extends
		Converter<VStudentFallibleExportRecord, StudentFallibleExportRecord, Long> {

	@Override
	protected Long getId(StudentFallibleExportRecord s) {
		return s.getId();
	}

	@Override
	protected VStudentFallibleExportRecord convert(StudentFallibleExportRecord s) {
		if (s == null) {
			return null;
		}
		VStudentFallibleExportRecord vo = new VStudentFallibleExportRecord();
		vo.setId(s.getId());
		vo.setHash(s.getHash());
		vo.setBuy(s.getBuy() == null ? false : s.getBuy().booleanValue());
		vo.setCount(s.getCount() == null ? 0 : s.getCount().intValue());
		vo.setCreatAt(s.getCreatAt());
		vo.setExtend(s.getExtend());
		vo.setName(s.getName());
		vo.setSellPrice(s.getSellPrice() == null ? 0 : s.getSellPrice().intValue());
		vo.setSellPriceRMB(s.getSellPriceRMB());
		vo.setSize(s.getSize());
		vo.setStatus(s.getStatus());
		vo.setStudentId(s.getStudentId());
		vo.setFallibleQuestionPrintOrderId(s.getFallibleQuestionPrintOrderId());
		return vo;
	}
}
