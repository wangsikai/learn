package com.lanking.uxb.service.file.resource;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.lanking.uxb.service.file.api.FileAuthService;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.api.FileStyleService;
import com.lanking.uxb.service.file.api.SpaceService;
import com.lanking.uxb.service.file.api.StyleHandle;
import com.lanking.uxb.service.file.cache.DocumentHandleCacheService;
import com.lanking.uxb.service.file.convert.FileConverter;

/**
 * 文件系统restful api基类
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月19日
 */
@MappedSuperclass
public class FileBaseController {

	@Autowired
	FileService fileService;
	@Autowired
	FileConverter fileConverter;
	@Autowired
	FileAuthService fileAuthService;
	@Autowired
	SpaceService spaceService;
	@Autowired
	FileStyleService fileStyleService;
	@Autowired
	StyleHandle styleHandle;
	@Autowired
	@Qualifier("defaultFileHandle")
	FileHandle defaultFileHandle;
	@Autowired
	DocumentHandleCacheService dhCacheService;

}
