package com.lanking.uxb.service.teachersDay01.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01TeacherTag;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01StudentTagTeacherService;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TeacherTagService;
import com.lanking.uxb.service.teachersDay01.convert.TeachersDayActiviy01TeacherTagConvert;
import com.lanking.uxb.service.teachersDay01.value.VTeachersDayActiviy01TeacherTag;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 教师节活动教师接口
 * 
 * @since yoomath(mobile) V1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年8月28日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/teaDays")
public class ZyMTeaTeachersDay01Controller {

	@Autowired
	private UserService userService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private TeachersDayActiviy01TeacherTagService teacherTagService;
	@Autowired
	private TeachersDayActiviy01TeacherTagConvert teacherTagConvert;
	@Autowired
	private TeachersDayActiviy01StudentTagTeacherService stTeacherService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private TeacherService teacherService;

	/**
	 * 查询老师的标签
	 * 
	 * @since yoomath(mobile) V1.3.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "tags", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getTags() {
		Map<String, Object> data = new HashMap<String, Object>();
		// 教师信息
		VUser vuser = userConvert.to(userService.get(Security.getUserId()));
		vuser.setName(vuser.getName().substring(0, 1) + "老师");
		data.put("userInfo", vuser);
		// 取tags
		List<TeachersDayActiviy01TeacherTag> tags = teacherTagService.getByTeacher(Security.getUserId());
		List<VTeachersDayActiviy01TeacherTag> vtags = teacherTagConvert.to(tags);

		data.put("tags", teacherTagConvert.sort(vtags));
		// 教师是否创建班级
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		data.put("hasClazz", CollectionUtils.isEmpty(clazzs) ? false : true);
		// 打标签人数
		data.put("numberOfSetTag", stTeacherService.numberOfSetTag(Security.getUserId()));
		data.put("biz", Biz.TEACHERS_DAY);
		data.put("bizId", Security.getUserId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("p0", Security.getUserId());
		data.put("extend", map);

		return new Value(data);
	}
}
