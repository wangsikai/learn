package com.lanking.uxb.rescon.book.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;
import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityExistsException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogManage;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogSectionManage;
import com.lanking.uxb.rescon.book.api.ResconBookManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;
import com.lanking.uxb.rescon.book.convert.ResconBookCatalogConvert;
import com.lanking.uxb.rescon.book.value.VBookCatalog;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 书本目录.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月27日
 */
@RestController
@RequestMapping("rescon/book/catalog")
@RolesAllowed(userTypes = { "VENDOR_ADMIN", "VENDOR_HEAD", "VENDOR_BUILD", "VENDOR_CHECK" })
public class ResconBookCatalogController {

	@Autowired
	private ResconBookCatalogManage bookCatalogManage;
	@Autowired
	private ResconBookCatalogConvert bookCatalogConvert;
	@Autowired
	private BookCounterProvider bookCounterProvider;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private ResconBookCatalogSectionManage resconBookCatalogSectionManage;
	@Autowired
	private ResconBookManage resconBookManage;
	@Autowired
	private ResconBookQuestionCategoryManage bookQuestionCategoryManage;

	/**
	 * 获得书本的目录.
	 * 
	 * @return
	 */
	@RequestMapping(value = "getBookCatalogs", method = RequestMethod.POST)
	public Value getBookCatalogs(Long bookVersionId) {
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		Map<String, Object> data = new HashMap<String, Object>(1);
		data.put("catalogs", catalogs);
		data.put("noCatalogCount", bookCounterProvider.getNoCatalogResourceCount(bookVersionId));
		data.put("resourceCount", bookCounterProvider.getResourceCount(bookVersionId));

		return new Value(data);
	}

	@RequestMapping(value = "delete", method = { RequestMethod.POST, RequestMethod.GET })
	public Value delete(@RequestParam(value = "bookVersionId") long bookVersionId,
			@RequestParam(value = "id") long id) {

		// 首先删除分类结构关系
		bookQuestionCategoryManage.delBookQuestionCategoryRelation(id, null);

		bookCatalogManage.delete(id);
		return getBookCatalogs(bookVersionId);
	}

	@RequestMapping(value = "add", method = { RequestMethod.POST, RequestMethod.GET })
	public Value add(@RequestParam(value = "bookVersionId") long bookVersionId, @RequestParam(value = "id") long id,
			@RequestParam(value = "name") String name) {
		int length = StringUtils.getJsUnicodeLength(name);
		if (length == 0 || length > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.CATALOG_NAME_TOO_LONG));
		}
		BookCatalog bookCatalog = bookCatalogManage.create(id, bookVersionId, name, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("catalogs", catalogs);
		map.put("catalog", bookCatalog);
		return new Value(map);
	}

	@RequestMapping(value = "up", method = { RequestMethod.POST, RequestMethod.GET })
	public Value up(@RequestParam(value = "bookVersionId") long bookVersionId, @RequestParam(value = "id") long id) {
		bookCatalogManage.up(id, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		return new Value(catalogs);
	}

	@RequestMapping(value = "down", method = { RequestMethod.POST, RequestMethod.GET })
	public Value down(@RequestParam(value = "bookVersionId") long bookVersionId, @RequestParam(value = "id") long id) {
		bookCatalogManage.down(id, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		return new Value(catalogs);
	}

	@RequestMapping(value = "up_level", method = { RequestMethod.POST, RequestMethod.GET })
	public Value upLevel(@RequestParam(value = "bookVersionId") long bookVersionId,
			@RequestParam(value = "id") long id) {
		bookCatalogManage.upLevel(id, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		return new Value(catalogs);
	}

	@RequestMapping(value = "down_level", method = { RequestMethod.POST, RequestMethod.GET })
	public Value downLevel(@RequestParam(value = "bookVersionId") long bookVersionId,
			@RequestParam(value = "id") long id) {
		bookCatalogManage.downLevel(id, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		return new Value(catalogs);
	}

	@RequestMapping(value = "update_name", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateName(@RequestParam(value = "bookVersionId") long bookVersionId,
			@RequestParam(value = "id") long id, @RequestParam(value = "name") String name) {
		int length = StringUtils.getJsUnicodeLength(name);
		if (length == 0 || length > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.CATALOG_NAME_TOO_LONG));
		}
		bookCatalogManage.updateName(id, name, Security.getUserId());
		List<BookCatalog> pcs = bookCatalogManage.getBookCatalog(bookVersionId);
		List<VBookCatalog> vpcs = bookCatalogConvert.to(pcs);

		// 获取书本目录与教材章节关联关系
		BookVersion bookVersion = resconBookManage.getVersion(bookVersionId);
		if (vpcs.size() > 0 && bookVersion.getResourceCategoryCode() != null
				&& bookVersion.getResourceCategoryCode() == 501) {
			List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage
					.findByBookVersion(bookVersionId);
			Map<Long, BookCatalogSection> bookCatalogSectionMap = new HashMap<Long, BookCatalogSection>();
			for (BookCatalogSection bcs : bookCatalogSections) {
				bookCatalogSectionMap.put(bcs.getBookCatalogId(), bcs);
			}
			for (VBookCatalog vbc : vpcs) {
				vbc.setHasLinkSection(bookCatalogSectionMap.get(vbc.getId()) != null);
			}
		}

		List<VBookCatalog> catalogs = bookCatalogConvert.assemblyCatalogTree(vpcs);
		return new Value(catalogs);
	}

	/**
	 * 获取书本教材章节列表.
	 * 
	 * @param bookVersionId
	 *            书本版本ID
	 * @param textbookCode
	 *            对应的教材CODE
	 * @return
	 */
	@RequestMapping(value = "findSections", method = { RequestMethod.POST })
	public Value findSections(Long bookVersionId, Integer textbookCode) {
		if (bookVersionId == null || textbookCode == null) {
			return new Value(new MissingArgumentException());
		}

		// 简单数据模式
		List<Map<String, Object>> sectionList = new ArrayList<Map<String, Object>>();
		List<Section> sections = sectionService.findByTextbookCode(textbookCode);
		List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage.findByBookVersion(bookVersionId);
		Map<Long, BookCatalog> catalogs = bookCatalogManage.mgetBookCatalog(bookVersionId);

		// 教材章节与教辅目录对应关系
		Map<Long, List<BookCatalog>> sectionCatalogMap = new HashMap<Long, List<BookCatalog>>();
		for (BookCatalogSection bookCatalogSection : bookCatalogSections) {
			List<BookCatalog> bookCatalogs = sectionCatalogMap.get(bookCatalogSection.getSectionCode());
			if (bookCatalogs == null) {
				bookCatalogs = new ArrayList<BookCatalog>();
				sectionCatalogMap.put(bookCatalogSection.getSectionCode(), bookCatalogs);
			}
			BookCatalog catalog = catalogs.get(bookCatalogSection.getBookCatalogId());
			if (catalog != null) {
				bookCatalogs.add(catalog);
			}
		}

		for (Section section : sections) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", section.getCode());
			map.put("level", section.getLevel());
			map.put("name", section.getName());
			map.put("pid", section.getPcode());
			map.put("type", "section");
			sectionList.add(map);

			List<BookCatalog> scatalogs = sectionCatalogMap.get(section.getCode());
			if (scatalogs != null) {
				for (BookCatalog catalog : scatalogs) {
					Map<String, Object> cmap = new HashMap<String, Object>();
					cmap.put("id", catalog.getId());
					cmap.put("level", catalog.getLevel());
					cmap.put("name", catalog.getName());
					cmap.put("pid", section.getCode());
					cmap.put("type", "catalog");
					sectionList.add(cmap);
				}
			}
		}

		return new Value(sectionList);
	}

	/**
	 * 添加书本目录与教材章节之间的关联.
	 * 
	 * @param versionId
	 *            版本ID
	 * @param catalogId
	 *            目录ID
	 * @param textbookCode
	 * @param sectionCode
	 * @return
	 */
	@RequestMapping(value = "addLink", method = { RequestMethod.POST })
	public Value addLink(Long bookVersionId, Long bookCatalogId, Integer textbookCode, Long sectionCode,
			Integer sequence) {
		if (bookVersionId == null || bookCatalogId == null || textbookCode == null || sectionCode == null
				|| sequence == null) {
			return new Value(new MissingArgumentException());
		}

		List<BookCatalogSection> bookCatalogSections = resconBookCatalogSectionManage.findByCatalog(bookVersionId,
				bookCatalogId);
		if (CollectionUtils.isEmpty(bookCatalogSections)) {
			resconBookCatalogSectionManage.save(bookVersionId, bookCatalogId, textbookCode, sectionCode,
					Security.getUserId(), sequence);
		} else {
			// 已经存在对应关系
			return new Value(new EntityExistsException());
		}

		return new Value();
	}

	@RequestMapping(value = "removeLink", method = { RequestMethod.POST })
	public Value removeLink(Long bookVersionId, Long bookCatalogId) {
		if (bookVersionId == null || bookCatalogId == null) {
			return new Value(new MissingArgumentException());
		}

		try {
			resconBookCatalogSectionManage.deleteCatalogRelation(bookVersionId, bookCatalogId);
		} catch (AbstractException e) {
			return new Value(e);
		}

		return new Value();
	}
}
