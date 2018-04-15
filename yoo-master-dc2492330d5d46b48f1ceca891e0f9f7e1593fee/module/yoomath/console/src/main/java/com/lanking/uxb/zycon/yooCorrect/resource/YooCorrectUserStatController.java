package com.lanking.uxb.zycon.yooCorrect.resource;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.base.session.Session;
import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.data.Pageable;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.client.CorrectConsoleConfigDatawayClient;
import com.lanking.intercomm.yoocorrect.dto.ConsoleCorrectUserStatData;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserResponse;
import com.lanking.uxb.zycon.operation.api.ZycSessionService;
import com.lanking.uxb.zycon.yooCorrect.value.VCorrectUser;

@RestController
@RequestMapping(value = "correct/userStat")
public class YooCorrectUserStatController {

	@Autowired
	private CorrectConsoleConfigDatawayClient correctConsoleConfigDatawayClient;
	@Autowired
	private ZycSessionService sessionService;
	
	/**
	 * 查询配置详情
	 * 
	 * @return
	 */
	@RequestMapping(value = "userDayStats", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserDayStats(@RequestParam(value = "size", defaultValue = "20") Integer size,
			@RequestParam(value = "page", defaultValue = "1") Integer page) {
		Value value = correctConsoleConfigDatawayClient.getUserDayStats(size, page);
		ConsoleCorrectUserStatData responseData = JSON.parseObject(value.getRet().toString(), ConsoleCorrectUserStatData.class);
		
		Map<String, Object> data = new HashMap<>();
		data.put("statPage", responseData.getVStatPage());
		
		List<Long> onlineUserIds = Lists.newArrayList();
		// 实时在线用户
		Pageable pageable = P.index(1, 1000);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -10);
		Page<Session> sessions = sessionService.getSessionByUserId(responseData.getUxbUserIds(), pageable, calendar.getTime());
		if (sessions.getItems() != null && !sessions.getItems().isEmpty()) {
			onlineUserIds = sessions.getItems().stream().map(p -> p.getUserId()).collect(Collectors.toList());
		}
		
		// 实时批改中用户
		List<Long> correctingUserIds = responseData.getCorrectingUxbUserIds();
		data.put("onlineUserIds", onlineUserIds);
		data.put("correctingUserIds", correctingUserIds);
		data.put("allQuestionCount", responseData.getAllQuestionCount());
		data.put("blankCount", responseData.getBlankCount());
		data.put("answerCount", responseData.getAnswerCount());
		
		return new Value(data);
	}
	
	/**
	 * 取用户的月度统计信息
	 * 
	 * @param userId
	 *            小悠系统的userId
	 * @return
	 */
	@RequestMapping(value = "userMonthStats", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserMonthStats(Long userId) {

		return correctConsoleConfigDatawayClient.getUserMonthStats(userId);
	}
	
	/**
	 * 取指定的用户信息
	 * 
	 * @param userId
	 *            uxb系统中的用户id
	 * @return
	 */
	@RequestMapping(value = "onlineUserInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getOnlineUserInfo(@RequestParam(value="userIds", required = false) List<Long> userIds) {
		Map<String, Object> data = new HashMap<>();
		if (userIds != null && !userIds.isEmpty()) {
			Map map = JSON.parseObject(correctConsoleConfigDatawayClient.getUserInfo(userIds).getRet().toString(), Map.class);
			List<CorrectUserResponse> correctUserResponses = JSON.parseArray(map.get("users").toString(), CorrectUserResponse.class);
			// 实时在线用户
			Pageable pageable = P.index(1, 1000);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -10);
			Page<Session> sessions = sessionService.getSessionByUserId(
					correctUserResponses.stream().map(p -> p.getUserId()).collect(Collectors.toList()), pageable,
					calendar.getTime());
			List<VCorrectUser> userInfos = Lists.newArrayList();
			for (CorrectUserResponse userResponse : correctUserResponses) {
				VCorrectUser vuser = new VCorrectUser();
				vuser.setAccountName(userResponse.getAccountName());
				vuser.setRealName(userResponse.getRealname());
				
				for (Session value : sessions) {
					if (userResponse.getUserId().longValue() == value.getUserId()) {
						vuser.setActiveAt(value.getActiveAt());
						vuser.setDeviceType(value.getDeviceType().getTitle());
					}
				}
				userInfos.add(vuser);
			}
			
			data.put("userInfos", userInfos);
		} else {
			data.put("userInfos", Lists.newArrayList());
		}
		
		return new Value(data);
	}
	
	/**
	 * 取指定的用户信息
	 * 
	 * @param userId
	 *            uxb系统中的用户id
	 * @return
	 */
	@RequestMapping(value = "userInfo", method = { RequestMethod.POST, RequestMethod.GET })
	public Value getUserInfo(@RequestParam(value="userIds", required = false) List<Long> userIds) {
		Map<String, Object> data = new HashMap<>();
		if (userIds != null && !userIds.isEmpty()) {
			Map map = JSON.parseObject(correctConsoleConfigDatawayClient.getUserInfo(userIds).getRet().toString(), Map.class);
			List<CorrectUserResponse> correctUserResponses = JSON.parseArray(map.get("users").toString(), CorrectUserResponse.class);
			// 实时在线用户
			Pageable pageable = P.index(1, 1000);
			Page<Session> sessions = sessionService.getSessionByUserId(
					correctUserResponses.stream().map(p -> p.getUserId()).collect(Collectors.toList()), pageable,
					null);
			List<VCorrectUser> userInfos = Lists.newArrayList();
			for (CorrectUserResponse userResponse : correctUserResponses) {
				VCorrectUser vuser = new VCorrectUser();
				vuser.setAccountName(userResponse.getAccountName());
				vuser.setRealName(userResponse.getRealname());
				
				for (Session value : sessions) {
					if (userResponse.getUserId().longValue() == value.getUserId()) {
						vuser.setActiveAt(value.getActiveAt());
						vuser.setDeviceType(value.getDeviceType().getTitle());
					}
				}
				userInfos.add(vuser);
			}
			
			data.put("userInfos", userInfos);
		} else {
			data.put("userInfos", Lists.newArrayList());
		}
		
		return new Value(data);
	}
	
	/**
	 * 统计指定月份的用户统计信息
	 * 
	 * @param date
	 *            指定年月,格式yyyy-mm
	 * @return
	 */
	@RequestMapping(value = "userMonthStat", method = { RequestMethod.POST, RequestMethod.GET })
	public Value userMonthStat(String date) {
		String[] dates = date.split("-");
		int year = Integer.parseInt(dates[0]);
		int month = Integer.parseInt(dates[1]);
		// 调用统计
		correctConsoleConfigDatawayClient.userMonthStat(year, month);
		return new Value();
	}
}
