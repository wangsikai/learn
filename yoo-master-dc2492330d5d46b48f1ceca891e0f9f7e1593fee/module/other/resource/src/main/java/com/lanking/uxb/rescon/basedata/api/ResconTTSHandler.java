package com.lanking.uxb.rescon.basedata.api;

import java.util.List;
import java.util.Map;

import com.lanking.uxb.rescon.basedata.form.ResconTTSForm;
import com.lanking.uxb.rescon.basedata.value.VResconTTS;

/**
 * TextbookCategory, Textbook, Section处理接口<br />
 * 因为前端使用zTree组件并且按照设计的要求，在同一目录树上进行处理，故在树上有三种类型的节点。<br />
 * zTree有一个特性就是可以直接将节点全部进行返回，由前端的js进行树行的处理，后端只需要返回数据就可以了<br />
 *
 * @author xinyu.zhou
 * @since V2.1
 */
public interface ResconTTSHandler {
	/**
	 * 返回节点类型
	 *
	 * @return {@link ResconTTSType}
	 */
	ResconTTSType getType();

	void save(ResconTTSForm form);

	/**
	 * 根据id得到节点数据
	 *
	 * @param id
	 *            id
	 * @return {@link VResconTTS}
	 */
	VResconTTS get(Long id);

	/**
	 * 根据查询参数得到多个节点数据
	 *
	 * @param params
	 *            参数
	 * @return {@link VResconTTS}
	 */
	List<VResconTTS> findAll(Map<String, Object> params);

	/**
	 * 同步数据
	 */
	void syncData();

	/**
	 * 更新排序值
	 *
	 * @param form
	 *            {@link ResconTTSForm}
	 */
	void updateSequence(ResconTTSForm form);

	/**
	 * 更新排序值
	 *
	 * @param forms
	 *            {@link ResconTTSForm}
	 */
	void updateSequence(List<ResconTTSForm> forms);
}
