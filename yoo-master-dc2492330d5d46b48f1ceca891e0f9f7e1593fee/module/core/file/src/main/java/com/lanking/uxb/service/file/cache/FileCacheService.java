package com.lanking.uxb.service.file.cache;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.cache.api.impl.AbstractCacheService;

@Service
@SuppressWarnings("unchecked")
public class FileCacheService extends AbstractCacheService {

	private ValueOperations<String, File> filesOpt;

	private static final String FILE_KEY = "f";
	private static final String FILE_MD5_KEY = "md5";

	private String getFileKey(long id) {
		return assemblyKey(FILE_KEY, id);
	}

	private List<String> mgetFileKey(List<Long> ids) {
		List<String> keys = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(ids)) {
			for (Long id : ids) {
				keys.add(getFileKey(id));
			}
		}
		return keys;
	}

	public File getFileById(long id) {
		return filesOpt.get(getFileKey(id));
	}

	public void setFile(long id, File file) {
		filesOpt.set(getFileKey(id), file);
	}

	public void setFile(File file) {
		filesOpt.set(getFileKey(file.getId()), file);
	}

	public void msetFile(List<File> files) {
		if (CollectionUtils.isNotEmpty(files)) {
			for (File file : files) {
				if (file != null) {
					setFile(file);
				}
			}
		}
	}

	public Map<Long, File> mget(List<Long> ids) {
		Map<Long, File> fileMap = Maps.newHashMap();
		List<String> keys = mgetFileKey(ids);
		List<File> files = filesOpt.multiGet(keys);
		if (CollectionUtils.isNotEmpty(files)) {
			for (File file : files) {
				if (file != null) {
					fileMap.put(file.getId(), file);
				}
			}
		}
		return fileMap;
	}

	public void delete(long id) {
		getRedisTemplate().delete(getFileKey(id));
	}

	public void delete(Collection<Long> ids) {
		List<String> keys = Lists.newArrayList();
		for (Long id : ids) {
			keys.add(getFileKey(id));
		}
		getRedisTemplate().delete(keys);
	}

	private String getFileMd5Key(long spaceId, String md5) {
		return assemblyKey(FILE_MD5_KEY, spaceId, md5);
	}

	public File getFileByMd5(long spaceId, String md5) {
		return filesOpt.get(getFileMd5Key(spaceId, md5));
	}

	public void setFileMd5(File file) {
		filesOpt.set(getFileMd5Key(file.getSpaceId(), file.getMd5()), file);
	}

	@Override
	public String getNs() {
		return "f";
	}

	@Override
	public String getNsCn() {
		return "文件";
	}

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		filesOpt = getRedisTemplate().opsForValue();
	}
}
