package com.lanking.uxb.rescon.basedata.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.basedata.api.ResconTTSType;
import com.lanking.uxb.rescon.basedata.api.impl.ResconTTSConsumer;
import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;

/**
 * @author xinyu.zhou
 * @since V2.1
 */
@RestController
@RequestMapping(value = "rescon/section")
public class ResconTTSController {

	@Autowired
	private ResconTTSConsumer consumer;

	/**
	 * 保存
	 *
	 * @param type
	 *            保存对象的类型
	 * @param formStr
	 *            ResconTTSForm stringfy后的字符串
	 * @return value
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(ResconTTSType type, @RequestParam(value = "formStr") String formStr) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}
		ResconTTSForm form = JSONObject.parseObject(formStr, ResconTTSForm.class);
		consumer.save(form, type);

		return new Value();
	}

	/**
	 * 根据不同类型进行查询
	 *
	 * @param type
	 * @return
	 */
	@RequestMapping(value = "findAll", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findAll(@RequestParam(value = "type", defaultValue = "TEXTBOOKCATEGORY") ResconTTSType type,
			Integer phaseCode, Integer subjectCode, Long pcode,
			@RequestParam(value = "level", defaultValue = "0") Integer level) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("phaseCode", phaseCode);
		params.put("subjectCode", subjectCode);
		params.put("pcode", pcode);
		params.put("level", level);

		return new Value(consumer.findAll(params, type));
	}

	/**
	 * 排序
	 *
	 * @param id
	 *            code
	 * @param sequence
	 *            排序值
	 * @param type
	 *            类型
	 * @return
	 */
	@RequestMapping(value = "sequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sequence(@RequestParam(value = "id") Long id, @RequestParam(value = "sequence") Integer sequence,
			ResconTTSType type) {
		ResconTTSForm form = new ResconTTSForm();
		form.setId(id);
		form.setSequence(sequence);

		consumer.sequence(form, type);

		return new Value();
	}

	@RequestMapping(value = "sync_data", method = { RequestMethod.GET, RequestMethod.POST })
	public Value syncData(ResconTTSType type) {
		if (type == null) {
			for (ResconTTSType t : ResconTTSType.values()) {
				consumer.syncData(t);
			}
		} else {
			consumer.syncData(type);
		}
		return new Value();
	}

	/**
	 * 批量更新排序值
	 *
	 * @param formStr
	 *            需要更新的排序对象
	 * @return {@link Value}
	 */
	@RequestMapping(value = "update_sequences", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateSequences(@RequestParam(value = "formStr") String formStr, ResconTTSType type) {
		if (StringUtils.isBlank(formStr)) {
			return new Value(new IllegalArgException());
		}

		List<ResconTTSForm> forms = JSONObject.parseArray(formStr, ResconTTSForm.class);
		consumer.sequence(forms, type);

		return new Value();
	}
}
