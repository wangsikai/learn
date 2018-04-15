package com.lanking.uxb.service.zuoye.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZyHomeworkStudentClazzServiceImpl implements ZyHomeworkStudentClazzService {

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> hkStudentClazzRepo;

	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkStudentClazzStatService stuClazzStatService;
	@Autowired
	private ZyHomeworkClassGroupService homeworkClassGroupService;
	// 一个班级最多的学生数量
	private static int MAX_STUDENT_PER_CLASS = 100;

	@PostConstruct
	void init() {
		MAX_STUDENT_PER_CLASS = Env.getInt("student.max.per_class");
	}

	@Transactional
	@Override
	public HomeworkStudentClazz join(long classId, long studentId) {
		HomeworkClazz clazz = hkClassService.get(classId);
		if (clazz == null || clazz.getStatus() != Status.ENABLED) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST);
		}
		if (clazz.getLockStatus() != Status.ENABLED) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASSS_LOCKED);
		}
		if (clazz.getStudentNum() >= MAX_STUDENT_PER_CLASS) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASSSTUDENT_MAXLIMIT);
		}
		HomeworkStudentClazz p = hkStudentClazzRepo
				.find("$zyQuery", Params.param("classId", classId).put("studentId", studentId)).get();
		if (p == null) {
			p = new HomeworkStudentClazz();
			p.setClassId(classId);
			p.setCreateAt(new Date());
			p.setJoinAt(p.getCreateAt());
			p.setStatus(Status.ENABLED);
			p.setStudentId(studentId);
			hkClassService.incrStudentNum(classId, 1);
			return hkStudentClazzRepo.save(p);
		} else if (p.getStatus() != Status.ENABLED) {
			p.setStatus(Status.ENABLED);
			p.setJoinAt(new Date());
			hkClassService.incrStudentNum(classId, 1);
			stuClazzStatService.recoverByStudentId(studentId, classId);
			return hkStudentClazzRepo.save(p);
		}
		return p;
	}

	@Override
	public HomeworkStudentClazz find(long classId, long studentId) {
		return hkStudentClazzRepo
				.find("$zyQuery", Params.param("classId", classId).put("studentId", studentId).put("status", 0)).get();
	}

	@Override
	public HomeworkStudentClazz findAll(long classId, long studentId) {
		return hkStudentClazzRepo.find("$zyQuery", Params.param("classId", classId).put("studentId", studentId)).get();
	}

	@Override
	public boolean isJoin(long classId, long studentId) {
		long count = hkStudentClazzRepo.find("$zyIsJoin", Params.param("classId", classId).put("studentId", studentId))
				.count();
		return count > 0;
	}

	@Transactional
	@Override
	public HomeworkStudentClazz exit(long classId, long studentId, Long teacherId) {
		HomeworkClazz clazz = hkClassService.get(classId);
		if (clazz == null) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST);
		}
		if (teacherId != null && clazz.getTeacherId() != teacherId.longValue()) {
			throw new NoPermissionException();
		}
		HomeworkStudentClazz p = hkStudentClazzRepo
				.find("$zyQuery", Params.param("classId", classId).put("studentId", studentId)).get();
		if (p == null) {
			throw new NoPermissionException();
		}

		List<Long> studentIds = new ArrayList<Long>(1);
		studentIds.add(studentId);
		homeworkClassGroupService.removeStudentsByClass(classId, studentIds);
		if (p.getStatus() == Status.ENABLED) {
			if (teacherId != null) {
				p.setStatus(Status.DELETED);
				stuClazzStatService.removeByStudentId(studentId, classId);
			} else {
				p.setStatus(Status.DISABLED);
			}
			p.setExitAt(new Date());
			hkClassService.incrStudentNum(classId, -1);
			return hkStudentClazzRepo.save(p);
		} else {
			return p;
		}
	}

	@Override
	@Transactional
	public void exit(long classId, Collection<Long> studentIds, Long teacherId) {
		HomeworkClazz clazz = hkClassService.get(classId);
		if (clazz == null) {
			throw new ZuoyeException(ZuoyeException.ZUOYE_CLASS_NOT_EXIST);
		}
		if (teacherId != null && clazz.getTeacherId() != teacherId.longValue()) {
			throw new NoPermissionException();
		}

		for (Long studentId : studentIds) {
			HomeworkStudentClazz p = hkStudentClazzRepo
					.find("$zyQuery", Params.param("classId", classId).put("studentId", studentId)).get();
			if (p == null) {
				throw new NoPermissionException();
			}
			if (p.getStatus() == Status.ENABLED) {
				if (teacherId != null) {
					p.setStatus(Status.DELETED);
					stuClazzStatService.removeByStudentId(studentId, classId);
				} else {
					p.setStatus(Status.DISABLED);
				}
				p.setExitAt(new Date());
				hkClassService.incrStudentNum(classId, -1);
			}
			hkStudentClazzRepo.save(p);
		}
		homeworkClassGroupService.removeStudentsByClass(classId, studentIds);

	}

	@Override
	public List<HomeworkStudentClazz> listCurrentClazzs(long studentId) {
		return hkStudentClazzRepo.find("$zyFindByStudentId", Params.param("studentId", studentId)).list();
	}

	@Override
	public List<HomeworkStudentClazz> listCurrentClazzsHasTeacher(long studentId) {
		return hkStudentClazzRepo.find("$zyFindByStudentId", Params.param("studentId", studentId).put("hasTeacher", 1))
				.list();
	}

	@Override
	public List<Long> listClassStudents(long classId) {
		return hkStudentClazzRepo.find("$zyListClassStudents", Params.param("classId", classId)).list(Long.class);
	}

	@Override
	public Page<HomeworkStudentClazz> query(long classId, Pageable pageable) {
		return hkStudentClazzRepo.find("$zyPageClassStudents", Params.param("classId", classId)).fetch(pageable);
	}

	@Override
	public Page<HomeworkStudentClazz> query(long classId, Pageable pageable, Direction joinorder) {
		Params params = Params.param("classId", classId);
		if (null != joinorder) {
			params.put("joinorder", joinorder.getValue());
		}
		return hkStudentClazzRepo.find("$zyPageClassStudents", params).fetch(pageable);
	}

	@Override
	public Page<HomeworkStudentClazz> query(long classId, long groupId, Pageable pageable) {
		return hkStudentClazzRepo
				.find("$zyPageClassStudentsByGroup", Params.param("classId", classId).put("groupId", groupId))
				.fetch(pageable);
	}

	@Override
	public Page<HomeworkStudentClazz> query(long classId, long groupId, Pageable pageable, Direction joinorder) {
		Params params = Params.param("classId", classId).put("groupId", groupId);
		if (null != joinorder) {
			params.put("joinorder", joinorder.getValue());
		}
		return hkStudentClazzRepo.find("$zyPageClassStudentsByGroup", params).fetch(pageable);
	}

	@Override
	public List<HomeworkStudentClazz> list(long classId) {
		return hkStudentClazzRepo
				.find("$zyListStudents", Params.param("classId", classId).put("limit", MAX_STUDENT_PER_CLASS)).list();
	}

	@Override
	public long countStudentClazz(long studentId, Status status) {
		Params params = Params.param("studentId", studentId);
		if (status != null) {
			params.put("status", status.getValue());
		}
		return hkStudentClazzRepo.find("$zyCountStudentClazz", params).get(Long.class);
	}

	@Transactional
	@Override
	public int mark(long teacherId, long classId, long studentId, String name) {
		HomeworkClazz hkClazz = hkClassService.get(classId);
		if (hkClazz == null || hkClazz.getTeacherId() != teacherId) {
			throw new NoPermissionException();
		}
		return hkStudentClazzRepo.execute("$zyMarkStudent",
				Params.param("studentId", studentId).put("classId", classId).put("name", name));
	}

	@Override
	public List<HomeworkStudentClazz> findByStudentIds(Collection<Long> studentIds) {
		return hkStudentClazzRepo.find("$zyFindStudentsByIds", Params.param("studentIds", studentIds)).list();
	}

	@Override
	public Map<Long, HomeworkStudentClazz> findByStudentIdsAndClassId(Collection<Long> studentIds, long classId) {
		if (CollectionUtils.isEmpty(studentIds)) {
			return Collections.EMPTY_MAP;
		}

		List<HomeworkStudentClazz> datas = hkStudentClazzRepo
				.find("$zyFindByStudentsAndClass", Params.param("studentIds", studentIds).put("classId", classId))
				.list();

		Map<Long, HomeworkStudentClazz> retMap = new HashMap<Long, HomeworkStudentClazz>(datas.size());

		for (HomeworkStudentClazz c : datas) {
			retMap.put(c.getStudentId(), c);
		}

		return retMap;
	}
}
