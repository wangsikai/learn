package com.lanking.uxb.service.report.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.stat.StudentPaperReport;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.report.api.StudentPaperReportService;
import com.lanking.uxb.service.report.convert.StudentPaperReportConvert;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

/**
 * 学生纸质报告h5页面接口
 * 
 * @since 渠道1.1.4
 * @author peng.zhao
 * @version 2017年10月9日
 */
@RestController
@RequestMapping("zy/m/report/studentPaperReport")
public class ZyMStudentPaperReportController {

	@Autowired
	private StudentPaperReportService studentPaperReportService;
	@Autowired
	private StudentPaperReportConvert studentPaperReportConvert;

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService homeworkClassService;

	/**
	 * 学生纸质报告接口
	 * 
	 * @since 渠道 V1.1.4
	 * @param list
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "report", method = { RequestMethod.POST, RequestMethod.GET })
	public Value report(Long reportId) {
		if (reportId == null) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> data = new HashMap<String, Object>();

		StudentPaperReport report = studentPaperReportService.get(reportId);
		if (report == null) {
			data.put("report", new StudentPaperReport());
		} else {
			data.put("report", studentPaperReportConvert.to(report));
		}

		return new Value(data);
	}

	/**
	 * 学生纸质报告接口
	 * 
	 * @since 2017-11-24
	 * @param reportId
	 *            学生报告ID
	 * @param page
	 *            页码
	 * @return
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "reportData", method = { RequestMethod.POST, RequestMethod.GET })
	public Value reportData(Long reportId, Integer page) {
		if (reportId == null || page == null || page < 0 || page > 4) {
			return new Value(new IllegalArgException());
		}

		StudentPaperReport report = studentPaperReportService.get(reportId);
		if (report == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> dataMap = new HashMap<String, Object>();

		switch (page) {
		case 1: {
			Account account = accountService.getAccountByUserId(report.getStudentId());
			VUser user = userConvert.get(report.getStudentId());
			HomeworkClazz clazz = homeworkClassService.get(report.getClassId());

			dataMap.put("startDate", report.getStartDate().getTime());
			dataMap.put("endDate", report.getEndDate().getTime());
			dataMap.put("accountName", account.getName());
			dataMap.put("userName", user.getName());
			dataMap.put("className", clazz.getName());
			if (user.getSchool() != null) {
				dataMap.put("schoolName", user.getSchool().getName());
			} else {
				// 学生学校为空直接取班级教师数据
				user = userConvert.get(clazz.getTeacherId());
				dataMap.put("schoolName", user.getSchool() == null ? "" : user.getSchool().getName());
			}

			break;
		}

		case 2: {
			String comprehensiveMap = report.getComprehensiveMap();
			if (StringUtils.isNotBlank(comprehensiveMap)) {
				JSONObject comprehensiveObj = JSONObject.parseObject(comprehensiveMap);
				JSONObject clazzSituationObj = comprehensiveObj.getJSONObject("clazzSituation");
				JSONObject stuSituationObj = comprehensiveObj.getJSONObject("stuSituation");
				if (null != clazzSituationObj) {
					dataMap.put("clazzSituation", clazzSituationObj.toJSONString());
				}
				if (null != stuSituationObj) {
					dataMap.put("stuSituation", stuSituationObj.toJSONString());
				}
			}
			break;
		}

		case 3: {
			VUser user = userConvert.get(report.getStudentId());
			dataMap.put("userName", user.getName());

			String comprehensiveMap = report.getComprehensiveMap();
			dataMap.put("homeworkList", "[]");
			if (StringUtils.isNotBlank(comprehensiveMap)) {
				JSONObject comprehensiveObj = JSONObject.parseObject(comprehensiveMap);
				JSONArray hkListArray = comprehensiveObj.getJSONArray("hkList");
				if (hkListArray != null) {
					dataMap.put("homeworkList", hkListArray.toJSONString());
				}
			}
			String sectionMap = report.getSectionMap();
			dataMap.put("sectionMap", sectionMap);
			break;
		}

		case 4: {
			String knowledges = report.getKnowledges();
			String comment = report.getComment();

			dataMap.put("knowledges", knowledges);

			// 处理 knowledges
			int totalKnowledgeCount = 0; // 知识点总数
			int excellentCount = 0; // 优于全班个数
			if (StringUtils.isNotBlank(knowledges)) {
				JSONArray knowledgeArray = JSON.parseArray(knowledges);
				totalKnowledgeCount = knowledgeArray.size();
				for (int i = 0; i < knowledgeArray.size(); i++) {
					int stuMasterRate = knowledgeArray.getJSONObject(i).getIntValue("stu_masterRate");
					int classMasterRate = knowledgeArray.getJSONObject(i).getIntValue("class_masterRate");
					if (stuMasterRate > classMasterRate) {
						excellentCount++;
					}
				}
				if (knowledgeArray.size() > 20) {
					dataMap.put("knowledges", JSON.toJSONString(knowledgeArray.subList(0, 20)));
				}
			}

			dataMap.put("totalKnowledgeCount", totalKnowledgeCount);
			dataMap.put("excellentCount", excellentCount);

			// 控制 comment
			int commentSectionCount = 0;
			if (StringUtils.isNotBlank(comment)) {
				JSONObject commentObject = JSON.parseObject(comment);
				JSONArray week = commentObject.getJSONArray("week");
				JSONArray excellent = commentObject.getJSONArray("excellent");
				JSONArray good = commentObject.getJSONArray("good");

				if (week != null && week.size() > 10) {
					commentObject.put("week", week.subList(0, 10));
				}
				if (excellent != null && excellent.size() > 10) {
					commentObject.put("excellent", excellent.subList(0, 10));
				}
				if (good != null && good.size() > 10) {
					commentObject.put("good", good.subList(0, 10));
				}
				commentSectionCount = (week == null ? 0 : week.size()) + (excellent == null ? 0 : excellent.size())
						+ (good == null ? 0 : good.size());
				comment = commentObject.toJSONString();
			}
			dataMap.put("commentSectionCount", commentSectionCount);
			dataMap.put("comment", comment);
			break;
		}

		default:
			break;
		}
		return new Value(dataMap);
	}
}
