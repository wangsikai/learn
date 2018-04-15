package com.lanking.uxb.service.sys.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.UserParameter;
import com.lanking.cloud.domain.yoo.user.UserParameterType;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.sys.value.VBanner;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.api.UserParameterService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserMemberConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 悠数学移动端(公共接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/common")
public class ZyMStuCommonController extends ZyMBaseCommonController {
	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private UserParameterService userParameterService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserMemberConvert userMemberConvert;

	/**
	 * 返回一些预加载的数据
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @since sprint73 2017-09-01 手写识别仅渠道用户可用，白名单改为黑名单，仅针对学校，默认白名单
	 * 
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "preload", method = { RequestMethod.POST, RequestMethod.GET })
	public Value preload(@RequestParam(value = "validBeta", defaultValue = "false") boolean validBeta) {
		Map<String, Object> dataMap = new HashMap<String, Object>(7);
		dataMap.put("todoHomeworks", (int) stuHkService.countAllHomeworks(Security.getUserId(),
				Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT)));
		UserParameter up = userParameterService.findOne(Product.YOOMATH, null, UserParameterType.STUDENT_APP_USE,
				Security.getUserId());
		boolean winterVacation = false;
		if (up == null) {
			dataMap.put("inviteShare", false);
			up = new UserParameter();
			up.setP0(String.valueOf(1));
			long notSubmit = holidayStuHomeworkService.countNotSubmit(Security.getUserId());
			winterVacation = notSubmit > 0;
			up.setP1(winterVacation ? String.valueOf(1) : String.valueOf(0));
			up.setProduct(Product.YOOMATH);
			up.setType(UserParameterType.STUDENT_APP_USE);
			up.setUserId(Security.getUserId());
		} else {
			int times = Integer.parseInt(up.getP0()) + 1;
			dataMap.put("inviteShare", times == 3);
			up.setP0(String.valueOf(times));
			if ("1".equals(up.getP1())) {
				winterVacation = false;
			} else {
				long notSubmit = holidayStuHomeworkService.countNotSubmit(Security.getUserId());
				winterVacation = notSubmit > 0;
				up.setP1(winterVacation ? String.valueOf(1) : String.valueOf(0));
			}
		}
		// 寒假作业提醒
		dataMap.put("winterVacation", winterVacation);
		// 金币商城是否启用
		dataMap.put("mallEnable", Env.getDynamicBoolean("mall.enable"));
		userParameterService.asyncSave(up);

		BannerQuery bannerQuery = new BannerQuery();
		bannerQuery.setApp(YooApp.MATH_STUDENT);
		bannerQuery.setLocation(BannerLocation.SPLASH_SCREEN);
		List<Banner> bannerList = bannerService.listEnable(bannerQuery);
		if (CollectionUtils.isNotEmpty(bannerList)) {
			VBanner banner = bannerConvert.to(bannerList.get(0));
			dataMap.put("splashScreen", banner);
		}

		if (validBeta) {
			long userId = Security.getUserId();
			VUser v = userConvert.get(userId);
			dataMap.put("keyboardBetaUser", true);
			if (!v.isChannelUser() || v.getSchool() == null) {
				dataMap.put("keyboardBetaUser", false); // 非渠道用户
			} else {
				Parameter notAllowSchool = parameterService.get(Product.YOOMATH, "keyboard.beta.school");
				if (notAllowSchool != null) {
					String notAllowSchoolIds = notAllowSchool.getValue();
					if (StringUtils.isNotBlank(notAllowSchoolIds)
							&& notAllowSchoolIds.contains(String.valueOf(v.getSchool().getId()))) {
						// 黑名单用户
						dataMap.put("keyboardBetaUser", false);
					}
				}
			}
		}

		UserMember userMember = userMemberService.$findByUserId(Security.getUserId());
		dataMap.put("userMember", userMemberConvert.to(userMember));

		// 电话
		Parameter telParameter1 = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
		Parameter telParameter2 = parameterService.get(Product.YOOMATH, "customer-service.TEL.2");
		dataMap.put("customerTel1", telParameter1 == null ? null : telParameter1.getValue());
		dataMap.put("customerTel2", telParameter2 == null ? null : telParameter2.getValue());

		// 个人中心相关url配置
		Parameter growthPageUrl = parameterService.get(Product.YOOMATH, "stu.growth.h5.url");
		Parameter coinsPageUrl = parameterService.get(Product.YOOMATH, "stu.coins.h5.url");
		Parameter vipPageUrl = parameterService.get(Product.YOOMATH, "stu.vip.h5.url");
		Parameter aboutPageUrl = parameterService.get(Product.YOOMATH, "stu.about.h5.url");
		Parameter vipLearnMorePageUrl = parameterService.get(Product.YOOMATH, "stu.vip-learn-more.h5.url");
		Parameter earnCoinsPageUrl = parameterService.get(Product.YOOMATH, "stu.earn-coins.h5.url");
		Parameter fallibleStatisticPageUrl = parameterService.get(Product.YOOMATH, "stu.fallible.statistic.h5.url");
		Parameter memberOpenPageUrl = parameterService.get(Product.YOOMATH, "stu.member.open.h5.url"); // IOS渠道会员购买页地址
		Parameter payProtocolUrl = parameterService.get(Product.YOOMATH, "tea.help.pay.h5.url");
		Parameter iapPayProtocolUrl = parameterService.get(Product.YOOMATH, "ios.iap.help.pay.h5.url");

		Map<String, Object> m = new HashMap<String, Object>(7);
		m.put("growthPageUrl", growthPageUrl == null ? "" : growthPageUrl.getValue());
		m.put("coinsPageUrl", coinsPageUrl == null ? "" : coinsPageUrl.getValue());
		m.put("vipPageUrl", vipPageUrl == null ? "" : vipPageUrl.getValue());
		m.put("aboutPageUrl", aboutPageUrl == null ? "" : aboutPageUrl.getValue());
		m.put("vipLearnMorePageUrl", vipLearnMorePageUrl == null ? "" : vipLearnMorePageUrl.getValue());
		m.put("earnCoinsPageUrl", earnCoinsPageUrl == null ? "" : earnCoinsPageUrl.getValue());
		m.put("fallibleStatisticPageUrl", fallibleStatisticPageUrl == null ? "" : fallibleStatisticPageUrl.getValue());
		m.put("memberOpenPageUrl", memberOpenPageUrl == null ? "" : memberOpenPageUrl.getValue());
		m.put("payProtocolUrl", payProtocolUrl == null ? "" : payProtocolUrl.getValue());
		m.put("iapPayProtocolUrl", iapPayProtocolUrl == null ? "" : iapPayProtocolUrl.getValue());

		dataMap.put("urls", m);
		return new Value(dataMap);
	}

}
