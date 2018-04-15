package com.lanking.uxb.service.file.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.lanking.uxb.service.file.util.FileUtil;

@Service("fileService")
@Transactional(readOnly = true)
@ConditionalOnExpression("!${file.cache}")
public class FileServiceImpl implements FileService {

	@Autowired
	@Qualifier("FileRepo")
	private Repo<File, Long> fileRepo;
	@Autowired
	private SpaceService spaceService;

	@Transactional(readOnly = true)
	@Override
	public File getFile(long id) {
		return fileRepo.get(id);
	}

	@Transactional(readOnly = true)
	@Override
	public Map<Long, File> mgetFile(Collection<Long> ids) {
		return fileRepo.mget(ids);
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
		return fileRepo.save(file);
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
			diskFile.deleteOnExit();
		}
		return fileRepo.save(file);
	}

	@Transactional
	@Override
	public File saveUrlImage(File file, String url) {
		throw new NotImplementedException();
	}

	@Transactional
	@Override
	public void delete(long id) {
		fileRepo.execute("$updateFileStatus", Params.param("id", id).put("status", Status.DELETED.getValue())
				.put("updateAt", System.currentTimeMillis()));
	}

	@Transactional
	@Override
	public void delete(Collection<Long> ids) {
		fileRepo.execute("$updateFilesStatus", Params.param("ids", ids).put("status", Status.DELETED.getValue()));
	}

	private ImageTransform getImageTransform() {
		return new AwtImageTransform();
	}

	@Override
	public File findByMd5(long spaceId, String md5) {
		return fileRepo.find("$findByMd5", Params.param("spaceId", spaceId).put("md5", md5)).get();
	}

	@Transactional
	@Async
	@Override
	public void reference(long id) {
		fileRepo.execute("$referenceFile", Params.param("id", id));
	}

}
