package com.lanking.uxb.service.mall.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsFavorite;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.examPaper.convert.ExamPaperConvert;
import com.lanking.uxb.service.mall.api.GoodsService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsFavoriteService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;
import com.lanking.uxb.service.mall.value.VExamPaperGoods;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 试卷商品Convert
 *
 * @author zemin.song
 */
@Component
public class ExamPaperGoodsConvert extends Converter<VExamPaperGoods, ExamPaper, Long> {

	@Autowired
	private ResourcesGoodsService resourcesGoodsService;

	@Autowired
	private SchoolConvert schoolConvert;

	@Autowired
	private GoodsService goodsService;

	@Autowired
	private ExamPaperConvert examPaperConvert;

	@Autowired
	private ResourcesGoodsFavoriteService resourcesGoodsFavoriteService;

	public VExamPaperGoods to(ExamPaper ep, ExamPaperGoodsConvertOption opn) {
		ep.setInitGoodsInfo(opn.isInitGoodsInfo());
		return super.to(ep);
	}

	public List<VExamPaperGoods> to(List<ExamPaper> eps, ExamPaperGoodsConvertOption opn) {
		for (ExamPaper ep : eps) {
			ep.setInitGoodsInfo(opn.isInitGoodsInfo());
			ep.setInitCollect(opn.isInitCollect());
		}
		return super.to(eps);
	}

	@Override
	protected Long getId(ExamPaper examPaper) {
		return examPaper.getId();
	}

	@Override
	protected VExamPaperGoods convert(ExamPaper examPaper) {
		VExamPaperGoods vo = new VExamPaperGoods();
		vo.setPaper(examPaperConvert.to(examPaper));
		return vo;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 商品是否收藏
		assemblers.add(new ConverterAssembler<VExamPaperGoods, ExamPaper, Long, Boolean>() {

			@Override
			public boolean accept(ExamPaper s) {
				return s.isInitCollect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExamPaperGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VExamPaperGoods d, Boolean value) {
				if (value != null) {
					d.setCollect(value);
				}

			}

			@Override
			public Boolean getValue(Long key) {
				List<Long> favorites = resourcesGoodsFavoriteService.getFavoriteIdByResourcesId(Security.getUserId(),
						key);
				if (favorites.size() > 0) {
					return true;
				} else {
					return false;
				}
			}

			@Override
			public Map<Long, Boolean> mgetValue(Collection<Long> keys) {
				List<ResourcesGoodsFavorite> favorite = resourcesGoodsFavoriteService.mgetFavoriteIdByResourcesId(
						Security.getUserId(), keys);
				Map<Long, Boolean> retMap = new HashMap<Long, Boolean>(keys.size());
				for (Long rids : keys) {
					boolean isCollect = false;
					go: for (ResourcesGoodsFavorite fd : favorite) {
						if (rids.longValue() == fd.getResourcesId().longValue()) {
							isCollect = true;
							break go;
						}
					}
					retMap.put(rids, isCollect);
				}
				return retMap;
			}

		});
		// 详情描述
		assemblers.add(new ConverterAssembler<VExamPaperGoods, ExamPaper, Long, Map<String, Object>>() {

			@Override
			public boolean accept(ExamPaper s) {
				return s.isInitGoodsInfo();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VExamPaperGoods d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VExamPaperGoods d, Map<String, Object> value) {
				if (null != value.get("resourcesGoods")) {
					ResourcesGoods resourcesGoods = (ResourcesGoods) value.get("resourcesGoods");
					d.setReason(resourcesGoods.getRecommendReason());
					d.setCategory(resourcesGoods.getCategory());
				}
				if (null != value.get("goods")) {
					Goods goods = (Goods) value.get("goods");
					d.setGoodsSnapshotId(goods.getGoodsSnapshotId());
					d.setPrice(goods.getPrice());
					d.setPriceRMB(goods.getPriceRMB());
					d.setId(goods.getId());
				}

			}

			@Override
			public Map<String, Object> getValue(Long key) {
				ResourcesGoods resourcesGoods = resourcesGoodsService.getGoodsByResourcesId(key);
				Goods goods = goodsService.get(resourcesGoods.getId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("resourcesGoods", resourcesGoods);
				map.put("goods", goods);
				return map;
			}

			@Override
			public Map<Long, Map<String, Object>> mgetValue(Collection<Long> keys) {
				List<ResourcesGoods> resourcesGoodslist = resourcesGoodsService.mgetGoods(keys);
				Map<Long, Map<String, Object>> retMap = new HashMap<Long, Map<String, Object>>(keys.size());
				Map<String, Object> objMap = null;
				List<Long> goodsIds = Lists.newArrayList();
				for (ResourcesGoods rgs : resourcesGoodslist) {
					goodsIds.add(rgs.getId());
					if (retMap.get(rgs.getResourcesId()) == null) {
						objMap = new HashMap<String, Object>();
						objMap.put("resourcesGoods", rgs);
						objMap.put("goodsId", rgs.getId());
						retMap.put(rgs.getResourcesId(), objMap);
					}
				}
				Map<Long, Goods> goodss = goodsService.mget(goodsIds);
				for (Long key : retMap.keySet()) {
					Long mGoodsId = null;
					if (null != retMap.get(key).get("goodsId")) {
						mGoodsId = Long.parseLong(retMap.get(key).get("goodsId").toString());
					}
					for (Long goodsId : goodss.keySet()) {
						if (goodsId.longValue() == mGoodsId.longValue()) {
							retMap.get(key).put("goods", goodss.get(goodsId));
						}
					}
				}
				return retMap;
			}

		});

	}
}
