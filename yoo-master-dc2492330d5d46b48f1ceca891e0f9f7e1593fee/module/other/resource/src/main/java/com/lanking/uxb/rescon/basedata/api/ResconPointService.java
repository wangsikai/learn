package com.lanking.uxb.rescon.basedata.api;

import com.lanking.uxb.rescon.basedata.form.ResconPointForm;

import java.util.List;

/**
 * 知识点及元知识点Service
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconPointService {
	/**
	 * 保存知识点或元知识点
	 *
	 * @param form
	 *            {@link ResconPointForm}
	 */
	void save(ResconPointForm form);

	/**
	 * 更新知识点或元知识点
	 *
	 * @param form
	 *            {@link ResconPointForm}
	 */
	void update(ResconPointForm form);

	/**
	 * 同步知识点及元知识点
	 */
	void syncData();

	/**
	 * 更新排序值
	 *
	 * @param code
	 *            知识点编码
	 * @param sequence
	 *            排序值
	 * @param type
	 *            知识点类型
	 */
	void updateSequence(int code, int sequence, ResconPointType type);

	/**
	 * 启用
	 */
	void turnOn();

	/**
	 * 批量更新排序值
	 *
	 * @param forms
	 *            需要更新的排序对象
	 */
	void updateSequence(List<ResconPointForm> forms);
}
