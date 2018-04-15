package com.lanking.uxb.service.question.api.impl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.util.QRCodeUtil;
import com.lanking.uxb.service.question.api.QuestionQRCodeService;

@Service
public class QuestionQRCodeServiceImpl implements QuestionQRCodeService {
	private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

	@Async
	@Override
	public void asyncMakeQRCodeImage(Question question) {
		String code = question.getCode();
		String p1 = code.substring(0, 3);
		String p2 = code.substring(3, 6);

		String basepath = Env.getString("question.qrcode.path");
		String baseurl = Env.getString("question.single.url");
		File img = new File(basepath + p1 + "/" + p2 + "/" + code + ".jpg");
		if (!img.exists()) {
			try {
				byte[] bytes = RSACoder.encryptByPublicKey(String.valueOf(code).getBytes(),
						Env.getString("yoomath.rsa.publicKey"));
				code = RSACoder.parseByte2HexStr(bytes);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			BufferedImage bufferImage = QRCodeUtil.createQRCode(baseurl + code, 100, 100, ErrorCorrectionLevel.L, 0);
			FileOutputStream outputStream = null;
			try {
				if (!img.getParentFile().exists()) {
					img.getParentFile().mkdirs();
				}
				outputStream = new FileOutputStream(img);
				ImageIO.write(bufferImage, "jpg", outputStream);
			} catch (FileNotFoundException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			} finally {
				if (outputStream != null) {
					try {
						outputStream.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
	}

	@Async
	@Override
	public void batchMakeQRCodeImage(List<Question> questions) {
		for (Question question : questions) {
			String code = question.getCode();
			String p1 = code.substring(0, 3);
			String p2 = code.substring(3, 6);

			String basepath = Env.getString("question.qrcode.path");
			String baseurl = Env.getString("question.single.url");
			File img = new File(basepath + p1 + "/" + p2 + "/" + code + ".jpg");
			if (!img.exists()) {
				try {
					byte[] bytes = RSACoder.encryptByPublicKey(String.valueOf(code).getBytes(),
							Env.getString("yoomath.rsa.publicKey"));
					code = RSACoder.parseByte2HexStr(bytes);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				BufferedImage bufferImage = QRCodeUtil.createQRCode(baseurl + code, 100, 100, ErrorCorrectionLevel.L,
						0);
				FileOutputStream outputStream = null;
				try {
					if (!img.getParentFile().exists()) {
						img.getParentFile().mkdirs();
					}
					outputStream = new FileOutputStream(img);
					ImageIO.write(bufferImage, "jpg", outputStream);
				} catch (FileNotFoundException e) {
					logger.error(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());
				} finally {
					if (outputStream != null) {
						try {
							outputStream.close();
						} catch (IOException e) {
							logger.error(e.getMessage());
						}
					}
				}
			}
		}
	}
}
