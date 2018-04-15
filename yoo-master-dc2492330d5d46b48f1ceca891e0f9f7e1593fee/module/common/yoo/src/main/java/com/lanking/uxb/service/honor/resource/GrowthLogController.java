package com.lanking.uxb.service.honor.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.UserLevels;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthAction;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskRuleCfg;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.GrowthRuleService;
import com.lanking.uxb.service.honor.api.GrowthService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.api.UserLevelsService;
import com.lanking.uxb.service.honor.api.UserTaskLogService;
import com.lanking.uxb.service.honor.api.UserTaskService;
import com.lanking.uxb.service.honor.cache.GrowthCacheService;
import com.lanking.uxb.service.honor.convert.GrowthLogConvert;
import com.lanking.uxb.service.honor.value.VGrowthLog;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 荣誉-成长值相关rest API
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
@RestController
@RequestMapping("honor/growth")
public class GrowthLogController {

	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private UserLevelsService userLevelsService;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private GrowthLogConvert growthLogConvert;
	@Autowired
	private GrowthService growthService;
	@Autowired
	private CoinsService coninsService;
	@Autowired
	private GrowthCacheService growthCacheService;
	@Autowired
	private GrowthRuleService growthRuleService;
	@Autowired
	private UserTaskLogService userTaskLogService;
	@Autowired
	private UserTaskService userTaskService;

	/**
	 * 获取用户荣誉相关
	 * 
	 * @return
	 */
	@RequestMapping(value = "getUserHonor", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserHonor() {
		return new Value(getHonor());
	}

	private VUserHonor getHonor() {
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		// 如果为空,说明用户还没有做任何提高荣誉的操作
		if (honor == null) {
			vhonor.setCoins(0);
			vhonor.setGrowth(0);
			vhonor.setLevel(1);
			vhonor.setNextLevel(2);
			UserLevels ul = userLevelsService.getUserLevel(2, Product.YOOMATH);
			vhonor.setUpNeedGrowth(ul.getMinGrowthValue());
			vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MIDDLELEVEL, Product.YOOMATH));
		} else {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
			if (honor.getLevel() != UserLevelsService.MAXLEVEL) {
				vhonor.setNextLevel(honor.getLevel() + 1);
				UserLevels ul = userLevelsService.getUserLevel(honor.getLevel() + 1, Product.YOOMATH);
				vhonor.setUpNeedGrowth(ul.getMinGrowthValue() - honor.getGrowth());
			}
			// 零时签到返回参数
			if (Security.getUserType() == UserType.STUDENT) {
				Date date = new Date();
				String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
				Date yesterday = null;
				try {
					date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
					yesterday = DateUtils.addDays(date, -1);
				} catch (ParseException e) {
				}
				UserTaskLog log = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(), date);
				vhonor.setCheckIn(log != null);
				if (log != null) {
					String content = log.getContent();
					JSONObject contentJsonObj = JSONObject.parseObject(content);
					vhonor.setCheckInCount(contentJsonObj.getLong("signDay"));
					if (contentJsonObj.getLong("signDay") > 1)
						vhonor.setYesterdayCheckIn(true);
				} else {
					UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(101010001, Security.getUserId(),
							yesterday);
					if (yesterdayLog == null) {
						vhonor.setCheckInCount(0L);
					} else {
						vhonor.setYesterdayCheckIn(true);
						String content = yesterdayLog.getContent();
						JSONObject contentJsonObj = JSONObject.parseObject(content);
						vhonor.setCheckInCount(contentJsonObj.getLong("signDay"));
					}
				}

			} else {
				String yesterdayKey = growthCacheService.getYesterdayKey(GrowthAction.DAILY_CHECKIN,
						Security.getUserId());
				int yesterdayGrowth = (int) growthCacheService.get(yesterdayKey);
				// 表示昨天签到过
				if (yesterdayGrowth != -1) {
					vhonor.setYesterdayCheckIn(true);
				}
				vhonor.setCheckIn(growthLogService.getCheck(GrowthAction.DAILY_CHECKIN, Security.getUserId()));
			}
			if (honor.getLevel() > UserLevelsService.MIDDLELEVEL || honor.getLevel() == UserLevelsService.MIDDLELEVEL) {
				vhonor.setLevels(userLevelsService.getUserLevel(UserLevelsService.MIDDLELEVEL - 1,
						UserLevelsService.MAXLEVEL, Product.YOOMATH));
			} else {
				vhonor.setLevels(userLevelsService.getUserLevel(0, UserLevelsService.MIDDLELEVEL, Product.YOOMATH));
			}
			String continuousKey = growthCacheService.getContinuousCheckInKey(GrowthAction.DAILY_CHECKIN,
					Security.getUserId());
			// 先判断缓存里是否存在，不存在去数据库查
			if (Security.getUserType() == UserType.TEACHER) {
				if (growthCacheService.get(continuousKey) != -1) {
					vhonor.setCheckInCount(growthCacheService.get(continuousKey));
				} else {
					GrowthLog lastestGlog = growthLogService.getLastestCheckIn(Security.getUserId(), growthRuleService
							.getByAction(GrowthAction.DAILY_CHECKIN).getCode());
					if (lastestGlog != null) {
						vhonor.setCheckInCount(Long.parseLong(lastestGlog.getP1()));
					}
				}

			}
		}

		return vhonor;
	}

	// 获取今天签到能得到的值 和明天能得到的值
	private Map<String, Map<String, Integer>> getNextSignReward() {
		Map<String, Map<String, Integer>> retMap = new HashMap<String, Map<String, Integer>>();
		Date date = new Date();
		String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(date);
		Date yesterday = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
			yesterday = DateUtils.addDays(date, -1);
		} catch (ParseException e) {
		}
		// 签到任务code
		int taskCode = 101010001;
		UserTaskLog yesterdayLog = userTaskLogService.findByCodeAndUser(taskCode, Security.getUserId(), yesterday);
		UserTaskRuleCfg cfg = userTaskService.get(taskCode).getUserTaskRuleCfg();
		List<Integer> itemCoins = cfg.getItemCoins();
		List<Integer> itemGrowth = cfg.getItemGrowth();
		int coins = 0, growth = 0, stars = 0;
		Map<String, Integer> twoDayMap = new HashMap<String, Integer>();
		for (Integer c : itemCoins) {
			coins += c;
		}
		for (Integer g : itemGrowth) {
			growth += g;
		}
		twoDayMap.put("coins", coins);
		twoDayMap.put("growth", growth);
		coins = 0;
		growth = 0;
		stars = 0;
		Map<String, Integer> oneDayMap = new HashMap<String, Integer>();
		if (itemCoins != null) {
			coins = itemCoins.get(0);
		}
		if (itemGrowth != null) {
			growth = itemGrowth.get(0);
		}
		oneDayMap.put("coins", coins);
		oneDayMap.put("growth", growth);
		if (yesterdayLog != null) {
			retMap.put("toDay", twoDayMap);
			retMap.put("nextDay", twoDayMap);
		} else {
			retMap.put("toDay", oneDayMap);
			retMap.put("nextDay", twoDayMap);
		}
		return retMap;
	}

	/**
	 * 用户查看金币成长情况,并包含转向金币商城地址
	 *
	 * @return {@link Value}
	 */
	@RequestMapping(value = "2/getUserHonor", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserHonor2() {
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		retMap.put("honor", getHonor());
		retMap.put("redirectToMallUrl", Env.getString("yoomath.redirect.mall"));
		retMap.put("enable", Env.getDynamicBoolean("mall.enable"));
		// （学生）今日签到可获得值 和明日可获得值 ， 以前是页面写死
		if (Security.getUserType() == UserType.STUDENT) {
			retMap.put("signReward", getNextSignReward());
		}
		return new Value(retMap);
	}

	/**
	 * 获取成长值的历史记录(最近一个月的)
	 * 
	 * @return
	 */
	@RequestMapping(value = "getLog", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getLog(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
		Page<GrowthLog> cp = growthLogService.queryGrowLog(P.index(page, pageSize), Security.getUserId());
		VPage<VGrowthLog> vp = new VPage<VGrowthLog>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(growthLogConvert.to(cp.getItems()));
		return new Value(vp);
	}
}
