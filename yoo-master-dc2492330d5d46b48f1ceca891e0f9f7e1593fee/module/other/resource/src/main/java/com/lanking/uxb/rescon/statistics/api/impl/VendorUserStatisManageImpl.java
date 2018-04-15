package com.lanking.uxb.rescon.statistics.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatis;
import com.lanking.cloud.domain.support.resources.vendor.VendorUserStatisType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.statistics.api.VendorUserStatisManage;

@Transactional(readOnly = true)
@Service
public class VendorUserStatisManageImpl implements VendorUserStatisManage {

	@Autowired
	@Qualifier("VendorUserStatisRepo")
	private Repo<VendorUserStatis, Long> statisRepo;

	private static long statisFrom = 1L;

	@Transactional
	@Override
	public void updateAfterDraft(long builderId) {
		if (statisFrom < 0 || System.currentTimeMillis() < statisFrom) {
			return;
		}
		// 总统计
		VendorUserStatis statis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (statis == null) {
			statis = new VendorUserStatis();
			statis.setUserId(builderId);
			statis.setType(VendorUserStatisType.DETAIL);
			statis.setDraftQCount(1L);
			statis.setCreateAt(new Date());
			statis.setUpdateAt(statis.getCreateAt());
		} else {
			statis.setDraftQCount(statis.getDraftQCount() + 1);
			statis.setUpdateAt(new Date());
		}
		statisRepo.save(statis);
	}

	@Transactional
	@Override
	public void deleteAfterDraft(long builderId) {
		// 总统计
		VendorUserStatis statis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (statis != null) {
			statis.setDraftQCount(statis.getDraftQCount() - 1);
			statis.setUpdateAt(new Date());
			statisRepo.save(statis);
		}
	}

	@Transactional
	@Override
	public void updateAfterBuild(long builderId, boolean fromDraft, Date questionCreateAt) {
		if (fromDraft && questionCreateAt == null) {
			throw new IllegalArgException();
		}
		if (statisFrom < 0 || (!fromDraft && System.currentTimeMillis() < statisFrom)
				|| (fromDraft && questionCreateAt.getTime() < statisFrom)) {
			return;
		}
		// 总统计
		VendorUserStatis statis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (statis == null) {
			statis = new VendorUserStatis();
			statis.setUserId(builderId);
			statis.setType(VendorUserStatisType.DETAIL);
			statis.setBuildQCount(1L);
			statis.setCreateAt(new Date());
			statis.setUpdateAt(statis.getCreateAt());
		} else {
			statis.setBuildQCount(statis.getBuildQCount() + 1);
			if (fromDraft) {
				statis.setDraftQCount(statis.getDraftQCount() - 1);
			}
			statis.setUpdateAt(new Date());
		}
		statisRepo.save(statis);
		// 天统计
		VendorUserStatis dayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", new Date())).get();
		if (dayStatis == null) {
			dayStatis = new VendorUserStatis();
			dayStatis.setUserId(builderId);
			dayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			dayStatis.setBuildQCount(1L);
			dayStatis.setCreateAt(new Date());
			dayStatis.setUpdateAt(dayStatis.getCreateAt());
		} else {
			dayStatis.setBuildQCount(dayStatis.getBuildQCount() + 1);
			dayStatis.setUpdateAt(new Date());
		}
		statisRepo.save(dayStatis);
	}

	@Transactional
	@Override
	public void updateAfterNoPassBuild(long builderId, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 总统计
		VendorUserStatis statis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (statis != null) {
			statis.setNopassQCount(statis.getNopassQCount() - 1);
			statis.setUpdateAt(new Date());
			statisRepo.save(statis);
		}
		// 天统计
		VendorUserStatis dayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (dayStatis != null) {
			dayStatis.setNopassQCount(dayStatis.getNopassQCount() - 1);
			dayStatis.setUpdateAt(new Date());
			statisRepo.save(dayStatis);
		}
	}

	@Transactional
	@Override
	public void updateAfterPassRecheck(long builderId, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 总统计
		VendorUserStatis statis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (statis != null) {
			statis.setPassQCount(statis.getPassQCount() - 1);
			statis.setUpdateAt(new Date());
			statisRepo.save(statis);
		}
		// 天统计
		VendorUserStatis dayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (dayStatis != null) {
			dayStatis.setPassQCount(dayStatis.getPassQCount() - 1);
			dayStatis.setUpdateAt(new Date());
			statisRepo.save(dayStatis);
		}
	}

	@Transactional
	@Override
	public void updateAfterStep1Pass(long builderId, long checkerId, boolean edit, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 录入员总统计
		VendorUserStatis builderStatis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (builderStatis != null) {
			if (edit) {
				builderStatis.setBuildModifyCount(builderStatis.getBuildModifyCount() + 1);
			}
			builderStatis.setPassStep1QCount(builderStatis.getPassStep1QCount() + 1);
			builderStatis.setUpdateAt(new Date());
			statisRepo.save(builderStatis);
		}
		// 录入员天统计
		VendorUserStatis builderDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (builderDayStatis != null) {
			if (edit) {
				builderDayStatis.setBuildModifyCount(builderDayStatis.getBuildModifyCount() + 1);
			}
			builderDayStatis.setPassStep1QCount(builderDayStatis.getPassStep1QCount() + 1);
			builderDayStatis.setUpdateAt(new Date());
			statisRepo.save(builderDayStatis);
		}
		// 校验员总统计
		VendorUserStatis checkStatis = statisRepo.find("$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (checkStatis == null) {
			checkStatis = new VendorUserStatis();
			checkStatis.setUserId(checkerId);
			checkStatis.setType(VendorUserStatisType.DETAIL);
			checkStatis.setCheckCheckCount(1L);
			checkStatis.setCheckCheck1Count(1L);
			if (edit) {
				checkStatis.setCheckModifyCount(1L);
			}
			checkStatis.setCreateAt(new Date());
			checkStatis.setUpdateAt(checkStatis.getCreateAt());
		} else {
			checkStatis.setCheckCheckCount(checkStatis.getCheckCheckCount() + 1);
			checkStatis.setCheckCheck1Count(checkStatis.getCheckCheck1Count() + 1);
			if (edit) {
				checkStatis.setCheckModifyCount(checkStatis.getCheckModifyCount() + 1);
			}
			checkStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkStatis);
		// 校验员 天统计
		VendorUserStatis checkDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", new Date())).get();
		if (checkDayStatis == null) {
			checkDayStatis = new VendorUserStatis();
			checkDayStatis.setUserId(checkerId);
			checkDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			checkDayStatis.setCheckCheckCount(1L);
			checkDayStatis.setCheckCheck1Count(1L);
			if (edit) {
				checkDayStatis.setCheckModifyCount(1L);
			}
			checkDayStatis.setCreateAt(new Date());
			checkDayStatis.setUpdateAt(checkDayStatis.getCreateAt());
		} else {
			checkDayStatis.setCheckCheckCount(checkDayStatis.getCheckCheckCount() + 1);
			checkDayStatis.setCheckCheck1Count(checkDayStatis.getCheckCheck1Count() + 1);
			if (edit) {
				checkDayStatis.setCheckModifyCount(checkDayStatis.getCheckModifyCount() + 1);
			}
			checkDayStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkDayStatis);
	}

	@Transactional
	@Override
	public void updateAfterStep1NoPass(long builderId, long checkerId, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 录入员总统计
		VendorUserStatis buildStatis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (buildStatis == null) {
			// buildStatis = new VendorUserStatis();
			// buildStatis.setUserId(builderId);
			// buildStatis.setType(VendorUserStatisType.DETAIL);
			// buildStatis.setNopassQCount(1L);
			// buildStatis.setCreateAt(new Date());
			// buildStatis.setUpdateAt(buildStatis.getCreateAt());
		} else {
			buildStatis.setNopassQCount(buildStatis.getNopassQCount() + 1);
			buildStatis.setUpdateAt(new Date());
		}
		if (buildStatis != null) {
			statisRepo.save(buildStatis);
		}
		// 录入员天统计
		VendorUserStatis buildDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (buildDayStatis == null) {
			// buildDayStatis = new VendorUserStatis();
			// buildDayStatis.setUserId(builderId);
			// buildDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			// buildDayStatis.setNopassQCount(1L);
			// buildDayStatis.setCreateAt(new Date());
			// buildDayStatis.setUpdateAt(buildDayStatis.getCreateAt());
		} else {
			buildDayStatis.setNopassQCount(buildDayStatis.getNopassQCount() + 1);
			buildDayStatis.setUpdateAt(new Date());
		}
		if (buildDayStatis != null) {
			statisRepo.save(buildDayStatis);
		}
		// 校验员总统计
		VendorUserStatis checkStatis = statisRepo.find("$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (checkStatis == null) {
			checkStatis = new VendorUserStatis();
			checkStatis.setUserId(checkerId);
			checkStatis.setType(VendorUserStatisType.DETAIL);
			checkStatis.setCheckCheckCount(1L);
			checkStatis.setCheckCheck1Count(1L);
			checkStatis.setCreateAt(new Date());
			checkStatis.setUpdateAt(checkStatis.getCreateAt());
		} else {
			checkStatis.setCheckCheckCount(checkStatis.getCheckCheckCount() + 1);
			checkStatis.setCheckCheck1Count(checkStatis.getCheckCheck1Count() + 1);
			checkStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkStatis);
		// 校验员天统计
		VendorUserStatis checkDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", new Date())).get();
		if (checkDayStatis == null) {
			checkDayStatis = new VendorUserStatis();
			checkDayStatis.setUserId(checkerId);
			checkDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			checkDayStatis.setCheckCheckCount(1L);
			checkDayStatis.setCheckCheck1Count(1L);
			checkDayStatis.setCreateAt(new Date());
			checkDayStatis.setUpdateAt(checkDayStatis.getCreateAt());
		} else {
			checkDayStatis.setCheckCheckCount(checkDayStatis.getCheckCheckCount() + 1);
			checkDayStatis.setCheckCheck1Count(checkDayStatis.getCheckCheck1Count() + 1);
			checkDayStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkDayStatis);
	}

	@Transactional
	@Override
	public void updateAfterStep2Pass(long builderId, long checkerId, boolean edit, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 录入员总统计
		VendorUserStatis buildStatis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (buildStatis == null) {
			// buildStatis = new VendorUserStatis();
			// buildStatis.setUserId(builderId);
			// buildStatis.setType(VendorUserStatisType.DETAIL);
			// buildStatis.setPassQCount(1L);
			// if (edit) {
			// buildStatis.setBuildModifyCount(1L);
			// }
			// buildStatis.setCreateAt(new Date());
			// buildStatis.setUpdateAt(buildStatis.getCreateAt());
		} else {
			buildStatis.setPassQCount(buildStatis.getPassQCount() + 1);
			buildStatis.setPassStep1QCount(buildStatis.getPassStep1QCount() - 1);
			if (edit) {
				buildStatis.setBuildModifyCount(buildStatis.getBuildModifyCount() + 1);
			}
			buildStatis.setUpdateAt(new Date());
		}
		if (buildStatis != null) {
			statisRepo.save(buildStatis);
		}
		// 录入员天统计
		VendorUserStatis buildDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (buildDayStatis == null) {
			// buildDayStatis = new VendorUserStatis();
			// buildDayStatis.setUserId(builderId);
			// buildDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			// buildDayStatis.setPassQCount(1L);
			// if (edit) {
			// buildDayStatis.setBuildModifyCount(1L);
			// }
			// buildDayStatis.setCreateAt(new Date());
			// buildDayStatis.setUpdateAt(buildDayStatis.getCreateAt());
		} else {
			buildDayStatis.setPassQCount(buildDayStatis.getPassQCount() + 1);
			buildDayStatis.setPassStep1QCount(buildDayStatis.getPassStep1QCount() - 1);
			if (edit) {
				buildDayStatis.setBuildModifyCount(buildDayStatis.getBuildModifyCount() + 1);
			}
			buildDayStatis.setUpdateAt(new Date());
		}
		if (buildDayStatis != null) {
			statisRepo.save(buildDayStatis);
		}
		// 校验员总统计
		VendorUserStatis checkStatis = statisRepo.find("$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (checkStatis == null) {
			checkStatis = new VendorUserStatis();
			checkStatis.setUserId(checkerId);
			checkStatis.setType(VendorUserStatisType.DETAIL);
			checkStatis.setCheckCheckCount(1L);
			checkStatis.setCheckCheck2Count(1L);
			if (edit) {
				checkStatis.setCheckModifyCount(1L);
			}
			checkStatis.setCreateAt(new Date());
			checkStatis.setUpdateAt(checkStatis.getCreateAt());
		} else {
			checkStatis.setCheckCheckCount(checkStatis.getCheckCheckCount() + 1);
			checkStatis.setCheckCheck2Count(checkStatis.getCheckCheck2Count() + 1);
			if (edit) {
				checkStatis.setCheckModifyCount(checkStatis.getCheckModifyCount() + 1);
			}
			checkStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkStatis);
		// 校验员 天统计
		VendorUserStatis checkDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", new Date())).get();
		if (checkDayStatis == null) {
			checkDayStatis = new VendorUserStatis();
			checkDayStatis.setUserId(checkerId);
			checkDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			checkDayStatis.setCheckCheckCount(1L);
			checkDayStatis.setCheckCheck2Count(1L);
			if (edit) {
				checkDayStatis.setCheckModifyCount(1L);
			}
			checkDayStatis.setCreateAt(new Date());
			checkDayStatis.setUpdateAt(checkDayStatis.getCreateAt());
		} else {
			checkDayStatis.setCheckCheckCount(checkDayStatis.getCheckCheckCount() + 1);
			checkDayStatis.setCheckCheck2Count(checkDayStatis.getCheckCheck2Count() + 1);
			if (edit) {
				checkDayStatis.setCheckModifyCount(checkDayStatis.getCheckModifyCount() + 1);
			}
			checkDayStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkDayStatis);
	}

	@Transactional
	@Override
	public void updateAfterStep2NoPass(long builderId, long checkerId, Date questionCreateAt) {
		if (statisFrom < 0 || questionCreateAt.getTime() < statisFrom) {
			return;
		}
		// 录入员总统计
		VendorUserStatis buildStatis = statisRepo.find("$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (buildStatis == null) {
			// buildStatis = new VendorUserStatis();
			// buildStatis.setUserId(builderId);
			// buildStatis.setType(VendorUserStatisType.DETAIL);
			// buildStatis.setNopassQCount(1L);
			// buildStatis.setCreateAt(new Date());
			// buildStatis.setUpdateAt(buildStatis.getCreateAt());
		} else {
			buildStatis.setNopassQCount(buildStatis.getNopassQCount() + 1);
			buildStatis.setPassStep1QCount(buildStatis.getPassStep1QCount() - 1);
			buildStatis.setUpdateAt(new Date());
		}
		if (buildStatis != null) {
			statisRepo.save(buildStatis);
		}
		// 录入员天统计
		VendorUserStatis buildDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", builderId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", questionCreateAt)).get();
		if (buildDayStatis == null) {
			// buildDayStatis = new VendorUserStatis();
			// buildDayStatis.setUserId(builderId);
			// buildDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			// buildDayStatis.setNopassQCount(1L);
			// buildDayStatis.setCreateAt(new Date());
			// buildDayStatis.setUpdateAt(buildDayStatis.getCreateAt());
		} else {
			buildDayStatis.setNopassQCount(buildDayStatis.getNopassQCount() + 1);
			buildDayStatis.setPassStep1QCount(buildDayStatis.getPassStep1QCount() - 1);
			buildDayStatis.setUpdateAt(new Date());
		}
		if (buildDayStatis != null) {
			statisRepo.save(buildDayStatis);
		}
		// 校验员总统计
		VendorUserStatis checkStatis = statisRepo.find("$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DETAIL.getValue())).get();
		if (checkStatis == null) {
			checkStatis = new VendorUserStatis();
			checkStatis.setUserId(checkerId);
			checkStatis.setType(VendorUserStatisType.DETAIL);
			checkStatis.setCheckCheckCount(1L);
			checkStatis.setCheckCheck2Count(1L);
			checkStatis.setCreateAt(new Date());
			checkStatis.setUpdateAt(checkStatis.getCreateAt());
		} else {
			checkStatis.setCheckCheckCount(checkStatis.getCheckCheckCount() + 1);
			checkStatis.setCheckCheck2Count(checkStatis.getCheckCheck2Count() + 1);
			checkStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkStatis);
		// 校验员天统计
		VendorUserStatis checkDayStatis = statisRepo.find(
				"$resconGet",
				Params.param("userId", checkerId).put("type", VendorUserStatisType.DAY_DETAIL.getValue())
						.put("dayDetail", new Date())).get();
		if (checkDayStatis == null) {
			checkDayStatis = new VendorUserStatis();
			checkDayStatis.setUserId(checkerId);
			checkDayStatis.setType(VendorUserStatisType.DAY_DETAIL);
			checkDayStatis.setCheckCheckCount(1L);
			checkDayStatis.setCheckCheck2Count(1L);
			checkDayStatis.setCreateAt(new Date());
			checkDayStatis.setUpdateAt(checkDayStatis.getCreateAt());
		} else {
			checkDayStatis.setCheckCheckCount(checkDayStatis.getCheckCheckCount() + 1);
			checkDayStatis.setCheckCheck2Count(checkDayStatis.getCheckCheck2Count() + 1);
			checkDayStatis.setUpdateAt(new Date());
		}
		statisRepo.save(checkDayStatis);
	}

}
