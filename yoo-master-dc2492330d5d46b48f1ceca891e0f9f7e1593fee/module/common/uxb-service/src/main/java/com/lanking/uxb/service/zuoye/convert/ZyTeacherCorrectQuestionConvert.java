package com.lanking.uxb.service.zuoye.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkAnswer;
import com.lanking.cloud.domain.yoomath.homework.StudentHomeworkQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.resources.api.StudentHomeworkAnswerService;
import com.lanking.uxb.service.resources.api.StudentHomeworkService;
import com.lanking.uxb.service.resources.convert.StudentHomeworkAnswerConvert;
import com.lanking.uxb.service.resources.convert.StudentHomeworkQuestionConvert;
import com.lanking.uxb.service.resources.value.VStudentHomeworkAnswer;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.value.VTeacherCorrectQuestion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentHomeworkQuestion -> VTeacherCorrectQuestion
 *
 * @author xinyu.zhou
 * @since yoomath V1.9.1
 */
@Component
public class ZyTeacherCorrectQuestionConvert extends Converter<VTeacherCorrectQuestion, StudentHomeworkQuestion, Long> {
	@Autowired
	private StudentHomeworkQuestionConvert studentHomeworkQuestionConvert;
	@Autowired
	private StudentHomeworkService studentHomeworkService;
	@Autowired
	private StudentHomeworkAnswerService studentHomeworkAnswerService;
	@Autowired
	private StudentHomeworkAnswerConvert studentHomeworkAnswerConvert;
	@Autowired
	private UserConvert userConvert;

	@Override
	protected Long getId(StudentHomeworkQuestion studentHomeworkQuestion) {
		return studentHomeworkQuestion.getId();
	}

	@Override
	protected VTeacherCorrectQuestion convert(StudentHomeworkQuestion studentHomeworkQuestion) {
		VTeacherCorrectQuestion v = new VTeacherCorrectQuestion();
		v.setStuHomeworkQuestion(studentHomeworkQuestionConvert.to(studentHomeworkQuestion));
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 学生作业
		assemblers
				.add(new ConverterAssembler<VTeacherCorrectQuestion, StudentHomeworkQuestion, Long, StudentHomework>() {

					@Override
					public boolean accept(StudentHomeworkQuestion studentHomeworkQuestion) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentHomeworkQuestion studentHomeworkQuestion,
							VTeacherCorrectQuestion vTeacherCorrectQuestion) {
						return studentHomeworkQuestion.getStudentHomeworkId();
					}

					@Override
					public void setValue(StudentHomeworkQuestion studentHomeworkQuestion,
							VTeacherCorrectQuestion vTeacherCorrectQuestion, StudentHomework value) {
						if (value != null) {
							vTeacherCorrectQuestion.setStuHomeworkId(value.getId());
							vTeacherCorrectQuestion.setUserId(value.getStudentId());
						}
					}

					@Override
					public StudentHomework getValue(Long key) {
						if (key == null) {
							return null;
						}
						return studentHomeworkService.get(key);
					}

					@Override
					public Map<Long, StudentHomework> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}

						return studentHomeworkService.mgetMap(keys);
					}
				});

		// 转换学生信息
		assemblers.add(new ConverterAssembler<VTeacherCorrectQuestion, StudentHomeworkQuestion, Long, VUser>() {

			@Override
			public boolean accept(StudentHomeworkQuestion studentHomeworkQuestion) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomeworkQuestion studentHomeworkQuestion,
					VTeacherCorrectQuestion vTeacherCorrectQuestion) {
				return vTeacherCorrectQuestion.getUserId();
			}

			@Override
			public void setValue(StudentHomeworkQuestion studentHomeworkQuestion,
					VTeacherCorrectQuestion vTeacherCorrectQuestion, VUser value) {
				vTeacherCorrectQuestion.setUser(value);
			}

			@Override
			public VUser getValue(Long key) {
				return userConvert.get(key);
			}

			@Override
			public Map<Long, VUser> mgetValue(Collection<Long> keys) {
				return userConvert.mget(keys);
			}
		});

		// 转换学生答案
		assemblers
				.add(new ConverterAssembler<VTeacherCorrectQuestion, StudentHomeworkQuestion, Long, List<VStudentHomeworkAnswer>>() {

					@Override
					public boolean accept(StudentHomeworkQuestion studentHomeworkQuestion) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(StudentHomeworkQuestion studentHomeworkQuestion,
							VTeacherCorrectQuestion vTeacherCorrectQuestion) {
						return studentHomeworkQuestion.getId();
					}

					@Override
					public void setValue(StudentHomeworkQuestion studentHomeworkQuestion,
							VTeacherCorrectQuestion vTeacherCorrectQuestion, List<VStudentHomeworkAnswer> value) {
						vTeacherCorrectQuestion.setAnswers(value);
					}

					@Override
					public List<VStudentHomeworkAnswer> getValue(Long key) {
						return studentHomeworkAnswerConvert.to(studentHomeworkAnswerService.find(key));
					}

					@Override
					public Map<Long, List<VStudentHomeworkAnswer>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}

						Map<Long, List<StudentHomeworkAnswer>> studentAnswerMap = studentHomeworkAnswerService
								.find(keys);
						Map<Long, List<VStudentHomeworkAnswer>> vMap = new HashMap<Long, List<VStudentHomeworkAnswer>>(
								studentAnswerMap.size());
						for (Map.Entry<Long, List<StudentHomeworkAnswer>> entry : studentAnswerMap.entrySet()) {
							List<VStudentHomeworkAnswer> vs = studentHomeworkAnswerConvert.to(entry.getValue());
							vMap.put(entry.getKey(), vs);
						}
						return vMap;
					}
				});
	}
}
