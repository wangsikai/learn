/**
 * 
 */
package com.lanking.uxb.zycon.activity.api.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.imperialExamination.ImperialExaminationActivityAward;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.activity.api.ZycImperialExaminationActivityAwardService;
import com.lanking.uxb.zycon.activity.form.ZycActivityUserForm;

import httl.util.StringUtils;

/**
 * @author <a href="mailto:zemin.song@elanking.com">zemin.song</a>
 */
@Transactional(readOnly = true)
@Service
public class ZycImperialExaminationActivityAwardServiceImpl implements ZycImperialExaminationActivityAwardService {

	@Autowired
	@Qualifier("ImperialExaminationActivityAwardRepo")
	private Repo<ImperialExaminationActivityAward, Long> repo;

	@Override
	public ImperialExaminationActivityAward get(Long id) {
		return repo.get(id);
	}

	@Override
	public Page<Map> queryActivityAward(ZycActivityUserForm form, Pageable p) {
		Params params = Params.param("activityCode", form.getActivityCode());
		if (StringUtils.isNotBlank(form.getAccountName())) {
			params.put("accountName", '%' + form.getAccountName() + '%');
		}
		if (StringUtils.isNotBlank(form.getChannelName())) {
			params.put("channelName", '%' + form.getChannelName() + '%');
		}
		if (StringUtils.isNotBlank(form.getSchoolName())) {
			params.put("schoolName", '%' + form.getSchoolName() + '%');
		}
		if (null != form.getAwardLevel()) {
			if (form.getAwardLevel().intValue() < 0) {
				params.put("levelType", 1);
			} else {
				params.put("levelType", 2);
				params.put("awardLevel", form.getAwardLevel());
			}
		} else {
			params.put("levelType", 0);
		}
		if (null != form.getStatus()) {
			params.put("status", form.getStatus().getValue());
		}
		if (null != form.getRoom() && 0 != form.getRoom()) {
			params.put("room", form.getRoom());
		}
		//考场为1或2，说明需要查询老师的奖项排名
		if(null != form.getRoom() && (form.getRoom() == 1 || form.getRoom() == 2)){
			return repo.find("$queryActivityTeacherAward", params).fetch(p, Map.class);
		} else { 
			//否则要查询学生的奖项排名
			return repo.find("$queryActivityStudentAward", params).fetch(p, Map.class);
		}

	}
	
	public List<ImperialExaminationActivityAward> queryRank(long activityCode,Integer room) {
		Params params = Params.param("code", activityCode);
		if (room != null) {
			params.put("room", room);
		}
		return repo.find("$TaskAwardRank", params).list();
	}
	
	@Transactional
	@Override
	public void save(Collection<ImperialExaminationActivityAward> awards) {
		repo.save(awards);
	}
	
	private void setAwardRank(List<ImperialExaminationActivityAward> awards, Integer room) {
		if (CollectionUtils.isNotEmpty(awards)) {
			Integer index = 1;
			for (ImperialExaminationActivityAward award : awards) {
				award.setRank(index.longValue());
				award.setRoom(room);
				if (index == 1) {
					// 第一名一等奖
					award.setAwardLevel(1);
				} else if ( index == 2 || index == 3){
					// 第二三名二等奖
					award.setAwardLevel(2);
				} else if ( index >= 4 && index <= 6){
					// 第四五六名三等奖
					award.setAwardLevel(3);
				} else {
					// 其它都为0 ？
					award.setAwardLevel(0);
				}
				
				index++;
			}
			
		}
	}

	@Transactional
	@Override
	public void updateStatus(long id, Long code, Status status,Integer room) {
		ImperialExaminationActivityAward ieaa = repo.get(id);
		//如果是冻结或者删除,把奖项清零
		if(status.getValue()==1 || status.getValue() ==2){
			ieaa.setAwardLevel(0);
		}
		ieaa.setStatus(status);
		repo.save(ieaa);
		//冻结或者解冻用户之后需要更新奖项排行榜，具体做法是重新生成新的
		Params params = Params.param("code", code);
		if (room != null) {
			params.put("room", room);
		}

		//再查询排名
		List<ImperialExaminationActivityAward> awards = queryRank(code,room);
		//设置名次
		setAwardRank(awards,room);
		//保存奖项
		save(awards);
	}

	@Override
	public List<Map> getActivityClazzScore(long code, Long userId) {
		Params params = Params.param("code", code);
		if (userId != null) {
			params.put("userId", userId);
		}
		return repo.find("$getActivityClazzScore", params).list(Map.class);
	}

	@Transactional
	@Override
	public void update(long id, int score, int doTime, long clazzId) {
		ImperialExaminationActivityAward ieaa = repo.get(id);
		ieaa.setDoTime(doTime);
		ieaa.setClazzId(clazzId);
		ieaa.setScore(score);
		repo.save(ieaa);
	}

	@Override
	public ImperialExaminationActivityAward get(long code, Long userId) {
		Params params = Params.param("code", code);
		if (userId != null) {
			params.put("userId", userId);
		}
		return repo.find("$queryActivityAwardByUser", params).get();
	}

}
