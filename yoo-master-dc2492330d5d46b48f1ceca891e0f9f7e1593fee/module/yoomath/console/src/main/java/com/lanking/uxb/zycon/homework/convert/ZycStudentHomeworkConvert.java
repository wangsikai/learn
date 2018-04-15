package com.lanking.uxb.zycon.homework.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.homework.StudentHomework;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkService;
import com.lanking.uxb.zycon.homework.api.ZycStudentHomeworkAnswerService;
import com.lanking.uxb.zycon.homework.api.ZycStudentService;
import com.lanking.uxb.zycon.homework.api.ZycUserService;
import com.lanking.uxb.zycon.homework.value.VZycStudentHomework;
import com.lanking.uxb.zycon.user.value.VZycUser;

@Component
public class ZycStudentHomeworkConvert extends Converter<VZycStudentHomework, StudentHomework, Long> {

	@Autowired
	private ZycStudentHomeworkAnswerService shaService;
	@Autowired
	private ZycHomeworkService homeworkService;
	@Autowired
	private ZycHomeworkConvert homeworkConvert;
	@Autowired
	private ZycUserService userService;
	@Autowired
	private ZycStudentService studentService;

	@Override
	protected Long getId(StudentHomework s) {
		return s.getId();
	}

	public VZycStudentHomework to(StudentHomework s, boolean statisticCorrected, boolean initHomework, boolean initUser) {
		s.setStatisticCorrected(statisticCorrected);
		s.setInitHomework(initHomework);
		s.setInitUser(initUser);
		return super.to(s);
	}

	public List<VZycStudentHomework> to(List<StudentHomework> ss, boolean statisticCorrected, boolean initHomework,
			boolean initUser) {
		for (StudentHomework studentHomework : ss) {
			studentHomework.setStatisticCorrected(statisticCorrected);
			studentHomework.setInitHomework(initHomework);
			studentHomework.setInitUser(initUser);
		}
		return super.to(ss);
	}

	public Map<Long, VZycStudentHomework> to(Map<Long, StudentHomework> sMap, boolean statisticCorrected,
			boolean initHomework, boolean initUser) {
		for (StudentHomework s : sMap.values()) {
			s.setStatisticCorrected(statisticCorrected);
			s.setInitHomework(initHomework);
			s.setInitUser(initUser);
		}
		return super.to(sMap);
	}

	public Map<Long, VZycStudentHomework> toMap(List<StudentHomework> ss, boolean statisticCorrected,
			boolean initHomework, boolean initUser) {
		for (StudentHomework studentHomework : ss) {
			studentHomework.setStatisticCorrected(statisticCorrected);
			studentHomework.setInitHomework(initHomework);
			studentHomework.setInitUser(initUser);
		}
		return super.toMap(ss);
	}

	@Override
	protected VZycStudentHomework convert(StudentHomework s) {
		VZycStudentHomework v = new VZycStudentHomework();
		v.setCreateAt(s.getCreateAt());
		v.setHomeworkId(s.getHomeworkId());
		v.setId(s.getId());
		v.setIssueAt(s.getIssueAt());
		v.setStudentId(s.getStudentId());
		v.setSubmitAt(s.getSubmitAt());
		v.setRightRate(s.getRightRate());
		v.setStatus(s.getStatus());
		v.setHomeworkTime(s.getHomeworkTime() == null ? 0 : s.getHomeworkTime());
		v.setRank(s.getRank() != null ? s.getRank() : s.getRightRate() == null ? 0 : 1);
		v.setCorrected(s.isCorrected());
		v.setStudentCorrected(s.isStudentCorrected());
		v.setWrongCount(s.getWrongCount() == null ? 0 : s.getWrongCount());
		v.setRightCount(s.getRightCount() == null ? 0 : s.getRightCount());
		v.setStuSubmitAt(s.getStuSubmitAt());
		v.setCorrectStatus(s.getCorrectStatus());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycStudentHomework, StudentHomework, Long, Student>() {

			@Override
			public boolean accept(StudentHomework studentHomework) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework studentHomework, VZycStudentHomework vZycStudentHomework) {
				return studentHomework.getStudentId();
			}

			@Override
			public void setValue(StudentHomework studentHomework, VZycStudentHomework vZycStudentHomework, Student value) {
				if (null != value) {
					VZycUser user = new VZycUser();
					user.setName(value.getName());
					user.setUserId(value.getId());
					vZycStudentHomework.setUser(user);
				}
			}

			@Override
			public Student getValue(Long key) {
				if (null == key) {
					return null;
				}
				return studentService.get(key);
			}

			@Override
			public Map<Long, Student> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}

				return studentService.mget(keys);
			}
		});

		// 设置作业
		assemblers.add(new ConverterAssembler<VZycStudentHomework, StudentHomework, Long, Homework>() {

			@Override
			public boolean accept(StudentHomework s) {
				return s.isInitHomework();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VZycStudentHomework d) {
				return s.getHomeworkId();
			}

			@Override
			public void setValue(StudentHomework s, VZycStudentHomework d, Homework value) {
				if (value != null) {
					d.setHomework(homeworkConvert.to(value));
				}
			}

			@Override
			public Homework getValue(Long key) {
				if (key == null) {
					return null;
				}
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
		// 设置是否批改完
		assemblers.add(new ConverterAssembler<VZycStudentHomework, StudentHomework, Long, Boolean>() {

			@Override
			public boolean accept(StudentHomework s) {
				return s.isStatisticCorrected();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(StudentHomework s, VZycStudentHomework d) {
				return s.getId();
			}

			@Override
			public void setValue(StudentHomework s, VZycStudentHomework d, Boolean value) {
				if (value != null) {
					d.setCorrected(value);
				}
			}

			@Override
			public Boolean getValue(Long key) {
				if (key == null) {
					return null;
				}
				Set<Long> keys = Sets.newHashSet();
				keys.add(key);
				return shaService.countNotCorrected(keys).get(key) == 0;
			}

			@Override
			public Map<Long, Boolean> mgetValue(Collection<Long> keys) {
				Map<Long, Boolean> map = Maps.newHashMap();
				if (CollectionUtils.isEmpty(keys)) {
					return map;
				}
				Map<Long, Long> countMap = shaService.countNotCorrected(keys);
				for (Long key : countMap.keySet()) {
					if (countMap.get(key) == 0) {
						map.put(key, true);
					} else {
						map.put(key, false);
					}
				}
				return map;
			}

		});
	}
}
