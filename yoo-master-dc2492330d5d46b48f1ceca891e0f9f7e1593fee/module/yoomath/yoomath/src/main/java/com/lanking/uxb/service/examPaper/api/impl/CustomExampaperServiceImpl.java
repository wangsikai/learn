package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaper;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperStatus;
import com.lanking.cloud.domain.yoomath.examPaper.CustomExampaperType;
import com.lanking.cloud.sdk.data.CursorGetter;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.examPaper.api.CustomExampaperCfgService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperClassService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperService;
import com.lanking.uxb.service.examPaper.api.CustomExampaperStudentService;
import com.lanking.uxb.service.examPaper.ex.CustomExampaperException;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperForm;
import com.lanking.uxb.service.examPaper.form.CustomExamPaperQuery;
import com.lanking.uxb.service.user.api.TeacherService;

/**
 * @see CustomExampaperService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class CustomExampaperServiceImpl implements CustomExampaperService {
	@Autowired
	@Qualifier("CustomExampaperRepo")
	private Repo<CustomExampaper, Long> repo;
	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	private Repo<HomeworkStudentClazz, Long> clazzRepo;

	@Autowired
	private CustomExampaperClassService cepClassService;
	@Autowired
	private CustomExampaperStudentService cepStudentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private CustomExampaperCfgService customExampaperCfgService;

	@Override
	public CustomExampaper get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, CustomExampaper> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	@Transactional
	public List<HomeworkStudentClazz> open(long id, Collection<Long> classIds) {
		CustomExampaper paper = this.get(id);

		List<HomeworkStudentClazz> studentClazzList = Lists.newArrayList();
		if (paper.getType() == CustomExampaperType.SMART) {
			long classId = cepClassService.findByPaper(id).get(0);
			studentClazzList.addAll(clazzRepo.find("$zyListStudents",
					Params.param("classId", classId).put("limit", Long.MAX_VALUE)).list());
		} else {
			for (Long classId : classIds) {
				cepClassService.create(id, classId);
				studentClazzList.addAll(clazzRepo.find("$zyListStudents",
						Params.param("classId", classId).put("limit", Long.MAX_VALUE)).list());
			}
		}

		paper.setStatus(CustomExampaperStatus.OPEN);
		paper.setOpenAt(new Date());
		repo.save(paper);

		// 发放给学生处理
		cepStudentService.open(studentClazzList, id);

		return studentClazzList;
	}

	@Override
	@Transactional
	public CustomExampaper createCustomExamPaper(CustomExamPaperForm form) throws CustomExampaperException {
		Teacher teacher = (Teacher) teacherService.getUser(form.getTeachId());
		Date date = new Date();
		CustomExampaper customExampaper = new CustomExampaper();
		// 如果是保存
		if (form.getId() != null) {
			customExampaper = this.get(form.getId());
			customExampaper.setUpdateAt(date);
		} else {
			customExampaper.setCreateAt(date);
			customExampaper.setUpdateAt(date);
		}
		if (form.getStatus() == CustomExampaperStatus.ENABLED) {
			customExampaper.setEnableAt(date);
		}
		customExampaper.setStatus(form.getStatus());
		customExampaper.setName(form.getTitle());
		customExampaper.setCreateId(teacher.getId());
		customExampaper.setDifficulty(form.getDifficulty());
		customExampaper.setName(form.getTitle());
		// 可以后台累计 可传值
		int questionCount = form.getSingleQuestions().size() + form.getFillQuestions().size()
				+ form.getAnswerQuestions().size();
		customExampaper.setQuestionCount(questionCount);
		customExampaper.setScore(form.getScore());
		customExampaper.setTime(form.getTime());
		// 教材相关
		customExampaper.setTextbookCategoryCode(teacher.getTextbookCategoryCode());
		customExampaper.setTextbookCode(teacher.getTextbookCode());
		customExampaper.setSubjectCode(teacher.getSubjectCode());
		customExampaper.setPhaseCode(teacher.getPhaseCode());
		customExampaper.setType(form.getType());
		// 存储组卷
		repo.save(customExampaper);
		// 设置班级
		if (form.getId() == null && form.getClassId() != null && form.getType() == CustomExampaperType.SMART) {
			cepClassService.create(customExampaper.getId(), form.getClassId());
		}
		// 试卷分值
		form.getCfg().setCustomExampaperId(customExampaper.getId());
		customExampaperCfgService.saveCustomExampaperCfg(form.getCfg());
		return customExampaper;
	}

	@Override
	public Page<CustomExampaper> queryCustomExampapers(CustomExamPaperQuery query, Pageable pageable) {
		Params params = Params.param("createId", query.getTeachId());
		if (null != query.getStatus()) {
			params.put("status", query.getStatus().getValue());
		} else {
			params.put("status", 9);
		}
		if (StringUtils.isNotBlank(query.getKey())) {
			params.put("key", "%" + query.getKey() + "%");
		}
		return repo.find("$queryCustomExampapers", params).fetch(pageable);
	}

	@Override
	public CursorPage<Date, CustomExampaper> queryCustomExampapers(long teacherId, CursorPageable<Date> pageable) {
		return repo.find("$queryCustomExampapersByCursor", Params.param("createId", teacherId)).fetch(pageable,
				CustomExampaper.class, new CursorGetter<Date, CustomExampaper>() {
					@Override
					public Date getCursor(CustomExampaper bean) {
						return bean.getUpdateAt();
					}
				});
	}

	@Override
	@Transactional
	public Value deleteCustomExamPaper(Long id) {
		return this.updateStatus(id, CustomExampaperStatus.DELETE);
	}

	@Override
	@Transactional
	public void updateDownloadFlag(long id) {
		CustomExampaper customExampaper = this.get(id);
		if (customExampaper != null) {
			customExampaper.setDownload(true);
			repo.save(customExampaper);
		}
	}

	@Override
	@Transactional
	public Value enabled(long id) {
		return this.updateStatus(id, CustomExampaperStatus.ENABLED);
	}

	@Transactional
	public Value updateStatus(long id, CustomExampaperStatus status) {
		Date date = new Date();
		CustomExampaper customExampaper = this.get(id);
		if (customExampaper.getStatus() == CustomExampaperStatus.DELETE) {
			return new Value(new CustomExampaperException(CustomExampaperException.CUSTOM_EXAMPAPER_DELETE));
		}

		// 删除
		if (status == CustomExampaperStatus.DELETE) {
			if (customExampaper.getStatus() == status) {
				return new Value(new CustomExampaperException(CustomExampaperException.CUSTOM_EXAMPAPER_DELETE));
			}
			customExampaper.setDeleteAt(date);
		}
		// 正式
		if (status == CustomExampaperStatus.ENABLED) {
			if (customExampaper.getStatus() == status) {
				return new Value(new CustomExampaperException(CustomExampaperException.CUSTOM_EXAMPAPER_OPEN));
			}
			customExampaper.setEnableAt(date);
		}
		customExampaper.setUpdateAt(date);
		customExampaper.setStatus(status);
		repo.save(customExampaper);
		return new Value();
	}

	@Override
	public long countAllCustomExampapers(long createId) {
		return repo.find("$countAllCustomExampapers", Params.param("createId", createId)).count();
	}
}
