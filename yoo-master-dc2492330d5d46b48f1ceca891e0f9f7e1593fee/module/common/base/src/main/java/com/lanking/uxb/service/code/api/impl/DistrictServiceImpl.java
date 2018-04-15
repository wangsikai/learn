package com.lanking.uxb.service.code.api.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.DistrictService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("!${common.code.cache}")
public class DistrictServiceImpl implements DistrictService {

	@Autowired
	@Qualifier("DistrictRepo")
	private Repo<District, Long> districtRepo;

	@Override
	public List<District> getDistrictByLevel(int level) {
		return districtRepo.find("$getDistrictByLevel", Params.param("level", level)).list();
	}

	@Override
	public List<District> getDistrictByPcode(long code) {
		return districtRepo.find("$getDistrictByPcode", Params.param("pcode", code)).list();
	}

	@Override
	public District getDistrict(long code) {
		return districtRepo.get(code);
	}

	@Override
	public District getPDistrict(long code) {
		return districtRepo.find("$getPDistrict", Params.param("code", code)).get();
	}

	@Override
	public String getDistrictName(Long districtCode) {
		String disName = null;
		// 根据level判断 来拼接获取名称
		if (districtCode == 0) {
			return disName;
		} else {
			District d = this.getDistrict(districtCode);
			if (d.getLevel() == 1) {
				disName = d.getName();
			} else if (d.getLevel() == 2) {
				disName = this.getPDistrict(d.getCode()).getName() + " " + d.getName();
			} else if (d.getLevel() == 3) {
				Long tempCode = this.getPDistrict(d.getCode()).getCode();
				disName = this.getPDistrict(tempCode).getName() + " " + this.getPDistrict(d.getCode()).getName() + " "
						+ d.getName();
			}
			return disName;
		}
	}

	@Override
	public Map<Long, String> mgetDistrictName(Collection<Long> codes) {
		String disName = null;
		Map<Long, String> disNameMap = Maps.newHashMap();
		// 去除重合的code
		Set<Long> codeSet = new HashSet<Long>(codes);
		List<District> disList = districtRepo.find("$findAllDistrictName", Params.param("codes", codeSet)).list();
		for (District d : disList) {
			if (d.getLevel() == 1) {
				disName = d.getName();
			} else if (d.getLevel() == 2) {
				disName = this.getPDistrict(d.getCode()).getName() + " " + d.getName();
			} else if (d.getLevel() == 3) {
				Long tempCode = this.getPDistrict(d.getCode()).getCode();
				disName = this.getPDistrict(tempCode).getName() + " " + this.getPDistrict(d.getCode()).getName() + " "
						+ d.getName();
			} else {

			}
			disNameMap.put(d.getCode(), disName);
		}

		return disNameMap;
	}

	@Override
	public Map<Long, District> mget(Collection<Long> ids) {
		return districtRepo.mget(ids);
	}

	@Override
	public List<District> findAll() {
		return districtRepo.getAll();
	}

}
