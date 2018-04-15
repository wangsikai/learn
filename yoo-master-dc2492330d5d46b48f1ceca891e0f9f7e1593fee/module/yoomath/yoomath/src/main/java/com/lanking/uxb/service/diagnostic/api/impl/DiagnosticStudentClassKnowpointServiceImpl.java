package com.lanking.uxb.service.diagnostic.api.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;

/**
 * 知识掌握情况接口
 * 
 * @author wangsenhao
 *
 */
@Service
@Transactional(readOnly = true)
public class DiagnosticStudentClassKnowpointServiceImpl implements DiagnosticStudentClassKnowpointService {

	@Autowired
	@Qualifier("DiagnosticStudentClassKnowpointRepo")
	private Repo<DiagnosticStudentClassKnowpoint, Long> repo;

	@Override
	public List<DiagnosticStudentClassKnowpoint> queryListByLevel(Integer level, Long studentId, Long classId) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		if (level != null) {
			if (level == 1) {
				// 专题
				params.put("length", 5);
			} else if (level == 2) {
				// 小专题
				params.put("length", 7);
			} else if (level == 3) {
				// 知识专项
				params.put("length", 8);
			}
		}
		return repo.find("$queryListByLevel", params).list();
	}

	@Override
	public List<DiagnosticStudentClassKnowpoint> queryknowListBySpecial(Long pcode, Long studentId, Long classId) {
		Params params = Params.param("classId", classId).put("studentId", studentId);
		if (pcode != null) {
			params.put("pcode", pcode);
			// 说明是知识专项,第三层
			if (pcode.toString().length() == 8) {
				params.put("level", 3);
			}
			// 说明是小专题,第二层
			if (pcode.toString().length() == 7) {
				params.put("level", 2);
			}
		}

		return repo.find("$queryknowListBySpecial", params).list();
	}

	@Override
	public List<DiagnosticStudentClassKnowpoint> queryHistoryWeakList(Long studentId, Long classId, Integer day0) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		if (day0 != null && day0 > 0) {
			params.put("endTime", new Date());
			params.put("startTime", getTimeByDay0(day0));
		}
		return repo.find("$queryWeakListBySpecial", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getClassAvgRightRateByPcode(Long pcode, Long classId) {
		Params params = Params.param("pcode", pcode).put("classId", classId);
		// 说明是知识专项,第三层
		if (pcode.toString().length() == 8) {
			params.put("level", 3);
		}
		// 说明是小专题,第二层
		if (pcode.toString().length() == 7) {
			params.put("level", 2);
		}
		return repo.find("$getClassAvgRightRateByPcode", params).list(Map.class);
	}

	@Override
	public List<DiagnosticStudentClassKnowpoint> querySamllTopicList(Collection<Long> codes, Long studentId,
			Long classId) {
		Params params = Params.param("codes", codes).put("classId", classId).put("studentId", studentId);
		return repo.find("$querySamllTopicList", params).list();
	}

	@Override
	public List<Map> querySmallTopicClassRateList(Collection<Long> codes, Long classId) {
		Params params = Params.param("codes", codes).put("classId", classId);
		return repo.find("$querySmallTopicClassRateList", params).list(Map.class);
	}

	@Override
	public List<DiagnosticStudentClassKnowpoint> queryByKnowledge(long code, long studentId) {
		Params params = Params.param("code", code).put("studentId", studentId);

		return repo.find("$queryByKnowledge", params).list();
	}

	@Override
	public DiagnosticStudentClassKnowpoint get(Long code, Long studentId, Long classId) {
		Params params = Params.param("code", code).put("classId", classId).put("studentId", studentId);
		List<DiagnosticStudentClassKnowpoint> list = repo.find("$getClassKp", params).list();
		// 临时处理，现发现正式环境diagno_stu_class_kp存在同一毫秒级保存的重复数据,处理完数据恢复原来方式
		if (CollectionUtils.isNotEmpty(list)) {
			return list.get(0);
		}
		return null;
	}

	public Date getTimeByDay0(Integer day0) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -day0);
		return calendar.getTime();
	}

	@Override
	public List<DiagnosticStudentClassKnowpoint> queryHistoryWeakListByCodes(Long studentId, Long classId,
			Collection<Long> codes) {
		Params params = Params.param("studentId", studentId).put("classId", classId);
		if (CollectionUtils.isNotEmpty(codes)) {
			params.put("codes", codes);
		}
		return repo.find("$queryWeakListBySpecial", params).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getSectionDoCountMap(List<Long> sectionCodes, long studentId, Long classId) {
		Params params = Params.param("sectionCodes", sectionCodes).put("studentId", studentId);
		if (classId != null) {
			params.put("classId", classId);
		}
		List<Map> list = repo.find("$getSectionDoCountMap", params).list(Map.class);
		return list;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getKpDataBySectioncodes(List<Long> sectionCodes, long studentId, Long classId) {
		Params params = Params.param("sectionCodes", sectionCodes).put("studentId", studentId);
		if (classId != null) {
			params.put("classId", classId);
		}
		List<Map> list = repo.find("$getKpDataBySectioncodes", params).list(Map.class);
		return list;
	}
}
