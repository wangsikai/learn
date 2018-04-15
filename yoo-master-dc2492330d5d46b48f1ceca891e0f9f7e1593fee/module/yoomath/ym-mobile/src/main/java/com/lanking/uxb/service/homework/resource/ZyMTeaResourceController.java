package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathHomeworkRegistryConstants;
import com.lanking.cloud.component.mq.common.constants.MqYoomathPushRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkCorrectingType;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleAction;
import com.lanking.cloud.domain.yoomath.push.api.PushHandleForm;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.QuestionKnowledgeService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;
import com.lanking.uxb.service.wx.api.ZyWXMessageService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectUserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;

/**
 * 教师端资源相关接口
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月18日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/resource")
public class ZyMTeaResourceController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	protected TextbookService tbService;
	@Autowired
	protected TextbookConvert tbConvert;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyBookVersionConvert zyBookVersionConvert;
	@Autowired
	private TeacherOperationCacheService operationCacheService;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private QuestionKnowledgeService questionKnowledgeService;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	@Qualifier("executor")
	private Executor executor;
	@Autowired
	private MqSender mqSender;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private ZyCorrectUserService zyCorrectUserService;
	@Autowired
	private ZyWXMessageService zyWXMessageService;
	@Autowired
	private ZyQuestionCarService zyQCarService;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkQuestionService hkQuestionService;
	@Autowired
	private ParameterService parameterService;

	/**
	 * 进入资源页
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 切换过教材，没选择教辅的情况下，记录缓存
		if (textbookCode != null) {
			operationCacheService.setProgress(Security.getUserId(), Long.valueOf(textbookCode),
					TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		}

		// 教材版本
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		TextbookCategory textbookCategory = textbookCategoryService.get(teacher.getTextbookCategoryCode());
		data.put("categoryName", textbookCategory != null ? textbookCategory.getName() : null);
		// 教材列表
		List<VTextbook> textbooks = tbConvert.to(
				tbService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(), teacher.getSubjectCode()));
		// 新版本首次登录， 从缓存取是否设置过教辅
		boolean islogin = false;
		Long textbookCodeCache = operationCacheService.getProgress(Security.getUserId(),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		if (textbookCodeCache == null) {
			islogin = true;
		} else {
			if (textbookCode == null) {
				textbookCode = textbookCodeCache.intValue();
			}
		}

		// 切换过版本
		boolean changeCategory = false;
		boolean changeTemp = false;
		for (VTextbook v : textbooks) {
			if (textbookCode != null && v.getCode() == textbookCode.intValue()) {
				changeTemp = true;
				break;
			}
		}
		if (!changeTemp) {
			changeCategory = true;
		}

		// 设置教材
		if (textbookCode == null) {
			// 新版本首次登录
			// 上次作业所用的教材
			ZyHomeworkQuery query = new ZyHomeworkQuery();
			query.setTeacherId(Security.getUserId());
			Homework homework = zyHkService.queryLastestByTeacher(query);
			if (homework == null) {
				// 当前教材
				textbookCode = teacher.getTextbookCode();
			} else if (homework.getTextbookCode() == null) {
				// 当前教材
				textbookCode = teacher.getTextbookCode();
			} else {
				textbookCode = homework.getTextbookCode();
			}

		} else {
			if (changeCategory) {
				// 是否是在设置中手动切换了版本
				textbookCode = textbooks.get(0).getCode();
			}
		}

		// 记录当前textbookCode
		// 2017/8/7 戚元鹏：不选择教材就一直弹框提示选择，不进入资源页
		// operationCacheService.setProgress(Security.getUserId(),
		// Long.valueOf(textbookCode),
		// TeacherOperationCacheService.PROGRESS_TEXTBOOK);

		VTextbook vtextbook = new VTextbook();
		boolean ff = false;
		for (VTextbook v : textbooks) {
			if (v.getCode() == textbookCode) {
				ff = true;
				vtextbook = v;
			}
		}
		if (ff) {
			data.put("textbook", vtextbook);
		}
		data.put("textbookCode", textbookCode);

		// 跳转选择教材和常用教辅页,要加是否在设置中修改版本逻辑
		if (islogin) {
			data.put("changeTextbook", islogin);
		} else {
			data.put("changeTextbook", changeCategory);
		}

		// 进度
		List<Section> sectionList = sectionService.findByTextbookCode(textbookCode);
		List<VSection> vsections = sectionConvert.to(sectionList);
		data.put("sections", sectionConvert.assemblySectionTree(vsections));

		Long sectionCode = null;
		// 切换过版本，sectionCode取第一小节
		if (changeCategory) {
			// 没设置过sectionCode取第一小节
			if (CollectionUtils.isNotEmpty(vsections)) {
				sectionCode = getFirstSectionCode(vsections.get(0));
			}
		} else {
			sectionCode = operationCacheService.getProgress(Security.getUserId(),
					TeacherOperationCacheService.PROGRESS);
			if (sectionCode == null) {
				// 没设置过sectionCode取第一小节
				if (CollectionUtils.isNotEmpty(vsections)) {
					sectionCode = getFirstSectionCode(vsections.get(0));
				}
			} else {
				boolean sectionFlag = false;
				for (VSection vSection : vsections) {
					if (vSection.getCode() == sectionCode.longValue()) {
						sectionFlag = true;
						break;
					}
				}
				if (!sectionFlag) {
					// 没设置过sectionCode取第一小结
					if (CollectionUtils.isNotEmpty(vsections)) {
						sectionCode = getFirstSectionCode(vsections.get(0));
					}
				}
			}
		}

		// 记录缓存，不记录的话推荐题目那边会有取不到进度的问题
		if (sectionCode != null) {
			operationCacheService.setProgress(Security.getUserId(), sectionCode, TeacherOperationCacheService.PROGRESS);
		}
		data.put("sectionCode", sectionCode);

		// 教辅列表
		// 判断用户有没有选择教辅
		List<BookVersion> userBooks = zyBookService.getUserBookList(teacher.getTextbookCategoryCode(), textbookCode,
				Security.getUserId());
		if (CollectionUtils.isNotEmpty(userBooks)) {
			List<Long> bookIds = operationCacheService.getSelectBooks(Security.getUserId());
			if (CollectionUtils.isEmpty(bookIds)) {
				data.put("userBooks", zyBookVersionConvert.to(userBooks));
			} else {
				// 排序
				Map<Long, BookVersion> bvMap = new HashMap<Long, BookVersion>();
				for (BookVersion bv : userBooks) {
					bvMap.put(bv.getBookId(), bv);
				}
				List<BookVersion> tempList = new ArrayList<BookVersion>();
				for (Long bookId : bookIds) {
					if (bvMap.get(bookId) != null) {
						tempList.add(bvMap.get(bookId));
					}
				}
				if (CollectionUtils.isEmpty(tempList)) {
					data.put("userBooks", Collections.EMPTY_LIST);
				} else {
					data.put("userBooks", zyBookVersionConvert.to(tempList));
				}
			}

		} else {
			data.put("userBooks", Collections.EMPTY_LIST);
		}

		return new Value(data);
	}

	/**
	 * 没设置进度的情况下取第一个sectionCode
	 * 
	 * @param vsection
	 *            章节
	 * @return sectionCode
	 */
	private long getFirstSectionCode(VSection vsection) {
		long sectionCode = 0l;
		if (CollectionUtils.isNotEmpty(vsection.getChildren())) {
			sectionCode = getFirstSectionCode(vsection.getChildren().get(0));
		} else {
			sectionCode = vsection.getCode();
		}

		return sectionCode;
	}

	/**
	 * 当前进度
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "progress", method = { RequestMethod.POST, RequestMethod.GET })
	public Value progress(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		List<Section> sectionList = sectionService.findByTextbookCode(textbookCode);
		List<VSection> vsections = sectionConvert.to(sectionList);
		data.put("sections", sectionConvert.assemblySectionTree(vsections));

		Long sectionCode = operationCacheService.getProgress(Security.getUserId(),
				TeacherOperationCacheService.PROGRESS);
		if (sectionCode == null) {
			sectionCode = vsections.get(0).getCode();
		} else {
			boolean sectionFlag = false;
			for (VSection vSection : vsections) {
				if (vSection.getCode() == sectionCode.longValue()) {
					sectionFlag = true;
					break;
				}
			}
			if (!sectionFlag) {
				sectionCode = vsections.get(0).getCode();
			}
		}
		data.put("sectionCode", sectionCode);

		return new Value(data);
	}

	/**
	 * 调整进度
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "changeProgress", method = { RequestMethod.POST, RequestMethod.GET })
	public Value changeProgress(Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		Section section = sectionService.get(sectionCode);
		if (section == null) {
			throw new IllegalArgException();
		}
		// 记录缓存
		operationCacheService.setProgress(Security.getUserId(), sectionCode, TeacherOperationCacheService.PROGRESS);
		operationCacheService.setProgress(Security.getUserId(), Long.valueOf(section.getTextbookCode()),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);

		data.put("sectionCode", sectionCode);
		return new Value(data);
	}

	/**
	 * 布置作业前获取相关数据
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "publishData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishData() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			data.put("clazzs", Collections.EMPTY_LIST);
		} else {
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
			List<VHomeworkClazz> emptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			List<VHomeworkClazz> notEmptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			for (VHomeworkClazz v : vclazzs) {
				if (v.getStudentNum() <= 0) {
					emptyClazz.add(v);
				} else {
					notEmptyClazz.add(v);
				}
			}

			// 取上次布置的作业
			List<Long> classIds = operationCacheService.getHomeworkClass(Security.getUserId());
			for (VHomeworkClazz v : notEmptyClazz) {
				for (Long id : classIds) {
					if (v.getId() == id.longValue()) {
						v.setSelected(true);
					}
				}
			}

			notEmptyClazz.addAll(emptyClazz);
			data.put("clazzs", notEmptyClazz);
		}
		data.put("autoIssueHour", Env.getDynamicInt("homework.issued.time"));

		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());

		return new Value(data);
	}

	/**
	 * 布置作业前获取相关数据
	 * 
	 * @author wangsenhao
	 * @since yoomath(mobile) V1.3.6
	 * @param
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "publishDataByGroup", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishDataByGroup() {
		Map<String, Object> data = new HashMap<String, Object>();

		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			data.put("clazzs", Collections.EMPTY_LIST);
		} else {
			ZyHomeworkClassConvertOption option = new ZyHomeworkClassConvertOption();
			option.setInitClassGroup(true);
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs, option);
			List<VHomeworkClazz> emptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			List<VHomeworkClazz> notEmptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			for (VHomeworkClazz v : vclazzs) {
				if (v.getStudentNum() <= 0) {
					emptyClazz.add(v);
				} else {
					notEmptyClazz.add(v);
				}
			}

			// 取上次布置的作业
			List<Long> classIds = operationCacheService.getHomeworkClass(Security.getUserId());
			// 取上次布置作业的小组
			List<Long> groupIds = operationCacheService.getHomeworkGroup(Security.getUserId());
			for (VHomeworkClazz v : notEmptyClazz) {
				for (Long id : classIds) {
					if (v.getId() == id.longValue()) {
						v.setSelected(true);
					}
				}
				for (Long id : groupIds) {
					if (CollectionUtils.isNotEmpty(v.getGroupList())) {
						for (VHomeworkClazzGroup group : v.getGroupList()) {
							if (group.getId() == id.longValue()) {
								group.setSelected(true);
							}
						}
					}
				}
			}

			notEmptyClazz.addAll(emptyClazz);
			data.put("clazzs", notEmptyClazz);
		}
		data.put("autoIssueHour", Env.getDynamicInt("homework.issued.time"));

		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());

		// since 2018-3-1 小悠快批
		// 查询用户是否可以启用解答题自动批改功能
		Parameter autoCorrectParameter = parameterService.get(Product.YOOMATH, "question-answering-auto-correct");
		boolean questionansweringAutoCorrect = false;
		if (autoCorrectParameter != null) { // 没取到说明还没有上线
			String correctValue = autoCorrectParameter.getValue();
			if (correctValue != null) {
				if ("0".equals(correctValue)) {
					questionansweringAutoCorrect = true;
				} else {
					String[] correctValues = correctValue.split(",");
					List<Long> teacherIds = Lists.newArrayList();
					for (int i = 0; i < correctValues.length; i++) {
						teacherIds.add(Long.valueOf(correctValues[i]));
					}
					if (teacherIds.contains(Security.getUserId())) {
						questionansweringAutoCorrect = true;
					}
				}
			}
		}
		data.put("answerQuestionAutoCorrect", questionansweringAutoCorrect);

		return new Value(data);
	}

	/**
	 * 布置作业
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@Deprecated
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publish", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publish(PublishHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.getJsUnicodeLength(form.getName()) > 40) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_NAME_TOO_LONG));
		}
		if (form == null || CollectionUtils.isEmpty(form.getHomeworkClassIds())
				|| CollectionUtils.isEmpty(form.getqIds()) || form.getDeadline() == null
				|| form.getStartTime() >= form.getDeadline() || form.getDeadline() < System.currentTimeMillis() + 600000
				|| form.getDifficulty() == null || form.getCorrectingType() == null) {
			return new Value(new IllegalArgException());
		}
		form.setCreateId(Security.getUserId());

		// 处理知识点，知识点只能根据questionid查
		Map<Long, List<Long>> kpCodeMap = questionKnowledgeService.mgetByQuestions(form.getqIds());
		List<Long> knowledgePointCodes = Lists.newArrayList();
		// 去重
		Map<Long, Long> resetKonwledgeCodes = new HashMap<>();
		for (Entry<Long, List<Long>> entry : kpCodeMap.entrySet()) {
			for (Long code : entry.getValue()) {
				resetKonwledgeCodes.put(code, code);
			}
		}
		for (Entry<Long, Long> entry : resetKonwledgeCodes.entrySet()) {
			knowledgePointCodes.add(entry.getValue());
		}
		// 知识点
		form.setQuestionknowledgePoints(knowledgePointCodes);

		// 创建作业
		Homework homework = createHomework(form);
		data.put("questionCount", homework.getQuestionCount());
		// 用户成长值
		VUserReward reward = getUserReward(homework);
		data.put("userReward", reward);

		// 清空作业篮子
		zyQCarService.removeAll(Security.getUserId());
		// 用户动作处理
		userActionService.asyncAction(UserAction.PUBLISH_HOMEWORK, Security.getUserId(), null);
		// 记录用户选择的class
		operationCacheService.setHomeworkClass(Security.getUserId(), form.getHomeworkClassIds());

		return new Value(data);
	}

	/**
	 * 布置作业---分组 2017-11-8新增
	 * 
	 * @author wangsenhao
	 * @since yoomath(mobile) V1.3.6
	 * @param
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publishByGroup", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishByGroup(PublishHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (StringUtils.getJsUnicodeLength(form.getName()) > 40) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_NAME_TOO_LONG));
		}
		if (form == null || form.getClassGroupList() == null || CollectionUtils.isEmpty(form.getqIds())
				|| form.getDeadline() == null || form.getStartTime() >= form.getDeadline()
				|| form.getDeadline() < System.currentTimeMillis() + 600000 || form.getDifficulty() == null
				|| form.getCorrectingType() == null) {
			return new Value(new IllegalArgException());
		}
		form.setCreateId(Security.getUserId());

		// 处理知识点，知识点只能根据questionid查
		Map<Long, List<Long>> kpCodeMap = questionKnowledgeService.mgetByQuestions(form.getqIds());
		List<Long> knowledgePointCodes = Lists.newArrayList();
		// 去重
		Map<Long, Long> resetKonwledgeCodes = new HashMap<>();
		for (Entry<Long, List<Long>> entry : kpCodeMap.entrySet()) {
			for (Long code : entry.getValue()) {
				resetKonwledgeCodes.put(code, code);
			}
		}
		for (Entry<Long, Long> entry : resetKonwledgeCodes.entrySet()) {
			knowledgePointCodes.add(entry.getValue());
		}
		// 知识点
		form.setQuestionknowledgePoints(knowledgePointCodes);

		// 创建作业
		Homework homework = createHomework(form);
		data.put("questionCount", homework.getQuestionCount());
		// 用户成长值
		VUserReward reward = getUserReward(homework);
		data.put("userReward", reward);

		// 清空作业篮子
		zyQCarService.removeAll(Security.getUserId());
		// 用户动作处理
		userActionService.asyncAction(UserAction.PUBLISH_HOMEWORK, Security.getUserId(), null);
		List<Map> groupList = JSON.parseObject(form.getClassGroupList(), List.class);
		List<Long> classIds = new ArrayList<Long>();
		List<Long> groupIds = new ArrayList<Long>();
		for (Map pa : groupList) {
			Long classId = Long.parseLong(pa.get("classId").toString());
			Long groupId = pa.get("groupId") == null ? null : Long.parseLong(pa.get("groupId").toString());
			if (groupId != null) {
				groupIds.add(groupId);
			} else {
				classIds.add(classId);
			}
		}
		// 记录用户选择的class和group,分开记录
		operationCacheService.setHomeworkClass(Security.getUserId(), classIds);
		operationCacheService.setHomeworkGroup(Security.getUserId(), groupIds);
		return new Value(data);
	}

	/**
	 * 创建作业
	 * 
	 * @param form
	 * @return
	 */
	private Homework createHomework(PublishHomeworkForm form) {
		try {
			// 小悠快批删除订正上一次作业错题
			// form.setCorrectLastHomework(Env.getBoolean("homework.correct"));
			form.setCorrectingType(HomeworkCorrectingType.TEACHER);
			List<Homework> homeworkList = new ArrayList<Homework>();
			if (form.getClassGroupList() == null) {
				homeworkList = zyHomeworkService.publish3(form);
			} else {
				homeworkList = zyHomeworkService.publishByGroup(form);
			}
			final List<Homework> homeworks = homeworkList;
			Homework homework = homeworks.get(0);
			executor.execute(new Runnable() {
				@Override
				public void run() {
					for (Homework hk : homeworks) {
						if (hk.getStatus() == HomeworkStatus.PUBLISH) {
							zyHomeworkService.publishHomework2(hk.getId());
							JSONObject jo = new JSONObject();
							jo.put("teacherId", hk.getCreateId());
							jo.put("homeworkId", hk.getId());
							mqSender.send(MqYoomathHomeworkRegistryConstants.EX_YM_HOMEWORK,
									MqYoomathHomeworkRegistryConstants.RK_YM_HOMEWORK_PUBLISH,
									MQ.builder().data(jo).build());
						}
					}
				}
			});

			// 通知
			if (homework.getId() != 0) {
				zyCorrectUserService.asyncNoticeUser(homework.getId());
				mqSender.send(MqYoomathPushRegistryConstants.EX_YM_PUSH, MqYoomathPushRegistryConstants.RK_YM_PUSH,
						MQ.builder().data(new PushHandleForm(PushHandleAction.NEW_HOMEWORK, homework.getId())).build());
				// 原来给班级布置的作业
				if (form.getClassGroupList() == null) {
					zyWXMessageService.sendPublishHomeworkMessage(form.getHomeworkClassIds(), homework.getId(), null);
				} else {
					// 按小组布置作业
					List<Map> groupList = JSON.parseObject(form.getClassGroupList(), List.class);
					List<Long> groupIds = new ArrayList<Long>();
					List<Long> classIds = new ArrayList<Long>();
					for (Map map : groupList) {
						Long homeworkClassId = Long.parseLong(map.get("classId").toString());
						Long groupId = map.get("groupId") == null ? null
								: Long.parseLong(map.get("groupId").toString());
						if (groupId != null) {
							groupIds.add(groupId);
						} else {
							classIds.add(homeworkClassId);
						}
					}
					zyWXMessageService.sendPublishHomeworkMessage(classIds, homework.getId(), groupIds);
				}

			}
			return homework;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerException();
		}
	}

	/**
	 * 创建作业获得的成长值相关
	 * 
	 * @param homework
	 * @return
	 */
	private VUserReward getUserReward(Homework homework) {
		GrowthLog growthLogFirst = growthService.grow(GrowthAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
				Biz.HOMEWORK, homework.getId(), true);
		CoinsLog coinsLogFirst = coinsService.earn(CoinsAction.FIRST_PUBLISH_HOMEWORK, Security.getUserId(), -1,
				Biz.HOMEWORK, homework.getId());
		GrowthLog growthLog = growthService.grow(GrowthAction.PUBLISH_HOMEWORK, Security.getUserId(), -1, Biz.HOMEWORK,
				homework.getId(), true);
		CoinsLog coinsLog = coinsService.earn(CoinsAction.PUBLISH_HOMEWORK, Security.getUserId(), -1, Biz.HOMEWORK,
				homework.getId());
		// 防止初次布置作业 升级，后面在给与常规奖励时返回页面的isUpgrade不正确
		if (growthLog.getHonor() != null) {
			boolean isupgrade = false;
			int upGradeCoins = 0;
			if (growthLogFirst.getHonor() != null) {
				if (growthLogFirst.getHonor().isUpgrade() && growthLogFirst.getHonor().getUpRewardCoins() != null) {
					isupgrade = true;
					upGradeCoins = growthLogFirst.getHonor().getUpRewardCoins();
				}
			} else {
				isupgrade = growthLog.getHonor().isUpgrade();
				if (isupgrade && growthLog.getHonor().getUpRewardCoins() != null) {
					upGradeCoins = growthLog.getHonor().getUpRewardCoins();
				}
			}

			return new VUserReward(upGradeCoins, isupgrade, growthLog.getHonor().getLevel(),
					growthLog.getGrowthValue()
							+ (growthLogFirst.getGrowthValue() == -1 ? 0 : growthLogFirst.getGrowthValue()),
					coinsLog.getCoinsValue()
							+ (coinsLogFirst.getCoinsValue() == -1 ? 0 : coinsLogFirst.getCoinsValue()));
		}
		return null;
	}

	/**
	 * 再次布置作业
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publishByHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishByHistory(PublishHomeworkForm form) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (form.getHomeworkId() == null || form.getHomeworkId() < 0) {
			return new Value(new IllegalArgException());
		}
		Homework hk = hkService.get(form.getHomeworkId());
		if (hk == null || hk.getCreateId().longValue() != Security.getUserId()) {
			return new Value(new IllegalArgException());
		}
		// Exercise exercise = exerciseService.get(hk.getExerciseId());
		// form.setTextbookExerciseId(exercise.getTextbookExerciseId());
		// form.setBookId(exercise.getBookId());
		// form.setSectionCode(exercise.getSectionCode());
		List<Long> qIds = hkQuestionService.getQuestion(form.getHomeworkId());
		form.setqIds(qIds);
		// form.setMetaKnowpoints(hk.getMetaKnowpoints());
		form.setKnowledgePoints(hk.getKnowledgePoints());
		form.setDifficulty(hk.getDifficulty());
		form.setAutoIssue(hk.isAutoIssue());
		form.setCreateId(Security.getUserId());

		// 创建作业
		Homework homework = createHomework(form);
		data.put("questionCount", homework.getQuestionCount());
		// 用户成长值
		VUserReward reward = getUserReward(homework);
		data.put("userReward", reward);

		// 记录用户选择的class
		if (form.getClassGroupList() != null) {
			List<Map> groupList = JSON.parseObject(form.getClassGroupList(), List.class);
			List<Long> classIds = new ArrayList<Long>();
			List<Long> groupIds = new ArrayList<Long>();
			for (Map pa : groupList) {
				Long classId = Long.parseLong(pa.get("classId").toString());
				Long groupId = pa.get("groupId") == null ? null : Long.parseLong(pa.get("groupId").toString());
				if (groupId != null) {
					groupIds.add(groupId);
				} else {
					classIds.add(classId);
				}
			}
			// 记录用户选择的class和group,分开记录
			operationCacheService.setHomeworkClass(Security.getUserId(), classIds);
			operationCacheService.setHomeworkGroup(Security.getUserId(), groupIds);
		} else {
			operationCacheService.setHomeworkClass(Security.getUserId(), form.getHomeworkClassIds());
		}

		return new Value(data);
	}
}
