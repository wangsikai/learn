package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Exercise;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.homework.form.ChooseBookForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkBookController;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkController;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyExerciseService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.api.ZySchoolQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.form.PublishHomeworkForm;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;
import com.lanking.uxb.service.zuoye.value.VBookVersion;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

@ApiAllowed
@RestController
@RequestMapping("zy/m/t/hk/book")
public class ZyMTeaHomeworkBookController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private ZySchoolQuestionService zySchoolQuestionService;
	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyBookVersionConvert zyBookVersionConvert;
	@Autowired
	private ZyUserSchoolBookService userSchoolBookService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private ZyTeaHomeworkController teaHomeworkController;
	@Autowired
	private ZyTeaHomeworkBookController teaHomeworkBookController;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private TeacherOperationCacheService operationCacheService;
	@Autowired
	private ZyExerciseService exerciseService;
	@Autowired
	private ZyQuestionCarService questionCarService;
	@Autowired
	private UserActionService userActionService;

	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(Integer textbookCode) {
		if (Security.getDeviceType() == DeviceType.IOS
				&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) <= 117) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOS_UNCORRECT_CLIENT_VERSION));
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getTextbookCode() == null) {
			// 如果没有设置教材则提示客户端设置教材
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_NEED_FILL_PROFILE));
		}
		ValueMap vm = ValueMap.value();

		// 可选教材列表
		List<Textbook> tbs = tbService.find(Product.YOOMATH, teacher.getPhaseCode(), teacher.getSubjectCode(),
				teacher.getTextbookCategoryCode());
		vm.put("textbooks", tbConvert.to(tbs));

		if (textbookCode == null) {
			// 当前教材
			textbookCode = teacher.getTextbookCode();
		}
		vm.put("textbookCode", textbookCode);

		// 判断用户有没有选择教辅
		List<BookVersion> userBooks = zyBookService.getUserBookList(teacher.getTextbookCategoryCode(), textbookCode,
				Security.getUserId());
		if (CollectionUtils.isNotEmpty(userBooks)) {
			List<VBookVersion> vuserBooks = zyBookVersionConvert.to(userBooks);
			vm.put("userBooks", vuserBooks);
			Long userBookId = null;
			Long cacheUserBookId = operationCacheService.getHomeworkBookSchoolBook(Security.getUserId());
			if (cacheUserBookId != null) {
				for (VBookVersion vb : vuserBooks) {
					if (vb.getId() == cacheUserBookId.longValue()) {
						userBookId = cacheUserBookId;
						break;
					}
				}
			}
			if (userBookId == null) {
				userBookId = vuserBooks.get(0).getId();
			}
			operationCacheService.setHomeworkBookSchoolBook(Security.getUserId(), userBookId);
			vm.put("userBookId", userBookId);
			List<VBookCatalog> vs = bookCatalogConvert.to(zyBookService.getBookCatalogs(userBookId));
			Exercise exercise = exerciseService.findLatestOne(Security.getUserId(), null, null, userBookId);
			if (exercise != null) {
				for (VBookCatalog v : vs) {
					if (v.getCode() == exercise.getSectionCode().longValue()) {
						v.setSelected(true);
						break;
					}
				}
			}
			vm.put("sections", bookCatalogConvert.assemblySectionTree(vs));
		} else {
			vm.put("userBooks", Collections.EMPTY_LIST);
		}

		// 当前用户的会员类型
		vm.put("memberType", SecurityContext.getMemberType());
		// 当前教材下的可选校本图书
		Map<Long, Long> userschoolMap = null;
		if (teacher.getSchoolId() != null && SecurityContext.getMemberType() == MemberType.SCHOOL_VIP) {
			vm.put("school", schoolConvert.get(teacher.getSchoolId()));
			Page<BookVersion> schoolBookPage = zyBookService.getSchoolBook(teacher.getTextbookCategoryCode(),
					textbookCode, teacher.getSchoolId(), P.index(1, 100));
			List<BookVersion> schoolBooks = schoolBookPage.getItems();
			if (CollectionUtils.isNotEmpty(schoolBooks)) {
				if (userschoolMap == null) {
					userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
				}
				// 把有标签放在前面，再按updateAt排序
				List<VBookVersion> vschoolBooks = zyBookVersionConvert.to(schoolBooks);
				List<VBookVersion> hasTagList = new ArrayList<VBookVersion>();
				List<VBookVersion> noTagList = new ArrayList<VBookVersion>();
				for (VBookVersion b : vschoolBooks) {
					if (b.getTagName() != null) {
						hasTagList.add(b);
					} else {
						noTagList.add(b);
					}
				}
				hasTagList.addAll(noTagList);
				for (VBookVersion vbook : hasTagList) {
					vbook.setSchoolId(teacher.getSchoolId());
					if (userschoolMap.get(vbook.getBookId()) != null) {
						vbook.setSelected(true);
					} else {
						vbook.setSelected(false);
					}
				}
				vm.put("schoolBooks", hasTagList);
			} else {
				vm.put("schoolBooks", Collections.EMPTY_LIST);
			}
		} else {
			vm.put("schoolBooks", Collections.EMPTY_LIST);
		}
		// 当前教材下的可选免费图书
		Page<BookVersion> freeBookPage = zyBookService.getFreeBook(teacher.getTextbookCategoryCode(), textbookCode,
				P.index(1, 100));
		List<BookVersion> freeBooks = freeBookPage.getItems();
		if (CollectionUtils.isNotEmpty(freeBooks)) {
			if (userschoolMap == null) {
				userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
			}
			List<VBookVersion> vfreeBooks = zyBookVersionConvert.to(freeBooks);
			List<VBookVersion> hasTagList = new ArrayList<VBookVersion>();
			List<VBookVersion> noTagList = new ArrayList<VBookVersion>();
			for (VBookVersion b : vfreeBooks) {
				if (b.getTagName() != null) {
					hasTagList.add(b);
				} else {
					noTagList.add(b);
				}
			}
			hasTagList.addAll(noTagList);
			for (VBookVersion vbook : hasTagList) {
				vbook.setSchoolId(null);
				if (userschoolMap.get(vbook.getBookId()) != null) {
					vbook.setSelected(true);
				} else {
					vbook.setSelected(false);
				}
			}
			vm.put("freeBooks", hasTagList);
		} else {
			vm.put("freeBooks", Collections.EMPTY_LIST);
		}
		return new Value(vm);
	}

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "choose", method = { RequestMethod.POST, RequestMethod.GET })
	public Value choose(ChooseBookForm form) {
		if (form.getFreeBookIds() == null) {
			form.setFreeBookIds(Collections.EMPTY_LIST);
		}
		if (form.getSchoolBookIds() == null) {
			form.setSchoolBookIds(Collections.EMPTY_LIST);
		}
		if (form.getTextbookCode() == null || (form.getFreeBookIds().size() + form.getSchoolBookIds().size() > 6)) {
			return new Value(new IllegalArgException());
		}
		// 获取当前用户选择的图书
		List<Long> userBookIds = Lists.newArrayList();
		Textbook textbook = tbService.get(form.getTextbookCode());
		int textbookCategoryCode = textbook.getCategoryCode();
		List<BookVersion> userBooks = zyBookService.getUserBookList(textbookCategoryCode, textbook.getCode(),
				Security.getUserId());
		for (BookVersion bookVersion : userBooks) {
			userBookIds.add(bookVersion.getBookId());
		}

		List<Map<String, String>> bookFromList = Lists.newArrayList();
		for (Long freeBookId : form.getFreeBookIds()) {
			Map<String, String> one = new HashMap<String, String>(1);
			one.put("bookId", String.valueOf(freeBookId));
			bookFromList.add(one);
			userBookIds.remove(freeBookId);
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		String schoolId = "";
		if (teacher.getSchoolId() != null) {
			schoolId = String.valueOf(teacher.getSchoolId());
		}
		for (Long schoolBookId : form.getSchoolBookIds()) {
			Map<String, String> one = new HashMap<String, String>(2);
			one.put("bookId", String.valueOf(schoolBookId));
			if (!userBookIds.contains(schoolBookId)) {
				one.put("schoolId", schoolId);
			}
			bookFromList.add(one);
			userBookIds.remove(schoolBookId);
		}
		teaHomeworkBookController.chooseBook(JSONObject.toJSONString(bookFromList),
				JSONObject.toJSONString(userBookIds), textbookCategoryCode, textbook.getCode());
		return new Value();
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(long id) {
		List<VBookCatalog> vs = bookCatalogConvert.to(zyBookService.getBookCatalogs(id));
		Exercise exercise = exerciseService.findLatestOne(Security.getUserId(), null, null, id);
		if (exercise != null) {
			for (VBookCatalog v : vs) {
				if (v.getCode() == exercise.getSectionCode().longValue()) {
					v.setSelected(true);
					break;
				}
			}
		}
		ValueMap vm = ValueMap.value("sections", bookCatalogConvert.assemblySectionTree(vs));
		operationCacheService.setHomeworkBookSchoolBook(Security.getUserId(), id);
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(long sectionCode) {
		ValueMap vm = ValueMap.value();
		List<Long> qIds = zyBookService.listQuestions(sectionCode);
		if (qIds.size() == 0) {
			vm.put("questions", Collections.EMPTY_LIST);
		} else {
			Map<Long, Question> questions = questionService.mget(qIds);
			QuestionConvertOption option = new QuestionConvertOption(false, true, true, true, null);
			// @since 教师端v1.3.0
			option.setInitPublishCount(true);
			option.setInitQuestionSimilarCount(true);
			option.setInitExamination(true);
			option.setInitQuestionTag(true);
			Map<Long, VQuestion> vquestions = questionConvert.to(questions, option);
			List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
			List<VQuestion> qs = new ArrayList<VQuestion>(qIds.size());
			for (Long qId : qIds) {
				VQuestion vq = vquestions.get(qId);
				if (vq.getType() != Type.COMPOSITE) {
					vq.setInQuestionCar(carQuestions.contains(vq.getId()));
					qs.add(vq);
				}
			}
			vm.put("questions", qs);
			// 知识点以及评价难度
			BigDecimal difficulty = BigDecimal.valueOf(0);
			List<VMetaKnowpoint> metaKnowpoints = Lists.newArrayList();
			Set<Integer> metaKnowpointCodes = Sets.newHashSet();
			List<Integer> metaKnowpointCodeList = Lists.newArrayList();

			List<VKnowledgePoint> knowledgePoints = Lists.newArrayList();
			Set<Long> knowledgePointCodes = Sets.newHashSet();
			List<Long> knowledgePointCodeList = Lists.newArrayList();
			for (VQuestion v : qs) {
				for (VMetaKnowpoint vmkp : v.getMetaKnowpoints()) {
					if (!metaKnowpointCodes.contains(vmkp.getCode())) {
						metaKnowpoints.add(vmkp);
						metaKnowpointCodes.add(vmkp.getCode());
						metaKnowpointCodeList.add(vmkp.getCode());
					}
				}
				for (VKnowledgePoint vkp : v.getNewKnowpoints()) {
					if (!knowledgePointCodes.contains(vkp.getCode())) {
						knowledgePoints.add(vkp);
						knowledgePointCodes.add(vkp.getCode());
						knowledgePointCodeList.add(vkp.getCode());
					}
				}
				difficulty = difficulty.add(BigDecimal.valueOf(v.getDifficulty()));
			}
			vm.put("metaKnowpoints", metaKnowpoints);
			vm.put("metaKnowpointCodes", metaKnowpointCodeList);
			vm.put("knowledgePoints", knowledgePoints);
			vm.put("knowledgePointCodes", knowledgePointCodeList);
			vm.put("difficulty", difficulty.divide(BigDecimal.valueOf(qs.size()), 1, BigDecimal.ROUND_HALF_UP));
		}
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "publishData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publishData(Long sectionCode) {
		ValueMap vm = ValueMap.value();
		if (sectionCode != null) {
			vm.put("name", zyHomeworkService.getHomeworkName(Security.getUserId(), 0, sectionCode, true));
		}
		List<HomeworkClazz> clazzs = hkClassService.listCurrentClazzs(Security.getUserId());
		if (CollectionUtils.isEmpty(clazzs)) {
			vm.put("clazzs", Collections.EMPTY_LIST);
		} else {
			List<VHomeworkClazz> vclazzs = hkClassConvert.to(clazzs);
			List<VHomeworkClazz> emptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			List<VHomeworkClazz> notEmptyClazz = new ArrayList<VHomeworkClazz>(vclazzs.size());
			for (VHomeworkClazz v : vclazzs) {
				if (v.getStudentNum() <= 0) {
					emptyClazz.add(v);
				} else {
					notEmptyClazz.add(v);
				}
			}

			notEmptyClazz.addAll(emptyClazz);
			vm.put("clazzs", notEmptyClazz);
		}
		return new Value(vm);
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "publish", method = { RequestMethod.POST, RequestMethod.GET })
	public Value publish(PublishHomeworkForm form) {
		if (Security.getDeviceType() == DeviceType.IOS
				&& Integer.valueOf(Security.getVersion().replaceAll("\\.", "")) <= 117) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_IOS_UNCORRECT_CLIENT_VERSION));
		}
		if (StringUtils.getJsUnicodeLength(form.getName()) > 40) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_NAME_TOO_LONG));
		}
		Value value = teaHomeworkController.publish2(form);
		operationCacheService.setHomeworkBookSchoolBook(Security.getUserId(), form.getBookId());

		// 用户动作处理
		userActionService.asyncAction(UserAction.PUBLISH_HOMEWORK, Security.getUserId(), null);
		return value;
	}
}
