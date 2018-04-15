package com.lanking.uxb.ycoorect.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.base.message.api.SmsPacket;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.intercomm.yoocorrect.client.CorrectUserDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectAuthStatus;
import com.lanking.intercomm.yoocorrect.dto.CorrectBillDayData;
import com.lanking.intercomm.yoocorrect.dto.CorrectConfigData;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserStatData;
import com.lanking.intercomm.yoocorrect.service.CorrectBillsDatawayService;
import com.lanking.intercomm.yoocorrect.service.CorrectHomeDatawayService;
import com.lanking.intercomm.yoocorrect.service.CorrectUserDatawayService;
import com.lanking.uxb.core.annotation.LoadCorrectUser;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.account.util.ValidateUtils;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.message.api.MessageSender;
import com.lanking.uxb.service.message.util.VerifyCodes;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.cache.AccountCacheService;
import com.lanking.uxb.ycoorect.ex.YooCorrectException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "ycorrect/user/center")
public class YooCorrectUserCenterController {

	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private AccountCacheService accountCacheService;
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private CorrectUserDatawayService correctUserDatawayService;
	@Autowired
	private CorrectHomeDatawayService correctHomeDatawayService;
	@Autowired
	private CorrectBillsDatawayService correctBillsDatawayService;
	@Autowired
	private CorrectUserDatawayClient correctUserDatawayClient;
	@Autowired
	private ParameterService parameterService;

	/**
	 * 首页数据.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "index")
	public Value index() {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		// 获取用户统计信息
		CorrectUserStatData userStatData = correctUserDatawayService.getUserStatData(correctUser.getId());

		Map<String, Object> map = Maps.newHashMap();
		map.put("balance", correctUser.getBalance()); // 我的余额
		map.put("totalCorrectCount", userStatData.getCorrectCount()); // 总共批改题数
		map.put("qualificationAuthStatus", correctUser.getQualificationAuthStatus()); // 教师资格认证状态
		map.put("idCardAuthStatus", correctUser.getIdCardAuthStatus()); // 实名认证状态
		map.put("mobileAuthStatus",
				StringUtils.isBlank(correctUser.getMobile()) ? CorrectAuthStatus.DEFAULT : CorrectAuthStatus.PASS); // 手机认证状态

		// 学校
		if (correctUser.getSchoolId() != null) {
			VSchool school = schoolConvert.get(correctUser.getSchoolId());
			map.put("school", school); // 学校信息
		}

		// 阶段
		if (correctUser.getPhaseId() != null) {
			VPhase phase = phaseConvert.get(correctUser.getPhaseId().intValue());
			map.put("phase", phase); // 阶段信息
		}

		return new Value(map);
	}

	/**
	 * 我的余额.
	 * 
	 * @param lastBillDate
	 *            上一条账目流水的日期时间戳，不传表示起始页
	 * @param size
	 *            取账目流水的条数
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "myBalance")
	public Value myBalance(Long lastBillDate, Integer size) {
		if (size == null || size < 0) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		Map<String, Object> map = Maps.newHashMap();
		map.put("balance", correctUser.getBalance()); // 我的余额
		map.put("obalance", correctUser.getObalance()); // 可用余额

		// 账目流水
		List<Map<String, Object>> correctBills = new ArrayList<Map<String, Object>>(size);
		List<CorrectBillDayData> correctBillDayDatas = correctBillsDatawayService.queryDayBillData(correctUser.getId(),
				lastBillDate, size);
		if (correctBillDayDatas != null && correctBillDayDatas.size() > 0) {
			for (CorrectBillDayData data : correctBillDayDatas) {
				Map<String, Object> correctBill = Maps.newHashMap();
				correctBill.put("date", data.getDate()); // 流水日期
				correctBill.put("correctCount", data.getCorrectCount()); // 批改题数
				correctBill.put("correctFee", data.getCorrectFee()); // 批改费用
				correctBill.put("rewardFee", data.getRewardFee()); // 奖励费用
				correctBill.put("errorFee", data.getErrorFee()); // 批错扣费
				correctBill.put("withdrawFee", data.getWithdrawFee()); // 提现费用
				correctBills.add(correctBill);
			}
		}
		map.put("correctBills", correctBills); // 仅7天的账目流水

		return new Value(map);
	}

	/**
	 * 获取提现信息.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getWithdrawInfo")
	public Value getWithdrawInfo() {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		// 获取配置数据
		CorrectConfigData configData = correctHomeDatawayService.getConfig();

		Map<String, Object> map = Maps.newHashMap();
		map.put("balance", correctUser.getBalance()); // 我的余额
		map.put("obalance", correctUser.getObalance()); // 可提现余额

		boolean isRightTime = this.checkWithdrawTime(configData);
		map.put("isRightTime", isRightTime); // 是否在有效的提现时间

		if (!isRightTime) {
			// 非提现时间提示
			map.put("wrongTimeTip", "抱歉，当前时间无法提现，你可在每周五9:00~22:00 间取出截止至上周日24:00前的所有未提现收入。当日提现仅限1次，提现总额不能超过800元");
		}

		// 判断当日是否已提现
		map.put("withdrawToday", this.withdrawToday(correctUser.getUserId()));

		map.put("realName", correctUser.getRealname()); // 当前用户的真实姓名
		map.put("alipayNo", StringUtils.defaultIfBlank(correctUser.getAlipayNo())); // 用户支付宝帐号

		return new Value(map);
	}

	/**
	 * 提现申请操作.
	 * 
	 * @param amount
	 *            提现金额
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "withdraw")
	public Value withdraw(BigDecimal amount) {
		if (amount == null || amount.doubleValue() <= 0) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		// 获取配置数据
		CorrectConfigData configData = correctHomeDatawayService.getConfig();

		// 判断是否在提现时间范围内
		boolean isRightTime = this.checkWithdrawTime(configData);
		if (!isRightTime) {
			return new Value(new YooCorrectException(YooCorrectException.NOT_RIGHT_WITHDRAW_TIME));
		}

		// 判断最小提现金额
		if (amount.compareTo(configData.getWithdrawMin()) < 0) {
			return new Value(new YooCorrectException(YooCorrectException.LOW_WITHDRAW_MONEY));
		}

		// 判断最大提现金额
		if (amount.compareTo(correctUser.getObalance()) > 0 || amount.compareTo(configData.getDayWithdrawMax()) > 0) {
			return new Value(new YooCorrectException(YooCorrectException.OUT_WITHDRAW_MONEY));
		}

		// 判断当日是否已提现
		if (this.withdrawToday(correctUser.getUserId())) {
			return new Value(new YooCorrectException(YooCorrectException.OUT_WITHDRAW_TODAY));
		}

		// 与支付宝交互

		return new Value();
	}

	/**
	 * 判断是否在提现时间范围内.
	 * 
	 * @param configData
	 *            配置数据
	 * @return
	 */
	private boolean checkWithdrawTime(CorrectConfigData configData) {
		// 判断提现日
		int weekDay = configData.getWithdrawWeekDay();
		Date bt = configData.getWithdrawBt();
		Date et = configData.getWithdrawEt();

		boolean isRightTime = false;
		Calendar calNow = Calendar.getInstance();
		int thisWeekDay = calNow.get(Calendar.DAY_OF_WEEK) - 1;
		if ((thisWeekDay == 0 && weekDay == 7) || thisWeekDay == weekDay) {
			// 在有效日内
			calNow.setTime(new Date());
			Calendar calBt = Calendar.getInstance();
			calBt.setTime(bt);
			calNow.set(calBt.get(Calendar.YEAR), calBt.get(Calendar.MONTH), calBt.get(Calendar.DATE));
			if (bt.compareTo(calNow.getTime()) <= 0 && et.compareTo(calNow.getTime()) > 0) {
				isRightTime = true;
			}
		}
		return isRightTime;
	}

	/**
	 * 判断当日是否已提现.
	 * 
	 * @param correctUserId
	 * @return
	 */
	private boolean withdrawToday(long correctUserId) {
		List<CorrectBillDayData> correctBillDayDatas = correctBillsDatawayService.queryDayBillData(correctUserId, null,
				1);
		if (CollectionUtils.isNotEmpty(correctBillDayDatas)) {
			CorrectBillDayData data = correctBillDayDatas.get(0);
			Calendar cal = Calendar.getInstance();
			cal.setTime(data.getDate());
			cal.add(Calendar.DAY_OF_YEAR, 1);
			long now = System.currentTimeMillis();
			if (data.getDate().getTime() <= now && now < cal.getTime().getTime()) {
				// 今日已提现，无法再次提现
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取教师资格认证信息.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getQualificationAuth")
	public Value getQualificationAuth() {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		Map<String, Object> map = Maps.newHashMap();
		map.put("qualificationAuthStatus", correctUser.getQualificationAuthStatus()); // 教师资格认证状态
		if (CollectionUtils.isNotEmpty(correctUser.getQualificationImgs())) {
			map.put("qualificationImgId", correctUser.getQualificationImgs().get(0)); // 教师资格认证照片文件ID
			map.put("qualificationImgSrc", FileUtil.getUrl(correctUser.getQualificationImgs().get(0))); // 教师资格认证照片文件地址
		}
		map.put("noPassReason", correctUser.getTQualificationNoPassReason()); // 认证失败的原因

		return new Value(map);
	}

	/**
	 * 获取实名认证信息.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getIDCardAuth")
	public Value getIDCardAuth() {
		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		Map<String, Object> map = Maps.newHashMap();
		map.put("idCardAuthStatus", correctUser.getIdCardAuthStatus()); // 实名认证状态
		map.put("realname", correctUser.getRealname()); // 真实姓名
		map.put("idCard", correctUser.getIdCard()); // 身份证号
		if (CollectionUtils.isNotEmpty(correctUser.getIdCardImgs())) {
			map.put("idCardImgId", correctUser.getIdCardImgs().get(0)); // 身份证照片文件ID
			map.put("idCardImgSrc", FileUtil.getUrl(correctUser.getIdCardImgs().get(0))); // 身份证照片文件地址
		}
		map.put("noPassReason", correctUser.getIdCardNoPassReason()); // 认证失败的原因

		return new Value(map);
	}

	/**
	 * 获取手机认证信息.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getMobileAuth")
	public Value getMobileAuth() {
		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		Map<String, Object> map = Maps.newHashMap();
		map.put("mobileAuthStatus",
				StringUtils.isNotBlank(correctUser.getMobile()) ? CorrectAuthStatus.PASS : CorrectAuthStatus.DEFAULT); // 实名认证状态
		map.put("mobile", correctUser.getMobile()); // 手机号

		// 获取客服电话
		String customerTel = "";
		Parameter parameter = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
		if (parameter != null && parameter.getValue() != null) {
			customerTel = parameter.getValue();
		}
		map.put("customerTel", customerTel);

		return new Value(map);
	}

	/**
	 * 提交教师资格认证信息.
	 * 
	 * @param imgId
	 *            资格证照片
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "submitQualificationAuth")
	public Value submitQualificationAuth(Long imgId) {
		if (imgId == null || imgId < 1) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		if (correctUser.getQualificationAuthStatus() == CorrectAuthStatus.CHECK
				|| correctUser.getQualificationAuthStatus() == CorrectAuthStatus.PASS) {
			return new Value(new YooCorrectException(YooCorrectException.QUALIFICA_ALREADY_SUBMIT));
		}

		return correctUserDatawayClient.submitQualificationAuth(correctUser.getId(), imgId);
	}

	/**
	 * 提交实名身份认证信息.
	 * 
	 * @param imgId
	 *            身份证照片
	 * @param realName
	 *            真实姓名
	 * @param idCard
	 *            身份证号
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "submitIDCardAuth")
	public Value submitIDCardAuth(Long imgId, String realName, String idCard) {
		if (imgId == null || imgId < 1 || StringUtils.isBlank(realName) || StringUtils.isBlank(idCard)) {
			return new Value(new MissingArgumentException());
		}
		if (realName.length() > 30) {
			realName = realName.substring(0, 30) + "...";
		}

		// boolean isCard = Pattern.matches("(^\\d{18}$)|(^\\d{15}$)", idCard);
		// if (!isCard) {
		// return new Value(new
		// YooCorrectException(YooCorrectException.IDCARD_WRONG));
		// }

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		if (correctUser.getIdCardAuthStatus() == CorrectAuthStatus.CHECK
				|| correctUser.getIdCardAuthStatus() == CorrectAuthStatus.PASS) {
			return new Value(new YooCorrectException(YooCorrectException.QUALIFICA_ALREADY_SUBMIT));
		}

		return correctUserDatawayClient.submitIDCardAuth(correctUser.getId(), imgId, realName, idCard);
	}

	/**
	 * 手机认证-获取手机验证码.
	 * 
	 * @param mobile
	 *            新手机号（不传表示使用已认证的手机号）
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "sendMobileAuthCode")
	public Value sendMobileAuthCode(String mobile) {

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		if (StringUtils.isBlank(mobile)) {
			if (correctUser == null || StringUtils.isBlank(correctUser.getMobile())) {
				return new Value(new MissingArgumentException());
			}
			mobile = correctUser.getMobile();
		} else {
			Value value = correctUserDatawayClient.countUserByMobile(correctUser.getId(), mobile);
			if (value.getRet_code() == 0) {
				Long count = Long.parseLong(value.getRet().toString());
				if (count > 0) {
					// 手机号码已存在
					return new Value(new YooCorrectException(YooCorrectException.MOBILE_EXISTS));
				}
			}
		}

		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException e) {
			return new Value(new YooCorrectException(YooCorrectException.MOBILE_WRONG));
		}

		String verifyCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + mobile);
		if (StringUtils.isBlank(verifyCode)) {
			verifyCode = VerifyCodes.smsCode(6);
			messageSender.send(new SmsPacket(mobile, 10000007, ValueMap.value("authCode", verifyCode)));

			accountCacheService.setVerifyCode(Security.getToken(), Security.getToken() + mobile, verifyCode, 1,
					TimeUnit.MINUTES); // 短信一分钟可以重发
			accountCacheService.setVerifyCode(Security.getToken(), mobile, verifyCode, 5, TimeUnit.MINUTES);

			log.info("send_verify_code:sms code is {}", verifyCode);
		}

		return new Value();
	}

	/**
	 * 验证原手机验证码.
	 * 
	 * @param verifyCode
	 *            验证码
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "checkMobileVerifyCode")
	public Value checkMobileVerifyCode(String verifyCode) {
		if (StringUtils.isBlank(verifyCode)) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		if (correctUser == null || StringUtils.isBlank(correctUser.getMobile())) {
			// 还没有进行过手机认证
			return new Value(new YooCorrectException(YooCorrectException.MOBILE_NOT_AUTH_WRONG));
		}
		String mobile = correctUser.getMobile();
		String oldVerifyCode = accountCacheService.getVerifyCode(Security.getToken(), Security.getToken() + mobile);
		if (StringUtils.isBlank(oldVerifyCode) || !verifyCode.equals(oldVerifyCode)) {
			// 验证码错误
			return new Value(new AccountException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}

		// 验证成功后删除缓存
		accountCacheService.invalidVerifyCode(Security.getToken(), Security.getToken() + mobile);

		return new Value();
	}

	/**
	 * 手机认证-提交新手机号.
	 * 
	 * @param mobile
	 *            新手机号
	 * @param verifyCode
	 *            认证码
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "submitMobileAuth")
	public Value submitMobileAuth(String mobile, String verifyCode) {
		if (StringUtils.isBlank(mobile) || StringUtils.isBlank(verifyCode)) {
			return new Value(new MissingArgumentException());
		}
		try {
			ValidateUtils.validateMobile(mobile);
		} catch (AccountException e) {
			return new Value(new YooCorrectException(YooCorrectException.MOBILE_WRONG));
		}

		String verifyCodeCache = accountCacheService.getVerifyCode(Security.getToken(), mobile);
		if (StringUtils.isBlank(verifyCodeCache) || !verifyCode.equals(verifyCodeCache)) {
			return new Value(new YooCorrectException(AccountException.ACCOUNT_VERIFYCODE_INVALID));
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		return correctUserDatawayClient.submitMobileAuth(correctUser.getId(), mobile);
	}

	/**
	 * 查询学校.
	 * 
	 * @param districtCode
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "querySchool", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySchool(Long districtCode) {
		if (districtCode == null || districtCode < 1) {
			return new Value(new MissingArgumentException());
		}

		// 首先获取小悠快批用户的阶段
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		SchoolType schoolType = null;
		if (correctUser.getPhaseId() != null) {
			if (correctUser.getPhaseId() == 2) {
				schoolType = SchoolType.MIDDLE;
			} else if (correctUser.getPhaseId() == 3) {
				schoolType = SchoolType.HIGH;
			}
		}

		if (schoolType == null) {
			// 需要首先设置阶段信息
			return new Value(new YooCorrectException(YooCorrectException.NEED_SET_PHASE));
		}

		List<School> schools = schoolService.findSchoolByDistrictCode(districtCode, schoolType);
		School school = new School();
		school.setDistrictCode(districtCode);
		school.setId(-1L);
		school.setName("没有我的学校");
		schools.add(school);
		ValueMap vm = ValueMap.value("items", schoolConvert.to(schools));
		return new Value(vm);
	}

	/**
	 * 设置学校.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "setSchool")
	public Value setSchool(Long schoolCode) {
		if (schoolCode == null || (schoolCode < 1 && schoolCode != -1)) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);
		String schoolName = "";
		if (schoolCode != -1) {
			VSchool school = schoolConvert.get(schoolCode);
			if (school == null) {
				return new Value(new IllegalArgException());
			}
			schoolName = school.getName();
		}
		return correctUserDatawayClient.setSchool(correctUser.getId(), schoolCode, schoolName);
	}

	/**
	 * 设置阶段.
	 * 
	 * @return
	 */
	@LoadCorrectUser
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "setPhase")
	public Value set(Long phaseCode) {
		if (phaseCode == null || (phaseCode != 2 && phaseCode != 3)) {
			return new Value(new MissingArgumentException());
		}

		// 获取用户信息
		CorrectUserResponse correctUser = Security.getSession().getAttrSession().getObject("correctUser",
				CorrectUserResponse.class);

		return correctUserDatawayClient.setPhase(correctUser.getId(), phaseCode);
	}

	/**
	 * 重置密码.
	 * 
	 * @param p1
	 *            旧密码
	 * @param p2
	 *            新密码
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "resetPassWord")
	public Value resetPassWord(String p1, String p2) {
		if (StringUtils.isBlank(p1) || StringUtils.isBlank(p2)) {
			return new Value(new MissingArgumentException());
		}
		Account account = accountService.getAccount(Security.getAccountId());

		if (!account.getPassword().equals(Codecs.md5Hex(p1.getBytes()))) {
			return new Value(new AccountException(AccountException.ACCOUNT_PASSWORD_WRONG));
		}

		if (p2.equals(account.getName())) {
			return new Value(new AccountException(YooCorrectException.PASSWOR_ACCOUNT_SAME));
		}

		try {
			accountService.updatePassword(Security.getAccountId(), p2, null);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}
}
