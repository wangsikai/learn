package com.lanking.uxb.rescon.question.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.rescon.question.api.ResconQuestionManage;
import com.lanking.uxb.rescon.question.api.ResconTaggingManage;

@Service
@Transactional(readOnly = true)
public class ResconTaggingManageImpl implements ResconTaggingManage {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ResconQuestionManage resconQuestionManage;
	@Autowired
	private HttpClient httpClient;

	@Override
	public List<Long> extractKnowledges(long questionId) {
		Question question = resconQuestionManage.get(questionId);

		try {
			HttpPost httpPost = new HttpPost(Env.getDynamicString("yoomath.tagging.extractUrl"));
			httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			// 设置参数
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("q", question.getContent()));
			list.add(new BasicNameValuePair("k", "avdxsjWxDG"));
			list.add(new BasicNameValuePair("v", "0"));
			list.add(new BasicNameValuePair("g", question.getSubjectCode().toString()));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "utf-8");
			httpPost.setEntity(entity);

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int status = httpResponse.getStatusLine().getStatusCode();
			String response = EntityUtils.toString(httpResponse.getEntity());
			if (status == 200) {
				JSONArray jsonarray = JSONArray.parseArray(response);
				if (jsonarray.size() > 0) {
					List<Long> codes = new ArrayList<Long>(jsonarray.size());
					for (int i = 0; i < jsonarray.size(); i++) {
						JSONObject obj = jsonarray.getJSONObject(i);
						codes.add(obj.getLong("code"));
					}
					return codes;
				}
			} else {
				logger.error("[extractKnowledges error, questionId = {}, status = {}]", questionId, status);
			}
		} catch (Exception e) {
			logger.error("[extractKnowledges error, questionId = {}]", questionId, e);
		}
		return Lists.newArrayList();
	}
}
