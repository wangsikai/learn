package com.lanking.uxb.operation.platformConfig.api.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.frame.config.Config;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.operation.platformConfig.api.OpPlatformConfigService;

@Transactional(readOnly = true)
@Service
public class OpPlatformConfigServiceImpl implements OpPlatformConfigService {
	@Autowired
	@Qualifier("ConfigRepo")
	private Repo<Config, Long> configRepo;

	@Override
	public Config get(long id) {
		return configRepo.get(id);
	}

	@Override
	public Config find(String key0) {
		Params params = Params.param("key0", key0);
		return configRepo.find("$opFind", params).get();
	}

	@Override
	public Map<String, Config> find(Collection<String> key0s) {
		List<Config> configs = configRepo.find("$opFind", Params.param("key0s", key0s)).list();
		Map<String, Config> configMap = new HashMap<String, Config>(key0s.size());
		for (Config config : configs) {
			configMap.put(config.getKey(), config);
		}
		return configMap;
	}

	@Transactional
	@Override
	public Config save(String key0, String value, String note, boolean realTime) {
		Config config = new Config();
		config.setKey(key0);
		config.setValue(value);
		config.setNote(note);
		config.setRealTime(realTime);
		return configRepo.save(config);
	}

	@Transactional
	@Override
	public Config updateValue(long id, String value) {
		Config config = configRepo.get(id);
		config.setValue(value);
		return configRepo.save(config);
	}

	@Transactional
	@Override
	public Config updateNote(long id, String note) {
		Config config = configRepo.get(id);
		config.setNote(note);
		return configRepo.save(config);
	}

	@Transactional
	@Override
	public Config updateRealTime(long id, boolean realTime) {
		Config config = configRepo.get(id);
		config.setRealTime(realTime);
		return configRepo.save(config);
	}

	@Transactional
	@Override
	public Config update(long id, String value, String note, boolean realTime) {
		Config config = configRepo.get(id);
		config.setValue(value);
		config.setNote(note);
		config.setRealTime(realTime);
		return configRepo.save(config);
	}

	@Override
	public List<Config> findAll() {
		return configRepo.find("$opFind").list();
	}

	@Transactional
	@Override
	public void delete(long id) {
		configRepo.deleteById(id);
	}

}
