package com.lanking.uxb.service.file.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.file.FileStyle;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.file.api.FileStyleService;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Service
@Transactional(readOnly = true)
public class FileStyleServiceImpl implements FileStyleService {

	@Autowired
	@Qualifier("FileStyleRepo")
	private Repo<FileStyle, Long> fileStyleRepo;

	@Transactional
	@Override
	public FileStyle save(FileStyle fileStyle) {
		return fileStyleRepo.save(fileStyle);
	}

	@Transactional(readOnly = true)
	@Override
	public FileStyle find(long spaceId, int mode, int width, int height, int quality) {
		Params params = Params.param("spaceId", spaceId).put("mode", mode).put("width", width).put("height", height)
				.put("quality", quality);
		return fileStyleRepo.find("$findFileStyle", params).get();
	}

	@Transactional(readOnly = true)
	@Override
	public FileStyle find(Long spaceId, String name) {
		Params params = Params.param("name", name);
		if (spaceId != null) {
			params.put("spaceId", spaceId);
		}
		return fileStyleRepo.find("$findFileStyleByName", params).get();
	}
}
