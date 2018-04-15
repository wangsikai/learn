package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;

/**
 * 
 * @author wangsenhao
 * @since 2.0.1
 */
@Transactional(readOnly = true)
@Service
public class ResconKnowledgePointServiceImpl implements ResconKnowledgePointService {
	@Autowired
	private ResconKnowledgeSystemService knowledgeSystem;

	@Autowired
	@Qualifier("KnowledgePointRepo")
	private Repo<KnowledgePoint, Long> kpRepo;

	@Override
	public List<KnowledgePoint> findPoint(Integer phaseCode, Integer subjectCode, Long pcode, Status status) {
		Params params = Params.param();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		params.put("pcode", pcode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return kpRepo.find("$findPoint", params).list();
	}

	@Override
	public Long getPointCount(Integer phaseCode, Integer subjectCode) {
		Params params = Params.param();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		return kpRepo.find("$getPointCount", params).get(Long.class);
	}

	@Override
	public KnowledgePoint get(long code) {
		return kpRepo.get(code);
	}

	@Override
	public String getLevelDesc(long code) {
		KnowledgePoint k1 = this.get(code);
		String name1 = k1.getName();
		KnowledgeSystem k2 = knowledgeSystem.get(k1.getPcode());
		String name2 = k2.getName();
		KnowledgeSystem k3 = knowledgeSystem.get(k2.getPcode());
		String name3 = k3.getName();
		KnowledgeSystem k4 = knowledgeSystem.get(k3.getPcode());
		String name4 = k4.getName();
		String temp = name4 + '>' + name3 + '>' + name2 + '>' + name1;
		return temp;
	}

	@Override
	public List<KnowledgePoint> findAll() {
		return kpRepo.find("$findAll").list();
	}

	@Override
	public List<KnowledgePoint> findAllByStatus(Integer phaseCode, Integer subjectCode, Status status) {
		Params params = Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return kpRepo.find("$findAll", params).list();
	}

	@Override
	public List<KnowledgePoint> mgetList(List<Long> ids) {
		if (ids != null && ids.size() > 0) {
			return kpRepo.mgetList(ids);
		} else {
			return new ArrayList<KnowledgePoint>(0);
		}
	}

	@Override
	public Map<Long, KnowledgePoint> mget(Collection<Long> ids) {
		if (ids != null && ids.size() > 0) {
			return kpRepo.mget(ids);
		} else {
			return new HashMap<Long, KnowledgePoint>(0);
		}
	}

	@Override
	public List<KnowledgePoint> findAll(Integer phaseCode, Integer subjectCode, Long knowpointCode) {
		return kpRepo.find(
				"$findAll",
				Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)
						.put("knowpointCode", knowpointCode)).list();
	}

	@Override
	public String getSmallSpecialCataLog(long knowCode) {
		KnowledgeSystem k1 = knowledgeSystem.get(knowCode);
		String name1 = k1.getName();
		KnowledgeSystem k2 = knowledgeSystem.get(k1.getPcode());
		String name2 = k2.getName();
		String temp = name2 + '>' + name1;
		return temp;
	}

	@Override
	public List<KnowledgePoint> findByCode(Long code) {
		return kpRepo.find("$findByCode", Params.param("code", code + "%")).list();
	}
}
