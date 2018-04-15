package com.lanking.uxb.service.file.api.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;

@Component("windowMediaHandle")
public class WindowMediaHandle extends AbstractMediaHandle {

	@Override
	public void convert(String srcFilePath, String destFilePath, FileType type) {
		try {
			String cmd = null;
			if (type == FileType.AUDIO) {
				if (StringUtils.isNotBlank(Env.getString("file.audio.convert"))) {
					cmd = ffmpeg() + " -i " + srcFilePath + " " + Env.getString("file.audio.convert") + " "
							+ destFilePath;
				} else {
					cmd = ffmpeg() + " -i " + srcFilePath + " " + destFilePath;
				}

			} else if (type == FileType.VIDEO) {
				if (StringUtils.isNotBlank(Env.getString("file.video.convert"))) {
					cmd = ffmpeg() + " -i " + srcFilePath + " " + Env.getString("file.video.convert") + " "
							+ destFilePath;
				} else {
					cmd = ffmpeg() + " -i " + srcFilePath + " " + destFilePath;
				}

			}
			if (cmd != null) {
				exeCmd(cmd);
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
			String cmd = null;
			if (type == FileType.AUDIO) {
				cmd = ffmpeg() + " -ss " + startTime + " -i " + srcFilePath + " " + Env.getString("file.audio.segment")
						+ " -t " + endTime + " " + destFilePath;
			} else if (type == FileType.VIDEO) {
				cmd = ffmpeg() + " -ss " + startTime + " -i " + srcFilePath + " " + Env.getString("file.video.segment")
						+ " -t " + endTime + " " + destFilePath;
			}
			if (cmd != null) {
				exeCmd(cmd);
			}
		} catch (Exception e) {
			logger.error("segment fail:", e);
		}
	}

	private void exeCmd(String cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			InputStream stderr = process.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				logger.info(line);
			}
			process.waitFor();
		} catch (Exception e) {
			logger.error("execute command fail:", e);
		}
	}

}
