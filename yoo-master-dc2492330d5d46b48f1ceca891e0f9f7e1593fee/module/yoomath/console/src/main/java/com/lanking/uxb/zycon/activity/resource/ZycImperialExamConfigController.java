package com.lanking.uxb.zycon.activity.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.activity.api.ZycImperialExamConfigService;
import com.lanking.uxb.zycon.activity.form.ZycExamConfigForm;

/**
 * 科举活动一些基本的配置<br>
 * 1.提供奖品名称、各阶段时间的等显示<br>
 * 2.提供配置的编辑、删除<br>
 * 3.提供数据的生成
 * 
 * @author wangsenhao
 * @version 2017年4月12日
 */
@RestController
@RequestMapping(value = "zyc/activity/config")
public class ZycImperialExamConfigController {

	@Autowired
	private ZycImperialExamConfigService configService;

	/**
	 * 保存
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save", method = { RequestMethod.POST, RequestMethod.GET })
	public Value save(ZycExamConfigForm form) {
		configService.save(form);
		return new Value();
	}

	/**
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "queryByCode", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryByCode(Long code) {
		ImperialExaminationActivity m = configService.queryByCode(code);
		return new Value(m);
	}

	/**
	 * 保存
	 * 
	 * @param form
	 * @return
	 */
	@RequestMapping(value = "save2", method = { RequestMethod.POST, RequestMethod.GET })
	public Value save2(ZycExamConfigForm form) {
		configService.save2(form);
		return new Value();
	}
}
