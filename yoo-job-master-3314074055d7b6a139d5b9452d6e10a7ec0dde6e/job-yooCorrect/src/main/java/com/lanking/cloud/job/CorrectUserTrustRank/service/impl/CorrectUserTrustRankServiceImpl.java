package com.lanking.cloud.job.CorrectUserTrustRank.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.CorrectUserTrustRank.DAO.CorrectUserTrustRankStatLogDao;
import com.lanking.cloud.job.CorrectUserTrustRank.service.CorrectUserTrustRankService;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserService;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectBillsDAO;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.microservice.domain.yoocorrect.CorrectBillType;
import com.lanking.microservice.domain.yoocorrect.CorrectBills;
import com.lanking.microservice.domain.yoocorrect.TrustRankStatLog;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月26日
 * @since 小优秀快批
 */
@Slf4j
@Service
public class CorrectUserTrustRankServiceImpl implements CorrectUserTrustRankService {
	// 习题个数统计周期（每统计满一个周期，教师的习题批改错误记录清空，重新在下一个周期进行统计）
	private static final int CLEAR_PERIOD = 5000;
	// 每一个习题个数统计周期内教师允许批改错误的习题个数（每超过1题扣信用值1分）
	private static final int ALLOW_CORRECT_ERROR_COUNT = 20;
	// 查询分页大小
	private static final int PAGE_SIZE = 50;
	@Autowired
	private CorrectBillsDAO correctBillsDao;
	@Autowired
	private CorrectUserTrustRankStatLogDao trustRankStatLogDao;
	@Autowired
	private CorrectUserService correctUserService;

	@Override
	@Transactional("yooCorrectTransactionManager")
	public void doTrustRankUpdate() {
		List<Long> correctUserIdList = correctUserService.findAllCorrectUserIds();
		if (null != correctUserIdList && correctUserIdList.size() > 0) {
			for (Long correctUserId : correctUserIdList) {
				TrustRankStatLog trustRankStatLog = trustRankStatLogDao.getByCorrectUserId(correctUserId);
				if (null == trustRankStatLog) {
					trustRankStatLog = add(correctUserId);
				}
				Long trustRankStatLogId = trustRankStatLog.getId();
				// 上次查询的数据库偏移量
				int offSet = trustRankStatLog.getQueryOffset();
				// 每5000题为一个信任值统计更新周期
				int needQueryCount = CLEAR_PERIOD - (offSet % CLEAR_PERIOD);// 还需要往后面查询的题目数
				int pageSize = PAGE_SIZE;
				if (needQueryCount < PAGE_SIZE) {
					pageSize = needQueryCount;
				}
				int totalCount = 0;// 已经查询的记录数
				// 接着上次查询的数据库偏移量接着往后查询，直到查询满5000道题
				Page<CorrectBills> page = correctBillsDao.query(correctUserId, P.offset(offSet, pageSize));
				// 错改题目数
				int currectErrorCount = 0;
				while (null != page && null != page.getItems()) {
					List<CorrectBills> billsList = page.getItems();
					List<Long> bizIds = new ArrayList<Long>();
					for (CorrectBills bill : billsList) {
						bizIds.add(bill.getBizId());
					}
					if(bizIds.size()>0){
						currectErrorCount+=correctBillsDao.getCorrectErrorCount(correctUserId, bizIds);
					}
					int count = billsList.size();
					totalCount += count;
					needQueryCount -= count;
					if (needQueryCount > 0 && count == pageSize) {
						offSet += count;
						if (needQueryCount < PAGE_SIZE) {
							pageSize = needQueryCount;
						}
						page = correctBillsDao.query(correctUserId, P.offset(offSet, pageSize));
					} else {
						log.info("CorrectUserTrustRankServiceImpl->userId=" + correctUserId + ",currentErrorCount="
								+ currectErrorCount + ",totalCount=" + totalCount);
						trustRankStatLog = trustRankStatLogDao.update(trustRankStatLogId, totalCount,
								currectErrorCount);
						Integer value = calculateTrusRankValue(trustRankStatLog);
						log.info("CorrectUserTrustRankServiceImpl->userId=" + correctUserId + ",value=" + value
								+ ",queryOffSet=" + trustRankStatLog.getQueryOffset());
						if (value != 0) {
							correctUserService.updateTrustRank(correctUserId, value);
						}
						if (trustRankStatLog.getQueryOffset() % CLEAR_PERIOD == 0) {
							trustRankStatLogDao.clear(trustRankStatLogId);
						}
						break;
					}
				}

			}
		}

	}

	/*
	 * 计算需要剪掉的信任值
	 */
	private Integer calculateTrusRankValue(TrustRankStatLog trustRankStatLog) {
		Integer totalErrorCount = trustRankStatLog.getTotalErrorCount();
		if (totalErrorCount < ALLOW_CORRECT_ERROR_COUNT) {
			if (totalErrorCount + trustRankStatLog.getCurErrorCount() - ALLOW_CORRECT_ERROR_COUNT > 0) {
				return -(totalErrorCount + trustRankStatLog.getCurErrorCount() - ALLOW_CORRECT_ERROR_COUNT);
			}
		} else {
			return -trustRankStatLog.getCurErrorCount();
		}
		return 0;
	}

	private TrustRankStatLog add(Long correctUserId) {
		TrustRankStatLog trustRankStatLog = new TrustRankStatLog();
		trustRankStatLog.setCorrectUserId(correctUserId);
		trustRankStatLog.setQueryOffset(0);
		trustRankStatLog.setTotalErrorCount(0);
		trustRankStatLog.setCurErrorCount(0);
		return trustRankStatLogDao.add(trustRankStatLog);
	}
}
