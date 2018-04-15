package com.lanking.uxb.service.file.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;

/**
 * 兼容导入数据的文件访问
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年4月21日
 */
@RestController
public class HopeFileController extends FileBaseController {
	private Logger logger = LoggerFactory.getLogger(HopeFileController.class);

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "dd")
	public void image(String fn, HttpServletRequest request, HttpServletResponse response) {
		String path = getHopePath(fn);
		File file = new File(path);
		OutputStream os = null;
		FileInputStream fips = null;
		ByteArrayOutputStream bops = null;
		try {
			os = response.getOutputStream();
			fips = new FileInputStream(file);
			bops = new ByteArrayOutputStream();
			int data = -1;
			while ((data = fips.read()) != -1) {
				bops.write(data);
			}
			os.write(bops.toByteArray());
			os.flush();
			os.close();
		} catch (Exception e) {
			logger.error("file not found,path:{}", path);
			logger.debug("out put image stream error:", e);
		} finally {
			try {
				if (bops != null) {
					bops.close();
				}
				if (fips != null) {
					fips.close();
				}
				os.close();
			} catch (IOException e) {
				logger.error("fileInputStream close error:", e);
			}
		}
	}

	public static String getHopePath(String fn) {
		long time = Long.parseLong(StringUtils.substringBefore(fn, "."));
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		String path = Env.getString("file.store.hope.path") + File.separator + c.get(Calendar.YEAR) + File.separator
				+ String.format("%02d", (c.get(Calendar.MONTH) + 1)) + String.format("%02d", c.get(Calendar.DATE))
				+ File.separator + fn;
		return path;
	}
}
