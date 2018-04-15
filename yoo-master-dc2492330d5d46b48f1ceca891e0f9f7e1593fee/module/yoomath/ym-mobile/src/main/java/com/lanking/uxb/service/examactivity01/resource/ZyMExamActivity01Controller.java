package com.lanking.uxb.service.examactivity01.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.type.StudentQuestionAnswerSource;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Answer;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001Question;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001User;
import com.lanking.cloud.domain.yoo.activity.exam001.ExamActivity001UserQ;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractiseQuestion;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.base.form.ExerciseCommitForm;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01ExamService;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01GiftService;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01Service;
import com.lanking.uxb.service.examactivity001.api.ExamActivity01UserService;
import com.lanking.uxb.service.question.api.QuestionService;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.zuoye.api.ZyCorrectingService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseQuestionConvert;
import com.lanking.uxb.service.zuoye.value.VDailyPractiseQuestion;
import com.lanking.uxb.service.zuoye.value.VExamActivity001Question;
import com.lanking.uxb.service.zuoye.value.VExerciseResult;

import httl.util.CollectionUtils;

/**
 * 期末活动01接口.
 * 
 * @author qiuxue.jiang
 * @version 2017年12月27日
 */
@RestController
@RequestMapping("zy/m/s/examActivity01")
public class ZyMExamActivity01Controller {
	
	@Autowired
	private ExamActivity01ExamService examActivity01ExamService;
	@Autowired
	private ExamActivity01GiftService examActivity01GiftService;
	@Autowired
	private ExamActivity01UserService examActivity01UserService;
	@Autowired
	private ExamActivity01Service examActivity01Service;
	@Autowired
	private ZyCorrectingService zyCorrectingService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private ZyDailyPractiseQuestionConvert dailyPractiseQuestionConvert;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TextbookService textbookService;
	@Autowired
	private StudentQuestionAnswerService zyStuQaService;
	
	
	/**
	 * 获取当前学生的礼包总数
	 * 
	 * @param code  活动代码
	 * @param userId  学生id
	 * @return 礼包数量
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getGiftCount", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getGiftCount(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		Map<String, Object> data = new HashMap<String, Object>(1);
		
		Long count = examActivity01GiftService.getGiftCount(code, userId);
		
		data.put("count", count);
		
		return new Value(data);
	}
	
	/**
	 * 获取当前学生的礼包总数
	 * 
	 * @param code  活动代码
	 * @param userId  学生id
	 * @return 礼包数量
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getGifts", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getGifts(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		Map<String, Object> data = new HashMap<String, Object>(1);
		
		List<ExamActivity001UserQ> gifts = examActivity01GiftService.getGifts(code, userId);
		
		ExamActivity001User user = examActivity01UserService.getUser(code, userId);
		
		data.put("gifts", gifts);
		if(user != null){
			if(StringUtils.isNoneEmpty(user.getQq())){
				data.put("needQQ", false);
			} else {
				data.put("needQQ", true);
			}
		} else {
			data.put("needQQ", false);
		}
		
		return new Value(data);
	}
	
	/**
	 * 保存学生的qq号
	 * 
	 * @param code  活动代码
	 * @param userId  学生id
	 * @param qq  学生qq
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "saveQQ", method = { RequestMethod.POST, RequestMethod.GET })
	public Value saveQQ(Long code,String qq) {
		if (code == null || StringUtils.isBlank(qq)) {
			return new Value(new MissingArgumentException());
		}
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		examActivity01GiftService.saveQQ(code, userId, qq);
		
		return new Value();
	}
	
	/**
	 * 确认抽奖
	 * 
	 * @param lotteryId
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "confirmGift", method = { RequestMethod.POST, RequestMethod.GET })
	public Value confirmGift(Long giftId) {
		
		if (giftId == null) {
			return new Value(new MissingArgumentException());
		}
		
		ExamActivity001UserQ userQ = examActivity01GiftService.getGift(giftId);
		
		if(userQ.getViewed() == 1){
			return new Value();
		}
		
		examActivity01GiftService.confirmGift(giftId);
		
		return new Value();
	}
	
	/**
	 * 添加当前学生的礼包
	 * 
	 * @param code  活动代码
	 * @param userId  学生id
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "addGift", method = { RequestMethod.POST, RequestMethod.GET })
	public Value addGift(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		Date currentTime = new Date();
		//如果当前时间不在活动时间，不添加礼包直接返回
		if (currentTime.getTime() < activity.getStartTime().getTime() 
				|| currentTime.getTime() > activity.getEndTime().getTime()){
			return new Value();
		}
		
		Long userId = Security.getUserId();
		
		examActivity01GiftService.addGift(code, userId);
		
		return new Value();
	}
	
	/**
	 * 添加当前学生的资料
	 * 
	 * @param code  活动代码
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "addUserCategory", method = { RequestMethod.POST, RequestMethod.GET })
	public Value addUserCategory(Long code,Integer category,Integer grade) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		ExamActivity001User user = examActivity01UserService.getUser(code, userId);
		
		if(user == null){
            user = new ExamActivity001User();
            user.setActivityCode(code);
    		user.setUserId(userId);
    		user.setQ_num(0);
    		user.setValue0(0);
    		user.setViewQNum(0);
    		user.setReceived(0);
    		user.setTextbookCategoryCode(category);
    		user.setGrade(grade);
    		
    		examActivity01UserService.addUser(user);
		} else {
			user.setGrade(grade);
			user.setTextbookCategoryCode(category);
			
			examActivity01UserService.updateUser(user);
		}
		
		
		return new Value();
	}
	
	/**
	 * 查询当前学生的资料
	 * 
	 * @param code  活动代码
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getUser", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUser(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		
		Long userId = Security.getUserId();
		
		ExamActivity001User user = examActivity01UserService.getUser(code, userId);
		
		//如果目前还没有，添加一个
		if(user == null){
			Student student = (Student)studentService.getUser(userId);
			
			user = new ExamActivity001User();
            user.setActivityCode(code);
    		user.setUserId(userId);
    		user.setQ_num(0);
    		user.setValue0(0);
    		user.setViewQNum(0);
    		user.setReceived(0);
    		Integer grade = 1;
    		Integer category = student.getTextbookCategoryCode();
    		if(category == null){
    			category = 15;
    		} else if(category != 15 && category != 30 && category != 23 && category != 31) {
    			//如果不是15  苏科版 30  人教新版 23  华师大版 31  北师新版，修改为默认的苏科版
    			category = 15;
    		} else {
    			Integer textbookCode = student.getTextbookCode();
        		if(textbookCode != null){
        			Textbook textbook = textbookService.get(textbookCode);
        			String name = textbook.getName();
        			if(name.indexOf("七") != -1){
        				grade = 1;
        			} else if(name.indexOf("八") != -1){
        				grade = 2;
        			} else if(name.indexOf("九") != -1){
        				grade = 3;
        			}
        		}
    		}
    		user.setTextbookCategoryCode(category);
    		
    		user.setGrade(grade);
    		
    		examActivity01UserService.addUser(user);
		}
		
		Map<String, Object> data = new HashMap<String, Object>(1);
		
		data.put("user", user);
		
		return new Value(data);
	}
	
	/**
	 * 获取真题列表
	 * 
	 * @param category  教材版本
	 * @param grade  年级
	 * @param type   题目类型
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getPastExams", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getPastExams(Long code,Integer category, Integer grade, Integer type) {
		//如果教材为空，默认取苏科版，15
		if(category == null){
			category = 15;
		}
		
		//如果年级为空，默认取初一，1
		if(grade == null){
			grade = 1;
		}
		
		List<Integer> typeList = new ArrayList<>();
		
		if(type == null){
			Integer[] types = {1,3,5};
			Collections.addAll(typeList, types);
		} else {
			typeList.add(type);
		}
		
		Map<String, Object> data = new HashMap<String, Object>(1);
		
		//查询出对应的试卷
		for(Integer currentType:typeList){
			List<ExamActivity001Question> exams = examActivity01ExamService.getPastExams(category, grade, currentType);
			
			List<VExamActivity001Question> vExams = new ArrayList<>();
			
			List<Long> examCodes = new ArrayList<>();
			List<ExamActivity001Answer> answers = null;
			Map<Long,ExamActivity001Answer> answerMap = new HashMap<>();
			
			if(CollectionUtils.isNotEmpty(exams)){
				for(ExamActivity001Question exam:exams){
					examCodes.add(exam.getCode());
				}
				
				Long userId = Security.getUserId();
				answers = examActivity01ExamService.getPastExamAnswers(userId,examCodes,code);
				
				if(CollectionUtils.isNotEmpty(answers)){
					for(ExamActivity001Answer answer:answers){
						answerMap.put(answer.getExamQuestioncode(), answer);
					}
				}
			}
			
			for(ExamActivity001Question exam:exams){
				VExamActivity001Question vExam = new VExamActivity001Question();
				vExam.setCode(exam.getCode());
				vExam.setDifficulty(exam.getDifficulty());
				vExam.setGrade(grade);
				vExam.setName(exam.getName());
				vExam.setQuestionCount(exam.getQuestionCount());
				vExam.setTextbookCategoryCode(exam.getTextbookCategoryCode());
				vExam.setType(exam.getType());
				
				if(answerMap.get(exam.getCode()) != null){
					vExam.setDone(true);
				} else {
					vExam.setDone(false);
				}
				
				vExams.add(vExam);
			}
			
			if(currentType == 1){
				data.put("exams1", vExams);
			} else if(currentType == 3){
				data.put("exams3", vExams);
			} else if(currentType == 5){
				data.put("exams5", vExams);
			} 
		}
		
		
		return new Value(data);
		
	}
	
	/**
	 * 获取真题题目
	 * 
	 * @param examId  真题代码
	 * @return 
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type = "MS")
	@RequestMapping(value = "getPastExamQuestions", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getPastExamQuestions(Long code,Long examCode) {
		if (examCode == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		//查询出对应的试卷
		ExamActivity001Question exam = examActivity01ExamService.getPastExam(examCode);
		//获取本真题试卷下的题目id
		List<Long> questionIds = examActivity01ExamService.getPastExamQuestionIds(examCode);
		
		Long userId = Security.getUserId();
		
		//获取该用户本真题试卷对应的答案记录
		ExamActivity001Answer answer = examActivity01ExamService.getPastExamAnswer(userId,examCode,code);
		
		List<String> answerList = null;
		List<HomeworkAnswerResult> resultList = null;
		
		Integer rightRate = 0;
		Date commitTime = null;
		Integer homeworkTime = 0;
		//因为是一次性的，有答案记录说明已经做过了
		boolean done = false;
		if(answer != null){
			done = true;
			answerList = answer.getAnswerList();
			resultList = answer.getResultList();
			rightRate = answer.getRightRate().intValue();
			commitTime = answer.getCreateAt();
			homeworkTime = answer.getHomeworkTime();
		}
		
		retMap.put("done", done);
		
		//构造每日练的数据结构
		List<DailyPractiseQuestion> dailyPractiseQuestions = new ArrayList<>();
		List<VDailyPractiseQuestion> qs = null;
		
		if(CollectionUtils.isNotEmpty(questionIds)){
			int index = 0;
			for(Long questionId : questionIds){
				DailyPractiseQuestion dailyPractiseQuestion = new DailyPractiseQuestion();
				dailyPractiseQuestion.setPractiseId(examCode);
				dailyPractiseQuestion.setQuestionId(questionId);
				if(CollectionUtils.isNotEmpty(resultList)){
					dailyPractiseQuestion.setResult(resultList.get(index));
				} else {
					dailyPractiseQuestion.setResult(HomeworkAnswerResult.INIT);
				}
				Map<Long,List<String>> answerMap = new HashMap<>();
				List<String> sList = new ArrayList<>();  
				if(CollectionUtils.isNotEmpty(answerList)){
					sList.add(answerList.get(index));
				}
				answerMap.put(questionId, sList);
				dailyPractiseQuestion.setAnswer(answerMap);
				
				dailyPractiseQuestions.add(dailyPractiseQuestion);
				index++;
			}
			
			qs = dailyPractiseQuestionConvert.to(dailyPractiseQuestions);
		}
		
		//如果没有做的话给出预测时间
		if (!done) {
			int predictTime = 0;
			for (VDailyPractiseQuestion vq : qs) {
				VQuestion v = vq.getQuestion();
				predictTime += questionService.calPredictTime(v.getType(), v.getDifficulty(), v.getSubject().getCode());
			}

			retMap.put("predictTime", predictTime);
		}
		retMap.put("items", qs);

		retMap.put("difficulty", exam.getDifficulty());
		retMap.put("type", exam.getType());
		retMap.put("commitTime", commitTime);
		retMap.put("rightRate", rightRate);
		retMap.put("rightRateTitle", rightRate + "%");
		retMap.put("homeworkTime", homeworkTime);
		
		return new Value(retMap);
	}
	
	/**
	 * 再次练习真题
	 *
	 * @param examCode
	 *            真题code
	 * @return 是否存在未完成的真题，如果有未完成的情况则返回未完成中的真题题目
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@MasterSlaveDataSource(type="MS")
	@RequestMapping(value = "doExamAgain", method = { RequestMethod.GET, RequestMethod.POST })
	public Value doExamAgain(Long code,Long examCode) {
		if (examCode == null) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		
		//获取本真题试卷下的题目id
		List<Long> questionIds = examActivity01ExamService.getPastExamQuestionIds(examCode);
		
		Long userId = Security.getUserId();
		
		//获取该用户本真题试卷对应的答案记录
		ExamActivity001Answer answer = examActivity01ExamService.getPastExamAnswer(userId,examCode,code);
		
		List<String> answerList = null;
		List<HomeworkAnswerResult> resultList = null;
		
		
		//因为是一次性的，有答案记录说明已经做过了
		boolean done = false;
		if(answer != null){
			done = true;
			answerList = answer.getAnswerList();
			resultList = answer.getResultList();
		}
		
		if (done) {
			retMap.put("hasFinish", true);
		} else {
			
			//构造每日练的数据结构
			List<DailyPractiseQuestion> dailyPractiseQuestions = new ArrayList<>();
			List<VDailyPractiseQuestion> qs = null;
			
			if(CollectionUtils.isNotEmpty(questionIds)){
				int index = 0;
				for(Long questionId : questionIds){
					DailyPractiseQuestion dailyPractiseQuestion = new DailyPractiseQuestion();
					dailyPractiseQuestion.setPractiseId(examCode);
					dailyPractiseQuestion.setQuestionId(questionId);
					if(CollectionUtils.isNotEmpty(resultList)){
						dailyPractiseQuestion.setResult(resultList.get(index));
					} else {
						dailyPractiseQuestion.setResult(HomeworkAnswerResult.INIT);
					}
					Map<Long,List<String>> answerMap = new HashMap<>();
					List<String> sList = new ArrayList<>();  
					if(CollectionUtils.isNotEmpty(answerList)){
						sList.add(answerList.get(index));
					}
					answerMap.put(questionId, sList);
					dailyPractiseQuestion.setAnswer(answerMap);
					
					dailyPractiseQuestions.add(dailyPractiseQuestion);
					index++;
				}
				
				qs = dailyPractiseQuestionConvert.to(dailyPractiseQuestions);
			}
			
			retMap.put("questions", qs);
			retMap.put("hasFinish", false);
		}
		
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
	public Value commit(Long code,ExerciseCommitForm form) {
		if (CollectionUtils.isEmpty(form.getqIds())
				|| CollectionUtils.isEmpty(form.getAnswerList()) || form.getqIds().size() != form.getCount()
				|| form.getAnswerList().size() != form.getCount()) {
			return new Value(new IllegalArgException());
		}
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		ExamActivity001 activity = examActivity01Service.getActivity(code);
		if (activity == null) {
			return new Value(new EntityNotFoundException());
		}
		try {
			List<Map<String, Object>> results = zyCorrectingService.simpleCorrect(form.getqIds(), form.getAnswerList());
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
			ExamActivity001Answer answer = new ExamActivity001Answer();
			
			Long userId = Security.getUserId();
			
			answer.setExamQuestioncode(form.getPaperId());
			answer.setCreateAt(new Date());
			answer.setActivityCode(code);
			answer.setUserId(userId);
			answer.setResultList(rets);
			answer.setAnswerList(answers);
			answer.setRightRate(BigDecimal.valueOf((rightCount * 100f) / form.getCount()).setScale(0, BigDecimal.ROUND_HALF_UP));
			answer.setHomeworkTime(form.getHomeworkTime());
			examActivity01ExamService.save(answer);
			
			//处理客户礼包的添加
			addGift(code);
			
			// 记录答案、统计学情、记录错题
			zyStuQaService.asynCreateExamActivity(Security.getUserId(), form.getqIds(), form.getAnswerList(), form.getAnswerList(),
					null, null, null, rets, StudentQuestionAnswerSource.DAILY_PRACTICE, new Date());

			result.setDones(dones);
			result.setRightRate(dailyPractise.getRightRate());
			result.setResults(rets);

			return new Value(result);
		} catch (AbstractException e) {
			return new Value(new IllegalArgException());
		}
	}
	

}
