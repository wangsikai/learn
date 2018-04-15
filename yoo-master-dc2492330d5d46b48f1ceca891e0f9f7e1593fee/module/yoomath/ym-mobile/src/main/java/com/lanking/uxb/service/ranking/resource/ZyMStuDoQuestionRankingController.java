package com.lanking.uxb.service.ranking.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingQuery;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingService;
import com.lanking.uxb.service.ranking.convert.ClassDoQuestionRankingConvert;
import com.lanking.uxb.service.ranking.convert.SchoolDoQuestionRankingConvert;
import com.lanking.uxb.service.ranking.form.DoQuestionRankingForm;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * 答题排行相关接口
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月24日
 */
@RestController
@RequestMapping("zy/m/s/ranking/doQuestion")
public class ZyMStuDoQuestionRankingController {

	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkClassService hcService;
	@Autowired
	private ZyHomeworkClazzConvert hcConvert;
	@Autowired
	private DoQuestionRankingService doQuestionRankingService;
	@Autowired
	private ClassDoQuestionRankingConvert classDoQuestionRankingConvert;
	@Autowired
	private SchoolDoQuestionRankingConvert schoolDoQuestionRankingConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolService schoolService;

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public Value list(DoQuestionRankingForm form) {
		if (form == null || form.getDay() == null || StringUtils.isBlank(form.getType())) {
			return new Value(new IllegalArgException());
		}
		Map<String, Object> data = new HashMap<String, Object>(5);
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzsHasTeacher(Security.getUserId());
		int clazzCount = clazzs.size();
		List<Long> clazzIds = new ArrayList<Long>(clazzCount);
		for (HomeworkStudentClazz homeworkStudentClazz : clazzs) {
			clazzIds.add(homeworkStudentClazz.getClassId());
		}
		data.put("clazzCount", clazzCount);

		if (clazzCount > 0) {
			Map<Long, HomeworkClazz> poMap = hcService.mget(clazzIds);
			List<HomeworkClazz> poList = new ArrayList<HomeworkClazz>(clazzCount);
			for (Long clazzId : clazzIds) {
				poList.add(poMap.get(clazzId));
			}
			// 待优化 这里继续点击选择班级后可以不需要查询班级了
			data.put("clazzs", hcConvert.to(poList));
			// 学校名称
			Teacher teacher = (Teacher) teacherService.getUser(poList.get(0).getTeacherId());
			if (teacher.getSchoolId() != null && teacher.getSchoolId() > 0) {
				data.put("school", schoolService.get(teacher.getSchoolId()).getName());
			}

			DoQuestionRankingQuery query = new DoQuestionRankingQuery();
			query.setDay(form.getDay());
			switch (form.getType()) {
			case "class":
				if (form.getClassId() == null || (form.getClassId() != null && clazzIds.contains(form.getClassId()))) {
					query.setClassId(form.getClassId() == null ? clazzIds.get(0) : form.getClassId());
					List<DoQuestionClassStat> list = doQuestionRankingService.listDoQuestionClassStatTopN(query, 10);
					if (list.size() > 0) {
						data.put("items", classDoQuestionRankingConvert.to(list));
					}
					DoQuestionClassStat meRank = null;
					for (DoQuestionClassStat stat : list) {
						if (stat.getUserId() == Security.getUserId()) {
							meRank = stat;
							break;
						}
					}
					if (meRank == null) {
						// 学生个人在班级里的排名情况
						query.setStudentId(Security.getUserId());
						// 若班级里有排名数据，但本人数据，自3.0.2后还要查看自己的信息，显示为"--"
						meRank = doQuestionRankingService.findStudentInClassStat(query);
						if (meRank == null && CollectionUtils.isNotEmpty(list)) {
							meRank = new DoQuestionClassStat();
							meRank.setUserId(Security.getUserId());
						}
					}
					data.put("meRank", classDoQuestionRankingConvert.to(meRank));
				}
				break;
			case "school":
				int phaseCode = teacher.getPhaseCode() == null ? 0 : teacher.getPhaseCode();
				long schoolId = teacher.getSchoolId() == null ? 0 : teacher.getSchoolId();
				if (phaseCode > 0 && schoolId > 0) {
					query.setPhaseCode(phaseCode);
					query.setSchoolId(schoolId);
					List<DoQuestionSchoolStat> list = doQuestionRankingService.listDoQuestionSchoolStatTopN(query, 50);
					if (list.size() > 0) {
						data.put("items", schoolDoQuestionRankingConvert.to(list));
					}
				}
				break;
			}
		}
		if (!data.containsKey("items")) {
			data.put("items", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}
}
