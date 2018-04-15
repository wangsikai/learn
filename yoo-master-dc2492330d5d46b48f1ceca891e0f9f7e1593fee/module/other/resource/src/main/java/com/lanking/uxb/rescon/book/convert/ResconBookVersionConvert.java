package com.lanking.uxb.rescon.book.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.book.BookVersion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.convert.ResconVendorUserConvert;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.rescon.book.value.VBookVersion;
import com.lanking.uxb.service.code.api.ResourceCategoryService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.ResourceCatgeoryConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VResourceCategory;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.file.util.FileUtil;

/**
 * 书本版本实体转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月24日
 */
@Component
public class ResconBookVersionConvert extends Converter<VBookVersion, BookVersion, Long> {

	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconVendorUserConvert vendorUserConvert;
	@Autowired
	private ResourceCategoryService resourceCategoryService;
	@Autowired
	private ResourceCatgeoryConvert resourceCatgeoryConvert;

	@Override
	protected Long getId(BookVersion s) {
		return s.getId();
	}

	@Override
	protected VBookVersion convert(BookVersion s) {
		VBookVersion v = new VBookVersion();
		v.setDescription(StringUtils.defaultIfBlank(s.getDescription()));
		v.setId(s.getId());
		v.setBookId(s.getBookId());
		v.setIsbn(StringUtils.defaultIfBlank(s.getIsbn()));
		v.setName(s.getName());
		v.setShortName(s.getShortName());
		v.setPress(StringUtils.defaultIfBlank(s.getPress()));
		v.setStatus(s.getStatus());
		v.setVersion(s.getVersion());
		v.setCoverId(s.getCoverId());
		v.setCoverUrl(FileUtil.getUrl(s.getCoverId()));
		v.setMainFlag(s.isMainFlag());
		v.setCreateAt(s.getCreateAt());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 阶段
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Integer, VPhase>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VBookVersion d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VPhase value) {
				d.setPhase(value);
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.get(key);
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				return phaseConvert.mget(keys);
			}

		});
		// 科目
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Integer, VSubject>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VBookVersion d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VSubject value) {
				d.setSubject(value);
			}

			@Override
			public VSubject getValue(Integer key) {
				return subjectConvert.get(key);
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				return subjectConvert.mget(keys);
			}

		});
		// 教材类别
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Integer, VTextbookCategory>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VBookVersion d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VTextbookCategory value) {
				d.setTextbookCategory(value);
			}

			@Override
			public VTextbookCategory getValue(Integer key) {
				return textbookCategoryConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbookCategory> mgetValue(Collection<Integer> keys) {
				return textbookCategoryConvert.mget(keys);
			}
		});

		// 书本类型
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Integer, VResourceCategory>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VBookVersion d) {
				return s.getResourceCategoryCode();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VResourceCategory value) {
				d.setResourceCategory(value);
			}

			@Override
			public VResourceCategory getValue(Integer key) {
				return resourceCatgeoryConvert.to(resourceCategoryService.getResCategory(key));
			}

			@Override
			public Map<Integer, VResourceCategory> mgetValue(Collection<Integer> keys) {
				return resourceCatgeoryConvert.to(resourceCategoryService.mget(keys));
			}

		});

		// 教材
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Integer, VTextbook>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(BookVersion s, VBookVersion d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VTextbook value) {
				d.setTextbook(value);
			}

			@Override
			public VTextbook getValue(Integer key) {
				return key == null ? null : tbConvert.to(tbService.get(key));
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				return keys == null ? null : tbConvert.to(tbService.mget(keys));
			}
		});
		// 顺序章节
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, List<Long>, List<VSection>>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Long> getKey(BookVersion s, VBookVersion d) {
				return s.getSectionCodes();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, List<VSection> value) {
				d.setSections(value);
			}

			@Override
			public List<VSection> getValue(List<Long> key) {
				if (key == null || key.size() == 0) {
					return null;
				}
				Map<Long, VSection> smap = sectionConvert.to(sectionService.mget(key));
				List<VSection> list = new ArrayList<VSection>(smap.size());
				for (Long code : key) {
					list.add(smap.get(code));
				}
				return list;
			}

			@Override
			public Map<List<Long>, List<VSection>> mgetValue(Collection<List<Long>> keys) {
				List<Long> codes = new ArrayList<Long>();
				for (List<Long> key : keys) {
					if (key != null && key.size() > 0) {
						codes.addAll(key);
					}
				}
				Map<Long, VSection> smap = sectionConvert.to(sectionService.mget(codes));
				Map<List<Long>, List<VSection>> vmap = new HashMap<List<Long>, List<VSection>>(keys.size());
				for (List<Long> key : keys) {
					if (key != null && key.size() > 0) {
						List<VSection> vs = new ArrayList<VSection>(key.size());
						for (Long code : key) {
							vs.add(smap.get(code));
						}
						vmap.put(key, vs);
					}
				}
				return vmap;
			}
		});

		// 创建人
		assemblers.add(new ConverterAssembler<VBookVersion, BookVersion, Long, VVendorUser>() {
			@Override
			public boolean accept(BookVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookVersion s, VBookVersion d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(BookVersion s, VBookVersion d, VVendorUser value) {
				d.setCreator(value);
			}

			@Override
			public VVendorUser getValue(Long key) {
				return key == null ? null : vendorUserConvert.to(vendorUserManage.getVendorUser(key));
			}

			@Override
			public Map<Long, VVendorUser> mgetValue(Collection<Long> keys) {
				return keys == null ? null : vendorUserConvert.to(vendorUserManage.mgetVendorUser(keys));
			}
		});
	}
}
