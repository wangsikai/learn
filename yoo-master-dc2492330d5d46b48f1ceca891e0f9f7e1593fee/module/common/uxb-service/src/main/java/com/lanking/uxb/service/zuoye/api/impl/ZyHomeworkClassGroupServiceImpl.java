package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.HomeworkClazzGroupForm;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Service
@Transactional(readOnly = true)
public class ZyHomeworkClassGroupServiceImpl implements ZyHomeworkClassGroupService {
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> clazzRepo;
	@Autowired
	@Qualifier("HomeworkClazzGroupRepo")
	Repo<HomeworkClazzGroup, Long> clazzGroupRepo;

	@Autowired
	private ZyHomeworkStudentClazzService studentClazzService;
	@Autowired
	private ZyHomeworkClassGroupStudentService groupStudentService;

	@Override
	@Transactional
	@SuppressWarnings("unchecked")
	public List<Long> create(HomeworkClazzGroupForm form) {
		if (CollectionUtils.isEmpty(form.getStudentIds()) || form.getClassId() <= 0 || form.getTeacherId() <= 0
				|| StringUtils.isBlank(form.getName()) || StringUtils.getJsUnicodeLength(form.getName()) > 20) {
			throw new IllegalArgException();
		}
		HomeworkClazz homeworkClazz = clazzRepo.get(form.getClassId());
		if (homeworkClazz == null || homeworkClazz.getStatus() != Status.ENABLED) {
			throw new IllegalArgException();
		}
		if (!homeworkClazz.getTeacherId().equals(form.getTeacherId())) {
			throw new NoPermissionException();
		}

		// 判断这些学生是否还在此班级中
		Map<Long, HomeworkStudentClazz> clazzMap = studentClazzService.findByStudentIdsAndClassId(form.getStudentIds(),
				form.getClassId());

		List<Long> removedStus = new ArrayList<Long>(form.getStudentIds().size());
		for (Long stuId : form.getStudentIds()) {
			if (clazzMap.get(stuId) == null) {
				removedStus.add(stuId);
			}
		}

		// 说明添加分组的时候，有学生被移除出班级了。
		if (CollectionUtils.isNotEmpty(removedStus)) {
			return removedStus;
		}

		// 判断当前小组名是否已经在这个班级里存在了
		if (isExist(form.getName(), form.getClassId())) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS);
		}

		// 判断教师是否已经在此班级下面创建了超过5个分组了
		if (countClassGroupNum(form.getClassId()) >= 5) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_MAXLIMIT);
		}

		// 移除此次添加的学生以前所在分组
		List<HomeworkClazzGroupStudent> groupStudents = groupStudentService.findByStusAndClass(form.getStudentIds(),
				form.getClassId());
		groupStudentService.removeStudents(form.getStudentIds(), form.getClassId());

		if (CollectionUtils.isNotEmpty(groupStudents)) {
			Map<Long, List<HomeworkClazzGroupStudent>> groupStudentMap = new HashMap<Long, List<HomeworkClazzGroupStudent>>(
					groupStudents.size());

			for (HomeworkClazzGroupStudent s : groupStudents) {
				List<HomeworkClazzGroupStudent> list = groupStudentMap.get(s.getGroupId());
				if (CollectionUtils.isEmpty(list)) {
					list = Lists.newArrayList();
				}

				list.add(s);

				groupStudentMap.put(s.getGroupId(), list);
			}

			for (Map.Entry<Long, List<HomeworkClazzGroupStudent>> e : groupStudentMap.entrySet()) {
				clazzGroupRepo.execute("$zyUpdateStudentCount",
						Params.param("id", e.getKey()).put("num", e.getValue().size()));
			}
		}

		// 创建新分组
		HomeworkClazzGroup g = new HomeworkClazzGroup();
		g.setStudentCount(form.getStudentIds().size());
		g.setClassId(form.getClassId());
		g.setCreateAt(new Date());
		g.setUpdateAt(new Date());
		g.setName(form.getName());
		g.setStatus(Status.ENABLED);

		clazzGroupRepo.save(g);

		// 添加学生分组对应关系
		groupStudentService.addStudents(form.getStudentIds(), g.getId(), form.getClassId());

		return Collections.EMPTY_LIST;
	}

	@Override
	@Transactional
	public HomeworkClazzGroup create(long clazzId, String name) {
		HomeworkClazzGroup group = new HomeworkClazzGroup();
		Date date = new Date();
		group.setClassId(clazzId);
		group.setCreateAt(date);
		group.setName(name);
		group.setStatus(Status.ENABLED);
		group.setStudentCount(0);
		group.setUpdateAt(date);
		clazzGroupRepo.save(group);
		return group;
	}

	@Override
	public boolean isExist(String name, long classId) {
		return clazzGroupRepo.find("$zyFindNameInClass", Params.param("name", name).put("classId", classId)).get() != null;
	}

	@Override
	public Long countClassGroupNum(long classId) {
		Long num = clazzGroupRepo.find("$zyCountGroupInClass", Params.param("classId", classId)).get(Long.class);
		return num == null ? 0 : num;
	}

	@Override
	@Transactional
	public void removeGroup(long id) {
		if (id <= 0) {
			throw new IllegalArgException();
		}

		HomeworkClazzGroup g = clazzGroupRepo.get(id);
		if (null == g) {
			throw new IllegalArgException();
		}

		if (g.getStatus() == Status.ENABLED) {
			g.setStatus(Status.DELETED);
			g.setStudentCount(0);

			clazzGroupRepo.save(g);

			groupStudentService.removeStudents(g.getId());
		}

	}

	@Override
	public HomeworkClazzGroup get(long id) {
		return clazzGroupRepo.get(id);
	}

	@Override
	public List<HomeworkClazzGroup> groups(long clazzId) {
		return clazzGroupRepo.find("$zyFindByClazzId", Params.param("clazzId", clazzId)).list();
	}

	@Override
	@Transactional
	public void updateGroupName(long id, String name) {
		HomeworkClazzGroup group = clazzGroupRepo.get(id);
		if (null == group) {
			throw new IllegalArgException();
		}
		if (isExist(name, group.getClassId())) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS);
		}
		group.setName(name);
		group.setUpdateAt(new Date());
		clazzGroupRepo.save(group);
	}

	@Override
	@Transactional
	public void removeStudents(long id, Collection<Long> studentIds) {
		if (id <= 0 || CollectionUtils.isEmpty(studentIds)) {
			throw new IllegalArgException();
		}

		HomeworkClazzGroup group = clazzGroupRepo.get(id);
		if (null == group) {
			throw new IllegalArgException();
		}
		int removeStudentsNum = groupStudentService.removeGroupStudents(studentIds, id);

		group.setStudentCount(group.getStudentCount() - removeStudentsNum);

		clazzGroupRepo.save(group);

	}

	@Override
	@Transactional
	public void removeStudentsByClass(long classId, Collection<Long> studentIds) {
		if (classId <= 0 || CollectionUtils.isEmpty(studentIds)) {
			throw new IllegalArgException();
		}

		List<HomeworkClazzGroupStudent> groupStudents = groupStudentService.findByStusAndClass(studentIds, classId);
		groupStudentService.removeStudents(studentIds, classId);

		if (CollectionUtils.isNotEmpty(groupStudents)) {
			Map<Long, List<HomeworkClazzGroupStudent>> groupStudentMap = new HashMap<Long, List<HomeworkClazzGroupStudent>>(
					groupStudents.size());

			for (HomeworkClazzGroupStudent s : groupStudents) {
				List<HomeworkClazzGroupStudent> list = groupStudentMap.get(s.getGroupId());
				if (CollectionUtils.isEmpty(list)) {
					list = Lists.newArrayList();
				}

				list.add(s);

				groupStudentMap.put(s.getGroupId(), list);
			}

			for (Map.Entry<Long, List<HomeworkClazzGroupStudent>> e : groupStudentMap.entrySet()) {
				clazzGroupRepo.execute("$zyUpdateStudentCount",
						Params.param("id", e.getKey()).put("num", e.getValue().size()));
			}
		}
	}

	@Override
	@Transactional
	public void addStudents(long id, Collection<Long> studentIds) {
		if (id <= 0 || CollectionUtils.isEmpty(studentIds)) {
			throw new IllegalArgException();
		}

		HomeworkClazzGroup group = clazzGroupRepo.get(id);
		if (null == group) {
			throw new IllegalArgException();
		}

		List<HomeworkClazzGroupStudent> groupStudents = groupStudentService.findByStusAndClass(studentIds,
				group.getClassId());
		groupStudentService.removeStudents(studentIds, group.getClassId());

		if (CollectionUtils.isNotEmpty(groupStudents)) {
			Map<Long, List<HomeworkClazzGroupStudent>> groupStudentMap = new HashMap<Long, List<HomeworkClazzGroupStudent>>(
					groupStudents.size());

			for (HomeworkClazzGroupStudent s : groupStudents) {
				List<HomeworkClazzGroupStudent> list = groupStudentMap.get(s.getGroupId());
				if (CollectionUtils.isEmpty(list)) {
					list = Lists.newArrayList();
				}

				list.add(s);

				groupStudentMap.put(s.getGroupId(), list);
			}

			for (Map.Entry<Long, List<HomeworkClazzGroupStudent>> e : groupStudentMap.entrySet()) {
				clazzGroupRepo.execute("$zyUpdateStudentCount",
						Params.param("id", e.getKey()).put("num", e.getValue().size()));
			}
		}

		addStudentCount(group.getId(), studentIds.size());
		groupStudentService.addStudents(studentIds, group.getId(), group.getClassId());
	}

	@Override
	@Transactional
	public void addStudentCount(long id, int addCount) {
		HomeworkClazzGroup group = clazzGroupRepo.get(id);
		if (null == group) {
			throw new IllegalArgException();
		}
		group.setStudentCount(group.getStudentCount() + addCount);
		clazzGroupRepo.save(group);
	}

	@Override
	@Transactional
	public void updateStudentCount(long id, int StudentCount) {
		HomeworkClazzGroup group = clazzGroupRepo.get(id);
		if (null == group) {
			throw new IllegalArgException();
		}
		group.setStudentCount(StudentCount);
		clazzGroupRepo.save(group);
	}

	@Override
	public Map<Long, List<HomeworkClazzGroup>> groupMaps(Collection<Long> classIds) {
		List<HomeworkClazzGroup> list = clazzGroupRepo.find("$zyFindByClazzIds", Params.param("clazzIds", classIds))
				.list();
		Map<Long, List<HomeworkClazzGroup>> map = new HashMap<Long, List<HomeworkClazzGroup>>();
		for (HomeworkClazzGroup group : list) {
			if (map.get(group.getClassId()) == null) {
				List<HomeworkClazzGroup> tempList = new ArrayList<HomeworkClazzGroup>();
				tempList.add(group);
				map.put(group.getClassId(), tempList);
			} else {
				map.get(group.getClassId()).add(group);
			}
		}
		return map;
	}

	@Override
	public Map<Long, HomeworkClazzGroup> mget(Collection<Long> ids) {
		return clazzGroupRepo.mget(ids);
	}
}
