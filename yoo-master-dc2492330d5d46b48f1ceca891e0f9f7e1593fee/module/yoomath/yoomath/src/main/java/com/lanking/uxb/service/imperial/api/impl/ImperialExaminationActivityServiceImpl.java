package com.lanking.uxb.service.imperial.api.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityCfg;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityUser;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationAwardType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationCompositeIndex;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationMessageTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationUserProcess;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityStudentService;
import com.lanking.uxb.service.imperial.api.ImperialExaminationActivityUserService;
import com.lanking.uxb.service.imperial.value.VExaminationTime;
import com.lanking.uxb.service.session.api.impl.Security;

@Service
@Transactional(readOnly = true)
public class ImperialExaminationActivityServiceImpl implements ImperialExaminationActivityService {
	@Autowired
	@Qualifier("ImperialExaminationActivityRepo")
	private Repo<ImperialExaminationActivity, Long> repo;
	@Autowired
	private ImperialExaminationActivityUserService activityUserService;
	@Autowired
	private ImperialExaminationActivityStudentService imperialExamActivityStudentService;

	@Transactional
	@Override
	public void createData() {
		ImperialExaminationActivity m = new ImperialExaminationActivity();
		m.setCode(1L);
		ImperialExaminationActivityCfg cfg = new ImperialExaminationActivityCfg();
		List<ImperialExaminationGrade> grades = new ArrayList<ImperialExaminationGrade>();
		for (ImperialExaminationGrade grade : ImperialExaminationGrade.values()) {
			grades.add(grade);
		}
		cfg.setGrades(grades);
		//
		List<ImperialExaminationProcessTime> timeList = new ArrayList<ImperialExaminationProcessTime>();
		for (ImperialExaminationProcess process : ImperialExaminationProcess.values()) {
			ImperialExaminationProcessTime time = new ImperialExaminationProcessTime();
			time.setProcess(process);
			time.setStartTime(new Date());
			time.setEndTime(new Date());
			timeList.add(time);
		}
		cfg.setTimeList(timeList);
		//
		List<ImperialExaminationAwardType> awardList = new ArrayList<ImperialExaminationAwardType>();
		ImperialExaminationAwardType type = new ImperialExaminationAwardType();
		type.setPrizeName("扫地机器人");
		type.setPrize("一等奖");
		type.setNum(1);
		type.setAwardLevel(1);
		awardList.add(type);
		ImperialExaminationAwardType type1 = new ImperialExaminationAwardType();
		type1.setPrizeName("小米空气净化器");
		type1.setPrize("二等奖");
		type1.setNum(2);
		type1.setAwardLevel(2);
		awardList.add(type1);
		cfg.setAwardList(awardList);
		//
		List<ImperialExaminationCompositeIndex> indexList = new ArrayList<ImperialExaminationCompositeIndex>();
		ImperialExaminationCompositeIndex index = new ImperialExaminationCompositeIndex();
		index.setName("平均提交率");
		index.setRate(0.6);
		indexList.add(index);
		ImperialExaminationCompositeIndex index1 = new ImperialExaminationCompositeIndex();
		index1.setName("平均正确率");
		index1.setRate(0.4);
		indexList.add(index1);
		cfg.setIndexList(indexList);

		// 消息发送时间配置
		try {
			List<ImperialExaminationMessageTime> messageTimes = new ArrayList<ImperialExaminationMessageTime>();
			SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");

			// 短信
			ImperialExaminationMessageTime mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.SMS);
			mt.setStartTime(format.parse("2017.04.05 12:00"));
			mt.setMessageTemplateCode(10000025);
			mt.setUserScope(1);
			messageTimes.add(mt);

			// 推送
			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.05 14:00"));
			mt.setMessageTemplateCode(12000012);
			mt.setUserScope(2);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.07 14:00"));
			mt.setMessageTemplateCode(12000013);
			mt.setUserScope(2);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.09 14:00"));
			mt.setMessageTemplateCode(12000014);
			mt.setUserScope(2);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.14 14:00"));
			mt.setMessageTemplateCode(12000015);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.16 14:00"));
			mt.setMessageTemplateCode(12000016);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.21 14:00"));
			mt.setMessageTemplateCode(12000017);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.23 14:00"));
			mt.setMessageTemplateCode(12000018);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.27 14:00"));
			mt.setMessageTemplateCode(12000019);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.04.30 14:00"));
			mt.setMessageTemplateCode(12000020);
			mt.setUserScope(3);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(format.parse("2017.05.03 10:00"));
			mt.setMessageTemplateCode(12000021);
			mt.setUserScope(2);
			messageTimes.add(mt);

			cfg.setMessageList(messageTimes);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		m.setCfg(cfg);
		repo.save(m);
	}

	@Override
	public ImperialExaminationActivity getActivity(Long code) {
		return repo.find("$getActivity", Params.param("code", code)).get();
	}

	@Override
	public ImperialExaminationProcessTime get(ImperialExaminationProcess process, long code) {
		ImperialExaminationActivity activity = this.getActivity(code);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == process) {
				return time;
			}
		}
		return null;
	}

	@Override
	public Map<ImperialExaminationUserProcess, Long> getCountDownTime(Long code, ImperialExaminationProcessTime time) {
		Map<ImperialExaminationUserProcess, Long> map = new HashMap<ImperialExaminationUserProcess, Long>();
		if (time != null) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				ImperialExaminationActivityUser activityUser = activityUserService.getUser(code, Security.getUserId());
				// 如果在报名时间内，用户没有报名进入看到的就是报名页，如果已经报名进来看到的就是活动页
				if (activityUser == null) {
					// 报名阶段,距报名截止时间
					map.put(ImperialExaminationUserProcess.USER_PROCESS_SIGNUP, time.getEndTime().getTime()
							- new Date().getTime());
				} else {
					map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL1,
							this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL2, code).getStartTime().getTime()
									- new Date().getTime());
				}

			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL1) {
				// 乡试准备阶段,距乡试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL1,
						this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
				// 乡试答题、下发批改阶段,距乡试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL2,
						this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL3, code).getEndTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
				// 乡试成绩调整、成绩公布阶段,会试准备阶段,距会试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_METROPOLITAN1,
						this.get(ImperialExaminationProcess.PROCESS_METROPOLITAN2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
				// 会试答题、下发批改阶段,距会试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_METROPOLITAN2,
						this.get(ImperialExaminationProcess.PROCESS_METROPOLITAN3, code).getEndTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
				// 会试成绩调整、成绩公布阶段,距殿试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_EXAMINATION1,
						this.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
				// 殿试答题、下发批改阶段,距殿试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_EXAMINATION2,
						this.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3, code).getEndTime().getTime()
								- new Date().getTime());
			}
			// 殿试成绩调整,殿试公布成绩阶段 返回空的map
		}
		return map;
	}

	@Override
	public List<VExaminationTime> queryExamTime(Long code) {
		ImperialExaminationActivity activity = this.getActivity(code);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		Date startTime1 = null;
		Date endTime1 = null;
		Date startTime2 = null;
		Date endTime2 = null;
		Date startTime3 = null;
		Date endTime3 = null;
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2) {
				startTime1 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
				endTime1 = time.getEndTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
				startTime2 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
				endTime2 = time.getEndTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
				startTime3 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
				endTime3 = time.getEndTime();
			}
		}
		List<VExaminationTime> list = new ArrayList<VExaminationTime>();
		for (ImperialExaminationType type : ImperialExaminationType.values()) {
			VExaminationTime v = new VExaminationTime();
			v.setType(type);
			if (ImperialExaminationType.PROVINCIAL_EXAMINATION == type) {
				v.setStartTime(startTime1);
				v.setEndTime(endTime1);
				v.setDayOfWeek1(getWeekOfDate(startTime1));
				v.setDayOfWeek2(getWeekOfDate(endTime1));
			} else if (ImperialExaminationType.METROPOLITAN_EXAMINATION == type) {
				v.setStartTime(startTime2);
				v.setEndTime(endTime2);
				v.setDayOfWeek1(getWeekOfDate(startTime2));
				v.setDayOfWeek2(getWeekOfDate(endTime2));
			} else if (ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION == type) {
				v.setStartTime(startTime3);
				v.setEndTime(endTime3);
				v.setDayOfWeek1(getWeekOfDate(startTime3));
				v.setDayOfWeek2(getWeekOfDate(endTime3));
			}
			list.add(v);
		}
		return list;
	}

	public String getWeekOfDate(Date dt) {
		String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekDays[w];
	}

	@Override
	public List<VExaminationTime> queryExamTime2(Long code) {
		ImperialExaminationActivity activity = this.getActivity(code);
		List<ImperialExaminationProcessTime> timeList = activity.getCfg().getTimeList();
		Date startTime1 = null;
		Date endTime1 = null;
		Date startTime2 = null;
		Date endTime2 = null;
		Date startTime3 = null;
		Date endTime3 = null;
		for (ImperialExaminationProcessTime time : timeList) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2) {
				startTime1 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4) {
				endTime1 = time.getEndTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
				startTime2 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
				endTime2 = time.getEndTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
				startTime3 = time.getStartTime();
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4) {
				endTime3 = time.getEndTime();
			}
		}
		List<VExaminationTime> list = new ArrayList<VExaminationTime>();
		for (ImperialExaminationType type : ImperialExaminationType.values()) {
			VExaminationTime v = new VExaminationTime();
			v.setType(type);
			if (ImperialExaminationType.PROVINCIAL_EXAMINATION == type) {
				v.setStartTime(startTime1);
				v.setEndTime(endTime1);
				v.setDayOfWeek1(getWeekOfDate(startTime1));
				v.setDayOfWeek2(getWeekOfDate(endTime1));
			} else if (ImperialExaminationType.METROPOLITAN_EXAMINATION == type) {
				v.setStartTime(startTime2);
				v.setEndTime(endTime2);
				v.setDayOfWeek1(getWeekOfDate(startTime2));
				v.setDayOfWeek2(getWeekOfDate(endTime2));
			} else if (ImperialExaminationType.FINAL_IMPERIAL_EXAMINATION == type) {
				v.setStartTime(startTime3);
				v.setEndTime(endTime3);
				v.setDayOfWeek1(getWeekOfDate(startTime3));
				v.setDayOfWeek2(getWeekOfDate(endTime3));
			}
			list.add(v);
		}
		return list;
	}

	@Override
	public Map<ImperialExaminationUserProcess, Long> getCountDownTimeStudent(Long code,
			ImperialExaminationProcessTime time) {
		Map<ImperialExaminationUserProcess, Long> map = new HashMap<ImperialExaminationUserProcess, Long>();
		if (time != null) {
			if (time.getProcess() == ImperialExaminationProcess.PROCESS_SIGNUP) {
				// ImperialExaminationActivityStudent activityStudentUser =
				// imperialExamActivityStudentService
				// .getUser(code, Security.getUserId());
				// 如果在报名时间内，用户没有报名进入看到的就是报名页，如果已经报名进来看到的就是活动页
				// if (activityStudentUser == null) {
				// // 报名阶段,距报名截止时间
				// map.put(ImperialExaminationUserProcess.USER_PROCESS_SIGNUP,
				// time.getEndTime().getTime() - new Date().getTime());
				// } else {
				// map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL1,
				// this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL2,
				// code).getStartTime().getTime()
				// - new Date().getTime());
				// }
				// 报名阶段,距报名截止时间 戚让改的
				map.put(ImperialExaminationUserProcess.USER_PROCESS_SIGNUP,
						time.getEndTime().getTime() - new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL1) {
				// 乡试准备阶段,距乡试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL1,
						this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
				// 乡试答题、下发批改阶段,距乡试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_PROVINCIAL2,
						this.get(ImperialExaminationProcess.PROCESS_PROVINCIAL3, code).getEndTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL4
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_PROVINCIAL5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
				// 乡试成绩调整、成绩公布阶段,会试准备阶段,距会试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_METROPOLITAN1,
						this.get(ImperialExaminationProcess.PROCESS_METROPOLITAN2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
				// 会试答题、下发批改阶段,距会试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_METROPOLITAN2,
						this.get(ImperialExaminationProcess.PROCESS_METROPOLITAN3, code).getEndTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN4
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_METROPOLITAN5
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
				// 会试成绩调整、成绩公布阶段,距殿试开始还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_EXAMINATION1,
						this.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2, code).getStartTime().getTime()
								- new Date().getTime());
			} else if (time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2
					|| time.getProcess() == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
				// 殿试答题、下发批改阶段,距殿试结束还有多久
				map.put(ImperialExaminationUserProcess.USER_PROCESS_EXAMINATION2,
						this.get(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3, code).getEndTime().getTime()
								- new Date().getTime());
			}
			// 殿试成绩调整,殿试公布成绩阶段 返回空的map
		}

		return map;
	}
}
