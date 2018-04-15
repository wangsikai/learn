package com.lanking.uxb.service.latex.api.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.JLabel;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.latex.api.LatexService;

import fmath.conversion.ConvertFromMathMLToLatex;

@Service
public class LatexServiceImpl implements LatexService {

	private Logger logger = LoggerFactory.getLogger(LatexServiceImpl.class);
	private List<String> specialSymbols = Lists.newArrayList("①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩");
	private List<String> replaceSpecialSymbols = Lists.newArrayList("\\\\ding\\{172\\}", "\\\\ding\\{173\\}",
			"\\\\ding\\{174\\}", "\\\\ding\\{175\\}", "\\\\ding\\{176\\}", "\\\\ding\\{177\\}", "\\\\ding\\{178\\}",
			"\\\\ding\\{179\\}", "\\\\ding\\{180\\}", "\\\\ding\\{181\\}");

	private String beforeStoreLatexImage(String latex) {
		final String MID = "\\mid";
		int index = latex.indexOf(MID);
		int i = 0;
		while (index != -1) {
			if (latex.length() > index + 5) {
				if (!" ".equals(latex.substring(index + 4, index + 5))) {
					latex = latex.substring(0, index + 4) + " " + latex.substring(index + 4);
				}
				index = latex.indexOf(MID, index + 5);
			} else {
				latex = latex + " ";
				break;
			}
			i++;
			if (i == 100) {
				break;
			}
		}
		latex = latex.replaceAll("，", ",").replaceAll("﹣", "-").replaceAll("＞", ">").replaceAll("＜", "<")
				.replaceAll("、", ",").replaceAll("（", "(").replaceAll("）", ")");
		return latex;
	}

	@Override
	public File storeLatexImage(String latex, String md5Latex, String colorStr) {
		Color color = Color.BLACK;
		if ("1".equals(colorStr) || "white".equals(colorStr.toLowerCase())) {
			color = Color.WHITE;
		} else if ("2".equals(colorStr) || "red".equals(colorStr.toLowerCase())) {
			color = Color.RED;
		} else if ("3".equals(colorStr) || "green".equals(colorStr.toLowerCase())) {
			color = Color.GREEN;
		} else if ("D90000".equals(colorStr)) {// 手机上的红色
			color = new Color(217, 0, 0);
		} else if ("80C04C".equals(colorStr)) {// 手机上的绿色
			color = new Color(128, 192, 76);
		} else if ("00bb9d".equals(colorStr)) {// 手机上的绿色
			color = new Color(0, 187, 157);
		}
		String filePath = Env.getString("latex.file.store.path") + File.separator + Math.abs(color.getRGB())
				+ File.separator + md5Latex.substring(0, 2) + File.separator + md5Latex.substring(2, 4) + File.separator
				+ md5Latex;
		File file = new File(filePath);

		if (file.exists()) {
			return file;
		}

		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}

		// latex 绘图
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			try {
				latex = this.handleLatexSpecial(beforeStoreLatexImage(latex));
			} catch (Exception e) {
				logger.error("store latex image error: latex = {} ", latex, e);
			}
			TeXFormula fomule = new TeXFormula(latex);
			TeXIcon ti = fomule.createTeXIcon(TeXConstants.STYLE_DISPLAY, 24, TeXConstants.TYPE_BIG_OPERATOR, color);
			int width = ti.getIconWidth();
			int height = ti.getIconHeight();
			BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g2 = (Graphics2D) b.getGraphics();

			// 设置透明begin
			b = g2.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
			Graphics graphics = b.getGraphics();
			graphics.setFont(new Font("宋体", Font.PLAIN, 24));
			ti.paintIcon(new JLabel(), graphics, 0, 0);
			ImageIO.write(b, "PNG", outputStream);
		} catch (Exception e) {
			logger.error("store latex image error: latex = {} ", latex, e);
			if (file.exists()) {
				file.delete();
				file = null;
			}
		} finally {
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("close outputStream error:", e);
				}
			}
		}
		return file;
	}

	@Override
	public String mml2Latex(String mml) {
		if (StringUtils.isBlank(mml)) {
			return StringUtils.EMPTY;
		}
		mml = mml.replaceAll(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", "");
		mml = mml.replaceAll("(?<=>)\\s+(?=<)", "");
		// 处理页面mathjax转换出的mml中，花括号丢失转义字符的问题
		mml = mml.replaceAll("<mo>\\{</mo>", "<mo>\\\\{</mo>");
		mml = mml.replaceAll("<mo>\\}</mo>", "<mo>\\\\}</mo>");
		String ltx = ConvertFromMathMLToLatex.convertToLatex(mml);
		if ("$ Error $".equals(ltx)) {
			return mml;
		} else {
			if (ltx.indexOf("\\[[\\begin{array}") != -1 && ltx.indexOf("\\end{array}]\\]") != -1) {
				ltx = ltx.replace("\\[[\\begin{array}", "\\begin{bmatrix}");
				ltx = ltx.replace("\\end{array}]\\]", "\\end{bmatrix}");
			} else {
				// mml转 latex，大括号会变成小括号
				ltx = ltx.replaceAll("\\{[\\s*]\\\\begin\\{array\\}", "\\left \\\\{ \\\\begin\\{array\\}");
				ltx = ltx.replaceAll("\\\\end\\{array\\}", "\\\\end\\{array\\} \\\\right. ");
			}
			return this.handleLatexSpecial(ltx);
		}
	}

	/**
	 * 处理Latex.
	 * 
	 * @param latex
	 * @return
	 */
	private String handleLatexSpecial(String ltx) {
		if (StringUtils.isBlank(ltx)) {
			return "";
		}
		if (ltx.indexOf("\\[") == 0 && ltx.indexOf("\\]") == ltx.length() - 2) {
			ltx = ltx.substring(2, ltx.length() - 2);
		}
		// 过滤 ≌等（TODO ... 待探讨）
		ltx = ltx.replace("\\allequal", "\\cong ").replace("\\bigtriangle up", "\\triangle ")
				.replace("\\exi sts", "\\exists ").replace("\\upsi lon", "\\upsilon ")
				.replace("\\textquotedblleft", "“").replace("\\textquotedblright", "”")
				.replace("\\textdegree", "^\\circ ").replace("\\mdash", "— ").replace("\\lbrack", "\\left[")
				.replace("\\rbrack", "\\right]").replace("\\lrdbrack", "\\left(").replace("\\rrdbrack", "\\right)")
				.replaceAll("%", "\\\\%");
		// .replaceAll("&", "\\\\&");

		// 空格处理（去除简单字串处理，防止字符重叠公式被拆解）
		ltx = ltx.replace("\\angle", "\\angle ").replace("\\neg", "\\neg ").replace("\\alpha", "\\alpha ")
				.replace("\\beta", "\\beta ").replace("\\gamma", "\\gamma ").replace("\\delta", "\\delta ")
				.replace("\\lambda", "\\lambda ").replace("\\rho", "\\rho ").replace("\\sigma", "\\sigma ")
				.replace("\\tau", "\\tau ").replace("\\upsilon", "\\upsilon ").replace("\\varphi", "\\varphi ")
				.replace("\\chi", "\\chi ").replace("\\omega", "\\omega ").replace("\\sin", "\\sin ")
				.replace("\\cos", "\\cos ").replace("\\tan", "\\tan ").replace("\\cot", "\\cot ")
				.replace("\\cdots", "\\cdots ").replace("\\div", "\\div ").replace("\\parallel", "\\parallel ")
				.replace("\\supseteq", "\\supseteq ").replace("\\times", "\\times ").replace("\\ast", "\\ast ")
				.replace("\\circ", "\\circ ").replace("\\varnothing", "\\varnothing ")
				.replace("\\complement", "\\complement ").replace("\\oplus", "\\oplus ")
				.replace("\\ominus", "\\ominus ").replace("\\otimes", "\\otimes ").replace("\\odot", "\\odot ")
				.replace("\\because", "\\because ").replace("\\therefore", "\\therefore ").replace("\\perp", "\\perp ")
				.replace("\\triangle", "\\triangle ").replace("\\infty", "\\infty ").replace("\\approx", "\\approx ")
				.replace("\\backsim", "\\backsim ").replace("\\cong", "\\cong ").replace("\\notin", "\\notin ")
				.replace("\\subseteq", "\\subseteq").replace("\\supseteq", "\\supseteq")
				.replace("\\nsubseteq", "\\nsubseteq ").replace("\\nsupseteq", "\\nsupseteq ")
				.replace("\\subsetneqq", "\\subsetneqq ").replace("\\supsetneqq", "\\supsetneqq ")
				.replace("\\foralla", "\\foralla ").replace("\\ln", "\\ln ");

		// 2016-11-15 去除下面这段代码，若出现非拉伸{，需要单独对题目修改添加left、right
		// ltx = ltx.replaceAll("\\{[\\s*]\\\\begin\\{array\\}",
		// "\\left \\\\{ \\\\begin\\{array\\}");
		// ltx = ltx.replaceAll("\\\\end\\{array\\}",
		// "\\\\end\\{array\\} \\\\right. ");

		int index = 0;
		for (String replaceSpecialSymbol : replaceSpecialSymbols) {
			ltx = ltx.replaceAll(replaceSpecialSymbol, specialSymbols.get(index));
			index++;
		}

		// 处理hat公式符号转成^^的问题
		Pattern barPattern = Pattern.compile("[\\s]*?([^\\s]+?)[\\s]*?\\^\\^");
		Matcher barMatcher = barPattern.matcher(ltx);
		StringBuffer barSb = new StringBuffer();
		while (barMatcher.find()) {
			// 用特殊字符包裹
			barMatcher.appendReplacement(barSb,
					"\\\\widehat{" + barMatcher.group(1).trim().replace("\\", "\\\\") + "}");
		}
		barMatcher.appendTail(barSb);
		return barSb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> mml2Latex(List<String> mmls) {
		if (CollectionUtils.isEmpty(mmls)) {
			return Collections.EMPTY_LIST;
		}
		List<String> latexs = new ArrayList<String>(mmls.size());
		for (String mml : mmls) {
			latexs.add(mml2Latex(mml));
		}
		return latexs;
	}

	@Override
	public String multiMml2Latex(String mml) {
		String reg = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\">(.+?)</math>";
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(mml);
		List<String> maths = Lists.newArrayList();
		while (m.find()) {
			maths.add(m.group());
		}
		String latex = mml;
		for (String math : maths) {
			if (StringUtils.isNotBlank(math)) {
				String onelatex = mml2Latex(math.replaceAll("<math xmlns=\"http://www.w3.org/1998/Math/MathML\">", "")
						.replaceAll("</math>", ""));
				latex = latex.replace(math, "<ux-mth>" + onelatex + "</ux-mth>");
			}
		}
		int index = 0;
		for (String replaceSpecialSymbol : replaceSpecialSymbols) {
			latex = latex.replaceAll(replaceSpecialSymbol, specialSymbols.get(index));
			index++;
		}
		return latex;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> multiMml2Latex(List<String> mmls) {
		if (CollectionUtils.isEmpty(mmls)) {
			return Collections.EMPTY_LIST;
		}
		List<String> latexs = new ArrayList<String>(mmls.size());
		for (String mml : mmls) {
			mml = mml.replaceAll("<ux-mth>", "").replaceAll("</ux-mth>", "");
			String latex = multiMml2Latex(mml);
			latexs.add(latex);
		}
		return latexs;
	}

	public static void main(String args[]) {
		LatexServiceImpl t = new LatexServiceImpl();
		String mml = " - 1  xx  10^3\\\",\\\"<math xmlns=\\\\\\\"http://www.w3.org/1998/Math/MathML\\\\\\\">  <mstyle displaystyle=\\\\\\\"true\\\\\\\">    <mo>-</mo>    <mn>1.203</mn>    <mo>×</mo>    <msup>      <mn>10</mn>      <mn>7</mn>    </msup>  </mstyle></math>";
		String x = t.multiMml2Latex(mml);
		System.out.println(x);
	}
}
