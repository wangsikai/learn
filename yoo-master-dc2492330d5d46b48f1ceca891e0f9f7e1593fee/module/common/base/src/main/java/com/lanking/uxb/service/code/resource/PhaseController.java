package com.lanking.uxb.service.code.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SubjectConvert;
import com.lanking.uxb.service.code.value.VPhase;

/**
 * 阶段相关接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
@RestController
@RequestMapping("common/phase")
public class PhaseController {

	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectConvert subjectConvert;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "all" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value all() {
		return new Value(phaseConvert.to(phaseService.getAll()));
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "phasesSubjects" }, method = { RequestMethod.GET, RequestMethod.POST })
	public Value phasesSubjects() {
		List<VPhase> vs = phaseConvert.to(phaseService.getAll());
		for (VPhase v : vs) {
			v.setSubjects(subjectConvert.to(subjectService.findByPhaseCode(v.getCode())));
		}
		return new Value(vs);
	}
}
