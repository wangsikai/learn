package com.lanking.uxb.service.file.api.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.api.ImageTransform;
import com.lanking.uxb.service.file.api.OFileService;
import com.lanking.uxb.service.file.api.SpaceService;
import com.lanking.uxb.service.file.util.FileUtil;

@Service
public class OFileServiceImpl implements OFileService {

	@Autowired
	private FileService fileService;
	@Autowired
	private SpaceService spaceService;

	@Override
	public File rotate(long id, int degree) throws IOException {
		File file = fileService.getFile(id);
		Space space = spaceService.getSpace(file.getSpaceId());
		String oriFilePath = FileUtil.getFilePath(file);
		ImageTransform imageTransform = imageTransform();
		imageTransform.load(oriFilePath);
		imageTransform.rotate(degree);
		String rotateFilePath = oriFilePath + "_" + degree + "." + FileExt.JPG;
		imageTransform.save(rotateFilePath);
		imageTransform.close();
		return fileService.saveDiskImage(space.getName(), new java.io.File(rotateFilePath), true);
	}

	public ImageTransform imageTransform() {
		return new AwtImageTransform();
	}

}
