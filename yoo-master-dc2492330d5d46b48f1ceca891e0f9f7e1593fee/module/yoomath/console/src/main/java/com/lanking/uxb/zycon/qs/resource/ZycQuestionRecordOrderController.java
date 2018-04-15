package com.lanking.uxb.zycon.qs.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrder;
import com.lanking.cloud.domain.yoo.order.questionRecord.QuestionRecordOrderStatus;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.qs.api.ZycQuestionRecordOrderService;
import com.lanking.uxb.zycon.qs.convert.ZycQuestionRecordOrderConvert;
import com.lanking.uxb.zycon.qs.form.QuestionRecordOrderQueryForm;
import com.lanking.uxb.zycon.qs.value.VZycQuestionRecordOrder;

/**
 * @author xinyu.zhou
 * @since 2.6.0
 */
@RestController
@RequestMapping(value = "zyc/qro")
public class ZycQuestionRecordOrderController {
	@Autowired
	private ZycQuestionRecordOrderService qroService;
	@Autowired
	private ZycQuestionRecordOrderConvert qroConvert;

	/**
	 * 分页查询数据
	 *
	 * @param page
	 *            当前页
	 * @param size
	 *            分页大小
	 * @param form
	 *            {@link QuestionRecordOrderQueryForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "20") int size, QuestionRecordOrderQueryForm form) {

		Map<String, Object> retMap = new HashMap<String, Object>(2);

		Pageable pageable = P.index(page, size);

		Page<QuestionRecordOrder> fetchPage = qroService.query(pageable, form);
		VPage<VZycQuestionRecordOrder> retPage = new VPage<VZycQuestionRecordOrder>();

		List<VZycQuestionRecordOrder> vs = qroConvert.to(fetchPage.getItems());
		retPage.setItems(vs);
		retPage.setCurrentPage(page);
		retPage.setPageSize(size);
		retPage.setTotal(fetchPage.getTotalCount());
		retPage.setTotalPage(fetchPage.getPageCount());

		retMap.put("retPage", retPage);
		retMap.put("countDo", qroService.countDo());

		return new Value(retMap);
	}

	/**
	 * 备注信息
	 *
	 * @param id
	 *            id
	 * @param message
	 *            备注信息
	 * @param status
	 *            {@link QuestionRecordOrderStatus}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "addMessage", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addMessage(long id, String message, QuestionRecordOrderStatus status) {
		try {
			qroService.memo(id, message, status, Security.getUserId());
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}

}
