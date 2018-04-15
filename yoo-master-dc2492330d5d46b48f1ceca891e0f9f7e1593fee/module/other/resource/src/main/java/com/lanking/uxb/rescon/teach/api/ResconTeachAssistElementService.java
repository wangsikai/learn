package com.lanking.uxb.rescon.teach.api;

import com.lanking.cloud.domain.common.resource.teachAssist.AbstractTeachAssistElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 教辅模块服务
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
public interface ResconTeachAssistElementService {
	/**
	 * 保存
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 */
	void save(TeachAssistElementForm form);

	/**
	 * 删除
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 */
	void delete(TeachAssistElementForm form);

	/**
	 * 更新顺序
	 * 
	 * @param form
	 *            {@link TeachAssistElementForm}
	 */
	void sequence(TeachAssistElementForm form);

	/**
	 * 根据目录id获得相应模块
	 *
	 * @param catalogId
	 *            目录id
	 * @return {@link AbstractTeachAssistElement}
	 */
	List<AbstractTeachAssistElement> get(long catalogId);

	/**
	 * 保存元素
	 *
	 * @param form
	 *            {@link TeachAssistElementForm}
	 * @param elementId
	 *            模块id
	 */
	void saveContent(TeachAssistElementForm form, long elementId);

	/**
	 * 更新元素排序值
	 *
	 * @param id
	 *            元素id
	 * @param sequence
	 *            排序值
	 * @param type
	 *            {@link TeachAssistElementType}
	 */
	void updateSequence(long id, int sequence, TeachAssistElementType type);

	/**
	 * 删除模块元素
	 *
	 * @param id
	 *            元素id
	 * @param type
	 *            {@link TeachAssistElementType}
	 */
	void deleteContent(long id, TeachAssistElementType type);

	/**
	 * 获得模块元素数据
	 *
	 * @param id
	 *            模块id
	 * @param type
	 *            {@link TeachAssistElementType}
	 * @return 元素列表
	 */
	List getContents(long id, TeachAssistElementType type);

	/**
	 * 根据id获得模块数据一条
	 *
	 * @param id
	 *            模块id
	 * @return {@link AbstractTeachAssistElement}
	 */
	AbstractTeachAssistElement findOne(long id, TeachAssistElementType type);

	/**
	 * 根据多个模块id列表获取数据
	 *
	 * @param ids
	 *            模块id列表
	 * @param type
	 *            {@link TeachAssistElementType}
	 * @return 元素列表
	 */
	List getContents(Collection<Long> ids, TeachAssistElementType type);

	/**
	 * 拷贝数据
	 *
	 * @param map
	 *            模块对应列表
	 * @param userId
	 *            用户id
	 */
	void copy(Map<Long, TeachAssistCatalog> map, long userId);
}
