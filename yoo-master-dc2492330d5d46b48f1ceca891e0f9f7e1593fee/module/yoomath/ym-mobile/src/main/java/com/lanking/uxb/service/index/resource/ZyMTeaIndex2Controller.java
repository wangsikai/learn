package com.lanking.uxb.service.index.resource;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoo.common.EmbeddedApp;
import com.lanking.cloud.domain.yoo.common.EmbeddedAppLocation;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.ActivityEntranceCfgService;
import com.lanking.uxb.service.activity.convert.ActivityEntranceCfgConvert;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.index.form.TeaIndexForm;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.api.EmbeddedAppQuery;
import com.lanking.uxb.service.sys.api.EmbeddedAppService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.sys.convert.EmbeddedAppConvert;
import com.lanking.uxb.service.sys.value.VEmbeddedApp;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 教师端首页相关接口
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年6月30日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/2")
public class ZyMTeaIndex2Controller {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ActivityEntranceCfgService activityEntranceCfgService;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private EmbeddedAppService embeddedAppService;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;

	@Autowired
	private ActivityEntranceCfgConvert activityEntranceCfgConvert;
	@Autowired
	private BannerConvert bannerConvert;
	@Autowired
	private EmbeddedAppConvert embeddedAppConvert;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;

	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(TeaIndexForm form) {
		Map<String, Object> data = new HashMap<String, Object>();

		// 活动入口
		ActivityEntranceCfg cfg = activityEntranceCfgService.findByApp(YooApp.MATH_TEACHER);
		if (cfg != null) {
			data.put("activityEntranceCfg", activityEntranceCfgConvert.to(cfg));
		}
		// banner
		List<Banner> banners = bannerService.listEnable(new BannerQuery(YooApp.MATH_TEACHER, BannerLocation.HOME));
		if (CollectionUtils.isNotEmpty(banners)) {
			data.put("banner", bannerConvert.to(banners));
		} else {
			data.put("banner", Collections.EMPTY_LIST);
		}
		// 内嵌入口
		List<EmbeddedApp> embeddedApps = embeddedAppService
				.list(new EmbeddedAppQuery(YooApp.MATH_TEACHER, EmbeddedAppLocation.HOME));
		if (CollectionUtils.isNotEmpty(embeddedApps)) {
			List<VEmbeddedApp> apps = embeddedAppConvert.to(embeddedApps);
			data.put("embeddedApp", apps);
		} else {
			data.put("embeddedApp", Collections.EMPTY_LIST);
		}

		// 悠数学介绍页
		Parameter introducePageUrl = parameterService.get(Product.YOOMATH, "tea.introduce.h5.url");
		data.put("introducePageUrl", introducePageUrl == null ? "" : introducePageUrl.getValue());
		// 查看示例作业成绩
		Parameter performancePageUrl = parameterService.get(Product.YOOMATH, "tea.sample.performance.h5.url");
		data.put("performancePageUrl", performancePageUrl == null ? "" : performancePageUrl.getValue());
		// 查看示例班级学情分析
		Parameter analysisPageUrl = parameterService.get(Product.YOOMATH, "tea.sample.analysis.h5.url");
		data.put("analysisPageUrl", analysisPageUrl == null ? "" : analysisPageUrl.getValue());

		// 是否新用户
		long homeworkCount = zyHkService.allCountByOriginalCreateId(Security.getUserId());
		if (homeworkCount == 0) {
			long holidayCount = holidayHomeworkService.allCountByCreateId(Security.getUserId());
			if (holidayCount == 0) {
				data.put("newTeacher", true);
			} else {
				data.put("newTeacher", false);
			}
		} else {
			data.put("newTeacher", false);
		}

		// 教师是否创建班级
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());

		// 当前班级
		if (CollectionUtils.isEmpty(clazzs)) {
			data.put("hasClazz", false);
			return new Value(data);
		} else {
			data.put("hasClazz", true);
		}

		ZyHomeworkClassConvertOption classOption = new ZyHomeworkClassConvertOption();
		classOption.setInitClassGroup(false);
		classOption.setInitLatestHomework(false);
		classOption.setInitStat(false);
		classOption.setInitTeacher(false);
		List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs, classOption);

		// 作业
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setEndTime(new Date());
		query.setStatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE));
		query.setClassId(null);
		Page<Homework> homeworkPage = zyHkService.queryForMobile2(query, P.index(1, 50));
		if (homeworkPage.isNotEmpty()) {
			// 不需要知识点
			List<Homework> homeworkList = homeworkPage.getItems();
			for (Homework homework : homeworkList) {
				homework.setInitKnowledgePoint(false);
				homework.setInitMetaKnowpoint(false);
			}

			HomeworkConvertOption homeworkOption = new HomeworkConvertOption();
			homeworkOption.setInitCount(false);
			homeworkOption.setInitExercise(false);
			homeworkOption.setInitKnowledgePoint(false);
			homeworkOption.setInitMetaKnowpoint(false);
			homeworkOption.setInitSectionOrBookCatalog(false);
			homeworkOption.setInitTeacherName(false);
			List<VHomework> homeworks = homeworkConvert.to(homeworkList, homeworkOption);
			for (VHomework v : homeworks) {
				for (VHomeworkClazz vclazz : vclazzs) {
					if (v.getHomeworkClazzId() == vclazz.getId()) {
						v.setHomeworkClazz(vclazz);
					}
				}
			}
			data.put("homeworks", homeworks);
			data.put("pendingHomework", homeworks.size());
		} else {
			data.put("homeworks", Collections.EMPTY_LIST);
			data.put("pendingHomework", 0);
		}

		return new Value(data);
	}
}
