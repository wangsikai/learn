package com.lanking.uxb.rescon.basedata.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateLog;
import com.lanking.cloud.domain.common.resource.specialTraining.SpecialTrainingOperateType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.basedata.api.ResconSpecialTrainingOperateLogService;

@Service
@Transactional(readOnly = true)
public class ResconSpecialTrainingOperateLogServiceImpl implements ResconSpecialTrainingOperateLogService {

	@Autowired
	@Qualifier("SpecialTrainingOperateLogRepo")
	private Repo<SpecialTrainingOperateLog, Long> Repo;

	@Override
	public List<SpecialTrainingOperateLog> findLogList(Long specialTrainingId) {
		return Repo.find("$findLogList", Params.param("specialTrainingId", specialTrainingId)).list();
	}

	@Override
	public void saveLog(Long specialTrainingId, SpecialTrainingOperateType operateType, Long createId, String p1) {
		SpecialTrainingOperateLog log = new SpecialTrainingOperateLog();
		log.setSpecialTrainingId(specialTrainingId);
		log.setCreateId(createId);
		log.setOperateType(operateType);
		log.setCreateAt(new Date());
		Repo.save(log);
	}
}
