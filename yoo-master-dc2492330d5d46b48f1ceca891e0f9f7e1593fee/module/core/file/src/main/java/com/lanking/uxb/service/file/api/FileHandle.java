package com.lanking.uxb.service.file.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.uxb.service.file.ex.FileException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
public interface FileHandle {

	public static final String FILE_NAME = "file";

	boolean accept(FileType fileType);

	void preUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException;

	void postUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException;

	void afterUpload(File file) throws FileException;

}
