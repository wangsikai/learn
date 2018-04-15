package com.lanking.uxb.service.file.api.impl;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.DocumentHandle;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.cache.DocumentHandleCacheService;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年5月12日
 */
@Component
public class BinFileHandle implements FileHandle {

	private Logger logger = LoggerFactory.getLogger(BinFileHandle.class);

	@Autowired
	@Qualifier("linuxDocumentHandle")
	private DocumentHandle linuxDocumentHandle;
	@Autowired
	@Qualifier("windowDocumentHandle")
	private DocumentHandle windowDocumentHandle;
	@Autowired
	private FileService fileService;
	@Autowired
	private DocumentHandleCacheService dhCacheService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	private boolean isWin = false;
	private boolean userMsoffice = false;

	@PostConstruct
	void init() {
		isWin = System.getProperty("os.name").toLowerCase().contains("windows");
		userMsoffice = Env.getBoolean("file.pdf2swf.convert.msoffice");
	}

	@Override
	public boolean accept(FileType fileType) {
		return fileType == FileType.BIN;
	}

	@Override
	public void preUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
	}

	@Override
	public void postUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
	}

	@Override
	public void afterUpload(final File file) throws FileException {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (isWin) {
						logger.info("window can not convert..wow");
					} else {
						String ext = file.getExt();
						if (userMsoffice) {
							if (FileUtil.isPdf(file)) {
								java.io.File nativeFile = FileUtil.getNativeFile(file);
								java.io.File nativePdfFile = new java.io.File(
										nativeFile.getAbsoluteFile() + "." + FileExt.PDF);
								Files.copy(nativeFile, nativePdfFile);
								linuxDocumentHandle.convertPDF2SWF(nativePdfFile.getAbsolutePath(), file);
								// pdf to image
								java.io.File pageOneImage = linuxDocumentHandle.pdf2Image(nativePdfFile);
								try {
									File screenFile = fileService.saveDiskImage("screen", pageOneImage, false);
									fileService.updateFileScreenshot(file.getId(), screenFile.getId());
								} catch (IOException e) {
									logger.error("save screen file fail:", e);
								}
							} else {
								dhCacheService.pushDoc2Pdf(file.getId());
							}
						} else {
							if (FileExt.DOC.contains(ext) || FileExt.TEXT.contains(ext)) {
								java.io.File nativeFile = FileUtil.getNativeFile(file);
								java.io.File nativePdfFile = new java.io.File(
										nativeFile.getAbsoluteFile() + "." + FileExt.PDF);
								if (FileUtil.isPdf(file)) {
									Files.copy(nativeFile, nativePdfFile);
									linuxDocumentHandle.convertPDF2SWF(nativePdfFile.getAbsolutePath(), file);
								} else {
									linuxDocumentHandle.convertDOC2SWF(nativeFile.getAbsolutePath(), file);
								}
							}
						}
					}
				} catch (Exception e) {
					logger.error("convert fail:", e);
				}
			}
		});
	}
}
