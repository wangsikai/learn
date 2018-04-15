package com.lanking.uxb.service.file.api.impl.listener;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.uxb.service.file.api.DocumentHandle;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.cache.DocumentHandleCacheService;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * pdf 转 swf 监听
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月24日
 */
@Component
@ConditionalOnExpression("${file.pdf2swf.convert.msoffice}")
public class Pdf2SwfListener {

	private Logger logger = LoggerFactory.getLogger(Pdf2SwfListener.class);

	@Autowired
	@Qualifier("linuxDocumentHandle")
	private DocumentHandle documentHandle;
	@Autowired
	private DocumentHandleCacheService dhCacheService;
	@Autowired
	private FileService fileService;

	@Scheduled(initialDelay = 3000, fixedDelay = 1)
	void process() {
		Long id = dhCacheService.getPdf2Swf();
		try {
			if (id != null) {
				File file = fileService.getFile(id);
				logger.info(FileUtil.getFilePath(file));
				String pdfFilePath = FileUtil.getFilePath(file) + "." + FileExt.PDF;
				try {
					documentHandle.convertPDF2SWF(pdfFilePath, file);
				} catch (FileException e) {
					logger.info("swf file exist...");
				}
				// pdf to image
				java.io.File pageOneImage = documentHandle.pdf2Image(new java.io.File(pdfFilePath));
				try {
					File screenFile = fileService.saveDiskImage("screen", pageOneImage, false);
					fileService.updateFileScreenshot(id, screenFile.getId());
				} catch (IOException e) {
					logger.error("save screen file fail:", e);
				}
			}
		} catch (Exception e) {
			logger.error("pdf 2 swf error:", e);
		} finally {
			if (id != null) {
				dhCacheService.popPdf2Swf();
				logger.info("pop file from pdf2swf queue after Pdf2SwfListener.process:" + id);
			}
		}
	}
}
