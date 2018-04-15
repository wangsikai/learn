package com.lanking.uxb.service.holidayActivity01.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.component.searcher.api.Document;
import com.lanking.cloud.component.searcher.api.SearchService;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.IndexType;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Answer;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Cfg;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Medal;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02PKRecord;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02Question;
import com.lanking.cloud.domain.yoo.activity.holiday002.HolidayActivity02User;
import com.lanking.cloud.domain.yoo.member.UserMember;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserInfo;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.bean.IndexTypeable;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.activity.api.HolidayActivity02AnswerService;
import com.lanking.uxb.service.activity.api.HolidayActivity02MedalService;
import com.lanking.uxb.service.activity.api.HolidayActivity02PkRecordService;
import com.lanking.uxb.service.activity.api.HolidayActivity02PowerRankService;
import com.lanking.uxb.service.activity.api.HolidayActivity02QuestionService;
import com.lanking.uxb.service.activity.api.HolidayActivity02Service;
import com.lanking.uxb.service.activity.api.HolidayActivity02UserService;
import com.lanking.uxb.service.activity.api.HolidayActivity02WeekPowerRankService;
import com.lanking.uxb.service.activity.value.HolidayActivity02PkRetUserInfo;
import com.lanking.uxb.service.activity.value.HolidayActivity02PkUserInfo;
import com.lanking.uxb.service.activity.value.HolidayActivity02RetRankInfo;
import com.lanking.uxb.service.activity.value.HolidayActivity02UserInfo;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.search.api.Page;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.UserMemberService;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VDailyPractiseQuestion;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

import httl.util.CollectionUtils;
import httl.util.StringUtils;

/**
 * 寒假学生端活动02接口.
 * 
 * @author qiuxue.jiang
 * @version 2018年01月16日
 */
@RestController
@RequestMapping("zy/m/s/holidayActivity02")
public class ZyMHolidayActivity02Controller {
	@Autowired
	private HolidayActivity02Service holidayActivity02Service;
	
	@Autowired
	private HolidayActivity02UserService holidayActivity02UserService;
	
	@Autowired
	private HolidayActivity02AnswerService holidayActivity02AnswerService;
	
	@Autowired
	private HolidayActivity02QuestionService holidayActivity02QuestionService;
	
	@Autowired
	private HolidayActivity02PkRecordService holidayActivity02PkRecordService;
	
	@Autowired
	private HolidayActivity02MedalService holidayActivity02MedalService;
	
	@Autowired
	private HolidayActivity02PowerRankService holidayActivity02PowerRankService;
	
	@Autowired
	private HolidayActivity02WeekPowerRankService holidayActivity02WeekPowerRankService;
	
	@Autowired
	@Qualifier("userService")
	private UserService userService;
	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private UserMemberService userMemberService;
	@Autowired
	private SearchService searchService;
	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private ZyDailyPractiseQuestionConvert dailyPractiseQuestionConvert;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	
	@Autowired
	private UserConvert userConvert;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private static AtomicLong beginUserCode = new AtomicLong(100000L);
	
	/*
	 * 保存pk用户信息的list，需要加锁并发访问保护
	 */
	private List<HolidayActivity02PkUserInfo> pkUsers = new ArrayList<>();
	
	/*
	 * 保存pk用户记录的list，需要加锁并发访问保护
	 */
	private List<HolidayActivity02PKRecord> pkUserRecords = new ArrayList<>();
	
	/*
	 * pk的超时时间，产品要求前台是3秒，服务器设为2.7秒，剩下的时间做其他处理
	 */
	private static final Integer PK_DEFAULT_TIMEOUT = 2700;  
	

	/*
	 * 查询对手答题记录的超时时间
	 */
	private static final Integer QUERY_RECORD_DEFAULT_TIMEOUT = 5000;  
	
	/*
	 * 没有匹配到对手的话，每200毫秒轮询一次
	 */
	private static final Integer POLL_TIME = 200; 
	
	/**
	 * 查看该用户是否已参加活动
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "hasJoinActivity", method = { RequestMethod.GET, RequestMethod.POST })
	public Value hasJoinActivity(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		Long userId = Security.getUserId();
		
		Boolean result = holidayActivity02UserService.hasJoinActivity(code, userId);
		
		data.put("join", result);
		
		return new Value(data);

	}
	
	/**
	 * 查询用户的必要信息
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "requireInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value requireInfo(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
				
		HolidayActivity02UserInfo userInfo = new HolidayActivity02UserInfo();
		
		Long userId = Security.getUserId();
		Account account = accountService.getAccountByUserId(userId);
		userInfo.setUserName(account.getName());
		userInfo.setMobile(account.getMobile());
		
		User user = userService.get(userId);
		userInfo.setRealName(user.getName());
		
		
		Student student = (Student)studentService.getUser(userId);
		
		String textbookName = null;
		String categoryName = null;
		String schoolName = null;

		Integer textbookCode = student.getTextbookCode();
		if(textbookCode != null){
			Textbook textbook = textbookService.get(textbookCode);
			textbookName = textbook.getName();
		}
		
		Integer category = student.getTextbookCategoryCode();
		if(category != null){
			TextbookCategory textbookCategory = textbookCategoryService.get(category);
			categoryName = textbookCategory.getName();
		}
		userInfo.setCategory(categoryName);
		userInfo.setGrade(textbookName);
		
		Long schoolId = student.getSchoolId();
		if(schoolId != null){
			Optional<School> school = Optional.ofNullable(schoolService.get(schoolId));
			Optional<String> name = school.map(School::getName);
			schoolName = name.orElse(null);
		}
		userInfo.setSchool(schoolName);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("user", userInfo);
		
		return new Value(data);
	}
	
	/**
	 * 确认用户参加活动
	 * 
	 * @param code
	 * @return
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "confirm", method = { RequestMethod.GET, RequestMethod.POST })
	public Value confirm(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
				
		Long nextUserCode = beginUserCode.getAndIncrement();
		
		Long userId = Security.getUserId();
		
		holidayActivity02UserService.addActivity02User(code, userId, nextUserCode);
		
		return new Value();
	}

	/**
	 * 获取参加活动的用户信息
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "userActivityInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public Value userActivityInfo(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		HolidayActivity02User userActivityInfo = holidayActivity02UserService.getUserActivityInfo(code, userId);
		
		List<HolidayActivity02Medal> medals = holidayActivity02MedalService.getNotReceivedMedals(code, userId);
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("user", userActivityInfo);
		
		UserInfo userInfo = userService.getUser(userId);
		
		if (userInfo.getAvatarId() != null && userInfo.getAvatarId() != 0) {
			data.put("avatarUrl", FileUtil.getUrl(userInfo.getAvatarId()));
		}
		
		data.put("name", userInfo.getName());
		
		UserMember member = userMemberService.findSafeByUserId(userId);
		
		if(CollectionUtils.isNotEmpty(medals)){
			data.put("hasMedal", true);
		} else {
			data.put("hasMedal", false);
		}
		
		data.put("vip", member.getMemberType().getValue());
		
		return new Value(data);
	}
	
	/**
	 * 获取当前活动的开始时间和结束时间，及当前阶段开始时间及结束时间
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "activityTime", method = { RequestMethod.GET, RequestMethod.POST })
	public Value activityTime(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		data.put("startTime", activity.getStartTime());
		data.put("endTime", activity.getEndTime());
		
		HolidayActivity02Cfg cfg = activity.getCfg();
		
		if(cfg != null){
			List<List<Date>> phases = cfg.getPhases();
			List<Date> currentPhase = null;
			Date currentTime = new Date();
			for(List<Date> phase:phases){
				if(currentTime.after(phase.get(0)) && currentTime.before(phase.get(1))){
					currentPhase = phase;
					break;
				}
			}
			
			if(currentPhase != null){
				data.put("phaseStart", currentPhase.get(0));
				data.put("phaseEnd", currentPhase.get(1));
			}
		}
		
		return new Value(data);
	}
	
	/**
	 * pk赛的匹配对手，好吧，这个很hard
	 * 
	 * @param code
	 * @return
	 */
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "findPkUser", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findPkUser(Long code) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		long beginTime = new Date().getTime();
		HolidayActivity02PkUserInfo userInfo = null;
		
		//循环等待匹配，直到超时
		while(System.currentTimeMillis() < (beginTime + PK_DEFAULT_TIMEOUT)){
			userInfo = findPkUser(code, userId, beginTime);
			
			if(userInfo != null){
				break;
			}
			
			//每次没有匹配到的话休眠200毫秒
			try {
				Thread.sleep(POLL_TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//没有找到对手信息，说明超时了，需要把自己从队列中删除,删除过程中也可能被别人匹配
		if(userInfo == null) {
			userInfo = timeOutDelMyself(code, userId);
		}
		
		HolidayActivity02PKRecord myRecord = null;
		
		//找到对手信息，布置题目
		if(userInfo != null) {
			/*
			 * 有两种情况
			 * 1、我被别人匹配，返回的是我自己的信息，pk记录由别人生成
			 * 2、我匹配到了别人，返回了别人的信息，pk记录由我自己生成
			 */
			if(userInfo.getUserId() != userId){
				//这种情况需要生成两条pk记录
				Date pkTime = new Date();
				myRecord = new HolidayActivity02PKRecord();
				myRecord.setActivityCode(code);
				myRecord.setUserId(userId);
				myRecord.setPkUserId(userInfo.getUserId());
				myRecord.setPkAt(pkTime);
				myRecord.setRealMan(1);
				
				HolidayActivity02PKRecord otherRecord = new HolidayActivity02PKRecord();
				otherRecord.setActivityCode(code);
				otherRecord.setUserId(userInfo.getUserId());
				otherRecord.setPkUserId(userId);
				otherRecord.setPkAt(pkTime);
				otherRecord.setRealMan(1);
				
				HolidayActivity02PKRecord pkUserRecord = holidayActivity02PkRecordService.addPkRecord(false, myRecord, otherRecord);
				
				synchronized(pkUserRecords) {
					Boolean exist = false;
					for(int i=0;i<pkUserRecords.size();i++){
					    if(pkUserRecords.get(i).getPkRecordId().equals(pkUserRecord.getPkRecordId())){
					    	exist = true;
					    	break;
					    }
					}
					
					if(!exist){
						pkUserRecords.add(pkUserRecord);
					}
				}
			} else {
				beginTime = new Date().getTime();
				while(System.currentTimeMillis() < (beginTime + 1000)){
					//pk记录由别人生成，需要到pkUserRecords找自己的记录
					synchronized(pkUserRecords) {
						Iterator<HolidayActivity02PKRecord> it = pkUserRecords.iterator();
						
						while (it.hasNext()) {
							HolidayActivity02PKRecord pkUserRecord = it.next();
							/*
							 * 寻找自己的记录
							 */
							if(pkUserRecord.getActivityCode() == code 
								&& pkUserRecord.getUserId() == userId 
								&& pkUserRecord.getPkUserId() == userInfo.getPkUserId()){
								it.remove();
								myRecord = pkUserRecord;
								
								break;
							}
							
							//否则如果记录的时间比较长了,删除掉
							if(System.currentTimeMillis() > (pkUserRecord.getPkAt().getTime() + 10000)){
								it.remove();
							}
						}
						
					}
					
					if(myRecord != null){
						break;
					}
					
					//每次没有匹配到的话休眠200毫秒
					try {
						Thread.sleep(POLL_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} else {
			//没有找到对手，只能找历史记录
			HolidayActivity02PKRecord historyRecord = holidayActivity02PkRecordService.getARandomPkRecord(code, userId);
			
			//只需要生成自己记录
			Date pkTime = new Date();
			myRecord = new HolidayActivity02PKRecord();
			myRecord.setActivityCode(code);
			myRecord.setUserId(userId);
			myRecord.setPkUserId(historyRecord.getUserId());
			myRecord.setPkAt(pkTime);
			myRecord.setPkRecordId(historyRecord.getId());
			myRecord.setRealMan(0);
			
			holidayActivity02PkRecordService.addPkRecord(true, myRecord, null);
		}
		
		HolidayActivity02Question holidayActivity02Question = null;
		List<Long> questionIds = null;
		//生成题目
		if(myRecord != null){
			questionIds = generateQuestions(code,userId,myRecord.getId());
			holidayActivity02Question = new HolidayActivity02Question(); 
			holidayActivity02Question.setActivityCode(code);
			holidayActivity02Question.setPkRecordId(myRecord.getId());
			holidayActivity02Question.setQuestionCount(questionIds.size());
			holidayActivity02Question.setQuestionList(questionIds);
			
			holidayActivity02QuestionService.save(holidayActivity02Question);
		}
		
		
		HolidayActivity02PkRetUserInfo myRetInfo = new HolidayActivity02PkRetUserInfo();
		HolidayActivity02PkRetUserInfo otherRetInfo = new HolidayActivity02PkRetUserInfo();
		
		HolidayActivity02User myUser = holidayActivity02UserService.getUserActivityInfo(code, userId);
		HolidayActivity02User pkUser = holidayActivity02UserService.getUserActivityInfo(code, myRecord.getPkUserId());
		
		UserInfo myInfo = userService.getUser(userId);
		
		UserMember member = userMemberService.findSafeByUserId(userId);
		
		if (myInfo.getAvatarId() != null && myInfo.getAvatarId() != 0) {
			myRetInfo.setAvatarUrl(FileUtil.getUrl(myInfo.getAvatarId()));
		}
		if(myUser != null){
			myRetInfo.setTotalPower(myUser.getPower());
		}
		if(member != null) {
			myRetInfo.setVip(member.getMemberType().getValue());
		}
		myRetInfo.setName(myInfo.getName());
		
		UserInfo pkUserInfo = userService.getUser(myRecord.getPkUserId());
		
		UserMember pkUserMember = userMemberService.findSafeByUserId(myRecord.getPkUserId());
		
		if (pkUserInfo.getAvatarId() != null && pkUserInfo.getAvatarId() != 0) {
			otherRetInfo.setAvatarUrl(FileUtil.getUrl(pkUserInfo.getAvatarId()));
		}
		if(pkUser != null){
			otherRetInfo.setTotalPower(pkUser.getPower());
		}
		String pkUserName = pkUserInfo.getName();
		if(StringUtils.isNotEmpty(pkUserName)){
			if(pkUserName.length() == 2){
				pkUserName = pkUserName.substring(0, 1) + "*";
			} else {
				pkUserName = pkUserName.substring(0, 1) + "**";
			}
		}
		otherRetInfo.setName(pkUserName);
		if(pkUserMember != null) {
			otherRetInfo.setVip(pkUserMember.getMemberType().getValue());
		}

		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		retMap.put("me", myRetInfo);
		retMap.put("other", otherRetInfo);
		
		retMap.put("pkId", myRecord.getId());
		
		return new Value(retMap);
	}
	
	
	/**
	 * 查询该用户pk的题目
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "getMyPkQuestions", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getMyPkQuestions(Long code,Long pkId) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
				
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		//构造每日练的数据结构
		List<DailyPractiseQuestion> dailyPractiseQuestions = new ArrayList<>();
		List<VDailyPractiseQuestion> qs = null;
		
		HolidayActivity02Question myQuestion = holidayActivity02QuestionService.getQuestion(code, pkId);
		
		if(myQuestion != null){
			List<Long> questionIds = myQuestion.getQuestionList();
			if(CollectionUtils.isNotEmpty(questionIds)){
				for(Long questionId : questionIds){
					DailyPractiseQuestion dailyPractiseQuestion = new DailyPractiseQuestion();
					dailyPractiseQuestion.setPractiseId(myQuestion.getId());
					dailyPractiseQuestion.setQuestionId(questionId);
					dailyPractiseQuestion.setResult(HomeworkAnswerResult.INIT);
					Map<Long,List<String>> answerMap = new HashMap<>();
					List<String> sList = new ArrayList<>();  
					answerMap.put(questionId, sList);
					dailyPractiseQuestion.setAnswer(answerMap);
					
					dailyPractiseQuestions.add(dailyPractiseQuestion);
				}
			}
			
			qs = dailyPractiseQuestionConvert.to(dailyPractiseQuestions);
		}
		
		retMap.put("items", qs);
		retMap.put("paperId", myQuestion.getId());
		retMap.put("pkId", pkId);
		retMap.put("pkStartTime", new Date());

		return new Value(retMap);
	}
	
	/**
	 * 查询排行榜, type 0 代表周战力提升排行, 1 代表总战力排行
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "getRanks", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getRanks(Long code,Integer type) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
				
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		Long userId = Security.getUserId();
		
		HolidayActivity02RetRankInfo me = new HolidayActivity02RetRankInfo();
		List<HolidayActivity02RetRankInfo> users = null;
		
		if(type == 0) {
			//查询周战力排行
			users = holidayActivity02WeekPowerRankService.getActivity02WeekPowerRanks(code, userId, me);
			
			UserInfo myInfo = userService.getUser(userId);
			if (myInfo.getAvatarId() != null && myInfo.getAvatarId() != 0) {
				me.setAvatarUrl(FileUtil.getUrl(myInfo.getAvatarId()));
			}
			UserMember member = userMemberService.findSafeByUserId(userId);
			if(member != null) {
				me.setVip(member.getMemberType().getValue());
			}
			
			String name = myInfo.getName();
			
			me.setName(name);
			
			if(CollectionUtils.isNotEmpty(users)) {
				for (HolidayActivity02RetRankInfo user : users) {
					VUser vUser = userConvert.get(user.getUserId(), new UserConvertOption(true));
					
					if (vUser.getAvatarId() != null && vUser.getAvatarId() != 0) {
						user.setAvatarUrl(FileUtil.getUrl(vUser.getAvatarId()));
					}
					
					user.setVip(vUser.getMemberType().getValue());
					
					String otherName = vUser.getName();
					
					if(StringUtils.isNotEmpty(otherName)){
						if(otherName.length() == 2){
							otherName = otherName.substring(0, 1) + "*";
						} else {
							otherName = otherName.substring(0, 1) + "**";
						}
					}
					user.setName(otherName);
				}
			}
		} else {
			//查询总战力排行
			
			users = holidayActivity02PowerRankService.getActivity02PowerRanks(code, userId, me);
			
			UserInfo myInfo = userService.getUser(userId);
			if (myInfo.getAvatarId() != null && myInfo.getAvatarId() != 0) {
				me.setAvatarUrl(FileUtil.getUrl(myInfo.getAvatarId()));
			}
			
			UserMember member = userMemberService.findSafeByUserId(userId);
			if(member != null) {
				me.setVip(member.getMemberType().getValue());
			}
			
			String name = myInfo.getName();
			
			me.setName(name);
			
			if(CollectionUtils.isNotEmpty(users)) {
				for (HolidayActivity02RetRankInfo user : users) {
					VUser vUser = userConvert.get(user.getUserId(), new UserConvertOption(true));
					
					if (vUser.getAvatarId() != null && vUser.getAvatarId() != 0) {
						user.setAvatarUrl(FileUtil.getUrl(vUser.getAvatarId()));
					}
					
					user.setVip(vUser.getMemberType().getValue());
					
					String otherName = vUser.getName();
					
					if(StringUtils.isNotEmpty(otherName)){
						if(otherName.length() == 2){
							otherName = otherName.substring(0, 1) + "*";
						} else {
							otherName = otherName.substring(0, 1) + "**";
						}
					}
					user.setName(otherName);
				}
			}
		}
		
		retMap.put("me",me);
		retMap.put("users",users);
		
		return new Value(retMap);
	}
	
	

	/**
	 * 提交练习
	 *
	 * @param form
	 *            {@link ExerciseCommitForm}
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "commit", method = { RequestMethod.GET, RequestMethod.POST })
	public Value commit(Long code, Long pkId, ExerciseCommitForm form) {
		logger.error("commit start " + System.currentTimeMillis() + " userid " + Security.getUserId());
		if (CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		try {
			logger.error("correct start " + System.currentTimeMillis() + " userid " + Security.getUserId());
			List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getqIds(), form.getAnswerList());
			logger.error("correct end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			List<HomeworkAnswerResult> rets = new ArrayList<HomeworkAnswerResult>(form.getCount());
			List<Boolean> dones = new ArrayList<Boolean>(form.getCount());
			List<String> answers = new ArrayList<>();
			VExerciseResult result = new VExerciseResult(2);
			result.setqIds(form.getqIds());
			int rightCount = 0;
			int doneCount = 0;
			for (Map<String, Object> map : results) {
				HomeworkAnswerResult ret = (HomeworkAnswerResult) map.get("result");
				boolean done = (Boolean) map.get("done");
				rets.add(ret);
				if (ret == HomeworkAnswerResult.RIGHT) {
					rightCount++;
				}
				if (done) {
					doneCount++;
				}
				dones.add(done);
				
				//处理出答案list
				Long qId = (Long)map.get("qId");
				JSONObject json = (JSONObject) map.get("answer");
				
				List<String> singleQuesAnswers = (List<String>) json.get(qId+"");
				//单选题只有一个答案，取第一个
				String single = "";
				if(CollectionUtils.isNotEmpty(singleQuesAnswers)){
					single = singleQuesAnswers.get(0);
				}
				answers.add(single);
			}

			DailyPractise dailyPractise = new DailyPractise();

			dailyPractise.setRightRate(
					BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			dailyPractise.setDoCount(doneCount);
			dailyPractise.setRightCount(rightCount);
			dailyPractise.setWrongCount(form.getCount() - rightCount);
			dailyPractise.setHomeworkTime(form.getHomeworkTime());

			//需要处理真题答案的记录
			HolidayActivity02Answer answer = new HolidayActivity02Answer();
			
			answer.setQuestionsId(form.getPaperId());
			answer.setCreateAt(new Date());
			answer.setResultList(rets);
			answer.setAnswerList(answers);
			answer.setRightRate(BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			answer.setHomeworkTime(form.getHomeworkTime());
			logger.error("save answer start " + System.currentTimeMillis() + " userid " + Security.getUserId());
			holidayActivity02AnswerService.save(answer);
			logger.error("save answer end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			
			logger.error("record answer start " + System.currentTimeMillis() + " userid " + Security.getUserId());
			// 记录答案、统计学情、记录错题
			zyStuQaService.asynCreateExamActivity(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
					null, null, null, rets, StudentQuestionAnswerSource.DAILY_PRACTICE, new Date());
			logger.error("record answer end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			
			long beginTime = new Date().getTime();
			HolidayActivity02PKRecord myRecord = holidayActivity02PkRecordService.get(pkId);
			HolidayActivity02PKRecord pkUserRecord = holidayActivity02PkRecordService.get(myRecord.getPkRecordId());
			HolidayActivity02Question myQuestion = holidayActivity02QuestionService.getQuestion(code, myRecord.getId());
			HolidayActivity02Question pkUserQuestion = holidayActivity02QuestionService.getQuestion(code, pkUserRecord.getId());
			HolidayActivity02Answer myAnswer = holidayActivity02AnswerService.getPastExamAnswer(myQuestion.getId());
			HolidayActivity02Answer pkUserAnswer = null;
			logger.error("find pkuser answer start " + System.currentTimeMillis() + " userid " + Security.getUserId() + " beginTime " + beginTime);
			//pk对手的问题都没生成，那就没必要查询正确率了
			if(pkUserQuestion != null){
				//循环等待查询pk对手的正确率，直到超时
				while(System.currentTimeMillis() < (beginTime + QUERY_RECORD_DEFAULT_TIMEOUT)){
					pkUserAnswer = holidayActivity02AnswerService.getPastExamAnswer(pkUserQuestion.getId());
					if(pkUserAnswer != null){
						break;
					}
					
					//每次没有找到对手的正确率的话休眠200毫秒
					try {
						Thread.sleep(POLL_TIME);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			logger.error("find pkuser answer end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			Long userId = Security.getUserId();
			
			HolidayActivity02PkRetUserInfo myRetInfo = new HolidayActivity02PkRetUserInfo();
			HolidayActivity02PkRetUserInfo otherRetInfo = new HolidayActivity02PkRetUserInfo();
			
			BigDecimal myRightRate = (myAnswer != null ? myAnswer.getRightRate() : new BigDecimal(0));
			
			myRetInfo.setRightRate(myRightRate.setScale(0, BigDecimal.ROUND_HALF_UP) + "%");
			
			Integer power = 0;
			
			//拼装pk结果，如果对手的答案记录不为空的话，按照答案来
			if(pkUserAnswer != null) {	
				BigDecimal pkUserRightRate = pkUserAnswer.getRightRate();
				
				Integer pkResult = myRightRate.compareTo(pkUserRightRate);
				
				otherRetInfo.setRightRate(pkUserRightRate.setScale(0, BigDecimal.ROUND_HALF_UP) + "%");
				//平局
				if(pkResult == 0){
					power = 10;
					myRetInfo.setResult(1);
					myRetInfo.setPower(power);
					otherRetInfo.setResult(1);
				} else if (pkResult == 1) {
					//我赢了
					power = 30;
					myRetInfo.setResult(2);
					myRetInfo.setPower(power);
					otherRetInfo.setResult(0);
				} else {
					//我输了
					myRetInfo.setResult(0);
					myRetInfo.setPower(power);
					otherRetInfo.setResult(2);
				}
			} else {
				/*
				 * 说明没有找到对手的答案记录，有两种情况
				 * 1、对手没提交，直接正确率为0
				 * 2、对手超时了很长时间才提交，这个可能性不大,也认为正确率为0
				 */
				Integer pkResult = myRightRate.compareTo(BigDecimal.valueOf(0));
				otherRetInfo.setRightRate("0%");
				
				//平均
				if(pkResult == 0){
					power = 10;
					myRetInfo.setResult(1);
					myRetInfo.setPower(10);
					otherRetInfo.setResult(1);
				} else if (pkResult == 1) {
					//我赢了
					power = 30;
					myRetInfo.setResult(2);
					myRetInfo.setPower(30);
					otherRetInfo.setResult(0);
				} 
			}
			logger.error("stats start " + System.currentTimeMillis() + " userid " + Security.getUserId());
			/*
			 * 更新pkrecord的战力值，增加总战力值，增加周新增战力值，增加战绩统计，还要查看勋章是否已获得
			 */
			//增加我的统计
			holidayActivity02UserService.updateActivity02UserStats(code, userId, power,myRecord);
			logger.error("stats end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			logger.error("commit end " + System.currentTimeMillis() + " userid " + Security.getUserId());
			return new Value();
		} catch (AbstractException e) {
			return new Value(new IllegalArgException());
		}
	}
	
	/**
	 * 查询pk结果
	 * 
	 * @param code
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="M")
	@RequestMapping(value = "getPkResult", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getPkResult(Long code, Long pkId) {
		// code为空
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		// 没有对应的活动
		HolidayActivity02 activity = holidayActivity02Service.get(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
				
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		long beginTime = new Date().getTime();
		HolidayActivity02PKRecord myRecord = holidayActivity02PkRecordService.get(pkId);
		HolidayActivity02PKRecord pkUserRecord = holidayActivity02PkRecordService.get(myRecord.getPkRecordId());
		HolidayActivity02Question myQuestion = holidayActivity02QuestionService.getQuestion(code, myRecord.getId());
		HolidayActivity02Question pkUserQuestion = holidayActivity02QuestionService.getQuestion(code, pkUserRecord.getId());
		HolidayActivity02Answer myAnswer = holidayActivity02AnswerService.getPastExamAnswer(myQuestion.getId());
		HolidayActivity02Answer pkUserAnswer = null;
		
		//pk对手的问题都没生成，那就没必要查询正确率了
		if(pkUserQuestion != null){
			//循环等待查询pk对手的正确率，直到超时
			while(System.currentTimeMillis() < (beginTime + QUERY_RECORD_DEFAULT_TIMEOUT)){
				pkUserAnswer = holidayActivity02AnswerService.getPastExamAnswer(pkUserQuestion.getId());
				if(pkUserAnswer != null){
					break;
				}
				
				//每次没有找到对手的正确率的话休眠200毫秒
				try {
					Thread.sleep(POLL_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		Long userId = Security.getUserId();
		
		HolidayActivity02PkRetUserInfo myRetInfo = new HolidayActivity02PkRetUserInfo();
		HolidayActivity02PkRetUserInfo otherRetInfo = new HolidayActivity02PkRetUserInfo();
		
		BigDecimal myRightRate = (myAnswer != null ? myAnswer.getRightRate() : new BigDecimal(0));
		
		myRetInfo.setRightRate(myRightRate.setScale(0, BigDecimal.ROUND_HALF_UP) + "%");
		
		Integer power = 0;
		
		//拼装pk结果，如果对手的答案记录不为空的话，按照答案来
		if(pkUserAnswer != null) {	
			BigDecimal pkUserRightRate = pkUserAnswer.getRightRate();
			
			Integer pkResult = myRightRate.compareTo(pkUserRightRate);
			
			otherRetInfo.setRightRate(pkUserRightRate.setScale(0, BigDecimal.ROUND_HALF_UP) + "%");
			//平局
			if(pkResult == 0){
				power = 10;
				myRetInfo.setResult(1);
				myRetInfo.setPower(power);
				otherRetInfo.setResult(1);
			} else if (pkResult == 1) {
				//我赢了
				power = 30;
				myRetInfo.setResult(2);
				myRetInfo.setPower(power);
				otherRetInfo.setResult(0);
			} else {
				//我输了
				myRetInfo.setResult(0);
				myRetInfo.setPower(power);
				otherRetInfo.setResult(2);
			}
		} else {
			/*
			 * 说明没有找到对手的答案记录，有两种情况
			 * 1、对手没提交，直接正确率为0
			 * 2、对手超时了很长时间才提交，这个可能性不大,也认为正确率为0
			 */
			Integer pkResult = myRightRate.compareTo(BigDecimal.valueOf(0));
			otherRetInfo.setRightRate("0%");
			
			//平局
			if(pkResult == 0){
				power = 10;
				myRetInfo.setResult(1);
				myRetInfo.setPower(10);
				otherRetInfo.setResult(1);
			} else if (pkResult == 1) {
				//我赢了
				power = 30;
				myRetInfo.setResult(2);
				myRetInfo.setPower(30);
				otherRetInfo.setResult(0);
			} 
		}
		
		UserInfo userInfo = userService.getUser(userId);
		
		if (userInfo.getAvatarId() != null && userInfo.getAvatarId() != 0) {
			myRetInfo.setAvatarUrl(FileUtil.getUrl(userInfo.getAvatarId()));
		}
		myRetInfo.setName(userInfo.getName());
		
		UserMember member = userMemberService.findSafeByUserId(userId);
		if(member != null) {
			myRetInfo.setVip(member.getMemberType().getValue());
		}
		
		UserInfo pkUserInfo = userService.getUser(myRecord.getPkUserId());
		
		if (pkUserInfo.getAvatarId() != null && pkUserInfo.getAvatarId() != 0) {
			otherRetInfo.setAvatarUrl(FileUtil.getUrl(pkUserInfo.getAvatarId()));
		}
		
		String pkUserName = pkUserInfo.getName();
		if(StringUtils.isNotEmpty(pkUserName)){
			if(pkUserName.length() == 2){
				pkUserName = pkUserName.substring(0, 1) + "*";
			} else {
				pkUserName = pkUserName.substring(0, 1) + "**";
			}
		}
		otherRetInfo.setName(pkUserName);
		
		UserMember pkUserMember = userMemberService.findSafeByUserId(myRecord.getPkUserId());
		
		if(pkUserMember != null) {
			otherRetInfo.setVip(pkUserMember.getMemberType().getValue());
		}
		
		retMap.put("me", myRetInfo);
		retMap.put("other", otherRetInfo);

		return new Value(retMap);
	}
	

	private List<Long> generateQuestions(Long code, Long userId, Long pkId){
		List<Long> questionIds = new ArrayList<>();
		
		 /*
		  * 对战的题目全部为选择题
		  * 每位选手根据自己的所属教材，随机抽取题目
		  * 教材版本不同，但题目的难度系数需要相同
		  * 难度系数分布如下
		  * 题目序号     题目难度
		  * 1~4               0.8~1（简单）
		  * 5                 0.6~0.79（中等）
		 */
		Student student = (Student)studentService.getUser(userId);

		Integer textbookCode = student.getTextbookCode();
		
		List<IndexTypeable> types = Lists.<IndexTypeable>newArrayList(IndexType.QUESTION); // 搜索习题
		
		BoolQueryBuilder qb = getQueryBuilder(textbookCode);
		
		//查第1-4题
		getQuestionIds(questionIds, qb, types, 0,4,textbookCode);
		
		//查第5题
		getQuestionIds(questionIds,qb, types, 1,1,textbookCode);
		
		return questionIds;
	}

	private BoolQueryBuilder getQueryBuilder(Integer textbookCode) {
		BoolQueryBuilder qb = null;
		qb = QueryBuilders.boolQuery();
		
		//已通过的题目
		Set<Integer> status = new HashSet<Integer>();
		status.add(CheckStatus.PASS.getValue());
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("checkStatus", status));
		
		//教材版本
		Set<Integer> textbookCodes = new HashSet<Integer>();
		textbookCodes.add(textbookCode);
		qb.must(com.lanking.uxb.service.search.api.QueryBuilders.inQuery("textbookCodes2", textbookCodes));
		
		//题型
		qb.must(QueryBuilders.termQuery("type", Question.Type.SINGLE_CHOICE.getValue()));
		return qb;
	}

	private void getQuestionIds(List<Long> questionIds,BoolQueryBuilder qb, List<IndexTypeable> types,int level, int sub,Integer textbookCode) {
		double[] gtes = {0.8, 0.6};
		double[] ltes = {1, 0.79};
		double gte = gtes[level];
		double lte = ltes[level];
		
		//难度选择
		qb.must(QueryBuilders.rangeQuery("difficulty").gte(gte).lte(lte));

		int offset = 0;
		long count = 0;
		
		while (true){
			count = searchService.count(IndexType.QUESTION, qb);
			//查询出的题目总数不够，降低难度
			if(count < sub){
				if(level == 1) {
					level = 0;
				} else {
					//难度降到第一级都不够，只能跳出了
					break;
				}
				gte = gtes[level];
				lte = ltes[level];
				
				qb = getQueryBuilder(textbookCode);
				
				//难度选择
				qb.must(QueryBuilders.rangeQuery("difficulty").gte(gte).lte(lte));
			} else {
				break;
			}
		}
		
		offset = (int) (Math.random() * count);
		
		if(offset > (count - sub)) {
			offset = (int) (count - sub);
		}
		
		if(offset < 0) {
			offset = 0;
		}
		Page docPage = searchService.search(types, offset, sub, qb, null, null);

		// 查询数据库
		for (Document document : docPage.getDocuments()) {
			if (document.getId() != null && !document.getId().equals("null")) {
				questionIds.add(Long.parseLong(document.getId()));
			}
		}
		
	}

	/**
	 * 寻找pk对手,并返回对手或自己的userInfo
	 */
	private HolidayActivity02PkUserInfo findPkUser(Long code,Long userId,long beginTime){
		Boolean inQueue = false;
		HolidayActivity02PkUserInfo userInfo = null;
		
		synchronized(pkUsers) {
			Iterator<HolidayActivity02PkUserInfo> it = pkUsers.iterator();
			
			while (it.hasNext()) {
				HolidayActivity02PkUserInfo pkUserInfo = it.next();
				/*
				 * 匹配对手的几个条件过滤
				 */
				
				//首先，code不一样跳过
				if(!code.equals(pkUserInfo.getActivityCode())){
					continue;
				}
				
				//如果userId一样，说明是自己的记录，查看是否已经被匹配
				if(userId.equals(pkUserInfo.getUserId())){
					//说明我自己已经被匹配，把自己从队列中删除，并返回
					if(pkUserInfo.getMatch()){
						it.remove();
						return pkUserInfo;
					}
					
					inQueue = true;
					continue;
				}
				
				//如果已经超时了也跳过
				if(System.currentTimeMillis() >= pkUserInfo.getPkOverTime()){
					continue;
				}
				
				//如果该用户已经被匹配了也跳过
				if(pkUserInfo.getMatch()){
					continue;
				}
				
				/*
				 * 此时说明已经找到了一个用户可以作为pk对手，把他标记为已被匹配
				 */
				pkUserInfo.setMatch(true);
				pkUserInfo.setPkUserId(userId);
				pkUserInfo.setPositive(false);
				
				userInfo  = pkUserInfo;
			}
			
			//如果找到了pk对手且我在队列里，需要把我从队列里删除
			if(userInfo != null && inQueue) {
				for(int i=0;i<pkUsers.size();i++){
				    if(pkUsers.get(i).getUserId() == userId){
				    	pkUsers.remove(i);
				    	break;
				    }
				}
			} else if (userInfo == null && !inQueue){
				//没有找到对手，且自己没有在队列里，创建一个自己的对象加入队列
				HolidayActivity02PkUserInfo myUserInfo = new HolidayActivity02PkUserInfo();
				myUserInfo.setActivityCode(code);
				myUserInfo.setMatch(false);
				myUserInfo.setUserId(userId);
				myUserInfo.setPkStartTime(beginTime);
				myUserInfo.setPkOverTime(beginTime + PK_DEFAULT_TIMEOUT);
				
				pkUsers.add(myUserInfo);
			}
		}
		
		return userInfo;
	}

	/**
	 * 超时把自己从队列里删除
	 */
	private HolidayActivity02PkUserInfo timeOutDelMyself(Long code,Long userId){
		synchronized(pkUsers) {
			for(int i=0;i<pkUsers.size();i++){
				HolidayActivity02PkUserInfo userInfo = pkUsers.get(i);
				
				if(userInfo.getActivityCode() == code && userInfo.getUserId() == userId){
					if(userInfo.getMatch()){
						return userInfo;
					}
					
					pkUsers.remove(i);
					return null;
				}
			}
		}
		
		return null;

	}
	
}
