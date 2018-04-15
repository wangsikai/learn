package com.lanking.uxb.service.zuoye.convert;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.MetaKnowpoint;
import com.lanking.cloud.domain.common.baseData.MetaKnowpointMap;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.zuoye.api.ZyMetaKnowpointMapService;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseKnowpointService;
import com.lanking.uxb.service.zuoye.value.VStudentExerciseKnowpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author xinyu.zhou
 * @since yoomath V1.6
 */
@Component
public class ZyStudentExerciseKnowpointConvert extends Converter<VStudentExerciseKnowpoint, MetaKnowpoint, Integer> {
	@Autowired
	private ZyStudentExerciseKnowpointService knowpointService;
	@Autowired
	private ZyMetaKnowpointMapService metaKnowpointMapService;

	@Override
	protected Integer getId(MetaKnowpoint metaKnowpoint) {
		return metaKnowpoint.getCode();
	}

	@Override
	protected VStudentExerciseKnowpoint convert(MetaKnowpoint metaKnowpoint) {
		VStudentExerciseKnowpoint v = new VStudentExerciseKnowpoint();
		v.setCode(metaKnowpoint.getCode());
		v.setName(metaKnowpoint.getName());
		v.setSubjectCode(v.getSubjectCode());
		return v;
	}

	/**
	 * 组装数据
	 *
	 * @param codes
	 *            知识点code列表
	 * @param points
	 *            {@link VStudentExerciseKnowpoint}
	 * @param studentId
	 *            学生id
	 */
	public void assembleData(List<Integer> codes, List<VStudentExerciseKnowpoint> points, long studentId) {
		Map<Integer, StudentExerciseKnowpoint> ekMap = knowpointService.mgetByCodes(codes, studentId);
		for (VStudentExerciseKnowpoint v : points) {
			StudentExerciseKnowpoint value = ekMap.get(v.getCode());

			if (value == null) {
				v.setDoCount(0);
				v.setWrongCount(0);
				v.setRightRate(0);
			} else {
				v.setDoCount(value.getDoCount());
				v.setId(value.getId());
				v.setStudentId(value.getStudentId());
				v.setWrongCount(value.getWrongCount());

				// 做过的题数量>20
				double rightRate;
				if (v.getDoCount() > 20) {
					rightRate = ((v.getDoCount() - v.getWrongCount()) * 100d) / v.getDoCount();
				} else {
					rightRate = ((v.getDoCount() - v.getWrongCount()) * 100d) / 20;
				}

				v.setRightRate(rightRate);
				v.setRightRateTitle(Math.round(rightRate) + "%");
			}
		}

	}

	/**
	 * 根据班级来进行组装数据
	 *
	 * @param codes
	 *            知识点编码列表
	 * @param points
	 *            知识点
	 * @param classId
	 *            班级id
	 */
	public void assembleDataByClassId(List<Integer> codes, List<VStudentExerciseKnowpoint> points, long classId) {
		Map<Integer, Double> codeCompleteRate = Maps.newHashMap();
		Map<Integer, List<StudentExerciseKnowpoint>> pointMap = knowpointService.getByCodeAndClass(codes, classId);

		for (Map.Entry<Integer, List<StudentExerciseKnowpoint>> entry : pointMap.entrySet()) {
			if (CollectionUtils.isNotEmpty(entry.getValue())) {
				double completeRate = 0;
				for (StudentExerciseKnowpoint k : entry.getValue()) {
					if (k.getDoCount() > 20) {
						completeRate += ((k.getDoCount() - k.getWrongCount()) * 100d) / k.getDoCount();
					} else {
						completeRate += ((k.getDoCount() - k.getWrongCount()) * 100d) / 20;
					}
				}

				completeRate = completeRate / entry.getValue().size();

				codeCompleteRate.put(entry.getKey(), completeRate);
			}
		}
		for (VStudentExerciseKnowpoint v : points) {
			if (codeCompleteRate.get(v.getCode()) != null) {
				v.setRightRate(codeCompleteRate.get(v.getCode()));
				v.setRightRateTitle(Math.round(v.getRightRate()) + "%");
			}
		}
	}

	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		assemblers.add(new ConverterAssembler<VStudentExerciseKnowpoint, MetaKnowpoint, Integer, List<Integer>>() {

			@Override
			public boolean accept(MetaKnowpoint metaKnowpoint) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(MetaKnowpoint metaKnowpoint, VStudentExerciseKnowpoint vStudentExerciseKnowpoint) {
				return metaKnowpoint.getCode();
			}

			@Override
			public void setValue(MetaKnowpoint metaKnowpoint, VStudentExerciseKnowpoint vStudentExerciseKnowpoint,
					List<Integer> value) {
				vStudentExerciseKnowpoint.setMaps(value);
			}

			@Override
			public List<Integer> getValue(Integer key) {
				List<MetaKnowpointMap> maps = metaKnowpointMapService.findByParent(key);
				List<Integer> codes = new ArrayList<Integer>(maps.size());
				for (MetaKnowpointMap m : maps) {
					codes.add(m.getMetaCode());
				}
				return codes;
			}

			@Override
			public Map<Integer, List<Integer>> mgetValue(Collection<Integer> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return Maps.newHashMap();
				}
				List<MetaKnowpointMap> mapList = metaKnowpointMapService.findByParents(keys);
				Map<Integer, List<Integer>> map = Maps.newHashMap();
				for (MetaKnowpointMap m : mapList) {
					if (map.get(m.getPcode()) == null) {
						map.put(m.getPcode(), Lists.<Integer> newArrayList());
					}

					map.get(m.getPcode()).add(m.getMetaCode());
				}
				return map;
			}
		});
	}
}
