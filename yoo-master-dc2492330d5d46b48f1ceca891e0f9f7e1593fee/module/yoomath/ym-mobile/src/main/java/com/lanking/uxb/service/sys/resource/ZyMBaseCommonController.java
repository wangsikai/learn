package com.lanking.uxb.service.sys.resource;

import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.SchoolType;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.sdk.value.ValueMap;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.DistrictService;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.DistrictConvert;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VPhase;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;

/**
 * 悠数学移动端(公共接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@MappedSuperclass
public class ZyMBaseCommonController {
	@Autowired
	PhaseService phaseService;
	@Autowired
	PhaseConvert phaseConvert;
	@Autowired
	TextbookCategoryService tbCateService;
	@Autowired
	TextbookCategoryConvert tbCateConvert;
	@Autowired
	TextbookService tbService;
	@Autowired
	TextbookConvert tbConvert;
	@Autowired
	private DistrictService districtService;
	@Autowired
	private DistrictConvert districtConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;

	/**
	 * 获取平台可选阶段/版本/教材列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "listTextbook", method = { RequestMethod.POST, RequestMethod.GET })
	public Value listTextbook(Integer phaseCode) {
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
		return new Value(dataList);
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "queryDistrict", method = { RequestMethod.POST, RequestMethod.GET })
	public Value queryDistrict(long pcode) {
		List<District> districts = districtService.getDistrictByPcode(pcode);
		ValueMap vm = ValueMap.value("items", districtConvert.to(districts));
		return new Value(vm);
	}

	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT", "TEACHER" })
	@RequestMapping(value = "querySchool", method = { RequestMethod.POST, RequestMethod.GET })
	public Value querySchool(long districtCode) {
		SchoolType schoolType = null;
		if (Security.getUserType() == UserType.TEACHER) {
			Teacher teacher = (Teacher) teacherService.getUser(Security.getUserId());
			if (teacher.getPhaseCode() != null) {
				if (teacher.getPhaseCode() == PhaseService.PHASE_MIDDLE) {
					schoolType = SchoolType.MIDDLE;
				} else if (teacher.getPhaseCode() == PhaseService.PHASE_HIGH) {
					schoolType = SchoolType.HIGH;
				}
			}
		} else if (Security.getUserType() == UserType.STUDENT) {
			Student student = (Student) studentService.getUser(Security.getUserId());
			if (student.getPhaseCode() != null) {
				if (student.getPhaseCode() == PhaseService.PHASE_MIDDLE) {
					schoolType = SchoolType.MIDDLE;
				} else if (student.getPhaseCode() == PhaseService.PHASE_HIGH) {
					schoolType = SchoolType.HIGH;
				}
			}
		}
		List<School> schools = schoolService.findSchoolByDistrictCode(districtCode, schoolType);
		School school = new School();
		school.setDistrictCode(districtCode);
		school.setId(-1L);
		school.setName("没有我的学校");
		schools.add(school);
		ValueMap vm = ValueMap.value("items", schoolConvert.to(schools));
		return new Value(vm);
	}
}
