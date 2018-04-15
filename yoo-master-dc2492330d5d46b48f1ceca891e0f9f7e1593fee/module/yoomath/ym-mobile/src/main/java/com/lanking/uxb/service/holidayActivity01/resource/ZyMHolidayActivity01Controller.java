package com.lanking.uxb.service.holidayActivity01.resource;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Grade;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01UserCategoryGrade;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsType;
import com.lanking.cloud.domain.yoo.order.common.CoinsGoodsOrder;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.HolidayActivity01ClassService;
import com.lanking.uxb.service.activity.api.HolidayActivity01HomeworkService;
import com.lanking.uxb.service.activity.api.HolidayActivity01Service;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserCategoryGradeService;
import com.lanking.uxb.service.activity.api.HolidayActivity01UserService;
import com.lanking.uxb.service.activity.cache.HolidayActivity01GradeCacheService;
import com.lanking.uxb.service.activity.form.HolidayActivityOrderSaveForm;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;

@ApiAllowed
@RestController
@RequestMapping("zy/m/activity/holiday01")
public class ZyMHolidayActivity01Controller {

	@Autowired
	private HolidayActivity01Service holidayActivity01Service;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private HolidayActivity01HomeworkService holidayActivity01HomeworkService;
	@Autowired
	private HolidayActivity01ClassService holidayActivity01ClassService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private HolidayActivity01UserService holidayActivity01UserService;
	@Autowired
	private HolidayActivity01GradeCacheService gradeCacheService;
	@Autowired
	private HolidayActivity01UserCategoryGradeService holidayActivity01UserCategoryGradeService;
	
	/**
	 * 介绍页
	 * 
	 * @param code
	 * @return
	 */
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "introduceIndex", method = { RequestMethod.GET, RequestMethod.POST })
	public Value introduceIndex(Long code) {

		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity01 activity = holidayActivity01Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		// 老师没有阶段
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		if (teacher.getPhaseCode() == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_PHASE_NULL));
		}

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("cfg", holidayActivity01Service.getActivityCfg(code));
		data.put("phase", teacher.getPhaseCode());
		data.put("hasHoliday", holidayActivity01HomeworkService.countHk(code, Security.getUserId()) > 0);
		if (holidayActivity01HomeworkService.countHk(code, Security.getUserId()) > 0) {
			data.put("page", 3);
			return new Value(data);
		}
		if (gradeCacheService.getGrade(Security.getUserId(), code) != -1) {
			data.put("page", 2);
			return new Value(data);
		}
		data.put("page", 1);
		return new Value(data);

	}

	/**
	 * 介绍页点击年级数据作业
	 * 
	 * @param code
	 * @param grade
	 *            选择的年级
	 * @return
	 */
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "selectGrade", method = { RequestMethod.GET, RequestMethod.POST })
	public Value selectGrade(Long code, Integer grade) {
		// code为空
		if (code == null || grade == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity01 activity = holidayActivity01Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		// 校验活动用户，保证后面使用时已经有数据
		holidayActivity01UserService.checkHolidayActivity01User(code, Security.getUserId());
		gradeCacheService.update(Security.getUserId(), code, grade);
		return new Value();
	}

	/**
	 * 统计页
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "statisticsIndex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticsIndex(Long code) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity01 activity = holidayActivity01Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> cfg = new HashMap<>();
		cfg.put("minClassStudents", activity.getCfg().getMinClassStudents());
		cfg.put("luckyDrawOneHomework", activity.getCfg().getLuckyDrawOneHomework());
		cfg.put("submitRateThreshold", activity.getCfg().getSubmitRateThreshold());
		cfg.put("luckyDrawThreshold", activity.getCfg().getLuckyDrawThreshold());

		// 作业提交率统计
		Map<String, Object> percentMap = holidayActivity01HomeworkService.getPercent(code, Security.getUserId(),
				activity.getCfg());
		retMap.put("jobsubrate", percentMap);
		// 班级名称，班级提交率
		retMap.put("classsubrate", holidayActivity01ClassService.getByUserId(Security.getUserId(), code));
		retMap.put("cfg", cfg);

		return new Value(retMap);
	}

	/**
	 * 获得新增抽奖次数
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "newLuckyDraw", method = { RequestMethod.POST, RequestMethod.GET })
	public Value newLuckyDraw(long code) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		int newLuckyDraw = holidayActivity01Service.getNewLuckyDraw(code, Security.getUserId());
		retMap.put("newLuckyDraw", newLuckyDraw);
		retMap.put("userId", Security.getUserId());
		return new Value(retMap);
	}

	/**
	 * 抽奖页
	 * 
	 * @param code
	 *            活动code
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "lotteryIndex", method = { RequestMethod.POST, RequestMethod.GET })
	public Value lotteryIndex(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity01 activity = holidayActivity01Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>();
		retMap.put("info", holidayActivity01Service.getLotteryIndex(code, Security.getUserId()));
		return new Value(retMap);
	}

	/**
	 * 阶段和教材版本
	 * 
	 * @author zemin.song
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "phaseAndTextbookCategory", method = { RequestMethod.GET, RequestMethod.POST })
	public Value phaseAndTextbookCategory(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		HolidayActivity01 hActive = holidayActivity01Service.get(code);
		if (hActive == null) {
			return new Value(new EntityNotFoundException());
		}
		Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
		HolidayActivity01Cfg cfg = hActive.getCfg();
		Map<String, Object> retMap = new HashMap<String, Object>();
		TextbookCategory initTextbookCategory = null;
		List<TextbookCategory> textbookCategoryList = null;
		List<HolidayActivity01Grade> gradesList = null;
		if (teacher.getPhaseCode() == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_PHASE_NULL));
		}
		
		textbookCategoryList = textbookCategoryService.mgetList(cfg.getTextbookCategoryCodes2());
		gradesList = cfg.getGrades2();
		
		retMap.put("textbookCategorys", textbookCategoryConvert.to(textbookCategoryList));
		retMap.put("grades", this.setGradesName(gradesList));
		
		Integer currentCategory = null;
		Integer currentGrade = null;
		
		HolidayActivity01UserCategoryGrade userCategoryGrade = holidayActivity01UserCategoryGradeService.get(code, Security.getUserId());
		
		//首先获取上次保存的教材和年级
		if(userCategoryGrade != null){
			currentCategory = userCategoryGrade.getTextbookCategoryCode();
			currentGrade = userCategoryGrade.getGrade();
		} else {
			if (teacher.getTextbookCategoryCode() != null) {
				currentCategory = teacher.getTextbookCategoryCode();
			}
			
			currentGrade = gradeCacheService.getGrade(Security.getUserId(), code);
		}
		
		if (currentCategory != null) {
			// 当前选中
			for (TextbookCategory tc : textbookCategoryList) {
				if (tc != null && tc.getCode().intValue() == currentCategory.intValue()) {
					initTextbookCategory = tc;
					break;
				}
			}
		}
					
		if (initTextbookCategory == null) {
			// 默认苏科
			initTextbookCategory = textbookCategoryService.get(15);
		}
		HolidayActivity01Grade initGrade = null;
		if (currentGrade != null) {
			for (HolidayActivity01Grade grade : HolidayActivity01Grade.values()) {
				if (grade.getValue() == currentGrade.intValue()) {
					initGrade = grade;
					break;
				}
			}
		}
		
		if(initGrade == null || initGrade == HolidayActivity01Grade.PHASE_3_1
				|| initGrade == HolidayActivity01Grade.PHASE_3_2
				|| initGrade == HolidayActivity01Grade.PHASE_3_3){
			initGrade = HolidayActivity01Grade.PHASE_2_1;
		}
		
		retMap.put("grade", initGrade);
		retMap.put("textbookCategory", textbookCategoryConvert.to(initTextbookCategory));
		return new Value(retMap);
	}

	private Map<String, String> setGradesName(List<HolidayActivity01Grade> gradesList) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		for (HolidayActivity01Grade grade : gradesList) {
			map.put(grade.name(), grade.getName());
		}
		return map;
	}

	/**
	 * 抽奖
	 * 
	 * @param code
	 *            活动code
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "draw", method = { RequestMethod.POST, RequestMethod.GET })
	public Value draw(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity01 activity = holidayActivity01Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>();

		try {
			retMap.put("info", holidayActivity01Service.getHolidayDraw(code, Security.getUserId()));
		} catch (AbstractException e) {
			return new Value(e);
		}
		return new Value(retMap);
	}

	/**
	 * 分享
	 * 
	 * @return {@link Value}
	 */
	@RolesAllowed(anyone = true)
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "statisticsShare", method = { RequestMethod.POST, RequestMethod.GET })
	public Value statisticsShare(Long id) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 班级名称，班级提交率
		retMap.put("classsubrate", holidayActivity01ClassService.getClassById(id));

		return new Value(retMap);
	}

	/**
	 * 更新订单
	 * 
	 * @param code
	 *            活动code
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "updateHolidayOrder", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateHolidayOrder(HolidayActivityOrderSaveForm form) {
		if (form == null) {
			return new Value(new MissingArgumentException());
		}
		CoinsGoodsType type = CoinsGoodsType.valueOf(form.getGoodsType());
		if (type == CoinsGoodsType.TELEPHONE_CHARGE || type == CoinsGoodsType.QQ_VIP
				|| type == CoinsGoodsType.COUPONS) {
			if (form.getP0() == null) {
				return new Value(new MissingArgumentException());
			}
			if (form.getContactPhone() != null || form.getContactAddress() != null || form.getContactName() != null) {
				return new Value(new MissingArgumentException());
			}
		} else if (type == CoinsGoodsType.PHYSICAL_COMMODITY) {
			if (form.getContactPhone() == null || form.getContactAddress() == null || form.getContactName() == null) {
				return new Value(new MissingArgumentException());
			}
		}

		CoinsGoodsOrder coinsGoodsOrder = holidayActivity01Service.updateHolidayOrder(form);
		if (coinsGoodsOrder == null) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MEMPACK_ORDER_NOTFOUND));
		}
		return new Value();
	}
}
