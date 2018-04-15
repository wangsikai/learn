package com.lanking.uxb.channelSales.user.resource;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.support.channelSales.user.ChannelUserOperateLog;
import com.lanking.cloud.domain.support.common.auth.ConsoleUser;
import com.lanking.cloud.domain.yoo.channel.UserChannel;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.ex.AbstractException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.channelSales.channel.api.CsUserChannelService;
import com.lanking.uxb.channelSales.openmember.api.CsUserMemberService;
import com.lanking.uxb.channelSales.user.api.CsUserManageService;
import com.lanking.uxb.channelSales.user.convert.ChannelUserOperateLogConvert;
import com.lanking.uxb.channelSales.user.form.CsUserQuery;
import com.lanking.uxb.service.adminSecurity.api.ConsoleUserService;
import com.lanking.uxb.service.adminSecurity.support.ConsoleRolesAllowed;
import com.lanking.uxb.service.session.api.impl.Security;

/**
 * 渠道商--用户管理
 * 
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("channelSales/userManage")
public class CsUserManageController {
	@Autowired
	private CsUserChannelService userChannelService;
	@Autowired
	private CsUserManageService userManageService;
	@Autowired
	private ChannelUserOperateLogConvert logConvert;
	@Autowired
	private ConsoleUserService consoleUserService;
	@Autowired
	private CsUserMemberService userMemberService;

	/**
	 * 查询渠道商用户列表
	 * 
	 * @param query
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "query")
	public Value query(CsUserQuery query) {
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		// 班级页面的成员信息，查询时候不过滤渠道
		if (query.getClassId() == null) {
			if (uc != null) {
				query.setChannelCode(uc.getCode());
			} else {
				try {
					// 超级管理员直接可以访问
					ConsoleUser consoleUser = consoleUserService.get(Security.getUserId());
					if (consoleUser.getSystemId() != 0) {
						return new Value(new VPage<Map>());
					}
				} catch (Exception e) {
					return new Value(new VPage<Map>());
				}
			}
		}
		Page<Map> userPage = userManageService.query(query, P.index(query.getPage(), query.getPageSize()));
		int tPage = (int) (userPage.getTotalCount() + query.getPageSize() - 1) / query.getPageSize();
		VPage<Map> vp = new VPage<Map>();
		vp.setPageSize(query.getPageSize());
		vp.setCurrentPage(query.getPage());
		vp.setTotalPage(tPage);
		vp.setTotal(userPage.getTotalCount());
		List<Map> map = userPage.getItems();
		List<Integer> codes = new ArrayList<Integer>();
		List<Long> userIds = new ArrayList<Long>(map.size());
		for (Map m : map) {
			m.put("id", Long.parseLong(String.valueOf(m.get("id"))));
			m.put("account_id", Long.parseLong(String.valueOf(m.get("account_id"))));
			Integer channelCode = Integer.parseInt(String.valueOf(m.get("user_channel_code")));
			codes.add(channelCode);
			// 当前渠道名称
			m.put("currentChannelName", uc == null ? "" : uc.getName());
			userIds.add(Long.parseLong(String.valueOf(m.get("id"))));
		}
		Map<Integer, UserChannel> channelMap = userChannelService.mget(codes);

		// 查找班级
		Map<Long, List<HomeworkClazz>> hcMap = new HashMap<Long, List<HomeworkClazz>>();
		if (query.getUserType() == UserType.TEACHER && userIds.size() > 0) {
			hcMap = userManageService.queryTeacherHomeworkClazzs(userIds);
		} else if (query.getUserType() == UserType.STUDENT && userIds.size() > 0) {
			hcMap = userManageService.queryStudentHomeworkClazzs(userIds);
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_YEAR, -1);
		for (Map m : map) {
			Integer channelCode = Integer.parseInt(String.valueOf(m.get("user_channel_code")));
			// 用户渠道名称
			m.put("channelName", channelMap.get(channelCode).getName());
			long userId = (Long) m.get("id");
			List<HomeworkClazz> hcs = hcMap.get(userId);
			String classname = "";
			if (CollectionUtils.isNotEmpty(hcs)) {
				for (HomeworkClazz hc : hcs) {
					classname += hc.getName() + "、";
				}
				classname = classname.substring(0, classname.length() - 1);
			}
			m.put("classname", classname); // 班级名称
			Date endAt = (Date) m.get("end_at");
			if (endAt != null && cal.getTime().getTime() > endAt.getTime()) {
				m.put("start_at", null);
				m.put("end_at", null);
			}
		}
		vp.setItems(userPage.getItems());
		return new Value(vp);
	}

	/**
	 * 操作记录
	 * 
	 * @param userId
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "operateLog")
	public Value operateLog(long userId) {
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		List<ChannelUserOperateLog> list = userManageService.findLogList(userId, uc.getCode());
		return new Value(logConvert.to(list));
	}

	/**
	 * 重置密码
	 * 
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN", "CSBUSINESS" })
	@RequestMapping(value = "resetPassword")
	public Value resetPassword(long userId) {
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		userManageService.resetPassword(userId, uc.getCode(), Security.getUserId());
		return new Value();
	}

	/**
	 * 导出学生列表数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "exportUserList")
	public Value exportUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		CsUserQuery query = new CsUserQuery();
		String classId = request.getParameter("classId");
		String accountName = request.getParameter("accountName");
		String name = request.getParameter("name");
		UserChannel uc = userChannelService.getChannelByUser(Security.getUserId());
		query.setChannelCode(uc.getCode());
		query.setClassId(Long.parseLong(classId));
		query.setAccountName(accountName);
		query.setName(name);
		List<Map> list = userManageService.query(query);
		HSSFWorkbook wb = userManageService.exportUserList(list);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=classUser.xls");
		OutputStream ouputStream = response.getOutputStream();
		wb.write(ouputStream);
		ouputStream.flush();
		ouputStream.close();
		return new Value();
	}

	/**
	 * 关闭会员.
	 * 
	 * @param userIds
	 *            用户ID集合
	 * @return
	 */
	@ConsoleRolesAllowed(roleCodes = { "CSADMIN" })
	@RequestMapping(value = "closeMember")
	public Value closeMember(Long[] userIds) {
		try {
			userMemberService.closeMember(userIds);
			return new Value();
		} catch (AbstractException e) {
			return new Value(e);
		}
	}
}
