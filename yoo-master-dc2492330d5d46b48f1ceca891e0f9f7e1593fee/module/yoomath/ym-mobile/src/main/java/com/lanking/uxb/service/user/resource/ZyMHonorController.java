package com.lanking.uxb.service.user.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.honor.UserHonor;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.domain.yoo.honor.growth.GrowthLog;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.api.GrowthLogService;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.honor.convert.CoinsLogConvert;
import com.lanking.uxb.service.honor.convert.GrowthLogConvert;
import com.lanking.uxb.service.honor.resource.UserHonorController;
import com.lanking.uxb.service.honor.value.VUserHonor;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 用户荣誉相关接口
 * 
 * @since yoomath(mobile) V1.1.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月23日
 */
@RestController
@RequestMapping("zy/m/honor")
public class ZyMHonorController {

	@Autowired
	private UserHonorService userHonorService;
	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private CoinsLogConvert coinsLogConvert;
	@Autowired
	private GrowthLogService growthLogService;
	@Autowired
	private GrowthLogConvert growthLogConvert;
	@Autowired
	private UserHonorController honorController;

	/**
	 * 成长值H5页面接口
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "growthLogs" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value growthLogs() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		if (honor != null) {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
		} else {
			// 设置默认数据
			honor = new UserHonor();
			honor.setCoins(0);
			honor.setGrowth(0);
			honor.setLevel(1);
			honor.setPoint(0);
			honor.setUpgrade(false);
			honor.setUserId(Security.getUserId());
		}

		data.put("honor", honor);
		// 获取成长值记录
		Page<GrowthLog> cp = growthLogService.queryGrowLog(P.index(1, 100), Security.getUserId());
		if (cp.isNotEmpty()) {
			data.put("logs", growthLogConvert.to(cp.getItems()));
		} else {
			data.put("logs", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}

	/**
	 * 金币值H5页面接口
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = { "coinsLogs" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value coinsLogs() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		UserHonor honor = userHonorService.getUserHonor(Security.getUserId());
		VUserHonor vhonor = new VUserHonor();
		if (honor != null) {
			vhonor.setCoins(honor.getCoins());
			vhonor.setGrowth(honor.getGrowth());
			vhonor.setLevel(honor.getLevel());
		} else {
			// 设置默认数据
			honor = new UserHonor();
			honor.setCoins(0);
			honor.setGrowth(0);
			honor.setLevel(1);
			honor.setPoint(0);
			honor.setUpgrade(false);
			honor.setUserId(Security.getUserId());
		}

		data.put("honor", honor);
		// 获取金币值记录
		Page<CoinsLog> cp = coinsLogService.queryCoinsLog(P.index(1, 100), Security.getUserId());
		if (cp.isNotEmpty()) {
			data.put("logs", coinsLogConvert.to(cp.getItems()));
		} else {
			data.put("logs", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}

	/**
	 * 签到
	 * 
	 * @since yoomath(mobile) V1.1.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "checkIn", method = { RequestMethod.POST, RequestMethod.GET })
	public Value checkIn() {
		return honorController.checkIn();
	}

	/**
	 * 显示过升级后调用
	 * 
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "hadUpgrade", method = { RequestMethod.POST, RequestMethod.GET })
	public Value hadUpgrade() {
		userHonorService.uptUserHonor(Security.getUserId(), false);
		return new Value();
	}
}
