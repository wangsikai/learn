package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.uxb.rescon.basedata.api.ResconTTSHandler;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.convert.ResconTextbookConvert;
import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@Service
@Transactional(readOnly = true)
public class ResconTextbookHandler implements ResconTTSHandler {
	@Autowired
	@Qualifier("TextbookRepo")
	private Repo<Textbook, Integer> repo;
	@Autowired
	private ResconTextbookConvert convert;

	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;

	@Override
	public ResconTTSType getType() {
		return ResconTTSType.TEXTBOOK;
	}

	@Override
	@Transactional(readOnly = false)
	public void save(ResconTTSForm form) {
		Textbook textbook = new Textbook();
		if (form.getId() == null) {
			Params params = Params.param();
			params.put("phaseCode", form.getPhaseCode());
			params.put("subjectCode", form.getSubjectCode());
			params.put("categoryCode", form.getPcode());
			Textbook lastestTextbook = repo.find("$findLastestCode", params).get();
			Integer code = 0;
			if (lastestTextbook == null) {
				code = Integer.valueOf(form.getPcode().toString() + form.getPhaseCode() + form.getSubjectCode() + "01");
			} else {
				code = lastestTextbook.getCode() + 1;
			}
			textbook.setCode(code);
			textbook.setSequence(form.getSequence());
			textbook.setCategoryCode(form.getPcode().intValue());
			textbook.setPhaseCode(form.getPhaseCode());
			textbook.setSubjectCode(form.getSubjectCode());
			textbook.setStatus(Status.ENABLED);
			textbook.setIcon(form.getIcon());
		} else {
			textbook = repo.get(form.getId().intValue());
			textbook.setIcon(form.getIcon());
		}
		textbook.setName(form.getName());
		repo.save(textbook);
	}

	@Override
	public VResconTTS get(Long id) {
		Textbook textbook = repo.get(id.intValue());
		return convert.to(textbook);
	}

	@Override
	public List<VResconTTS> findAll(Map<String, Object> params) {
		Params queryParam = Params.param();
		queryParam.put("phaseCode", params.get("phaseCode"));
		queryParam.put("subjectCode", params.get("subjectCode"));
		queryParam.put("categoryCode", params.get("pcode"));
		List<Textbook> textbooks = repo.find("$find", queryParam).list();
		return convert.to(textbooks);
	}

	@Override
	public void syncData() {
		ClusterEvent<String> event = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
				BaseDataType.TEXTBOOK.name());
		sender.send(event);
	}

	@Override
	@Transactional(readOnly = false)
	public void updateSequence(ResconTTSForm form) {
		Textbook textbook = repo.get(form.getId().intValue());
		textbook.setSequence(form.getSequence());
		repo.save(textbook);
	}

	@Override
	@Transactional
	public void updateSequence(List<ResconTTSForm> forms) {
		for (ResconTTSForm form : forms) {
			Textbook textBook = repo.get(form.getId().intValue());
			textBook.setSequence(form.getSequence());
			repo.save(textBook);
		}
	}
}
