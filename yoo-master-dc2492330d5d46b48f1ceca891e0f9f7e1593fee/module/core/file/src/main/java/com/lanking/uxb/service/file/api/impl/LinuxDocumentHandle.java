package com.lanking.uxb.service.file.api.impl;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.ex.FileException;

@Component("linuxDocumentHandle")
public class LinuxDocumentHandle extends AbtractDocumentHandle {

	private Logger logger = LoggerFactory.getLogger(LinuxDocumentHandle.class);

	@Override
	public void convertDOC2PDF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		try {
			File file = new File(filePath);
			String shell2pdf = Env.getString("file.doc2pdf.convert") + " " + file.getParent() + " " + filePath;
			runShell(shell2pdf);
		} catch (Exception e) {
			logger.error("convert doc 2 pdf error:", e);
		}

	}

	@Override
	public void convertPDF2SWF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		File file = new File(filePath);
		String swfFilePath = file.getParent() + File.separator + file.getName().split("\\.")[0] + "." + FileExt.SWF;
		if (new File(swfFilePath).exists()) {
			throw new FileException(FileException.FILE_EXIST);
		}
		try {
			String shell2Swf = Env.getString("file.pdf2swf.convert") + " " + filePath + " -o " + swfFilePath;
			if (StringUtils.isNotBlank(Env.getString("file.pdf2swf.languagedir"))) {
				shell2Swf += " -s languagedir=" + Env.getString("file.pdf2swf.languagedir");
			}
			shell2Swf += " -s flashversion=9";
			runShell(shell2Swf);
		} catch (Exception e) {
			logger.error("convert pdf 2 swf error:", e);
		}

	}

	@Override
	public void convertDOC2SWF(String filePath, com.lanking.cloud.domain.base.file.File f) {
		try {
			File file = new File(filePath);
			String shell2pdf = Env.getString("file.doc2pdf.convert") + " " + file.getParent() + " " + filePath;
			runShell(shell2pdf);
			String pdfFilePath = file.getParent() + File.separator + file.getName().split("\\.")[0] + "." + FileExt.PDF;
			String swfFilePath = file.getParent() + File.separator + file.getName().split("\\.")[0] + "." + FileExt.SWF;
			String shell2Swf = Env.getString("file.pdf2swf.convert") + " " + pdfFilePath + " -o " + swfFilePath;
			if (StringUtils.isNotBlank(Env.getString("file.pdf2swf.languagedir"))) {
				shell2Swf += " -s languagedir=" + Env.getString("file.pdf2swf.languagedir");
			}
			shell2Swf += " -s flashversion=9";
			runShell(shell2Swf);
		} catch (Exception e) {
			logger.error("convert doc 2 swf error:", e);
		}

	}

	private void runShell(String shell) {
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shell }, null, null);
			process.waitFor();
		} catch (Exception e) {
			logger.error("execute command fail:", e);
		}
	}

}
