package com.lanking.uxb.service.file.api.impl.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.DocumentHandle;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.cache.DocumentHandleCacheService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.file.util.SmbFileUtil;

/**
 * doc 转 pdf 监听
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年6月24日
 */
@Component
@ConditionalOnExpression("${file.doc2pdf.convert.msoffice}")
public class Doc2PdfListener {
	private Logger logger = LoggerFactory.getLogger(Doc2PdfListener.class);

	@Autowired
	@Qualifier("windowDocumentHandle")
	private DocumentHandle documentHandle;
	@Autowired
	private DocumentHandleCacheService dhCacheService;
	@Autowired
	private FileService fileService;

	@Scheduled(initialDelay = 3000, fixedDelay = 1)
	void process() {
		Long id = dhCacheService.getDoc2Pdf();
		try {
			if (id != null) {
				File file = fileService.getFile(id);
				logger.info(FileUtil.getFilePath(file));
				String remoteUrl = SmbFileUtil.getFileSmbPath(file);
				String smbDirPath = Env.getString("file.smb.processPath");
				java.io.File smbDirFile = new java.io.File(smbDirPath);
				if (!smbDirFile.exists()) {
					smbDirFile.mkdirs();
				}
				SmbFileUtil.getSmbFile(remoteUrl, smbDirFile.getAbsolutePath(), file.getExt());
				String smbFilePath = smbDirFile.getAbsolutePath() + java.io.File.separator
						+ Codecs.md5Hex(String.valueOf(file.getId())) + "." + file.getExt();
				documentHandle.convertDOC2PDF(smbFilePath, file);
				new java.io.File(smbFilePath).delete();
				String smbPdfFilePath = smbDirFile.getAbsolutePath() + java.io.File.separator
						+ Codecs.md5Hex(String.valueOf(file.getId())) + "." + FileExt.PDF;
				SmbFileUtil.pubSmbFile(SmbFileUtil.getFileSmbDir(file), smbPdfFilePath);
				new java.io.File(smbPdfFilePath).delete();
			}
		} catch (Exception e) {
			logger.error("doc 2 pdf error:", e);
		} finally {
			if (id != null) {
				dhCacheService.popDoc2Pdf();
				logger.info("pop file from doc2pdf queue after Doc2PdfListener.process:" + id);
				dhCacheService.pushPdf2Swf(id);
				logger.info("push file to pdf2swf queue after Doc2PdfListener.process:" + id);
			}
		}
	}
}
