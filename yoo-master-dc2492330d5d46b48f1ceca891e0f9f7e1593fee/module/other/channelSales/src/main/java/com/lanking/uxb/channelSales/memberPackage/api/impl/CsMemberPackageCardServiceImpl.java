package com.lanking.uxb.channelSales.memberPackage.api.impl;

import httl.util.StringUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.member.MemberPackageCard;
import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.Codecs;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageCardService;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardQueryForm;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageCard;

/**
 * @author zemin.song
 * @version 2016年11月15日
 */
@Service
@Transactional(readOnly = true)
public class CsMemberPackageCardServiceImpl implements CsMemberPackageCardService {
	@Autowired
	@Qualifier("MemberPackageCardRepo")
	private Repo<MemberPackageCard, String> memberPackageCardRepo;
	@Autowired
	@Qualifier("ConsoleUserRepo")
	private Repo<ConsoleUser, Long> consoleUserRepo;

	// 去除0、O、I、1
	private char[] baseLetters = { '3', 'B', 'L', '7', 'T', 'P', '5', 'E', 'X', '4', 'M', '9', 'V', 'C', 'J', 'F', '8',
			'H', 'Q', 'Z', 'S', '2', 'D', 'W', 'K', '6', 'U', 'Y', 'G', 'R', 'N', 'A' };

	/**
	 * 批量生成卡号.
	 * 
	 * @param cards
	 */
	public void generateCodes(List<MemberPackageCard> cards) {
		String time = this.toUnsignedString(System.currentTimeMillis(), 5);
		for (int i = 0; i < cards.size(); i++) {
			MemberPackageCard card = cards.get(i);
			String encryptCode = time
					+ this.toUnsignedString(Long.parseLong((int) (Math.random() * 999999) + "" + i), 5);
			String code = Codecs.md5Hex16(encryptCode).replaceAll("0", "W").replaceAll("1", "X").replaceAll("2", "Y")
					.toUpperCase();
			card.setEncryptCode(encryptCode);
			card.setCode(code);
		}
	}

	private String toUnsignedString(long input, int shift) {
		char[] buf = new char[32];
		int charPos = 32;
		int radix = 1 << shift;
		int mask = radix - 1;
		do {
			buf[--charPos] = baseLetters[(int) input & mask];
			input >>>= shift;
		} while (input != 0);
		return new String(buf, charPos, (32 - charPos));
	}

	@Override
	public Page<MemberPackageCard> query(MemberPackageCardQueryForm query, Pageable pageable) {
		Params params = Params.param();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long nowDate = cal.getTimeInMillis();
		Date dt = new Date(nowDate);
		params.put("curdate", dt);
		if (StringUtils.isNotBlank(query.getCode())) {
			params.put("code", query.getCode());
		}
		if (StringUtils.isNotBlank(query.getMemo())) {
			params.put("memo", "%" + query.getMemo() + "%");
		}
		if (null != query.getUserType() && null != query.getMemberType()) {
			params.put("userType", query.getUserType().getValue());
			params.put("memberType", query.getMemberType().getValue());
		}
		if (null != query.getMonth()) {
			params.put("month", query.getMonth());
		}
		if (null != query.getStatus()) {
			params.put("status", query.getStatus().getValue());
		}
		if (null != query.getCreateId()) {
			params.put("createId", query.getCreateId());
		}
		if (null != query.getEndDate()) {
			params.put("endAt", query.getEndDate());
		}
		if (null != query.getStartDate()) {
			params.put("startAt", query.getStartDate());
		}
		if (query.getOrderType() != null) {
			params.put("orderType", query.getOrderType());
		} else {
			params.put("orderType", 0);
		}
		return memberPackageCardRepo.find("$csQueryMemberPackageCard", params).fetch(pageable);
	}

	@Transactional
	@Override
	public MemberPackageCard create(MemberPackageCardForm form) {
		List<MemberPackageCard> cards = new ArrayList<MemberPackageCard>(form.getCount());
		Date dt = new Date();
		Date endDt = new Date(form.getEndDate());
		MemberPackageCard mpc = null;
		for (int i = 0; i < form.getCount(); i++) {
			mpc = new MemberPackageCard();
			mpc.setUserType(form.getUserType());
			mpc.setMemberType(form.getMemberType());
			mpc.setEndAt(endDt);
			mpc.setPrice(form.getPrice());
			mpc.setMemo(form.getMemo());
			mpc.setCreateAt(dt);
			mpc.setCreateId(form.getCreateId());
			mpc.setMonth(form.getMonth());
			mpc.setStatus(MemberPackageCardStatus.DEFAULT);
			cards.add(mpc);
		}
		generateCodes(cards);
		memberPackageCardRepo.save(cards);
		return cards.get(0);
	}

	@Transactional
	@Override
	public void update(MemberPackageCardForm form) {
		MemberPackageCard mp = memberPackageCardRepo.get(form.getCode());
		if (null != form.getEndDate()) {
			mp.setEndAt(new Date(form.getEndDate()));
		}
		if (null != form.getStatus()) {
			mp.setStatus(form.getStatus());
		}
		mp.setUpdateAt(new Date());
		memberPackageCardRepo.save(mp);
	}

	@Override
	public HSSFWorkbook exportCards(List<VMemberPackageCard> cards) {
		String[] excelHeader = { "状态", "会员卡类型", "时长", "卡号(16位)", "有效期至", "价值(元)" };
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("VIPcard");
		HSSFRow row = sheet.createRow((int) 0);
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);

		for (int i = 0; i < excelHeader.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellValue(excelHeader[i]);
			cell.setCellStyle(style);
			sheet.autoSizeColumn(i);
		}

		DateFormat format = new java.text.SimpleDateFormat("yyyy年MM月dd日 24:00");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long nowDate = cal.getTimeInMillis();
		String status = "";
		for (int i = 0; i < cards.size(); i++) {
			row = sheet.createRow(i + 1);
			VMemberPackageCard v = cards.get(i);
			if (v.getStatus().getValue() == MemberPackageCardStatus.DEFAULT.getValue()
					&& nowDate <= v.getEndAt().getTime()) {
				status = "正常";
			}
			if (v.getStatus().getValue() == MemberPackageCardStatus.DISABLE.getValue()
					|| (v.getStatus().getValue() == 0 && nowDate > v.getEndAt().getTime())) {
				status = "冻结";
			}
			if (v.getStatus().getValue() == MemberPackageCardStatus.DELETE.getValue()) {
				status = "废弃";
			}
			row.createCell(0).setCellValue(status);
			row.createCell(1).setCellValue(
					v.getUserType() == UserType.STUDENT ? "学生VIP会员" : v.getUserType() == UserType.TEACHER ? "老师VIP会员"
							: "类型有误");
			row.createCell(2).setCellValue("1个月");
			row.createCell(3).setCellValue(v.getCode());
			row.createCell(4).setCellValue(format.format(v.getEndAt()));
			row.createCell(5).setCellValue(v.getPrice().doubleValue());
		}
		return wb;
	}

	@Override
	public List<MemberPackageCard> queryNewCreate(MemberPackageCardForm form) {
		Params params = Params.param();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long nowDate = cal.getTimeInMillis();
		Date dt = new Date(nowDate);
		params.put("curdate", dt);
		if (null != form.getCreateId()) {
			params.put("createId", form.getCreateId());
		}
		if (null != form.getCreateAt()) {
			Date createDate = new Date(form.getCreateAt());
			params.put("createDate", createDate);
		}
		return memberPackageCardRepo.find("$csQueryMemberPackageCard", params).list();
	}

	@Override
	public List<MemberPackageCard> queryAll(MemberPackageCardQueryForm query) {
		Params params = Params.param();
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Long nowDate = cal.getTimeInMillis();
		Date dt = new Date(nowDate);
		params.put("curdate", dt);
		if (null != query.getCodes()) {
			String strCodes = query.getCodes();
			String[] codes = strCodes.split(",");
			List<String> codeList = new ArrayList<String>();
			for (String code : codes) {
				codeList.add(code);
			}
			params.put("codes", codeList);
		}
		if (null != query.getNoCodes()) {
			String strCodes = query.getNoCodes();
			String[] codes = strCodes.split(",");
			List<String> codeList = new ArrayList<String>();
			for (String code : codes) {
				codeList.add(code);
			}
			params.put("noCodes", codeList);
		}

		if (StringUtils.isNotBlank(query.getCode())) {
			params.put("code", query.getCode());
		}
		if (StringUtils.isNotBlank(query.getMemo())) {
			params.put("memo", "%" + query.getMemo() + "%");
		}
		if (null != query.getUserType() && null != query.getMemberType()) {
			params.put("userType", query.getUserType().getValue());
			params.put("memberType", query.getMemberType().getValue());
		}
		if (null != query.getMonth()) {
			params.put("month", query.getMonth());
		}
		if (null != query.getStatus()) {
			params.put("status", query.getStatus().getValue());
		}
		if (null != query.getCreateId()) {
			params.put("createId", query.getCreateId());
		}
		if (null != query.getEndDate()) {
			params.put("endAt", query.getEndDate());
		}
		if (null != query.getStartDate()) {
			params.put("startAt", query.getStartDate());
		}
		if (query.getOrderType() != null) {
			params.put("orderType", query.getOrderType());
		} else {
			params.put("orderType", 0);
		}
		return memberPackageCardRepo.find("$csQueryMemberPackageCard", params).list();
	}

	@Override
	public List<ConsoleUser> getCreateUsers() {
		return consoleUserRepo.find("$csGetCreateUsers").list();
	}

}
