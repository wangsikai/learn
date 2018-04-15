package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticClassKnowpointServiceImpl implements DiagnosticClassKnowpointService {
	@Autowired
	@Qualifier("DiagnosticClassKnowpointRepo")
	private Repo<DiagnosticClassKnowpoint, Long> repo;

	@Override
	public List<DiagnosticClassKnowpoint> findByCodesAndClass(long classId, Collection<Long> codes) {
		return repo.find("$findByCodesAndClass", Params.param("classId", classId).put("codes", codes)).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> findWeakDatas(long classId, int limit) {
		return repo.find("$findByWeakDatas", Params.param("classId", classId).put("limit", limit)).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> findAllByWeakDatas(long classId) {
		return repo.find("$findAllByWeakDatas", Params.param("classId", classId)).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> findWeakDatas(long classId, int limit, Date bt) {
		if (bt == null) {
			return this.findWeakDatas(classId, limit);
		}
		return repo.find("$findByWeakDatas", Params.param("classId", classId).put("limit", limit).put("bt", bt)).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> smartWeakDatas(long classId, long textbookCode, int limit) {
		Params params = Params.param();
		params.put("classId", classId);
		params.put("textbookCode", textbookCode + "%");
		params.put("limit", limit);
		return repo.find("$smartWeakDatas", params).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> smartBalanceDatas(long classId, long textbookCode, Integer limit) {
		Params params = Params.param();
		params.put("classId", classId);
		params.put("textbookCode", textbookCode + "%");
		if (limit != null) {
			params.put("limit", limit);
		}
		return repo.find("$smartBalanceDatas", params).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> findByCodes(long classId, List<Long> codes) {
		Params params = Params.param("classId", classId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("codes", codes);
		}
		return repo.find("$findByCodes", params).list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<DiagnosticClassKnowpoint> findWeakPointsByCodes(List<Long> codes, long classId) {
		if (CollectionUtils.isEmpty(codes)) {
			return Collections.EMPTY_LIST;
		}

		List<DiagnosticClassKnowpoint> diaPoints = findByCodes(classId, codes);
		List<DiagnosticClassKnowpoint> weakPoints = new ArrayList<DiagnosticClassKnowpoint>(diaPoints.size());

		for (DiagnosticClassKnowpoint p : diaPoints) {
			double rate = (p.getRightCount() + 1) * 1d / (p.getDoCount() + 2);
			if (rate < 0.6) {
				weakPoints.add(p);
			}
		}

		return weakPoints;
	}

	@Override
	public List<DiagnosticClassKnowpoint> findWeakDatasByKps(long classId, List<Long> codes) {
		return repo.find("$findByWeakDatas", Params.param("classId", classId).put("kps", codes)).list();
	}

	@Override
	public List<DiagnosticClassKnowpoint> findDiagnosticDatas(long classId, int limit, Date bt) {
		return repo.find("$findDiagnosticDatas", Params.param("classId", classId).put("limit", limit).put("bt", bt))
				.list();
	}
}
