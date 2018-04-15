package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.resource.card.KnowpointCard;
import com.lanking.cloud.domain.yoomath.homework.Homework;
import com.lanking.cloud.domain.yoomath.stat.StudentExerciseKnowpoint;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowpointService;
import com.lanking.uxb.service.code.convert.KnowpointConvert;
import com.lanking.uxb.service.code.value.VMetaKnowpoint;
import com.lanking.uxb.service.resources.api.HomeworkService;
import com.lanking.uxb.service.resources.convert.HomeworkConvert;
import com.lanking.uxb.service.resources.convert.HomeworkConvertOption;
import com.lanking.uxb.service.resources.value.VHomework;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyKnowpointLearnService;
import com.lanking.uxb.service.zuoye.api.ZyStudentExerciseKnowpointService;
import com.lanking.uxb.service.zuoye.convert.ZyKnowpointCardConvert;

/**
 * 知识点学习相关.
 * 
 * @since 2.0.0
 * @author wlche
 *
 */
@RestController
@RequestMapping(value = "zy/kplearn")
public class ZyKnowpointLearnController {
	@Autowired
	private KnowpointService knowpointService;
	@Autowired
	private KnowpointConvert knowpointConvert;
	@Autowired
	private ZyKnowpointLearnService knowpointLearnService;
	@Autowired
	private ZyKnowpointCardConvert knowpointCardConvert;
	@Autowired
	private HomeworkService komeworkService;
	@Autowired
	private HomeworkConvert homeworkConvert;
	@Autowired
	private ZyStudentExerciseKnowpointService exerciseknowpointService;

	/**
	 * 首页数据.
	 * 
	 * @param homeworkId
	 *            作业ID
	 * @param isWeak
	 *            弱项知识点标记
	 * @since 2.0.0
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index(Long homeworkId, Boolean isWeak) {
		if (null == homeworkId) {
			return new Value(new MissingArgumentException());
		}
		Map<String, Object> dataMap = new HashMap<String, Object>(2);
		isWeak = isWeak == null ? false : isWeak;
		Homework homework = komeworkService.get(homeworkId);
		VHomework vHomework = homeworkConvert.to(homework, new HomeworkConvertOption());
		List<VMetaKnowpoint> metaKnowpoints = vHomework.getMetaKnowpoints();
		List<VMetaKnowpoint> knowpoints = new ArrayList<VMetaKnowpoint>(metaKnowpoints.size());
		if (isWeak) {
			// 弱项知识点
			Set<Integer> codes = new HashSet<Integer>(metaKnowpoints.size());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				codes.add(metaKnowpoint.getCode());
			}
			Map<Integer, StudentExerciseKnowpoint> knowpointMap = exerciseknowpointService.mgetByCodes(codes,
					Security.getUserId());
			for (VMetaKnowpoint metaKnowpoint : metaKnowpoints) {
				StudentExerciseKnowpoint studentExerciseKnowpoint = knowpointMap.get(metaKnowpoint.getCode());
				if (studentExerciseKnowpoint != null && studentExerciseKnowpoint.getDoCount() > 0) {
					// 做过的题数量>20
					double rightRate;
					if (studentExerciseKnowpoint.getDoCount() > 20) {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount()) * 100d)
								/ studentExerciseKnowpoint.getDoCount();
					} else {
						rightRate = ((studentExerciseKnowpoint.getDoCount() - studentExerciseKnowpoint.getWrongCount()) * 100d) / 20;
					}
					if (rightRate <= 50) {
						knowpoints.add(metaKnowpoint);
					}
				}
			}
			dataMap.put("knowpoints", knowpoints);
		} else {
			knowpoints = metaKnowpoints;
			dataMap.put("knowpoints", knowpoints);
		}

		if (knowpoints.size() > 0) {
			VMetaKnowpoint firstKnowpoint = knowpoints.get(0);
			KnowpointCard knowpointCard = knowpointLearnService.getByCode(firstKnowpoint.getCode());
			dataMap.put("knowpointCard", knowpointCardConvert.to(knowpointCard));
		}

		return new Value(dataMap);
	}

	/**
	 * 获取知识点卡片数据.
	 * 
	 * @param code
	 *            知识点CODE
	 * @since 2.0.0
	 * @return
	 */
	@RequestMapping(value = "getKnowpointCard", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKnowpointCard(Integer code) {
		if (null == code || code < 1) {
			return new Value(new MissingArgumentException());
		}
		// code = 202111116;
		KnowpointCard knowpointCard = knowpointLearnService.getByCode(code);
		if (knowpointCard != null) {
			return new Value(knowpointCardConvert.to(knowpointCard));
		}
		return new Value();
	}
}
