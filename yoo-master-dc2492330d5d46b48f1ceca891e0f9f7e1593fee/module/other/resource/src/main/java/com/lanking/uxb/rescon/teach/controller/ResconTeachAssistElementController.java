package com.lanking.uxb.rescon.teach.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistElementConvert;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;
import com.lanking.uxb.rescon.teach.value.VTeachAssistElement;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 教辅模块相关Controller
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@RestController
@RequestMapping(value = "rescon/tae")
public class ResconTeachAssistElementController {
	@Autowired
	private ResconTeachAssistElementService elementService;
	@Autowired
	private ResconTeachAssistElementConvert elementConvert;

	/**
	 * 保存模块
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value save(TeachAssistElementForm form) {
		form.setUserId(Security.getUserId());

		elementService.save(form);
		return new Value();
	}

	/**
	 * 根据模块id查找数据
	 *
	 * @param id
	 *            模块id
	 * @param type
	 *            {@link TeachAssistElementType}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getById", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getById(long id, TeachAssistElementType type) {
		VTeachAssistElement v = elementConvert.to(elementService.findOne(id, type));
		return new Value(v);
	}

	/**
	 * 删除某个模块
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delete(TeachAssistElementForm form) {
		if (form.getId() == null || form.getId() <= 0) {
			return new Value(new IllegalArgException());
		}
		elementService.delete(form);

		return new Value();
	}

	/**
	 * 排序
	 *
	 * @param ids
	 *            需要排序的id列表
	 * @param sequences
	 *            对应列表中的id排序值
	 * @param types
	 *            需要更新的模块类型 {@link TeachAssistElementType}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "sequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sequence(@RequestParam(value = "ids") List<Long> ids,
			@RequestParam(value = "sequences") List<Integer> sequences, long catalogId,
			@RequestParam(value = "types") List<TeachAssistElementType> types) {

		if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(sequences) || ids.size() != sequences.size()) {
			return new Value(new IllegalArgException());
		}

		int i = 0;
		TeachAssistElementForm form = new TeachAssistElementForm();
		form.setCatalogId(catalogId);
		form.setUserId(Security.getUserId());
		for (Long id : ids) {
			form.setId(id);
			form.setSequence(sequences.get(i));
			form.setType(types.get(i));
			elementService.sequence(form);

			i++;
		}

		return new Value();
	}

	/**
	 * 根据目录id获得数据
	 *
	 * @param catalogId
	 *            目录id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "getByCatalog", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getByCatalog(long catalogId) {
		List<VTeachAssistElement> vs = elementConvert.to(elementService.get(catalogId));
		// 根据sequence进进行序
		Collections.sort(vs);

		Map<String, Object> retMap = new HashMap<String, Object>(2);
		retMap.put("elements", vs);
		retMap.put("catalogId", catalogId);
		return new Value(retMap);
	}

	/**
	 * 保存模块元素数据
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 * @param elementId
	 *            元素id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "saveContent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveContent(TeachAssistElementForm form, long elementId) {
		form.setUserId(Security.getUserId());

		elementService.saveContent(form, elementId);
		return new Value();
	}

	/**
	 * 更新元素排序值
	 *
	 * @param ids
	 *            元素id列表
	 * @param sequences
	 *            各元素排序值列表
	 * @param type
	 *            {@link TeachAssistElementType}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "updateContentSequence", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateContentSequence(@RequestParam(value = "ids") List<Long> ids,
			@RequestParam(value = "sequences") List<Integer> sequences, TeachAssistElementType type) {
		if (CollectionUtils.isEmpty(ids) || CollectionUtils.isEmpty(sequences) || ids.size() != sequences.size()) {
			return new Value(new IllegalArgException());
		}
		int i = 0;
		for (Long id : ids) {
			elementService.updateSequence(id, sequences.get(i), type);
			i++;
		}
		return new Value();
	}

	/**
	 * 删除模块元素内容
	 *
	 * @param id
	 *            元素id
	 * @param type
	 *            {@link TeachAssistElementType}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "deleteContent", method = { RequestMethod.GET, RequestMethod.POST })
	public Value deleteContent(long id, TeachAssistElementType type) {
		elementService.deleteContent(id, type);
		return new Value();
	}

}
