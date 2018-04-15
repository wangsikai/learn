package com.lanking.uxb.service.user.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.dailyPractise.DailyPracticeSettings;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyDailyPracticeSettingsService;

/**
 * 悠数学移动端(学生用户中心接口)
 * 
 * @since yoomath(mobile) V1.0.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年11月9日
 */
@RestController
@RequestMapping("zy/m/s/uc")
public class ZyMStuUserCenterController extends ZyMBaseUserCenterController {

	@Autowired
	private ZyDailyPracticeSettingsService dailyPracticeSettingsService;

	/**
	 * 获取用户当前设置的版本下的教材列表
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = { "textbookList" }, method = { RequestMethod.POST, RequestMethod.GET })
	public Value textbookList() {
		return new Value(textbookList(Security.getUserType(), Security.getUserId(), 3));
	}

	/**
	 * 获取用户当前设置的教材的章节数据
	 * 
	 * @since yoomath(mobile) V1.0.0
	 * @param initProcess
	 *            是否返回进度(学生使用)
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "sectionTree", method = { RequestMethod.POST, RequestMethod.GET })
	public Value sectionTree(
			@RequestParam(value = "initDailyPracticeProcess", defaultValue = "false") boolean initDailyPracticeProcess) {
		Map<String, Object> dataMap = new HashMap<String, Object>(initDailyPracticeProcess ? 2 : 1);
		Student student = (Student) studentService.getUser(com.lanking.cloud.domain.yoo.user.UserType.STUDENT,
				Security.getUserId());
		if (student.getTextbookCode() != null) {
			List<Section> sections = sectionServce.findByTextbookCode(student.getTextbookCode());
			List<VSection> sectionTree = sectionConvert.assemblySectionTree(sectionConvert.to(sections));
			dataMap.put("sectionTree", sectionTree);
			if (initDailyPracticeProcess) {
				DailyPracticeSettings settings = dailyPracticeSettingsService.findByTextbookCode(Security.getUserId(),
						student.getTextbookCode());
				if (settings == null || settings.getSectionCode() == null) {
					long sectionCode = 0;
					if (sectionTree.get(0).getChildren().size() == 0) {
						sectionCode = sectionTree.get(0).getCode();
					} else {
						if (sectionTree.get(0).getChildren().get(0).getChildren().size() == 0) {
							sectionCode = sectionTree.get(0).getChildren().get(0).getCode();
						} else {
							sectionCode = sectionTree.get(0).getChildren().get(0).getChildren().get(0).getCode();
						}
					}
					dataMap.put("dailyPracticeSectionCode", sectionCode);
				} else {
					dataMap.put("dailyPracticeSectionCode", settings.getSectionCode());
				}
			}
		}
		return new Value(dataMap);
	}
}
