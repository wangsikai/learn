package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.baseData.Knowpoint;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.examPaper.convert.KnowledgePointTreeNodeConvert;
import com.lanking.uxb.service.examPaper.convert.KnowledgeSystemTreeNodeConvert;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;
import com.lanking.uxb.service.resources.api.QuestionTypeService;
import com.lanking.uxb.service.resources.convert.QuestionTypeConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyLevelKnowpointConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.form.StuFallibleQuestion2Form;
import com.lanking.uxb.service.zuoye.value.VExerciseSection;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 悠数学学生错题本
 * 
 * @since yoomath V2.0
 * @author wangsenhao
 *
 */
@ApiAllowed
@RestController
@RequestMapping("zy/s/fallible/2")
public class ZyStuFallibleQuestion2Controller {

	@Autowired
	private ZyStudentFallibleQuestionService zyStudentFallibleQuestionService;
	@Autowired
	private ZyStudentFallibleQuestionConvert zyStudentFallibleQuestionConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private KnowpointService knowPointService;
	@Autowired
	private KnowpointConvert knowPointConvert;
	@Autowired
	private ZyLevelKnowpointConvert zlkConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private QuestionTypeConvert questionTypeConvert;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private QuestionTypeService questionTypeService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private KnowledgeSystemTreeNodeConvert knowledgeSystemTreeNodeConvert;
	@Autowired
	private KnowledgePointTreeNodeConvert knowledgePointTreeNodeConvert;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private MqSender mqSender;

	/**
	 * 查询学生的错题
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(StuFallibleQuestion2Form form) {
		form.setUserId(Security.getUserId());
		Page<StudentFallibleQuestion> cp = zyStudentFallibleQuestionService.queryStuFallibleQuestionByIndex(form,
				P.index(form.getPage(), form.getPageSize()));
		VPage<VStudentFallibleQuestion> vp = new VPage<VStudentFallibleQuestion>();
		int tPage = (int) (cp.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(zyStudentFallibleQuestionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 删除学生错题
	 * 
	 * @param questionId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "deleteFailQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value deleteFailQuestion(Long id) {
		zyStudentFallibleQuestionService.deleteFailQuestion(id, Security.getUserId());
		return new Value();
	}

	/**
	 * 我的错题知识点树
	 * 
	 * @since yoomath V1.9.1
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "knowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value knowPointTree() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Teacher teacher = null;
		Student student = null;
		Integer subjectCode = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			Long teacherId = zyHkClassService.get(clazzs.get(0).getClassId()).getTeacherId();
			if (teacherId != null) {
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
				subjectCode = teacher.getSubjectCode();
			}
		} else {
			student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
			if (student.getPhaseCode() != null) {
				subjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
						: SubjectService.PHASE_2_MATH;
			}
		}
		if (subjectCode == null) {
			data.put("knowpoints", Collections.EMPTY_LIST);
			return new Value(data);
		}
		List<Knowpoint> kpList = knowPointService.listAllBySubject(subjectCode);
		List<VLevelKnowpoint> vlkList = zlkConvert.to(knowPointConvert.to(kpList));
		Map<Integer, Integer> countMaps = zyStudentFallibleQuestionService.statisKnowPoint(Security.getUserId());
		for (VLevelKnowpoint v : vlkList) {
			if (countMaps.containsKey(v.getCode().intValue())) {
				v.setFallibleCount(countMaps.get(v.getCode().intValue()).longValue());
			}
		}
		data.put("knowpoints", zlkConvert.assemblySectionTree(vlkList));
		return new Value(data);
	}

	/**
	 * 新知识点树
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "newKnowPointTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value newKnowPointTree() {
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Teacher teacher = null;
		Student student = null;
		Integer subjectCode = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			Long teacherId = zyHkClassService.get(clazzs.get(0).getClassId()).getTeacherId();
			if (teacherId != null) {
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
				subjectCode = teacher.getSubjectCode();
			}
		} else {
			student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
			if (student.getPhaseCode() != null) {
				subjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
						: SubjectService.PHASE_2_MATH;
			}
		}
		if (subjectCode == null) {
			retMap.put("tree", Collections.EMPTY_LIST);
			return new Value(retMap);
		}
		List<KnowledgePoint> points = knowledgePointService.findBySubject(subjectCode);
		List<Long> codes = new ArrayList<Long>();
		for (KnowledgePoint k : points) {
			codes.add(k.getCode());
		}
		List<Long> parentCodes = questionKnowledgeService.queryParentKnowledgeCodes(codes);
		List<KnowledgeSystem> systems = knowledgeSystemService.mgetList(parentCodes);
		List<VKnowledgeTreeNode> nodes = knowledgePointTreeNodeConvert.to(points);
		Map<Long, Long> countMaps = zyStudentFallibleQuestionService.statisNewKnowPoint(Security.getUserId());
		for (VKnowledgeTreeNode n : nodes) {
			if (countMaps.keySet().contains(n.getCode())) {
				n.setFallibleCount(countMaps.get(n.getCode()));
			}
		}
		List<VKnowledgeTreeNode> newList = knowledgeSystemTreeNodeConvert.to(systems);
		newList.addAll(nodes);
		retMap.put("tree", knowledgeSystemTreeNodeConvert.assemblyPointTreeFilterNoFall(newList));
		return new Value(retMap);
	}

	/**
	 * 查询练习题目
	 * 
	 * @param form
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "queryExerciseList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryExerciseList(StuFallibleQuestion2Form form) {
		form.setUserId(Security.getUserId());
		List<StudentFallibleQuestion> list = zyStudentFallibleQuestionService.queryDoStuFallibleQuestions(form);
		return new Value(zyStudentFallibleQuestionConvert.to(list));
	}

	/**
	 * 错题统计.
	 * 
	 * @since v2.0.8 会员控制
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "statIndex", method = { RequestMethod.POST, RequestMethod.GET })
	@MemberAllowed
	public Value statIndex() {
		Map<String, Object> data = new HashMap<String, Object>();
		StudentFallibleQuestion sfq = zyStudentFallibleQuestionService.getFirst(Security.getUserId());
		// 是否含有错题
		if (sfq == null) {
			data.put("hasError", false);
			return new Value(data);
		} else {
			data.put("hasError", true);
		}
		data.put("errorStartAt", sfq.getCreateAt());
		data.put("days", daysBetween(sfq.getCreateAt(), new Date()));
		data.put("totalFallible", zyStudentFallibleQuestionService.getFallibleCount(Security.getUserId()));
		// 昨天
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		data.put("yesterday", cal.getTime());
		data.put("today", new Date());
		cal.add(Calendar.DATE, -29);
		data.put("last30Time", cal.getTime());
		Long count = zyStudentFallibleQuestionService.getLast30Stat(Security.getUserId());
		data.put("last30Fallible", count);
		// 返回当前会员类型
		data.put("memberType", SecurityContext.getMemberType());
		// 会员数据
		if (SecurityContext.getMemberType() != MemberType.NONE) {
			// 近6个月错题统计
			List<Map> list1 = zyStudentFallibleQuestionService.queryLast6MonthStat(Security.getUserId());
			// 易错知识点
			List<Map> list2 = zyStudentFallibleQuestionService.queryWeakKpList(Security.getUserId());
			data.put("last6Month", list1);
			for (Map map : list2) {
				Parameter parameter = parameterService.get(Product.YOOMATH, "h5.knowledge-card.url",
						String.valueOf(map.get("code").toString()));
				map.put("h5PageUrl", parameter.getValue());
			}
			data.put("weakList", list2);

			// 最近6个月
			List<Integer> monthList = new ArrayList<Integer>();
			Calendar cal1 = Calendar.getInstance();
			Integer month = cal1.get(Calendar.MONTH) + 1;
			monthList.add(month - 5 > 0 ? month - 5 : 12 + (month - 5));
			monthList.add(month - 4 > 0 ? month - 4 : 12 + (month - 4));
			monthList.add(month - 3 > 0 ? month - 3 : 12 + (month - 3));
			monthList.add(month - 2 > 0 ? month - 2 : 12 + (month - 2));
			monthList.add(month - 1 > 0 ? month - 1 : 12 + (month - 1));
			monthList.add(month);
			data.put("monthList", monthList);
		} else {
			data.put("last6Month", Lists.newArrayList());
			data.put("weakList", Lists.newArrayList());
			data.put("monthList", Lists.newArrayList());
		}

		return new Value(data);
	}

	/**
	 * 提交OCR图片的题目
	 * 
	 * @since yoomath web v2.3.1支持多图
	 * 
	 * @param result
	 *            作业结果
	 * @param answerImg
	 *            答题图片Id
	 * @param stuFailQuestionId
	 *            错题练习的Id
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "submitOcrQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value submitOcrQuestion(HomeworkAnswerResult result, @RequestParam(required = false) List<Long> answerImgs,
			Long stuFailQuestionId) {

		try {
			zyStudentFallibleQuestionService.update(stuFailQuestionId, Security.getUserId(), answerImgs,
					StudentQuestionAnswerSource.FALLIBLE, result);

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

		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	public static int daysBetween(Date date1, Date date2) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * @since 2.0.3
	 * @param textbookCode
	 * @return
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
			Student student = null;
			if (CollectionUtils.isNotEmpty(clazzs)) {
				HomeworkClazz homeworkClazz = zyHkClassService.get(clazzs.get(0).getClassId());
				if (homeworkClazz.getTeacherId() != null) {
					teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, homeworkClazz.getTeacherId()));
					phaseCode = teacher.getPhaseCode();
				}
			} else {
				/**
				 * 表示学生没有加入班级，现手机端没有加入班级可以进行每日练等，会产生错题进入错题本，这里做处理 <br>
				 * 没有班级的学生取学生自己的版本、版本对应的学科<br>
				 * 2016.7.11
				 */
				student = ((Student) studentService.getUser(UserType.STUDENT, Security.getUserId()));
				phaseCode = student.getPhaseCode();
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
						// 教材有收藏时才返回,需求调整后需要优化
						List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
						for (VTextbook v : textbooks) {
							tbCodes.add(v.getCode());
						}
						Map<Integer, Boolean> cacheMap = zyStudentFallibleQuestionService
								.statisTextbookExistStuFallWithCache2(tbCodes, Security.getUserId());
						List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
						for (VTextbook v : textbooks) {
							if (cacheMap.get(v.getCode())) {
								tbs.add(v);
							}
						}
						data.put("cataName", tbCate.getName());
						data.put("textbooks", tbs);
						// 2016-6-24 修正，发现textbooks有空的情况
						// textbookCode = textbooks.get(0).getCode();
						if (CollectionUtils.isNotEmpty(tbs)) {
							data.put("textbookCode", tbs.get(0).getCode());
						}
						StuFallibleQuestion2Form form = new StuFallibleQuestion2Form();
						form.setUserId(Security.getUserId());
						form.setOther(true);
						form.setCategoryCode(tbCate.getCode());
						Page<StudentFallibleQuestion> cp = zyStudentFallibleQuestionService
								.queryStuFallibleQuestionByIndex(form, P.index(form.getPage(), form.getPageSize()));
						if (cp.getTotalCount() > 0) {
							data.put("otherShow", true);
						} else {
							data.put("otherShow", false);
						}
					}
					data.put("questionType",
							questionTypeConvert.to(questionTypeService.findBySubject(teacher.getSubjectCode())));
				} else if (student != null) {
					Integer stuSubjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH
							? SubjectService.PHASE_3_MATH : SubjectService.PHASE_2_MATH;
					if (student.getTextbookCode() == null) {// 学生没有设置版本教材
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
						TextbookCategory tbCate = tbCateService.get(student.getTextbookCategoryCode());
						List<VTextbook> textbooks = tbConvert
								.to(tbService.find(phaseCode, student.getTextbookCategoryCode(), stuSubjectCode));
						// 教材有收藏时才返回,需求调整后需要优化
						List<Integer> tbCodes = new ArrayList<Integer>(textbooks.size());
						for (VTextbook v : textbooks) {
							tbCodes.add(v.getCode());
						}
						Map<Integer, Boolean> cacheMap = zyStudentFallibleQuestionService
								.statisTextbookExistStuFallWithCache2(tbCodes, Security.getUserId());
						List<VTextbook> tbs = new ArrayList<VTextbook>(textbooks.size());
						for (VTextbook v : textbooks) {
							if (cacheMap.get(v.getCode())) {
								tbs.add(v);
							}
						}
						data.put("cataName", tbCate.getName());
						data.put("textbooks", tbs);
						if (CollectionUtils.isNotEmpty(tbs)) {
							data.put("textbookCode", tbs.get(0).getCode());
						}
						StuFallibleQuestion2Form form = new StuFallibleQuestion2Form();
						form.setUserId(Security.getUserId());
						form.setOther(true);
						form.setCategoryCode(tbCate.getCode());
						Page<StudentFallibleQuestion> cp = zyStudentFallibleQuestionService
								.queryStuFallibleQuestionByIndex(form, P.index(form.getPage(), form.getPageSize()));
						if (cp.getTotalCount() > 0) {
							data.put("otherShow", true);
						} else {
							data.put("otherShow", false);
						}
					}
					data.put("questionType", questionTypeConvert.to(questionTypeService.findBySubject(stuSubjectCode)));
				}
			} else {
				data.put("otherShow", false);
			}
		}
		if (textbookCode == null) {
			// 说明用户刚注册，没有加入班级，也没有在手机端选择过教材和版本
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
			data.put("sections", sectionConvert.assemblySectionTreeFilterNoFall(vsections));
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
}
