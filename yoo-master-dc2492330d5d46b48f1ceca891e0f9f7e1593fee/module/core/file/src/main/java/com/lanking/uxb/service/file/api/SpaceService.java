package com.lanking.uxb.service.file.api;

import com.lanking.cloud.domain.base.file.Space;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月27日
 *
 */
public interface SpaceService {

	Space getSpace(String name);

	Space getSpace(long spaceId);

	Space saveSpace(Space space);

	void updateSpace(long id, int incrNum, long incrSize);
}
