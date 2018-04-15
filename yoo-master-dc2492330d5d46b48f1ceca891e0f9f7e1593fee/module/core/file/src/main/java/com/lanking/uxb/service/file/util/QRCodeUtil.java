package com.lanking.uxb.service.file.util;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lanking.cloud.sdk.util.Charsets;

/**
 * 二维码工具类
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月17日
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public final class QRCodeUtil {
	private static final int BLACK = 0xFF000000;
	private static final int WHITE = 0xFFFFFFFF;

	public static BufferedImage createQRCode(String data, int size) {
		return createQRCode(data, size, size);
	}

	public static BufferedImage createQRCode(String data, int width, int height, ErrorCorrectionLevel level,
			Integer margin) {
		Map hint = new HashMap();
		hint.put(EncodeHintType.ERROR_CORRECTION, level);
		hint.put(EncodeHintType.CHARACTER_SET, Charsets.UTF8);
		if (margin != null) {
			hint.put(EncodeHintType.MARGIN, margin);
		}
		try {
			BitMatrix matrix = new MultiFormatWriter().encode(new String(data.getBytes(Charsets.UTF8), Charsets.UTF8),
					BarcodeFormat.QR_CODE, width, height, hint);
			return toBufferedImage(matrix);
		} catch (WriterException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	public static BufferedImage createQRCode(String data, int width, int height) {
		return QRCodeUtil.createQRCode(data, width, height, ErrorCorrectionLevel.H, null);
	}

	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
}