package com.lanking.uxb.channelSales.order.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderType;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.order.api.CsOrderService;
import com.lanking.uxb.channelSales.order.form.OrderQueryForm;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 渠道商管理
 * 
 * @author zemin.song
 * @version 2016年10月9日
 */
@RestController
@RequestMapping("channelSales/order")
public class CsOrderController {
	@Autowired
	private CsOrderService csOrderService;
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private CsUserChannelService userChannelService;

	// 会员套餐查询
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "umQuery")
	public Value memberPackageOrderQuery(OrderQueryForm query) {

		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = csOrderService.memberPackageOrderQuery(query, pageable);

		List<Long> orderIds = new ArrayList<Long>();
		for (Map map : page.getItems()) {
			// 后台导入且来源渠道
			if (Long.parseLong(map.get("type").toString()) != MemberPackageOrderType.USER.getValue()) {
				orderIds.add(Long.parseLong(map.get("id").toString()));
			}
		}
		List<Map> accountList = null;
		if (orderIds.size() > 0) {
			accountList = csOrderService.searchToOneOrderUserByOrderId(orderIds);
		}
		for (Map map : page.getItems()) {
			if (accountList != null) {
				for (Map account : accountList) {
					if (map.get("id").equals(account.get("orderid"))) {
						// 当为渠道用户的时候查询结果账户名为空,此时取一个补填
						if (map.get("accountname") == null && account.get("accountname") != null) {
							map.put("accountname", account.get("accountname"));
						}
						// 补填用户类型( 修改为均从第一条数据里面取用户身份类型)
						if (account.get("user_type") != null) {
							map.put("user_type", account.get("user_type"));
						}
						if (map.get("member_package_id") != null && account.get("enddate") != null
								&& Long.parseLong(map.get("member_package_id").toString()) == 0) {
							map.put("enddate", account.get("enddate"));
						}
						if (account.get("countpaper") != null) {
							map.put("countpaper", account.get("countpaper"));
						}
						break;
					}
				}
			}
			// 将ID转化成字符串防止传入前台改变
			String id = map.get("id").toString();
			map.put("id", id);
		}
		VPage vpage = new VPage();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	// 会员套餐查询
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "umQueryByChannel")
	public Value memberPackageOrderQueryByChannel(OrderQueryForm query) {
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		if (uc == null) {
			return new Value();
		}
		query.setChannelCode(uc.getCode());
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = csOrderService.memberPackageOrderQuery(query, pageable);

		List<Long> orderIds = new ArrayList<Long>();
		for (Map map : page.getItems()) {
			// 后台导入且来源渠道
			if (Long.parseLong(map.get("type").toString()) != MemberPackageOrderType.USER.getValue()) {
				orderIds.add(Long.parseLong(map.get("id").toString()));
			}
		}
		List<Map> accountList = null;
		if (orderIds.size() > 0) {
			accountList = csOrderService.searchToOneOrderUserByOrderId(orderIds);
		}
		for (Map map : page.getItems()) {
			if (accountList != null) {
				for (Map account : accountList) {
					if (map.get("id").equals(account.get("orderid"))) {
						// 当为渠道用户的时候查询结果账户名为空,此时取一个补填
						if (map.get("accountname") == null && account.get("accountname") != null) {
							map.put("accountname", account.get("accountname"));
						}
						// 补填用户类型( 修改为均从第一条数据里面取用户身份类型)
						if (account.get("user_type") != null) {
							map.put("user_type", account.get("user_type"));
						}
						if (map.get("member_package_id") != null && account.get("enddate") != null
								&& Long.parseLong(map.get("member_package_id").toString()) == 0) {
							map.put("enddate", account.get("enddate"));
						}
						if (account.get("countpaper") != null) {
							map.put("countpaper", account.get("countpaper"));
						}
						break;
					}
				}
			}
			// 将ID转化成字符串防止传入前台改变
			String id = map.get("id").toString();
			map.put("id", id);
		}
		VPage vpage = new VPage();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	// 错题代印
	@RequestMapping(value = "fqpoQuery")
	public Value fallibleQuestionPrintOrderQuery(OrderQueryForm query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = csOrderService.fallibleQuestionPrintOrderQuery(query, pageable);
		VPage<Map> vpage = new VPage<Map>();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	// 试卷订单
	@RequestMapping(value = "rgoQuery")
	public Value resourcesGoodsOrderQuery(OrderQueryForm query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = csOrderService.resourcesGoodsOrderQuery(query, pageable);
		VPage<Map> vpage = new VPage<Map>();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	// 查询用户
	@RequestMapping(value = "searchUserByOrder")
	public Value searchUserByOrder(Long orderId) {
		List<String> account = csOrderService.searchUserByOrder(orderId);
		return new Value(account);
	}

	/**
	 * 查询会员卡激活记录
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "queryMemberCardList")
	public Value queryMemberCardList(OrderQueryForm query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = csOrderService.queryMemberCardList(query, pageable);
		VPage<Map> vpage = new VPage<Map>();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}
}
