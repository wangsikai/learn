package com.lanking.uxb.zycon.mall.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.mall.api.ZycExcellentPaperGoodsManage;
import com.lanking.uxb.zycon.mall.form.ExamOrdersQueryForm;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class ZycExcellentPaperGoodsManageImpl implements ZycExcellentPaperGoodsManage {

	@Autowired
	@Qualifier("ResourcesGoodsRepo")
	private Repo<ResourcesGoods, Long> repo;

	@Override
	public Page<ResourcesGoods> queryResconExamGoods(ExamOrdersQueryForm form, Pageable pageable) {

		Params params = Params.param("goodsType", ResourcesGoodsType.EXAM_PAPER);
		// 关键字
		if (StringUtils.isNotBlank(form.getKey())) {
			params.put("key", "%" + form.getKey() + "%");
		}
		// 资源类型
		if (StringUtils.isNotBlank(form.getExamCode())) {
			params.put("examCode", form.getExamCode());
		}
		if (null != form.getCategory()) {
			params.put("category", form.getCategory());
		}
		if (null != form.getPhaseCode()) {
			params.put("phaseCode", form.getPhaseCode());
		}
		return repo.find("$zycGetResourcesGoods", params).fetch(pageable);
	}

}
