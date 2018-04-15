package com.lanking.uxb.service.diagnostic.resource;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgePoint;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassKnowpoint;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.KnowledgePointService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.ParameterService;
import com.lanking.uxb.service.code.convert.KnowledgeSystemConvert;
import com.lanking.uxb.service.code.value.VKnowledgeSystem;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassKnowpointConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticKnowledgeSystemConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassKnowpoint;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticKnowledgeSystem;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;

/**
 * 教师班级整体掌握情况controller
 *
 * @author xinyu.zhou
 * @since 2.1.0
 * @since 2.3.1 修改教师教学诊断相关数据接口
 */
@RestController
@RequestMapping(value = "zy/t/dia/kp")
public class ZyTeaDiagnosticKpController {
	@Autowired
	private KnowledgePointService knowledgePointService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;
	@Autowired
	private DiagnosticClassKnowpointService diagnosticClassKnowpointService;
	@Autowired
	private DiagnosticClassKnowpointConvert diagnosticClassKnowpointConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private DiagnosticKnowledgeSystemConvert knowledgeSystemConvert;
	@Autowired
	private KnowledgeSystemConvert ksConvert;
	@Autowired
	private KnowledgeSystemService ksService;
	@Autowired
	private ParameterService parameterService;

	/**
	 * 查询知识图谱数据
	 *
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryPointMap", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryPointMap(long classId) {
		Map<String, Object> retMap = new HashMap<String, Object>();
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		Integer subjectCode = teacher.getSubjectCode();
		if (subjectCode == null) {
			return new Value(new IllegalArgException());
		}
		List<KnowledgeSystem> systems = knowledgeSystemService.getBySubjectAndLevel(3, teacher.getSubjectCode());
		List<VDiagnosticKnowledgeSystem> vs = knowledgeSystemConvert.to(systems);

		List<Long> codes = new ArrayList<Long>(systems.size());
		for (KnowledgeSystem s : systems) {
			codes.add(s.getCode());
		}

		List<DiagnosticClassKnowpoint> points = diagnosticClassKnowpointService.findByCodesAndClass(classId, codes);
		if (CollectionUtils.isNotEmpty(points)) {
			List<VDiagnosticClassKnowpoint> vClassPoints = diagnosticClassKnowpointConvert.to(points);
			Map<Long, VDiagnosticClassKnowpoint> pointMap = new HashMap<Long, VDiagnosticClassKnowpoint>(
					vClassPoints.size());
			for (VDiagnosticClassKnowpoint vck : vClassPoints) {
				pointMap.put(vck.getKnowledgeCode(), vck);
			}

			for (VDiagnosticKnowledgeSystem v : vs) {
				v.setHasData(pointMap.get(v.getCode()) != null);
			}

			retMap.put("classPoints", pointMap);
		}

		retMap.put("maps", vs);

		// 查询全局薄弱知识点数据
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -7);
		Date bt = cal.getTime();

		// 最近一周按指定日期后的时间进行查看
		Parameter parameter = parameterService.get(Product.YOOMATH, "diagno.tea-stu.weak7");
		if (parameter != null) {
			String timestr = parameter.getValue();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			try {
				Date time = format.parse(timestr);
				if (time.compareTo(bt) > 0) {
					bt = time;
				}
			} catch (Exception e) {
			}
		}

		List<VDiagnosticClassKnowpoint> vcks = diagnosticClassKnowpointConvert
				.to(diagnosticClassKnowpointService.findWeakDatas(classId, Integer.MAX_VALUE, bt));
		retMap.put("weakDatas", vcks);

		return new Value(retMap);
	}

	/**
	 * 获得小知识专项数据及其子知识点数据
	 *
	 * @param code
	 *            小知识专项代码
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "getKps", method = { RequestMethod.GET, RequestMethod.POST })
	public Value getKps(long code, long classId) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		List<DiagnosticClassKnowpoint> list = diagnosticClassKnowpointService.findByCodesAndClass(classId,
				Lists.newArrayList(code));
		if (CollectionUtils.isNotEmpty(list)) {
			VDiagnosticClassKnowpoint getPoint = diagnosticClassKnowpointConvert.to(list.get(0));
			VDiagnosticClassKnowpoint parentPoint = diagnosticClassKnowpointConvert.to(diagnosticClassKnowpointService
					.findByCodesAndClass(classId, Lists.<Long>newArrayList(getPoint.getParentCode()))).get(0);
			retMap.put("getKp", getPoint);
			retMap.put("pKp", parentPoint);
		} else {
			// 未学习
			VKnowledgeSystem v = ksConvert.to(ksService.get(code));
			retMap.put("getKp", v);
			retMap.put("pKp", ksConvert.to(ksService.get(v.getPcode())));
			return new Value(retMap);
		}

		List<KnowledgePoint> points = knowledgePointService.findByPcode(code);
		List<Long> codes = new ArrayList<Long>(points.size());

		for (KnowledgePoint p : points) {
			codes.add(p.getCode());
		}

		List<VDiagnosticClassKnowpoint> vs = diagnosticClassKnowpointConvert
				.to(diagnosticClassKnowpointService.findByCodesAndClass(classId, codes));

		retMap.put("kps", vs);

		return new Value(retMap);
	}

	/**
	 * 获取班级知识弱项.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "findKnowpointWeakDatas", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findKnowpointWeakDatas(Long classId, Integer days) {
		if (classId == null || days == null) {
			throw new MissingArgumentException();
		}
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -days);
		Date bt = days <= 0 ? null : cal.getTime();
		if (days == 7) {
			// 最近一周按指定日期后的时间进行查看
			Parameter parameter = parameterService.get(Product.YOOMATH, "diagno.tea-stu.weak7");
			if (parameter != null) {
				String timestr = parameter.getValue();
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					Date time = format.parse(timestr);
					if (time.compareTo(bt) > 0) {
						bt = time;
					}
				} catch (Exception e) {
				}
			}
		}

		List<DiagnosticClassKnowpoint> list = diagnosticClassKnowpointService.findWeakDatas(classId, Integer.MAX_VALUE,
				bt);
		List<VDiagnosticClassKnowpoint> vcks = diagnosticClassKnowpointConvert.to(list);
		return new Value(vcks);
	}

	/**
	 * 获取弱项知识点树.
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "findKnowpointWeakTree", method = { RequestMethod.GET, RequestMethod.POST })
	public Value findKnowpointWeakTree(Long classId) {
		if (classId == null) {
			throw new MissingArgumentException();
		}
		Map<String, Object> retMap = new HashMap<String, Object>(1);
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());

		// 所有的弱项数据集合
		List<DiagnosticClassKnowpoint> points = diagnosticClassKnowpointService.findAllByWeakDatas(classId);
		Map<Long, VDiagnosticClassKnowpoint> pointMap = new HashMap<Long, VDiagnosticClassKnowpoint>(points.size());
		if (CollectionUtils.isNotEmpty(points)) {
			List<VDiagnosticClassKnowpoint> vClassPoints = diagnosticClassKnowpointConvert.to(points);

			for (VDiagnosticClassKnowpoint vck : vClassPoints) {
				if (vck.getDoCount() > 0) {
					vck.setMasterRate(new BigDecimal((double) (vck.getRightCount() + 1) / (vck.getDoCount() + 2)));
				}
				pointMap.put(vck.getKnowledgeCode(), vck);
			}
		}

		// 所有的新知识点末级
		List<KnowledgePoint> allKnowledgePoint = knowledgePointService.findAll(null);
		Map<Long, List<VDiagnosticClassKnowpoint>> parentLeafs = new HashMap<Long, List<VDiagnosticClassKnowpoint>>();
		for (KnowledgePoint knowledgePoint : allKnowledgePoint) {
			List<VDiagnosticClassKnowpoint> leafs = parentLeafs.get(knowledgePoint.getPcode());
			if (leafs == null) {
				leafs = new ArrayList<VDiagnosticClassKnowpoint>();
				parentLeafs.put(knowledgePoint.getPcode(), leafs);
			}
			VDiagnosticClassKnowpoint vdck = pointMap.get(knowledgePoint.getCode());
			if (vdck == null) {
				// 未学习的知识点
				vdck = new VDiagnosticClassKnowpoint();
				vdck.setKnowledgeCode(knowledgePoint.getCode());
				vdck.setKnowpointName(knowledgePoint.getName());
			}
			if (vdck.getDoCount() > 0) {
				vdck.setMasterRate(new BigDecimal((double) (vdck.getRightCount() + 1) / (vdck.getDoCount() + 2)));
			}
			leafs.add(vdck);
		}

		// 列表树状结构
		List<KnowledgeSystem> systemsAll = knowledgeSystemService.findAllBySubject(teacher.getSubjectCode());
		List<VDiagnosticClassKnowpoint> pointTree = this.assembleTree(systemsAll);
		for (VDiagnosticClassKnowpoint v : pointTree) {
			VDiagnosticClassKnowpoint tmp = pointMap.get(v.getKnowledgeCode());
			if (tmp != null) {
				v.setMasterRate(tmp.getMasterRate());
			}
			for (VDiagnosticClassKnowpoint vv : v.getChildren()) {
				VDiagnosticClassKnowpoint tmp1 = pointMap.get(vv.getKnowledgeCode());
				if (tmp1 != null) {
					vv.setMasterRate(tmp1.getMasterRate());
				}
				for (VDiagnosticClassKnowpoint vvv : vv.getChildren()) {
					VDiagnosticClassKnowpoint tmp2 = pointMap.get(vvv.getKnowledgeCode());
					if (tmp2 != null) {
						vvv.setAvgDifficulty(tmp2.getAvgDifficulty());
						vvv.setDoCount(tmp2.getDoCount());
						vvv.setDoHard1Count(tmp2.getDoHard1Count());
						vvv.setDoHard2Count(tmp2.getDoHard2Count());
						vvv.setDoHard3Count(tmp2.getDoHard3Count());
						vvv.setMasterRate(tmp2.getMasterRate());
						vvv.setMaxDifficulty(tmp2.getMaxDifficulty());
						vvv.setMinDifficulty(tmp2.getMinDifficulty());
						vvv.setRightCount(tmp2.getRightCount());
						vvv.setRightHard1Count(tmp2.getRightHard1Count());
						vvv.setRightHard2Count(tmp2.getRightHard2Count());
						vvv.setRightHard3Count(tmp2.getRightHard3Count());
						vvv.setRightRate(tmp2.getRightRate());
						vvv.setRightRateTitle(tmp2.getRightRateTitle());
						vvv.setTopnRightRate(tmp2.getTopnRightRate());
						vvv.setTopnRightRateTitle(tmp2.getTopnRightRateTitle());
					}

					// 填充末级节点数据
					List<VDiagnosticClassKnowpoint> v4s = parentLeafs.get(vvv.getKnowledgeCode());
					vvv.setChildren(new ArrayList<VDiagnosticClassKnowpoint>());
					if (v4s != null) {
						boolean isLearn = false;
						for (VDiagnosticClassKnowpoint vdck : v4s) {
							if (vdck.getDoCount() > 0) {
								isLearn = true;
								break;
							}
						}
						if (isLearn) {
							vvv.setChildren(v4s);
						}
					}
				}
			}
		}
		retMap.put("pointTree", pointTree == null ? Lists.newArrayList() : pointTree);

		return new Value(retMap);
	}

	/**
	 * 组装树型
	 *
	 * @param from
	 *            {@link List}
	 * @return {@link List}
	 */
	private List<VDiagnosticClassKnowpoint> assembleTree(List<KnowledgeSystem> from) {
		List<VDiagnosticClassKnowpoint> retList = Lists.newArrayList();
		for (KnowledgeSystem v : from) {
			assemble(v, retList);
		}
		return retList;
	}

	/**
	 * 对树型结构进行组装
	 *
	 * @param v
	 *            {@link VKnowledgeSystem}
	 * @param retList
	 *            {@link List}
	 */
	private void assemble(KnowledgeSystem v, List<VDiagnosticClassKnowpoint> retList) {
		if (v.getLevel() == 1) {
			VDiagnosticClassKnowpoint vk = new VDiagnosticClassKnowpoint();
			vk.setKnowledgeCode(v.getCode());
			vk.setKnowpointName(v.getName());
			vk.setParentCode(v.getPcode());
			vk.setChildren(new ArrayList<VDiagnosticClassKnowpoint>());
			retList.add(vk);
		} else {
			for (VDiagnosticClassKnowpoint s : retList) {
				if (s.getKnowledgeCode() == v.getPcode().longValue()) {
					VDiagnosticClassKnowpoint vk = new VDiagnosticClassKnowpoint();
					vk.setKnowledgeCode(v.getCode());
					vk.setKnowpointName(v.getName());
					vk.setParentCode(v.getPcode());
					vk.setChildren(new ArrayList<VDiagnosticClassKnowpoint>());
					s.getChildren().add(vk);
				} else {
					this.assemble(v, s.getChildren());
				}
			}
		}
	}
}
