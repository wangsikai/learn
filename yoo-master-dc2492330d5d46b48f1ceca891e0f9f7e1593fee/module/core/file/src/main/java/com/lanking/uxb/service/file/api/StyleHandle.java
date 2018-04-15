package com.lanking.uxb.service.file.api;

import com.lanking.cloud.domain.base.file.FileStyle;
import com.lanking.uxb.service.file.ex.FileException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月1日
 *
 */
public interface StyleHandle {

	void imageStyleHandle(FileStyle style, String srcPath, String destPath) throws FileException;
}
