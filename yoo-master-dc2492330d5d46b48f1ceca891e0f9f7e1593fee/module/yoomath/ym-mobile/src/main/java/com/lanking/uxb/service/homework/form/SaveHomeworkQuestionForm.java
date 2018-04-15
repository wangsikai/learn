package com.lanking.uxb.service.homework.form;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.sdk.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 新的客户端保存作业答案参数 多份题目答题都可以上传答案
 *
 * @author xinyu.zhou
 * @since 3.0.0
 */
public class SaveHomeworkQuestionForm {
	// 作业用时
	private int time;
	// 完成率
	private Double completionRate;
	// 作业id
	private long homeworkId;
	// 学生作业id
	private long stuHkId;
	// 是{@link HomeworkQuestionForm} 这个类的 JSON数组序列化String
	// 注意其中的CompleteRate以及作业时间都可以不用传了
	private String homeworkQuestionForms;

	public long getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(long homeworkId) {
		this.homeworkId = homeworkId;
	}

	public long getStuHkId() {
		return stuHkId;
	}

	public void setStuHkId(long stuHkId) {
		this.stuHkId = stuHkId;
	}

	@SuppressWarnings("unchecked")
	public List<HomeworkQuestionForm> getHomeworkQuestionForms() {
		if (StringUtils.isBlank(homeworkQuestionForms)) {
			return Collections.EMPTY_LIST;
		}

		JSONArray jsonArray = JSONArray.parseArray(homeworkQuestionForms);
		List<HomeworkQuestionForm> formList = new ArrayList<HomeworkQuestionForm>(jsonArray.size());
		for (Object o : jsonArray) {
			JSONObject obj = JSONObject.parseObject(o.toString());
			JSONArray images = JSONArray.parseArray(obj.getString("images"));

			obj.remove("images");
			HomeworkQuestionForm f = JSONObject.parseObject(obj.toString(), HomeworkQuestionForm.class);

			if (images == null || images.size() == 0) {
				f.setImages(Collections.EMPTY_LIST);
			} else {
				List<Long> imageList = new ArrayList<Long>(images.size());
				for (Object i : images) {
					String val = i.toString();
					if (StringUtils.isBlank(val)) {
						continue;
					}

					imageList.add(Long.valueOf(val));
				}

				f.setImages(imageList);
			}

			formList.add(f);
		}
		return formList;
	}

	public void setHomeworkQuestionForms(String homeworkQuestionForms) {
		this.homeworkQuestionForms = homeworkQuestionForms;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public Double getCompletionRate() {
		return completionRate;
	}

	public void setCompletionRate(Double completionRate) {
		this.completionRate = completionRate;
	}
}
