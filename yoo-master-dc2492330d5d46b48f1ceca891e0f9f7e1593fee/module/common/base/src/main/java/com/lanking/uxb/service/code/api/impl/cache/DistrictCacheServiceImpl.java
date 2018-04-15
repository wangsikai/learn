package com.lanking.uxb.service.code.api.impl.cache;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.cache.DistrictCacheService;

@Service
@Transactional(readOnly = true)
@ConditionalOnExpression("${common.code.cache}")
public class DistrictCacheServiceImpl implements DistrictService {

	@Autowired
	@Qualifier("DistrictRepo")
	private Repo<District, Long> districtRepo;

	@Autowired
	private DistrictCacheService districtCacheService;

	private Logger logger = LoggerFactory.getLogger(DistrictCacheServiceImpl.class);

	@Override
	public List<District> getDistrictByLevel(int level) {
		List<District> districts = districtCacheService.getLevelDistricts(level);
		if (CollectionUtils.isEmpty(districts)) {
			districts = districtRepo.find("$getDistrictByLevel", Params.param("level", level)).list();
			districtCacheService.setLevelDistricts(level, districts);
			logger.info("get data from db...");
		}
		return districts;
	}

	@Override
	public List<District> getDistrictByPcode(long code) {
		List<District> districts = districtCacheService.getChildDistricts(code);
		if (CollectionUtils.isEmpty(districts)) {
			districts = districtRepo.find("$getDistrictByPcode", Params.param("pcode", code)).list();
			districtCacheService.setChildDistricts(code, districts);
			logger.info("get data from db...");
		}
		return districts;
	}

	@Override
	public District getDistrict(long code) {
		District district = districtCacheService.getCodeHashDistrict(code);
		if (district == null) {
			district = districtRepo.get(code);
			if (district != null) {
				districtCacheService.setCodeHashDistrict(district);
			}
			logger.info("get data from db...");
		}
		return district;
	}

	@Override
	public District getPDistrict(long code) {
		District district = districtCacheService.getPCodeHashDistrict(code);
		if (district == null) {
			district = districtRepo.find("$getPDistrict", Params.param("code", code)).get();
			if (district != null) {
				districtCacheService.setPCodeHashDistrict(code, district);
			}
			logger.info("get data from db...");
		}
		return district;
	}

	@Override
	public String getDistrictName(Long code) {
		String fullname = districtCacheService.getcodeHashFullName(code);
		if (StringUtils.isBlank(fullname)) {
			District d = getDistrict(code);
			if (d != null) {
				if (d.getLevel() == 1) {
					fullname = d.getName();
				} else if (d.getLevel() == 2) {
					fullname = getPDistrict(d.getCode()).getName() + " " + d.getName();
				} else if (d.getLevel() == 3) {
					District dTemp = getPDistrict(d.getCode());
					if (dTemp.getLevel() == 2) {
						Long pcode = dTemp.getCode();
						fullname = getPDistrict(pcode).getName() + " " + getPDistrict(code).getName() + " "
								+ d.getName();
					} else {
						fullname = getPDistrict(code).getName() + " " + d.getName();
					}
				}
				if (StringUtils.isNotBlank(fullname)) {
					districtCacheService.setcodeHashFullName(code, fullname);
				}
			}
			logger.info("get data from db...");
		}
		return fullname;
	}

	@Override
	public Map<Long, String> mgetDistrictName(Collection<Long> codes) {
		Map<Long, String> fullnames = districtCacheService.getCodeHashFullNames(Lists.newArrayList(codes));
		Map<Long, String> names = Maps.newHashMap();
		for (Long key : fullnames.keySet()) {
			String fullname = fullnames.get(key);
			if (StringUtils.isBlank(fullname)) {
				fullname = getDistrictName(key);
			}
			names.put(key, fullname);
		}
		return names;
	}

	public List<District> getAll() {
		return districtRepo.find("$getAll").list();
	}

	@Override
	public Map<Long, District> mget(Collection<Long> ids) {
		Map<Long, District> districtMap = new HashMap<Long, District>(ids.size());

		for (Long id : ids) {
			if (id == null) {
				continue;
			}
			districtMap.put(id, this.getDistrict(id));
		}
		return districtMap;
	}

	@Override
	public List<District> findAll() {
		List<District> districts = Lists.newArrayList();
		for (int i = 1; i <= 3; i++) {
			List<District> temp = districtCacheService.getLevelDistricts(i);
			if (CollectionUtils.isEmpty(temp)) {
				temp = districtRepo.find("$getDistrictByLevel", Params.param("level", i)).list();
				districtCacheService.setLevelDistricts(i, temp);
			}
			districts.addAll(temp);
		}
		return districts;
	}

}
