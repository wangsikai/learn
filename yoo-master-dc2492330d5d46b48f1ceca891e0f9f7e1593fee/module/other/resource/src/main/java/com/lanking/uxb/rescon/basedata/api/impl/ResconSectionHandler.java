package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconTTSHandler;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.convert.ResconSectionConvert;
import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogSectionManage;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconSectionHandler implements ResconTTSHandler {
	@Autowired
	@Qualifier("SectionRepo")
	private Repo<Section, Long> sectionRepo;

	@Autowired
	private ResconSectionConvert convert;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Autowired
	private ResconBookCatalogSectionManage bookCatalogSectionManage;

	@Override
	public ResconTTSType getType() {
		return ResconTTSType.SECTION;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(ResconTTSForm form) {
		Section section = new Section();
		if (form.getId() == null) {
			Params params = Params.param();
			params.put("pcode", form.getPcode());
			params.put("textbookCode", form.getPcode2());

			Section lastestSection = sectionRepo.find("$findLastestCode", params).get();
			Long code = 0L;
			if (lastestSection == null) {
				if (form.getPcode() == null || form.getPcode() == 0) {
					code = form.getPcode2() * 100 + 1;
				} else {
					code = form.getPcode() * 100 + 1;
				}
			} else {
				code = lastestSection.getCode() + 1L;
			}
			if (form.getPcode() == null || form.getPcode() == form.getPcode2()) {
				section.setPcode(0L);
			} else {
				section.setPcode(form.getPcode());
			}
			section.setTextbookCode(form.getPcode2().intValue());
			section.setCode(code);
			section.setLevel(form.getLevel());
			section.setLevel(form.getLevel() + 1);
			section.setSequence(form.getSequence());
			section.setStatus(Status.ENABLED);
		} else {
			section = sectionRepo.get(form.getId());
		}
		section.setName(form.getName());
		sectionRepo.save(section);

		// 添加的章节父级，需要删除书本目录对应关系
		if (section.getPcode() != null) {
			bookCatalogSectionManage.deleteCatalogRelationBySection(section.getTextbookCode(), section.getPcode());
		}
	}

	@Override
	public VResconTTS get(Long id) {
		Section section = sectionRepo.get(id);
		return convert.to(section);
	}

	@Override
	public List<VResconTTS> findAll(Map<String, Object> params) {
		Long pcode = Long.valueOf(params.get("pcode").toString());
		Params queryParam = Params.param();
		Integer level = (Integer) params.get("level");
		if (level >= 1) {
			queryParam.put("pcode", pcode);
		} else {
			queryParam.put("textbookCode", pcode);
		}
		queryParam.put("level", ++level);
		List<Section> sections = sectionRepo.find("$findByOrTextbookCode", queryParam).list();
		List<VResconTTS> vs = convert.to(sections);

		return vs;
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.SECTION.name());
		sender.send(event);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateSequence(ResconTTSForm form) {
		Section section = sectionRepo.get(form.getId());
		section.setSequence(form.getSequence());
		sectionRepo.save(section);
	}

	@Override
	@Transactional
	public void updateSequence(List<ResconTTSForm> forms) {
		for (ResconTTSForm form : forms) {
			Section section = sectionRepo.get(form.getId());
			section.setSequence(form.getSequence());
			sectionRepo.save(section);
		}
	}
}
