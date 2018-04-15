package com.lanking.uxb.service.examactivity001.api.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Q;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01GiftService;

/**
 * 期末考试活动礼包相关接口
 * 
 * @author qiuxue.jiang
 *
 * @version 2017年12月27日
 */
@Service
@Transactional(readOnly = true)
public class ExamActivity01GiftServiceImpl implements ExamActivity01GiftService {
	
	@Autowired
	@Qualifier("ExamActivity001QRepo")
	private Repo<ExamActivity001Q, Long> qRepo;
	
	@Autowired
	@Qualifier("ExamActivity001UserRepo")
	private Repo<ExamActivity001User, Long> userRepo;
	
	@Autowired
	@Qualifier("ExamActivity001UserQRepo")
	private Repo<ExamActivity001UserQ, Long> userQRepo;

	@Override
	public Long getGiftCount(Long code, Long userId) {
		Params params = Params.param("code", code);
		
		params.put("userId", userId);
		
		return userQRepo.find("$ymCountGift", params).get(Long.class);
	}

	@Override
	public List<ExamActivity001UserQ> getGifts(Long code, Long userId) {
		Params params = Params.param("code", code);
		
		params.put("userId", userId);
		
		return userQRepo.find("$ymGetGifts", params).list();
	}

	@Override
	@Transactional
	public void saveQQ(Long code, Long userId,String qq) {
		Params params = Params.param("code", code);
		
		params.put("userId", userId);
		
		ExamActivity001User user = userRepo.find("$ymFindUser", params).get(ExamActivity001User.class);
		
		
		if(user != null){
			user.setQq(qq);
			userRepo.save(user);
		}
	}
	
	/*
	 * 给用户添加礼包，直接生成奖品，如果该用户还没有资料记录需要新增资料记录
	 * 如果用户已经有资料记录需要更新资料记录
	 */
	@Override
	@Transactional
	public void addGift(Long code, Long userId) {
		Date currentDay = new Date();
		
		Params params = Params.param("code", code);
		
		params.put("userId", userId);
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		
		String dateString = formatter.format(currentDay);
		
		String dateBegin = dateString + " 00:00:00";
		String dateEnd = dateString + " 23:59:59";
		
		params.put("dateBegin", dateBegin);
		params.put("dateEnd", dateEnd);
		
		ExamActivity001UserQ userQ = userQRepo.find("$ymTodayGift", params).get(ExamActivity001UserQ.class);
		
		//说明已经有礼包了，一天只能有一个，直接返回
		if(userQ != null){
			return;
		}
		
		// 当天没有礼包，增加一个，并抽奖
		userQ = new ExamActivity001UserQ();
		
		userQ.setActivityCode(code);
		userQ.setUserId(userId);
		userQ.setCreateAt(new Date());
		userQ.setViewed(0);
		
		Params prizesParam = Params.param("code", code);
		prizesParam.put("day", dateString);
		
		ExamActivity001Q win = null;
		
		List<ExamActivity001Q> prizes = qRepo.find("$ymFindPrizes", prizesParam).list();
		// 没有奖品设置成谢谢参与
		if (CollectionUtils.isEmpty(prizes)) {
			//没有奖品了，设置为0
			userQ.setValue0(0);

			userQRepo.save(userQ);
		} else {
			// 抽奖
			win = draw(prizes);
			if (win == null) {
				//没有中奖，设置为0
				userQ.setValue0(0);
	
				userQRepo.save(userQ);
			} else {
				Params winParam = Params.param();
				winParam.put("id", win.getId());
				int updateSize = qRepo.execute("$ymUpdatePrizesCost", winParam);
				if (updateSize == 0) {
					// 说明奖品被别人抽走
					userQ.setValue0(0);
	
					userQRepo.save(userQ);
				} else {
					userQ.setValue0(win.getValue0());
	
					userQRepo.save(userQ);
				}
			}
		}
		
		Params userParams = Params.param("code", code);
		
		userParams.put("userId", userId);
		
		ExamActivity001User user = userRepo.find("$ymFindUser", userParams).get(ExamActivity001User.class);
		
		//新增一条记录
		if(user == null){
			user = new ExamActivity001User();
			user.setActivityCode(code);
			user.setUserId(userId);
			user.setQ_num(1);
			user.setViewQNum(0);
			user.setReceived(0);
			userRepo.save(user);
		} else {
			user.setQ_num(user.getQ_num() + 1);
			userParams = Params.param("id", user.getId());
			
			userParams.put("qNum", user.getQ_num());
			
			userRepo.find("$ymUpdateUserQNum", userParams).execute();
		}
		
	}
	
	/*
	 * 用户确认礼包，需要更新资料记录
	 */
	@Override
	@Transactional
	public void confirmGift(Long giftId) {
		ExamActivity001UserQ lottery = userQRepo.get(giftId);
		if (lottery == null) {
			return;
		}

		lottery.setViewed(1);
		lottery.setViewAt(new Date());

		userQRepo.save(lottery);
		
		Params userParams = Params.param("code", lottery.getActivityCode());
		
		userParams.put("userId", lottery.getUserId());
		
		ExamActivity001User user = userRepo.find("$ymFindUser", userParams).get(ExamActivity001User.class);
		
		if(user != null){
			user.setViewQNum(user.getViewQNum() + 1);
			user.setValue0(user.getValue0() + lottery.getValue0());
			
			userRepo.save(user);
		}
	}
	
	/**
	 * 抽奖算法
	 * 
	 * @param prizes
	 * @return 奖品 null说明没中奖
	 */
	private ExamActivity001Q draw(List<ExamActivity001Q> prizes) {
		ExamActivity001Q data = null;
		double rand = Math.random() * 100;
		double tempMin = 0;
		double tempMax = 0;

		for (ExamActivity001Q prize : prizes) {
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
	public ExamActivity001UserQ getGift(Long id) {
		return userQRepo.get(id);
	}

}
