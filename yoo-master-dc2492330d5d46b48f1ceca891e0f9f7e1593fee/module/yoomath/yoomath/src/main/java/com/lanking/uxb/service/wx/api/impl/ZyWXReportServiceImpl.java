package com.lanking.uxb.service.wx.api.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lanking.uxb.service.wx.api.ZyWXReportService;
import com.lanking.uxb.service.wx.cache.ZyWXCacheService;

@Service
public class ZyWXReportServiceImpl implements ZyWXReportService {
	@Autowired
	private ZyWXCacheService zyWXCacheService;

	@Override
	public String getReportCodeAndSave() {
		return zyWXCacheService.getReportCodeAndSave();
	}

	@Override
	public Boolean checkReportCode(String code) {
		return zyWXCacheService.checkReportCode(code);
	}
}
