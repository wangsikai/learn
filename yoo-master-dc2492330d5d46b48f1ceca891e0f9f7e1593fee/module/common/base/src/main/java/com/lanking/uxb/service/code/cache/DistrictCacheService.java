package com.lanking.uxb.service.code.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
@SuppressWarnings("unchecked")
public class DistrictCacheService extends AbstractCacheService {

	private ValueOperations<String, List<District>> districtsOpt;

	private HashOperations<String, Long, District> districtHashOpt;

	private HashOperations<String, Long, String> districtFullNameHashOpt;

	private static final String LEVEL_DISTRICTS_KEY = "l";
	private static final String CHILD_DISTRICTS_KEY = "c";

	private static final String CODE_HASH_DISTRICT_KEY = "ch";
	private static final String PCODE_HASH_DISTRICT_KEY = "pch";

	private static final String CODE_HASH_FULLNAME_KEY = "pcf";

	public void setcodeHashFullName(long code, String fullName) {
		districtFullNameHashOpt.put(CODE_HASH_FULLNAME_KEY, code, fullName);
	}

	public String getcodeHashFullName(long code) {
		return districtFullNameHashOpt.get(CODE_HASH_FULLNAME_KEY, code);
	}

	public List<String> mgetCodeHashFullName(Collection<Long> codes) {
		return districtFullNameHashOpt.multiGet(CODE_HASH_FULLNAME_KEY, codes);
	}

	public Map<Long, String> getCodeHashFullNames(List<Long> codes) {
		Map<Long, String> names = Maps.newHashMap();
		List<String> fullnames = districtFullNameHashOpt.multiGet(CODE_HASH_FULLNAME_KEY, codes);
		int size = codes.size();
		for (int i = 0; i < size; i++) {
			names.put(codes.get(i), fullnames.get(i));
		}
		return names;
	}

	public void setCodeHashDistrict(District district) {
		districtHashOpt.put(CODE_HASH_DISTRICT_KEY, district.getCode(), district);
	}

	public District getCodeHashDistrict(long code) {
		return districtHashOpt.get(CODE_HASH_DISTRICT_KEY, code);
	}

	public void setPCodeHashDistrict(long code, District pdistrict) {
		districtHashOpt.put(PCODE_HASH_DISTRICT_KEY, code, pdistrict);
	}

	public District getPCodeHashDistrict(long code) {
		return districtHashOpt.get(PCODE_HASH_DISTRICT_KEY, code);
	}

	private String getLevelDistrictsKey(int level) {
		return assemblyKey(LEVEL_DISTRICTS_KEY, level);
	}

	public List<District> getLevelDistricts(int level) {
		return districtsOpt.get(getLevelDistrictsKey(level));
	}

	public void setLevelDistricts(int level, List<District> districts) {
		districtsOpt.set(getLevelDistrictsKey(level), districts);
	}

	public void invlidLevelDistricts(int level) {
		getRedisTemplate().delete(getLevelDistrictsKey(level));
	}

	private String getChildDistrictsKey(long pcode) {
		return assemblyKey(CHILD_DISTRICTS_KEY, pcode);
	}

	public List<District> getChildDistricts(long pcode) {
		return districtsOpt.get(getChildDistrictsKey(pcode));
	}

	public void setChildDistricts(long pcode, List<District> districts) {
		districtsOpt.set(getChildDistrictsKey(pcode), districts);
	}

	public void invlidChildDistricts(long pcode) {
		getRedisTemplate().delete(getChildDistrictsKey(pcode));
	}

	@Override
	public String getNs() {
		return "d";
	}

	@Override
	public String getNsCn() {
		return "地域";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		districtsOpt = getRedisTemplate().opsForValue();
		districtHashOpt = getRedisTemplate().opsForHash();
		districtFullNameHashOpt = getRedisTemplate().opsForHash();

	}
}
