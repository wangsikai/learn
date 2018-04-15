package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequest;
import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequestStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.web.cache.ClassJoinRequestTeacherNoticeCacheService;
import com.lanking.uxb.service.web.convert.ClazzJoinRequestConvert;
import com.lanking.uxb.service.web.value.VClazzJoinRequest;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzGroupConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzGroupStudentConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

/**
 * 悠作业老师班级管理接口
 * 
 * @since yoomath V1.2
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/class/man/2")
public class ZyTeaClassManage2Controller {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkService homeworkService;
	@Autowired
	private ZyStudentHomeworkStatService zyStuHkStatService;
	@Autowired
	private ZyStudentHomeworkStatConvert zyStuHkStatConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coninsService;
	@Autowired
	private ZyClazzJoinRequestService zyClazzJoinRequestService;
	@Autowired
	private ClazzJoinRequestConvert clazzJoinRequestConvert;
	@Autowired
	private ClassJoinRequestTeacherNoticeCacheService classJoinRequestCacheService;
	@Autowired
	private ZyHomeworkClassGroupService homeworkClassGroupService;
	@Autowired
	private ZyHomeworkClazzGroupConvert homeworkClazzGroupConvert;
	@Autowired
	private ZyHomeworkClassGroupStudentService homeworkClassGroupStudentService;
	@Autowired
	private ZyHomeworkClazzGroupStudentConvert homeworkClazzGroupStudentConvert;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	MqSender mqSender;

	/**
	 * 创建班级
	 * 
	 * @since yoomath V1.2
	 * @param name
	 *            班级名称
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "create", method = { RequestMethod.POST, RequestMethod.GET })
	public Value createClass(String name) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		try {
			HomeworkClazz homeworkClazz = zyHkClassService.create(name, Security.getUserId());
			GrowthLog growthLog = growthService.grow(GrowthAction.CREATE_CLAZZ, Security.getUserId(), -1, Biz.CLASS,
					homeworkClazz.getId(), true);
			CoinsLog coinsLog = coninsService.earn(CoinsAction.CREATE_CLAZZ, Security.getUserId(), -1, Biz.CLASS,
					homeworkClazz.getId());
			if (growthLog.getHonor() != null) {
				data.put("userReward",
						new VUserReward(growthLog.getHonor().getUpRewardCoins(), growthLog.getHonor().isUpgrade(),
								growthLog.getHonor().getLevel(), growthLog.getGrowthValue(), coinsLog.getCoinsValue()));
			}
			data.put("clazz", zyHkClassConvert.to(homeworkClazz));
			return new Value(data);
		} catch (ZuoyeException e) {
			return new Value(e);
		} catch (Exception e) {
			// 防止有唯一键的约束，正常不会出现
			return new Value(e);
		}
	}

	/**
	 * 保存班级名
	 *
	 * @param name
	 *            班级名
	 * @param classId
	 *            班级的id
	 * @return 保存后的班级
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "update", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateClass(String name, Long classId) {
		try {
			if (StringUtils.isBlank(name)) {
				return new Value(new IllegalArgException());
			}
			return new Value(zyHkClassConvert.to(zyHkClassService.updateName(name, classId, Security.getUserId())));
		} catch (ZuoyeException e) {
			return new Value(e);
		} catch (Exception e) {
			return new Value(e);
		}
	}

	/**
	 * 刷新班级码
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "refresh_code", method = { RequestMethod.POST, RequestMethod.GET })
	public Value refreshCode(long classId) {
		try {
			return new Value(zyHkClassConvert.to(zyHkClassService.refreshCode(classId, Security.getUserId())));
		} catch (ZuoyeException e) {
			return new Value(e);
		} catch (Exception e) {
			// 防止有唯一键的约束，正常不会出现
			return new Value(e);
		}
	}

	/**
	 * 关闭班级
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "close", method = { RequestMethod.POST, RequestMethod.GET })
	public Value close(long classId) {
		try {
			return new Value(zyHkClassConvert.to(zyHkClassService.close(classId, Security.getUserId())));
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 查询当前班级
	 * 
	 * @since yoomath V1.2
	 * @param history
	 *            是否是历史班级(默认为false即当前班级 )
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @return {@link Value}
	 */
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(@RequestParam(value = "history", defaultValue = "false") boolean history,
			@RequestParam(value = "page", defaultValue = "1") int pageNo, @RequestParam(defaultValue = "40") int size) {
		size = Math.min(size, 40);
		Map<String, Object> data = new HashMap<String, Object>(2);
		ZyHomeworkClassQuery query = new ZyHomeworkClassQuery();
		query.setTeacherId(Security.getUserId());
		query.setStatus(history ? Status.DISABLED : Status.ENABLED);
		Page<HomeworkClazz> page = zyHkClassService.query(query, P.index(pageNo, size));
		List<VHomeworkClazz> vs = null;
		if (page.isEmpty()) {
			vs = Collections.EMPTY_LIST;
		} else {
			vs = zyHkClassConvert.to(page.getItems(), new ZyHomeworkClassConvertOption(false, true, false));
		}
		VPage<VHomeworkClazz> vpage = new VPage<VHomeworkClazz>();
		vpage.setCurrentPage(pageNo);
		vpage.setItems(vs);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		data.put("vpage", vpage);
		if (!history && pageNo == 1) {
			data.put("historyCount", zyHkClassService.historyCount(Security.getUserId()));
		}
		return new Value(data);
	}

	/**
	 * 删除班级
	 *
	 * @param classId
	 *            班级的id
	 * @return 保存信息
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(@RequestParam(value = "classId") Long classId) {
		try {
			zyHkClassService.delete(classId, Security.getUserId());
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 教师查看班级统计(教师首页和班级管理使用)
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@Deprecated
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "statistic", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statistic(@RequestParam(required = false) Long classId,
			@RequestParam(value = "history", defaultValue = "false") boolean history) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		List<HomeworkClazz> list = Lists.newArrayList();
		if (history
				|| (classId != null && classId > 0 && zyHkClassService.get(classId).getStatus() == Status.DISABLED)) {
			list = zyHkClassService.listHistoryClazzs(Security.getUserId());
		} else {
			list = zyHkClassService.listCurrentClazzs(Security.getUserId());
		}
		List<VHomeworkClazz> vs = null;
		if (CollectionUtils.isEmpty(list)) {
			vs = Collections.EMPTY_LIST;
		} else {
			vs = zyHkClassConvert.to(list, new ZyHomeworkClassConvertOption(false, true, true));
			if (classId != null && classId > 0) {
				List<VHomework> latestHomework = homeworkConvert
						.to(homeworkService.getLatestIssuedHomeWorks(classId, 20));
				data.put("latestHomework", latestHomework);
			} else {
				List<VHomework> latestHomework = homeworkConvert
						.to(homeworkService.getLatestIssuedHomeWorks(list.get(0).getId(), 20));
				data.put("latestHomework", latestHomework);
			}
		}
		data.put("clazzs", vs);
		return new Value(data);
	}

	/**
	 * 查询班级作业列表
	 * 
	 * @since yoomath V1.2
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query_homework", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHomework(@RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "40") int size, long classId) {
		size = Math.min(size, 40);
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setClassId(classId);
		query.setCourse(false);
		Page<Homework> page = homeworkService.query(query, P.index(pageNo, size));
		VPage<VHomework> vpage = new VPage<VHomework>();
		vpage.setCurrentPage(pageNo);
		vpage.setItems(homeworkConvert.to(page.getItems(), new HomeworkConvertOption(true)));
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		return new Value(vpage);
	}

	/**
	 * 班级学生列表
	 * 
	 * @since yoomath V1.2
	 * @since yoomath v2.3.2 添加学生分组信息
	 * @param classId
	 *            班级ID
	 * @param pageNo
	 *            页码
	 * @param size
	 *            获取数量
	 * @param joinorder
	 *            排序
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query_student", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryStudent(long classId, @RequestParam(defaultValue = "1") int pageNo,
			@RequestParam(defaultValue = "60") int size, Long groupId, Direction joinorder) {
		VPage<VHomeworkStudentClazz> vpage = new VPage<VHomeworkStudentClazz>();
		Page<HomeworkStudentClazz> page = null;
		if (groupId == null) {
			page = hkStuClazzService.query(classId, P.index(pageNo, size), joinorder);
		} else {
			page = hkStuClazzService.query(classId, groupId, P.index(pageNo, size), joinorder);
		}
		List<HomeworkStudentClazz> list = page.getItems();
		List<VHomeworkStudentClazz> items = hkStuClazzConvert.to(list);
		if (CollectionUtils.isNotEmpty(items)) {
			List<Long> studentIds = new ArrayList<Long>(items.size());
			for (VHomeworkStudentClazz v : items) {
				studentIds.add(v.getStudentId());
			}
			List<StudentHomeworkStat> studentHomeworkStats = zyStuHkStatService.find(classId, studentIds);
			List<VStudentHomeworkStat> vstudentHomeworkStats = zyStuHkStatConvert.to(studentHomeworkStats);
			Map<Long, VStudentHomeworkStat> map = new HashMap<Long, VStudentHomeworkStat>(vstudentHomeworkStats.size());
			UserConvertOption option = new UserConvertOption();
			option.setInitMemberType(true);
			Map<Long, VUser> users = userConvert.mget(studentIds, option);
			for (VStudentHomeworkStat v : vstudentHomeworkStats) {
				v.setUser(users.get(v.getUserId()));
				map.put(v.getUserId(), v);
			}
			for (VHomeworkStudentClazz v : items) {
				VStudentHomeworkStat stat = map.get(v.getStudentId());
				if (stat == null) {
					stat = new VStudentHomeworkStat();
					stat.setHomeworkClassId(classId);
					stat.setUserId(v.getStudentId());
					stat.setUser(users.get(v.getStudentId()));
				}
				v.setHomeworkStat(stat);
			}
		}
		vpage.setCurrentPage(pageNo);
		vpage.setItems(items);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		return new Value(vpage);
	}

	/**
	 * 移除学生
	 * 
	 * @since yoomath V1.2
	 * @param classId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "remove_student", method = { RequestMethod.POST, RequestMethod.GET })
	public Value removeStudent(long classId, long studentId) {
		try {
			hkStuClazzService.exit(classId, studentId, Security.getUserId());

			// 学生被移除班级，需要重新计算班级整体统计
			homeworkStatisticService.updateTeacherHomeworkStatByClass(classId);

			// 学生被移除班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
			JSONObject object = new JSONObject();
			object.put("classId", classId);
			object.put("studentId", studentId);
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_LEAVE,
					MQ.builder().data(object).build());
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}

	}

	/**
	 * 锁定班级
	 * 
	 * @since yoomath V1.3
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "lock", method = { RequestMethod.POST, RequestMethod.GET })
	public Value lock(long classId) {
		zyHkClassService.lock(classId, Security.getUserId());
		return new Value();
	}

	/**
	 * 解锁班级
	 * 
	 * @since yoomath V1.3
	 * @param classId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "unlock", method = { RequestMethod.POST, RequestMethod.GET })
	public Value unlock(long classId) {
		int ret = zyHkClassService.unlock(classId, Security.getUserId());
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new NoPermissionException());
		}
	}

	/**
	 * 备注学生
	 * 
	 * @since yoomath V1.4
	 * @param classId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "mark", method = { RequestMethod.POST, RequestMethod.GET })
	public Value mark(long classId, long studentId, String name) {
		int len = StringUtils.getJsUnicodeLength(name);
		if (len > 30) {
			return new Value(new IllegalArgException());
		}
		try {
			int ret = hkStuClazzService.mark(Security.getUserId(), classId, studentId, name);
			if (ret == 0) {
				return new Value(new NoPermissionException());
			}
			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 查询老师对应的班级加入申请列表
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryRequestList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryRequestList(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<ClazzJoinRequest> cp = zyClazzJoinRequestService.list(Security.getUserId(), P.index(page, pageSize));
		VPage<VClazzJoinRequest> vp = new VPage<VClazzJoinRequest>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(clazzJoinRequestConvert.to(cp.getItems()));
		// 清空未读缓存
		classJoinRequestCacheService.clear(Security.getUserId());
		classJoinRequestCacheService.update(Security.getUserId(), System.currentTimeMillis());
		return new Value(vp);
	}

	/**
	 * 获得NEW提醒.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getNotice", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getNotice() {
		Long needNotice = 0L;
		long teacherId = Security.getUserId();
		Long readTimestamp = classJoinRequestCacheService.getTimestamp(teacherId);
		if (readTimestamp != null) {
			needNotice = zyClazzJoinRequestService.requestCount(teacherId, new Date(readTimestamp));
		} else {
			needNotice = zyClazzJoinRequestService.requestCount(teacherId, null);
		}
		return new Value(needNotice);
	}

	/**
	 * 班级需要申请加入
	 * 
	 * @param classId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "needConfirm", method = { RequestMethod.POST, RequestMethod.GET })
	public Value needConfirm(Long classId) {
		return new Value(zyHkClassService.needConfirm(classId, Security.getUserId()));
	}

	/**
	 * 允许任何人加入
	 * 
	 * @param classId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "notNeedConfirm", method = { RequestMethod.POST, RequestMethod.GET })
	public Value notNeedConfirm(Long classId) {
		return new Value(zyHkClassService.notNeedConfirm(classId, Security.getUserId()));
	}

	/**
	 * 同意加入
	 * 
	 * @param id
	 *            申请Id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "agreeJoin", method = { RequestMethod.POST, RequestMethod.GET })
	public Value agreeJoin(Long id) {
		ClazzJoinRequest c = zyClazzJoinRequestService.get(id);
		if (c.getRequestStatus() == ClazzJoinRequestStatus.PROCESSING) {
			try {
				hkStuClazzService.join(c.getHomeworkClassId(), c.getStudentId());
				zyClazzJoinRequestService.updateRequestStatus(id, ClazzJoinRequestStatus.APPLY);

				// 学生加入班级，需要重新计算班级整体统计
				homeworkStatisticService.updateTeacherHomeworkStatByClass(c.getHomeworkClassId());

				// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
				JSONObject object = new JSONObject();
				object.put("classId", c.getHomeworkClassId());
				object.put("studentId", c.getStudentId());
				mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
						MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN,
						MQ.builder().data(object).build());
			} catch (ZuoyeException e) {
				return new Value(e);
			}
		} else {
			if (c.getRequestStatus() == ClazzJoinRequestStatus.APPLY) {
				return new Value(new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_ALREADY_INCLASS));
			} else if (c.getRequestStatus() == ClazzJoinRequestStatus.DENIED) {
				return new Value(new ZuoyeException(ZuoyeException.ZUOYE_APPLY_ALREADY_REFUSE));
			}
		}
		return new Value();
	}

	/**
	 * 拒绝加入的时候，要判断是否已加入或者已拒绝
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "refuseJoinApply", method = { RequestMethod.POST, RequestMethod.GET })
	public Value refuseJoinApply(Long id) {
		ClazzJoinRequest c = zyClazzJoinRequestService.get(id);
		// 首先判断当前申请的学生是否经加入到班级
		HomeworkStudentClazz h = hkStuClazzService.find(c.getHomeworkClassId(), c.getStudentId());
		if (h != null) {
			// 如果已经加入，提示已加入到班级
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_STUDENT_ALREADY_INCLASS));
		} else {
			// 没有加入，判断当前申请是否已经被拒绝
			if (c.getRequestStatus() == ClazzJoinRequestStatus.DENIED) {
				return new Value(new ZuoyeException(ZuoyeException.ZUOYE_APPLY_ALREADY_REFUSE));
			}
		}
		return new Value();
	}

	/**
	 * 拒绝
	 * 
	 * @param id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "refuseJoin", method = { RequestMethod.POST, RequestMethod.GET })
	public Value refuseJoin(Long id) {
		zyClazzJoinRequestService.updateRequestStatus(id, ClazzJoinRequestStatus.DENIED);
		return new Value();
	}

	/**
	 * 获取班级分组信息.
	 * 
	 * @since yoomath web v2.3.2
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "clazzGroups", method = { RequestMethod.POST })
	public Value clazzGroups(Long clazzId) {
		if (clazzId == null) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazz clazz = zyHkClassService.get(clazzId);
		if (clazz == null || clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new EntityNotFoundException());
		}
		Map<String, Object> map = new HashMap<String, Object>(2);
		List<HomeworkClazzGroup> groups = homeworkClassGroupService.groups(clazzId);
		List<HomeworkClazzGroupStudent> groupStudents = homeworkClassGroupStudentService.findAll(clazzId);
		List<VHomeworkClazzGroup> vgroups = homeworkClazzGroupConvert.to(groups);
		map.put("groups", vgroups);
		map.put("groupStudents", homeworkClazzGroupStudentConvert.to(groupStudents, vgroups));
		return new Value(map);
	}

	/**
	 * 创建分组.
	 * 
	 * @param clazzId
	 *            班级
	 * @param name
	 *            组名称
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "createGroup", method = { RequestMethod.POST })
	public Value createGroup(Long clazzId, String name) {
		if (clazzId == null || StringUtils.isBlank(name)) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazz clazz = zyHkClassService.get(clazzId);
		if (clazz == null || clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new EntityNotFoundException());
		}
		List<HomeworkClazzGroup> groups = homeworkClassGroupService.groups(clazzId);
		if (groups.size() >= 5) {
			// 分组超出数量
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_MAXLIMIT));
		} else {
			for (HomeworkClazzGroup group : groups) {
				if (group.getName().equals(name)) {
					// 分组重名
					return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS));
				}
			}
		}

		HomeworkClazzGroup group = homeworkClassGroupService.create(clazzId, name);
		Map<String, Object> map = new HashMap<String, Object>(1);
		map.put("group", homeworkClazzGroupConvert.to(group));
		return new Value(map);
	}

	/**
	 * 更新分组名称.
	 * 
	 * @param groupId
	 *            分组ID
	 * @param name
	 *            名称
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "updateGroupName", method = { RequestMethod.POST })
	public Value updateGroupName(Long groupId, String name) {
		if (groupId == null || StringUtils.isBlank(name)) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazzGroup group = homeworkClassGroupService.get(groupId);
		if (group == null) {
			return new Value(new EntityNotFoundException());
		}
		HomeworkClazz clazz = zyHkClassService.get(group.getClassId());
		if (clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}

		boolean isExist = homeworkClassGroupService.isExist(name, group.getClassId());
		if (isExist) {
			// 分组重名
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS));
		}

		try {
			homeworkClassGroupService.updateGroupName(groupId, name);
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 删除分组.
	 * 
	 * @param groupId
	 *            分组ID.
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "deleteGroup", method = { RequestMethod.POST })
	public Value deleteGroup(Long groupId) {
		if (groupId == null) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazzGroup group = homeworkClassGroupService.get(groupId);
		if (group == null) {
			return new Value(new EntityNotFoundException());
		}
		HomeworkClazz clazz = zyHkClassService.get(group.getClassId());
		if (clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}

		try {
			homeworkClassGroupStudentService.removeStudents(group.getId());
			homeworkClassGroupService.removeGroup(group.getId());
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 更新单个学生分组.
	 * 
	 * @param clazzId
	 *            班级ID
	 * @param studentId
	 *            学生ID
	 * @param groupId
	 *            组ID
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "changeGroup", method = { RequestMethod.POST })
	public Value changeGroup(Long clazzId, Long studentId, Long newGroupId, Long oldGroupId) {
		if (clazzId == null || studentId == null || newGroupId == null) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazz clazz = zyHkClassService.get(clazzId);
		HomeworkClazzGroup group = homeworkClassGroupService.get(newGroupId);
		if (clazz == null || group == null) {
			return new Value(new EntityNotFoundException());
		}
		if (clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}
		HomeworkStudentClazz homeworkStudentClazz = hkStuClazzService.findAll(clazzId, studentId);
		if (homeworkStudentClazz == null || homeworkStudentClazz.getStatus() == Status.DELETED) {
			return new Value(new ZuoyeException(ZuoyeException.ZUOYE_APPLY_ALREADY_REFUSE));
		}
		try {
			homeworkClassGroupStudentService.changeGroup(clazzId, studentId, newGroupId);
			homeworkClassGroupService.addStudentCount(newGroupId, 1);
			if (oldGroupId != null) {
				homeworkClassGroupService.addStudentCount(oldGroupId, -1);
			}
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 重置学生分组.
	 * 
	 * @param clazzId
	 *            班级ID
	 * @param groupId
	 *            组ID
	 * @param studentIds
	 *            学生ID集合
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "resetGroup", method = { RequestMethod.POST })
	public Value resetGroup(Long clazzId, Long groupId,
			@RequestParam(value = "studentIds", required = false) List<Long> studentIds) {
		if (clazzId == null || groupId == null) {
			return new Value(new MissingArgumentException());
		}
		HomeworkClazz clazz = zyHkClassService.get(clazzId);
		HomeworkClazzGroup group = homeworkClassGroupService.get(groupId);
		if (clazz == null || group == null) {
			return new Value(new EntityNotFoundException());
		}
		if (clazz.getTeacherId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}

		Map<String, Object> map = new HashMap<String, Object>(1);
		if (CollectionUtils.isEmpty(studentIds)) {
			// 仅清空本组
			try {
				homeworkClassGroupStudentService.removeStudents(groupId);
				homeworkClassGroupService.updateStudentCount(groupId, 0);
				map.put("errorStudentIds", Lists.newArrayList());
				return new Value(map);
			} catch (AbstractException e) {
				return new Value(e);
			}
		}

		Map<Long, HomeworkStudentClazz> studentClazzMap = hkStuClazzService.findByStudentIdsAndClassId(studentIds,
				clazz.getId());
		List<Long> addStudentIds = new ArrayList<Long>();
		List<Long> errorStudentIds = new ArrayList<Long>();

		// 判断不能加入的学生集合
		if (studentClazzMap.size() != studentIds.size()) {
			for (long studentId : studentIds) {
				if (studentClazzMap.get(studentId) == null) {
					errorStudentIds.add(studentId);
				} else {
					addStudentIds.add(studentId);
				}
			}
		} else {
			addStudentIds = studentIds;
		}

		try {
			List<HomeworkClazzGroupStudent> groupStudents = homeworkClassGroupStudentService
					.findByStusAndClass(addStudentIds, clazzId);
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
					homeworkClassGroupService.addStudentCount(e.getKey(), -e.getValue().size());
				}
			}
			homeworkClassGroupStudentService.removeStudents(addStudentIds, clazzId);
			homeworkClassGroupStudentService.removeStudents(groupId);
			homeworkClassGroupStudentService.addStudents(addStudentIds, groupId, clazzId);
			homeworkClassGroupService.updateStudentCount(groupId, addStudentIds.size());

			map.put("errorStudentIds", errorStudentIds);
			return new Value(map);
		} catch (AbstractException e) {
			return new Value(e);
		}
	}
}