package com.lanking.uxb.zycon.mall.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.ResourceCatgeoryConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.zycon.mall.api.ZycExamPaperQuestionService;
import com.lanking.uxb.zycon.mall.api.ZycGoodsService;
import com.lanking.uxb.zycon.mall.api.ZycResourcesGoodsService;
import com.lanking.uxb.zycon.mall.value.VZycExam;

/**
 * 试卷VO
 * 
 * @since 2.0.7
 * @author zemin.song
 * @version 2016年9月6日 19:05:16
 */
@Component
public class ZycExcellentPaperConvert extends Converter<VZycExam, ExamPaper, Long> {
	@Autowired
	private ZycGoodsService goodsService;

	@Autowired
	private ZycResourcesGoodsService zycResourcesGoodsService;

	@Autowired
	private PhaseConvert phaseConvert;

	@Autowired
	private SubjectConvert subjectConvert;

	@Autowired
	private ResourceCatgeoryConvert rcConvert;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private SchoolConvert schoolConvert;

	@Autowired
	private TextbookConvert tbConvert;

	@Autowired
	private TextbookCategoryConvert tbcConvert;

	@Autowired
	private ZycExamPaperQuestionService examPaperQuestionService;

	@Override
	protected Long getId(ExamPaper s) {
		return s.getId();
	}

	@Override
	protected VZycExam convert(ExamPaper s) {
		VZycExam v = new VZycExam();
		v.setName(s.getName());
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		if (s.getDifficulty() != null) {
			v.setDifficulty(s.getDifficulty());
		}
		v.setScore(s.getScore());
		v.setYear(s.getYear() == null ? 0 : s.getYear());
		v.setDistrictCode(s.getDistrictCode());
		v.setSectionCode(s.getSectionCode() == null ? null : Long.valueOf(s.getSectionCode()));
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Long, Map<String, Object>>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VZycExam d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, Map<String, Object> value) {
				if (null != value.get("resourcesGoods")) {
					ResourcesGoods resourcesGoods = (ResourcesGoods) value.get("resourcesGoods");
					d.setResourcesGoods(resourcesGoods);
				}
				if (null != value.get("goods")) {
					Goods goods = (Goods) value.get("goods");
					d.setPrice(goods.getPrice());
					d.setPriceRMB(goods.getPriceRMB());
				}

			}

			@Override
			public Map<String, Object> getValue(Long key) {
				ResourcesGoods resourcesGoods = zycResourcesGoodsService.getGoodsByResourcesId(key);
				if (resourcesGoods == null) {
					return null;
				}
				Goods goods = goodsService.get(resourcesGoods.getId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("resourcesGoods", resourcesGoods);
				map.put("goods", goods);
				return map;
			}

			@Override
			public Map<Long, Map<String, Object>> mgetValue(Collection<Long> keys) {
				List<ResourcesGoods> resourcesGoodslist = zycResourcesGoodsService.mgetGoods(keys);
				Map<Long, Map<String, Object>> retMap = new HashMap<Long, Map<String, Object>>(keys.size());
				Map<String, Object> objMap = null;
				List<Long> goodsIds = Lists.newArrayList();
				for (ResourcesGoods rgs : resourcesGoodslist) {
					goodsIds.add(rgs.getId());
					if (retMap.get(rgs.getResourcesId()) == null) {
						objMap = new HashMap<String, Object>();
						objMap.put("resourcesGoods", rgs);
						objMap.put("goodsId", rgs.getId());
						retMap.put(rgs.getResourcesId(), objMap);
					}
				}
				Map<Long, Goods> goodss = goodsService.mget(goodsIds);
				for (Long key : retMap.keySet()) {
					Long mGoodsId = null;
					if (null != retMap.get(key).get("goodsId")) {
						mGoodsId = Long.parseLong(retMap.get(key).get("goodsId").toString());
					}
					for (Long goodsId : goodss.keySet()) {
						if (goodsId.longValue() == mGoodsId.longValue()) {
							retMap.get(key).put("goods", goodss.get(goodsId));
							break;
						}
					}
				}
				return retMap;
			}

		});

		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Integer, VPhase>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VZycExam d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VPhase value) {
				if (value != null) {
					d.setPhase(value);
				}
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.get(key);
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return phaseConvert.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Integer, VSubject>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VZycExam d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VSubject value) {
				if (value != null) {
					d.setSubject(value);
				}
			}

			@Override
			public VSubject getValue(Integer key) {
				return subjectConvert.get(key);
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return subjectConvert.mget(keys);
			}

		});

		// 类别 resourcecategory
		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Integer, VResourceCategory>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VZycExam d) {
				return s.getResourceCategoryCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VResourceCategory value) {
				if (value != null) {
					if (value.getCode() == 111) {
						value.setName("名校模拟");
					}
					if (value.getCode() == 112) {
						value.setName("中高考真题");
					}
					d.setCategory(value);
				}
			}

			@Override
			public VResourceCategory getValue(Integer key) {
				return rcConvert.get(key);
			}

			@Override
			public Map<Integer, VResourceCategory> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return rcConvert.mget(keys);
			}

		});

		// 地区
		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Long, String>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VZycExam d) {
				return s.getDistrictCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, String value) {
				d.setDistrict(value == null ? "" : value);
			}

			@Override
			public String getValue(Long key) {
				return key == null ? null : districtService.getDistrictName(key);
			}

			@Override
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return districtService.mgetDistrictName(Lists.newArrayList(keys));
			}

		});

		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Long, VSchool>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VZycExam d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VSchool value) {
				d.setSchool(value == null ? null : value);
			}

			@Override
			public VSchool getValue(Long key) {
				return key == null ? null : schoolConvert.get(key);
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return schoolConvert.mget(keys);
			}

		});

		// textbookcategory
		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VZycExam d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VTextbookCategory value) {
				d.setTextbookCategory(value);
			}

			@Override
			public VTextbookCategory getValue(Integer key) {
				return key == null ? null : tbcConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbookCategory> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return tbcConvert.mget(keys);
			}

		});

		// textbook
		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Integer, VTextbook>() {
			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExamPaper s, VZycExam d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, VTextbook value) {
				d.setTextbook(value);
			}

			@Override
			public VTextbook getValue(Integer key) {
				return key == null ? null : tbConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return tbConvert.mget(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycExam, ExamPaper, Long, Integer>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, VZycExam d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, VZycExam d, Integer value) {
				d.setQuestionCount(value);
			}

			@Override
			public Integer getValue(Long key) {
				List<Long> keys = new ArrayList<Long>(1);
				keys.add(key);
				return examPaperQuestionService.getExampaperQuestionCount(keys).get(key);

			}

			@Override
			public Map<Long, Integer> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return examPaperQuestionService.getExampaperQuestionCount(keys);
			}

		});

	}
}
