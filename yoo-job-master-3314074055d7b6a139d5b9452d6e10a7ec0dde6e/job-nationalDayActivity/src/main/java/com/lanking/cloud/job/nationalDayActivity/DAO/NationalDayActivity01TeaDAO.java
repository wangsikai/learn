package com.lanking.cloud.job.nationalDayActivity.DAO;

import java.util.List;

import com.lanking.cloud.component.db.support.hibernate.dao.IHibernateDAO;
import com.lanking.cloud.domain.yoo.activity.nationalDay01.NationalDayActivity01Tea;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.data.CursorPageable;

public interface NationalDayActivity01TeaDAO extends IHibernateDAO<NationalDayActivity01Tea, Long> {

	/**
	 * 游标查询所有指定时间段的数据数据
	 * 
	 * @param modVal
	 *            根据userId取模
	 * @param pageable
	 * @return
	 */
	CursorPage<Long, NationalDayActivity01Tea> getAllTeaByCursor(CursorPageable<Long> pageable);

	/**
	 * 删除数据
	 */
	void deletes(List<Long> ids);

	/**
	 * 查询所有需要删除的教师id
	 */
	List<Long> getDeteleUserIds();

	/**
	 * 按照规则取前x个教师信息
	 */
	List<NationalDayActivity01Tea> getTopNTea(int count);
}
