package com.lanking.uxb.zycon.wx.resource;

import httl.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.thirdparty.WXCallbackMessage;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.thirdparty.api.WXService;
import com.lanking.uxb.service.thirdparty.weixin.response.WXArticle;
import com.lanking.uxb.service.thirdparty.weixin.response.WXButton;
import com.lanking.uxb.service.thirdparty.weixin.response.WXMenus;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplate;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateContent;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplateItem;
import com.lanking.uxb.service.thirdparty.weixin.response.WXTemplates;
import com.lanking.uxb.zycon.wx.api.ZycWXService;
import com.lanking.uxb.zycon.wx.convert.ZycWXButtonConvert;
import com.lanking.uxb.zycon.wx.value.RelationType;
import com.lanking.uxb.zycon.wx.value.VWXButton;

/**
 * 微信设置.
 * 
 * @author wlche
 * @since server v2.2.0
 *
 */
@RestController
@RequestMapping(value = "zyc/wx")
public class ZycWXController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private WXService wxService;
	@Autowired
	private ZycWXService zyWXService;
	@Autowired
	private ZycWXButtonConvert wxButtonConvert;

	/**
	 * 获得微信菜单.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getMenus")
	public Value getMenus() {
		WXMenus menus = wxService.getMenus(Product.YOOMATH);
		if (menus == null) {
			return new Value(new EntityNotFoundException());
		} else if (StringUtils.isNotBlank(menus.getErrcode())) {
			logger.error("[WX-getMenus] code = {}, msg = {}", menus.getErrcode(), menus.getErrmsg());
			return new Value(new EntityNotFoundException());
		}

		// 微信菜单集合.
		List<WXButton> buttons = menus.getMenu().getButton();
		List<VWXButton> vButtons = wxButtonConvert.to(buttons);

		// 处理判断菜单关联内容类型、获取客服消息内容等
		WXCallbackMessage welcomeMsg = null; // 关注消息
		List<WXCallbackMessage> btRelations = zyWXService.listWXCallbackMessages(Product.YOOMATH);
		Map<String, WXCallbackMessage> temp = new HashMap<String, WXCallbackMessage>(btRelations.size());
		if (btRelations.size() > 0) {
			for (WXCallbackMessage r : btRelations) {
				if (r.getMessageType() != null && r.getMessageType().equals("event")
						&& r.getEvent().equals("subscribe")) {
					welcomeMsg = r;
				} else {
					temp.put(r.getKey(), r);
				}
			}
		}
		for (int i = 0; i < vButtons.size(); i++) {
			VWXButton vbt1 = vButtons.get(i);
			if (vbt1.getSubButtons() != null && vbt1.getSubButtons().size() > 0) {
				vbt1.setRelationType(RelationType.DEFUALT);
				for (int j = 0; j < vbt1.getSubButtons().size(); j++) {
					VWXButton vbt2 = vbt1.getSubButtons().get(j);
					WXCallbackMessage callmessage = temp.get(vbt2.getKey());
					if (vbt2.getType().toLowerCase().endsWith("click") && callmessage != null) {
						if (StringUtils.isNotBlank(callmessage.getMediaId())) {
							// 图文消息
							vbt2.setMediaId(temp.get(vbt2.getKey()).getMediaId());
							vbt2.setType("view_limited");
						} else {
							// 客服消息
							vbt2.setMessage(temp.get(vbt2.getKey()).getMessage());
						}
					}
					this.fillRelationType(vbt2);
				}
			} else {
				WXCallbackMessage callmessage = temp.get(vbt1.getKey());
				if (vbt1.getType().toLowerCase().endsWith("click") && callmessage != null) {
					if (StringUtils.isNotBlank(callmessage.getMediaId())) {
						// 图文消息
						vbt1.setMediaId(temp.get(vbt1.getKey()).getMediaId());
						vbt1.setType("view_limited");
					} else {
						// 客服消息
						vbt1.setMessage(temp.get(vbt1.getKey()).getMessage());
					}
				}
				this.fillRelationType(vbt1);
			}
		}

		Map<String, Object> datas = new HashMap<String, Object>(4);
		datas.put("welcomeMsg", welcomeMsg);
		datas.put("menus", vButtons);
		datas.put("remindUrl", Env.getString("weixin.page.student.homeworkRemind"));
		datas.put("reportUrl", Env.getString("weixin.page.student.report"));
		return new Value(datas);
	}

	/**
	 * 查询图文素材.
	 * 
	 * @param page
	 *            当前页码
	 * @param pageSize
	 *            每页显示条数
	 * @return
	 */
	@RequestMapping(value = "queryNews")
	public Value queryNewsTemplate(Integer page, Integer pageSize) {
		page = page == null ? 1 : page;
		pageSize = pageSize == null ? 20 : pageSize;
		WXTemplates wxTemplates = wxService.getTemplates(Product.YOOMATH, "news", (page - 1) * pageSize, pageSize);
		if (StringUtils.isNotBlank(wxTemplates.getErrcode())) {
			logger.error("[WX-queryNewsTemplate] errcode = " + wxTemplates.getErrcode() + ", errmsg = "
					+ wxTemplates.getErrmsg());
			return new Value(new ServerException());
		}

		VPage<WXTemplate> vp = new VPage<WXTemplate>();
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		int totalPage = (int) ((wxTemplates.getTotal_count() + pageSize - 1) / pageSize);
		vp.setTotalPage(totalPage);
		vp.setTotal(wxTemplates.getTotal_count());
		vp.setItems(wxTemplates.getItem());
		return new Value(vp);
	}

	/**
	 * 获得图片.
	 * 
	 * @param mediaId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "getPic")
	public void getPic(String mediaId, HttpServletRequest request, HttpServletResponse response) {
		wxService.getStreamTemplate(Product.YOOMATH, mediaId, request, response);
	}

	/**
	 * 保存菜单设置.
	 * 
	 * @param json
	 *            菜单设置JSON字串
	 * @param welcomeMessage
	 *            欢迎用于
	 * @return
	 */
	@RequestMapping(value = "saveMenus")
	public Value saveMenus(String json, String welcomeMessage) {
		List<VWXButton> buttons = JSONArray.parseArray(json, VWXButton.class);
		List<WXCallbackMessage> btRelations = new ArrayList<WXCallbackMessage>();

		// 处理关注欢迎语
		if (StringUtils.isNotBlank(welcomeMessage)) {
			WXCallbackMessage welmsg = new WXCallbackMessage();
			welmsg.setEvent("subscribe");
			welmsg.setKey("subscribe");
			welmsg.setMessage(welcomeMessage);
			welmsg.setMessageType("event");
			welmsg.setProduct(Product.YOOMATH);
			btRelations.add(welmsg);
		}

		// 过滤处理菜单
		List<WXButton> wxbts = new ArrayList<WXButton>(buttons.size());
		for (int i = 0; i < buttons.size(); i++) {
			VWXButton button1 = buttons.get(i);
			WXButton bt1 = new WXButton();
			bt1.setName(button1.getName());
			if (button1.getSubButtons() != null && button1.getSubButtons().size() > 0) {
				bt1.setSub_button(new ArrayList<WXButton>(button1.getSubButtons().size()));
				for (int j = 0; j < button1.getSubButtons().size(); j++) {
					VWXButton button2 = button1.getSubButtons().get(j);
					WXButton bt2 = new WXButton();
					bt2.setName(button2.getName());
					if (button2.getRelationType() == RelationType.CUSTOM_MESSAGE) {
						bt2.setType("click");
						WXCallbackMessage ralation = new WXCallbackMessage();
						ralation.setKey("YOOMATH_MSG_" + i + "_" + j);
						ralation.setMessage(button2.getMessage());
						ralation.setMessageType("event");
						ralation.setEvent("click");
						btRelations.add(ralation);
						bt2.setKey("YOOMATH_MSG_" + i + "_" + j);
					} else if (button2.getRelationType() == RelationType.VIEW) {
						bt2.setType("view");
						bt2.setUrl(button2.getUrl());
					} else if (button2.getRelationType() == RelationType.NEWS) {
						bt2.setType("click");
						WXCallbackMessage ralation = new WXCallbackMessage();
						ralation.setKey("YOOMATH_MSG_" + i + "_" + j);
						ralation.setMessage(button2.getMessage());
						ralation.setMessageType("event");
						ralation.setEvent("click");
						ralation.setMediaId(button2.getMediaId());

						List<WXArticle> articles = this.initArticles(button2.getWxTemplateContent());
						if (articles.size() > 1) {
							// 多图文不需要存储描述
							for (WXArticle wxArticle : articles) {
								wxArticle.setDescription("");
							}
						}
						ralation.setMessage(JSON.toJSONString(articles));
						btRelations.add(ralation);
						bt2.setKey("YOOMATH_MSG_" + i + "_" + j);
					}
					bt1.getSub_button().add(bt2);
				}
			} else {
				if (button1.getRelationType() == RelationType.CUSTOM_MESSAGE) {
					bt1.setType("click");
					WXCallbackMessage ralation = new WXCallbackMessage();
					ralation.setKey("YOOMATH_MSG_" + i);
					ralation.setMessage(button1.getMessage());
					ralation.setMessageType("event");
					ralation.setEvent("click");
					btRelations.add(ralation);
					bt1.setKey("YOOMATH_MSG_" + i);
				} else if (button1.getRelationType() == RelationType.VIEW) {
					bt1.setType("view");
					bt1.setUrl(button1.getUrl());
				} else if (button1.getRelationType() == RelationType.NEWS) {
					bt1.setType("click");
					WXCallbackMessage ralation = new WXCallbackMessage();
					ralation.setKey("YOOMATH_MSG_" + i);
					ralation.setMessage(button1.getMessage());
					ralation.setMessageType("event");
					ralation.setEvent("click");
					ralation.setMediaId(button1.getMediaId());

					List<WXArticle> articles = this.initArticles(button1.getWxTemplateContent());
					if (articles.size() > 1) {
						// 多图文不需要存储描述
						for (WXArticle wxArticle : articles) {
							wxArticle.setDescription("");
						}
					}
					ralation.setMessage(JSON.toJSONString(articles));
					btRelations.add(ralation);
					bt1.setKey("YOOMATH_MSG_" + i);
				}
			}
			wxbts.add(bt1);
		}

		// 提交微信菜单
		boolean success = wxService.saveMenus(Product.YOOMATH, wxbts);
		if (success) {
			// 保存本地菜单关系
			zyWXService.cleanAndSaveWXCallbackMessages(Product.YOOMATH, btRelations);
		}
		return new Value(success);
	}

	/**
	 * 拼装图文消息存储内容.
	 * 
	 * @param wxTemplateContent
	 *            图文消息.
	 * @return
	 */
	private List<WXArticle> initArticles(WXTemplateContent wxTemplateContent) {
		if (null != wxTemplateContent) {
			List<WXArticle> articles = new ArrayList<WXArticle>(wxTemplateContent.getNews_item().size());
			for (WXTemplateItem wxTemplateItem : wxTemplateContent.getNews_item()) {
				WXArticle article = new WXArticle();
				article.setTitle(wxTemplateItem.getTitle());
				article.setDescription(wxTemplateItem.getDigest());
				article.setPicUrl(wxTemplateItem.getThumb_url());
				article.setUrl(wxTemplateItem.getUrl());
				articles.add(article);
			}
			return articles;
		}

		return Lists.newArrayList();
	}

	/**
	 * 处理RelationType等.<br/>
	 * 当前仅支持图文消息关联菜单！
	 * 
	 * @param vbt
	 * @param r
	 */
	private void fillRelationType(VWXButton vbt) {
		if (vbt.getType().equals("click")) {
			// 客服
			vbt.setRelationType(RelationType.CUSTOM_MESSAGE);
		} else if (vbt.getType().equals("view")) {
			// 跳转
			vbt.setRelationType(RelationType.VIEW);
		} else if (vbt.getType().equals("view_limited")) {
			// 图文
			vbt.setRelationType(RelationType.NEWS);

			// 根据media_id 获取图文内容
			WXTemplateContent wxTemplateContent = wxService.getNewsTemplate(Product.YOOMATH, vbt.getMediaId());
			vbt.setWxTemplateContent(wxTemplateContent);
		} else {
			vbt.setRelationType(RelationType.DEFUALT);
		}
	}
}
