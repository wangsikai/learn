package com.lanking.uxb.zycon.mall.api.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.mall.api.ZycResourcesGoodsOrderService;
import com.lanking.uxb.zycon.mall.form.ExamOrdersQueryForm;
import com.lanking.uxb.zycon.mall.value.VZycTotalOrdersData;

@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZycResourcesGoodsOrderServiceImpl implements ZycResourcesGoodsOrderService {

	@Autowired
	@Qualifier("ResourcesGoodsOrderRepo")
	private Repo<ResourcesGoodsOrder, Long> repo;

	@Override
	public VZycTotalOrdersData totalOrders(ExamOrdersQueryForm form) {
		Params params = Params.param("type", form.getType().getValue());
		params.put("status", form.getStatus().getValue());
		if (StringUtils.isNotBlank(form.getKey())) {
			params.put("examName", "%" + form.getKey() + "%");
		}
		if (StringUtils.isNotBlank(form.getExamCode())) {
			params.put("examCode", form.getExamCode());
		}
		if (StringUtils.isNotBlank(form.getStartDate())) {
			params.put("starAt", form.getStartDate());
		}
		if (StringUtils.isNotBlank(form.getEndDate())) {
			params.put("endAt", form.getEndDate());
		}
		if (null != form.getPhaseCode()) {
			params.put("phaseCode", form.getPhaseCode());
		}
		if (null != form.getCategory()) {
			params.put("resourceCategoryCode", form.getCategory());
		}
		List<Map> dataList = repo.find("$zycTotalResourcesGoodsOrders", params).list(Map.class);
		Map<String, Object> retMap = new HashMap<String, Object>();
		long count = 0L;
		BigDecimal price = new BigDecimal(0L);
		BigDecimal priceRMB = new BigDecimal(0L);
		for (Map map : dataList) {
			if (map.get("pay_mod") != null) {
				count += Long.parseLong(map.get("count").toString());
				if (map.get("pay_mod").toString().equals("1")) {
					priceRMB = new BigDecimal(map.get("totalprice").toString());
				}
				if (map.get("pay_mod").toString().equals("2")) {
					price = new BigDecimal(map.get("totalprice").toString());
				}

			}

		}
		VZycTotalOrdersData vTotalData = new VZycTotalOrdersData();
		vTotalData.setCount(count);
		vTotalData.setTotalPrice(price);
		vTotalData.setTotalPriceRMB(priceRMB);
		return vTotalData;
	}

	@Override
	public Page<Map> mGetResourcesGoodsOrders(ExamOrdersQueryForm form, Pageable pageable) {
		Params params = Params.param("type", form.getType().getValue());
		params.put("status", form.getStatus().getValue());
		if (StringUtils.isNotBlank(form.getKey())) {
			params.put("examName", "%" + form.getKey() + "%");
		}
		if (StringUtils.isNotBlank(form.getExamCode())) {
			params.put("examCode", form.getExamCode());
		}
		if (StringUtils.isNotBlank(form.getStartDate())) {
			params.put("starAt", form.getStartDate());
		}
		if (StringUtils.isNotBlank(form.getEndDate())) {
			params.put("endAt", form.getEndDate());
		}
		if (null != form.getPhaseCode()) {
			params.put("phaseCode", form.getPhaseCode());
		}
		if (null != form.getCategory()) {
			params.put("resourceCategoryCode", form.getCategory());
		}
		return repo.find("$zycmGetGoodsOrdersCount", params).fetch(pageable, Map.class);
	}

}
