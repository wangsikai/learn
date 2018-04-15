package com.lanking.uxb.zycon.homework.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.common.resource.question.Answer;
import com.lanking.cloud.domain.common.resource.question.ChoiceFormat;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.question.api.AnswerService;
import com.lanking.uxb.service.resources.value.VQuestion;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.resources.value.VStudentHomeworkQuestion;
import com.lanking.uxb.zycon.base.api.ZycQuestionService;
import com.lanking.uxb.zycon.base.convert.ZycAnswerConvert;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkAnswerService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkQuestionService;
import com.lanking.uxb.zycon.homework.value.VZycQuestion;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomeworkQuestion;

@Component(value = "hzycQuestionConvert")
public class ZycQuestionConvert extends Converter<VZycQuestion, Question, Long> {

	@Autowired
	private ZycAnswerConvert zycAnswerConvert;
	@Autowired
	private ZycStudentHomeworkAnswerConvert shaConvert;
	@Autowired
	private ZycQuestionService questionService;
	@Autowired
	private ZycStudentHomeworkQuestionConvert shqConvert;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private ZycStudentHomeworkQuestionService shqService;
	@Autowired
	private ZycStudentHomeworkAnswerService shaService;
	@Autowired
	private ZycHomeworkService homeworkService;

	public VZycQuestion to(Question s, Long studentHomeworkId) {
		if (studentHomeworkId != null) {
			s.setStudentHomeworkId(studentHomeworkId);
		}

		return super.to(s);
	}

	public List<VZycQuestion> to(List<Question> ss, Long studentHomeworkId) {
		if (studentHomeworkId != null) {
			for (Question s : ss) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}

		return super.to(ss);
	}

	public Map<Long, VZycQuestion> to(Map<Long, Question> sMap, Long studentHomeworkId) {
		if (studentHomeworkId != null) {
			for (Question s : sMap.values()) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}

		return super.to(sMap);
	}

	public Map<Long, VZycQuestion> toMap(List<Question> ss, Long studentHomeworkId) {
		if (studentHomeworkId != null) {
			for (Question s : ss) {
				s.setStudentHomeworkId(studentHomeworkId);
			}
		}
		return super.toMap(ss);
	}

	@Override
	protected Long getId(Question question) {
		return question.getId();
	}

	@Override
	protected VZycQuestion convert(Question question) {
		VZycQuestion v = new VZycQuestion();
		convert(question, v);
		return v;
	}

	public void convert(Question s, VZycQuestion v) {
		v.setId(s.getId());
		v.setType(s.getType());
		v.setCode(StringUtils.defaultIfBlank(s.getCode()));
		v.setSubFlag(s.isSubFlag());
		v.setSequence(s.getSequence() == null ? 1 : s.getSequence());
		v.setParentId(s.getParentId() == null ? 0 : s.getParentId());
		v.setContent(s.getContent());
		v.setAnalysis(validBlank(s.getAnalysis()));
		// 选项
		List<String> choices = Lists.newArrayList();
		if (StringUtils.isNotBlank(s.getChoiceA())) {
			choices.add(this.delPContent(s.getChoiceA()));
		}
		if (StringUtils.isNotBlank(s.getChoiceB())) {
			choices.add(this.delPContent(s.getChoiceB()));
		}
		if (StringUtils.isNotBlank(s.getChoiceC())) {
			choices.add(this.delPContent(s.getChoiceC()));
		}
		if (StringUtils.isNotBlank(s.getChoiceD())) {
			choices.add(this.delPContent(s.getChoiceD()));
		}
		if (StringUtils.isNotBlank(s.getChoiceE())) {
			choices.add(this.delPContent(s.getChoiceE()));
		}
		if (StringUtils.isNotBlank(s.getChoiceF())) {
			choices.add(this.delPContent(s.getChoiceF()));
		}
		v.setChoices(choices);
		v.setAnswerNumber(s.getAnswerNumber());
		v.setCreateAt(s.getCreateAt());
		v.setUpdateAt(s.getUpdateAt());
		v.setCheckStatus(s.getStatus());
		v.setCorrectQuestion(s.isCorrectQuestion());
		v.setStudentHomeworkId(s.getStudentHomeworkId());
		v.setHomeworkId(s.getHomeworkId());
		v.setStudentQuestionId(s.getStudentQuestionId());

		if (s.getType() == Question.Type.SINGLE_CHOICE || s.getType() == Question.Type.MULTIPLE_CHOICE) {
			if (s.getChoiceFormat() != null) {
				v.setChoiceFormat(s.getChoiceFormat());
			} else {
				// 历史题目默认的排列方式
				if (choices.size() < 4) {
					v.setChoiceFormat(ChoiceFormat.HORIZONTAL);
				} else if (choices.size() == 4) {
					v.setChoiceFormat(ChoiceFormat.ABREAST);
				} else {
					v.setChoiceFormat(ChoiceFormat.VERTICAL);
				}
			}
		}
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 子题
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, Long, List<Question>>() {
			@Override
			public boolean accept(Question s) {
				return s.isInitSub();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VZycQuestion d) {
				if (s.isSubFlag()) {
					return null;
				}
				return s.getId();
			}

			@Override
			public void setValue(Question s, VZycQuestion d, List<Question> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					for (Question q : value) {
						q.setInitSub(s.isInitSub());
						q.setAnalysis(s.isAnalysis());
						q.setAnswer(s.isAnswer());
						q.setStudentHomeworkId(s.getStudentHomeworkId());
					}
					d.setChildren(to(value));
				}
			}

			@Override
			public List<Question> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return questionService.getSubQuestions(key);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Map<Long, List<Question>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, List<Question>> map = questionService.mgetSubQuestions(keys);
				return map;
			}

		});
		// 答案
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, Long, List<Answer>>() {
			@Override
			public boolean accept(Question s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VZycQuestion d) {
				return s.getId();
			}

			@Override
			public void setValue(Question s, VZycQuestion d, List<Answer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setAnswers(zycAnswerConvert.to(value));
				}
			}

			@Override
			public List<Answer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return answerService.getQuestionAnswers(key);
			}

			@Override
			public Map<Long, List<Answer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return answerService.getQuestionAnswers(keys);
			}
		});
		// StudentHomeworkQuestion原题转换
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, String, List<StudentHomeworkQuestion>>() {

			@Override
			public boolean accept(Question question) {
				return question.getStudentHomeworkId() != null && question.getStudentHomeworkId() > 0 && !question.isNewCorrect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public String getKey(Question s, VZycQuestion vQuestion) {
				return s.getId() + "_" + s.getStudentHomeworkId();
			}

			@Override
			public void setValue(Question question, VZycQuestion vQuestion, List<StudentHomeworkQuestion> value) {
				if (value != null && value.size()>0) {
					for(StudentHomeworkQuestion v:value){
						if(v.isNewCorrect()){
							vQuestion.setCorrectStudentHomeworkQuestion(shqConvert.to(v));
						}else{
							vQuestion.setStudentHomeworkQuestion(shqConvert.to(v));
						}
					}
				}
			}

			@Override
			public List<StudentHomeworkQuestion> getValue(String key) {
				if (StringUtils.isBlank(key)) {
					return null;
				}
				String[] idArr = key.split("_");
				List<StudentHomeworkQuestion> sthkqList = shqService.queryStuQuestions(Long.parseLong(idArr[1]),
						Long.parseLong(idArr[0]),null,false);
				return sthkqList;
			}

			@Override
			public Map<String, List<StudentHomeworkQuestion>> mgetValue(Collection<String> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				
				Map<String, List<StudentHomeworkQuestion>> map = Maps.newHashMap();
				
				Long id = null;
				Long studentHomeworkId = null;
				for (String key : keys) {
					String[] idArr = key.split("_");
					
					studentHomeworkId = Long.parseLong(idArr[1]);
					id = Long.parseLong(idArr[0]);
					
					List<StudentHomeworkQuestion> ps = shqService.queryStuQuestions(studentHomeworkId, id, null,false);
					
					for (StudentHomeworkQuestion p : ps) {
						if(null==map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId())){
							map.put(p.getQuestionId() + "_" + p.getStudentHomeworkId(), new ArrayList<StudentHomeworkQuestion>());
						}
						map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId()).add(p);
					}
				}
				
				return map;
			}
		});
		// StudentHomeworkQuestion订正题转换
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, String, List<StudentHomeworkQuestion>>() {

			@Override
			public boolean accept(Question question) {
				return question.getStudentHomeworkId() != null && question.getStudentHomeworkId() > 0 && question.isNewCorrect();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public String getKey(Question s, VZycQuestion vQuestion) {
				return s.getId() + "_" + s.getStudentHomeworkId();
			}

			@Override
			public void setValue(Question question, VZycQuestion vQuestion, List<StudentHomeworkQuestion> value) {
				if (value != null && value.size()>0) {
					for(StudentHomeworkQuestion v:value){
						if(v.isNewCorrect()){
							vQuestion.setCorrectStudentHomeworkQuestion(shqConvert.to(v));
						}else{
							vQuestion.setStudentHomeworkQuestion(shqConvert.to(v));
						}
					}
				}
			}

			@Override
			public List<StudentHomeworkQuestion> getValue(String key) {
				if (StringUtils.isBlank(key)) {
					return null;
				}
				String[] idArr = key.split("_");
				List<StudentHomeworkQuestion> sthkqList = shqService.queryStuQuestions(Long.parseLong(idArr[1]),
						Long.parseLong(idArr[0]),null,true);
				return sthkqList;
			}

			@Override
			public Map<String, List<StudentHomeworkQuestion>> mgetValue(Collection<String> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				
				Map<String, List<StudentHomeworkQuestion>> map = Maps.newHashMap();
				
				Long id = null;
				Long studentHomeworkId = null;
				for (String key : keys) {
					String[] idArr = key.split("_");
					
					studentHomeworkId = Long.parseLong(idArr[1]);
					id = Long.parseLong(idArr[0]);
					
					List<StudentHomeworkQuestion> ps = shqService.queryStuQuestions(studentHomeworkId, id, null,true);
					
					for (StudentHomeworkQuestion p : ps) {
						if(null==map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId())){
							map.put(p.getQuestionId() + "_" + p.getStudentHomeworkId(), new ArrayList<StudentHomeworkQuestion>());
						}
						map.get(p.getQuestionId() + "_" + p.getStudentHomeworkId()).add(p);
					}
				}
				
				return map;
			}
		});
		// 学生作业答案
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, Long, List<StudentHomeworkAnswer>>() {
			@Override
			public boolean accept(Question s) {
				return s.getStudentHomeworkId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VZycQuestion d) {
				if (d.getStudentHomeworkQuestion() == null) {
					return null;
				}
				return d.getStudentHomeworkQuestion().getId();
			}

			@Override
			public void setValue(Question s, VZycQuestion d, List<StudentHomeworkAnswer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setStudentHomeworkAnswers(shaConvert.to(value));
				}
			}

			@Override
			public List<StudentHomeworkAnswer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return shaService.find(key);
			}

			@Override
			public Map<Long, List<StudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return shaService.find(keys);
			}
		});
		// 学生作业订正答案
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, Long, List<StudentHomeworkAnswer>>() {
			@Override
			public boolean accept(Question s) {
				return s.getStudentHomeworkId() != null && s.getStudentHomeworkId() > 0;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question s, VZycQuestion d) {
				if (d.getCorrectStudentHomeworkQuestion() == null) {
					return null;
				}
				return d.getCorrectStudentHomeworkQuestion().getId();
			}

			@Override
			public void setValue(Question s, VZycQuestion d, List<StudentHomeworkAnswer> value) {
				if (CollectionUtils.isNotEmpty(value)) {
					d.setCorrectStudentHomeworkAnswers(shaConvert.to(value));
				}
			}

			@Override
			public List<StudentHomeworkAnswer> getValue(Long key) {
				if (key == null) {
					return null;
				}
				return shaService.find(key);
			}

			@Override
			public Map<Long, List<StudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				} else {
					Map<Long, List<StudentHomeworkAnswer>> answers = shaService.find(keys);
					
					return answers;
				}
			}
		});
		// 计算倒计时时间
		assemblers.add(new ConverterAssembler<VZycQuestion, Question, Long, Homework>() {

			@Override
			public boolean accept(Question question) {
				return question.getHomeworkId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Question question, VZycQuestion vZycQuestion) {
				return question.getHomeworkId();
			}

			@Override
			public void setValue(Question question, VZycQuestion vZycQuestion, Homework value) {
				if (null != value) {
					if (value.getLastCommitAt() != null) {
						Integer lastCommitMinutes = Env.getInt("homework.allcommit.then");
						long commitDeadlineMillis = TimeUnit.MINUTES.toMillis(lastCommitMinutes);
						long nowTimeMillis = System.currentTimeMillis();
						long commitAtMillis = value.getLastCommitAt().getTime();
						long countDownTime = commitAtMillis + commitDeadlineMillis - nowTimeMillis;
						vZycQuestion.setCountDownTime(countDownTime);
					}
				}
			}

			@Override
			public Homework getValue(Long key) {
				return homeworkService.get(key);
			}

			@Override
			public Map<Long, Homework> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return homeworkService.mget(keys);
			}
		});
	}

	private String delPContent(String str) {
		if (StringUtils.isNotBlank(str) && str.indexOf("<p>") == 0 && str.lastIndexOf("</p>") == str.length() - 4) {
			// 去除首尾的P标签
			str = str.substring(3, str.length() - 4);
		}
		return str;
	}
}
