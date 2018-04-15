package com.lanking.uxb.service.teachersDay01.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01Tag;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01TeacherTag;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01StudentTagTeacherService;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TagService;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TeacherTagService;
import com.lanking.uxb.service.teachersDay01.convert.TeachersDayActiviy01TagConvert;
import com.lanking.uxb.service.teachersDay01.convert.TeachersDayActiviy01TeacherTagConvert;
import com.lanking.uxb.service.teachersDay01.value.VTeachersDayActiviy01TeacherTag;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.cache.TeacherOperationCacheService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 教师节活动--学生相关接口
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/m/s/teaDays")
public class ZyMStuTeacherDay01Controller {

	@Autowired
	private ZyHomeworkStudentClazzService zyHomeworkStudentClazzService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private TeachersDayActiviy01TagService teachersDayActiviy01TagService;
	@Autowired
	private TeachersDayActiviy01StudentTagTeacherService stuTagTeaService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TeachersDayActiviy01TagConvert teachersDayActiviy01TagConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private TeachersDayActiviy01TeacherTagService teacherTagService;
	@Autowired
	private TeachersDayActiviy01TeacherTagConvert teacherTagConvert;
	@Autowired
	private ShareLogService shareLogService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private TeacherOperationCacheService teacherOperationCacheService;

	private static final Long ACTIVITYCODE = 795646319942180865L;

	/**
	 * 选择老师页<br>
	 * 页面只会在用户加入多个不同老师的班级时出现 只有同一个老师的班级或无班级的用户直接跳过该页面<br>
	 * 只有一个老师或者没有老师，分享和未分享逻辑都在这个接口里完成
	 * 
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		Map<String, Object> data = new HashMap<String, Object>();
		List<HomeworkStudentClazz> clazzList = zyHomeworkStudentClazzService
				.listCurrentClazzsHasTeacher(Security.getUserId());
		Set<Long> teacherIds = new HashSet<Long>();
		List<HomeworkClazz> newClassList = new ArrayList<HomeworkClazz>();
		if (CollectionUtils.isNotEmpty(clazzList)) {
			List<Long> clazzIds = new ArrayList<Long>();
			for (HomeworkStudentClazz clazz : clazzList) {
				clazzIds.add(clazz.getClassId());
			}
			Map<Long, HomeworkClazz> classMap = zyHomeworkClassService.mget(clazzIds);
			for (Long clazzId : clazzIds) {
				HomeworkClazz clazz = classMap.get(clazzId);
				if (!teacherIds.contains(clazz.getTeacherId())) {
					if (newClassList.size() < 6) {
						newClassList.add(clazz);
					}
				}
				teacherIds.add(clazz.getTeacherId());
			}
		}
		data.put("teacherNum", teacherIds.size());
		// 没有班级和只有一个老师的区别，页面显示不一样
		if (CollectionUtils.isEmpty(clazzList)) {
			List<Long> stuTagTeaList = stuTagTeaService.findTagList(Security.getUserId(), 0L);
			if (CollectionUtils.isEmpty(stuTagTeaList)) {
				List<TeachersDayActiviy01Tag> tagList = teachersDayActiviy01TagService.findList(Sex.MALE);
				data.put("tagInitList", teachersDayActiviy01TagConvert.to(tagList));
			} else {
				// 已标记
				data.put("tagSelList",
						teachersDayActiviy01TagConvert.to(teachersDayActiviy01TagService.mgetList(stuTagTeaList)));
				data.put("biz", Biz.TEACHERS_DAY);
				data.put("bizId", 0);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("p0", 0);
				data.put("extend", map);
				if (shareLogService.isShare(Biz.ACTIVITY, ACTIVITYCODE, Security.getUserId(), String.valueOf(0))) {
					data.put("share", true);
				} else {
					data.put("share", false);
				}
			}
			return new Value(data);
		}
		List<Long> teaIds = new ArrayList<Long>();
		teaIds.addAll(teacherIds);
		// 一个老师
		if (teaIds.size() == 1) {
			Long teacherId = teaIds.get(0);
			data.put("teacherId", teacherId);
			data.put("avatarUrl", userConvert.get(teacherId).getAvatarUrl());
			// 判断是否已经打过标签
			List<Long> stuTagTeaList = stuTagTeaService.findTagList(Security.getUserId(), teacherId);
			Teacher teacher = (Teacher) teacherService.getUser(teacherId);
			// 未标记
			if (CollectionUtils.isEmpty(stuTagTeaList)) {
				List<TeachersDayActiviy01Tag> tagList = teachersDayActiviy01TagService.findList(teacher.getSex());
				data.put("tagInitList", teachersDayActiviy01TagConvert.to(tagList));
			} else {
				// 已标记
				data.put("tagSelList",
						teachersDayActiviy01TagConvert.to(teachersDayActiviy01TagService.mgetList(stuTagTeaList)));
				data.put("tagNum", stuTagTeaService.numberOfSetTag(teacherId));
				data.put("teacherName", teacher.getName().substring(0, 1) + "老师");
				List<TeachersDayActiviy01TeacherTag> tags = teacherTagService.getByTeacher(teacherId);
				List<VTeachersDayActiviy01TeacherTag> vtags = teacherTagConvert.to(tags);
				data.put("tags", teacherTagConvert.sort(vtags));
				data.put("biz", Biz.TEACHERS_DAY);
				data.put("bizId", teacherId);
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("p0", teacherId);
				data.put("extend", map);
				// 判断是否已分享
				if (shareLogService.isShare(Biz.ACTIVITY, ACTIVITYCODE, Security.getUserId(), teacherId.toString())) {
					data.put("share", true);
				} else {
					data.put("share", false);
				}
			}
		} else {
			// 多个老师，多个班级
			Map<Long, User> userMap = userService.getUsers(teacherIds);
			List<Map<String, Object>> teacherList = new ArrayList<Map<String, Object>>();
			for (HomeworkClazz clazz : newClassList) {
				Map<String, Object> map = new HashMap<String, Object>();
				String teacherName = userMap.get(clazz.getTeacherId()).getName();
				map.put("teacherName", teacherName.substring(0, 1) + "老师");
				map.put("teacherId", clazz.getTeacherId());
				map.put("className", clazz.getName());
				map.put("classId", clazz.getId());
				teacherList.add(map);
			}
			data.put("teacherList", teacherList);
		}
		return new Value(data);
	}

	/**
	 * 查询老师对应的标签
	 * 
	 * @param teacherId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryTag", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryTag(Long teacherId) {
		if (teacherId == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> data = new HashMap<String, Object>();

		// 判断是否已经打过标签
		List<Long> stuTagTeaList = stuTagTeaService.findTagList(Security.getUserId(), teacherId);
		Teacher teacher = (Teacher) teacherService.getUser(teacherId);
		// 未标记
		if (CollectionUtils.isEmpty(stuTagTeaList)) {
			List<TeachersDayActiviy01Tag> tagList = teachersDayActiviy01TagService.findList(teacher.getSex());
			data.put("tagInitList", teachersDayActiviy01TagConvert.to(tagList));
		} else {
			// 已标记
			data.put("tagSelList",
					teachersDayActiviy01TagConvert.to(teachersDayActiviy01TagService.mgetList(stuTagTeaList)));
			data.put("tagNum", stuTagTeaService.numberOfSetTag(teacherId));
			data.put("teacherName", teacher.getName().substring(0, 1) + "老师");
			data.put("avatarUrl", userConvert.get(teacherId).getAvatarUrl());
			List<TeachersDayActiviy01TeacherTag> tags = teacherTagService.getByTeacher(teacherId);
			List<VTeachersDayActiviy01TeacherTag> vtags = teacherTagConvert.to(tags);
			data.put("tags", teacherTagConvert.sort(vtags));
			data.put("biz", Biz.TEACHERS_DAY);
			data.put("bizId", teacherId);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("p0", teacherId);
			data.put("extend", map);
			// 判断是否已分享
			if (shareLogService.isShare(Biz.ACTIVITY, ACTIVITYCODE, Security.getUserId(), teacherId.toString())) {
				data.put("share", true);
			} else {
				data.put("share", false);
			}
		}
		return new Value(data);
	}

	/**
	 * 判断当前是否分享
	 * 
	 * @param teacherId
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "isShare", method = { RequestMethod.POST, RequestMethod.GET })
	public Value isShare(Long teacherId) {
		return new Value(
				shareLogService.isShare(Biz.ACTIVITY, ACTIVITYCODE, Security.getUserId(), teacherId.toString()));
	}

	/**
	 * 给老师打标签
	 * 
	 * @param tagCodes
	 *            tagcode集合
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "setTag", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setTag(@RequestParam(value = "tagCodes", required = false) List<Long> tagCodes, Long teacherId) {
		if (CollectionUtils.isEmpty(tagCodes)) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_TEACHERSDAY_TAG_NULL));
		}
		if (tagCodes.size() > 4) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_TEACHERSDAY_TAG_MAXLIMIT));
		}

		if (teacherId != null && teacherId > 0) {
			List<TeachersDayActiviy01TeacherTag> tags = teacherTagService.getByTeacher(teacherId);
			// 取不到标签，说明是第一次贴
			if (CollectionUtils.isEmpty(tags)) {
				// 推送
				List<String> tokens = deviceService.findTokenByUserIds(Lists.newArrayList(teacherId), Product.YOOMATH);
				if (CollectionUtils.isNotEmpty(tokens)) {
					String h5page = Env.getString("activity.teachersday.tintroduce.h5.url");
					messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens, 12000024,
							new HashMap<String, Object>(), h5page));
				}

				// 短信
				String mobile = userProfileConvert.get(teacherId).getAccount().getMobile();
				if (mobile != null) {
					messageSender.send(new SmsPacket(mobile, 10000028));
				}
			}
		}

		stuTagTeaService.setTag(tagCodes, Security.getUserId(), teacherId);

		return new Value();
	}

	/**
	 * 存储分享链接字串参数.
	 * 
	 * @param p
	 *            参数
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "saveParams", method = { RequestMethod.POST, RequestMethod.GET })
	public Value saveParams(String p) {
		if (StringUtils.isBlank(p)) {
			return new Value(new MissingArgumentException());
		}
		String id = String.valueOf(System.currentTimeMillis());
		teacherOperationCacheService.setActivityShareParams(id, p);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("id", id);
		return new Value(map);
	}

	/**
	 * 获取存储分享链接字串参数.
	 * 
	 * @param id
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "getParams", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getParams(String id) {
		if (StringUtils.isBlank(id)) {
			return new Value(new MissingArgumentException());
		}
		String params = teacherOperationCacheService.getActivityShareParams(id);
		Map<String, String> map = new HashMap<String, String>(1);
		map.put("params", params);
		return new Value(map);
	}
}
