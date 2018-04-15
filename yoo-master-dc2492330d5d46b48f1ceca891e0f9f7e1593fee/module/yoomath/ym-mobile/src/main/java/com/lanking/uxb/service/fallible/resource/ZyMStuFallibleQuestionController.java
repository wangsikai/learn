package com.lanking.uxb.service.fallible.resource;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathFallibleRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.fallible.StudentFallibleQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.RSACoder;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.MetaKnowpointService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.MetaKnowpointConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.fallible.form.AddOcrForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.web.resource.ZyStuFallibleQuestion2Controller;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionOCRService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyStudentFallibleQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VStudentFallibleQuestion;

/**
 * 悠数学移动端(学生错题练习相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月11日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/fallible")
public class ZyMStuFallibleQuestionController extends ZyMBaseController {

	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private ZyStudentFallibleQuestionConvert sfqConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private MetaKnowpointService kpService;
	@Autowired
	private MetaKnowpointConvert kpConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private ZyStudentFallibleQuestionOCRService sfqOrcService;
	@Autowired
	private ZyStuFallibleQuestion2Controller stuFallibleQuestion2Controller;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgeSectionService knowSectionService;
	@Autowired
	private ParameterService parameterService;

	/**
	 * 获取错题首页数据<br>
	 * 1.学生可选教材列表: 先判断学生自己有没有设置阶段版本教材信息,如果没有设置则取此学生加入班级的老师的阶段信息(此时则高中苏教版初中苏科版)<br>
	 * 2.获取默认教材下面的错题章节列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> dataMap = textbookList(Security.getUserType(), Security.getUserId(), 6);
		List<VTextbook> tbs = (List<VTextbook>) dataMap.get("textbooks");
		if (CollectionUtils.isNotEmpty(tbs)) {
			Integer textbookCode = (Integer) dataMap.get("textbookCode");
			List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
			Map<Long, Long> countMap = sfqService.staticFallibleCount(Security.getUserId(), textbookCode);
			for (VSection v : vsections) {
				if (countMap.containsKey(v.getCode())) {
					v.setFallibleCount(countMap.get(v.getCode()));
				}
			}
			List<VSection> vs = sectionConvert.assemblySectionTree(vsections);
			dataMap.put("sections", vs);
			dataMap.put("totalCount", fallibleQuestionCount(vs));
		}
		return new Value(dataMap);
	}

	/**
	 * 通过教材代码获取错题章节结构数据
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param textbookCode
	 *            教材代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(int textbookCode) {
		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		List<VSection> vsections = sectionConvert.to(sectionService.findByTextbookCode(textbookCode));
		Map<Long, Long> countMap = sfqService.staticFallibleCount(Security.getUserId(), textbookCode);
		for (VSection v : vsections) {
			if (countMap.containsKey(v.getCode())) {
				v.setFallibleCount(countMap.get(v.getCode()));
			}
		}
		List<VSection> vs = sectionConvert.assemblySectionTree(vsections);
		dataMap.put("sections", vs);
		dataMap.put("totalCount", fallibleQuestionCount(vs));
		return new Value(dataMap);
	}

	long fallibleQuestionCount(List<VSection> vs) {
		long count = 0;
		for (VSection v : vs) {
			count += v.getFallibleCount();
		}
		return count;
	}

	/**
	 * 获取章节下面的错题列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param sectionCode
	 *            章节代码
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(long sectionCode) {
		final int size = 100;
		final int pageNo = 1;
		ZyStudentFallibleQuestionQuery query = new ZyStudentFallibleQuestionQuery();
		query.setStudentId(Security.getUserId());
		query.setSectionCode(sectionCode);
		query.setIsUpdateAtDesc(true);

		Page<StudentFallibleQuestion> page = sfqService.query(query, P.index(pageNo, size));
		VPage<VStudentFallibleQuestion> vpage = new VPage<VStudentFallibleQuestion>();
		vpage.setPageSize(size);
		if (page.isEmpty()) {
			vpage.setItems(Collections.EMPTY_LIST);
		} else {
			vpage.setItems(sfqConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(pageNo);
		return new Value(vpage);
	}

	/**
	 * ocr识别
	 * 
	 * @since 2.1.0
	 * @param fileId
	 *            图片ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "ocr", method = { RequestMethod.POST, RequestMethod.GET })
	public Value ocr(long fileId) {
		ValueMap vm = ValueMap.value();
		List<Long> questionIds = sfqOrcService.ocr(fileId);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			Map<Long, Question> questions = questionService.mget(questionIds);
			Map<Long, VQuestion> vQuestions = questionConvert.to(questions, new QuestionConvertOption(false, false,
					false, null));
			List<ValueMap> questionVos = new ArrayList<ValueMap>();
			Map<Long, Boolean> existMap = sfqService.exist(questionIds, Security.getUserId());
			for (Long questionId : questionIds) {
				VQuestion v = vQuestions.get(questionId);
				// 处理OCR使用的题目库与当前环境的库不一致时的特殊情况
				if (v == null) {
					continue;
				}
				ValueMap one = ValueMap.value("question", vQuestions.get(questionId));
				one.put("inFallible", existMap.get(questionId) == null ? false : existMap.get(questionId));
				questionVos.add(one);
			}

			if (CollectionUtils.isNotEmpty(questionVos)) {
				vm.put("questions", questionVos);
			}

		}
		JSONObject mq = new JSONObject();
		mq.put("action", "create");
		mq.put("fileId", fileId);
		if (CollectionUtils.isNotEmpty(questionIds)) {
			JSONArray array = new JSONArray();
			for (Long qId : questionIds) {
				array.add(qId);
			}
			mq.put("questions", array);
		}
		mq.put("createAt", new Date());
		mqSender.send(MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK,
				MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_OCRSEARCH, MQ.builder().data(mq).build());

		return new Value(vm);
	}

	/**
	 * 添加到错题中
	 * 
	 * @since 2.1.0
	 * @param form
	 *            错题本参数
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "addByOcr", method = { RequestMethod.POST, RequestMethod.GET })
	public Value addByOcr(AddOcrForm form) {
		if ((form.getFileId() == null || form.getFileId() <= 0) && form.getQuestionId() == null) {
			return new Value(new IllegalArgException());
		}
		ValueMap vm = ValueMap.value();
		// 从库中找到题目了
		if (form.getQuestionId() != null && form.getQuestionId() > 0) {
			List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
			Integer categoryCode = null;
			Teacher teacher = null;
			Student student = null;
			if (CollectionUtils.isNotEmpty(clazzs)) {
				for (HomeworkStudentClazz c : clazzs) {
					Long teacherId = zyHkClassService.get(c.getClassId()).getTeacherId();
					if (teacherId == null) {
						continue;
					}
					teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
					if (teacher != null) {
						categoryCode = teacher.getTextbookCategoryCode();
						break;
					}
				}

				if (teacher == null || categoryCode == null) {
					return new Value(new YoomathMobileException(
							YoomathMobileException.YOOMATH_MOBILE_STU_NOCATEGORYCODE));
				}

			} else {
				student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
				categoryCode = student.getTextbookCategoryCode();
			}

			List<Integer> textbookCodes = sfqService.add(form.getQuestionId(), Security.getUserId(), form.getFileId(),
					StudentQuestionAnswerSource.OCR);
			Integer code = null;
			for (Integer c : textbookCodes) {
				if (c.toString().startsWith(categoryCode.toString())) {
					code = c;
					break;
				}
			}
			if (code == null) {
				VTextbook textbook = new VTextbook();
				textbook.setName("其他");
				vm.put("textbook", textbook);
			} else {
				VTextbook textbook = tbConvert.to(tbService.get(code));
				vm.put("textbook", textbook);
			}
			mqSender.send(
					MqYoomathFallibleRegistryConstants.EX_YM_FALLIBLE_TASK,
					MqYoomathFallibleRegistryConstants.RK_YM_FALLIBLE_TASK_OCRSEARCH,
					MQ.builder()
							.data(new JSONObject(ValueMap.value("action", "choose").put("fileId", form.getFileId())
									.put("chooseQuestion", form.getQuestionId()).put("chooseAt", new Date()))).build());
		} else {
			List<Long> codes = new ArrayList<Long>(form.getKnowpoints().size());
			for (Integer c : form.getKnowpoints()) {
				codes.add(Long.parseLong(c.toString()));
			}
			sfqService.addOcr(Security.getUserId(), form.getFileId(), codes);
			VTextbook textbook = new VTextbook();
			textbook.setName("图片错题");
			vm.put("textbook", textbook);
		}

		JSONObject taskMessageObj = new JSONObject();
		taskMessageObj.put("taskCode", 101010007);
		taskMessageObj.put("userId", Security.getUserId());
		mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
				MQ.builder().data(taskMessageObj).build());

		return new Value(vm);
	}

	/**
	 * 删除ID
	 * 
	 * @since 2.1.0
	 * @param id
	 *            错题ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(long id) {
		try {
			sfqService.deleteFailQuestion(id, Security.getUserId());
		} catch (AbstractException e) {
			return new Value(new YoomathMobileException(e.getCode()));
		}
		return new Value();
	}

	/**
	 * 错题统计页面接口
	 * 
	 * @since 2.2.0
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "statistics", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statistics() {
		return stuFallibleQuestion2Controller.statIndex();
	}

	/**
	 * 错题统计页面接口<br>
	 * 2017.10.18王森浩更新
	 * 
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "statistics2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statistics2() {
		Map<String, Object> data = new HashMap<String, Object>();
		StudentFallibleQuestion sfq = sfqService.getFirst(Security.getUserId());
		// 是否含有错题
		if (sfq == null) {
			data.put("hasError", false);
			return new Value(data);
		} else {
			data.put("hasError", true);
		}
		data.put("errorStartAt", sfq.getCreateAt());
		data.put("days", daysBetween(sfq.getCreateAt(), new Date()));
		data.put("totalFallible", sfqService.getFallibleCount(Security.getUserId()));
		// 昨天
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		data.put("yesterday", cal.getTime());
		data.put("today", new Date());
		cal.add(Calendar.DATE, -29);
		data.put("last30Time", cal.getTime());
		Long count = sfqService.getLast30Stat(Security.getUserId());
		data.put("last30Fallible", count);
		// 返回当前会员类型
		data.put("memberType", SecurityContext.getMemberType());
		// 会员数据
		if (SecurityContext.getMemberType() != MemberType.NONE) {
			// 近6个月错题统计
			List<Map> list1 = sfqService.queryLast6MonthStat(Security.getUserId());
			data.put("last6Month", list1);

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
			data.put("monthList", Lists.newArrayList());
		}

		return new Value(data);
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
	 * 易错知识点统计,原版本是全部知识点显示，变更按章节显示<br>
	 * 2017.10.16新增
	 * 
	 * @param textbookCode
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "weakKpStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value weakKpStat(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 为空，默认情况。只展示有错题的教材。默认展示用户自己在个人中心所设置的当前教材。
		// 如果当前教材中无错题，则展示高年级中的教材，
		// 例如：错题所属教材有：必修一、必修二，则优先展示必修二
		if (textbookCode == null) {
			Student student = (Student) studentService.getUser(Security.getUserId());
			if (student.getTextbookCode() == null) {
				// 教材未选异常
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_TEXTBOOKCODE_NULL));
			}
			textbookCode = student.getTextbookCode();
			int subjectCode = student.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
					: SubjectService.PHASE_2_MATH;
			List<Textbook> textbookList = tbService.find(student.getPhaseCode(), student.getTextbookCategoryCode(),
					subjectCode);
			List<Integer> textbookCodes = new ArrayList<Integer>();
			for (Textbook t : textbookList) {
				textbookCodes.add(t.getCode());
			}
			Map<Integer, Long> textbookMap = sfqService.getWeakKpCountByTextbookCodes(Security.getUserId(),
					textbookCodes);
			List<Textbook> textbookHasDataList = new ArrayList<Textbook>();
			int maxTextBookCode = 0;
			for (Textbook t : textbookList) {
				if (textbookMap.get(t.getCode()) != null && textbookMap.get(t.getCode()) != 0) {
					textbookHasDataList.add(t);
					if (t.getCode() > maxTextBookCode) {
						maxTextBookCode = t.getCode();
					}
				}
			}
			// 没有易错知识点的教材不显示，处理一下
			data.put("textbookList", tbConvert.to(textbookHasDataList));
			// 判断当前教材下有没有错题
			if (textbookMap.get(textbookCode) == null || textbookMap.get(textbookCode) == 0) {
				// 若当前教材下没有错题，获取有错题的最高一个教材，即取最大
				textbookCode = maxTextBookCode;
			}
		}
		// 选中的教材
		data.put("selTextbookCode", textbookCode);
		// 获取章节列表
		List<VSection> tempList = sectionConvert.to(sectionService.findByTextbookCode(textbookCode, 1));
		List<VSection> vList = new ArrayList<VSection>();
		// 不显示本章综合与测试
		for (VSection v : tempList) {
			if (!v.getName().equals("本章综合与测试")) {
				vList.add(v);
			}
		}
		List<Long> sectionCodes = new ArrayList<Long>();
		for (VSection v : vList) {
			sectionCodes.add(v.getCode());
		}
		if (CollectionUtils.isEmpty(sectionCodes)) {
			return new Value(data);
		}
		Map<Long, Long> sectionData = sfqService.queryWeakKpCount(Security.getUserId(), sectionCodes);
		List<Map> sectionKpList = new ArrayList<Map>();
		for (VSection v : vList) {
			if (sectionData.get(v.getCode()) != null && sectionData.get(v.getCode()) > 0) {
				Map<String, Object> sectionMap = new HashMap<String, Object>();
				sectionMap.put("name", v.getName());
				sectionMap.put("code", v.getCode());
				sectionMap.put("weekKpCount", sectionData.get(v.getCode()));
				sectionKpList.add(sectionMap);
			}
		}
		data.put("sectionKpList", sectionKpList);
		return new Value(data);
	}

	/**
	 * 章节易错知识点详情
	 * 
	 * @param sectionCode
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "weakSectionDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value weakSectionDetail(Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();
		// 获取章节下小节点集合
		List<Long> childCodes = sectionService.findSectionChildren(sectionCode);
		List<VSection> tempList = sectionConvert.to(sectionService.mgetList(childCodes));
		List<VSection> vList = new ArrayList<VSection>();
		for (VSection v : tempList) {
			if (!v.getName().equals("本章综合与测试")) {
				vList.add(v);
			}
		}
		List<VSection> treeList = sectionConvert.assemblySectionTree(vList);
		// 去除当前sectionCode,当前章节下的小章节
		List<Long> newCodes = new ArrayList<Long>();
		for (Long code : childCodes) {
			if (code != sectionCode) {
				newCodes.add(code);
			}
		}
		// 小节和知识点情况对应关系
		Map<Long, List<Map<String, Object>>> sectionKpMap = new HashMap<Long, List<Map<String, Object>>>();
		List<Map> sectionKpList = sfqService.queryFallKpBySectionCodes(Security.getUserId(), newCodes);
		for (Map map : sectionKpList) {
			Long doCount = Long.parseLong(String.valueOf(map.get("docount")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			Long sCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long rightCount = Long.parseLong(String.valueOf(map.get("rightcount")));
			Map<String, Object> kpMap = new HashMap<String, Object>();
			Parameter parameter = parameterService.get(Product.YOOMATH, "h5.knowledge-card.url",
					String.valueOf(map.get("knowpoint_code").toString()));
			kpMap.put("h5PageUrl", parameter.getValue());
			kpMap.put("kpCode", kpCode);
			// 直接查的缓存
			kpMap.put("kpName", knowledgePointService.get(kpCode).getName());
			kpMap.put("doCount", doCount);
			kpMap.put("status", this.getMasterStatus(doCount, rightCount));
			if (sectionKpMap.get(sCode) == null) {
				List<Map<String, Object>> kpList = new ArrayList<Map<String, Object>>();
				kpList.add(kpMap);
				sectionKpMap.put(sCode, kpList);
			} else {
				sectionKpMap.get(sCode).add(kpMap);
			}
		}
		List<VSection> childList = treeList.get(0).getChildren();
		List<Map> childKpList = new ArrayList<Map>();
		// 最多只考虑3层，这边查节和小节
		for (VSection v : childList) {
			Map<String, Object> childMap = new HashMap<String, Object>();
			if (CollectionUtils.isNotEmpty(v.getChildren())) {
				List<Map> childKp2List = new ArrayList<Map>();
				for (VSection vv : v.getChildren()) {
					int kpCount = sectionKpMap.get(vv.getCode()) == null ? 0 : sectionKpMap.get(vv.getCode()).size();
					if (kpCount > 0) {
						Map<String, Object> child2Map = new HashMap<String, Object>();
						child2Map.put("code", vv.getCode());
						child2Map.put("name", vv.getName());
						List<Map<String, Object>> kpList = sectionKpMap.get(vv.getCode());
						child2Map.put("kpList", kpList);
						child2Map.put("kpCount", kpCount);
						if (CollectionUtils.isNotEmpty(kpList)) {
							child2Map.put("weekCodes", this.getWeekKpCodes(kpList));
						}
						childKp2List.add(child2Map);
					}
				}
				if (CollectionUtils.isNotEmpty(childKp2List)) {
					childMap.put("code", v.getCode());
					childMap.put("name", v.getName());
					childMap.put("children", childKp2List);
				}

			} else {
				int kpCount = sectionKpMap.get(v.getCode()) == null ? 0 : sectionKpMap.get(v.getCode()).size();
				if (kpCount > 0) {
					childMap.put("code", v.getCode());
					childMap.put("name", v.getName());
					List<Map<String, Object>> kpList = sectionKpMap.get(v.getCode());
					childMap.put("kpList", kpList);
					if (CollectionUtils.isNotEmpty(kpList)) {
						childMap.put("weekCodes", this.getWeekKpCodes(kpList));
					}
					childMap.put("kpCount", kpCount);
				}
			}
			if (CollectionUtils.isNotEmpty(childMap)) {
				childKpList.add(childMap);
			}
		}
		data.put("childKpList", childKpList);
		data.put("sectionName", sectionService.get(sectionCode).getName());
		return new Value(data);
	}

	public List<Long> getWeekKpCodes(List<Map<String, Object>> list) {
		List<Long> codes = new ArrayList<Long>();
		for (Map<String, Object> map : list) {
			Long kpCode = Long.parseLong(map.get("kpCode").toString());
			codes.add(kpCode);
		}
		return codes;
	}

	/**
	 * 获取掌握情况
	 * 
	 * @param doCount
	 * @param rightCount
	 * @return
	 */
	public MasterStatus getMasterStatus(Long doCount, Long rightCount) {
		if (doCount == null || doCount == 0) {
			return MasterStatus.NO_PRACTICE;
		} else {
			// 做平滑处理 (n+1)/(N+2)
			Double tempRate = new BigDecimal((rightCount + 1) * 100d / (doCount + 2)).setScale(0,
					BigDecimal.ROUND_HALF_UP).doubleValue();
			if (tempRate > 90 && tempRate <= 100) {
				return MasterStatus.EXCELLENT;
			} else if (tempRate > 60 && tempRate <= 90) {
				return MasterStatus.GOOD;
			} else if (tempRate > 30 && tempRate <= 60) {
				return MasterStatus.COMMONLY;
			} else if (tempRate >= 0 && tempRate <= 30) {
				return MasterStatus.WEAK;
			}
		}
		return MasterStatus.NO_PRACTICE;
	}

	/**
	 * 章节code和知识点联合key
	 * 
	 * @param sectionKpList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, Object> initSectionkpMap(List<Map> sectionKpList) {
		Map<String, Object> temp = new HashMap<String, Object>();
		for (Map map : sectionKpList) {
			Long sCode = Long.parseLong(String.valueOf(map.get("section_code")));
			Long kpCode = Long.parseLong(String.valueOf(map.get("knowpoint_code")));
			temp.put(sCode + ":" + kpCode, map);
		}
		return temp;
	}

	/**
	 * 错题本扫题获取详情
	 * 
	 * @since 2.2.0
	 * @param code
	 *            题目CODE
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "questionDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questionDetail(String code) {
		String realCode = null;
		try {
			byte[] newbyte = RSACoder.parseHexStr2Byte(code);
			byte[] result = RSACoder.decryptByPrivateKey(newbyte, Env.getString("yoomath.rsa.privateKey"));
			realCode = new String(result);
		} catch (Exception e) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_QUESTION_NOT_EXIST));
		}
		Question question = questionService.findByCode(realCode);
		if (question == null || question.getStatus() != CheckStatus.PASS || question.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_QUESTION_NOT_EXIST));
		}
		ValueMap vm = ValueMap.value("question",
				questionConvert.to(question, new QuestionConvertOption(false, true, true, false, null)));
		Map<Long, Boolean> existMap = sfqService.exist(Lists.newArrayList(question.getId()), Security.getUserId());
		vm.put("inFallible", existMap.get(question.getId()) == null ? false : existMap.get(question.getId()));
		return new Value(vm);
	}

	@MemberAllowed
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "2/questionDetail", method = { RequestMethod.GET, RequestMethod.POST })
	public Value questionDetail2(String code) {
		String realCode = null;
		try {
			byte[] newbyte = RSACoder.parseHexStr2Byte(code);
			byte[] result = RSACoder.decryptByPrivateKey(newbyte, Env.getString("yoomath.rsa.privateKey"));
			realCode = new String(result);
		} catch (Exception e) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_QUESTION_NOT_EXIST));
		}

		ValueMap vm = ValueMap.value();
		Question question = questionService.findByCode(realCode);
		if (question == null || question.getStatus() != CheckStatus.PASS || question.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_QUESTION_NOT_EXIST));
		}
		// 用户没有登录或者当前用户不是学生用户
		VStudentFallibleQuestion vfq = null;
		QuestionConvertOption questionConvertOption = new QuestionConvertOption(false, false, true, false, null);
		questionConvertOption.setInitExamination(true);
		if (!Security.isLogin() || Security.getUserType() != UserType.STUDENT) {
			vm.put("inFallible", false);
			vfq = new VStudentFallibleQuestion();
			vfq.setQuestion(questionConvert.to(question, questionConvertOption));
			vm.put("fallQuestion", vfq);
		} else if (Security.isLogin() && Security.getUserType() == UserType.STUDENT) {
			MemberType memberType = SecurityContext.getMemberType();
			vm.put("memberType", memberType);
			StudentFallibleQuestion q = sfqService.findByStudentAndQuestion(Security.getUserId(), question.getId());

			if (q == null) {
				vm.put("inFallible", false);
				vfq = new VStudentFallibleQuestion();

				if (memberType == MemberType.VIP) {
					questionConvertOption.setAnalysis(true);
					VQuestion vq = questionConvert.to(question, questionConvertOption);
					vfq.setQuestion(vq);
				} else {
					VQuestion vq = questionConvert.to(question, questionConvertOption);
					vfq.setQuestion(vq);
				}
				vm.put("fallQuestion", vfq);
			} else {
				VStudentFallibleQuestion v = sfqConvert.to(q);

				if (memberType == MemberType.VIP) {
					questionConvertOption.setAnalysis(true);
					VQuestion vq = questionConvert.to(question, questionConvertOption);
					v.setQuestion(vq);
				} else {
					VQuestion vq = questionConvert.to(question, questionConvertOption);
					v.setQuestion(vq);
				}
				vm.put("inFallible", true);
				vm.put("fallQuestion", v);
			}

		}

		return new Value(vm);

	}

	/**
	 * 添加错题
	 * 
	 * @since 2.2.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "add", method = { RequestMethod.POST, RequestMethod.GET })
	public Value add(Long questionId) {
		ValueMap vm = ValueMap.value();
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		Integer categoryCode = null;
		Teacher teacher = null;
		Student student = null;
		if (CollectionUtils.isNotEmpty(clazzs)) {
			for (HomeworkStudentClazz c : clazzs) {
				Long teacherId = zyHkClassService.get(c.getClassId()).getTeacherId();
				if (teacherId == null) {
					continue;
				}
				teacher = ((Teacher) teacherService.getUser(UserType.TEACHER, teacherId));
				if (teacher != null) {
					categoryCode = teacher.getTextbookCategoryCode();
					break;
				}
			}

			if (teacher == null || categoryCode == null) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_STU_NOCATEGORYCODE));
			}

		} else {
			student = ((Student) teacherService.getUser(UserType.STUDENT, Security.getUserId()));
			categoryCode = student.getTextbookCategoryCode();
		}

		List<Integer> textbookCodes = sfqService.add(questionId, Security.getUserId(), null,
				StudentQuestionAnswerSource.QR);
		Integer code = null;
		for (Integer c : textbookCodes) {
			if (c.toString().startsWith(categoryCode.toString())) {
				code = c;
				break;
			}
		}
		if (code == null) {
			VTextbook textbook = new VTextbook();
			textbook.setName("其他");
			vm.put("textbook", textbook);
		} else {
			VTextbook textbook = tbConvert.to(tbService.get(code));
			vm.put("textbook", textbook);
		}
		return new Value(vm);
	}
}
