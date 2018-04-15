package com.lanking.uxb.service.zuoye.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroup;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;

/**
 * 班级组转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月17日
 */
@Component
public class ZyHomeworkClazzGroupConvert extends Converter<VHomeworkClazzGroup, HomeworkClazzGroup, Long> {

	@Override
	protected Long getId(HomeworkClazzGroup s) {
		return s.getId();
	}

	@Override
	protected VHomeworkClazzGroup convert(HomeworkClazzGroup s) {
		if (s != null) {
			VHomeworkClazzGroup v = new VHomeworkClazzGroup();
			v.setClassId(s.getClassId());
			v.setCreateAt(s.getCreateAt());
			v.setId(s.getId());
			v.setName(s.getName());
			v.setStatus(s.getStatus());
			v.setStudentCount(s.getStudentCount());
			v.setUpdateAt(s.getUpdateAt());
			return v;
		}
		return null;
	}

}
