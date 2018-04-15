package com.lanking.uxb.service.web.api.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;
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

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.question.QuestionWordImageData;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.sdk.util.Charsets;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.export.word.utils.WordUtils;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.question.util.QuestionWordMLPretreat;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.web.api.ZyStudentFallibleExportService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import httl.util.CollectionUtils;

/**
 * 学生错题导出接口实现.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年9月13日
 */
@Component
public class ZyStudentFallibleExportServiceImpl implements ZyStudentFallibleExportService, InitializingBean {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private FileService fileService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private FreeMarkerConfigurationFactory freeMarkerConfigurationFactory;
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private ZyStudentFallibleExportRecordService studentFallibleExportRecordService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private StudentQuestionAnswerService studentQuestionAnswerService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private ZyStudentFallibleExportService studentFallibleExportService;

	private Configuration configuration;
	private Template documentTemplate;
	private Template imageTemplate;
	private Template relsTemplate;
	private StreamSource streamSource;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

	@SuppressWarnings("unchecked")
	@Override
	public File exportStuFallible(String destDir, String docTitle, List<Map<String, Object>> sectionList,
			Map<Long, List<Map<String, Object>>> historyMap, Map<String, String> sfQuestionRateMap, String host,
			int hash, Map<Long, QuestionWordMLData> questionWordMLDataMap, Map<Long, String> qrcodeMap,
			Map<String, String> outLocalImgMap) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(3);
		SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm");

		// 外部预处理的图片数据
		List<QuestionWordImageData> imgMLDatas = new ArrayList<QuestionWordImageData>();

		// 目标文件夹目录，初始化word导出工具
		WordUtils exportUtils = new WordUtils(destDir, host,
				Env.getString("word.template.destPath") + "/student-fallible-self/template.zip", documentTemplate,
				imageTemplate, relsTemplate, streamSource, httpClient);
		exportUtils.setOutLocalImgMap(outLocalImgMap);

		// 整理章节习题数据
		for (Map<String, Object> sectionMap : sectionList) {

			// 题目处理
			List<VQuestion> questionList = (List<VQuestion>) sectionMap.get("questions");
			List<Map<String, Object>> questions = new ArrayList<Map<String, Object>>(questionList.size());

			int sequence = 0;
			for (VQuestion vquestion : questionList) {
				sequence++;
				Map<String, Object> question = new HashMap<String, Object>();

				// 找到习题WordML缓存
				QuestionWordMLData questionWordMLData = questionWordMLDataMap.get(vquestion.getId());
				if (questionWordMLData == null) {
					try {
						QuestionWordMLPretreat pretreat = new QuestionWordMLPretreat(host, streamSource, imageTemplate,
								httpClient, fileService);
						questionWordMLData = pretreat.getQuestionContenMLDatas(questionService.get(vquestion.getId()),
								answerService.getQuestionAnswers(vquestion.getId()));
					} catch (Exception e) {
						logger.error("[学生错题导出] 转换QuestionWordMLData出错，question id = {}", vquestion.getId());
						continue;
					}
				}
				if (questionWordMLData.getImageDatas() != null) {
					imgMLDatas.addAll(questionWordMLData.getImageDatas());
				}

				String contentML = WordUtils.contentPrefixAdd(questionWordMLData.getContentML(),
						"[" + vquestion.getCode() + "] ", false);
				question.put("content", contentML);
				question.put("code", vquestion.getCode());
				question.put("sequence", sequence);
				question.put("knowlegde", CollectionUtils.isNotEmpty(vquestion.getNewKnowpoints())
						? vquestion.getNewKnowpoints().get(0).getName() : "");
				question.put("difficulty", vquestion.getDifficulty());

				String rate = sfQuestionRateMap.get(String.valueOf(vquestion.getId()));
				question.put("rightRate", rate == null ? "" : rate + "%");

				// 处理答案
				StringBuffer anwsers = new StringBuffer();
				for (VAnswer answers : vquestion.getAnswers()) {
					anwsers.append(answers.getContent()).append("    ");
				}
				String answersML = "";
				if (questionWordMLData != null && questionWordMLData.getAnswersML().length() > 5) {
					answersML = WordUtils.contentPrefixAdd(questionWordMLData.getAnswersML(), "答案：", false);
				} else {
					answersML = exportUtils.transContent(null, "答案：" + anwsers.toString());
				}
				question.put("answers", answersML);

				// 处理解析
				String analysisML = "";
				if (questionWordMLData != null && questionWordMLData.getAnalysisML().length() > 5) {
					answersML = WordUtils.contentPrefixAdd(questionWordMLData.getAnalysisML(), "解析：", false);
				} else {
					analysisML = exportUtils.transContent(null, "解析：" + vquestion.getAnalysis());
				}
				question.put("analysis", analysisML);

				// 处理答题历史
				List<Map<String, Object>> historys = historyMap.get(vquestion.getId());
				if (null != historyMap && historys != null && historys.size() > 0) {
					for (Map<String, Object> history : historys) {
						history.put("content", exportUtils.transContent(null, history.get("content").toString()));
					}
					question.put("history1", historys.get(0));
					historys.remove(0);
					question.put("historys", historys);
				}

				// 处理二维码
				String qrcodepath = qrcodeMap.get(vquestion.getId());
				QuestionWordImageData imgMLData = new QuestionWordImageData();
				imgMLData.setRid("QR" + vquestion.getCode());
				imgMLData.setLocalPath(qrcodepath);
				imgMLDatas.add(imgMLData);

				questions.add(question);
			}
			sectionMap.put("questions", questions);
		}

		params.put("docTitle", docTitle);
		params.put("sections", sectionList);
		params.put("exportTime", format.format(new Date()));

		File file = exportUtils.build(String.valueOf(hash), params, imgMLDatas, null, null);
		return file;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		configuration = freeMarkerConfigurationFactory.createConfiguration();

		final File baseFileDir = new File(Env.getString("word.template.destPath") + "/student-fallible-self");
		if (!baseFileDir.exists()) {
			baseFileDir.mkdirs();
		}
		configuration.setTemplateLoader(new FileTemplateLoader(baseFileDir));

		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver
				.getResources("classpath*:" + Env.getString("word.template.srcPath") + "/student-fallible-self/*.*");
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

	@SuppressWarnings("rawtypes")
	@Override
	public Object writeFileAndRecordTask(final String host, final long studentId, final int hash,
			final List<Map> fquesions, final List<Long> sectionCodes, final Map<String, Object> map, final boolean buy,
			final String attachData, final Long printOrderID, final boolean isMember, final Date createAt) {
		Object result = "fail";

		// 错题记录同步生成
		if (createAt != null) {
			Future future = fixedThreadPool.submit(new Runnable() {
				@Override
				public void run() {
					try {
						writeFileAndRecord(host, studentId, hash, fquesions, sectionCodes, map, buy, attachData,
								printOrderID, isMember, createAt);
					} catch (Exception e) {
						logger.error("保存学生错题文档失败！", e);
					}
				}
			});

			try {
				// returns null if the task has finished correctly.
				result = future.get();
			} catch (InterruptedException | ExecutionException e) {
				logger.error("保存学生错题文档失败！", e);
			}
		} else {
			// 错题本生成保持原有异步处理
			fixedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					try {
						writeFileAndRecord(host, studentId, hash, fquesions, sectionCodes, map, buy, attachData,
								printOrderID, isMember, createAt);
					} catch (Exception e) {
						logger.error("保存学生错题文档失败！", e);
					}
				}
			});
		}

		return result;
	}

	/**
	 * 处理学生错题文档生成，记录.
	 * 
	 * @param host
	 *            服务地址
	 * @param studentId
	 * @param hash
	 * @param fquesions
	 * @param sectionCodes
	 * @param returnmap
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void writeFileAndRecord(String host, long studentId, int hash, List<Map> fquesions, List<Long> sectionCodes,
			Map<String, Object> returnmap, boolean buy, final String attachData, final Long printOrderID,
			boolean isMember, Date createAt) throws Exception {
		// 处理文档
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String today = createAt == null ? format.format(new Date()) : format.format(createAt);
		String name = "错题本（" + today + "）";

		// 获取文档存储路径
		String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/stuFallible2/")
				.append(studentId).append("/").append(today).toString();

		File file = this.createStudentFallDocument(destDir, name, studentId, "http://" + host, hash, sectionCodes,
				fquesions);

		if (createAt == null) {
			// 保存记录
			double size = Math.round(file.length() * 10 / 1024.0) / 10.0;
			StudentFallibleExportRecord record = new StudentFallibleExportRecord();
			record.setHash(hash);
			record.setBuy(isMember ? true : buy); // 会员直接免费使用
			record.setCount(fquesions.size());
			record.setCreatAt(new Date());
			record.setExtend("DOCX");
			record.setName(name);
			record.setSectionCodes(sectionCodes);
			record.setSellPrice(fquesions.size());
			record.setSize(size);
			record.setStudentId(studentId);
			record.setAttachData(attachData);

			// 错题代打印订单记录
			if (printOrderID != null) {
				FallibleQuestionPrintOrder order = stuFalliblePrintService.get(printOrderID);
				record.setBuy(true);
				record.setName("错题本代印");
				record.setAttachData(attachData);
				record.setFallibleQuestionPrintOrderId(printOrderID);
				record.setSellPriceRMB(order.getTotalPrice());
				studentFallibleExportRecordService.save(record);
			} else {
				studentFallibleExportRecordService.save(record);

				// 金币记录
				if (buy && !isMember) {
					coinsService.earn(CoinsAction.BUY_COINS_FALLIBLE_DOC, studentId, -fquesions.size(), Biz.NULL, 0);
				}
			}

			returnmap.put("recordId", record.getId());
			returnmap.put("hash", hash);
			returnmap.put("fileSize", size);
		}
	}

	/**
	 * 生成学生错题文档.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private File createStudentFallDocument(String destDir, String name, Long studentId, String host, int hash,
			List<Long> sectionCodes, List<Map> fquesions) throws Exception {

		Set<Long> questionIds = new HashSet<Long>(fquesions.size());
		for (Map fquestionMap : fquesions) {
			questionIds.add(Long.parseLong(fquestionMap.get("qid").toString()));
		}

		// 获得章节
		Map<Long, Section> sectionMap = sectionService.mget(sectionCodes);
		List<Section> sections = new ArrayList<Section>(sectionMap.size());
		for (Long code : sectionCodes) {
			sections.add(sectionMap.get(code));
		}

		// 获取习题WordML缓存
		Map<Long, QuestionWordMLData> questionWordMLDataMap = questionWordMLService.mget(questionIds);

		// 获取习题
		Map<String, Map<String, Object>> sectionTempMap = new HashMap<String, Map<String, Object>>(sections.size()); // 章节习题临时Map
		QuestionConvertOption option = new QuestionConvertOption();
		option.setAnswer(true);
		option.setAnalysis(true);
		Map<Long, VQuestion> questionMap = questionIds.size() == 0 ? new HashMap<Long, VQuestion>()
				: questionConvert.to(questionService.mget(questionIds), option);

		// 获取习题二维码
		Map<Long, String> qrcodeMap = new HashMap<Long, String>(questionMap.size());
		for (VQuestion vQuestion : questionMap.values()) {
			String code = vQuestion.getCode();
			String p1 = code.substring(0, 3);
			String p2 = code.substring(3, 6);
			String basepath = Env.getString("question.qrcode.path");
			qrcodeMap.put(vQuestion.getId(), basepath + p1 + "/" + p2 + "/" + code + ".jpg");
		}

		// 获取答题历史
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Map<Long, List<Map<String, Object>>> historyMap = new HashMap<Long, List<Map<String, Object>>>();
		List<StudentQuestionAnswer> historys = studentQuestionAnswerService.findByQuestionIdGroup(studentId,
				questionIds, 5);

		// 处理答题历史图片
		List<Long> fileIds = new ArrayList<Long>();
		for (StudentQuestionAnswer answer : historys) {
			if (answer.getAnswerImgs() != null && answer.getAnswerImgs().size() > 0) {
				// 多图
				for (int i = 0; i < answer.getAnswerImgs().size(); i++) {
					if (answer.getAnswerImgs().get(i) != null && answer.getAnswerImgs().get(i) != 0) {
						fileIds.add(answer.getAnswerImgs().get(i));
					}
				}
			} else if (answer.getAnswerImg() != null && answer.getAnswerImg() != 0) {
				fileIds.add(answer.getAnswerImg());
			}
		}
		Map<Long, com.lanking.cloud.domain.base.file.File> historysFileMap = new HashMap<Long, com.lanking.cloud.domain.base.file.File>(
				fileIds.size());
		Map<String, String> outLocalImgMap = new HashMap<String, String>();
		if (fileIds.size() > 0) {
			historysFileMap = fileService.mgetFile(fileIds);
			for (Long fileId : fileIds) {
				com.lanking.cloud.domain.base.file.File file = historysFileMap.get(fileId);
				String localPath = FileUtil.getFilePath(file);
				outLocalImgMap.put(host + FileUtil.getUrl(fileId), localPath);
			}
		}

		for (StudentQuestionAnswer answer : historys) {
			List<Map<String, Object>> answers = historyMap.get(answer.getQuestionId());
			if (answers == null) {
				answers = new ArrayList<Map<String, Object>>();
				historyMap.put(answer.getQuestionId(), answers);
			}
			Map<String, Object> history = new HashMap<String, Object>(2);
			String answerContents = "";

			// @since web v2.3.1 有答案显示答案，没答案显示
			Map<Long, List<String>> ansMap = answer.getAnswers();
			for (Entry<Long, List<String>> entry : ansMap.entrySet()) {
				for (String str : entry.getValue()) {
					answerContents += str + " 、";
				}
			}
			if (answerContents.length() > 0) {
				answerContents = answerContents.substring(0, answerContents.length() - 1);
			}
			if (StringUtils.isBlank(answerContents.trim())) {
				if (answer.getAnswerImgs() != null && answer.getAnswerImgs().size() > 0) {
					// 多图
					for (int i = 0; i < answer.getAnswerImgs().size(); i++) {
						answerContents += "<img src='" + FileUtil.getUrl(answer.getAnswerImgs().get(i)) + "' />";
					}
				} else if (answer.getAnswerImg() != null && answer.getAnswerImg() > 0) {
					answerContents = "<img src='" + FileUtil.getUrl(answer.getAnswerImg()) + "' />";
				}
			}

			history.put("content", answerContents);
			history.put("time", answer.getCreateAt() == null ? "" : format.format(answer.getCreateAt()));
			answers.add(history);
		}

		// 获取习题正确率
		List<Map> sfQuestionRates = questionIds.size() == 0 ? new ArrayList<Map>(0)
				: sfqService.sfQuestionRateQuery(studentId, questionIds);
		Map<String, String> sfQuestionRateMap = new HashMap<String, String>(sfQuestionRates.size());
		for (Map map : sfQuestionRates) {
			if (map.get("qid") == null || map.get("rate") == null) {
				continue;
			}
			String rate = String.valueOf((int) (Double.parseDouble(map.get("rate").toString()) * 10) / 10);
			sfQuestionRateMap.put(map.get("qid").toString(), map.get("rate") == null ? null : rate);
		}

		for (Map fquestionMap : fquesions) {
			String scodeL1 = fquestionMap.get("scode").toString().substring(0, 10);
			Map<String, Object> sectionTemp = sectionTempMap.get(scodeL1);
			if (sectionTemp == null) {
				sectionTemp = new HashMap<String, Object>();
				sectionTemp.put("total", 0);
				sectionTemp.put("totalL1", 0);
				sectionTemp.put("totalL2", 0);
				sectionTemp.put("totalL3", 0);
				sectionTempMap.put(scodeL1, sectionTemp);
			}
			List<VQuestion> questions = (List<VQuestion>) sectionTemp.get("questions");
			int total = (Integer) sectionTemp.get("total");
			int totalL1 = (Integer) sectionTemp.get("totalL1");
			int totalL2 = (Integer) sectionTemp.get("totalL2");
			int totalL3 = (Integer) sectionTemp.get("totalL3");
			if (questions == null) {
				questions = new ArrayList<VQuestion>();
				sectionTemp.put("questions", questions);
			}
			VQuestion vquestion = questionMap.get(Long.parseLong(fquestionMap.get("qid").toString()));
			if (vquestion != null) {
				questions.add(vquestion);
				sectionTemp.put("total", total + 1);
				if (vquestion.getDifficulty() >= 0.8) {
					sectionTemp.put("totalL1", totalL1 + 1);
				} else if (vquestion.getDifficulty() >= 0.4) {
					sectionTemp.put("totalL2", totalL2 + 1);
				} else {
					sectionTemp.put("totalL3", totalL3 + 1);
				}
			}
		}

		// 组织章节习题数据集合
		List<Map<String, Object>> sectionList = new ArrayList<Map<String, Object>>(sections.size());
		for (Section section : sections) {
			Map<String, Object> sectionTemp = sectionTempMap.get(section.getCode().toString());
			if (sectionTemp != null) {
				sectionTemp.put("name", section.getName());
				sectionList.add(sectionTemp);
			}
		}

		File file = studentFallibleExportService.exportStuFallible(destDir, name, sectionList, historyMap,
				sfQuestionRateMap, host, hash, questionWordMLDataMap, qrcodeMap, outLocalImgMap);
		return file;
	}
}
