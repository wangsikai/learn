package com.lanking.uxb.service.diagnostic.api.teacher.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.diagnostic.Diagnostic;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClass;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.diagnostic.api.teacher.TaskDiagnosticService;

/**
 * 诊断-全国数据服务接口实现.
 * 
 * @author wlche
 *
 */
@Service
@Transactional(readOnly = true)
public class TaskDiagnosticServiceImpl implements TaskDiagnosticService {

	@Autowired
	@Qualifier("DiagnosticClassRepo")
	private Repo<DiagnosticClass, Long> diaClassRepo;

	@Autowired
	@Qualifier("DiagnosticRepo")
	private Repo<Diagnostic, Long> diaRepo;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public void doDiagnostic() {

		// 获取全国平均数据
		List<Map> datas = diaClassRepo.find("$ymAvg", Params.param()).list(Map.class);

		// 所有教材
		Set<Integer> textbookCodes = new HashSet<Integer>();
		for (Map m : datas) {
			Integer textbookCode = Integer.parseInt(m.get("textbook_code").toString());
			textbookCodes.add(textbookCode);
		}

		if (textbookCodes.size() > 0) {
			// 批量获取当前教材的全国统计数数据
			List<Diagnostic> diagnostics = diaRepo.find("$ymGets", Params.param("textbookCodes", textbookCodes)).list();
			Map<Integer, Diagnostic> diagnosticMap = new HashMap<Integer, Diagnostic>(diagnostics.size());
			for (Diagnostic diagnostic : diagnostics) {
				diagnosticMap.put(diagnostic.getTextbookCode(), diagnostic);
			}

			for (Map m : datas) {
				Integer textbookCode = Integer.parseInt(m.get("textbook_code").toString());
				Long doCountMonth = ((BigDecimal) m.get("do_count_month")).longValue();
				Long rightCountMonth = ((BigDecimal) m.get("right_count_month")).longValue();

				Date date = new Date();
				Diagnostic diagnostic = diagnosticMap.get(textbookCode);
				if (diagnostic == null) {
					diagnostic = new Diagnostic();
					diagnostic.setCreateAt(date);
					diagnostic.setTextbookCode(textbookCode);
					diagnosticMap.put(textbookCode, diagnostic);
				}

				diagnostic.setClassMonthDoCount(doCountMonth.intValue());
				diagnostic.setClassMonthRightCount(rightCountMonth.intValue());
				diagnostic.setUpdateAt(date);
			}

			if (diagnosticMap.size() > 0) {
				diaRepo.save(diagnosticMap.values());
			}
		}
	}

}
