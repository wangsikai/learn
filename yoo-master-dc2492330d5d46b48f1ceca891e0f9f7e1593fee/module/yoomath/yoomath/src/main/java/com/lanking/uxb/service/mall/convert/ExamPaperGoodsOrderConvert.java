package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.mall.api.GoodsSnapshotService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsSnapshotService;
import com.lanking.uxb.service.mall.value.VExamPaperGoodsOrder;
import com.lanking.uxb.service.mall.value.VResourcesGoods;

/**
 * 试卷商品Convert
 *
 * @author zemin.song
 */
@Component
public class ExamPaperGoodsOrderConvert extends Converter<VExamPaperGoodsOrder, ResourcesGoodsOrder, Long> {

	@Autowired
	private ResourcesGoodsService resourcesGoodsService;
	@Autowired
	private GoodsSnapshotService goodsSnapshotService;
	@Autowired
	private ResourcesGoodsSnapshotService resourcesGoodsSnapshotService;

	@Override
	protected Long getId(ResourcesGoodsOrder s) {
		return s.getId();
	}

	@Override
	protected VExamPaperGoodsOrder convert(ResourcesGoodsOrder s) {
		VExamPaperGoodsOrder vo = new VExamPaperGoodsOrder();
		VResourcesGoods vrg = new VResourcesGoods();
		vo.setId(s.getId());
		vo.setGoodsId(s.getGoodsId());
		vo.setPayMod(s.getPayMod());
		vo.setOrderAt(s.getOrderAt());
		vo.setTotalPrice(s.getTotalPrice());
		vo.setResourcesGoodsSnapshot(vrg);
		return vo;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VExamPaperGoodsOrder, ResourcesGoodsOrder, Long, GoodsSnapshot>() {

			@Override
			public boolean accept(ResourcesGoodsOrder s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ResourcesGoodsOrder s, VExamPaperGoodsOrder d) {
				return s.getGoodsSnapshotId();
			}

			@Override
			public void setValue(ResourcesGoodsOrder s, VExamPaperGoodsOrder d, GoodsSnapshot value) {
				d.getResourcesGoodsSnapshot().setName(value.getName());
				d.setGoodsSnapshotId(value.getId());
			}

			@Override
			public GoodsSnapshot getValue(Long key) {
				return goodsSnapshotService.get(key);
			}

			@Override
			public Map<Long, GoodsSnapshot> mgetValue(Collection<Long> keys) {
				return goodsSnapshotService.mget(keys);
			}

		});
		assemblers.add(new ConverterAssembler<VExamPaperGoodsOrder, ResourcesGoodsOrder, Long, ResourcesGoods>() {

			@Override
			public boolean accept(ResourcesGoodsOrder s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ResourcesGoodsOrder s, VExamPaperGoodsOrder d) {
				return s.getGoodsId();
			}

			@Override
			public void setValue(ResourcesGoodsOrder s, VExamPaperGoodsOrder d, ResourcesGoods value) {
				VResourcesGoods vrg = new VResourcesGoods();
				vrg.setId(value.getId());
				vrg.setCategory(value.getCategory());
				vrg.setResourcesId(value.getResourcesId());
				d.setResourcesGoods(vrg);
			}

			@Override
			public ResourcesGoods getValue(Long key) {
				return resourcesGoodsService.get(key);
			}

			@Override
			public Map<Long, ResourcesGoods> mgetValue(Collection<Long> keys) {
				return resourcesGoodsService.mget(keys);
			}

		});

		// 订单对应的资源商品快照
		assemblers
				.add(new ConverterAssembler<VExamPaperGoodsOrder, ResourcesGoodsOrder, Long, ResourcesGoodsSnapshot>() {

					@Override
					public boolean accept(ResourcesGoodsOrder s) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(ResourcesGoodsOrder s, VExamPaperGoodsOrder d) {
						return s.getResourcesGoodsSnapshotId();
					}

					@Override
					public void setValue(ResourcesGoodsOrder s, VExamPaperGoodsOrder d, ResourcesGoodsSnapshot value) {
						d.getResourcesGoodsSnapshot().setId(value.getId());
					}

					@Override
					public ResourcesGoodsSnapshot getValue(Long key) {
						return resourcesGoodsSnapshotService.get(key);
					}

					@Override
					public Map<Long, ResourcesGoodsSnapshot> mgetValue(Collection<Long> keys) {
						return resourcesGoodsSnapshotService.mget(keys);
					}

				});
	}
}
