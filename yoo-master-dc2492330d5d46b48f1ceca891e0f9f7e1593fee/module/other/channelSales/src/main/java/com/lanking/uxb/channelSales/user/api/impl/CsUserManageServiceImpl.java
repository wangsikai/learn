package com.lanking.uxb.channelSales.user.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLog;
import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLogType;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.channelSales.user.api.CsUserManageService;
import com.lanking.uxb.channelSales.user.form.CsUserQuery;

@Service
@Transactional(readOnly = true)
public class CsUserManageServiceImpl implements CsUserManageService {

	@Autowired
	@Qualifier("UserRepo")
	Repo<User, Long> userRepo;
	@Autowired
	@Qualifier("AccountRepo")
	Repo<Account, Long> accountRepo;
	@Autowired
	@Qualifier("ChannelUserOperateLogRepo")
	Repo<ChannelUserOperateLog, Long> logRepo;
	@Autowired
	@Qualifier("HomeworkStudentClazzRepo")
	Repo<HomeworkStudentClazz, Long> hkStudentClazzRepo;
	@Autowired
	@Qualifier("HomeworkClazzRepo")
	private Repo<HomeworkClazz, Long> homeworkClazzRepo;

	@SuppressWarnings("rawtypes")
	@Override
	public Page<Map> query(CsUserQuery query, Pageable p) {
		Params params = Params.param();
		if (query.getChannelCode() != null) {
			params.put("channelCode", query.getChannelCode());
		}
		if (query.getClassId() != null) {
			params.put("classId", query.getClassId());
		}
		if (query.getAccountName() != null) {
			params.put("accountName", "%" + query.getAccountName() + "%");
		}
		if (query.getMemberType() != null) {
			params.put("memberType", query.getMemberType().getValue());
		}
		if (query.getName() != null) {
			params.put("name", "%" + query.getName() + "%");
		}
		if (query.getSchoolName() != null) {
			params.put("schoolName", "%" + query.getSchoolName() + "%");
		}
		if (query.getUserType() != null) {
			params.put("userType", query.getUserType().getValue());
		} else {
			params.put("userType", 99);
		}
		if (query.getPhaseCode() != null) {
			params.put("phaseCode", query.getPhaseCode());
		}
		if (query.getActivationStatus() != null) {
			params.put("activationStatus", query.getActivationStatus().getValue());
		}
		if (StringUtils.isNotBlank(query.getClazzName())) {
			params.put("clazzName", "%" + query.getClazzName() + "%");
		}
		params.put("nowDate", new Date());
		return userRepo.find("$csQueryUserList", params).fetch(p, Map.class);
	}

	@Transactional
	@Override
	public void resetPassword(Long userId, Integer channelCode, Long createId) {
		User user = userRepo.get(userId);
		String password = "123456";
		Account account = accountRepo.get(user.getAccountId());
		account.setPassword(Codecs.md5Hex(password.getBytes()));
		account.setStrength(1);
		accountRepo.save(account);
		// 记录操作日志
		ChannelUserOperateLog log = new ChannelUserOperateLog();
		log.setChannelCode(channelCode);
		log.setCreateAt(new Date());
		log.setCreateId(createId);
		log.setUserId(userId);
		log.setOperateType(ChannelUserOperateLogType.RESETPASSWORD);
		logRepo.save(log);
	}

	@Override
	public List<ChannelUserOperateLog> findLogList(Long userId, Integer channelCode) {
		return logRepo.find("$findLogList", Params.param("userId", userId).put("channelCode", channelCode)).list();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> query(CsUserQuery query) {
		Params params = Params.param();
		if (query.getChannelCode() != null) {
			params.put("channelCode", query.getChannelCode());
		}
		if (query.getClassId() != null) {
			params.put("classId", query.getClassId());
		}
		if (query.getAccountName() != null) {
			params.put("accountName", "%" + query.getAccountName() + "%");
		}
		if (query.getMemberType() != null) {
			params.put("memberType", query.getMemberType().getValue());
		}
		if (query.getName() != null) {
			params.put("name", "%" + query.getName() + "%");
		}
		if (query.getSchoolName() != null) {
			params.put("schoolName", "%" + query.getSchoolName() + "%");
		}
		if (query.getUserType() != null) {
			params.put("userType", query.getUserType().getValue());
		} else {
			params.put("userType", 99);
		}
		if (query.getPhaseCode() != null) {
			params.put("phaseCode", query.getPhaseCode());
		}
		return userRepo.find("$csQueryUserList", params).list(Map.class);
	}

	@Override
	public HSSFWorkbook exportUserList(List<Map> list) {
		String[] excelHeader = { "学校", "班级", "账号", "真实姓名", "会员状态", "会员期限" };
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Campaign");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int i = 0; i < excelHeader.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
		}
		for (int i = 0; i < list.size(); i++) {
			row = sheet.createRow(i + 1);
			Map map = list.get(i);
			row.createCell(0).setCellValue(String.valueOf(map.get("schoolname")));
			row.createCell(1).setCellValue(String.valueOf(map.get("classname")));
			row.createCell(2).setCellValue(String.valueOf(map.get("accountname")));
			row.createCell(3).setCellValue(String.valueOf(map.get("name")));
			if (map.get("member_type") == null || map.get("start_at") == null) {
				row.createCell(4).setCellValue("非会员");
				row.createCell(5).setCellValue("无");

			} else {
				row.createCell(4).setCellValue("会员");
				row.createCell(5).setCellValue(String.valueOf(map.get("start_at")).substring(0, 10) + "--"
						+ String.valueOf(map.get("end_at")).substring(0, 10));
			}
		}
		return wb;
	}

	@Override
	public Map<Long, List<HomeworkClazz>> queryTeacherHomeworkClazzs(Collection<Long> teacherIds) {
		List<HomeworkClazz> homeworkClazzs = homeworkClazzRepo
				.find("$csFindByTeacherIds", Params.param("teacherIds", teacherIds)).list();
		Map<Long, List<HomeworkClazz>> returnData = new HashMap<Long, List<HomeworkClazz>>();
		for (HomeworkClazz hc : homeworkClazzs) {
			List<HomeworkClazz> hcs = returnData.get(hc.getTeacherId());
			if (hcs == null) {
				hcs = new ArrayList<HomeworkClazz>();
				returnData.put(hc.getTeacherId(), hcs);
			}
			hcs.add(hc);
		}
		return returnData;
	}

	@Override
	public Map<Long, List<HomeworkClazz>> queryStudentHomeworkClazzs(Collection<Long> studentIds) {
		List<HomeworkStudentClazz> homeworkStudentClazzs = hkStudentClazzRepo
				.find("$csFindByStudentIds", Params.param("studentIds", studentIds)).list();
		Set<Long> classIds = new HashSet<Long>();
		for (HomeworkStudentClazz hsc : homeworkStudentClazzs) {
			classIds.add(hsc.getClassId());
		}
		Map<Long, HomeworkClazz> homeworkClazzMap = homeworkClazzRepo.mget(classIds);
		Map<Long, List<HomeworkClazz>> returnData = new HashMap<Long, List<HomeworkClazz>>();
		for (HomeworkStudentClazz hsc : homeworkStudentClazzs) {
			List<HomeworkClazz> hcs = returnData.get(hsc.getStudentId());
			if (hcs == null) {
				hcs = new ArrayList<HomeworkClazz>();
				returnData.put(hsc.getStudentId(), hcs);
			}
			hcs.add(homeworkClazzMap.get(hsc.getClassId()));
		}
		return returnData;
	}
}
