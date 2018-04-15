package com.lanking.uxb.service.file.api;

import com.lanking.cloud.domain.base.file.FileStyle;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月27日
 *
 */
public interface FileStyleService {
	FileStyle save(FileStyle fileStyle);

	FileStyle find(long spaceId, int mode, int width, int height, int quality);

	FileStyle find(Long spaceId, String name);
}
