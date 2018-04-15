package com.lanking.uxb.service.mall.api.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsFavorite;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;
import com.lanking.uxb.service.mall.api.ResourcesGoodsFavoriteService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;

@Transactional(readOnly = true)
@Service
public class ResourcesGoodsFavoriteServiceImpl implements ResourcesGoodsFavoriteService {

	@Autowired
	@Qualifier("ResourcesGoodsFavoriteRepo")
	private Repo<ResourcesGoodsFavorite, Long> repo;
	@Autowired
	private ResourcesGoodsService resourcesGoodsService;

	@Override
	public ResourcesGoodsFavorite get(Long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ResourcesGoodsFavorite> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Page<ResourcesGoodsFavorite> queryExamPaperFavorite(ExamQueryForm form, Pageable pageable) {
		Params params = Params.param("createId", form.getCreateId());
		// 关键字
		if (StringUtils.isNotBlank(form.getKey())) {
			params.put("key", "%" + form.getKey() + "%");
		}
		// 资源类型
		if (null != form.getGoodsType()) {
			params.put("goodsType", form.getGoodsType());
		}
		// 是否推荐
		if (form.getIsRecommend() != null && form.getIsRecommend()) {
			params.put("isRecommend", form.getIsRecommend());
		}
		// 试卷大类.
		if (CollectionUtils.isNotEmpty(form.getCategoryCodes())) {
			params.put("resourceCategoryCode", form.getCategoryCodes());
		}

		// 地区
		if (null != form.getDistrictCode()) {
			if (form.getDistrictCode().longValue() == 0) {
				params.put("districtNull", 0);
			} else {
				params.put("districtNull", 1);
				params.put("districtCode", form.getDistrictCode().longValue());
			}
		}
		// 上下架
		if (null != form.getStatus()) {
			params.put("status", form.getStatus().getValue());
		}

		if (null != form.getYear()) {
			// 判断是不是三年以前
			Calendar cal = Calendar.getInstance();
			int nowYear = cal.get(Calendar.YEAR);
			if ((nowYear - form.getYear()) >= 3) {
				params.put("otherYear", 1);
				params.put("year", form.getYear());
			} else {
				params.put("otherYear", 2);
				params.put("year", form.getYear());
			}
		}
		if (form.getOrderBy().equals("difficulty")) {
			if (form.getOrder()) {
				params.put("orderBy", 1);
			} else {
				params.put("orderBy", 2);
			}
		} else {
			if (form.getOrder()) {
				params.put("orderBy", 3);
			} else {
				params.put("orderBy", 4);
			}
		}
		return repo.find("$queryFavorite", params).fetch(pageable);

	}

	@Override
	public List<Long> getFavoriteIdByUserId(long createId, Long goodsId) {
		Params params = Params.param("createId", createId);
		if (goodsId != null) {
			params.put("goodsId", goodsId);
		}
		return repo.find("$getFavoriteIdByUserId", params).list(Long.class);
	}

	@Override
	public List<Long> getFavoriteIdByResourcesId(long createId, Long resourcesId) {
		Params params = Params.param("createId", createId);
		params.put("resourcesId", resourcesId);
		return repo.find("$getFavoriteIdByResourcesId", params).list(Long.class);
	}

	@Override
	public List<ResourcesGoodsFavorite> mgetFavoriteIdByResourcesId(long createId, Collection<Long> resourcesIds) {
		Params params = Params.param("createId", createId);
		params.put("resourcesIds", resourcesIds);
		return repo.find("$mgetFavoriteIdByResourcesId", params).list(ResourcesGoodsFavorite.class);
	}

	@Transactional
	@Override
	public void addFavorite(Long createId, Long goodsId) {
		// 判断改商品是否已经加入收藏
		List<Long> ids = getFavoriteIdByUserId(createId, goodsId);
		if (ids.size() == 0) {
			Date dt = new Date();
			ResourcesGoodsFavorite rgfe = new ResourcesGoodsFavorite();
			rgfe.setCreateAt(dt);
			rgfe.setCreateId(createId);
			rgfe.setResourcesGoodsId(goodsId);
			ResourcesGoods rgoods = resourcesGoodsService.get(goodsId);
			rgfe.setResourcesId(rgoods.getResourcesId());
			rgfe.setType(rgoods.getType());
			repo.save(rgfe);
		}
	}

	@Transactional
	@Override
	public int removeFavorite(Long createId, Long id) {
		return repo.execute("$deleteFavoriteById", Params.param("createId", createId).put("id", id));
	}

}
