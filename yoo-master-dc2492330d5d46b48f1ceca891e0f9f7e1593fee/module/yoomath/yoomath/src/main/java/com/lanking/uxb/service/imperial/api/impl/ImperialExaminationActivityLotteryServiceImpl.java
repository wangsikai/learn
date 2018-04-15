package com.lanking.uxb.service.imperial.api.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityLottery;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityPrizes;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityLotteryService;

import httl.util.CollectionUtils;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityLotteryServiceImpl implements ImperialExaminationActivityLotteryService {

	@Autowired
	@Qualifier("ImperialExaminationActivityPrizesRepo")
	private Repo<ImperialExaminationActivityPrizes, Long> prizesRepo;
	@Autowired
	@Qualifier("ImperialExaminationActivityLotteryRepo")
	private Repo<ImperialExaminationActivityLottery, Long> repo;

	@Override
	@Transactional
	public ImperialExaminationActivityLottery addLottery(long userId, ImperialExaminationProcess process,
			UserType userType, long code) {
		ImperialExaminationActivityLottery lottery = new ImperialExaminationActivityLottery();
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
	public ImperialExaminationActivityLottery luckDraw(long code, long lotteryId, Integer room, UserType userType) {
		ImperialExaminationActivityLottery lottery = repo.get(lotteryId);
		if (lottery == null) {
			return null;
		}

		// 存在prizesId说明已经抽过
		if (lottery.getPrizesId() != null) {
			return lottery;
		}

		// 抽奖时间
		lottery.setLotteryAt(new Date());
		// 查询对应的奖品
		Params prizesParam = Params.param();
		prizesParam.put("code", code);
		prizesParam.put("process", lottery.getProcess().ordinal());
		prizesParam.put("room", room);
		prizesParam.put("userType", userType.getValue());
		
		List<ImperialExaminationActivityPrizes> prizes = prizesRepo.find("$findPrizes", prizesParam).list();
		// 没有奖品设置成谢谢参与
		if (CollectionUtils.isEmpty(prizes)) {
			lottery.setName("谢谢参与");
			lottery.setPrizesId(0L);

			return repo.save(lottery);
		}

		// 抽奖
		ImperialExaminationActivityPrizes win = draw(prizes);
		if (win == null) {
			lottery.setName("谢谢参与");
			lottery.setPrizesId(0L);

			return repo.save(lottery);
		} else {
			Params winParam = Params.param();
			winParam.put("id", win.getId());
			int updateSize = prizesRepo.execute("$updatePrizesCost", winParam);
			if (updateSize == 0) {
				// 说明奖品被别人抽走
				lottery.setName("谢谢参与");
				lottery.setPrizesId(0L);

				return repo.save(lottery);
			} else {
				lottery.setName(win.getName());
				lottery.setPrizesId(win.getId());

				return repo.save(lottery);
			}
		}
	}

	/**
	 * 抽奖算法
	 * 
	 * @param prizes
	 * @return 奖品 null说明没中奖
	 */
	private ImperialExaminationActivityPrizes draw(List<ImperialExaminationActivityPrizes> prizes) {
		ImperialExaminationActivityPrizes data = null;
		double rand = Math.random() * 100;
		double tempMin = 0;
		double tempMax = 0;

		for (ImperialExaminationActivityPrizes prize : prizes) {
			double awardsRate = 0; // 当前奖品的中奖概率
			if (prize.getAwardsRate() != null) {
				awardsRate = prize.getAwardsRate().doubleValue();
			}

			// 概率为0直接跳过
			if (awardsRate == 0) {
				continue;
			}

			tempMax += awardsRate;
			if (rand >= tempMin && rand <= tempMax) {
				data = prize;
				break;
			}
			tempMin += awardsRate;
		}

		return data;
	}

	@Override
	public ImperialExaminationActivityLottery get(long id) {
		return repo.get(id);
	}

	@Override
	@Transactional
	public ImperialExaminationActivityLottery confirmLottery(long id) {
		ImperialExaminationActivityLottery lottery = repo.get(id);
		if (lottery == null) {
			return null;
		}

		lottery.setStatus(Status.DISABLED);

		return repo.save(lottery);
	}

	@Override
	public ImperialExaminationActivityLottery getLotteryByUser(long code, long userId,
			ImperialExaminationProcess process, Status status) {
		Params params = Params.param();
		params.put("code", code);
		params.put("userId", userId);
		params.put("process", process.ordinal());
		params.put("status", status.getValue());

		return repo.find("$findLotteryByUser", params).get();
	}

	@Override
	@Transactional
	public ImperialExaminationActivityLottery luckDrawStudent(long code, long lotteryId, UserType userType) {
		ImperialExaminationActivityLottery lottery = repo.get(lotteryId);
		if (lottery == null) {
			return null;
		}

		// 存在prizesId说明已经抽过
		if (lottery.getPrizesId() != null) {
			return lottery;
		}

		// 抽奖时间
		lottery.setLotteryAt(new Date());
		// 查询对应的奖品
		Params prizesParam = Params.param();
		prizesParam.put("code", code);
		prizesParam.put("process", lottery.getProcess().ordinal());
		prizesParam.put("userType", userType.getValue());

		List<ImperialExaminationActivityPrizes> prizes = prizesRepo.find("$findPrizes", prizesParam).list();
		// 没有奖品设置成谢谢参与
		if (CollectionUtils.isEmpty(prizes)) {
			lottery.setName("谢谢参与");
			lottery.setPrizesId(0L);

			return repo.save(lottery);
		}

		// 抽奖
		ImperialExaminationActivityPrizes win = draw(prizes);
		if (win == null) {
			lottery.setName("谢谢参与");
			lottery.setPrizesId(0L);

			return repo.save(lottery);
		} else {
			Params winParam = Params.param();
			winParam.put("id", win.getId());
			int updateSize = prizesRepo.execute("$updatePrizesCost", winParam);
			if (updateSize == 0) {
				// 说明奖品被别人抽走
				lottery.setName("谢谢参与");
				lottery.setPrizesId(0L);

				return repo.save(lottery);
			} else {
				lottery.setName(win.getName());
				lottery.setPrizesId(win.getId());

				return repo.save(lottery);
			}
		}
	}

}
