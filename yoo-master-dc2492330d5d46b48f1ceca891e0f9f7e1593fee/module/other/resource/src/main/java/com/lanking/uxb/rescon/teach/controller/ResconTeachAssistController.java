package com.lanking.uxb.rescon.teach.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.IllegalArgFormatException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgePointService;
import com.lanking.uxb.rescon.basedata.api.ResconKnowledgeSystemService;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistCatalogManage;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistHistoryManage;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistManage;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistVersionManage;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistCatalogConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistHistoryConvert;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistVersionConvert;
import com.lanking.uxb.rescon.teach.form.TeachAssistForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistQueryForm;
import com.lanking.uxb.rescon.teach.value.VTeachAssist;
import com.lanking.uxb.rescon.teach.value.VTeachAssistCatalog;
import com.lanking.uxb.rescon.teach.value.VTeachAssistVersion;
import com.lanking.uxb.service.counter.api.impl.TeachAssistsCounterProvider;
import com.lanking.uxb.service.search.api.IndexService;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 教辅.
 * 
 * @author wlche
 * @since v1.3
 *
 */
@RestController
@RequestMapping(value = "/rescon/teachAssist")
public class ResconTeachAssistController {
	private static final Logger logger = LoggerFactory.getLogger(ResconTeachAssistController.class);

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconTeachAssistManage teachAssistManage;
	@Autowired
	private ResconTeachAssistHistoryManage teachAssistHistoryManage;
	@Autowired
	private ResconTeachAssistVersionConvert resconTeachAssistVersionConvert;
	@Autowired
	private ResconTeachAssistManage resconTeachAssistManage;
	@Autowired
	private IndexService indexService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ResconTeachAssistConvert resconTeachAssistConvert;
	@Autowired
	private ResconTeachAssistVersionManage resconTeachAssistVersionManage;
	@Autowired
	private ResconTeachAssistHistoryConvert historyConvert;
	@Autowired
	private ResconTeachAssistCatalogManage resconTeachAssistCatalogManage;
	@Autowired
	private ResconTeachAssistCatalogConvert catalogConvert;
	@Autowired
	private TeachAssistsCounterProvider teachAssistsCounterProvider;
	@Autowired
	private ResconKnowledgeSystemService resconKnowledgeSystemService;
	@Autowired
	private ResconKnowledgePointService resconKnowledgePointService;
	@Autowired
	private ResconTeachAssistElementService elementService;

	/**
	 * 创建教辅（非创建新版本）.
	 * 
	 * @param json
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public Value create(String json) {
		TeachAssistForm teachAssistForm = JSON.parseObject(json, TeachAssistForm.class);
		if (null == teachAssistForm || StringUtils.isBlank(teachAssistForm.getName())
				|| null == teachAssistForm.getPhaseCode() || null == teachAssistForm.getSubjectCode()
				|| null == teachAssistForm.getTextbookCategoryCode()) {
			return new Value(new MissingArgumentException());
		}

		if (teachAssistForm.getName().length() > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.NAME_OUT_OF_LENGTH_TEACHASSIST));
		}

		if (teachAssistForm.getDescription() != null && teachAssistForm.getDescription().length() > 500) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.DESC_OUT_OF_LENGTH_TEACHASSIST));
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());

		try {
			TeachAssist teachAssist = teachAssistManage.createTeachAssist(teachAssistForm, user);
			// 保存操作历史记录
			TeachAssistHistory history = new TeachAssistHistory();
			history.setCreateAt(new Date());
			history.setCreateId(user.getId());
			history.setTeachAssistId(teachAssist.getId());
			history.setType(TeachAssistHistory.OperateType.CREATE);
			history.setVersion(1);
			teachAssistHistoryManage.save(history);
			// 教辅计数更新
			teachAssistsCounterProvider.incrTotalCount(user.getVendorId(), 1);
			// 教辅录入中更新
			teachAssistsCounterProvider.incrEditCount(user.getVendorId(), 1);
			// 更新索引
			this.addTeachAssistIndex(teachAssist.getId());

			return new Value(teachAssist);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 建立教辅索引.
	 * 
	 * @param teachAssistId
	 */
	private void addTeachAssistIndex(long teachAssistId) {
		indexService.add(IndexType.TEACH_ASSIST, teachAssistId);
	}

	/**
	 * 查询教辅
	 * 
	 * @param form
	 *            查询参数
	 * @return
	 */
	@RequestMapping(value = "query", method = RequestMethod.POST)
	private Value query(TeachAssistQueryForm form) {
		int offset = 0;
		int size = 0;
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		List<IndexTypeable> types = Lists.<IndexTypeable> newArrayList(IndexType.TEACH_ASSIST); // 教辅
		BoolQueryBuilder qb = null;
		List<Order> orders = new ArrayList<Order>();
		try {
			offset = (form.getPage() - 1) * form.getPageSize();
			size = form.getPageSize();
			orders = new ArrayList<Order>();
			orders.add(new Order("createAt", Direction.DESC));
			qb = QueryBuilders.boolQuery();
			qb.must(QueryBuilders.termQuery("vendorId", user.getVendorId())); // 供应商限定
			// 创建人
			if (form.getCreateId() != null) {
				qb.must(QueryBuilders.termQuery("createId", form.getCreateId()));
			}
			// 未删除
			if (form.getDelStatus() == null) {
				qb.mustNot(QueryBuilders.termQuery("delStatus", Status.DELETED.getValue()));
			} else {
				qb.mustNot(QueryBuilders.termQuery("delStatus", form.getDelStatus()));
			}
			// 关键字
			if (StringUtils.isNotBlank(form.getKey())) {
				qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "name1", "name2"));
			}
			// 阶段
			if (form.getPhaseCode() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("phaseCode1", form.getPhaseCode()));
				qbi.should(QueryBuilders.termQuery("phaseCode2", form.getPhaseCode()));
				qb.must(qbi);
			}
			// 版本
			if (form.getTextbookCategoryCode() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("textbookCategoryCode1", form.getTextbookCategoryCode()));
				qbi.should(QueryBuilders.termQuery("textbookCategoryCode2", form.getTextbookCategoryCode()));
				qb.must(qbi);
			}

			// 学科
			if (form.getSubjectCode() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("subjectCode1", form.getSubjectCode()));
				qbi.should(QueryBuilders.termQuery("subjectCode2", form.getSubjectCode()));
				qb.must(qbi);
			}
			// 教材
			if (form.getTextbookCode() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("textbookCode1", form.getTextbookCode()));
				qbi.should(QueryBuilders.termQuery("textbookCode2", form.getTextbookCode()));
				qb.must(qbi);
			}
			// 章节
			if (form.getSectionCode() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("sectionCode1", form.getSectionCode()));
				qbi.should(QueryBuilders.termQuery("sectionCode2", form.getSectionCode()));
				qb.must(qbi);
			}
			// 状态
			if (form.getStatus() != null) {
				BoolQueryBuilder qbi = QueryBuilders.boolQuery();
				qbi.should(QueryBuilders.termQuery("teachAssistStatus1", form.getStatus().getValue()));
				qbi.should(QueryBuilders.termQuery("teachAssistStatus2", form.getStatus().getValue()));
				qb.must(qbi);
			}
			// 时间
			if (form.getCreateBt() != null && form.getCreateEt() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date(form.getCreateEt()));
				cal.add(Calendar.DAY_OF_YEAR, 1);
				qb.must(QueryBuilders.rangeQuery("createAt").gte(form.getCreateBt()).lt(cal.getTime().getTime()));
			}
		} catch (Exception e) {
			return new Value(new IllegalArgFormatException());
		}

		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		Page docPage = searchService.search(types, offset, size, qb, null, orderArray);

		List<Long> teachAssistIds = new ArrayList<Long>();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				teachAssistIds.add(Long.parseLong(document.getId()));
			}
		}
		List<VTeachAssistVersion> vTeachAssistVersions = resconTeachAssistVersionConvert.to(resconTeachAssistManage
				.listTeachAssistVersion(teachAssistIds));
		Map<Long, VTeachAssist> vteachMap = resconTeachAssistConvert.to(resconTeachAssistManage
				.mgetTeachAssist(teachAssistIds));

		for (VTeachAssistVersion vteachVersion : vTeachAssistVersions) {
			// 获取vTeachAssist
			VTeachAssist vTeachAssist = vteachMap.get(vteachVersion.getTeachAssistId());
			// 教辅VO为空则创建一个
			if (vTeachAssist == null) {
				vTeachAssist = new VTeachAssist();
				vTeachAssist.setId(vteachVersion.getTeachAssistId());
				vTeachAssist.setNum(1);
				vteachMap.put(vteachVersion.getTeachAssistId(), vTeachAssist);
			} else {
				// 如果存在更新版本（版本+1）
				vTeachAssist.setNum(vTeachAssist.getNum() + 1);
			}
			// 如果是主版本
			if (vteachVersion.isMainFlag()) {
				// 设置主版本
				vTeachAssist.setMainVersion(vteachVersion);
			} else {
				// 否 设置辅版本
				vTeachAssist.setDeputyVersion(vteachVersion);
			}

		}
		List<VTeachAssist> vTeachAssists = new ArrayList<VTeachAssist>(vteachMap.size());
		// 填入vTeachAssists
		for (Long teachAssistId : teachAssistIds) {
			vTeachAssists.add(vteachMap.get(teachAssistId));
		}
		// 组装返回前台数据
		VPage<VTeachAssist> vPage = new VPage<VTeachAssist>();
		vPage.setCurrentPage(form.getPage());
		vPage.setPageSize(form.getPageSize());
		vPage.setTotal(docPage.getTotalCount());
		vPage.setTotalPage(docPage.getTotalPage());
		vPage.setItems(vTeachAssists);
		return new Value(vPage);
	}

	/**
	 * 获取教辅相关信息
	 * 
	 * @param teachId
	 * @author wangsenhao
	 * @return
	 */
	@RequestMapping(value = "getTeachAssistInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getTeachAssistInfo(Long teachId) {
		Map<String, Object> data = new HashMap<String, Object>();
		if (teachId != null) {
			// 只会同时存在两个版本，只有一个主版本
			List<TeachAssistVersion> versionList = resconTeachAssistVersionManage.listTeachVersion(teachId);
			for (TeachAssistVersion v : versionList) {
				if (v.isMainFlag()) {
					data.put("data", resconTeachAssistVersionConvert.to(v));
				} else {
					data.put("deputy", resconTeachAssistVersionConvert.to(v));
				}
			}
			List<TeachAssistHistory> list = teachAssistHistoryManage.findList(teachId);
			data.put("operateList", historyConvert.to(list));
		}
		return new Value(data);
	}

	/**
	 * 教辅校验--目录列表<br>
	 * 这里展示的是最底层的
	 * 
	 * @author wangsenhao
	 * @param teachassistVersionId
	 * @return
	 */
	@RequestMapping(value = "teachAssistCheckCataLogList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value teachAssistCheckCataLogList(Long teachassistVersionId) {
		if (teachassistVersionId == null) {
			return new Value(new MissingArgumentException());
		}
		List<TeachAssistCatalog> catalogs = resconTeachAssistCatalogManage
				.listTeachAssistCatalogCatalog(teachassistVersionId);
		List<VTeachAssistCatalog> vcatalogs = catalogConvert.assemblyCatalogTree(catalogConvert.to(catalogs));
		List<VTeachAssistCatalog> childs = new ArrayList<VTeachAssistCatalog>();
		for (VTeachAssistCatalog v : vcatalogs) {
			if (CollectionUtils.isEmpty(v.getChildren())) {
				childs.add(v);
			} else {
				for (VTeachAssistCatalog v1 : v.getChildren()) {
					if (CollectionUtils.isEmpty(v1.getChildren())) {
						childs.add(v1);
					} else {
						for (VTeachAssistCatalog v2 : v1.getChildren()) {
							childs.add(v2);
						}
					}
				}
			}
		}
		return new Value(childs);
	}

	/**
	 * 教辅目录校验
	 * 
	 * @author wangsenhao
	 * @param status
	 *            目录的状态
	 * 
	 * @param status0
	 *            教辅的状态,可以为空 是否需要更新教辅状态<br>
	 *            1.页面判断是否所有教辅下的目录是否已经是已通过了,如果全部已通过了,需要更新<br>
	 *            2.原先是已通过的，后来有一个以上改成不通过了，需要更新
	 * @param id
	 *            目录对应的Id
	 * @param versionId
	 *            教辅对应的Id,可以为空
	 * @return
	 */
	@RequestMapping(value = "updateCheckStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateCheckStatus(CardStatus status, Long id, TeachAssistStatus status0, Long versionId) {
		resconTeachAssistCatalogManage.updateCheckStatus(status, id);
		if (versionId != null) {
			VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
			resconTeachAssistVersionManage.updateTeachStatus(versionId, status0, user);
		}
		return new Value();
	}

	/**
	 * 更新教辅状态
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	@RequestMapping(value = "updateTeachStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateTeachStatus(Long id, TeachAssistStatus status) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		resconTeachAssistVersionManage.updateTeachStatus(id, status, user);
		return new Value();
	}

	/**
	 * 知识专项或者知识小专项获取下级树结构
	 * 
	 * @param knowledgeCode
	 * @return
	 */
	@RequestMapping(value = "getKpList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getKpList(Long knowledgeCode) {
		List<KnowledgeSystem> list = resconKnowledgeSystemService.findByCode(knowledgeCode);
		List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
		// 知识体系
		for (KnowledgeSystem ks : list) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ks.getCode());
			map.put("name", ks.getName());
			map.put("parent", ks.getPcode());
			map.put("level", ks.getLevel());
			map.put("isParent", true);
			datas.add(map);
		}
		List<KnowledgePoint> pointList = resconKnowledgePointService.findByCode(knowledgeCode);
		// 知识点
		for (KnowledgePoint ks : pointList) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", ks.getCode());
			map.put("name", ks.getName());
			map.put("parent", ks.getPcode());
			datas.add(map);
		}
		return new Value(datas);
	}

	/**
	 * 判断版本是否存在.
	 * 
	 * @param teachassistVersionId
	 *            版本ID
	 * @return
	 */
	@RequestMapping(value = "checkVersion")
	public Value checkVersion(Long teachassistVersionId) {
		if (teachassistVersionId == null) {
			return new Value(new MissingArgumentException());
		}
		TeachAssistVersion t = resconTeachAssistVersionManage.get(teachassistVersionId);
		return new Value(t == null ? null : t);
	}

	/**
	 * 教辅库统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "teachAssistsDatas", method = RequestMethod.POST)
	public Value teachAssistsDatas() {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		Map<String, Object> map = new HashMap<String, Object>(4);
		Map<Integer, Long> statMap = resconTeachAssistVersionManage.getStat(user.getVendorId());
		map.put("publishCount", statMap.get(4) == null ? 0 : statMap.get(4));
		map.put("noPublishCount", statMap.get(2) == null ? 0 : statMap.get(2));
		map.put("editCount", statMap.get(0) == null ? 0 : statMap.get(0));
		return new Value(map);
	}

	/**
	 * 保存版本
	 *
	 * @param form
	 *            {@link TeachAssistForm}
	 * @return {@link Value}
	 */
	@RequestMapping(value = "saveVersion", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveVersion(TeachAssistForm form) {
		if (null == form || form.getTeachAssistVersionId() == null || StringUtils.isBlank(form.getName())
				|| null == form.getPhaseCode() || null == form.getSubjectCode()
				|| null == form.getTextbookCategoryCode()) {
			return new Value(new MissingArgumentException());
		}

		if (form.getName().length() > 100) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.NAME_OUT_OF_LENGTH_TEACHASSIST));
		}

		if (form.getDescription() != null && form.getDescription().length() > 500) {
			return new Value(new ResourceConsoleException(ResourceConsoleException.DESC_OUT_OF_LENGTH_TEACHASSIST));
		}

		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		try {
			TeachAssistVersion version = resconTeachAssistVersionManage.save(form, user);
			// 更新索引
			this.addTeachAssistIndex(form.getTeachAssistId());

			return new Value(resconTeachAssistVersionConvert.to(version));
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}
	}

	/**
	 * 拷贝
	 *
	 * @param versionId
	 *            版本id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "copy", method = { RequestMethod.GET, RequestMethod.POST })
	public Value copy(long versionId) {
		VendorUser user = vendorUserManage.getVendorUser(Security.getUserId());
		TeachAssistVersion teachAssistVersion = resconTeachAssistVersionManage.get(versionId);

		if (teachAssistVersion.getTeachAssistStatus() == TeachAssistStatus.PUBLISH) {
			Integer newVersion = resconTeachAssistVersionManage.getMaxVersion(teachAssistVersion.getTeachassistId());
			TeachAssistForm form = new TeachAssistForm();
			// 复制创建版本
			form.setCoverId(teachAssistVersion.getCoverId());
			form.setName(teachAssistVersion.getName());
			form.setPhaseCode(teachAssistVersion.getPhaseCode());
			form.setSubjectCode(teachAssistVersion.getSubjectCode());
			form.setSchoolId(teachAssistVersion.getSchoolId());
			form.setTeachAssistId(teachAssistVersion.getTeachassistId());
			form.setTextbookCategoryCode(teachAssistVersion.getTextbookCategoryCode());
			form.setTextbookCode(teachAssistVersion.getTextbookCode());
			form.setDescription(teachAssistVersion.getDescription());
			form.setSectionCodes(teachAssistVersion.getSectionCodes());
			form.setVersion(newVersion == null ? 0 : newVersion + 1);

			TeachAssistVersion v = resconTeachAssistVersionManage.save(form, user);
			// 把原来的版本mainflag 改成false
			resconTeachAssistVersionManage.updateMainFlag(v.getId(), false, teachAssistVersion.getTeachassistId());
			// 复制版本目录
			long startAt = System.currentTimeMillis();
			logger.debug("copy catalog start at : {}", startAt);
			List<TeachAssistCatalog> catalogList = resconTeachAssistCatalogManage
					.listTeachAssistCatalogCatalog(teachAssistVersion.getId());
			Map<Long, TeachAssistCatalog> catalogMap = resconTeachAssistCatalogManage.copy(v.getId(), catalogList,
					user.getVendorId());
			logger.debug("copy catalog spend {}", System.currentTimeMillis() - startAt);

			// 复制模块数据
			startAt = System.currentTimeMillis();
			logger.debug("copy elements start at: {}", startAt);
			elementService.copy(catalogMap, user.getVendorId());
			logger.debug("copy elements spend {}", System.currentTimeMillis() - startAt);

			// 保存操作历史记录
			TeachAssistHistory history = new TeachAssistHistory();
			history.setCreateAt(new Date());
			history.setCreateId(user.getId());
			history.setTeachAssistId(v.getTeachassistId());
			history.setType(TeachAssistHistory.OperateType.CREATE);
			history.setVersion(newVersion == null ? 0 : newVersion + 1);
			teachAssistHistoryManage.save(history);
			// 教辅计数更新
			teachAssistsCounterProvider.incrTotalCount(user.getVendorId(), 1);
			// 教辅录入中更新
			teachAssistsCounterProvider.incrEditCount(user.getVendorId(), 1);
			// 更新索引
			this.addTeachAssistIndex(v.getTeachassistId());

			return new Value();
		}

		return new Value();
	}

	/**
	 * 教辅编辑更新封面
	 *
	 * @param versionId
	 *            教辅版本id
	 * @param coverId
	 *            封面id
	 * @return {@link Value}
	 */
	@RequestMapping(value = "uploadCover", method = { RequestMethod.GET, RequestMethod.POST })
	public Value uploadCover(long versionId, long coverId) {
		try {
			resconTeachAssistVersionManage.updateCover(versionId, coverId);
		} catch (ResourceConsoleException e) {
			return new Value(e);
		}

		return new Value();
	}
}
