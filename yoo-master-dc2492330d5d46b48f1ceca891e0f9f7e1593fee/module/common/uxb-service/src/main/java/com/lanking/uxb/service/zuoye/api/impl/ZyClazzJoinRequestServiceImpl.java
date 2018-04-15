package com.lanking.uxb.service.zuoye.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequest;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequestStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;

/**
 * 学生用户请求加入班级service
 *
 * @author xinyu.zhou
 * @since 3.0.2
 */
@Service
@Transactional(readOnly = true)
public class ZyClazzJoinRequestServiceImpl implements ZyClazzJoinRequestService {
	@Autowired
	@Qualifier("ClazzJoinRequestRepo")
	Repo<ClazzJoinRequest, Long> repo;

	@Override
	@Transactional
	public void request(long studentId, long homeworkClassId, long teacherId, String realName) {
		ClazzJoinRequest request = repo
				.find("$zyFindClazzRequest", Params.param("studentId", studentId).put("classId", homeworkClassId))
				.get();

		Date now = new Date();

		if (request == null) {
			request = new ClazzJoinRequest();
			request.setCreateAt(now);
			request.setHomeworkClassId(homeworkClassId);
			request.setRealName(realName);
			request.setRequestStatus(ClazzJoinRequestStatus.PROCESSING);
			request.setTeacherId(teacherId);
			request.setStudentId(studentId);
			request.setDeleteStatus(Status.ENABLED);
		}
		request.setRealName(realName);
		request.setUpdateAt(now);
		repo.save(request);
	}

	@Override
	@Transactional
	public void updateRequestStatus(long id, ClazzJoinRequestStatus status) {
		ClazzJoinRequest request = repo.get(id);
		if (request != null) {
			request.setRequestStatus(status);
			request.setUpdateAt(new Date());

			repo.save(request);
		}
	}

	@Override
	public Page<ClazzJoinRequest> list(long teacherId, Pageable pageable) {
		return repo.find("$zyRequestList", Params.param("teacherId", teacherId)).fetch(pageable);
	}

	@Override
	public CursorPage<Long, ClazzJoinRequest> list(long teacherId, CursorPageable<Long> cursorPageable) {
		return repo.find("$zyCursorRequestList", Params.param("teacherId", teacherId)).fetch(cursorPageable);
	}

	@Override
	public Long requestCount(long teacherId, Date startTime) {
		Params params = Params.param("teacherId", teacherId);
		if (startTime != null) {
			params.put("startTime", startTime);
		}
		return repo.find("$zyRequestCount", params).get(Long.class);
	}

	@Override
	public ClazzJoinRequest get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public void delete(long id) {
		repo.execute("$delete", Params.param("id", id).put("updateAt", new Date()));
	}

}
