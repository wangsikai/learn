package com.lanking.uxb.rescon.teach.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentPreview;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.rescon.basedata.convert.ResconKnowledgePointConvert;
import com.lanking.uxb.rescon.basedata.value.VKnowledgePoint;
import com.lanking.uxb.rescon.teach.value.VTeachAssistPresetContentPreview;

@Component
public class ResconTeachAssistPresetContentPreviewConvert extends
		Converter<VTeachAssistPresetContentPreview, TeachAssistPresetContentPreview, Long> {
	@Autowired
	private ResconKnowledgePointConvert knowledgePointConvert;

	@Override
	protected Long getId(TeachAssistPresetContentPreview s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistPresetContentPreview convert(TeachAssistPresetContentPreview s) {
		VTeachAssistPresetContentPreview v = new VTeachAssistPresetContentPreview();
		v.setKnowpoints(s.getKnowpoints());
		v.setPreviewQuestions(s.getPreviewQuestions());
		v.setTeachassistPresetcontentId(s.getTeachassistPresetcontentId());
		v.setSelfTestQuestions(s.getSelfTestQuestions());
		v.setCheckStatus(s.getCheckStatus());
		v.setName(s.getName());
		List<VKnowledgePoint> list = knowledgePointConvert.mgetList(s.getKnowpoints());
		List<String> vknowpoints = new ArrayList<String>();
		for (VKnowledgePoint k : list) {
			vknowpoints.add(k.getName());
		}
		v.setVknowpoints(vknowpoints);
		v.setId(s.getId());
		return v;
	}
}
