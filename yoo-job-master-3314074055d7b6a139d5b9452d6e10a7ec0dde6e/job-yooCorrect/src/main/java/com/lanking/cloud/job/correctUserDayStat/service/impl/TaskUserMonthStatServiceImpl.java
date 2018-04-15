package com.lanking.cloud.job.correctUserDayStat.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectBillsDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectQuestionDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserMonthStatDAO;
import com.lanking.cloud.job.correctUserDayStat.service.TaskUserMonthStatService;
import com.lanking.microservice.domain.yoocorrect.CorrectBillType;
import com.lanking.microservice.domain.yoocorrect.CorrectBills;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionType;
import com.lanking.microservice.domain.yoocorrect.CorrectUserMonthStat;

@Transactional("yooCorrectTransactionManager")
@Service
public class TaskUserMonthStatServiceImpl implements TaskUserMonthStatService {

	@Qualifier("StatCorrectUserMonthStatDAO")
	@Autowired
	private CorrectUserMonthStatDAO correctUserMonthStatDAO;
	@Qualifier("StatCorrectQuestionDAO")
	@Autowired
	private CorrectQuestionDAO correctQuestionDAO;
	@Qualifier("StatCorrectBillsDAO")
	@Autowired
	private CorrectBillsDAO correctBillsDAO;

	@Override
	public void statUserCorrect(List<Long> userIds) {
		Date lastFirstDay = getLastMonthFirst();
		Date lastDay = getLastMonthLast();
		for (Long userId : userIds) {
			CorrectUserMonthStat correctUserMonthStat = correctUserMonthStatDAO.getByMonthDate(userId, lastFirstDay);
			if (correctUserMonthStat == null) {
				correctUserMonthStat = new CorrectUserMonthStat();
				correctUserMonthStat.setUserId(userId);
				correctUserMonthStat.setMonthDate(lastFirstDay);
				correctUserMonthStat.setCreateAt(new Date());
			}

			// 查询当月数据
			List<CorrectQuestion> monthCorrects = correctQuestionDAO.getCompleteQuestionsByUserId(userId, lastFirstDay,
					lastDay);
//			if (monthCorrects.isEmpty()) {
//				correctUserMonthStatDAO.save(correctUserMonthStat);
//				continue;
//			}

			List<Long> cqIds = monthCorrects.stream().map(p -> p.getId()).collect(Collectors.toList());
			// 流水
			List<CorrectBills> bills = Lists.newArrayList();
			if (!cqIds.isEmpty()) {
				bills = correctBillsDAO.getByCorrectQuestionIds(cqIds);
			}
			
			// 填空题批改数量
			Integer blankQuestionCount = 0;
			// 填空题错改数量
			Integer blankQuestionErrorCount = 0;
			// 填空题批改用时
			Integer blankCostTimeTotal = 0;
			// 解答题批改数量
			Integer answerQuestionCount = 0;
			// 解答题错改数量
			Integer answerQuestionErrorCount = 0;
			// 解答批改用时
			Integer answerCostTimeTotal = 0;
			// 填空题correctQuestionIds
			List<Long> blankCorrectQuestionIds = Lists.newArrayList();
			// 解答题correctQuestionIds
			List<Long> answerCorrectQuestionIds = Lists.newArrayList();

			for (CorrectQuestion value : monthCorrects) {
				if (value.getType() == CorrectQuestionType.FILL_BLANK) {
					blankQuestionCount++;
					blankCostTimeTotal += value.getCostTime();
					blankCorrectQuestionIds.add(value.getId());
					if (value.getReduceFee() != null) {
						blankQuestionErrorCount++;
					}
				} else if (value.getType() == CorrectQuestionType.QUESTION_ANSWERING) {
					answerQuestionCount++;
					answerCostTimeTotal += value.getCostTime();
					answerCorrectQuestionIds.add(value.getId());
					if (value.getReduceFee() != null) {
						answerQuestionErrorCount++;
					}
				}
			}
			
			// 填空题平均批改用时（秒）
			Integer blankQuestionCostTime = 0;
			if (blankQuestionCount > 0) {
				blankQuestionCostTime = new BigDecimal(blankCostTimeTotal)
						.divide(new BigDecimal(blankQuestionCount), BigDecimal.ROUND_HALF_UP).intValue();
			}
			// 解答题平均批改用时（秒）
			Integer answerQuestionCostTime = 0;
			if (answerQuestionCount > 0) {
				answerQuestionCostTime = new BigDecimal(answerCostTimeTotal)
						.divide(new BigDecimal(answerQuestionCount), BigDecimal.ROUND_HALF_UP).intValue();
			}
			// 填空题批改费用
			BigDecimal blankQuestionCorrectFee = BigDecimal.ZERO;
			// 解答题批改费用
			BigDecimal answerQuestionCorrectFee = BigDecimal.ZERO;
			// 奖励费用
			BigDecimal rewardFee = BigDecimal.ZERO;
			// 错改惩罚费用
			BigDecimal reduceFee = BigDecimal.ZERO;
			for (CorrectBills value : bills) {
				if (value.getBillType() == CorrectBillType.CORRECT) {
					for (Long correctQuestionId : blankCorrectQuestionIds) {
						if (correctQuestionId.longValue() == value.getBizId().longValue()) {
							blankQuestionCorrectFee = blankQuestionCorrectFee.add(value.getAmount());
							break;
						}
					}
					for (Long correctQuestionId : answerCorrectQuestionIds) {
						if (correctQuestionId.longValue() == value.getBizId().longValue()) {
							answerQuestionCorrectFee = answerQuestionCorrectFee.add(value.getAmount());
							break;
						}
					}
				} else if (value.getBillType() == CorrectBillType.REWARD) {
					rewardFee = rewardFee.add(value.getAmount());
				} else if (value.getBillType() == CorrectBillType.ERROR) {
					reduceFee = reduceFee.add(value.getAmount());
				}
			}
			
			correctUserMonthStat.setAnswerQuestionCorrectFee(answerQuestionCorrectFee);
			correctUserMonthStat.setAnswerQuestionCostTime(answerQuestionCostTime);
			correctUserMonthStat.setAnswerQuestionCount(answerQuestionCount);
			correctUserMonthStat.setAnswerQuestionErrorCount(answerQuestionErrorCount);
			correctUserMonthStat.setBlankQuestionCorrectFee(blankQuestionCorrectFee);
			correctUserMonthStat.setBlankQuestionCostTime(blankQuestionCostTime);
			correctUserMonthStat.setBlankQuestionCount(blankQuestionCount);
			correctUserMonthStat.setBlankQuestionErrorCount(blankQuestionErrorCount);
			correctUserMonthStat.setReduceFee(reduceFee);
			correctUserMonthStat.setRewardFee(rewardFee);
			
			correctUserMonthStatDAO.save(correctUserMonthStat);
		}
	}

	/**
	 * 取当月月初
	 */
	private Date getLastMonthFirst() {
		LocalDate date = LocalDate.now();
		LocalDateTime localtime = LocalDateTime.now();
		// 月初1号前5分钟继续统计上个月数据
		if (localtime.getDayOfMonth() == 1 && localtime.getHour() == 0 && localtime.getMinute() <= 5) {
			date = date.minusMonths(1);
		}
		LocalDate firstday = LocalDate.of(date.getYear(), date.getMonth(), 1);
		String first = firstday.toString() + " 00:00:00";

		Date result = new Date();
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			result = format1.parse(first);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 取当月月末
	 */
	private Date getLastMonthLast() {
		LocalDate date = LocalDate.now();
		LocalDateTime localtime = LocalDateTime.now();
		// 月初1号前5分钟继续统计上个月数据
		if (localtime.getDayOfMonth() == 1 && localtime.getHour() == 0 && localtime.getMinute() <= 5) {
			date = date.minusMonths(1);
		}
		LocalDate lastday = date.with(TemporalAdjusters.lastDayOfMonth());
		String last = lastday.toString() + " 23:59:59";

		Date result = new Date();
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			result = format1.parse(last);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return result;
	}
}
