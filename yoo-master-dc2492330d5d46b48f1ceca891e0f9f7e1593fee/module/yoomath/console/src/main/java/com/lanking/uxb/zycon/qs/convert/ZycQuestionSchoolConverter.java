package com.lanking.uxb.zycon.qs.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoomath.school.QuestionSchool;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.zycon.book.api.ZycSchoolBookService;
import com.lanking.uxb.zycon.qs.api.ZycSchoolQuestionService;
import com.lanking.uxb.zycon.qs.api.ZycSchoolService;
import com.lanking.uxb.zycon.qs.value.VZycQuestionSchool;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4.2
 */
@Component
public class ZycQuestionSchoolConverter extends Converter<VZycQuestionSchool, QuestionSchool, Long> {
	@Autowired
	private ZycSchoolService zycSchoolService;
	@Autowired
	private ZycSchoolBookService zycSchoolBookService;
	@Autowired
	private ZycSchoolQuestionService schoolQuestionService;

	@Override
	protected Long getId(QuestionSchool questionSchool) {
		return questionSchool.getSchoolId();
	}

	@Override
	protected VZycQuestionSchool convert(QuestionSchool questionSchool) {
		VZycQuestionSchool v = new VZycQuestionSchool();
		v.setTeacherCount(questionSchool.getTeacherCount());
		v.setStatus(questionSchool.getStatus());
		v.setSchoolId(questionSchool.getSchoolId());
		v.setRecordQuestionCount(questionSchool.getRecordQuestionCount() == null ? 0 : questionSchool
				.getRecordQuestionCount());
		v.setTeacherSchoolVipCount(questionSchool.getTeacherSchoolVipCount());
		return v;
	}

	@Override
	public void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycQuestionSchool, QuestionSchool, Long, School>() {

			@Override
			public boolean accept(QuestionSchool questionSchool) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionSchool questionSchool, VZycQuestionSchool vZycQuestionSchool) {
				return questionSchool.getSchoolId();
			}

			@Override
			public void setValue(QuestionSchool questionSchool, VZycQuestionSchool vZycQuestionSchool, School value) {
				if (value != null) {
					vZycQuestionSchool.setSchoolName(value.getName());
				}
			}

			@Override
			public School getValue(Long key) {
				if (key == null)
					return null;

				return zycSchoolService.get(key);
			}

			@Override
			public Map<Long, School> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();

				return zycSchoolService.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycQuestionSchool, QuestionSchool, Long, Long>() {

			@Override
			public boolean accept(QuestionSchool s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionSchool s, VZycQuestionSchool d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(QuestionSchool s, VZycQuestionSchool d, Long value) {
				d.setBookCount(value);
			}

			@Override
			public Long getValue(Long key) {
				return zycSchoolBookService.getBookCount(key);
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				return zycSchoolBookService.mgetBookCount(keys);
			}

		});

		assemblers.add(new ConverterAssembler<VZycQuestionSchool, QuestionSchool, Long, Long>() {

			@Override
			public boolean accept(QuestionSchool questionSchool) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(QuestionSchool questionSchool, VZycQuestionSchool vZycQuestionSchool) {
				return questionSchool.getSchoolId();
			}

			@Override
			public void setValue(QuestionSchool questionSchool, VZycQuestionSchool vZycQuestionSchool, Long value) {
				if (value != null) {
					vZycQuestionSchool.setQuestionCount(value);
				}
			}

			@Override
			public Long getValue(Long key) {
				return schoolQuestionService.countSchoolQuestion(key);
			}

			@Override
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return schoolQuestionService.mgetCountSchoolQuestion(keys);
			}
		});
	}
}
