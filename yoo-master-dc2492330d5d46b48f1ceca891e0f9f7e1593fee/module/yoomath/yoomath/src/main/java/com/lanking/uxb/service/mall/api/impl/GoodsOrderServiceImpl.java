package com.lanking.uxb.service.mall.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.mall.api.GoodsOrderService;
import com.lanking.uxb.service.mall.cache.GoodsOrderCacheService;

@Service
public class GoodsOrderServiceImpl implements GoodsOrderService {

	@Autowired
	private GoodsOrderCacheService goodsOrderCacheService;

	@Override
	public String generateCode() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
		Date now = new Date();
		return sdf.format(new Date()) + String.format("%08d", goodsOrderCacheService.getOrderCounter(now));
	}
}
