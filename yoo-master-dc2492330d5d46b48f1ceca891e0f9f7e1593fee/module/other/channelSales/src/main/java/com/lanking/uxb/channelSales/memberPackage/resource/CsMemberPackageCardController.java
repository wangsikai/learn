package com.lanking.uxb.channelSales.memberPackage.resource;

import httl.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoo.member.MemberPackageCardStatus;
import com.lanking.cloud.domain.yoo.member.MemberType;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.DBException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.memberPackage.api.CsMemberPackageCardService;
import com.lanking.uxb.channelSales.memberPackage.convert.CSMemberPackageCreateConvert;
import com.lanking.uxb.channelSales.memberPackage.convert.CsMemberPackageCardConvert;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardForm;
import com.lanking.uxb.channelSales.memberPackage.form.MemberPackageCardQueryForm;
import com.lanking.uxb.channelSales.memberPackage.value.VMemberPackageCard;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 会员兑换卡管理
 * 
 * @author zemin.song
 * @version 2016年11月15日 14:16:37
 */
@RestController
@RequestMapping("channelSales/mpc")
public class CsMemberPackageCardController {
	@Autowired
	private CsMemberPackageCardService memberPackageCardService;
	@Autowired
	private CsMemberPackageCardConvert memberPackageCardConvert;
	@Autowired
	private CSMemberPackageCreateConvert memberPackageCreateConvert;

	/**
	 * 会员卡查询
	 * 
	 * @param queryForm
	 */
	@RequestMapping(value = "query")
	public Value query(MemberPackageCardQueryForm query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<VMemberPackageCard> page = memberPackageCardConvert.to(memberPackageCardService.query(query, pageable));
		VPage vpage = new VPage();
		vpage.setItems(page.getItems());
		vpage.setPageSize(query.getPage());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	/**
	 * 创建
	 * 
	 * @param MemberPackageCardForm
	 */
	@RequestMapping(value = "create")
	public Value create(MemberPackageCardForm form) {
		form.setCreateId(Security.getUserId());
		if (form.getCount() == null || form.getCount() == 0) {
			return new Value(new DBException());
		}
		return new Value(memberPackageCardConvert.to(memberPackageCardService.create(form)));
	}

	/**
	 * 获取创建用户
	 * 
	 * @param
	 */
	@RequestMapping(value = "getCreateUsers")
	public Value getCreateUsers() {
		return new Value(memberPackageCreateConvert.to(memberPackageCardService.getCreateUsers()));
	}

	/**
	 * 更新会员卡
	 * 
	 * @param MemberPackageCardForm
	 */
	@RequestMapping(value = "update")
	public Value update(MemberPackageCardForm form) {
		form.setUpdateId(Security.getUserId());
		memberPackageCardService.update(form);
		return new Value();
	}

	@RequestMapping(value = "exportAll")
	public void exportAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberPackageCardQueryForm form = new MemberPackageCardQueryForm();
		String codes = request.getParameter("codes");
		if (StringUtils.isNotBlank(codes)) {
			form.setCodes(codes);
		}
		String noCodes = request.getParameter("noCodes");
		if (StringUtils.isNotBlank(noCodes)) {
			form.setNoCodes(noCodes);
		}
		String code = request.getParameter("code");
		if (StringUtils.isNotBlank(code)) {
			form.setCode(code);
		}
		String memo = request.getParameter("memo");
		if (StringUtils.isNotBlank(memo)) {
			form.setMemo(memo);
		}
		String status = request.getParameter("status");
		if (StringUtils.isNotBlank(status)) {
			if (status.equals(MemberPackageCardStatus.DEFAULT.toString())) {
				form.setStatus(MemberPackageCardStatus.DEFAULT);
			}
			if (status.equals(MemberPackageCardStatus.DELETE.toString())) {
				form.setStatus(MemberPackageCardStatus.DELETE);
			}
			if (status.equals(MemberPackageCardStatus.DISABLE.toString())) {
				form.setStatus(MemberPackageCardStatus.DISABLE);
			}
		}
		String createId = request.getParameter("createId");
		if (StringUtils.isNotBlank(createId)) {
			form.setCreateId(Long.parseLong(createId));
		}
		String startDate = request.getParameter("startDate");
		if (StringUtils.isNotBlank(startDate)) {
			form.setStartDate(startDate);
		}
		String endDate = request.getParameter("endDate");
		if (StringUtils.isNotBlank(endDate)) {
			form.setEndDate(endDate);
		}
		String userType = request.getParameter("userType");
		if (StringUtils.isNotBlank(userType)) {
			if (userType.equals(UserType.STUDENT.toString())) {
				form.setUserType(UserType.STUDENT);
			}
			if (userType.equals(UserType.TEACHER.toString())) {
				form.setUserType(UserType.TEACHER);
			}
			// 默认会员
			form.setMemberType(MemberType.VIP);
		}

		List<VMemberPackageCard> cards = memberPackageCardConvert.to(memberPackageCardService.queryAll(form));
		HSSFWorkbook wb = memberPackageCardService.exportCards(cards);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=VIPcard.xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();

	}

	/**
	 * 创建成功导出会员卡
	 * 
	 * @param MemberPackageCardForm
	 * @throws IOException
	 */
	@RequestMapping(value = "exportByCreate")
	public void exportByCreate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MemberPackageCardForm form = new MemberPackageCardForm();
		form.setCreateId(Security.getUserId());
		String createDate = request.getParameter("createDate");
		if (StringUtils.isNotBlank(createDate)) {
			form.setCreateAt(Long.parseLong(createDate));
		}
		List<VMemberPackageCard> cards = memberPackageCardConvert.to(memberPackageCardService.queryNewCreate(form));
		HSSFWorkbook wb = memberPackageCardService.exportCards(cards);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=VIPcard.xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
	}
}
