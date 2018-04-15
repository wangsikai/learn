package com.lanking.uxb.service.clazz.resource;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.HomeworkClazzGroupForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 教师班级组管理
 *
 * @author xinyu.zhou
 * @since 3.9.3
 */
@RestController
@RequestMapping(value = "zy/m/t/class/group")
public class ZyMTeaClassGroupManageController {
	@Autowired
	private ZyHomeworkClassGroupService classGroupService;

	/**
	 * 创建班级分组
	 *
	 * @param form
	 *            {@link HomeworkClazzGroupForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(HomeworkClazzGroupForm form) {
		try {
			form.setTeacherId(Security.getUserId());
			List<Long> notSuccessStuIds = classGroupService.create(form);

			Map<String, Object> retMap = new HashMap<String, Object>(1);
			retMap.put("failList",notSuccessStuIds);

			return new Value(retMap);
		} catch (ZuoyeException e) {
			// 超出当前班级最大限度
			if (e.getCode() == ZuoyeException.ZUOYE_CLASS_GROUP_MAXLIMIT) {
				return new Value(new YoomathMobileException(
				        YoomathMobileException.YOOMATH_MOBILE_CLASS_GROUP_MAXLIMIT));
			} else if (e.getCode() == ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS) {
				// 小组名称已经存在
				return new Value(new YoomathMobileException(
				        YoomathMobileException.YOOMATH_MOBILE_CLASS_GROUP_NAME_EXISTS));
			}
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 更新班级分组名
	 *
	 * @param id
	 *            小组id
	 * @param name
	 *            名称
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "updateName", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateName(long id, String name) {
		if (id <= 0 || StringUtils.isBlank(name) || StringUtils.getJsUnicodeLength(name) > 20) {
			return new Value(new IllegalArgException());
		}

		try {
			classGroupService.updateGroupName(id, name);
		} catch (ZuoyeException e) {
			if (e.getCode() == ZuoyeException.ZUOYE_CLASS_GROUP_NAME_EXISTS) {
				return new Value(new YoomathMobileException(
				        YoomathMobileException.YOOMATH_MOBILE_CLASS_GROUP_NAME_EXISTS));
			}
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

	/**
	 * 添加学生至指定班级学生组
	 *
	 * @param id
	 *            学生组id
	 * @param studentIds
	 *            学生id列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "addStudentToGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addStudentToGroup(long id, @RequestParam(value = "studentIds") List<Long> studentIds) {
		try {
			classGroupService.addStudents(id, studentIds);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 删除分组
	 *
	 * @param id
	 *            分组id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "deleteGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value deleteGroup(long id) {
		try {
			classGroupService.removeGroup(id);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	/**
	 * 移除分组里的学生
	 *
	 * @param id
	 *            分组id
	 * @param studentIds
	 *            需要移除的学生列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "removeStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeStudents(long id, @RequestParam(value = "studentIds") List<Long> studentIds) {
		try {
			classGroupService.removeStudents(id, studentIds);
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

}
