package com.lanking.cloud.job.correctQuestionDistribute.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectQuestionService;
import com.lanking.cloud.job.correctQuestionDistribute.service.CorrectUserService;
import com.lanking.cloud.job.correctQuestionDistribute.service.DistributeCorrectQuestionService;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestion;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionCategory;
import com.lanking.microservice.domain.yoocorrect.CorrectQuestionType;
import com.lanking.microservice.domain.yoocorrect.CorrectUser;
import com.lanking.microservice.domain.yoocorrect.CorrectUserPool;
import com.lanking.microservice.domain.yoocorrect.CorrectUserType;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Description:题目派发接口
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月7日
 * @since 小优秀快批
 */
@Slf4j
@Service
public class DistributeCorrectQuestionServiceImpl implements DistributeCorrectQuestionService {

	private final static int FILL_BLANK_TIME_OUT = 160 * 1000;
	private final static int QUESTION_ANSWERING_TIME_OUT = 340 * 1000;
	@Autowired
	private CorrectQuestionService correctQuestionService;

	@Autowired
	private CorrectUserService correctUserService;

	/**
	 * 小优快批中待批改池中题目派发算法见如下链接
	 * 
	 * @see {@link http://dev.proto.elanking.com:8088/yoomath-correct/V1.0/#g=1&p=人工批改逻辑}
	 */
	@Override
	public void distributeCorrectQuestions() {

		// 查询所有等待批改的批改员（包括管理员），按信用值倒序
		List<CorrectUser> userList = correctUserService.queryWaitToCoorecUsersByType(null);
		if (CollectionUtils.isNotEmpty(userList)) {

			// 全部按管理员查找待批改的习题（全分类）
			List<CorrectQuestion> correctQuestions = correctQuestionService
					.queryCorrectQuestionsToAdmin(userList.size() + 20);

			List<Long> exqids = Lists.newArrayList(); // 需要排除搜索的题目
			Map<Long, CorrectUser> assignedMap = Maps.newHashMap(); // 已派题用户
			for (int i = 0; i < correctQuestions.size(); i++) {
				CorrectQuestion question = correctQuestions.get(i);
				for (int j = 0; j < userList.size(); j++) {
					CorrectUser user = userList.get(j);
					if (assignedMap.get(user.getId()) != null) {
						continue;
					}

					if ((question.getCategory() == CorrectQuestionCategory.ANSWER_QUESTION_APPEAL
							|| question.getCategory() == CorrectQuestionCategory.BLANK_QUESTION_APPEAL
							|| question.getIsError()) && user.getCorrectUserType() == CorrectUserType.ADMIN) {
						// 如果是申诉、反馈题不能将题目再次分配给上次批改人
						// 历史批改人
						List<Long> historyCorrectUserList = question.getCorrectUserIds();
						if (null != historyCorrectUserList) {
							int size = historyCorrectUserList.size();
							if (size > 0) {
								// 上一次批改人
								long lastCorrectUserId = historyCorrectUserList.get(size - 1);
								if (user.getId() == lastCorrectUserId) {
									continue;
								}
							}
						}
						// 只有管理员能分配申诉题、反馈题
						exqids.add(question.getId());
						assignedMap.put(user.getId(), user);
						correctQuestionService.distributeCorrectQuestion(question, user);
						log.info("[-paiti-][1] uid=" + user.getId() + ",qid=" + question.getId());
						break;
					} else if ((question.getCategory() == CorrectQuestionCategory.BLANK_QUESTION_UNKNOW
							|| question.getCategory() == CorrectQuestionCategory.ANSWER_QUESTION)
							&& !question.getIsError()) {
						// 普通题目任何批改员都能分配
						exqids.add(question.getId());
						assignedMap.put(user.getId(), user);
						correctQuestionService.distributeCorrectQuestion(question, user);
						log.info("[-paiti-][2] uid=" + user.getId() + ",qid=" + question.getId());
						break;
					}
				}
			}

			// 剩余未分配到题目的普通批改员，除非题目不够数，否则上一步骤中管理员肯定能分配到题目
			if (assignedMap.size() < userList.size() && correctQuestions.size() >= userList.size()) {
				// 全部按普通批改员取题
				correctQuestions = correctQuestionService
						.queryCorrectQuestionsToTeacher(userList.size() - assignedMap.size(), exqids);
				for (int i = 0; i < correctQuestions.size(); i++) {
					CorrectQuestion question = correctQuestions.get(i);
					for (int j = 0; j < userList.size(); j++) {
						CorrectUser user = userList.get(j);
						if (assignedMap.get(user.getId()) != null) {
							continue;
						}

						assignedMap.put(user.getId(), user);
						correctQuestionService.distributeCorrectQuestion(question, user);
						log.info("[-paiti-][3] uid=" + user.getId() + ",qid=" + question.getId());
						break;
					}
				}
			}
		}
	}

	@Override
	public void distributeCorrectQuestions2() {

		// 查询所有等待批改的批改员（包括管理员），按信用值倒序
		List<CorrectUser> userList = correctUserService.queryWaitToCoorecUsersByType(null);
		int questionCount = 0; // 查询出的待分配题目数
		int assignedUserCount = 0;// 已派题的用户数
		int waitGetQuestionUserCount = 0;// 剩余等待派题的批改员数量
		Map<Long, CorrectUser> assignedMap = Maps.newHashMap(); // 已派题用户
		int index = 1;
		if (CollectionUtils.isNotEmpty(userList)) {
			do {
				waitGetQuestionUserCount = userList.size() - assignedUserCount;
				Page<CorrectQuestion> page = correctQuestionService.queryCorrectQuestions(P.index(index, waitGetQuestionUserCount+20));
				List<CorrectQuestion> correctQuestions = page.getItems();
				questionCount = correctQuestions.size();
				for (int i = 0; i < correctQuestions.size(); i++) {
					CorrectQuestion question = correctQuestions.get(i);
					for (int j = 0; j < userList.size(); j++) {
						CorrectUser user = userList.get(j);
						// 如果该用户已经分配了题目
						if (assignedMap.get(user.getId()) != null) {
							continue;
						}
						CorrectQuestionCategory questionCategory = question.getCategory();
						Integer trustRank = user.getTrustRank();// 批改员信任值

						if (questionCategory == CorrectQuestionCategory.ANSWER_QUESTION_APPEAL
								|| questionCategory == CorrectQuestionCategory.BLANK_QUESTION_APPEAL
								|| question.getIsError()) {
							/*
							 * 申诉和反馈题的处理
							 */
							if (CorrectUserType.ADMIN == user.getCorrectUserType()) {
								if (questionCategory == CorrectQuestionCategory.ANSWER_QUESTION_APPEAL) {
									if (trustRank <= 70) {
										// 批改员信任值小于70的不能批改解答题
										continue;
									} else if (isPreCorrectUser(question, user)) {
										continue;
									} else {
										assignedMap.put(user.getId(), user);
										correctQuestionService.distributeCorrectQuestion(question, user);
										break;
									}
								} else {
									if (isPreCorrectUser(question, user)) {
										continue;
									} else {
										assignedMap.put(user.getId(), user);
										correctQuestionService.distributeCorrectQuestion(question, user);
										break;
									}
								}
							} else if (CorrectUserType.TEACHER == user.getCorrectUserType()) {
								// 普通批改员不能批改申诉和反馈题
								continue;
							}
						} else if ((questionCategory == CorrectQuestionCategory.BLANK_QUESTION_UNKNOW
								|| questionCategory == CorrectQuestionCategory.ANSWER_QUESTION)
								&& !question.getIsError()) {
							/*
							 * 普通题的处理，普通题所有批改员都可以批改(普通批改员和管理员)
							 */
							if (questionCategory == CorrectQuestionCategory.ANSWER_QUESTION) {
								if (trustRank <= 70) {
									// 批改员信任值小于70的不能批改解答题
									continue;
								} else {
									assignedMap.put(user.getId(), user);
									correctQuestionService.distributeCorrectQuestion(question, user);
									break;
								}
							} else {
								assignedMap.put(user.getId(), user);
								correctQuestionService.distributeCorrectQuestion(question, user);
								break;
							}
						}
					}
				}
				assignedUserCount = assignedMap.size();
				index++;
				log.info("[-paiti-][3] assignedUserCount=" + assignedUserCount + ",questionCount=" + questionCount
						+ ",waitGetQuestionUserCount=" + waitGetQuestionUserCount);
			} while (assignedUserCount < userList.size() && questionCount >= (waitGetQuestionUserCount+20));
			log.info("[-paiti-][11] success !!!");
		}
	}

	/**
	 * 是否是上次的批改员. 如果是申诉、反馈题不能将题目再次分配给上次批改人.
	 * 
	 * @param question
	 * @param user
	 * @return
	 */
	private boolean isPreCorrectUser(CorrectQuestion question, CorrectUser user) {
		// 历史批改人
		List<Long> historyCorrectUserList = question.getCorrectUserIds();
		if (null != historyCorrectUserList) {
			int size = historyCorrectUserList.size();
			if (size > 0) {
				// 上一次批改人
				long lastCorrectUserId = historyCorrectUserList.get(size - 1);
				if (user.getId() == lastCorrectUserId) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional("yooCorrectTransactionManager")
	public void checkTimeoutCorrectQuestionInfos() {
		List<CorrectUserPool> userPoolList = correctUserService.findHasDistributedUsers();
		if (null != userPoolList) {
			// 超时未处理的批改用户池id集合
			List<Long> timeOutUserPoolList = new ArrayList<Long>();
			// 超时未处理的待批改题目id集合
			List<Long> timeoutCorrectQestionList = new ArrayList<Long>();
			for (CorrectUserPool userPool : userPoolList) {
				Date startAtDate = userPool.getStartAt();
				if (null == startAtDate) {
					continue;
				}
				long startAt = userPool.getStartAt().getTime();
				if (userPool.getType() == CorrectQuestionType.FILL_BLANK) {
					if (System.currentTimeMillis() - startAt >= FILL_BLANK_TIME_OUT) {
						timeOutUserPoolList.add(userPool.getId());
						timeoutCorrectQestionList.add(userPool.getCorrectQuestionId());
					}
				} else if (userPool.getType() == CorrectQuestionType.QUESTION_ANSWERING) {
					if (System.currentTimeMillis() - startAt >= QUESTION_ANSWERING_TIME_OUT) {
						timeOutUserPoolList.add(userPool.getId());
						timeoutCorrectQestionList.add(userPool.getCorrectQuestionId());
					}
				}
			}
			// 将超时未处理的题目退回到待批改池重新分配
			if (timeOutUserPoolList.size() > 0 && timeoutCorrectQestionList.size() > 0) {
				log.info("[-qingti-][11],poolids="+JSON.toJSONString(timeOutUserPoolList)+",questionIds="+JSON.toJSONString(timeoutCorrectQestionList));
				correctUserService.deleteTimeOutCorrectUserPools(timeOutUserPoolList);
				correctQuestionService.sendBackCorrectQuestions(timeoutCorrectQestionList);
			}
		}
	}

	@Override
	public void clearAbnormalCorrectQuestions() {
		List<CorrectQuestion> questionList = correctQuestionService.queryAbnormalCorrectQuestions();
		if(null != questionList && questionList.size()>0){
			List<Long> abnormalQuestionList = new ArrayList<Long>();
			for(CorrectQuestion question : questionList){
				if(question.getAllotAt()!=null){
					long startAt = question.getAllotAt().getTime();
					if (question.getType() == CorrectQuestionType.FILL_BLANK) {
						if (System.currentTimeMillis() - startAt >= FILL_BLANK_TIME_OUT) {
							
							abnormalQuestionList.add(question.getId());
						}
					} else if (question.getType() == CorrectQuestionType.QUESTION_ANSWERING) {
						if (System.currentTimeMillis() - startAt >= QUESTION_ANSWERING_TIME_OUT) {
							abnormalQuestionList.add(question.getId());
						}
					}
				}
			}
			if(abnormalQuestionList.size()>0){
				log.info("[-clearAbnormal-][11] ,qids="+JSON.toJSONString(abnormalQuestionList));
				correctQuestionService.clearAbnormalCorrectQuestions(abnormalQuestionList);
			}
		}
		
	}
}