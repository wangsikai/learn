package com.lanking.uxb.zycon.operation.api.impl;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.customerService.YoomathCustomerServiceSession;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.zycon.operation.api.CustomerTalkService;
import com.lanking.uxb.zycon.operation.api.TalkQuery;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;

/**
 * 客服对话记录接口
 * 
 * @author wangsenhao
 *
 */
@SuppressWarnings("unchecked")
@Transactional(readOnly = true)
@Service
public class CustomerTalkServiceImpl implements CustomerTalkService {

	@Autowired
	@Qualifier("YoomathCustomerServiceSessionRepo")
	Repo<YoomathCustomerServiceSession, Long> sessionRepo;

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("StudentRepo")
	Repo<Student, Long> studentRepo;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private SchoolService schoolService;

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> querySession(TalkQuery tq) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Params params = Params.param();
		try {
			if (tq.getStartTime() != null) {
				params.put("startTime", sdf.parse(tq.getStartTime()));
			}
			if (tq.getEndTime() != null) {
				params.put("endTime", sdf.parse(tq.getEndTime()));
			}
			if (tq.getUserType() == UserType.TEACHER) {
				params.put("userType", tq.getUserType().getValue());
				params.put("phaseCode", tq.getPhaseCode());
			} else if (tq.getUserType() == UserType.STUDENT) {
				params.put("userType", tq.getUserType().getValue());
			}
		} catch (Exception e) {
			logger.error("YoomathCustomerServiceSession time convert error!", e);
		}
		return sessionRepo.find("$querySession", params).fetch(P.index(tq.getPage(), tq.getPageSize()), Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryLog(TalkQuery tq) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Params params = Params.param();
		params.put("userId", tq.getUserId());
		if (tq.getKeyStr() != null) {
			params.put("keyStr", "%" + tq.getKeyStr() + "%");
		}
		try {
			if (tq.getStartTime() != null) {
				params.put("startTime", sdf.parse(tq.getStartTime()));
			}
			if (tq.getEndTime() != null) {
				params.put("endTime", sdf.parse(tq.getEndTime()));
			}
		} catch (Exception e) {
			logger.error("YoomathCustomerServiceLog time convert error!", e);
		}

		return sessionRepo.find("$queryLog", params).fetch(P.index(tq.getPage(), tq.getPageSize()), Map.class);
	}

	@Override
	public Long getLogCount(TalkQuery tq) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Params params = Params.param();
		params.put("userId", tq.getUserId());
		if (tq.getKeyStr() != null) {
			params.put("keyStr", "%" + tq.getKeyStr() + "%");
		}
		try {
			if (tq.getStartTime() != null) {
				params.put("startTime", sdf.parse(tq.getStartTime()));
			}
			if (tq.getEndTime() != null) {
				params.put("endTime", sdf.parse(tq.getEndTime()));
			}
		} catch (Exception e) {
			logger.error("YoomathCustomerServiceLog time convert error!", e);
		}

		return sessionRepo.find("$getLogCount", params).count();
	}

	@Transactional
	@Override
	public void updateSessionByUserId(Long userId) {
		YoomathCustomerServiceSession ycSession = sessionRepo.find("$zycGetByUser", Params.param("userId", userId))
				.get();
		if (ycSession != null && ycSession.getStatus() != Status.ENABLED) {
			ycSession.setStatus(Status.ENABLED);
			sessionRepo.save(ycSession);
		}
	}

	@Override
	public Map<String, Object> queryUserInfo(Long userId) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = userRepo.get(userId);
		map.put("user_id", userId);
		map.put("name", user.getName());
		map.put("user_type", user.getUserType().getValue());
		Long schoolId = null;
		if (user.getUserType() == UserType.TEACHER) {
			Teacher t = teacherRepo.get(userId);
			map.put("phase_code", t.getPhaseCode());
			map.put("sex", t.getSex());
			schoolId = t.getSchoolId();
		}
		if (user.getUserType() == UserType.STUDENT) {
			Student s = studentRepo.get(userId);
			map.put("phase_code", s.getPhaseCode());
			map.put("sex", s.getSex());
			schoolId = s.getSchoolId();
		}
		Account account = accountService.getAccountByUserId(userId);
		if (null != schoolId && 0L != schoolId) {
			School school = schoolService.get(schoolId);
			if (school != null) {
				map.put("schoolname", school.getName());
			}

		}
		map.put("accountname", account.getName());
		map.put("email", account.getEmail());
		map.put("mobile", account.getMobile());
		return map;
	}
}
