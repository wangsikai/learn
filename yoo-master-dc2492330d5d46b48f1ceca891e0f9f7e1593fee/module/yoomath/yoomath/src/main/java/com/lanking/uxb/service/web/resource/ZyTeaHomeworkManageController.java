package com.lanking.uxb.service.web.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomework;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.web.form.HomeworkQueryForm;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkPage;

/**
 * 悠作业老师作业管理接口
 * 
 * @since yoomath V1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月14日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/t/hk/man")
public class ZyTeaHomeworkManageController {

	@Autowired
	private ZyHomeworkService zyHomeworkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyHomeworkClassService zyHkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHkClassConvert;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private HolidayHomeworkService holidayHomeworkService;
	@Autowired
	private SectionService sectionService;

	/**
	 * 教师作业管理接口
	 * 
	 * @since yoomath V1.2
	 * @param pageNo
	 *            页码
	 * @param size
	 *            每页获取大小
	 * @param todo
	 *            是否为待处理列表
	 * @param classId
	 *            班级ID
	 * @param needClazzs
	 *            是否要获取班级列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "2/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query2(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "40") int size,
			@RequestParam(value = "todo", defaultValue = "true") boolean todo,
			@RequestParam(value = "classId", required = false) Long classId,
			@RequestParam(value = "needClazzs", defaultValue = "false") boolean needClazzs) {
		size = Math.min(40, size);
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setClassId(classId);
		query.setCourse(false);
		if (todo) {// 获取待处理的
			query.setStatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE));
		}
		Page<Homework> page = zyHomeworkService.query(query, P.index(pageNo, size));

		VHomeworkPage vpage = new VHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());

		List<VHomework> vHomeworks = homeworkConvert.to(page.getItems(), new HomeworkConvertOption(true));
		if (CollectionUtils.isNotEmpty(vHomeworks)) {
			List<Long> homeworkClassIds = new ArrayList<Long>(vHomeworks.size());
			for (VHomework v : vHomeworks) {
				homeworkClassIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> map = zyHkClassConvert.to(zyHkClassService.mget(homeworkClassIds));
			for (VHomework v : vHomeworks) {
				v.setHomeworkClazz(map.get(v.getHomeworkClazzId()));
			}
		}
		vpage.setItems(vHomeworks);
		if (needClazzs) {// 是否需要班级列表
			List<HomeworkClazz> homeworkClazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isEmpty(homeworkClazzs)) {
				vpage.setHomeworkClazzs(new ArrayList<VHomeworkClazz>(0));
			} else {
				vpage.setHomeworkClazzs(zyHkClassConvert.to(homeworkClazzs));
			}

		}
		return new Value(vpage);
	}

	/**
	 * 教师作业管理接口
	 * 
	 * @since yoomath V1.9
	 * @param pageNo
	 *            页码
	 * @param size
	 *            每页获取大小
	 * @param todo
	 *            是否为待处理列表
	 * @param classId
	 *            班级ID
	 * @param needClazzs
	 *            是否要获取班级列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "3/query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query3(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "40") int size,
			@RequestParam(value = "todo", defaultValue = "true") boolean todo,
			@RequestParam(value = "classId", required = false) Long classId,
			@RequestParam(value = "needClazzs", defaultValue = "false") boolean needClazzs, Integer type) {
		size = Math.min(40, size);
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		query.setTeacherId(Security.getUserId());
		query.setClassId(classId);
		query.setCourse(false);
		query.setType(type);
		if (todo) {// 获取待处理的
			query.setStatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE));
			query.setHolidayHkstatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH));
		} else {
			query.setStatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE,
					HomeworkStatus.ISSUED));
			query.setHolidayHkstatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH,
					HomeworkStatus.NOT_ISSUE, HomeworkStatus.ISSUED));
		}
		// Page<Homework> page = zyHomeworkService.query(query, P.index(pageNo,
		// size));

		Page<Map> page = zyHomeworkService.queryUnionHolidayHomework(query, P.index(pageNo, size));
		List<Long> homeworkIds = Lists.newArrayList();
		List<Long> holidayHomeworkIds = Lists.newArrayList();
		for (Map map : page.getItems()) {
			// type 1为普通作业
			if (Integer.valueOf(map.get("type").toString()) == 1) {
				homeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
			// type 2为假期作业
			if (Integer.valueOf(map.get("type").toString()) == 2) {
				holidayHomeworkIds.add(Long.valueOf(map.get("id").toString()));
			}
		}
		// 假期作业构造Vhomework
		List<VHomework> vhomeworks2 = Lists.newArrayList();
		// 班级ID集合（只用于假期作业班级ID）
		List<Long> clazzIds = Lists.newArrayList();
		Map<Long, HolidayHomework> holidayMap = holidayHomeworkService.mget(holidayHomeworkIds);
		for (HolidayHomework holidayHomework : holidayMap.values()) {
			clazzIds.add(holidayHomework.getHomeworkClassId());
		}
		Map<Long, HomeworkClazz> clazzMap = zyHkClassService.mget(clazzIds);
		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(clazzMap);
		for (HolidayHomework h : holidayMap.values()) {
			VHomework homework = new VHomework();
			homework.setName(h.getName());
			homework.setDifficulty(h.getDifficulty());
			homework.setStartTime(h.getStartTime());
			homework.setDeadline(h.getDeadline());
			homework.setStatus(h.getStatus());
			homework.setId(h.getId());
			homework.setHomeworkClazz(vClazzMap.get(h.getHomeworkClassId()));
			// 2是假期作业
			homework.setType(2);
			homework.setRightRate(h.getRightRate());
			homework.setCompletionRate(h.getCompletionRate() == null ? BigDecimal.valueOf(0) : h.getCompletionRate());
			vhomeworks2.add(homework);
		}
		VHomeworkPage vpage = new VHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		List<VHomework> vHomeworks = new ArrayList<VHomework>();
		if (CollectionUtils.isNotEmpty(homeworkIds)) {
			vHomeworks = homeworkConvert.to(Lists.newArrayList(homeworkService.mget(homeworkIds).values()),
					new HomeworkConvertOption(true));
		}
		if (CollectionUtils.isNotEmpty(vHomeworks)) {
			List<Long> homeworkClassIds = new ArrayList<Long>(vHomeworks.size());
			for (VHomework v : vHomeworks) {
				homeworkClassIds.add(v.getHomeworkClazzId());
			}
			Map<Long, VHomeworkClazz> map = zyHkClassConvert.to(zyHkClassService.mget(homeworkClassIds));
			for (VHomework v : vHomeworks) {
				v.setHomeworkClazz(map.get(v.getHomeworkClazzId()));
			}
		}
		if (CollectionUtils.isNotEmpty(vhomeworks2)) {
			vHomeworks.addAll(vhomeworks2);
		}
		List<VHomework> homeworkItems = Lists.newArrayList();
		for (Map map : page.getItems()) {
			Long id = Long.valueOf(map.get("id").toString());
			for (VHomework vHomework : vHomeworks) {
				if (vHomework.getId() == id) {
					homeworkItems.add(vHomework);
				}
			}
		}
		vpage.setItems(homeworkItems);
		if (needClazzs) {// 是否需要班级列表
			List<HomeworkClazz> homeworkClazzs = zyHkClassService.listCurrentClazzs(Security.getUserId());
			if (CollectionUtils.isEmpty(homeworkClazzs)) {
				vpage.setHomeworkClazzs(new ArrayList<VHomeworkClazz>(0));
			} else {
				vpage.setHomeworkClazzs(zyHkClassConvert.to(homeworkClazzs));
			}

		}
		return new Value(vpage);
	}

	/**
	 * 查询作业列表.
	 * 
	 * @param form
	 *            查询参数表单
	 * @since v2.0.3(web v2.0)
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryCommons")
	public Value queryCommons(HomeworkQueryForm form) {
		int pageNo = form.getPageNo() == null ? 1 : form.getPageNo();
		int size = form.getSize() == null ? 10 : form.getSize();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		if (form.getBt() != null) {
			query.setBeginTime(new Date(form.getBt()));
		}
		if (form.getEt() != null) {
			query.setEndTime(new Date(form.getEt()));
		}
		query.setClassId(form.getClassId());
		query.setKey(form.getKey());
		query.setTeacherId(Security.getUserId());
		query.setSectionName(form.getSectionName());
		query.setLineQuestion(form.getIsLineQuestion());
		query.setHomeworkStatus(form.getHkStatus());
		Page<Homework> page = zyHomeworkService.queryHomeworkWeb3(query, P.index(pageNo, size));

		VHomeworkPage vpage = new VHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());

		List<Homework> homeworks = page.getItems();
		List<VHomework> items = new ArrayList<VHomework>();
		if (CollectionUtils.isNotEmpty(homeworks)) {
			items = homeworkConvert.to(homeworks, new HomeworkConvertOption(true));
		}

		List<Long> clazzIds = Lists.newArrayList();
		for (Homework homework : homeworks) {
			clazzIds.add(homework.getHomeworkClassId());
		}
		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
		for (VHomework v : items) {
			v.setHomeworkClazz(vClazzMap.get(v.getHomeworkClazzId()));
		}

		vpage.setItems(items);
		return new Value(vpage);
	}

	/**
	 * 查询假期作业列表.
	 * 
	 * @param form
	 *            查询参数表单
	 * @since v2.0.3(web v2.0)
	 * @since v2.0.8 新增会员过滤
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryHolidays")
	public Value queryHolidays(HomeworkQueryForm form) {
		int pageNo = form.getPageNo() == null ? 1 : form.getPageNo();
		int size = form.getSize() == null ? 10 : form.getSize();
		ZyHomeworkQuery query = new ZyHomeworkQuery();
		if (form.getBt() != null) {
			query.setBeginTime(new Date(form.getBt()));
		}
		if (form.getEt() != null) {
			query.setEndTime(new Date(form.getEt()));
		}
		query.setClassId(form.getClassId());
		query.setKey(form.getKey());
		query.setHolidayHkstatus(Sets.newHashSet(HomeworkStatus.INIT, HomeworkStatus.PUBLISH, HomeworkStatus.NOT_ISSUE,
				HomeworkStatus.ISSUED));
		query.setTeacherId(Security.getUserId());
		if (form.getStatus() != null) {
			if (form.getStatus() == HomeworkStatus.NOT_ISSUE || form.getStatus() == HomeworkStatus.ISSUED) {
				query.setStatus(Sets.newHashSet(HomeworkStatus.NOT_ISSUE, HomeworkStatus.ISSUED));
			} else {
				query.setStatus(Sets.newHashSet(form.getStatus()));
			}
		}

		Page<HolidayHomework> page = holidayHomeworkService.queryHolidayHomeworkWeb2(query, P.index(pageNo, size));

		VHomeworkPage vpage = new VHomeworkPage();
		vpage.setCurrentPage(pageNo);
		vpage.setPageSize(size);
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());

		List<Long> clazzIds = Lists.newArrayList();
		List<Long> HolidayHomeworkIds = Lists.newArrayList();
		List<HolidayHomework> holidayHomeworks = page.getItems();
		List<VHomework> items = new ArrayList<VHomework>();
		if (CollectionUtils.isNotEmpty(holidayHomeworks)) {
			for (HolidayHomework h : holidayHomeworks) {
				VHomework homework = new VHomework();
				homework.setName(h.getName());
				homework.setDifficulty(h.getDifficulty());
				homework.setStartTime(h.getStartTime());
				homework.setDeadline(h.getDeadline());
				homework.setStatus(h.getStatus());
				homework.setId(h.getId());
				homework.setHomeworkClazzId(h.getHomeworkClassId());
				homework.setType(2); // 2是假期作业
				homework.setRightRate(h.getRightRate());
				homework.setCompletionRate(
						h.getCompletionRate() == null ? BigDecimal.valueOf(0) : h.getCompletionRate());
				homework.setQuestionCount(h.getQuestionCount());
				items.add(homework);
				clazzIds.add(h.getHomeworkClassId());
				HolidayHomeworkIds.add(h.getId());
			}
		}

		Map<Long, Integer> itemCountMap = holidayHomeworkService.queryHolidayHomeworkItemCount(HolidayHomeworkIds);
		Map<Long, VHomeworkClazz> vClazzMap = zyHkClassConvert.to(zyHkClassService.mget(clazzIds));
		for (VHomework v : items) {
			v.setHomeworkClazz(vClazzMap.get(v.getHomeworkClazzId()));
			Integer itemCount = itemCountMap.get(v.getId());
			v.setRightCount(itemCount == null ? 0 : itemCount); // 临时用于专项数量
		}

		vpage.setItems(items);
		return new Value(vpage);
	}

	/**
	 * 获取作业管理首页数据.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getManageIndexDatas")
	public Value getManageIndexDatas(String scode) {
		Map<String, Object> map = new HashMap<String, Object>(2);
		long userId = Security.getUserId();
		Date commonStartDate = homeworkService.getFirstCreateAt(userId);
		Date holidayStartDate = holidayHomeworkService.getFirstCreateAt(userId);
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MINUTE, +1);
		map.put("endDate", cal.getTime());
		map.put("commonStartDate", commonStartDate);
		map.put("holidayStartDate", holidayStartDate);
		if (StringUtils.isNotBlank(scode)) {
			try {
				long code = Long.parseLong(scode);
				Section section = sectionService.get(code);
				map.put("section", section);
			} catch (NumberFormatException e) {
			}
		}
		return new Value(map);
	}
}
