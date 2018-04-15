package com.lanking.uxb.service.web.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleExportRecord;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.domain.yoomath.stat.StudentQuestionAnswer;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.api.ZyStudentFallibleExportService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentQuestionAnswerConvert;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestionForm;
import com.lanking.uxb.service.zuoye.value.VExerciseSection;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 悠作业学生错题本接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/fallible")
public class ZyStuFallibleQuestionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private ZyStudentFallibleQuestionConvert sfqConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private StudentQuestionAnswerService stuQuestionAnswerService;
	@Autowired
	private ZyStudentQuestionAnswerConvert stuQuestionAnswerConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private ZyStudentFallibleExportRecordService studentFallibleExportRecordService;
	@Autowired
	private ZyStudentFallibleExportService studentFallibleExportService;
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 获取首页数据,上面的过滤条件以及章节树形结构<br>
	 * 注:经和设计讨论,目前缺乏版本的确定,so现在固定为苏教版[TextbookService.SU_JIAO_BAN]<br>
	 * 根据和设计的讨论,我的理解是学生错题只看当前阶段所属版本(一般情况下就一个固定的版本)的所有教材的历史错题O(∩_∩)O哈哈~
	 * 
	 * @since 2.1
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "textbookCode", required = false) Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>(4);
		Long sectionCode = null;
		if (textbookCode == null) {
			List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			Integer phaseCode = null;
			Teacher teacher = null;
			if (CollectionUtils.isNotEmpty(clazzs)) {
				HomeworkClazz homeworkClazz = zyHkClassService.get(clazzs.get(0).getClassId());
				if (homeworkClazz.getTeacherId() != null) {
					teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, homeworkClazz.getTeacherId()));
					phaseCode = teacher.getPhaseCode();
				}
			}
			if (phaseCode != null) {
				List<Subject> osubjects = subjectService.findByPhaseCode(phaseCode);
				List<Subject> subjects = new ArrayList<Subject>(1);
				// 只取数学
				for (Subject subject : osubjects) {
					if (SubjectService.PHASE_2_MATH == subject.getCode()
							|| SubjectService.PHASE_3_MATH == subject.getCode()) {
						subjects.add(subject);
						break;
					}
				}
				data.put("subjects", subjects);
				if (teacher != null) {// 加入班级了
					if (teacher.getTextbookCode() == null) {// 老师没有设置版本教材
						List<VTextbook> textbooks = null;
						if (subjects.get(0).getCode() == SubjectService.PHASE_2_MATH) {
							// 初中苏科版
							textbooks = tbConvert.to(tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
									SubjectService.PHASE_2_MATH));
							data.put("cataName", "苏科版");
						} else if (subjects.get(0).getCode() == SubjectService.PHASE_3_MATH) {
							// 高中苏教版
							textbooks = tbConvert.to(tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
									SubjectService.PHASE_3_MATH));
							data.put("cataName", "苏教版");
						}

						data.put("textbooks", textbooks);
						// 获取上次错题
						List<Integer> textbookCodes = new ArrayList<Integer>(textbooks.size());
						for (VTextbook v : textbooks) {
							textbookCodes.add(v.getCode());
						}
						textbookCode = textbooks.get(0).getCode();
						data.put("textbookCode", textbookCode);
					} else {
						TextbookCategory tbCate = tbCateService.get(teacher.getTextbookCategoryCode());
						List<VTextbook> textbooks = tbConvert.to(
								tbService.find(phaseCode, teacher.getTextbookCategoryCode(), teacher.getSubjectCode()));
						data.put("cataName", tbCate.getName());
						data.put("textbooks", textbooks);
						if (CollectionUtils.isNotEmpty(textbooks)) {
							data.put("textbookCode", textbooks.get(0).getCode());
						}
					}
					data.put("questionType",
							questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
				}
			}
		}
		if (textbookCode == null) {
			data.put("sections", new ArrayList<VExerciseSection>(0));
		} else {
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = sfqService.staticFallibleCount(Security.getUserId(), textbookCode);
			long total = 0;
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					total += countMap.get(v.getCode());
					v.setFallibleCount(countMap.get(v.getCode()));
				}
			}
			// 重新组装为树形结构
			data.put("sections", sectionConvert.assemblySectionTree(vsections));
			data.put("total", total);
			if (!data.containsKey("textbookCode")) {
				data.put("textbookCode", textbookCode);
			}
			if (sectionCode == null) {
				sectionCode = vsections.get(0).getCode();
				if (CollectionUtils.isNotEmpty(vsections.get(0).getChildren())) {
					sectionCode = vsections.get(0).getChildren().get(0).getCode();
					if (CollectionUtils.isNotEmpty(vsections.get(0).getChildren().get(0).getChildren())) {
						sectionCode = vsections.get(0).getChildren().get(0).getChildren().get(0).getCode();
						if (CollectionUtils
								.isNotEmpty(vsections.get(0).getChildren().get(0).getChildren().get(0).getChildren())) {
							sectionCode = vsections.get(0).getChildren().get(0).getChildren().get(0).getChildren()
									.get(0).getCode();
						}
					}
				}
			}
		}
		data.put("sectionCode", sectionCode);
		return new Value(data);
	}

	/**
	 * 查询错题列表
	 * 
	 * @since 2.1
	 * @param form
	 *            查询条件
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(StuFallibleQuestionForm form) {
		ZyStudentFallibleQuestionQuery query = new ZyStudentFallibleQuestionQuery();
		query.setStudentId(Security.getUserId());
		if (CollectionUtils.isNotEmpty(form.getSectionCodes())) {
			Set<Long> sectionCodes = Sets.newHashSet(form.getSectionCodes());
			if (sectionCodes.size() == 1) {
				query.setSectionCode(sectionCodes.iterator().next());
			} else {
				query.setSectionCodes(sectionCodes);
			}
		}
		query.setIsCreateAtDesc(form.getIsCreateAtDesc());
		query.setIsUpdateAtDesc(form.getIsUpdateAtDesc());
		query.setIsMistakeNumDesc(form.getIsMistakeNumDesc());
		query.setTextbookCode(form.getTextbookCode());

		Page<StudentFallibleQuestion> page = sfqService.query(query, P.index(form.getPageNo(), form.getSize()));
		VPage<VStudentFallibleQuestion> vpage = new VPage<VStudentFallibleQuestion>();
		vpage.setPageSize(form.getSize());
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(sfqConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(form.getPageNo());
		return new Value(vpage);
	}

	/**
	 * 获取错题进行练习
	 * 
	 * @since 2.1
	 * @since yoomath v1.7 章节错题练习与全部错题练习分开
	 * @param textbookCode
	 *            教材代码
	 * @param size
	 *            获取条数
	 * @param cursor
	 *            下次获取的游标
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "get", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getFallQuestion(@RequestParam(value = "textbookCode", required = false) Integer textbookCode,
			@RequestParam(value = "sectionCodes", required = false) List<Long> sectionCodes,
			@RequestParam(value = "size", required = false) Integer size,
			@RequestParam(value = "cursor", required = false) Long cursor) {
		ZyStudentFallibleQuestionQuery query = new ZyStudentFallibleQuestionQuery();
		query.setStudentId(Security.getUserId());
		query.setTextbookCode(textbookCode);
		if (cursor == null) {
			query.setUpdateAtCursor(new Date(Long.MAX_VALUE));
		} else {
			query.setUpdateAtCursor(new Date(cursor));
		}
		if (null != sectionCodes && sectionCodes.size() > 0) {
			query.setSectionCodes(sectionCodes);
		}
		CursorPage<Long, StudentFallibleQuestion> cursorPage = sfqService.query(query,
				CP.cursor(Long.MAX_VALUE, size == null ? 1 : size));
		VCursorPage<VStudentFallibleQuestion> vpage = new VCursorPage<VStudentFallibleQuestion>();
		if (cursorPage.isEmpty()) {
			vpage.setCursor(cursor == null ? Long.MAX_VALUE : cursor);
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentFallibleQuestion> vs = sfqConvert.to(cursorPage.getItems());
			vpage.setItems(vs);
			vpage.setCursor(vs.get(vs.size() - 1).getUpdateAt().getTime());
		}
		return new Value(vpage);
	}

	/**
	 * 提交错题练习,暂时不提交答案,只记录练习次数
	 * 
	 * @since 2.1
	 * @param id
	 *            错题集记录ID
	 * @param questionId
	 *            题目ID
	 * @param answer
	 *            答案JSON格式 <br>
	 * 
	 *            <pre>
	 *            [
	 *            	{qid:题目ID,answers:[]},
	 *            	{qid:子题ID,answers:[]},
	 *            	{qid:子题ID,answers:[]}
	 *            ]
	 *            </pre>
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "exercise_commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value exerciseCommit(long id, Long questionId, String answer, HomeworkAnswerResult result, Long answerImg,
			Integer rightRate, @RequestParam(required = false) List<HomeworkAnswerResult> itemResults,
			@RequestParam(required = false) List<Long> answerImgs) {
		Map<Long, List<String>> latex_answers = Maps.newHashMap();
		Map<Long, List<String>> asciimath_answers = Maps.newHashMap();
		if (answer != null) {
			JSONArray answerArray = JSONArray.parseArray(answer);
			for (int i = 0; i < answerArray.size(); i++) {
				JSONObject answerObject = (JSONObject) answerArray.get(i);
				long qid = answerObject.getLong("qid");
				JSONArray array = answerObject.getJSONArray("answers");
				List<String> latexs = new ArrayList<String>(array.size());
				List<String> asciis = new ArrayList<String>(array.size());
				for (Object object : array) {
					latexs.add(((JSONObject) object).getString("content"));
					asciis.add(((JSONObject) object).getString("contentAscii"));
				}
				latex_answers.put(qid, latexs);
				asciimath_answers.put(qid, asciis);
			}
		}
		// 多图预留
		if (CollectionUtils.isEmpty(answerImgs)) {
			answerImgs = new ArrayList<Long>();
			if (answerImg != null && answerImg > 0) {
				answerImgs = new ArrayList<Long>(1);
				answerImgs.add(answerImg);
			}
		}

		stuQuestionAnswerService.create(Security.getUserId(), questionId, latex_answers, asciimath_answers, answerImgs,
				itemResults, rightRate, result == null ? HomeworkAnswerResult.UNKNOW : result,
				StudentQuestionAnswerSource.FALLIBLE, new Date());
		/*
		 * GrowthLog growthLog =
		 * growthService.grow(GrowthAction.DOING_DAILY_EXERCISE,
		 * Security.getUserId(), -1, Biz.QUESTION, questionId, true); CoinsLog
		 * coinsLog = coinsService.earn(CoinsAction.DOING_DAILY_EXERCISE,
		 * Security.getUserId(), -1, Biz.QUESTION, questionId);
		 * 
		 * Map<String, Object> rtnMap = new HashMap<String, Object>(2);
		 * rtnMap.put("max", growthLog.getHonor() == null);
		 * 
		 * VUserReward v = new VUserReward();
		 * v.setCoinsValue(coinsLog.getCoinsValue());
		 * v.setGrowthValue(growthLog.getGrowthValue());
		 * v.setOrder(growthLog.getP1() != null ?
		 * Integer.valueOf(growthLog.getP1()) : null); rtnMap.put("reward", v);
		 * 
		 * return new Value(rtnMap);
		 */

		// 完成错题练习任务
		JSONObject messageObj = new JSONObject();
		messageObj.put("taskCode", 101010003);
		messageObj.put("userId", Security.getUserId());
		messageObj.put("isClient", Security.isClient());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(messageObj).build());

		// 错题练习，题量任务
		JSONObject obj = new JSONObject();
		obj.put("taskCode", 101020016);
		obj.put("userId", Security.getUserId());
		obj.put("isClient", Security.isClient()); // web端
		Map<String, Object> pms = new HashMap<String, Object>(1);
		pms.put("questionCount", 1);
		obj.put("params", pms);
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(obj).build());

		return new Value();
	}

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "history_answers", method = { RequestMethod.GET, RequestMethod.POST })
	public Value historyAnswers(long questionId) {
		List<StudentQuestionAnswer> answers = stuQuestionAnswerService.findByQuestionId(Security.getUserId(),
				questionId, 5);
		return new Value(stuQuestionAnswerConvert.to(answers));
	}

	/**
	 * 错题下载首页数据.
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "downloadIndex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value downloadIndex(Integer[] textbookCodes) {
		List<Textbook> textbooks = new ArrayList<Textbook>();

		if (textbookCodes == null || textbookCodes.length == 0) {
			List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			Integer phaseCode = null;
			Teacher teacher = null;
			Student student = null;
			if (CollectionUtils.isNotEmpty(clazzs)) {
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER,
						zyHkClassService.get(clazzs.get(0).getClassId()).getTeacherId()));
				phaseCode = teacher.getPhaseCode();
			} else {
				student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
				phaseCode = student.getPhaseCode();
			}
			if (teacher != null) {// 加入班级了
				if (teacher.getTextbookCode() == null) {// 老师没有设置版本教材
					if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
						// 初中苏科版
						textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
								SubjectService.PHASE_2_MATH);
					} else if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
						// 高中苏教版
						textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
								SubjectService.PHASE_3_MATH);
					}
				} else {
					textbooks = tbService.find(phaseCode, teacher.getTextbookCategoryCode(), teacher.getSubjectCode());
				}
			} else if (student != null) {
				Integer stuSubjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
						: SubjectService.PHASE_2_MATH;
				if (student.getTextbookCode() == null) { // 学生没有设置版本教材
					if (student.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
						// 初中苏科版
						textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
								SubjectService.PHASE_2_MATH);
					} else if (student.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
						// 高中苏教版
						textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
								SubjectService.PHASE_3_MATH);
					}
				} else {
					textbooks = tbService.find(phaseCode, student.getTextbookCategoryCode(), stuSubjectCode);
				}
			}
		} else {
			Map<Integer, Textbook> textbookMap = tbService.mget(Lists.newArrayList(textbookCodes));
			for (Integer code : textbookCodes) {
				textbooks.add(textbookMap.get(code));
			}
		}

		List<Map<String, Object>> nodes = Lists.newArrayList();
		if (textbooks != null && textbooks.size() > 0) {
			List<Integer> textBookCodes = new ArrayList<Integer>(textbooks.size());

			// 教材数据
			Map<Integer, Map<String, Object>> textBookMap = new HashMap<Integer, Map<String, Object>>(textbooks.size());
			for (Textbook t : textbooks) {
				textBookCodes.add(t.getCode());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", t.getCode());
				map.put("name", t.getName());
				map.put("children", new ArrayList<Map<String, Object>>());
				map.put("count", "");
				map.put("type", "TEXTBOOK");
				textBookMap.put(t.getCode(), map);
				nodes.add(map);
			}

			// 章节数据
			Map<Long, Map<String, Object>> section1s = new HashMap<Long, Map<String, Object>>();
			List<Section> sections = sectionService.findByTextbookCode(textBookCodes, 1);
			Map<Long, Long> countMap = sfqService.studentFallibleLevel1SectionCounts(Security.getUserId(),
					textBookCodes);
			for (Section section : sections) {
				if (section.getLevel() == 1) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("id", section.getCode());
					map.put("name", section.getName());
					map.put("count", 0);
					map.put("textbookCode", section.getTextbookCode());
					map.put("type", "SECTION");

					Long count = countMap.get(section.getCode());
					map.put("count", count == null ? 0 : count);

					section1s.put(section.getCode(), map);
				}
			}

			for (Section section : sections) {
				Map<String, Object> s = section1s.get(section.getCode());
				if (s == null) {
					continue;
				}
				Long count = (Long) s.get("count");
				if (section.getLevel() == 1 && null != s && count > 0) {
					((List) textBookMap.get((Integer) s.get("textbookCode")).get("children")).add(s);
				}
			}

			for (int i = nodes.size() - 1; i >= 0; i--) {
				if (((List) nodes.get(i).get("children")).size() == 0) {
					nodes.remove(i);
				}
			}
		}

		return new Value(nodes);
	}

	/**
	 * 查看统计下载习题数据.
	 * 
	 * @param timeScope
	 *            时间范围
	 * @param questionType
	 *            习题类型
	 * @param errorTimes
	 *            错误次数
	 * @param sectionCodes
	 *            节点集合
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "previewDownload")
	public Value previewDownload(Integer timeScope,
			@RequestParam(value = "questionTypes") List<Question.Type> questionTypes, Integer errorTimes,
			@RequestParam(value = "sectionCodes") List<Long> sectionCodes) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		List<Integer> qTypes = new ArrayList<Integer>();
		if (questionTypes != null) {
			for (Question.Type type : questionTypes) {
				qTypes.add(type.getValue());
			}
		}

		Date dateScope = null;
		if (timeScope != null && timeScope != 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(format.parse(format.format(new Date())));
				if (timeScope == 1) {
					cal.add(Calendar.DAY_OF_YEAR, -30);
				} else if (timeScope == 3) {
					cal.add(Calendar.DAY_OF_YEAR, -90);
				} else if (timeScope == 6) {
					cal.add(Calendar.DAY_OF_YEAR, -180);
				}
				dateScope = cal.getTime();
			} catch (ParseException e) {
				logger.error("previewDownload format date error ", e);
			}
		}

		// 错题个数
		long count = sfqService.getStudentExportCount(Security.getUserId(), sectionCodes, dateScope, qTypes,
				errorTimes);

		// 个人金币个数
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());

		map.put("count", count);
		map.put("coins", honor == null ? 0 : honor.getCoins());
		return new Value(map);
	}

	/**
	 * 生成错题文档.
	 * 
	 * @param timeScope
	 *            时间范围
	 * @param questionType
	 *            习题类型
	 * @param errorTimes
	 *            错误次数
	 * @param sectionCodes
	 *            节点集合
	 * @param printOrderId
	 *            待打印订单的ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "createExportDoc")
	@MemberAllowed
	public Value createExportDoc(Integer timeScope,
			@RequestParam(value = "questionTypes") List<Question.Type> questionTypes, Integer errorTimes,
			@RequestParam(value = "sectionCodes") List<Long> sectionCodes, String host, Long printOrderId) {
		Map<String, Object> map = new HashMap<String, Object>();

		long studentId = Security.getUserId();
		if (printOrderId != null) {
			FallibleQuestionPrintOrder order = stuFalliblePrintService.get(printOrderId);
			if (order != null) {
				studentId = order.getUserId();
			}
		}

		List<Integer> qTypes = new ArrayList<Integer>();
		if (questionTypes != null) {
			for (Question.Type type : questionTypes) {
				qTypes.add(type.getValue());
			}
		}
		Date dateScope = null;
		if (timeScope != null && timeScope != 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(format.parse(format.format(new Date())));
				if (timeScope == 1) {
					cal.add(Calendar.DAY_OF_YEAR, -30);
				} else if (timeScope == 3) {
					cal.add(Calendar.DAY_OF_YEAR, -90);
				} else if (timeScope == 6) {
					cal.add(Calendar.DAY_OF_YEAR, -180);
				}
				dateScope = cal.getTime();
			} catch (ParseException e) {
				logger.error("previewDownload format date error ", e);
			}
		}

		// 附加数据
		JSONObject jo = new JSONObject();
		jo.put("timeScope", timeScope == null ? 0 : timeScope.intValue());
		jo.put("questionTypes", qTypes);
		jo.put("errorTimes", errorTimes);
		String attachData = jo.toJSONString();

		// 错题个数
		long count = sfqService.getStudentExportCount(studentId, sectionCodes, dateScope, qTypes, errorTimes);

		// 个人金币个数
		UserHonor honor = userHonorService.getUserHonor(studentId);

		map.put("coins", honor == null ? 0 : honor.getCoins());
		boolean isMember = true;
		if (SecurityContext.getMemberType() == MemberType.NONE) {
			isMember = false;
			// 会员免费
			if (honor == null || honor.getCoins() - count < 0) {
				// 金币个数不足
				map.put("coinsEnough", 0);
				return new Value(map);
			}
		}
		map.put("coinsEnough", 1);

		// 错题集合
		List<Map> fquesions = sfqService.queryStudentExportQuestion(studentId, sectionCodes, dateScope, qTypes,
				errorTimes);

		int hash = new String(studentId + "" + System.currentTimeMillis()).hashCode();
		map.put("hash", hash);
		studentFallibleExportService.writeFileAndRecordTask(host, studentId, hash, fquesions, sectionCodes, map, true,
				attachData, printOrderId, isMember, null);

		return new Value(map);
	}

	/**
	 * 保存错题文档.
	 * 
	 * @param timeScope
	 *            时间范围
	 * @param questionType
	 *            习题类型
	 * @param errorTimes
	 *            错误次数
	 * @param sectionCodes
	 *            节点集合
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "saveExportDoc")
	@MemberAllowed
	public Value saveExportDoc(Integer timeScope,
			@RequestParam(value = "questionTypes") List<Question.Type> questionTypes, Integer errorTimes,
			@RequestParam(value = "sectionCodes") List<Long> sectionCodes, String host) {
		Map<String, Object> map = new HashMap<String, Object>();

		long studentId = Security.getUserId();

		List<Integer> qTypes = new ArrayList<Integer>();
		if (questionTypes != null) {
			for (Question.Type type : questionTypes) {
				qTypes.add(type.getValue());
			}
		}
		Date dateScope = null;
		if (timeScope != null && timeScope != 0) {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			try {
				cal.setTime(format.parse(format.format(new Date())));
				if (timeScope == 1) {
					cal.add(Calendar.DAY_OF_YEAR, -30);
				} else if (timeScope == 3) {
					cal.add(Calendar.DAY_OF_YEAR, -90);
				} else if (timeScope == 6) {
					cal.add(Calendar.DAY_OF_YEAR, -180);
				}
				dateScope = cal.getTime();
			} catch (ParseException e) {
				logger.error("previewDownload format date error ", e);
			}
		}

		// 附加数据
		JSONObject jo = new JSONObject();
		jo.put("timeScope", timeScope == null ? 0 : timeScope.intValue());
		jo.put("questionTypes", qTypes);
		jo.put("errorTimes", errorTimes);
		String attachData = jo.toJSONString();

		// 错题集合
		List<Map> fquesions = sfqService.queryStudentExportQuestion(studentId, sectionCodes, dateScope, qTypes,
				errorTimes);

		int hash = new String(studentId + "" + System.currentTimeMillis()).hashCode();
		map.put("hash", hash);

		boolean isMember = true;
		if (SecurityContext.getMemberType() == MemberType.NONE) {
			isMember = false;
		}
		studentFallibleExportService.writeFileAndRecordTask(host, studentId, hash, fquesions, sectionCodes, map, false,
				attachData, null, isMember, null);

		return new Value(map);
	}

	/**
	 * 判断学生所有教材章节是否有错题.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "getTextbookQuestionCount")
	public Value getTextbookQuestionCount() {

		List<Textbook> textbooks = new ArrayList<Textbook>();
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Integer phaseCode = null;
		Teacher teacher = null;
		Student student = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			HomeworkClazz clazz = zyHkClassService.get(clazzs.get(0).getClassId());
			if (clazz.getTeacherId() != null) {
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, clazz.getTeacherId()));
				phaseCode = teacher.getPhaseCode();
			}
		} else {
			student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
			phaseCode = student.getPhaseCode();
		}
		if (teacher != null) {// 加入班级了
			if (teacher.getTextbookCode() == null) {// 老师没有设置版本教材
				if (teacher.getPhaseCode() == null) {
					return new Value(0);
				}
				if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
					// 初中苏科版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
							SubjectService.PHASE_2_MATH);
				} else if (teacher.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
					// 高中苏教版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
							SubjectService.PHASE_3_MATH);
				}
			} else {
				textbooks = tbService.find(phaseCode, teacher.getTextbookCategoryCode(), teacher.getSubjectCode());
			}
		} else if (student != null) {
			if (student.getPhaseCode() == null) {
				return new Value(0);
			}
			Integer stuSubjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH;
			if (student.getTextbookCode() == null) { // 学生没有设置版本教材
				if (student.getPhaseCode().intValue() == SubjectService.PHASE_2_MATH) {
					// 初中苏科版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_KE_BAN,
							SubjectService.PHASE_2_MATH);
				} else if (student.getPhaseCode().intValue() == SubjectService.PHASE_3_MATH) {
					// 高中苏教版
					textbooks = tbService.find(phaseCode, TextbookCategoryService.SU_JIAO_BAN,
							SubjectService.PHASE_3_MATH);
				}
			} else {
				textbooks = tbService.find(phaseCode, student.getTextbookCategoryCode(), stuSubjectCode);
			}
		}

		if (textbooks.size() == 0) {
			return new Value(0);
		}
		List<Integer> textbookCodes = new ArrayList<Integer>(textbooks.size());
		for (Textbook tbook : textbooks) {
			textbookCodes.add(tbook.getCode());
		}

		return new Value(sfqService.getTextbookQuestionCount(Security.getUserId(), textbookCodes));
	}

	/**
	 * 检测文档生成状态.
	 * 
	 * @param hash
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "checkDocStatus")
	public Value checkDocStatus(Integer hash) {
		if (hash == null) {
			return new Value(new MissingArgumentException());
		}

		Map<String, Object> returnmap = new HashMap<String, Object>(3);
		StudentFallibleExportRecord record = studentFallibleExportRecordService.findByHash(Security.getUserId(), hash);
		if (record != null) {
			returnmap.put("recordId", record.getId());
			returnmap.put("hash", hash);
			returnmap.put("fileSize", record.getSize());
		}
		return new Value(returnmap);
	}
}
