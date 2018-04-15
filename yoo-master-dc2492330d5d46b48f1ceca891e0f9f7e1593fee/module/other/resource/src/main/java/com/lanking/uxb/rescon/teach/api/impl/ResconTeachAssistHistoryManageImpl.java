package com.lanking.uxb.rescon.teach.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistHistoryManage;

/**
 * 操作历史接口实现.
 * 
 * @author wlche
 * @version v1.3
 */
@Transactional(readOnly = true)
@Service
public class ResconTeachAssistHistoryManageImpl implements ResconTeachAssistHistoryManage {
	@Autowired
	@Qualifier("TeachAssistHistoryRepo")
	Repo<TeachAssistHistory, Long> teachAssistHistoryRepo;

	@Override
	@Transactional
	public TeachAssistHistory save(TeachAssistHistory history) {
		return teachAssistHistoryRepo.save(history);
	}

	@Override
	public List<TeachAssistHistory> findList(Long teachAssistId) {
		List<TeachAssistHistory> fisrt = teachAssistHistoryRepo.find("$findFirstLog",
				Params.param("teachAssistId", teachAssistId)).list();
		if (CollectionUtils.isNotEmpty(fisrt)) {
			List<TeachAssistHistory> list = teachAssistHistoryRepo.find("$findList",
					Params.param("teachAssistId", teachAssistId)).list();
			list.removeAll(fisrt);
			fisrt.addAll(list);
		}
		return fisrt;
	}
}
