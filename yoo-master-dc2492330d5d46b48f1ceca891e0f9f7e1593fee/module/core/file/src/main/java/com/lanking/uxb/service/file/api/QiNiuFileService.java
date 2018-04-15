package com.lanking.uxb.service.file.api;

import java.util.Collection;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
public interface QiNiuFileService {

	/**
	 * 根据场景获得token
	 *
	 * @param scene
	 *            场景
	 * @return token
	 */
	String getUpToken(String scene);

	/**
	 * 根据场景获得空间
	 *
	 * @param scene
	 *            空间
	 * @return bucket
	 */
	String getBucket(String scene);

	String getDownloadUrl(String key);

	List<String> getDownloadUrls(Collection<String> keys);

}
