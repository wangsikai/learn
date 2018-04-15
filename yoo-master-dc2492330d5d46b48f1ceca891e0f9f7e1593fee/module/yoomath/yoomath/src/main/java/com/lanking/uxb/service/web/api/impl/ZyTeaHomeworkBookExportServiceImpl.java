package com.lanking.uxb.service.web.api.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.export.word.utils.WordUtils;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionWordMLPretreat;
import com.lanking.uxb.service.web.api.ZyTeaHomeworkBookExportService;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 教师布置作业习题导出接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月13日
 */
@Component
public class ZyTeaHomeworkBookExportServiceImpl implements ZyTeaHomeworkBookExportService, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private FileService fileService;

	@Autowired
	private FreeMarkerConfigurationFactory freeMarkerConfigurationFactory;

	private Configuration configuration;
	private Template documentTemplate;
	private Template imageTemplate;
	private Template relsTemplate;
	private StreamSource streamSource;

	private static String[] UP_NUMBERS = { "一", "二", "三", "四", "五", "六" };

	@SuppressWarnings("unchecked")
	@Override
	public File export(String destDir, String host, String cacheFileName, String docTitle, List<Question> questions,
			Map<Long, QuestionWordMLData> questionWordMLDataMap) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(3);

		// 外部预处理的图片数据
		List<QuestionWordImageData> imgMLDatas = new ArrayList<QuestionWordImageData>();

		// 目标文件夹目录，初始化word导出工具
		WordUtils exportUtils = new WordUtils(destDir, host,
				Env.getString("word.template.destPath") + "/teacher-homework-book/template.zip", documentTemplate,
				imageTemplate, relsTemplate, streamSource, httpClient);

		// 整理数据结构
		List<Map<String, Object>> qtypes = new ArrayList<Map<String, Object>>(); // 习题分类
		Map<String, Object> singleChoices = new HashMap<String, Object>(3); // 单选题
		Map<String, Object> multiChoices = new HashMap<String, Object>(3); // 多选题
		Map<String, Object> blanks = new HashMap<String, Object>(3); // 填空题
		Map<String, Object> tfs = new HashMap<String, Object>(3); // 判断题
		Map<String, Object> qas = new HashMap<String, Object>(3); // 解答题
		singleChoices.put("questions", new ArrayList<Map<String, Object>>());
		singleChoices.put("oquestions", new ArrayList<Question>());
		multiChoices.put("questions", new ArrayList<Map<String, Object>>());
		multiChoices.put("oquestions", new ArrayList<Question>());
		blanks.put("questions", new ArrayList<Map<String, Object>>());
		blanks.put("oquestions", new ArrayList<Question>());
		tfs.put("questions", new ArrayList<Map<String, Object>>());
		tfs.put("oquestions", new ArrayList<Question>());
		qas.put("questions", new ArrayList<Map<String, Object>>());
		qas.put("oquestions", new ArrayList<Question>());

		int singleChoicesCount = 0;
		int multiChoicesCount = 0;
		int blanksCount = 0;
		int tfsCount = 0;
		int qasCount = 0;

		// 题目分类并统计分类总数
		for (int i = 0; i < questions.size(); i++) {
			Question question = questions.get(i);
			List<Question> qs = null;
			if (question.getType() == Question.Type.SINGLE_CHOICE) {
				qs = (List<Question>) singleChoices.get("oquestions");
				singleChoicesCount++;
			} else if (question.getType() == Question.Type.MULTIPLE_CHOICE) {
				qs = (List<Question>) multiChoices.get("oquestions");
				multiChoicesCount++;
			} else if (question.getType() == Question.Type.FILL_BLANK) {
				qs = (List<Question>) blanks.get("oquestions");
				blanksCount++;
			} else if (question.getType() == Question.Type.TRUE_OR_FALSE) {
				qs = (List<Question>) tfs.get("oquestions");
				tfsCount++;
			} else if (question.getType() == Question.Type.QUESTION_ANSWERING) {
				qs = (List<Question>) qas.get("oquestions");
				qasCount++;
			}
			if (question.getType() != Question.Type.COMPOSITE) {
				qs.add(question);
			}
		}

		// 题目转换
		int typenum = 0;
		int sequence = 1; // 题目序列
		if (singleChoicesCount > 0) {
			singleChoices.put("title", UP_NUMBERS[typenum] + "、单选题");
			for (Question question : (List<Question>) singleChoices.get("oquestions")) {
				Map<String, Object> questionMap = this.getQuestion(exportUtils, sequence, imgMLDatas, question,
						questionWordMLDataMap, host, httpClient);
				if (questionMap != null) {
					((List<Map<String, Object>>) singleChoices.get("questions")).add(questionMap);
					sequence++;
				}
			}
			qtypes.add(singleChoices);
			typenum++;
		}
		if (multiChoicesCount > 0) {
			multiChoices.put("title", UP_NUMBERS[typenum] + "、多选题");
			for (Question question : (List<Question>) multiChoices.get("oquestions")) {
				Map<String, Object> questionMap = this.getQuestion(exportUtils, sequence, imgMLDatas, question,
						questionWordMLDataMap, host, httpClient);
				if (questionMap != null) {
					((List<Map<String, Object>>) multiChoices.get("questions")).add(questionMap);
					sequence++;
				}
			}
			qtypes.add(multiChoices);
			typenum++;
		}
		if (blanksCount > 0) {
			blanks.put("title", UP_NUMBERS[typenum] + "、填空题");
			for (Question question : (List<Question>) blanks.get("oquestions")) {
				Map<String, Object> questionMap = this.getQuestion(exportUtils, sequence, imgMLDatas, question,
						questionWordMLDataMap, host, httpClient);
				if (questionMap != null) {
					((List<Map<String, Object>>) blanks.get("questions")).add(questionMap);
					sequence++;
				}
			}
			qtypes.add(blanks);
			typenum++;
		}
		if (tfsCount > 0) {
			tfs.put("title", UP_NUMBERS[typenum] + "、判断题");
			for (Question question : (List<Question>) tfs.get("oquestions")) {
				Map<String, Object> questionMap = this.getQuestion(exportUtils, sequence, imgMLDatas, question,
						questionWordMLDataMap, host, httpClient);
				if (questionMap != null) {
					((List<Map<String, Object>>) tfs.get("questions")).add(questionMap);
					sequence++;
				}
			}
			qtypes.add(tfs);
			typenum++;
		}
		if (qasCount > 0) {
			qas.put("title", UP_NUMBERS[typenum] + "、解答题");
			for (Question question : (List<Question>) qas.get("oquestions")) {
				Map<String, Object> questionMap = this.getQuestion(exportUtils, sequence, imgMLDatas, question,
						questionWordMLDataMap, host, httpClient);
				if (questionMap != null) {
					((List<Map<String, Object>>) qas.get("questions")).add(questionMap);
					sequence++;
				}
			}
			qtypes.add(qas);
			typenum++;
		}

		params.put("title", docTitle);
		params.put("qtypes", qtypes);

		File file = exportUtils.build(cacheFileName, params, imgMLDatas, null, null);
		return file;
	}

	/**
	 * 获取单个习题格式化数据.
	 * 
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
	private Map<String, Object> getQuestion(WordUtils exportUtils, int sequence, List<QuestionWordImageData> imgMLDatas,
			Question question, Map<Long, QuestionWordMLData> questionWordMLDataMap, String host, HttpClient httpClient)
			throws IOException, TemplateException {
		Map<String, Object> questionMap = new HashMap<String, Object>(2);

		// 找到习题WordML缓存
		QuestionWordMLData questionWordMLData = questionWordMLDataMap.get(question.getId());
		if (questionWordMLData == null) {
			try {
				QuestionWordMLPretreat pretreat = new QuestionWordMLPretreat(host, streamSource, imageTemplate,
						httpClient, fileService);
				questionWordMLData = pretreat.getQuestionContenMLDatas(questionService.get(question.getId()),
						answerService.getQuestionAnswers(question.getId()));
			} catch (Exception e) {
				logger.error("[教师布置作业练习导出] 转换QuestionWordMLData出错，question id = {}", question.getId());
				return null;
			}
		}
		if (questionWordMLData.getImageDatas() != null) {
			imgMLDatas.addAll(questionWordMLData.getImageDatas());
		}

		String contentML = WordUtils.contentPrefixAdd(questionWordMLData.getContentML(), sequence + ". ", false);
		questionMap.put("content", contentML);
		return questionMap;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configuration = freeMarkerConfigurationFactory.createConfiguration();

		final File baseFileDir = new File(Env.getString("word.template.destPath") + "/teacher-homework-book");
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		configuration.setTemplateLoader(new FileTemplateLoader(baseFileDir));

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver
				.getResources("classpath*:" + Env.getString("word.template.srcPath") + "/teacher-homework-book/*.*");
		if (resources != null) {
			for (Resource resource : resources) {
				String fileName = resource.getFilename();
				File ftlFile = new File(baseFileDir, fileName);
				if (ftlFile.exists()) {
					ftlFile.delete();
				}
				ftlFile.createNewFile();

				InputStream ins = resource.getInputStream();
				OutputStream os = new FileOutputStream(ftlFile);
				int bytesRead = 0;
				byte[] buffer = new byte[4096];
				while ((bytesRead = ins.read(buffer, 0, 4096)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				ins.close();
			}
		}

		imageTemplate = configuration.getTemplate("image.ftl", Charsets.UTF8);
		documentTemplate = configuration.getTemplate("document.ftl", Charsets.UTF8);
		relsTemplate = configuration.getTemplate("document.xml.rels.ftl", Charsets.UTF8);
		streamSource = new StreamSource(Env.getString("word.template.destPath") + "/MML2OMML.xsl");
	}
}
