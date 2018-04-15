package com.lanking.uxb.service.file.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import com.lanking.cloud.domain.base.file.File;

/**
 * 文件相关接口
 * 
 * @since yoomath V1.9
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年1月19日
 */
public interface FileService {
	File getFile(long id);

	Map<Long, File> mgetFile(Collection<Long> ids);

	File saveFile(File file);

	File updateFileScreenshot(long id, long screenshotId);

	File saveInputStreamFile(File file, InputStream inputStream);

	File saveDiskImage(String spaceName, java.io.File diskFile, boolean deleteDiskFile) throws IOException;

	File saveUrlImage(File file, String url);

	void delete(long id);

	void delete(Collection<Long> ids);

	File findByMd5(long spaceId, String md5);

	void reference(long id);
}
