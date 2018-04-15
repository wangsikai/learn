package com.lanking.uxb.service.mall.resource;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.lanking.cloud.domain.base.file.api.FileExt;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.util.QRCodeUtil;
import com.lanking.uxb.service.mall.api.MemberPackagePaymentService;
import com.lanking.uxb.service.payment.OrderPayBusinessSpace;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.cache.WXQRCodeImageCache;
import com.lanking.uxb.service.payment.weixin.response.UnifiedPayResult;

/**
 * 会员套餐支付通用控制.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月27日
 */
@SuppressWarnings("restriction")
public abstract class AbstractMemberPackagePaymentController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MemberPackagePaymentService memberPackagePaymentService;
	@Autowired
	private WXQRCodeImageCache wxQRCodeImageCache;

	/**
	 * 创建微信订单并获得微信支付二维码.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	@RequestMapping("getWXQRCodeImage")
	public void getWXQRCodeImage(Long memberPackageID, Long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response) {
		if (memberPackageID == null || orderID == null) {
			throw new MissingArgumentException();
		}
		String base64 = wxQRCodeImageCache.getWXQRCodeImageCache(OrderBusinessSource.USER_MEMBER, orderID);

		BufferedImage bufferImage = null;
		if (StringUtils.isNotBlank(base64)) {
			// 缓存读取
			InputStream inputStream = null;
			try {
				byte[] bytes = new BASE64Decoder().decodeBuffer(base64);
				inputStream = new ByteArrayInputStream(bytes);
				bufferImage = ImageIO.read(inputStream);
			} catch (IOException e) {
				logger.error("write qr code image error:", e);
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						logger.error("write qr code image error:", e);
					}
				}
			}
		} else {
			String appid = Env.getString("weixin.pay.appid");
			UnifiedPayResult result = memberPackagePaymentService.getWXQRCodeImage(appid, memberPackageID, orderID,
					space, "NATIVE", null, request, response);
			if (result != null && StringUtils.isNotBlank(result.getCode_url())) {
				bufferImage = QRCodeUtil.createQRCode(result.getCode_url(), 300);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				try {
					ImageIO.write(bufferImage, "jpg", outputStream);
					base64 = new BASE64Encoder().encode(outputStream.toByteArray());
					int invalidTime = Env.getDynamicInt("weixin.pay.qrcode.timeout"); // 微信支付验证码有效时间（分钟）
					wxQRCodeImageCache.setWXQRCodeImageCache(OrderBusinessSource.USER_MEMBER, orderID, base64,
							TimeUnit.MINUTES, (invalidTime - 2));
				} catch (IOException e) {
					logger.error("write qr code image error:", e);
				}
			}
		}

		response.setContentType("image/" + FileExt.PNG);
		String contentDisposition = new StringBuilder("inline; filename=\"").append("wxqrcode.").append(FileExt.PNG)
				.append("\"").toString();
		response.setHeader("Content-Disposition", contentDisposition);
		try {
			ImageIO.write(bufferImage, FileExt.PNG, response.getOutputStream());
		} catch (ClientAbortException e) {
			logger.info("write qr code image, client abort the channel");
		} catch (IOException e) {
			logger.error("write qr code image error:", e);
		}
	}

	/**
	 * 跳转至支付宝页面.
	 * 
	 * @param memberPackageID
	 *            会员套餐ID
	 * @param orderID
	 *            订单ID
	 * @param space
	 *            来源
	 * @param request
	 * @param response
	 */
	@RequestMapping("jumpToAlipay")
	public void jumpToAlipay(Long memberPackageID, Long orderID, OrderPayBusinessSpace space,
			HttpServletRequest request, HttpServletResponse response) {
		memberPackagePaymentService.jumpToAlipay(memberPackageID, orderID, space, request, response);
	}
}
