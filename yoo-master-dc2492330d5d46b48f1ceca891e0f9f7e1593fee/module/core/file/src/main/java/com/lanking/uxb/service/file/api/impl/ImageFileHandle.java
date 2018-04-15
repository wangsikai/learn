package com.lanking.uxb.service.file.api.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectory;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.base.file.FileType;
import com.lanking.cloud.sdk.util.UUID;
import com.lanking.uxb.service.file.api.FileHandle;
import com.lanking.uxb.service.file.api.ImageTransform;
import com.lanking.uxb.service.file.ex.FileException;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年10月28日
 *
 */
@Component
public class ImageFileHandle implements FileHandle {

	private Logger logger = LoggerFactory.getLogger(ImageFileHandle.class);

	@Override
	public boolean accept(FileType fileType) {
		return fileType == FileType.IMAGE;
	}

	@Override
	public void preUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
	}

	@Override
	public void postUpload(File file, HttpServletRequest request, HttpServletResponse response) throws FileException {
		ImageTransform itf = getImageTransform();
		try {
			itf.load(new FileInputStream(new java.io.File(file.getNativePath())));
			file.setWidth(itf.getSize().getWidth());
			file.setHeight(itf.getSize().getHeight());
		} catch (IOException | NullPointerException e) {
			logger.error("image transform load error:", e);
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void afterUpload(File file) throws FileException {
		// 校正图片
		if (file.isCorrection()) {
			try {
				String filePath = FileUtil.getFilePath(file);
				java.io.File jpegFile = new java.io.File(filePath);
				Metadata metadata = JpegMetadataReader.readMetadata(jpegFile);
				Directory exif = metadata.getDirectory(ExifDirectory.class);
				Iterator tags = exif.getTagIterator();
				int angle = 0;
				while (tags.hasNext()) {
					Tag tag = (Tag) tags.next();
					// [Exif] Orientation - Bottom, right side (Rotate 180)
					// [exif] orientation - right side, top (rotate 90 cw)
					String info = tag.toString().toLowerCase();
					if (info.contains("orientation")) {
						if (info.contains("rotate")) {
							Pattern p = Pattern.compile("\\(rotate (\\d+.)(.*)\\)");
							Matcher m = p.matcher(info);
							while (m.find()) {
								angle = Integer.parseInt(m.group(1).trim());
								break;
							}
						}
					}
				}
				if (angle != 0) {
					String tmpFilePath = UUID.uuid() + "." + file.getExt();
					ImageTransform aif = getImageTransform();
					aif.load(filePath);
					aif.rotate(angle);
					aif.save(tmpFilePath);
					aif.close();
					new java.io.File(filePath).delete();
					java.io.File tmpFile = new java.io.File(tmpFilePath);
					tmpFile.renameTo(new java.io.File(filePath));
				}
			} catch (Exception e) {
				logger.error("rotate image fail:", e);
			}
		}
	}

	private ImageTransform getImageTransform() {
		return new AwtImageTransform();
	}

}
