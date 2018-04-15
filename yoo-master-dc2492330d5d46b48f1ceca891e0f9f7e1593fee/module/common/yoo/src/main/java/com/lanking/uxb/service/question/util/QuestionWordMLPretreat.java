package com.lanking.uxb.service.question.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;

import sun.misc.BASE64Decoder;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.resource.HopeFileController;
import com.lanking.uxb.service.file.util.FileUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 习题WordML预处理.
 * <p>
 * 分解出的内容以"<w:p></w:p>"标签包裹，内部包含图片结构。<br>
 * </p>
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version v2.2.0 2016年8月3日
 */
@SuppressWarnings("restriction")
public class QuestionWordMLPretreat extends AbstractWordML {

	private Question question; // 处理的习题
	private List<QuestionWordImageData> images = new ArrayList<QuestionWordImageData>(); // 处理的图片
	private FileService fileService;

	public QuestionWordMLPretreat(String host, StreamSource streamSource, Template imageTemplate,
			HttpClient httpClient, FileService fileService) throws TransformerConfigurationException {
		this.host = host;
		this.imageTemplate = imageTemplate;
		this.httpClient = httpClient;
		this.fileService = fileService;
		factory = TransformerFactory.newInstance();
		transformer = factory.newTransformer(streamSource);
	}

	/**
	 * 处理生成习题WordML存储对象.
	 * 
	 * @param question
	 *            习题
	 * @param answers
	 *            答案
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public QuestionWordMLData getQuestionContenMLDatas(Question question, List<Answer> answers) throws IOException,
			TemplateException {
		this.question = question;

		// 题干处理
		String contentML = this.transContent(null, question.getContent());

		// 选择项处理
		if (question.getType() == Question.Type.SINGLE_CHOICE || question.getType() == Question.Type.MULTIPLE_CHOICE) {
			List<String> choices = new ArrayList<String>();
			if (StringUtils.isNotBlank(question.getChoiceA())) {
				choices.add(question.getChoiceA());
			}
			if (StringUtils.isNotBlank(question.getChoiceB())) {
				choices.add(question.getChoiceB());
			}
			if (StringUtils.isNotBlank(question.getChoiceC())) {
				choices.add(question.getChoiceC());
			}
			if (StringUtils.isNotBlank(question.getChoiceD())) {
				choices.add(question.getChoiceD());
			}
			if (StringUtils.isNotBlank(question.getChoiceE())) {
				choices.add(question.getChoiceE());
			}
			if (StringUtils.isNotBlank(question.getChoiceF())) {
				choices.add(question.getChoiceF());
			}
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
			contentML = contentML + sb.toString();
		}

		// 解析处理
		String analysisML = this.transContent(null, question.getAnalysis());

		// 提示处理
		String hintML = this.transContent(null, question.getHint());

		// 答案处理
		String answersML = "";
		StringBuffer anwsersBuffer = new StringBuffer();
		for (Answer answer : answers) {
			anwsersBuffer.append(answer.getContent()).append("       ");
		}
		answersML = this.transContent(null, anwsersBuffer.toString());

		// 过滤图片集合，处理需要数据库查询的path
		List<Long> ids = Lists.newArrayList();
		for (QuestionWordImageData image : images) {
			if (StringUtils.isBlank(image.getLocalPath()) && StringUtils.isBlank(image.getBase64())) {
				try {
					String temp = image.getUrl().substring(image.getUrl().indexOf("fs/") + 3, image.getUrl().length());
					Long id = Long.parseLong(temp.split("&")[0].split("\\?")[0]);
					ids.add(id);
				} catch (NumberFormatException e) {
					logger.error("图片URL 处理不正确，URL=" + image.getUrl());
				}
			}
		}

		QuestionWordMLData questionWordML = new QuestionWordMLData();
		questionWordML.setId(question.getId());
		questionWordML.setCode(question.getCode());
		questionWordML.setContentML(contentML);
		questionWordML.setAnalysisML(analysisML);
		questionWordML.setHintML(hintML);
		questionWordML.setImageDatas(this.images);
		questionWordML.setAnswersML(answersML);
		questionWordML.setCreateAt(new Date());
		return questionWordML;
	}

	/**
	 * 处理图片（URL形式）.
	 * 
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	@Override
	Map<String, Object> downloadImage(String src) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(6);

		int imageWidth = 0;
		int imageHeight = 0;

		// 判断URL类型
		String localPath = null;
		if (src.indexOf("dd?") != -1) {
			String fn = src.substring(src.indexOf("fn=") + 3, src.length());
			localPath = HopeFileController.getHopePath(fn);
			java.io.File iofile = new java.io.File(localPath);
			if (iofile.exists()) {
				BufferedImage bufferImage = ImageIO.read(new FileInputStream(iofile));
				imageWidth = bufferImage.getWidth(null);
				imageHeight = bufferImage.getHeight(null);
			}
		} else {
			String idStr = src.substring(src.indexOf("fs/") + 3, src.length());
			if (idStr.indexOf("?") != -1) {
				idStr = idStr.substring(0, idStr.indexOf("?"));
			}
			if (idStr.indexOf("&") != -1) {
				idStr = idStr.substring(0, idStr.indexOf("&"));
			}
			try {
				File file = fileService.getFile(Long.parseLong(idStr));
				if (file != null) {
					imageWidth = file.getWidth();
					imageHeight = file.getHeight();
				}
				localPath = FileUtil.getFilePath(file);
			} catch (Exception e) {
				logger.error("word下载图片源有错误！url: " + src);
			}
		}

		if (imageWidth != 0 && imageHeight != 0) {
			// 图片宽高需要换算成EMU英制单位
			double width = imageWidth * 0.0264583;
			double height = imageHeight * 0.0264583;
			if (width > 15) {
				height = height * (15.0 / width);
				width = 15;
			}
			int int_width = (int) (width * 360000);
			int int_height = (int) (height * 360000);

			String rId = "rId_" + this.question.getCode() + "_" + images.size();
			map.put("name", "IMG" + this.question.getCode() + "_" + images.size() + ".png"); // 名称
			map.put("width", String.valueOf(int_width)); // 宽
			map.put("height", String.valueOf(int_height)); // 高
			map.put("base64", ""); // base64数据
			map.put("sequence", 1); // 序号
			map.put("rid", rId); // OOXML-relation

			QuestionWordImageData questionWordImage = new QuestionWordImageData();
			questionWordImage.setLocalPath(localPath);
			questionWordImage.setRid(rId);
			questionWordImage.setUrl(src);
			images.add(questionWordImage);
			return map;
		} else {
			logger.error("word下载图片源不存在！url: " + src);
			return null;
		}
	}

	/**
	 * 处理图片（base64形式）.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("restriction")
	@Override
	Map<String, Object> downloadImageFromBase64(String base64) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>(6);
		byte[] bytes = new BASE64Decoder().decodeBuffer(base64);
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(bytes);
			BufferedImage bufferImage = ImageIO.read(inputStream);
			if (bufferImage != null) {
				String rId = "rId_" + this.question.getCode() + "_" + images.size();

				// 图片宽高需要换算成EMU英制单位
				int width = (int) (bufferImage.getWidth(null) * 0.0264583);
				int height = (int) (bufferImage.getHeight(null) * 0.0264583);
				if (width > 15) {
					height = (int) (height * (15.0 / width));
					width = 15;
				}
				width = width * 360000;
				height = height * 360000;

				map.put("name", "IMG" + this.question.getCode() + "_" + images.size() + ".png"); // 名称
				map.put("width", String.valueOf(width)); // 宽
				map.put("height", String.valueOf(height)); // 高
				map.put("base64", ""); // base64数据
				map.put("sequence", 1); // 序号
				map.put("rid", rId); // OOXML-relation

				QuestionWordImageData questionWordImage = new QuestionWordImageData();
				questionWordImage.setRid(rId);
				questionWordImage.setBase64(base64);
				images.add(questionWordImage);
				return map;
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}
		return map;
	}
}
