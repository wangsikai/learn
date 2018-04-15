package com.lanking.uxb.service.intelligentCorrection.api.impl;

import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.lanking.cloud.domain.type.HomeworkAnswerResult;
import com.lanking.cloud.domain.yoomath.homework.QuestionAutoCorrectMethod;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.uxb.service.intelligentCorrection.ex.IntelligentCorrectionException;
import com.lanking.uxb.service.intelligentCorrection.value.CorrectResult;

/**
 * 通过调用自动化批改接口进行批改
 * 
 * @since 3.9.0
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年2月9日
 */
public class AutoCorrectIntelligentCorrectionHandle extends AbstractIntelligentCorrectionHandle {

	private Logger logger = LoggerFactory.getLogger(AutoCorrectIntelligentCorrectionHandle.class);

	private String autoCorrectRestAPI;
	private HttpClient httpClient;

	@Override
	public void handle(List<Long> queryIds, List<Long> answerIds, List<String> targets, List<String> querys,
			Map<Long, CorrectResult> results) throws IntelligentCorrectionException, IllegalArgException {
		logger.info("intelligent correction[auto correct] start");
		// 封装请求数据
		int queryIdSize = queryIds.size();
		List<JSONArray> arrayList = Lists.newArrayList();
		JSONArray array = new JSONArray();
		Map<Long, String> requestTargets = new HashMap<Long, String>(queryIdSize);
		Map<Long, String> requestQuerys = new HashMap<Long, String>(queryIdSize);

		int currentSize = 0;
		for (int i = 0; i < queryIdSize; i++) {
			long queryId = queryIds.get(i);
			CorrectResult result = results.get(queryId);
			if (result == null || !result.isCredible()) {
				JSONObject object = new JSONObject();
				object.put("id", queryId);
				String target = targets.get(i);
				object.put("target", target);
				String query = querys.get(i);
				object.put("query", query);
				requestTargets.put(queryId, target);
				requestQuerys.put(queryId, query);
				array.add(object);
				currentSize = currentSize + 1;
				if (currentSize % 20 == 0) {
					arrayList.add(array);
					array = new JSONArray();
					currentSize = 0;
				}
			}
		}
		if (currentSize > 0) {
			arrayList.add(array);
		}
		for (JSONArray ja : arrayList) {
			StringEntity entity = new StringEntity(ja.toString(), "UTF-8");
			try {
				HttpPost httpPost = new HttpPost(autoCorrectRestAPI);
				httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
				httpPost.setEntity(entity);
				HttpResponse httpResponse = httpClient.execute(httpPost);
				int status = httpResponse.getStatusLine().getStatusCode();
				String response = EntityUtils.toString(httpResponse.getEntity());
				if (status == 200) {
					JSONArray result = JSONArray.parseArray(response);
					for (Object o : result) {
						JSONObject one = JSONObject.parseObject(o.toString());
						long queryId = one.getLongValue("id");
						String ret = one.getString("content");
						if ("1".equals(ret)) {
							results.put(queryId, new CorrectResult(HomeworkAnswerResult.RIGHT, true,
									QuestionAutoCorrectMethod.CORRECT_SERVER));
						} else if ("-1".equals(ret)) {
							// 纯数字可信任
							boolean isDigital = isDigital(requestTargets.get(queryId))
									&& isDigital(requestQuerys.get(queryId));

							results.put(queryId, new CorrectResult(HomeworkAnswerResult.WRONG, isDigital,
									QuestionAutoCorrectMethod.CORRECT_SERVER));

							logger.info("[intelligent] return ERR, targets:{}, querys:{}",
									JSONObject.toJSONString(targets), JSONObject.toJSONString(querys));
						} else {
							results.put(queryId, new CorrectResult(HomeworkAnswerResult.UNKNOW, false,
									QuestionAutoCorrectMethod.CORRECT_SERVER));
						}
					}
				} else {
					logger.info("intelligent correction[auto correct],call restAPI result:{}", response);
				}
			} catch (SocketTimeoutException e) {
				logger.error("targets:{},querys:{}", JSONObject.toJSONString(targets), JSONObject.toJSONString(querys));
				logger.error("intelligent correction[auto correct]", e);
			} catch (Exception e) {
				logger.error("intelligent correction[auto correct]", e);
			}
		}

		logger.info("intelligent correction[auto correct] end");
	}

	public String getAutoCorrectRestAPI() {
		return autoCorrectRestAPI;
	}

	public void setAutoCorrectRestAPI(String autoCorrectRestAPI) {
		this.autoCorrectRestAPI = autoCorrectRestAPI;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

}
