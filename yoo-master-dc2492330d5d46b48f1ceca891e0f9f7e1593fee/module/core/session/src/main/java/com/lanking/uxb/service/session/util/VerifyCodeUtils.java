package com.lanking.uxb.service.session.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Random;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年12月9日
 *
 */
public class VerifyCodeUtils {
	private static final int w = 70;
	private static final int h = 35;
	private static final Color bgColor = new Color(216, 216, 216);

	/**
	 * 默认颜色
	 */
	public static final int[][] COLORS = { { 170, 99, 231 }, { 0, 187, 157 }, { 42, 183, 247 }, { 234, 81, 81 },
			{ 237, 123, 61 } };

	public static BufferedImage bufferedImage(String verifyCode, int width, int height) {
		int codeCount = verifyCode.length();
		int startX = width / (codeCount + 1);
		int startY = height - 4;
		int fontHeight = height - 2;

		BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = buffImg.createGraphics();
		Random random = new Random();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		Font font = new Font("Fixedsys", Font.PLAIN, fontHeight);
		g.setFont(font);
		g.setColor(Color.BLACK);
		g.drawRect(0, 0, width - 1, height - 1);
		g.setColor(Color.BLACK);
		for (int i = 0; i < 10; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
		int red = 0, green = 0, blue = 0;
		char[] verifyCodeArray = verifyCode.toCharArray();

		for (int i = 0; i < verifyCodeArray.length; i++) {
			red = random.nextInt(255);
			green = random.nextInt(255);
			blue = random.nextInt(255);
			g.setColor(new Color(red, green, blue));
			g.drawString(String.valueOf(verifyCodeArray[i]), (i + 1) * startX, startY);
		}
		return buffImg;
	}

	// 创建BufferedImage
	private static BufferedImage createImage(int width, int height) {
		BufferedImage image = new BufferedImage(width > 0 ? width : w, height > 0 ? height : h,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) image.getGraphics();
		g2.setColor(bgColor);
		g2.fillRect(0, 0, width > 0 ? width : w, height > 0 ? height : h);
		return image;
	}

	/**
	 * V2.1开始的验证码生成方法
	 *
	 * @since 2.1
	 * @param verifyCode
	 *            Verify Code
	 * @return BufferedImage
	 */
	public static BufferedImage getImage(String verifyCode, int width, int height) {
		BufferedImage image = createImage(width, height);// 创建图片缓冲区
		Graphics2D g2 = (Graphics2D) image.getGraphics();// 得到绘制环境
		StringBuilder sb = new StringBuilder();// 用来装载生成的验证码文本
		String fontName = "微软雅黑";
		for (int i = 0; i < verifyCode.length(); i++) {
			String s = verifyCode.charAt(i) + "";
			sb.append(s);
			float x = i * 1.0F * w / 4; // 设置当前字符的x轴坐标
			g2.setFont(new Font(fontName, 1, 20));
			g2.setColor(new Color(144, 19, 254)); // 设置随机颜色
			g2.drawString(s, x, h - 7); // 画图
		}
		sb.append(verifyCode);
		return image;
	}

	/**
	 * 随机颜色
	 * 
	 * @param g
	 * @return
	 */
	public static Color getColorRandom() {
		Random random = new Random();
		int s = random.nextInt(4);
		// 设置颜色
		Color color = new Color(COLORS[s][0], COLORS[s][1], COLORS[s][2]);

		return color;
	}

	/**
	 * 随机线条
	 * 
	 * @param g
	 * @param color
	 */
	public static void drawRandomLine(Graphics g, Color color) {
		g.setColor(color);
		// 设置线条个数并画线
		for (int i = 0; i < 5; i++) {
			int x1 = new Random().nextInt(320);
			int y1 = new Random().nextInt(150);
			int x2 = new Random().nextInt(320);
			int y2 = new Random().nextInt(150);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * 汉字画到图片上
	 * 
	 * @param g
	 * @param point
	 * @param color
	 * @param chs
	 * @return
	 */
	public static List<String> drawRandomNum(Graphics2D g, Integer[][] point, Color color, List<String> chs) {
		// g.setColor(color);
		// 设置字体
		g.setFont(new Font("微软雅黑", Font.BOLD, 26));
		for (int i = 0; i < chs.size(); i++) {
			Color random = new Color(COLORS[i][0], COLORS[i][1], COLORS[i][2]);
			g.setColor(random);
			// 设置字体旋转角度10
			int degree = new Random().nextInt() % 10;
			// 正向角度
			g.rotate(degree * Math.PI / 180, point[i][0], 23);
			g.drawString(chs.get(i), point[i][0], point[i][1]);
			// 反向角度
			g.rotate(-degree * Math.PI / 180, point[i][0], 23);
		}

		return chs;
	}

	/**
	 * 优化的随机汉字生成
	 * 
	 * @return
	 */
	public static String getRandomChar() {
		String str = "";
		int hightPos; //
		int lowPos;

		Random random = new Random();

		hightPos = (176 + Math.abs(random.nextInt(39)));
		lowPos = (161 + Math.abs(random.nextInt(93)));

		byte[] b = new byte[2];
		b[0] = (Integer.valueOf(hightPos)).byteValue();
		b[1] = (Integer.valueOf(lowPos)).byteValue();

		try {
			str = new String(b, "GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return String.valueOf(str.charAt(0));
	}

	/**
	 * 随机图片
	 * 
	 * @return imageName
	 */
	public static String getImageRandom() {
		StringBuilder name = new StringBuilder();

		Random random = new Random();
		int max = 10;
		int min = 1;
		int s = random.nextInt(max) % (max - min + 1) + min;
		name.append("yz0");
		name.append(s);
		name.append(".jpg");
		return name.toString();
	}

	/**
	 * 计算验证码出现在图片上的位置
	 * 
	 * @return point
	 */
	public static Integer[][] getPoint() {
		Integer[][] result = new Integer[5][2];
		int[][] temp = new int[5][2];

		// 优化算法
		int widthPoint = 11;
		int heightPoint = 5;
		Random random = new Random();

		int count = 0;
		while (count < 4) {
			int x = random.nextInt(widthPoint) + 1;
			int y = random.nextInt(heightPoint) + 1;
			int localx = x * 25 - 14;
			int localy = y * 25 + 7;

			boolean flag = true;
			for (int j = 0; j < 4; j++) {
				if (localx == temp[j][0] && localy == temp[j][1]) {
					flag = false;
					break;
				}
			}
			if (flag) {
				temp[count][0] = localx;
				temp[count][1] = localy;
				count++;
			}
		}

		for (int i = 0; i < 4; i++) {
			result[i][0] = temp[i][0];
			result[i][1] = temp[i][1];
		}

		// 生成一个相邻的点
		result[4][0] = result[0][0] + 15;
		result[4][1] = result[0][1] + 15;

		return result;
	}
}
