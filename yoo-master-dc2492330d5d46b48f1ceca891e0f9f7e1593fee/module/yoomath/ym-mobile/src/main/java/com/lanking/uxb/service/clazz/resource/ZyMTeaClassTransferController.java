package com.lanking.uxb.service.clazz.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.base.message.api.PushPacket;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.push.type.OpenPath;
import com.lanking.uxb.service.push.util.YmPushUrls;
import com.lanking.uxb.service.session.api.DeviceService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClazzTransferService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;

/**
 * 老师班级转让
 * 
 * @author wangsenhao
 * @version 2017年7月13日
 *
 */
@RestController
@RequestMapping(value = "zy/m/class/transfer")
public class ZyMTeaClassTransferController {

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private ZyHomeworkClazzTransferService transferService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private DeviceService deviceService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private ZyHomeworkService hkService;

	/**
	 * 查询老师列表(当前学校渠道用户)
	 * 
	 * @param str
	 *            老师姓名/手机号(前者模糊匹配，后者精确匹配)
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "findTeacherList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value findTeacherList(String str) {
		Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> data = new HashMap<String, Object>();
		if (teacher.getSchoolId() == null) {
			return new Value(data);
		}
		String pattern = "^1[\\d]{10}";
		List<Long> teacherList = new ArrayList<Long>();
		if (str == null) {
			teacherList = transferService.findUserList(null, null, teacher.getSchoolId(), Security.getUserId());
		} else {
			// 判断是否是手机号
			if (Pattern.matches(pattern, str)) {
				teacherList = transferService.findUserList(null, str, teacher.getSchoolId(), Security.getUserId());
			} else {
				teacherList = transferService.findUserList(str, null, teacher.getSchoolId(), Security.getUserId());
			}
		}
		Map<Long, VUserProfile> map = userProfileConvert.mget(teacherList);
		List<VUserProfile> list = new ArrayList<VUserProfile>();
		for (Long key : map.keySet()) {
			list.add(map.get(key));
		}
		data.put("teacherList", list);
		data.put("schoolName", schoolService.get(teacher.getSchoolId()).getName());
		return new Value(data);
	}

	/**
	 * 确认转让班级
	 * 
	 * @param classId
	 * @param toTeacherId
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit", method = { RequestMethod.POST, RequestMethod.GET })
	public Value transfer(Long classId, Long toTeacherId) {
		Long count = classService.currentCount(toTeacherId);
		if (count >= 5) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_TRANSFER_CLASS_MAXLIMIT));
		}
		HomeworkClazz clazz = classService.get(classId);
		Long oldCreateId = clazz.getTeacherId();
		if (clazz.getStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(
					YoomathMobileException.YOOMATH_MOBILE_TRANSFER_CLAZZ_REQUEST_CLOSE));
		}
		transferService.classTransfer(classId, Security.getUserId(), toTeacherId);
		VUser oldTeacher = userConvert.get(oldCreateId);
		// 推送
		List<String> tokens = deviceService.findTokenByUserIds(Lists.newArrayList(toTeacherId), Product.YOOMATH);
		if (CollectionUtils.isNotEmpty(tokens)) {
			messageSender.send(new PushPacket(Product.YOOMATH, YooApp.MATH_TEACHER, tokens, 12000023, ValueMap.value(
					"teacherName", oldTeacher.getName()), YmPushUrls.url(YooApp.MATH_TEACHER,
					OpenPath.CLASSMANAGER_HOME, null), ValueMap.value("className", clazz.getName()).put("teacherName",
					oldTeacher.getName())));
		}
		// 短信
		String mobile = userProfileConvert.get(toTeacherId).getAccount().getMobile();
		if (mobile != null) {
			messageSender.send(new SmsPacket(userProfileConvert.get(toTeacherId).getAccount().getMobile(), 10000027,
					ValueMap.value("teacherName", oldTeacher.getName()).put("className", clazz.getName())));
		}
		return new Value();
	}
}
