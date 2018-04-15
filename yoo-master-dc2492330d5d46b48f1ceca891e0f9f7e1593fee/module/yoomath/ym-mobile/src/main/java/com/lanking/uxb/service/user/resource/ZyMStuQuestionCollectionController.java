package com.lanking.uxb.service.user.resource;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.collection.QuestionCollection;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCollectionService;
import com.lanking.uxb.service.zuoye.convert.ZyQuestionCollectionConvert2;
import com.lanking.uxb.service.zuoye.form.QuestionQueryForm;
import com.lanking.uxb.service.zuoye.value.VQuestionCollection;

/**
 * 悠数学移动端(学生题目收藏相关接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月17日
 */
@ApiAllowed
@RestController
@RequestMapping("zy/m/s/qc")
public class ZyMStuQuestionCollectionController {

	@Autowired
	private ZyQuestionCollectionService zyQuestionCollectionService;
	@Autowired
	private ZyQuestionCollectionConvert2 zyQuestionCollectConvert;

	/**
	 * 收藏题目
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "collect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value collect(long questionId) {
		zyQuestionCollectionService.collect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 取消收藏题目
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "cancelCollect", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cancelCollect(long questionId) {
		zyQuestionCollectionService.cancelCollect(questionId, Security.getUserId());
		return new Value();
	}

	/**
	 * 查询收藏列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param keyword
	 *            关键字
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.POST, RequestMethod.GET })
	public Value query(String keyword, long cursor, @RequestParam(value = "size", defaultValue = "20") int size) {
		VCursorPage<VQuestionCollection> vp = new VCursorPage<VQuestionCollection>();
		QuestionQueryForm query = new QuestionQueryForm();
		query.setUserId(Security.getUserId());
		query.setKey(keyword);
		CursorPage<Long, QuestionCollection> collectPage = zyQuestionCollectionService.queryCollection(query,
				CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, Math.min(size, 20)));
		if (collectPage.isEmpty()) {
			vp.setCursor(cursor);
			vp.setItems(Collections.EMPTY_LIST);
		} else {
			vp.setCursor(collectPage.getNextCursor());
			vp.setItems(zyQuestionCollectConvert.to(collectPage.getItems()));
		}
		return new Value(vp);
	}
}
