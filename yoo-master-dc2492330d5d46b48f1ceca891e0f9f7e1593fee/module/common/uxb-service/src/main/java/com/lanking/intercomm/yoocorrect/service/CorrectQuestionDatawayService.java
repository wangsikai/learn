package com.lanking.intercomm.yoocorrect.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.correct.CorrectQuestionFailRecord;
import com.lanking.cloud.domain.yoomath.homework.CorrectQuestionSource;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectQuestionDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.CorrectErrorQuestionData;
import com.lanking.intercomm.yoocorrect.dto.CorrectQuestionCategory;
import com.lanking.intercomm.yoocorrect.dto.CorrectQuestionData;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;

import lombok.extern.slf4j.Slf4j;

/**
 * 小悠快批-题目批改服务通道.
 * 
 * @author wanlong.che
 *
 */
@Slf4j
@Component
public class CorrectQuestionDatawayService {

	@Autowired
	private StudentHomeworkQuestionService studentHomeworkQuestionService;

	@Autowired
	private CorrectQuestionDatawayClient correctQuestionDatawayClient;

	@Autowired
	private CorrectQuestionFailRecordService correctQuestionFailRecordService;

	@Autowired
	@Qualifier("executor")
	private Executor executor;

	/**
	 * 传送需要小悠快批批改的题目.
	 * 
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 */
	public void sendCorrectQuestion(long studentId, Collection<Long> studentHomeworkQuestionIds, boolean isAppear) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				sendCorrectQuestionThread(studentId, studentHomeworkQuestionIds, isAppear);
			}
		});
	}

	// 传送批改题目
	private void sendCorrectQuestionThread(long studentId, Collection<Long> studentHomeworkQuestionIds,
			boolean isAppear) {
		List<StudentHomeworkQuestion> studentHomeworkQuestions = studentHomeworkQuestionService
				.mgetList(studentHomeworkQuestionIds);
		List<CorrectQuestionData> datas = new ArrayList<CorrectQuestionData>(studentHomeworkQuestions.size());
		for (StudentHomeworkQuestion shq : studentHomeworkQuestions) {
			if (shq.getType() != Type.FILL_BLANK && shq.getType() != Type.QUESTION_ANSWERING) {
				// 非填空解答题
				continue;
			}
			if (shq.getType() == Type.FILL_BLANK && shq.getResult() != null
					&& shq.getResult() == HomeworkAnswerResult.INIT) {
				// 填空题要先过自动批改
				continue;
			}
			if (shq.getResult() != null
					&& (shq.getResult() == HomeworkAnswerResult.RIGHT || shq.getResult() == HomeworkAnswerResult.WRONG)
					&& !isAppear) {
				// 有批改结果但又不是申诉题
				continue;
			}

			CorrectQuestionData data = new CorrectQuestionData();
			data.setBizId(shq.getId());
			data.setQuestionId(shq.getQuestionId());
			data.setStudentId(studentId);
			data.setType(shq.getType());
			if (shq.isNewCorrect()) {
				data.setSource(CorrectQuestionSource.AMEND);
			} else {
				data.setSource(CorrectQuestionSource.HOMEWORK);
			}
			if (shq.getType() == Type.FILL_BLANK) {
				data.setCategory(isAppear ? CorrectQuestionCategory.BLANK_QUESTION_APPEAL
						: CorrectQuestionCategory.BLANK_QUESTION_UNKNOW);
			} else if (shq.getType() == Type.QUESTION_ANSWERING) {
				data.setCategory(isAppear ? CorrectQuestionCategory.ANSWER_QUESTION_APPEAL
						: CorrectQuestionCategory.ANSWER_QUESTION);
			}
			datas.add(data);
		}

		if (datas.size() > 0) {

			// 调用小悠快批服务
			boolean isSuccess = false;
			try {
				Value value = correctQuestionDatawayClient.receiveCorrectQuestions(datas);
				if (value.getRet_code() == 0) {
					isSuccess = true;
				} else {
					log.error("传送小悠快批待批改题目出错，" + value.getRet_code());
				}
			} catch (Exception e) {
				log.error("传送小悠快批待批改题目出错，" + e.getMessage(), e);
			}

			if (!isSuccess) {
				// 保存至本地
				List<CorrectQuestionFailRecord> records = new ArrayList<CorrectQuestionFailRecord>();
				for (CorrectQuestionData data : datas) {
					CorrectQuestionFailRecord record = new CorrectQuestionFailRecord();
					record.setBizId(data.getBizId());
					record.setCategory(data.getCategory().getValue());
					record.setCreateAt(new Date());
					record.setFailCount(1);
					record.setQuestionId(data.getQuestionId());
					record.setSource(data.getSource().getValue());
					record.setStatus(Status.ENABLED);
					record.setStudentId(data.getStudentId());
					record.setType(data.getType());
					records.add(record);
				}
				correctQuestionFailRecordService.save(records);
			}
		}
	}

	/**
	 * 开始批改.
	 * 
	 * @param userId
	 *            快批用户ID
	 */
	public void startCorrect(long userId) {
		try {
			Value value = correctQuestionDatawayClient.startCorrect(userId);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 获取待批改的习题.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @return
	 */
	public CorrectQuestionData getCorrectQuestion(long userId) {
		try {
			Value value = correctQuestionDatawayClient.getCorrectQuestion(userId);
			CorrectQuestionData data = null;
			if (value.getRet_code() == 0) {
				if (value.getRet() == null) {
					return null;
				}
				data = JSON.parseObject(value.getRet().toString(), CorrectQuestionData.class);
			} else {
				throw new ZuoyeException(value.getRet_code());
			}
			return data;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 批改完成当前习题.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @param studentHomeworkQuestionId
	 *            学生作业习题ID
	 * @param costTime
	 *            花费时间
	 * @param rightRate
	 *            正确率
	 * @param statBills
	 *            是否统计财务数据
	 * @return
	 */
	public void correctComplete(long userId, long studentHomeworkQuestionId, int costTime, int rightRate,
			boolean statBills) {
		try {
			Value value = correctQuestionDatawayClient.correctComplete(userId, studentHomeworkQuestionId, costTime,
					rightRate, statBills);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 停止接收题目.
	 * 
	 * @param userId
	 *            快批用户ID
	 * @return
	 */
	public void stopCorrect(long userId) {
		try {
			Value value = correctQuestionDatawayClient.stopCorrect(userId);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 模拟完成.
	 * 
	 * @param userId
	 * @return
	 */
	public void mockComplete(long userId) {
		try {
			Value value = correctQuestionDatawayClient.mockComplete(userId);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 反馈题目.
	 * 
	 * @param userId
	 * @param studentHomeworkQuestionId
	 */
	public void feedbackQuestion(long userId, long studentHomeworkQuestionId) {
		try {
			Value value = correctQuestionDatawayClient.feedbackQuestion(userId, studentHomeworkQuestionId);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}

	/**
	 * 错改题.
	 * 
	 * @param correctErrorQuestionDatas
	 * @return
	 */
	public void errorCorrect(List<CorrectErrorQuestionData> correctErrorQuestionDatas) {
		try {
			Value value = correctQuestionDatawayClient.errorCorrect(correctErrorQuestionDatas);
			if (value.getRet_code() != 0) {
				throw new ZuoyeException(value.getRet_code());
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ZuoyeException(CoreExceptionCode.SERVER_EX, e);
		}
	}
}
