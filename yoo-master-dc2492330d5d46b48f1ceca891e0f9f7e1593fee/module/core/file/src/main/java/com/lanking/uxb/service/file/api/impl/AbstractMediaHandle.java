package com.lanking.uxb.service.file.api.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.MediaHandle;

/**
 * 完成实现音频操作的公共操作
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月3日
 *
 */
public abstract class AbstractMediaHandle implements MediaHandle {

	protected static Logger logger = LoggerFactory.getLogger(AbstractMediaHandle.class);

	private String ffmpeg = null;

	protected String ffmpeg() {
		if (ffmpeg == null) {
			ffmpeg = Env.getString("file.ffmpeg.path");
		}
		return ffmpeg;
	}

	@Override
	public int getDuration(File file) {
		String absolutePath = file.getAbsolutePath();
		String result = processInfo(absolutePath, "Duration");
		try {
			String time = result.replace("Duration:", "").split("\\.")[0].trim();
			String[] timeArr = time.split(":");
			int totalTime = Integer.parseInt(timeArr[0]) * 3600 + Integer.parseInt(timeArr[1]) * 60
					+ Integer.parseInt(timeArr[2]);
			return totalTime;
		} catch (Exception e) {
			logger.error("get Duration failed...", e);
		}
		return 0;
	}

	@Override
	public Map<Integer, Integer> getWH(File file) {
		Map<Integer, Integer> wh = Maps.newHashMap();
		String absolutePath = file.getAbsolutePath();
		String result = processInfo(absolutePath, new String[] { "Video", "x" });
		try {
			String[] whArr = result.split("Video:")[1].trim().split(",")[2].trim().split(" ")[0].split("x");
			wh.put(Integer.parseInt(whArr[0]), Integer.parseInt(whArr[1]));
		} catch (Exception e) {
			logger.error("get wh failed...", e);
			wh.put(0, 0);
		}
		return wh;
	}

	@Override
	public void screenshots(File file, int w, int h, int s) {
		if (file.exists()) {
			List<String> commend = new java.util.ArrayList<String>();
			commend.add(ffmpeg());
			commend.add("-i");
			commend.add(file.getAbsolutePath());
			commend.add("-y");
			commend.add("-f");
			commend.add("image2");
			commend.add("-ss");
			commend.add(String.valueOf(s));
			commend.add("-s");
			commend.add(w + "x" + h);
			commend.add(file.getAbsolutePath() + ".jpg");
			try {
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(commend);
				builder.redirectErrorStream(true);
				Process p = builder.start();
				p.waitFor();
			} catch (Exception e) {
				logger.error("screenshots failed...", e);
			}
		} else {
			logger.error("file not exist...");
		}
	}

	private String processInfo(String inputPath, String... keywords) {
		File f = new File(inputPath);
		if (f.exists()) {
			List<String> commend = new java.util.ArrayList<String>();
			commend.add(ffmpeg());
			commend.add("-i");
			commend.add(inputPath);
			try {
				ProcessBuilder builder = new ProcessBuilder();
				builder.command(commend);
				builder.redirectErrorStream(true);
				Process p = builder.start();
				BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
				String line = null;
				while ((line = buf.readLine()) != null) {
					boolean contains = true;
					for (String keyword : keywords) {
						if (!line.contains(keyword)) {
							contains = false;
							break;
						}
					}
					if (contains) {
						break;
					}

				}
				p.waitFor();
				return line;
			} catch (Exception e) {
				logger.error("get uploadfile information failed...", e);
				return null;
			}
		} else {
			logger.error("file not exist...");
			return null;
		}
	}

	protected String processTime(int second) {
		return String.format("%02d", second / 3600) + ":" + String.format("%02d", second % 3600 / 60) + ":"
				+ String.format("%02d", second % 3600 % 60);
	}

}
