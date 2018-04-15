package com.lanking.uxb.service.mall.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsFavorite;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsType;
import com.lanking.cloud.domain.yoo.order.resources.ResourcesGoodsOrder;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;
import com.lanking.uxb.service.mall.api.ResourcesGoodsFavoriteService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsOrderService;
import com.lanking.uxb.service.mall.api.ResourcesGoodsService;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvert;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsConvertOption;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsFavoriteConvert;
import com.lanking.uxb.service.mall.convert.ExamPaperGoodsOrderConvert;
import com.lanking.uxb.service.mall.convert.ResourcesGoodsConvert;
import com.lanking.uxb.service.mall.value.VExamPaperGoods;
import com.lanking.uxb.service.mall.value.VExamPaperGoodsFavorite;
import com.lanking.uxb.service.mall.value.VExamPaperGoodsOrder;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 精品组卷服务
 * 
 * @author zemin.song
 *
 * @version 2016年9月1日
 */
@RestController
@RequestMapping(value = "zy/mall/ep")
public class ZyTeaExcellentExampaperController {
	@Autowired
	private UserService userService;
	@Autowired
	private ExamPaperService examPaperService;
	@Autowired
	private ExamPaperGoodsConvert examPaperGoodsConvert;
	@Autowired
	private ResourcesGoodsOrderService resourcesGoodsOrderService;
	@Autowired
	private ExamPaperGoodsOrderConvert examPaperOrderConvert;
	@Autowired
	private ResourcesGoodsFavoriteService resourcesGoodsFavoriteService;
	@Autowired
	private ExamPaperGoodsFavoriteConvert examPaperFavoriteConvert;
	@Autowired
	private ResourcesGoodsService resourcesGoodsService;
	@Autowired
	private ResourcesGoodsConvert resourcesGoodsConvert;

	/**
	 * 精品组卷列表List
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryExcellentExampaper", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryCollectExampaper(ExamQueryForm form) {
		if (form.getPage() == null || form.getPageSize() == null || form.getOrderBy() == null) {
			return new Value(new IllegalArgException());
		}
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		form.setPhaseCode(teacher.getPhaseCode());
		form.setSubjectCode(teacher.getSubjectCode());
		form.setStatus(ResourcesGoodsStatus.PUBLISH);
		Page<ExamPaper> page = examPaperService.queryExam(form);
		VPage<VExamPaperGoods> retPage = new VPage<VExamPaperGoods>();
		retPage.setCurrentPage(form.getPage());
		retPage.setPageSize(form.getPageSize());
		retPage.setTotal(page.getTotalCount());
		retPage.setTotalPage(page.getPageCount());
		ExamPaperGoodsConvertOption opn = new ExamPaperGoodsConvertOption();
		opn.setInitGoodsInfo(true);
		opn.setInitCollect(true);
		List<VExamPaperGoods> vs = examPaperGoodsConvert.to(page.getItems(), opn);
		retPage.setItems(vs);
		return new Value(retPage);
	}

	/**
	 * 加入收藏
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "addFavorite", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addFavorite(Long goodsId) {
		Long createId = Security.getUserId();
		resourcesGoodsFavoriteService.addFavorite(createId, goodsId);
		return new Value();
	}

	/**
	 * 取消收藏
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "removeFavorite", method = { RequestMethod.GET, RequestMethod.POST })
	public Value removeFavorite(Long id) {
		Long createId = Security.getUserId();
		int ret = resourcesGoodsFavoriteService.removeFavorite(createId, id);
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new NoPermissionException());
		}
	}

	/**
	 * 收藏列表
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryFavorite", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryFavorite(ExamQueryForm form) {
		int offset = (form.getPage() - 1) * form.getPageSize();
		int size = form.getPageSize();
		Long createId = Security.getUserId();
		form.setCreateId(createId);
		form.setStatus(ResourcesGoodsStatus.PUBLISH);
		Page<ResourcesGoodsFavorite> page = resourcesGoodsFavoriteService.queryExamPaperFavorite(form,
				P.offset(offset, size));
		VPage<VExamPaperGoodsFavorite> retPage = new VPage<VExamPaperGoodsFavorite>();
		retPage.setCurrentPage(form.getPage());
		retPage.setPageSize(form.getPageSize());
		retPage.setTotal(page.getTotalCount());
		retPage.setTotalPage(page.getPageCount());
		retPage.setItems(examPaperFavoriteConvert.to(page.getItems()));
		return new Value(retPage);
	}

	/**
	 * 查询已完成订单
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getOrders", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getOrders(int page, int size) {
		int offset = (page - 1) * size;
		Long userId = Security.getUserId();
		Page<ResourcesGoodsOrder> orders = resourcesGoodsOrderService.getOrdersByUser(userId,
				ResourcesGoodsType.EXAM_PAPER.getValue(), P.offset(offset, size));
		// oder转化
		VPage<VExamPaperGoodsOrder> vpage = new VPage<VExamPaperGoodsOrder>();
		vpage.setCurrentPage(page);
		vpage.setPageSize(size);
		vpage.setTotal(orders.getTotalCount());
		vpage.setTotalPage(orders.getPageCount());
		vpage.setItems(examPaperOrderConvert.to(orders.getItems()));
		return new Value(vpage);
	}

	/**
	 * 用户删除订单
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "delOrders", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delOrders(Long id) {
		Long createId = Security.getUserId();
		return resourcesGoodsOrderService.delOrders(createId, id);
	}

	/**
	 * 通过商品ID查询商品信息
	 * 
	 * @return
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "getResourcesGoods", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getResourcesGoods(Long id) {
		return new Value(resourcesGoodsConvert.to(resourcesGoodsService.get(id)));
	}
}
