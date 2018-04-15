package com.lanking.uxb.service.file.api.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.FileStyle;
import com.lanking.cloud.domain.base.file.StyleMode;
import com.lanking.uxb.service.file.api.ImageTransform;
import com.lanking.uxb.service.file.api.StyleHandle;
import com.lanking.uxb.service.file.ex.FileException;

@Component
public class StyleHandleImpl implements StyleHandle {

	private Logger logger = LoggerFactory.getLogger(StyleHandleImpl.class);

	@Override
	public void imageStyleHandle(FileStyle style, String srcPath, String destPath) throws FileException {
		File f = new File(destPath);
		if (!f.exists()) {
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			try {
				if (style.getMode() == StyleMode.MAX_EDGE) {
					dealMaxEdge(style, srcPath, destPath);
				} else if (style.getMode() == StyleMode.MAX_W_H_CUT) {
					dealMaxWHCut(style, srcPath, destPath);
				} else if (style.getMode() == StyleMode.MAX_W_H) {
					dealMaxWH(style, srcPath, destPath);
				} else if (style.getMode() == StyleMode.QUALITY_COMPRESS) {
					dealQualityCompress(style, srcPath, destPath);
				}
			} catch (Exception e) {
				logger.debug("convert ex to uxbex...");
				throw new FileException(FileException.IMAGE_STYLE_DEAL_ERROR);
			}
		}
	}

	private void dealMaxEdge(FileStyle style, String srcPath, String destPath) throws IOException {
		ImageTransform itf = getImageTransform();
		itf.load(srcPath);
		itf.resizeWithMaxEdge(style.getWidth() <= 0 ? Integer.MAX_VALUE : style.getWidth(),
				style.getHeight() <= 0 ? Integer.MAX_VALUE : style.getHeight());
		itf.save(destPath);
		itf.close();
	}

	private void dealMaxWHCut(FileStyle style, String srcPath, String destPath) throws IOException {
		ImageTransform itf = getImageTransform();
		itf.load(srcPath);
		itf.resizeWithMinAndCrop(style.getWidth(), style.getHeight());
		itf.save(destPath);
		itf.close();
	}

	private void dealMaxWH(FileStyle style, String srcPath, String destPath) throws IOException {
		ImageTransform itf = getImageTransform();
		itf.load(srcPath);
		itf.resizeWithMax(style.getWidth() <= 0 ? Integer.MAX_VALUE : style.getWidth(),
				style.getHeight() <= 0 ? Integer.MAX_VALUE : style.getHeight());
		itf.save(destPath);
		itf.close();
	}

	private void dealQualityCompress(FileStyle style, String srcPath, String destPath) throws IOException {
		ImageTransform itf = getImageTransform();
		itf.load(srcPath);
		itf.compressImageWithSave(destPath, "jpg", style.getQuality() * 1f / 100);
		itf.close();
	}

	private ImageTransform getImageTransform() {
		return new AwtImageTransform();
	}
}
