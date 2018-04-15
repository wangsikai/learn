package com.lanking.uxb.service.session.resource;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.session.form.VerifyCode;
import com.lanking.uxb.service.session.util.VerifyCodeUtils;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月26日
 *
 */
@RestController
@RequestMapping("sec")
public class SessionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionController.class);

	private static final int DEF_WIDTH = 80;
	private static final int DEF_HEIGHT = 25;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "verifyCode" }, method = RequestMethod.GET)
	public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		this.verifyCode(0, 0, request, response);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "verifyCode/{width}/{height}" }, method = RequestMethod.GET)
	public void verifyCode(@PathVariable("width") int width, @PathVariable("height") int height,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (width <= 0 || height <= 0) {
			width = DEF_WIDTH;
			height = DEF_HEIGHT;
		}
		String verifyCode = Security.generateVerifyCode();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(VerifyCodeUtils.bufferedImage(verifyCode, width, height), "jpeg", sos);
		sos.close();
	}

	/**
	 * web 2.1 验证码方法
	 * 
	 * @since 2.1
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "2/verifyCode/{width}/{height}" }, method = RequestMethod.GET)
	public void verifyCode2(@PathVariable("width") int width, @PathVariable("height") int height,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (width <= 0 || height <= 0) {
			width = DEF_WIDTH;
			height = DEF_HEIGHT;
		}
		String verifyCode = Security.generateVerifyCode();
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(VerifyCodeUtils.getImage(verifyCode, 0, 0), "jpeg", sos);
		sos.close();
	}

	@RequestMapping(value = { "refresh" }, method = RequestMethod.POST)
	public void refresh() {
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "info" }, method = RequestMethod.GET)
	public Value info() {
		return new Value();
	}

	/**
	 * 获取当前userID
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param token
	 *            会话token
	 * @return {@link Value}
	 */
	@RequestMapping(value = { "uid" }, method = RequestMethod.GET)
	public Value uid() {
		return new Value(Security.getSession().getUserId());
	}

	/**
	 * 生成点选式验证码图片
	 * 
	 * @since
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "pointVerifyCode/{width}/{height}" }, method = { RequestMethod.GET, RequestMethod.POST })
	public void pointVerifyCode(@PathVariable("width") int width, @PathVariable("height") int height,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 默认数据
		int defwidth = 320;
		int defheight = 150;

		width = defwidth;
		height = defheight;
		VerifyCode checkCode = Security.getPointVerifyCodeOnly();

		// 创建缓存
		InputStream is = this.getClass().getResourceAsStream("/image/" + VerifyCodeUtils.getImageRandom());
		BufferedImage bi = new BufferedImage(defwidth, defheight, BufferedImage.TYPE_INT_RGB);
		BufferedImage inputImg = ImageIO.read(is);

		// 获得画布
		Graphics g = bi.getGraphics();
		g.drawImage(inputImg, 0, 0, defwidth, defheight, null);
		// 获得字体颜色
		Color color = VerifyCodeUtils.getColorRandom();
		// 画干扰线
		VerifyCodeUtils.drawRandomLine(g, color);
		// 获得验证码在图片坐标
		Integer[][] point = checkCode.getLocation();
		// 生成随机汉字
		List<String> chs = checkCode.getCharacters();
		LOGGER.info(point.toString());
		// 写图片
		VerifyCodeUtils.drawRandomNum((Graphics2D) g, point, color, chs);

		response.setContentType("image/jpeg");
		// 控制浏览器不要缓存
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		// 将图片写给浏览器
		ServletOutputStream sos = response.getOutputStream();
		ImageIO.write(bi, "jpg", sos);
		sos.close();
	}

	/**
	 * 生成验证码
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "characters" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value getCharacters() {
		Map<String, Object> data = new HashMap<String, Object>();
		// 生成随机汉字
		List<String> chs = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			chs.add(VerifyCodeUtils.getRandomChar());
		}

		Integer[][] point = VerifyCodeUtils.getPoint();
		LOGGER.info(String.valueOf(point[0][0]) + "," + String.valueOf(point[0][1]));
		LOGGER.info(String.valueOf(point[1][0]) + "," + String.valueOf(point[1][1]));
		LOGGER.info(String.valueOf(point[2][0]) + "," + String.valueOf(point[2][1]));
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setCharacters(chs);
		verifyCode.setLocation(point);
		// 过期时间设置60s
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 60);
		verifyCode.setDeadline(c.getTime());
		Security.setPointVerifyCode(verifyCode);

		// 验证前三个汉字
		List<String> checkList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			checkList.add(chs.get(i));
		}
		data.put("needVerifyCode", true);
		data.put("characters", checkList);

		return new Value(data);
	}

	/**
	 * 获得图片点击验证码数据
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "2/getCharacters", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getCharacters2(HttpServletRequest request) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		// 生成随机汉字
		List<String> chs = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			chs.add(VerifyCodeUtils.getRandomChar());
		}

		Integer[][] point = VerifyCodeUtils.getPoint();
		LOGGER.info(String.valueOf(point[0][0]) + "," + String.valueOf(point[0][1]));
		LOGGER.info(String.valueOf(point[1][0]) + "," + String.valueOf(point[1][1]));
		LOGGER.info(String.valueOf(point[2][0]) + "," + String.valueOf(point[2][1]));
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setCharacters(chs);
		verifyCode.setLocation(point);
		// 过期时间设置60s
		Calendar c = Calendar.getInstance();
		c.set(Calendar.SECOND, c.get(Calendar.SECOND) + 60);
		verifyCode.setDeadline(c.getTime());
		Security.setPointVerifyCode(verifyCode);

		// 验证前三个汉字
		List<String> checkList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			checkList.add(chs.get(i));
		}
		data.put("characters", checkList);
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(request.getScheme()).append("://").append(request.getServerName())
				.append("/sec/pointVerifyCode/320/150");
		data.put("picUrl", urlBuilder.toString());

		return new Value(data);
	}
}
