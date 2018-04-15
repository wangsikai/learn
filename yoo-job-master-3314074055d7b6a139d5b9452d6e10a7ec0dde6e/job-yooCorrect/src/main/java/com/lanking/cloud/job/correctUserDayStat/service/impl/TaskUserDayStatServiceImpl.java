package com.lanking.cloud.job.correctUserDayStat.service.impl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectQuestionDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserMonthStatDAO;
import com.lanking.cloud.job.correctUserDayStat.DAO.CorrectUserStatDAO;
import com.lanking.cloud.job.correctUserDayStat.service.TaskUserDayStatService;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionType;
import com.lanking.microservice.domain.yoocorrect.CorrectUserStat;

@Transactional("yooCorrectTransactionManager")
@Service
public class TaskUserDayStatServiceImpl implements TaskUserDayStatService {

	@Qualifier("UserDayStatUserDAO")
	@Autowired
	private CorrectUserDAO userDAO;
	@Qualifier("StatCorrectUserStatDAO")
	@Autowired
	private CorrectUserStatDAO correctUserStatDAO;
	@Qualifier("StatCorrectQuestionDAO")
	@Autowired
	private CorrectQuestionDAO correctQuestionDAO;
	@Qualifier("StatCorrectUserMonthStatDAO")
	@Autowired
	private CorrectUserMonthStatDAO correctUserMonthStatDAO;

	@SuppressWarnings("rawtypes")
	@Override
	public CursorPage<Long, Map> queryUserId(int fetchCount, long cursor) {
		return userDAO.queryUserId(CP.cursor(cursor, fetchCount));
	}

	@Override
	public void statUserCorrect(List<Long> userIds) {
		// Date firstDay = getFirstOfMonth();
		for (Long userId : userIds) {
			CorrectUserStat correctUserStat = correctUserStatDAO.get(userId);
			if (correctUserStat == null) {
				correctUserStat = new CorrectUserStat();
				correctUserStat.setUserId(userId);
				correctUserStat.setCreateAt(new Date());
				correctUserStat.setUpdateAt(new Date());
			} else {
				correctUserStat.setUpdateAt(new Date());
			}

			Integer blankCount = 0; // 填空题批改次数
			BigDecimal blankTotalTime = BigDecimal.ZERO; // 填空总批改用时
			Integer answerCount = 0; // 解答题批改次数
			BigDecimal answerTotalTime = BigDecimal.ZERO; // 解答总批改用时
			Integer blankErrorCount = 0; // 填空题错改数
			Integer answerErrorCount = 0; // 解答题错改数

			// 查询当月数据
			List<CorrectQuestion> monthCorrects = correctQuestionDAO.getCompleteQuestionsByUserId(userId, null, null);
			for (CorrectQuestion value : monthCorrects) {
				if (value.getType() == CorrectQuestionType.FILL_BLANK) {
					blankCount++;
					blankTotalTime = blankTotalTime.add(new BigDecimal(value.getCostTime()));
					if (value.getReduceFee() != null) {
						blankErrorCount++;
					}
				} else if (value.getType() == CorrectQuestionType.QUESTION_ANSWERING) {
					answerCount++;
					answerTotalTime = answerTotalTime.add(new BigDecimal(value.getCostTime()));
					if (value.getReduceFee() != null) {
						answerErrorCount++;
					}
				}
			}

			// 查询历史数据
			// 暂时不通过历史数据统计
			// List<CorrectUserMonthStat> monthStats =
			// correctUserMonthStatDAO.getAllByUser(userId);
			// for (CorrectUserMonthStat value : monthStats) {
			// blankCount += value.getBlankQuestionCount();
			// blankErrorCount += value.getBlankQuestionErrorCount();
			// answerCount += value.getAnswerQuestionCount();
			// answerErrorCount += value.getAnswerQuestionErrorCount();
			// blankTotalTime.add(new BigDecimal(value.getBlankQuestionCount() *
			// value.getBlankQuestionCostTime()));
			// answerTotalTime.add(new BigDecimal(value.getAnswerQuestionCount()
			// * value.getBlankQuestionCostTime()));
			// }

			// 填空题平均批改用时
			Integer blankQuestionTime = null;
			if (blankCount > 0) {
				blankQuestionTime = blankTotalTime.divide(new BigDecimal(blankCount), BigDecimal.ROUND_HALF_UP)
						.intValue();
			}
			// 解答题平均批改用时
			Integer answerQuestionTime = null;
			if (answerCount > 0) {
				answerQuestionTime = answerTotalTime.divide(new BigDecimal(answerCount), BigDecimal.ROUND_HALF_UP)
						.intValue();
			}
			// 填空题平均错改率
			BigDecimal blankQuestionErrorRate = null;
			if (blankErrorCount > 0) {
				blankQuestionErrorRate = new BigDecimal(blankErrorCount).divide(new BigDecimal(blankCount), 2,
						BigDecimal.ROUND_HALF_UP);
			}
			// 解答题平均错改率
			BigDecimal answerQuestionErrorRate = null;
			if (answerErrorCount > 0) {
				answerQuestionErrorRate = new BigDecimal(answerErrorCount).divide(new BigDecimal(answerCount), 2,
						BigDecimal.ROUND_HALF_UP);
			}
			// 总批改题数（有实时统计）
			// Long correctCount = Long.valueOf(blankCount) +
			// Long.valueOf(answerCount);

			correctUserStat.setBlankQuestionTime(blankQuestionTime);
			correctUserStat.setAnswerQuestionTime(answerQuestionTime);
			correctUserStat.setBlankQuestionErrorRate(blankQuestionErrorRate);
			correctUserStat.setAnswerQuestionErrorRate(answerQuestionErrorRate);
			// correctUserStat.setCorrectCount(correctCount);

			correctUserStatDAO.save(correctUserStat);
		}
	}

	/**
	 * 取月初
	 */
	private Date getFirstOfMonth() {
		// 本月的第一天
		LocalDate firstday = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
		String first = firstday.toString() + " 00:00:00";

		Date date = new Date();
		try {
			DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			date = format1.parse(first);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date;
	}

}
