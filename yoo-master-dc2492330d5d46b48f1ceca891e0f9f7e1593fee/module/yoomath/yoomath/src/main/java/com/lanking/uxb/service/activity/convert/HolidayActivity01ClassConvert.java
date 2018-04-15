package com.lanking.uxb.service.activity.convert;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.yoo.activity.holiday001.HolidayActivity01Class;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.activity.value.VHolidayActivity01Class;

@Component
public class HolidayActivity01ClassConvert extends Converter<VHolidayActivity01Class, HolidayActivity01Class, Long> {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Override
	protected Long getId(HolidayActivity01Class s) {
		return s.getId();
	}

	@Override
	protected VHolidayActivity01Class convert(HolidayActivity01Class s) {
		VHolidayActivity01Class v = new VHolidayActivity01Class();
		v.setId(s.getId());
		v.setClassId(s.getClassId());
		v.setSubmitRate(s.getSubmitRate());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 班级名
		assemblers.add(new ConverterAssembler<VHolidayActivity01Class, HolidayActivity01Class, Long, Map>() {
			@Override
			public boolean accept(HolidayActivity01Class s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(HolidayActivity01Class s, VHolidayActivity01Class d) {
				return s.getClassId();
			}

			@Override
			public void setValue(HolidayActivity01Class s, VHolidayActivity01Class d, Map value) {
				d.setName(String.valueOf(value.get("class_name")));
			}

			@Override
			public Map getValue(Long key) {
				List<Map> list = homeworkClazzRepo.find("$csQueryClassInfo", Params.param("classId", key))
						.list(Map.class);
				return list.size() == 0 ? null : list.get(0);
			}

			@Override
			public Map<Long, Map> mgetValue(Collection<Long> keys) {
				Map<Long, Map> result = new HashMap<Long, Map>();
				for (Long id : keys) {
					List<Map> list = homeworkClazzRepo.find("$csQueryClassInfo", Params.param("classId", id))
							.list(Map.class);
					if (list != null && list.size() > 0) {
						result.put(id, list.get(0));
					}
				}

				return result;
			}

		});
	}
}
