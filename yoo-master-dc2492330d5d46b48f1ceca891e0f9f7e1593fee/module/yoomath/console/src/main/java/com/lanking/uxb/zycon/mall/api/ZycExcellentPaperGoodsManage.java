package com.lanking.uxb.zycon.mall.api;

import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.uxb.zycon.mall.form.ExamOrdersQueryForm;

/**
 * 提供试卷相关接口
 * 
 * @since 2.0.7
 * @author zemin.song
 * @version 2016年9月11日 20:04:47
 */
public interface ZycExcellentPaperGoodsManage {

	/**
	 * 试卷商品查询接口
	 * 
	 * @param queryForm
	 *            查询form
	 * @return
	 */
	Page<ResourcesGoods> queryResconExamGoods(ExamOrdersQueryForm queryForm, Pageable pageable);

}
