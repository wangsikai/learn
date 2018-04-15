package com.lanking.uxb.service.diagnostic.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.diagnostic.Diagnostic;
import com.lanking.cloud.domain.yoomath.diagnostic.DiagnosticClassTopnKnowpoint;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.security.api.SecurityContext;
import com.lanking.uxb.service.code.api.KnowledgeSectionService;
import com.lanking.uxb.service.code.api.KnowledgeSystemService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassLatestHomeworkService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTextbookService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticClassTopnKnowpointService;
import com.lanking.uxb.service.diagnostic.api.DiagnosticService;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassKnowpointConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassLatestHomeworkConvert;
import com.lanking.uxb.service.diagnostic.convert.DiagnosticClassLatestHomeworkKnowpointConvert;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClass;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassKnowpoint;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomework;
import com.lanking.uxb.service.diagnostic.value.VDiagnosticClassLatestHomeworkKnowpoint;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.UserService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClassConvertOption;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;

/**
 * 班级整体情况Controller
 *
 * @author xinyu.zhou
 * @since 2.1.0
 */
@RestController
@RequestMapping(value = "zy/t/dia/class")
public class ZyTeaDiagnosticClassController {
	@Autowired
	private ZyHomeworkClassService homeworkClassService;
	@Autowired
	private ZyHomeworkClazzConvert homeworkClazzConvert;
	@Autowired
	private DiagnosticClassLatestHomeworkService latestHomeworkService;
	@Autowired
	private DiagnosticClassLatestHomeworkConvert latestHomeworkConvert;
	@Autowired
	private DiagnosticClassLatestHomeworkKnowpointService latestHomeworkKnowpointService;
	@Autowired
	private DiagnosticClassLatestHomeworkKnowpointConvert latestHomeworkKnowpointConvert;
	@Autowired
	private DiagnosticClassTextbookService classTextbookService;
	@Autowired
	private DiagnosticClassKnowpointService diagnosticClassKnowpointService;
	@Autowired
	private DiagnosticClassKnowpointConvert diagnosticClassKnowpointConvert;
	@Autowired
	private DiagnosticClassService diagnosticClassService;
	@Autowired
	private DiagnosticClassConvert diagnosticClassConvert;
	@Autowired
	private DiagnosticService diagnosticService;
	@Autowired
	private DiagnosticClassTopnKnowpointService diagnosticClassTopnKnowpointService;
	@Autowired
	private TextbookCategoryService textbookCategoryService;
	@Autowired
	private TextbookCategoryConvert textbookCategoryConvert;
	@Autowired
	private TextbookConvert textbookConvert;
	@Autowired
	private UserService userService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private KnowledgeSectionService knowledgeSectionService;
	@Autowired
	private KnowledgeSystemService knowledgeSystemService;

	/**
	 * 首次进来调用本接口获得全部相关设置<br/>
	 * 并返回当前第一个班级的30天数据情况
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed
	@RequestMapping(value = "index", method = { RequestMethod.GET, RequestMethod.POST })
	public Value index() {
		Teacher teacher = (Teacher) userService.getUser(UserType.TEACHER, Security.getUserId());
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		if (teacher.getTextbookCategoryCode() == null) {
			throw new IllegalArgException();
		}

		List<HomeworkClazz> clazzs = homeworkClassService.listCurrentClazzs(Security.getUserId());
		if (clazzs == null || clazzs.size() <= 0) {
			return new Value();
		}
		retMap.put("clazzs", homeworkClazzConvert.to(clazzs, new ZyHomeworkClassConvertOption(false, true, false)));
		retMap.put("category",
				textbookCategoryConvert.to(textbookCategoryService.get(teacher.getTextbookCategoryCode())));

		MemberType memberType = SecurityContext.getMemberType();
		if (memberType != MemberType.NONE) {
			Long classId = clazzs.get(0).getId();

			retMap.put("textbooks", textbookConvert
					.mgetList(classTextbookService.getClassTextbooks(classId, teacher.getTextbookCategoryCode())));
		}

		return new Value(retMap);
	}

	/**
	 * 切换班级时查询对应教材等
	 *
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryClass", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClass(long classId, int textbookCategory, @RequestParam(defaultValue = "30") Integer times) {
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		// 新增首页显示最近7,15,30次作业
		retMap.put("latestHk", latestHomeworkConvert.to(latestHomeworkService.findByClassId(classId, times)));
		MemberType memberType = SecurityContext.getMemberType();
		if (memberType != MemberType.NONE) {
			retMap.put("latestKp",
					latestHomeworkKnowpointConvert.to(latestHomeworkKnowpointService.findByPage(classId, times, 6)));
			retMap.put("textbooks",
					textbookConvert.mgetList(classTextbookService.getClassTextbooks(classId, textbookCategory)));
		}

		return new Value(retMap);
	}

	/**
	 * 查看班级正确率、提交率、薄弱知识点
	 *
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */
	@MemberAllowed
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryClass2", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClass2(long classId, @RequestParam(defaultValue = "30") Integer times) {
		Map<String, Object> retMap = new HashMap<String, Object>(2);
		List<VDiagnosticClassLatestHomework> list = latestHomeworkConvert
				.to(latestHomeworkService.findByClassId(classId, times));
		List<Long> homeworkIds = new ArrayList<Long>();
		for (VDiagnosticClassLatestHomework v : list) {
			homeworkIds.add(v.getHomeworkId());
		}
		Map<Long, Double> submitRateMap = latestHomeworkService.getSubmitRate(homeworkIds);
		for (VDiagnosticClassLatestHomework v : list) {
			v.setSubmitRate(BigDecimal.valueOf(submitRateMap.get(v.getHomeworkId())));
		}
		// 新增首页显示最近7,15,30次作业
		retMap.put("latestHk", list);
		List<VDiagnosticClassLatestHomeworkKnowpoint> latestKp = latestHomeworkKnowpointConvert
				.to(latestHomeworkKnowpointService.findByPage(classId, times, 4));
		if (CollectionUtils.isNotEmpty(latestKp)) {
			retMap.put("latestKp",
					latestHomeworkKnowpointConvert.to(latestHomeworkKnowpointService.findByPage(classId, times, 4)));
		}
		return new Value(retMap);
	}

	/**
	 * 查询: 1. 一个班级一个教材下的章节(第一层)目录.<br/>
	 * 2. 第一章节下各知识点掌握情况<br/>
	 * 3. 分析与教学建议<br/>
	 *
	 * @since 2.6.0 只有VIP或者SCHOOL_VIP才可以访问此接口
	 *
	 * @param textbookCode
	 *            教材码
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryTextbookStat", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryTextbookStat(int textbookCode, long classId) {
		Map<String, Object> retMap = new HashMap<String, Object>(3);
		List<Section> sections = sectionService.findByTextbookCode(textbookCode, 1);

		retMap.put("sections", sectionConvert.to(sections));

		Map<Long, List<VDiagnosticClassKnowpoint>> pointMap = new HashMap<Long, List<VDiagnosticClassKnowpoint>>(
				sections.size());

		for (Section s : sections) {
			List<Long> codes = knowledgeSectionService.getBySection(s.getCode());
			if (CollectionUtils.isEmpty(codes)) {
				continue;
			}

			List<VDiagnosticClassKnowpoint> vs = diagnosticClassKnowpointConvert
					.to(diagnosticClassKnowpointService.findByCodesAndClass(classId, codes));
			pointMap.put(s.getCode(), vs);
		}

		retMap.put("sectionKpDatas", pointMap);

		VDiagnosticClass v = diagnosticClassConvert.to(diagnosticClassService.getByTextbook(textbookCode, classId));

		// 极端情况: 全国并没有一个班在此教材下布置过作业,因全国统计是延时
		Diagnostic diagnostic = diagnosticService.findByTextbook(textbookCode);
		if (v == null) {
			v = new VDiagnosticClass();
			v.setDoHard1RightRate(new BigDecimal(0));
			v.setDoHard2RightRate(new BigDecimal(0));
			v.setDoHard3RightRate(new BigDecimal(0));
		}
		v.setAllDoCount(diagnostic == null ? 0 : diagnostic.getClassMonthDoCount());

		retMap.put("classStat", v);

		return new Value(retMap);
	}

	/**
	 * 班级整体技术知识图谱
	 *
	 * @since 2.6.0 -> 只有VIP或者SCHOOL_VIP可以访问此接口
	 *
	 * @param textbookCode
	 *            教材码
	 * @param classId
	 *            班级id
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "TEACHER" })
	@MemberAllowed(memberType = "VIP")
	@RequestMapping(value = "queryClassKps", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryClassKps(int textbookCode, long classId) {
		Map<String, Object> retMap = new HashMap<String, Object>(1);

		List<Long> allCodes = knowledgeSectionService.getByTextbook(textbookCode);
		allCodes.addAll(knowledgeSystemService.findAllCodeByKPoint(allCodes));
		List<VDiagnosticClassKnowpoint> points = diagnosticClassKnowpointConvert
				.to(diagnosticClassKnowpointService.findByCodesAndClass(classId, allCodes));

		Map<Long, DiagnosticClassTopnKnowpoint> topnKnowpoints = diagnosticClassTopnKnowpointService.query(classId,
				allCodes);
		if (CollectionUtils.isNotEmpty(points)) {
			for (VDiagnosticClassKnowpoint v : points) {
				if (topnKnowpoints.get(v.getKnowledgeCode()) != null) {
					v.setTopnRightRate(topnKnowpoints.get(v.getKnowledgeCode()).getRightRate());
					v.setTopnRightRateTitle(v.getTopnRightRate() + "%");
				}
			}
			points = diagnosticClassKnowpointConvert.assembleTree(points);
		}

		// 根据ue的设计来看,以后还会加其他统计,故返回map
		retMap.put("kp", points);

		return new Value(retMap);
	}

}
