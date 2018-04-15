package com.lanking.uxb.service.web.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.QuestionWordMLData;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.school.UserSchoolBook;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.api.QuestionWordMLService;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.web.api.ZyTeaHomeworkBookExportService;
import com.lanking.uxb.service.zuoye.api.ZyBookCatalogService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZySchoolBookService;
import com.lanking.uxb.service.zuoye.api.ZySchoolQuestionService;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyBookVersionConvert;
import com.lanking.uxb.service.zuoye.form.BookChooseForm;
import com.lanking.uxb.service.zuoye.value.VBookCatalog;
import com.lanking.uxb.service.zuoye.value.VBookVersion;

/**
 * U数学 老师布置作业书本相关接口
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年11月3日 下午5:16:26
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/hk")
public class ZyTeaHomeworkBookController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ZyBookService zyBookService;
	@Autowired
	private ZyBookVersionConvert zyBookVersionConvert;
	@Autowired
	private ZyUserSchoolBookService userSchoolBookService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private ZySchoolQuestionService zySchoolQuestionService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ZySchoolBookService schoolBookService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private ZyBookCatalogService bookCatalogService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyTeaHomeworkBookExportService teaHomeworkBookExportService;
	@Autowired
	private QuestionWordMLService questionWordMLService;
	@Autowired
	private TeacherService teacherService;

	/**
	 * 获取校本图书
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "SCHOOL_VIP")
	@RequestMapping(value = "querySchoolBook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySchoolBook(@RequestParam(value = "textCategoryCode") Long textCategoryCode,
			@RequestParam(value = "textbookCode") Long textbookCode, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "50") int pageSize) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		if (teacher.getSchoolId() == null) {
			return new Value();
		}
		Page<BookVersion> bookVersionPage = zyBookService.getSchoolBook(textCategoryCode, textbookCode,
				teacher.getSchoolId(), P.index(page, pageSize));
		List<BookVersion> bookVersionList = bookVersionPage.getItems();
		List<VBookVersion> vbList = zyBookVersionConvert.to(bookVersionList);
		// 改书是否已经被该用户添加过
		Map<Long, Long> userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
		for (VBookVersion vBook : vbList) {
			vBook.setSchoolId(teacher.getSchoolId());
			if (userschoolMap.get(vBook.getBookId()) != null) {
				vBook.setSelected(true);
			} else {
				vBook.setSelected(false);
			}
		}
		VPage<VBookVersion> vbookPage = new VPage<VBookVersion>();
		vbookPage.setTotalPage(bookVersionPage.getPageCount());
		vbookPage.setCurrentPage(page);
		vbookPage.setTotal(bookVersionPage.getTotalCount());
		vbookPage.setItems(vbList);
		return new Value(vbookPage);
	}

	/**
	 * 获取完全开放图书
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryFreeBook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryFreeBook(@RequestParam(value = "textCategoryCode") Integer textCategoryCode,
			@RequestParam(value = "textbookCode") Integer textbookCode, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "50") int pageSize) {
		Page<BookVersion> bookVersionPage = zyBookService.getFreeBook(textCategoryCode, textbookCode,
				P.index(page, pageSize));
		List<BookVersion> bookVersionList = bookVersionPage.getItems();
		List<VBookVersion> vbList = zyBookVersionConvert.to(bookVersionList);
		// 该书是否已经被该用户添加过
		Map<Long, Long> userschoolMap = userSchoolBookService.getUserSelectedBook(Security.getUserId());
		for (VBookVersion vBook : vbList) {
			vBook.setSchoolId(null);
			if (userschoolMap.get(vBook.getBookId()) != null) {
				vBook.setSelected(true);
			} else {
				vBook.setSelected(false);
			}
		}
		VPage<VBookVersion> vbookPage = new VPage<VBookVersion>();
		vbookPage.setTotalPage(bookVersionPage.getPageCount());
		vbookPage.setCurrentPage(page);
		vbookPage.setTotal(bookVersionPage.getTotalCount());
		vbookPage.setItems(vbList);
		return new Value(vbookPage);
	}

	/**
	 * 用户绑定学校信息
	 * 
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "userBindSchool", method = { RequestMethod.POST, RequestMethod.GET })
	public Value userBindSchool() {
		Map<String, Object> data = new HashMap<String, Object>(3);
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType != MemberType.SCHOOL_VIP) {
			data.put("isBindSchool", false);
		} else {
			Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
			if (teacher.getSchoolId() == null) {
				data.put("isBindSchool", false);
			} else {
				Integer schoolBookCount = schoolBookService
						.getSchoolBookByStatus(teacher.getSchoolId(), Status.ENABLED);
				data.put("schoolOrder", schoolBookCount > 0);
				data.put("isBindSchool", true);
				data.put("school", schoolConvert.get(teacher.getSchoolId()));
			}
		}

		return new Value(data);
	}

	/**
	 * 教师选择教辅图书
	 * 
	 * @param bookFromList
	 *            页面上老师选择的书本 ID 和schoolId 集合
	 * @param delBookIds
	 *            页面上原来老师选择，现在被取消掉的bookId集合
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "chooseBook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value chooseBook(String bookFromList, String delBookIds,
			@RequestParam(value = "textCategoryCode") Integer textCategoryCode,
			@RequestParam(value = "textbookCode") Integer textbookCode) {

		List<BookChooseForm> bookFromList2 = JSON.parseArray(bookFromList, BookChooseForm.class);
		List<Long> delBookIds2 = JSONObject.parseArray(delBookIds, Long.class);
		if (bookFromList2.size() > 6) {
			return new Value(new IllegalArgException());
		}
		if (CollectionUtils.isNotEmpty(delBookIds2)) {
			userSchoolBookService.changeBookStatus(delBookIds2, Security.getUserId(), Status.DISABLED);
			homeworkClassService.resetBook(delBookIds2, Security.getUserId());
		}
		List<Long> bookIds = Lists.newArrayList();
		Map<Long, Long> bookFromMap = Maps.newHashMap();
		// 获取书本ID集合
		for (BookChooseForm form : bookFromList2) {
			bookIds.add(form.getBookId());
		}
		List<UserSchoolBook> usbList = Lists.newArrayList();
		if (!bookIds.isEmpty()) {
			usbList = userSchoolBookService.existBook(bookIds, Security.getUserId());
		}
		List<Long> disableBookList = Lists.newArrayList();
		List<Long> enableBookList = Lists.newArrayList();
		// 判断书本之前知否被老师选择过（可能由于后台操作导致状态变更）
		if (CollectionUtils.isNotEmpty(usbList)) {
			for (UserSchoolBook userSchoolBook : usbList) {
				if (userSchoolBook.getStatus().equals(Status.ENABLED)) {
					enableBookList.add(userSchoolBook.getBookId());
				} else if (userSchoolBook.getStatus().equals(Status.DISABLED)) {
					disableBookList.add(userSchoolBook.getBookId());
				}
			}
		}
		// 获取从没有被用户选择过的书本的 书本ID 和学校的Map
		for (BookChooseForm form : bookFromList2) {
			if (!disableBookList.contains(form.getBookId())) {
				bookFromMap.put(form.getBookId(), form.getSchoolId() == null ? 0 : form.getSchoolId());
			}
		}
		// 移除已经存在的有效的
		for (Long long1 : enableBookList) {
			bookFromMap.remove(long1);
		}
		if (CollectionUtils.isNotEmpty(disableBookList)) {
			userSchoolBookService.changeBookStatus(disableBookList, Security.getUserId(), Status.ENABLED);
		}
		List<String> unionIds = Lists.newArrayList();
		// bookFromMap构造schooId和BookId 为key 的map，用于查询schoolbookId
		for (Long long1 : bookFromMap.keySet()) {
			if (bookFromMap.get(long1) != null) {
				unionIds.add(bookFromMap.get(long1).toString() + long1.toString());
			}
		}
		Map<Long, Long> bookMap = Maps.newHashMap();
		// 选择的校本图书之前没有被选择过
		if (!unionIds.isEmpty()) {
			bookMap = schoolBookService.findBySchoolAndBooK(unionIds);
			userSchoolBookService.save(bookMap, Security.getUserId());
		}
		// 选择的免费图书之前没有被选择过
		if (!bookMap.isEmpty()) {
			for (Long key : bookMap.keySet()) {
				if (bookFromMap.keySet().contains(key)) {
					bookFromMap.remove(key);
				}
			}
		}
		userSchoolBookService.save(bookFromMap, Security.getUserId());
		List<BookVersion> vbList = zyBookService.getUserBookList(textCategoryCode, textbookCode, Security.getUserId());
		return new Value(zyBookVersionConvert.to(vbList));
	}

	/**
	 * 获取用户选择过的书本列表
	 *
	 * 2.6.0 增加校级会员过滤
	 * 
	 * @param textCategoryCode
	 *            版本code
	 * @param textbookCode
	 *            教材code
	 * @return
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "bookChooseList", method = { RequestMethod.POST })
	public Value bookChooseList(@RequestParam(value = "textCategoryCode") Integer textCategoryCode,
			@RequestParam(value = "textbookCode") Integer textbookCode) {
		long userId = Security.getUserId();
		MemberType memberType = SecurityContext.getMemberType();
		List<BookVersion> vbList = null;
		if (memberType == MemberType.SCHOOL_VIP) {
			vbList = zyBookService.getUserBookList(textCategoryCode, textbookCode, userId);
		} else {
			vbList = zyBookService.getUserFreeBookList(textCategoryCode, textbookCode, userId);
		}
		return new Value(zyBookVersionConvert.to(vbList));
	}

	/**
	 * 获取书本章节树结构
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "bookSectionTree", method = { RequestMethod.POST })
	public Value bookSectionTree(long bookVersionId) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<VBookCatalog> vs = bookCatalogConvert.to(zyBookService.getBookCatalogs(bookVersionId));
		data.put("sections", bookCatalogConvert.assemblySectionTree(vs));
		return new Value(data);
	}

	/**
	 * 导出OFFICE WORD试题文件.
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "exportword")
	public void exportWordFile(HttpServletRequest request, HttpServletResponse response, String host, Long catalogId) {
		if (catalogId == null) {
			return;
		}
		BookCatalog catalog = bookCatalogService.get(catalogId);
		if (null == catalog) {
			return;
		}

		BookVersion bookVersion = zyBookService.getBookVersion(catalog.getBookVersionId());
		List<Long> qIds = zyBookService.listQuestions(catalogId);
		Map<Long, Question> questionMap = questionService.mget(qIds);
		List<Question> questions = new ArrayList<Question>(questionMap.size());

		StringBuffer hashString = new StringBuffer(); // 题目发生变化，需要重新生成文档
		for (Long id : qIds) {
			Question question = questionMap.get(id);
			if (question != null) {
				hashString.append(id).append(question.getUpdateAt() == null ? 0 : question.getUpdateAt().getTime());
				questions.add(questionMap.get(id));
			}
		}
		String cacheFileName = "h" + hashString.toString().hashCode();

		// 获取文档存储路径
		String destDir = new StringBuffer(Env.getString("word.file.store.path")).append("/tea-homework-book/")
				.append(bookVersion.getBookId()).append("/").append(catalogId).toString();
		File file = new File(destDir + "/" + cacheFileName + ".docx");
		OutputStream os = null;
		FileInputStream inputStream = null;
		try {
			if (!file.exists()) {
				// 获取习题WordML缓存
				Map<Long, QuestionWordMLData> questionWordMLDataMap = questionWordMLService.mget(qIds);

				file = teaHomeworkBookExportService.export(destDir, host, cacheFileName, catalog.getName(), questions,
						questionWordMLDataMap);
			}

			String filename = new String(catalog.getName().getBytes(Charsets.UTF_8), Charsets.ISO_8859_1);
			String agent = request.getHeader("User-Agent").toLowerCase();
			if (agent.indexOf("msie") > 0 || agent.indexOf("trident") > 0 || agent.indexOf("edge") > 0) {
				filename = URLEncoder.encode(catalog.getName(), "UTF-8").replace("+", " ");
			}

			response.setHeader("Content-Disposition", "inline; filename=\"" + filename + ".docx\"");
			response.setHeader("Content-Type", "application/x-zip-compressed; charset=UTF-8");

			os = response.getOutputStream();
			inputStream = new FileInputStream(file);
			ByteArrayOutputStream bops = new ByteArrayOutputStream();
			int data = -1;
			while ((data = inputStream.read()) != -1) {
				bops.write(data);
			}
			os.write(bops.toByteArray());
			os.flush();
		} catch (Exception e) {
			logger.error("export word fail ", e);
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
	}
}
