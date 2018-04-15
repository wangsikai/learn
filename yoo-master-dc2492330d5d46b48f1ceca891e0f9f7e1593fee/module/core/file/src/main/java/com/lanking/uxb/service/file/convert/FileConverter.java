package com.lanking.uxb.service.file.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.api.FileStyleService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.file.value.VFile;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月17日
 *
 */
@Component
public class FileConverter extends Converter<VFile, File, Long> {
	@Autowired
	private FileStyleService fileStyleService;
	@Autowired
	private FileService fileService;

	@Override
	protected Long getId(File s) {
		return s.getId();
	}

	@Override
	protected VFile convert(File s) {
		VFile vfile = new VFile();
		vfile.setId(String.valueOf(s.getId()));
		vfile.setName(s.getName());
		vfile.setSize(s.getSize());
		vfile.setWidth(s.getWidth());
		vfile.setHeight(s.getHeight());
		vfile.setDuration(s.getDuration());
		vfile.setType(s.getType());
		vfile.setUrl(FileUtil.getUrl(s));
		if (s.getScreenshotId() != null && s.getScreenshotId() != 0) {
			vfile.setScreenshotId(s.getScreenshotId());
			vfile.setScreenshotUrl(FileUtil.getUrl(s.getScreenshotId()));
		} else {
			vfile.setScreenshotId(0);
			vfile.setScreenshotUrl(StringUtils.EMPTY);
		}
		return vfile;
	}

	public VFile convert(File s, String webMinStyleName, String webMidStyleName) {
		VFile vfile = convert(s);
		if (StringUtils.isNotBlank(webMinStyleName)) {
			vfile.setWebThumbUrl(FileUtil.getUrl(webMinStyleName, s));
		}
		if (StringUtils.isNotBlank(webMidStyleName)) {
			vfile.setWebMidThumbUrl(FileUtil.getUrl(webMidStyleName, s));
		} else {
			vfile.setWebMidThumbUrl(vfile.getWebThumbUrl());
		}
		return vfile;
	}

	@Override
	protected File internalGet(Long fileId) {
		if (fileId == 0L) {
			File file = new File();
			file.setId(0L);
			return file;
		}
		return fileService.getFile(fileId);
	}

	@Override
	protected Map<Long, File> internalMGet(Collection<Long> fileIds) {
		return fileService.mgetFile(fileIds);
	}

	public Map<Long, VFile> mget(Collection<Long> ids, String webMinStyleName, String webMidStyleName) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyMap();
		}
		Map<Long, File> fileMap = internalMGet(ids);
		if (CollectionUtils.isEmpty(fileMap)) {
			return new HashMap<Long, VFile>(0);
		}
		Map<Long, VFile> map = new HashMap<Long, VFile>();
		for (Entry<Long, File> entry : fileMap.entrySet()) {
			map.put(entry.getKey(), convert(entry.getValue(), webMinStyleName, webMidStyleName));
		}
		return map;
	}

	public VFile get(Long id, String webMinStyleName, String webMidStyleName) {
		File file = internalGet(id);
		return convert(file, webMinStyleName, webMidStyleName);
	}
}
