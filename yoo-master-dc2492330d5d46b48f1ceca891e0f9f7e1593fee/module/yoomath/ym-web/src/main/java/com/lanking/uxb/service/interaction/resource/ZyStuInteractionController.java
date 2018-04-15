package com.lanking.uxb.service.interaction.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.interaction.Interaction;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.interaction.api.InteractionService;
import com.lanking.uxb.service.interaction.convert.InteractionConvert;
import com.lanking.uxb.service.interaction.value.VInteraction;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;

/**
 * 师生互动学生相关接口
 * 
 * @since 2.0.3
 * @author wangsenhao
 *
 */
@RestController
@RequestMapping("zy/s/interaction")
public class ZyStuInteractionController {
	@Autowired
	private InteractionService interactionService;
	@Autowired
	private InteractionConvert interactionConvert;
	@Autowired
	private ZyHomeworkStudentClazzService hkStuClazzService;

	/**
	 * 学生荣耀榜
	 * 
	 * @param page
	 * @param pageSize
	 * @param type
	 *            type=1 表示是首页的学生荣耀榜<br>
	 *            type=2 表示是学生全部荣耀榜<br>
	 *            type=3 表示是学生自己的荣耀榜
	 * @return
	 */
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public Value query(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int pageSize,
			@RequestParam(defaultValue = "1") int type) {
		// 获取当前学生所在的班级
		List<HomeworkStudentClazz> stuClazzs = hkStuClazzService.listCurrentClazzs(Security.getUserId());
		List<Long> clazzIds = new ArrayList<Long>();
		for (HomeworkStudentClazz h : stuClazzs) {
			clazzIds.add(h.getClassId());
		}
		// 没有班级
		if (CollectionUtils.isEmpty(clazzIds)) {
			return new Value(Collections.EMPTY_LIST);
		}
		// 首页
		if (type == 1) {
			List<Interaction> list = interactionService.queryIndexHonourList(clazzIds);
			return new Value(interactionConvert.to(list));
		} else {
			Page<Interaction> cp = null;
			if (type == 2) {
				cp = interactionService.queryHonourList(clazzIds, P.index(page, pageSize), null);
			} else if (type == 3) {
				// 可能存在班级被删除、关闭或个人被移除，自己的荣誉跟所在班级没有关系
				cp = interactionService.queryHonourList(null, P.index(page, pageSize), Security.getUserId());
			} else {
				return new Value(new IllegalArgException());
			}
			VPage<VInteraction> vp = new VPage<VInteraction>();
			int tPage = (int) (cp.getTotalCount() + pageSize - 1) / pageSize;
			vp.setPageSize(pageSize);
			vp.setCurrentPage(page);
			vp.setTotalPage(tPage);
			vp.setTotal(cp.getTotalCount());
			vp.setItems(interactionConvert.to(cp.getItems()));
			return new Value(vp);
		}
	}
}
