package com.lanking.uxb.service.web.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeDifficulty;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPractise;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.ServerException;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RestController;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SectionService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SectionConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.web.api.DailyPractiseGenerateService;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyDailyPractiseConvert;

/**
 * 悠数学web端教材版本设置相关接口
 *
 * @author xinyu.zhou
 * @since 2.0.3
 */
@RestController
@RequestMapping(value = "zy/s/dp/setting")
public class ZyStuDailyPractiseSettingController {
	@Autowired
	private SectionService sectionServce;
	@Autowired
	private SectionConvert sectionConvert;
	@Autowired
	private PhaseService phaseService;
	@Autowired
	private PhaseConvert phaseConvert;
	@Autowired
	private TextbookCategoryService tbCateService;
	@Autowired
	private TextbookCategoryConvert tbCateConvert;
	@Autowired
	private TextbookService tbService;
	@Autowired
	private TextbookConvert tbConvert;
	@Autowired
	private ZyDailyPracticeSettingsService dailyPracticeSettingsService;
	@Autowired
	private StudentService studentService;
	@Autowired
	protected ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private DailyPractiseGenerateService dailyPractiseGenerateService;
	@Autowired
	private ZyDailyPractiseConvert dailyPractiseConvert;

	/**
	 * 获取可选难度列表
	 *
	 * @since 2.0.3
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "listSettings", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listSettings() {
		DailyPracticeDifficulty[] arr = DailyPracticeDifficulty.values();
		List<Map<String, String>> difficulties = new ArrayList<Map<String, String>>(arr.length);
		for (DailyPracticeDifficulty one : arr) {
			Map<String, String> oneMap = new HashMap<String, String>(3);
			oneMap.put("code", one.name());
			oneMap.put("name", one.getName());
			oneMap.put("difficulty", one.getDifficult());
			difficulties.add(oneMap);
		}
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("list", difficulties);
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() != null) {
			DailyPracticeSettings settings = dailyPracticeSettingsService.findByTextbookCode(Security.getUserId(),
					student.getTextbookCode());
			if (settings != null && settings.getDifficulty() != null) {
				data.put("code", settings.getDifficulty().name());
			} else {
				data.put("code", ZyDailyPracticeSettingsService.DEF.name());
			}
		}
		return new Value(data);
	}

	/**
	 * 设置难度
	 *
	 * @since 2.0.3
	 * @param difficulty
	 *            难度
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "setDifficulty", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setDifficulty(DailyPracticeDifficulty difficulty) {
		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		if (student.getTextbookCode() == null) {
			return new Value(new ServerException());
		}
		dailyPracticeSettingsService.set(Security.getUserId(), student.getTextbookCode(), difficulty, null);
		return new Value();
	}

	/**
	 * 设置每日练进度(章节代码)及章节码
	 *
	 * @since 2.0.3
	 * @param sectionCode
	 *            章节代码
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "setProgressAndTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setProgressAndTextbook(long sectionCode, int textbookCode) {
		Textbook textbook = tbService.get(textbookCode);
		if (textbook == null) {
			return new Value(new IllegalArgException());
		}
		studentService.setTextbook(Security.getUserId(), textbook.getPhaseCode(), textbook.getCategoryCode(),
				textbookCode);
		dailyPracticeSettingsService.set(Security.getUserId(), textbookCode, null, sectionCode);

		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		// 对当前教材版本下进行生成每日练操作
		DailyPractise dailyPractise = (DailyPractise) dailyPractiseGenerateService.generate(student, 20)
				.get("practise");

		return new Value(dailyPractiseConvert.to(dailyPractise));
	}

	/**
	 * 查询教材版本设置及当前进度设置
	 *
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query() {
		Map<String, Object> retMap = new HashMap<String, Object>(4);
		// 获得进度相关数据及当前进度码
		retMap.putAll(sectionTree());
		retMap.put("textbooks", listTextbook());

		Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
		retMap.put("textbookCode", student.getTextbookCode());

		return new Value(retMap);
	}

	/**
	 * 根据不同的教材得到章节信息
	 *
	 * @param textbookCode
	 *            教材码
	 * @return {@link Value}
	 */

	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "querySections", method = { RequestMethod.GET, RequestMethod.POST })
	public Value querySections(int textbookCode) {
		List<Section> sections = sectionServce.findByTextbookCode(textbookCode);
		List<VSection> sectionTree = sectionConvert.assemblySectionTree(sectionConvert.to(sections));

		return new Value(sectionTree);
	}

	/**
	 * 获得章节数据及学生当前进度对应的章节码
	 *
	 * @return
	 */
	private Map<String, Object> sectionTree() {
		Map<String, Object> dataMap = null;
		if (Security.getUserType() == UserType.STUDENT) {
			dataMap = new HashMap<String, Object>(2);
			Student student = (Student) studentService.getUser(com.lanking.cloud.domain.yoo.user.UserType.STUDENT,
					Security.getUserId());
			if (student.getTextbookCode() != null) {
				List<Section> sections = sectionServce.findByTextbookCode(student.getTextbookCode());
				List<VSection> sectionTree = sectionConvert.assemblySectionTree(sectionConvert.to(sections));
				dataMap.put("sections", sectionTree);
				DailyPracticeSettings settings = dailyPracticeSettingsService.findByTextbookCode(Security.getUserId(),
						student.getTextbookCode());
				if (settings == null || settings.getSectionCode() == null) {
					long sectionCode = 0;
					if (sectionTree.get(0).getChildren().size() == 0) {
						sectionCode = sectionTree.get(0).getCode();
					} else {
						if (sectionTree.get(0).getChildren().get(0).getChildren().size() == 0) {
							sectionCode = sectionTree.get(0).getChildren().get(0).getCode();
						} else {
							sectionCode = sectionTree.get(0).getChildren().get(0).getChildren().get(0).getCode();
						}
					}
					dataMap.put("dailyPracticeSectionCode", sectionCode);
				} else {
					dataMap.put("dailyPracticeSectionCode", settings.getSectionCode());
				}
			}
		}

		return dataMap;
	}

	/**
	 * 获得各版本教材版本信息
	 *
	 * @since 2.0.3
	 * @return {@link Value}
	 */
	private List<Map<String, Object>> listTextbook() {
		List<Map<String, Object>> dataList = Lists.newArrayList();
		List<VPhase> vphases = phaseConvert.to(phaseService.getAll());
		for (VPhase vphase : vphases) {
			Map<String, Object> one = Maps.newHashMap();
			one.put("phase", vphase);
			List<TextbookCategory> tbCates = tbCateService.find(Product.YOOMATH, vphase.getCode());
			List<VTextbookCategory> vtbCates = tbCateConvert.to(tbCates);
			List<VTextbookCategory> oneList = Lists.newArrayList();
			for (VTextbookCategory vtbCate : vtbCates) {
				List<Textbook> tbs = null;
				if (vphase.getCode() == PhaseService.PHASE_MIDDLE) {
					tbs = tbService.find(Product.YOOMATH, vphase.getCode(), SubjectService.PHASE_2_MATH,
							vtbCate.getCode());
				} else {
					tbs = tbService.find(Product.YOOMATH, vphase.getCode(), SubjectService.PHASE_3_MATH,
							vtbCate.getCode());
				}
				vtbCate.setTextbooks(tbConvert.to(tbs));
				oneList.add(vtbCate);
			}
			one.put("tbCates", oneList);
			dataList.add(one);
		}

		return dataList;
	}

}
