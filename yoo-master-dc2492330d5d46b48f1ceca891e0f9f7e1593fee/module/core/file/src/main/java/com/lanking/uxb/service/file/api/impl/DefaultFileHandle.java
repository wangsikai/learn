package com.lanking.uxb.service.file.api.impl;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.ex.FileException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Component("defaultFileHandle")
public class DefaultFileHandle extends AbstractFileHandle implements ApplicationContextAware, InitializingBean {

	private Logger logger = LoggerFactory.getLogger(DefaultFileHandle.class);

	private ApplicationContext appContext;

	private List<FileHandle> fileHandles = Lists.newArrayList();

	@Override
	public boolean accept(FileType fileType) {
		return fileType == null;
	}

	@Override
	public void preUpload(File file, HttpServletRequest request, HttpServletResponse response)
			throws FileException {
		super.preUpload(file, request, response);
		for (FileHandle fileHandle : fileHandles) {
			if (fileHandle.accept(file.getType())) {
				fileHandle.preUpload(file, request, response);
			}
		}
	}

	@Override
	public void postUpload(File file, HttpServletRequest request, HttpServletResponse response)
			throws FileException {
		super.postUpload(file, request, response);
		for (FileHandle fileHandle : fileHandles) {
			if (fileHandle.accept(file.getType())) {
				fileHandle.postUpload(file, request, response);
			}
		}
	}

	@Override
	public void afterUpload(File file) throws FileException {
		super.afterUpload(file);
		for (FileHandle fileHandle : fileHandles) {
			if (fileHandle.accept(file.getType())) {
				fileHandle.afterUpload(file);
			}
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.appContext = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (FileHandle fileHandle : appContext.getBeansOfType(FileHandle.class).values()) {
			logger.debug("{} bean is init", fileHandle.getClass().getName());
			fileHandles.add(fileHandle);
		}
	}

}
