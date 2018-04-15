package com.lanking.uxb.service.file.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lanking.uxb.service.file.ex.FileException;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年3月20日
 */
public interface FileAuthService {

	void authorization(long id, long userId) throws FileException;

	void authorization(HttpServletRequest request, HttpServletResponse response) throws FileException;
}
