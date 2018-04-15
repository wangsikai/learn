package com.lanking.uxb.service.fallible.resource;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.file.util.QRCodeUtil;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 错题本代打印支付通用控制.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月9日
 */
public abstract class AbstractStuFalliblePrintPaymentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;

	/**
	 * 创建微信订单并获得微信支付二维码.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	@RequestMapping("getWXQRCodeImage")
	public void getWXQRCodeImage(Long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response) {
		String appid = Env.getString("weixin.pay.appid");
		UnifiedPayResult result = stuFalliblePrintService.getWXQRCodeImage(appid, orderID, space, request, response);
		if (result != null && StringUtils.isNotBlank(result.getCode_url())) {
			BufferedImage image = QRCodeUtil.createQRCode(result.getCode_url(), 300);
			response.setContentType("image/" + FileExt.PNG);
			String contentDisposition = new StringBuilder("inline; filename=\"")
					.append(Codecs.md5Hex(result.getCode_url().getBytes())).append(".").append(FileExt.PNG)
					.append("\"").toString();
			response.setHeader("Content-Disposition", contentDisposition);
			try {
				ImageIO.write(image, FileExt.PNG, response.getOutputStream());
			} catch (ClientAbortException e) {
				logger.info("write qr code image, client abort the channel");
			} catch (IOException e) {
				logger.error("write qr code image error:", e);
			}
		}
	}

	/**
	 * 跳转至支付宝页面.
	 * 
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	@RequestMapping("jumpToAlipay")
	public void jumpToAlipay(Long orderID, OrderPayBusinessSpace space, HttpServletRequest request,
			HttpServletResponse response) {
		stuFalliblePrintService.jumpToAlipay(orderID, space, request, response);
	}
}
