package com.lanking.uxb.rescon.teach.api;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForms;
import com.lanking.uxb.rescon.teach.value.VTeachAssistCatalog;

/**
 * 教辅目录树接口.
 * 
 * @author wlche
 * @since v1.3
 */
public interface ResconTeachAssistCatalogManage {

	/**
	 * 获取一个教辅版本的目录列表
	 * 
	 * @param teachAssistVersionId
	 *            教辅版本ID
	 * @return 教辅目录集合
	 */
	List<TeachAssistCatalog> listTeachAssistCatalogCatalog(long teachAssistVersionId);

	/**
	 * 添加教辅目录.
	 * 
	 * @param catalogForm
	 *            目录表单数据.
	 * @return
	 */
	VTeachAssistCatalog addCatalog(TeachAssistCatalogForm catalogForm, long createId) throws ResourceConsoleException;

	/**
	 * 批量保存目录.
	 * 
	 * @param catalogForms
	 *            目录集合
	 * @param deleteCatalogs
	 *            删除的目录ID集合.
	 * @param updateId
	 *            更新人
	 * @throws ResourceConsoleException
	 */
	void batchSaveCatalogs(TeachAssistCatalogForms catalogForms, long updateId) throws ResourceConsoleException;

	/**
	 * 更新目录名称
	 * 
	 * @param id
	 *            目录ID
	 * @param name
	 *            新的名称
	 * @param updateId
	 *            操作人ID
	 * @return 更新后的目录
	 */
	TeachAssistCatalog updateName(long id, String name, long updateId) throws ResourceConsoleException;

	/**
	 * 上移.
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 * @return
	 */
	void up(long id, long updateId) throws ResourceConsoleException;

	/**
	 * 下移.
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 * @return
	 */
	void down(long id, long updateId) throws ResourceConsoleException;

	/**
	 * 向下降级.
	 * 
	 * @param id
	 *            目录ID
	 * @param updateId
	 *            操作人ID
	 */
	void downLevel(long id, long updateId) throws ResourceConsoleException;

	/**
	 * 获得单层子目录.
	 * 
	 * @param pid
	 *            父节点
	 */
	List<TeachAssistCatalog> children(long pid);

	/**
	 * 获得单层子目录数量.
	 * 
	 * @param pid
	 *            父节点
	 */
	long childrenCount(long pid);

	/**
	 * 获得所有子孙菜单.
	 * 
	 * @param pid
	 *            父节点
	 * @param pidLevel
	 *            父层级
	 * @return
	 */
	List<Long> allChildrenIds(long pid, int pidLevel);

	/**
	 * 删除菜单.
	 * 
	 * @param catalogId
	 *            菜单ID.
	 * @param confirm
	 *            菜单下有资源时，是否提示返回
	 * @return
	 */
	void delete(Collection<Long> catalogIds, boolean confirm, long updateId) throws ResourceConsoleException;

	/**
	 * 获取最底层
	 * 
	 * @param teachAssistVersionId
	 * @return
	 */
	List<TeachAssistCatalog> findLowestList(long teachAssistVersionId);

	/**
	 * 教辅校验
	 * 
	 * @param status
	 * @param id
	 */
	void updateCheckStatus(CardStatus status, long id);

	/**
	 * 复制目录
	 *
	 * @param bookVersionId
	 *            新版的目录id
	 * @param catalogs
	 *            目录列表
	 * @param userId
	 *            用户id
	 * @return {@link Map}
	 */
	Map<Long, TeachAssistCatalog> copy(long bookVersionId, Collection<TeachAssistCatalog> catalogs, long userId);
}
