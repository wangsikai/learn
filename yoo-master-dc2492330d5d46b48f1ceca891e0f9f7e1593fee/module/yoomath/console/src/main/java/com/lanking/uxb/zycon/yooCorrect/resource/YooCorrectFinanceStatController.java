package com.lanking.uxb.zycon.yooCorrect.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectConsoleConfigDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectWithdrawApplyRequest;

@RestController
@RequestMapping(value = "correct/finaStat")
public class YooCorrectFinanceStatController {

	@Autowired
	private CorrectConsoleConfigDatawayClient correctConsoleConfigDatawayClient;
	
	/**
	 * 批改财务统计列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getFinaStats", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getFinaStats(Integer size, Integer page, Integer year, Integer month) {
		Value value = correctConsoleConfigDatawayClient.getFinaStats(size, page, year, month);
		
		return value;
	}
	
	/**
	 * 批改财务统计列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "getWithdrawApplys", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getWithdrawApplys(CorrectWithdrawApplyRequest request) {
		Value value = correctConsoleConfigDatawayClient.getWithdrawApplys(request.getSize(), request.getPage(),
				request.getYear(), request.getMonth(), request.getCorrectWithdrawApplyId(), request.getAccountName(),
				request.getRealName(), request.getStatus());

		return value;
	}
}
