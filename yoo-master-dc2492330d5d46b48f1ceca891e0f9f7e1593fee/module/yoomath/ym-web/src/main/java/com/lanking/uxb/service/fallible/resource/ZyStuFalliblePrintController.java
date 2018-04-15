package com.lanking.uxb.service.fallible.resource;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrder;
import com.lanking.cloud.domain.yoo.order.fallible.FallibleQuestionPrintOrderStatus;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.fallible.api.ZyStuFalliblePrintService;
import com.lanking.uxb.service.fallible.form.ZyStuFalliblePrintOrderForm;
import com.lanking.uxb.service.payment.cache.CallbackOrderCache;
import com.lanking.uxb.service.payment.cache.OrderBusinessSource;
import com.lanking.uxb.service.payment.weixin.api.WXPaymentService;
import com.lanking.uxb.service.payment.weixin.response.OrderQueryResult;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.web.resource.ZyStuFallibleQuestionController;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleExportRecordService;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionService;

/**
 * 学生错题待打印服务.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年10月8日
 */

@ApiAllowed
@RestController
@RequestMapping("zy/s/fallibleprint")
@RolesAllowed(userTypes = { "STUDENT" })
public class ZyStuFalliblePrintController extends AbstractStuFalliblePrintPaymentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private ZyStuFalliblePrintService stuFalliblePrintService;
	@Autowired
	private ZyStudentFallibleQuestionService sfqService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private WXPaymentService wxPaymentService;
	@Autowired
	private ZyStuFallibleQuestionController stuFallibleQuestionController;
	@Autowired
	private ZyStudentFallibleExportRecordService studentFallibleExportRecordService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private CallbackOrderCache callbackOrderCache;

	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

	/**
	 * 创建错题打印订单.
	 * 
	 * @param form
	 *            订单参数
	 * @return
	 */
	@MemberAllowed
	@RequestMapping("createOrader")
	public Value createOrader(ZyStuFalliblePrintOrderForm form) {
		long userID = Security.getUserId();

		// 创建订单
		try {
			// 首先确认题目数量，计算价格
			Date dateScope = null;
			if (form.getTimeScope() != 0) {
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
				Calendar cal = Calendar.getInstance();
				try {
					cal.setTime(format.parse(format.format(new Date())));
					if (form.getTimeScope() == 1) {
						cal.add(Calendar.DAY_OF_YEAR, -30);
					} else if (form.getTimeScope() == 3) {
						cal.add(Calendar.DAY_OF_YEAR, -90);
					} else if (form.getTimeScope() == 6) {
						cal.add(Calendar.DAY_OF_YEAR, -180);
					}
					dateScope = cal.getTime();
				} catch (ParseException e) {
					logger.error("previewDownload format date error ", e);
				}
			}

			List<Integer> qTypes = new ArrayList<Integer>();
			if (form.getTypes() != null) {
				for (Question.Type type : form.getTypes()) {
					qTypes.add(type.getValue());
				}
			}

			// 附加数据
			JSONObject jo = new JSONObject();
			JSONArray sectionCodes = new JSONArray();
			sectionCodes.addAll(form.getSectionCodes());
			jo.put("sectionCodes", sectionCodes);
			jo.put("timeScope", form.getTimeScope());
			jo.put("questionTypes", qTypes);
			jo.put("errorTimes", form.getErrorTimes());
			String attachData = jo.toJSONString();

			MemberType memberType = SecurityContext.getMemberType();
			long count = sfqService.getStudentExportCount(Security.getUserId(), form.getSectionCodes(), dateScope,
					qTypes, form.getErrorTimes());
			BigDecimal totalPrice = null;
			BigDecimal price100 = null;
			BigDecimal singlePrice = null;

			if (memberType == MemberType.NONE) {
				// 非会员
				price100 = new BigDecimal(Env.getDynamicString("stu-fallprint-price100"));
				singlePrice = new BigDecimal(Env.getDynamicString("stu-fallprint-singleprice"));
			} else {
				// 会员
				price100 = new BigDecimal(Env.getDynamicString("stu-fallprint-price100-vip"));
				singlePrice = new BigDecimal(Env.getDynamicString("stu-fallprint-singleprice-vip"));
			}
			if (count <= 100) {
				totalPrice = price100;
			} else {
				totalPrice = new BigDecimal(price100.doubleValue() + (count - 100) * singlePrice.doubleValue()); // 总价
			}

			// TODO 测试使用
			// totalPrice = new BigDecimal(0.01);

			FallibleQuestionPrintOrder fallibleQuestionPrintOrder = stuFalliblePrintService.createOrder(userID,
					form.getPayMod(), form.getPaymentPlatformCode(), totalPrice, attachData, form.getContactName(),
					form.getContactPhone(), form.getDistrictCode(), form.getContactAddress());
			Map<String, Object> map = new HashMap<String, Object>(1);
			map.put("orderID", fallibleQuestionPrintOrder.getId());
			return new Value(map);
		} catch (AbstractException e) {
			return new Value(e);
		}
	}

	/**
	 * 查询微信订单状态.
	 * 
	 * @param orderID
	 *            代打印订单ID
	 * @return
	 */
	@RequestMapping("queryWXOrder")
	public Value queryWXOrder(final Long orderID, final String host) {
		if (null == orderID) {
			return new Value(new MissingArgumentException());
		}

		try {
			final SimpleDateFormat formate = new SimpleDateFormat("yyyyMMddHHmmss");
			String appid = Env.getString("weixin.pay.appid");
			OrderQueryResult result = wxPaymentService.orderQuery(appid, null, String.valueOf(orderID));
			final String return_code = result.getReturnCode(); // 返回状态
			String trade_state = result.getTradeState(); // 交易状态
			final String outTradeNo = result.getOutTradeNo(); // 本地订单ID
			final String timeEnd = result.getTimeEnd(); // 交易付款时间
			final String transactionId = result.getTransactionId(); // 微信支付订单号

			FallibleQuestionPrintOrder order = stuFalliblePrintService.get(orderID);
			if ("SUCCESS".equalsIgnoreCase(return_code) && ("SUCCESS".equalsIgnoreCase(trade_state))) {
				// 交易成功

				// 判断订单是否已被处理中
				if (callbackOrderCache.hasProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID)) {
					return new Value(0);
				} else {
					callbackOrderCache.setPayOrderProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID);
				}
				logger.info("[错题代印] 微信购买, orderID = " + orderID);

				if (order.getStatus() != FallibleQuestionPrintOrderStatus.PAY) {
					fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							Date payTime = null;
							try {
								payTime = formate.parse(timeEnd);
							} catch (ParseException e) {
								payTime = new Date();
								logger.error(e.getMessage(), e);
							}

							// 支付订单
							FallibleQuestionPrintOrder order = stuFalliblePrintService.updatePaymentCallback(
									Long.parseLong(outTradeNo), transactionId, null, payTime);

							// 生成错题文档
							String attachData = order.getAttachData();
							JSONObject jo = JSONObject.parseObject(attachData);
							JSONArray scArray = jo.getJSONArray("sectionCodes");
							List<Long> sectionCodes = new ArrayList<Long>(scArray.size());
							for (int i = 0; i < scArray.size(); i++) {
								sectionCodes.add(scArray.getLong(i));
							}

							int timeScope = jo.getIntValue("timeScope");
							JSONArray qtArray = jo.getJSONArray("questionTypes");
							List<Question.Type> qTypes = new ArrayList<Question.Type>();
							for (int i = 0; i < qtArray.size(); i++) {
								qTypes.add(Question.Type.findByValue(qtArray.getIntValue(i)));
							}
							int errorTimes = jo.getIntValue("errorTimes");

							stuFallibleQuestionController.createExportDoc(timeScope, qTypes, errorTimes, sectionCodes,
									host, order.getId());
						}
					});
				}

				return new Value(1);
			} else if ("REVOKED".equalsIgnoreCase(trade_state) || "PAYERROR".equalsIgnoreCase(trade_state)
					|| "CLOSED".equalsIgnoreCase(trade_state)) {
				// 交易失败
				if (order.getStatus() != FallibleQuestionPrintOrderStatus.FAIL) {
					fixedThreadPool.execute(new Runnable() {
						@Override
						public void run() {
							stuFalliblePrintService.updateOrderStatus(Long.parseLong(outTradeNo), null,
									FallibleQuestionPrintOrderStatus.FAIL);
						}
					});
				}
			} else {
				return new Value(2);
			}
		} catch (Exception e) {
			logger.error("查询微信订单状态出错！", e);
			// 订单处理完成
			callbackOrderCache.invalidPayOrderProcessing(OrderBusinessSource.STU_FALL_PRINT, orderID);
			return new Value(new ServerException(e));
		}
		return new Value(0);
	}

	/**
	 * 获取代打印服务价格设置.
	 * 
	 * @return
	 */
	@RequestMapping("getPrintPriceSet")
	public Value getPrintPriceSet() {
		Map<String, Object> map = new HashMap<String, Object>(4);
		map.put("price100", Double.parseDouble(Env.getDynamicString("stu-fallprint-price100")));
		map.put("singleprice", Double.parseDouble(Env.getDynamicString("stu-fallprint-singleprice")));
		map.put("price100_vip", Double.parseDouble(Env.getDynamicString("stu-fallprint-price100-vip")));
		map.put("singleprice_vip", Double.parseDouble(Env.getDynamicString("stu-fallprint-singleprice-vip")));
		return new Value(map);
	}

	/**
	 * 获得代打印订单.
	 * 
	 * @param orderID
	 *            订单ID.
	 * 
	 * @return
	 */
	@RequestMapping("geyPrintOrder")
	public Value geyPrintOrder(Long orderID) {
		if (orderID == null) {
			return new Value(new MissingArgumentException());
		}

		FallibleQuestionPrintOrder order = stuFalliblePrintService.get(orderID);
		if (order == null) {
			return new Value(new EntityNotFoundException());
		}
		if (order.getUserId() != Security.getUserId()) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("order", order);

		// 处理省市区
		List<District> districts = new ArrayList<District>(3);
		District district1 = districtService.getDistrict(order.getDistrictCode());
		districts.add(district1);
		if (district1.getLevel() > 1) {
			District district2 = districtService.getDistrict(district1.getPcode());
			districts.add(district2);
			if (district2.getLevel() > 1) {
				District district3 = districtService.getDistrict(district2.getPcode());
				districts.add(district3);
			}
		}
		map.put("districts", districts);

		if (StringUtils.isNotBlank(order.getAttachData())) {
			JSONObject jo = JSONObject.parseObject(order.getAttachData());

			JSONArray qtArray = jo.getJSONArray("questionTypes");
			List<Question.Type> questionTypes = new ArrayList<Question.Type>(qtArray.size());
			for (int i = 0; i < qtArray.size(); i++) {
				questionTypes.add(Question.Type.findByValue(qtArray.getIntValue(i)));
			}
			JSONArray scArray = jo.getJSONArray("sectionCodes");
			List<Long> sectionCodes = new ArrayList<Long>(qtArray.size());
			for (int i = 0; i < scArray.size(); i++) {
				sectionCodes.add(scArray.getLongValue(i));
			}
			map.put("errorTimes", jo.getIntValue("errorTimes"));
			map.put("questionTypes", questionTypes);
			map.put("sectionCodes", sectionCodes);
			map.put("timeScope", jo.getIntValue("timeScope"));
		}

		return new Value(map);
	}

	/**
	 * 获得上一次的代打印订单.
	 * 
	 * @param orderID
	 *            订单ID.
	 * 
	 * @return
	 */
	@RequestMapping("geyLastPrintOrder")
	public Value geyLastPrintOrder() {
		FallibleQuestionPrintOrder order = stuFalliblePrintService.getLast(Security.getUserId());

		Map<String, Object> map = new HashMap<String, Object>(6);
		map.put("order", order);

		// 处理省市区
		List<District> districts = new ArrayList<District>(3);
		District district1 = districtService.getDistrict(order.getDistrictCode());
		districts.add(district1);
		if (district1.getLevel() > 1) {
			District district2 = districtService.getDistrict(district1.getPcode());
			districts.add(district2);
			if (district2.getLevel() > 1) {
				District district3 = districtService.getDistrict(district2.getPcode());
				districts.add(district3);
			}
		}
		map.put("districts", districts);

		if (StringUtils.isNotBlank(order.getAttachData())) {
			JSONObject jo = JSONObject.parseObject(order.getAttachData());

			JSONArray qtArray = jo.getJSONArray("questionTypes");
			List<Question.Type> questionTypes = new ArrayList<Question.Type>(qtArray.size());
			for (int i = 0; i < qtArray.size(); i++) {
				questionTypes.add(Question.Type.findByValue(qtArray.getIntValue(i)));
			}
			JSONArray scArray = jo.getJSONArray("sectionCodes");
			List<Long> sectionCodes = new ArrayList<Long>(qtArray.size());
			for (int i = 0; i < scArray.size(); i++) {
				sectionCodes.add(scArray.getLongValue(i));
			}
			map.put("errorTimes", jo.getIntValue("errorTimes"));
			map.put("questionTypes", questionTypes);
			map.put("sectionCodes", sectionCodes);
			map.put("timeScope", jo.getIntValue("timeScope"));
		}

		return new Value(map);
	}
}
