package com.lanking.uxb.service.zuoye.convert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazzGroupStudent;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroup;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazzGroupStudent;

/**
 * 学生组转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2017年3月20日
 */
@Component
public class ZyHomeworkClazzGroupStudentConvert extends
		Converter<VHomeworkClazzGroupStudent, HomeworkClazzGroupStudent, Long> {

	@Override
	protected Long getId(HomeworkClazzGroupStudent s) {
		return s.getId();
	}

	@Override
	protected VHomeworkClazzGroupStudent convert(HomeworkClazzGroupStudent s) {
		if (s != null) {
			VHomeworkClazzGroupStudent v = new VHomeworkClazzGroupStudent();
			v.setClassId(s.getClassId());
			v.setCreateAt(s.getCreateAt());
			v.setGroupId(s.getGroupId());
			v.setId(s.getId());
			v.setStudentId(s.getStudentId());
			v.setUpdateAt(s.getUpdateAt());
			return v;
		}
		return null;
	}

	public List<VHomeworkClazzGroupStudent> to(List<HomeworkClazzGroupStudent> ss, List<VHomeworkClazzGroup> groups) {
		List<VHomeworkClazzGroupStudent> vs = super.to(ss);
		if (CollectionUtils.isNotEmpty(groups)) {
			Map<Long, VHomeworkClazzGroup> groupMap = new HashMap<Long, VHomeworkClazzGroup>(groups.size());
			for (VHomeworkClazzGroup group : groups) {
				groupMap.put(group.getId(), group);
			}
			for (VHomeworkClazzGroupStudent v : vs) {
				v.setGroup(groupMap.get(v.getGroupId()));
			}
		}
		return vs;
	}
}
