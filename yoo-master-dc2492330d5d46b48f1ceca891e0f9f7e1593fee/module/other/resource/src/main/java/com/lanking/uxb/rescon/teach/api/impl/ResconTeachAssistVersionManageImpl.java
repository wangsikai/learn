package com.lanking.uxb.rescon.teach.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistHistory;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistVersion;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistHistoryManage;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistVersionManage;
import com.lanking.uxb.rescon.teach.form.TeachAssistForm;
import com.lanking.uxb.service.counter.api.impl.TeachAssistsCounterProvider;
import com.lanking.uxb.service.search.api.IndexService;

/**
 * 教辅版本接口实现.
 * 
 * @author
 * @since v1.3
 */
@Transactional(readOnly = true)
@Service
public class ResconTeachAssistVersionManageImpl implements ResconTeachAssistVersionManage {

	@Autowired
	@Qualifier("TeachAssistVersionRepo")
	private Repo<TeachAssistVersion, Long> repo;
	@Autowired
	private ResconTeachAssistHistoryManage teachAssistHistoryManage;
	@Autowired
	private TeachAssistsCounterProvider teachAssistsCounterProvider;
	@Autowired
	private IndexService indexService;

	@Override
	public TeachAssistVersion get(Long id) {
		return repo.get(id);
	}

	@Transactional
	@Override
	public void updateTeachStatus(Long id, TeachAssistStatus status, VendorUser user) {
		TeachAssistVersion t = repo.get(id);
		if (status == TeachAssistStatus.PUBLISH) {
			t.setMainFlag(true);
		}
		t.setTeachAssistStatus(status);
		repo.save(t);
		indexService.syncUpdate(IndexType.TEACH_ASSIST, t.getTeachassistId());
		if (status == TeachAssistStatus.PUBLISH) {
			List<TeachAssistVersion> list = this.listTeachVersion(t.getTeachassistId());
			for (TeachAssistVersion teach : list) {
				if (teach.getId() != id) {
					teach.setDelStatus(Status.DELETED);
					repo.save(teach);
				}
			}
			// 保存操作历史记录
			TeachAssistHistory history = new TeachAssistHistory();
			history.setCreateAt(new Date());
			history.setCreateId(user.getId());
			history.setTeachAssistId(t.getTeachassistId());
			history.setType(TeachAssistHistory.OperateType.PUBLISH);
			history.setVersion(t.getVersion());
			teachAssistHistoryManage.save(history);
		}
	}

	@Override
	public List<TeachAssistVersion> listTeachVersion(Long teachId) {
		return repo.find("$listTeachVersion", Params.param("teachId", teachId)).list();
	}

	@Override
	@Transactional
	public TeachAssistVersion save(TeachAssistForm form, VendorUser updator) {
		TeachAssistVersion version = null;
		if (form.getTeachAssistVersionId() == null) {
			version = new TeachAssistVersion();
			version.setCreateAt(new Date());
			version.setCreateId(updator.getVendorId());
		} else {
			version = repo.get(form.getTeachAssistVersionId());
		}
		if (form.getCoverId() != null) {
			version.setCoverId(form.getCoverId());
		}
		version.setTeachassistId(form.getTeachAssistId());
		version.setPhaseCode(form.getPhaseCode());
		version.setSubjectCode(form.getSubjectCode());
		version.setSchoolId(form.getSchoolId());
		version.setTextbookCode(form.getTextbookCode());
		version.setTextbookCategoryCode(form.getTextbookCategoryCode());
		version.setDescription(form.getDescription());
		version.setName(form.getName());
		version.setSectionCodes(form.getSectionCodes());
		version.setMainFlag(true);
		version.setVersion(form.getVersion());
		version.setUpdateAt(new Date());
		version.setUpdateId(updator.getVendorId());
		indexService.syncUpdate(IndexType.TEACH_ASSIST, version.getTeachassistId());
		return repo.save(version);
	}

	@Override
	@Transactional
	public void updateMainFlag(Long versionId, boolean mainFlag, Long teachAssistId) {
		repo.execute("$updateMainFlag",
				Params.param("versionId", versionId).put("mainFlag", mainFlag).put("teachAssistId", teachAssistId));
	}

	@Override
	public Integer getMaxVersion(Long teachAssistId) {
		return repo.find("$getMaxVersion", Params.param("teachAssistId", teachAssistId)).get(Integer.class);
	}

	@Override
	public Map<Integer, Long> getStat(Long vendorId) {
		List<Map> list = repo.find("$getStat", Params.param("vendorId", vendorId)).list(Map.class);
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map pa : list) {
				map.put(Integer.parseInt(pa.get("status").toString()), Long.parseLong(pa.get("count").toString()));
			}
		}
		return map;
	}

	@Override
	@Transactional
	public void updateCover(long versionId, long coverId) {
		TeachAssistVersion version = repo.get(versionId);
		if (version.getTeachAssistStatus() == TeachAssistStatus.PASS) {
			throw new NoPermissionException();
		}

		version.setCoverId(coverId);
		repo.save(version);
	}
}
