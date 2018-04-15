package com.lanking.uxb.zycon.base.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.base.api.ZycTeachVersionService;
import com.lanking.uxb.zycon.base.value.CTeachVersion;

/**
 * 悠数学管控台教材版本配置
 * 
 * @since 2.1
 * @author wangsenhao
 * @version 2015年9月1日
 */
@RestController
@RequestMapping("zyc/teachVersion")
public class ZycTeachVersionController {
	@Autowired
	private ZycTeachVersionService zycTeachVersionService;

	/**
	 * 获取悠数学教材版本
	 * 
	 * @param subjectCode
	 *            科目code
	 * @param phaseName
	 *            当前需要获取的阶段标识
	 * @return
	 */
	@RequestMapping(value = "getMathTextList")
	public Value getMathTextList(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseName") String phaseName) {
		return new Value(zycTeachVersionService.getMathTextList(subjectCode, phaseName, "math"));
	}

	/**
	 * 获取对应阶段和学科的所有教材版本
	 * 
	 * @param subjectCode
	 * @param phaseName
	 * @return
	 */
	@RequestMapping(value = "getAllTextList")
	public Value getAllTextList(@RequestParam(value = "subjectCode") Integer subjectCode,
			@RequestParam(value = "phaseName") String phaseName) {
		return new Value(zycTeachVersionService.getMathTextList(subjectCode, phaseName, "all"));
	}

	/**
	 * 更新
	 * 
	 * @param json
	 * @param phaseName
	 * @return
	 */
	@RequestMapping(value = "save")
	public Value save(@RequestParam(value = "list") String json, @RequestParam(value = "phaseName") String phaseName) {
		List<CTeachVersion> jsonlist = JSON.parseArray(json, CTeachVersion.class);
		zycTeachVersionService.save(jsonlist, phaseName);
		return new Value();
	}

	/**
	 * 应用
	 * 
	 * @return
	 */
	@RequestMapping(value = "apply")
	public Value apply() {
		zycTeachVersionService.syncData();
		return new Value();
	}
}
