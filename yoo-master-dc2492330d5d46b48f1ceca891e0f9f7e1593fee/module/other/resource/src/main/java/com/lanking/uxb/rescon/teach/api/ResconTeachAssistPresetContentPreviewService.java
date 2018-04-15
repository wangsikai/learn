package com.lanking.uxb.rescon.teach.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentPreview;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentPreviewForm;

/**
 * 教辅预置内容-预习点 接口
 * 
 * @author wangsenhao
 *
 */
public interface ResconTeachAssistPresetContentPreviewService {

	/**
	 * 获取预习点
	 * 
	 * @param teachassistPresetcontentId
	 *            预置内容id
	 * @return
	 */
	List<TeachAssistPresetContentPreview> getByPresetId(Long teachassistPresetcontentId);

	/**
	 * 查询是否有没有未通过校验的
	 * 
	 * @param teachassistPresetcontentId
	 * @return
	 */
	Long nopassCount(Long teachassistPresetcontentId);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 校验
	 * 
	 * @param id
	 * @param status
	 */
	void check(Long id, CardStatus status);

	/**
	 * 保存预习点
	 * 
	 * @param form
	 */
	void save(TeachAssistPresetContentPreviewForm form);

	/**
	 * 
	 * @param id
	 * @return
	 */
	TeachAssistPresetContentPreview get(Long id);

	/**
	 * 根据知识专项查找预习点
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link List}
	 */
	List<TeachAssistPresetContentPreview> findByKnowledgeSystem(long code);
}
