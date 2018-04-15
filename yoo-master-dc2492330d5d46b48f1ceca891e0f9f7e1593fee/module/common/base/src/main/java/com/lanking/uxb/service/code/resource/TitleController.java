package com.lanking.uxb.service.code.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TitleService;
import com.lanking.uxb.service.code.convert.TitleConvert;

/**
 * 地区相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
@RestController
@RequestMapping("common/title")
public class TitleController {

	@Autowired
	private TitleService titleService;
	@Autowired
	private TitleConvert titleConvert;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "all", method = { RequestMethod.GET, RequestMethod.POST })
	public Value all() {
		return new Value(titleConvert.to(titleService.getAll()));
	}
}
