package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Duty;

public interface DutyService {

	Duty get(Integer code);

	List<Duty> getAll();

	Map<Integer, Duty> mget(Collection<Integer> codes);

}
