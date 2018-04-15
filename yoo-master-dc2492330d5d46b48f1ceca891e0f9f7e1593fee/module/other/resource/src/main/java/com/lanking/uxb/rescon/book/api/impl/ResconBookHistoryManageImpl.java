package com.lanking.uxb.rescon.book.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.book.BookHistory;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.book.api.ResconBookHistoryManage;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;

/**
 * 历史操作.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月31日
 */
@Service
@Transactional(readOnly = true)
public class ResconBookHistoryManageImpl implements ResconBookHistoryManage {
	@Autowired
	@Qualifier("BookHistoryRepo")
	Repo<BookHistory, Long> historyRepo;

	@Override
	@Transactional
	public void saveHistory(BookHistory history) throws ResourceConsoleException {
		historyRepo.save(history);
	}

	@Override
	@Transactional(readOnly = true)
	public List<BookHistory> listCountHistory(long bookId, int count) {
		Page<BookHistory> page = historyRepo.find(
				"select * from book_history where book_id=:bookId order by create_at ASC",
				Params.param("bookId", bookId)).fetch(P.offset(0, count + 1));
		List<BookHistory> historys = new ArrayList<BookHistory>();
		if (page.getItemSize() > count) {
			// 保留创建记录，另取最近的count-1条记录
			historys.add(page.getFirst());
			page = historyRepo.find("select * from book_history where book_id=:bookId order by create_at DESC",
					Params.param("bookId", bookId)).fetch(P.offset(0, count - 1));
			Collections.reverse(page.getItems());
			historys.addAll(page.getItems());
		} else {
			historys = page.getItems();
		}
		return historys;
	}

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Map<Long, Date> mgetTimeByLastPublish(Collection<Long> bookIds) {
		if (CollectionUtils.isEmpty(bookIds)) {
			return Maps.newHashMap();
		}
		List<Map> list = historyRepo.find("$mgetTimeByPublish", Params.param("bookIds", bookIds)).list(Map.class);
		Map<Long, Date> returnMap = new HashMap<Long, Date>(list.size());
		for (Map map : list) {
			long bookid = Long.parseLong(map.get("bookid").toString());
			returnMap.put(bookid, (Date) map.get("ct"));
		}

		return returnMap;
	}
}
