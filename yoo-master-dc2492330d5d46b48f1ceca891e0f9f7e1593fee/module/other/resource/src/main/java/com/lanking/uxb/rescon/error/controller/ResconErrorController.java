package com.lanking.uxb.rescon.error.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lanking.cloud.sdk.data.P;
import com.lanking.cloud.sdk.data.Page;
import com.lanking.cloud.sdk.value.VPage;
import com.lanking.cloud.sdk.value.Value;
import com.lanking.uxb.rescon.error.api.ResconErrorManage;
import com.lanking.uxb.rescon.error.value.VError;
import com.lanking.uxb.rescon.error.value.VErrorUser;

/**
 * 纠错
 * 
 * @author wangsenhao
 * @version 2015年9月21日
 */
@RestController
@RequestMapping("rescon/error")
public class ResconErrorController {
	@Autowired
	private ResconErrorManage resconErrorManage;

	/**
	 * 查询纠错列表
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "queryError")
	public Value queryError(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int pageSize) {
		Page<Map> errorPage = resconErrorManage.queryErrorQuestion(P.index(page, pageSize));
		VPage<VError> vp = new VPage<VError>();
		int tPage = (int) (errorPage.getTotalCount() + pageSize - 1) / pageSize;
		vp.setPageSize(pageSize);
		vp.setCurrentPage(page);
		vp.setTotalPage(tPage);
		vp.setTotal(errorPage.getTotalCount());
		List<Map> list = errorPage.getItems();
		List<VError> vErrorList = new ArrayList<VError>();
		Set<Long> questionIds = new HashSet<Long>();
		for (Map map : list) {
			questionIds.add(Long.parseLong(map.get("question_id").toString()));
		}
		Map<Long, List<VErrorUser>> newMap = resconErrorManage.mQueryError(questionIds);
		for (Map map : list) {
			VError vError = new VError();
			vError.setCode(String.valueOf(map.get("code")));
			vError.setErrorCount(Long.parseLong(map.get("count").toString()));
			vError.setPhaseName(map.get("phasename").toString());
			vError.setTypeName(map.get("typename").toString());
			vError.setErrorUserList(newMap.get(Long.parseLong(map.get("question_id").toString())));
			vError.setQuestionId(Long.parseLong(map.get("question_id").toString()));
			vErrorList.add(vError);
		}
		vp.setItems(vErrorList);
		return new Value(vp);
	}

	/**
	 * 取未处理的最新的一条纠错
	 * 
	 * @param id
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "getlatest")
	public Value getlatestQuestionError() {
		Map map = resconErrorManage.getlatestQuestionError();
		VError vError = new VError();
		vError.setCode(String.valueOf(map.get("code")));
		vError.setErrorCount(Long.parseLong(map.get("count").toString()));
		vError.setPhaseName(map.get("phasename").toString());
		vError.setTypeName(map.get("typename").toString());
		vError.setErrorUserList(resconErrorManage.queryError(Long.parseLong(map.get("question_id").toString())));
		vError.setQuestionId(Long.parseLong(map.get("question_id").toString()));
		return new Value(vError);
	}

}
