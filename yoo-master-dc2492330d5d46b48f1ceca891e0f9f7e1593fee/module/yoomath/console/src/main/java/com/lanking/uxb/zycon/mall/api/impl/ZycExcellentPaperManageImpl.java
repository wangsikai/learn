package com.lanking.uxb.zycon.mall.api.impl;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.data.Order;
import com.lanking.cloud.sdk.data.Order.Direction;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.PageImpl;
import com.lanking.uxb.zycon.mall.api.ZycExcellentPaperManage;
import com.lanking.uxb.zycon.mall.form.ExcellentPaperForm;

@Service
@Transactional(readOnly = true)
@SuppressWarnings("unchecked")
public class ZycExcellentPaperManageImpl implements ZycExcellentPaperManage {

	@Autowired
	@Qualifier("ExamPaperRepo")
	private Repo<ExamPaper, Long> examRepo;
	@Autowired
	private SearchService searchService;

	@Override
	public ExamPaper get(long id) {
		return examRepo.get(id);
	}

	@Override
	public Map<Long, ExamPaper> mget(List<Long> ids) {
		return examRepo.mget(ids);
	}

	@Override
	public Page<ExamPaper> queryResconExam(ExcellentPaperForm form) {

		int size = form.getPageSize();
		int p = form.getPage();
		int offset = (p - 1) * size;
		List<IndexTypeable> types = Lists.newArrayList();
		types.add(IndexType.EXAM_PAPER);// 类型BIZ.XXX
		BoolQueryBuilder qb = QueryBuilders.boolQuery();
		// 供应商 暂时不需要
		if (form.getVendorId() != null) {
			qb.must(QueryBuilders.matchQuery("vendorId", form.getVendorId()));
		}
		if (null != form.getKey() && form.getKey() != "") {
			qb.must(QueryBuilders.matchQuery("name", form.getKey()));
		}
		if (StringUtils.isNotBlank(form.getExamCode())) {
			try {
				qb.must(QueryBuilders.matchQuery("id", Long.parseLong(form.getExamCode())));
			} catch (NumberFormatException e) {
				return new PageImpl<ExamPaper>(new ArrayList<ExamPaper>(0), 0,
						P.index(form.getPage(), form.getPageSize()));
			}
		}
		// 上架状态
		if (null != form.getStatus()) {
			if (form.getStatus() == ResourcesGoodsStatus.PUBLISH) {
				qb.must(QueryBuilders.termQuery("exampaperGoodsStatus", form.getStatus().getValue()));
			} else {
				qb.mustNot(QueryBuilders.termQuery("exampaperGoodsStatus", ResourcesGoodsStatus.PUBLISH.getValue()));
				qb.mustNot(QueryBuilders.termQuery("exampaperGoodsStatus", ResourcesGoodsStatus.DELETE.getValue()));
			}
		} else {
			qb.mustNot(QueryBuilders.termQuery("exampaperGoodsStatus", ResourcesGoodsStatus.DELETE.getValue()));
		}
		// 已发布状态
		qb.must(QueryBuilders.termQuery("status", ExamPaperStatus.PASS.getValue()));
		if (null != form.getStartCreateAt()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("createAt");
			rqb.gte(form.getStartCreateAt());
			qb.must(rqb);
		}
		if (null != form.getEndCreateAt()) {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(form.getEndCreateAt());
			cal.add(Calendar.DATE, 1);
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("createAt");
			rqb.lt(cal.getTimeInMillis());
			qb.must(rqb);
		}
		if (null != form.getSubjectCode()) {
			qb.must(QueryBuilders.termQuery("subjectCode", form.getSubjectCode()));
		}
		if (null != form.getPhaseCode()) {
			qb.must(QueryBuilders.termQuery("phaseCode", form.getPhaseCode()));
		}
		if (null != form.getCategory()) {
			qb.must(QueryBuilders.termQuery("category", form.getCategory()));
		}
		if (null != form.getDistrictCode()) {
			qb.must(QueryBuilders.termQuery("districtCode", form.getDistrictCode()));
		}
		qb.must(QueryBuilders.termQuery("ownSchoolId", 0L));
		if (null != form.getYear()) {
			// 判断是不是五年以前
			Calendar cal = Calendar.getInstance();
			int nowYear = cal.get(Calendar.YEAR);
			if ((nowYear - form.getYear()) >= 5) {
				RangeQueryBuilder rqb = QueryBuilders.rangeQuery("year");
				rqb.lte(form.getYear());
				qb.must(rqb);
			} else {
				qb.must(QueryBuilders.termQuery("year", form.getYear()));
			}
		}
		if (null != form.getMinDifficulty()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			rqb.gte(form.getMinDifficulty().doubleValue());
			qb.must(rqb);
		}
		if (null != form.getMaxDifficulty()) {
			RangeQueryBuilder rqb = QueryBuilders.rangeQuery("difficulty");
			rqb.lte(form.getMaxDifficulty().doubleValue());
			qb.must(rqb);
		}
		if (null != form.getTextBookCode()) {
			qb.must(QueryBuilders.termQuery("textbookCode", form.getTextBookCode()));
		}
		if (form.getTextBookCategoryCode() != null) {
			qb.must(QueryBuilders.termQuery("textbookcategoryCode", form.getTextBookCategoryCode()));
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			qb.must(QueryBuilders.matchQuery("schoolName", form.getSchoolName()));
		}
		if (null != form.getSectionCode()) {
			qb.must(QueryBuilders.termQuery("sectionCode", form.getSectionCode()));
		}

		List<Order> orders = Lists.newArrayList();
		orders.add(new Order("exampaperGoodsStatus", Direction.DESC));
		orders.add(new Order("createAt", Direction.DESC));
		orders.add(new Order("_score", Direction.DESC));
		Order[] orderArray = new Order[orders.size()];
		for (int i = 0; i < orders.size(); i++) {
			Order order = orders.get(i);
			orderArray[i] = order;
		}
		com.lanking.uxb.service.search.api.Page docPage = searchService.search(types, offset, size, qb, null,
				orderArray);
		List<Long> examIds = Lists.newArrayList();
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				examIds.add(Long.valueOf(document.getId()));
			}
		}
		Map<Long, ExamPaper> examMap = this.mget(examIds);
		List<ExamPaper> examList = Lists.newArrayList();
		for (Long examId : examIds) {
			if (examMap.get(examId) != null) {
				examList.add(examMap.get(examId));
			}
		}
		return new PageImpl<ExamPaper>(examList, docPage.getTotalCount(), P.index(form.getPage(), form.getPageSize()));

	}

}
