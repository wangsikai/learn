package com.lanking.cloud.job.correctQuestionFailRecord.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.job.correctQuestionFailRecord.service.CorrectConfigService;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.microservice.domain.yoocorrect.CorrectConfig;

@Service
public class CorrectConfigServiceImpl implements CorrectConfigService {

	@Autowired
	@Qualifier("CorrectConfigRepo")
	Repo<CorrectConfig, Long> correctConfigRepo;

	@Override
	@Transactional("yooCorrectTransactionManager")
	public CorrectConfig getCorrectConfigs() {
		List<CorrectConfig> list = correctConfigRepo.find("$queryCorrectConfigs", Params.param()).list();
		if(null != list && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
