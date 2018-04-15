package com.lanking.uxb.rescon.teach.api;

import java.util.List;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistPresetContentFallibleDifficult;
import com.lanking.uxb.rescon.teach.form.TeachAssistPresetContentFallibleDifficultForm;

/**
 * 预置内容-易错疑难
 * 
 * @author wangsenhao
 *
 */
public interface ResconTeachAssistPresetContentFallibleDifficultService {

	/**
	 * 获取教辅预置内容-易错疑难
	 * 
	 * @param teachassistPresetcontentId
	 * @return
	 */
	List<TeachAssistPresetContentFallibleDifficult> getByPresetId(Long teachassistPresetcontentId);

	/**
	 * 查询是否有没有未通过校验的
	 * 
	 * @param teachassistPresetcontentId
	 * @return
	 */
	Long nopassCount(Long teachassistPresetcontentId);

	/**
	 * 校验
	 * 
	 * @param id
	 * @param status
	 */
	void check(Long id, CardStatus status);

	/**
	 * 删除
	 * 
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 保存易错点相关信息
	 * 
	 * @param form
	 */
	void save(TeachAssistPresetContentFallibleDifficultForm form);

	/**
	 * 
	 * @param id
	 * @return
	 */
	TeachAssistPresetContentFallibleDifficult get(Long id);

	/**
	 * 根据知识专项码查找易错疑难点
	 *
	 * @param code
	 *            知识专项代码
	 * @return {@link List}
	 */
	List<TeachAssistPresetContentFallibleDifficult> getByCode(long code);
}
