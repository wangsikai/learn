package com.lanking.uxb.service.ranking.resource;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkClazz;
import com.lanking.cloud.domain.yoomath.clazz.HomeworkStudentClazz;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionClassRankPraise;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRank;
import com.lanking.cloud.domain.yoomath.stat.DoQuestionSchoolRankPraise;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.CP;
import com.lanking.cloud.sdk.data.CursorPage;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.sdk.value.VCursorPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.core.annotation.ApiAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.ranking.api.DoQuestionClassRankPraiseService;
import com.lanking.uxb.service.ranking.api.DoQuestionClassRankService;
import com.lanking.uxb.service.ranking.api.DoQuestionSchoolRankPraiseService;
import com.lanking.uxb.service.ranking.api.DoQuestionSchoolRankService;
import com.lanking.uxb.service.ranking.convert.DoQuestionClassRankConvert;
import com.lanking.uxb.service.ranking.convert.DoQuestionSchoolRankConvert;
import com.lanking.uxb.service.ranking.form.DoQuestionRankingForm;
import com.lanking.uxb.service.ranking.util.DoQuestionClassRankUtil;
import com.lanking.uxb.service.ranking.value.VDoQuestionRank;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;
import com.lanking.uxb.service.user.convert.UserConvert;
import com.lanking.uxb.service.user.convert.UserConvertOption;
import com.lanking.uxb.service.user.value.VUser;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkClassService;
import com.lanking.uxb.service.zuoye.api.ZyHomeworkStudentClazzService;
import com.lanking.uxb.service.zuoye.convert.ZyHomeworkClazzConvert;
import com.lanking.uxb.service.zuoye.value.VHomeworkClazz;

/**
 * 新版答题排行相关接口
 * 
 * @since 1.4.7
 * @author <a href="mailto:peng.zhao@elanking.com">peng.zhao</a>
 * @version 2017年9月8日
 */
@RestController
@RequestMapping("zy/m/s/ranking/doQuestion/2")
public class ZyMStuDoQuestionRanking2Controller {

	@Autowired
	private ZyHomeworkStudentClazzService zyHkStuClazzService;
	@Autowired
	private ZyHomeworkClassService hcService;
	@Autowired
	private ZyHomeworkClazzConvert hcConvert;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private DoQuestionClassRankService doQuestionClassRankService;
	@Autowired
	private DoQuestionClassRankConvert doQuestionClassRankConvert;
	@Autowired
	private DoQuestionSchoolRankService doQuestionSchoolRankService;
	@Autowired
	private DoQuestionSchoolRankConvert doQuestionSchoolRankConvert;
	@Autowired
	private DoQuestionClassRankPraiseService doQuestionClassRankPraiseService;
	@Autowired
	private DoQuestionSchoolRankPraiseService doQuestionSchoolRankPraiseService;
	@Autowired
	private UserConvert userConvert;
	@Autowired
	private StudentService studentService;

	/**
	 * 排行榜列表接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @param form
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "list", method = { RequestMethod.POST, RequestMethod.GET })
	public Value list(DoQuestionRankingForm form) {
		if (form == null || form.getDay() == null || StringUtils.isBlank(form.getType())) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		List<HomeworkStudentClazz> clazzs = zyHkStuClazzService.listCurrentClazzsHasTeacher(Security.getUserId());
		int clazzCount = clazzs.size();
		if (clazzCount <= 0) {
			data.put("items", Collections.EMPTY_LIST);
			data.put("clazzCount", clazzCount);
			return new Value(data);
		}

		List<Long> clazzIds = new ArrayList<Long>(clazzCount);
		for (HomeworkStudentClazz homeworkStudentClazz : clazzs) {
			clazzIds.add(homeworkStudentClazz.getClassId());
		}
		data.put("clazzCount", clazzCount);

		Map<Long, HomeworkClazz> poMap = hcService.mget(clazzIds);
		List<HomeworkClazz> poList = new ArrayList<HomeworkClazz>(clazzCount);
		for (Long clazzId : clazzIds) {
			poList.add(poMap.get(clazzId));
		}

		// 班级名称format
		List<VHomeworkClazz> vclazzs = hcConvert.to(poList);
		for (VHomeworkClazz v : vclazzs) {
			v.setName(interceptWord(v.getName(), 20));
		}
		data.put("clazzs", vclazzs);

		// 查询时间信息
		Map<String, Integer> timeInfo = DoQuestionClassRankUtil.getNowTime(form.getDay());
		int startDate = timeInfo.get("startDate");
		int endDate = timeInfo.get("endDate");

		if ("class".equals(form.getType())) {
			// 班级排行榜 DoQuestionClassRank
			long classId = form.getClassId() == null ? clazzIds.get(0) : form.getClassId();
			List<DoQuestionClassRank> doQuestionClassRankList = doQuestionClassRankService
					.listDoQuestionClassStatTopN(classId, startDate, endDate, 10);
			// 设置是否被当前用户点赞过
			Map<Long, DoQuestionClassRankPraise> praiseMap = doQuestionClassRankPraiseService
					.getUserPraise(Security.getUserId());
			List<VDoQuestionRank> vranks = doQuestionClassRankConvert.to(doQuestionClassRankList);
			for (VDoQuestionRank v : vranks) {
				if (praiseMap.get(v.getId()) != null) {
					v.setHasPraised(true);
				}
			}

			// 该用户自己的排行
			DoQuestionClassRank meRank = null;
			for (DoQuestionClassRank rank : doQuestionClassRankList) {
				if (rank.getUserId() == Security.getUserId()) {
					meRank = rank;
					break;
				}
			}
			if (meRank == null) {
				meRank = doQuestionClassRankService.findStudentInClassRank(classId, startDate, endDate,
						Security.getUserId());
			}
			if (meRank == null) {
				meRank = new DoQuestionClassRank();
				meRank.setUserId(Security.getUserId());
			}
			VDoQuestionRank meRankVo = doQuestionClassRankConvert.to(meRank);

			if (vranks.size() == 0) {
				vranks = new ArrayList<VDoQuestionRank>();
				// vranks.add(meRankVo);
			}
			data.put("items", vranks);
			data.put("meRank", meRankVo);
		} else if ("school".equals(form.getType())) {
			// 学生信息
			Student studentInfo = (Student) studentService.getUser(Security.getUserId());
			List<DoQuestionSchoolRank> doQuestionSchoolRankList = doQuestionSchoolRankService
					.listDoQuestionSchoolRankTopN(studentInfo.getSchoolId(), startDate, endDate, 10);
			// 设置是否被当前用户点赞过
			Map<Long, DoQuestionSchoolRankPraise> praiseMap = doQuestionSchoolRankPraiseService
					.getUserPraise(Security.getUserId());
			List<VDoQuestionRank> vranks = doQuestionSchoolRankConvert.to(doQuestionSchoolRankList);
			for (VDoQuestionRank v : vranks) {
				if (praiseMap.get(v.getId()) != null) {
					v.setHasPraised(true);
				}
			}

			// 该用户自己的排行
			DoQuestionSchoolRank meRank = null;
			for (DoQuestionSchoolRank rank : doQuestionSchoolRankList) {
				if (rank.getUserId() == Security.getUserId()) {
					meRank = rank;
					break;
				}
			}
			if (meRank == null) {
				meRank = doQuestionSchoolRankService.findStudentInSchoolRank(studentInfo.getSchoolId(), startDate,
						endDate,
						Security.getUserId());
			}
			if (meRank == null) {
				meRank = new DoQuestionSchoolRank();
				meRank.setUserId(Security.getUserId());
			}

			VDoQuestionRank meRankVo = doQuestionSchoolRankConvert.to(meRank);

			if (vranks.size() == 0) {
				vranks = new ArrayList<VDoQuestionRank>();
				// vranks.add(meRankVo);
			}
			data.put("items", vranks);
			data.put("meRank", meRankVo);
			data.put("school", schoolService.get(studentInfo.getSchoolId()).getName());
		}

		return new Value(data);
	}

	/**
	 * 截取指定长度的字符串，每个汉字计算2个，每个数字/英文算一个
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	private String interceptWord(String str, int length) {
		try {
			int counterOfDoubleByte = 0;
			byte b[] = str.getBytes("GBK");
			if (b.length <= length)
				return str;
			for (int i = 0; i < length; i++) {
				if (b[i] < 0)
					counterOfDoubleByte++;
			}
			if (counterOfDoubleByte % 2 == 0)
				return new String(b, 0, length, "GBK");
			else
				return new String(b, 0, length - 1, "GBK");

		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}

	/**
	 * 点赞接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @param rankId
	 * @param type
	 *            class:班级榜 school:校级榜
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "praise", method = { RequestMethod.POST, RequestMethod.GET })
	public Value praise(Long rankId, String type) {
		if (rankId == null || type == null) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		if ("class".equals(type)) {
			// 查询rank是否存在
			DoQuestionClassRank rank = doQuestionClassRankService.get(rankId);
			if (rank == null) {
				return new Value(new IllegalArgException());
			}
			// 用户不能给自己点赞
			if (rank.getUserId() == Security.getUserId()) {
				return new Value(new IllegalArgException());
			}

			// 查询是否点赞过
			long praiseCount = doQuestionClassRankPraiseService.countByRankId(rankId, Security.getUserId());
			if (praiseCount > 0) {
				return new Value(new IllegalArgException());
			}

			// 更新点赞信息
			doQuestionClassRankService.updateClassPraiseCount(rankId, Security.getUserId());

			// 重新取新的点赞数
			rank = doQuestionClassRankService.get(rankId);
			data.put("praiseCount", rank.getPraiseCount());
		} else if ("school".equals(type)) {
			// 查询rank是否存在
			DoQuestionSchoolRank rank = doQuestionSchoolRankService.get(rankId);
			if (rank == null) {
				return new Value(new IllegalArgException());
			}
			// 用户不能给自己点赞
			if (rank.getUserId() == Security.getUserId()) {
				return new Value(new IllegalArgException());
			}

			// 查询是否点赞过
			long praiseCount = doQuestionSchoolRankPraiseService.countByRankId(rankId, Security.getUserId());
			if (praiseCount > 0) {
				return new Value(new IllegalArgException());
			}

			// 更新点赞信息
			doQuestionSchoolRankService.updateSchoolPraiseCount(rankId, Security.getUserId());

			// 重新取新的点赞数
			rank = doQuestionSchoolRankService.get(rankId);
			data.put("praiseCount", rank.getPraiseCount());
		}

		return new Value(data);
	}

	/**
	 * 查询赞我的人列表接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @param rankId
	 * @param type
	 *            class:班级榜 school:校级榜
	 * @param cursor
	 *            游标
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "MS")
	@SuppressWarnings("unchecked")
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "bePraised", method = { RequestMethod.POST, RequestMethod.GET })
	public Value bePraised(Long rankId, String type, long cursor,
			@RequestParam(value = "size", defaultValue = "30") int size) {
		if (rankId == null || type == null) {
			return new Value(new IllegalArgException());
		}
		VCursorPage<VUser> vp = new VCursorPage<VUser>();

		if ("class".equals(type)) {
			CursorPage<Long, DoQuestionClassRankPraise> rankPage = doQuestionClassRankPraiseService
					.getRankPraiseByRankId(rankId, CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, size));
			if (rankPage.isEmpty()) {
				vp.setCursor(cursor);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				vp.setCursor(rankPage.getNextCursor());
				// 转换成user对象
				List<Long> userIds = rankPage.getItems().stream().map(p -> p.getUserId()).collect(Collectors.toList());
				vp.setTotal(userIds.size());
				vp.setItems(getUsers(userIds));
			}
		} else if ("school".equals(type)) {
			CursorPage<Long, DoQuestionSchoolRankPraise> rankPage = doQuestionSchoolRankPraiseService
					.getRankPraiseByRankId(rankId, CP.cursor(cursor == 0 ? Long.MAX_VALUE : cursor, size));
			if (rankPage.isEmpty()) {
				vp.setCursor(cursor);
				vp.setItems(Collections.EMPTY_LIST);
			} else {
				vp.setCursor(rankPage.getNextCursor());
				// 转换成user对象
				List<Long> userIds = rankPage.getItems().stream().map(p -> p.getUserId()).collect(Collectors.toList());
				vp.setTotal(userIds.size());
				vp.setItems(getUsers(userIds));
			}
		}

		return new Value(vp);
	}

	/**
	 * 根据userId取user对象
	 * 
	 * @param userIds
	 * @return list
	 */
	private List<VUser> getUsers(List<Long> userIds) {
		List<VUser> users = Lists.newArrayList();
		UserConvertOption option = new UserConvertOption();
		option.setInitMemberType(true);
		option.setInitPhase(false);
		option.setInitTeaDuty(false);
		option.setInitTeaSubject(false);
		option.setInitTeaTitle(false);
		option.setInitTextbook(false);
		option.setInitTextbookCategory(false);
		option.setInitUserState(false);
		Map<Long, VUser> userMap = userConvert.mget(userIds, option);

		for (VUser value : userMap.values()) {
			users.add(value);
		}

		return users;
	}

	/**
	 * 取消点赞接口
	 * 
	 * @since yoomath(mobile) V1.4.7
	 * @param rankId
	 * @param type
	 *            class:班级榜 school:校级榜
	 * @return {@link Value}
	 */
	@MasterSlaveDataSource(type = "M")
	@ApiAllowed(accessRate = 0)
	@RolesAllowed(userTypes = { "STUDENT" })
	@RequestMapping(value = "cancelPraise", method = { RequestMethod.POST, RequestMethod.GET })
	public Value cancelPraise(Long rankId, String type) {
		if (rankId == null || type == null) {
			return new Value(new IllegalArgException());
		}

		Map<String, Object> data = new HashMap<String, Object>();
		if ("class".equals(type)) {
			// 查询rank是否存在
			DoQuestionClassRank rank = doQuestionClassRankService.get(rankId);
			if (rank == null) {
				return new Value(new IllegalArgException());
			}

			// 查询是否点赞过
			DoQuestionClassRankPraise praise = doQuestionClassRankPraiseService.getRankPraiseByRankId(rankId,
					Security.getUserId());
			if (praise == null) {
				return new Value(new IllegalArgException());
			}

			// 取消点赞
			doQuestionClassRankService.cancelPraise(rankId, Security.getUserId(), praise.getId());

			// 重新取新的点赞数
			rank = doQuestionClassRankService.get(rankId);
			data.put("praiseCount", rank.getPraiseCount());
		} else if ("school".equals(type)) {
			// 查询rank是否存在
			DoQuestionSchoolRank rank = doQuestionSchoolRankService.get(rankId);
			if (rank == null) {
				return new Value(new IllegalArgException());
			}

			// 查询是否点赞过
			DoQuestionSchoolRankPraise praise = doQuestionSchoolRankPraiseService.getRankPraiseByRankId(rankId,
					Security.getUserId());
			if (praise == null) {
				return new Value(new IllegalArgException());
			}

			// 取消点赞
			doQuestionSchoolRankService.cancelSchoolPraise(rankId, Security.getUserId(), praise.getId());

			// 重新取新的点赞数
			rank = doQuestionSchoolRankService.get(rankId);
			data.put("praiseCount", rank.getPraiseCount());
		}

		return new Value(data);
	}
}
