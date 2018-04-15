package com.lanking.uxb.service.teachersDay01.convert;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01Tag;
import com.lanking.cloud.domain.yoo.activity.teachersDay01.TeachersDayActiviy01TeacherTag;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.service.teachersDay01.api.TeachersDayActiviy01TagService;
import com.lanking.uxb.service.teachersDay01.value.VTeachersDayActiviy01TeacherTag;

@Component
public class TeachersDayActiviy01TeacherTagConvert
		extends Converter<VTeachersDayActiviy01TeacherTag, TeachersDayActiviy01TeacherTag, Long> {

	@Autowired
	private TeachersDayActiviy01TagService tagService;

	@Override
	protected VTeachersDayActiviy01TeacherTag convert(TeachersDayActiviy01TeacherTag value) {
		VTeachersDayActiviy01TeacherTag v = new VTeachersDayActiviy01TeacherTag();
		v.setCodeTag(value.getCodeTag());
		v.setId(value.getId());
		v.setNum(value.getNum());

		return v;
	}

	public List<VTeachersDayActiviy01TeacherTag> sort(List<VTeachersDayActiviy01TeacherTag> vtags) {
		Collections.sort(vtags, new Comparator<VTeachersDayActiviy01TeacherTag>() {
			@Override
			public int compare(VTeachersDayActiviy01TeacherTag v1, VTeachersDayActiviy01TeacherTag v2) {
				if (v1.getNum() == v2.getNum()) {
					return new Long(v1.getCodeTag() - v2.getCodeTag()).intValue();
				}
				return 0;
			}
		});

		return vtags;
	}

	@Override
	protected Long getId(TeachersDayActiviy01TeacherTag value) {
		return value.getId();
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(
				new ConverterAssembler<VTeachersDayActiviy01TeacherTag, TeachersDayActiviy01TeacherTag, Long, String>() {

					@Override
					public boolean accept(TeachersDayActiviy01TeacherTag arg0) {
						return true;
					}

					@Override
					public boolean accept(Map<String, Object> arg0) {
						return true;
					}

					@Override
					public Long getKey(TeachersDayActiviy01TeacherTag v1, VTeachersDayActiviy01TeacherTag v2) {
						return v1.getCodeTag();
					}

					@Override
					public String getValue(Long codeTag) {
						TeachersDayActiviy01Tag v = tagService.get(codeTag);
						Optional<TeachersDayActiviy01Tag> optional = Optional.ofNullable(v);
						Optional<String> name = optional.map(TeachersDayActiviy01Tag::getName);
						return name.orElse(null);
					}

					@Override
					public Map<Long, String> mgetValue(Collection<Long> value) {
						Map<Long, String> data = new HashMap<>();
						Map<Long, TeachersDayActiviy01Tag> maps = tagService.mget(value);
						for (Map.Entry<Long, TeachersDayActiviy01Tag> entry : maps.entrySet()) {
							data.put(entry.getKey(), entry.getValue().getName());
						}
						return data;
					}

					@Override
					public void setValue(TeachersDayActiviy01TeacherTag v, VTeachersDayActiviy01TeacherTag d,
							String value) {
						d.setTagName(value);
					}

				});
	}
}
