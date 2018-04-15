package com.lanking.uxb.zycon.nationalDayActivity01.api.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.nationalDayActivity01.api.ZycNationalDayActivity01TeaService;
import com.lanking.uxb.zycon.nationalDayActivity01.form.ZycNationalDayActivity01Form;

import httl.util.StringUtils;

@Transactional(readOnly = true)
@Service
public class ZycNationalDayActivity01TeaServiceImpl implements ZycNationalDayActivity01TeaService {

	@Autowired
	@Qualifier("NationalDayActivity01TeaRepo")
	private Repo<NationalDayActivity01Tea, Long> nationalDayActivity01TeaRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryNdaTeaRank(ZycNationalDayActivity01Form form, Pageable p) {
		Params param = Params.param();
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			param.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (StringUtils.isNotBlank(form.getAccountName())) {
			param.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			param.put("realName", '%' + form.getRealName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			param.put("channelName", '%' + form.getChannelName() + '%');
		}
		if (form.getPhase() != null) {
			param.put("phase", form.getPhase());
		}

		return nationalDayActivity01TeaRepo.find("$queryNdaTeaRank", param).fetch(p, Map.class);
	}
}
