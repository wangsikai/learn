package com.lanking.uxb.zycon.mall.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.GoodsBaseInfo;
import com.lanking.cloud.domain.yoo.goods.GoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsBaseInfo;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsSnapshot;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.mall.api.ZycResourcesGoodsService;
import com.lanking.uxb.zycon.mall.form.ResourcesGoodsForm;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZycResourcesGoodsServiceImpl implements ZycResourcesGoodsService {

	@Autowired
	@Qualifier("ResourcesGoodsRepo")
	private Repo<ResourcesGoods, Long> repo;

	@Autowired
	@Qualifier("GoodsRepo")
	private Repo<Goods, Long> goodsRepo;

	@Autowired
	@Qualifier("GoodsSnapshotRepo")
	private Repo<GoodsSnapshot, Long> goodsSnapshotRepo;

	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> examRepo;

	@Autowired
	@Qualifier("ResourcesGoodsSnapshotRepo")
	private Repo<ResourcesGoodsSnapshot, Long> resourcesGoodsSnapshotRepo;

	@Override
	public ResourcesGoods getGoodsByResourcesId(Long resourcesId) {
		return repo.find("$zycGetGoodsByResourcesId", Params.param("resourcesId", resourcesId)).get();
	}

	@Override
	public List<ResourcesGoods> mgetGoods(Collection<Long> resourcesIds) {
		return repo.find("$zycMgetGoods", Params.param("resourcesIds", resourcesIds)).list();
	}

	@Override
	public ResourcesGoods get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ResourcesGoods> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Transactional
	@Override
	public ResourcesGoods chengeStatus(Long id, Long userId) {
		// 对userId进行权限控制
		ResourcesGoods rg = repo.get(id);
		rg.setUpdateAt(new Date());
		if (rg.getStatus() == ResourcesGoodsStatus.PUBLISH) {
			rg.setStatus(ResourcesGoodsStatus.UN_PUBLISH);
		} else {
			rg.setStatus(ResourcesGoodsStatus.PUBLISH);
		}
		return repo.save(rg);
	}

	private Goods convertBaseGoodsInfo(ResourcesGoodsForm goods) {
		Goods insertGoods = new Goods();
		if (goods.getId() != null) {// 修改
			insertGoods = goodsRepo.get(goods.getId());
			Assert.notNull(insertGoods);
			insertGoods.setUpdateAt(new Date());
			insertGoods.setUpdateId(Security.getUserId());
		} else { // 新增
			insertGoods.setCreateAt(new Date());
			insertGoods.setCreateId(Security.getUserId());
		}
		insertGoods.setName(goods.getName());
		insertGoods.setPrice(goods.getPrice());
		insertGoods.setPriceRMB(goods.getPriceRMB());
		insertGoods.setSalesTime(goods.getSalesTime());
		insertGoods.setSoldOutTime(goods.getSoldOutTime());
		return insertGoods;
	}

	@Transactional
	@Override
	public ResourcesGoods save(ResourcesGoodsForm form) {
		form.setType(ResourcesGoodsType.EXAM_PAPER);
		if (form.getStatus() == null) {
			form.setStatus(ResourcesGoodsStatus.DRAFT);
		}
		ResourcesGoods resourcesGoods = null;
		if (form.getResourcesId() != null) {
			resourcesGoods = this.getGoodsByResourcesId(form.getResourcesId());
		}
		if (form.getId() == null && resourcesGoods == null) {
			// 创建
			// 资源商品
			resourcesGoods = new ResourcesGoods();
			resourcesGoods.setCategory(form.getCategory());
			resourcesGoods.setResourcesId(form.getResourcesId());
			resourcesGoods.setRecommendReason(form.getRecommendReason());
			resourcesGoods.setStatus(form.getStatus());
			resourcesGoods.setType(form.getType());
		} else { // 编辑
			if (resourcesGoods == null) {
				resourcesGoods = repo.get(form.getId());
			}
			// 下架
			if (form.getStatus() == ResourcesGoodsStatus.UN_PUBLISH
					&& resourcesGoods.getStatus() == ResourcesGoodsStatus.PUBLISH) {
				resourcesGoods.setStatus(ResourcesGoodsStatus.UN_PUBLISH);
				resourcesGoods.setUpdateAt(new Date());
				repo.save(resourcesGoods);
				return null;
			}
			resourcesGoods.setCategory(form.getCategory());
			resourcesGoods.setResourcesId(form.getResourcesId());
			resourcesGoods.setRecommendReason(form.getRecommendReason());
			resourcesGoods.setStatus(form.getStatus());
			resourcesGoods.setType(form.getType());
		}
		resourcesGoods.setUpdateAt(new Date());
		Goods goods = this.convertBaseGoodsInfo(form);
		// 如果商品名字为空，这取资源id name
		if (goods.getName() == null) {
			ExamPaper paper = examRepo.get(resourcesGoods.getResourcesId());
			goods.setName(paper.getName());
		}
		// 保存基本信息快照（快照ID）
		GoodsSnapshot gss = new GoodsSnapshot();
		BeanUtils.copyProperties(goods, gss, GoodsBaseInfo.class);
		gss.setGoodsId(goods.getId() == null ? 0 : goods.getId());
		gss.setId(null);// 置空
		goodsSnapshotRepo.save(gss); // 快照存储 创建goodsId为0
		// 保存基本信息
		goods.setGoodsSnapshotId(gss.getId());
		goodsRepo.save(goods); // 商品存储

		// 保存资源信息快照
		ResourcesGoodsSnapshot rgsh = new ResourcesGoodsSnapshot();
		BeanUtils.copyProperties(resourcesGoods, rgsh, ResourcesGoodsBaseInfo.class);
		rgsh.setResourcesGoodsId(goods.getId());
		rgsh.setId(null);
		resourcesGoodsSnapshotRepo.save(rgsh); // 资源快照存储

		// 保存 resourcesGoods goods 一致
		resourcesGoods.setId(goods.getId());
		resourcesGoods.setResourcesGoodsSnapshotId(rgsh.getId());
		repo.save(resourcesGoods);
		// 创建时 快照 goodsId 为 0，现补填
		if (gss.getGoodsId() == 0) {
			gss.setGoodsId(goods.getId());
			goodsSnapshotRepo.save(gss);
		}
		return resourcesGoods;
	}
}
