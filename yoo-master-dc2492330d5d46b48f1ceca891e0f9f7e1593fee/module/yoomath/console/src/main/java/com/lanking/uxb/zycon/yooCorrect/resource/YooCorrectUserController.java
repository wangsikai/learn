package com.lanking.uxb.zycon.yooCorrect.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.masterslave.MasterSlaveDataSource;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Account;
import com.lanking.cloud.domain.yoo.account.api.GetType;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.intercomm.yoocorrect.dto.AddCorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserAuthRequest;
import com.lanking.intercomm.yoocorrect.dto.CorrectUserRequest;
import com.lanking.intercomm.yoocorrect.dto.ModifyCorrectUserRequest;
import com.lanking.intercomm.yoocorrect.service.CorrectUserDatawayService;
import com.lanking.uxb.core.annotation.MemberAllowed;
import com.lanking.uxb.core.annotation.RolesAllowed;
import com.lanking.uxb.service.account.ex.AccountException;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.form.RegisterForm;

/**
 * <p>
 * Description:
 * <p>
 * 
 * @author pengcheng.yu
 * @date 2018年3月13日
 * @since 小优秀快批
 */
@RestController
@RequestMapping(value = "yoo/correct")
public class YooCorrectUserController {

	@Autowired
	CorrectUserDatawayService CorrectUserDatawayService;

	@Autowired
	@Qualifier("accountService")
	private AccountService accountService;

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	public Value list(CorrectUserRequest request) {
		Value value = CorrectUserDatawayService.list(request);
		// 获取实名认证和教师认证的图片地址
		// JSONObject jsonObject = (JSONObject) value.getRet();
		// JSONArray array = (JSONArray) jsonObject.get("correctUserList");
		// if(array.size()>0){
		// for(int i=0;i<array.size();i++){
		// JSONObject correctUser = array.getJSONObject(i);
		// List<String> idCardImgUrls = null;
		// List<String> qualificationImgUrls = null;
		// if(correctUser.containsKey("idCardImgs")){
		// JSONArray idCardImgs = (JSONArray) correctUser.get("idCardImgs");
		// if(null != idCardImgs && idCardImgs.size()>0){
		// List<Long> imgIds = new ArrayList<Long>();
		// for(int index=0;index<idCardImgs.size();index++){
		// Long imgId = idCardImgs.getLongValue(index);
		// imgIds.add(imgId);
		// }
		// idCardImgUrls = FileUtil.getUrl(imgIds);
		// }
		// }
		// if(correctUser.containsKey("qualificationImgs")){
		// JSONArray qualificationImgs = (JSONArray)
		// correctUser.get("qualificationImgs");
		// if(null != qualificationImgs && qualificationImgs.size()>0){
		// List<Long> imgIds = new ArrayList<Long>();
		// for(int index=0;index<qualificationImgs.size();index++){
		// Long imgId = qualificationImgs.getLongValue(index);
		// imgIds.add(imgId);
		// }
		// qualificationImgUrls = FileUtil.getUrl(imgIds);
		// }
		// }
		// correctUser.put("idCardImgs", idCardImgUrls);
		// correctUser.put("qualificationImgUrls", qualificationImgUrls);
		// }
		// }
		return value;
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/add", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	@MasterSlaveDataSource(type = "M")
	public Value add(AddCorrectUserRequest request) {
		return CorrectUserDatawayService.add(request);

	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/get", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	public Value get(Long correctUserId) {
		Value value = CorrectUserDatawayService.get2(correctUserId, null);
		JSONObject correctUser = (JSONObject) value.getRet();
		List<String> idCardImgUrls = null;
		List<String> qualificationImgUrls = null;
		if (correctUser.containsKey("idCardImgs")) {
			JSONArray idCardImgs = (JSONArray) correctUser.get("idCardImgs");
			if (null != idCardImgs && idCardImgs.size() > 0) {
				List<Long> imgIds = new ArrayList<Long>();
				for (int index = 0; index < idCardImgs.size(); index++) {
					Long imgId = idCardImgs.getLongValue(index);
					imgIds.add(imgId);
				}
				idCardImgUrls = FileUtil.getUrl(imgIds);
			}
		}
		if (correctUser.containsKey("qualificationImgs")) {
			JSONArray qualificationImgs = (JSONArray) correctUser.get("qualificationImgs");
			if (null != qualificationImgs && qualificationImgs.size() > 0) {
				List<Long> imgIds = new ArrayList<Long>();
				for (int index = 0; index < qualificationImgs.size(); index++) {
					Long imgId = qualificationImgs.getLongValue(index);
					imgIds.add(imgId);
				}
				qualificationImgUrls = FileUtil.getUrl(imgIds);
			}
		}
		correctUser.put("idCardImgUrls", idCardImgUrls);
		correctUser.put("qualificationImgUrls", qualificationImgUrls);
		return value;
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/modify", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	public Value modify(ModifyCorrectUserRequest request) {
		return CorrectUserDatawayService.modify(request);
	}

	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/auth", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	public Value auth(CorrectUserAuthRequest request) {
		return CorrectUserDatawayService.auth(request);
	}

	/**
	 * <p>
	 * Description:查询需要认证审核的批改用户
	 * <p>
	 * 
	 * @date: 2018年3月15日
	 * @author: pengcheng.yu
	 * @param request
	 * @return
	 */
	@RolesAllowed(anyone = true)
	@RequestMapping(value = "/authCorrectUsers", method = { RequestMethod.GET, RequestMethod.POST })
	@MemberAllowed
	public Value queryAuthCorrectUsers() {
		return CorrectUserDatawayService.queryAuthCorrectUsers();
	}
}
