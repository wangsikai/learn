package com.lanking.uxb.service.zuoye.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStatService;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;
import com.lanking.uxb.service.zuoye.value.VHomeworkStat;

@Component
public class ZyHomeworkClazzConvert extends Converter<VHomeworkClazz, HomeworkClazz, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkStatService hkStatService;
	@Autowired
	private ZyHomeworkStatConvert hkStatConvert;
	@Autowired
	private ZyHomeworkClassGroupService groupService;
	@Autowired
	private ZyHomeworkClazzGroupConvert groupConvert;

	public VHomeworkClazz to(HomeworkClazz s, ZyHomeworkClassConvertOption option) {
		s.setInitStat(option.isInitStat());
		s.setInitTeacher(option.isInitTeacher());
		return super.to(s);
	}

	public List<VHomeworkClazz> to(List<HomeworkClazz> ss, ZyHomeworkClassConvertOption option) {
		for (HomeworkClazz s : ss) {
			s.setInitStat(option.isInitStat());
			s.setInitTeacher(option.isInitTeacher());
			s.setInitClassGroup(option.isInitClassGroup());
		}
		return super.to(ss);
	}

	public Map<Long, VHomeworkClazz> to(Map<Long, HomeworkClazz> sMap, ZyHomeworkClassConvertOption option) {
		for (HomeworkClazz s : sMap.values()) {
			s.setInitStat(option.isInitStat());
			s.setInitTeacher(option.isInitTeacher());
		}
		return super.to(sMap);
	}

	public Map<Long, VHomeworkClazz> toMap(List<HomeworkClazz> ss, ZyHomeworkClassConvertOption option) {
		for (HomeworkClazz s : ss) {
			s.setInitStat(option.isInitStat());
			s.setInitTeacher(option.isInitTeacher());
		}
		return super.toMap(ss);
	}

	@Override
	protected Long getId(HomeworkClazz s) {
		return s.getId();
	}

	@Override
	protected VHomeworkClazz convert(HomeworkClazz s) {
		VHomeworkClazz v = new VHomeworkClazz();
		v.setCloseAt(s.getCloseAt());
		v.setDeleteAt(s.getDeleteAt());
		v.setCode(s.getCode());
		v.setCreateAt(s.getCreateAt());
		v.setDescription(validBlank(s.getDescription()));
		v.setId(s.getId());
		v.setName(s.getName());
		v.setStatus(s.getStatus());
		v.setLockStatus(s.getLockStatus());
		v.setStudentNum(s.getStudentNum());
		v.setTeacherId(s.getTeacherId());
		v.setClazzFrom(s.getClazzFrom());
		v.setFromCode(s.getFromCode());
		v.setBookVersionId(s.getBookVersionId());
		v.setBookCataId(s.getBookCataId());
		v.setNeedConfirm(s.isNeedConfirm());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// init teacher
		assemblers.add(new ConverterAssembler<VHomeworkClazz, HomeworkClazz, Long, VUser>() {

			@Override
			public boolean accept(HomeworkClazz s) {
				return s.isInitTeacher();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz s, VHomeworkClazz d) {
				return s.getTeacherId();
			}

			@Override
			public void setValue(HomeworkClazz s, VHomeworkClazz d, VUser value) {
				d.setTeacher(value);
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
		// init stat
		assemblers.add(new ConverterAssembler<VHomeworkClazz, HomeworkClazz, Long, VHomeworkStat>() {

			@Override
			public boolean accept(HomeworkClazz s) {
				return s.isInitStat();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz s, VHomeworkClazz d) {
				return s.getId();
			}

			@Override
			public void setValue(HomeworkClazz s, VHomeworkClazz d, VHomeworkStat value) {
				if (value == null) {
					VHomeworkStat state = new VHomeworkStat();
					if (Security.isClient()) {
						state.setCompletionRate(null);
						state.setRightRate(null);
					}
					d.setHomeworkStat(state);
				} else {
					d.setHomeworkStat(value);
				}
			}

			@Override
			public VHomeworkStat getValue(Long key) {
				if (key == null || key <= 0) {
					return null;
				}
				return hkStatConvert.to(hkStatService.getByHomeworkClassId(key));
			}

			@SuppressWarnings("unchecked")
			@Override
			public Map<Long, VHomeworkStat> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				Map<Long, VHomeworkStat> vmap = new HashMap<Long, VHomeworkStat>(keys.size());
				List<HomeworkStat> list = hkStatService.getByHomeworkClassIds(keys);
				List<VHomeworkStat> vlist = hkStatConvert.to(list);
				for (VHomeworkStat v : vlist) {
					vmap.put(v.getHomeworkClassId(), v);
				}
				return vmap;
			}

		});

		// init stat
		assemblers.add(new ConverterAssembler<VHomeworkClazz, HomeworkClazz, Long, List<HomeworkClazzGroup>>() {

			@Override
			public boolean accept(HomeworkClazz s) {
				return s.isInitClassGroup();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz s, VHomeworkClazz d) {
				return s.getId();
			}

			@Override
			public void setValue(HomeworkClazz s, VHomeworkClazz d, List<HomeworkClazzGroup> value) {
				d.setGroupList(groupConvert.to(value));
			}

			@Override
			public List<HomeworkClazzGroup> getValue(Long key) {
				return groupService.groups(key);
			}

			@Override
			public Map<Long, List<HomeworkClazzGroup>> mgetValue(Collection<Long> keys) {
				return groupService.groupMaps(keys);
			}

		});
	}
}
