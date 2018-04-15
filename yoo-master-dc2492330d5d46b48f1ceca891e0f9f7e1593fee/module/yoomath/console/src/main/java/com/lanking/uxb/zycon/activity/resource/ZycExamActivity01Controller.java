package com.lanking.uxb.zycon.activity.resource;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.activity.api.ZycExamActivity01LotteryUserService;
import com.lanking.uxb.zycon.activity.api.ZycExaminationActivity01Service;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * 期末活动001
 * 
 * @author qiuxue.jiang
 * @version 2017年12月27日
 */
@RestController
@RequestMapping(value = "zyc/examActivity001")
public class ZycExamActivity01Controller {
	
	@Autowired
	private ZycExamActivity01LotteryUserService examActivity01LotteryUserService;
	
	@Autowired
	private ZycExaminationActivity01Service examinationActivity01Service;

	/**
	 * 中奖用户
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryUser(ZycActivityUserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<Map> cp = examActivity01LotteryUserService.queryActivityLotteryUser(form, P.index(page, pageSize));
	
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cp.getItems());
		for (Map map : cp.getItems()) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("id", id);
			Long userId = ((BigInteger) map.get("userid")).longValue();
			map.put("userid", userId);
		}
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		vp.setItems(cp.getItems());
		retMap.put("page", vp);
		return new Value(retMap);
	}
	
	/**
	 * 用户中奖详情
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryGiftDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryGiftDetail(Long code,Long userId) {
		List<ExamActivity001UserQ> listUserQs = examActivity01LotteryUserService. get(code,userId);
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("userQs", listUserQs);
		return new Value(retMap);
	}
	
	/**
	 * 中奖用户总数
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryLotteryUsers", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryLotteryUsers(Long code) {
		Long count = examActivity01LotteryUserService.getLotteryUserCount(code);
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("count", count);
		return new Value(retMap);
	}
	
	/**
	 * 中奖总Q点
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryLotteryQNum", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryLotteryQNum(Long code) {
		Long qNum = examActivity01LotteryUserService.getLotteryTotalQ(code);
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("qNum", qNum);
		return new Value(retMap);
	}
	
	/**
	 * 修改兑换状态
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateLotteryUserStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateLotteryUserStatus(Long id, Integer received) {
		if (id == null || received == null) {
			return new Value(new IllegalArgException());
		}
	
		examActivity01LotteryUserService.updateReceived(id, received);
		return new Value();
	}

}
