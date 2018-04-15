package com.lanking.uxb.service.user.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.resource.ZyMBaseController;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.CredentialService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.convert.CredentialConvert;
import com.lanking.uxb.service.user.convert.UserMemberConvert;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.zuoye.api.PracticeHistoryService;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;

/**
 * 悠数学移动端(用户中心公共接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@MappedSuperclass
public class ZyMBaseUserCenterController extends ZyMBaseController {

	@Autowired
	UserProfileConvert userProfileConvert;
	@Autowired
	ZyQuestionCollectionService qcService;
	@Autowired
	SectionService sectionServce;
	@Autowired
	SectionConvert sectionConvert;
	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private CredentialService credentialService;
	@Autowired
	private CredentialConvert credentialConvert;
	@Autowired
	private PracticeHistoryService practiceHistoryService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserMemberConvert userMemberConvert;
	@Autowired
	private ZyClazzJoinRequestService clazzJoinRequestService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	/**
	 * 用户中心数据接口
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "index" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value index() {
		VUserProfile up = userProfileConvert.get(Security.getUserId());
		if (Security.getUserType() == UserType.STUDENT) {
			up.getUserState().setQuestionCollectCount(qcService.count(Security.getUserId()));
			return new Value(up);
		} else if (Security.getUserType() == UserType.TEACHER) {
			return new Value(up);
		}
		return new Value();
	}

	/**
	 * 用户中心数据接口
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "2/index" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value index2() {
		Map<String, Object> data = new HashMap<String, Object>(9);
		// 用户资料
		VUserProfile up = userProfileConvert.get(Security.getUserId());
		data.put("profile", up);
		// 荣誉 相关
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MAXLEVEL + 1, Product.YOOMATH));

		if (Security.getUserType() == UserType.TEACHER) {
			vhonor.setCheckIn(growthLogService.getCheck(GrowthAction.DAILY_CHECKIN, Security.getUserId()));
		} else {
			// 旧接口兼容
			Date date = new Date();
			String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
			try {
				date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			} catch (ParseException e) {
			}
			UserTaskLog log = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(), date);
			vhonor.setCheckIn(log != null);
		}
		if (honor != null) {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
		}
		data.put("honor", vhonor);
		// 凭证列表
		List<CredentialType> credentialTypes = new ArrayList<CredentialType>(5);
		List<Credential> credentials = credentialService.listCredentials(Product.YOOMATH, up.getAccount().getId());
		boolean hasBindQQ = false;
		boolean hasBindWX = false;
		for (Credential credential : credentials) {
			credentialTypes.add(credential.getType());
			if (credential.getType() == CredentialType.QQ) {
				hasBindQQ = true;
				continue;
			}
			if (credential.getType() == CredentialType.WEIXIN) {
				hasBindWX = true;
			}
		}
		up.getAccount().setCredentialTypes(credentialTypes);
		data.put("credentialTypes", credentialTypes);
		if (CollectionUtils.isEmpty(credentials)) {
			data.put("credentials", Collections.EMPTY_LIST);
		} else {
			data.put("credentials", credentialConvert.to(credentials));
		}
		// 会员信息
		UserMember userMember = userMemberService.$findByUserId(Security.getUserId());
		data.put("userMember", userMemberConvert.to(userMember));
		// 金币商城是否启用
		data.put("mallEnable", Env.getDynamicBoolean("mall.enable"));
		Parameter parameter = parameterService.get(Product.YOOMATH, "member.center.enable");
		data.put("memberEnable", parameter == null ? false : Boolean.valueOf(parameter.getValue()));
		data.put("showBindInfo", (!hasBindQQ) && StringUtils.isEmpty(up.getAccount().getEmail())
				&& StringUtils.isEmpty(up.getAccount().getMobile()) && (!hasBindWX));
		if (Security.getUserType() == UserType.STUDENT) {
			up.getUserState().setQuestionCollectCount(qcService.count(Security.getUserId()));
			up.getUserState().setPracticeHistoryCount(practiceHistoryService.count(Security.getUserId()));
			// 学业报告
			Map<String, Object> report = new HashMap<String, Object>(1);
			Parameter wholeUrl = parameterService.get(Product.YOOMATH, "stu.report.learning2.h5.url");
			report.put("url", wholeUrl.getValue());
			data.put("report", report);

			return new Value(data);
		} else if (Security.getUserType() == UserType.TEACHER) {
			// 学情报告
			Map<String, Object> report = new HashMap<String, Object>(1);
			report.put("url", Env.getString("report.teach.url", new Object[] { Security.getToken() }));
			data.put("report", report);

			// 当前班级 sprint72
			data.put("hasClazz", homeworkClassService.currentCount(Security.getUserId()) > 0 ? true : false);
			// 教师返回待加入班级学生数据数量统计
			data.put("confirmStuJoinRequestNum", clazzJoinRequestService.requestCount(Security.getUserId(), null));

			// 添加小优快批入口
			boolean yooCorrectEnable = false;
			String yooCorrectUrl = "";
			Parameter uParameter = parameterService.get(Product.YOOMATH, "yoocorrect.h5.user");
			Parameter yParameter = parameterService.get(Product.YOOMATH, "yoocorrect.h5.url");
			if (uParameter != null) {
				String yusers = StringUtils.defaultIfBlank(uParameter.getValue());
				if (yusers.equals("0") || yusers.indexOf(String.valueOf(Security.getUserId())) > -1) {
					yooCorrectEnable = true;
				}
				if (yParameter != null) {
					yooCorrectUrl = StringUtils.defaultIfBlank(yParameter.getValue());
				}
			}
			data.put("yooCorrectEnable", yooCorrectEnable);
			data.put("yooCorrectUrl", yooCorrectUrl);

			return new Value(data);
		}
		return new Value();
	}

	/**
	 * 用户可选教材列表
	 * 
	 * @since 2.0.3
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "textbookCategoryList" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value textbookCategoryList() {
		if (Security.getUserType() == UserType.TEACHER) {
			Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
			ValueMap vm = ValueMap.value("items", tbCateService.find(Product.YOOMATH, teacher.getPhaseCode()));
			return new Value(vm);
		}
		return new Value();
	}

}
