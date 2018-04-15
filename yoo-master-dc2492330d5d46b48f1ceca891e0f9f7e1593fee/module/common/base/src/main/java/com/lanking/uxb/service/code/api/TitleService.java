package com.lanking.uxb.service.code.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.baseData.Title;

public interface TitleService {

	Title getTitle(Integer code);

	List<Title> getAll();

	Map<Integer, Title> mget(Collection<Integer> codes);

}
