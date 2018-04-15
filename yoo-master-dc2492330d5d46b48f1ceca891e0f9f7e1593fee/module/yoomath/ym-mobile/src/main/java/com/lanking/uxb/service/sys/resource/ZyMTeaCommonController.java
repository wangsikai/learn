package com.lanking.uxb.service.sys.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzTransfer;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.DutyService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.code.convert.DutyConvert;
import com.lanking.uxb.service.code.convert.TitleConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.sys.value.VBanner;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserMemberConvert;
import com.lanking.uxb.service.zuoye.api.ZyClazzJoinRequestService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClazzTransferService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;

@RestController
@RequestMapping("zy/m/t/common")
public class ZyMTeaCommonController extends ZyMBaseCommonController {

	@Autowired
	private TeacherService teacherService;

	@Autowired
	private TitleService titleService;
	@Autowired
	private TitleConvert titleConvert;
	@Autowired
	private DutyService dutyService;
	@Autowired
	private DutyConvert dutyConvert;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private UserMemberConvert userMemberConvert;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private ZyClazzJoinRequestService clazzJoinRequestService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZyHomeworkClazzTransferService transferService;
	@Autowired
	private ZyHomeworkClassService classService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkService hkService;

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = { "listTitle" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value listTitle() {
		ValueMap vm = ValueMap.value("items", titleConvert.to(titleService.getAll()));
		return new Value(vm);
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = { "listDuty" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value listDuty() {
		ValueMap vm = ValueMap.value("items", dutyConvert.to(dutyService.getAll()));
		return new Value(vm);
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "listTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listTextbook(Integer phaseCode) {
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		Map<String, Object> one = Maps.newHashMap();
		if (phaseCode != null) {
			one.put("phase", phaseConvert.get(phaseCode));
			List<TextbookCategory> tbCates = tbCateService.find(Product.YOOMATH, phaseCode);
			List<VTextbookCategory> vtbCates = tbCateConvert.to(tbCates);
			List<VTextbookCategory> oneList = Lists.newArrayList();
			for (VTextbookCategory vtbCate : vtbCates) {
				List<Textbook> tbs = null;
				if (phaseCode == PhaseService.PHASE_MIDDLE) {
					tbs = tbService.find(Product.YOOMATH, phaseCode, SubjectService.PHASE_2_MATH, vtbCate.getCode());
				} else {
					tbs = tbService.find(Product.YOOMATH, phaseCode, SubjectService.PHASE_3_MATH, vtbCate.getCode());
				}
				vtbCate.setTextbooks(tbConvert.to(tbs));
				oneList.add(vtbCate);
			}
			one.put("tbCates", oneList);
			return new Value(one);
		}
		List<VPhase> vphases = phaseConvert.to(phaseService.getAll());
		for (VPhase vphase : vphases) {
			if (teacher.getPhaseCode() != null && teacher.getPhaseCode().intValue() == vphase.getCode()) {
				one.put("phase", vphase);
				List<TextbookCategory> tbCates = tbCateService.find(Product.YOOMATH, vphase.getCode());
				List<VTextbookCategory> vtbCates = tbCateConvert.to(tbCates);
				List<VTextbookCategory> oneList = Lists.newArrayList();
				for (VTextbookCategory vtbCate : vtbCates) {
					List<Textbook> tbs = null;
					if (vphase.getCode() == PhaseService.PHASE_MIDDLE) {
						tbs = tbService.find(Product.YOOMATH, vphase.getCode(), SubjectService.PHASE_2_MATH,
								vtbCate.getCode());
					} else {
						tbs = tbService.find(Product.YOOMATH, vphase.getCode(), SubjectService.PHASE_3_MATH,
								vtbCate.getCode());
					}
					vtbCate.setTextbooks(tbConvert.to(tbs));
					oneList.add(vtbCate);
				}
				one.put("tbCates", oneList);
				break;
			}
		}
		return new Value(one);
	}

	/**
	 * 教师端预加载接口
	 *
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "preload", method = { RequestMethod.GET, RequestMethod.POST })
	public Value preload() {
		Map<String, Object> dataMap = new HashMap<String, Object>(9);

		BannerQuery bannerQuery = new BannerQuery();
		bannerQuery.setApp(YooApp.MATH_TEACHER);
		bannerQuery.setLocation(BannerLocation.SPLASH_SCREEN);
		List<Banner> bannerList = bannerService.listEnable(bannerQuery);
		if (CollectionUtils.isNotEmpty(bannerList)) {
			VBanner banner = bannerConvert.to(bannerList.get(0));
			dataMap.put("splashScreen", banner);
		}

		UserMember userMember = userMemberService.$findByUserId(Security.getUserId());
		dataMap.put("userMember", userMemberConvert.to(userMember));

		dataMap.put("confirmStuJoinRequestNum", clazzJoinRequestService.requestCount(Security.getUserId(), null));
		Parameter telParameter1 = parameterService.get(Product.YOOMATH, "customer-service.TEL.1");
		Parameter telParameter2 = parameterService.get(Product.YOOMATH, "customer-service.TEL.2");
		dataMap.put("customerTel1", telParameter1 == null ? null : telParameter1.getValue());
		dataMap.put("customerTel2", telParameter2 == null ? null : telParameter2.getValue());

		Parameter growthPageUrl = parameterService.get(Product.YOOMATH, "tea.growth.h5.url");
		Parameter coinsPageUrl = parameterService.get(Product.YOOMATH, "tea.coins.h5.url");
		Parameter vipPageUrl = parameterService.get(Product.YOOMATH, "tea.vip.h5.url");
		Parameter aboutPageUrl = parameterService.get(Product.YOOMATH, "tea.about.h5.url");
		Parameter vipLearnMorePageUrl = parameterService.get(Product.YOOMATH, "tea.vip-learn-more.h5.url");
		Parameter earnCoinsPageUrl = parameterService.get(Product.YOOMATH, "tea.earn-coins.h5.url");
		Parameter helpPageUrl = parameterService.get(Product.YOOMATH, "tea.help.h5.url");
		Parameter limitPageUrl = parameterService.get(Product.YOOMATH, "tea.help.limit.h5.url");
		Parameter payProtocolUrl = parameterService.get(Product.YOOMATH, "tea.help.pay.h5.url");
		Parameter iapPayProtocolUrl = parameterService.get(Product.YOOMATH, "ios.iap.help.pay.h5.url");

		dataMap.put("growthPageUrl", growthPageUrl == null ? "" : growthPageUrl.getValue());
		dataMap.put("coinsPageUrl", coinsPageUrl == null ? "" : coinsPageUrl.getValue());
		dataMap.put("vipPageUrl", vipPageUrl == null ? "" : vipPageUrl.getValue());
		dataMap.put("aboutPageUrl", aboutPageUrl == null ? "" : aboutPageUrl.getValue());
		dataMap.put("vipLearnMorePageUrl", vipLearnMorePageUrl == null ? "" : vipLearnMorePageUrl.getValue());
		dataMap.put("earnCoinsPageUrl", earnCoinsPageUrl == null ? "" : earnCoinsPageUrl.getValue());
		dataMap.put("helpPageUrl", helpPageUrl == null ? "" : helpPageUrl.getValue());
		dataMap.put("limitPageUrl", limitPageUrl == null ? "" : limitPageUrl.getValue());
		dataMap.put("payProtocolUrl", payProtocolUrl == null ? "" : payProtocolUrl.getValue());
		dataMap.put("iapPayProtocolUrl", iapPayProtocolUrl == null ? "" : iapPayProtocolUrl.getValue());

		HomeworkClazzTransfer transfer = transferService.getLastest(Security.getUserId());
		if (transfer == null) {
			return new Value(dataMap);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		HomeworkClazz clazz = classService.get(transfer.getHomeworkClassId());

		String toTeaName = teacherService.get(transfer.getTo()).getName().substring(0, 1) + "老师";
		data.put("toTeaName", toTeaName);
		data.put("className", clazz.getName());
		data.put("notIssueNum", hkService.countNotIssue(transfer.getHomeworkClassId()));
		String fromTeaName = teacherService.get(transfer.getFrom()).getName().substring(0, 1) + "老师";
		data.put("fromTeaName", fromTeaName);
		data.put("id", clazz.getId());
		dataMap.put("transferData", data);
		transferService.read(transfer.getId(), Security.getUserId());
		return new Value(dataMap);
	}
}
