package com.lanking.uxb.service.interaction.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.interaction.Interaction;
import com.lanking.cloud.domain.yoomath.interaction.InteractionStatus;
import com.lanking.cloud.sdk.bean.Status;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.edu.ex.ClazzException;
import com.lanking.uxb.service.interaction.api.InteractionService;
import com.lanking.uxb.service.interaction.convert.InteractionConvert;
import com.lanking.uxb.service.interaction.value.VInteraction;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 师生互动教师相关接口
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/t/interaction")
public class ZyTeaInteractionController {
	@Autowired
	private InteractionService interactionService;
	@Autowired
	private InteractionConvert interactionConvert;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;
	@Autowired
	private ZyHomeworkClassService zyHomeworkClassService;
	@Autowired
	private ZyHomeworkStudentClazzService zyHomeworkStudentClazzService;

	/**
	 * 师生互动列表
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int pageSize) {
		Page<Interaction> cp = interactionService.query(Security.getUserId(), P.index(page, pageSize));
		VPage<VInteraction> vp = new VPage<VInteraction>();
		int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(cp.getTotalCount());
		vp.setItems(interactionConvert.to(cp.getItems()));
		return new Value(vp);
	}

	/**
	 * 首页互动列表
	 * 
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "queryIndex", method = { RequestMethod.GET, RequestMethod.POST })
	public Value queryIndex() {
		return new Value(interactionConvert.to(interactionService.queryIndex(Security.getUserId())));
	}

	/**
	 * 首页删除互动
	 * 
	 * @param id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "delIndexInteraction", method = { RequestMethod.GET, RequestMethod.POST })
	public Value delIndexInteraction(Long id) {
		interactionService.delIndexInteraction(id);
		return new Value();
	}

	/**
	 * 发送奖励
	 * 
	 * @param id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "sentReward", method = { RequestMethod.GET, RequestMethod.POST })
	public Value sentReward(Long id) {
		Interaction interaction = interactionService.get(id);
		HomeworkClazz clazz = zyHomeworkClassService.get(interaction.getClassId());
		if (clazz.getStatus() == Status.ENABLED) {
			// 该学生已被移出班级，不可发送奖励
			if (zyHomeworkStudentClazzService.find(clazz.getId(), interaction.getStudentId()) == null) {
				return new Value(new ClazzException(ClazzException.STUDENT_REMOVE_CLASS));
			}
			interactionService.updateStatus(id, InteractionStatus.SENT_PRAISE);
		} else {
			// 当前班级已删除或关闭
			return new Value(new ClazzException(ClazzException.CLASS_CLOSE_DELTE));
		}
		return new Value();
	}

	/**
	 * 忽略奖励
	 * 
	 * @param id
	 * @return
	 */
	@RolesAllowed(userTypes = { "TEACHER" })
	@RequestMapping(value = "ignoreReward", method = { RequestMethod.GET, RequestMethod.POST })
	public Value ignoreReward(Long id) {
		interactionService.updateStatus(id, InteractionStatus.IGNORE_PRAISE);
		return new Value();
	}

}
