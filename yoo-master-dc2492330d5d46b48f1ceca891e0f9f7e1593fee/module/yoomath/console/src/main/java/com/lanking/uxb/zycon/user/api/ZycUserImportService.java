package com.lanking.uxb.zycon.user.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserSettings;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.user.form.UserForm;
import com.lanking.uxb.zycon.user.value.VZycUserImport;

public interface ZycUserImportService {
	/**
	 * 获取excel的内容,发现不合格数据,直接失败
	 * 
	 * @param request
	 * @return
	 */
	Map<String, Object> getWb(HttpServletRequest request);

	/**
	 * 
	 * 保存数据
	 * 
	 * @param list
	 *            excel解析出来的内容
	 */
	String save(List<VZycUserImport> list, Long createId) throws Exception;

	/**
	 * 检验导入数据,返回检测结果和失败原因
	 * 
	 * @param v
	 *            excel数据集合
	 * @return
	 */
	Map<String, Object> checkImportDatas(VZycUserImport v);

	/**
	 * 查询用户
	 * 
	 * @param form
	 * @return
	 */
	Page<Map> query(UserQuery form, Pageable p);

	/**
	 * 保存或编辑用户
	 * 
	 * @param form
	 */
	void saveUser(UserForm form);

	/**
	 * 查询当前账号名是否存在
	 * 
	 * @param accountName
	 * @return
	 */
	List<Account> getAccounts(String accountName);

	/**
	 * 启用或禁用
	 * 
	 * @param form
	 */
	void updateStatus(UserForm form);

	/**
	 * 通过邮箱,手机号获取账号
	 * 
	 * @param type
	 * @param value
	 * @return
	 */
	List<Account> getAccountsByType(GetType type, String value);

	/**
	 * 获取用户短信提醒
	 * 
	 * @param userId
	 * @return
	 */
	UserSettings getUs(Long userId);

	/**
	 * 获取老师有效班级数,每个老师最多只能有5个有效班级
	 * 
	 * @param teacherId
	 * @return
	 */
	Long getClassNumByTeacher(Long teacherId);

	/**
	 * 通过省、市、区县等查询对应的区域信息
	 * 
	 * @param name
	 * @return
	 */
	List<District> getDistrictByName(String name);

	/**
	 * 判断当前账号是否学生账号
	 * 
	 * @param accountId
	 * @return
	 */
	Boolean isStudent(Long accountId);

	void updateUserChannel(long userId, int channelCode);
	
	void updateTeacherName(List<Teacher> teachers,List<User> users);
	
	Long getTeachersCount();
	
	List<Teacher> getTeachers(int start,int num);
	
	List<User> getUsers(List<Long> ids);
	
	List<Account> getAccountsByIds(List<Long> ids);

	/**
	 * 判断当前的渠道code是否是合法的
	 * 
	 * @param channelCode
	 * @return
	 */
	boolean isLegalChannel(Integer channelCode);

	/**
	 * 学校和渠道是否存在对应关系
	 * 
	 * @param channelCode
	 * @param schoolId
	 * @return
	 */
	boolean isLegalSchoolChannel(Integer channelCode, Long schoolId);

}
