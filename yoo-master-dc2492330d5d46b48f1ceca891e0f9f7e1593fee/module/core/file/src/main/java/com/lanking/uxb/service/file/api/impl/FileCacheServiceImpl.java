package com.lanking.uxb.service.file.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.ex.core.NotImplementedException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.api.ImageTransform;
import com.lanking.uxb.service.file.api.SpaceService;
import com.lanking.uxb.service.file.cache.FileCacheService;
import com.lanking.uxb.service.file.util.FileUtil;

@Service("fileService")
@Transactional(readOnly = true)
@ConditionalOnExpression("${file.cache}")
public class FileCacheServiceImpl implements FileService {

	@Autowired
	@Qualifier("FileRepo")
	private Repo<File, Long> fileRepo;

	@Autowired
	private FileCacheService fileCacheService;
	@Autowired
	private SpaceService spaceService;

	private Logger logger = LoggerFactory.getLogger(FileCacheServiceImpl.class);

	@Transactional(readOnly = true)
	@Override
	public File getFile(long id) {
		File file = fileCacheService.getFileById(id);
		if (file == null) {
			file = fileRepo.get(id);
			if (file != null) {
				fileCacheService.setFile(file);
			}
			logger.info("get data from db...");
		}
		return file;
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, File> mgetFile(Collection<Long> ids) {
		Map<Long, File> cacheFiles = fileCacheService.mget(Lists.newArrayList(ids));
		Set<Long> noCacheIds = Sets.newHashSet();
		for (Long id : ids) {
			if (!cacheFiles.containsKey(id)) {
				noCacheIds.add(id);
			}
		}
		if (noCacheIds.size() > 0) {
			Map<Long, File> dbFiles = fileRepo.mget(noCacheIds);
			cacheFiles.putAll(dbFiles);
			fileCacheService.msetFile(Lists.newArrayList(dbFiles.values()));
			logger.info("get data from db...");
		}
		return cacheFiles;
	}

	@Transactional
	@Override
	public File saveFile(File file) {
		if (file.getId() == null) {
			file.setId(SnowflakeUUID.next());
		}
		return fileRepo.save(file);
	}

	@Transactional
	@Override
	public File updateFileScreenshot(long id, long screenshotId) {
		File file = fileRepo.get(id);
		file.setScreenshotId(screenshotId);
		fileRepo.save(file);
		fileCacheService.setFile(file);
		return file;
	}

	@Transactional
	@Override
	public File saveInputStreamFile(File file, InputStream inputStream) {
		throw new NotImplementedException();
	}

	@Transactional
	@Override
	public File saveDiskImage(String spaceName, java.io.File diskFile, boolean deleteDiskFile) throws IOException {
		Space space = spaceService.getSpace(spaceName);
		File file = new File();
		file.setId(SnowflakeUUID.next());
		file.setSpaceId(space.getId());
		file.setName(diskFile.getName());
		ImageTransform itf = getImageTransform();
		itf.load(diskFile.getAbsolutePath());
		file.setSize(itf.getTransformed().available());
		file.setWidth(itf.getSize().getWidth());
		file.setHeight(itf.getSize().getHeight());
		file.setCreateAt(System.currentTimeMillis());
		file.setUpdateAt(file.getCreateAt());
		file.setType(FileType.IMAGE);
		file.setStatus(Status.ENABLED);
		java.io.File destFile = new java.io.File(FileUtil.getFilePath(file));
		if (!destFile.getParentFile().exists()) {
			destFile.getParentFile().mkdirs();
		}
		Files.copy(diskFile, destFile);
		file.setCrc32(FileUtil.getFileCrc32(destFile.getAbsolutePath()));
		file.setMd5(FileUtil.getFileMD5(destFile));
		itf.close();
		if (deleteDiskFile) {
			diskFile.delete();
		}
		return fileRepo.save(file);
	}

	@Override
	public File saveUrlImage(File file, String url) {
		throw new NotImplementedException();
	}

	@Transactional
	@Override
	public void delete(long id) {
		fileRepo.execute("$updateFileStatus", Params.param("id", id).put("status", Status.DELETED.getValue())
				.put("updateAt", System.currentTimeMillis()));
		fileCacheService.delete(id);
	}

	@Transactional
	@Override
	public void delete(Collection<Long> ids) {
		fileRepo.execute("$updateFilesStatus", Params.param("ids", ids).put("status", Status.DELETED.getValue()));
		fileCacheService.delete(ids);
	}

	@Override
	public File findByMd5(long spaceId, String md5) {
		File file = fileCacheService.getFileByMd5(spaceId, md5);
		if (file == null) {
			file = fileRepo.find("$findByMd5", Params.param("spaceId", spaceId).put("md5", md5)).get();
			if (file != null) {
				fileCacheService.setFileMd5(file);
			}
			logger.info("get data from db...");
		}
		return file;
	}

	private ImageTransform getImageTransform() {
		return new AwtImageTransform();
	}

	@Transactional
	@Async
	@Override
	public void reference(long id) {
		fileRepo.execute("$referenceFile", Params.param("id", id));
		fileCacheService.delete(id);
	}

}
