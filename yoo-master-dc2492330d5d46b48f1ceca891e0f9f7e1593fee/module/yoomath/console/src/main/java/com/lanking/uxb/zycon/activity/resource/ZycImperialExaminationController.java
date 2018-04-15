package com.lanking.uxb.zycon.activity.resource;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityAwardService;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityLotteryUserService;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityRankService;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityService;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityUserService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;
import com.lanking.uxb.zycon.common.ex.YoomathConsoleException;

/**
 * 科举考试
 * 
 * @author zemin.song
 * @version 2017年3月30日
 */
@RestController
@RequestMapping(value = "zyc/activity")
public class ZycImperialExaminationController {

	@Autowired
	private ZycImperialExaminationActivityUserService zycImperialExaminationActivityUserService;
	@Autowired
	private ZycImperialExaminationActivityRankService zycImperialExaminationActivityRankService;
	@Autowired
	private ZycImperialExaminationActivityAwardService zycImperialExaminationActivityAwardService;
	@Autowired
	private ZycImperialExaminationActivityLotteryUserService zycImperialExaminationActivityLotteryUserService;
	@Autowired
	private ZycImperialExaminationActivityService zycImperialExaminationActivityService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHomeworkClazzConvert;

	/**
	 * 报名用户
	 * 
	 * @param code
	 *            活动code
	 * @return
	 */
	@RequestMapping(value = "queryUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryUser(ZycActivityUserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		Page<Map> cp = zycImperialExaminationActivityUserService.findActivityUser(form, P.index(page, pageSize));
		for (Map map : cp.getItems()) {
			List<Long> classIds = Lists.newArrayList();
			if (map.get("class_list") != null) {
				String strClazzs = map.get("class_list").toString();
				JSONArray jsonArray = JSONObject.parseArray(strClazzs);
				for (Object o : jsonArray) {
					classIds.add(Long.parseLong(o.toString()));
				}
			}

			List<VHomeworkClazz> clazzList = zyHomeworkClazzConvert.to(zyHomeworkClassService.mgetList(classIds));
			map.put("class_list", clazzList);
		}
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(cp.getItems());
		return new Value(vp);
	}

	/**
	 * 成绩排名
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryRank", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryActivityRank(ZycActivityUserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		ImperialExaminationActivity activity = zycImperialExaminationActivityService.get(form.getActivityCode());
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		ImperialExaminationProcessTime currentProcessTime = activity.getCfg().getCurretStage();
		List<ImperialExaminationProcessTime> pTimes = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime pTime : pTimes) {
			if (form.getType() == ImperialExaminationType.PROVINCIAL_EXAMINATION
					&& pTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4) {
				retMap.put("processTime", pTime);
				// 是否可以修改
				retMap.put("isEditor", currentProcessTime != null
						&& currentProcessTime.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4);
				break;
			}
			if (form.getType() == ImperialExaminationType.METROPOLITAN_EXAMINATION
					&& pTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
				retMap.put("processTime", pTime);
				retMap.put("isEditor", currentProcessTime != null
						&& currentProcessTime.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4);
				break;
			}
			if (form.getType() == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION
					&& pTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4) {
				retMap.put("processTime", pTime);
				retMap.put("isEditor", currentProcessTime != null
						&& currentProcessTime.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4);
				break;
			}
		}

		Page<Map> cp = zycImperialExaminationActivityRankService.queryActivityRank(form, P.index(page, pageSize));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		for (Map map : cp.getItems()) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("id", id);
		}
		vp.setItems(cp.getItems());
		retMap.put("page", vp);
		return new Value(retMap);
	}

	/**
	 * 修改分数
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateScore", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateScore(Long rankId, Integer manualScore,
			@RequestParam(defaultValue = "false") boolean isUpdateAward) {
		if (rankId == null || manualScore == null) {
			return new Value(new IllegalArgException());
		}
		// 判定是否可以修改
		ImperialExaminationActivityRank rank = zycImperialExaminationActivityRankService.get(rankId);
		ImperialExaminationActivity activity = zycImperialExaminationActivityService.get(rank.getActivityCode());
		ImperialExaminationProcessTime currentProcessTime = activity.getCfg().getCurretStage();
		// 当前时间不可修改
		if (currentProcessTime == null
				|| (rank.getType() == ImperialExaminationType.PROVINCIAL_EXAMINATION && currentProcessTime.getProcess() != ImperialExaminationProcess.PROCESS_PROVINCIAL4)
				|| (rank.getType() == ImperialExaminationType.METROPOLITAN_EXAMINATION && currentProcessTime
						.getProcess() != ImperialExaminationProcess.PROCESS_METROPOLITAN4)
				|| (rank.getType() == ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION && currentProcessTime
						.getProcess() != ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4)) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.ACTIVITY_UPDATEDATE_NO_NOW));
		}

		int ret = zycImperialExaminationActivityRankService.updateScore(rankId, manualScore);
		if (ret > 0) {
			// 更新当前综合排名
			if (isUpdateAward == true) {
				List<Map> clazzMap = zycImperialExaminationActivityAwardService.getActivityClazzScore(
						activity.getCode(), rank.getUserId());
				// 排名必须有
				Map firstClazzMap = clazzMap.get(0);
				Integer score = Integer.parseInt(firstClazzMap.get("avg_score").toString());
				Integer doTime = Integer.parseInt(firstClazzMap.get("avg_dotime").toString());
				Long clazzId = Long.parseLong(firstClazzMap.get("clazz_id").toString());
				ImperialExaminationActivityAward award = zycImperialExaminationActivityAwardService.get(
						activity.getCode(), rank.getUserId());
				zycImperialExaminationActivityAwardService.update(award.getId(), score, doTime, clazzId);
			}
			return new Value();
		} else {
			return new Value(new IllegalArgException());
		}
	}

	/**
	 * 最终排名
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "queryActivityAward", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryActivityAward(ZycActivityUserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		ImperialExaminationActivity activity = zycImperialExaminationActivityService.get(form.getActivityCode());
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(4);
		ImperialExaminationProcessTime currentProcessTime = activity.getCfg().getCurretStage();
		// 是否可以修改
		retMap.put("isEditor", currentProcessTime != null
				&& currentProcessTime.getProcess() == ImperialExaminationProcess.PROCESS_TOTALRANKING);
		List<ImperialExaminationProcessTime> pTimes = activity.getCfg().getTimeList();
		Date nowTime = new Date();
		for (ImperialExaminationProcessTime pTime : pTimes) {
			if (pTime.getProcess() == ImperialExaminationProcess.PROCESS_TOTALRANKING) {
				retMap.put("processTime", pTime);
				// 是否开始颁奖
				retMap.put("isStartAwards", nowTime.after(pTime.getStartTime()));
			}
		}
		Page<Map> cp = zycImperialExaminationActivityAwardService.queryActivityAward(form, P.index(page, pageSize));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		for (Map map : cp.getItems()) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("id", id);
		}
		vp.setItems(cp.getItems());
		retMap.put("page", vp);
		return new Value(retMap);
	}
	
	/**
	 * 查询中奖用户
	 * 
	 * @param
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "queryLotteryUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryLotteryUser(ZycActivityUserForm form, @RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int pageSize) {
		ImperialExaminationActivity activity = zycImperialExaminationActivityService.get(form.getActivityCode());
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		Page<Map> cp = zycImperialExaminationActivityLotteryUserService.queryActivityLotteryUser(form, P.index(page, pageSize));
		VPage<Map> vp = new VPage<Map>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		for (Map map : cp.getItems()) {
			Long id = ((BigInteger) map.get("id")).longValue();
			map.put("id", id);
		}
		vp.setItems(cp.getItems());
		retMap.put("page", vp);
		return new Value(retMap);
	}


	/**
	 * 冻结
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateActivityAwardStatus(Long awardId, Status status,Long code) {
		if (awardId == null || status == null) {
			return new Value(new IllegalArgException());
		}
		// 判定是否可以修改
		ImperialExaminationActivityAward award = zycImperialExaminationActivityAwardService.get(awardId);
		ImperialExaminationActivity activity = zycImperialExaminationActivityService.get(award.getActivityCode());
		ImperialExaminationProcessTime currentProcessTime = activity.getCfg().getCurretStage();
		// 当前时间不可修改
		if (currentProcessTime == null
				|| currentProcessTime.getProcess() != ImperialExaminationProcess.PROCESS_TOTALRANKING) {
			return new Value(new YoomathConsoleException(YoomathConsoleException.ACTIVITY_UPDATEDATE_NO_NOW));
		}
		Integer room = award.getRoom();
		zycImperialExaminationActivityAwardService.updateStatus(awardId, code, status,room);
		return new Value();
	}
	
	/**
	 * 修改领奖状态
	 * 
	 * @param
	 * 
	 * @return
	 */
	@RequestMapping(value = "updateLotteryUserStatus", method = { RequestMethod.POST, RequestMethod.GET })
	public Value updateLotteryUserStatus(Long awardId, Integer received) {
		if (awardId == null || received == null) {
			return new Value(new IllegalArgException());
		}
	
		zycImperialExaminationActivityLotteryUserService.updateReceived(awardId, received);
		return new Value();
	}

}
