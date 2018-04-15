package com.lanking.uxb.service.honor.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.honor.coins.CoinsLog;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.honor.api.CoinsLogService;
import com.lanking.uxb.service.honor.convert.CoinsLogConvert;
import com.lanking.uxb.service.honor.value.VCoinsLog;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 荣誉-金币相关rest API
 * 
 * @since yoomath V1.8
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年12月9日
 */
@RestController
@RequestMapping("honor/coins")
public class CoinsLogController {

	@Autowired
	private CoinsLogService coinsLogService;
	@Autowired
	private CoinsLogConvert coinsLogConvert;

	/**
	 * 获取金币值的历史记录(最近一年的)
	 * 
	 * @return
	 */
	@RequestMapping(value = "getLog", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getLog(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int pageSize) {
		Page<CoinsLog> cp = coinsLogService.queryCoinsLog(P.index(page, pageSize), Security.getUserId());
		VPage<VCoinsLog> vp = new VPage<VCoinsLog>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(coinsLogConvert.to(cp.getItems()));
		return new Value(vp);
	}
}
