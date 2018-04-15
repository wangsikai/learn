package com.lanking.uxb.channelSales.channel.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.School;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.order.member.MemberPackageOrderChannelSettlement;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.ex.core.MissingArgumentException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsAggClassHomeworkService;
import com.lanking.uxb.channelSales.channel.api.CsAggStudentService;
import com.lanking.uxb.channelSales.channel.api.CsDmnChannelService;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.channel.api.CsUserService;
import com.lanking.uxb.channelSales.channel.convert.CsUserChannelConvert;
import com.lanking.uxb.channelSales.channel.form.ChannelUserQueryForm;
import com.lanking.uxb.channelSales.channel.value.VPageChannel;
import com.lanking.uxb.channelSales.channel.value.VUserChannel;
import com.lanking.uxb.channelSales.finance.api.CsMemberPackageOrderChannelSettlementService;
import com.lanking.uxb.channelSales.finance.convert.MemberPackageOrderChannelSettlementConvert;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 渠道商管理
 * 
 * @author zemin.song
 * @version 2016年9月28日
 */
@RestController
@RequestMapping("channelSales/cv")
public class CsChannelVendorController {

	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsUserChannelConvert userChannelConvert;
	@Autowired
	private CsUserService userService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private CsAggStudentService aggStuService;
	@Autowired
	private CsDmnChannelService dmnChannelService;
	@Autowired
	private CsAggClassHomeworkService aggClassService;
	@Autowired
	private SchoolConvert schoolConvert;
	@Autowired
	private CsMemberPackageOrderChannelSettlementService csMemberPackageOrderChannelSettlementService;
	@Autowired
	private MemberPackageOrderChannelSettlementConvert memberPackageOrderChannelSettlementConvert;

	@RequestMapping(value = "query")
	public Value query(Integer pageSize, Integer page) {
		pageSize = pageSize == null ? 20 : pageSize;
		page = page == null ? 1 : page;
		VPage<VUserChannel> voPage = new VPage<VUserChannel>();
		Page<VUserChannel> poPage = userChannelConvert.to(userChannelService.query(P.index(page, pageSize)));
		voPage.setPageSize(pageSize);
		voPage.setCurrentPage(page);
		voPage.setTotalPage(poPage.getPageCount());
		voPage.setTotal(poPage.getTotalCount());
		voPage.setItems(poPage.getItems());
		return new Value(voPage);
	}

	@RequestMapping(value = "bindConUser")
	public Value bind(Long code, String conName) {
		if (code == null || conName.equals("")) {
			return new Value(new MissingArgumentException());
		}
		int ret = userChannelService.bindConName(code, conName);
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new IllegalArgException());
		}
	}

	@RequestMapping(value = "removeBind")
	public Value removeBind(Long code) {
		if (code == null) {
			return new Value(new MissingArgumentException());
		}
		int ret = userChannelService.removeBind(code);
		if (ret > 0) {
			return new Value();
		} else {
			return new Value(new IllegalArgException());
		}
	}

	@RequestMapping(value = "findUsers")
	public Value findUserByChannel(ChannelUserQueryForm query) {
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = userService.findByChannel(query, pageable);
		List<Long> schoolIds = new ArrayList<Long>();
		List<Map> retMap = new ArrayList<Map>();
		for (Map mp : page.getItems()) {
			if (null != mp.get("schoolid")) {
				schoolIds.add(Long.parseLong(mp.get("schoolid").toString()));
			}
		}
		Map<Long, School> schoolMap = schoolService.mget(schoolIds);
		for (Map mp : page.getItems()) {
			if (null != mp.get("schoolid")) {
				Long schoolId = Long.parseLong(mp.get("schoolid").toString());
				School sl = schoolMap.get(schoolId);
				if (null != sl) {
					mp.put("schoolName", sl.getName());
				}
			}
			retMap.add(mp);
		}
		VPage<Map> vpage = new VPage<Map>();
		vpage.setItems(retMap);
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	@RequestMapping(value = "findMyChannel")
	public Value findMyChannel(ChannelUserQueryForm query) {
		// 查询自己的信息
		VUserChannel vu = null;
		if (query.getPage() == 1) {
			vu = userChannelConvert.to(userChannelService.getChannelByUser(Security.getUserId()));
			if (vu == null) {
				return new Value(new VPage());
			}
			query.setCode(vu.getCode());
		}
		Pageable pageable = P.index(query.getPage(), query.getSize());
		Page<Map> page = userService.findByChannel(query, pageable);
		List<Long> schoolIds = new ArrayList<Long>();
		List<Map> retMap = new ArrayList<Map>();
		for (Map mp : page.getItems()) {
			if (null != mp.get("schoolid")) {
				schoolIds.add(Long.parseLong(mp.get("schoolid").toString()));
			}
		}
		Map<Long, School> schoolMap = schoolService.mget(schoolIds);
		for (Map mp : page.getItems()) {
			if (null != mp.get("schoolid")) {
				Long schoolId = Long.parseLong(mp.get("schoolid").toString());
				School sl = schoolMap.get(schoolId);
				if (null != sl) {
					mp.put("schoolName", sl.getName());
				}
			}
			retMap.add(mp);
		}
		VPageChannel vpage = new VPageChannel();
		vpage.setvUserChannel(vu);
		vpage.setItems(retMap);
		vpage.setPageSize(query.getSize());
		vpage.setTotal(page.getTotalCount());
		vpage.setTotalPage(page.getPageCount());
		vpage.setCurrentPage(query.getPage());
		return new Value(vpage);
	}

	@RequestMapping(value = "create")
	public Value create(String name) {
		userChannelService.create(name);
		return new Value();
	}

	@RequestMapping(value = "update")
	public Value create(String name, Integer code) {
		if (code == null) {
			return new Value(new IllegalArgException());
		}
		userChannelService.update(code, name);
		return new Value();
	}

	@RequestMapping(value = "chengeLimit")
	public Value create(BigDecimal limit, Integer code) {
		if (code == null || limit == null) {
			return new Value(new IllegalArgException());
		}
		userChannelService.updateLimit(code, limit);
		return new Value();
	}

	/**
	 * 查看我的渠道信息和前十转化率列表
	 * 
	 * @param query
	 * @return
	 */
	@RequestMapping(value = "findMyChannel2")
	public Value findMyChannel2() {
		Map<String, Object> data = new HashMap<String, Object>();
		// 查询自己的信息
		VUserChannel vu = userChannelConvert.to(userChannelService.getChannelByUser(Security.getUserId()));
		Integer code = dmnChannelService.getIdChannel(vu.getCode());
		if (code == null) {
			return new Value(data);
		}
		List<Map<String, Object>> list = aggStuService.queryList(code, null, null);
		data.put("vUserChannel", vu);
		data.put("list", list.size() < 10 ? list : list.subList(0, 10));
		return new Value(data);
	}

	/**
	 * 查询渠道学校对应学生用户数，会员数，收费转化率,作业数等
	 * 
	 * @param name
	 * @param phaseCode
	 * @return
	 */
	@RequestMapping(value = "querySchoolList")
	public Value querySchoolList(String name, Integer phaseCode) {
		Integer code = dmnChannelService.getIdChannel(userChannelService.getChannelByUser(Security.getUserId())
				.getCode());
		List<Map<String, Object>> list = aggStuService.queryList(code, name, phaseCode);
		List<Map<String, Object>> hkList = aggClassService.queryList(name, code, phaseCode);
		Map<String, Object> hkNumMap = new HashMap<String, Object>();
		List<Long> schoolIds = new ArrayList<Long>();
		// 作业数
		for (Map map : hkList) {
			String schoolId = String.valueOf(map.get("id_school"));
			Long num = Long.parseLong(String.valueOf(map.get("hk_num")));
			hkNumMap.put(schoolId, num);
		}
		for (Map p : list) {
			String schoolId = String.valueOf(p.get("id_school"));
			Long schoolCode = Long.parseLong(p.get("code_school").toString());
			p.put("hkNum", hkNumMap.get(schoolId));
			schoolIds.add(schoolCode);
		}
		// 学校对应区域地址
		Map<Long, VSchool> schoolMap = schoolConvert.mget(schoolIds);
		for (Map p : list) {
			Long schoolCode = Long.parseLong(p.get("code_school").toString());
			p.put("address", schoolMap.get(schoolCode).getDistrictName());
		}
		return new Value(list);
	}

	/**
	 * 查询渠道商额度
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "queryMyLimit")
	public Value querySchoolList() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		if (uc == null) {
			return new Value(map);
		}
		map.put("userChannel", userChannelConvert.to(uc));
		map.put("starYear", userChannelService.getFirstYearByChannel(uc.getCode()));
		return new Value(map);
	}

	/**
	 * 查询渠道商年度统计
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSBUSINESS" })
	@RequestMapping(value = "statByYear")
	public Value queryStatByYear(Integer year) {
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		Map<String, Object> map = new HashMap<String, Object>();
		if (uc == null) {
			return new Value(map);
		}
		List<MemberPackageOrderChannelSettlement> ocslist = csMemberPackageOrderChannelSettlementService.list(year,
				uc.getCode());
		map.put("list", memberPackageOrderChannelSettlementConvert.to(ocslist));
		map.put("stat", csMemberPackageOrderChannelSettlementService.allYearStat(year, uc.getCode(), null));
		return new Value(map);
	}
}
