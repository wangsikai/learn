package com.lanking.uxb.rescon.teach.api.impl;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssist;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistManage;
import com.lanking.uxb.rescon.teach.form.TeachAssistForm;

/**
 * 教辅接口实现.
 * 
 * @author wlche
 * @since v1.3
 */
@Transactional(readOnly = true)
@Service
public class ResconTeachAssistManageImpl implements ResconTeachAssistManage {
	@Autowired
	@Qualifier("TeachAssistRepo")
	Repo<TeachAssist, Long> teachAssistRepo;
	@Autowired
	@Qualifier("TeachAssistVersionRepo")
	Repo<TeachAssistVersion, Long> teachVersionRepo;
	@Autowired
	@Qualifier("TeachAssistVersionRepo")
	Repo<TeachAssistVersion, Long> teachAssistVersionRepo;

	@Override
	@Transactional
	public TeachAssist createTeachAssist(TeachAssistForm form, VendorUser creater) throws ResourceConsoleException {
		Date date = new Date();

		// 创建教辅
		TeachAssist teachAssist = new TeachAssist();
		teachAssist.setCreateAt(date);
		teachAssist.setUpdateAt(date);
		teachAssist.setCreateId(creater.getId());
		teachAssist.setUpdateId(creater.getId());
		teachAssist.setVendorId(creater.getVendorId());
		teachAssistRepo.save(teachAssist);

		// 创建教辅版本
		TeachAssistVersion version = new TeachAssistVersion();
		version.setCreateAt(date);
		version.setUpdateAt(date);
		version.setCreateId(creater.getId());
		version.setUpdateId(creater.getId());
		version.setDescription(form.getDescription());
		version.setMainFlag(true);
		version.setName(form.getName());
		version.setPhaseCode(form.getPhaseCode());
		version.setSubjectCode(form.getSubjectCode());
		version.setSchoolId(form.getSchoolId() == null ? 0 : form.getSchoolId());

		if (null != form.getSectionCodes() && form.getSectionCodes().size() > 0) {
			// 找到节点顺序
			version.setSectionCode(form.getSectionCodes().get(form.getSectionCodes().size() - 1));
			version.setSectionCodes(form.getSectionCodes());
		} else {
			version.setSectionCode(null);
			version.setSectionCodes(null);
		}
		version.setTeachassistId(teachAssist.getId());
		version.setTextbookCategoryCode(form.getTextbookCategoryCode());
		version.setTextbookCode(form.getTextbookCode());
		teachAssistVersionRepo.save(version);

		// 更新教辅当前版本
		teachAssist.setTeachassistVersionId(version.getId());
		teachAssistRepo.save(teachAssist);

		return teachAssist;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TeachAssistVersion> listTeachAssistVersion(Collection<Long> teachAssistIds) {
		if (null == teachAssistIds || teachAssistIds.size() == 0) {
			return Lists.newArrayList();
		}
		return teachVersionRepo.find("$listTeachAssisVersions", Params.param("teachAssisIds", teachAssistIds)).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, TeachAssist> mgetTeachAssist(Collection<Long> ids) {
		return teachAssistRepo.mget(ids);
	}

}
