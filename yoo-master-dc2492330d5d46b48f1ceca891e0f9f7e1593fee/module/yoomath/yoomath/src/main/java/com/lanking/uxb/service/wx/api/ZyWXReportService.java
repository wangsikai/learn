package com.lanking.uxb.service.wx.api;

/**
 * 作业报告服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月5日
 */
public interface ZyWXReportService {

	/**
	 * 获得报告兑换码.
	 * 
	 * @param accountId
	 *            账户ID
	 * @return
	 */
	String getReportCodeAndSave();

	/**
	 * 校验兑换码.
	 * 
	 * @param code
	 *            兑换码.
	 * @return
	 */
	Boolean checkReportCode(String code);
}
