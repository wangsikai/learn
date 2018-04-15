package com.lanking.uxb.service.honor.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.honor.userTask.UserTaskLog;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.honor.value.VUserTaskLog;

/**
 * @author xinyu.zhou
 * @since 4.0.0
 */
@Component
public class UserTaskLogConvert extends Converter<VUserTaskLog, UserTaskLog, Long> {
	@Override
	protected Long getId(UserTaskLog userTaskLog) {
		return userTaskLog.getId();
	}

	@Override
	protected VUserTaskLog convert(UserTaskLog userTaskLog) {
		VUserTaskLog v = new VUserTaskLog();
		v.setId(userTaskLog.getId());
		v.setCoins(userTaskLog.getCoins());
		v.setCreateAt(userTaskLog.getCreateAt());
		v.setGrowth(userTaskLog.getGrowth());
		v.setStar(userTaskLog.getStar());
		v.setType(userTaskLog.getTaskType());
		v.setUserId(userTaskLog.getUserId());
		v.setStatus(userTaskLog.getStatus());

		if (StringUtils.isNotBlank(userTaskLog.getContent())) {
			JSONObject contentJSONObj = JSONObject.parseObject(userTaskLog.getContent());
			JSONArray itemArray = contentJSONObj.getJSONArray("items");
			Map<String, Object> contentMap = new HashMap<String, Object>(3);
			if (itemArray != null && itemArray.size() > 0) {
				List<Map<String, Object>> itemMapList = new ArrayList<Map<String, Object>>(itemArray.size());
				for (Object o : itemArray) {
					JSONObject itemJsonObj = JSONObject.parseObject(o.toString());

					Map<String, Object> m = new HashMap<String, Object>(3);
					m.put("index", itemJsonObj.get("index"));
					m.put("completeAt", itemJsonObj.get("completeAt"));
					m.put("content", itemJsonObj.get("content"));

					itemMapList.add(m);
				}

				contentMap.put("items", itemMapList);
			} else {
				contentMap.put("items", Collections.EMPTY_LIST);
			}
			contentMap.put("completeAt", contentJSONObj.get("completeAt"));
			contentMap.put("content", contentJSONObj.get("content"));

			v.setDetail(contentMap);

			Integer allCount = contentJSONObj.getInteger("allCount");
			if (allCount != null) {
				Integer doCount = contentJSONObj.getInteger("doCount");
				if (doCount > allCount) {
					v.setCompleteTitle(allCount + "/" + allCount);
				} else {
					v.setCompleteTitle(doCount + "/" + allCount);
				}
			}
		} else {
			Map<String, Object> contentMap = new HashMap<String, Object>(1);
			contentMap.put("items", Collections.EMPTY_LIST);
			v.setDetail(contentMap);
		}

		return v;
	}
}
