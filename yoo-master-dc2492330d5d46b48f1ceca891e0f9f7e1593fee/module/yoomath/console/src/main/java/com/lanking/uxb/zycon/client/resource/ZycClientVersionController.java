package com.lanking.uxb.zycon.client.resource;

import com.lanking.cloud.domain.base.session.DeviceType;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.frame.system.YooApp;
import com.lanking.cloud.ex.core.EntityExistsException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ValidationException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.zycon.client.value.VZycVersion;

import com.lanking.uxb.zycon.parameter.api.ZycParameterService;
import com.lanking.uxb.zycon.parameter.convert.ZycParameterConvert;
import com.lanking.uxb.zycon.parameter.form.ParameterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.uxb.zycon.client.api.ZycClientVersionService;
import com.lanking.uxb.zycon.client.convert.YoomathClientVersionConvert;
import com.lanking.uxb.zycon.client.form.CVersionForm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 悠数学Android客户端版本管理
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zyc/client")
public class ZycClientVersionController {
	@Autowired
	private ZycClientVersionService zycClientVersionService;
	@Autowired
	private YoomathClientVersionConvert yoomathClientVersionConvert;
	@Autowired
	private ParameterService parameterService;
	@Autowired
	private ZycParameterService zycParameterService;

	/**
	 * 查询客户端版本信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryVersion")
	public Value queryVersion(YooApp app, DeviceType deviceType) {
		if (app == null) {
			return new Value(new MissingArgumentException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		List<VZycVersion> versions = yoomathClientVersionConvert
		        .to(zycClientVersionService.queryVersion(app, deviceType));
		retMap.put("versions", versions);

		Parameter parameter = parameterService.get(Product.YOOMATH, "app.notice.rate");
		retMap.put("noticeRate", parameter == null ? 0 : parameter.getValue());
		return new Value(retMap);
	}

	/**
	 * 保存或更新版本信息
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "updateVersion")
	public Value updateVersion(CVersionForm form) {
		if (form.getVersion() != null) {
			if (zycClientVersionService.versionCount(form.getApp(), form.getVersion(),
			        form.getDeviceType()) && form.isFlag()) {
				return new Value(new EntityExistsException());
			}
			// 版本号必须是未删除里面最大的
			if (!zycClientVersionService.isMaxVersionNum(form.getApp(),
					Integer.parseInt(form.getVersion().replace(".", "")), form.getDeviceType())
					&& form.isFlag()) {
				return new Value(new ValidationException());
			}
		}
		zycClientVersionService.updateVersion(form);
		return new Value();
	}

	/**
	 * 更新频率
	 *
	 * @param noticeRate
	 *            提醒更新频率
	 * @return {@link Value}
	 */
	@RequestMapping(value = "updateNoticeRate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateNoticeRate(int noticeRate) {
		// 更新频率不可以为负数并且不可以大于30天
		if (noticeRate <= 0 || noticeRate > 30) {
			return new Value(new IllegalArgException());
		}
		Parameter parameter = parameterService.get(Product.YOOMATH, "app.notice.rate");
		ParameterForm form = new ParameterForm();
		form.setId(parameter.getId());
		form.setNote(parameter.getNote());
		form.setKey("app.notice.rate");
		form.setProduct(Product.YOOMATH);
		form.setValue(String.valueOf(noticeRate));
		zycParameterService.save(form);
		zycParameterService.syncData();
		return new Value();
	}
}
