package com.lanking.uxb.service.clazz.resource;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzTransfer;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentHomeworkStat;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.code.StatusCode;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.web.resource.ZyTeaClassManage2Controller;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupStudentService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClazzTransferService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatisticService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzGroupConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyStudentHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;
import com.lanking.uxb.service.zuoye.value.VHomeworkStat;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;
import com.lanking.uxb.service.zuoye.value.VStudentHomeworkStat;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * 班级管理相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月11日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/class")
public class ZyMTeaClassManageController {

	@Autowired
	private ZyTeaClassManage2Controller teaClassManage2Controller;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private ZyStudentHomeworkStatService zyStuHkStatService;
	@Autowired
	private ZyStudentHomeworkStatConvert zyStuHkStatConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassGroupService groupService;
	@Autowired
	private ZyHomeworkClazzGroupConvert groupConvert;
	@Autowired
	private ZyHomeworkClassGroupStudentService groupStudentService;
	@Autowired
	private ZyHomeworkStatisticService homeworkStatisticService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coninsService;
	@Autowired
	private UserService userService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZyHomeworkClazzTransferService transferService;
	@Autowired
	MqSender mqSender;

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "create", method = { RequestMethod.POST, RequestMethod.GET })
	public Value create(String name) {
		Value value = teaClassManage2Controller.createClass(name);
		if (value.getRet_code() == StatusCode.SUCCEED) {
			return value;
		} else {
			if (value.getRet_code() == ZuoyeException.ZUOYE_CLASS_MAXLIMIT) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASS_MAXLIMIT));
			} else if (value.getRet_code() == ZuoyeException.ZUOYE_CLASSNAME_EXIST) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASSNAME_EXIST));
			} else {
				return value;
			}
		}
	}

	/**
	 * 查询班级列表
	 * 
	 * @since 2.0.3
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query() {
		List<HomeworkClazz> hkClazzs = hkClassService.listCurrentClazzs(Security.getUserId());
		ValueMap vm = ValueMap.value();
		if (CollectionUtils.isEmpty(hkClazzs)) {
			vm.put("items", Collections.EMPTY_LIST);
		} else {
			List<VHomeworkClazz> items = hkClassConvert.to(hkClazzs,
					new ZyHomeworkClassConvertOption(false, true, false));
			for (VHomeworkClazz item : items) {
				if (item.getHomeworkStat() == null) {
					VHomeworkStat vhomeworkStat = new VHomeworkStat();
					vhomeworkStat.setClassId(item.getId());
					vhomeworkStat.setHomeworkClassId(item.getId());
					vhomeworkStat.setUserId(Security.getUserId());
					// @since v1.3.0 SSS-2019优化
					vhomeworkStat.setRightRate(null);
					item.setHomeworkStat(vhomeworkStat);
				}
			}
			vm.put("items", items);
		}
		HomeworkClazzTransfer transfer = transferService.getLastest(Security.getUserId());
		if (transfer != null) {
			transferService.read(transfer.getId(), Security.getUserId());
		}
		return new Value(vm);
	}

	/**
	 * 班级详情
	 * 
	 * @since 2.0.3
	 * @param clazzId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long clazzId) {
		VHomeworkClazz clazz = hkClassConvert.to(hkClassService.get(clazzId),
				new ZyHomeworkClassConvertOption(false, true, false));
		if (clazz.getHomeworkStat() == null) {
			VHomeworkStat vhomeworkStat = new VHomeworkStat();
			vhomeworkStat.setClassId(clazz.getId());
			vhomeworkStat.setHomeworkClassId(clazz.getId());
			vhomeworkStat.setUserId(Security.getUserId());
			vhomeworkStat.setRightRate(null);
			vhomeworkStat.setCompletionRate(null);
			clazz.setHomeworkStat(vhomeworkStat);
		}
		ValueMap vm = ValueMap.value("clazz", clazz);
		// 分享班级h5 @since v1.3.0
		// 教师名
		User user = userService.get(Security.getUserId());
		String teacherName = user.getName().substring(0, 1) + "老师";
		vm.put("teacherName", teacherName);
		vm.put("shareUrl", getShareUrl(teacherName, clazz.getName(), clazz.getCode()));

		// 学生信息
		List<HomeworkStudentClazz> list = hkStuClazzService.list(clazzId);
		if (CollectionUtils.isNotEmpty(list)) {
			List<VHomeworkStudentClazz> items = hkStuClazzConvert.to(list);
			List<Long> studentIds = new ArrayList<Long>(items.size());
			for (VHomeworkStudentClazz v : items) {
				studentIds.add(v.getStudentId());
			}
			List<StudentHomeworkStat> studentHomeworkStats = zyStuHkStatService.find(clazzId, studentIds);
			List<VStudentHomeworkStat> vstudentHomeworkStats = zyStuHkStatConvert.to(studentHomeworkStats);
			Map<Long, VStudentHomeworkStat> map = new HashMap<Long, VStudentHomeworkStat>(vstudentHomeworkStats.size());
			Map<Long, VUser> users = userConvert.mget(studentIds, new UserConvertOption(true));
			for (VStudentHomeworkStat v : vstudentHomeworkStats) {
				v.setUser(users.get(v.getUserId()));
				map.put(v.getUserId(), v);
			}
			List<HomeworkClazzGroup> groups = groupService.groups(clazz.getId());
			Map<Long, HomeworkClazzGroupStudent> studentGroupMap = new HashMap<Long, HomeworkClazzGroupStudent>(
					studentIds.size());
			Map<Long, HomeworkClazzGroup> groupMap = new HashMap<Long, HomeworkClazzGroup>(groups.size());
			if (CollectionUtils.isNotEmpty(groups)) {
				List<HomeworkClazzGroupStudent> groupStudents = groupStudentService.findByStusAndClass(studentIds,
						clazz.getId());

				for (HomeworkClazzGroupStudent s : groupStudents) {
					studentGroupMap.put(s.getStudentId(), s);
				}

				vm.put("groups", groupConvert.to(groups));

				for (HomeworkClazzGroup g : groups) {
					groupMap.put(g.getId(), g);
				}
			}
			for (VHomeworkStudentClazz v : items) {
				VStudentHomeworkStat stat = map.get(v.getStudentId());
				if (stat == null) {
					stat = new VStudentHomeworkStat();
					stat.setHomeworkClassId(clazzId);
					stat.setUserId(v.getStudentId());
					stat.setUser(users.get(v.getStudentId()));
					stat.setRightRate(null);
				}
				v.setHomeworkStat(stat);
				if (studentGroupMap.get(v.getStudentId()) != null) {
					HomeworkClazzGroup g = groupMap.get(studentGroupMap.get(v.getStudentId()).getGroupId());
					if (g != null) {
						VHomeworkClazzGroup vg = new VHomeworkClazzGroup();
						vg.setId(g.getId());
						vg.setName(g.getName());

						v.setGroup(vg);
					}
				}
			}
			dealNameSort(items); // 排序
			vm.put("students", items);

		} else {
			vm.put("students", Collections.EMPTY_LIST);
		}
		return new Value(vm);
	}

	public void dealNameSort(List<VHomeworkStudentClazz> list) {
		Collections.sort(list, new Comparator<VHomeworkStudentClazz>() {

			@Override
			public int compare(VHomeworkStudentClazz o1, VHomeworkStudentClazz o2) {
				String name1 = o1.getHomeworkStat().getUser().getName();
				if (name1 != null) {
					if (containsEmoji(name1)) {
						name1 = filterEmoji(name1);
					}
				}
				String name2 = o2.getHomeworkStat().getUser().getName();
				if (name2 != null) {
					if (containsEmoji(name2)) {
						name2 = filterEmoji(name2);
					}
				}
				
				String getFullSpell1 = this.getFullSpell(name1);
				String getFullSpell2 = this.getFullSpell(name2);

				return getFullSpell1.compareTo(getFullSpell2);
			}

			/**
			 * 获取汉字串拼音首字母，英文字符不变
			 * 
			 * @param chinese
			 *            汉字串
			 * @return 汉语拼音首字母
			 */
			@SuppressWarnings("unused")
			public String getFirstSpell(String chinese) {
				StringBuffer pybf = new StringBuffer();
				char[] arr = chinese.toCharArray();
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
				defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				for (int i = 0; i < arr.length; i++) {
					if (arr[i] > 128) {
						try {
							String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
							if (temp != null) {
								pybf.append(temp[0].charAt(0));
							}
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							e.printStackTrace();
						}
					} else {
						pybf.append(arr[i]);
					}
				}
				return pybf.toString().replaceAll("\\W", "").trim();
			}

			/**
			 * 获取汉字串拼音，英文字符不变
			 * 
			 * @param chinese
			 *            汉字串
			 * @return 汉语拼音
			 */
			public String getFullSpell(String chinese) {
				StringBuffer pybf = new StringBuffer();
				char[] arr = chinese.toCharArray();
				HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
				defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
				defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
				for (int i = 0; i < arr.length; i++) {
					if (arr[i] > 128) {
						try {
							pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
						} catch (BadHanyuPinyinOutputFormatCombination e) {
							e.printStackTrace();
						}
					} else {
						pybf.append(arr[i]);
					}
				}
				return pybf.toString();
			}
		});

	}
	
	/**
	 * 检测是否有emoji字符
	 * 
	 * @param source
	 *            需要判断的字符串
	 * @return 一旦含有就抛出
	 */
	private boolean containsEmoji(String source) {
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (!notisEmojiCharacter(codePoint)) {
				// 判断确认有表情字符
				return true;
			}
		}
		return false;
	}

	/**
	 * 非emoji表情字符判断
	 * 
	 * @param codePoint
	 * @return
	 */
	private boolean notisEmojiCharacter(char codePoint) {
		return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
				|| ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
				|| ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
	}

	/**
	 * 过滤emoji 或者 其他非文字类型的字符
	 * 
	 * @param source
	 *            需要过滤的字符串
	 * @return
	 */
	private String filterEmoji(String source) {
		if (!containsEmoji(source)) {
			return source;// 如果不包含，直接返回
		}

		StringBuilder buf = null;// 该buf保存非emoji的字符
		int len = source.length();
		for (int i = 0; i < len; i++) {
			char codePoint = source.charAt(i);
			if (notisEmojiCharacter(codePoint)) {
				if (buf == null) {
					buf = new StringBuilder(source.length());
				}
				buf.append(codePoint);
			}
		}

		if (buf == null) {
			return "";// 如果没有找到非emoji的字符，则返回无内容的字符串
		} else {
			if (buf.length() == len) {
				buf = null;
				return source;
			} else {
				return buf.toString();
			}
		}
	}

	/**
	 * 重命名班级
	 * 
	 * @since 2.0.3
	 * @param clazzId
	 *            班级ID
	 * @param name
	 *            班级名称
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "rename", method = { RequestMethod.POST, RequestMethod.GET })
	public Value rename(long clazzId, String name) {
		try {
			if (StringUtils.isBlank(name)) {
				return new Value(new IllegalArgException());
			}
			ZyHomeworkClassConvertOption option = new ZyHomeworkClassConvertOption(false, true, false);
			VHomeworkClazz clazz = hkClassConvert.to(hkClassService.updateName(name, clazzId, Security.getUserId()),
					option);
			if (clazz.getHomeworkStat() == null) {
				VHomeworkStat vhomeworkStat = new VHomeworkStat();
				vhomeworkStat.setClassId(clazz.getId());
				vhomeworkStat.setHomeworkClassId(clazz.getId());
				vhomeworkStat.setUserId(Security.getUserId());
				vhomeworkStat.setRightRate(null);
				vhomeworkStat.setCompletionRate(null);
				clazz.setHomeworkStat(vhomeworkStat);
			}
			return new Value(clazz);
		} catch (ZuoyeException e) {
			if (e.getCode() == ZuoyeException.ZUOYE_CLASSNAME_EXIST) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASSNAME_EXIST));
			}
			return new Value(e);
		} catch (Exception e) {
			return new Value(e);
		}
	}

	/**
	 * 删除班级
	 * 
	 * @since 2.0.3
	 * @param clazzId
	 *            班级ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(long clazzId) {
		try {
			hkClassService.delete(clazzId, Security.getUserId());

			return new Value();
		} catch (ZuoyeException e) {
			return new Value(e);
		}
	}

	/**
	 * 设置加入方式
	 *
	 * @param lockStatus
	 *            班级锁定设置
	 * @param needConfirm
	 *            是否需要验证才可以加入班级
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "setJoinRequest", method = { RequestMethod.GET, RequestMethod.POST })
	public Value setJoinStatus(Status lockStatus, Boolean needConfirm, long classId) {
		if (classId <= 0 || (lockStatus == null && needConfirm == null)) {
			return new Value(new IllegalArgException());
		}
		if (lockStatus != null) {
			if (lockStatus == Status.DISABLED) {
				hkClassService.lock(classId, Security.getUserId());
			} else {
				hkClassService.unlock(classId, Security.getUserId());
				if (needConfirm) {
					hkClassService.needConfirm(classId, Security.getUserId());
				} else {
					hkClassService.notNeedConfirm(classId, Security.getUserId());
				}
			}
		} else {
			if (needConfirm) {
				hkClassService.unlock(classId, Security.getUserId());
				hkClassService.needConfirm(classId, Security.getUserId());
			} else {
				hkClassService.notNeedConfirm(classId, Security.getUserId());
			}
		}

		return new Value();
	}

	/**
	 * 移除班级内学生
	 *
	 * @since 教师端 v1.3.0 2017-7-5 学生退出班级不再参与班级的整体统计，班级统计来源于学生统计
	 * 
	 * @param classId
	 *            班级id
	 * @param studentIds
	 *            学生id列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "removeStudents", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeStudents(long classId, @RequestParam(value = "studentIds") List<Long> studentIds) {
		if (classId <= 0 || CollectionUtils.isEmpty(studentIds)) {
			return new Value(new IllegalArgException());
		}

		try {
			hkStuClazzService.exit(classId, studentIds, Security.getUserId());

			// 学生被移除班级，需要重新计算班级整体统计
			homeworkStatisticService.updateTeacherHomeworkStatByClass(classId);

			// 学生被移除班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
			for (Long studentId : studentIds) {
				JSONObject object = new JSONObject();
				object.put("classId", classId);
				object.put("studentId", studentId);
				mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
						MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_LEAVE,
						MQ.builder().data(object).build());
			}
		} catch (ZuoyeException e) {
			if (e.getCode() == ZuoyeException.ZUOYE_CLASS_NOT_EXIST) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASS_NOT_EXISTS));
			}
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value();
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "2/create", method = { RequestMethod.POST, RequestMethod.GET })
	public Value create2(String name, int schoolYear) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			HomeworkClazz homeworkClazz = zyHkClassService.createByMobile(name, Security.getUserId(), schoolYear);
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
			// 教师名
			User user = userService.get(Security.getUserId());
			String teacherName = user.getName().substring(0, 1) + "老师";
			data.put("teacherName", teacherName);
			data.put("shareUrl", getShareUrl(teacherName, homeworkClazz.getName(), homeworkClazz.getCode()));

			return new Value(data);
		} catch (ZuoyeException e) {
			if (e.getCode() == ZuoyeException.ZUOYE_CLASS_MAXLIMIT) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASS_MAXLIMIT));
			} else if (e.getCode() == ZuoyeException.ZUOYE_CLASSNAME_EXIST) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_CLASSNAME_EXIST));
			} else {
				return new Value(e);
			}
		} catch (Exception e) {
			// 防止有唯一键的约束，正常不会出现
			return new Value(e);
		}
	}

	/**
	 * 分享班级h5页面
	 * 
	 * @param teacherName
	 * @param name
	 * @param code
	 */
	private String getShareUrl(String teacherName, String name, String code) {
		// 分享h5URL
		try {
			String[] args = new String[3];
			args[0] = URLEncoder.encode(teacherName, "UTF-8");
			args[1] = URLEncoder.encode(name, "UTF-8");
			args[2] = URLEncoder.encode(code, "UTF-8");
			Parameter shareUrl = parameterService.get(Product.YOOMATH, "tea.createClass.share.h5.url", args);

			return shareUrl.getValue();
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "getSchoolYearList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getSchoolYearList() {
		Map<String, Object> data = new HashMap<String, Object>();
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		List<Integer> yearList = new ArrayList<>();
		for (int i = 0; i <= 3; i++) {
			yearList.add(year - i);
		}

		data.put("year", yearList);
		return new Value(data);
	}
}
