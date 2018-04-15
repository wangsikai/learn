/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import httl.util.StringUtils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityRank;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityRankService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class ZycImperialExaminationActivityRankServiceImpl implements ZycImperialExaminationActivityRankService {

	@Autowired
	@Qualifier("ImperialExaminationActivityRankRepo")
	private Repo<ImperialExaminationActivityRank, Long> repo;

	@Override
	public Page<Map> queryActivityRank(ZycActivityUserForm form, Pageable p) {
		Params params = Params.param("activityCode", form.getActivityCode()).put("type", form.getType().getValue());
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getRealName())) {
			params.put("realName", '%' + form.getRealName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			params.put("channelName", '%' + form.getChannelName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (null != form.getTag()) {
			params.put("tag", form.getTag().getValue());
		}
		if (null != form.getGrade()) {
			params.put("grade", form.getGrade().getValue());
		}
		if (StringUtils.isNotBlank(form.getClazzCode())) {
			params.put("clazzCode", form.getClazzCode());
		}
		if (null != form.getRoom() && 0 != form.getRoom()) {
			params.put("room", form.getRoom());
		}
		
		//考场为1或2，说明需要查询老师的排名
		if(null != form.getRoom() && (form.getRoom() == 1 || form.getRoom() == 2)){
			return repo.find("$queryActivityTeacherRank", params).fetch(p, Map.class);
		} else { 
			//否则要查询学生的排名
			return repo.find("$queryActivityStudentRank", params).fetch(p, Map.class);
		}
	}

	@Transactional
	@Override
	public int updateScore(long rankId, int manualScore) {
		return repo.execute("$updateActivityRank", Params.param("rankId", rankId).put("score", manualScore));
	}

	@Override
	public ImperialExaminationActivityRank get(long rankId) {
		return repo.get(rankId);
	}
}
