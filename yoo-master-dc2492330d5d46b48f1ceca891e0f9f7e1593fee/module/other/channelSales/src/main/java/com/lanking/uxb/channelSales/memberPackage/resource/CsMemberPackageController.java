package com.lanking.uxb.channelSales.memberPackage.resource;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.member.MemberPackageGroupType;
import com.lanking.cloud.ex.core.DBException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.event.ClusterEvent;
import com.lanking.cloud.sdk.event.ClusterEventSender;
import com.lanking.cloud.sdk.event.LocalCacheEvent;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.convert.CsUserChannelConvert;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageService;
import com.lanking.uxb.channelSales.memberPackage.convert.CsMemberPackageConvert;
import com.lanking.uxb.channelSales.memberPackage.convert.CsMemberPackageGroupConvert;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageGroupForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageQueryForm;
import com.lanking.uxb.channelSales.memberPackage.form.ParameterForm;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackage;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageGroup;
import com.lanking.uxb.service.code.api.BaseDataAction;
import com.lanking.uxb.service.code.api.BaseDataType;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.mall.api.MemberPackageOrderRefundService;

/**
 * 会员套餐管理
 * 
 * @author zemin.song
 * @version 2016年9月26日
 */
@RestController
@RequestMapping("channelSales/mp")
public class CsMemberPackageController {
	@Autowired
	private CsMemberPackageService csMemberPackageService;
	@Autowired
	private CsMemberPackageConvert csMemberPackageConvert;
	@Autowired
	private CsMemberPackageGroupConvert csMemberPackageGroupConvert;
	@Autowired
	private CsUserChannelService csUserChannelService;
	@Autowired
	private CsUserChannelConvert csUserChannelConvert;
	@Autowired
	@Qualifier(value = "clusterDataSender")
	private ClusterEventSender sender;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private MemberPackageOrderRefundService memberPackageOrderRefundService;

	/**
	 * 套餐查询
	 * 
	 * @param queryForm
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(MemberPackageQueryForm query) {
		int offset = (query.getPage() - 1) * query.getPageSize();
		int size = query.getPageSize();
		Page<VMemberPackage> items = csMemberPackageConvert.to(csMemberPackageService.query(query,
				P.offset(offset, size)));
		VPage<VMemberPackage> vpage = new VPage<VMemberPackage>();
		// 当页码为1的时候查询统计数据
		vpage.setItems(items.getItems());
		vpage.setPageSize(query.getPageSize());
		vpage.setTotal(items.getTotalCount());
		vpage.setTotalPage(items.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	/**
	 * 套餐添加
	 * 
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(MemberPackageForm form) {
		csMemberPackageService.save(form);
		return new Value();

	}

	/**
	 * 套餐删除
	 * 
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(Long id) {
		int ret = csMemberPackageService.delete(id);
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new DBException());
		}
	}

	/**
	 * 重置排序
	 * 
	 */
	@RequestMapping(value = "sort", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sort(@RequestParam(value = "ids") List<Long> ids) {
		csMemberPackageService.sort(ids);
		return new Value();
	}

	/**
	 * 套餐组查询
	 */
	@RequestMapping(value = "queryGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryGroup(MemberPackageQueryForm query) {
		List<VMemberPackageGroup> groups = csMemberPackageGroupConvert.to(csMemberPackageService.queryGroup(query));
		query.setOrderByMonth(true);
		List<VMemberPackage> mp = csMemberPackageConvert.to(csMemberPackageService.query(query, P.offset(0, 500)))
				.getItems();
		for (VMemberPackageGroup vmg : groups) {
			for (VMemberPackage vm : mp) {
				if (vmg.getId().equals(vm.getMemberPackageGroupId())) {
					vmg.getvMemberPackages().add(vm);
				}
			}
		}
		return new Value(groups);
	}

	/**
	 * 套餐组添加
	 */
	@RequestMapping(value = "createGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value createGroup(MemberPackageGroupForm form) {
		if (form.getType() != MemberPackageGroupType.REGISTER_USER
				&& (form.getProfits1() == null || form.getProfits2() == null)) {
			return new Value(new MissingArgumentException());
		}
		csMemberPackageService.createGroup(form);
		return new Value();
	}

	/**
	 * 套餐组删除
	 * 
	 */
	@RequestMapping(value = "deleteGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value createGroup(Long id) {
		csMemberPackageService.deleteGroup(id);
		return new Value();
	}

	/**
	 * 渠道树
	 */
	@RequestMapping(value = "getChannelSchoolTree", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getChannelSchoolTree() {
		List<VUserChannel> ucList = csUserChannelConvert.to(csUserChannelService.query(P.index(1, 500)).getItems());
		return new Value(ucList);
	}

	/**
	 * 更新渠道关联
	 */
	@RequestMapping(value = "updateGroupChannel", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateChannel(MemberPackageGroupForm form) {
		csMemberPackageService.updateGroup(form);
		return new Value();
	}

	/**
	 * 修改阀值
	 */
	@RequestMapping(value = "updateParam", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateParameter(ParameterForm form) {
		if (form == null || StringUtils.isBlank(form.getValue())) {
			return new Value(new MissingArgumentException());
		}

		// 查询原值
		Parameter parameter = parameterService.get(Product.YOOMATH, "memberPackage.settlement");

		// 新值处理
		int ret = csMemberPackageService.updateParam(form);
		if (ret > 0) {
			ClusterEvent<String> e = new LocalCacheEvent<String>(BaseDataAction.RELOAD.name(),
					BaseDataType.PARAMETER.name());
			sender.send(e);

			// 新值变小后的处理，已有的渠道订单需要做判断是否需要返还利润
			if (parameter != null && parameter.getValue() != null) {
				int oldValue = Integer.parseInt(parameter.getValue());
				int newValue = Integer.parseInt(form.getValue());
				if (oldValue > newValue) {
					memberPackageOrderRefundService.refund(newValue);
				}
			}
			return new Value();
		} else {
			return new Value(new DBException());
		}
	}
}
