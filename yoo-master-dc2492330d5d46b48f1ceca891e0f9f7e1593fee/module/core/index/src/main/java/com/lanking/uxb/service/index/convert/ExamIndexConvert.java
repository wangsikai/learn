package com.lanking.uxb.service.index.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaper;
import com.lanking.cloud.domain.common.resource.examPaper.ExamPaperCount;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoods;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsCategory;
import com.lanking.cloud.domain.yoo.goods.resources.ResourcesGoodsStatus;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.index.value.ExamIndexDoc;

/***
 * 试卷索引转换
 * 
 * @since 2.5
 * @author <a href="mailto:zhonghui.geng@elanking.com">zhonghui.geng</a>
 * @version 2015年10月23日 下午2:51:52
 */
@Component
@Transactional(readOnly = true)
public class ExamIndexConvert extends Converter<ExamIndexDoc, ExamPaper, Long> {

	@Autowired
	@Qualifier("ResourcesGoodsRepo")
	Repo<ResourcesGoods, Long> resourcesGoodsRepo;
	@Autowired
	@Qualifier("VendorUserRepo")
	Repo<VendorUser, Long> vendorUserRepo;
	@Autowired
	@Qualifier("ExamPaperCountRepo")
	Repo<ExamPaperCount, Long> examCountRepo;
	@Autowired
	private SchoolConvert schoolConvert;

	@Override
	protected Long getId(ExamPaper s) {
		return s.getId();
	}

	@Override
	protected ExamIndexDoc convert(ExamPaper s) {
		ExamIndexDoc doc = new ExamIndexDoc();
		doc.setId(s.getId());
		doc.setClickCount(0L);
		doc.setCategory(s.getResourceCategoryCode());
		doc.setCreateAt(s.getCreateAt().getTime());
		doc.setCreateId(s.getCreateId());
		doc.setDifficulty(s.getDifficulty());
		doc.setDistrictCode(s.getDistrictCode() == null ? 0L : s.getDistrictCode());
		doc.setName(s.getName());
		doc.setPhaseCode(s.getPhaseCode());
		doc.setSchoolId(s.getSchoolId());
		doc.setOwnSchoolId(s.getOwnSchoolId() == null ? 0L : s.getOwnSchoolId());
		doc.setSectionCode(s.getSectionCode());
		if (s.getSectionCode() != null) {
			String scString = s.getSectionCode().toString();
			List<Long> sectionCodes = new ArrayList<Long>();
			sectionCodes.add(s.getSectionCode());
			if (scString.length() > 10) {
				int num = (scString.length() - 10) / 2;
				for (int i = 0; i < num; i++) {
					String newScStr = scString.substring(0, scString.length() - (i + 1) * 2);
					sectionCodes.add(Long.parseLong(newScStr));
				}
			}
			doc.setSectionCodes(sectionCodes);
		}
		doc.setStatus(s.getStatus().getValue());
		doc.setSubjectCode(s.getSubjectCode());
		doc.setTextbookcategoryCode(s.getTextbookCategoryCode());
		doc.setTextbookCode(s.getTextbookCode());
		doc.setYear(s.getYear());
		// 默认值
		doc.setExampaperGoodsStatus(ResourcesGoodsStatus.UN_PUBLISH.getValue());
		doc.setIsRecommend(ResourcesGoodsCategory.COMMON.getValue());
		return doc;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// vendorId
		assemblers.add(new ConverterAssembler<ExamIndexDoc, ExamPaper, Long, VendorUser>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, ExamIndexDoc d) {
				return d.getCreateId();
			}

			@Override
			public void setValue(ExamPaper s, ExamIndexDoc d, VendorUser value) {
				if (value != null) {
					d.setVendorId(value.getVendorId());
				}
			}

			@Override
			public VendorUser getValue(Long key) {
				if (key == null || key == 0) {
					return null;
				}
				return vendorUserRepo.get(key);
			}

			@Override
			public Map<Long, VendorUser> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return vendorUserRepo.mget(keys);
			}
		});
		//
		assemblers.add(new ConverterAssembler<ExamIndexDoc, ExamPaper, Long, ResourcesGoods>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, ExamIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, ExamIndexDoc d, ResourcesGoods value) {
				if (value != null) {
					// 只区分上下架
					if (value.getStatus() != ResourcesGoodsStatus.PUBLISH) {
						d.setExampaperGoodsStatus(ResourcesGoodsStatus.UN_PUBLISH.getValue());
					} else {
						d.setExampaperGoodsStatus(ResourcesGoodsStatus.PUBLISH.getValue());
					}
					d.setIsRecommend(value.getCategory().getValue());
				}
			}

			@Override
			public ResourcesGoods getValue(Long key) {
				// 有重复数据
				List<ResourcesGoods> result = resourcesGoodsRepo
						.find("$getExamPaperGoodsByExamId", Params.param("examId", key)).list();
				if (result != null && result.size() > 0) {
					return result.get(0);
				} else {
					return null;
				}
			}

			@Override
			public Map<Long, ResourcesGoods> mgetValue(Collection<Long> keys) {
				Map<Long, ResourcesGoods> map = new HashMap<Long, ResourcesGoods>(keys.size());
				List<ResourcesGoods> examPapers = resourcesGoodsRepo
						.find("$mgetExamPaperGoodsByExamIds", Params.param("examIds", keys)).list();
				for (ResourcesGoods exampaperGoods : examPapers) {
					if (map.get(exampaperGoods.getResourcesId()) == null) {
						map.put(exampaperGoods.getResourcesId(), exampaperGoods);
					}
				}
				return map;
			}

		});

		// 获取学校名称
		assemblers.add(new ConverterAssembler<ExamIndexDoc, ExamPaper, Long, VSchool>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, ExamIndexDoc d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(ExamPaper s, ExamIndexDoc d, VSchool value) {
				if (value != null) {
					d.setSchoolName(value.getName());
				}
			}

			@Override
			public VSchool getValue(Long key) {
				if (key == null) {
					return null;
				}
				return schoolConvert.get(key);
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				return schoolConvert.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<ExamIndexDoc, ExamPaper, Long, ExamPaperCount>() {

			@Override
			public boolean accept(ExamPaper s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ExamPaper s, ExamIndexDoc d) {
				return s.getId();
			}

			@Override
			public void setValue(ExamPaper s, ExamIndexDoc d, ExamPaperCount value) {
				if (value == null) {
					d.setClickCount(0L);
				} else {
					d.setClickCount(value.getClickCount());
				}
			}

			@Override
			public ExamPaperCount getValue(Long key) {
				return examCountRepo
						.find("$indexFindPaperCount", Params.param("examPaperIds", key).put("dayOfN", 0).put("nDay", 0))
						.get();
			}

			@Override
			public Map<Long, ExamPaperCount> mgetValue(Collection<Long> keys) {

				Map<Long, ExamPaperCount> map = new HashMap<Long, ExamPaperCount>(keys.size());
				List<ExamPaperCount> examPaperCounts = examCountRepo.find("$indexFindPaperCount",
						Params.param("examPaperIds", keys).put("dayOfN", 0).put("nDay", 0)).list();
				for (ExamPaperCount epc : examPaperCounts) {
					if (map.get(epc.getExamPaperId()) == null) {
						map.put(epc.getExamPaperId(), epc);
					}
				}
				return map;
			}
		});

	}
}
