package com.lanking.uxb.channelSales.base.convert;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.channelSales.base.api.CsHomeworkClassService;
import com.lanking.uxb.channelSales.base.api.CsSchoolService;
import com.lanking.uxb.channelSales.base.value.VCsHomeworkClass;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.service.user.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author xinyu.zhou
 * @since 3.9.2
 */
@Component
public class CsHomeworkClassConvert extends Converter<VCsHomeworkClass, HomeworkClazz, Long> {
	@Autowired
	private CsUserMemberService userMemberService;
	@Autowired
	private UserService userService;
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsSchoolService schoolService;
	@Autowired
	private CsHomeworkClassService homeworkClassService;

	@Override
	protected Long getId(HomeworkClazz homeworkClazz) {
		return homeworkClazz.getId();
	}

	@Override
	protected VCsHomeworkClass convert(HomeworkClazz homeworkClazz) {
		VCsHomeworkClass v = new VCsHomeworkClass();

		v.setId(homeworkClazz.getId());
		v.setName(homeworkClazz.getName());
		v.setCode(homeworkClazz.getCode());
		v.setClazzNum(homeworkClazz.getStudentNum());
		v.setStatus(homeworkClazz.getStatus());

		return v;
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 班级会员数量
		assemblers.add(new ConverterAssembler<VCsHomeworkClass, HomeworkClazz, Long, Integer>() {

			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> map) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass) {
				return homeworkClazz.getId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass, Integer value) {
				vCsHomeworkClass.setMemberNum(value == null ? 0 : value);
			}

			@Override
			public Integer getValue(Long aLong) {
				List<Long> classIds = new ArrayList<Long>(1);
				classIds.add(aLong);
				Map<Long, Integer> retMap = userMemberService.countMemberCountByClasses(classIds);
				return retMap == null || retMap.size() == 0 ? 0 : retMap.get(aLong);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Integer> mgetValue(Collection<Long> collection) {
				if (CollectionUtils.isEmpty(collection)) {
					return Collections.EMPTY_MAP;
				}
				return userMemberService.countMemberCountByClasses(collection);
			}
		});

		// 转换班级学生数量
		assemblers.add(new ConverterAssembler<VCsHomeworkClass, HomeworkClazz, Long, Integer>() {

			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass) {
				return homeworkClazz.getId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass, Integer value) {
				vCsHomeworkClass.setClazzNum(value);
			}

			@Override
			public Integer getValue(Long key) {
				List<Long> classIds = new ArrayList<Long>(1);
				classIds.add(key);

				List<Map> datas = homeworkClassService.countStus(classIds);
				if (CollectionUtils.isEmpty(datas)) {
					return 0;
				}
				Map m = datas.get(0);
				return ((BigInteger) m.get("stu_num")).intValue();
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, Integer> mgetValue(Collection<Long> keys) {
				List<Map> datas = homeworkClassService.countStus(keys);
				if (CollectionUtils.isEmpty(datas)) {
					return Collections.EMPTY_MAP;
				}

				Map<Long, Integer> retMap = new HashMap<Long, Integer>(datas.size());
				for (Map m : datas) {
					Long id = ((BigInteger) m.get("id")).longValue();
					Integer stuNum = ((BigInteger) m.get("stu_num")).intValue();

					retMap.put(id, stuNum);
				}
				return retMap;
			}

		});

		// 转换用户信息
		assemblers.add(new ConverterAssembler<VCsHomeworkClass, HomeworkClazz, Long, User>() {
			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> map) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass) {
				return homeworkClazz.getTeacherId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass, User user) {
				if (user != null) {
					vCsHomeworkClass.setTeacherName(user.getName());
					vCsHomeworkClass.setChannelCode(user.getUserChannelCode());
				}
			}

			@Override
			public User getValue(Long aLong) {
				return userService.get(aLong);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Long, User> mgetValue(Collection<Long> collection) {
				if (CollectionUtils.isEmpty(collection)) {
					return Collections.EMPTY_MAP;
				}

				Set<Long> ids = new HashSet<Long>(collection.size());
				for (Long id : collection) {
					ids.add(id);
				}

				return userService.getUsers(ids);
			}
		});

		// 转换渠道商名
		assemblers.add(new ConverterAssembler<VCsHomeworkClass, HomeworkClazz, Integer, UserChannel>() {
			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> map) {
				return true;
			}

			@Override
			public Integer getKey(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass) {
				return vCsHomeworkClass.getChannelCode();
			}

			@Override
			        public void setValue(HomeworkClazz homeworkClazz,
			                VCsHomeworkClass vCsHomeworkClass, UserChannel userChannel) {
				        if (null != userChannel) {
					        vCsHomeworkClass.setChannelName(userChannel.getName());
				        }
			        }

			@Override
			public UserChannel getValue(Integer integer) {
				return userChannelService.get(integer);
			}

			@Override
			@SuppressWarnings("unchecked")
			public Map<Integer, UserChannel> mgetValue(Collection<Integer> collection) {
				if (CollectionUtils.isEmpty(collection)) {
					return Collections.EMPTY_MAP;
				}

				return userChannelService.mget(collection);
			}
		});

		// 所属学校名
		assemblers.add(new ConverterAssembler<VCsHomeworkClass, HomeworkClazz, Long, School>() {

			@Override
			public boolean accept(HomeworkClazz homeworkClazz) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> map) {
				return true;
			}

			@Override
			public Long getKey(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass) {
				return homeworkClazz.getTeacherId();
			}

			@Override
			public void setValue(HomeworkClazz homeworkClazz, VCsHomeworkClass vCsHomeworkClass, School s) {
				if (null != s) {
					vCsHomeworkClass.setSchoolName(s.getName());
				}
			}

			@Override
			public School getValue(Long aLong) {
				return schoolService.findByTeacherId(aLong);
			}

			@Override
			public Map<Long, School> mgetValue(Collection<Long> collection) {
				return schoolService.findByTeacherIds(collection);
			}
		});
	}
}
