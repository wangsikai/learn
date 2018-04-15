package com.lanking.uxb.service.holiday.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayHomeworkItemQuestion;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemAnswer;
import com.lanking.cloud.domain.yoomath.holidayHomework.HolidayStuHomeworkItemQuestion;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.stat.api.StudentQuestionAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.uxb.service.holiday.api.HolidayHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemAnswerService;
import com.lanking.uxb.service.holiday.api.HolidayStuHomeworkItemQuestionService;
import com.lanking.uxb.service.holiday.value.VHolidayQuestion;

/**
 * 假期作业convert
 * 
 * @since yoomath V1.9
 * @author wangsenhao
 *
 */
@Component
public class HolidayQuestionConvert extends Converter<VHolidayQuestion, VHolidayQuestion, Long> {

	@Autowired
	private HolidayStuHomeworkItemQuestionConvert stuItemQuestionConvert;
	@Autowired
	private HolidayStuHomeworkItemQuestionService stuItemQuestionService;
	@Autowired
	private HolidayStuHomeworkItemAnswerConvert stuItemAnswerConvert;
	@Autowired
	private HolidayStuHomeworkItemAnswerService stuItemAnswerService;
	@Autowired
	private HolidayHomeworkItemQuestionConvert hdHkItemQuestionConvert;
	@Autowired
	private HolidayHomeworkItemQuestionService hdItemQuestionService;
	@Autowired
	private StudentQuestionAnswerService studentQuestionAnswerService;

	@Override
	protected Long getId(VHolidayQuestion s) {
		return s.getId();
	}

	public VHolidayQuestion to(VHolidayQuestion s, HolidayQuestionConvertOption option) {
		if (option.getHolidayStuHomeworkItemId() != null) {
			s.putTransientId("holidayStuHomeworkItemId", option.getHolidayStuHomeworkItemId());
		}
		if (option.getHolidayHomeworkItemId() != null) {
			s.putTransientId("holidayHomeworkItemId", option.getHolidayHomeworkItemId());
		}
		return super.to(s);
	}

	public List<VHolidayQuestion> to(List<VHolidayQuestion> ss, HolidayQuestionConvertOption option) {
		for (VHolidayQuestion s : ss) {
			if (option.getHolidayStuHomeworkItemId() != null) {
				s.putTransientId("holidayStuHomeworkItemId", option.getHolidayStuHomeworkItemId());
			}
			if (option.getHolidayHomeworkItemId() != null) {
				s.putTransientId("holidayHomeworkItemId", option.getHolidayHomeworkItemId());
			}
		}
		return super.to(ss);
	}

	@Override
	protected VHolidayQuestion convert(VHolidayQuestion s) {
		VHolidayQuestion v = s;
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 假期学生作业专项题目
		assemblers
				.add(new ConverterAssembler<VHolidayQuestion, VHolidayQuestion, String, HolidayStuHomeworkItemQuestion>() {
					@Override
					public boolean accept(VHolidayQuestion s) {
						return s.getTransientId("holidayStuHomeworkItemId") != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public String getKey(VHolidayQuestion s, VHolidayQuestion d) {
						return s.getId() + "_" + s.getTransientId("holidayStuHomeworkItemId");
					}

					@Override
					public void setValue(VHolidayQuestion s, VHolidayQuestion d, HolidayStuHomeworkItemQuestion value) {
						if (value != null) {
							d.setHolidayStuHomeworkItemQuestion(stuItemQuestionConvert.to(value));
						}
					}

					@Override
					public HolidayStuHomeworkItemQuestion getValue(String key) {
						if (StringUtils.isBlank(key)) {
							return null;
						}
						String[] idArr = key.split("_");
						return stuItemQuestionService.find(Long.parseLong(idArr[0]), Long.parseLong(idArr[1]));
					}

					@Override
					public Map<String, HolidayStuHomeworkItemQuestion> mgetValue(Collection<String> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						Set<Long> ids = Sets.newHashSet();
						Long holidayStuHomeworkItemId = null;
						for (String key : keys) {
							String[] idArr = key.split("_");
							if (holidayStuHomeworkItemId == null) {
								holidayStuHomeworkItemId = Long.parseLong(idArr[1]);
							}
							ids.add(Long.parseLong(idArr[0]));
						}
						List<HolidayStuHomeworkItemQuestion> ps = stuItemQuestionService.find(ids,
								holidayStuHomeworkItemId);
						Map<String, HolidayStuHomeworkItemQuestion> map = Maps.newHashMap();
						for (HolidayStuHomeworkItemQuestion p : ps) {
							map.put(p.getQuestionId() + "_" + p.getHolidayStuHomeworkItemId(), p);
						}
						return map;

					}
				});

		assemblers
				.add(new ConverterAssembler<VHolidayQuestion, VHolidayQuestion, Long, List<HolidayStuHomeworkItemAnswer>>() {
					@Override
					public boolean accept(VHolidayQuestion s) {
						return s.getTransientId("holidayStuHomeworkItemId") != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(VHolidayQuestion s, VHolidayQuestion d) {
						if (d.getHolidayStuHomeworkItemQuestion() == null) {
							return null;
						}
						return d.getHolidayStuHomeworkItemQuestion().getId();
					}

					@Override
					public void setValue(VHolidayQuestion s, VHolidayQuestion d,
							List<HolidayStuHomeworkItemAnswer> value) {
						if (CollectionUtils.isNotEmpty(value)) {
							d.setHolidayStuHomeworkItemAnswers(stuItemAnswerConvert.to(value));
						}
					}

					@Override
					public List<HolidayStuHomeworkItemAnswer> getValue(Long key) {
						if (key == null) {
							return null;
						}
						return stuItemAnswerService.queryItemAnswers(key);
					}

					@Override
					public Map<Long, List<HolidayStuHomeworkItemAnswer>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						return stuItemAnswerService.queryItemAnswers(keys);

					}
				});
		// 假期作业专项
		assemblers
				.add(new ConverterAssembler<VHolidayQuestion, VHolidayQuestion, String, HolidayHomeworkItemQuestion>() {
					@Override
					public boolean accept(VHolidayQuestion s) {
						return s.getTransientId("holidayHomeworkItemId") != null;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public String getKey(VHolidayQuestion s, VHolidayQuestion d) {
						return s.getId() + "_" + s.getTransientId("holidayHomeworkItemId");
					}

					@Override
					public void setValue(VHolidayQuestion s, VHolidayQuestion d, HolidayHomeworkItemQuestion value) {
						if (value != null) {
							d.setHolidayHomeworkItemQuestion(hdHkItemQuestionConvert.to(value));
						}
					}

					@Override
					public HolidayHomeworkItemQuestion getValue(String key) {
						if (StringUtils.isBlank(key)) {
							return null;
						}
						String[] idArr = key.split("_");
						return hdItemQuestionService.find(Long.parseLong(idArr[1]), Long.parseLong(idArr[0]));
					}

					@Override
					public Map<String, HolidayHomeworkItemQuestion> mgetValue(Collection<String> keys) {
						if (CollectionUtils.isEmpty(keys)) {
							return Maps.newHashMap();
						}
						Set<Long> ids = Sets.newHashSet();
						Long holidayHomeworkItemId = null;
						for (String key : keys) {
							String[] idArr = key.split("_");
							if (holidayHomeworkItemId == null) {
								holidayHomeworkItemId = Long.parseLong(idArr[1]);
							}
							ids.add(Long.parseLong(idArr[0]));
						}
						List<HolidayHomeworkItemQuestion> ps = hdItemQuestionService.find(ids, holidayHomeworkItemId);
						Map<String, HolidayHomeworkItemQuestion> map = Maps.newHashMap();
						for (HolidayHomeworkItemQuestion p : ps) {
							map.put(p.getQuestionId() + "_" + p.getHolidayHomeworkItemId(), p);
						}
						return map;

					}
				});

		// 假期作业做题情况信息
		assemblers.add(new ConverterAssembler<VHolidayQuestion, VHolidayQuestion, Long, Map>() {

			@Override
			public boolean accept(VHolidayQuestion question) {
				return Security.isClient() && Security.getUserType() == UserType.STUDENT;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(VHolidayQuestion question, VHolidayQuestion vQuestion) {
				return question.getId();
			}

			@Override
			public void setValue(VHolidayQuestion question, VHolidayQuestion vQuestion, Map value) {
				if (value != null && value.size() != 0) {
					BigDecimal doCountVal = (BigDecimal) value.get("do_count");
					BigDecimal wrongCountVal = (BigDecimal) value.get("wrong_count");
					BigDecimal wrongPeopleVal = (BigDecimal) value.get("wrong_people");
					Long doCount = doCountVal == null ? 0 : doCountVal.longValue();
					Long wrongCount = wrongCountVal == null ? 0 : wrongCountVal.longValue();
					if (doCount > 0) {
						Double rightRate = (doCount - wrongCount) * 100d / doCount;
						BigDecimal rightRateDecimal = new BigDecimal(rightRate).setScale(0,BigDecimal.ROUND_HALF_UP);
						vQuestion.setQuestionRightRate(rightRateDecimal.doubleValue());
					} else {
						vQuestion.setQuestionRightRate(0d);
					}
					vQuestion.setDoCount(doCount);
					Long wrongPeople = wrongPeopleVal == null ? 0 : wrongPeopleVal.longValue();
					vQuestion.setWrongPeopleCount(wrongPeople);
				} else {
					vQuestion.setDoCount(0L);
					vQuestion.setQuestionRightRate(0d);
					vQuestion.setWrongPeopleCount(0L);
				}
			}

			@Override
			public Map getValue(Long key) {

				List<Long> questionIds = new ArrayList<Long>(1);
				questionIds.add(key);

				List<Map> results = studentQuestionAnswerService
						.findStudentCondition(Security.getUserId(), questionIds);
				return results == null || results.size() == 0 ? null : results.get(0);
			}

			@Override
			public Map<Long, Map> mgetValue(Collection<Long> keys) {
				List<Map> results = studentQuestionAnswerService.findStudentCondition(Security.getUserId(), keys);
				Map<Long, Map> retMap = new HashMap<Long, Map>(results.size());

				for (Map m : results) {
					Long questionId = ((BigInteger) m.get("question_id")).longValue();
					retMap.put(questionId, m);
				}

				return retMap;
			}
		});
	}
}
