package com.lanking.uxb.service.homework.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.common.convert.VStudentHomeworkConvert;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkService;
import com.lanking.uxb.service.holiday.convert.HolidayStuHomeworkConvert;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuery;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkService;

/**
 * 悠数学移动端(学生作业相关接口)
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/hk/3")
public class ZyMStuHomework3Controller {

	@Autowired
	private ZyStudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private HolidayStuHomeworkService holidayStuHomeworkService;
	@Autowired
	private HolidayStuHomeworkConvert holidayStuHomeworkConvert;
	@Autowired
	private VStudentHomeworkConvert vstudentHomeworkConvert;

	/**
	 * 作业首页数据接口(历史作业返回已提交未下发的作业),包含寒假作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @param historySize
	 *            获取历史记录的条数
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "index", method = { RequestMethod.POST, RequestMethod.GET })
	public Value index(@RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(3);
		long clazzCount = zyHkStuClazzService.countStudentClazz(Security.getUserId(), null);
		dataMap.put("clazzCount", clazzCount);
		if (clazzCount > 0) {
			Set<StudentHomeworkStatus> stauts = Sets.newHashSet(StudentHomeworkStatus.SUBMITED,
					StudentHomeworkStatus.ISSUED);
			dataMap.put("historyCount", stuHkService.countAllHomeworks(Security.getUserId(), stauts));
			// 历史作业
			VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
			ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
			historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
			historyQuery.setCourse(false);
			historyQuery.setStudentId(Security.getUserId());
			CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
					CP.cursor(Long.MAX_VALUE, Math.min(historySize, 20)));
			if (historyPage.isEmpty()) {
				vp.setCursor(Long.MAX_VALUE);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
				List<Long> ids = new ArrayList<Long>(historySize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = historyPage.getItems();
				for (Map map : maps) {
					int type = ((BigInteger) map.get("type")).intValue();
					long id = ((BigInteger) map.get("id")).longValue();
					if (type == 1) {
						stuHkIds.add(id);
					} else if (type == 2) {
						stuHolidayHkIds.add(id);
					}
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				if (stuHolidayHkIds.size() > 0) {
					Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				vp.setCursor(historyPage.getNextCursor());
				vp.setItems(items);
			}
			dataMap.put("history", vp);
			// 待批改作业
			ZyStudentHomeworkQuery todoQuery = new ZyStudentHomeworkQuery();
			todoQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.NOT_SUBMIT));
			todoQuery.setCourse(false);
			todoQuery.setStudentId(Security.getUserId());
			CursorPage<Long, Map> todoPage = stuHkService.queryUnionHolidayStuHk(todoQuery,
					CP.cursor(Long.MAX_VALUE, 50));
			if (todoPage.isEmpty()) {
				dataMap.put("todo", Collections.EMPTY_LIST);
			} else {
				int todoSize = todoPage.getItemSize();
				List<VStudentHomework> items = new ArrayList<VStudentHomework>(todoSize);
				List<Long> ids = new ArrayList<Long>(todoSize);
				List<Long> stuHkIds = Lists.newArrayList();
				List<Long> stuHolidayHkIds = Lists.newArrayList();
				List<Map> maps = todoPage.getItems();
				for (Map map : maps) {
					int type = ((BigInteger) map.get("type")).intValue();
					long id = ((BigInteger) map.get("id")).longValue();
					if (type == 1) {
						stuHkIds.add(id);
					} else if (type == 2) {
						stuHolidayHkIds.add(id);
					}
					ids.add(id);
				}
				Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(todoSize);
				if (stuHkIds.size() > 0) {
					Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
					vs.putAll(stuHkConvert.to(map, false, true, false, false));
				}
				if (stuHolidayHkIds.size() > 0) {
					Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
					vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
				}
				for (Long id : ids) {
					items.add(vs.get(id));
				}
				dataMap.put("todo", items);
			}
		}
		return new Value(dataMap);
	}

	/**
	 * 作业首页历史作业数据接口(历史作业返回已提交未下发的作业),包含寒假作业
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "queryHistory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryHistory(long cursor, @RequestParam(value = "historySize", defaultValue = "20") int historySize) {
		VCursorPage<VStudentHomework> vp = new VCursorPage<VStudentHomework>();
		ZyStudentHomeworkQuery historyQuery = new ZyStudentHomeworkQuery();
		historyQuery.setStatus(Sets.newHashSet(StudentHomeworkStatus.SUBMITED, StudentHomeworkStatus.ISSUED));
		historyQuery.setCourse(false);
		historyQuery.setStudentId(Security.getUserId());
		CursorPage<Long, Map> historyPage = stuHkService.queryUnionHolidayStuHk(historyQuery,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(historySize, 20)));

		if (historyPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			List<VStudentHomework> items = new ArrayList<VStudentHomework>(historySize);
			List<Long> ids = new ArrayList<Long>(historySize);
			List<Long> stuHkIds = Lists.newArrayList();
			List<Long> stuHolidayHkIds = Lists.newArrayList();
			List<Map> maps = historyPage.getItems();
			for (Map map : maps) {
				int type = ((BigInteger) map.get("type")).intValue();
				long id = ((BigInteger) map.get("id")).longValue();
				if (type == 1) {
					stuHkIds.add(id);
				} else if (type == 2) {
					stuHolidayHkIds.add(id);
				}
				ids.add(id);
			}
			Map<Long, VStudentHomework> vs = new HashMap<Long, VStudentHomework>(historySize);
			if (stuHkIds.size() > 0) {
				Map<Long, StudentHomework> map = studentHomeworkService.mgetMap(stuHkIds);
				vs.putAll(stuHkConvert.to(map, false, true, false, false));
			}
			if (stuHolidayHkIds.size() > 0) {
				Map<Long, HolidayStuHomework> map = holidayStuHomeworkService.mgetMap(stuHolidayHkIds);
				vs.putAll(vstudentHomeworkConvert.to(holidayStuHomeworkConvert.to(map)));
			}
			for (Long id : ids) {
				items.add(vs.get(id));
			}
			vp.setCursor(historyPage.getNextCursor());
			vp.setItems(items);
		}
		return new Value(vp);
	}
}
