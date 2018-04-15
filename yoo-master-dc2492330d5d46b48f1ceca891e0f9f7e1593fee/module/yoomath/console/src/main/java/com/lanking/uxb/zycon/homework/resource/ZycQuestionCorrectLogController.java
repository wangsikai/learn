package com.lanking.uxb.zycon.homework.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.zycon.homework.api.ZycQuestionCorrectLogService;
import com.lanking.uxb.zycon.homework.form.QuestionCorrectLogForm;

/**
 * 后台批改作业接口
 *
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@RestController
@RequestMapping(value = "zyc/correctLog")
public class ZycQuestionCorrectLogController {
	@Autowired
	private ZycQuestionCorrectLogService zycQuestionCorrectLogService;

	/**
	 * 查询现在作业列表(非下发)
	 *
	 * @return Value
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(value = "page", defaultValue = "1") int page,
			@RequestParam(value = "size", defaultValue = "100") int size,
			QuestionCorrectLogForm form) {
		Pageable pageable = P.index(page, size);
		Page<Map> pageValue = zycQuestionCorrectLogService.page(pageable,form);
		
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (pageValue.getTotalCount() + size - 1) / size;
		vp.setPageSize(size);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(pageValue.getTotalCount());
		vp.setItems(pageValue.getItems());
		
		return new Value(vp);
	}

}
