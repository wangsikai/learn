package com.lanking.uxb.service.code.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.TextbookService;

@Transactional(readOnly = true)
@Service
@ConditionalOnExpression("!${common.code.cache}")
public class TextbookServiceImpl implements TextbookService {
	@Autowired
	@Qualifier("TextbookRepo")
	Repo<Textbook, Integer> tbRepo;

	@Override
	public Textbook get(int code) {
		return tbRepo.get(code);
	}

	@Override
	public List<Textbook> getAll() {
		return tbRepo.find("$findAll").list();
	}

	@Override
	public Map<Integer, Textbook> mget(Collection<Integer> codes) {
		return tbRepo.mget(codes);
	}

	@Override
	public List<Textbook> mgetList(Collection<Integer> codes) {
		return tbRepo.mgetList(codes);
	}

	@Override
	public List<Textbook> find(int phaseCode, Integer categoryCode, Integer subjectCode) {
		Params params = Params.param("phaseCode", phaseCode);
		if (categoryCode == null) {
			params.put("categoryCode", categoryCode);
		}
		if (subjectCode == null) {
			params.put("subjectCode", subjectCode);
		}
		return tbRepo.find("$find", params).list();
	}

	@Override
	public List<Textbook> find(int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes) {
		Params params = Params.param("phaseCode", phaseCode);
		if (CollectionUtils.isEmpty(categoryCodes)) {
			categoryCodes = new ArrayList<Integer>(1);
			categoryCodes.add(-1);
		}
		params.put("categoryCodes", categoryCodes);
		if (subjectCode == null) {
			params.put("subjectCode", subjectCode);
		}
		return tbRepo.find("$find", params).list();
	}

	@Override
	public List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Collection<Integer> categoryCodes) {
		Params params = Params.param("phaseCode", phaseCode).put("categoryCodes", categoryCodes).put("subjectCode",
				subjectCode);
		if (product == Product.YOOMATH) {
			params.put("yoomathStatus", "1");
		}
		return tbRepo.find("$find", params).list();
	}

	@Override
	public List<Textbook> find(Product product, int phaseCode, Integer subjectCode, Integer categoryCode) {
		Params params = Params.param("phaseCode", phaseCode).put("categoryCode", categoryCode).put("subjectCode",
				subjectCode);
		if (product == Product.YOOMATH) {
			params.put("yoomathStatus", "1");
		}
		return tbRepo.find("$find", params).list();
	}
}
