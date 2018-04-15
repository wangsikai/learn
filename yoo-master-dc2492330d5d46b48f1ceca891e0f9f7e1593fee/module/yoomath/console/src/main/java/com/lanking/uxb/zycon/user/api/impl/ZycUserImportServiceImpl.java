package com.lanking.uxb.zycon.user.api.impl;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.baseData.District;
import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.PasswordStatus;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.channel.ChannelSchool;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.Sex;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserSettings;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.ex.core.EntityNotFoundException;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.uxb.service.code.api.PhaseService;
import com.lanking.uxb.service.code.api.SubjectService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.zycon.user.api.UserQuery;
import com.lanking.uxb.zycon.user.api.ZycUserChannelService;
import com.lanking.uxb.zycon.user.api.ZycUserImportService;
import com.lanking.uxb.zycon.user.form.UserForm;
import com.lanking.uxb.zycon.user.util.ZycUserExcelCheck;
import com.lanking.uxb.zycon.user.value.VZycUserImport;

@Transactional(readOnly = true)
@Service
public class ZycUserImportServiceImpl implements ZycUserImportService {

	private Logger logger = LoggerFactory.getLogger(ZycUserImportServiceImpl.class);
	private static String CODE_PREFIX = "1";
	private static int CODE_LENGTH = 5;
	private static int CODE_TRYTIME = 100;
	private static int MAX_IMPORT_RECORD = 1000;

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;

	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;

	@Autowired
	@Qualifier("TeacherRepo")
	Repo<Teacher, Long> teacherRepo;

	@Autowired
	@Qualifier("StudentRepo")
	Repo<Student, Long> studentRepo;

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Autowired
	@Qualifier("DistrictRepo")
	private Repo<District, Long> districtRepo;

	@Autowired
	@Qualifier("UserSettingsRepo")
	Repo<UserSettings, Long> userSettingRepo;

	@Autowired
	@Qualifier("SchoolRepo")
	Repo<School, Long> schoolRepo;

	@Autowired
	@Qualifier("ChannelSchoolRepo")
	Repo<ChannelSchool, Long> channelSchoolRepo;

	@Autowired
	@Qualifier("UserChannelRepo")
	Repo<UserChannel, Integer> userChannelRepo;

	@Autowired
	private TextbookService textbookService;
	@Autowired
	private ZycUserChannelService zycUserChannelService;

	@SuppressWarnings("resource")
	@Override
	public Map<String, Object> getWb(HttpServletRequest request) {
		Workbook workbook = null;
		// 最后返回的list
		List<VZycUserImport> dataList = new ArrayList<VZycUserImport>();
		// 当前excel学生用户名集合，用来判断当前excel是否存在重复用户名
		List<String> stuAccountList = new ArrayList<String>();
		// 当前excel老师用户名集合，用来判断当前excel是否存在学生用户名跟老师重复
		List<String> teaAccountList = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			InputStream in = request.getInputStream();
			if (!in.markSupported()) {
				in = new PushbackInputStream(in, 1024);
			}
			if (POIFSFileSystem.hasPOIFSHeader(in)) {
				workbook = new HSSFWorkbook(in);
			} else if (POIXMLDocument.hasOOXMLHeader(in)) {
				workbook = new XSSFWorkbook(OPCPackage.open(in));
			}

			Sheet sheet = workbook.getSheetAt(0);
			int rowCount = sheet.getPhysicalNumberOfRows();
			if (rowCount >= (MAX_IMPORT_RECORD + 1)) {
				return null;
			}
			// 目前取到的所有列名，顺序
			List<String> titleList = new ArrayList<String>();
			Row row1 = sheet.getRow(0);
			for (int c = 0; c < 18; c++) {
				Cell cell = row1.getCell(c);
				titleList.add(cell.getStringCellValue().trim());
			}
			for (int r = 1; r < rowCount; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				VZycUserImport v = new VZycUserImport();
				for (int c = 0; c < 18; c++) {
					Cell cell = row.getCell(c);
					// 2017.4.26新增班级入学年份导入
					// 对应EXCEL：0.班级ID 1.班级入学年份 2.班级名称 3.教师名称 4.教师手机号 5.教师密码
					// 6.教师真实姓名
					// 7.教师性别 8.阶段 9.学科 10.学生用户名 11.学生密码 12.学生真实姓名 13.学生性别
					// 14.教材15.渠道代码 16.学校代码 17.学生入学年份
					// 模板中班级id,手机号,密码是数字类型
					DecimalFormat decimalFormat = new DecimalFormat("#");
					String val = null;
					try {
						val = decimalFormat.format(cell.getNumericCellValue());
					} catch (Exception e) {
						if (cell == null) {
							val = null;
						} else {
							val = cell.getStringCellValue().trim();
						}
					}
					if (titleList.get(c).equals("班级ID")) {
						v.setClassId(val);
					} else if (titleList.get(c).equals("班级入学年份")) {
						v.setClassEnterYear(val);
					} else if (titleList.get(c).equals("班级名称")) {
						v.setClassName(val);
					} else if (titleList.get(c).equals("教师用户名")) {
						v.setTeacherName(val);
					} else if (titleList.get(c).equals("手机号")) {
						v.setTeacherMobile(val);
					} else if (titleList.get(c).equals("教师密码")) {
						v.setTeacherPwd(val);
					} else if (titleList.get(c).equals("教师真实姓名")) {
						v.setTeacherRealName(val);
					} else if (titleList.get(c).equals("教师性别")) {
						v.setTeacherSex(val);
					} else if (titleList.get(c).equals("阶段")) {
						v.setPhase(val);
					} else if (titleList.get(c).equals("学科")) {
						v.setSubject(val);
					} else if (titleList.get(c).equals("学生用户名")) {
						v.setStudentName(val);
					} else if (titleList.get(c).equals("学生密码")) {
						v.setStudentPwd(val);
					} else if (titleList.get(c).equals("学生真实姓名")) {
						v.setStudentRealName(val);
					} else if (titleList.get(c).equals("学生性别")) {
						v.setStudentSex(val);
					} else if (titleList.get(c).equals("教师教材")) {
						v.setTextbookCode(val);
					} else if (titleList.get(c).equals("渠道代码")) {
						v.setChannelCode(val);
					} else if (titleList.get(c).equals("学校代码")) {
						v.setSchoolId(val);
					} else if (titleList.get(c).equals("学生入学年份")) {
						v.setEnterYear(val);
					}
				}
				Map<String, Object> checkMap = this.checkImportDatas(v);
				if (checkMap.get("result").toString().equals("fail")) {
					map.put("result", "fail");
					int n = r + 1;
					map.put("dataList", "第" + n + "行数据异常:" + checkMap.get("content"));
					return map;
				}
				if (!teaAccountList.contains(v.getTeacherName())) {
					teaAccountList.add(v.getTeacherName());
				}
				if (teaAccountList.contains(v.getStudentName())) {
					map.put("result", "fail");
					int n = r + 1;
					map.put("dataList", "第" + n + "行数据异常，存在跟老师重复用户名：" + v.getStudentName());
					return map;
				}
				if (!stuAccountList.contains(v.getStudentName())) {
					stuAccountList.add(v.getStudentName());
				} else {
					map.put("result", "fail");
					int n = r + 1;
					map.put("dataList", "第" + n + "行数据异常，存在学生重复用户名：" + v.getStudentName());
					return map;
				}
				dataList.add(v);
			}
			// 增加验证学生不能存在重名的或者学生和老师不能存在重名的验证
			map.put("result", "success");
			map.put("dataList", dataList.size() == 0 ? null : dataList);
		} catch (Exception e) {
			logger.error("userImport getWb error", e);
		}
		return map;
	}

	@Transactional
	@Override
	public String save(List<VZycUserImport> list, Long createId) throws Exception {
		// 临时存放channelCode,schoolId
		List<String> csTemp = new ArrayList<String>();
		// 临时存放teacherId,className用来减少重复老师班级的判断
		List<String> teachClassTemp = new ArrayList<String>();
		// mobile临时存放
		List<String> mobileTemp = new ArrayList<String>();
		// 部分重复的验证，在保存的时候验证
		for (VZycUserImport ll : list) {
			if (ll == null)
				continue;
			Long classId = Long.valueOf(ll.getClassId());
			String classEnterYear = ll.getClassEnterYear();
			String className = ll.getClassName();
			String teacherName = ll.getTeacherName();
			String mobile = ll.getTeacherMobile();
			String teacherPwd = ll.getTeacherPwd();
			String teacherRealName = ll.getTeacherRealName();
			String teacherSex = ll.getTeacherSex();
			String phaseName = ll.getPhase();
			String subjectName = ll.getSubject();
			String studentName = ll.getStudentName();
			String studentPwd = ll.getStudentPwd();
			String studentRealName = ll.getStudentRealName();
			String studentSex = ll.getStudentSex();
			String textbookCode = ll.getTextbookCode();
			String channelCode = ll.getChannelCode();
			String schoolId = ll.getSchoolId();
			String enterYear = ll.getEnterYear();
			/**
			 * 保存channel_school,已经存在的数据不保存<br>
			 * 优化：目前导入的数据渠道商code和schoolId基本是一致的, 所以只要判断一次或少次
			 */
			String csKey = Integer.parseInt(channelCode) + "," + Long.parseLong(schoolId);
			if (!csTemp.contains(csKey)) {
				if (this.getCSchool(Integer.parseInt(channelCode), Long.parseLong(schoolId)) == null) {
					ChannelSchool cs = new ChannelSchool();
					cs.setChannelCode(Integer.parseInt(channelCode));
					cs.setSchoolId(Long.parseLong(schoolId));
					cs.setCreateAt(new Date());
					cs.setStartAt(new Date());
					cs.setCreateId(createId);
					cs.setStatus(Status.ENABLED);
					channelSchoolRepo.save(cs);
				}
				csTemp.add(csKey);
			}
			// 先创建老师
			Account account = new Account();
			account.setName(teacherName);
			// 2.5.0开始用户导入手机可以为空
			if (mobile != null) {
				if (!mobile.equals("0")) {
					account.setMobile(mobile);
				}
			}
			account.setPassword(Codecs.md5Hex(teacherPwd));
			account.setStrength(getStrength(teacherPwd));
			account.setStatus(Status.ENABLED);
			account.setNameUpdateStatus(Status.DISABLED);
			account.setRegisterAt(new Date());
			// 2017.4.26新增
			account.setPasswordStatus(PasswordStatus.SYSTEM);
			List<Account> accounts = accountRepo.find("$zycExistsAccount", Params.param("accountName", teacherName))
					.list();
			Teacher teacher = new Teacher();
			// 数据库不存在当前用户
			if (accounts.size() == 0) {
				if (!mobileTemp.contains(mobile)) {
					if (mobile != null && !mobile.equals("0")) {
						// 当前手机号对应的账号个数
						List<Account> mAccounts = getAccountsByType(GetType.MOBILE, mobile);
						// 不同老师之间存在相同手机号
						if (mAccounts.size() > 0) {
							throw new RuntimeException("数据异常：不同老师之间存在相同手机号");
						}
					}
					mobileTemp.add(mobile);
				}

				accountRepo.save(account);
				User user = new User();
				user.setAccountId(account.getId());
				user.setName(teacherRealName);
				user.setUserType(UserType.TEACHER);
				user.setUserChannelCode(Integer.parseInt(channelCode));
				//
				teacher.setCreateAt(new Date());
				teacher.setName(teacherRealName);
				teacher.setPhaseCode(phaseName.equals("高中") ? PhaseService.PHASE_HIGH : PhaseService.PHASE_MIDDLE);
				teacher.setSubjectCode(
						phaseName.equals("高中") ? SubjectService.PHASE_3_MATH : SubjectService.PHASE_2_MATH);
				teacher.setSex(teacherSex.equals("男") ? Sex.MALE : Sex.FEMALE);
				teacher.setTextbookCategoryCode(textbookService.get(Integer.parseInt(textbookCode)).getCategoryCode());
				teacher.setTextbookCode(Integer.parseInt(textbookCode));
				teacher.setSchoolId(Long.parseLong(schoolId));
				teacherRepo.save(teacher);
				user.setId(teacher.getId());
				user.setImport0(true);
				userRepo.save(user);
			} else {
				teacher = teacherRepo.find("$zycFindByAccount", Params.param("accountId", accounts.get(0).getId()))
						.get();
			}

			// 再创建学生
			Account account_stu = new Account();
			accounts = accountRepo.find("$zycExistsAccount", Params.param("accountName", studentName)).list();
			Student stu = new Student();
			if (accounts.size() == 0) {
				account_stu.setName(studentName);
				account_stu.setPassword(Codecs.md5Hex(studentPwd));
				account.setStrength(getStrength(studentPwd));
				account_stu.setStatus(Status.ENABLED);
				account_stu.setRegisterAt(new Date());
				account_stu.setNameUpdateStatus(Status.DISABLED);
				// 2017.4.26新增
				account_stu.setPasswordStatus(PasswordStatus.SYSTEM);
				accountRepo.save(account_stu);
				User user_stu = new User();
				user_stu.setAccountId(account_stu.getId());
				user_stu.setName(studentRealName);
				user_stu.setUserType(UserType.STUDENT);
				user_stu.setUserChannelCode(Integer.parseInt(channelCode));
				// Student stu = new Student();
				stu.setName(studentRealName);
				stu.setCreateAt(new Date());
				stu.setPhaseCode(phaseName.equals("高中") ? PhaseService.PHASE_HIGH : PhaseService.PHASE_MIDDLE);
				stu.setSex(studentSex.equals("男") ? Sex.MALE : Sex.FEMALE);
				stu.setTextbookCategoryCode(textbookService.get(Integer.parseInt(textbookCode)).getCategoryCode());
				stu.setTextbookCode(Integer.parseInt(textbookCode));
				stu.setSchoolId(Long.parseLong(schoolId));
				stu.setYear(Integer.parseInt(enterYear));
				studentRepo.save(stu);
				user_stu.setId(stu.getId());
				user_stu.setImport0(true);
				userRepo.save(user_stu);
			} else {
				stu = studentRepo.find("$zycFindByAccount", Params.param("accountId", accounts.get(0).getId())).get();
			}
			/**
			 * 优化：每一个老师一个班级只需要判断一次
			 */
			String teachClassKey = teacher.getId() + "," + className;
			if (!teachClassTemp.contains(teachClassKey)) {
				// 创建班级前，确认该老师的班级个数，已经有五个提示异常
				Long classNum = this.getClassNumByTeacher(teacher.getId());
				if (classNum == 5) {
					throw new RuntimeException(teacher.getName() + "有效班级数不能超过5个");
				}
				teachClassTemp.add(teachClassKey);
			}

			// 创建班级
			HomeworkClazz homeworkClazz = homeworkClazzRepo
					.find("$zycGetClass", Params.param("teacherId", teacher.getId()).put("name", className)).get();
			int num = 1;
			if (homeworkClazz == null) {
				homeworkClazz = new HomeworkClazz();
			} else {
				num = homeworkClazz.getStudentNum() + 1;
			}
			homeworkClazz.setName(className);
			homeworkClazz.setCode(generateCode());
			homeworkClazz.setCreateAt(new Date());
			homeworkClazz.setStartAt(homeworkClazz.getCreateAt());
			homeworkClazz.setStudentNum(num);
			homeworkClazz.setDescription(className);
			homeworkClazz.setTeacherId(teacher.getId());
			homeworkClazz.setOriginalTeacherId(teacher.getId());
			homeworkClazz.setSchoolYear(Integer.parseInt(classEnterYear));
			homeworkClazzRepo.save(homeworkClazz);
			// 学生加入班级
			HomeworkStudentClazz stuHomeworkClazz = homeworkStudentClazzRepo.find("$zycGetByClazzId",
					Params.param("classId", homeworkClazz.getId()).put("studentId", stu.getId())).get();
			if (stuHomeworkClazz == null) {
				stuHomeworkClazz = new HomeworkStudentClazz();
			}
			stuHomeworkClazz.setClassId(homeworkClazz.getId());
			stuHomeworkClazz.setJoinAt(new Date());
			stuHomeworkClazz.setStudentId(stu.getId());
			stuHomeworkClazz.setCreateAt(new Date());
			homeworkStudentClazzRepo.save(stuHomeworkClazz);
		}
		return null;
	}

	public String generateCode() {
		int tryTime = 1;
		String code = CODE_PREFIX + RandomStringUtils.random(CODE_LENGTH, false, true);
		while (codeExist(code)) {
			if (tryTime >= CODE_TRYTIME) {
				code = null;
				break;
			}
			code = CODE_PREFIX + RandomStringUtils.random(CODE_LENGTH, false, true);
			tryTime++;
		}
		return code;
	}

	/**
	 * 导入的用户，判断当前密码的强度
	 * 
	 * @param password
	 * @return
	 */
	public int getStrength(String password) {
		int strength = 0;
		Pattern p = Pattern.compile("[a-z]");
		Matcher m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		p = Pattern.compile("[0-9]");
		m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		p = Pattern.compile("(.[^a-z0-9])");
		m = p.matcher(password);
		if (m.find()) {
			strength++;
		}
		return strength;
	}

	public boolean codeExist(String code) {
		return homeworkClazzRepo.find("$zycCountByCode", Params.param("code", code)).count() > 0;
	}

	@Override
	public Page<Map> query(UserQuery form, Pageable p) {
		Params params = Params.param();
		if (form.getUserType() != null) {
			params.put("userType", form.getUserType().getValue());
		}
		if (form.getStatus() != null) {
			params.put("status", form.getStatus().getValue());
		}
		if (form.getSex() != null) {
			params.put("sex", form.getSex().getValue());
		}
		if (form.getPhaseCode() != null) {
			params.put("phaseCode", form.getPhaseCode());
		}
		if (form.getActivationStatus() != null) {
			params.put("activationStatus", form.getActivationStatus().getValue());
		}
		if (form.getSubjectCode() != null) {
			params.put("subjectCode", form.getSubjectCode());
		}
		if (form.getAccountName() != null) {
			params.put("accountName", "%" + form.getAccountName() + "%");
		}
		if (form.getName() != null) {
			params.put("name", "%" + form.getName() + "%");
		}
		if (form.getMobile() != null) {
			params.put("mobile", "%" + form.getMobile() + "%");
		}
		if (form.getEmail() != null) {
			params.put("email", "%" + form.getEmail() + "%");
		}
		if (form.getSchoolName() != null) {
			params.put("schoolName", "%" + form.getSchoolName() + "%");
		}
		if (form.getDistrictCodeStr() != null) {
			params.put("districtCodeStr", form.getDistrictCodeStr());
		}
		if (form.getDistricts() != null) {
			params.put("districts", form.getDistricts());
		}
		if (form.getChannelCode() != null) {
			params.put("channelCode", form.getChannelCode().intValue());
		}
		// 新增用户会员类型
		if (form.getMemberType() != null) {
			params.put("memberType", form.getMemberType().getValue());
		}
		params.put("nowTime", new Date());
		return userRepo.find("$zyQueryUser", params).fetch(p, Map.class);
	}

	@Transactional
	@Override
	public void saveUser(UserForm form) {
		if (form.getAccountId() != null) {
			Account account = accountRepo.get(form.getAccountId());
			// 处理手机号码重复
			if (account.getMobile() != form.getMobile()) {
				account.setMobile(form.getMobile());
			}
			if (account.getEmail() != form.getEmail()) {
				account.setEmail(form.getEmail());
			}
			if (form.getPassword() != null) {
				account.setPassword(Codecs.md5Hex(form.getPassword()));
				account.setStrength(getStrength(form.getPassword()));
			}
			account.setStatus(form.getStatus());
			accountRepo.save(account);
			User user = userRepo.get(form.getUserId());
			user.setName(form.getName());
			user.setUserType(form.getUserType());
			user.setStatus(form.getStatus());
			if (UserType.TEACHER == form.getUserType()) {
				Teacher teacher = teacherRepo.get(form.getUserId());
				teacher.setName(form.getName());
				teacher.setPhaseCode(form.getPhaseCode());
				teacher.setDutyCode(form.getDutyCode());
				teacher.setTitleCode(form.getTitleCode());
				teacher.setSubjectCode(form.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
						: SubjectService.PHASE_2_MATH);
				teacher.setSex(form.getSex());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				try {
					if (form.getWorkAt() != null && !form.getWorkAt().equals("")) {
						teacher.setWorkAt(sdf.parse(form.getWorkAt()));
					}
				} catch (Exception e) {
					logger.error("date parse error:", e);
				}
				if (form.getSchoolId() != null) {
					teacher.setSchoolId(form.getSchoolId());
				}
				teacherRepo.save(teacher);
				userRepo.save(user);
				UserSettings us = userSettingRepo.get(form.getUserId());
				if (us != null) {
					us.setHomeworkSms(form.getSmsNotice());
				} else {
					us = new UserSettings();
					us.setId(form.getUserId());
					us.setHomeworkSms(form.getSmsNotice());
				}
				userSettingRepo.save(us);
			}
			if (UserType.STUDENT == form.getUserType()) {
				Student stu = studentRepo.get(form.getUserId());
				stu.setName(form.getName());
				stu.setPhaseCode(form.getPhaseCode());
				stu.setSex(form.getSex());
				if (form.getSchoolId() != null) {
					stu.setSchoolId(form.getSchoolId());
				}
				stu.setYear(form.getEnterYear());
				studentRepo.save(stu);
				userRepo.save(user);
			}
		} else {
			Account account = new Account();
			account.setName(form.getAccountName());
			account.setEmail(form.getEmail());
			account.setMobile(form.getMobile());
			account.setPassword(Codecs.md5Hex(form.getPassword()));
			account.setStrength(getStrength(form.getPassword()));
			account.setStatus(form.getStatus());
			account.setRegisterAt(new Date());
			accountRepo.save(account);
			User user = new User();
			user.setAccountId(account.getId());
			user.setName(form.getName());
			user.setUserType(form.getUserType());
			user.setStatus(form.getStatus());
			if (UserType.TEACHER == form.getUserType()) {
				Teacher teacher = new Teacher();
				teacher.setCreateAt(new Date());
				teacher.setName(form.getName());
				teacher.setPhaseCode(form.getPhaseCode());
				teacher.setSubjectCode(form.getPhaseCode() == PhaseService.PHASE_HIGH ? SubjectService.PHASE_3_MATH
						: SubjectService.PHASE_2_MATH);
				teacher.setDutyCode(form.getDutyCode());
				teacher.setTitleCode(form.getTitleCode());
				teacher.setSex(form.getSex());
				if (form.getWorkAt() != null && !form.getWorkAt().equals("")) {
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						teacher.setWorkAt(sdf.parse(form.getWorkAt()));
					} catch (Exception e) {
						logger.error("date parse error:", e);
					}
				} else {
					teacher.setWorkAt(null);
				}
				if (form.getSchoolId() != null) {
					teacher.setSchoolId(form.getSchoolId());
				}
				teacherRepo.save(teacher);
				user.setId(teacher.getId());
				userRepo.save(user);
				UserSettings us = userSettingRepo.get(teacher.getId());
				if (us != null) {
					us.setHomeworkSms(form.getSmsNotice());
				} else {
					us = new UserSettings();
					us.setId(teacher.getId());
					us.setHomeworkSms(form.getSmsNotice());
				}
				userSettingRepo.save(us);
			}
			if (UserType.STUDENT == form.getUserType()) {
				Student stu = new Student();
				stu.setName(form.getName());
				stu.setCreateAt(new Date());
				stu.setPhaseCode(form.getPhaseCode());
				stu.setSex(form.getSex());
				stu.setYear(form.getEnterYear());
				if (form.getSchoolId() != null) {
					stu.setSchoolId(form.getSchoolId());
				}
				studentRepo.save(stu);
				user.setId(stu.getId());
				userRepo.save(user);
			}

		}
	}

	@Override
	public List<Account> getAccounts(String accountName) {
		return accountRepo.find("$zycExistsAccount", Params.param("accountName", accountName)).list();
	}

	@Transactional
	@Override
	public void updateStatus(UserForm form) {
		User user = userRepo.get(form.getUserId());
		user.setStatus(form.getStatus());
		userRepo.save(user);
		Account account = accountRepo.get(form.getAccountId());
		account.setStatus(form.getStatus());
		accountRepo.save(account);
	}

	@Override
	public List<Account> getAccountsByType(GetType type, String value) {
		List<Account> accounts = accountRepo.find("SELECT * FROM account WHERE " + type.getName() + " = ?", value)
				.list();
		return accounts;
	}

	/**
	 * 判断当前学校ID是否存在
	 * 
	 * @param schoolId
	 * @return
	 */
	public boolean schoolExist(Long schoolId) {
		Long count = schoolRepo.find("$zycExistSchool", Params.param("schoolId", schoolId)).get(Long.class);
		return count == 0 ? false : true;
	}

	/**
	 * 验证用户名是否存在
	 * 
	 * @param value
	 * @return
	 */
	public List<Account> getAccountsByName(String value) {
		List<Account> accounts = accountRepo.find("$zycExistsAccount", Params.param("accountName", value)).list();
		return accounts;
	}

	@Override
	public UserSettings getUs(Long userId) {
		return userSettingRepo.get(userId);
	}

	@Override
	public Long getClassNumByTeacher(Long teacherId) {
		return homeworkClazzRepo.find("$getClassNumByTeacher", Params.param("teacherId", teacherId)).count();
	}

	/**
	 * 获取ChannelSchool
	 * 
	 * @param channelCode
	 * @param schoolId
	 * @return
	 */
	public ChannelSchool getCSchool(Integer channelCode, Long schoolId) {
		Params params = Params.param();
		if (channelCode != null) {
			params.put("channelCode", channelCode);
		}
		if (schoolId != null) {
			params.put("schoolId", schoolId);
		}
		List<ChannelSchool> list = channelSchoolRepo.find("$getCSchool", params).list();
		if (list.size() > 1) {
			logger.error("数据库学校渠道数据异常:");
			return list.get(0);
		} else if (list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public Map<String, Object> checkImportDatas(VZycUserImport ll) {
		Map<String, Object> map = new HashMap<String, Object>();
		String classId = ll.getClassId();
		String classEnterYear = ll.getClassEnterYear();
		String className = ll.getClassName();
		String teacherName = ll.getTeacherName();
		String mobile = ll.getTeacherMobile();
		String teacherPwd = ll.getTeacherPwd();
		String teacherRealName = ll.getTeacherRealName();
		String teacherSex = ll.getTeacherSex();
		String phaseName = ll.getPhase();
		String subjectName = ll.getSubject();
		String studentName = ll.getStudentName();
		String studentPwd = ll.getStudentPwd();
		String studentRealName = ll.getStudentRealName();
		String studentSex = ll.getStudentSex();
		String textbookCode = ll.getTextbookCode();
		String channelCode = ll.getChannelCode();
		String schoolId = ll.getSchoolId();
		String enterYear = ll.getEnterYear();
		StringBuffer sb = new StringBuffer();
		Integer phaseCode = PhaseService.PHASE_HIGH;

		if (!ZycUserExcelCheck.isNumber(classId)) {
			sb.append("班级ID");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		if (!ZycUserExcelCheck.isNumber(classEnterYear) || classEnterYear == null) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("班级入学年份");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			Calendar c = Calendar.getInstance();
			// 判断入学
			if (c.get(Calendar.YEAR) < Integer.parseInt(classEnterYear)) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("班级入学年份");
				sb.append("不能大于当前年份");
			}
		}
		// 1.班级名称
		if (ZycUserExcelCheck.getLength(className) > 20) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("班级名称");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		// 2.教师用户名
		// 用户名为4-16位
		if (ZycUserExcelCheck.getLength(teacherName) < 4 || ZycUserExcelCheck.getLength(teacherName) > 16) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师用户名");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		// 不合法
		if (!ZycUserExcelCheck.isLegalUser(teacherName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师用户名");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 教师用户名已经存在
		if (getAccountsByName(teacherName).size() > 0) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师用户名");
			sb.append(ZycUserExcelCheck.ERROR_EXIST);
		}
		// 3.手机号不正确
		/**
		 * @since 2.5.0用户导入的时候，老师手机号可以为空
		 */
		// 只有不为空的时候，才会进行对手机号的判断
		if (mobile != null) {
			if (!mobile.equals("0")) {
				if (!ZycUserExcelCheck.isMobile(mobile)) {
					sb.append(sb.length() != 0 ? "," : "");
					sb.append("教师手机号");
					sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
				}
				// 教师手机号码已经存在
				if (getAccountsByType(GetType.MOBILE, mobile).size() > 0) {
					sb.append(sb.length() != 0 ? "," : "");
					sb.append("教师手机号");
					sb.append(ZycUserExcelCheck.ERROR_EXIST);
				}
			}
		}
		// 4.教师密码长度不符合
		if (ZycUserExcelCheck.getLength(teacherPwd) < 6 || ZycUserExcelCheck.getLength(teacherPwd) > 16) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师密码");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		// 教师密码格式错误
		if (!ZycUserExcelCheck.isLegalPwd(teacherPwd, teacherName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师密码");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 5.教师真实姓名(不为空且不大于15位,这里英文和汉字都算一位)
		if (teacherRealName.length() > 15 || teacherRealName.length() == 0) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师真实姓名");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		if (!ZycUserExcelCheck.isLegalRealName(teacherRealName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师真实姓名");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 6.教师性别
		if (!teacherSex.equals("男") && !teacherSex.equals("女")) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教师性别");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 7.阶段
		if (!"高中".equals(phaseName) && !"初中".equals(phaseName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("阶段");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			phaseCode = phaseName.equals("高中") ? PhaseService.PHASE_HIGH : PhaseService.PHASE_MIDDLE;
		}
		// 8.学科
		if (!"数学".equals(subjectName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学科");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 9.学生用户名
		if (ZycUserExcelCheck.getLength(studentName) < 4 || ZycUserExcelCheck.getLength(studentName) > 16) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生用户名");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		// 不合法
		if (!ZycUserExcelCheck.isLegalUser(studentName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生用户名");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 学生用户名已经存在
		if (getAccountsByName(studentName).size() > 0) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生用户名");
			sb.append(ZycUserExcelCheck.ERROR_EXIST);
		}
		// 10.学生密码长度不符合
		if (ZycUserExcelCheck.getLength(studentPwd) < 6 || ZycUserExcelCheck.getLength(studentPwd) > 16) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生密码");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		// 学生密码格式错误
		if (!ZycUserExcelCheck.isLegalPwd(studentPwd, studentName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生密码");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 11.学生真实姓名校验(不为空且不大于15位,这里英文和汉字都算一位)
		if (studentRealName.length() == 0 || studentRealName.length() > 15) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生真实姓名");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		if (!ZycUserExcelCheck.isLegalRealName(studentRealName)) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生真实姓名");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 12.学生性别
		if (!studentSex.equals("男") && !studentSex.equals("女")) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生性别");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		// 新增教材字段
		if (!ZycUserExcelCheck.isNumber(textbookCode) || textbookCode == null) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("教材");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			Textbook tb = textbookService.get(Integer.parseInt(textbookCode));
			if (tb == null) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("教材不存在");
			} else {
				if (tb.getPhaseCode() != phaseCode) {
					sb.append(sb.length() != 0 ? "," : "");
					sb.append("教材不是当前阶段的");
				}
			}
		}
		// 渠道代码
		if (!ZycUserExcelCheck.isNumber(channelCode) || channelCode == null) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("渠道代码");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			if (zycUserChannelService.get(Integer.parseInt(channelCode)) == null) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("渠道代码不存在");
			}
		}
		// 学校代码
		if (!ZycUserExcelCheck.isNumber(schoolId) || schoolId == null) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学校代码");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			if (!this.schoolExist(Long.parseLong(schoolId))) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("学校代码不存在");
			}
		}
		ChannelSchool cs = this.getCSchool(null, Long.parseLong(schoolId));
		if (cs != null) {
			if (cs.getChannelCode() != Integer.parseInt(channelCode)) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("一个学校只能存在一个渠道商");
			}
		}
		if (!ZycUserExcelCheck.isNumber(enterYear) || enterYear == null) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生入学年份");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		} else {
			Calendar c = Calendar.getInstance();
			// 判断入学
			if (c.get(Calendar.YEAR) < Integer.parseInt(enterYear)) {
				sb.append(sb.length() != 0 ? "," : "");
				sb.append("学生入学年份");
				sb.append("不能大于当前年份");
			}
		}
		if (sb.length() == 0) {
			map.put("result", "success");
		} else {
			map.put("result", "fail");
		}
		map.put("content", sb);
		return map;
	}

	@Override
	public List<District> getDistrictByName(String name) {
		return districtRepo.find("$getDistrictByName", Params.param("name", name + '%')).list();
	}

	@Override
	public Boolean isStudent(Long accountId) {
		User user = userRepo.find("$getUserByAccountId1", Params.param("accountId", accountId)).get();
		Student student = studentRepo.get(user.getId());
		return student == null ? false : true;
	}

	@Transactional(readOnly = false)
	@Override
	public void updateUserChannel(long userId, int channelCode) {
		if (userId < 1 || channelCode < 10000) {
			throw new IllegalArgException();
		}
		User user = userRepo.get(userId);
		if (user == null) {
			throw new EntityNotFoundException();
		}
		UserChannel uc = userChannelRepo.get(channelCode);
		if (uc == null) {
			throw new EntityNotFoundException();
		}
		user.setUserChannelCode(channelCode);
		userRepo.save(user);
	}

	@Override
	public boolean isLegalChannel(Integer channelCode) {
		Params params = Params.param("channelCode", channelCode);
		Long count = userRepo.find("$isLegalChannel", params).get(Long.class);
		return count > 0 ? true : false;
	}

	@Override
	public boolean isLegalSchoolChannel(Integer channelCode, Long schoolId) {
		Params params = Params.param("channelCode", channelCode).put("schoolId", schoolId);
		Long count = userRepo.find("$isLegalSchoolChannel", params).get(Long.class);
		return count > 0 ? true : false;
	}

	@Transactional
	@Override
	public void updateTeacherName(List<Teacher> teachers, List<User> users) {
		for(Teacher teacher:teachers){
			teacherRepo.execute("$updateName",Params.param("id", teacher.getId()).put("name", teacher.getName()));
		}
		
		for(User user:users){
			userRepo.execute("$updateName",Params.param("id", user.getId()).put("name", user.getName()));
		}
	}

	@Override
	public Long getTeachersCount() {
		Long count = teacherRepo.find("$zycGetTeachersCount").get(Long.class);
		return count;
	}

	@Override
	public List<Teacher> getTeachers(int start, int num) {
		List<Teacher> teachers = teacherRepo.find("$zycGetTeachers", Params.param("start", start).put("num", num)).list();
		return teachers;
	}

	@Override
	public List<User> getUsers(List<Long> ids) {
		List<User> users = userRepo.mgetList(ids);
		return users;
	}

	@Override
	public List<Account> getAccountsByIds(List<Long> ids) {
		List<Account> Accounts = accountRepo.mgetList(ids);
		return Accounts;
	}
}
