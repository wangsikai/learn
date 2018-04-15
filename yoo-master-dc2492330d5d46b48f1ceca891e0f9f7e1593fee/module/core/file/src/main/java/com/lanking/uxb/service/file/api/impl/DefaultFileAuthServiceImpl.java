package com.lanking.uxb.service.file.api.impl;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.FileAuthService;
import com.lanking.uxb.service.file.ex.FileException;

@Service
public class DefaultFileAuthServiceImpl implements FileAuthService {

	private static String FS_REFERER = null;
	private static Boolean FS_CHECKREFERER = null;

	@PostConstruct
	void init() {
		FS_REFERER = Env.getString("fs.referer");
		FS_CHECKREFERER = Env.getBoolean("fs.checkReferer");
	}

	@Override
	public void authorization(long id, long userId) throws FileException {
	}

	@Override
	public void authorization(HttpServletRequest request, HttpServletResponse response) throws FileException {
		// if (!Security.isLogin()) {
		// throw new FileStoreException(FileStoreException.NO_PERMISSON);
		// }
		// if (FS_CHECKREFERER) {
		// String referer = request.getHeader("referer");
		// if (StringUtils.isBlank(referer) || !referer.contains(FS_REFERER)) {
		// throw new FileStoreException(FileStoreException.NO_PERMISSON);
		// }
		// }
	}

}
