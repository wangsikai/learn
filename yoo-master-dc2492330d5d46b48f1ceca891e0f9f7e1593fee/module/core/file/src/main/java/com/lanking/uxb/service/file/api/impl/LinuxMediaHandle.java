package com.lanking.uxb.service.file.api.impl;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;

@Component("linuxMediaHandle")
public class LinuxMediaHandle extends AbstractMediaHandle {

	@Override
	public void convert(String srcFilePath, String destFilePath, FileType type) {
		try {
			String shell = null;
			if (type == FileType.AUDIO) {
				if (StringUtils.isNotBlank(Env.getString("file.audio.convert"))) {
					shell = ffmpeg() + " -i " + srcFilePath + " " + Env.getString("file.audio.convert") + " "
							+ destFilePath;
				} else {
					shell = ffmpeg() + " -i " + srcFilePath + " " + destFilePath;
				}
			} else if (type == FileType.VIDEO) {
				if (StringUtils.isNotBlank(Env.getString("file.video.convert"))) {
					shell = ffmpeg() + " -i " + srcFilePath + " " + Env.getString("file.video.convert") + " "
							+ destFilePath;
				} else {
					shell = ffmpeg() + " -i " + srcFilePath + " " + destFilePath;
				}
			}
			if (shell != null) {
				logger.info("convert start:{}", srcFilePath);
				runShell(shell);
				logger.info("convert end:{}", destFilePath);
			}
		} catch (Exception e) {
			logger.error("convert fail:", e);
		}
	}

	@Override
	public void segment(String srcFilePath, String destFilePath, FileType type, int start, int end) {
		try {
			String startTime = processTime(start);
			String endTime = processTime(end);
			String shell = null;
			if (type == FileType.AUDIO) {
				shell = ffmpeg() + " -ss " + startTime + " -i " + srcFilePath + " "
						+ Env.getString("file.audio.segment") + " -t " + endTime + " " + destFilePath;
			} else if (type == FileType.VIDEO) {
				shell = ffmpeg() + " -ss " + startTime + " -i " + srcFilePath + " "
						+ Env.getString("file.video.segment") + " -t " + endTime + " " + destFilePath;
			}
			if (shell != null) {
				runShell(shell);
			}
		} catch (Exception e) {
			logger.error("segment fail:", e);
		}
	}

	private void runShell(String shell) {
		try {
			Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shell }, null, null);
			// InputStream stderr = process.getErrorStream();
			// InputStreamReader isr = new InputStreamReader(stderr);
			// BufferedReader br = new BufferedReader(isr);
			// String line = null;
			// while ((line = br.readLine()) != null) {
			// logger.info(line);
			// }
			process.waitFor();
		} catch (Exception e) {
			logger.error("execute command fail:", e);
		}
	}

}
