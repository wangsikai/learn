package com.lanking.uxb.zycon.mall.resource;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.domain.yoo.order.GoodsOrderStatus;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.zycon.mall.api.ZycExcellentPaperManage;
import com.lanking.uxb.zycon.mall.api.ZycResourcesGoodsOrderService;
import com.lanking.uxb.zycon.mall.api.ZycResourcesGoodsService;
import com.lanking.uxb.zycon.mall.convert.ZycExcellentPaperConvert;
import com.lanking.uxb.zycon.mall.form.ExamOrdersQueryForm;
import com.lanking.uxb.zycon.mall.form.ExcellentPaperForm;
import com.lanking.uxb.zycon.mall.form.ResourcesGoodsForm;
import com.lanking.uxb.zycon.mall.value.VZycExam;
import com.lanking.uxb.zycon.mall.value.VZycPageOrdersTotal;
import com.lanking.uxb.zycon.mall.value.VZycTotalOrdersData;

/**
 * 精品试卷管理
 * 
 * @since V2.0.7
 * @author zemin.song
 *
 */
@RestController
@RequestMapping("zyc/goods/ep")
public class ZycExcellentPaperManageController {

	@Autowired
	private ZycExcellentPaperManage zycExcellentPaperManage;
	@Autowired
	private ZycExcellentPaperConvert zycExcellentPaperConvert;
	@Autowired
	private ZycResourcesGoodsService zycResourcesGoodsService;
	@Autowired
	private IndexService indexService;
	@Autowired
	private ZycResourcesGoodsOrderService zycResourcesGoodsOrderService;

	/**
	 * 精品试卷查询
	 * 
	 * @param queryForm
	 * @author zemin.song
	 */
	@RequestMapping(value = "queryExam", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryExam(ExcellentPaperForm queryForm) {
		Page<ExamPaper> page = zycExcellentPaperManage.queryResconExam(queryForm);
		VPage<VZycExam> vpage = new VPage<VZycExam>();
		vpage.setPageSize(queryForm.getPageSize());
		if (page.isEmpty()) {
			// 处理异常
		} else {
			vpage.setItems(zycExcellentPaperConvert.to(page.getItems()));
		}
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(queryForm.getPage());
		return new Value(vpage);
	}

	/**
	 * 上下架
	 * 
	 * @param id
	 *            资源商品Id
	 * @author zemin.song
	 */
	@RequestMapping(value = "doStatus", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doStatus(Long id) {
		ResourcesGoods rg = zycResourcesGoodsService.chengeStatus(id, Security.getUserId());
		indexService.syncUpdate(IndexType.EXAM_PAPER, rg.getResourcesId());
		return new Value(rg);
	}

	/**
	 * 获取试卷商品信息
	 * 
	 * @param
	 * 
	 * @author zemin.song
	 */
	@RequestMapping(value = "getExamsGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getExamsGoods(Long id) {
		ExamPaper ep = zycExcellentPaperManage.get(id);
		return new Value(zycExcellentPaperConvert.to(ep));
	}

	/**
	 * 编辑信息保存/上架
	 * 
	 * @param
	 * 
	 * @author zemin.song
	 */
	@RequestMapping(value = "save", method = { RequestMethod.GET, RequestMethod.POST })
	public Value editExamsGoods(ResourcesGoodsForm form) {
		zycResourcesGoodsService.save(form);
		indexService.syncUpdate(IndexType.EXAM_PAPER, form.getResourcesId());
		return new Value();
	}

	/**
	 * 商品统计
	 * 
	 * @param
	 * 
	 * @author zemin.song
	 */
	@RequestMapping(value = "examOrdersTotalStatis", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryExamOrdersTotalStatis(ExamOrdersQueryForm queryForm) {
		int offset = (queryForm.getPage() - 1) * queryForm.getPageSize();
		int size = queryForm.getPageSize();
		queryForm.setType(ResourcesGoodsType.EXAM_PAPER);
		queryForm.setStatus(GoodsOrderStatus.COMPLETE);
		Page<Map> page = zycResourcesGoodsOrderService.mGetResourcesGoodsOrders(queryForm, P.offset(offset, size));
		VZycPageOrdersTotal vpage = new VZycPageOrdersTotal();
		// 当页码为1的时候查询统计数据
		if (queryForm.getPage() == 1) {
			VZycTotalOrdersData vTotalData = zycResourcesGoodsOrderService.totalOrders(queryForm);
			vpage.setTotalData(vTotalData);
		}
		if (!page.isEmpty()) {
			vpage.setItems(page.getItems());
		}
		vpage.setPageSize(queryForm.getPageSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(queryForm.getPage());
		return new Value(vpage);
	}
}
