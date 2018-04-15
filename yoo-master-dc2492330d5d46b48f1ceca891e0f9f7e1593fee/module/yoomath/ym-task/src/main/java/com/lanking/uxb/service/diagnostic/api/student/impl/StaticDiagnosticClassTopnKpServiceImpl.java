package com.lanking.uxb.service.diagnostic.api.student.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassStudent;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTopnKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.diagnostic.api.student.StaticDiagnosticClassTopnKpService;

@Transactional(readOnly = true)
@Service
public class StaticDiagnosticClassTopnKpServiceImpl implements StaticDiagnosticClassTopnKpService {

	@Autowired
	@Qualifier("DiagnosticClassStudentRepo")
	private Repo<DiagnosticClassStudent, Long> diaClassStuRepo;

	@Autowired
	@Qualifier("DiagnosticStudentClassKnowpointRepo")
	private Repo<DiagnosticStudentClassKnowpoint, Long> stuClassKpRepo;

	@Autowired
	@Qualifier("DiagnosticClassTopnKnowpointRepo")
	private Repo<DiagnosticClassTopnKnowpoint, Long> stuTopnKpRepo;

	@Transactional
	@SuppressWarnings("rawtypes")
	@Override
	public void staticTopnStu(Long classId) {

		stuTopnKpRepo.execute("$deleteTopn", Params.param("classId", classId));

		List<Long> topStudentIds = diaClassStuRepo.find("$ymFindTopnStudent", Params.param("classId", classId)).list(
				Long.class);

		if (CollectionUtils.isEmpty(topStudentIds)) {
			return;
		}

		List<Map> topnRightRates = stuClassKpRepo.find("$ymTopnStu",
				Params.param("classId", classId).put("studentIds", topStudentIds)).list(Map.class);

		for (Map m : topnRightRates) {
			Long code = Long.parseLong(m.get("knowpoint_code").toString());
			DiagnosticClassTopnKnowpoint kp = stuTopnKpRepo.find("$ymFindByClassAndKp",
					Params.param("classId", classId).put("code", code)).get();

			if (kp == null) {
				kp = new DiagnosticClassTopnKnowpoint();
				kp.setCreateAt(new Date());
				kp.setKnowpointCode(code);
				kp.setClassId(classId);
			}

			Long doCount = ((BigDecimal) m.get("do_count")).longValue();
			Long rightCount = ((BigDecimal) m.get("right_count")).longValue();

			BigDecimal rightRate = new BigDecimal(rightCount * 100d / doCount).setScale(0, BigDecimal.ROUND_HALF_UP);

			kp.setDoCount(doCount.intValue());
			kp.setRightCount(rightCount.intValue());
			kp.setRightRate(rightRate);

			stuTopnKpRepo.save(kp);
		}

	}
}
