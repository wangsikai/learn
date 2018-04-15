package com.lanking.uxb.service.file.api.impl;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.domain.base.file.api.FileConstants;
import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.api.MediaHandle;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Component
public class AudioFileHandle implements FileHandle {

	private Logger logger = LoggerFactory.getLogger(AudioFileHandle.class);

	@Autowired
	@Qualifier("linuxMediaHandle")
	private MediaHandle linuxMediaHandle;
	@Autowired
	@Qualifier("windowMediaHandle")
	private MediaHandle windowMediaHandle;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	private boolean isWin = false;
	private int segmentTime = 300;

	@PostConstruct
	void init() {
		isWin = System.getProperty("os.name").toLowerCase().contains("windows");
		segmentTime = Env.getInt("file.audio.segment.time");
	}

	@Override
	public boolean accept(FileType fileType) {
		return fileType == FileType.AUDIO;
	}

	@Override
	public void preUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
	}

	@Override
	public void postUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
		if (isWin) {
			file.setDuration(windowMediaHandle.getDuration(new java.io.File(file.getNativePath())));
		} else {
			file.setDuration(linuxMediaHandle.getDuration(new java.io.File(file.getNativePath())));
		}
	}

	@Override
	public void afterUpload(final File file) throws FileException {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					java.io.File nativeFile = FileUtil.getNativeFile(file);
					java.io.File tempNativeFile = new java.io.File(
							nativeFile.getAbsolutePath() + FileExt.SEPARATOR + FileUtil.getSimpleExt(file.getName()));
					Files.copy(nativeFile, tempNativeFile);
					java.io.File tempMp3File = new java.io.File(
							nativeFile.getParent() + java.io.File.separator + nativeFile.getName()
									+ FileConstants.PROCESS_SEPARATOR + FileExt.MP3 + FileExt.SEPARATOR + FileExt.MP3);
					if (isWin) {
						windowMediaHandle.convert(tempNativeFile.getAbsolutePath(), tempMp3File.getAbsolutePath(),
								FileType.AUDIO);
					} else {
						linuxMediaHandle.convert(tempNativeFile.getAbsolutePath(), tempMp3File.getAbsolutePath(),
								FileType.AUDIO);
					}
					tempNativeFile.delete();
					// convert to mp4
					java.io.File tempMp4File = new java.io.File(
							nativeFile.getParent() + java.io.File.separator + nativeFile.getName()
									+ FileConstants.PROCESS_SEPARATOR + FileExt.MP4 + FileExt.SEPARATOR + FileExt.MP4);
					if (isWin) {
						windowMediaHandle.convert(tempMp3File.getAbsolutePath(), tempMp4File.getAbsolutePath(),
								FileType.AUDIO);
					} else {
						linuxMediaHandle.convert(tempMp3File.getAbsolutePath(), tempMp4File.getAbsolutePath(),
								FileType.AUDIO);
					}
					// 预览
					if (file.isPreview()) {
						java.io.File tempMp4PreviewFile = new java.io.File(nativeFile.getParent()
								+ java.io.File.separator + nativeFile.getName() + FileConstants.PROCESS_SEPARATOR
								+ FileExt.MP4 + FileConstants.PROCESS_SEPARATOR + FileConstants.PREVIEW_FILE_SUFFIX
								+ FileExt.SEPARATOR + FileExt.MP4);
						if (isWin) {
							windowMediaHandle.segment(tempMp4File.getAbsolutePath(),
									tempMp4PreviewFile.getAbsolutePath(), FileType.AUDIO, 0,
									file.getDuration() > segmentTime ? segmentTime : file.getDuration() - 1);
						} else {
							windowMediaHandle.segment(tempMp4File.getAbsolutePath(),
									tempMp4PreviewFile.getAbsolutePath(), FileType.AUDIO, 0,
									file.getDuration() > segmentTime ? segmentTime : file.getDuration() - 1);
						}
						java.io.File mp4PreviewFile = new java.io.File(nativeFile.getParent() + java.io.File.separator
								+ nativeFile.getName() + FileConstants.PROCESS_SEPARATOR + FileExt.MP4
								+ FileConstants.PROCESS_SEPARATOR + FileConstants.PREVIEW_FILE_SUFFIX);
						tempMp4PreviewFile.renameTo(mp4PreviewFile);
					}
					java.io.File mp4File = new java.io.File(nativeFile.getParent() + java.io.File.separator
							+ nativeFile.getName() + FileConstants.PROCESS_SEPARATOR + FileExt.MP4);
					tempMp4File.renameTo(mp4File);
					int fileSystemMode = Env.getInt("file.audio.convert.mode");
					if (fileSystemMode == 1) {
						if (file.isPreview()) {
							java.io.File tempMp3PreviewFile = new java.io.File(nativeFile.getParent()
									+ java.io.File.separator + nativeFile.getName() + FileConstants.PROCESS_SEPARATOR
									+ FileExt.MP3 + FileConstants.PROCESS_SEPARATOR + FileConstants.PREVIEW_FILE_SUFFIX
									+ FileExt.SEPARATOR + FileExt.MP3);
							if (isWin) {
								windowMediaHandle.segment(tempMp3File.getAbsolutePath(),
										tempMp3PreviewFile.getAbsolutePath(), FileType.AUDIO, 0,
										file.getDuration() > segmentTime ? segmentTime : file.getDuration() - 1);
							} else {
								windowMediaHandle.segment(tempMp3File.getAbsolutePath(),
										tempMp3PreviewFile.getAbsolutePath(), FileType.AUDIO, 0,
										file.getDuration() > segmentTime ? segmentTime : file.getDuration() - 1);
							}
							java.io.File mp3PreviewFile = new java.io.File(
									nativeFile.getParent() + java.io.File.separator + nativeFile.getName()
											+ FileConstants.PROCESS_SEPARATOR + FileExt.MP3
											+ FileConstants.PROCESS_SEPARATOR + FileConstants.PREVIEW_FILE_SUFFIX);
							tempMp3PreviewFile.renameTo(mp3PreviewFile);
						}
						java.io.File mp3File = new java.io.File(nativeFile.getParent() + java.io.File.separator
								+ nativeFile.getName() + FileConstants.PROCESS_SEPARATOR + FileExt.MP3);
						tempMp3File.renameTo(mp3File);
					} else if (fileSystemMode == 2) {
						tempMp3File.delete();
					}

				} catch (IOException e) {
					logger.error("convert fail:", e);
				}
			}
		});
	}
}
