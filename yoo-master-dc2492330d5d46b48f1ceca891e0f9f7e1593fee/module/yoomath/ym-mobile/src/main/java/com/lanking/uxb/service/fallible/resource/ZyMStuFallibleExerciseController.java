package com.lanking.uxb.service.fallible.resource;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.fallible.form.FallibleExerciseForm;
import com.lanking.uxb.service.fallible.form.StuFallibleFilterForm;
import com.lanking.uxb.service.fallible.value.VStuFallibleTextbook;
import com.lanking.uxb.service.latex.api.LatexService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert2;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestion2Form;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 错题练习相关接口
 * 
 * @since 2.0.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年2月29日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/fallible/exe")
public class ZyMStuFallibleExerciseController extends ZyMBaseController {

	@Autowired
	private LatexService latexService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private ZyStudentFallibleQuestionConvert sfqConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private StudentQuestionAnswerService stuQuestionAnswerService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private ZyStudentFallibleQuestionConvert2 sfqConvert2;
	
	// 定义线程池
//	private ExecutorService threadPool = Executors.newFixedThreadPool(24);
	
	private Logger LOGGER = LoggerFactory.getLogger(ZyMStuFallibleExerciseController.class);

	@SuppressWarnings("deprecation")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "processMML", method = { RequestMethod.POST, RequestMethod.GET })
	public Value processMML(String mmls) {
		JSONArray mmlArray = JSONArray.parseArray(mmls);
		List<Map<String, String>> latexs = new ArrayList<Map<String, String>>(mmlArray.size());
		for (int i = 0; i < mmlArray.size(); i++) {
			Map<String, String> one = new HashMap<String, String>(2);
			String mml = mmlArray.getString(i);
			if (StringUtils.isBlank(mml)) {
				one.put("latex", StringUtils.EMPTY);
				one.put("latexImg", StringUtils.EMPTY);
			} else {
				String latex = latexService.multiMml2Latex(mml);
				one.put("latex", latex);
				one.put("latexImg", Env.getString("fs.domain.m") + "/f/latex/img?math=" + URLEncoder.encode(latex));
			}
			latexs.add(one);
		}
		return new Value(latexs);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "do", method = { RequestMethod.POST, RequestMethod.GET })
	public Value $do(FallibleExerciseForm form) {
		if (form == null || form.getType() != 3 || (form.getQuestionId() <= 0 && form.getStuFallbileId() <= 0)
				|| form.getResult() == null || form.getResult() == HomeworkAnswerResult.UNKNOW) {
			return new Value(new IllegalArgException());
		}
		List<Long> images = null;
		// 优先判断是否上传一张图片了(老版本客户端同样支持)
		if (form.getImage() != null) {
			images = new ArrayList<Long>(1);
			images.add(form.getImage());
		} else {
			images = form.getImages();
		}
		if (form.getStuFallbileId() != null && form.getStuFallbileId() > 0) {
			try {
				sfqService.update(form.getStuFallbileId(), Security.getUserId(), images,
						StudentQuestionAnswerSource.OCR, form.getResult());

				// 用户动作行为
				userActionService.action(UserAction.SUBMIT_FALLIBLE_PRACTISE, Security.getUserId(), null);

				// 完成错题练习任务
				JSONObject messageObj = new JSONObject();
				messageObj.put("taskCode", 101010003);
				messageObj.put("userId", Security.getUserId());
				mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
						MQ.builder().data(messageObj).build());
			} catch (AbstractException e) {
				return new Value(new YoomathMobileException(e.getCode()));
			}
		} else {
			Question question = questionService.get(form.getQuestionId());
			if (question == null || question.isSubFlag()) {
				return new Value(new IllegalArgException());
			}
			if (question.getType() == Type.COMPOSITE) {// 不支持复合题
				return new Value(new IllegalArgException());
			}
			if (StringUtils.isNotBlank(form.getItemResults())) {
				List<String> results = JSONArray.parseArray(form.getItemResults(), String.class);
				form.setItemResultList(new ArrayList<HomeworkAnswerResult>(results.size()));
				for (String result : results) {
					form.getItemResultList().add(HomeworkAnswerResult.findByName(result));
				}
			}
			if (question.getType() == Type.FILL_BLANK && question.getAnswerNumber() > 1
					&& form.getItemResultList().size() != question.getAnswerNumber()) {
				return new Value(new IllegalArgException());
			}
			if (question.getType() == Type.FILL_BLANK && question.getAnswerNumber() == 1) {
				form.setItemResultList(new ArrayList<HomeworkAnswerResult>(1));
				form.getItemResultList().add(form.getResult());

			}
			if (StringUtils.isBlank(form.getAsciimathAnswers())) {
				form.setAsciimathAnswerList(Collections.EMPTY_LIST);
			} else {
				List<String> asciimaths = JSONArray.parseArray(form.getAsciimathAnswers(), String.class);
				List<String> asciimathsWithTag = new ArrayList<String>(asciimaths.size());
				for (String asciimath : asciimaths) {
					if (question.getType() == Type.FILL_BLANK) {
						asciimathsWithTag.add("<ux-mth>" + asciimath + "</ux-mth>");
					} else {
						asciimathsWithTag.add(asciimath);
					}
				}
				form.setAsciimathAnswerList(asciimathsWithTag);
			}
			if (StringUtils.isBlank(form.getLatexAnswers())) {
				form.setLatexAnswerList(Collections.EMPTY_LIST);
			} else {
				List<String> latexs = JSONArray.parseArray(form.getLatexAnswers(), String.class);
				List<String> latexsWithTag = new ArrayList<String>(latexs.size());
				for (String latex : latexs) {
					if (question.getType() == Type.FILL_BLANK) {
						latexsWithTag.add(latex);
					} else {
						latexsWithTag.add(latex);
					}
				}
				form.setLatexAnswerList(latexsWithTag);
			}
			if (question.getType() == Type.FILL_BLANK
					&& form.getLatexAnswerList().size() != question.getAnswerNumber()) {// 答案数量不对
				return new Value(new IllegalArgException());
			}
			Integer rightRate = null;
			if (question.getType() == Type.QUESTION_ANSWERING) {
				rightRate = form.getResult() == HomeworkAnswerResult.RIGHT ? 100 : 0;
			}
			Map<Long, List<String>> latexAnswers = new HashMap<Long, List<String>>(1);
			latexAnswers.put(form.getQuestionId(), form.getLatexAnswerList());
			Map<Long, List<String>> asciimathAnswers = new HashMap<Long, List<String>>(1);
			asciimathAnswers.put(form.getQuestionId(), form.getAsciimathAnswerList());
			stuQuestionAnswerService.create(Security.getUserId(), form.getQuestionId(), latexAnswers, asciimathAnswers,
					images, form.getItemResultList(), rightRate, form.getResult(), StudentQuestionAnswerSource.FALLIBLE,
					new Date());

			List<String> answers = new ArrayList<String>(latexAnswers.get(question.getId()).size());
			if (CollectionUtils.isNotEmpty(latexAnswers)) {
				int i = 0;
				int size = 0;
				if (question.getType() == Type.FILL_BLANK) {
					size = latexAnswers.get(question.getId()).size();
				}
				for (String answer : latexAnswers.get(question.getId())) {
					if (question.getType() == Type.FILL_BLANK && i <= size - 1) {
						answers.add(QuestionUtils.process(answer,
								form.getItemResultList().get(i) == HomeworkAnswerResult.RIGHT, true));
					} else {
						answers.add(
								QuestionUtils.process(answer, form.getResult() == HomeworkAnswerResult.RIGHT, true));
					}
					i++;
				}
			}

			// 完成错题练习任务
			JSONObject messageObj = new JSONObject();
			messageObj.put("taskCode", 101010003);
			messageObj.put("userId", Security.getUserId());
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(messageObj).build());

			// 错题练习，题量任务
			JSONObject obj = new JSONObject();
			obj.put("taskCode", 101020016);
			obj.put("userId", Security.getUserId());
			obj.put("isClient", Security.isClient());
			Map<String, Object> pms = new HashMap<String, Object>(1);
			pms.put("questionCount", 1);
			obj.put("params", pms);
			mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
					MQ.builder().data(obj).build());

			// 用户动作行为
			userActionService.action(UserAction.SUBMIT_FALLIBLE_PRACTISE, Security.getUserId(), null);
			return new Value(answers);

		}

		return new Value();
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(int textbookCode, Long cursor, Integer size) {
		ZyStudentFallibleQuestionQuery query = new ZyStudentFallibleQuestionQuery();
		query.setStudentId(Security.getUserId());
		query.setTextbookCode(textbookCode);
		if (cursor == null) {
			query.setUpdateAtCursor(new Date(Long.MAX_VALUE));
		} else {
			query.setUpdateAtCursor(new Date(cursor));
		}
		query.setIsUpdateAtDesc(true);

		VCursorPage<VStudentFallibleQuestion> vpage = new VCursorPage<VStudentFallibleQuestion>();
		if (cursor == null) {
			Page<StudentFallibleQuestion> page = sfqService.query(query,
					P.index(1, size == null ? 20 : Math.min(20, size)));
			if (page.isEmpty()) {
				vpage.setCursor(cursor == null ? Long.MAX_VALUE : cursor);
				vpage.setItems(Collections.EMPTY_LIST);
				vpage.setTotal(0L);
			} else {
				List<VStudentFallibleQuestion> vs = sfqConvert.to(page.getItems());
				vpage.setItems(vs);
				vpage.setCursor(vs.get(vs.size() - 1).getUpdateAt().getTime());
				vpage.setTotal(page.getTotalCount());
			}
		} else {
			CursorPage<Long, StudentFallibleQuestion> cursorPage = sfqService.query(query,
					CP.cursor(Long.MAX_VALUE, size == null ? 20 : Math.min(20, size)));
			if (cursorPage.isEmpty()) {
				vpage.setCursor(cursor == null ? Long.MAX_VALUE : cursor);
				vpage.setItems(Collections.EMPTY_LIST);
			} else {
				List<VStudentFallibleQuestion> vs = sfqConvert.to(cursorPage.getItems());
				vpage.setItems(vs);
				vpage.setCursor(vs.get(vs.size() - 1).getUpdateAt().getTime());
			}
		}
		return new Value(vpage);
	}

	/**
	 * 获取练习错题的过滤条件
	 * 
	 * @since 2.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "filterConditions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value filterConditions() {
		Student student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
		Integer categoryCode = student.getTextbookCategoryCode();

		// 学生未加入班级并且此学生也并没有设置教材版本！返回错误信息1888(未能获得版本教材)
		if (categoryCode == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_NOCATEGORYCODE));
		}

		Map<String, Object> retMap = new HashMap<String, Object>(5);
		// List<Map> res =
		// sfqService.getStudentFallTSCount(Security.getUserId(), categoryCode);
		// since 2017-10-25 章节数量去重
		List<Map> res = sfqService.getStudentFallibleFirstSectionCount(Security.getUserId(), categoryCode);

		// 处理不同教材下的错题等
		if (res == null || res.size() == 0) {
			retMap.put("TEXTBOOK", Collections.EMPTY_LIST);
		} else {
			Map<Long, Long> sections = new HashMap<Long, Long>(res.size());
			List<Integer> textbookCodes = Lists.newArrayList();
			Set<Long> sectionCodes = new HashSet<Long>(res.size());
			Map<Integer, List<Long>> textbookSections = new HashMap<Integer, List<Long>>(res.size());
			for (Map map : res) {
				Integer textbookCode = ((BigInteger) map.get("textbook_code")).intValue();
				Object sectionCodeObj = map.get("scode");
				Long sectionCode = Long.valueOf(sectionCodeObj.toString());
				Long count = ((BigInteger) map.get("amount")).longValue();

				if (!textbookCodes.contains(textbookCode)) {
					textbookCodes.add(textbookCode);
				}
				sectionCodes.add(sectionCode);
				sections.put(sectionCode, count);

				List<Long> textbookSectionCodes = textbookSections.get(textbookCode);
				if (CollectionUtils.isEmpty(textbookSectionCodes)) {
					textbookSectionCodes = Lists.newArrayList();
				}
				if (!textbookSectionCodes.contains(sectionCode)) {
					textbookSectionCodes.add(sectionCode);
				}
				textbookSections.put(textbookCode, textbookSectionCodes);
			}

			List<Textbook> textbookList = textbookService.mgetList(textbookCodes);
			Map<Long, Section> sectionMap = sectionService.mget(sectionCodes);
			// 根据textbookcode取去重的题目数量
			Map<Integer, Long> textbookCount = sfqService.getStudentFallibleTextbookCodeCount(Security.getUserId(),
					textbookCodes);
			List<VStuFallibleTextbook> vts = new ArrayList<VStuFallibleTextbook>(textbookList.size());

			for (Textbook t : textbookList) {
				VStuFallibleTextbook v = new VStuFallibleTextbook();
				v.setCode(t.getCode());
				v.setName(t.getName());

				List<VSection> children = new ArrayList<VSection>(textbookSections.get(t.getCode()).size());

				// long count = 0;
				for (Long sCode : textbookSections.get(t.getCode())) {
					Section section = sectionMap.get(sCode);
					VSection vSection = new VSection();
					vSection.setName(section.getName());
					vSection.setCode(sCode);
					vSection.setFallibleCount(sections.get(sCode));
					// count += vSection.getFallibleCount();

					children.add(vSection);
				}
				Long count = 0L;
				if (!textbookCount.isEmpty()) {
					if (textbookCount.get(t.getCode()) != null) {
						count = textbookCount.get(t.getCode());
					}
				}
				v.setCount(count);
				v.setSections(children);

				vts.add(v);
			}

			retMap.put("TEXTBOOK", vts);
		}

		List<Long> allList = new ArrayList<Long>(4);
		Date endDate = new Date();
		Date month1Date = DateUtils.addDays(endDate, -30);
		Date month2Date = DateUtils.addDays(endDate, -30 * 3);
		Date month3Date = DateUtils.addDays(endDate, -30 * 6);

//        Future<Long> allCountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countByDate(Security.getUserId(), categoryCode, null, null);
//            }
//        });
//        Future<Long> month1CountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countByDate(Security.getUserId(), categoryCode, month1Date, endDate);
//            }
//        });
//        Future<Long> month2CountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countByDate(Security.getUserId(), categoryCode, month2Date, endDate);
//            }
//        });
//        Future<Long> month3CountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countByDate(Security.getUserId(), categoryCode, month3Date, endDate);
//            }
//        });
//        Future<Long> ocrCountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countOcr(Security.getUserId(), categoryCode);
//            }
//        });
//        Future<Long> otherCountFuture = threadPool.submit(new Callable<Long>() {
//            @Override
//            public Long call() throws Exception {
//                return sfqService.countOther(Security.getUserId(), categoryCode);
//            }
//        });
//
//        try {
//        	Long allCount = allCountFuture.get();
//        	Long month1Count = month1CountFuture.get();
//        	Long month2Count = month2CountFuture.get();
//        	Long month3Count = month3CountFuture.get();
//        	
//        	allList.add(allCount == null ? 0 : allCount);
//    		allList.add(month1Count == null ? 0 : month1Count);
//    		allList.add(month2Count == null ? 0 : month2Count);
//    		allList.add(month3Count == null ? 0 : month3Count);
//    		
//    		retMap.put("OCR", ocrCountFuture.get());
//    		retMap.put("OTHER", otherCountFuture.get());
//		} catch (InterruptedException | ExecutionException e) {
//			LOGGER.error("获取学生错题统计数据失败", e);
//		}
//
//		retMap.put("ALL", allList);
//		retMap.put("categoryCode", categoryCode);
		
		Long allCount = sfqService.countByDate(Security.getUserId(), categoryCode, null, null);
		Long month1Count = sfqService.countByDate(Security.getUserId(), categoryCode, month1Date, endDate);
		Long month2Count = sfqService.countByDate(Security.getUserId(), categoryCode, month2Date, endDate);
		Long month3Count = sfqService.countByDate(Security.getUserId(), categoryCode, month3Date, endDate);

		allList.add(allCount == null ? 0 : allCount);
		allList.add(month1Count == null ? 0 : month1Count);
		allList.add(month2Count == null ? 0 : month2Count);
		allList.add(month3Count == null ? 0 : month3Count);

		retMap.put("ALL", allList);

		retMap.put("OCR", sfqService.countOcr(Security.getUserId(), categoryCode));
		retMap.put("OTHER", sfqService.countOther(Security.getUserId(), categoryCode));
		retMap.put("categoryCode", categoryCode);

		return new Value(retMap);
	}

	/**
	 * 根据过滤条件获取错题列表
	 * 
	 * @since 2.1.0
	 * @param form
	 *            过滤条件
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "pullQuestionByFilter", method = { RequestMethod.POST, RequestMethod.GET })
	public Value pullQuestionByFilter(StuFallibleFilterForm form) {
		if (form.getType() == null) {
			form.setType("ALL");

			Student student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
			Integer categoryCode = student.getTextbookCategoryCode();

			form.setCategoryCode(categoryCode);
		}
		if (form.getPullDoQuestions()) {
			// 过滤查询做错题的题目
			ValueMap vm = ValueMap.value();
			if (form.getSize() == null) {
				form.setSize(10);
			}
			StuFallibleQuestion2Form query = new StuFallibleQuestion2Form();
			query.setPageSize(form.getSize());
			if (form.getMonth() != null) {
				query.setDay(form.getMonth() * 30);
			}
			if (CollectionUtils.isNotEmpty(form.getQuestionTypes())) {
				List<Integer> questionTypes = new ArrayList<Integer>(form.getQuestionTypes().size());
				for (Question.Type t : form.getQuestionTypes()) {
					questionTypes.add(t.getValue());
				}

				query.setQuestionTypes(questionTypes);
			}
			query.setCategoryCode(form.getCategoryCode());
			if (form.getType().equals("OCR")) {
				query.setSource(StudentQuestionAnswerSource.OCR.getValue());
			} else if (form.getType().equals("OTHER")) {
				query.setOther(true);
			}
			query.setTextbookCode(form.getTextbookCode());
			query.setSectionCode(form.getSectionCode());
			query.setUserId(Security.getUserId());
			if (form.getWrongTime() != null) {
				query.setMistakeNum(form.getWrongTime().longValue());
			}

			List<StudentFallibleQuestion> questions = sfqService.queryDoStuFallibleQuestions(query);
			vm.put("items", sfqConvert2.to(questions));
			vm.put("total", questions.size());

			return new Value(vm);
		} else {
			ZyStudentFallibleQuestionQuery query = new ZyStudentFallibleQuestionQuery();
			query.setStudentId(Security.getUserId());
			switch (form.getType()) {
			case "ALL":
				if (form.getCategoryCode() == null) {
					return new Value(new IllegalArgException());
				}
				query.setAll(true);
				query.setCategoryCode(form.getCategoryCode());
				if (form.getMonth() != null) {
					Date nowDate = new Date();
					query.setCreateAtCursor(DateUtils.addDays(nowDate, -30 * form.getMonth()));
				}
				break;
			case "OTHER":
				query.setCategoryCode(form.getCategoryCode());
				query.setOther(true);
				break;
			case "OCR":
				query.setCategoryCode(form.getCategoryCode());
				query.setOcr(true);
				break;
			case "TEXTBOOK":
				if (form.getSectionCode() == null && form.getTextbookCode() == null) {
					return new Value(new IllegalArgException());
				}
				if (form.getSectionCode() != null) {
					query.setSectionCodes(sectionService.findSectionChildren(form.getSectionCode()));
				} else {
					query.setSectionCodes(sectionService.findLeafSectionByTextbook(form.getTextbookCode()));
				}
				break;
			default:
				return new Value(new IllegalArgException());
			}

			CursorPageable<Long> cursorPageable = null;
			if (form.getCursor() == null) {
				cursorPageable = CP.cursor(Long.MAX_VALUE, form.getSize() == null ? 20 : Math.min(form.getSize(), 20));
			} else {
				cursorPageable = CP.cursor(form.getCursor(),
						form.getSize() == null ? 20 : Math.min(form.getSize(), 20));
			}

			// 查询OTHER时重新处理,只查询不在当前版本的题目,对在多个版本的题目做排除处理
			CursorPage<Long, StudentFallibleQuestion> ret = null;
			if (query.getOther() != null && query.getOther()) {
				ret = sfqService.queryOtherCategoryCode(query, cursorPageable);
			} else {
				ret = sfqService.query2(query, cursorPageable);
			}

			VCursorPage<VStudentFallibleQuestion> vc = new VCursorPage<VStudentFallibleQuestion>();
			if (ret.isEmpty()) {
				vc.setItems(Collections.EMPTY_LIST);
				vc.setCursor(form.getCursor() == null ? Long.MAX_VALUE : form.getCursor());
			} else {
				List<VStudentFallibleQuestion> vs = sfqConvert2.to(ret.getItems());
				vc.setItems(vs);
				vc.setCursor(vs.get(vs.size() - 1).getUpdateAt().getTime());
			}

			return new Value(vc);
		}
	}

}
