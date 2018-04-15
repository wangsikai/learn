package com.lanking.uxb.channelSales.channel.convert;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.base.api.CsSchoolService;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.service.code.api.DistrictService;

/**
 * School -> VSchool
 *
 * @author xinyu.zhou
 * @since 2.5.0
 */
@Component
public class CsSchoolConvert extends Converter<VSchool, School, Long> {

	private Logger logger = LoggerFactory.getLogger(CsSchoolConvert.class);
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private CsSchoolService schoolService;

	@Override
	protected Long getId(School school) {
		return school.getId();
	}

	@Override
	protected VSchool convert(School school) {
		VSchool v = new VSchool();
		v.setId(school.getId());
		v.setName(school.getName());
		v.setSchoolType(school.getType());
		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VSchool, School, Long, UserChannel>() {
			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getId();
			}

			@Override
			public void setValue(School school, VSchool vSchool, UserChannel value) {
				if (value != null) {
					vSchool.setChannelName(value.getName());
					vSchool.setChannelCode(value.getCode());
				}
			}

			@Override
			public UserChannel getValue(Long key) {
				return userChannelService.findBySchool(key);
			}

			@Override
			public Map<Long, UserChannel> mgetValue(Collection<Long> keys) {
				return userChannelService.mgetBySchools(keys);
			}
		});

		assemblers.add(new ConverterAssembler<VSchool, School, Long, String>() {
			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getDistrictCode();
			}

			@Override
			public void setValue(School school, VSchool vSchool, String value) {
				if (value != null) {
					vSchool.setDistrictName(value);
				}
			}

			@Override
			public String getValue(Long key) {
				return districtService.getDistrictName(key);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, String> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}
				return districtService.mgetDistrictName(keys);
			}
		});

		// 转换教师用户数量
		assemblers.add(new ConverterAssembler<VSchool, School, Long, Map>() {
			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getId();
			}

			@Override
			public void setValue(School school, VSchool vSchool, Map value) {
				if (value != null) {
					vSchool.setTeacherNum(((BigInteger) value.get("user_num")).longValue());
				}
			}

			@Override
			public Map getValue(Long key) {
				List<Long> ids = new ArrayList<Long>(1);
				ids.add(key);
				return schoolService.countSchoolUserNum(ids, UserType.TEACHER).get(0);
			}

			@Override
			public Map<Long, Map> mgetValue(Collection<Long> keys) {
				List<Map> results = schoolService.countSchoolUserNum(keys, UserType.TEACHER);
				Map<Long, Map> ret = new HashMap<Long, Map>(results.size());
				for (Map m : results) {
					ret.put(((BigInteger) m.get("school_id")).longValue(), m);
				}
				return ret;
			}
		});

		// 转换学生用户数量
		assemblers.add(new ConverterAssembler<VSchool, School, Long, Map>() {
			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getId();
			}

			@Override
			public void setValue(School school, VSchool vSchool, Map value) {
				if (value != null) {
					vSchool.setStudentNum(((BigInteger) value.get("user_num")).longValue());
				}
			}

			@Override
			public Map getValue(Long key) {
				List<Long> ids = new ArrayList<Long>(1);
				ids.add(key);
				return schoolService.countSchoolUserNum(ids, UserType.STUDENT).get(0);
			}

			@Override
			public Map<Long, Map> mgetValue(Collection<Long> keys) {
				List<Map> results = schoolService.countSchoolUserNum(keys, UserType.STUDENT);
				Map<Long, Map> ret = new HashMap<Long, Map>(results.size());
				for (Map m : results) {
					ret.put(((BigInteger) m.get("school_id")).longValue(), m);
				}
				return ret;
			}
		});

		// 转换学生会员数量
		assemblers.add(new ConverterAssembler<VSchool, School, Long, Long>() {

			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getId();
			}

			@Override
			public void setValue(School school, VSchool vSchool, Long value) {
				vSchool.setStudentMemberNum(value);
			}

			@Override
			public Long getValue(Long key) {
				List<Long> ids = new ArrayList<Long>(1);
				ids.add(key);
				List<Map> results = schoolService.countSchoolStudentMemberNum(ids);
				Map m = results.get(0);

				return ((BigInteger) m.get("member_num")).longValue();
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				List<Map> results = schoolService.countSchoolStudentMemberNum(keys);
				Map<Long, Long> ret = new HashMap<Long, Long>(results.size());

				for (Map m : results) {
					try {
						Long schoolId = ((BigInteger) m.get("school_id")).longValue();
						Long memberNum = ((BigInteger) m.get("member_num")).longValue();
						ret.put(schoolId, memberNum);
					} catch (Exception e) {
						logger.error("指定渠道学校ID获取异常", e);
					}
				}
				return ret;
			}
		});

		// 转换学校教师会员数量
		assemblers.add(new ConverterAssembler<VSchool, School, Long, Long>() {
			@Override
			public boolean accept(School school) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(School school, VSchool vSchool) {
				return school.getId();
			}

			@Override
			public void setValue(School school, VSchool vSchool, Long value) {
				vSchool.setTeacherMemberNum(value);
			}

			@Override
			public Long getValue(Long key) {
				List<Long> ids = new ArrayList<Long>(1);
				ids.add(key);
				Map result = schoolService.countSchoolTeacherMemberNum(ids).get(0);
				return ((BigInteger) result.get("member_num")).longValue();
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Long> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Collections.EMPTY_MAP;
				}

				List<Map> results = schoolService.countSchoolTeacherMemberNum(keys);

				Map<Long, Long> ret = new HashMap<Long, Long>(results.size());
				for (Map m : results) {
					try {
						Long schoolId = ((BigInteger) m.get("school_id")).longValue();
						Long memberNum = ((BigInteger) m.get("member_num")).longValue();

						ret.put(schoolId, memberNum);
					} catch (Exception e) {
						logger.error("指定渠道学校ID获取异常", e);
					}
				}
				return ret;
			}
		});

	}
}
