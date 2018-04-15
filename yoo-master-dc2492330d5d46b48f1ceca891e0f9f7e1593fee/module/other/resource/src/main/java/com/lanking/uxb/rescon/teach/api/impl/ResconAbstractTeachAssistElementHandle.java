package com.lanking.uxb.rescon.teach.api.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 抽象处理类
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public abstract class ResconAbstractTeachAssistElementHandle {
	/**
	 * 获得模块类型
	 *
	 * @return {@link TeachAssistElementType}
	 */
	public TeachAssistElementType getType() {
		return TeachAssistElementType.NULL;
	}

	/**
	 * String -> JSONObject
	 *
	 * @param form
	 *            待转换的字符串
	 * @return {@link JSONObject}
	 */
	public JSONObject parseForm(String form) {
		if (StringUtils.isNotBlank(form)) {
			return JSONObject.parseObject(form);
		}

		return null;
	}

	/**
	 * 解析传参中的list对象
	 *
	 * @param key
	 *            key
	 * @param param
	 *            参数
	 * @return {@link List}
	 */
	public List<Long> parseList(String key, JSONObject param) {
		JSONArray jsonArray = param.getJSONArray(key);
		if (null != jsonArray && jsonArray.size() > 0) {
			List<Long> retList = new ArrayList<Long>(jsonArray.size());

			for (Object o : jsonArray) {
				retList.add(Long.valueOf(o.toString()));
			}

			return retList;
		}

		return Collections.EMPTY_LIST;
	}

	/**
	 * 保存，具体由子类实现
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 */
	public abstract void save(TeachAssistElementForm form);

	/**
	 * 删除 具体由子类实现
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 */
	public abstract void delete(TeachAssistElementForm form);

	/**
	 * 更新顺序
	 *
	 * @param id
	 *            id
	 * @param sequence
	 *            顺序
	 */
	public abstract void sequence(long id, int sequence, long userId);

	/**
	 * 根据目录id获得相应模块
	 *
	 * @param catalogId
	 *            目录id
	 * @return {@link AbstractTeachAssistElement}
	 */
	public abstract List<? extends AbstractTeachAssistElement> get(long catalogId);

	/**
	 * 保存元素模块内容
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 * @param elementId
	 *            模块id
	 */
	public void saveContent(TeachAssistElementForm form, long elementId) {
	}

	/**
	 * 更新元素排序顺序
	 *
	 * @param id
	 *            元素id
	 * @param sequence
	 *            排序值
	 */
	public void updateContentSequence(long id, int sequence) {
	}

	/**
	 * 删除元素内容
	 *
	 * @param id
	 *            元素id
	 */
	public void deleteContent(long id) {
	}

	/**
	 * 获得元素列表
	 *
	 * @param id
	 *            模块id
	 * @return {@link List}
	 */
	public List getContents(long id) {
		return Collections.EMPTY_LIST;
	}

	/**
	 * 根据模块id获得一条模块数据
	 *
	 * @param id
	 *            模块id
	 * @return 模块数据对象
	 */
	public abstract AbstractTeachAssistElement findOne(long id);

	/**
	 * 根据模块id删除元素
	 *
	 * @param elementId
	 *            模块id
	 */
	public void deleteByElement(long elementId) {
	}

	/**
	 * 根据模块id列表查找数据
	 *
	 * @param ids
	 *            id列表
	 * @return 元素列表
	 */
	public List mgetContents(Collection<Long> ids) {
		return Collections.EMPTY_LIST;
	}

	public abstract void copy(long newCatalogId, Collection<Long> ids, long userId);

}
