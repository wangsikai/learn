package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;

@Transactional(readOnly = true)
@Service
public class ResconKnowledgeSystemServiceImpl implements ResconKnowledgeSystemService {

	@Autowired
	@Qualifier("KnowledgeSystemRepo")
	private Repo<KnowledgeSystem, Long> knowledgeSystemRepo;

	@Override
	public List<KnowledgeSystem> findAll(Integer phaseCode, Integer subjectCode, Long pcode, Integer level) {
		Params params = Params.param();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		params.put("pcode", pcode);
		if (level != null) {
			params.put("lv", ++level);
		}
		return knowledgeSystemRepo.find("$findAll", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Integer, Long> getStat(Integer phaseCode, Integer subjectCode) {
		Params params = Params.param();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		List<Map> list = knowledgeSystemRepo.find("$getStat", params).list(Map.class);
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map pa : list) {
				map.put(Integer.parseInt(pa.get("level").toString()), Long.parseLong(pa.get("count").toString()));
			}
		}
		return map;
	}

	@Override
	public KnowledgeSystem get(long code) {
		return knowledgeSystemRepo.get(code);
	}

	@Override
	public List<KnowledgeSystem> findSmallSpecial(Integer phaseCode, Integer subjectCode, Long knowpointCode) {
		Params params = Params.param();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		params.put("knowpointCode", knowpointCode);
		return knowledgeSystemRepo.find("$findSmallSpecial", params).list();
	}

	@Override
	public List<KnowledgeSystem> findByCode(Long code) {
		return knowledgeSystemRepo.find("$findByCode", Params.param("code", code + "%")).list();
	}
}
