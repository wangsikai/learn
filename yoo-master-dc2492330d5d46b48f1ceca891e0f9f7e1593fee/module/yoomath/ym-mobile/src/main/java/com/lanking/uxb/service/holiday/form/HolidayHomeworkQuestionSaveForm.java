package com.lanking.uxb.service.holiday.form;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 假期作业保存form
 *
 * @author xinyu.zhou
 * @since 3.0.0
 */
public class HolidayHomeworkQuestionSaveForm {
	// 作业ID
	private long holidayHkItemId;
	// 学生作业ID
	private long holidayStuHkItemId;
	// 答题时长
	private int time;
	// 完成率
	private Double completionRate;
	// 假期作业保存form列表
	private String forms;

	public long getHolidayHkItemId() {
		return holidayHkItemId;
	}

	public void setHolidayHkItemId(long holidayHkItemId) {
		this.holidayHkItemId = holidayHkItemId;
	}

	public long getHolidayStuHkItemId() {
		return holidayStuHkItemId;
	}

	public void setHolidayStuHkItemId(long holidayStuHkItemId) {
		this.holidayStuHkItemId = holidayStuHkItemId;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	@SuppressWarnings("unchecked")
	public List<HolidayHomeworkQuestionForm> getForms() {
		if (StringUtils.isBlank(forms))
			return Collections.EMPTY_LIST;

		JSONArray jsonArray = JSONArray.parseArray(forms);

		List<HolidayHomeworkQuestionForm> formList = new ArrayList<HolidayHomeworkQuestionForm>(jsonArray.size());
		for (Object o : jsonArray) {
			JSONObject obj = JSONObject.parseObject(o.toString());
			JSONArray images = JSONArray.parseArray(obj.getString("images"));

			obj.remove("images");
			HolidayHomeworkQuestionForm f = JSONObject.parseObject(obj.toString(), HolidayHomeworkQuestionForm.class);

			if (images == null || images.size() == 0) {
				f.setImages(Collections.EMPTY_LIST);
			} else {
				List<Long> imageList = new ArrayList<Long>(images.size());
				for (Object i : images) {
					String val = i.toString();
					if (StringUtils.isBlank(val)) {
						continue;
					}
					imageList.add(Long.valueOf(i.toString()));
				}

				f.setImages(imageList);
			}

			formList.add(f);

		}

		return formList;

	}

	public void setForms(String forms) {
		this.forms = forms;
	}

	public Double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}
}
