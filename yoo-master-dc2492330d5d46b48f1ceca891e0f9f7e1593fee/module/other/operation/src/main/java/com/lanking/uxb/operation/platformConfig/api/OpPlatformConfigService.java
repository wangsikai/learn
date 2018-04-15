package com.lanking.uxb.operation.platformConfig.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.frame.config.Config;

public interface OpPlatformConfigService {

	Config get(long id);

	Config find(String key0);

	Map<String, Config> find(Collection<String> key0s);

	Config save(String key0, String value, String note, boolean realTime);

	Config updateValue(long id, String value);

	Config updateNote(long id, String note);

	Config updateRealTime(long id, boolean realTime);

	Config update(long id, String value, String note, boolean realTime);

	List<Config> findAll();

	void delete(long id);

}
