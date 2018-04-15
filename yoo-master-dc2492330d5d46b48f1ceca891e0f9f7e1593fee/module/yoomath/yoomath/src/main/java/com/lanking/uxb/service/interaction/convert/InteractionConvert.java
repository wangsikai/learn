package com.lanking.uxb.service.interaction.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.interaction.Interaction;
import com.lanking.cloud.domain.yoomath.interaction.InteractionType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.interaction.value.VInteraction;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;

@Component
public class InteractionConvert extends Converter<VInteraction, Interaction, Long> {

	@Autowired
	private UserConvert userConvert;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;

	@Override
	protected Long getId(Interaction s) {
		return s.getId();
	}

	@Override
	protected VInteraction convert(Interaction s) {
		VInteraction v = new VInteraction();
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		v.setStatus(s.getStatus());
		if (s.getType() == InteractionType.ONE_HOMEWORK_TOP5) {
			v.setReason("作业排名进入班级前5名");
		} else if (s.getType() == InteractionType.CLASS_HOMEWORK_TOP5) {
			v.setReason("总排名进入班级前5名");
		} else if (s.getType() == InteractionType.MOST_IMPROVED_STU) {
			v.setReason("进步明显");
		} else if (s.getType() == InteractionType.MOST_BACKWARD_STU) {
			v.setReason("退步严重");
		} else if (s.getType() == InteractionType.SERIES_NOTSUBMIT_STU) {
			v.setReason("已连续3次没有交作业");
		}
		v.setType(s.getType());
		v.setClassId(s.getClassId());
		v.setP1(s.getP1());
		v.setP2(s.getP2());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VInteraction, Interaction, Long, VUser>() {

			@Override
			public boolean accept(Interaction s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Interaction s, VInteraction d) {
				return s.getStudentId();
			}

			@Override
			public void setValue(Interaction s, VInteraction d, VUser value) {
				d.setStudentName(value.getName());
				if (Security.getUserType() == UserType.STUDENT) {
					d.setReason(value.getName() + "同学，因" + d.getReason() + ",获得小红花一朵。");
					if (s.getUpdateAt() != null) {
						d.setCreateAt(s.getUpdateAt());
					}
				}
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

		assemblers.add(new ConverterAssembler<VInteraction, Interaction, Long, HomeworkClazz>() {

			@Override
			public boolean accept(Interaction s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Interaction s, VInteraction d) {
				return s.getClassId();
			}

			@Override
			public void setValue(Interaction s, VInteraction d, HomeworkClazz value) {
				d.setClassName(value.getName());
			}

			@Override
			public HomeworkClazz getValue(Long key) {
				return zyHomeworkClassService.get(key);
			}

			@Override
			public Map<Long, HomeworkClazz> mgetValue(Collection<Long> keys) {
				return zyHomeworkClassService.mget(keys);
			}

		});

	}
}
