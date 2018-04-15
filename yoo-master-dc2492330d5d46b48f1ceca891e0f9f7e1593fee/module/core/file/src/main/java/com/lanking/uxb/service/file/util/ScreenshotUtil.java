package com.lanking.uxb.service.file.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.springboot.environment.Env;

/**
 * 截图工具类
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月22日
 */
public class ScreenshotUtil {

	private static Logger logger = LoggerFactory.getLogger(ScreenshotUtil.class);

	/**
	 * 调用js对网页截图，生成base64类型图片
	 *
	 * @return base64类型图片
	 */
	public static String getImageBase64(String url, String st, String domain, String jsName) {
		// windows下phantomjs位置
		// String path = "F:/phantomjs-2.1.1-windows/bin/";
		String jsPath = Env.getString("file.phantomjs.path");
		// 拼接url
		StringBuilder builder = new StringBuilder();
		// builder.append(path);
		builder.append("phantomjs");
		builder.append(" ");
		builder.append(jsPath);
		builder.append(File.separator);
		builder.append(jsName);
		builder.append(" ");
		builder.append(url);
		builder.append(" ");
		builder.append(st);
		builder.append(" ");
		builder.append(domain);

		Runtime rt = Runtime.getRuntime();
		Process process = null;
		try {
			process = rt.exec(builder.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		InputStream is = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder imageChar = new StringBuilder();
		String tmp = "";
		try {
			while ((tmp = reader.readLine()) != null) {
				imageChar.append(tmp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return imageChar.toString();
	}

}
