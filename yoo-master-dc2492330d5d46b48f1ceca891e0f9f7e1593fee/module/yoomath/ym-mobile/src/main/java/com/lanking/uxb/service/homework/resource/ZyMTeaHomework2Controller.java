package com.lanking.uxb.service.homework.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.homework.form.TeaHomeworkFilterForm;
import com.lanking.uxb.service.homework.value.VHomeworkPage;
import com.lanking.uxb.service.homework.value.VHomeworkStatus;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 教师端作业相关接口
 * 
 * @since 1.3.0
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年7月6日
 */
@RestController
@RequestMapping("zy/m/t/hk/2")
public class ZyMTeaHomework2Controller {

	@Autowired
	private ZyHomeworkService zyHkService;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClassConvert;
	@Autowired
	private HomeworkConvert homeworkConvert;

	@Deprecated
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(TeaHomeworkFilterForm form) {
		int pageNo = form.getPageNo() == null ? 1 : (form.getPageNo().intValue() <= 0 ? 1 : form.getPageNo());
		long endTime = form.getEndTime() == null ? System.currentTimeMillis() : form.getEndTime();
		VHomeworkPage vpage = new VHomeworkPage();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setEndTime(new Date(endTime));
		if (form.getStatus() != null) {
			Set<HomeworkStatus> status = new HashSet<>();
			status.add(form.getStatus());
			query.setStatus(status);
			if (form.getStatus() == HomeworkStatus.ISSUED) {
				query.setIssueKey(1);
			}
		}
		if (form.getClassId() != null) {
			query.setClassId(form.getClassId());
		}
		if (form.isClassManage()) {
			query.setClassManage(true);
		}
		Page<Homework> homeworkPage = zyHkService.queryForMobile(query, P.index(pageNo, Math.min(form.getSize(), 20)));
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		if (homeworkPage.isNotEmpty()) {
			List<VHomeworkClazz> vclazzs = homeworkClassConvert.to(clazzs);
			Map<Long, VHomeworkClazz> vclazzMap = new HashMap<Long, VHomeworkClazz>(vclazzs.size());

			for (VHomeworkClazz v : vclazzs) {
				vclazzMap.put(v.getId(), v);
			}
			List<VHomework> homeworks = homeworkConvert.to(homeworkPage.getItems());
			for (VHomework v : homeworks) {
				v.setHomeworkClazz(vclazzMap.get(v.getHomeworkClazzId()));
			}
			vpage.setItems(homeworks);
		} else {
			vpage.setItems(Collections.EMPTY_LIST);
		}
		vpage.setCurrentPage(pageNo);
		vpage.setEndTime(endTime);
		vpage.setPageSize(20);
		vpage.setTotal(homeworkPage.getTotalCount());
		vpage.setTotalPage(homeworkPage.getPageCount());
		// 当前班级
		if (CollectionUtils.isEmpty(clazzs)) {
			vpage.setHasClazz(false);
		} else {
			vpage.setHasClazz(true);
		}

		return new Value(vpage);
	}

	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryFilter", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryFilter() {
		Map<String, Object> data = new HashMap<String, Object>();
		// 状态
		List<VHomeworkStatus> vList = new ArrayList<>();
		// 顺序 作业中PUBLISH 待批改NOT_ISSUE 已下发ISSUED 待分发INIT
		vList.add(new VHomeworkStatus(HomeworkStatus.PUBLISH));
		vList.add(new VHomeworkStatus(HomeworkStatus.NOT_ISSUE));
		vList.add(new VHomeworkStatus(HomeworkStatus.ISSUED));
		vList.add(new VHomeworkStatus(HomeworkStatus.INIT));

		data.put("homeworkStatus", vList);

		// 班级
		List<HomeworkClazz> clazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
		data.put("clazzs", clazzs);
		return new Value(data);
	}
}
