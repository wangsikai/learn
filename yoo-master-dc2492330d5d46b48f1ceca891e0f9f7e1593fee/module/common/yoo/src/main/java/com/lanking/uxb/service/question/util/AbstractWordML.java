package com.lanking.uxb.service.question.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * WordML 处理抽象类.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月3日
 */
public abstract class AbstractWordML {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 图片下载服务host.
	 */
	protected String host;

	/**
	 * 图片模板.
	 */
	protected Template imageTemplate;

	protected TransformerFactory factory = null;
	protected Transformer transformer = null;
	protected HttpClient httpClient = null;

	protected String[] letters = { "A", "B", "C", "D", "E", "F", "G" };

	/**
	 * 处理文本内容.
	 * 
	 * @param sequence
	 *            内容序列.
	 * @param content
	 *            原始文本
	 * @param images
	 *            分解出的图片存储集合
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	protected String transContent(String sequence, String content) throws IOException, TemplateException {
		if (StringUtils.isBlank(content)) {
			return "";
		}

		// 首尾有p标签包裹，去除
		content = content.trim();
		if (content.indexOf("<p>") == 0) {
			if (content.lastIndexOf("</p>") == content.length() - 4) {
				content = content.substring(3, content.length() - 4);
			}
		}

		// 处理填空
		content = content.replace("&nbsp;", " ");
		content = content.replaceAll("<img[^>]+src=\"/images/blank.png\"[^>]+>", "_________");
		content = content.replaceAll("<ux-blank sequence=\\d+></ux-blank>", "_________");

		// 清除表格的标签折行，同时处理\n折行
		Pattern pattern = Pattern.compile("<table[\\S\\s]*?>([^\"]+?)</table>");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			String temp = content.substring(lastAppendPosition, matcher.start());
			if (StringUtils.isNotBlank(temp)) {
				sb.append(temp);
			}
			sb.append(matcher.group(0).replaceAll("[\b\r\n\t]*", ""));
			lastAppendPosition = matcher.end();
		}
		if (lastAppendPosition == 0) {
			sb.append(content);
		} else if (lastAppendPosition < content.length()) {
			sb.append(content.substring(lastAppendPosition, content.length()));
		}
		content = sb.toString().replaceAll("\n{1}", "<p> </p>");

		// 需要显示序列号
		if (StringUtils.isNotBlank(sequence)) {
			content = content.trim();
			if (content.indexOf("</p>") == 0) {
				content = content.substring(4, content.length());
			}
			if (content.indexOf("<p>") == 0) {
				content = "<p>" + content.substring(3, content.length()).trim();
			}
			content = new StringBuffer(sequence).append(".").append("　").append(content).toString();
		}

		// 处理表格
		content = this.transTable(content);

		// 处理P标签
		content = this.transP(content);

		return content;
	}

	/**
	 * 处理表格.
	 * 
	 * @param content
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	protected String transTable(String content) throws IOException, TemplateException {
		Pattern pattern = Pattern.compile("<table[\\S\\s]*?>([\\s\\S]*?)</table>");
		Matcher matcher = pattern.matcher(content);

		StringBuffer sb = new StringBuffer();
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			String temp = content.substring(lastAppendPosition, matcher.start());
			if (StringUtils.isNotBlank(temp)) {
				sb.append(this.transMath(temp));
			}

			sb.append("<w:tbl><w:tblPr><w:tblStyle w:val=\"ab\"/><w:tblW w:w=\"0\" w:type=\"auto\"/>")
					.append("<w:tblBorders><w:top w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/>")
					.append("<w:left w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/>")
					.append("<w:bottom w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/>")
					.append("<w:right w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/>")
					.append("<w:insideH w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/>")
					.append("<w:insideV w:val=\"single\" w:sz=\"1\" w:space=\"0\" w:color=\"auto\"/></w:tblBorders>")
					.append("<w:tblLook/></w:tblPr>");
			Document document = Jsoup.parse(matcher.group(0));
			Elements trs = document.select("table").select("tr");
			for (int i = 0; i < trs.size(); i++) {
				sb.append("<w:tr>");
				Elements tds = trs.get(i).select("td");
				for (int j = 0; j < tds.size(); j++) {
					Element td = tds.get(j);
					String colspan = "";
					if (StringUtils.isNotBlank(td.attr("colspan"))) {
						colspan = "<w:tcPr><w:gridSpan w:val=\"" + td.attr("colspan") + "\"/></w:tcPr>";
					}
					sb.append("<w:tc>").append(colspan).append(this.transMath(td.html())).append("</w:tc>");
				}
				sb.append("</w:tr>");
			}
			sb.append("</w:tbl>");
			lastAppendPosition = matcher.end();
		}
		if (lastAppendPosition == 0) {
			sb.append(this.transMath(content));
		} else if (lastAppendPosition < content.length()) {
			sb.append(this.transMath(content.substring(lastAppendPosition, content.length())));
		}

		return sb.toString();
	}

	/**
	 * 处理公式（使用SnuggleTeX）.
	 * 
	 * @param destDir
	 *            目标路径
	 * @param content
	 *            内容
	 * @param images
	 *            分解出的图片存储集合
	 * @param request
	 * @param response
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	protected String transMath(String content) throws IOException, TemplateException {
		content = content.replaceAll("<ux-mth>\\\\left\\([\\s]*?\\\\right\\)</ux-mth>", "( )");
		content = content.replaceAll("<ux-mth>\\([\\s]*?\\)</ux-mth>", "( )");

		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("<ux-mth>([^\"]+?)</ux-mth>");
		Matcher matcher = pattern.matcher(content);
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			String groupTxt = matcher.group(1);
			groupTxt = groupTxt.replace("<ux-mth>", "").replace("</ux-mth>", "");

			// 处理不标准的LATEX输入
			groupTxt = this.transNoStandardLatex(groupTxt.replace("\n", ""));
			groupTxt = groupTxt.replace("\\perp", "⊥"); // ...
			groupTxt = groupTxt.replace("\\triangle", "△"); // ...
			groupTxt = groupTxt.replace("\\Delta", "△"); // ...
			groupTxt = groupTxt.replace("\\square", "□"); // ...

			// 处理\lim\limits_{}{}极限标签无法转换的问题，暂改为\lim_{}{}形式
			Pattern limPattern = Pattern
					.compile("\\\\lim[\\s]*?\\\\limits[\\s]*?_[\\s]*?\\{([^\"]+?)\\}[\\s]*?\\{([^\"]+?)\\}");
			Matcher limMatcher = limPattern.matcher(groupTxt);
			StringBuffer limSb = new StringBuffer();
			while (limMatcher.find()) {
				limMatcher.appendReplacement(limSb, "\\lim_{" + limMatcher.group(1).trim() + "}{"
						+ limMatcher.group(2).trim() + "}");
			}
			limMatcher.appendTail(limSb);
			groupTxt = limSb.toString();

			// 处理 \overbrace{}^{}标签
			Pattern obPattern = Pattern.compile("\\\\overbrace[\\s]*?\\{([^\"]+?)\\}[\\s]*?\\^[\\s]*?\\{([^\"]*?)\\}");
			Matcher obMatcher = obPattern.matcher(groupTxt);
			StringBuffer obSb = new StringBuffer();
			while (obMatcher.find()) {
				String p1 = StringUtils.isBlank(obMatcher.group(1)) ? "" : obMatcher.group(1).trim();
				String p2 = StringUtils.isBlank(obMatcher.group(2)) ? "" : obMatcher.group(2).trim();
				obMatcher.appendReplacement(obSb, "\\\\overbrace{" + p1 + "}^{" + p2 + "}");
			}
			obMatcher.appendTail(obSb);
			groupTxt = obSb.toString();

			// Snugglex
			SnuggleEngine engine = new SnuggleEngine();
			SnuggleSession session = engine.createSession();
			SnuggleInput input = new SnuggleInput("$" + groupTxt + "$");
			session.parseInput(input);
			String mml = session.buildXMLString();
			mml = mml.replace("<mo>→</mo></mover>", "<mo>⃗</mo></mover>");
			mml = mml.replace("<mo>̄</mo>", "<mo>̅</mo>");
			mml = mml.replace("<mo>︷</mo>", "<mo>⏞</mo>");
			// mml = mml.replace("⟩", ">");

			String temp = content.substring(lastAppendPosition, matcher.start());
			if (StringUtils.isNotBlank(temp)) {
				sb.append(this.transImage(temp));
			}
			String oomath = this.mml2omml(mml);

			// oomath处理
			// 去除部分公式word中显示框框的问题
			oomath = oomath.replace("<m:e/>", "<m:e><m:r><m:t xml:space=\"preserve\"> </m:t></m:r></m:e>");

			sb.append(oomath).append("<m:r><m:t xml:space=\"preserve\"> </m:t></m:r>");
			lastAppendPosition = matcher.end();
		}

		if (lastAppendPosition == 0) {
			sb.append(this.transImage(content));
		} else if (lastAppendPosition < content.length()) {
			sb.append(this.transImage(content.substring(lastAppendPosition, content.length())));
		}
		return "<w:p>" + sb.toString() + "</w:p>";
	}

	/**
	 * 处理段落.
	 * 
	 * @return
	 */
	protected String transP(String content) {
		return content.replace("<p></p>", "").replace("<p>", "<w:br />").replace("</p>", "");
	}

	/**
	 * 处理不标准的Latex输入.
	 * 
	 * @param content
	 *            包含latex代码的文本
	 * @return
	 */
	protected String transNoStandardLatex(String content) {

		content = content.replace("\\pi", "π");

		// 处理/frac分数公式书写不标准，例如\fracab、\frac12、\frac 12等
		Pattern pattern = Pattern.compile("(\\\\frac[\\s]*?[\\S]{1}[\\s]*?[\\S]{1})");
		Matcher matcher = pattern.matcher(content);
		StringBuffer sb = new StringBuffer();
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			String temp = matcher.group(1);
			String outString = content.substring(lastAppendPosition, matcher.start());
			sb.append(outString);

			String part = temp.replace("\\frac", "").trim();
			if (temp.indexOf("{") != -1) {
				if (part.indexOf("{") != 0) {
					temp = "\\frac{" + part.substring(0, 1) + "}" + part.substring(1);
				}
				sb.append(temp);
			} else if (part.indexOf("\\") != -1) {
				sb.append(temp);
			} else {
				temp = temp.replace("\\frac", "").trim();
				temp = "\\frac{" + temp.substring(0, 1) + "}{" + temp.substring(1) + "}";
				sb.append(temp);
			}
			lastAppendPosition = matcher.end();
		}
		if (lastAppendPosition == 0) {
			sb.append(content);
		} else if (lastAppendPosition < content.length()) {
			sb.append(content.substring(lastAppendPosition, content.length()));
		}
		content = sb.toString();

		// 处理对数lg不规范问题
		content = content.replace("\\lg", " lg");

		// 处理对数log_xx不规范问题
		Pattern logPattern = Pattern.compile("\\\\log_[\\s]*?([0-9]{2})");
		Matcher logMatcher = logPattern.matcher(content);
		StringBuffer logSb = new StringBuffer();
		while (logMatcher.find()) {
			// 用特殊字符包裹
			String temp = logMatcher.group(1).trim();
			if (temp.indexOf("\\") == -1 && temp.length() > 1) {
				logMatcher.appendReplacement(logSb, "log_{" + temp.substring(0, 1) + "}{" + temp.substring(1) + "}");
			} else {
				logMatcher.appendReplacement(logSb, "log_" + temp);
			}
		}
		logMatcher.appendTail(logSb);
		content = logSb.toString();

		// 去除无法识别的\rm 标记
		content = content.replace("\\rm ", "");

		// 替换摄氏度^\circ\hspace{-0.09em}\mathrm{C} -> ℃
		content = content.replace("^\\circ\\hspace{-0.09em}\\mathrm{C}", "℃")
				.replace("^\\circ\\hspace{-0.09em}\\mathrm{}", "℃").replace("^\\circ\\hspace{-0.09em}", "℃")
				.replace("^\\circ\\mathrm{C}", "℃").replace("^\\circ", "°");

		// 替换\cong
		content = content.replace("\\cong", "≌");

		// 处理begin{vmatrix}、begin{bmatrix}间距
		content = content.replace("\\begin{vmatrix}{", "\\begin{vmatrix} {").replace("\\begin{bmatrix}{",
				"\\begin{bmatrix} {");

		// 处理sqrt书写不规范的问题
		content = content.replaceAll("\\\\sqrt[\\s]*?\\[[\\s]*?\\][\\s]*?\\{", "\\\\sqrt{");
		Pattern sqrtPattern = Pattern.compile("\\\\sqrt[\\s]*?\\[[\\s]*?\\][\\s]*?([A-Za-z0-9]{1})");
		Matcher sqrtMatcher = sqrtPattern.matcher(content);
		StringBuffer sqrtSb = new StringBuffer();
		while (sqrtMatcher.find()) {
			sqrtMatcher.appendReplacement(sqrtSb, "\\\\sqrt{" + sqrtMatcher.group(1) + "}");
		}
		sqrtMatcher.appendTail(sqrtSb);
		content = sqrtSb.toString();

		sqrtPattern = Pattern.compile("\\\\sqrt[\\s]*?([A-Za-z0-9]{1})");
		sqrtMatcher = sqrtPattern.matcher(content);
		sqrtSb = new StringBuffer();
		while (sqrtMatcher.find()) {
			sqrtMatcher.appendReplacement(sqrtSb, "\\\\sqrt{" + sqrtMatcher.group(1) + "}");
		}
		sqrtMatcher.appendTail(sqrtSb);
		content = sqrtSb.toString();

		return content;
	}

	/**
	 * 处理图片.
	 * 
	 * @param content
	 *            无公式的内容
	 * @param images
	 *            分解出的图片存储集合
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	protected String transImage(String content) throws IOException, TemplateException {
		Pattern pattern = Pattern.compile("<img[^>]+src=[\"|\']([^\"]+?)[\"|\'][\\s\\S]*?>");
		Matcher matcher = pattern.matcher(content);

		StringBuffer sb = new StringBuffer();
		int lastAppendPosition = 0; // 上次添加位置
		while (matcher.find()) {
			// 下载图片
			Map<String, Object> imageMap = null;
			try {
				String src = matcher.group(1);
				if (src.indexOf("base64") != -1 && src.indexOf("data") != -1) {
					// 直接的base64数据
					String base64 = src.split(",")[1];
					imageMap = this.downloadImageFromBase64(base64);
				} else if (src.indexOf("http") == -1) {
					src = src.replace("..", "");// v1版本录入的题目中会有此类型的图片资源,如:../dd
					src = host + src;
					// src =
					// "http://zuoye.web.dev.uxuebao.com/dd?fn=1406840609362.jpg";
					imageMap = this.downloadImage(src);
				}
			} catch (Exception e) {
				logger.error("download image error ", e);
			}
			if (imageMap != null) {
				// 图片模板
				String imgooxml = FreeMarkerTemplateUtils.processTemplateIntoString(imageTemplate, imageMap);
				String temp = content.substring(lastAppendPosition, matcher.start());
				if (StringUtils.isNotBlank(temp)) {
					sb.append("<w:r><w:t>").append(this.removeTags(temp)).append("</w:t></w:r>");
				}
				sb.append(imgooxml);
				lastAppendPosition = matcher.end();
			}
		}
		if (lastAppendPosition == 0) {
			sb.append("<w:r><w:t>").append(this.removeTags(content)).append("</w:t></w:r>");
		} else if (lastAppendPosition < content.length()) {
			sb.append("<w:r><w:t>").append(this.removeTags(content.substring(lastAppendPosition, content.length())))
					.append("</w:t></w:r>");
		}
		return sb.toString();
	}

	/**
	 * MML转OMML.
	 * 
	 * @param mml
	 * @return
	 */
	protected String mml2omml(String mml) {
		String omml = "";
		mml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + mml;
		ByteArrayInputStream byteArrayInputStream = null;
		ByteArrayOutputStream byteArrayOutputStream = null;
		try {
			byteArrayInputStream = new ByteArrayInputStream(mml.getBytes("utf-8"));
			byteArrayOutputStream = new ByteArrayOutputStream(1024);

			transformer.transform(new StreamSource(byteArrayInputStream), new StreamResult(byteArrayOutputStream));
			omml = new String(byteArrayOutputStream.toByteArray(), "utf-8");
			omml = omml.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
			omml = omml
					.replace(
							" xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\" xmlns:mml=\"http://www.w3.org/1998/Math/MathML\"",
							"");
			omml = omml.replace("/r/n/t", "");
			omml = omml.replace("</table>", "");

			byteArrayOutputStream.close();
			byteArrayInputStream.close();
		} catch (IOException | TransformerException e) {
			logger.error("mml convert to omml error {}", mml, e);
		} finally {
			if (byteArrayOutputStream != null) {
				try {
					byteArrayOutputStream.close();
				} catch (IOException e) {
					logger.error("mml convert to omml error {}", mml, e);
				}
			}
			if (byteArrayInputStream != null) {
				try {
					byteArrayInputStream.close();
				} catch (IOException e) {
					logger.error("mml convert to omml error {}", mml, e);
				}
			}
		}
		return omml;
	}

	/**
	 * 处理部分html标签.
	 * 
	 * @param txt
	 * @return
	 */
	protected String removeTags(String txt) {
		txt = txt.replaceAll("<(?!p|/p|table|/table|tr|/tr|td|/td|th|/th)+[\\s\\S]*?>", "");
		txt = txt.replace("<p>", ":::[p:b]:::").replace("</p>", ":::[p:e]:::");
		txt = txt.replace("<", "&lt;").replace(">", "&gt;");
		txt = txt.replace(":::[p:b]:::", "<p>").replace(":::[p:e]:::", "</p>");
		return txt;
	}

	/**
	 * 转换base64图片数据并返回所需数据.
	 * 
	 * @param base64
	 *            图片base64数据
	 * @param 分解出的图片存储集合
	 * @return
	 * @throws Exception
	 */
	abstract Map<String, Object> downloadImageFromBase64(String base64) throws Exception;

	/**
	 * 下载图片并返回所需数据.
	 * 
	 * @param src
	 *            图片SRC
	 * @param 分解出的图片存储集合
	 * @return
	 * @throws Exception
	 */
	abstract Map<String, Object> downloadImage(String src) throws Exception;
}
