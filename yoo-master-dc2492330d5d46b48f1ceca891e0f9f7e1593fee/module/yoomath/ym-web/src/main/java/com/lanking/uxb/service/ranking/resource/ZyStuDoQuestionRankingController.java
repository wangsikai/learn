package com.lanking.uxb.service.ranking.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassStat;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolStat;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingQuery;
import com.lanking.uxb.service.ranking.api.DoQuestionRankingService;
import com.lanking.uxb.service.ranking.convert.ClassDoQuestionRankingConvert;
import com.lanking.uxb.service.ranking.convert.SchoolDoQuestionRankingConvert;
import com.lanking.uxb.service.ranking.form.DoQuestionRankingForm;
import com.lanking.uxb.service.ranking.value.VSchoolDoQuestionRanking;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkStudentClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazz;

/**
 * 学生排行榜
 * 
 * @since 2.0
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/s/ranking/doQuestion")
public class ZyStuDoQuestionRankingController {

	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkStudentClazzConvert hkStuClazzConvert;
	@Autowired
	private DoQuestionRankingService doQuestionRankingService;
	@Autowired
	private ClassDoQuestionRankingConvert classDoQuestionRankingConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolDoQuestionRankingConvert schoolDoQuestionRankingConvert;

	/**
	 * 排行榜列表
	 * 
	 * @param form
	 * @param topN
	 *            查询条数
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value rankList(DoQuestionRankingForm form, @RequestParam(defaultValue = "10") int topN,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int pageSize) {
		Map<String, Object> data = new HashMap<String, Object>();
		Long currentClassId = form.getClassId();
		List<HomeworkStudentClazz> stuClazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
		List<VHomeworkStudentClazz> vstuClazzs = hkStuClazzConvert.to(stuClazzs);
		List<Long> classIds = new ArrayList<Long>(stuClazzs.size());
		for (VHomeworkStudentClazz v : vstuClazzs) {
			classIds.add(v.getClassId());
		}
		Map<Long, HomeworkClazz> clazzs = zyHkClassService.mget(classIds);
		if (stuClazzs.size() > 0) {
			// 学校名称
			List<HomeworkClazz> poList = new ArrayList<HomeworkClazz>(stuClazzs.size());
			for (Long clazzId : classIds) {
				poList.add(clazzs.get(clazzId));
			}
			if (poList.get(0).getTeacherId() != null) {
				Teacher teacher = (Teacher) teacherService.getUser(poList.get(0).getTeacherId());
				// 没有传类型，说明是首次，默认取最后一个加入班级
				if (form.getType() == null) {
					currentClassId = stuClazzs.get(0).getClassId();
					// 如果没有班级，没有排行榜信息
					data.put("classCount", stuClazzs.size());
					Map<Long, VHomeworkClazz> vclazzs = zyHkClassConvert.to(clazzs, new ZyHomeworkClassConvertOption(
							true, false, false));
					for (VHomeworkStudentClazz v : vstuClazzs) {
						v.setHomeworkClazz(vclazzs.get(v.getClassId()));
					}
					data.put("clazzs", vstuClazzs);
					if (teacher.getSchoolId() != null && teacher.getSchoolId() > 0) {
						data.put("school", schoolService.get(teacher.getSchoolId()).getName());
					}
					form.setType("class");
				} else {
					if (form.getType() == "class") {
						currentClassId = form.getClassId();
					}
				}
				DoQuestionRankingQuery query = new DoQuestionRankingQuery();
				query.setDay(form.getDay());
				if ("class".equals(form.getType())) {
					query.setClassId(currentClassId);
					List<DoQuestionClassStat> list = doQuestionRankingService.listDoQuestionClassStatTopN(query, topN);
					if (list.size() > 0) {
						data.put("items", classDoQuestionRankingConvert.to(list));
						query.setStudentId(Security.getUserId());
						DoQuestionClassStat myStat = doQuestionRankingService.findStudentInClassStat(query);
						if (myStat != null) {
							data.put("myStat", classDoQuestionRankingConvert.to(myStat));
						}
					}
				} else if ("school".equals(form.getType())) {
					int phaseCode = teacher.getPhaseCode() == null ? 0 : teacher.getPhaseCode();
					long schoolId = teacher.getSchoolId() == null ? 0 : teacher.getSchoolId();
					if (phaseCode > 0 && schoolId > 0) {
						query.setPhaseCode(phaseCode);
						query.setSchoolId(schoolId);
						Page<DoQuestionSchoolStat> cp = doQuestionRankingService.query(query, P.index(page, pageSize));
						VPage<VSchoolDoQuestionRanking> vp = new VPage<VSchoolDoQuestionRanking>();
						int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
						vp.setPageSize(pageSize);
						vp.setCurrentPage(page);
						vp.setTotalPage(tPage);
						vp.setTotal(cp.getTotalCount());
						vp.setItems(schoolDoQuestionRankingConvert.to(cp.getItems()));
						return new Value(vp);
					}
				}
			}
		}
		if (!data.containsKey("items")) {
			data.put("items", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}
}
