package com.lanking.uxb.service.zuoye.convert;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStudentClazzStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzStatService;
import com.lanking.uxb.service.zuoye.value.VHomeworkStudentClazzStat;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Component
public class ZyHomeworkStudentClazzStatConvert extends
		Converter<VHomeworkStudentClazzStat, HomeworkStudentClazzStat, Long> {
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkStudentClazzStatService zyHomeworkStudentClazzStatService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;

	@Override
	protected Long getId(HomeworkStudentClazzStat homeworkStudentClazzStat) {
		return homeworkStudentClazzStat.getId();
	}

	@Override
	protected VHomeworkStudentClazzStat convert(HomeworkStudentClazzStat homeworkStudentClazzStat) {
		VHomeworkStudentClazzStat v = new VHomeworkStudentClazzStat();
		v.setRightRate(homeworkStudentClazzStat.getDays30RightRate() == null ? new BigDecimal(0)
				: homeworkStudentClazzStat.getDays30RightRate());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VHomeworkStudentClazzStat, HomeworkStudentClazzStat, Long, VUser>() {

			@Override
			public boolean accept(HomeworkStudentClazzStat homeworkStudentClazzStat) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkStudentClazzStat homeworkStudentClazzStat,
					VHomeworkStudentClazzStat vHomeworkStudentClazzStat) {
				return homeworkStudentClazzStat.getStudentId();
			}

			@Override
			public void setValue(HomeworkStudentClazzStat homeworkStudentClazzStat,
					VHomeworkStudentClazzStat vHomeworkStudentClazzStat, VUser value) {
				if (value != null) {
					vHomeworkStudentClazzStat.setStudent(value);
				}
			}

			@Override
			public VUser getValue(Long key) {
				if (key == null)
					return null;
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				return userConvert.get(key, option);
			}

			@Override
			public Map<Long, VUser> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				UserConvertOption option = new UserConvertOption();
				option.setInitMemberType(true);
				return userConvert.mget(keys, option);
			}
		});

		assemblers
				.add(new ConverterAssembler<VHomeworkStudentClazzStat, HomeworkStudentClazzStat, Long, Map<Long, String>>() {

					@Override
					public boolean accept(HomeworkStudentClazzStat homeworkStudentClazzStat) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(HomeworkStudentClazzStat homeworkStudentClazzStat,
							VHomeworkStudentClazzStat vHomeworkStudentClazzStat) {
						return homeworkStudentClazzStat.getStudentId();
					}

					@Override
					public void setValue(HomeworkStudentClazzStat homeworkStudentClazzStat,
							VHomeworkStudentClazzStat vHomeworkStudentClazzStat, Map<Long, String> value) {
						if (value != null) {
							vHomeworkStudentClazzStat.setMark(value.get(homeworkStudentClazzStat.getClassId()));
						}
					}

					@Override
					public Map<Long, String> getValue(Long key) {
						if (key == null)
							return null;
						List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.findByStudentIds(Lists
								.newArrayList(key));
						Map<Long, String> map = Maps.newHashMap();
						for (HomeworkStudentClazz c : clazzs) {
							map.put(c.getClassId(), c.getMark() == null ? "" : c.getMark());
						}

						return map;
					}

					@Override
					public Map<Long, Map<Long, String>> mgetValue(Collection<Long> keys) {
						if (CollectionUtils.isEmpty(keys))
							return Maps.newHashMap();
						List<HomeworkStudentClazz> list = zyHkStuClazzService.findByStudentIds(keys);
						Map<Long, Map<Long, String>> map = Maps.newHashMap();
						for (HomeworkStudentClazz c : list) {
							if (map.get(c.getStudentId()) == null) {
								map.put(c.getStudentId(), Maps.<Long, String> newHashMap());
							}
							map.get(c.getStudentId()).put(c.getClassId(), c.getMark() == null ? "" : c.getMark());
						}
						return map;
					}
				});
	}
}
