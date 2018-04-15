package com.lanking.uxb.service.examPaper.api.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperCfg;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopic;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperTopicType;
import com.lanking.cloud.sdk.util.ZipUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.examPaper.api.CustomExampaperExportService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvert;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvertOption;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm.Scope;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm.Sets;
import com.lanking.uxb.service.examPaper.form.TeaCustomExampaperExportForm.Type;
import com.lanking.uxb.service.examPaper.value.VCustomExamPaperQuestion;
import com.lanking.uxb.service.examPaper.value.VCustomExampaper;
import com.lanking.uxb.service.export.word.utils.WordUtils;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.util.QuestionWordMLPretreat;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 组件导出接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月19日
 */
@Service
public class CustomExampaperExportServiceImpl implements CustomExampaperExportService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private CustomExampaperService customExampaperService;
	@Autowired
	private CustomExampaperConvert customExampaperConvert;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ExampaperExportConfigService configService;
	// @Autowired
	// private DocumentHandle documentHandle;

	@Autowired
	private HttpClient httpClient;

	private String[] zh_num = { "一", "二", "三", "四", "五" };

	@Override
	public int createTeaCustomExampaperDoc(TeaCustomExampaperExportForm form) throws Exception {
		CustomExampaper customExampaper = customExampaperService.get(form.getId());
		String host = form.getHost();

		// 获取文档存储路径
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String createAtStr = format.format(customExampaper.getCreateAt());
		String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/tea-exampaper/")
				.append(createAtStr).append("/").append(customExampaper.getCreateId()).toString();

		// 判断压缩文档是否已存在
		String tohashString = customExampaper.getId() + "_" + customExampaper.getUpdateAt().getTime() + "-"
				+ form.getSets().name() + "-" + form.getScopes().toString();
		int hash = tohashString.hashCode();
		String zipFilePath = destDir + "/" + hash + ".zip";
		String zipFilePDFPath = destDir + "/" + hash + "-pdf.zip";

		File zipFile = new File(form.getType() == Type.DOCX ? zipFilePath : zipFilePDFPath);
		if (zipFile.exists()) {
			return hash;
		}

		// 生成文档
		CustomExampaperConvertOption option = new CustomExampaperConvertOption();
		option.setShowQuestions(true);
		option.setShowTopic(true);
		VCustomExampaper vCustomExampaper = customExampaperConvert.to(customExampaper, option);

		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("title", customExampaper.getName());
		templateMap.put("time", customExampaper.getTime());
		templateMap.put("showtTime",
				(customExampaper.getTime() != null || customExampaper.getTime() != 0) ? true : false);
		templateMap.put("totalScore", customExampaper.getScore());

		// 习题分类
		List<Long> questionIds = new ArrayList<Long>();
		for (VCustomExamPaperQuestion vPaperQuestion : vCustomExampaper.getQuestions()) {
			questionIds.add(vPaperQuestion.getQuestion().getId());
		}

		// 获取习题WordML缓存
		Map<Long, QuestionWordMLData> questionWordMLDataMap = questionWordMLService.mget(questionIds);

		// 外部预处理的图片数据
		List<QuestionWordImageData> imgMLDatas = new ArrayList<QuestionWordImageData>();

		// 拼装分类习题
		Map<Long, List<VCustomExamPaperQuestion>> topicQuestionMap = new HashMap<Long, List<VCustomExamPaperQuestion>>();
		for (VCustomExamPaperQuestion vPaperQuestion : vCustomExampaper.getQuestions()) {
			List<VCustomExamPaperQuestion> topicQuestions = topicQuestionMap.get(vPaperQuestion.getTopicId());
			if (topicQuestions == null) {
				topicQuestions = new ArrayList<VCustomExamPaperQuestion>();
				topicQuestionMap.put(vPaperQuestion.getTopicId(), topicQuestions);
			}
			topicQuestions.add(vPaperQuestion);
		}

		List<Map<String, Object>> topics = new ArrayList<Map<String, Object>>(3);
		int totalSeq = 0;
		int topicNum = 0;
		for (int i = 0; i < vCustomExampaper.getTopic().size(); i++) {
			CustomExampaperTopic topic = vCustomExampaper.getTopic().get(i);
			List<VCustomExamPaperQuestion> topicQuestions = topicQuestionMap.get(topic.getId());
			if (topicQuestions == null || topicQuestions.size() == 0) {
				topicQuestions = new ArrayList<VCustomExamPaperQuestion>(0);
				continue;
			}
			topics.add(this.fillTopicQuestions(topicNum, totalSeq, vCustomExampaper.getCfg(), topic.getType(),
					topicQuestions, questionWordMLDataMap, host, httpClient, imgMLDatas));
			totalSeq += topicQuestions.size();
			topicNum++;
		}
		templateMap.put("topics", topics);

		// 生成缓存文件、压缩打包
		String tempFileDir = destDir + "/" + hash + "-temp";
		String tempFilePDFDir = destDir + "/" + hash + "-pdf-temp";
		String docname = "";
		if (form.getSets() == Sets.K16) {
			docname = "16开";
		} else if (form.getSets() == Sets.K8_2) {
			docname = "8K双栏";
		} else if (form.getSets() == Sets.A3_2) {
			docname = "A3双栏";
		} else if (form.getSets() == Sets.A4) {
			docname = "A4";
		}
		File outFileDir = new File(tempFileDir);
		if (!outFileDir.exists()) {
			outFileDir.mkdirs();
		}
		File outFilePDFDir = new File(tempFilePDFDir);
		if (!outFilePDFDir.exists()) {
			outFilePDFDir.mkdirs();
		}
		for (Scope scope : form.getScopes()) {
			// 目标文件夹目录
			WordUtils exportUtils = new WordUtils(destDir, host,
					Env.getString("word.template.destPath") + "/teacher-exampaper/template.zip",
					configService.documentTemplateMap.get(form.getSets().getValue()), configService.imageTemplate,
					configService.relsTemplate, configService.streamSource, httpClient);
			boolean showQuestion = false;
			boolean showAnswer = false;
			if (scope == Scope.STUDENT || scope == Scope.TEACHER) {
				showQuestion = true;
			}
			if (scope == Scope.TEACHER || scope == Scope.ANSWER) {
				showAnswer = true;
			}
			templateMap.put("showQuestion", showQuestion);
			templateMap.put("showAnswer", showAnswer);

			String outFileName = hash + "-" + scope.name() + "-" + form.getSets().name();
			File file = exportUtils.build(outFileName, templateMap, imgMLDatas, destDir + "/template/word/document.xml",
					destDir + "/template/word/_rels/document.xml.rels");
			InputStream inputStream = new FileInputStream(file);
			String filePath = tempFileDir + "/" + scope.getZhName() + "-" + docname + ".docx";
			FileOutputStream outputStream = new FileOutputStream(filePath);
			FileChannel in = ((FileInputStream) inputStream).getChannel();
			FileChannel out = outputStream.getChannel();
			in.transferTo(0, in.size(), out); // NIO输出
			out.close();
			in.close();
			outputStream.close();
			inputStream.close();

			if (form.getType() == Type.PDF) {
				com.lanking.cloud.domain.base.file.File dmfile = new com.lanking.cloud.domain.base.file.File();
				dmfile.setName(filePath);
				// documentHandle.convertDOC2PDF(filePath, dmfile);
				configService.copyFile(new File(filePath),
						tempFilePDFDir + "/" + scope.getZhName() + "-" + docname + ".pdf");
			}
		}

		// 压缩
		ZipUtils.zip(tempFileDir, zipFilePath);

		// 删除template
		configService.deleteDir(new File(tempFileDir));

		// 下载标记
		customExampaperService.updateDownloadFlag(customExampaper.getId());

		return hash;
	}

	/**
	 * 填充题目类型数据.
	 * 
	 * @param index
	 *            分类index
	 * @param totalSeq
	 *            题目序号index
	 * @param config
	 *            配置
	 * @param type
	 *            类型
	 * @param paperQuestions
	 *            组卷习题
	 * @param questionWordMLDataMap
	 *            习题wordML缓存
	 * @param host
	 *            服务地址
	 * @param httpClient
	 * @param imgMLDatas
	 *            图片openXML数据
	 * @return
	 */
	private Map<String, Object> fillTopicQuestions(int index, int totalSeq, CustomExampaperCfg config,
			CustomExampaperTopicType type, List<VCustomExamPaperQuestion> paperQuestions,
			Map<Long, QuestionWordMLData> questionWordMLDataMap, String host, HttpClient httpClient,
			List<QuestionWordImageData> imgMLDatas) {
		Map<String, Object> topic = new HashMap<String, Object>();

		int totalScore = 0;
		List<Map<String, Object>> questions = new ArrayList<Map<String, Object>>();
		if (paperQuestions != null) {
			for (int i = 0; i < paperQuestions.size(); i++) {
				VCustomExamPaperQuestion paperQuestion = paperQuestions.get(i);
				Map<String, Object> questionMap = new HashMap<String, Object>();
				totalScore += paperQuestion.getScore();

				// 找到习题WordML缓存
				VQuestion vquestion = paperQuestion.getQuestion();
				QuestionWordMLData questionWordMLData = questionWordMLDataMap.get(vquestion.getId());
				if (questionWordMLData == null) {
					try {
						QuestionWordMLPretreat pretreat = new QuestionWordMLPretreat(host, configService.streamSource,
								configService.imageTemplate, httpClient, fileService);
						questionWordMLData = pretreat.getQuestionContenMLDatas(questionService.get(vquestion.getId()),
								answerService.getQuestionAnswers(vquestion.getId()));
					} catch (Exception e) {
						logger.error("[教师组卷导出] 转换QuestionWordMLData出错，question id = {}", vquestion.getId());
						continue;
					}
				}
				if (questionWordMLData.getImageDatas() != null) {
					imgMLDatas.addAll(questionWordMLData.getImageDatas());
				}

				// 处理前置信息
				String part = "";
				if (paperQuestion.getQuestion().getType() == Question.Type.QUESTION_ANSWERING) {
					// 解答题
					part = "（本题" + paperQuestion.getScore() + "分）";
				}
				String contentML = WordUtils.contentPrefixAdd(questionWordMLData.getContentML(),
						(totalSeq + i + 1) + "、" + part, false);
				if (paperQuestion.getQuestion().getType() == Question.Type.QUESTION_ANSWERING) {
					// 解答题
					contentML += "<w:p/><w:p/><w:p/><w:p/><w:p/><w:p/><w:p/><w:p/><w:p/><w:p/>";
				}
				questionMap.put("content", contentML);

				// 处理答案
				StringBuffer anwsers = new StringBuffer();
				for (VAnswer answers : vquestion.getAnswers()) {
					anwsers.append(answers.getContent()).append("    ");
				}
				String answersML = "";
				if (vquestion.getType() != Question.Type.QUESTION_ANSWERING) {
					answersML = WordUtils.contentPrefixAdd(questionWordMLData.getAnswersML(), (totalSeq + i + 1) + "、",
							false);
				} else {
					answersML = WordUtils.contentPrefixAdd(questionWordMLData.getAnswersML(),
							(totalSeq + i + 1) + "、（本题" + paperQuestion.getScore() + "分）", true);
				}
				questionMap.put("answers", answersML);
				questions.add(questionMap);
			}
		}

		StringBuffer tilte = new StringBuffer(this.zh_num[index]);
		if (type == CustomExampaperTopicType.SINGLE_CHOICE) {
			tilte.append("、单选题（共").append(paperQuestions.size()).append("题，每题").append(config.getSingleChoiceScore())
					.append("分，总计").append(totalScore).append("分)");
		} else if (type == CustomExampaperTopicType.FILL_BLANK) {
			tilte.append("、填空题（共").append(paperQuestions.size()).append("题，每空").append(config.getFillBlankScore())
					.append("分，总计").append(totalScore).append("分)");
		} else if (type == CustomExampaperTopicType.QUESTION_ANSWERING) {
			tilte.append("、解答题（共").append(paperQuestions.size()).append("题，总计").append(totalScore).append("分)");
		}
		topic.put("title", tilte.toString());
		topic.put("questions", questions);

		return topic;
	}
}
