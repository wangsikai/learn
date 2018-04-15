package com.lanking.uxb.service.intelligentCorrection.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.intelligentCorrection.api.IntelligentCorrectionService;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;

@RestController
@RequestMapping("intelligentCorrection")
public class IntelligentCorrectionController {

	@Autowired
	private IntelligentCorrectionService intelligentCorrectionService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "single", method = { RequestMethod.POST, RequestMethod.GET })
	public Value single(Long queryId, Long answerId, String target, String query) {
		try {
			return new Value(intelligentCorrectionService.correct(queryId, answerId, target, query));
		} catch (IntelligentCorrectionException | IllegalArgException e) {
			return new Value(e);
		}
	}
}
