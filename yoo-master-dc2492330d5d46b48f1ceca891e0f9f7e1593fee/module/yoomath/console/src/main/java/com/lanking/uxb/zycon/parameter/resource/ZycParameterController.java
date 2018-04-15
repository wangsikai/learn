package com.lanking.uxb.zycon.parameter.resource;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.ex.core.DBException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;
import com.lanking.uxb.zycon.parameter.api.ZycParameterService;
import com.lanking.uxb.zycon.parameter.convert.ZycParameterConvert;
import com.lanking.uxb.zycon.parameter.form.ParameterForm;
import com.lanking.uxb.zycon.parameter.value.VParameter;

/**
 * 系统配置
 * 
 * @author zemin.song
 */
@RestController
@RequestMapping(value = "zyc/parameter")
public class ZycParameterController {

	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZycParameterConvert parameterConvert;
	@Autowired
	private ZycParameterService zycParameterService;

	/**
	 * 查询配置信息
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size) {
		int offset = (page - 1) * size;
		Pageable pageable = P.offset(offset, size);
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		if (page == 1) {
			retMap.put("products", Product.values());
		}
		Page<Parameter> pageValue = zycParameterService.getAllList(pageable);
		VPage<VParameter> vPage = new VPage<VParameter>();
		vPage.setItems(parameterConvert.to(pageValue.getItems()));
		vPage.setCurrentPage(page);
		vPage.setTotalPage(pageValue.getPageCount());
		vPage.setPageSize(size);
		vPage.setTotal(pageValue.getTotalCount());
		retMap.put("vPage", vPage);
		return new Value(retMap);
	}

	/**
	 * 添加/编辑配置信息
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "create", method = { RequestMethod.GET, RequestMethod.POST })
	public Value create(ParameterForm form) {
		int ret = zycParameterService.save(form);
		if (ret > 0) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.PARAMETER_KEY_REPEAT));
		} else {
			return new Value();
		}
	}

	/**
	 * 删除配置信息
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(@RequestParam(value = "id") Long id) {
		int ret = zycParameterService.dalete(id);
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new DBException());
		}
	}

	/**
	 * 同步数据
	 * 
	 * @param
	 * @return
	 */
	@RequestMapping(value = "sync_data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncData() {
		zycParameterService.syncData();
		return new Value();
	}

}
