package com.lanking.uxb.channelSales.channel.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.channelSales.channel.api.CsChannelSchoolService;
import com.lanking.uxb.channelSales.channel.form.ChannelSchoolQueryForm;

/**
 * @see CsChannelSchoolService
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Service
@Transactional(readOnly = true)
public class CsChannelSchoolServiceImpl implements CsChannelSchoolService {
	@Autowired
	@Qualifier("ChannelSchoolRepo")
	private Repo<ChannelSchool, Long> channelSchoolRepo;
	@Autowired
	@Qualifier("SchoolRepo")
	private Repo<School, Long> schoolRepo;

	@Override
	public Page<ChannelSchool> query(ChannelSchoolQueryForm queryForm) {
		Pageable pageable = P.index(queryForm.getPage(), queryForm.getSize());
		Params params = Params.param();
		if (StringUtils.isNotBlank(queryForm.getChannelCode())) {
			params.put("channelCode", queryForm.getChannelCode());
		}
		if (StringUtils.isNotBlank(queryForm.getChannelName())) {
			params.put("channelName", queryForm.getChannelName() + '%');
		}
		if (StringUtils.isNotBlank(queryForm.getSchoolName())) {
			params.put("schoolName", queryForm.getSchoolName() + '%');
		}
		if (queryForm.getSchoolType() != null) {
			params.put("schoolType", queryForm.getSchoolType().getValue());
		}
		return channelSchoolRepo.find("$csQueryChannelSchool", params).fetch(pageable);
	}

	@Override
	public Page<School> querySchool(ChannelSchoolQueryForm queryForm) {
		Pageable pageable = P.index(queryForm.getPage(), queryForm.getSize());
		Params params = Params.param();
		if (StringUtils.isNotBlank(queryForm.getChannelCode())) {
			params.put("code", queryForm.getChannelCode());
		}
		if (StringUtils.isNotBlank(queryForm.getChannelName())) {
			params.put("channelName", "%" + queryForm.getChannelName() + "%");
		}
		if (StringUtils.isNotBlank(queryForm.getSchoolName())) {
			params.put("schoolName", "%" + queryForm.getSchoolName() + "%");
		}
		if (queryForm.getSchoolType() != null) {
			params.put("schoolType", queryForm.getSchoolType().getValue());
		}

		return schoolRepo.find("$csQuerySchool", params).fetch(pageable);
	}

	@Override
	public Map<Integer, List<ChannelSchool>> mgetSchoolByChannel(Collection<Integer> codes) {
		List<ChannelSchool> csList = channelSchoolRepo.find("$csSchoolByChannel", Params.param("codes", codes)).list();
		Map<Integer, List<ChannelSchool>> map = new HashMap<Integer, List<ChannelSchool>>();
		for (Integer code : codes) {
			List<ChannelSchool> newCsList = Lists.newArrayList();
			for (ChannelSchool cs : csList) {
				if (cs.getChannelCode() == code.intValue()) {
					newCsList.add(cs);
				}
			}
			map.put(code, newCsList);
		}
		return map;
	}

	@Override
	public Map<String, Date> getSchoolBindDate(Long schoolId) {
		return channelSchoolRepo.find("$csGetSchoolBindDate", Params.param("schoolId", schoolId)).get(Map.class);
	}
}
