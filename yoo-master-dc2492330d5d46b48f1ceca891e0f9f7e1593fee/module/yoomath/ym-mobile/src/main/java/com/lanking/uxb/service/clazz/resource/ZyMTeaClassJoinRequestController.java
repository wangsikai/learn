package com.lanking.uxb.service.clazz.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequest;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequestStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.web.convert.ClazzJoinRequestConvert;
import com.lanking.uxb.service.web.value.VClazzJoinRequest;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

/**
 * 教师处理学生加入班级请求
 *
 * @author xinyu.zhou
 * @since 3.9.2
 */
@RestController
@RequestMapping(value = "zy/m/t/cj")
public class ZyMTeaClassJoinRequestController {
	@Autowired
	private ZyClazzJoinRequestService clazzJoinRequestService;
	@Autowired
	private ClazzJoinRequestConvert clazzJoinRequestConvert;
	@Autowired
	private ZyHomeworkStudentClazzService studentClazzService;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	MqSender mqSender;

	/**
	 * 获得待加入班级请求list
	 *
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "getRequestList", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getRequestList(@RequestParam(value = "cursor", defaultValue = "0") long cursor,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		if (cursor <= 0) {
			cursor = Long.MAX_VALUE;
		}

		CursorPageable<Long> cursorPageable = CP.cursor(cursor, size);
		long teacherId = Security.getUserId();

		CursorPage<Long, ClazzJoinRequest> page = clazzJoinRequestService.list(teacherId, cursorPageable);
		List<VClazzJoinRequest> vs = clazzJoinRequestConvert.to(page.getItems());

		VCursorPage<VClazzJoinRequest> vpage = new VCursorPage<VClazzJoinRequest>();
		vpage.setItems(vs);
		vpage.setCursor(page.getNextCursor() == null ? 0 : page.getNextCursor());

		return new Value(vpage);
	}

	/**
	 * 更新学生请求加入班级数据
	 *
	 * @param id
	 *            id
	 * @param status
	 *            {@link ClazzJoinRequestStatus}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "updateStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateStatus(long id, ClazzJoinRequestStatus status) {
		if (id <= 0 || status == null) {
			return new Value(new IllegalArgException());
		}
		ClazzJoinRequest request = clazzJoinRequestService.get(id);
		if (null == request) {
			return new Value(new IllegalArgException());
		}
		if (Security.getUserId() != request.getTeacherId()) {
			return new Value(new NoPermissionException());
		}
		// 其他端已经同意了，这时候再同意，直接返回
		if (request.getRequestStatus() == ClazzJoinRequestStatus.APPLY && status == ClazzJoinRequestStatus.APPLY) {
			return new Value();
		}
		// 其他端已经拒绝，再点同意
		if (request.getRequestStatus() == ClazzJoinRequestStatus.DENIED && status == ClazzJoinRequestStatus.APPLY) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLASS_REQUEST_DENIED));
		}
		// 学生已经加入此班级
		HomeworkStudentClazz h = studentClazzService.find(request.getHomeworkClassId(), request.getStudentId());
		if (h != null) {
			// 已加入班级，点击同意后提示，并且更新此请求为已同意
			clazzJoinRequestService.updateRequestStatus(id, ClazzJoinRequestStatus.APPLY);
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_EXISTS));
		}
		try {
			if (status == ClazzJoinRequestStatus.APPLY) {
				studentClazzService.join(request.getHomeworkClassId(), request.getStudentId());

				// 学生加入班级，需要重新计算班级整体统计
				homeworkStatisticService.updateTeacherHomeworkStatByClass(request.getHomeworkClassId());

				// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
				JSONObject object = new JSONObject();
				object.put("classId", request.getHomeworkClassId());
				object.put("studentId", request.getStudentId());
				mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
						MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN,
						MQ.builder().data(object).build());
			}
			clazzJoinRequestService.updateRequestStatus(id, status);
		} catch (ZuoyeException e) {
			// 发生如下情况则更新请求状态为拒绝
			if (e.getCode() == ZuoyeException.ZUOYE_CLASS_NOT_EXIST) {
				clazzJoinRequestService.updateRequestStatus(id, ClazzJoinRequestStatus.DENIED);
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_CLOSE));
			} else if (e.getCode() == ZuoyeException.ZUOYE_CLASSS_LOCKED) {
				return new Value(
						new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLAZZ_REQUEST_LOCK));
			} else if (e.getCode() == ZuoyeException.ZUOYE_CLASSSTUDENT_MAXLIMIT) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_JOIN_CLASS_MAXLIMIT));
			}
		}

		return new Value();
	}

	/**
	 * 删除请求信息
	 *
	 * @param id
	 *            id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(long id) {
		if (id <= 0) {
			return new Value(new IllegalArgException());
		}

		ClazzJoinRequest clazzJoinRequest = clazzJoinRequestService.get(id);
		if (clazzJoinRequest == null) {
			return new Value(new IllegalArgException());
		}
		if (clazzJoinRequest.getDeleteStatus() != Status.ENABLED) {
			return new Value();
		}
		if (clazzJoinRequest.getTeacherId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}
		clazzJoinRequestService.delete(id);

		return new Value();
	}
}
