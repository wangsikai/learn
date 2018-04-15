package com.lanking.uxb.rescon.teach.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistCatalogManage;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistCatalogConvert;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForms;
import com.lanking.uxb.rescon.teach.value.VTeachAssistCatalog;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 教辅目录.
 * 
 * @author wlche
 * @since v1.3
 *
 */
@RestController
@RequestMapping(value = "/rescon/teachAssist/catalog")
public class ResconTeachAssistCatalogController {
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconTeachAssistCatalogManage teachAssistCatalogManage;
	@Autowired
	private ResconTeachAssistCatalogConvert teachAssistCatalogConvert;

	/**
	 * 新增目录.
	 * 
	 * @param catalogForm
	 *            目录表单
	 * @return
	 */
	@RequestMapping(value = "add", method = RequestMethod.POST)
	public Value addCatalog(TeachAssistCatalogForm catalogForm) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		VTeachAssistCatalog vo = teachAssistCatalogManage.addCatalog(catalogForm, user.getId());

		List<TeachAssistCatalog> catalogs = teachAssistCatalogManage.listTeachAssistCatalogCatalog(catalogForm
				.getTeachassistVersionId());
		List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert.assemblyCatalogTree(teachAssistCatalogConvert
				.to(catalogs));

		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("catalogs", vcatalogs);
		map.put("catalog", vo);
		return new Value(map);
	}

	/**
	 * 批量统一保存目录结构.
	 * 
	 * @param catalogForms
	 *            目录集合
	 * @param deleteCatalogs
	 *            删除的目录ID集合
	 * @return
	 */
	@RequestMapping(value = "batchSaveCatalogs", method = RequestMethod.POST)
	public Value batchSaveCatalogs(String json) {
		if (StringUtils.isBlank(json)) {
			return new Value(new MissingArgumentException());
		}
		TeachAssistCatalogForms catalogForms = com.alibaba.fastjson.JSON.parseObject(json,
				TeachAssistCatalogForms.class);
		if (catalogForms == null || catalogForms.getForms() == null || catalogForms.getForms().size() == 0) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			teachAssistCatalogManage.batchSaveCatalogs(catalogForms, user.getId());
			return new Value();
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 获取教辅版本目录.
	 * 
	 * @param teachassistVersionId
	 * @return
	 */
	@RequestMapping(value = "getCatalogs", method = RequestMethod.POST)
	public Value getCatalogs(Long teachassistVersionId) {
		if (teachassistVersionId == null) {
			return new Value(new MissingArgumentException());
		}
		List<TeachAssistCatalog> catalogs = teachAssistCatalogManage
				.listTeachAssistCatalogCatalog(teachassistVersionId);
		List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert.assemblyCatalogTree(teachAssistCatalogConvert
				.to(catalogs));
		return new Value(vcatalogs);
	}

	/**
	 * 更新目录名称.
	 * 
	 * @param id
	 *            目录ID
	 * @param name
	 *            目录名称
	 * @return
	 */
	@RequestMapping(value = "update_name", method = RequestMethod.POST)
	public Value updateName(Long id, String name) {
		int length = StringUtils.getJsUnicodeLength(name);
		if (length == 0 || length > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.CATALOG_NAME_TOO_LONG_TEACHASSIST));
		}

		try {
			TeachAssistCatalog teachAssistCatalog = teachAssistCatalogManage.updateName(id, name, Security.getUserId());
			return new Value(teachAssistCatalogConvert.to(teachAssistCatalog));
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 上移.
	 * 
	 * @param teachAssistVersionId
	 *            版本ID
	 * @param catalogId
	 *            目录节点
	 * @return
	 */
	@RequestMapping(value = "up", method = { RequestMethod.POST, RequestMethod.GET })
	public Value up(Long teachassistVersionId, Long catalogId) {
		if (teachassistVersionId == null || catalogId == null || catalogId <= 0) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			teachAssistCatalogManage.up(catalogId, user.getId());

			List<TeachAssistCatalog> catalogs = teachAssistCatalogManage
					.listTeachAssistCatalogCatalog(teachassistVersionId);
			List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert
					.assemblyCatalogTree(teachAssistCatalogConvert.to(catalogs));

			return new Value(vcatalogs);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 下移.
	 * 
	 * @param teachAssistVersionId
	 *            版本ID
	 * @param catalogId
	 *            目录节点
	 * @return
	 */
	@RequestMapping(value = "down", method = { RequestMethod.POST, RequestMethod.GET })
	public Value down(Long teachassistVersionId, Long catalogId) {
		if (catalogId == null || catalogId <= 0) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			teachAssistCatalogManage.down(catalogId, user.getId());

			List<TeachAssistCatalog> catalogs = teachAssistCatalogManage
					.listTeachAssistCatalogCatalog(teachassistVersionId);
			List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert
					.assemblyCatalogTree(teachAssistCatalogConvert.to(catalogs));

			return new Value(vcatalogs);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 降级.
	 * 
	 * @param teachAssistVersionId
	 *            版本ID
	 * @param catalogId
	 *            目录节点
	 * @return
	 */
	@RequestMapping(value = "down_level", method = { RequestMethod.POST, RequestMethod.GET })
	public Value downLevel(Long teachassistVersionId, Long catalogId) {
		if (catalogId == null || catalogId <= 0) {
			return new Value(new MissingArgumentException());
		}
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			teachAssistCatalogManage.downLevel(catalogId, user.getId());

			List<TeachAssistCatalog> catalogs = teachAssistCatalogManage
					.listTeachAssistCatalogCatalog(teachassistVersionId);
			List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert
					.assemblyCatalogTree(teachAssistCatalogConvert.to(catalogs));

			return new Value(vcatalogs);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 删除目录.
	 * 
	 * @param catalogId
	 * @return
	 */
	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(Long teachassistVersionId, @RequestParam(value = "catalogIds") List<Long> catalogIds,
			Boolean confirm) {
		if (catalogIds == null || catalogIds.size() == 0) {
			return new Value(new MissingArgumentException());
		}
		try {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			teachAssistCatalogManage.delete(catalogIds, confirm, user.getId());
			List<TeachAssistCatalog> catalogs = teachAssistCatalogManage
					.listTeachAssistCatalogCatalog(teachassistVersionId);
			List<VTeachAssistCatalog> vcatalogs = teachAssistCatalogConvert
					.assemblyCatalogTree(teachAssistCatalogConvert.to(catalogs));

			return new Value(vcatalogs);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}
}
