package com.lanking.uxb.service.file.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.uxb.service.file.form.QRCodeForm;
import com.lanking.uxb.service.file.util.QRCodeUtil;

/**
 * 二维码相关接口
 * 
 * @since 2.3.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年8月17日
 */
@RestController
@RequestMapping({ "f/qr", "fs/qr" })
public class QRCodeController {

	private Logger logger = LoggerFactory.getLogger(QRCodeController.class);

	@RequestMapping(value = "generate")
	public void file(QRCodeForm form, HttpServletRequest request, HttpServletResponse response) {
		BufferedImage image = QRCodeUtil.createQRCode(form.getData(), form.getSize());
		response.setContentType("image/" + FileExt.PNG);
		String contentDisposition = new StringBuilder(form.getContentDisposition()).append("; filename=\"")
				.append(Codecs.md5Hex(form.getData().getBytes())).append(".").append(FileExt.PNG).append("\"")
				.toString();
		response.setHeader("Content-Disposition", contentDisposition);
		try {
			ImageIO.write(image, FileExt.PNG, response.getOutputStream());
		} catch (IOException e) {
			logger.error("write qr code image error:", e);
		}
	}
}
