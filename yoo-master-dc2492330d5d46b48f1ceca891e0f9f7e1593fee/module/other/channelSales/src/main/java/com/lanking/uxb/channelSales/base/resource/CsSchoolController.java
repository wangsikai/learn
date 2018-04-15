package com.lanking.uxb.channelSales.base.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.base.api.CsSchoolService;
import com.lanking.uxb.channelSales.base.form.SchoolForm;
import com.lanking.uxb.channelSales.channel.convert.CsSchoolConvert;
import com.lanking.uxb.channelSales.channel.value.VSchool;
import com.lanking.uxb.channelSales.common.ex.ChannelSalesConsoleException;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 基础数据-学校管理
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("channelSales/school")
public class CsSchoolController {

	@Autowired
	private CsSchoolService schoolService;
	@Autowired
	private CsSchoolConvert schoolConvert;

	/**
	 * 查询学校列表
	 * 
	 * @param query
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "querySchoolList")
	public Value querySchoolList(SchoolForm query) {
		Page<School> page = schoolService.query(query, P.index(query.getPage(), query.getPageSize()));
		VPage<VSchool> vp = new VPage<VSchool>();
		int tPage = (int) (page.getTotalCount() + query.getPageSize() - 1) / query.getPageSize();
		vp.setPageSize(query.getPageSize());
		vp.setCurrentPage(query.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(page.getTotalCount());
		vp.setItems(schoolConvert.to(page.getItems()));
		return new Value(vp);
	}

	/**
	 * 给学校绑定渠道
	 * 
	 * @param schoolId
	 * @param code
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "bindChannel")
	public Value bindChannel(Long schoolId, Integer code) {
		// 渠道号不存在
		if (!schoolService.isExistChannel(code)) {
			return new Value(new ChannelSalesConsoleException(ChannelSalesConsoleException.CHANNELCODE_NOT_EXIST));
		}
		schoolService.bindChannel(schoolId, code, Security.getUserId());
		return new Value();
	}
}
