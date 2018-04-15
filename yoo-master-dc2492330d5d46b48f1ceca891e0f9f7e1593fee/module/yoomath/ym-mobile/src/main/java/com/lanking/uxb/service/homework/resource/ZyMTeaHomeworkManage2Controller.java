package com.lanking.uxb.service.homework.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.common.resource.question.Question.Type;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticStudentClassKnowpoint;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.HomeworkQuestion;
import com.lanking.cloud.domain.yoomath.homework.QuestionCorrectType;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.ex.YoomathMobileException;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.convert.KnowledgePointConvert;
import com.lanking.uxb.service.code.value.VKnowledgePoint;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticStudentClassKnowpointService;
import com.lanking.uxb.service.diagnostic.value.MasterStatus;
import com.lanking.uxb.service.homework.value.VHomeworkQuestionStudent;
import com.lanking.uxb.service.homework.value.VWeakKonwledgePoint;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.question.util.QuestionUtils;
import com.lanking.uxb.service.resources.api.HomeworkQuestionService;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkQuestionService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.HomeworkQuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvert;
import com.lanking.uxb.service.resources.convert.QuestionConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkAnswerConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkConvertOption;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.value.VAnswer;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.resources.value.VHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomework;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.service.resources.value.VWrongAnswer;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkAnswerService;
import com.lanking.uxb.service.zuoye.api.ZyStudentHomeworkQuestionService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * @since 2.0.3
 * @author peng.zhao
 * @version 2018年2月9日
 */
@RestController
@RequestMapping("zy/m/t/hk/2")
public class ZyMTeaHomeworkManage2Controller {

	@Autowired
	private HomeworkService hkService;
	@Autowired
	private HomeworkConvert hkConvert;
	@Autowired
	private ZyHomeworkClazzConvert hkClassConvert;
	@Autowired
	private ZyHomeworkClassService hkClassService;
	@Autowired
	private HomeworkQuestionService hkQuestionService;
	@Autowired
	private HomeworkQuestionConvert hkQuestionConvert;
	@Autowired
	private StudentHomeworkService stuHkService;
	@Autowired
	private StudentHomeworkQuestionService shqService;
	@Autowired
	private StudentHomeworkAnswerService studentHkAnswerService;
	@Autowired
	private StudentHomeworkQuestionConvert shqConvert;
	@Autowired
	private StudentHomeworkConvert stuHkConvert;
	@Autowired
	private StudentHomeworkAnswerConvert stuHkAnswerConvert;
	@Autowired
	private ZyStudentHomeworkAnswerService stuHkAnswerService;
	@Autowired
	private ZyHomeworkStudentClazzService homeworkStudentClazzService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private DiagnosticClassKnowpointService diagKpService;
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgePointConvert knowledgePointConvert;
	@Autowired
	private ZyStudentHomeworkQuestionService stuHkQuestionService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private QuestionConvert questionConvert;
	@Autowired
	private DiagnosticStudentClassKnowpointService diagStuKpService;

	/**
	 * 待分发
	 */
	private static final String HOMEWORK_STATUS_INIT = "INIT";
	/**
	 * 作业中
	 */
	private static final String HOMEWORK_STATUS_PUBLISH = "PUBLISH";
	/**
	 * 已截止
	 */
	private static final String HOMEWORK_STATUS_CLOSED = "CLOSED";

	@SuppressWarnings("unused")
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "detail", method = { RequestMethod.POST, RequestMethod.GET })
	public Value detail(long id) {
		Homework homework = hkService.get(id);
		if (homework == null || homework.getDelStatus() != Status.ENABLED) {
			return new Value(new YoomathMobileException(YoomathMobileException.YOOMATH_MOBILE_HOMEWORK_DELETE));
		}

		HomeworkConvertOption hOption = new HomeworkConvertOption();
		hOption.setInitMessages(true);
		VHomework v = hkConvert.to(homework, hOption);
		v.setHomeworkClazz(hkClassConvert.to(hkClassService.get(homework.getHomeworkClassId())));
		ValueMap vm = ValueMap.value();

		// 修改作业状态newHomeworkStatus
		if ("0".equals(v.getNewHomeworkStatus())) {
			v.setNewHomeworkStatus(HOMEWORK_STATUS_INIT);
			v.setNewHomeworkStatusName("待分发");
		} else if ("1".equals(v.getNewHomeworkStatus())) {
			v.setNewHomeworkStatus(HOMEWORK_STATUS_PUBLISH);
			v.setNewHomeworkStatusName("作业中");
		} else {
			v.setNewHomeworkStatus(HOMEWORK_STATUS_CLOSED);
			v.setNewHomeworkStatusName("已截止");
		}

		// 作业预估时间
		int homeworkPredictTime = 0;
		
		// 作业题目
		List<HomeworkQuestion> hqs = hkQuestionService.getHomeworkQuestion(id);
		List<VHomeworkQuestion> vhqs = hkQuestionConvert.to(hqs);
		// 按照题型重新排序
		vhqs = getSortHomeworkQuestions(vhqs);

		Map<Long, VHomeworkQuestion> vhqsMap = new HashMap<Long, VHomeworkQuestion>(vhqs.size());
		for (VHomeworkQuestion vhq : vhqs) {
			vhqsMap.put(vhq.getQuestionId(), vhq);
			homeworkPredictTime = homeworkPredictTime + questionService.calPredictTime(vhq.getQuestion().getType(),
					vhq.getQuestion().getDifficulty(), vhq.getQuestion().getSubjectCode());
		}
		v.setHomeworkPredictTime(homeworkPredictTime); // 作业预估时间
		vm.put("homework", v);// 作业

		// 待分发
		long nowTime = System.currentTimeMillis();
		if (HOMEWORK_STATUS_INIT.equals(v.getNewHomeworkStatus())) {
			vm.put("homeworkQuestions", vhqs);
		} else {
			vm.put("homeworkQuestions", vhqs);
			// 学生作业
			List<StudentHomework> studentHomeworks = stuHkService.listByHomework(id);
			StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
			option.setStatisticCorrected(false);
			option.setInitHomework(false);
			option.setInitUser(true);
			option.setSimpleHomework(true);
			option.setInitStuHomeworkCorrectedAndCorrecting(true);
			option.setInitStuHomeworkWrongAndCorrect(true);
			List<VStudentHomework> vstuHks = stuHkConvert.to(studentHomeworks, option);
			if (homework.getTimeLimit() != null && homework.getTimeLimit().intValue() > 0) {
				int timelimitSecond = 0;
				for (VStudentHomework vs : vstuHks) {
					timelimitSecond = homework.getTimeLimit() * 60;
					if (vs.getHomeworkTime() > timelimitSecond) {
						vs.setTimeout(true);
					}
				}
			}
			// 对学生作业按正确率重新排序
			vstuHks = sortStudentHomeworks(vstuHks);
			
			vm.put("studentHomeworks", vstuHks);// 学生作业列表

			Map<Long, StudentHomeworkAnswer> answerMap = getStudentAnswers(hqs, studentHomeworks);
			// 学生作业问题
			Map<Long, VStudentHomeworkQuestion> homeworkQuestions = getStudentHomeworkQuestion(vhqs, studentHomeworks);

			// 题目-学生列表
			// 查询学生个人信息
			List<Long> homeworkStudentIds = Lists.newArrayList();
			for (StudentHomework value : studentHomeworks) {
				homeworkStudentIds.add(value.getStudentId());
			}
			Map<Long, VUser> studentInfoMap = userConvert.mget(homeworkStudentIds, new UserConvertOption(true));
			List<VHomeworkQuestionStudent> vHomeworkQuestionStudent = getTobeCorrectQuestion(v, studentHomeworks,
					homeworkQuestions, studentInfoMap, vhqs);
			vm.put("needCorrectQuestions", vHomeworkQuestionStudent);

			// 作业中
			// 整份作业的正确率=0&有需要批改的作业->进入已到截止时间-有题目需要批改页面
			// 整份作业的正确率=0&没有需要批改的作业->进入已到截止时间-无人提交作业页面

			// 已截止
			if (HOMEWORK_STATUS_CLOSED.equals(v.getNewHomeworkStatus())) {
				// 取得学生加入班级的时间
				Map<Long, Long> studentJoinTime = getJoinClassTime(homework.getHomeworkClassId());

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
								if (studentHomeworkAnswer != null) {
									VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
											.to(studentHomeworkAnswer);
									VWrongAnswer wrongAnswer = new VWrongAnswer();
									BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
									wrongAnswer.setName(
											studentInfoMap.get(vstudentHomeworkAnswer.getAnswerId()).getName());
									// 重新设置imageContent,增加标签
									if (StringUtils.isEmpty(wrongAnswer.getContent())) {
										wrongAnswer.setImageContent("");
									} else {
										wrongAnswer.setImageContent(
												QuestionUtils.process(wrongAnswer.getContent(), false, true));
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
								}
							} else if (Type.FILL_BLANK.equals(vquestion.getType())) {
								// 填空统计的map加sequence
								StudentHomeworkAnswer studentHomeworkAnswer = answerMap.get(sa.getId());
								if (studentHomeworkAnswer != null) {
									VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
											.to(studentHomeworkAnswer);
									VWrongAnswer wrongAnswer = new VWrongAnswer();
									BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
									wrongAnswer.setName(
											studentInfoMap.get(vstudentHomeworkAnswer.getAnswerId()).getName());
									// 重新设置imageContent,增加标签
									if (StringUtils.isEmpty(wrongAnswer.getContent())) {
										wrongAnswer.setImageContent("");
									} else {
										wrongAnswer.setImageContent(
												QuestionUtils.process(wrongAnswer.getContent(), false, true));
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
								}
							} else {
								// 其它题目类型只加入错误学生姓名
								StudentHomeworkAnswer studentHomeworkAnswer = answerMap.get(sa.getId());
								if (studentHomeworkAnswer != null) {
									VStudentHomeworkAnswer vstudentHomeworkAnswer = stuHkAnswerConvert
											.to(studentHomeworkAnswer);
									VWrongAnswer wrongAnswer = new VWrongAnswer();
									BeanUtils.copyProperties(vstudentHomeworkAnswer, wrongAnswer);
									wrongAnswer.setName(
											studentInfoMap.get(vstudentHomeworkAnswer.getAnswerId()).getName());
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
				
				// 表示新知识上线，作业已经采用新知识点了。
				if (CollectionUtils.isNotEmpty(homework.getKnowledgePoints())) {
					// 包含的知识点是当前班级薄弱知识点的查询出来
					List<DiagnosticClassKnowpoint> points = diagKpService
							.findWeakDatasByKps(homework.getHomeworkClassId(), homework.getKnowledgePoints());

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

				if (vHomeworkQuestionStudent.isEmpty()) {
					List<Integer> resultsDistribution = null;
					if (v.getDistributeCount() > 0) {
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
						resultsDistribution = Lists.newArrayList(0, 0, 0, 0);
					}
					vm.put("resultsDistribution", resultsDistribution);// 作业结果分布

					
				}
			}
		}

		return new Value(vm);
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
	 * 取待批改的学生题目列表
	 * 
	 * @return vHomeworkQuestionStudent
	 */
	private List<VHomeworkQuestionStudent> getTobeCorrectQuestion(VHomework v, List<StudentHomework> studentHomeworks,
			Map<Long, VStudentHomeworkQuestion> homeworkQuestions, Map<Long, VUser> studentInfoMap, 
			List<VHomeworkQuestion> vhqs) {
		List<VHomeworkQuestionStudent> vHomeworkQuestionStudent = Lists.newArrayList();
		if (v.isTobeCorrected()) {
			List<VStudentHomeworkQuestion> vsqs = Lists.newArrayList(); // 原题目
			for (VStudentHomeworkQuestion hq : homeworkQuestions.values()) {
				vsqs.add(hq);
			}
			// 根据学生作业id取studentHomeworkQuestion
			List<Long> studentHomeworkIds = studentHomeworks.stream().map(p -> p.getId()).collect(Collectors.toList());
			List<StudentHomeworkQuestion> studentHomeworkQuestions = shqService.findByNewCorrect(studentHomeworkIds,
					null, true); // 订正的题目
			if (!studentHomeworkQuestions.isEmpty()) {
				List<VStudentHomeworkQuestion> vps = shqConvert.to(studentHomeworkQuestions);
				vsqs.addAll(vps);
			}

			// 把待教师批改的学生作业取出来result = INIT && correctType = TEACHER_CORRECT
			List<VStudentHomeworkQuestion> needCorrectQuestions = Lists.newArrayList();
			for (VStudentHomeworkQuestion studentHomeworkQuestion : vsqs) {
				if ((studentHomeworkQuestion.getResult() == HomeworkAnswerResult.INIT
						|| studentHomeworkQuestion.getResult() == HomeworkAnswerResult.UNKNOW)
						&& studentHomeworkQuestion.getCorrectType() == QuestionCorrectType.TEACHER_CORRECT) {
					needCorrectQuestions.add(studentHomeworkQuestion);
				}
			}
			
			if (!needCorrectQuestions.isEmpty()) {
				Map<Long, Integer> questionSequence = new HashMap<>(); // 排序后的题目顺序
				int questionCount = 1;
				for (VHomeworkQuestion vhq : vhqs) {
					if (vhq.getQuestion().getType() == Type.QUESTION_ANSWERING) {
						questionSequence.put(vhq.getQuestionId(), questionCount);
					}
					questionCount++;
				}
				
				
				// 待批改的学生题目id
				List<Long> todoCorrectIds = needCorrectQuestions.stream().map(p -> p.getId()).collect(Collectors.toList());

				Map<Long, List<StudentHomeworkAnswer>> answers = studentHkAnswerService.find(todoCorrectIds);
				// 这里的questionId是按sequence排序的，可以直接用
				List<Long> homeworkQuestionIds = hkQuestionService.getQuestion(v.getId());
							
				// 定义保存题目id对应学生作业的map
				Map<Long, List<VStudentHomeworkQuestion>> questionIdStudentMap = new HashMap<>();
				for (Long questionId : homeworkQuestionIds) {
					for (VStudentHomeworkQuestion value : needCorrectQuestions) {
						// 添加答案信息
						value.setStudentHomeworkAnswers(stuHkAnswerConvert.to(answers.get(value.getId())));
						if (questionId.longValue() == value.getQuestionId()) {
							if (questionIdStudentMap.containsKey(questionId)) {
								List<VStudentHomeworkQuestion> s = questionIdStudentMap.get(questionId);
								s.add(value);
								questionIdStudentMap.put(questionId, s);
							} else {
								List<VStudentHomeworkQuestion> s = Lists.newArrayList(value);
								questionIdStudentMap.put(questionId, s);
							}
						}
					}
				}

				// 查询学生个人信息
				for (int i = 0; i < homeworkQuestionIds.size(); i++) {
					if (questionIdStudentMap.containsKey(homeworkQuestionIds.get(i))) {
						VHomeworkQuestionStudent vquestionStudent = new VHomeworkQuestionStudent();
						vquestionStudent.setQuestionId(homeworkQuestionIds.get(i));
						vquestionStudent.setSequence(questionSequence.get(homeworkQuestionIds.get(i)));
						vquestionStudent.setStudentHomeworkQuestions(questionIdStudentMap.get(homeworkQuestionIds.get(i)));
						// 添加学生信息
						List<VUser> studentInfos = Lists.newArrayList();
						
						for (VStudentHomeworkQuestion shq : questionIdStudentMap.get(homeworkQuestionIds.get(i))) {
							for (StudentHomework studentHomework : studentHomeworks) {
								if (studentHomework.getId().longValue() == shq.getStudentHomeworkId()) {
									studentInfos.add(studentInfoMap.get(studentHomework.getStudentId()));
									break;
								}
							}
						}
						vquestionStudent.setUsers(studentInfos);

						vHomeworkQuestionStudent.add(vquestionStudent);
					}
				}
				
				// 添加题目对象
				for (VHomeworkQuestionStudent value : vHomeworkQuestionStudent) {
					for (VHomeworkQuestion vhq : vhqs) {
						if (value.getQuestionId() == vhq.getQuestionId()) {
							value.setQuestion(vhq.getQuestion());
							break;
						}
					}
				}
			}
		}

		return vHomeworkQuestionStudent;
	}
	
	/**
	 * 小悠快批对题目重新排序,按照选择-填空-解答-其它排序
	 * 
	 * @return sortVhqs
	 */
	private List<VHomeworkQuestion> getSortHomeworkQuestions(List<VHomeworkQuestion> vhqs) {
		List<VHomeworkQuestion> sortVhqs = Lists.newArrayList();
		
		List<VHomeworkQuestion> singleChoices = Lists.newArrayList();
		List<VHomeworkQuestion> multipleChoices = Lists.newArrayList();
		List<VHomeworkQuestion> fillBanks = Lists.newArrayList();
		List<VHomeworkQuestion> trueOrFalse = Lists.newArrayList();
		List<VHomeworkQuestion> answers = Lists.newArrayList();
		List<VHomeworkQuestion> others = Lists.newArrayList();
		for (VHomeworkQuestion vhq : vhqs) {
			if (vhq.getQuestion().getType() == Type.SINGLE_CHOICE) {
				singleChoices.add(vhq);
			} else if (vhq.getQuestion().getType() == Type.MULTIPLE_CHOICE) {
				multipleChoices.add(vhq);
			} else if (vhq.getQuestion().getType() == Type.FILL_BLANK) {
				fillBanks.add(vhq);
			} else if (vhq.getQuestion().getType() == Type.TRUE_OR_FALSE) {
				trueOrFalse.add(vhq);
			} else if (vhq.getQuestion().getType() == Type.QUESTION_ANSWERING) {
				answers.add(vhq);
			} else {
				others.add(vhq);
			}
		}
		
		sortVhqs.addAll(singleChoices);
		sortVhqs.addAll(multipleChoices);
		sortVhqs.addAll(fillBanks);
		sortVhqs.addAll(trueOrFalse);
		sortVhqs.addAll(answers);
		sortVhqs.addAll(others);
		
		return sortVhqs;
	}
	
	/**
	 * 对学生作业重新排序
	 * 
	 * @param vstuHks
	 */
	private List<VStudentHomework> sortStudentHomeworks(List<VStudentHomework> vstuHks) {
		List<VStudentHomework> result = Lists.newArrayList();
		List<VStudentHomework> notSubmit = Lists.newArrayList();
		List<VStudentHomework> submited = Lists.newArrayList();
		
		for (VStudentHomework value : vstuHks) {
			if (value.getStuSubmitAt() == null) {
				notSubmit.add(value);
			} else {
				submited.add(value);
			}
		}
		// 按照sequence排序
		Collections.sort(submited, new Comparator<VStudentHomework>() {
			@Override
			public int compare(VStudentHomework q1, VStudentHomework q2) {
				if (q1.getRightRate() != null && q2.getRightRate() != null) {
					return q2.getRightRate().compareTo(q1.getRightRate());
				} else if (q1.getRightRate() == null && q2.getRightRate() == null) {
					return 0;
				} else if (q1.getRightRate() == null) {
					return 1;
				} else {
					return -1;
				}
			}
		});
		
		result.addAll(submited);
		result.addAll(notSubmit);
		return result;
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
		
		HomeworkConvertOption hOption = new HomeworkConvertOption();
		hOption.setInitMessages(true);
		VHomework vhomework = hkConvert.to(hk, hOption);
		ValueMap vm = ValueMap.value("homework", vhomework);

		StudentHomeworkConvertOption option = new StudentHomeworkConvertOption();
		option.setStatisticCorrected(false);
		option.setInitHomework(false);
		option.setInitUser(true);
		option.setSimpleHomework(true);
		option.setInitStuHomeworkCorrectedAndCorrecting(true);
		option.setInitStuHomeworkWrongAndCorrect(true);
		option.setStatisticTobeCorrected(true);
		VStudentHomework vstudentHomework = stuHkConvert.to(stuHk, option);
		vm.put("studentHomework", vstudentHomework);
		
		// 作业题目
		List<HomeworkQuestion> hqs = hkQuestionService.getHomeworkQuestion(stuHk.getHomeworkId());
		List<VHomeworkQuestion> vhqs = hkQuestionConvert.to(hqs);
		// 按照题型重新排序
		vhqs = getSortHomeworkQuestions(vhqs);
		List<Long> qids = vhqs.stream().map(p -> p.getQuestionId()).collect(Collectors.toList());
//		List<Long> qids = hkQuestionService.getQuestion(stuHk.getHomeworkId());

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
		for (VQuestion v : vqs) {
			if (v.getType() == Type.FILL_BLANK) {
				int size = v.getStudentHomeworkAnswers().size();
				for (int i = 0; i < size; i++) {
					// 每一空的颜色根据学生空的正确性来进行渲染
					if (v.getStudentHomeworkAnswers().get(i).getResult() == HomeworkAnswerResult.RIGHT) {
						v.getStudentHomeworkAnswers().get(i).setImageContent(
								QuestionUtils.process(v.getStudentHomeworkAnswers().get(i).getContent(), true, true));
					} else if (v.getStudentHomeworkAnswers().get(i).getResult() == HomeworkAnswerResult.WRONG) {
						v.getStudentHomeworkAnswers().get(i).setImageContent(
								QuestionUtils.process(v.getStudentHomeworkAnswers().get(i).getContent(), false, true));
					} else {
						v.getStudentHomeworkAnswers().get(i).setImageContent(
								QuestionUtils.process(v.getStudentHomeworkAnswers().get(i).getContent(), null, true));
					}
					// 处理订正题
					if (v.getCorrectStudentHomeworkAnswers() != null
							&& !v.getCorrectStudentHomeworkAnswers().isEmpty()) {
						VStudentHomeworkAnswer correctAnswer = v.getCorrectStudentHomeworkAnswers().get(i);
						if (correctAnswer != null) {
							v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(QuestionUtils.process(
									v.getCorrectStudentHomeworkAnswers().get(i).getContent(),
									v.getStudentHomeworkAnswers().get(i).getResult() == HomeworkAnswerResult.RIGHT,
									true));
							if (correctAnswer.getResult() == HomeworkAnswerResult.RIGHT) {
								v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(
										QuestionUtils.process(correctAnswer.getContent(), true, true));
							} else if (correctAnswer.getResult() == HomeworkAnswerResult.WRONG) {
								v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(
										QuestionUtils.process(correctAnswer.getContent(), false, true));
							} else {
								v.getCorrectStudentHomeworkAnswers().get(i).setImageContent(
										QuestionUtils.process(correctAnswer.getContent(), null, true));
							}
						}
					}
				}
			}
		}
		
		// 处理申诉入口
		for (VQuestion value : vqs) {
			getAppeal(value, hk);
		}
		
		vm.put("questions", vqs);

		// 返回此学生薄弱知识点
		List<VKnowledgePoint> knowledgePoints = vhomework.getKnowledgePoints();
		if (CollectionUtils.isNotEmpty(knowledgePoints)) {
			// 新知识点
			Set<Long> codes = new HashSet<Long>(knowledgePoints.size());
			for (VKnowledgePoint knowledgePoint : knowledgePoints) {
				codes.add(knowledgePoint.getCode());
			}

			// 与web端保持一致
			List<DiagnosticStudentClassKnowpoint> list = diagStuKpService.queryHistoryWeakListByCodes(
					stuHk.getStudentId(), vhomework.getHomeworkClazzId(), codes);
			if (list.size() > 0) {
				Set<Long> weakCodes = new HashSet<Long>(knowledgePoints.size());
				for (DiagnosticStudentClassKnowpoint dsck : list) {
					weakCodes.add(dsck.getKnowpointCode());
				}
				List<KnowledgePoint> weakKnowledges = knowledgePointService.mgetList(weakCodes);
				vm.put("weakPoints", knowledgePointConvert.to(weakKnowledges));
			} else {
				vm.put("weakPoints", Lists.newArrayList());
			}
		}

		return new Value(vm);
	}
	
	/**
	 * 处理题目是否可申诉
	 * 
	 * @param vquestion
	 */
	private void getAppeal(VQuestion v, Homework homework) {
		VStudentHomeworkQuestion vStudentHomeworkQuestion = v.getStudentHomeworkQuestion();
		if ((v.getType() == Question.Type.FILL_BLANK && vStudentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG)
				|| (v.getType() == Question.Type.QUESTION_ANSWERING
						&& homework.isAnswerQuestionAutoCorrect()
						&& vStudentHomeworkQuestion.getResult() == HomeworkAnswerResult.WRONG && vStudentHomeworkQuestion.getRightRate() == 0)) {
			Boolean canAppeal = true;
			Boolean allBlank = true;
			
			// 填空题如果一个空都没有作答，不能申述
			if(v.getType() == Question.Type.FILL_BLANK) {
				for (VStudentHomeworkAnswer sha : v.getStudentHomeworkAnswers()) {
					if (StringUtils.isNotBlank(sha.getContent()) && sha.getResult() == HomeworkAnswerResult.WRONG) {
						allBlank = false;
					}
				}
				
				if(allBlank) {
					canAppeal = false;
				}
			}
					
			// 解答题 如果没有作答，也不能申述
			if (v.getType() == Question.Type.QUESTION_ANSWERING && (vStudentHomeworkQuestion.getAnswerImg() == null || vStudentHomeworkQuestion.getAnswerImgId() <= 0)) {
				canAppeal = false;
			}
			
			vStudentHomeworkQuestion.setCanAppeal(canAppeal);
		}
	}
	
	/**
	 * 修改待分发作业答题时间、截止时间、限时
	 * 
	 * @since 1.5.0
	 * @param id
	 *            作业ID
	 * @param startTime
	 *            答题开始时间
	 * @param deadline
	 *            截止时间
	 * @param timeLimit
	 *            作业限时时间（单位分钟） 不传默认null
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MasterSlaveDataSource(type = "M")
	@RequestMapping(value = "changeInitHomeworkTime", method = { RequestMethod.POST, RequestMethod.GET })
	public Value changeInitHomeworkTime(Long id, Long startTime, Long deadline, Integer timeLimit) {
		if (id == null || startTime == null || deadline == null) {
			return new Value(new IllegalArgException());
		}
		Homework hk = hkService.get(id);
		// 业务校验
		if (hk == null || hk.getCreateId() != Security.getUserId() || startTime >= deadline
				|| deadline < System.currentTimeMillis() + 600000) {
			return new Value(new IllegalArgException());
		}
		
		// 更新
		Homework homework = hkService.setTimeAndTimelimit(id, startTime, deadline, timeLimit);
		
		Map<String, Object> data = new HashMap<>();
		data.put("homework", homework);
		
		return new Value(data);
	}
}
