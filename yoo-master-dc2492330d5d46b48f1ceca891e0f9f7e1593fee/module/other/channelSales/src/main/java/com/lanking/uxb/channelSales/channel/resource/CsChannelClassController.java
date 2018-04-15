package com.lanking.uxb.channelSales.channel.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.channel.api.CsAggClassHomeworkHisService;
import com.lanking.uxb.channelSales.channel.api.CsAggClassHomeworkService;
import com.lanking.uxb.channelSales.channel.api.CsHolidayHomeworkService;
import com.lanking.uxb.channelSales.channel.api.CsHomeworkQuery;
import com.lanking.uxb.channelSales.channel.api.CsHomeworkService;
import com.lanking.uxb.channelSales.channel.value.VHomework;

/**
 * 我的渠道--班级数据
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("channelSales/class")
public class CsChannelClassController {
	@Autowired
	private CsHomeworkService hkService;
	@Autowired
	private CsHolidayHomeworkService holidayHkservice;
	@Autowired
	private CsAggClassHomeworkService classHkService;
	@Autowired
	private CsAggClassHomeworkHisService classHkHisService;
	@Autowired
	private CsHomeworkClassService hkClassService;

	/**
	 * 获取班级的数据
	 * 
	 * @param classId
	 * @return
	 */
	@RequestMapping(value = "classData")
	public Value classData(Long classId) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("data", classHkService.queryMapByClass(classId));
		data.put("other", hkClassService.queryClassInfo(classId));
		data.put("vipCount", hkClassService.countVip(classId));
		data.put("stuCount", hkClassService.countStu(classId));
		return new Value(data);
	}

	/**
	 * 作业作业量、提交率统计数据
	 * 
	 * @param classId
	 * @param day0
	 *            7,15,30
	 * @return
	 */
	@RequestMapping(value = "hkStat")
	public Value hkStat(Long classId, Integer day0) {
		return new Value(classHkHisService.hkStat(classId, day0));
	}

	/**
	 * 班级普通作业列表/假期作业列表
	 * 
	 * @param classId
	 * @param isCommon
	 *            是否普通作业
	 * @return
	 */
	@RequestMapping(value = "homeworkList")
	public Value homeworkList(CsHomeworkQuery query) {
		List<VHomework> vList = new ArrayList<VHomework>();
		VPage<VHomework> vp = new VPage<VHomework>();
		if (query.getIsCommon()) {
			Page<Homework> page = hkService.query(query, P.index(query.getPage(), query.getPageSize()));
			int tPage = (int) (page.getTotalCount() + query.getPageSize() - 1) / query.getPageSize();
			vp.setPageSize(query.getPageSize());
			vp.setCurrentPage(query.getPage());
			vp.setTotalPage(tPage);
			vp.setTotal(page.getTotalCount());
			for (Homework h : page.getItems()) {
				VHomework v = new VHomework();
				v.setName(h.getName());
				v.setStartAt(h.getStartTime());
				v.setEndAt(h.getDeadline());
				v.setCommitCount(h.getCommitCount());
				v.setDistributeCount(h.getDistributeCount());
				v.setRightRate(h.getRightRate());
				if (h.getStatus() == HomeworkStatus.INIT) {
					v.setStatusName("待分发");
				} else if (h.getStatus() == HomeworkStatus.PUBLISH) {
					v.setStatusName("作业中");
				} else if (h.getStatus() == HomeworkStatus.NOT_ISSUE) {
					v.setStatusName("待批改");
				} else if (h.getStatus() == HomeworkStatus.ISSUED) {
					v.setStatusName("已下发");
				}
				vList.add(v);
			}
			vp.setItems(vList);
		} else {
			Page<HolidayHomework> page = holidayHkservice.query(query, P.index(query.getPage(), query.getPageSize()));
			for (HolidayHomework h : page.getItems()) {
				VHomework v = new VHomework();
				v.setName(h.getName());
				v.setStartAt(h.getStartTime());
				v.setEndAt(h.getDeadline());
				v.setRightRate(h.getRightRate());
				if (h.getStatus() == HomeworkStatus.ISSUED) {
					v.setStatusName("已下发");
				} else {
					v.setStatusName("作业中");
				}
				v.setCompletionRate(h.getCompletionRate());
				vList.add(v);
			}
			int tPage = (int) (page.getTotalCount() + query.getPageSize() - 1) / query.getPageSize();
			vp.setPageSize(query.getPageSize());
			vp.setCurrentPage(query.getPage());
			vp.setTotalPage(tPage);
			vp.setTotal(page.getTotalCount());
			vp.setItems(vList);
		}
		return new Value(vp);
	}
}
