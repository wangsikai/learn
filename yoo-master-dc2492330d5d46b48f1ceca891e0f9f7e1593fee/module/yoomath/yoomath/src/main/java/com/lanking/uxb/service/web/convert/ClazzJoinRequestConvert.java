package com.lanking.uxb.service.web.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.ClazzJoinRequest;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.user.convert.UserProfileConvert;
import com.lanking.uxb.service.user.value.VUserProfile;
import com.lanking.uxb.service.web.value.VClazzJoinRequest;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

@Component
public class ClazzJoinRequestConvert extends Converter<VClazzJoinRequest, ClazzJoinRequest, Long> {

	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert zyHomeworkClazzConvert;
	@Autowired
	private UserProfileConvert userConvert;

	@Override
	protected Long getId(ClazzJoinRequest s) {
		return s.getId();
	}

	@Override
	protected VClazzJoinRequest convert(ClazzJoinRequest s) {
		VClazzJoinRequest v = new VClazzJoinRequest();
		v.setRealName(s.getRealName());
		v.setRequestStatus(s.getRequestStatus());
		v.setRequestAt(s.getUpdateAt());
		v.setId(s.getId());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 获取班级信息
		assemblers.add(new ConverterAssembler<VClazzJoinRequest, ClazzJoinRequest, Long, VHomeworkClazz>() {

			@Override
			public boolean accept(ClazzJoinRequest s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ClazzJoinRequest s, VClazzJoinRequest d) {
				return s.getHomeworkClassId();
			}

			@Override
			public void setValue(ClazzJoinRequest s, VClazzJoinRequest d, VHomeworkClazz value) {
				d.setClazz(value);

			}

			@Override
			public VHomeworkClazz getValue(Long key) {
				return zyHomeworkClazzConvert.to(zyHomeworkClassService.get(key));
			}

			@Override
			public Map<Long, VHomeworkClazz> mgetValue(Collection<Long> keys) {
				return zyHomeworkClazzConvert.to(zyHomeworkClassService.mget(keys));
			}

		});

		assemblers.add(new ConverterAssembler<VClazzJoinRequest, ClazzJoinRequest, Long, VUserProfile>() {

			@Override
			public boolean accept(ClazzJoinRequest s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(ClazzJoinRequest s, VClazzJoinRequest d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(ClazzJoinRequest s, VClazzJoinRequest d, VUserProfile value) {
				d.setStudent(value);
			}

			@Override
			public VUserProfile getValue(Long key) {
				return userConvert.get(key);
			}

			@Override
			public Map<Long, VUserProfile> mgetValue(Collection<Long> keys) {
				return userConvert.mget(keys);
			}
		});

	}

}
