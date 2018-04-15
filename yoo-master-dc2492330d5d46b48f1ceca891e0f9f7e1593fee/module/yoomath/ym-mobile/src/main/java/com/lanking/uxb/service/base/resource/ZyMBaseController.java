package com.lanking.uxb.service.base.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import org.springframework.beans.factory.annotation.Autowired;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.PhaseConvert;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 悠数学移动端(rest API controller 基类)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@MappedSuperclass
public abstract class ZyMBaseController {

	@Autowired
	protected StudentService studentService;
	@Autowired
	protected ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	protected TeacherService teacherService;
	@Autowired
	protected ZyHomeworkClassService zyHkClassService;
	@Autowired
	protected TextbookService tbService;
	@Autowired
	protected TextbookConvert tbConvert;
	@Autowired
	protected PhaseService phaseService;
	@Autowired
	protected PhaseConvert phaseConvert;
	@Autowired
	protected TextbookCategoryService tbCateService;
	@Autowired
	protected TextbookCategoryConvert tbCateConvert;

	/**
	 * 获取用户可选教材列表以及相关信息
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param userType
	 *            用户类型
	 * @param userId
	 *            学生Id
	 * @param mapSize
	 *            Map初始化大小
	 * @return {@link Map}
	 */
	@SuppressWarnings("unchecked")
	@MasterSlaveDataSource(type="MS")
	protected Map<String, Object> textbookList(UserType userType, long userId, int mapSize) {
		Map<String, Object> dataMap = new HashMap<String, Object>(mapSize);
		if (userType == UserType.STUDENT) {
			Student student = (Student) studentService.getUser(UserType.STUDENT, Security.getUserId());
			Integer phaseCode = null;
			Integer categoryCode = null;
			if (student.getTextbookCode() == null) {// 学生自己没有设置,获取老师的阶段信息
				List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzs(Security.getUserId());
				phaseCode = ((Teacher) teacherService.getUser(UserType.TEACHER,
						zyHkClassService.get(clazzs.get(0).getClassId()).getTeacherId())).getPhaseCode();
				if (phaseCode == PhaseService.PHASE_MIDDLE) {// 初中苏科版
					categoryCode = TextbookCategoryService.SU_KE_BAN;
				} else if (phaseCode == PhaseService.PHASE_HIGH) {// 高中苏教版
					categoryCode = TextbookCategoryService.SU_JIAO_BAN;
				}
			} else {
				// phaseCode = student.getPhaseCode();
				phaseCode = tbService.get(student.getTextbookCode()).getPhaseCode();// 取学生所设置教材的所属阶段
				categoryCode = student.getTextbookCategoryCode();
				if (mapSize > 3) {
					dataMap.put("textbookCode", student.getTextbookCode());
				}

			}
			dataMap.put("phase", phaseConvert.to(phaseService.get(phaseCode)));
			dataMap.put("textbookCategory", tbCateConvert.to(tbCateService.get(categoryCode)));
			List<Textbook> tbs = null;
			if (phaseCode == PhaseService.PHASE_MIDDLE) {// 初中苏科版
				tbs = tbService.find(Product.YOOMATH, phaseCode, SubjectService.PHASE_2_MATH, categoryCode);
			} else if (phaseCode == PhaseService.PHASE_HIGH) {// 高中苏教版
				tbs = tbService.find(Product.YOOMATH, phaseCode, SubjectService.PHASE_3_MATH, categoryCode);
			} else {
				tbs = Collections.EMPTY_LIST;
			}
			dataMap.put("textbooks", tbConvert.to(tbs));
		} else if (userType == UserType.TEACHER) {

		}
		return dataMap;
	}
}
