package com.lanking.uxb.service.examPaper.api.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperStatus;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsCategory;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.api.ExamPaperService;
import com.lanking.uxb.service.examPaper.form.ExamQueryForm;

/**
 * @see ExamPaperService
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Service
@Transactional(readOnly = true)
public class ExamPaperServiceImpl implements ExamPaperService {
	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> repo;

	@Autowired
	private SearchService searchService;

	@Override
	public ExamPaper get(long id) {
		return repo.get(id);
	}

	@Override
	public Map<Long, ExamPaper> mget(Collection<Long> ids) {
		return repo.mget(ids);
	}

	@Override
	public Page<ExamPaper> queryExam(ExamQueryForm form) {
		int size = form.getPageSize();
		int p = form.getPage();
		int offset = (p - 1) * size;
		List<IndexTypeable> types = Lists.newArrayList();
		types.add(IndexType.EXAM_PAPER);// 类型BIZ.XXX
		BoolQueryBuilder qb = QueryBuilders.boolQuery();

		qb.must(QueryBuilders.termQuery("status", ExamPaperStatus.PASS.getValue()));

		// 关键字查询
		if (StringUtils.isNotBlank(form.getKey())) {
			qb.must(QueryBuilders.multiMatchQuery(form.getKey(), "name", "schoolName"));
		}
		// 是否推荐
		if (form.getIsRecommend() != null && form.getIsRecommend()) {
			qb.must(QueryBuilders.termQuery("isRecommend", ResourcesGoodsCategory.RECOMMEND.getValue()));
		}
		if (null != form.getSubjectCode()) {
			qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
		}
		if (null != form.getPhaseCode()) {
			qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
		}
		if (CollectionUtils.isNotEmpty(form.getCategoryCodes())) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("category", form.getCategoryCodes()));
		}
		if (null != form.getDistrictCode()) {
			qb.must(QueryBuilders.termQuery("districtCode", form.getDistrictCode()));
		}
		if (null != form.getStatus()) {
			qb.must(QueryBuilders.termQuery("exampaperGoodsStatus", form.getStatus().getValue()));
		}
		qb.must(QueryBuilders.termQuery("ownSchoolId", 0L));

		if (null != form.getSectionCodes()) {
			qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("sectionCodes", form.getSectionCodes()));
		}

		if (null != form.getYear()) {
			// 判断是不是三年以前
			Calendar cal = Calendar.getInstance();
			int nowYear = cal.get(Calendar.YEAR);
			if ((nowYear - form.getYear()) >= 3) {
				RangeQueryBuilder rqb = QueryBuilders.rangeQuery("year");
				rqb.lte(form.getYear());
				qb.must(rqb);
			} else {
				qb.must(QueryBuilders.termQuery("year", form.getYear()));
			}
		}
		// if (null != form.getIsRandom() && form.getIsRandom() == true) {
		// qb.filter(QueryBuilders.functionScoreQuery(ScoreFunctionBuilders.randomFunction(1)));
		//
		// }

		List<Order> orders = Lists.newArrayList();
		if (StringUtils.isNotBlank(form.getOrderBy()) && form.getOrder() != null) {
			orders.add(new Order(form.getOrderBy(), form.getOrder() ? Order.Direction.DESC : Order.Direction.ASC));
		}
		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		// 随机
		int newSize = size;
		if (null != form.getIsRandom() && form.getIsRandom() == true) {
			newSize = 500;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, newSize, qb, null,
				orderArray);
		List<Long> examIds = Lists.newArrayList();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				examIds.add(Long.valueOf(document.getId()));
			}
		}
		// 随机
		if (null != form.getIsRandom() && form.getIsRandom() == true) {
			Collections.shuffle(examIds);
			if (examIds.size() > size) {
				examIds = examIds.subList(0, size);
			}
		}
		Map<Long, ExamPaper> examMap = repo.mget(examIds);
		List<ExamPaper> examList = Lists.newArrayList();
		for (Long examId : examIds) {
			if (examMap.get(examId) != null) {
				examList.add(examMap.get(examId));
			}
		}
		return new PageImpl<ExamPaper>(examList, docPage.getTotalCount(), P.index(form.getPage(), form.getPageSize()));
	}

	@Override
	public List<Long> getDistricts(Integer phaseCode, Integer subjectCode) {
		return repo.find("$findDistrict", Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)).list(
				Long.class);
	}

	@Override
	public List<Long> getDistrictsByGoods(Integer phaseCode, Integer subjectCode) {
		return repo.find("$findDistrictByExamGoods",
				Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode)).list(Long.class);
	}

	@Override
	public List<Long> getDistrictsByFavorite(Integer phaseCode, Integer subjectCode, Long createId) {
		return repo.find("$findDistrictByFavorite",
				Params.param("phaseCode", phaseCode).put("subjectCode", subjectCode).put("createId", createId)).list(
				Long.class);
	}

	@Override
	public List<ExamPaper> findRecommendByNdayHot(Integer subjectCode, Integer phaseCode, Integer limit,
			boolean isPutaway, int nDay) {
		return repo.find(
				"$findRecommendByNdayHot",
				Params.param("subjectCode", subjectCode).put("phaseCode", phaseCode).put("limit", limit)
						.put("isPutaway", isPutaway).put("nDay", nDay)).list();
	}

	@Override
	public List<ExamPaper> findNewPaper(Integer subjectCode, Integer phaseCode, Integer limit) {
		return repo.find("$findNewPaper",
				Params.param("subjectCode", subjectCode).put("phaseCode", phaseCode).put("limit", limit)).list();
	}

}
