package com.lanking.uxb.zycon.homeclazz.api.impl;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqYoomathDiagnoRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.HomeworkStat;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.zycon.homeclazz.api.ClazzQuery;
import com.lanking.uxb.zycon.homeclazz.api.ZycHkClazzService;
import com.lanking.uxb.zycon.user.api.ZycUserImportService;
import com.lanking.uxb.zycon.user.util.ZycUserExcelCheck;

@Transactional(readOnly = true)
@Service
public class ZycHkClazzServiceImpl implements ZycHkClazzService {

	@Autowired
	@Qualifier("HomeworkClazzRepo")
	Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@Autowired
	@Qualifier("StudentRepo")
	Repo<Student, Long> studentRepo;

	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> homeworkStudentClazzRepo;

	@Autowired
	@Qualifier("HomeworkStatRepo")
	Repo<HomeworkStat, Long> homeworkStatRepo;

	private static int MAX_IMPORT_RECORD = 300;

	@Autowired
	private ZycUserImportService zycUserImportService;

	@Autowired
	MqSender mqSender;

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryHkClazz(ClazzQuery cq, Pageable p) {
		Params param = Params.param();
		if (cq.getStatus() != null) {
			param.put("status", cq.getStatus().getValue());
		}
		if (cq.getPhaseCode() != null) {
			param.put("phaseCode", cq.getPhaseCode());
		}
		if (cq.getSubjectCode() != null) {
			param.put("subjectCode", cq.getSubjectCode());
		}
		if (cq.getSchoolName() != null) {
			param.put("schoolName", "%" + cq.getSchoolName() + "%");
		}
		if (cq.getLoginName() != null) {
			param.put("accountName", "%" + cq.getLoginName() + "%");
		}
		if (cq.getTeacherName() != null) {
			param.put("teacherName", "%" + cq.getTeacherName() + "%");
		}
		if (cq.getClazzName() != null) {
			param.put("clazzName", "%" + cq.getClazzName() + "%");
		}
		if (cq.getCode() != null) {
			param.put("code", "%" + cq.getCode() + "%");
		}
		if (cq.getSchoolYear() != null) {
			param.put("schoolYear", cq.getSchoolYear());
		}
		return homeworkClazzRepo.find("$queryHkClazz", param).fetch(p, Map.class);
	}

	@Transactional
	@Override
	public void updateHkClazz(Long hkClazzId, String name, Status status, Integer schoolYear) {
		HomeworkClazz hc = homeworkClazzRepo.get(hkClazzId);
		hc.setName(name);
		hc.setStatus(status);
		hc.setSchoolYear(schoolYear);
		homeworkClazzRepo.save(hc);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> queryClazzMember(Long hkClazzId) {
		return studentRepo.find("$queryClazzMember", Params.param("hkClazzId", hkClazzId)).list(Map.class);
	}

	@Transactional
	@Override
	public void delStudent(Long studentId, Long clazzId) {
		homeworkStudentClazzRepo.execute("$delStudent", Params.param("studentId", studentId).put("clazzId", clazzId));
		// 更新班级人数
		HomeworkClazz homeworkClazz = homeworkClazzRepo.get(clazzId);
		homeworkClazz.setStudentNum(homeworkClazz.getStudentNum() - 1);
		homeworkClazzRepo.save(homeworkClazz);
	}

	@Override
	public Map<Long, HomeworkStat> mgetStat(Collection<Long> hkClazzIds) {
		List<HomeworkStat> list = homeworkStatRepo.find("$mgetStat", Params.param("hkClazzIds", hkClazzIds)).list();
		Map<Long, HomeworkStat> map = new HashMap<Long, HomeworkStat>();
		if (list.size() > 0) {
			for (HomeworkStat hs : list) {
				map.put(hs.getHomeworkClassId(), hs);
			}
		}
		return map;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> queryStudent(String name, Pageable p) {
		Params param = Params.param();
		if (name != null) {
			param.put("name", "%" + name + "%");
		}
		return studentRepo.find("$queryStudent", param).fetch(p, Map.class);
	}

	@Transactional
	@Override
	public void addStu(Long studentId, Long clazzId) {
		HomeworkStudentClazz stuHomeworkClazz = this.getHK(studentId, clazzId);
		if (stuHomeworkClazz == null) {
			stuHomeworkClazz = new HomeworkStudentClazz();
			stuHomeworkClazz.setClassId(clazzId);
			stuHomeworkClazz.setJoinAt(new Date());
			stuHomeworkClazz.setStudentId(studentId);
			stuHomeworkClazz.setCreateAt(new Date());
		} else {
			stuHomeworkClazz.setJoinAt(new Date());
			stuHomeworkClazz.setStatus(Status.ENABLED);
		}

		homeworkStudentClazzRepo.save(stuHomeworkClazz);
		// 更新班级人数
		HomeworkClazz homeworkClazz = homeworkClazzRepo.get(clazzId);
		homeworkClazz.setStudentNum(homeworkClazz.getStudentNum() + 1);
		homeworkClazzRepo.save(homeworkClazz);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getHkClazz(Long clazzId) {
		return homeworkClazzRepo.find("$getHkClazz", Params.param("clazzId", clazzId)).get(Map.class);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Map getHkClazz(String clazzCode) {
		return homeworkClazzRepo.find("$getHkClazz", Params.param("clazzCode", clazzCode)).get(Map.class);
	}

	@Override
	public boolean classNameIsExist(Long teacherId, String name) {
		HomeworkClazz homeworkClazz = homeworkClazzRepo
				.find("$zycGetClass", Params.param("teacherId", teacherId).put("name", name)).get();
		return homeworkClazz == null ? false : true;
	}

	@Override
	public HomeworkStudentClazz getHK(Long studentId, Long classId) {
		return homeworkStudentClazzRepo
				.find("$zycGetByClazzId", Params.param("studentId", studentId).put("classId", classId)).get();
	}

	@Override
	public Map<String, Object> getWb(HttpServletRequest request) {
		Workbook workbook = null;
		// 最后返回的list
		List<List<String>> dataList = new ArrayList<List<String>>();
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
			for (int r = 1; r < rowCount; r++) {
				Row row = sheet.getRow(r);
				if (row == null) {
					continue;
				}
				// 账户名
				List<String> rowLst = new ArrayList<String>();
				Cell cell = row.getCell(0);
				DecimalFormat decimalFormat = new DecimalFormat("#");
				try {
					rowLst.add(decimalFormat.format(cell.getNumericCellValue()));
				} catch (Exception e) {
					if (cell == null) {
						rowLst.add(null);
					} else {
						rowLst.add(cell.getStringCellValue().trim());
					}
				}
				Map<String, Object> checkMap = this.checkImportDatas(rowLst);
				if (checkMap.get("result").toString().equals("fail")) {
					map.put("result", "fail");
					int n = r + 1;
					map.put("dataList", "第" + n + "行数据异常:" + checkMap.get("content"));
					return map;
				}
				dataList.add(rowLst);
			}
			map.put("result", "success");
			map.put("dataList", dataList.size() == 0 ? null : dataList);
		} catch (Exception e) {
		}
		return map;
	}

	@Transactional
	@Override
	public void save(List<List<String>> list, Long clazzId) {
		// 新增班级人数
		int addNum = 0;
		for (List<String> ll : list) {
			String accountName = ll.get(0);
			List<Account> accounts = zycUserImportService.getAccounts(accountName);
			Student stu = studentRepo.find("$zycFindByAccount", Params.param("accountId", accounts.get(0).getId()))
					.get();
			// 学生加入班级
			HomeworkStudentClazz stuHomeworkClazz = homeworkStudentClazzRepo
					.find("$zycGetByClazzId", Params.param("classId", clazzId).put("studentId", stu.getId())).get();

			if (stuHomeworkClazz == null) {
				stuHomeworkClazz = new HomeworkStudentClazz();
				stuHomeworkClazz.setClassId(clazzId);
				stuHomeworkClazz.setJoinAt(new Date());
				stuHomeworkClazz.setStudentId(stu.getId());
				stuHomeworkClazz.setCreateAt(new Date());
				homeworkStudentClazzRepo.save(stuHomeworkClazz);
				addNum++;
			} else {
				if (stuHomeworkClazz.getStatus() != Status.ENABLED) {
					stuHomeworkClazz.setStatus(Status.ENABLED);
					stuHomeworkClazz.setJoinAt(new Date());
					homeworkStudentClazzRepo.save(stuHomeworkClazz);
					addNum++;
				}
			}
			// 学生加入班级，需要重新更新学生诊断相应数据 数据跟随2017.7.6 senhao.wang
			JSONObject object = new JSONObject();
			object.put("classId", clazzId);
			object.put("studentId", stu.getId());
			mqSender.send(MqYoomathDiagnoRegistryConstants.EX_YM_DIAGNO_TASK,
					MqYoomathDiagnoRegistryConstants.RK_YM_DIAGNO_TASK_STUDENT_JOIN, MQ.builder().data(object).build());
		}
		HomeworkClazz homeworkClazz = homeworkClazzRepo.get(clazzId);
		homeworkClazz.setStudentNum(homeworkClazz.getStudentNum() + addNum);
		homeworkClazzRepo.save(homeworkClazz);
	}

	public Map<String, Object> checkImportDatas(List<String> ll) {
		Map<String, Object> map = new HashMap<String, Object>();
		String accountName = ll.get(0);
		StringBuffer sb = new StringBuffer();
		if (!ZycUserExcelCheck.isLegalUser(accountName)) {
			sb.append("学生账号");
			sb.append(ZycUserExcelCheck.ERROR_LLEGAL);
		}
		if (ZycUserExcelCheck.getLength(accountName) < 4 || ZycUserExcelCheck.getLength(accountName) > 16) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生账号");
			sb.append(ZycUserExcelCheck.ERROR_LENGTH);
		}
		if (zycUserImportService.getAccounts(accountName).size() == 0) {
			sb.append(sb.length() != 0 ? "," : "");
			sb.append("学生账号不存在");
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
	public HomeworkClazz getHkClassByClassId(Long clazzId) {
		return homeworkClazzRepo.get(clazzId);
	}
}
