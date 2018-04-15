package com.lanking.uxb.rescon.basedata.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.baseData.ExaminationPoint;
import com.lanking.cloud.domain.common.baseData.ExaminationPointKnowledgePoint;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.basedata.value.ExaminationPointOption;
import com.lanking.uxb.rescon.basedata.value.VResconExaminationPoint;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.convert.ResconQuestionConvert;
import com.lanking.uxb.service.code.api.ExaminationPointKnowledgePointService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSubject;

/**
 * 考点VO转换.
 * 
 * @author wlche
 * @since v2.0.1
 */
@Component
public class ResconExaminationPointConvert extends Converter<VResconExaminationPoint, ExaminationPoint, Long> {
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;
	@Autowired
	private ResconQuestionManage questionManage;
	@Autowired
	private ResconQuestionConvert questionConvert;
	@Autowired
	private ExaminationPointKnowledgePointService examinationPointKnowledgePointService;

	@Override
	protected Long getId(ExaminationPoint s) {
		return s.getId();
	}

	@Override
	protected VResconExaminationPoint convert(ExaminationPoint s) {
		if (s == null) {
			return null;
		}
		VResconExaminationPoint v = new VResconExaminationPoint();
		v.setId(s.getId());
		v.setFrequency(s.getFrequency());
		v.setName(s.getName());
		v.setPcode(s.getPcode());
		v.setPhaseCode(s.getPhaseCode());
		v.setQuestionIds(s.getQuestions());
		v.setSubjectCode(s.getSubjectCode());
		return v;
	}

	public VResconExaminationPoint to(ExaminationPoint sp, ExaminationPointOption option) {
		sp.setHasPhase(option.isHasPhase());
		sp.setHasSubject(option.isHasSubject());
		return super.to(sp);
	}

	public List<VResconExaminationPoint> to(List<ExaminationPoint> sps, ExaminationPointOption option) {
		for (ExaminationPoint sp : sps) {
			sp.setHasPhase(option.isHasPhase());
			sp.setHasSubject(option.isHasSubject());
		}
		return super.to(sps);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 阶段
		assemblers.add(new ConverterAssembler<VResconExaminationPoint, ExaminationPoint, Integer, VPhase>() {
			@Override
			public boolean accept(ExaminationPoint s) {
				return s.isHasPhase();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExaminationPoint s, VResconExaminationPoint d) {
				return s.getPhaseCode();
			}

			@Override
			public void setValue(ExaminationPoint s, VResconExaminationPoint d, VPhase value) {
				d.setPhase(value);
			}

			@Override
			public VPhase getValue(Integer key) {
				return phaseConvert.to(phaseService.get(key));
			}

			@Override
			public Map<Integer, VPhase> mgetValue(Collection<Integer> keys) {
				return phaseConvert.to(phaseService.mget(keys));
			}
		});
		// 学科
		assemblers.add(new ConverterAssembler<VResconExaminationPoint, ExaminationPoint, Integer, VSubject>() {
			@Override
			public boolean accept(ExaminationPoint s) {
				return s.isHasSubject();
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Integer getKey(ExaminationPoint s, VResconExaminationPoint d) {
				return s.getSubjectCode();
			}

			@Override
			public void setValue(ExaminationPoint s, VResconExaminationPoint d, VSubject value) {
				d.setSubject(value);
			}

			@Override
			public VSubject getValue(Integer key) {
				return subjectConvert.to(subjectService.get(key));
			}

			@Override
			public Map<Integer, VSubject> mgetValue(Collection<Integer> keys) {
				return subjectConvert.to(subjectService.mget(keys));
			}
		});
		// 知识点关联
		assemblers
				.add(new ConverterAssembler<VResconExaminationPoint, ExaminationPoint, Long, List<ExaminationPointKnowledgePoint>>() {
					@Override
					public boolean accept(ExaminationPoint s) {
						return s.isHasSubject();
					}

					@Override
					public boolean accept(Map<String, Object> hints) {
						return true;
					}

					@Override
					public Long getKey(ExaminationPoint s, VResconExaminationPoint d) {
						return s.getId();
					}

					@Override
					public void setValue(ExaminationPoint s, VResconExaminationPoint d,
							List<ExaminationPointKnowledgePoint> value) {
						if (null != value) {
							List<Long> knowpointCodes = new ArrayList<Long>(value.size());
							for (ExaminationPointKnowledgePoint ep : value) {
								knowpointCodes.add(ep.getKnowledgePointCode());
							}
							d.setKnowpointCodes(knowpointCodes);
						}
					}

					@Override
					public List<ExaminationPointKnowledgePoint> getValue(Long key) {
						return examinationPointKnowledgePointService.findByExaminationPoint(key);
					}

					@Override
					public Map<Long, List<ExaminationPointKnowledgePoint>> mgetValue(Collection<Long> keys) {
						return examinationPointKnowledgePointService.findByExaminationPoints(keys);
					}
				});
	}
}
