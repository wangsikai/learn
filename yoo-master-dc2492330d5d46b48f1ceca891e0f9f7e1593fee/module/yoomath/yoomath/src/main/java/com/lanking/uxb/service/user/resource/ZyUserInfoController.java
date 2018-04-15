package com.lanking.uxb.service.user.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsRule;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTask;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.CoinsRuleService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.value.VUserReward;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.CredentialConvert;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;

/**
 * 悠数学获取用户信息相关接口
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
@RestController
@RequestMapping("zy/user/info")
public class ZyUserInfoController {

	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private UserProfileConvert userProfileConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsRuleService coninsService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private CredentialConvert credentialConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert zyHomeworkStudentClazzConvert;
	@Autowired
	private UserTaskService userTaskService;

	@RequestMapping(value = "simple", method = { RequestMethod.POST, RequestMethod.GET })
	public Value simple(@RequestParam(defaultValue = "0", value = "userId") long userId) {
		Map<String, Object> data = new HashMap<String, Object>(3);
		long uid = userId == 0 ? Security.getUserId() : userId;
		VUser vUser = userConvert.get(uid);
		data.put("user", vUser);
		if (vUser.getType() == UserType.STUDENT) {
			// 所属平台集
			data.put("credentials",
					credentialConvert.to(credentialService.listCredentials(Product.YOOMATH, Security.getAccountId())));
			// 班级
			List<VHomeworkStudentClazz> VStudentClazzs = zyHomeworkStudentClazzConvert.to(zyHkStuClazzService
					.listCurrentClazzs(Security.getUserId()));
			data.put("homeworkStudentClazzs", VStudentClazzs);
		}
		// 构建用户升级相关信息
		VUserReward userReward = new VUserReward();
		UserHonor userHonor = userHonorService.getUserHonor(Security.getUserId());
		if (userHonor != null) {
			userReward.setLevel(userHonor.getLevel());
			userReward.setUpGrade(userHonor.isUpgrade());
			if (userHonor.isUpgrade()) {
				if (Security.getUserType() == UserType.TEACHER) {
					CoinsRule coinsRule = coninsService.getByAction(CoinsAction.GRADE_UP_REWARD);
					userReward.setUpRewardCoins(coinsRule.getCoinsValue());
				} else {
					if (userHonor.getLevel() > 1) {
						int code = 101020001;
						code += userHonor.getLevel() - 2;
						UserTask userTask = userTaskService.get(code);
						userReward.setUpRewardCoins(userTask.getUserTaskRuleCfg().getCoinsValue());
					} else {
						userReward.setUpGrade(false);
					}
				}
			}
			userReward.setCoinsValue(userHonor.getCoins());
			data.put("userReward", userReward);
		}
		return new Value(data);
	}

	@RequestMapping(value = "profile", method = { RequestMethod.POST, RequestMethod.GET })
	public Value profile() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("userProfile", userProfileConvert.get(Security.getUserId()));
		// 构建用户升级相关信息
		VUserReward userReward = new VUserReward();
		UserHonor userHonor = userHonorService.getUserHonor(Security.getUserId());
		if (userHonor != null) {
			userReward.setLevel(userHonor.getLevel());
			userReward.setUpGrade(userHonor.isUpgrade());
			if (userHonor.isUpgrade()) {
				CoinsRule coinsRule = coninsService.getByAction(CoinsAction.GRADE_UP_REWARD);
				userReward.setUpRewardCoins(coinsRule.getCoinsValue());
			}
			data.put("userReward", userReward);
		}
		return new Value(data);
	}
}
