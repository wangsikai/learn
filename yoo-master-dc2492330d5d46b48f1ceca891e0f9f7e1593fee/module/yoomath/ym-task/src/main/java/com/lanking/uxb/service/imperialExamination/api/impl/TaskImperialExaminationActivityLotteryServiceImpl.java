package com.lanking.uxb.service.imperialExamination.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperialExamination.api.TaskImperialExaminationActivityLotteryService;

@Service
@Transactional(readOnly = true)
public class TaskImperialExaminationActivityLotteryServiceImpl
		implements TaskImperialExaminationActivityLotteryService {

	@Autowired
	@Qualifier("ImperialExaminationActivityLotteryRepo")
	private Repo<ImperialExaminationActivityLottery, Long> repo;

	@Override
	@Transactional
	public ImperialExaminationActivityLottery addLottery(long userId, ImperialExaminationProcess process, long code) {
		// 先查询后添加
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		params.put("process", process.ordinal());
		ImperialExaminationActivityLottery lottery = repo.find("$findLotteryByUser", params).get();
		if (lottery != null) {
			return lottery;
		}

		lottery = new ImperialExaminationActivityLottery();
		lottery.setActivityCode(code);
		lottery.setName(null);
		lottery.setPrizesId(null);
		lottery.setProcess(process);
		lottery.setStatus(Status.ENABLED);
		lottery.setUserId(userId);
		lottery.setCreateAt(new Date());
		lottery.setReceived(0);

		return repo.save(lottery);
	}

	@Override
	@Transactional
	public void addLotterys(List<ImperialExaminationActivityLottery> lotterys) {
		repo.save(lotterys);
	}

}
