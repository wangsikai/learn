package com.lanking.uxb.rescon.book.api;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lanking.cloud.domain.common.resource.book.BookHistory;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;

/**
 * 书本操作记录.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月31日
 */
public interface ResconBookHistoryManage {

	/**
	 * 保存历史记录.
	 * 
	 * @param history
	 *            历史.
	 * @throws ResourceConsoleException
	 */
	void saveHistory(BookHistory history) throws ResourceConsoleException;

	/**
	 * 获得指定业务的历史记录.
	 * 
	 * @param count
	 * @return
	 */
	List<BookHistory> listCountHistory(long bookId, int count);

	/**
	 * 获得指定版本最后通过的历史记录时间.
	 * 
	 * @param bookIds
	 *            版本ID集合
	 * @return
	 */
	Map<Long, Date> mgetTimeByLastPublish(Collection<Long> bookIds);
}
