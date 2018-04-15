package com.lanking.uxb.service.thirdparty.api;

import javax.servlet.http.HttpServletRequest;

public interface ThirdpartyService {
	/**
	 * 第三方退出（需要在本地退出之前执行）.
	 */
	void logout();

	/**
	 * 下载第三方图片文件并上传至本地服务器.
	 * 
	 * @param url
	 *            图片链接地址.
	 * @param upurl
	 *            上传图片路径
	 * @return 图片在本地服务器上的ID
	 */
	long loadThirdImage(String url, String upurl, HttpServletRequest request) throws Exception;
}
