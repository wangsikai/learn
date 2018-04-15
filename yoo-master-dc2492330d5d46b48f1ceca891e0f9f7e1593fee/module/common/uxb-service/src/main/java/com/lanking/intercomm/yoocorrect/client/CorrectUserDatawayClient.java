package com.lanking.intercomm.yoocorrect.client;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.dto.AddCorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserAuthRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.ModifyCorrectUserRequest;

/**
 * 小悠快批-用户服务客户端.
 * 
 * @author wanlong.che
 *
 */
@FeignClient("${correct-server.name}")
public interface CorrectUserDatawayClient {

	/**
	 * 获取快批对应用户.
	 * 
	 * @param uxbUserId
	 *            UXB用户ID
	 * @return
	 */
	@RequestMapping(value = "/correctUser/findByUXBUserId", method = { RequestMethod.POST })
	Value get(@RequestParam("uxbUserId") Long uxbUserId, @RequestParam("canUsedBalance") Boolean canUsedBalance);

	/**
	 * <p>
	 * Description:查询小优快用户列表
	 * <p>
	 * 
	 * @date: 2018年3月13日
	 * @author: pengcheng.yu
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/correctUser/list", method = RequestMethod.POST)
	Value list(CorrectUserRequest request);

	/**
	 * <p>
	 * Description:添加快批用户
	 * <p>
	 * 
	 * @date: 2018年3月13日
	 * @author: pengcheng.yu
	 * @param accountName
	 * @param passWord
	 * @return
	 */
	@RequestMapping(value = "/correctUser/add", method = RequestMethod.POST)
	Value add(AddCorrectUserRequest request);

	@RequestMapping(value = "/correctUser/modify", method = RequestMethod.POST)
	Value modify(ModifyCorrectUserRequest request);

	@RequestMapping(value = "/correctUser/get", method = { RequestMethod.POST })
	Value get2(@RequestParam("correctUserId") Long correctUserId, @RequestParam("userType") UserType userType);

	@RequestMapping(value = "/correctUser/auth", method = RequestMethod.POST)
	Value auth(CorrectUserAuthRequest request);

	@RequestMapping(value = "/correctUser/authCorrectUsers", method = RequestMethod.POST)
	Value queryAuthCorrectUsers();

	/**
	 * 获取用户统计数据.
	 * 
	 * @param userId
	 *            快批用户
	 * @return
	 */
	@RequestMapping(value = "/correctUser/getUserStatData", method = RequestMethod.POST)
	Value getUserStatData(@RequestParam("userId") Long userId);

	/**
	 * 提交资格证认证.
	 * 
	 * @param userId
	 *            快批用户
	 * @param imgId
	 *            认证图片
	 * @return
	 */
	@RequestMapping(value = "/correctUser/submitQualificationAuth", method = RequestMethod.POST)
	Value submitQualificationAuth(@RequestParam("userId") Long userId, @RequestParam("imgId") Long imgId);

	/**
	 * 提交身份认证.
	 * 
	 * @param userId
	 *            快批用户
	 * @param imgId
	 *            认证图片
	 * @param realName
	 *            真实姓名
	 * @param idCard
	 *            身份证
	 * @return
	 */
	@RequestMapping(value = "/correctUser/submitIDCardAuth", method = RequestMethod.POST)
	Value submitIDCardAuth(@RequestParam("userId") Long userId, @RequestParam("imgId") Long imgId,
			@RequestParam("realName") String realName, @RequestParam("idCard") String idCard);

	/**
	 * 提交手机认证.
	 * 
	 * @param userId
	 *            快批用户
	 * @param mobile
	 *            手机号
	 * @return
	 */
	@RequestMapping(value = "/correctUser/submitMobileAuth", method = RequestMethod.POST)
	Value submitMobileAuth(@RequestParam("userId") Long userId, @RequestParam("mobile") String mobile);

	/**
	 * 提交学校.
	 * 
	 * @param userId
	 *            快批用户
	 * @param schoolId
	 *            学校ID
	 * @param schoolName
	 *            学校名称
	 * @return
	 */
	@RequestMapping(value = "/correctUser/setSchool", method = RequestMethod.POST)
	Value setSchool(@RequestParam("userId") Long userId, @RequestParam("schoolId") Long schoolId,
			@RequestParam("schoolName") String schoolName);

	/**
	 * 提交阶段.
	 * 
	 * @param phaseId
	 *            阶段ID
	 * @return
	 */
	@RequestMapping(value = "/correctUser/setPhase", method = RequestMethod.POST)
	Value setPhase(@RequestParam("userId") Long userId, @RequestParam("phaseId") Long phaseId);

	/**
	 * 查找绑定手机的用户个数.
	 * 
	 * @param userId
	 *            当前用户ID
	 * @param mobile
	 *            手机号
	 * @return
	 */
	@RequestMapping(value = "/correctUser/countUserByMobile", method = { RequestMethod.POST })
	Value countUserByMobile(@RequestParam("userId") Long userId, @RequestParam("mobile") String mobile);
}
