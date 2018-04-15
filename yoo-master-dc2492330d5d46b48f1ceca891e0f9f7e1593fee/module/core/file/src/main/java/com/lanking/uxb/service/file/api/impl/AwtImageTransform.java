package com.lanking.uxb.service.file.api.impl;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lanking.cloud.domain.base.file.api.Dimension;
import com.lanking.cloud.domain.base.file.api.ImageType;
import com.lanking.cloud.domain.base.file.api.Position;
import com.lanking.uxb.service.file.api.ImageTransform;

public class AwtImageTransform implements ImageTransform {

	private Logger logger = LoggerFactory.getLogger(AwtImageTransform.class);
	protected BufferedImage srcBImage;
	protected BufferedImage destBImage;
	protected int imgWidth;
	protected int imgHeight;
	protected boolean modified = false;
	protected String formatName;

	protected String tmpPath = null;
	protected FileInputStream srcIs;

	@Override
	public void load(String srcPath) throws IOException {
		srcIs = new FileInputStream(srcPath);
		load(srcIs);
	}

	@Override
	public void save(String destPath) throws IOException {
		if (destBImage == null) {
			destBImage = srcBImage;
		}
		ImageIO.write(destBImage, formatName, new File(destPath));
		srcIs.close();
		if (tmpPath != null) {
			new File(tmpPath).delete();
		}
	}

	@Override
	public void close() {
		if (srcIs != null) {
			try {
				srcIs.close();
			} catch (IOException e) {
				logger.info("close src image error:", e);
			}
		}
	}

	@Override
	public void load(InputStream input) throws IOException {
		convertToBufferedImage(input);
	}

	@Override
	public void save(OutputStream out) throws IOException {
		if (destBImage == null) {
			destBImage = srcBImage;
		}
		ImageIO.write(destBImage, formatName, out);
	}

	public boolean isModified() {
		return modified;
	}

	@Override
	public Dimension getSize() throws IOException {
		return new Dimension(imgWidth, imgHeight);
	}

	@Override
	public void resize(int width, int height) throws IOException {
		zoomImage(width, height);
	}

	@Override
	public void rotate(double rotate) throws IOException {
		if (rotate < 1e-3) {
			return;
		}
		// get radian
		double radian = rotate * Math.PI / 180;
		// get rotated height
		double w = Math.abs(imgHeight * Math.sin(radian)) + Math.abs(imgWidth * Math.cos(radian));
		// get rotated width
		double h = Math.abs(imgHeight * Math.cos(radian)) + Math.abs(imgWidth * Math.sin(radian));
		rotateImage((int) w, (int) h, radian, 1);
	}

	@Override
	public void resizeWithMax(Integer maxWidth, Integer maxHeight) throws IOException {
		Double ratio = null;
		if (maxWidth != null && maxWidth > 0 && imgWidth > maxWidth) {
			ratio = (double) maxWidth / imgWidth;
		}
		if (maxHeight != null && maxHeight > 0 && imgHeight > maxHeight) {
			double yRatio = (double) maxHeight / imgHeight;
			if (ratio == null || yRatio < ratio) {
				ratio = yRatio;
			}
		}
		if (ratio != null) {
			zoomImage((int) (imgWidth * ratio), (int) (imgHeight * ratio));
		}
	}

	@Override
	public void resizeWithMin(Integer maxWidth, Integer maxHeight) throws IOException {
		Double ratio = null;
		if (maxWidth != null && maxWidth > 0 && imgWidth > maxWidth) {
			ratio = (double) maxWidth / imgWidth;
		}
		if (maxHeight != null && maxHeight > 0 && imgHeight > maxHeight) {
			double yRatio = (double) maxHeight / imgHeight;
			if (ratio == null || yRatio > ratio) {
				ratio = yRatio;
			}
		}
		if (ratio != null) {
			zoomImage((int) (imgWidth * ratio), (int) (imgHeight * ratio));
		}
	}

	@Override
	public void resizeWithMinAndCrop(Integer maxWidth, Integer maxHeight) throws IOException {
		if (maxWidth == null && maxHeight == null || (maxWidth != null && maxWidth <= 0)
				|| (maxHeight != null && maxHeight <= 0)) {
			return;
		}
		if (maxWidth == null) {
			maxWidth = maxHeight;
		}
		if (maxHeight == null) {
			maxHeight = maxWidth;
		}
		if (imgWidth >= maxWidth && imgHeight >= maxHeight) {
			Double xRatio = (double) maxWidth / imgWidth;
			Double yRatio = (double) maxHeight / imgHeight;
			Double ratio = Math.max(xRatio, yRatio);
			int nw = (int) (imgWidth * ratio);
			int nh = (int) (imgHeight * ratio);
			zoomImage(nw, nh);
			tmpPath = System.getProperty("java.io.tmpdir") + System.currentTimeMillis();
			ImageIO.write(destBImage, formatName, new File(tmpPath));
			load(tmpPath);
			int left = nw > maxWidth ? (nw - maxWidth) / 2 : 0;
			int top = nh > maxHeight ? (nh - maxHeight) / 2 : 0;
			cropImage(left, top, left == 0 ? nw : maxWidth, top == 0 ? nh : maxHeight);
		} else {
			int width = imgWidth;
			int height = imgHeight;
			if (imgWidth > maxWidth && imgHeight < maxHeight) {
				width = imgHeight * maxWidth / maxHeight;
				cropImage(imgWidth - width, 0, width, height);
			} else if (imgWidth < maxWidth && imgHeight < maxHeight) {
				Double srcRatio = (double) imgWidth / imgHeight;
				Double destRatio = (double) maxWidth / maxHeight;
				if (srcRatio > destRatio) {
					width = imgHeight * maxWidth / maxHeight;
					cropImage(imgWidth - width, 0, width, height);
				} else if (srcRatio < destRatio) {
					height = imgWidth * maxHeight / maxWidth;
					cropImage(0, imgHeight - height, width, height);
				}
			} else if (imgWidth < maxWidth && imgHeight > maxHeight) {
				height = imgWidth * maxHeight / maxWidth;
				cropImage(0, imgHeight - height, width, height);
			}
		}
	}

	@Override
	public void resizeWithMaxEdge(Integer maxWidth, Integer maxHeight) throws IOException {
		if (maxWidth == null || maxHeight == null) {
			if (maxWidth == null && maxHeight != null) {
				if (imgWidth > imgHeight) {
					resizeWithMax(Integer.MAX_VALUE, maxHeight);
				} else {
					resizeWithMax(maxHeight, Integer.MAX_VALUE);
				}
			} else if (maxWidth != null && maxHeight == null) {
				if (imgWidth > imgHeight) {
					resizeWithMax(maxWidth, Integer.MAX_VALUE);
				} else {
					resizeWithMax(Integer.MAX_VALUE, maxWidth);
				}
			}
		} else {
			if (imgWidth > imgHeight) {
				double sRatio = (double) imgWidth / imgHeight;
				double dRatio = (double) maxWidth / maxHeight;
				if (sRatio > dRatio) {
					resizeWithMax(maxWidth, Integer.MAX_VALUE);
				} else {
					resizeWithMax(Integer.MAX_VALUE, maxHeight);
				}
			} else {
				double sRatio = (double) imgHeight / imgWidth;
				double dRatio = (double) maxWidth / maxHeight;
				if (sRatio > dRatio) {
					resizeWithMax(Integer.MAX_VALUE, maxHeight);
				} else {
					resizeWithMax(maxWidth, Integer.MAX_VALUE);
				}
			}
		}

	}

	@Override
	public void rotateWithMax(double rotate, Integer maxWidth, Integer maxHeight) throws IOException {
		if (rotate < 1) {
			return;
		}
		// get radian
		double radian = rotate * Math.PI / 180;
		// get rotated height
		double w = Math.abs(imgHeight * Math.sin(radian)) + Math.abs(imgWidth * Math.cos(radian));
		// get rotated width
		double h = Math.abs(imgHeight * Math.cos(radian)) + Math.abs(imgWidth * Math.sin(radian));
		double ratio = 1;
		if (maxWidth != null && maxWidth > 0) {
			ratio = (double) maxWidth / w;
		}
		if (maxHeight != null && maxHeight > 0) {
			double yRatio = (double) maxHeight / h;
			if (ratio == 1 || yRatio < ratio) {
				ratio = yRatio;
			}
		}
		rotateImage((int) Math.abs(w), (int) Math.abs(h), radian, ratio);
	}

	@Override
	public void crop(int left, int top, Integer width, Integer height) throws IOException {
		if (width == null || width == 0) {
			width = imgWidth - left;
		}
		if (height == null || height == 0) {
			height = imgHeight - top;
		}
		if (width > 0 && height > 0 && width <= imgWidth && height <= imgHeight) {
			cropImage(left, top, width, height);
		}
	}

	@Override
	public void resizeAndcrop(int maxWidth, int maxHeight, int cropHeight) throws IOException {
		Double ratio = null;
		if (maxWidth > 0 && imgWidth > maxWidth) {
			ratio = (double) maxWidth / imgWidth;
		}
		if (maxHeight > 0 && imgHeight > maxHeight) {
			double yRatio = (double) maxHeight / imgHeight;
			if (ratio == null || yRatio < ratio) {
				ratio = yRatio;
			}
		}
		int width = imgWidth;
		int height = imgHeight;
		if (ratio != null) {
			width = (int) (imgWidth * ratio);
			height = (int) (imgHeight * ratio);
			if (width == 0) {
				width = 1;
			}
			if (height == 0) {
				height = 1;
			}
		}
		boolean needCrop = cropHeight > 0 && height > cropHeight;
		if (ratio == null && !needCrop) {
			return;
		}
		destBImage = new BufferedImage(width, needCrop ? cropHeight : height, srcBImage.getType());
		destBImage.getGraphics().drawImage(
				ratio != null ? srcBImage.getScaledInstance(width, height, Image.SCALE_SMOOTH) : srcBImage, 0, 0, null);
	}

	private void zoomImage(int width, int height) {
		if (width == 0) {
			width = 1;
		}
		if (height == 0) {
			height = 1;
		}
		destBImage = new BufferedImage(width, height, srcBImage.getType());
		destBImage.getGraphics().drawImage(srcBImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		modified = true;
	}

	private void rotateImage(int w, int h, double radian, double ratio) {
		AffineTransform transform = new AffineTransform();
		transform.setToScale(ratio, ratio);
		transform.rotate(radian, w / 2, h / 2);
		transform.translate(w / 2 - imgWidth / 2, h / 2 - imgHeight / 2);
		AffineTransformOp ato = new AffineTransformOp(transform, null);
		w *= ratio;
		h *= ratio;
		destBImage = new BufferedImage(w, h, srcBImage.getType());
		if (getImageType() == ImageType.JPG) {
			Graphics gs = destBImage.getGraphics();
			gs.setColor(Color.white); // set canvas background to white
			gs.fillRect(0, 0, w, h);
		}
		ato.filter(srcBImage, destBImage);
		modified = true;
	}

	private void cropImage(int left, int top, int width, int height) {
		destBImage = new BufferedImage(width, height, srcBImage.getType());
		destBImage.getGraphics().drawImage(srcBImage, -left, -top, imgWidth, imgHeight, null);
		modified = true;
	}

	private void convertToBufferedImage(InputStream inputStream) throws IOException {
		if (inputStream != null) {
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(inputStream);
			initFormatName(imageInputStream);
			this.srcBImage = ImageIO.read(imageInputStream);
			this.imgWidth = srcBImage.getWidth();
			this.imgHeight = srcBImage.getHeight();
		}
	}

	private void initFormatName(ImageInputStream imageInputStream) throws IOException {
		Iterator<ImageReader> ir = ImageIO.getImageReaders(imageInputStream);
		if (ir.hasNext()) {
			this.formatName = ir.next().getFormatName();
		}
	}

	@Override
	public InputStream getTransformed() throws IOException {
		if (destBImage == null) {
			destBImage = srcBImage;
		}
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
		ImageIO.write(destBImage, formatName, imOut);
		return new ByteArrayInputStream(bs.toByteArray());
	}

	@Override
	public ImageType getImageType() {
		return ImageType.fromExt(formatName);
	}

	@Override
	public void pressImage(InputStream pressInputStream, Position position, float alpha) throws IOException {
		ImageInputStream imageInputStream = ImageIO.createImageInputStream(pressInputStream);
		BufferedImage pBImage = ImageIO.read(imageInputStream);
		int pWidth = pBImage.getWidth();
		int pHeight = pBImage.getHeight();

		// determine press
		// TODO more clever
		if (pBImage.getWidth() < imgWidth / 3 && pBImage.getHeight() < imgHeight / 3) {
			destBImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = destBImage.createGraphics();
			g.drawImage(srcBImage, 0, 0, imgWidth, imgHeight, null);
			// add press
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));
			// default LEFT_TOP
			int x = 0, y = 0;
			if (Position.LEFT_BOTTOM == position) {
				y = imgHeight - pHeight;
			} else if (Position.RIGHT_TOP == position) {
				x = imgWidth - pWidth;
			} else if (Position.RIGHT_BOTTOM == position) {
				x = imgWidth - pWidth;
				y = imgHeight - pHeight;
			} else if (Position.CENTER == position) {
				x = (imgWidth - pWidth) / 2;
				y = (imgHeight - pHeight) / 2;
			}
			g.drawImage(pBImage, x, y, pBImage.getWidth(), pBImage.getHeight(), null);
			g.dispose();
			modified = true;
		}
	}

	@Override
	public void compressImageWithSave(String destPath, String formatName, float quality) {
		File destFile = new File(destPath);
		try {
			destFile.createNewFile();
		} catch (IOException e) {
			logger.error("create compress image file fail:", e);
		}
		FileOutputStream fos = null;

		ImageWriter imgWrier = ImageIO.getImageWritersByFormatName(formatName).next();
		ImageWriteParam imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(null);
		imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		imgWriteParams
				.setCompressionQuality(BigDecimal.valueOf(quality).setScale(1, BigDecimal.ROUND_HALF_UP).floatValue());
		imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		imgWriteParams.setDestinationType(
				new javax.imageio.ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
		try {
			imgWrier.reset();
			fos = new FileOutputStream(destPath);
			imgWrier.setOutput(ImageIO.createImageOutputStream(fos));
			imgWrier.write(null, new IIOImage(srcBImage, null, null), imgWriteParams);
			fos.flush();
			imgWrier.dispose();
		} catch (Exception e) {
			logger.error("compress image fail:", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.error("compress image fail:", e);
				}
			}
		}
		modified = true;
	}
}
