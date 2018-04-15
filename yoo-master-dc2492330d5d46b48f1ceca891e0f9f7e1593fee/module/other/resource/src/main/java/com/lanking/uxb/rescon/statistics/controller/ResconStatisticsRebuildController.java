package com.lanking.uxb.rescon.statistics.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.counter.Counter;
import com.lanking.cloud.domain.base.counter.api.Count;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BooksCounterProvider;

/**
 * 计数重建（临时）.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2016年1月4日
 */
@RestController
@RequestMapping("rescon/statis/rebuild")
@RolesAllowed(userTypes = { "VENDOR_ADMIN" })
public class ResconStatisticsRebuildController {
	@Autowired
	@Qualifier("CounterRepo")
	Repo<Counter, Long> counter;

	@Autowired
	private ResconBookManage bookManage;
	@Autowired
	private BooksCounterProvider booksCounterProvider;
	@Autowired
	private BookCounterProvider bookCounterProvider;

	/**
	 * 重建书本库.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	@RequestMapping(value = "books", method = { RequestMethod.GET, RequestMethod.POST })
	public Value rebuildBooks() {

		try {
			// 书本库总数
			List<Map> totalList = counter
					.find("SELECT COUNT(b.id) as num,b.vendor_id as vendor,b.status FROM book b where b.status != 2 GROUP BY b.vendor_id")
					.list(Map.class);
			for (Map map : totalList) {
				Long num = Long.parseLong(map.get("num").toString());
				Long vendorId = Long.parseLong(map.get("vendor").toString());
				booksCounterProvider.counterReset(vendorId, Count.COUNTER_1, num);
			}

			// 书本库已发布计数
			List<Map> passList = counter.find(
					"SELECT COUNT(v.book_id) as num,b.vendor_id as vendor,v.status FROM book_version v"
							+ " INNER JOIN book b ON b.id=v.book_id AND b.status != 2"
							+ " WHERE v.status = 2 GROUP BY b.vendor_id ").list(Map.class);
			for (Map map : passList) {
				Long num = Long.parseLong(map.get("num").toString());
				Long vendorId = Long.parseLong(map.get("vendor").toString());
				booksCounterProvider.counterReset(vendorId, Count.COUNTER_2, num);
			}

			// 书本库待发布计数
			List<Map> daiList = counter.find(
					"SELECT COUNT(v.id) as num,b.vendor_id as vendor,v.status FROM book_version v"
							+ " INNER JOIN book b ON b.id=v.book_id AND b.status != 2"
							+ " WHERE v.status = 1 GROUP BY b.vendor_id").list(Map.class);
			for (Map map : daiList) {
				Long num = Long.parseLong(map.get("num").toString());
				Long vendorId = Long.parseLong(map.get("vendor").toString());
				booksCounterProvider.counterReset(vendorId, Count.COUNTER_3, num);
			}

			// 书本库录入中计数
			List<Map> inList = counter.find(
					"SELECT COUNT(v.id) as num,b.vendor_id as vendor,v.status FROM book_version v"
							+ " INNER JOIN book b ON b.id=v.book_id AND b.status != 2"
							+ " WHERE v.status = 0 GROUP BY b.vendor_id").list(Map.class);
			for (Map map : inList) {
				Long num = Long.parseLong(map.get("num").toString());
				Long vendorId = Long.parseLong(map.get("vendor").toString());
				booksCounterProvider.counterReset(vendorId, Count.COUNTER_4, num);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Value();
	}

	/**
	 * 重建书本统计.
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional
	@RequestMapping(value = "book", method = { RequestMethod.GET, RequestMethod.POST })
	public Value rebuildBook() {
		List<Map> allList = counter.find(
				"SELECT COUNT(t.question_id) AS num,t.book_version_id AS id FROM book_question t"
						+ " INNER JOIN question q ON q.id = t.question_id AND q.status!=5"
						+ " INNER JOIN book_version v ON v.id=t.book_version_id AND v.status != 4 AND v.status != 5"
						+ " GROUP BY t.book_version_id").list(Map.class);

		List<Map> passList = counter.find(
				"SELECT COUNT(t.question_id) AS num,t.book_version_id AS id FROM book_question t"
						+ " INNER JOIN question q ON q.id = t.question_id AND q.status=2"
						+ " INNER JOIN book_version v ON v.id=t.book_version_id AND v.status != 4 AND v.status != 5"
						+ " GROUP BY t.book_version_id").list(Map.class);

		List<Map> noCheckList = counter.find(
				"SELECT COUNT(t.question_id) AS num,t.book_version_id AS id FROM book_question t"
						+ " INNER JOIN question q ON q.id = t.question_id AND q.status=0"
						+ " INNER JOIN book_version v ON v.id=t.book_version_id AND v.status != 4 AND v.status != 5"
						+ " GROUP BY t.book_version_id").list(Map.class);

		List<Map> onePassList = counter.find(
				"SELECT COUNT(t.question_id) AS num,t.book_version_id AS id FROM book_question t"
						+ " INNER JOIN question q ON q.id = t.question_id AND q.status=4"
						+ " INNER JOIN book_version v ON v.id=t.book_version_id AND v.status != 4 AND v.status != 5"
						+ " GROUP BY t.book_version_id").list(Map.class);

		List<Map> draftList = counter.find(
				"SELECT COUNT(t.question_id) AS num,t.book_version_id AS id FROM book_question t"
						+ " INNER JOIN question q ON q.id = t.question_id AND q.status=5"
						+ " INNER JOIN book_version v ON v.id=t.book_version_id AND v.status != 4 AND v.status != 5"
						+ " GROUP BY t.book_version_id").list(Map.class);

		return new Value();
	}
}
