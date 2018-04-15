package com.lanking.uxb.zycon.activity.api.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.base.message.api.MessageType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivity;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityCfg;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityPrizes;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationAwardType;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationCompositeIndex;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationGrade;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationMessageTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcess;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationProcessTime;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationRoom;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycImperialExamConfigService;
import com.lanking.uxb.zycon.activity.form.ZycExamConfigForm;

import httl.util.CollectionUtils;

@Transactional(readOnly = true)
@Service
public class ZycImperialExamConfigServiceImpl implements ZycImperialExamConfigService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRepo")
	private Repo<ImperialExaminationActivity, Long> repo;
	@Autowired
	@Qualifier("ImperialExaminationActivityPrizesRepo")
	private Repo<ImperialExaminationActivityPrizes, Long> prizesRepo;

	@Transactional
	@Override
	public void save(ZycExamConfigForm form) {
		ImperialExaminationActivity activity = new ImperialExaminationActivity();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (form.getCode() == null) {
				activity.setCode(this.maxCode());
			} else {
				this.deleteByCode(form.getCode());
				activity.setCode(form.getCode());
			}

			activity.setStartTime(sdf.parse(form.getStartTime()));
			activity.setEndTime(sdf.parse(form.getEndTime()));
			activity.setStatus(Status.ENABLED);
			activity.setCreateAt(new Date());
			// 这个是固定的
			ImperialExaminationActivityCfg cfg = new ImperialExaminationActivityCfg();
			List<ImperialExaminationGrade> grades = new ArrayList<ImperialExaminationGrade>();
			for (ImperialExaminationGrade grade : ImperialExaminationGrade.values()) {
				grades.add(grade);
			}
			cfg.setGrades(grades);
			//
			List<ImperialExaminationCompositeIndex> indexList = new ArrayList<ImperialExaminationCompositeIndex>();
			ImperialExaminationCompositeIndex index = new ImperialExaminationCompositeIndex();
			index.setName("平均提交率");
			index.setRate(0.7);
			indexList.add(index);
			ImperialExaminationCompositeIndex index1 = new ImperialExaminationCompositeIndex();
			index1.setName("平均正确率");
			index1.setRate(0.3);
			indexList.add(index1);
			cfg.setIndexList(indexList);
			// 阶段流程配置
			List<ImperialExaminationProcessTime> timeList = new ArrayList<ImperialExaminationProcessTime>();
			for (ImperialExaminationProcess process : ImperialExaminationProcess.values()) {
				ImperialExaminationProcessTime time = new ImperialExaminationProcessTime();
				time.setProcess(process);
				if (process == ImperialExaminationProcess.PROCESS_SIGNUP) {
					time.setStartTime(form.getSignupStart() == null ? null : sdf.parse(form.getSignupStart()));
					time.setEndTime(form.getSignupEnd() == null ? null : sdf.parse(form.getSignupEnd()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL1) {
					time.setStartTime(form.getProvincial1Start() == null ? null : sdf.parse(form.getProvincial1Start()));
					time.setEndTime(form.getProvincial1End() == null ? null : sdf.parse(form.getProvincial1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL2) {
					time.setStartTime(form.getProvincial2Start() == null ? null : sdf.parse(form.getProvincial2Start()));
					time.setEndTime(form.getProvincial2End() == null ? null : sdf.parse(form.getProvincial2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
					time.setStartTime(form.getProvincial3Start() == null ? null : sdf.parse(form.getProvincial3Start()));
					time.setEndTime(form.getProvincial3End() == null ? null : sdf.parse(form.getProvincial3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL4) {
					time.setStartTime(form.getProvincial4Start() == null ? null : sdf.parse(form.getProvincial4Start()));
					time.setEndTime(form.getProvincial4End() == null ? null : sdf.parse(form.getProvincial4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
					time.setStartTime(form.getProvincial5Start() == null ? null : sdf.parse(form.getProvincial5Start()));
					time.setEndTime(form.getProvincial5End() == null ? null : sdf.parse(form.getProvincial5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
					time.setStartTime(form.getMetropolitan1Start() == null ? null : sdf.parse(form
							.getMetropolitan1Start()));
					time.setEndTime(form.getMetropolitan1End() == null ? null : sdf.parse(form.getMetropolitan1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
					time.setStartTime(form.getMetropolitan2Start() == null ? null : sdf.parse(form
							.getMetropolitan2Start()));
					time.setEndTime(form.getMetropolitan2End() == null ? null : sdf.parse(form.getMetropolitan2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
					time.setStartTime(form.getMetropolitan3Start() == null ? null : sdf.parse(form
							.getMetropolitan3Start()));
					time.setEndTime(form.getMetropolitan3End() == null ? null : sdf.parse(form.getMetropolitan3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
					time.setStartTime(form.getMetropolitan4Start() == null ? null : sdf.parse(form
							.getMetropolitan4Start()));
					time.setEndTime(form.getMetropolitan4End() == null ? null : sdf.parse(form.getMetropolitan4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
					time.setStartTime(form.getMetropolitan5Start() == null ? null : sdf.parse(form
							.getMetropolitan5Start()));
					time.setEndTime(form.getMetropolitan5End() == null ? null : sdf.parse(form.getMetropolitan5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
					time.setStartTime(form.getFinal1Start() == null ? null : sdf.parse(form.getFinal1Start()));
					time.setEndTime(form.getFinal1End() == null ? null : sdf.parse(form.getFinal1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
					time.setStartTime(form.getFinal2Start() == null ? null : sdf.parse(form.getFinal2Start()));
					time.setEndTime(form.getFinal2End() == null ? null : sdf.parse(form.getFinal2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
					time.setStartTime(form.getFinal3Start() == null ? null : sdf.parse(form.getFinal3Start()));
					time.setEndTime(form.getFinal3End() == null ? null : sdf.parse(form.getFinal3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4) {
					time.setStartTime(form.getFinal4Start() == null ? null : sdf.parse(form.getFinal4Start()));
					time.setEndTime(form.getFinal4End() == null ? null : sdf.parse(form.getFinal4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5) {
					time.setStartTime(form.getFinal5Start() == null ? null : sdf.parse(form.getFinal5Start()));
					time.setEndTime(form.getFinal5End() == null ? null : sdf.parse(form.getFinal5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_TOTALRANKING) {
					time.setStartTime(form.getTotalRankStart() == null ? null : sdf.parse(form.getTotalRankStart()));
					time.setEndTime(form.getTotalRankEnd() == null ? null : sdf.parse(form.getTotalRankEnd()));
				} else if (process == ImperialExaminationProcess.PROCESS_AWARDS) {
					time.setStartTime(form.getAwardsStart() == null ? null : sdf.parse(form.getAwardsStart()));
					time.setEndTime(form.getAwardsEnd() == null ? null : sdf.parse(form.getAwardsEnd()));
				}
				timeList.add(time);
			}
			cfg.setTimeList(timeList);

			List<ImperialExaminationMessageTime> messageTimes = new ArrayList<ImperialExaminationMessageTime>();

			// 短信
			ImperialExaminationMessageTime mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.SMS);
			mt.setStartTime(form.getSmsStart() == null ? null : sdf.parse(form.getSmsStart()));
			mt.setMessageTemplateCode(10000025);
			mt.setUserScope(1); // 未使用过APP的渠道初中教师
			messageTimes.add(mt);

			// 推送
			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush1() == null ? null : sdf.parse(form.getPush1()));
			mt.setMessageTemplateCode(12000012);
			mt.setUserScope(2); // 未报名的渠道初中教师
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush2() == null ? null : sdf.parse(form.getPush2()));
			mt.setMessageTemplateCode(12000013); // 未报名的渠道初中教师
			mt.setUserScope(2);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush3() == null ? null : sdf.parse(form.getPush3()));
			mt.setMessageTemplateCode(12000014); // 未报名的渠道初中教师
			mt.setUserScope(2);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush4() == null ? null : sdf.parse(form.getPush4()));
			mt.setMessageTemplateCode(12000015); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush5() == null ? null : sdf.parse(form.getPush5()));
			mt.setMessageTemplateCode(12000016); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush6() == null ? null : sdf.parse(form.getPush6()));
			mt.setMessageTemplateCode(12000017); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush7() == null ? null : sdf.parse(form.getPush7()));
			mt.setMessageTemplateCode(12000018); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush8() == null ? null : sdf.parse(form.getPush8()));
			mt.setMessageTemplateCode(12000019); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush9() == null ? null : sdf.parse(form.getPush9()));
			mt.setMessageTemplateCode(12000020); // 已报名的渠道初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush10() == null ? null : sdf.parse(form.getPush10()));
			mt.setMessageTemplateCode(12000021); // 所有渠道初中教师
			mt.setUserScope(3);
			messageTimes.add(mt);

			cfg.setMessageList(messageTimes);

			// 奖品列表
			List<ImperialExaminationAwardType> awardList = new ArrayList<ImperialExaminationAwardType>();
			for (int i = 0; i < 4; i++) {
				ImperialExaminationAwardType type = new ImperialExaminationAwardType();
				if (i == 0) {
					type.setPrizeName(form.getName1());
					type.setNum(form.getNum1());
					type.setPrize("状元");
					type.setAwardLevel(1);
				}
				if (i == 1) {
					type.setPrizeName(form.getName2());
					type.setNum(form.getNum2());
					type.setPrize("榜眼");
					type.setAwardLevel(2);
				}
				if (i == 2) {
					type.setPrizeName(form.getName3());
					type.setNum(form.getNum3());
					type.setPrize("探花");
					type.setAwardLevel(3);
				}
				if (i == 3) {
					type.setPrizeName(form.getName4());
					type.setNum(form.getNum4());
					type.setPrize("举人");
					type.setAwardLevel(4);
				}
				awardList.add(type);
			}
			cfg.setAwardList(awardList);
			activity.setCfg(cfg);
			repo.save(activity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public long maxCode() {
		Long maxCode = repo.find("$maxCode").get(Long.class);
		return maxCode == null ? 1 : maxCode + 1;
	}

	@Transactional
	public void deleteByCode(Long code) {
		repo.execute("$deleteByCode", Params.param("code", code));
	}

	@Override
	public ImperialExaminationActivity queryByCode(Long code) {
		return repo.get(code);
	}

	@Transactional
	@Override
	public void save2(ZycExamConfigForm form) {
		ImperialExaminationActivity activity = new ImperialExaminationActivity();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			if (form.getCode() == null) {
				activity.setCode(this.maxCode());
			} else {
				this.deleteByCode(form.getCode());
				activity.setCode(form.getCode());
			}

			activity.setStartTime(sdf.parse(form.getStartTime()));
			activity.setEndTime(sdf.parse(form.getEndTime()));
			activity.setStatus(Status.ENABLED);
			activity.setCreateAt(new Date());
			// 这个是固定的
			ImperialExaminationActivityCfg cfg = new ImperialExaminationActivityCfg();
			List<ImperialExaminationGrade> grades = new ArrayList<ImperialExaminationGrade>();
			for (ImperialExaminationGrade grade : ImperialExaminationGrade.values()) {
				grades.add(grade);
			}
			cfg.setGrades(grades);

			// 阶段流程配置
			List<ImperialExaminationProcessTime> timeList = new ArrayList<ImperialExaminationProcessTime>();
			for (ImperialExaminationProcess process : ImperialExaminationProcess.values()) {
				ImperialExaminationProcessTime time = new ImperialExaminationProcessTime();
				time.setProcess(process);
				if (process == ImperialExaminationProcess.PROCESS_SIGNUP) {
					time.setStartTime(form.getSignupStart() == null ? null : sdf.parse(form.getSignupStart()));
					time.setEndTime(form.getSignupEnd() == null ? null : sdf.parse(form.getSignupEnd()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL1) {
					time.setStartTime(
							form.getProvincial1Start() == null ? null : sdf.parse(form.getProvincial1Start()));
					time.setEndTime(form.getProvincial1End() == null ? null : sdf.parse(form.getProvincial1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL2) {
					time.setStartTime(
							form.getProvincial2Start() == null ? null : sdf.parse(form.getProvincial2Start()));
					time.setEndTime(form.getProvincial2End() == null ? null : sdf.parse(form.getProvincial2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL3) {
					time.setStartTime(
							form.getProvincial3Start() == null ? null : sdf.parse(form.getProvincial3Start()));
					time.setEndTime(form.getProvincial3End() == null ? null : sdf.parse(form.getProvincial3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL4) {
					time.setStartTime(
							form.getProvincial4Start() == null ? null : sdf.parse(form.getProvincial4Start()));
					time.setEndTime(form.getProvincial4End() == null ? null : sdf.parse(form.getProvincial4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_PROVINCIAL5) {
					time.setStartTime(
							form.getProvincial5Start() == null ? null : sdf.parse(form.getProvincial5Start()));
					time.setEndTime(form.getProvincial5End() == null ? null : sdf.parse(form.getProvincial5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN1) {
					time.setStartTime(
							form.getMetropolitan1Start() == null ? null : sdf.parse(form.getMetropolitan1Start()));
					time.setEndTime(form.getMetropolitan1End() == null ? null : sdf.parse(form.getMetropolitan1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN2) {
					time.setStartTime(
							form.getMetropolitan2Start() == null ? null : sdf.parse(form.getMetropolitan2Start()));
					time.setEndTime(form.getMetropolitan2End() == null ? null : sdf.parse(form.getMetropolitan2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN3) {
					time.setStartTime(
							form.getMetropolitan3Start() == null ? null : sdf.parse(form.getMetropolitan3Start()));
					time.setEndTime(form.getMetropolitan3End() == null ? null : sdf.parse(form.getMetropolitan3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN4) {
					time.setStartTime(
							form.getMetropolitan4Start() == null ? null : sdf.parse(form.getMetropolitan4Start()));
					time.setEndTime(form.getMetropolitan4End() == null ? null : sdf.parse(form.getMetropolitan4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_METROPOLITAN5) {
					time.setStartTime(
							form.getMetropolitan5Start() == null ? null : sdf.parse(form.getMetropolitan5Start()));
					time.setEndTime(form.getMetropolitan5End() == null ? null : sdf.parse(form.getMetropolitan5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL1) {
					time.setStartTime(form.getFinal1Start() == null ? null : sdf.parse(form.getFinal1Start()));
					time.setEndTime(form.getFinal1End() == null ? null : sdf.parse(form.getFinal1End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL2) {
					time.setStartTime(form.getFinal2Start() == null ? null : sdf.parse(form.getFinal2Start()));
					time.setEndTime(form.getFinal2End() == null ? null : sdf.parse(form.getFinal2End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL3) {
					time.setStartTime(form.getFinal3Start() == null ? null : sdf.parse(form.getFinal3Start()));
					time.setEndTime(form.getFinal3End() == null ? null : sdf.parse(form.getFinal3End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL4) {
					time.setStartTime(form.getFinal4Start() == null ? null : sdf.parse(form.getFinal4Start()));
					time.setEndTime(form.getFinal4End() == null ? null : sdf.parse(form.getFinal4End()));
				} else if (process == ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5) {
					time.setStartTime(form.getFinal5Start() == null ? null : sdf.parse(form.getFinal5Start()));
					time.setEndTime(form.getFinal5End() == null ? null : sdf.parse(form.getFinal5End()));
				} else if (process == ImperialExaminationProcess.PROCESS_TOTALRANKING) {
					time.setStartTime(form.getTotalRankStart() == null ? null : sdf.parse(form.getTotalRankStart()));
					time.setEndTime(form.getTotalRankEnd() == null ? null : sdf.parse(form.getTotalRankEnd()));
				} else if (process == ImperialExaminationProcess.PROCESS_AWARDS) {
					time.setStartTime(form.getAwardsStart() == null ? null : sdf.parse(form.getAwardsStart()));
					time.setEndTime(form.getAwardsEnd() == null ? null : sdf.parse(form.getAwardsEnd()));
				}
				timeList.add(time);
			}
			cfg.setTimeList(timeList);

			List<ImperialExaminationMessageTime> messageTimes = new ArrayList<ImperialExaminationMessageTime>();

			// 短信
			ImperialExaminationMessageTime mt = new ImperialExaminationMessageTime();

			// 推送
			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush1() == null ? null : sdf.parse(form.getPush1()));
			mt.setMessageTemplateCode(12000025);
			mt.setUserScope(6); // 所有初中老师
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush2() == null ? null : sdf.parse(form.getPush2()));
			mt.setMessageTemplateCode(12000026); // 所有初中老师
			mt.setUserScope(6);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush3() == null ? null : sdf.parse(form.getPush3()));
			mt.setMessageTemplateCode(12000027); // 所有初中老师
			mt.setUserScope(6);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush4() == null ? null : sdf.parse(form.getPush4()));
			mt.setMessageTemplateCode(12000028); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush5() == null ? null : sdf.parse(form.getPush5()));
			mt.setMessageTemplateCode(12000029); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush6() == null ? null : sdf.parse(form.getPush6()));
			mt.setMessageTemplateCode(12000030); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush7() == null ? null : sdf.parse(form.getPush7()));
			mt.setMessageTemplateCode(12000031); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush8() == null ? null : sdf.parse(form.getPush8()));
			mt.setMessageTemplateCode(12000032); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush9() == null ? null : sdf.parse(form.getPush9()));
			mt.setMessageTemplateCode(12000033); // 已报名的初中教师
			mt.setUserScope(4);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush10() == null ? null : sdf.parse(form.getPush10()));
			mt.setMessageTemplateCode(12000034); // 所有初中教师
			mt.setUserScope(6);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush11() == null ? null : sdf.parse(form.getPush11()));
			mt.setMessageTemplateCode(12000038); // 所有初中学生
			mt.setUserScope(7);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush12() == null ? null : sdf.parse(form.getPush12()));
			mt.setMessageTemplateCode(12000039); // 所有初中学生
			mt.setUserScope(7);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush13() == null ? null : sdf.parse(form.getPush13()));
			mt.setMessageTemplateCode(12000040); // 所有初中学生
			mt.setUserScope(7);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush14() == null ? null : sdf.parse(form.getPush14()));
			mt.setMessageTemplateCode(12000035); // 未提交作业的初中学生
			mt.setUserScope(8);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush15() == null ? null : sdf.parse(form.getPush15()));
			mt.setMessageTemplateCode(12000036); // 未提交作业的初中学生
			mt.setUserScope(8);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush16() == null ? null : sdf.parse(form.getPush16()));
			mt.setMessageTemplateCode(12000037); // 未提交作业的初中学生
			mt.setUserScope(8);
			messageTimes.add(mt);

			mt = new ImperialExaminationMessageTime();
			mt.setMessageType(MessageType.PUSH);
			mt.setStartTime(form.getPush17() == null ? null : sdf.parse(form.getPush17()));
			mt.setMessageTemplateCode(12000041); // 所有初中学生
			mt.setUserScope(7);
			messageTimes.add(mt);

			cfg.setMessageList(messageTimes);

			// textbookCategoryCodes 固定
			List<ImperialExaminationRoom> rooms = Lists.newArrayList();
			ImperialExaminationRoom room1 = new ImperialExaminationRoom();
			room1.setRoom(1);
			List<Integer> textbookCategoryCodes1 = Lists.newArrayList();
			textbookCategoryCodes1.add(15);
			room1.setTextbookCategoryCodes(textbookCategoryCodes1);
			rooms.add(room1);

			ImperialExaminationRoom room2 = new ImperialExaminationRoom();
			room2.setRoom(2);
			List<Integer> textbookCategoryCodes2 = Lists.newArrayList();
			textbookCategoryCodes2.add(31);
			textbookCategoryCodes2.add(30);
			textbookCategoryCodes2.add(27);
			textbookCategoryCodes2.add(23);
			room2.setTextbookCategoryCodes(textbookCategoryCodes2);
			rooms.add(room2);

			cfg.setRooms(rooms);

			// textbookCategoryCodes 固定
			List<Integer> textbookCategoryCodes = Lists.newArrayList();
			textbookCategoryCodes.add(15);
			textbookCategoryCodes.add(31);
			textbookCategoryCodes.add(30);
			textbookCategoryCodes.add(27);
			textbookCategoryCodes.add(23);
			cfg.setTextbookCategoryCodes(textbookCategoryCodes);

			activity.setCfg(cfg);

			// 期别:固定2
			activity.setPeriod(2);
			// 活动名称
			activity.setName("2017年“科举大典”状元杯");

			repo.save(activity);

			// 添加抽奖奖品
			List<ImperialExaminationActivityPrizes> tempPrizes = prizesRepo
					.find("$findPrizesByCode", Params.param("code", activity.getCode())).list();
			if (CollectionUtils.isEmpty(tempPrizes)) {
				List<ImperialExaminationActivityPrizes> prizes = Lists.newArrayList();
				ImperialExaminationActivityPrizes prizes1 = new ImperialExaminationActivityPrizes();
				prizes1.setActivityCode(activity.getCode());
				prizes1.setProcess(ImperialExaminationProcess.PROCESS_SIGNUP);
				prizes1.setName("5元红包");
				prizes1.setNum(50);
				prizes1.setCost(0);
				prizes1.setRoom(1);
				prizes1.setUserType(UserType.TEACHER);
				prizes1.setAwardsRate(new BigDecimal(50));
				prizes.add(prizes1);

				ImperialExaminationActivityPrizes prizes2 = new ImperialExaminationActivityPrizes();
				prizes2.setActivityCode(activity.getCode());
				prizes2.setProcess(ImperialExaminationProcess.PROCESS_SIGNUP);
				prizes2.setName("5元红包");
				prizes2.setNum(50);
				prizes2.setCost(0);
				prizes2.setRoom(2);
				prizes2.setUserType(UserType.TEACHER);
				prizes2.setAwardsRate(new BigDecimal(50));
				prizes.add(prizes2);

				ImperialExaminationActivityPrizes prizes3 = new ImperialExaminationActivityPrizes();
				prizes3.setActivityCode(activity.getCode());
				prizes3.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes3.setName("5元红包");
				prizes3.setNum(25);
				prizes3.setCost(0);
				prizes3.setRoom(1);
				prizes3.setUserType(UserType.TEACHER);
				prizes3.setAwardsRate(new BigDecimal(25));
				prizes.add(prizes3);

				ImperialExaminationActivityPrizes prizes4 = new ImperialExaminationActivityPrizes();
				prizes4.setActivityCode(activity.getCode());
				prizes4.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes4.setName("5元话费");
				prizes4.setNum(25);
				prizes4.setCost(0);
				prizes4.setRoom(1);
				prizes4.setUserType(UserType.TEACHER);
				prizes4.setAwardsRate(new BigDecimal(25));
				prizes.add(prizes4);

				ImperialExaminationActivityPrizes prizes5 = new ImperialExaminationActivityPrizes();
				prizes5.setActivityCode(activity.getCode());
				prizes5.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes5.setName("5元红包");
				prizes5.setNum(25);
				prizes5.setCost(0);
				prizes5.setRoom(2);
				prizes5.setUserType(UserType.TEACHER);
				prizes5.setAwardsRate(new BigDecimal(25));
				prizes.add(prizes5);

				ImperialExaminationActivityPrizes prizes6 = new ImperialExaminationActivityPrizes();
				prizes6.setActivityCode(activity.getCode());
				prizes6.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes6.setName("5元话费");
				prizes6.setNum(25);
				prizes6.setCost(0);
				prizes6.setRoom(2);
				prizes6.setUserType(UserType.TEACHER);
				prizes6.setAwardsRate(new BigDecimal(25));
				prizes.add(prizes6);

				ImperialExaminationActivityPrizes prizes7 = new ImperialExaminationActivityPrizes();
				prizes7.setActivityCode(activity.getCode());
				prizes7.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes7.setName("10元红包");
				prizes7.setNum(12);
				prizes7.setCost(0);
				prizes7.setRoom(1);
				prizes7.setUserType(UserType.TEACHER);
				prizes7.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes7);

				ImperialExaminationActivityPrizes prizes8 = new ImperialExaminationActivityPrizes();
				prizes8.setActivityCode(activity.getCode());
				prizes8.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes8.setName("10元话费");
				prizes8.setNum(12);
				prizes8.setCost(0);
				prizes8.setRoom(1);
				prizes8.setUserType(UserType.TEACHER);
				prizes8.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes8);

				ImperialExaminationActivityPrizes prizes9 = new ImperialExaminationActivityPrizes();
				prizes9.setActivityCode(activity.getCode());
				prizes9.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes9.setName("10元红包");
				prizes9.setNum(12);
				prizes9.setCost(0);
				prizes9.setRoom(2);
				prizes9.setUserType(UserType.TEACHER);
				prizes9.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes9);

				ImperialExaminationActivityPrizes prizes10 = new ImperialExaminationActivityPrizes();
				prizes10.setActivityCode(activity.getCode());
				prizes10.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes10.setName("10元话费");
				prizes10.setNum(12);
				prizes10.setCost(0);
				prizes10.setRoom(2);
				prizes10.setUserType(UserType.TEACHER);
				prizes10.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes10);

				ImperialExaminationActivityPrizes prizes11 = new ImperialExaminationActivityPrizes();
				prizes11.setActivityCode(activity.getCode());
				prizes11.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes11.setName("20元红包");
				prizes11.setNum(12);
				prizes11.setCost(0);
				prizes11.setRoom(1);
				prizes11.setUserType(UserType.TEACHER);
				prizes11.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes11);

				ImperialExaminationActivityPrizes prizes12 = new ImperialExaminationActivityPrizes();
				prizes12.setActivityCode(activity.getCode());
				prizes12.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes12.setName("20元话费");
				prizes12.setNum(12);
				prizes12.setCost(0);
				prizes12.setRoom(1);
				prizes12.setUserType(UserType.TEACHER);
				prizes12.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes12);

				ImperialExaminationActivityPrizes prizes13 = new ImperialExaminationActivityPrizes();
				prizes13.setActivityCode(activity.getCode());
				prizes13.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes13.setName("20元红包");
				prizes13.setNum(12);
				prizes13.setCost(0);
				prizes13.setRoom(2);
				prizes13.setUserType(UserType.TEACHER);
				prizes13.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes13);

				ImperialExaminationActivityPrizes prizes14 = new ImperialExaminationActivityPrizes();
				prizes14.setActivityCode(activity.getCode());
				prizes14.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes14.setName("20元话费");
				prizes14.setNum(12);
				prizes14.setCost(0);
				prizes14.setRoom(2);
				prizes14.setUserType(UserType.TEACHER);
				prizes14.setAwardsRate(new BigDecimal(12));
				prizes.add(prizes14);

				ImperialExaminationActivityPrizes prizes15 = new ImperialExaminationActivityPrizes();
				prizes15.setActivityCode(activity.getCode());
				prizes15.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes15.setName("试用会员7天");
				prizes15.setNum(100);
				prizes15.setCost(0);
				prizes15.setUserType(UserType.STUDENT);
				prizes15.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes15);

				ImperialExaminationActivityPrizes prizes16 = new ImperialExaminationActivityPrizes();
				prizes16.setActivityCode(activity.getCode());
				prizes16.setProcess(ImperialExaminationProcess.PROCESS_PROVINCIAL5);
				prizes16.setName("1Q币");
				prizes16.setNum(100);
				prizes16.setCost(0);
				prizes16.setUserType(UserType.STUDENT);
				prizes16.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes16);

				ImperialExaminationActivityPrizes prizes17 = new ImperialExaminationActivityPrizes();
				prizes17.setActivityCode(activity.getCode());
				prizes17.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes17.setName("试用会员15天");
				prizes17.setNum(100);
				prizes17.setCost(0);
				prizes17.setUserType(UserType.STUDENT);
				prizes17.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes17);

				ImperialExaminationActivityPrizes prizes18 = new ImperialExaminationActivityPrizes();
				prizes18.setActivityCode(activity.getCode());
				prizes18.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes18.setName("1Q币");
				prizes18.setNum(100);
				prizes18.setCost(0);
				prizes18.setUserType(UserType.STUDENT);
				prizes18.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes18);

				ImperialExaminationActivityPrizes prizes19 = new ImperialExaminationActivityPrizes();
				prizes19.setActivityCode(activity.getCode());
				prizes19.setProcess(ImperialExaminationProcess.PROCESS_METROPOLITAN5);
				prizes19.setName("5Q币");
				prizes19.setNum(20);
				prizes19.setCost(0);
				prizes19.setUserType(UserType.STUDENT);
				prizes19.setAwardsRate(new BigDecimal(0.17));
				prizes.add(prizes19);

				ImperialExaminationActivityPrizes prizes20 = new ImperialExaminationActivityPrizes();
				prizes20.setActivityCode(activity.getCode());
				prizes20.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes20.setName("试用会员30天");
				prizes20.setNum(100);
				prizes20.setCost(0);
				prizes20.setUserType(UserType.STUDENT);
				prizes20.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes20);

				ImperialExaminationActivityPrizes prizes21 = new ImperialExaminationActivityPrizes();
				prizes21.setActivityCode(activity.getCode());
				prizes21.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes21.setName("1Q币");
				prizes21.setNum(100);
				prizes21.setCost(0);
				prizes21.setUserType(UserType.STUDENT);
				prizes21.setAwardsRate(new BigDecimal(0.85));
				prizes.add(prizes21);

				ImperialExaminationActivityPrizes prizes22 = new ImperialExaminationActivityPrizes();
				prizes22.setActivityCode(activity.getCode());
				prizes22.setProcess(ImperialExaminationProcess.PROCESS_FINAL_IMPERIAL5);
				prizes22.setName("5Q币");
				prizes22.setNum(20);
				prizes22.setCost(0);
				prizes22.setUserType(UserType.STUDENT);
				prizes22.setAwardsRate(new BigDecimal(0.17));
				prizes.add(prizes22);
				prizesRepo.save(prizes);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
