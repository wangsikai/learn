package com.lanking.uxb.zycon.homework.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassGroupService;
import com.lanking.uxb.zycon.homework.api.ZycHomeworkClazzService;
import com.lanking.uxb.zycon.homework.value.VZycHomeworkClazz;
import com.lanking.uxb.zycon.qs.api.ZycTeacherService;

/**
 * @author xinyu.zhou
 * @since yoomath V1.4
 */
@Component
public class ZycHomeworkClazzConvert extends Converter<VZycHomeworkClazz, HomeworkClazz, Long> {
	@Autowired
	private ZycHomeworkClazzService zycHomeworkClazzService;
	@Autowired
	private ZycTeacherService zycTeacherService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private DistrictService districtService;
	

	@Override
	protected Long getId(HomeworkClazz homeworkClazz) {
		return homeworkClazz.getId();
	}

	@Override
	protected VZycHomeworkClazz convert(HomeworkClazz homeworkClazz) {
		VZycHomeworkClazz v = new VZycHomeworkClazz();
		v.setName(homeworkClazz.getName());
		v.setId(homeworkClazz.getId());
		v.setCode(homeworkClazz.getCode());
		v.setDescription(homeworkClazz.getDescription());
		return v;
	}

	public VZycHomeworkClazz get(Long id) {
		return this.to(zycHomeworkClazzService.get(id));
	}

	public Map<Long, VZycHomeworkClazz> mget(Collection<Long> ids) {
		List<HomeworkClazz> list = zycHomeworkClazzService.mgetList(ids);
		List<VZycHomeworkClazz> vs = this.to(list);
		Map<Long, VZycHomeworkClazz> map = Maps.newHashMap();
		for (VZycHomeworkClazz v : vs) {
			map.put(v.getId(), v);
		}
		return map;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VZycHomeworkClazz, HomeworkClazz, Long, Teacher>() {

			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VZycHomeworkClazz vZycHomeworkClazz) {
				return homeworkClazz.getTeacherId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VZycHomeworkClazz vZycHomeworkClazz, Teacher value) {
				if (value != null) {
					vZycHomeworkClazz.setTeacherName(value.getName());
					vZycHomeworkClazz.setSchoolId(value.getSchoolId());
				}
			}

			@Override
			public Teacher getValue(Long key) {
				if (null == key)
					return null;
				return zycTeacherService.get(key);
			}

			@Override
			public Map<Long, Teacher> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys))
					return Maps.newHashMap();

				return zycTeacherService.mget(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VZycHomeworkClazz, HomeworkClazz, Long, School>() {

			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VZycHomeworkClazz vZycHomeworkClazz) {
				return vZycHomeworkClazz.getSchoolId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VZycHomeworkClazz vZycHomeworkClazz, School value) {
				if (value != null) {
					vZycHomeworkClazz.setSchoolName(value.getName());
					
					Long districtCode = value.getDistrictCode();
					//获取学校所在的省市
					String province = null;
					String city = null;
					int level = 0;
					do{
						District district = districtService.getDistrict(districtCode);
						//获取省
						if(district.getLevel() == 1) {
							province = district.getName();
						}
						
						String name = district.getName();
						
						//获取市
						if("市".equals(name.substring(name.length() - 1))){
							city = name;
						}
						
						districtCode = district.getPcode();
						level = district.getLevel();
					} while(level != 1);
					
					if(city == null) {
						city = province;
					}
					
					vZycHomeworkClazz.setProvince(province);
					vZycHomeworkClazz.setCity(city);
				}
			}

			@Override
			public School getValue(Long key) {
				if (key == null) {
					return null;
				}
				return schoolService.get(key);
			}

			@Override
			public Map<Long, School> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				return schoolService.mget(keys);
			}
		});
	}
}
