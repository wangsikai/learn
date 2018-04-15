package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.ResourceCategory;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookQuestion;
import com.lanking.cloud.domain.common.resource.book.BookQuestionCategory;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.fallible.form.TeaFallibleFilterForm.DifficultyType;
import com.lanking.uxb.service.homework.form.ChooseBookForm;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkBookController;
import com.lanking.uxb.service.zuoye.api.ZyBookCatalogService;
import com.lanking.uxb.service.zuoye.api.ZyBookQuestionCategoryService;
import com.lanking.uxb.service.zuoye.api.ZyBookQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;
import com.lanking.uxb.service.zuoye.value.VBookVersion;

@ApiAllowed
@RestController
@RequestMapping("zy/m/t/hk/book/2")
public class ZyMTeaHomeworkBook2Controller {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
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
	private ZyTeaHomeworkBookController teaHomeworkBookController;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ZyUserSchoolBookService userSchoolBookService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyBookCatalogService bookCatalogService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ZyQuestionCarService questionCarService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private ZyBookQuestionCategoryService zyBookQuestionCategoryService;
	@Autowired
	private ZyBookQuestionService zyBookQuestionService;

	/**
	 * 选择教材和常用教辅首页
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(Integer textbookCode) {

		Map<String, Object> data = new HashMap<String, Object>();

		// 当前用户的会员类型
		data.put("memberType", SecurityContext.getMemberType());
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());

		// 教材版本
		TextbookCategory textbookCategory = textbookCategoryService.get(teacher.getTextbookCategoryCode());
		// 教材列表每次都返回
		List<VTextbook> textbooks = tbConvert.to(
				tbService.find(teacher.getPhaseCode(), teacher.getTextbookCategoryCode(), teacher.getSubjectCode()));
		data.put("categoryName", textbookCategory != null ? textbookCategory.getName() : null);
		data.put("textbooks", textbooks);
		// 学校
		if (teacher.getSchoolName() != null) {
			data.put("schoolName", teacher.getSchoolName());
		} else {
			if (teacher.getSchoolId() != null) {
				Optional<School> school = Optional.ofNullable(schoolService.get(teacher.getSchoolId()));
				Optional<String> name = school.map(School::getName);
				data.put("schoolName", name.orElse(null));
			}
		}

		// 新版本首次登录， 从缓存取是否设置过教辅
		Long textbookCodeCache = operationCacheService.getProgress(Security.getUserId(),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		if (textbookCodeCache != null && textbookCode == null) {
			textbookCode = textbookCodeCache.intValue();
		}

		// 设置教材
		if (textbookCode == null) {
			textbookCode = teacher.getTextbookCode();
		}
		data.put("textbookCode", textbookCode);

		// 当前教材下的可选校本图书,类型为同步教辅
		Map<Long, Long> userschoolMap = null;
		if (teacher.getSchoolId() != null && SecurityContext.getMemberType() == MemberType.SCHOOL_VIP) {
			data.put("school", schoolConvert.get(teacher.getSchoolId()));
			List<BookVersion> schoolBooks = zyBookService.getSchoolBookByDifResource(teacher.getTextbookCategoryCode(),
					textbookCode, teacher.getSchoolId(), 501);
			if (CollectionUtils.isNotEmpty(schoolBooks)) {
				if (userschoolMap == null) {
					userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
				}
				List<VBookVersion> vschoolBooks = zyBookVersionConvert.to(schoolBooks);
				for (VBookVersion vbook : vschoolBooks) {
					vbook.setSchoolId(teacher.getSchoolId());
					if (userschoolMap.get(vbook.getBookId()) != null) {
						vbook.setSelected(true);
					} else {
						vbook.setSelected(false);
					}
				}
				data.put("schoolBooks", vschoolBooks);
			} else {
				data.put("schoolBooks", Collections.EMPTY_LIST);
			}
		} else {
			data.put("schoolBooks", Collections.EMPTY_LIST);
		}
		// 当前教材下的可选免费图书,类型为同步教辅
		List<BookVersion> freeBooks = zyBookService.getFreeBookByDifResource(teacher.getTextbookCategoryCode(),
				textbookCode, 501);
		if (CollectionUtils.isNotEmpty(freeBooks)) {
			if (userschoolMap == null) {
				userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
			}
			List<VBookVersion> vfreeBooks = zyBookVersionConvert.to(freeBooks);
			for (VBookVersion vbook : vfreeBooks) {
				vbook.setSchoolId(null);
				if (userschoolMap.get(vbook.getBookId()) != null) {
					vbook.setSelected(true);
				} else {
					vbook.setSelected(false);
				}
			}
			data.put("freeBooks", vfreeBooks);
		} else {
			data.put("freeBooks", Collections.EMPTY_LIST);
		}

		return new Value(data);

	}

	public List<VBookVersion> handleSelect(List<BookVersion> books, Map<Long, Long> userschoolMap) {
		List<VBookVersion> vBooks = zyBookVersionConvert.to(books);
		List<VBookVersion> hasTagList = new ArrayList<VBookVersion>();
		List<VBookVersion> noTagList = new ArrayList<VBookVersion>();
		for (VBookVersion b : vBooks) {
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
		return hasTagList;
	}

	/**
	 * 选择教材和常用教辅首页 -- 新
	 * 
	 * @since 2017.11.7
	 * @param
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index2(Integer textbookCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 当前用户的会员类型
		data.put("memberType", SecurityContext.getMemberType());
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		if (textbookCode == null) {
			textbookCode = teacher.getTextbookCode();
		}
		// 教材版本
		Textbook textbook = textbookService.get(textbookCode);
		// 教材列表每次都返回
		List<VTextbook> textbooks = tbConvert
				.to(tbService.find(teacher.getPhaseCode(), textbook.getCategoryCode(), teacher.getSubjectCode()));
		data.put("categoryName",
				textbook != null ? textbookCategoryService.get(textbook.getCategoryCode()).getName() : null);
		data.put("categoryCode", textbook != null ? textbook.getCategoryCode() : null);
		data.put("textbooks", textbooks);
		// 学校
		if (teacher.getSchoolName() != null) {
			data.put("schoolName", teacher.getSchoolName());
		} else {
			if (teacher.getSchoolId() != null) {
				Optional<School> school = Optional.ofNullable(schoolService.get(teacher.getSchoolId()));
				Optional<String> name = school.map(School::getName);
				data.put("schoolName", name.orElse(null));
			}
		}

		// 新版本首次登录， 从缓存取是否设置过教辅
		Long textbookCodeCache = operationCacheService.getProgress(Security.getUserId(),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		if (textbookCodeCache != null && textbookCode == null) {
			textbookCode = textbookCodeCache.intValue();
		}

		// 设置教材
		if (textbookCode == null) {
			textbookCode = teacher.getTextbookCode();
		}
		data.put("textbookCode", textbookCode);

		// 当前教材下的可选校本图书,类型为同步教辅
		Map<Long, Long> userschoolMap = null;
		if (teacher.getSchoolId() != null && SecurityContext.getMemberType() == MemberType.SCHOOL_VIP) {
			data.put("school", schoolConvert.get(teacher.getSchoolId()));
			List<BookVersion> schoolBooks = zyBookService.getSchoolBookByDifResource(textbook.getCategoryCode(),
					textbookCode, teacher.getSchoolId(), null);
			if (CollectionUtils.isNotEmpty(schoolBooks)) {
				if (userschoolMap == null) {
					userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
				}
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
				data.put("schoolBooks", hasTagList);
			} else {
				data.put("schoolBooks", Collections.EMPTY_LIST);
			}
		} else {
			data.put("schoolBooks", Collections.EMPTY_LIST);
		}
		Map<Integer, List<BookVersion>> bookMap = zyBookService.getBooksMapByDifResource(textbook.getCategoryCode(),
				textbookCode, null);
		List<Map<String, Object>> bookList = new ArrayList<Map<String, Object>>();
		if (CollectionUtils.isNotEmpty(bookMap)) {
			if (userschoolMap == null) {
				userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
			}
			List<Integer> resourceCategoryCodes = new ArrayList<Integer>();
			for (Integer key : bookMap.keySet()) {
				resourceCategoryCodes.add(key);
			}
			Map<Integer, ResourceCategory> rcMap = resourceCategoryService.getcategories(resourceCategoryCodes);
			for (Integer key : bookMap.keySet()) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("name", rcMap.get(key).getName());
				map.put("list", this.handleSelect(bookMap.get(key), userschoolMap));
				bookList.add(map);
			}
			data.put("bookList", bookList);
		}

		// 新增修改教辅书页banner获取
		BannerQuery bannerQuery = new BannerQuery();
		bannerQuery.setApp(YooApp.MATH_TEACHER);
		bannerQuery.setLocation(BannerLocation.RESOURCES);
		List<Banner> bannerList = bannerService.listEnable(bannerQuery);
		data.put("bannerList", bannerConvert.to(bannerList));
		return new Value(data);
	}

	/**
	 * 选择教辅
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param
	 * @return {@link Value}
	 */
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
		if (form.getTextbookCode() == null || (form.getFreeBookIds().size() + form.getSchoolBookIds().size() > 3)) {
			return new Value(new IllegalArgException());
		}
		// 获取当前用户选择的图书
		List<Long> userBookIds = Lists.newArrayList();
		Textbook textbook = tbService.get(form.getTextbookCode());
		int textbookCategoryCode = textbook.getCategoryCode();
		// since教师端 v1.3.0
		// 查用户选择的教辅要查询全部的，不能只查当前textbook下，否则会出现其它textbook下面的教辅没有删掉，切换教材出问题
		List<BookVersion> userBooks = zyBookService.getUserBookList(textbookCategoryCode, null, Security.getUserId());
		for (BookVersion bookVersion : userBooks) {
			userBookIds.add(bookVersion.getBookId());
		}

		List<Map<String, String>> bookFromList = Lists.newArrayList();
		List<Long> bookIdList = Lists.newArrayList();
		for (Long freeBookId : form.getFreeBookIds()) {
			Map<String, String> one = new HashMap<String, String>(1);
			one.put("bookId", String.valueOf(freeBookId));
			bookFromList.add(one);
			userBookIds.remove(freeBookId);
			bookIdList.add(freeBookId);
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
			bookIdList.add(schoolBookId);
		}
		teaHomeworkBookController.chooseBook(JSONObject.toJSONString(bookFromList),
				JSONObject.toJSONString(userBookIds), textbookCategoryCode, textbook.getCode());
		// 添加缓存
		operationCacheService.setProgress(Security.getUserId(), Long.valueOf(form.getTextbookCode()),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		return new Value();
	}

	/**
	 * 选择教辅--新
	 * 
	 * @since yoomath(mobile) V1.3.6
	 * @author wangsenhao
	 * @param
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "choose2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value choose2(ChooseBookForm form) {
		if (form.getBookIds() == null) {
			form.setBookIds(Collections.EMPTY_LIST);
		}
		if (form.getTextbookCode() == null || (form.getBookIds().size() > 6)) {
			return new Value(new IllegalArgException());
		}
		// 获取当前用户选择的图书
		List<Long> userBookIds = Lists.newArrayList();
		Textbook textbook = tbService.get(form.getTextbookCode());
		int textbookCategoryCode = textbook.getCategoryCode();
		// since教师端 v1.3.0
		// 查用户选择的教辅要查询全部的，不能只查当前textbook下，否则会出现其它textbook下面的教辅没有删掉，切换教材出问题
		List<BookVersion> userBooks = zyBookService.getUserBookList(textbookCategoryCode, null, Security.getUserId());
		for (BookVersion bookVersion : userBooks) {
			userBookIds.add(bookVersion.getBookId());
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		String schoolId = "";
		if (teacher.getSchoolId() != null) {
			schoolId = String.valueOf(teacher.getSchoolId());
		}
		List<Map<String, String>> bookFromList = Lists.newArrayList();
		for (Long bookId : form.getBookIds()) {
			Map<String, String> one = new HashMap<String, String>(1);
			one.put("bookId", String.valueOf(bookId));
			if (!userBookIds.contains(bookId) && form.getSchoolBookIds().contains(bookId)) {
				one.put("schoolId", schoolId);
			}
			bookFromList.add(one);
			userBookIds.remove(bookId);
		}
		operationCacheService.setSelectBooks(Security.getUserId(), form.getBookIds());
		teaHomeworkBookController.chooseBook(JSONObject.toJSONString(bookFromList),
				JSONObject.toJSONString(userBookIds), textbookCategoryCode, textbook.getCode());
		// 修改用户的教材版本---2017.11.10
		teacherService.updateCategory(Security.getUserId(), textbookCategoryCode, form.getTextbookCode());
		// 添加缓存
		operationCacheService.setProgress(Security.getUserId(), Long.valueOf(form.getTextbookCode()),
				TeacherOperationCacheService.PROGRESS_TEXTBOOK);
		return new Value();
	}

	/**
	 * 教辅下题目列表
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param bookCatalogId
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions(Long bookCatalogId, Long cursor, @RequestParam(value = "size", defaultValue = "20") int size,
			Type questionType, DifficultyType difficultyType) {
		VCursorPage<VQuestion> vp = new VCursorPage<VQuestion>();
		// 校验
		if (bookCatalogId == null) {
			throw new IllegalArgException();
		}
		if (cursor == null) {
			cursor = 0l;
		}
		Double diff1 = null;
		Double diff2 = null;
		if (difficultyType == DifficultyType.BASIS) {
			diff1 = 0.8;
			diff2 = 1.01;
		} else if (difficultyType == DifficultyType.IMPROVE) {
			diff1 = 0.4;
			diff2 = 0.8;
		} else if (difficultyType == DifficultyType.HARD) {
			diff1 = 0.0;
			diff2 = 0.4;
		}
		// 游标查询
		CursorPage<Long, BookQuestion> questionPage = questionService.queryQuestionByCatalog(bookCatalogId,
				CP.cursor(cursor.longValue() == 0 ? Long.MIN_VALUE : cursor, Math.min(size, 20)), questionType, diff1,
				diff2);
		if (questionPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<Long> questionIds = new ArrayList<Long>(questionPage.getItemSize());
			for (BookQuestion bookQuestion : questionPage.getItems()) {
				questionIds.add(bookQuestion.getQuestionId());
			}
			vp.setCursor(questionPage.getNextCursor());
			List<VQuestion> vqList = this.dealQuestion(questionIds);
			vp.setItems(vqList);
			vp.setTotal(vqList.size());
		}

		return new Value(vp);
	}

	public List<VQuestion> dealQuestion(List<Long> questionIds) {
		Map<Long, Question> questions = questionService.mget(questionIds);

		// 处理重复题
		if (questions.size() > 0) {
			Set<Long> showIds = new HashSet<Long>();
			for (Question question : questions.values()) {
				if (question.getSameShowId() != null && question.getSameShowId() > 0) {
					showIds.add(question.getSameShowId());
				}
			}
			if (showIds.size() > 0) {
				questions.putAll(questionService.mget(showIds));
			}
		}

		QuestionConvertOption questionOption = new QuestionConvertOption();
		questionOption.setInitPhase(false);
		questionOption.setInitSub(false);
		questionOption.setInitSubject(false);
		questionOption.setInitTextbookCategory(false);
		questionOption.setInitQuestionType(false);
		questionOption.setInitMetaKnowpoint(false);
		questionOption.setInitStudentQuestionCount(false);
		questionOption.setCollect(false);

		questionOption.setInitPublishCount(true); // 布置作业次数
		questionOption.setInitQuestionSimilarCount(true); // 相似题个数
		questionOption.setInitQuestionTag(true); // 标签
		questionOption.setInitKnowledgePoint(true); // 知识点
		questionOption.setInitExamination(true); // 考点
		questionOption.setAnalysis(true); // 解析
		questionOption.setAnswer(true); // 答案
		Map<Long, VQuestion> vqMap = questionConvert.to(questions, questionOption);
		List<VQuestion> vqList = new ArrayList<VQuestion>(vqMap.size());

		// 设置题目是否被加入作业篮子
		List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
		Set<Long> returnIds = new HashSet<Long>();
		for (Long questionId : questionIds) {
			Question question = questions.get(questionId);
			VQuestion q = null;

			// 重复题处理
			if (question.getSameShowId() != null && question.getSameShowId() > 0) {
				q = vqMap.get(question.getSameShowId().longValue());
				if (q.getCheckStatus() != CheckStatus.PASS) {
					q = vqMap.get(questionId.longValue());
				}
			} else {
				q = vqMap.get(questionId.longValue());
			}

			if (q.getType() != Type.COMPOSITE) {
				q.setInQuestionCar(carQuestions.contains(q.getId()));
			}
			if (!returnIds.contains(q.getId())) {
				returnIds.add(q.getId());
				vqList.add(q);
			}
		}
		return vqList;
	}

	/**
	 * 教辅下题目列表，新增分类
	 * 
	 * @since yoomath(mobile) V1.3.8
	 * @param bookCatalogId
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "questions2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value questions2(Long bookCatalogId, Long cursor,
			@RequestParam(value = "size", defaultValue = "20") int size, Type questionType,
			DifficultyType difficultyType) {
		List<BookQuestionCategory> bookCategoryList = zyBookQuestionCategoryService.findListByCatalogId(bookCatalogId);
		// 不为空说明存在分类
		if (CollectionUtils.isNotEmpty(bookCategoryList)) {
			// 校验
			if (bookCatalogId == null) {
				throw new IllegalArgException();
			}
			if (cursor == null) {
				cursor = 0l;
			}
			Double diff1 = null;
			Double diff2 = null;
			if (difficultyType == DifficultyType.BASIS) {
				diff1 = 0.8;
				diff2 = 1.01;
			} else if (difficultyType == DifficultyType.IMPROVE) {
				diff1 = 0.4;
				diff2 = 0.8;
			} else if (difficultyType == DifficultyType.HARD) {
				diff1 = 0.0;
				diff2 = 0.4;
			}
			List<Long> categoryIds = new ArrayList<Long>();
			for (BookQuestionCategory b : bookCategoryList) {
				categoryIds.add(b.getId());
			}
			Map<Long, List<Long>> map = zyBookQuestionService.findQuestionByBookCategroyIds(categoryIds, questionType,
					diff1, diff2);

			// 批量转换习题
			Set<Long> questionIds = new HashSet<Long>();
			for (List<Long> qidList : map.values()) {
				questionIds.addAll(qidList);
			}
			Map<Long, Question> questionMap = questionService.mget(questionIds);

			QuestionConvertOption questionOption = new QuestionConvertOption();
			questionOption.setInitPhase(false);
			questionOption.setInitSub(false);
			questionOption.setInitSubject(false);
			questionOption.setInitTextbookCategory(false);
			questionOption.setInitQuestionType(false);
			questionOption.setInitMetaKnowpoint(false);
			questionOption.setInitStudentQuestionCount(false);
			questionOption.setCollect(false);

			questionOption.setInitPublishCount(true); // 布置作业次数
			questionOption.setInitQuestionSimilarCount(true); // 相似题个数
			questionOption.setInitQuestionTag(true); // 标签
			questionOption.setInitKnowledgePoint(true); // 知识点
			questionOption.setInitExamination(true); // 考点
			questionOption.setAnalysis(true); // 解析
			questionOption.setAnswer(true); // 答案
			Map<Long, VQuestion> vquestionMap = questionConvert.to(questionMap, questionOption);

			// 查找重复题
			if (questionMap.size() > 0) {
				Set<Long> showIds = new HashSet<Long>();
				for (Question question : questionMap.values()) {
					if (question.getSameShowId() != null && question.getSameShowId() > 0) {
						showIds.add(question.getSameShowId());
					}
				}
				if (showIds.size() > 0) {
					vquestionMap.putAll(questionConvert.to(questionService.mget(showIds), questionOption));
				}
			}

			// 设置题目是否被加入作业篮子
			List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());

			Map<Long, BookQuestionCategory> categoryMap = zyBookQuestionCategoryService.mgetByCatalogIds(categoryIds);
			List<Map<String, Object>> cMap = new ArrayList<Map<String, Object>>();
			for (Long key : map.keySet()) {
				List<Long> qidList = map.get(key);
				List<VQuestion> vquesstions = new ArrayList<VQuestion>(qidList.size());
				for (Long questionId : qidList) {
					VQuestion vquestion = vquestionMap.get(questionId);
					Question question = questionMap.get(questionId);

					// 重复题处理
					if (question.getSameShowId() != null && question.getSameShowId() > 0) {
						vquestion = vquestionMap.get(question.getSameShowId());
						if (vquestion.getCheckStatus() != CheckStatus.PASS) {
//							vquestion = vquestionMap.get(key);
							vquestion = vquestionMap.get(questionId);
						}
					}

					if (vquestion.getType() != Type.COMPOSITE) {
						vquestion.setInQuestionCar(carQuestions.contains(questionId));
					}

					vquesstions.add(vquestion);
				}

				Map<String, Object> temp = new HashMap<String, Object>();
				temp.put("categoryId", key);
				temp.put("categoryName", categoryMap.get(key).getName());
				temp.put("questionList", vquesstions);
				cMap.add(temp);
			}
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("categoryItems", cMap);

			return new Value(data);
		} else {
			// 没有分类走原来的接口
			Value value = this.questions(bookCatalogId, cursor, size, questionType, difficultyType);

			return value;
		}
	}

	/**
	 * 查询书本的章节
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param sectionCode
	 *            教材的sectionCode
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "bookCatalogs", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bookCatalogs(Long bookVersionId, Long textbookCode, Long sectionCode) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 校验
		if (bookVersionId == null) {
			throw new IllegalArgException();
		}

		List<BookCatalog> catalogList = Lists.newArrayList();

		if (textbookCode != null && sectionCode != null) {
			// 查询
			catalogList = bookCatalogService.listBookCatalog(bookVersionId, textbookCode, sectionCode);
			// 2017.11.2戚元鹏修改需求，显示第一个
			if (CollectionUtils.isNotEmpty(catalogList)) {
				data.put("bookCatalogs", bookCatalogConvert.to(Lists.newArrayList(catalogList.get(0))));
			}

		} else {
			// 查询
			List<VBookCatalog> vs = bookCatalogConvert.to(zyBookService.getBookCatalogs(bookVersionId));
			data.put("bookCatalogs", bookCatalogConvert.assemblySectionTree(vs));

			// catalogList = bookCatalogService.listBookCatalog(bookVersionId);
			// data.put("bookCatalogs",
			// bookCatalogConvert.assemblySectionTree(bookCatalogConvert.to(catalogList)));
		}

		return new Value(data);
	}
}
