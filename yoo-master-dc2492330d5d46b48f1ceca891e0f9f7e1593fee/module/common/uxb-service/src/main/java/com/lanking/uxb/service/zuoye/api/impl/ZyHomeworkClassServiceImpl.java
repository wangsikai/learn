package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.ClazzFrom;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItem;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItem;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyBookCatalogService;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyUserSchoolBookService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@Transactional(readOnly = true)
@Service
public class ZyHomeworkClassServiceImpl implements ZyHomeworkClassService {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	private static String CODE_PREFIX = "1";
	private static int CODE_LENGTH = 5;
	private static int CODE_TRYTIME = 100;
	// 一个老师最多能开的班级数量
	private static int MAX_CLASS_PER_TEACHER = 5;

	@Autowired
	private ZyBookService bookService;
	@Autowired
	private ZyBookCatalogService bookCatalogService;
	@Autowired
	private UserService userService;
	@Autowired
	private ZyUserSchoolBookService userSchoolBookService;
	@Autowired
	@Qualifier("HomeworkRepo")
	Repo<Homework, Long> homeworkRepo;
	@Autowired
	@Qualifier("StudentHomeworkRepo")
	Repo<StudentHomework, Long> studentHomeworkRepo;
	@Autowired
	@Qualifier("HolidayHomeworkRepo")
	private Repo<HolidayHomework, Long> holidayHomeworkRepo;
	@Autowired
	@Qualifier("HolidayHomeworkItemRepo")
	private Repo<HolidayHomeworkItem, Long> hdHomeworkItemRepo;
	@Autowired
	@Qualifier("HolidayStuHomeworkRepo")
	private Repo<HolidayStuHomework, Long> hdStuHomework;
	@Autowired
	@Qualifier("HolidayStuHomeworkItemRepo")
	private Repo<HolidayStuHomeworkItem, Long> hdStuHomeworkItem;

	public String generateCode() {
		int tryTime = 1;
		String code = CODE_PREFIX + RandomStringUtils.random(CODE_LENGTH, false, true);
		while (codeExist(code)) {
			if (tryTime >= CODE_TRYTIME) {
				code = null;
				break;
			}
			code = CODE_PREFIX + RandomStringUtils.random(CODE_LENGTH, false, true);
			tryTime++;
		}
		return code;
	}

	@PostConstruct
	void init() {
		CODE_PREFIX = Env.getString("class.code.prefix");
		CODE_LENGTH = Env.getInt("class.code.length");
		CODE_TRYTIME = Env.getInt("class.code.trytime");
		MAX_CLASS_PER_TEACHER = Env.getInt("class.max.per_teacher");
	}

	@Override
	public HomeworkClazz get(long id) {
		return homeworkClazzRepo.get(id);
	}

	@Override
	public Map<Long, HomeworkClazz> mget(Collection<Long> ids) {
		return homeworkClazzRepo.mget(ids);
	}

	@Override
	public HomeworkClazz findByCode(String code) {
		return homeworkClazzRepo.find("$zyFindByCode", Params.param("code", code)).get();
	}

	@Override
	public boolean codeExist(String code) {
		return homeworkClazzRepo.find("$zyCountByCode", Params.param("code", code)).count() > 0;
	}

	@Override
	public boolean nameExist(String name, Long teacherId) {
		return homeworkClazzRepo.find("$zyCountByName", Params.param("name", name).put("teacherId", teacherId))
				.count() > 0;
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz create(String name, Long teacherId) {
		return this.createByThird(name, teacherId, null, null);
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz createByThird(String name, Long teacherId, ClazzFrom from, String fromCode) {
		int length = StringUtils.getJsUnicodeLength(name);
		if (length == 0 || length > 40) {
			throw new IllegalArgException();
		}
		if (teacherId != null) {
			long count = currentCount(teacherId);
			if (count >= MAX_CLASS_PER_TEACHER) {
				throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_MAXLIMIT);
			}
		}
		if (nameExist(name, teacherId)) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASSNAME_EXIST);
		}

		HomeworkClazz clazz = new HomeworkClazz();
		String code = generateCode();
		if (code == null) {
			throw new ServerException();
		}
		clazz.setCode(code);
		clazz.setCreateAt(new Date());
		clazz.setStartAt(clazz.getCreateAt());
		clazz.setUpdateAt(clazz.getCreateAt());
		clazz.setName(name);
		clazz.setDescription(name);
		clazz.setStatus(Status.ENABLED);
		clazz.setStudentNum(0);
		clazz.setTeacherId(teacherId);
		clazz.setOriginalTeacherId(teacherId);
		if (from != null) {
			clazz.setClazzFrom(from);
		}
		clazz.setFromCode(fromCode);
		return homeworkClazzRepo.save(clazz);
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz refreshCode(long id, long teacherId) {
		HomeworkClazz clazz = homeworkClazzRepo.get(id);
		if (clazz.getTeacherId() != teacherId) {
			throw new NoPermissionException();
		}
		String code = generateCode();
		if (code == null) {
			throw new ServerException();
		}
		clazz.setCode(code);
		clazz.setUpdateAt(new Date());
		return homeworkClazzRepo.save(clazz);
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz close(long id, long teacherId) {
		HomeworkClazz clazz = homeworkClazzRepo.get(id);
		if (clazz.getTeacherId() != teacherId) {
			throw new NoPermissionException();
		}
		clazz.setUpdateAt(new Date());
		clazz.setCloseAt(clazz.getUpdateAt());
		clazz.setStatus(Status.DISABLED);
		return homeworkClazzRepo.save(clazz);
	}

	@Override
	public Page<HomeworkClazz> query(ZyHomeworkClassQuery query, Pageable page) {
		Params params = Params.param("teacherId", query.getTeacherId());
		if (query.getStatus() != null) {
			params.put("status", query.getStatus().getValue());
		}
		return homeworkClazzRepo.find("$zyQuery", params).fetch(page);
	}

	@Override
	public List<HomeworkClazz> listCurrentClazzs(long teacherId) {
		return homeworkClazzRepo
				.find("$zyQuery", Params.param("teacherId", teacherId).put("status", Status.ENABLED.getValue())).list();
	}

	@Override
	public List<HomeworkClazz> listCurrentAllClazzs(long teacherId) {
		return homeworkClazzRepo.find("$zyQuery", Params.param("teacherId", teacherId)).list();
	}

	@Override
	public List<HomeworkClazz> listHistoryClazzs(long teacherId) {
		return homeworkClazzRepo
				.find("$zyQuery",
						Params.param("teacherId", teacherId).put("status", Status.DISABLED.getValue()).put("size", 30))
				.list();
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(long id, long teacherId) {
		HomeworkClazz clazz = homeworkClazzRepo.get(id);
		if (clazz.getTeacherId() != teacherId) {
			throw new NoPermissionException();
		}
		if (clazz.getStatus() != Status.DELETED) {
			clazz.setDeleteAt(new Date());
			clazz.setUpdateAt(clazz.getDeleteAt());
			clazz.setStatus(Status.DELETED);
			homeworkClazzRepo.save(clazz);

			// v1.3.0 戚元鹏定 删除班级删除班级下所有作业
			List<Homework> homeworks = homeworkRepo
					.find("$queryAllHomeworkByClazz", Params.param("homeworkClassId", clazz.getId())).list();
			if (!CollectionUtils.isEmpty(homeworks)) {
				// 更新作业表
				int count = homeworkRepo.execute("$deleteAllHomeworkByClazz",
						Params.param("homeworkClassId", clazz.getId()));
				// 若数据不存在，则不更新学生作业表
				List<Long> homeworkids = new ArrayList<>();
				for (Homework homework : homeworks) {
					homeworkids.add(homework.getId());
				}
				if (count > 0) {
					// 更新学生作业表
					studentHomeworkRepo.execute("$deleteAllStuHomeworkByClazz",
							Params.param("homeworkids", homeworkids));
				}
			}
			// 更新假期作业表
			List<HolidayHomework> hList = holidayHomeworkRepo
					.find("$queryAllHolidayHomeworkByClazz",
					Params.param("homeworkClassId", clazz.getId())).list();
			if (!CollectionUtils.isEmpty(hList)) {
				int count = holidayHomeworkRepo.execute("$deleteAllHolidayHomeworkByClazz",
						Params.param("homeworkClassId", clazz.getId()));
				// 更新假期作业项表
				int count2 = hdHomeworkItemRepo.execute("$deleteHolidayHomeworkItemByClazz",
						Params.param("homeworkClassId", clazz.getId()));

				// 若数据不存在，则不更新学生作业表
				List<Long> holidayids = new ArrayList<>();
				for (HolidayHomework holidayHomework : hList) {
					holidayids.add(holidayHomework.getId());
				}
				if (count > 0 && count2 > 0) {
					// 更新学生作业表
					hdStuHomework.execute("$deleteStuHolidayHomeworkByClazz", Params.param("holidayids", holidayids));
					// 更新学生作业项表
					hdStuHomeworkItem.execute("$deleteStuHolidayHomeworkItemByClazz",
							Params.param("holidayids", holidayids));
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz updateName(String name, long classId, long teacherId) {
		HomeworkClazz clazz = homeworkClazzRepo.get(classId);
		if (clazz == null) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST);
		}
		if (clazz.getTeacherId() != teacherId) {
			throw new NoPermissionException();
		}
		if (clazz.getName().equals(name)) {
			return clazz;
		}
		if (nameExist(name, teacherId)) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASSNAME_EXIST);
		}
		clazz.setName(name);
		// 创建班级时，name与description一样，这里修改时保持与前面一致
		clazz.setDescription(name);
		clazz.setUpdateAt(new Date());
		homeworkClazzRepo.save(clazz);
		return clazz;
	}

	@Override
	@Transactional
	public void incrStudentNum(long id, int delta) {
		homeworkClazzRepo.execute("$zyIncrStudentNum",
				Params.param("updateAt", new Date()).put("id", id).put("delta", delta));
	}

	@Override
	public long historyCount(long teacherId) {
		return homeworkClazzRepo.find("$zyHistoryCount", Params.param("teacherId", teacherId)).count();
	}

	@Override
	public long currentCount(long teacherId) {
		return homeworkClazzRepo.find("$zyCurrentCount", Params.param("teacherId", teacherId)).count();
	}

	@Transactional
	@Override
	public int lock(long classId, long teacherId) {
		return homeworkClazzRepo.execute("$zyLock", Params.param("id", classId).put("teacherId", teacherId));
	}

	@Transactional
	@Override
	public int unlock(long classId, long teacherId) {
		return homeworkClazzRepo.execute("$zyUnlock", Params.param("id", classId).put("teacherId", teacherId));
	}

	@Override
	public Map<String, List<HomeworkClazz>> findTeaUsedByFromCode(ClazzFrom from, Collection<String> codes) {
		Map<String, List<HomeworkClazz>> map = new HashMap<String, List<HomeworkClazz>>(codes.size());
		Params params = Params.param("frm", from.getValue()).put("codes", codes);
		List<HomeworkClazz> list = homeworkClazzRepo.find("$findByFromCode", params).list();
		for (HomeworkClazz clazz : list) {
			List<HomeworkClazz> hcs = map.get(clazz.getFromCode());
			if (hcs == null) {
				hcs = new ArrayList<HomeworkClazz>();
				map.put(clazz.getFromCode(), hcs);
			}
			hcs.add(clazz);
		}
		return map;
	}

	@Override
	@Transactional
	public void updateTeacherByFromCode(ClazzFrom from, String fromCode, Long teacherId) {
		Params params = Params.param("frm", from.getValue()).put("fromCode", fromCode).put("teacherId", teacherId);
		homeworkClazzRepo.execute("$updateTeacherByFromCode", params);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map<Long, Double> getMapAVGComplete(Collection<Long> classIds) {
		if (CollectionUtils.isEmpty(classIds)) {
			return Maps.newHashMap();
		}
		List<Map> list = homeworkClazzRepo.find("$getMapAVGComplete", Params.param("clazzIds", classIds))
				.list(Map.class);
		Map<Long, Double> returnMap = new HashMap<Long, Double>(list.size());
		for (Map map : list) {
			returnMap.put(Long.parseLong(map.get("cid").toString()),
					map.get("avgs") == null ? 0.0 : Double.parseDouble(map.get("avgs").toString()));
		}
		return returnMap;
	}

	@Override
	@Transactional
	public void setBook(long classId, long bookVersionId, long bookCataId, long userId) {
		BookVersion bookVersion = bookService.getBookVersion(bookVersionId);
		if (bookVersion == null) {
			throw new IllegalArgException();
		}

		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, userId);
		if (!bookService.existBook(teacher.getTextbookCategoryCode(), userId, bookVersionId)) {
			throw new IllegalArgException();
		}

		HomeworkClazz homeworkClazz = homeworkClazzRepo.get(classId);
		if (homeworkClazz == null || homeworkClazz.getStatus() != Status.ENABLED) {
			throw new IllegalArgException();
		}

		homeworkClazz.setBookCataId(bookCataId);
		homeworkClazz.setBookVersionId(bookVersionId);

		homeworkClazzRepo.save(homeworkClazz);

		List<BookVersion> books = bookService.getUserBookList(bookVersion.getTextbookCategoryCode(),
				bookVersion.getTextbookCode(), userId);

		Map<Long, Long> saveFromMap = new HashMap<Long, Long>(1);

		if (CollectionUtils.isEmpty(books)) {
			saveFromMap.put(bookVersion.getBookId(), bookService.getBook(bookVersion.getBookId()).getSchoolId());
			userSchoolBookService.save(saveFromMap, userId);
		} else {
			boolean alreadyHaveBook = false;
			for (BookVersion b : books) {
				if (b.getId().equals(bookVersion.getId())) {
					alreadyHaveBook = true;
					break;
				}
			}

			if (!alreadyHaveBook) {
				if (books.size() >= 6) {
					BookVersion lastBook = books.get(0);
					userSchoolBookService.changeBookStatus(Lists.<Long>newArrayList(lastBook.getBookId()), userId,
							Status.DISABLED);

				}

				saveFromMap.put(bookVersion.getBookId(), bookService.getBook(bookVersion.getBookId()).getSchoolId());
				userSchoolBookService.save(saveFromMap, userId);
			}

		}
	}

	@Override
	@Transactional
	public void setBook(Collection<Long> classIds, long bookVersionId, long bookCataId, long userId) {
		BookVersion bookVersion = bookService.getBookVersion(bookVersionId);
		if (bookVersion == null) {
			throw new IllegalArgException();
		}

		if (CollectionUtils.isEmpty(classIds)) {
			throw new IllegalArgException();
		}

		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, userId);
		if (!bookService.existBook(teacher.getTextbookCategoryCode(), userId, bookVersionId)) {
			throw new IllegalArgException();
		}

		Map<Long, Long> saveFromMap = new HashMap<Long, Long>(1);

		List<BookVersion> books = bookService.getUserBookList(bookVersion.getTextbookCategoryCode(),
				bookVersion.getTextbookCode(), userId);

		boolean alreadyHaveBook = false;
		for (BookVersion b : books) {
			if (b.getId().equals(bookVersion.getId())) {
				alreadyHaveBook = true;
				break;
			}
		}

		if (!alreadyHaveBook) {
			if (books.size() >= 6) {
				BookVersion lastBook = books.get(0);
				userSchoolBookService.changeBookStatus(Lists.<Long>newArrayList(lastBook.getBookId()), userId,
						Status.DISABLED);

			}
			saveFromMap.put(bookVersion.getBookId(), bookService.getBook(bookVersion.getBookId()).getSchoolId());
			userSchoolBookService.save(saveFromMap, userId);
		}

		homeworkClazzRepo.execute("$updateBookProcess",
				Params.param("classIds", classIds).put("bookVersionId", bookVersionId).put("bookCataId", bookCataId));
	}

	@Override
	@Transactional
	public void clearBookSetting(long userId) {
		homeworkClazzRepo.execute("$clearBookSetting", Params.param("userId", userId));
	}

	@Override
	@Transactional
	public void resetBook(Collection<Long> bookIds, long userId) {
		homeworkClazzRepo.execute("$resetBookSetting", Params.param("bookIds", bookIds).put("userId", userId));
	}

	@Override
	public List<HomeworkClazz> mgetListEnableClazz(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_LIST;
		}
		return homeworkClazzRepo.find("$findEnableClass", Params.param("ids", ids)).list();
	}

	@Override
	public Map<Long, HomeworkClazz> mgetEnableClazz(Collection<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.EMPTY_MAP;
		}
		List<HomeworkClazz> clazzs = homeworkClazzRepo.find("$findEnableClass", Params.param("ids", ids)).list();
		Map<Long, HomeworkClazz> retMap = new HashMap<Long, HomeworkClazz>(clazzs.size());
		for (HomeworkClazz c : clazzs) {
			retMap.put(c.getId(), c);
		}

		return retMap;
	}

	@Override
	public CursorPage<Long, HomeworkClazz> findByChannel(long userId, String key, CursorPageable<Long> cursorPageable) {
		Params params = Params.param("userId", userId);
		if (StringUtils.isNotEmpty(key)) {
			params.put("key", "%" + key + "%");
		}
		return homeworkClazzRepo.find("$zyFindUserChannelSchoolClass", params).fetch(cursorPageable);
	}

	@Override
	public List<HomeworkClazz> findByCodeOrMobile(String key) {
		if (StringUtils.isEmpty(key)) {
			return Collections.EMPTY_LIST;
		}
		Params params = Params.param();
		params.put("key", key);

		return homeworkClazzRepo.find("$zyFindByCodeOrMobile", params).list();
	}

	@Override
	public List<HomeworkClazz> mgetList(Collection<Long> ids) {
		return homeworkClazzRepo.mgetList(ids);
	}

	@Override
	@Transactional
	public int needConfirm(long classId, long teacherId) {
		return homeworkClazzRepo.execute("$zyNeedConfirm", Params.param("id", classId).put("teacherId", teacherId));
	}

	@Override
	@Transactional
	public int notNeedConfirm(long classId, long teacherId) {
		return homeworkClazzRepo.execute("$zyNotNeedConfirm", Params.param("id", classId).put("teacherId", teacherId));
	}

	@Override
	public List<HomeworkClazz> listClazzsOrderByStuNum(long teacherId) {
		return homeworkClazzRepo.find("$listClazzsOrderByStuNum",
				Params.param("teacherId", teacherId).put("status", Status.ENABLED.getValue())).list();
	}

	@Override
	public long allCountByTeacherId(long teacherId, Status status) {
		Params params = Params.param();
		params.put("teacherId", teacherId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return homeworkClazzRepo.find("$zyAllCount", params).count();
	}

	@Override
	@Transactional(readOnly = false)
	public HomeworkClazz createByMobile(String name, Long teacherId, int schoolYear) {
		int length = StringUtils.getJsUnicodeLength(name);
		if (length == 0 || length > 40) {
			throw new IllegalArgException();
		}
		if (teacherId != null) {
			long count = currentCount(teacherId);
			if (count >= MAX_CLASS_PER_TEACHER) {
				throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_MAXLIMIT);
			}
		}
		if (nameExist(name, teacherId)) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASSNAME_EXIST);
		}

		HomeworkClazz clazz = new HomeworkClazz();
		String code = generateCode();
		if (code == null) {
			throw new ServerException();
		}
		clazz.setCode(code);
		clazz.setCreateAt(new Date());
		clazz.setStartAt(clazz.getCreateAt());
		clazz.setUpdateAt(clazz.getCreateAt());
		clazz.setName(name);
		clazz.setDescription(name);
		clazz.setStatus(Status.ENABLED);
		clazz.setStudentNum(0);
		clazz.setTeacherId(teacherId);
		clazz.setOriginalTeacherId(teacherId);
		clazz.setSchoolYear(schoolYear);
		return homeworkClazzRepo.save(clazz);
	}
}
