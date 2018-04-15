package com.lanking.uxb.zycon.mall.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoods;
import com.lanking.cloud.domain.yoo.goods.coins.CoinsGoodsGroup;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsGroupService;
import com.lanking.uxb.zycon.mall.api.ZycCoinsGoodsService;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsGoodsConvert;
import com.lanking.uxb.zycon.mall.convert.ZycCoinsGoodsGroupConvert;
import com.lanking.uxb.zycon.mall.value.VZycGoods;

/**
 * 商品组
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping(value = "zyc/goods/coins/group")
public class ZycCoinsGoodsGroupController {

	@Autowired
	private ZycCoinsGoodsGroupService groupService;

	@Autowired
	private ZycCoinsGoodsGroupConvert groupConvert;

	@Autowired
	private ZycCoinsGoodsService zycCoinGoodsService;

	@Autowired
	private ZycCoinsGoodsConvert cgConvert;

	@Autowired
	private ZycCoinsGoodsGroupGoodsService groupGoodsService;

	/**
	 * 获取商品组列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "list", method = { RequestMethod.GET, RequestMethod.POST })
	public Value list() {
		List<CoinsGoodsGroup> list = groupService.list();
		return new Value(groupConvert.to(list));
	}

	/**
	 * 获取上架的商品
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "getGroundingCount", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getGroundingCount(long id, @RequestParam(value = "userType", defaultValue = "111") String useTypeStr) {
		int userType = Integer.parseInt(useTypeStr, 2);
		return new Value(groupService.publishCountInGroup(id, userType));
	}

	/**
	 * 删除商品组，判断当前商品组底下是否有已上架的商品
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "delGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delGroup(long id) {
		if (groupService.publishCountInGroup(id, 7) > 0) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.GROUP_GOODS_HAS_PUBLISH));
		}
		return new Value();
	}

	/**
	 * 确认删除商品组
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "confirmDelGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value confirmDelGroup(long id) {
		groupService.delGroup(id);
		return new Value();
	}

	/**
	 * 增加商品组
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "addGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addGroup(String name) {
		groupService.addGroup(name);
		return new Value();
	}

	/**
	 * 上移/下移
	 * 
	 * @param upMoveId
	 * @param downMoveId
	 * @return
	 */
	@RequestMapping(value = "move", method = { RequestMethod.GET, RequestMethod.POST })
	public Value move(long upMoveId, long downMoveId) {
		groupService.move(upMoveId, downMoveId);
		return new Value();
	}

	/**
	 * 查询商品列表
	 * 
	 * @param page
	 * @param size
	 * @param groupId
	 *            当前查询的商品组
	 * @param useTypeStr
	 *            类型，默认全部(二进制)
	 * @return
	 */
	@RequestMapping(value = "queryByGroup", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryByGroup(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, @RequestParam(value = "groupId") long groupId,
			@RequestParam(value = "userType", defaultValue = "111") String useTypeStr) {
		// 处理数据
		groupGoodsService.reviewSequence(groupId);
		int userType = Integer.parseInt(useTypeStr, 2);
		Page<CoinsGoods> cp = zycCoinGoodsService.queryCoinsGoods2(groupId, userType, P.index(page, size));
		VPage<VZycGoods> vp = new VPage<VZycGoods>();
		int tPage = (int) (cp.getTotalCount() + size - 1) / size;
		vp.setPageSize(size);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cgConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 上移/下移
	 * 
	 * @param upMoveId
	 *            商品id
	 * @param downMoveId
	 *            商品id
	 * @return
	 */
	@RequestMapping(value = "moveGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public Value moveGoods(long upMoveId, long downMoveId,
			@RequestParam(value = "userTypeStr", defaultValue = "111") String useTypeStr) {
		int userType = Integer.parseInt(useTypeStr, 2);
		groupGoodsService.move(upMoveId, downMoveId, userType);
		return new Value();
	}
}
