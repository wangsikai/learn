package com.lanking.uxb.zycon.user.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.Duty;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.Title;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserSettings;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.code.api.DutyService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.code.convert.DutyConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.TitleConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.user.api.UserQuery;
import com.lanking.uxb.zycon.user.api.ZycUserChannelService;
import com.lanking.uxb.zycon.user.api.ZycUserImportService;
import com.lanking.uxb.zycon.user.form.UserForm;
import com.lanking.uxb.zycon.user.value.VZycUser;
import com.lanking.uxb.zycon.user.value.VZycUserImport;
import com.lanking.uxb.zycon.user.value.VZycUserOther;

/**
 * 悠数学管控台用户管理
 * 
 * @since yoomath V1.4
 * @author wangsenhao
 * @version 2015年9月29日
 */
@RestController
@RequestMapping("zyc/userManage")
public class ZycUserImportController {
	private Logger logger = LoggerFactory.getLogger(ZycUserImportController.class);
	@Autowired
	private ZycUserImportService zycUserImportService;
	@Autowired
	private TitleService titleService;
	@Autowired
	private DutyService dutyService;
	@Autowired
	private TitleConvert titleConvert;
	@Autowired
	private DutyConvert dutyConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private ZycUserChannelService zycUserChannelService;

	@SuppressWarnings("unchecked")
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "import")
	public Value importUsers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> excelMap = zycUserImportService.getWb(request);
		if (excelMap == null) {
			return new Value(new IllegalArgException());
		}
		if (excelMap.get("dataList") == null) {
			return new Value(new MissingArgumentException());
		}
		if (excelMap.get("result") == "fail") {
			return new Value(excelMap.get("dataList"));
		}
		try {
			zycUserImportService.save((List<VZycUserImport>) excelMap.get("dataList"), Security.getUserId());
		} catch (RuntimeException e) {
			return new Value(e.getMessage());
		}
		return new Value();
	}

	/**
	 * 查询用户信息
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "query")
	public Value query(UserQuery form) {
		if (form.getSchoolName() != null) {
			List<District> districtList = zycUserImportService.getDistrictByName(form.getSchoolName());
			// 为空说明可能是对学校名称的模糊匹配
			if (!CollectionUtils.isEmpty(districtList)) {
				if (districtList.size() > 1) {
					List<Long> temps = new ArrayList<Long>();
					for (District d : districtList) {
						temps.add(d.getCode());
					}
					form.setDistricts(temps);
				} else if (districtList.size() == 1) {
					District district = districtList.get(0);
					String value = String.valueOf(district.getCode());
					if (district.getLevel() == 1) {
						form.setDistrictCodeStr(value.substring(0, 2) + "%");
					} else if (district.getLevel() == 2) {
						form.setDistrictCodeStr(value.substring(0, 4) + "%");
					} else if (district.getLevel() == 3) {
						form.setDistrictCodeStr(value);
					}
				}
				// 匹配上区域，则不需要学校模糊匹配
				form.setSchoolName(null);
			}

		}
		Page<Map> userPage = zycUserImportService.query(form, P.index(form.getPage(), form.getPageSize()));
		VPage<VZycUser> vp = new VPage<VZycUser>();
		int tPage = (int) (userPage.getTotalCount() + form.getPageSize() - 1) / form.getPageSize();
		vp.setPageSize(form.getPageSize());
		vp.setCurrentPage(form.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(userPage.getTotalCount());
		List<VZycUser> vZycUserList = new ArrayList<VZycUser>();
		Set<Integer> channelCodes = Sets.newHashSet();
		List<Title> titleList = titleService.getAll();
		Map<Integer, Title> titleMap = new HashMap<Integer, Title>();
		for (Title t : titleList) {
			titleMap.put(t.getCode(), t);
		}
		List<Duty> dutyList = dutyService.getAll();
		Map<Integer, Duty> dutyMap = new HashMap<Integer, Duty>();
		for (Duty t : dutyList) {
			dutyMap.put(t.getCode(), t);
		}
		List<Long> schoolIds = new ArrayList<Long>();
		for (Map map : userPage.getItems()) {
			VZycUser vZycUser = new VZycUser();
			if (map.get("activation_status") != null) {
				vZycUser.setActivationStatus(Status.findByValue(Integer.parseInt(map.get("activation_status")
						.toString())));
			} else {
				vZycUser.setActivationStatus(Status.DISABLED);
			}
			vZycUser.setUserId(Long.parseLong(map.get("id").toString()));
			vZycUser.setUserType(UserType.findByValue(Integer.parseInt(map.get("user_type").toString())));
			if (map.get("sex") != null) {
				vZycUser.setSex(Sex.findByValue(Integer.parseInt(map.get("sex").toString())));
			} else {
				vZycUser.setSex(Sex.UNKNOWN);
			}
			if (map.get("member_type") != null) {
				Integer memberType = Integer.parseInt(map.get("member_type").toString());
				if (memberType == 0) {
					vZycUser.setMemberTypeDes("非会员");
				} else if (memberType == 1) {
					vZycUser.setMemberTypeDes("会员");
				} else if (memberType == 2) {
					vZycUser.setMemberTypeDes("校级会员");
				}
				// 会员开始、结束时间
				if (map.get("start_at") != null) {
					vZycUser.setStartAt((Date) map.get("start_at"));
				}
				if (map.get("end_at") != null) {
					vZycUser.setEndAt((Date) map.get("end_at"));
					if (vZycUser.getEndAt().getTime() < new Date().getTime()) {
						vZycUser.setMemberTypeDes("非会员");
					}
				}
			} else {
				vZycUser.setMemberTypeDes("非会员");
			}

			vZycUser.setStatus(Status.findByValue(Integer.parseInt(map.get("status").toString())));
			vZycUser.setName(String.valueOf(map.get("name")));
			if (map.get("mobile") != null) {
				vZycUser.setMobile(String.valueOf(map.get("mobile")));
			}
			if (map.get("email") != null) {
				vZycUser.setEmail(String.valueOf(map.get("email")));
			}
			if (map.get("school_id") != null) {
				if (Long.parseLong(map.get("school_id").toString()) != 0L) {
					schoolIds.add(Long.parseLong(map.get("school_id").toString()));
					vZycUser.setSchoolId(Long.parseLong(map.get("school_id").toString()));
				}
			}
			vZycUser.setAccountName(String.valueOf(map.get("accountname")));
			if (map.get("phase_code") != null) {
				Integer phaseCode = Integer.parseInt(String.valueOf(map.get("phase_code")));
				vZycUser.setPhaseName(PhaseService.PHASE_HIGH == phaseCode ? "高中" : "初中");
				vZycUser.setPhaseCode(phaseCode);
			}
			vZycUser.setAccountId(Long.parseLong(map.get("account_id").toString()));
			vZycUser.setCreateAt((Date) map.get("create_at"));
			if (map.get("user_channel_code") != null) {
				int channelCode = Integer.parseInt(String.valueOf(map.get("user_channel_code")));
				vZycUser.setChannelCode(channelCode);
				channelCodes.add(channelCode);
			}

			if (UserType.TEACHER == UserType.findByValue(Integer.parseInt(map.get("user_type").toString()))) {
				if (map.get("duty_code") != null) {
					Integer dutyCode = Integer.parseInt(String.valueOf(map.get("duty_code")));
					vZycUser.setDutyCode(dutyCode);
					vZycUser.setDutyName(dutyMap.get(dutyCode) == null ? null : dutyMap.get(dutyCode).getName());
				}
				if (map.get("title_code") != null) {
					Integer titleCode = Integer.parseInt(String.valueOf(map.get("title_code")));
					vZycUser.setTitleCode(titleCode);
					vZycUser.setTitleName(titleMap.get(titleCode) == null ? null : titleMap.get(titleCode).getName());
				}
				if (map.get("workat") != null) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						vZycUser.setWorkAt(sdf.parse(map.get("workat").toString()));
					} catch (Exception e) {
						logger.error("date parse error:", e);
					}
				}
			} else {
				if (map.get("enter_year") != null) {
					vZycUser.setEnterYear(Integer.parseInt(map.get("enter_year").toString()));
				}
			}
			vZycUserList.add(vZycUser);
		}
		if (schoolIds.size() > 0) {
			Map<Long, VSchool> schoolMap = schoolConvert.mget(schoolIds);
			for (VZycUser v : vZycUserList) {
				if (v.getSchoolId() != null && schoolMap.containsKey(v.getSchoolId())) {
					v.setSchoolName(schoolMap.get(v.getSchoolId()).getDistrictName() + " "
							+ schoolMap.get(v.getSchoolId()).getName());
				}
			}
		}
		if (channelCodes.size() > 0) {
			Map<Integer, UserChannel> channelMap = zycUserChannelService.mget(channelCodes);
			for (VZycUser v : vZycUserList) {
				if (v.getChannelCode() != null && channelMap.containsKey(v.getChannelCode())) {
					v.setChannelName(channelMap.get(v.getChannelCode()).getName());
				}
			}
		}
		vp.setItems(vZycUserList);
		return new Value(vp);
	}

	/**
	 * 查看短信提醒
	 * 
	 * @return
	 */
	@RequestMapping(value = "querySms")
	public Value querySms(@RequestParam(value = "userId") Long userId) {
		UserSettings us = zycUserImportService.getUs(userId);
		if (us == null) {
			return new Value(true);
		}
		return new Value(us.isHomeworkSms());
	}

	/**
	 * 禁用或者启用悠数学用户
	 * 
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "operationByStatus")
	public Value operationByStatus(UserForm form) {
		zycUserImportService.updateStatus(form);
		return new Value();
	}

	/**
	 * 保存或编辑
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save")
	public Value save(UserForm form) {
		// 增加的用户需要验证，账号唯一性
		if (form.getIsAdd()) {
			List<Account> accounts = zycUserImportService.getAccounts(form.getAccountName());
			// 账户名已经存在
			if (accounts.size() > 0) {
				return new Value(new AccountException(AccountException.ACCOUNT_NAME_EXIST));
			}
		}
		// 验证邮箱是否存在
		if (form.getEmail() != null && form.getIsCheckEmail()) {
			List<Account> accounts = zycUserImportService.getAccountsByType(GetType.EMAIL, form.getEmail());
			if (accounts.size() > 0) {
				return new Value(new AccountException(AccountException.ACCOUNT_EMAIL_EXIST));
			}
		}
		// 验证手机号是否存在
		if (form.getMobile() != null && form.getIsCheckMobile()) {
			List<Account> accounts = zycUserImportService.getAccountsByType(GetType.MOBILE, form.getMobile());
			if (accounts.size() > 0) {
				return new Value(new AccountException(AccountException.ACCOUNT_MOBILE_EXIST));
			}
		}
		zycUserImportService.saveUser(form);
		return new Value();
	}

	/**
	 * 获取编辑用户页面的下拉列表数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "getInitInfo")
	public Value getInitInfo() {
		VZycUserOther vZycUserOther = new VZycUserOther();
		List<Title> titleList = titleService.getAll();
		List<Duty> dutyList = dutyService.getAll();
		List<Phase> phaseList = phaseService.getAll();
		vZycUserOther.setDutyList(dutyConvert.to(dutyList));
		vZycUserOther.setTitleList(titleConvert.to(titleList));
		vZycUserOther.setPhaseList(phaseConvert.to(phaseList));
		return new Value(vZycUserOther);
	}

	@RequestMapping(value = "updateUserChannel")
	@ConsoleRolesAllowed(systemAdmin = true)
	public Value updateUserChannel(long userId, int channelCode, Long schoolId) {
		try {
			if (!zycUserImportService.isLegalSchoolChannel(channelCode, schoolId)) {
				return new Value(new YoomathConsoleException(YoomathConsoleException.USER_CHANNEL_SCHOOL_ERROR));
			}
			zycUserImportService.updateUserChannel(userId, channelCode);
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}
	
	/**
	 * 判断当前渠道code是否合法
	 * 
	 * @param channelCode
	 * @return
	 */
	@RequestMapping(value = "isLegalChannel")
	public Value isLegalChannel(int channelCode) {
		return new Value(zycUserImportService.isLegalChannel(channelCode));
	}
	
}
