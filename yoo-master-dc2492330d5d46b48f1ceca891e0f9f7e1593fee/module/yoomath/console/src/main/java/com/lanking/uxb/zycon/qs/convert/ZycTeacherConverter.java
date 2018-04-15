package com.lanking.uxb.zycon.qs.convert;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.Phase;
import com.lanking.cloud.domain.common.baseData.Subject;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.zycon.operation.api.ZycAccountService;
import com.lanking.uxb.zycon.qs.value.VZycTeacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Component
public class ZycTeacherConverter extends Converter<VZycTeacher, Teacher, Long> {
	@Autowired
	private ZycAccountService accountService;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private SubjectService subjectService;

	@Override
	protected Long getId(Teacher teacher) {
		return teacher.getId();
	}

	@Override
	protected VZycTeacher convert(Teacher teacher) {
		VZycTeacher v = new VZycTeacher();
		v.setRealName(teacher.getName());
		v.setSchoolName(teacher.getSchoolName());
		v.setSex(v.getSex());
		v.setPhaseCode(teacher.getPhaseCode());
		v.setSubjectCode(teacher.getSubjectCode());
		v.setUserId(teacher.getId());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycTeacher, Teacher, Long, Account>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Teacher teacher, VZycTeacher vZycTeacher) {
				return teacher.getId();
			}

			@Override
			public void setValue(Teacher teacher, VZycTeacher vZycTeacher, Account value) {
				if (value != null) {
					vZycTeacher.setAccountName(value.getName());
					vZycTeacher.setMobile(value.getMobile());
					vZycTeacher.setEmail(value.getEmail());
				}
			}

			@Override
			public Account getValue(Long key) {
				if (null == key)
					return null;

				return accountService.getAccountByUserId(key);
			}

			@Override
			public Map<Long, Account> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();
				return accountService.mgetByUserId(keys);
			}
		});

		// 阶段
		assemblers.add(new ConverterAssembler<VZycTeacher, Teacher, Integer, Phase>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Teacher teacher, VZycTeacher vZycTeacher) {
				return teacher.getPhaseCode();
			}

			@Override
			public void setValue(Teacher teacher, VZycTeacher vZycTeacher, Phase value) {
				if (value != null) {
					vZycTeacher.setPhaseName(value.getName());
				}
			}

			@Override
			public Phase getValue(Integer key) {
				if (null == key)
					return null;
				return phaseService.get(key);
			}

			@Override
			public Map<Integer, Phase> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();

				return phaseService.mget(keys);
			}
		});

		// 阶段
		assemblers.add(new ConverterAssembler<VZycTeacher, Teacher, Integer, Subject>() {

			@Override
			public boolean accept(Teacher teacher) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(Teacher teacher, VZycTeacher vZycTeacher) {
				return vZycTeacher.getSubjectCode();
			}

			@Override
			public void setValue(Teacher teacher, VZycTeacher vZycTeacher, Subject value) {
				if (value != null) {
					vZycTeacher.setSubjectName(value.getName());
				}
			}

			@Override
			public Subject getValue(Integer key) {
				if (null == key)
					return null;
				return subjectService.get(key);
			}

			@Override
			public Map<Integer, Subject> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();

				return subjectService.mget(keys);
			}
		});
	}
}
