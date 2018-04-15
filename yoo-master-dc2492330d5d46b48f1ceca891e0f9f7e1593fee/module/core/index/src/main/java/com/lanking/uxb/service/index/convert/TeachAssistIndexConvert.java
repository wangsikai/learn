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

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.index.value.TeachAssistIndexDoc;

/**
 * 索引convert类
 *
 * @author xinyu.zhou
 * @since 2.2.0
 */
@Component
@Transactional(readOnly = true)
public class TeachAssistIndexConvert extends Converter<TeachAssistIndexDoc, TeachAssist, Long> {
	@Autowired
	@Qualifier("TeachAssistVersionRepo")
	Repo<TeachAssistVersion, Long> teachAssistVersionRepo;

	@Override
	protected Long getId(TeachAssist teachAssist) {
		return teachAssist.getId();
	}

	@Override
	protected TeachAssistIndexDoc convert(TeachAssist teachAssist) {
		TeachAssistIndexDoc doc = new TeachAssistIndexDoc();
		doc.setCreateAt(teachAssist.getCreateAt().getTime());
		doc.setCreateId(teachAssist.getCreateId());
		doc.setDelStatus(teachAssist.getDelStatus().getValue());
		doc.setId(teachAssist.getId());
		doc.setVendorId(teachAssist.getVendorId());
		return doc;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<TeachAssistIndexDoc, TeachAssist, Long, List<TeachAssistVersion>>() {

			@Override
			public boolean accept(TeachAssist teachAssist) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(TeachAssist teachAssist, TeachAssistIndexDoc teachAssistIndexDoc) {
				return teachAssist.getId();
			}

			@Override
			public void setValue(TeachAssist teachAssist, TeachAssistIndexDoc teachAssistIndexDoc,
					List<TeachAssistVersion> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					for (TeachAssistVersion v : value) {
						if (v.isMainFlag()) {
							teachAssistIndexDoc.setName1(v.getName());
							teachAssistIndexDoc.setPhaseCode1(v.getPhaseCode());
							teachAssistIndexDoc.setSubjectCode1(v.getSubjectCode());
							teachAssistIndexDoc.setSectionCode1(v.getSectionCode());
							teachAssistIndexDoc.setTeachAssistStatus1(v.getTeachAssistStatus().getValue());
							teachAssistIndexDoc.setTextbookCode1(v.getTextbookCode());
							teachAssistIndexDoc.setTextbookCategoryCode1(v.getTextbookCategoryCode());
						} else {
							teachAssistIndexDoc.setName2(v.getName());
							teachAssistIndexDoc.setPhaseCode2(v.getPhaseCode());
							teachAssistIndexDoc.setSubjectCode2(v.getSubjectCode());
							teachAssistIndexDoc.setSectionCode2(v.getSectionCode());
							teachAssistIndexDoc.setTeachAssistStatus2(v.getTeachAssistStatus().getValue());
							teachAssistIndexDoc.setTextbookCode2(v.getTextbookCode());
							teachAssistIndexDoc.setTextbookCategoryCode2(v.getTextbookCategoryCode());
						}
					}
				}

			}

			@Override
			public List<TeachAssistVersion> getValue(Long key) {
				return teachAssistVersionRepo.find("$findByTeachAssist", Params.param("ids", Lists.newArrayList(key)))
						.list();
			}

			@Override
			public Map<Long, List<TeachAssistVersion>> mgetValue(Collection<Long> keys) {
				List<TeachAssistVersion> versions = teachAssistVersionRepo
						.find("$findByTeachAssist", Params.param("ids", keys)).list();

				Map<Long, List<TeachAssistVersion>> retMap = new HashMap<Long, List<TeachAssistVersion>>(keys.size());
				for (TeachAssistVersion tav : versions) {
					List<TeachAssistVersion> assistVersions = retMap.get(tav.getTeachassistId());
					if (CollectionUtils.isEmpty(assistVersions)) {
						assistVersions = new ArrayList<TeachAssistVersion>(2);
					}
					assistVersions.add(tav);
					retMap.put(tav.getTeachassistId(), assistVersions);
				}
				return retMap;
			}
		});
	}
}
