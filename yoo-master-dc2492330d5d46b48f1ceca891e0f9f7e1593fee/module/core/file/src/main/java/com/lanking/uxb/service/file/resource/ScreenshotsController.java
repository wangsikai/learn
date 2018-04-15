package com.lanking.uxb.service.file.resource;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.domain.base.file.api.CropModel;
import com.lanking.cloud.domain.base.session.api.Cookies;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.web.WebUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.ScreenshotUtil;

/**
 * 截图相关接口
 * 
 * @since 4.4.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年8月21日
 */
@RestController
@RequestMapping({ "f/up", "fs/up" })
public class ScreenshotsController extends FileBaseController {

	private Logger logger = LoggerFactory.getLogger(ScreenshotsController.class);

	@PostConstruct
	void init() throws Exception {
		// create directory
		java.io.File baseFileDir = new java.io.File(Env.getString("file.phantomjs.path"));
		baseFileDir.mkdirs();
		// delete old js
		for (java.io.File f : baseFileDir.listFiles()) {
			f.delete();
		}
		// load js resources
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = null;
		try {
			resources = resolver.getResources("classpath:phantomjs/*.js");
		} catch (IOException e) {
			logger.error("load js resources error:", e);
		}
		// copy js
		if (resources != null) {
			for (Resource resource : resources) {
				FileWriter fw = null;
				BufferedReader br = null;
				try {
					java.io.File jsFile = new java.io.File(baseFileDir, resource.getFilename());
					jsFile.createNewFile();
					fw = new FileWriter(jsFile, false);
					br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
					String s = "";
					while ((s = br.readLine()) != null) {
						fw.write(s + "\n");
					}
					br.close();
					fw.close();
				} catch (IOException ignore) {
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException ignore) {
						}
					}
					if (fw != null) {
						try {
							fw.close();
						} catch (IOException ignore) {
						}
					}
				}
			}
		}

	}

	/**
	 * 服务端截图接口
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "screenShot", method = { RequestMethod.GET, RequestMethod.POST })
	public Value screenShot(HttpServletRequest request, HttpServletResponse response, String url) {
		String st = WebUtils.getCookie(request, Cookies.SECURITY_TOKEN).getValue();
		String domain = null;
		url = URLDecoder.decode(url);
		// 取domain
		try {
			domain = new URL(url).getHost();
		} catch (MalformedURLException e) {
		}

		String jsName = "screenShot.js";

		String imageChar = ScreenshotUtil.getImageBase64(url, st, domain, jsName);
		Value value = new Value();
		if (imageChar != null && !"".equals(imageChar)) {
			// 文件放到服务器上
			Space space = spaceService.getSpace("post-img");
			File file = new File();
			file.setSpace(space);
			file.setPreview(space.isPreview());
			file.setCrop(false);
			file.setCropModel(new CropModel());
			file.setBase64Size(Long.valueOf(imageChar.length()));
			file.setBase64Type("jpeg");
			file.setBase64Data(imageChar);

			try {
				defaultFileHandle.preUpload(file, request, response);
				defaultFileHandle.postUpload(file, request, response);
				defaultFileHandle.afterUpload(file);
				value = new Value(fileConverter.to(file));
			} catch (FileException e) {
				logger.error("upload fail:", e);
				value = new Value(e.getCode(), e.getMessage());
			} catch (AbstractException e) {
				logger.error("upload fail:", e);
				value = new Value(e.getCode(), e.getMessage());
			} catch (Exception e) {
				logger.error("upload fail:", e);
				value = new Value(new ServerException());
			}
		}

		return value;
	}
}
