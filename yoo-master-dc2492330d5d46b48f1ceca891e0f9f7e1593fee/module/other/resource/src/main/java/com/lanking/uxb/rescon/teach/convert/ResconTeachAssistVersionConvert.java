package com.lanking.uxb.rescon.teach.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.teach.value.VTeachAssistVersion;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VSubject;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.file.util.FileUtil;

@Component
public class ResconTeachAssistVersionConvert extends Converter<VTeachAssistVersion, TeachAssistVersion, Long> {

	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private TextbookService tbService;

	@Override
	protected Long getId(TeachAssistVersion s) {
		return s.getId();
	}

	@Override
	protected VTeachAssistVersion convert(TeachAssistVersion s) {
		VTeachAssistVersion teachV = new VTeachAssistVersion();
		teachV.setId(s.getId());
		teachV.setTeachAssistId(s.getTeachassistId());
		teachV.setMainFlag(s.isMainFlag());
		teachV.setCoverId(s.getCoverId());
		// 图片URL
		teachV.setCoverUrl(FileUtil.getUrl(teachV.getCoverId()));
		teachV.setName(s.getName());
		teachV.setDescription(StringUtils.defaultIfBlank(s.getDescription()));
		teachV.setVersion(s.getVersion());
		teachV.setPhaseCode(s.getPhaseCode());
		teachV.setSubjectCode(s.getSubjectCode());
		teachV.setStatus(s.getTeachAssistStatus());
		teachV.setCreateAt(s.getCreateAt());
		teachV.setSchoolId(s.getSchoolId());
		return teachV;
	}

	// 初始化

	@SuppressWarnings("rawtypes")
	protected void initAssemblers(List<ConverterAssembler> assemblers) {

		// 获取创建人
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Long, VendorUser>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VendorUser value) {
				d.setCreator(value.getRealName() == null ? value.getName() : value.getRealName());

			}

			@Override
			public VendorUser getValue(Long key) {
				return vendorUserManage.getVendorUser(key);
			}

			@Override
			public Map<Long, VendorUser> mgetValue(Collection<Long> keys) {
				return vendorUserManage.mgetVendorUser(keys);
			}

		});

		// 获取学校名称
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Long, VSchool>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return s.getSchoolId() != 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VSchool value) {
				if (value != null) {
					d.setSchoolName(value.getName());
				}
			}

			@Override
			public VSchool getValue(Long key) {
				return schoolConvert.get(key);
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				return schoolConvert.mget(keys);
			}

		});

		// 获取版本Vo
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Integer, VTextbookCategory>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return s.getTextbookCategoryCode() != null && s.getTextbookCategoryCode() != 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VTextbookCategory value) {
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

		// 获取教材VO
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Integer, VTextbook>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return s.getTextbookCategoryCode() != null && s.getTextbookCategoryCode() != 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getTextbookCategoryCode();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VTextbook value) {
				d.setTextbook(value);
			}

			@Override
			public VTextbook getValue(Integer key) {
				return textbookConvert.get(key);
			}

			@Override
			public Map<Integer, VTextbook> mgetValue(Collection<Integer> keys) {
				return textbookConvert.mget(keys);
			}

		});

		// 获取阶段phase
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Integer, VPhase>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VPhase value) {
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
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Integer, VSubject>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VSubject value) {
				if (value != null) {
					d.setSubjectName(value.getName());
				}
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

		// 章节目录
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, List<Long>, List<VSection>>() {

			@Override
			public boolean accept(TeachAssistVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public List<Long> getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getSectionCodes();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, List<VSection> value) {
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

		// 教材
		assemblers.add(new ConverterAssembler<VTeachAssistVersion, TeachAssistVersion, Integer, VTextbook>() {
			@Override
			public boolean accept(TeachAssistVersion s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(TeachAssistVersion s, VTeachAssistVersion d) {
				return s.getTextbookCode();
			}

			@Override
			public void setValue(TeachAssistVersion s, VTeachAssistVersion d, VTextbook value) {
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

	}
}
