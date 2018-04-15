package com.lanking.uxb.service.export.word.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.SocketTimeoutException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.sdk.util.ZipUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.util.QRCodeUtil;
import com.lanking.uxb.service.resources.value.VQuestion;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import sun.misc.BASE64Decoder;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

/**
 * Word工具.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月13日
 */
@SuppressWarnings("restriction")
public class WordUtils {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 目标WORD文件的存储路径.
	 */
	private String destDir;

	/**
	 * 图片下载服务host.
	 */
	private String host;

	/**
	 * 文档模板.
	 */
	private Template documentTemplate;

	/**
	 * 图片模板.
	 */
	private Template imageTemplate;

	/**
	 * 关系模板.
	 */
	private Template relsTemplate;

	/**
	 * 外部处理的图片本地路径集合.
	 */
	private Map<String, String> outLocalImgMap = new HashMap<String, String>();

	// 图片数据集合
	// 仅需要处理关系头声明、引用段，图片文件直接存储至media文件夹中
	List<Map<String, Object>> images = new ArrayList<Map<String, Object>>();

	TransformerFactory factory = null;
	Transformer transformer = null;
	HttpClient httpClient = null;

	private String[] letters = { "A", "B", "C", "D", "E", "F", "G" };

	public WordUtils(String destDir, String host, String templateFilePath, Template documentTemplate,
			Template imageTemplate, Template relsTemplate, StreamSource streamSource, HttpClient httpClient)
			throws TransformerConfigurationException {
		this.destDir = destDir;
		this.host = host;
		this.documentTemplate = documentTemplate;
		this.imageTemplate = imageTemplate;
		this.relsTemplate = relsTemplate;
		this.httpClient = httpClient;
		factory = TransformerFactory.newInstance();
		transformer = factory.newTransformer(streamSource);

		// 首先解压模板文件至目标文件夹中
		File templateDirFile = new File(destDir + "/template");
		if (!templateDirFile.exists()) {
			templateDirFile.getParentFile().mkdirs();
		} else {
			this.deleteDir(templateDirFile);
		}
		ZipUtils.unzip(templateFilePath, destDir + "/template");
	}

	public void setOutLocalImgMap(Map<String, String> outLocalImgMap) {
		if (outLocalImgMap != null) {
			this.outLocalImgMap = outLocalImgMap;
		}
	}

	/**
	 * 创建docx文档.
	 * 
	 * @param outFileName
	 *            生成文件名
	 * @param documentParams
	 *            文档参数
	 * @param imgMLDatas
	 *            预处理图片集合
	 * @param outLocalImgMap
	 *            外部处理图片集合
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public File build(String outFileName, Map<String, Object> documentParams, List<QuestionWordImageData> imgMLDatas,
			String documentXMLPath, String relationsXMLPath) throws IOException, TemplateException {
		long t1 = System.currentTimeMillis();

		// 处理预处理的图片集合
		Set<String> ridSets = new HashSet<String>();
		if (imgMLDatas.size() > 0) {
			for (QuestionWordImageData imageData : imgMLDatas) {
				ridSets.add(imageData.getRid());
				FileOutputStream outputStream = null;
				InputStream inputStream = null;
				FileChannel in = null;
				FileChannel out = null;
				try {
					if (StringUtils.isNotBlank(imageData.getLocalPath())) {
						File file = new File(imageData.getLocalPath());
						// 更换了地址，二维码得重新生成下
						if (file.exists() && !imageData.getRid().substring(0, 2).equals("QR")) {
							inputStream = new FileInputStream(file);
							outputStream = new FileOutputStream(
									destDir + "/template/word/media/" + imageData.getRid() + ".png");
							in = ((FileInputStream) inputStream).getChannel();
							out = outputStream.getChannel();
							in.transferTo(0, in.size(), out); // NIO管道输出
						} else {
							if (imageData.getRid().substring(0, 2).equals("QR")) {
								// 二维码图片
								String code = imageData.getRid().substring(2);
								String p1 = code.substring(0, 3);
								String p2 = code.substring(3, 6);

								String basepath = Env.getString("question.qrcode.path");
								String baseurl = Env.getString("question.single.url");
								File img = new File(basepath + p1 + "/" + p2 + "/" + code + ".jpg");

								try {
									byte[] bytes = RSACoder.encryptByPublicKey(String.valueOf(code).getBytes(),
											Env.getString("yoomath.rsa.publicKey"));
									code = RSACoder.parseByte2HexStr(bytes);
								} catch (Exception e1) {
									logger.error(e1.getMessage());
								}
								BufferedImage bufferImage = QRCodeUtil.createQRCode(baseurl + code, 100, 100,
										ErrorCorrectionLevel.L, 0);
								FileOutputStream qrQutputStream = null;
								try {
									if (!img.getParentFile().exists()) {
										img.getParentFile().mkdirs();
									}
									qrQutputStream = new FileOutputStream(img);
									ImageIO.write(bufferImage, "jpg", qrQutputStream);

									inputStream = new FileInputStream(img);
									outputStream = new FileOutputStream(
											destDir + "/template/word/media/" + imageData.getRid() + ".png");
									in = ((FileInputStream) inputStream).getChannel();
									out = outputStream.getChannel();
									in.transferTo(0, in.size(), out); // NIO管道输出
								} catch (FileNotFoundException e) {
									logger.error("[WORD 预处理] 二维码图片处理出错! path = " + imageData.getLocalPath());
								} catch (IOException e) {
									logger.error("[WORD 预处理] 二维码图片处理出错! path = " + imageData.getLocalPath());
								} finally {
									if (qrQutputStream != null) {
										try {
											qrQutputStream.close();
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
								}
							} else {
								logger.error("[WORD 预处理] 图片未找到! path = " + imageData.getLocalPath());
							}
						}
					} else {
						byte[] bytes = new BASE64Decoder().decodeBuffer(imageData.getBase64());
						inputStream = new ByteArrayInputStream(bytes);
						byte[] buffer = new byte[1024];
						int byteread = 0; // 读取的字节数
						String rId = "rId" + System.currentTimeMillis();
						outputStream = new FileOutputStream(destDir + "/template/word/media/" + rId + ".png");
						while ((byteread = inputStream.read(buffer)) != -1) {
							outputStream.write(buffer, 0, byteread);
						}
					}
				} finally {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
					if (outputStream != null) {
						outputStream.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
		}

		if (ridSets.size() > 0) {
			for (String rid : ridSets) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("rid", rid);
				images.add(map);
			}
		}

		long t2 = System.currentTimeMillis();
		logger.info("-------------------------------------->预处理图片：" + (t2 - t1) / 1000);

		// 关系头图片引用声明
		String relsXML = FreeMarkerTemplateUtils.processTemplateIntoString(relsTemplate,
				Params.param("images", images));

		// document
		String documentXML = FreeMarkerTemplateUtils.processTemplateIntoString(documentTemplate, documentParams);

		// 生成文档
		return this.create(relsXML, documentXML, outFileName, documentXMLPath, relationsXMLPath);
	}

	/**
	 * 获取单个习题格式化数据.
	 * 
	 * @param destDir
	 *            目标路径
	 * @param sequence
	 *            题序
	 * @param question
	 *            习题
	 * @param images
	 *            分解出的图片存储集合
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public Map<String, Object> getQuestion(int sequence, VQuestion question) throws IOException, TemplateException {
		Map<String, Object> map = new HashMap<String, Object>(2);

		// 题干处理
		String content = this.transContent(null, "[" + question.getCode() + "] " + question.getContent());

		// 选择项处理
		if (question.getType() == Question.Type.SINGLE_CHOICE || question.getType() == Question.Type.MULTIPLE_CHOICE) {
			List<String> choices = question.getChoices();
			StringBuffer sb = new StringBuffer("<w:tbl><w:tblPr>");
			if (question.getChoiceFormat() == ChoiceFormat.VERTICAL) {
				sb.append("<w:tblW w:w=\"0\" w:type=\"auto\"/>"); // 纵向排列
			} else {
				sb.append("<w:tblW w:w=\"5000\" w:type=\"pct\"/>"); // 水平排列、两项并列
			}
			sb.append("<w:tblLook/></w:tblPr>");
			for (int i = 0; i < choices.size(); i++) {
				String choice = choices.get(i);
				Map<String, Object> item = new HashMap<String, Object>(1);
				item.put("content", this.transContent(this.letters[i], choice));
				if (question.getChoiceFormat() == ChoiceFormat.HORIZONTAL && i == 0) {
					sb.append("<w:tr>");
				} else if (question.getChoiceFormat() == ChoiceFormat.ABREAST && i % 2 == 0) {
					sb.append("<w:tr>");
				} else if (question.getChoiceFormat() == ChoiceFormat.VERTICAL) {
					sb.append("<w:tr>");
				}
				sb.append("<w:tc>").append(this.transContent(this.letters[i], choice)).append("</w:tc>");
				if (question.getChoiceFormat() == ChoiceFormat.HORIZONTAL && i == choices.size() - 1) {
					// 水平排列
					sb.append("</w:tr>");
				} else if (question.getChoiceFormat() == ChoiceFormat.ABREAST && i % 2 != 0) {
					sb.append("</w:tr>");
				} else if (question.getChoiceFormat() == ChoiceFormat.VERTICAL) {
					sb.append("</w:tr>");
				}
			}
			sb.append("</w:tbl>");
			map.put("items", sb.toString());
		}

		map.put("content", content);
		return map;
	}

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
	public String transContent(String sequence, String content) throws IOException, TemplateException {

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
	private String transTable(String content) throws IOException, TemplateException {
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
	private String transMath(String content) throws IOException, TemplateException {
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
				limMatcher.appendReplacement(limSb,
						"\\lim_{" + limMatcher.group(1).trim() + "}{" + limMatcher.group(2).trim() + "}");
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
	private String transP(String content) {
		return content.replace("<p></p>", "").replace("<p>", "<w:br />").replace("</p>", "");
	}

	/**
	 * 处理不标准的Latex输入.
	 * 
	 * @param content
	 *            包含latex代码的文本
	 * @return
	 */
	private String transNoStandardLatex(String content) {

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
	private String transImage(String content) throws IOException, TemplateException {
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
	private String mml2omml(String mml) {
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
			omml = omml.replace(
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
	private String removeTags(String txt) {
		txt = txt.replaceAll("<(?!p|/p|table|/table|tr|/tr|td|/td|th|/th)+[\\s\\S]*?>", "");
		txt = txt.replace("<p>", ":::[p:b]:::").replace("</p>", ":::[p:e]:::");
		txt = txt.replace("<", "&lt;").replace(">", "&gt;");
		txt = txt.replace(":::[p:b]:::", "<p>").replace(":::[p:e]:::", "</p>");
		return txt;
	}

	/**
	 * 输出生成WORD.
	 * 
	 * @param destDir
	 *            目标文件夹路径
	 * @param relsXML
	 *            关系声明
	 * @param documentXML
	 *            文档结构
	 * @param response
	 * @throws IOException
	 */
	private File create(String relsXML, String documentXML, String outFileName, String documentXMLPath,
			String relationsXMLPath) throws IOException {

		Writer out = null;
		FileOutputStream outputStream = null;

		try {
			// 生成document XML
			outputStream = new FileOutputStream(
					documentXMLPath == null ? destDir + "/template/word/document.xml" : documentXMLPath);
			outputStream.write(documentXML.getBytes());
			outputStream.flush();
			outputStream.close();

			// 生成rels XML
			outputStream = new FileOutputStream(
					relationsXMLPath == null ? destDir + "/template/word/_rels/document.xml.rels" : relationsXMLPath);
			outputStream.write(relsXML.getBytes());
			outputStream.flush();
			outputStream.close();

			// 压缩生成docx
			ZipUtils.zip(destDir + "/template", destDir + "/" + outFileName + ".docx");

			// 删除template
			this.deleteDir(new File(destDir + "/template"));

			return new File(destDir + "/" + outFileName + ".docx");
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("close response writer error ", e);
				}
			}
			if (null != outputStream) {
				try {
					outputStream.close();
				} catch (IOException e) {
					logger.error("store word writer error ", e);
				}
			}
		}
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
	private Map<String, Object> downloadImageFromBase64(String base64) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(6);
		byte[] bytes = new BASE64Decoder().decodeBuffer(base64);
		FileOutputStream outputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			BufferedImage bufferImage = ImageIO.read(inputStream);
			if (bufferImage != null) {
				String rId = "rId" + System.currentTimeMillis();
				outputStream = new FileOutputStream(destDir + "/template/word/media/" + rId + ".png");
				ImageIO.write(bufferImage, "png", outputStream);

				// 图片宽高需要换算成EMU英制单位
				int width = (int) (bufferImage.getWidth(null) * 0.0264583);
				int height = (int) (bufferImage.getHeight(null) * 0.0264583);
				if (width > 15) {
					height = (int) (height * (15.0 / width));
					width = 15;
				}
				width = width * 360000;
				height = height * 360000;

				map.put("name", "IMG" + images.size() + ".png"); // 名称
				map.put("width", String.valueOf(width)); // 宽
				map.put("height", String.valueOf(height)); // 高
				map.put("base64", ""); // base64数据
				map.put("sequence", (images.size() + 1)); // 序号
				map.put("rid", rId); // OOXML-relation

				images.add(map);
				return map;
			}
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return map;
	}

	/**
	 * 下载图片并返回所需数据.
	 * 
	 * @param src
	 *            图片SRC
	 * @param 分解出的图片存储集合
	 * @return
	 * @throws Exception
	 */
	private Map<String, Object> downloadImage(String src) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(6);

		String localPath = outLocalImgMap.get(src);
		if (StringUtils.isNotBlank(localPath)) {
			// 图片在外部已处理
			File file = new File(localPath);
			if (file.exists()) {
				FileOutputStream outputStream = null;
				InputStream inputStream = null;
				FileChannel in = null;
				FileChannel out = null;
				String rId = "rId" + System.currentTimeMillis();

				try {
					inputStream = new FileInputStream(file);
					outputStream = new FileOutputStream(destDir + "/template/word/media/" + rId + ".png");
					BufferedImage bufferImage = ImageIO.read(inputStream);

					in = ((FileInputStream) inputStream).getChannel();
					out = outputStream.getChannel();
					in.transferTo(0, in.size(), out); // NIO管道输出

					// 图片宽高需要换算成EMU英制单位
					double width = bufferImage.getWidth(null) * 0.0264583;
					double height = bufferImage.getHeight(null) * 0.0264583;
					if (width > 15) {
						height = height * (15.0 / width);
						width = 15;
					}
					int int_width = (int) (width * 360000);
					int int_height = (int) (height * 360000);

					map.put("name", "IMG" + images.size() + ".jpg"); // 名称
					map.put("width", String.valueOf(int_width)); // 宽
					map.put("height", String.valueOf(int_height)); // 高
					map.put("base64", ""); // base64数据
					map.put("sequence", (images.size() + 1)); // 序号
					map.put("rid", rId); // OOXML-relation

					images.add(map);
					return map;
				} finally {
					if (outputStream != null) {
						outputStream.close();
					}
					if (inputStream != null) {
						inputStream.close();
					}
				}
			}
		}

		// 设置连接超时
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(200).setConnectionRequestTimeout(200)
				.setSocketTimeout(500).build();

		HttpGet httpget = new HttpGet(src);
		httpget.setConfig(requestConfig);
		HttpResponse response = null;
		try {
			response = httpClient.execute(httpget);
		} catch (SocketTimeoutException e) {
			logger.error("[downloadImage] src = {} read socket timeout!", src);
			return null;
		}

		HttpEntity entity = response.getEntity();
		if (response.getStatusLine().getStatusCode() != 200) {
			logger.error(
					"Got bad response, error code = " + response.getStatusLine().getStatusCode() + " imageUrl: " + src);
			return null;
		}

		if (entity != null) {
			FileOutputStream outputStream = null;
			InputStream inputStream = null;
			try {
				inputStream = response.getEntity().getContent();
				BufferedImage bufferImage = ImageIO.read(inputStream);
				if (bufferImage != null) {
					String rId = "rId" + System.currentTimeMillis();
					outputStream = new FileOutputStream(destDir + "/template/word/media/" + rId + ".png");
					ImageIO.write(bufferImage, "png", outputStream);

					// 图片宽高需要换算成EMU英制单位
					double width = bufferImage.getWidth(null) * 0.0264583;
					double height = bufferImage.getHeight(null) * 0.0264583;
					if (width > 15) {
						height = height * (15.0 / width);
						width = 15;
					}
					int int_width = (int) (width * 360000);
					int int_height = (int) (height * 360000);

					map.put("name", "IMG" + images.size() + ".jpg"); // 名称
					map.put("width", String.valueOf(int_width)); // 宽
					map.put("height", String.valueOf(int_height)); // 高
					map.put("base64", ""); // base64数据
					map.put("sequence", (images.size() + 1)); // 序号
					map.put("rid", rId); // OOXML-relation

					images.add(map);
					return map;
				} else {
					logger.error("word下载图片源不存在！url: " + src);
				}
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}
		}
		return null;
	}

	private boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list(); // 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	/**
	 * WordML 前置内容添加.
	 * 
	 * @param contentML
	 *            WordML 文档
	 * @param 需要添加的内容
	 * @return
	 */
	public static String contentPrefixAdd(String contentML, String text, boolean breakLine) {
		if (contentML.indexOf("<w:p>") == 0) {
			if (contentML.length() > 5) {
				String bl = breakLine ? "</w:p><w:p>" : "";
				contentML = "<w:p><w:r><w:t>" + text + "</w:t></w:r>" + bl + contentML.substring(5, contentML.length());
			}
		} else {
			contentML = "<w:p><w:r><w:t>" + text + "</w:t></w:r></w:p>" + contentML;
		}
		return contentML;
	}
}
