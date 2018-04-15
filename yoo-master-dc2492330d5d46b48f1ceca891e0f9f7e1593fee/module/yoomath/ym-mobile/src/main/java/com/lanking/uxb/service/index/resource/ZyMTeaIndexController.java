package com.lanking.uxb.service.index.resource;

import java.util.Calendar;
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
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoo.activity.ActivityEntranceCfg;
import com.lanking.cloud.domain.yoo.common.Banner;
import com.lanking.cloud.domain.yoo.common.BannerLocation;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.ActivityEntranceCfgService;
import com.lanking.uxb.service.activity.convert.ActivityEntranceCfgConvert;
import com.lanking.uxb.service.index.form.TeaIndexForm;
import com.lanking.uxb.service.report.api.ClassStatisticsReportService;
import com.lanking.uxb.service.report.cache.LearnReportCacheService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.sys.api.BannerQuery;
import com.lanking.uxb.service.sys.api.BannerService;
import com.lanking.uxb.service.sys.convert.BannerConvert;
import com.lanking.uxb.service.zuoye.api.ZyBookService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.convert.ZyBookCatalogConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStatConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStat;

/**
 * 教师端首页相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年4月12日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/t/index")
public class ZyMTeaIndexController {

	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private ZyHomeworkStatService zyHkStatService;
	@Autowired
	private ZyHomeworkStatConvert zyHkStatConvert;
	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ClassStatisticsReportService classStatisticsReportService;
	@Autowired
	private LearnReportCacheService learnReportCacheService;
	@Autowired
	private ZyBookService bookService;
	@Autowired
	private ZyBookCatalogConvert bookCatalogConvert;
	@Autowired
	private ActivityEntranceCfgService activityEntranceCfgService;
	@Autowired
	private ActivityEntranceCfgConvert activityEntranceCfgConvert;
	@Autowired
	private BannerService bannerService;
	@Autowired
	private BannerConvert bannerConvert;

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(TeaIndexForm form) {
		Long clazzId = form.getClazzId();
		List<Long> clazzIds = form.getClazzIds();
		Map<String, Object> data = new HashMap<String, Object>(7);
		HomeworkClazz clazz = null;
		VHomeworkClazz vclazz = null;

		boolean clazzChange = false;
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());

		if (CollectionUtils.isEmpty(clazzs)) {// 如果当前没有班级的时候
			data.put("clazzChange", true);
			data.put("clazzs", Collections.EMPTY_LIST);
			return new Value(data);
		}
		if (clazzId == null || CollectionUtils.isEmpty(clazzIds) || clazzs.size() != clazzIds.size()) {
			clazzChange = true;
		}
		if (!clazzChange) {
			for (HomeworkClazz homeworkClazz : clazzs) {
				clazzIds.remove(homeworkClazz.getId());
			}
			clazzChange = clazzIds.size() != 0;
		}
		List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
		// 推荐作业的信息
		for (VHomeworkClazz v : vclazzs) {
			if (v.getBookVersionId() != null && v.getBookCataId() != null) {
				Map<String, Object> recommendMap = bookService.getRecommendCatalogs(v.getBookVersionId(),
						v.getBookCataId(), Security.getUserId());
				if (recommendMap == null) {
					v.setBookVersionId(0L);
					v.setBookCataId(0L);
				} else {
					v.setRecommendCatalogs(bookCatalogConvert.to((List<BookCatalog>) recommendMap.get("catalogs")));
					v.setLevelOneCatalog(bookCatalogConvert.to((BookCatalog) recommendMap.get("levelOneCatalog")));
				}
			} else {
				v.setBookVersionId(0L);
				v.setBookCataId(0L);
			}
		}
		data.put("clazzs", vclazzs);
		// 班级
		if (clazzChange) {
			if (clazzId != null) {
				boolean clazzExist = false;
				for (HomeworkClazz hc : clazzs) {
					if (hc.getId() == clazzId) {
						clazzExist = true;
						clazz = hc;
						break;
					}
				}
				if (clazzExist) {
					for (VHomeworkClazz v : vclazzs) {
						if (v.getId() == clazzId) {
							vclazz = v;
							break;
						}
					}
				}
				if (!clazzExist) {
					clazz = clazzs.get(0);
					clazzId = clazzs.get(0).getId();
					vclazz = vclazzs.get(0);
				}
			} else {
				clazz = clazzs.get(0);
				clazzId = clazzs.get(0).getId();
				vclazz = vclazzs.get(0);
			}
		} else {
			clazz = zyHkClassService.get(clazzId);
			vclazz = homeworkClassConvert.to(clazz);
			if (clazz == null || clazz.getTeacherId() != Security.getUserId()) {
				clazzChange = true;
				clazz = clazzs.get(0);
				clazzId = clazzs.get(0).getId();
				vclazz = vclazzs.get(0);
			}
		}
		data.put("clazzChange", clazzChange);
		// 对应班级的统计信息
		HomeworkStat homeworkStat = zyHkStatService.getByHomeworkClassId(clazzId);
		VHomeworkStat vhomeworkStat = null;
		if (homeworkStat == null) {
			vhomeworkStat = new VHomeworkStat();
			vhomeworkStat.setClassId(clazzId);
			vhomeworkStat.setHomeworkClassId(clazzId);
			vhomeworkStat.setUserId(Security.getUserId());
			vhomeworkStat.setRightRate(null);
			vhomeworkStat.setCompletionRate(null);
		} else {
			vhomeworkStat = zyHkStatConvert.to(homeworkStat);
		}
		data.put("clazzStat", vhomeworkStat);

		// 作业
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setEndTime(new Date());
		query.setStatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE));
		query.setClassId(clazzId);
		Page<Homework> homeworkPage = zyHkService.queryForMobile(query, P.index(1, 50));
		if (homeworkPage.isNotEmpty()) {
			List<VHomework> homeworks = homeworkConvert.to(homeworkPage.getItems());
			for (VHomework v : homeworks) {
				v.setHomeworkClazz(vclazz);
			}
			data.put("homeworks", homeworks);
		} else {
			data.put("homeworks", Collections.EMPTY_LIST);
		}

		// 学情报告
		Map<String, Object> report = new HashMap<String, Object>(3);
		Calendar now = Calendar.getInstance();
		now.add(Calendar.MONTH, -1);
		String cacheFlag = learnReportCacheService.getCurMonthTips(clazzId);
		if ("true".equals(cacheFlag)) {
			report.put("show", false);
		} else {
			if ("false".equals(cacheFlag)) {
				report.put("show", true);
			} else {
				boolean exist = classStatisticsReportService.existReport(now.get(Calendar.YEAR),
						now.get(Calendar.MONTH) + 1, clazzId);
				if (exist) {
					report.put("show", true);
					learnReportCacheService.setCurMonthTips(clazzId, false);
				} else {
					report.put("show", false);
				}
			}
		}
		int month = now.get(Calendar.MONTH) + 1;
		report.put("title", clazz.getName() + "," + month + "月份  学情分析报告");
		report.put("url", Env.getString("report.teach.url", new Object[] { Security.getToken() }));
		data.put("report", report);
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
		return new Value(data);
	}
}
