package com.lanking.uxb.service.file.api;

import java.io.IOException;
import java.util.Set;

import com.google.common.collect.Sets;
import com.lanking.cloud.domain.base.file.File;

/**
 * 文档操作类
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年5月12日
 */
public interface DocumentHandle {

	Set<String> WORD = Sets.newHashSet("doc", "docx", "rtf", "txt");
	Set<String> EXCEL = Sets.newHashSet("xls", "xlsx", "csv");
	Set<String> POWERPOINT = Sets.newHashSet("ppt", "pptx", "pps", "ppsx", "pot", "potx");

	void convertDOC2PDF(String filePath, File f);

	void convertPDF2SWF(String filePath, File f);

	void convertDOC2SWF(String filePath, File f);

	java.io.File pdf2Image(java.io.File file) throws IOException;
}
