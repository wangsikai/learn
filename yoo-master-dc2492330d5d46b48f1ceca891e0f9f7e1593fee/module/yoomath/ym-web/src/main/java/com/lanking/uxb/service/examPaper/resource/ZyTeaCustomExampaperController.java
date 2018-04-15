package com.lanking.uxb.service.examPaper.resource;

import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.CustomExampaperQuestionService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.cache.CustomExampaperStudentNoticeCacheService;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvert;
import com.lanking.uxb.service.examPaper.convert.CustomExampaperConvertOption;
import com.lanking.uxb.service.examPaper.ex.CustomExampaperException;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperForm;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperQuery;
import com.lanking.uxb.service.examPaper.value.VCustomExampaper;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 教师组卷相关接口
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@RestController
@RequestMapping(value = "zy/t/ep")
public class ZyTeaCustomExampaperController {
	private Logger logger = LoggerFactory.getLogger(ZyTeaCustomExampaperController.class);

	@Autowired
	private CustomExampaperService exampaperService;
	@Autowired
	private CustomExampaperStudentNoticeCacheService cacheService;
	@Autowired
	private CustomExampaperQuestionService customExampaperQuestionService;
	@Autowired
	private CustomExampaperConvert customExampaperConvert;
	@Autowired
	@Qualifier("executor")
	private Executor executor;

	/**
	 * 开卷接口（attention: 若此组卷的类型为 SMART 则不需要传班级）
	 *
	 * @param paperId
	 *            组卷id
	 * @param classIds
	 *            班级id列表(SMART不传)
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "open", method = { RequestMethod.GET, RequestMethod.POST })
	public Value open(final long paperId, @RequestParam(value = "classIds") final List<Long> classIds) {
		CustomExampaper paper = exampaperService.get(paperId);
		if (paper == null) {
			return new Value(new IllegalArgException());
		}
		// 若组卷为草稿->开放
		if (paper.getStatus() == CustomExampaperStatus.DRAFT) {
			return new Value(new CustomExampaperException(CustomExampaperException.CUSTOM_EXAMPAPER_OPEN_DRAFT));
		}
		// 若组卷已开放
		if (paper.getStatus() == CustomExampaperStatus.OPEN) {
			return new Value(new CustomExampaperException(CustomExampaperException.CUSTOM_EXAMPAPER_OPENED));
		}
		if (paper.getType() == CustomExampaperType.MANUAL && CollectionUtils.isEmpty(classIds)) {
			return new Value(new IllegalArgException());
		}

		final long nowMillis = System.currentTimeMillis();
		exampaperService.updateStatus(paperId, CustomExampaperStatus.OPEN);
		// 异步将组卷开卷给学生
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					List<HomeworkStudentClazz> students = exampaperService.open(paperId, classIds);
					for (HomeworkStudentClazz s : students) {
						cacheService.update(s.getStudentId(), nowMillis);
					}
				} catch (Exception e) {
					exampaperService.updateStatus(paperId, CustomExampaperStatus.ENABLED);
					logger.error("open paper error paperId: {}", paperId);
				}
			}
		});

		return new Value();
	}

	/**
	 * 创建，保存组卷
	 * 
	 * @param json
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Value create(String json) {
		CustomExamPaperForm customExamPaperForm = JSON.parseObject(json, CustomExamPaperForm.class);
		if (null == customExamPaperForm.getTime() || null == customExamPaperForm.getScore()
				|| null == customExamPaperForm.getTitle()) {
			return new Value(new MissingArgumentException());
		}
		try {
			customExamPaperForm.setTeachId(Security.getUserId());
			// 保存组卷
			CustomExampaper customExampaper = exampaperService.createCustomExamPaper(customExamPaperForm);
			customExamPaperForm.setId(customExampaper.getId());
			// 保存试卷题目 配置 班级 分类 组卷类型
			customExampaperQuestionService.updateCustomExamQuesions(customExamPaperForm);
			return new Value(customExampaper);
		} catch (CustomExampaperException e) {
			return new Value(e);
		}
	}

	/**
	 * 删除组卷
	 * 
	 * @param id
	 *            组卷ID
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public Value deleteQuestionById(Long id, Boolean needCount) {
		try {
			exampaperService.deleteCustomExamPaper(id);
			if (needCount != null && needCount) {
				long allcount = exampaperService.countAllCustomExampapers(Security.getUserId());
				return new Value(allcount);
			}
			return new Value();
		} catch (CustomExampaperException e) {
			return new Value(e);
		}
	}

	/**
	 * 更新组卷状态
	 * 
	 * @param id
	 *            组卷ID
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "enabledExamPaper", method = RequestMethod.POST)
	public Value enabledExamPaperById(Long id) {
		return exampaperService.enabled(id);
	}

	/**
	 * 组卷ID查询组卷详情
	 * 
	 * @param id
	 *            组卷ID
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryQuestions(Long id) {
		CustomExampaper cep = exampaperService.get(id);
		CustomExampaperConvertOption ceco = new CustomExampaperConvertOption();
		ceco.setShowClazz(true);
		ceco.setShowQuestions(true);
		ceco.setShowTopic(true);
		VCustomExampaper vcep = customExampaperConvert.to(cep, ceco);
		return new Value(vcep);
	}

	/**
	 * 通过教师查询组卷
	 * 
	 * @param Teacher
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryExampapers", method = RequestMethod.POST)
	public Value queryExampapers(CustomExamPaperQuery form) {
		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();
		form.setTeachId(Security.getUserId());
		Page<CustomExampaper> page = exampaperService.queryCustomExampapers(form, P.offset(offset, size));
		VPage<VCustomExampaper> vpage = new VPage<VCustomExampaper>();
		vpage.setCurrentPage(form.getPage());
		vpage.setPageSize(form.getPage());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setItems(customExampaperConvert.to(page.getItems()));
		// 组装页面信息
		return new Value(vpage);
	}

	/**
	 * 判断组卷是否已被删除.
	 * 
	 * @param id
	 *            组卷ID
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "checkExampaperDelete", method = RequestMethod.POST)
	public Value checkExampaperDelete(Long id) {
		int isDelete = 0;
		CustomExampaper customExampaper = exampaperService.get(id);
		if (customExampaper == null || customExampaper.getStatus() == CustomExampaperStatus.DELETE) {
			isDelete = 1;
		}
		return new Value(isDelete);
	}
}
