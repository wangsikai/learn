package com.lanking.uxb.zycon.operation.resource;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.zycon.operation.api.CustomerTalkService;
import com.lanking.uxb.zycon.operation.api.TalkQuery;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;

/**
 * 后台问题反馈记录
 * 
 * @since yoomath V1.7
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/customer")
public class CustomerServiceController {

	@Autowired
	private CustomerTalkService customerTalkService;
	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private SchoolService schoolService;

	/**
	 * 查看最新会话
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "querySession")
	public Value querySession(TalkQuery tq) {
		Page<Map> cp = customerTalkService.querySession(tq);
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + tq.getPageSize() - 1) / tq.getPageSize();
		vp.setPageSize(tq.getPageSize());
		vp.setCurrentPage(tq.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		List<Long> userIds = new ArrayList<Long>();
		List<Long> schoolIds = new ArrayList<Long>();
		if (cp.getItems().size() > 0) {
			for (Map map : cp.getItems()) {
				Long userId = ((BigInteger) map.get("user_id")).longValue();
				userIds.add(userId);
				if (map.get("school_id") != null) {
					Long schoolId = ((BigInteger) map.get("school_id")).longValue();
					schoolIds.add(schoolId);
				}
			}
			Map<Long, Account> accountMap = accountService.mgetByUserId(userIds);
			Map<Long, School> schoolMap = schoolService.mget(schoolIds);
			for (Map map : cp.getItems()) {
				Long userId = ((BigInteger) map.get("user_id")).longValue();
				map.put("user_id", userId);
				map.put("accountname", accountMap.get(userId).getName());
				map.put("email", accountMap.get(userId).getEmail());
				map.put("mobile", accountMap.get(userId).getMobile());
				if (map.get("school_id") != null) {
					Long schoolId = ((BigInteger) map.get("school_id")).longValue();
					if (schoolMap.get(schoolId) != null) {
						map.put("schoolname", schoolMap.get(schoolId).getName());
					}
				}
			}
		}
		vp.setItems(cp.getItems());
		return new Value(vp);
	}

	/**
	 * 查询用户的历史记录个数
	 * 
	 * @param tq
	 * @return
	 */
	@RequestMapping(value = "getLogCount")
	public Value getLogCount(TalkQuery tq) {
		Long count = customerTalkService.getLogCount(tq);
		return new Value(count);
	}

	/**
	 * 查询用户信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "queryUserInfo")
	public Value queryUserInfo(Long userId) {
		return new Value(customerTalkService.queryUserInfo(userId));
	}

	/**
	 * 通过用户更新对话
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateSessionByUserId")
	public Value updateSessionByUserId(Long userId) {
		customerTalkService.updateSessionByUserId(userId);
		return new Value();
	}

	/**
	 * 查询历史
	 * 
	 * @param tq
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "queryLog")
	public Value queryLog(TalkQuery tq) {
		Page<Map> cp = customerTalkService.queryLog(tq);
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + tq.getPageSize() - 1) / tq.getPageSize();
		vp.setPageSize(tq.getPageSize());
		vp.setCurrentPage(tq.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		for (Map map : cp.getItems()) {
			map.put("user_id", ((BigInteger) map.get("user_id")).longValue());
			if (map.get("img_id") != null) {
				map.put("imgUrl", FileUtil.getUrl(Long.parseLong(map.get("img_id").toString())));
			}
		}
		vp.setItems(cp.getItems());
		return new Value(vp);
	}
}
