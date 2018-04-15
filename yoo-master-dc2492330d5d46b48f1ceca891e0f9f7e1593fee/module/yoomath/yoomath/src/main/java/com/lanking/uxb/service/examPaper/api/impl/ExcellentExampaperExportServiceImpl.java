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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperQuestion;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopic;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperTopicType;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.ZipUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.examPaper.api.ExcellentExampaperExportService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvertOption;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm.Scope;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm.Sets;
import com.lanking.uxb.service.examPaper.form.TeaExcellentExampaperExportForm.Type;
import com.lanking.uxb.service.examPaper.value.VCustomExamPaperQuestion;
import com.lanking.uxb.service.export.word.utils.WordUtils;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsSnapshotService;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.util.QuestionWordMLPretreat;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VQuestion;

/**
 * 精品试卷导出接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月7日
 */
@Service
public class ExcellentExampaperExportServiceImpl implements ExcellentExampaperExportService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> examRepo;
	@Autowired
	@Qualifier("ExamPaperQuestionRepo")
	private Repo<ExamPaperQuestion, Long> examQuestionRepo;
	@Autowired
	@Qualifier("ExamPaperTopicRepo")
	private Repo<ExamPaperTopic, Long> examTopicRepo;

	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private FileService fileService;
	@Autowired
	private ExampaperExportConfigService configService;
	@Autowired
	private ResourcesGoodsSnapshotService resourcesGoodsSnapshotService;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private HttpClient httpClient;
	private String[] zh_num = { "一", "二", "三", "四", "五" };

	@Override
	@Transactional(readOnly = true)
	public ExamPaper getPaper(long id) {
		return examRepo.get(id);
	}

	@Override
	@Transactional(readOnly = true)
	public int createTeaExcellentExampaperDoc(TeaExcellentExampaperExportForm form) throws Exception {
		String outName = "";
		ExamPaper examPaper = null;
		if (form.getExampaperID() == null) {
			ResourcesGoodsSnapshot goods = resourcesGoodsSnapshotService.get(form.getResourcesGoodsSnapshotID());
			GoodsSnapshot goodsSnapshot = goodsSnapshotService.get(form.getGoodsSnapshotID());
			examPaper = examRepo.get(goods.getResourcesId());
			outName = goodsSnapshot.getName();
		} else {
			examPaper = examRepo.get(form.getExampaperID());
			outName = examPaper.getName();
		}
		String host = form.getHost();

		// 获取文档存储路径
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String createAtStr = format.format(examPaper.getCreateAt());
		String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/tea-exampaper-ext/")
				.append(createAtStr).append("/").append(examPaper.getCreateId()).toString();

		// 判断压缩文档是否已存在
		String tohashString = examPaper.getId() + "_" + examPaper.getUpdateAt().getTime() + "-" + form.getSets().name()
				+ "-" + form.getScopes().toString() + "-" + outName;
		int hash = tohashString.hashCode();
		String hashName = "h" + hash;
		String zipFilePath = destDir + "/" + hashName + ".zip";
		String zipFilePDFPath = destDir + "/" + hashName + "-pdf.zip";

		File zipFile = new File(form.getType() == Type.DOCX ? zipFilePath : zipFilePDFPath);
		if (zipFile.exists()) {
			return hash;
		}

		// 生成文档
		CustomExampaperConvertOption option = new CustomExampaperConvertOption();
		option.setShowQuestions(true);
		option.setShowTopic(true);

		Map<String, Object> templateMap = new HashMap<String, Object>();
		templateMap.put("title", outName);
		templateMap.put("time", "");
		templateMap.put("showtTime", false);

		// 习题
		int totalScore = 0;
		List<ExamPaperQuestion> examQuestions = examQuestionRepo
				.find("$zyListExportQuestionsByExampaper", Params.param("examId", examPaper.getId())).list();
		List<Long> questionIds = new ArrayList<Long>();
		for (ExamPaperQuestion examPaperQuestion : examQuestions) {
			questionIds.add(examPaperQuestion.getQuestionId());
			totalScore += examPaperQuestion.getScore() == null ? 0 : examPaperQuestion.getScore().intValue();
		}
		templateMap.put("totalScore", totalScore == 0 ? "____________________" : totalScore); // 总分

		QuestionConvertOption questionOption = new QuestionConvertOption();
		questionOption.setAnalysis(true);
		questionOption.setInitExamination(true);
		questionOption.setAnswer(true);
		Map<Long, VQuestion> quesionMap = questionConvert.to(questionService.mget(questionIds), questionOption);

		// 分类
		List<ExamPaperTopic> examPaperTopics = examTopicRepo
				.find("$zyFindExportByPaper", Params.param("paperId", examPaper.getId())).list();
		Map<Long, ExamPaperTopic> topicMap = new HashMap<Long, ExamPaperTopic>(examPaperTopics.size());
		for (ExamPaperTopic topic : examPaperTopics) {
			topicMap.put(topic.getId(), topic);
		}

		// 获 xd取习题WordML缓存
		Map<Long, QuestionWordMLData> questionWordMLDataMap = questionWordMLService.mget(questionIds);

		// 外部预处理的图片数据
		List<QuestionWordImageData> imgMLDatas = new ArrayList<QuestionWordImageData>();

		// 拼装分类习题
		Map<Long, List<VCustomExamPaperQuestion>> topicQuestionMap = new HashMap<Long, List<VCustomExamPaperQuestion>>();
		for (ExamPaperQuestion examPaperQuestion : examQuestions) {
			List<VCustomExamPaperQuestion> topicQuestions = topicQuestionMap.get(examPaperQuestion.getTopicId());
			if (topicQuestions == null) {
				topicQuestions = new ArrayList<VCustomExamPaperQuestion>();
				topicQuestionMap.put(examPaperQuestion.getTopicId(), topicQuestions);
			}
			VCustomExamPaperQuestion vcq = new VCustomExamPaperQuestion();
			vcq.setQuestion(quesionMap.get(examPaperQuestion.getQuestionId()));
			vcq.setCreateAt(vcq.getCreateAt());
			vcq.setScore(examPaperQuestion.getScore());
			vcq.setSequence(vcq.getSequence());
			topicQuestions.add(vcq);
		}

		List<Map<String, Object>> topics = new ArrayList<Map<String, Object>>(3);
		int totalSeq = 0;
		int topicNum = 0;
		for (int i = 0; i < examPaperTopics.size(); i++) {
			ExamPaperTopic topic = examPaperTopics.get(i);
			List<VCustomExamPaperQuestion> topicQuestions = topicQuestionMap.get(topic.getId());
			if (topicQuestions == null || topicQuestions.size() == 0) {
				topicQuestions = new ArrayList<VCustomExamPaperQuestion>(0);
				continue;
			}
			topics.add(this.fillTopicQuestions(topicNum, totalSeq, topic.getType(), topicQuestions,
					questionWordMLDataMap, host, httpClient, imgMLDatas));
			totalSeq += topicQuestions.size();
			topicNum++;
		}
		templateMap.put("topics", topics);

		// 生成缓存文件、压缩打包
		String tempFileDir = destDir + "/" + hashName + "-temp";
		String tempFilePDFDir = destDir + "/" + hashName + "-pdf-temp";
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

			String outFileName = hashName + "-" + scope.name() + "-" + form.getSets().name();
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
	private Map<String, Object> fillTopicQuestions(int index, int totalSeq, ExamPaperTopicType type,
			List<VCustomExamPaperQuestion> paperQuestions, Map<Long, QuestionWordMLData> questionWordMLDataMap,
			String host, HttpClient httpClient, List<QuestionWordImageData> imgMLDatas) {
		Map<String, Object> topic = new HashMap<String, Object>();

		int totalScore = 0;
		int singleScore = 0;
		List<Map<String, Object>> questions = new ArrayList<Map<String, Object>>();
		if (paperQuestions != null) {
			for (int i = 0; i < paperQuestions.size(); i++) {
				VCustomExamPaperQuestion paperQuestion = paperQuestions.get(i);
				int score = paperQuestion.getScore() == null ? 0 : paperQuestion.getScore();
				if (i == 0) {
					singleScore = score;
				}
				Map<String, Object> questionMap = new HashMap<String, Object>();
				totalScore += score;

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
						logger.error("[精品试卷导出] 转换QuestionWordMLData出错，question id = {}", vquestion.getId());
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
					part = "（本题" + (score > 0 ? score : "__________") + "分）";
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
					Integer questionScore = paperQuestion.getScore() == null ? 0 : paperQuestion.getScore().intValue();
					answersML = WordUtils.contentPrefixAdd(questionWordMLData.getAnswersML(),
							(totalSeq + i + 1) + "、（本题" + (questionScore > 0 ? questionScore : "__________") + "分）",
							true);
				}
				questionMap.put("answers", answersML);
				questions.add(questionMap);
			}
		}

		StringBuffer tilte = new StringBuffer(this.zh_num[index]);
		Object singleScoreStr = singleScore > 0 ? singleScore : "__________";
		Object totalScoreStr = totalScore > 0 ? totalScore : "__________";
		if (type == ExamPaperTopicType.SINGLE_CHOICE) {
			tilte.append("、单选题（共").append(paperQuestions.size()).append("题，每题").append(singleScoreStr).append("分，总计")
					.append(totalScoreStr).append("分)");
		} else if (type == ExamPaperTopicType.FILL_BLANK) {
			tilte.append("、填空题（共").append(paperQuestions.size()).append("题，每题").append(singleScoreStr).append("分，总计")
					.append(totalScoreStr).append("分)");
		} else if (type == ExamPaperTopicType.QUESTION_ANSWERING) {
			tilte.append("、解答题（共").append(paperQuestions.size()).append("题，总计").append(totalScoreStr).append("分)");
		}
		topic.put("title", tilte.toString());
		topic.put("questions", questions);

		return topic;
	}
}
