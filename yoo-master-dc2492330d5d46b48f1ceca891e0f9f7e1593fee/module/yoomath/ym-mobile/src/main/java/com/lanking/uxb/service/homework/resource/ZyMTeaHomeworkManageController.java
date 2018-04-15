package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.HomeworkStatus;
import com.lanking.cloud.domain.type.StudentHomeworkStatus;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessageType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.code.core.CoreExceptionCode;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.NoPermissionException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.file.api.OFileService;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.homework.form.CorrectForm;
import com.lanking.uxb.service.homework.form.PertinenceType;
import com.lanking.uxb.service.homework.value.VWeakKonwledgePoint;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.HomeworkMessageService;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkQuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkAnswerConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.ex.HomeworkException;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkMessage;
import com.lanking.uxb.service.resources.value.VHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VWrongAnswer;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserActionService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.web.api.ZyTeaPertinenceHomeworkService;
import com.lanking.uxb.service.web.form.PertinenceHomeworkForm;
import com.lanking.uxb.service.web.resource.ZyTeaHomework2Controller;
import com.lanking.uxb.service.web.resource.ZyTeaHomeworkCorrectController;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyQuestionCarService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.ex.ZuoyeException;
import com.lanking.uxb.service.zuoye.form.TeaCorrectQuestionForm2;

/**
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年5月11日
 */
@RestController
@RequestMapping("zy/m/t/hk")
public class ZyMTeaHomeworkManageController {

	private Logger logger = LoggerFactory.getLogger(ZyMTeaHomeworkManageController.class);

	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private HomeworkQuestionService hkQuestionService;
	@Autowired
	private HomeworkQuestionConvert hkQuestionConvert;
	@Autowired
	private StudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private StudentHomeworkQuestionConvert stuHkQuestionConvert;
	@Autowired
	private StudentHomeworkAnswerService studentHkAnswerService;
	@Autowired
	private ZyStudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	private StudentHomeworkAnswerConvert stuHkAnswerConvert;
	@Autowired
	private ZyTeaHomework2Controller zyTeaHomework2Controller;
	@Autowired
	private ZyTeaHomeworkCorrectController zyTeaHomeworkCorrectController;
	@Autowired
	private UserActionService userActionService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ZyTeaPertinenceHomeworkService pertinenceHomeworkService;
	@Autowired
	private QiNiuFileService qiNiuFileService;
	@Autowired
	private OFileService fileService;
	@Autowired
	private DiagnosticClassKnowpointService diagKpService;
	@Autowired
	private UserService userService;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private ZyQuestionCarService questionCarService;
	@Autowired
	private StudentHomeworkQuestionConvert shqConvert;
	@Autowired
	private HomeworkMessageService hkMessageService;

	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long id) {
		Homework homework = hkService.get(id);
		if (homework == null || homework.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}
		// 不能查看别人布置的作业,转让过班级的可以
		// if (homework.getCreateId().longValue() != Security.getUserId()) {
		// return new Value(new NoPermissionException());
		// }
		VHomework v = hkConvert.to(homework);
		v.setHomeworkClazz(hkClassConvert.to(hkClassService.get(homework.getHomeworkClassId())));
		ValueMap vm = ValueMap.value();
		vm.put("homework", v);// 作业
		List<HomeworkQuestion> hqs = hkQuestionService.getHomeworkQuestion(id);
		List<VHomeworkQuestion> vhqs = hkQuestionConvert.to(hqs);
		Map<Long, VHomeworkQuestion> vhqsMap = new HashMap<Long, VHomeworkQuestion>(vhqs.size());
		for (VHomeworkQuestion vhq : vhqs) {
			vhqsMap.put(vhq.getQuestionId(), vhq);
		}

		// 增加答错学生和易错答案，已经下发才有此数据
		// 学生作业
		List<StudentHomework> studentHomeworks = stuHkService.listByHomework(id);
		Map<Long, StudentHomeworkAnswer> answerMap = getStudentAnswers(hqs, studentHomeworks);
		// 学生作业问题
		Map<Long, VStudentHomeworkQuestion> homeworkQuestions = getStudentHomeworkQuestion(vhqs, studentHomeworks);
		// vm.put("studentHomeworkQuestions", homeworkQuestions);

		// 取得学生加入班级的时间
		Map<Long, Long> studentJoinTime = getJoinClassTime(homework.getHomeworkClassId());
		// 学生信息
		Set<Long> studentIds = new HashSet<>();
		for (StudentHomeworkAnswer value : answerMap.values()) {
			studentIds.add(value.getAnswerId());
		}
		Map<Long, User> students = userService.getUsers(studentIds);
		if (homework.getStatus() == HomeworkStatus.ISSUED) {
			for (VHomeworkQuestion h : vhqs) {
				List<VStudentHomeworkAnswer> studentHomeworkAnswers = stuHkAnswerConvert
						.to(stuHkAnswerService.listByQuestionId(id, h.getQuestionId()));
				// 取得对象下的标准答案
				VQuestion vquestion = h.getQuestion();
				List<VAnswer> answers = vquestion.getAnswers();

				Map<String, Integer> fallibleAnswersMap = new HashMap<>();
				List<VWrongAnswer> fallibleAnswers = Lists.newArrayList();

				for (VStudentHomeworkAnswer sa : studentHomeworkAnswers) {
					if (!sa.getResult().equals(HomeworkAnswerResult.RIGHT)) {
						// 易错答案，选择和填空有
						if (Type.SINGLE_CHOICE.equals(vquestion.getType())
								|| Type.MULTIPLE_CHOICE.equals(vquestion.getType())) {
							StudentHomeworkAnswer studentHomeworkAnswer = answerMap.get(sa.getId());
							VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
									.to(studentHomeworkAnswer);
							VWrongAnswer wrongAnswer = new VWrongAnswer();
							BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
							wrongAnswer.setName(students.get(vstudentHomeworkAnswer.getAnswerId()).getName());
							// 重新设置imageContent,增加标签
							if (StringUtils.isEmpty(wrongAnswer.getContent())) {
								wrongAnswer.setImageContent("");
							} else {
								wrongAnswer
										.setImageContent(QuestionUtils.process(wrongAnswer.getContent(), false, true));
							}

							if (fallibleAnswersMap.containsKey(wrongAnswer.getContent())) {
								int count = fallibleAnswersMap.get(wrongAnswer.getContent()) + 1;
								fallibleAnswersMap.put(wrongAnswer.getContent(), count);

								// 设置错误次数
								wrongAnswer.setWrongCount(count);
								// 统计每个答案错误次数
								List<VWrongAnswer> newVStudentHomeworkAnswers = Lists.newArrayList();
								for (VWrongAnswer vsa : fallibleAnswers) {
									if (vsa.getContent().equals(wrongAnswer.getContent())) {
										vsa.setWrongCount(count);
									}
									newVStudentHomeworkAnswers.add(vsa);
								}

								fallibleAnswers.clear();
								fallibleAnswers.addAll(newVStudentHomeworkAnswers);
							} else {
								fallibleAnswersMap.put(wrongAnswer.getContent(), 1);
								wrongAnswer.setWrongCount(1);
							}

							fallibleAnswers.add(wrongAnswer);
						} else if (Type.FILL_BLANK.equals(vquestion.getType())) {
							// 填空统计的map加sequence
							StudentHomeworkAnswer studentHomeworkAnswer = answerMap.get(sa.getId());
							VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
									.to(studentHomeworkAnswer);
							VWrongAnswer wrongAnswer = new VWrongAnswer();
							BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
							wrongAnswer.setName(students.get(vstudentHomeworkAnswer.getAnswerId()).getName());
							// 重新设置imageContent,增加标签
							if (StringUtils.isEmpty(wrongAnswer.getContent())) {
								wrongAnswer.setImageContent("");
							} else {
								wrongAnswer
										.setImageContent(QuestionUtils.process(wrongAnswer.getContent(), false, true));
							}
							if (wrongAnswer.getContent() != null) {
								String content = wrongAnswer.getContent().replaceAll("<ux-mth>", "")
										.replaceAll("</ux-mth>", "");
								wrongAnswer.setContent(content);
							}

							String content = wrongAnswer.getContent() + "con" + wrongAnswer.getSequence();
							if (fallibleAnswersMap.containsKey(content)) {
								int count = fallibleAnswersMap.get(content) + 1;
								fallibleAnswersMap.put(content, count);

								// 设置错误次数
								wrongAnswer.setWrongCount(count);
								// 统计每个答案错误次数
								List<VWrongAnswer> newVStudentHomeworkAnswers = Lists.newArrayList();
								for (VWrongAnswer vsa : fallibleAnswers) {
									String compareStr = vsa.getContent() + "con" + wrongAnswer.getSequence();
									if (compareStr.equals(content)) {
										vsa.setWrongCount(count);
									}
									newVStudentHomeworkAnswers.add(vsa);
								}

								fallibleAnswers.clear();
								fallibleAnswers.addAll(newVStudentHomeworkAnswers);
							} else {
								fallibleAnswersMap.put(content, 1);
								wrongAnswer.setWrongCount(1);
							}

							fallibleAnswers.add(wrongAnswer);
						} else {
							// 其它题目类型只加入错误学生姓名
							StudentHomeworkAnswer studentHomeworkAnswer = answerMap.get(sa.getId());
							VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
									.to(studentHomeworkAnswer);
							VWrongAnswer wrongAnswer = new VWrongAnswer();
							BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
							wrongAnswer.setName(students.get(vstudentHomeworkAnswer.getAnswerId()).getName());
							VStudentHomeworkQuestion vsq = homeworkQuestions
									.get(wrongAnswer.getStudentHomeworkQuestionId());
							if (vsq != null) {
								wrongAnswer.setRightRate(vsq.getRightRate());
								wrongAnswer.setRightRateTitle(vsq.getRightRateTitle());
								if (CollectionUtils.isEmpty(vsq.getNotationAnswerImgs())) {
									wrongAnswer.setAnswerImages(vsq.getAnswerImgs());
								} else {
									wrongAnswer.setAnswerImages(vsq.getNotationAnswerImgs());
								}

							}
							fallibleAnswers.add(wrongAnswer);
						}
					}
				}

				// 对错误答案排序
				// 1.填空题:匹配数量最多>匹配数量第二>非重复答案 >未作答
				// 选择题: A>B>C>D>未作答
				// 简答题: 10%>30%>未作答
				Collections.sort(fallibleAnswers, new Comparator<VWrongAnswer>() {
					@Override
					public int compare(VWrongAnswer v1, VWrongAnswer v2) {
						if (Type.FILL_BLANK.equals(vquestion.getType())) {
							if (StringUtils.isEmpty(v1.getContent()) && StringUtils.isEmpty(v2.getContent())) {
								return compareJoinTime(studentJoinTime, v1, v2);
							} else if (StringUtils.isEmpty(v1.getContent())) {
								return 1;
							} else if (StringUtils.isEmpty(v2.getContent())) {
								return -1;
							} else if (v1.getWrongCount() == v2.getWrongCount()) {
								return compareJoinTime(studentJoinTime, v1, v2);
							} else {
								return v2.getWrongCount() - v1.getWrongCount();
							}
						} else if (Type.SINGLE_CHOICE.equals(vquestion.getType())
								|| Type.MULTIPLE_CHOICE.equals(vquestion.getType())) {
							if (StringUtils.isEmpty(v1.getContent()) && StringUtils.isEmpty(v2.getContent())) {
								return compareJoinTime(studentJoinTime, v1, v2);
							} else if (StringUtils.isEmpty(v1.getContent())) {
								return 1;
							} else if (StringUtils.isEmpty(v2.getContent())) {
								return -1;
							} else {
								return v1.getContent().compareTo(v2.getContent());
							}
						} else if (Type.QUESTION_ANSWERING.equals(vquestion.getType())) {
							// 先判是否未作答
							if (CollectionUtils.isEmpty(v1.getAnswerImages())
									&& CollectionUtils.isEmpty(v2.getAnswerImages())) {
								return compareJoinTime(studentJoinTime, v1, v2);
							} else if (CollectionUtils.isEmpty(v1.getAnswerImages())) {
								return 1;
							} else if (CollectionUtils.isEmpty(v2.getAnswerImages())) {
								return -1;
							} else {
								if (v1.getRightRate() == null && v2.getRightRate() == null) {
									return compareJoinTime(studentJoinTime, v1, v2);
								} else if (v1.getRightRate() == null) {
									return 1;
								} else if (v2.getRightRate() == null) {
									return -1;
								} else {
									return v1.getRightRate() - v2.getRightRate();
								}
							}
						} else {
							return compareJoinTime(studentJoinTime, v1, v2);
						}
					}
				});

				// 根据sequence取得answer对象
				List<VAnswer> newAnswers = Lists.newArrayList();
				for (VAnswer va : answers) {
					List<VWrongAnswer> fList = Lists.newArrayList();
					for (VWrongAnswer vsa : fallibleAnswers) {
						if (va.getSequence() == vsa.getSequence()) {
							fList.add(vsa);
						}
					}
					// 设置易错答案
					if (Type.SINGLE_CHOICE.equals(vquestion.getType())
							|| Type.MULTIPLE_CHOICE.equals(vquestion.getType())
							|| Type.FILL_BLANK.equals(vquestion.getType())) {
						Map<String, Object> fallbileMap = getFallibleAnswer(fList);
						if (fallbileMap != null && fallbileMap.size() > 0) {
							va.setFallibleAnswer((String) fallbileMap.get("content"));
							va.setFallibleCount((int) fallbileMap.get("count"));
						}
					}

					va.setWrongAnswers(fList);
					newAnswers.add(va);
				}

				vquestion.setAnswers(newAnswers);
				h.setQuestion(vquestion);
			}
		}

		vm.put("homeworkQuestions", vhqs);// 作业题目列表
		boolean isLastCommit = false;
		if (homework.getLastCommitAt() != null) {
			isLastCommit = System.currentTimeMillis() > homework.getLastCommitAt().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000;
		} else {
			isLastCommit = homework.getDeadline().getTime() + Env.getInt("homework.allcommit.then") * 60 * 1000 < System
					.currentTimeMillis();
		}
		// List<StudentHomework> studentHomeworks =
		// stuHkService.listByHomework(id);
		if (homework.getStatus() == HomeworkStatus.PUBLISH || homework.getStatus() == HomeworkStatus.NOT_ISSUE) {// 作业中
			boolean canIssue = true;
			int autoCorrect = 0;
			boolean hasAutoCorrected = false;// 有没有已经批改出来的学生作业
			if (v.getDistributeCount() > 0) {
				for (StudentHomework stuHk : studentHomeworks) {
					if (canIssue) {
						if (stuHk.getStatus() == StudentHomeworkStatus.SUBMITED && stuHk.getStuSubmitAt() != null
								&& stuHk.getRightRate() == null) {
							canIssue = false;
						}
					}
					if (!isLastCommit) {
						if (stuHk.getStatus() == StudentHomeworkStatus.SUBMITED && stuHk.getStuSubmitAt() != null
								&& !stuHk.isAutoManualAllCorrected() && stuHk.getRightRate() == null) {
							autoCorrect++;
						}
					}
					if (!hasAutoCorrected) {
						if (isLastCommit) {
							hasAutoCorrected = true;
						} else {
							if (stuHk.getStatus() == StudentHomeworkStatus.SUBMITED && stuHk.getStuSubmitAt() != null
									&& stuHk.isAutoManualAllCorrected()) {
								hasAutoCorrected = true;
							}
						}
					}
				}
			}
			vm.put("canIssue", canIssue);// 是否能下发
			vm.put("autoCorrect", autoCorrect);// 有几个学生作业在自动批改中
			List<Long> correctQuestionIds = stuHkQuestionService.listCorrectQuestions(id);
			List<Long> needCorrectQuestionIds = null;
			if (isLastCommit) {
				needCorrectQuestionIds = stuHkQuestionService.findNeedCorrectQuestionsAll(id);
			} else {
				needCorrectQuestionIds = stuHkQuestionService.findNeedCorrectQuestions(id);
			}
			if (needCorrectQuestionIds == null) {
				needCorrectQuestionIds = Collections.EMPTY_LIST;
			}
			List<Map<String, Object>> needCorrectQuestions = Lists.newArrayList(); // 需要批改的题目
			List<Map<String, Object>> hasCorrectQuestions = Lists.newArrayList(); // 已经批改的题目
			List<Map<String, Object>> needCorrectCorrectQuestions = Lists.newArrayList(); // 需要批改的订正题
			List<Map<String, Object>> hasCorrectCorrectQuestions = Lists.newArrayList(); // 已经批改的订正题
			for (VHomeworkQuestion vhq : vhqs) {
				if (needCorrectQuestionIds.contains(vhq.getQuestionId())) {
					needCorrectQuestions
							.add(ValueMap.value("question", vhq.getQuestion()).put("sequence", vhq.getSequence()));
				} else {
					if (hasAutoCorrected) {
						hasCorrectQuestions
								.add(ValueMap.value("question", vhq.getQuestion()).put("sequence", vhq.getSequence()));
					}
				}
			}
			if (CollectionUtils.isNotEmpty(correctQuestionIds)) {
				Map<Long, Question> correctQuestions = questionService.mget(correctQuestionIds);
				QuestionConvertOption questionConvertOption = new QuestionConvertOption(false, true, true, null);
				questionConvertOption.setInitQuestionTag(true); // 标签
				Map<Long, VQuestion> vcorrectQuestions = questionConvert.to(correctQuestions, questionConvertOption);
				for (Long cId : correctQuestionIds) {
					vcorrectQuestions.get(cId).setCorrectQuestion(true);
					if (needCorrectQuestionIds.contains(cId)) {
						needCorrectCorrectQuestions.add(ValueMap.value("question", vcorrectQuestions.get(cId))
								.put("sequence", correctQuestionIds.indexOf(cId) + 1));
					} else {
						if (hasAutoCorrected) {
							hasCorrectCorrectQuestions.add(ValueMap.value("question", vcorrectQuestions.get(cId))
									.put("sequence", correctQuestionIds.indexOf(cId) + 1));
						}
					}
				}
			}
			vm.put("needCorrectQuestions", needCorrectQuestions);
			vm.put("hasCorrectQuestions", hasCorrectQuestions);
			vm.put("needCorrectCorrectQuestions", needCorrectCorrectQuestions);
			vm.put("hasCorrectCorrectQuestions", hasCorrectCorrectQuestions);
		} else if (homework.getStatus() == HomeworkStatus.ISSUED) {// 已经下发
			List<VStudentHomework> vstuHks = null;
			List<Integer> resultsDistribution = null;
			if (v.getDistributeCount() > 0) {
				vstuHks = stuHkConvert.to(studentHomeworks, false, false, true, true);
				int a1 = 0, a2 = 0, a3 = 0, a4 = 0;
				for (StudentHomework studentHomework : studentHomeworks) {
					BigDecimal rightRate = studentHomework.getRightRate();
					if (studentHomework.getRightRate() != null) {
						if (rightRate.compareTo(BigDecimal.valueOf(85)) >= 0) {
							a1++;
						} else if (rightRate.compareTo(BigDecimal.valueOf(70)) >= 0) {
							a2++;
						} else if (rightRate.compareTo(BigDecimal.valueOf(60)) >= 0) {
							a3++;
						} else {
							a4++;
						}
					}
				}
				resultsDistribution = Lists.newArrayList(a1, a2, a3, a4);

				// @since v1.3.0
				if (homework.getTimeLimit() != null && homework.getTimeLimit().intValue() > 0) {
					int timelimitSecond = 0;
					for (VStudentHomework vs : vstuHks) {
						timelimitSecond = homework.getTimeLimit() * 60;
						if (vs.getHomeworkTime() > timelimitSecond) {
							vs.setTimeout(true);
						}
					}
				}
			} else {
				vstuHks = Collections.EMPTY_LIST;
				resultsDistribution = Lists.newArrayList(0, 0, 0, 0);
			}
			vm.put("studentHomeworks", vstuHks);// 学生作业列表
			vm.put("resultsDistribution", resultsDistribution);// 作业结果分布

			// 表示新知识上线，作业已经采用新知识点了。
			if (CollectionUtils.isNotEmpty(homework.getKnowledgePoints())) {
				// 包含的知识点是当前班级薄弱知识点的查询出来
				List<DiagnosticClassKnowpoint> points = diagKpService.findWeakDatasByKps(homework.getHomeworkClassId(),
						homework.getKnowledgePoints());

				// 薄弱知识点是否统计中statistics
				// 取到数据说明统计已经结束
				List<DiagnosticClassKnowpoint> diagnostics = diagKpService
						.findDiagnosticDatas(homework.getHomeworkClassId(), 1, homework.getIssueAt());
				if (CollectionUtils.isEmpty(diagnostics)) {
					vm.put("weakStatisticsFlag", false);
				} else {
					vm.put("weakStatisticsFlag", true);
				}
				if (CollectionUtils.isEmpty(points)) {
					vm.put("weakPoints", Collections.EMPTY_LIST);
				} else {
					// 薄弱知识点添加掌握情况规则 @since v1.3.0
					Map<Long, MasterStatus> weakMap = getPointMasterStatus(points);

					List<Long> codes = new ArrayList<Long>(points.size());
					for (DiagnosticClassKnowpoint p : points) {
						codes.add(p.getKnowpointCode());
					}

					List<KnowledgePoint> knowledgePoints = knowledgePointService.mgetList(codes);
					List<VKnowledgePoint> vKnowledgePoints = knowledgePointConvert.to(knowledgePoints);
					List<VWeakKonwledgePoint> weakPoints = Lists.newArrayList();
					VWeakKonwledgePoint weakPoint = null;
					for (VKnowledgePoint value : vKnowledgePoints) {
						weakPoint = new VWeakKonwledgePoint();
						BeanUtils.copyProperties(value, weakPoint);
						weakPoint.setMasterStatus(weakMap.get(value.getCode()).name());
						weakPoints.add(weakPoint);
					}

					vm.put("weakPoints", weakPoints);
				}
			}

			// 未在限时内完成学生人数 @since 教师端v1.3.0
			if (v.getTimeLimit() != null && v.getTimeLimit().intValue() > 0) {
				int timeout = 0;
				int timelimitSecond = 0;
				for (StudentHomework s : studentHomeworks) {
					timelimitSecond = v.getTimeLimit() * 60;
					if (s.getHomeworkTime().intValue() > timelimitSecond) {
						timeout++;
					}
				}
				vm.put("timeoutCount", timeout);
			}
		}

		vm.put("autoIssueHour", Env.getDynamicInt("homework.issued.time"));
		return new Value(vm);
	}

	/**
	 * 获得易错答案
	 * 
	 * @param fallibleAnswers
	 */
	private Map<String, Object> getFallibleAnswer(List<VWrongAnswer> fallibleAnswers) {
		Map<String, Object> result = new HashMap<>();
		String content = "";
		int count = 1;
		for (VWrongAnswer v : fallibleAnswers) {
			if (StringUtils.isNotEmpty(v.getContent())) {
				if (v.getWrongCount() > count) {
					content = v.getImageContent();
					count = v.getWrongCount();
				}
			}
		}

		if (!content.isEmpty()) {
			result.put("content", content);
			result.put("count", count);
		}

		return result;
	}

	/**
	 * 取学生homeworkquestion
	 * 
	 * @param vhqs
	 * @param studentHomeworks
	 */
	private Map<Long, VStudentHomeworkQuestion> getStudentHomeworkQuestion(List<VHomeworkQuestion> vhqs,
			List<StudentHomework> studentHomeworks) {
		List<Long> questionIds = Lists.newArrayList();
		for (VHomeworkQuestion v : vhqs) {
			questionIds.add(v.getQuestionId());
		}

		List<VStudentHomeworkQuestion> vstudentHomeworkQuestions = Lists.newArrayList();
		for (StudentHomework s : studentHomeworks) {
			List<StudentHomeworkQuestion> ps = shqService.find(s.getId(), questionIds);
			List<VStudentHomeworkQuestion> vps = shqConvert.to(ps);
			vstudentHomeworkQuestions.addAll(vps);
		}

		Map<Long, VStudentHomeworkQuestion> data = new HashMap<>();
		for (VStudentHomeworkQuestion v : vstudentHomeworkQuestions) {
			data.put(v.getId(), v);
		}

		return data;
	}

	/**
	 * 取得所有学生的答案，未提交的设置answerId
	 * 
	 * @param hqs
	 * @param studentHomeworks
	 * @return map
	 */
	private Map<Long, StudentHomeworkAnswer> getStudentAnswers(List<HomeworkQuestion> hqs,
			List<StudentHomework> studentHomeworks) {
		// 目标 根据学生作业查询学生答案，未作答的要设置用户名
		List<Long> questionIds = Lists.newArrayList();
		for (HomeworkQuestion h : hqs) {
			questionIds.add(h.getQuestionId());
		}

		Map<Long, StudentHomeworkAnswer> result = new HashMap<>();
		// 查询作业下问题
		for (StudentHomework s : studentHomeworks) {
			List<StudentHomeworkQuestion> ps = shqService.find(s.getId(), questionIds);
			List<Long> studentHomeworkQuestions = Lists.newArrayList();
			for (StudentHomeworkQuestion sq : ps) {
				studentHomeworkQuestions.add(sq.getId());
			}
			Map<Long, List<StudentHomeworkAnswer>> answerMap = studentHkAnswerService.find(studentHomeworkQuestions);

			for (List<StudentHomeworkAnswer> value : answerMap.values()) {
				for (StudentHomeworkAnswer sha : value) {
					if (sha.getAnswerId() == null) {
						sha.setAnswerId(s.getStudentId());
						// sha.setContent(NOT_ANSWER);
					}
					// 未批改的答案置空,页面显示未作答
					if (HomeworkAnswerResult.INIT == sha.getResult()) {
						sha.setContent(null);
					}
					result.put(sha.getId(), sha);
				}
			}
		}

		return result;
	}

	/**
	 * 取得所有学生加入班级的时间
	 * 
	 * @param classId
	 * @return map
	 */
	private Map<Long, Long> getJoinClassTime(Long classId) {
		Map<Long, Long> data = new HashMap<>();
		List<HomeworkStudentClazz> students = homeworkStudentClazzService.list(classId);
		for (HomeworkStudentClazz v : students) {
			data.put(v.getStudentId(), v.getJoinAt().getTime());
		}
		return data;
	}

	/**
	 * 按照加入班级时间排序
	 * 
	 * @param studentJoinTime
	 * @param v1
	 * @param v2
	 * @return map
	 */
	private int compareJoinTime(Map<Long, Long> studentJoinTime, VStudentHomeworkAnswer v1, VStudentHomeworkAnswer v2) {
		// 按照加入班级时间排序
		Long time1 = 0L;
		if (studentJoinTime.containsKey(v1.getAnswerId())) {
			time1 = studentJoinTime.get(v1.getAnswerId());
		}
		Long time2 = 0L;
		if (studentJoinTime.containsKey(v2.getAnswerId())) {
			time2 = studentJoinTime.get(v2.getAnswerId());
		}
		if (time2 - time1 > 0) {
			return 1;
		} else if (time2 - time1 < 0) {
			return -1;
		} else {
			return 0;
		}
	}

	/**
	 * 取知识点掌握情况
	 * 
	 * @param points
	 * @return map
	 */
	private Map<Long, MasterStatus> getPointMasterStatus(List<DiagnosticClassKnowpoint> points) {
		Map<Long, MasterStatus> weakMap = new HashMap<>();
		BigDecimal rightCount = null;
		BigDecimal doCount = null;
		BigDecimal result = null;
		for (DiagnosticClassKnowpoint d : points) {
			rightCount = new BigDecimal(d.getRightCount() + 1);
			doCount = new BigDecimal(d.getDoCount() + 2);
			result = rightCount.divide(doCount, 2, BigDecimal.ROUND_HALF_EVEN);
			if (result.compareTo(BigDecimal.ZERO) >= 0 && result.compareTo(BigDecimal.valueOf(0.3D)) <= 0) {
				weakMap.put(d.getKnowpointCode(), MasterStatus.WEAK);
			} else if (result.compareTo(BigDecimal.valueOf(0.3D)) > 0
					&& result.compareTo(BigDecimal.valueOf(0.6D)) <= 0) {
				weakMap.put(d.getKnowpointCode(), MasterStatus.COMMONLY);
			} else if (result.compareTo(BigDecimal.valueOf(0.6D)) > 0
					&& result.compareTo(BigDecimal.valueOf(0.9D)) <= 0) {
				weakMap.put(d.getKnowpointCode(), MasterStatus.GOOD);
			} else {
				weakMap.put(d.getKnowpointCode(), MasterStatus.EXCELLENT);
			}
		}

		return weakMap;
	}

	/**
	 * 完成情况列表
	 * 
	 * @since 2.0.3
	 * @param id
	 *            作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "completionList", method = { RequestMethod.POST, RequestMethod.GET })
	public Value completionList(long id) {
		List<StudentHomework> studentHomeworks = stuHkService.listByHomework(id);
		List<VStudentHomework> vstudentHomeworks = stuHkConvert.to(studentHomeworks, false, false, true, true);
		List<VStudentHomework> commit = Lists.newArrayList();
		List<VStudentHomework> notCommit = Lists.newArrayList();

		for (VStudentHomework v : vstudentHomeworks) {
			if (v.getStuSubmitAt() != null) {
				commit.add(v);
			} else {
				notCommit.add(v);
			}
		}

		// 添加是否在限时内完成标签，针对已提交学生 @since 教师端v1.3.0
		Homework homework = hkService.get(id);
		if (homework == null || homework.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}

		if (homework.getTimeLimit() != null && homework.getTimeLimit().intValue() > 0) {
			int timelimitSecond = 0;
			for (VStudentHomework s : commit) {
				timelimitSecond = homework.getTimeLimit() * 60;
				if (s.getHomeworkTime() > timelimitSecond) {
					s.setTimeout(true);
				}
			}
		}

		// 对完成列表按照作业用时正序排列
		Collections.sort(commit, (x, y) -> x.getHomeworkTime() - y.getHomeworkTime());

		ValueMap vm = ValueMap.value("commit", commit).put("notCommit", notCommit);
		return new Value(vm);
	}

	/**
	 * 立即分发作业
	 * 
	 * @since 2.0.3
	 * @param id
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "distribute", method = { RequestMethod.POST, RequestMethod.GET })
	public Value distribute(long id) {
		Homework homework = hkService.get(id);
		if (homework == null || homework.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}
		
		zyTeaHomework2Controller.distribute(id);

		// 延迟处理
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new Value();
	}

	/**
	 * 获取学生作业详情
	 * 
	 * @since 2.0.3
	 * @param hkId
	 *            作业ID
	 * @param stuHkId
	 *            学生作业ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "studentHomeworkDetail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworkDetail(long hkId, long stuHkId) {
		Homework hk = hkService.get(hkId);
		StudentHomework stuHk = stuHkService.get(stuHkId);
		if (hk == null || hk.getCreateId() != Security.getUserId() || stuHk == null || stuHk.getHomeworkId() != hkId) {
			return new Value(new IllegalArgException());
		}
		ValueMap vm = ValueMap.value("homework", hkConvert.to(hk));
		// 是否在自动批改中
		boolean autoCorrecting = false;
		if (hk.getStatus() == HomeworkStatus.PUBLISH || hk.getStatus() == HomeworkStatus.NOT_ISSUE) {
			if (hk.getLastCommitAt() != null) {
				autoCorrecting = !(System.currentTimeMillis() > hk.getLastCommitAt().getTime()
						+ Env.getInt("homework.allcommit.then") * 60 * 1000);
			} else {
				autoCorrecting = !(hk.getDeadline().getTime()
						+ Env.getInt("homework.allcommit.then") * 60 * 1000 < System.currentTimeMillis());
			}
			if (autoCorrecting) {
				autoCorrecting = !stuHk.isAutoManualAllCorrected();
			}
		}
		vm.put("autoCorrecting", autoCorrecting);
		vm.put("studentHomework", stuHkConvert.to(stuHk));
		List<Long> qids = hkQuestionService.getQuestion(stuHk.getHomeworkId());
		List<Long> correctQIds = stuHkQuestionService.getCorrectQuestions(stuHkId);
		qids.addAll(correctQIds);
		List<Question> questions = new ArrayList<Question>(qids.size());
		Map<Long, Question> qs = questionService.mget(qids);
		for (Long qid : qids) {
			Question question = qs.get(qid);
			if (correctQIds.contains(qid)) {
				question.setCorrectQuestion(true);
			}
			questions.add(question);
		}
		QuestionConvertOption questionConvertOption = new QuestionConvertOption(false, true, true, false, true,
				stuHkId);
		questionConvertOption.setInitQuestionTag(true); // 标签
		List<VQuestion> vqs = questionConvert.to(questions, questionConvertOption);
		if (!autoCorrecting) {// 作业下发作业状态的时候设置答案颜色
			for (VQuestion v : vqs) {
				if (v.getType() == Type.FILL_BLANK) {
					int size = v.getStudentHomeworkAnswers().size();
					for (int i = 0; i < size; i++) {
						// 每一空的颜色根据学生空的正确性来进行渲染
						v.getStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils.process(
								v.getStudentHomeworkAnswers().get(i).getContent(),
								v.getStudentHomeworkAnswers().get(i).getResult() == HomeworkAnswerResult.RIGHT, true));
					}
				}
			}
		} else {
			// 非下发状态下，教师查看待批改中学生作业详情，学生答案render
			for (VQuestion v : vqs) {
				if (v.getType() == Type.FILL_BLANK) {
					int size = v.getStudentHomeworkAnswers().size();
					for (int i = 0; i < size; i++) {
						v.getStudentHomeworkAnswers().get(i).setImageContent(
								QuestionUtils.process(v.getStudentHomeworkAnswers().get(i).getContent(), null, true));
					}
				}
			}
		}
		vm.put("questions", vqs);

		return new Value(vm);
	}

	/**
	 * 下发作业
	 * 
	 * @since 2.0.3
	 * @param id
	 *            作业ID
	 * @return {@link Value}
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "issue", method = { RequestMethod.POST, RequestMethod.GET })
	public Value issue(long id) {
		Value value = zyTeaHomework2Controller.issue(id);

		if (value.getRet_code() == CoreExceptionCode.PAGE_NOT_FOUND_EX
				|| value.getRet_code() == HomeworkException.HOMEWORK_HAS_ISSUE) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_ISSUED));
		} else {
			// 用户动作行为
			userActionService.asyncAction(UserAction.ISSUE_HOMEWORK, Security.getUserId(), null);
			// TODO 临时处理
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			return value;
		}
	}

	/**
	 * 根据题目获取学生作业题目列表
	 * 
	 * @since 2.0.3
	 * @param id
	 *            作业ID
	 * @param questionId
	 *            题目ID
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "listByQuestion", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listByQuestion(long id, long questionId) {
		Homework homework = hkService.get(id);
		// 是否过了截止时间(最后一个人提交后的X分钟|截止时间后的X分钟)
		boolean teacherCanCorrect = false;
		if (homework.getLastCommitAt() != null) {
			teacherCanCorrect = System.currentTimeMillis() > homework.getLastCommitAt().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000;
		} else {
			teacherCanCorrect = homework.getDeadline().getTime()
					+ Env.getInt("homework.allcommit.then") * 60 * 1000 < System.currentTimeMillis();
		}

		// 学生作业列表
		List<VStudentHomework> needCorrectList = Lists.newArrayList();
		List<VStudentHomework> hasCorrectList = Lists.newArrayList();

		List<VStudentHomework> needCorrect = Lists.newArrayList();
		List<VStudentHomework> notSubmit = Lists.newArrayList();
		List<VStudentHomework> autoCorrect = Lists.newArrayList();
		List<VStudentHomework> hasCorrect = Lists.newArrayList();

		// 学生作业题目列表
		List<VStudentHomeworkQuestion> needCorrectQList = Lists.newArrayList();
		List<VStudentHomeworkQuestion> hasCorrectQList = Lists.newArrayList();

		List<VStudentHomeworkQuestion> needCorrectQ = Lists.newArrayList();
		List<VStudentHomeworkQuestion> notSubmitQ = Lists.newArrayList();
		List<VStudentHomeworkQuestion> autoCorrectQ = Lists.newArrayList();
		List<VStudentHomeworkQuestion> hasCorrectQ = Lists.newArrayList();

		// 学生作业答案列表
		List<List<VStudentHomeworkAnswer>> needCorrectAList = Lists.newArrayList();
		List<List<VStudentHomeworkAnswer>> hasCorrectAList = Lists.newArrayList();

		List<List<VStudentHomeworkAnswer>> needCorrectA = Lists.newArrayList();
		List<List<VStudentHomeworkAnswer>> notSubmitA = Lists.newArrayList();
		List<List<VStudentHomeworkAnswer>> autoCorrectA = Lists.newArrayList();
		List<List<VStudentHomeworkAnswer>> hasCorrectA = Lists.newArrayList();

		// 学生作业
		List<VStudentHomework> studentHomeworks = stuHkConvert.to(stuHkService.listByHomework(id), false, false, true,
				false);
		// 学生作业题目
		List<VStudentHomeworkQuestion> studentHomeworkQuestions = stuHkQuestionConvert
				.to(stuHkQuestionService.listByQuestionId(id, questionId));
		Map<Long, VStudentHomeworkQuestion> studentHomeworkQuestionMap = Maps.newHashMap();
		for (VStudentHomeworkQuestion v : studentHomeworkQuestions) {
			studentHomeworkQuestionMap.put(v.getStudentHomeworkId(), v);
		}
		// 学生作业
		List<VStudentHomeworkAnswer> studentHomeworkAnswers = stuHkAnswerConvert
				.to(stuHkAnswerService.listByQuestionId(id, questionId));
		Map<Long, List<VStudentHomeworkAnswer>> studentHomeworkAnswerMap = Maps.newHashMap();
		for (VStudentHomeworkAnswer v : studentHomeworkAnswers) {
			if (!studentHomeworkAnswerMap.containsKey(v.getStudentHomeworkQuestionId())) {
				studentHomeworkAnswerMap.put(v.getStudentHomeworkQuestionId(), new ArrayList<VStudentHomeworkAnswer>());
			}
			if (v.getResult() == HomeworkAnswerResult.RIGHT || v.getResult() == HomeworkAnswerResult.WRONG) {
				v.setImageContent(
						QuestionUtils.process(v.getContent(), v.getResult() == HomeworkAnswerResult.RIGHT, true));
			}
			studentHomeworkAnswerMap.get(v.getStudentHomeworkQuestionId()).add(v);
		}
		// 分析数据
		for (VStudentHomework v : studentHomeworks) {
			VStudentHomeworkQuestion studentHomeworkQuestion = studentHomeworkQuestionMap.get(v.getId());
			if (studentHomeworkQuestion == null) {
				continue;
			}
			List<VStudentHomeworkAnswer> answers = studentHomeworkAnswerMap.get(studentHomeworkQuestion.getId());
			if (teacherCanCorrect) {
				boolean toNeedCorrect = false;
				boolean toNotSubmit = false;
				if (v.getStuSubmitAt() != null) {
					for (VStudentHomeworkAnswer va : answers) {
						if (va.getResult() == HomeworkAnswerResult.INIT
								|| va.getResult() == HomeworkAnswerResult.UNKNOW) {
							toNeedCorrect = true;
							break;
						}
					}
				} else {
					toNotSubmit = true;
				}
				// needCorrectList hasCorrectList
				if (!toNotSubmit) {
					if (toNeedCorrect) {
						needCorrectList.add(v);
						needCorrectQList.add(studentHomeworkQuestion);
						needCorrectAList.add(answers);
					} else {
						hasCorrectList.add(v);
						hasCorrectQList.add(studentHomeworkQuestion);
						hasCorrectAList.add(answers);
					}
				}
				// needCorrect hasCorrect
				if (!toNotSubmit) {
					if (toNeedCorrect) {
						needCorrect.add(v);
						needCorrectQ.add(studentHomeworkQuestion);
						needCorrectA.add(answers);
					} else {
						hasCorrect.add(v);
						hasCorrectQ.add(studentHomeworkQuestion);
						hasCorrectA.add(answers);
					}
				}
				// notSubmit
				if (toNotSubmit) {
					notSubmit.add(v);
					notSubmitQ.add(studentHomeworkQuestion);
					notSubmitA.add(answers);
				}
				// autoCorrect 无
			} else {
				boolean toNeedCorrect = false;
				boolean toNotSubmit = false;
				boolean toAutoCorrect = false;
				if (v.getStuSubmitAt() != null) {
					if (v.isAutoManualAllCorrected()) {
						for (VStudentHomeworkAnswer va : answers) {
							if (va.getResult() == HomeworkAnswerResult.INIT
									|| va.getResult() == HomeworkAnswerResult.UNKNOW) {
								toNeedCorrect = true;
								break;
							}
						}
					} else {
						toAutoCorrect = true;
					}
				} else {
					toNotSubmit = true;
				}
				// needCorrectList hasCorrectList
				if (!toNotSubmit) {
					if (toNeedCorrect) {
						needCorrectList.add(v);
						needCorrectQList.add(studentHomeworkQuestion);
						needCorrectAList.add(answers);
					} else if (v.isAutoManualAllCorrected()) {
						hasCorrectList.add(v);
						hasCorrectQList.add(studentHomeworkQuestion);
						hasCorrectAList.add(answers);
					}
				}
				// needCorrect hasCorrect
				if (!toNotSubmit) {
					if (toNeedCorrect) {
						needCorrect.add(v);
						needCorrectQ.add(studentHomeworkQuestion);
						needCorrectA.add(answers);
					} else if (v.isAutoManualAllCorrected()) {
						hasCorrect.add(v);
						hasCorrectQ.add(studentHomeworkQuestion);
						hasCorrectA.add(answers);
					}
				}
				// notSubmit
				if (toNotSubmit) {
					notSubmit.add(v);
					notSubmitQ.add(studentHomeworkQuestion);
					notSubmitA.add(answers);
				}
				// autoCorrect
				if (toAutoCorrect) {
					autoCorrect.add(v);
					autoCorrectQ.add(studentHomeworkQuestion);
					autoCorrectA.add(answers);
				}
			}
		}

		// 封装返回数据结构
		ValueMap vm = ValueMap.value();
		// 上面的用户头像数据列表
		List<Map<String, Object>> items = Lists.newArrayList();
		int idx = 0;

		// @since v1.3.0 按照学生姓名排序
		// Collections.sort(needCorrectList, (x, y) ->
		// x.getUser().getName().compareTo(y.getUser().getName()));
		for (VStudentHomework v : needCorrectList) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", needCorrectQList.get(idx));
			map.put("studentHomeworkAnswer", needCorrectAList.get(idx));
			items.add(map);
			idx++;
		}
		idx = 0;
		// Collections.sort(hasCorrectList, (x, y) ->
		// x.getUser().getName().compareTo(y.getUser().getName()));
		for (VStudentHomework v : hasCorrectList) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", hasCorrectQList.get(idx));
			map.put("studentHomeworkAnswer", hasCorrectAList.get(idx));
			items.add(map);
			idx++;
		}
		vm.put("items", items);
		// 统计数据
		Map<String, Object> statistics = new HashMap<String, Object>(4);
		// 待批改
		List<Map<String, Object>> needCorrectItems = Lists.newArrayList();
		idx = 0;
		for (VStudentHomework v : needCorrect) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", needCorrectQ.get(idx));
			map.put("studentHomeworkAnswer", needCorrectA.get(idx));
			needCorrectItems.add(map);
			idx++;
		}
		statistics.put("needCorrect", needCorrectItems);
		// 未提交的
		List<Map<String, Object>> notSubmitItems = Lists.newArrayList();
		idx = 0;
		for (VStudentHomework v : notSubmit) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", notSubmitQ.get(idx));
			map.put("studentHomeworkAnswer", notSubmitA.get(idx));
			notSubmitItems.add(map);
			idx++;
		}
		statistics.put("notSubmit", notSubmitItems);
		// 自动批改中的
		List<Map<String, Object>> autoCorrectItems = Lists.newArrayList();
		idx = 0;
		for (VStudentHomework v : autoCorrect) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", autoCorrectQ.get(idx));
			List<VStudentHomeworkAnswer> vanswers = autoCorrectA.get(idx);
			for (VStudentHomeworkAnswer va : vanswers) {
				va.setImageContent(QuestionUtils.process(va.getContent(), null, true));
			}
			map.put("studentHomeworkAnswer", vanswers);
			autoCorrectItems.add(map);
			idx++;
		}
		statistics.put("autoCorrect", autoCorrectItems);
		// 已经批改的
		List<Map<String, Object>> hasCorrectItems = Lists.newArrayList();
		idx = 0;
		for (VStudentHomework v : hasCorrect) {
			Map<String, Object> map = new HashMap<String, Object>(3);
			map.put("studentHomework", v);
			map.put("studentHomeworkQuestion", hasCorrectQ.get(idx));
			map.put("studentHomeworkAnswer", hasCorrectA.get(idx));
			hasCorrectItems.add(map);
			idx++;
		}
		statistics.put("hasCorrect", hasCorrectItems);
		vm.put("statistics", statistics);
		return new Value(vm);
	}

	/**
	 * 批改
	 * 
	 * @since 2.0.3
	 * @param form
	 *            批改接口参数
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "correct", method = { RequestMethod.POST, RequestMethod.GET })
	public Value correct(CorrectForm form) {
		TeaCorrectQuestionForm2 form2 = new TeaCorrectQuestionForm2();
		form2.setStuHkId(form.getStuHkId());
		form2.setStuHkQuestionId(form.getStuHkQuestionId());
		form2.setType(form.getType());
		if (form.getType() == Type.FILL_BLANK) {
			Map<Long, HomeworkAnswerResult> results = Maps.newHashMap();
			results.put(form.getStuHkAnswerId(), form.getResult());
			form2.setAnswerResults(JSONObject.toJSONString(results));
		}
		if (!(form.getType() == Type.FILL_BLANK || form.getType() == Type.QUESTION_ANSWERING)) {
			form2.setResult(form.getResult());
		}
		if (form.getType() == Type.QUESTION_ANSWERING) {
			form2.setRightRate(form.getRightRate());
		}
		Value value = zyTeaHomeworkCorrectController.save2(form2);
		if (value.getRet_code() == YoomathMobileException.SUCCEED) {
			ValueMap vm = ValueMap.value();
			VStudentHomework studentHomework = stuHkConvert.to(stuHkService.get(form.getStuHkId()), false, false, true,
					false);
			vm.put("studentHomework", studentHomework);
			VStudentHomeworkQuestion studentHomeworkQuestion = stuHkQuestionConvert
					.to(stuHkQuestionService.get(form.getStuHkQuestionId()));
			vm.put("studentHomeworkQuestion", studentHomeworkQuestion);
			List<VStudentHomeworkAnswer> studentHomeworkAnswers = stuHkAnswerConvert
					.to(studentHkAnswerService.find(studentHomeworkQuestion.getId()));
			for (VStudentHomeworkAnswer v : studentHomeworkAnswers) {
				if (v.getResult() == HomeworkAnswerResult.RIGHT || v.getResult() == HomeworkAnswerResult.WRONG) {
					v.setImageContent(
							QuestionUtils.process(v.getContent(), v.getResult() == HomeworkAnswerResult.RIGHT, true));
				}
			}
			vm.put("studentHomeworkAnswer", studentHomeworkAnswers);
			value.setRet(vm);
		}
		return value;
	}

	/**
	 * 更新作业截止时间
	 *
	 * @param hkId
	 *            作业id
	 * @param deadline
	 *            截止时间
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "setDeadline", method = { RequestMethod.GET, RequestMethod.POST })
	public Value setDeadline(long hkId, long deadline) {
		try {
			Date updateDeadline = new Date(deadline);
			hkService.setDeadline(hkId, updateDeadline);
		} catch (IllegalArgException e) {
			return new Value(e);
		} catch (ZuoyeException e) {
			if (e.getCode() == ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_EXPIRE) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_UPDATEDEADLINE_EXPIRE));
			} else if (e.getCode() == ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_ISSUED) {
				return new Value(new YoomathMobileException(
						YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_UPDATEDEADLINE_ISSUED));
			} else if (e.getCode() == ZuoyeException.ZUOYE_HOMEWORK_UPDATEDEADLINE_DELETED) {
				return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
			}
		}

		return new Value();
	}

	@SuppressWarnings("deprecation")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "saveAnnotate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveAnnotate(@RequestBody String contents) {
		contents = URLDecoder.decode(contents);
		String[] contentArr = contents.split("#");
		String stuHkQuestionId = contentArr[0];
		String srcImageId = contentArr[1];
		String generateImageId = contentArr[2];
		String content = contents.replace(stuHkQuestionId + "#" + srcImageId + "#" + generateImageId + "#", "");
		stuHkQuestionService.saveNotation(Long.parseLong(stuHkQuestionId), Long.parseLong(srcImageId),
				Long.parseLong(generateImageId), null, content, null, null);
		return new Value();
	}

	/**
	 * 教师批注多张图片
	 *
	 * @param contents
	 *            批注内容
	 * @return {@link Value}
	 */
	@SuppressWarnings("deprecation")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "2/saveAnnotate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveAnnotate2(@RequestBody String contents) {
		contents = URLDecoder.decode(contents);
		String[] contentArr = contents.split("#");
		Long stuHkQuestionId = Long.parseLong(contentArr[0]);
		Long srcImageId1 = Long.parseLong(contentArr[1]);
		Long generateImageId1 = Long.parseLong(contentArr[2]);
		String content1 = "";
		if (!srcImageId1.equals(generateImageId1)) {
			content1 = contentArr[3];
		}

		stuHkQuestionService.saveMultiNotation(stuHkQuestionId, srcImageId1, srcImageId1, generateImageId1, null,
				content1);

		if (contentArr.length > 4) {
			Long srcImageId2 = Long.parseLong(contentArr[4]);
			Long generateImageId2 = Long.parseLong(contentArr[5]);
			String content2 = "";
			if (!srcImageId2.equals(generateImageId2)) {
				content2 = contentArr[6];
			}
			stuHkQuestionService.saveMultiNotation(stuHkQuestionId, srcImageId2, srcImageId2, generateImageId2, null,
					content2);
		}

		return new Value();
	}

	@SuppressWarnings("deprecation")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "3/saveAnnotate", method = { RequestMethod.GET, RequestMethod.POST })
	public Value saveAnnotate3(@RequestBody String contents) {
		List<Map<String, Object>> list = Lists.newArrayList();
		contents = URLDecoder.decode(contents);
		String[] contentArr = contents.split("#");
		Long stuHkQuestionId = Long.parseLong(contentArr[0]);
		Long srcImageId1 = Long.parseLong(contentArr[1]);
		Long generateImageId1 = Long.parseLong(contentArr[2]);
		int degree1 = Integer.parseInt(contentArr[3]);
		String content1 = "";
		if (!srcImageId1.equals(generateImageId1)) {
			content1 = contentArr[4];
		}
		long rotateSrcImageId1 = srcImageId1;
		if (degree1 > 0) {
			try {
				rotateSrcImageId1 = fileService.rotate(srcImageId1, degree1).getId();
			} catch (Exception e) {
				logger.error("rotate image error", e);
			}
		}
		stuHkQuestionService.saveMultiNotation(stuHkQuestionId, srcImageId1, rotateSrcImageId1, generateImageId1, null,
				content1);

		Map<String, Object> one = new HashMap<>(2);
		one.put("answerImgId", rotateSrcImageId1);
		one.put("answerImg", FileUtil.getUrl(rotateSrcImageId1));
		list.add(one);

		if (contentArr.length > 5) {
			Long srcImageId2 = Long.parseLong(contentArr[5]);
			Long generateImageId2 = Long.parseLong(contentArr[6]);
			int degree2 = Integer.parseInt(contentArr[7]);
			String content2 = "";
			if (!srcImageId2.equals(generateImageId2)) {
				content2 = contentArr[8];
			}
			long rotateSrcImageId2 = srcImageId2;
			if (degree2 > 0) {
				try {
					rotateSrcImageId2 = fileService.rotate(srcImageId2, degree2).getId();
				} catch (Exception e) {
					logger.error("rotate image error", e);
				}
			}
			stuHkQuestionService.saveMultiNotation(stuHkQuestionId, srcImageId2, rotateSrcImageId2, generateImageId2,
					null, content2);

			Map<String, Object> two = new HashMap<>(2);
			two.put("answerImgId", rotateSrcImageId2);
			two.put("answerImg", FileUtil.getUrl(rotateSrcImageId2));
			list.add(two);
		}

		return new Value(list);
	}

	/**
	 * 普通作业关闭自动下发功能
	 *
	 * @param hkId
	 *            作业id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "setAutoIssue", method = { RequestMethod.GET, RequestMethod.POST })
	public Value setAtuoIssue(long hkId, Status status) {
		if (hkId <= 0) {
			return new Value(new IllegalArgException());
		}
		Homework homework = hkService.get(hkId);
		if (null == homework) {
			return new Value(new IllegalArgException());
		}
		if (homework.getCreateId() != Security.getUserId()) {
			return new Value(new NoPermissionException());
		}
		if (System.currentTimeMillis() - homework.getDeadline().getTime() >= TimeUnit.HOURS
				.toMillis(Env.getDynamicInt("homework.issued.time"))) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_SETAUTOISSUE_OVERTIME));
		}
		// 已经删除作业不可以再更改自动下发状态
		if (Status.ENABLED != homework.getDelStatus()) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}
		// 作业已经下发
		if (HomeworkStatus.ISSUED == homework.getStatus()) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_UPDATEDEADLINE_ISSUED));
		}
		// 作业中含有解答题，不可以使用
		if (homework.isHasQuestionAnswering()) {
			return new Value(new NoPermissionException());
		}

		hkService.setAutoIssued(hkId, Status.ENABLED == status);
		return new Value();
	}

	/**
	 * 知识点巩固练习拉取题目接口
	 *
	 * @param knowpointCodes
	 *            新知识点列表
	 * @param type
	 *            {@link PertinenceType}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getPertinenceQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getPertinenceQuestions(@RequestParam(value = "knowpointCodes") List<Long> knowpointCodes,
			PertinenceType type) {
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			return new Value(new IllegalArgException());
		}
		PertinenceHomeworkForm form = new PertinenceHomeworkForm();
		switch (type) {
		// 基础-> 难度 基础80% 提高20% 选择题4题 、填空题6题
		case BASE:
			form.setBasePercent(80);
			form.setRaisePercent(20);
			form.setSprintPercent(0);
			form.setChoiceNum(4);
			form.setFillBlankNum(6);
			form.setAnswerNum(0);
			break;
		// 提高 -> 难度 基础60% 提高40% 选择题4题、填空题6题
		case RAISE:
			form.setBasePercent(60);
			form.setRaisePercent(40);
			form.setSprintPercent(0);
			form.setChoiceNum(4);
			form.setFillBlankNum(6);
			form.setAnswerNum(0);
			break;
		// 冲刺 -> 难度 提高60% 冲刺40% 选择题3题、填空题3题、解答题4题
		case SPRINT:
			form.setBasePercent(0);
			form.setRaisePercent(60);
			form.setSprintPercent(40);
			form.setChoiceNum(3);
			form.setFillBlankNum(3);
			form.setAnswerNum(4);
			break;
		}
		form.setKnowledgeCodes(knowpointCodes);
		Map<String, Object> retMap = null;
		List<Long> questionIds = pertinenceHomeworkService.queryPertinenceHomeworkQuestions(form);
		if (CollectionUtils.isEmpty(questionIds)) {
			retMap = new HashMap<String, Object>(1);
			retMap.put("questions", Collections.EMPTY_LIST);

			return new Value(retMap);
		}

		retMap = new HashMap<String, Object>(2);
		List<Question> questions = questionService.mgetList(questionIds);
		int predictTime = 0;

		for (Question q : questions) {
			predictTime += questionService.calPredictTime(q);
		}

		retMap.put("predictTime", predictTime);
		QuestionConvertOption convertOption = new QuestionConvertOption();
		convertOption.setAnswer(true);
		convertOption.setAnalysis(true);
		convertOption.setInitQuestionTag(true); // 标签
		retMap.put("questions", questionConvert.to(questions, convertOption));

		return new Value(retMap);
	}

	/**
	 * 更新语音批注，以前只支持一条语音批注的接口，后续不再使用
	 *
	 * @param voiceTime
	 *            语音时间
	 * @param fileKey
	 *            七牛上传后的唯一标识
	 * @param shqId
	 *            学生作业题目id
	 * @return {@link Value}
	 */
	@Deprecated
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "updateVoice", method = { RequestMethod.GET, RequestMethod.POST })
	public Value updateVoice(int voiceTime, String fileKey, long shqId) {
		if (voiceTime < 0 || voiceTime > 60 || shqId <= 0) {
			return new Value(new IllegalArgException());
		}

		stuHkQuestionService.updateVoice(voiceTime, fileKey, shqId);
		if (StringUtils.isNotBlank(fileKey)) {

			Map<String, Object> retMap = new HashMap<String, Object>(1);
			retMap.put("url", qiNiuFileService.getDownloadUrl(fileKey));
			return new Value(retMap);
		} else {
			return new Value();
		}
	}
	
	
	/**
	 * 新增一条批注
	 *
	 * @param voiceTime
	 *            语音时间
	 * @param fileKey
	 *            七牛上传后的唯一标识
	 * @param id
	 *            id,可能为作业id，学生作业id，学生作业题目id，场景不同id含义不同
	 * @param scene
	 *            场景，0 学生习题 1 学生作业  2 整份作业
	 * @param type
	 *            留言类型  TEXT 文本  AUDIO 语音  VIDEO 视频
	 * @param comment
	 *            文本留言，留言类型为 0 时有效
	 * @param iconKey
	 *            留言图标key属性
	 *            
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "addComment", method = { RequestMethod.GET, RequestMethod.POST })
	public Value addComment(Integer voiceTime, String fileKey, Long id, HomeworkMessageType type, Integer scene, String comment, String iconKey) {
		if (id <= 0 || type == null || scene == null) {
			return new Value(new IllegalArgException());
		}
		if (type == HomeworkMessageType.AUDIO && (voiceTime < 0 || voiceTime > 60)) {
			return new Value(new IllegalArgException());
		}
		
		if (type == HomeworkMessageType.TEXT && StringUtils.isEmpty(comment)) {
			return new Value(new IllegalArgException());
		}
		
		//判断是否该份作业或题目的留言超过了20条，超过了返回错误
		Boolean isExceed = hkMessageService.isMessagesExceed(id, Security.getUserId(), scene);
		if(isExceed) {
			return new Value(
					new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_MESSAGES_COUNT_EXCEED));
		}

		Long messageId = hkMessageService.addComment(voiceTime, fileKey, id, type, scene, comment, iconKey, UserType.TEACHER);
		
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		if (StringUtils.isNotBlank(fileKey)) {
			retMap.put("url", qiNiuFileService.getDownloadUrl(fileKey));
		} else {
			retMap.put("comment", comment);
		}
		retMap.put("id", messageId);
		
		return new Value(retMap);
	}
	
	/**
	 * 查询批注列表
	 *
	 * @param id
	 *            id,可能为作业id，学生作业id，学生作业题目id，场景不同id含义不同
	 * @param scene
	 *            场景，0 学生习题 1 学生作业  2 整份作业
	 *            
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getComments", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getComments(Long id, Integer scene) {
		if (id <= 0 || scene == null) {
			return new Value(new IllegalArgException());
		}

		long userId = Security.getUserId();
		
		List<HomeworkMessage> messages = hkMessageService.findMessages(id, userId, scene);
		
		List<VHomeworkMessage> vMessages = new ArrayList<>();
		
		if(CollectionUtils.isNotEmpty(messages)){
			for(HomeworkMessage message:messages){
				VHomeworkMessage vMessage = new VHomeworkMessage();
				vMessage.setId(message.getId());
				vMessage.setType(message.getType());
				vMessage.setCreateAt(message.getCreateAt());
				vMessage.setIconKey(message.getIconKey());
				
				if (StringUtils.isNotBlank(message.getComment())) {
					vMessage.setComment(message.getComment());
				}
				
				if (StringUtils.isNotBlank(message.getVoiceFileKey())) {
					vMessage.setVoiceUrl(qiNiuFileService.getDownloadUrl(message.getVoiceFileKey()));
					vMessage.setVoiceTime(message.getVoiceTime());
				}
				vMessages.add(vMessage);
			}
		}
		
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		retMap.put("messages", vMessages);
		
		return new Value(retMap);
	}
	
	/**
	 * 删除批注
	 *
	 * @param id
	 *            批注的id
	 *            
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "delComment", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delComment(Long id){
		if (id <= 0) {
			return new Value(new IllegalArgException());
		}
		
		hkMessageService.updateStatus(id, Status.DELETED);
		
		return new Value();
	}

	/**
	 * 学生作业列表
	 *
	 * @param id
	 *            作业id
	 * @param status
	 *            筛选条件
	 * @param orderby
	 *            排序方式
	 * @param name
	 *            学生名
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "studentHomeworks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value studentHomeworks(long id, String status, String orderby, String name) {
		ValueMap vm = ValueMap.value();
		Homework homework = hkService.get(id);
		if (homework == null || homework.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}

		// 1.筛选条件 全部 优秀BEST 良好GOOD 及格PASS 不及格FAIL 未限时完成TIMEOUT 未提交NOTSUBMIT
		// 作业等级 studentHomework.getRightRate();
		// 优秀： 正确率[85%--100%)
		// 良好：正确率[70%--85%)
		// 及格：正确率[60%--70%)
		// 不及格：正确率[0%--60%)
		BigDecimal rightRate = null;
		BigDecimal leftRate = null;
		Integer timeLimit = null;
		StudentHomeworkStatus hStatus = null;

		if (status != null) {
			switch (status) {
			case "BEST":
				// rightRate = new BigDecimal(100);
				leftRate = new BigDecimal(85);
				break;
			case "GOOD":
				rightRate = new BigDecimal(85);
				leftRate = new BigDecimal(70);
				break;
			case "PASS":
				rightRate = new BigDecimal(70);
				leftRate = new BigDecimal(60);
				break;
			case "FAIL":
				rightRate = new BigDecimal(60);
				leftRate = new BigDecimal(0);
				break;
			case "TIMEOUT":
				// 限时作业 通过homework查询timeLimit字段，没取到值的话，即使传了也不按限时查询
				timeLimit = homework.getTimeLimit();
				break;
			case "NOTSUBMIT":
				// 未提交 通过学生作业表的status来查询
				hStatus = StudentHomeworkStatus.NOT_SUBMIT;
				break;
			}
		}

		// 2.排序 正确率排序RATE 用时排序TIME 默认排序
		// 3.学生姓名
		List<StudentHomework> studentHomeworks = stuHkService.listByHomework(id, rightRate, leftRate, timeLimit,
				hStatus, orderby, name);

		// 转换
		List<VStudentHomework> vstuHks = stuHkConvert.to(studentHomeworks, false, false, true, true);
		// 返回值增加是否在限时内完成
		if (homework.getTimeLimit() != null && homework.getTimeLimit().intValue() > 0) {
			int timelimitSecond = 0;
			timelimitSecond = homework.getTimeLimit() * 60;
			for (VStudentHomework v : vstuHks) {
				if (v.getHomeworkTime() > timelimitSecond) {
					v.setTimeout(true);
				}
			}
		}

		vm.put("studentHomeworks", vstuHks);
		return new Value(vm);
	}

	/**
	 * 知识点巩固练习拉取题目接口
	 *
	 * @param knowpointCodes
	 *            新知识点列表
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "2/getPertinenceQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getPertinenceQuestions2(@RequestParam(value = "knowpointCodes") List<Long> knowpointCodes) {
		if (CollectionUtils.isEmpty(knowpointCodes)) {
			return new Value(new IllegalArgException());
		}

		PertinenceHomeworkForm form = new PertinenceHomeworkForm();
		// 难度 基础50% 提高40%冲刺10%,选择题6题 、填空题4题
		form.setBasePercent(50);
		form.setRaisePercent(40);
		form.setSprintPercent(10);
		form.setChoiceNum(6);
		form.setFillBlankNum(4);
		form.setAnswerNum(0);

		form.setKnowledgeCodes(knowpointCodes);
		Map<String, Object> retMap = null;
		List<Long> questionIds = pertinenceHomeworkService.queryPertinenceHomeworkQuestions(form);
		if (CollectionUtils.isEmpty(questionIds)) {
			retMap = new HashMap<String, Object>(1);
			retMap.put("questions", Collections.EMPTY_LIST);

			return new Value(retMap);
		}

		retMap = new HashMap<String, Object>(2);
		List<Question> questions = questionService.mgetList(questionIds);
		int predictTime = 0;

		for (Question q : questions) {
			predictTime += questionService.calPredictTime(q);
		}

		// 按照题型排序
		Collections.sort(questions, (x, y) -> x.getType().getValue() - y.getType().getValue());

		retMap.put("predictTime", predictTime);
		QuestionConvertOption convertOption = new QuestionConvertOption();
		convertOption.setAnswer(true);
		convertOption.setAnalysis(true);
		convertOption.setInitPublishCount(true);
		convertOption.setInitQuestionTag(true);
		convertOption.setInitQuestionSimilarCount(true);

		// 设置题目是否被加入作业篮子
		List<VQuestion> vqList = questionConvert.to(questions, convertOption);
		// 删除source
		for (VQuestion v : vqList) {
			v.setSource("");
		}

		List<Long> carQuestions = questionCarService.getQuestionCarIds(Security.getUserId());
		for (VQuestion q : vqList) {
			if (q.getType() != Type.COMPOSITE) {
				q.setInQuestionCar(carQuestions.contains(q.getId()));
			}
		}

		retMap.put("questions", vqList);

		return new Value(retMap);
	}
}
