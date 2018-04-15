package com.lanking.uxb.service.web.resource;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.common.baseData.Textbook;
import com.lanking.cloud.domain.common.baseData.TextbookCategory;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoo.version.YoomathVersionLog;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.TextbookCategoryService;
import com.lanking.uxb.service.code.api.TextbookService;
import com.lanking.uxb.service.code.convert.TextbookCategoryConvert;
import com.lanking.uxb.service.code.convert.TextbookConvert;
import com.lanking.uxb.service.code.value.VTextbook;
import com.lanking.uxb.service.code.value.VTextbookCategory;
import com.lanking.uxb.service.honor.api.UserHonorService;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.zuoye.api.ZyCommonService;
import com.lanking.uxb.service.zuoye.api.ZyMottoService;
import com.lanking.uxb.service.zuoye.api.ZyYoomathVersionLogService;
import com.lanking.uxb.service.zuoye.convert.ZyVersionLogConvert;

@ApiAllowed
@RestController
@RequestMapping("zy/common")
public class ZyCommonController {

	@Autowired
	private ZyMottoService zyMottoService;
	@Autowired
	private ZyCommonService zyCommonService;
	@Autowired
	private ZyYoomathVersionLogService versionService;
	@Autowired
	private ZyVersionLogConvert versionLogConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private TextbookCategoryService tbcService;
	@Autowired
	private TextbookCategoryConvert tbcConvert;
	@Autowired
	private TextbookService textBookService;
	@Autowired
	private TextbookConvert textBookConvert;
	@Autowired
	private UserHonorService userHonorService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "loginPageData" }, method = RequestMethod.GET)
	public Value loginPageData(@RequestParam(value = "needCounts", required = false) boolean needCounts) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (needCounts) {
			data.put("users", String.format("%05d",
					(Integer) (zyCommonService.getUsers() + Env.getInt("homework.user.basecount"))));// 用户数量

			Integer homeworks = zyCommonService.getHomeWorks() + Env.getInt("homework.basecount");
			data.put("homeworks", String.format("%05d", homeworks));// 作业数量
			data.put("motto", zyMottoService.getOne());
		}
		// 版本更新data相关
		List<YoomathVersionLog> versionLogList = versionService.latestVersionLogs(1);
		if (CollectionUtils.isNotEmpty(versionLogList)) {
			data.put("versionLog", versionLogList == null ? null : versionLogConvert.to(versionLogList.get(0)));
		} else {
			data.put("versionLog", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "header" }, method = RequestMethod.GET)
	public Value unloginHeader() {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("users",
				String.format("%05d", (Integer) (zyCommonService.getUsers() + Env.getInt("homework.user.basecount"))));// 用户数量

		Integer homeworks = zyCommonService.getHomeWorks() + Env.getInt("homework.basecount");
		data.put("homeworks", String.format("%05d", homeworks));// 作业数量
		data.put("motto", zyMottoService.getOne());
		// 版本更新data相关
		List<YoomathVersionLog> versionLogList = versionService.latestVersionLogs(1);
		if (CollectionUtils.isNotEmpty(versionLogList)) {
			data.put("versionLog", versionLogList == null ? null : versionLogConvert.to(versionLogList.get(0)));
		} else {
			data.put("versionLog", Collections.EMPTY_LIST);
		}
		return new Value(data);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = { "moreVersion" }, method = RequestMethod.GET)
	public Value moreVersion(Integer limit) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		data.put("motto", zyMottoService.getOne());
		data.put("versionLogs", versionLogConvert.to(versionService.latestVersionLogs(limit)));
		return new Value(data);
	}

	/**
	 * 获取可选版本教材数据
	 * 
	 * @since yoomath V1.7
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "cateBooks", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cateBooks(@RequestParam(value = "subjectCode", defaultValue = "0") int subjectCode) {
		Map<String, Object> data = new HashMap<String, Object>(2);
		if (subjectCode == 0) {
			if (Security.getUserType() == UserType.TEACHER) {
				// 未设置此参数则去当前用户信息
				Teacher teacher = (Teacher) teacherService.getUser(UserType.TEACHER, Security.getUserId());
				List<TextbookCategory> tbcList = tbcService.find(Product.YOOMATH, teacher.getPhaseCode());
				List<VTextbookCategory> vtbcList = tbcConvert.to(tbcList);
				Set<Integer> cateCodes = new HashSet<Integer>(vtbcList.size());
				for (VTextbookCategory v : vtbcList) {
					cateCodes.add(v.getCode());
				}
				if (cateCodes.size() > 0) {
					List<Textbook> tbList = textBookService.find(Product.YOOMATH, teacher.getPhaseCode(),
							teacher.getSubjectCode(), cateCodes);
					List<VTextbook> vtbList = textBookConvert.to(tbList);
					data.put("textbooks", vtbList);
				} else {
					data.put("textbooks", Collections.EMPTY_LIST);
				}
				data.put("categories", vtbcList);
			}

		} else {
			int phaseCode = subjectCode / 100;
			List<TextbookCategory> tbcList = tbcService.find(Product.YOOMATH, phaseCode);
			List<VTextbookCategory> vtbcList = tbcConvert.to(tbcList);
			Set<Integer> cateCodes = new HashSet<Integer>(vtbcList.size());
			for (VTextbookCategory v : vtbcList) {
				cateCodes.add(v.getCode());
			}
			if (cateCodes.size() > 0) {
				List<Textbook> tbList = textBookService.find(Product.YOOMATH, phaseCode, subjectCode, cateCodes);
				List<VTextbook> vtbList = textBookConvert.to(tbList);
				data.put("textbooks", vtbList);
			} else {
				data.put("textbooks", Collections.EMPTY_LIST);
			}
			data.put("categories", vtbcList);
		}

		return new Value(data);
	}

	/**
	 * 获取可选版本教材数据
	 * 
	 * @since yoomath V1.7
	 * @return {@link Value}
	 */
	@RolesAllowed(userTypes = { "TEACHER", "STUDENT" })
	@RequestMapping(value = "setNotUpgrade", method = { RequestMethod.POST, RequestMethod.GET })
	public Value setNotUpgrade() {
		userHonorService.uptUserHonor(Security.getUserId(), false);
		return new Value();
	}
}
