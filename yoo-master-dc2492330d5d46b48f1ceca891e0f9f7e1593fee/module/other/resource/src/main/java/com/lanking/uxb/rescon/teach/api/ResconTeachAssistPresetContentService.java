package com.lanking.uxb.rescon.teach.api;

import java.util.Collection;
import java.util.List;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContent;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentForm;

/**
 * 教辅预置内容接口
 * 
 * @author wangsenhao
 *
 */
public interface ResconTeachAssistPresetContentService {

	/**
	 * 通过知识专项查询教辅预置内容
	 * 
	 * @param code
	 *            知识专项code
	 * @return
	 */
	TeachAssistPresetContent getByKsCode(Long code);

	/**
	 * 
	 * @param codes
	 * @return
	 */
	List<TeachAssistPresetContent> mgetByKsCodes(Collection<Long> codes);

	/**
	 * 保存
	 * 
	 * @param form
	 */
	void save(TeachAssistPresetContentForm form);
}
