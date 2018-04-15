package com.lanking.uxb.operation.platformConfig.controller;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Properties;

import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.config.Config;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.sdk.util.UnicodeUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.operation.platformConfig.api.OpPlatformConfigService;

/**
 * 平台配置管理
 * 
 * @since 2.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月31日
 */
@RestController
@RequestMapping(value = "op/platformConfig")
public class OpPlatformConfigController {

	@Autowired
	private OpPlatformConfigService platformConfigService;
	@Autowired
	private ZooKeeper zooKeeper;

	@RequestMapping(value = "listAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value listAll(String configServerPath) {
		List<Config> items = platformConfigService.findAll();
		for (Config config : items) {
			String zookeeperValue = getZookeeperValue(configServerPath, config);
			config.setSyncZookeeper(config.getValue().equals(zookeeperValue));
			config.setZookeeperValue(zookeeperValue);
		}
		ValueMap vm = ValueMap.value("items", items);
		return new Value(vm);
	}

	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(String configServerPath, String key, String value, String note, boolean realTime) {
		if (platformConfigService.find(key) != null) {
			return new Value(new ServerException("config exist!"));
		}
		Config config = platformConfigService.save(key, value, note, realTime);
		config.setSyncZookeeper(false);
		ValueMap vm = ValueMap.value("config", config);
		return new Value(vm);
	}

	@RequestMapping(value = "deleteConfig", method = { RequestMethod.GET, RequestMethod.POST })
	public Value deleteConfig(String configServerPath, long id) {
		Config config = platformConfigService.get(id);
		if (config == null) {
			return new Value(new ServerException("config not exist!"));
		}
		try {
			String configs = new String(zooKeeper.getData(configServerPath, false, new Stat()), Charsets.UTF8);
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(configs.getBytes(Charsets.UTF8)));
			StringBuffer sb = new StringBuffer();
			int idx = 0;
			int size = properties.size() - 1;
			for (Object key : properties.keySet()) {
				if (key.toString().equals(config.getKey())) {
					continue;
				}
				String value = (String) properties.get(key.toString());
				sb.append(key + "=" + UnicodeUtils.toEncodedUnicode(value, true));
				if (idx < size) {
					sb.append("\r\n");
				}
				idx++;
			}
			zooKeeper.setData(configServerPath, sb.toString().getBytes(), -1);
		} catch (Exception e) {
			return new Value(new ServerException("delete zookeeper config fail!"));
		}
		platformConfigService.delete(id);
		return new Value();
	}

	private String getZookeeperValue(String configServerPath, Config config) {
		try {
			String configs = new String(zooKeeper.getData(configServerPath, false, new Stat()), Charsets.UTF8);
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(configs.getBytes(Charsets.UTF8)));
			if (properties.containsKey(config.getKey())) {
				return properties.getProperty(config.getKey()).toString();
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "updateValue", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateValue(String configServerPath, long id, String value) {
		try {
			Config config = platformConfigService.updateValue(id, value);
			ValueMap vm = ValueMap.value("config", config);
			String zookeeperValue = getZookeeperValue(configServerPath, config);
			config.setSyncZookeeper(config.getValue().equals(zookeeperValue));
			config.setZookeeperValue(zookeeperValue);
			return new Value(vm);
		} catch (Exception e) {
			return new Value(new ServerException("maybe column value illegal!!"));
		}
	}

	@RequestMapping(value = "updateRealTime", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateRealTime(String configServerPath, long id, Boolean realTime) {
		try {
			Config config = platformConfigService.updateRealTime(id, realTime);
			String zookeeperValue = getZookeeperValue(configServerPath, config);
			config.setSyncZookeeper(config.getValue().equals(zookeeperValue));
			config.setZookeeperValue(zookeeperValue);
			ValueMap vm = ValueMap.value("config", config);
			return new Value(vm);
		} catch (Exception e) {
			return new Value(new ServerException("maybe column value illegal!!"));
		}
	}

	@RequestMapping(value = "updateNote", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateNote(String configServerPath, long id, String note) {
		try {
			Config config = platformConfigService.updateNote(id, note);
			String zookeeperValue = getZookeeperValue(configServerPath, config);
			config.setSyncZookeeper(config.getValue().equals(zookeeperValue));
			config.setZookeeperValue(zookeeperValue);
			ValueMap vm = ValueMap.value("config", config);
			return new Value(vm);
		} catch (Exception e) {
			return new Value(new ServerException("maybe column value illegal!!"));
		}
	}

	@RequestMapping(value = "syncUpdateZookeeper", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncUpdateZookeeper(String configServerPath, long id) {
		try {
			Config config = platformConfigService.get(id);
			String configs = new String(zooKeeper.getData(configServerPath, false, new Stat()), Charsets.UTF8);
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(configs.getBytes(Charsets.UTF8)));
			properties.put(config.getKey(), config.getValue());
			StringBuffer sb = new StringBuffer();
			int idx = 0;
			int size = properties.size() - 1;
			for (Object key : properties.keySet()) {
				String value = (String) properties.get(key.toString());
				sb.append(key + "=" + UnicodeUtils.toEncodedUnicode(value, true));
				if (idx < size) {
					sb.append("\r\n");
				}
				idx++;
			}
			zooKeeper.setData(configServerPath, sb.toString().getBytes(), -1);
			config.setSyncZookeeper(true);
			config.setZookeeperValue(config.getValue());
			return new Value(ValueMap.value("config", config));
		} catch (Exception e) {
			return new Value(new ServerException("sync zookeeper fail!!"));
		}
	}

	@RequestMapping(value = "syncDeleteZookeeper", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncDeleteZookeeper(String configServerPath, long id) {
		try {
			Config config = platformConfigService.get(id);
			String configs = new String(zooKeeper.getData(configServerPath, false, new Stat()), Charsets.UTF8);
			Properties properties = new Properties();
			properties.load(new ByteArrayInputStream(configs.getBytes(Charsets.UTF8)));
			StringBuffer sb = new StringBuffer();
			int idx = 0;
			int size = properties.size() - 1;
			for (Object key : properties.keySet()) {
				if (key.toString().equals(config.getKey())) {
					continue;
				}
				String value = (String) properties.get(key.toString());
				sb.append(key + "=" + UnicodeUtils.toEncodedUnicode(value, true));
				if (idx < size) {
					sb.append("\r\n");
				}
				idx++;
			}
			zooKeeper.setData(configServerPath, sb.toString().getBytes(), -1);
			config.setSyncZookeeper(false);
			config.setZookeeperValue(null);
			return new Value(ValueMap.value("config", config));
		} catch (Exception e) {
			return new Value(new ServerException("sync zookeeper fail!!"));
		}
	}
}
