package com.lanking.uxb.service.search.api.impl.listener;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.lanking.cloud.component.mq.common.annotation.Exchange;
import com.lanking.cloud.component.mq.common.annotation.Listener;
import com.lanking.cloud.component.mq.common.constants.MqSearchRegistryConstants;
import com.lanking.cloud.domain.yoo.user.Student;
import com.lanking.cloud.domain.yoo.user.Teacher;
import com.lanking.cloud.domain.yoo.user.User;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.search.api.KeyWordService;
import com.lanking.uxb.service.user.api.AccountService;
import com.lanking.uxb.service.user.api.StudentService;
import com.lanking.uxb.service.user.api.TeacherService;

@Component
@Exchange(name = MqSearchRegistryConstants.EX_SEARCH)
public class SearchDataMqListener {

	private Logger logger = LoggerFactory.getLogger(SearchDataMqListener.class);

	private static final String PARSE_QUERY = "query";
	private static final String IGNORE_PARSE_QUERY = "query_string";

	@Autowired
	private KeyWordService keyWordService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;

	@SuppressWarnings("unchecked")
	@Listener(queue = MqSearchRegistryConstants.QUEUE_SEARCH_WORD, routingKey = MqSearchRegistryConstants.RK_SEARCH_WORD)
	public void collectSearchWord(JSONObject json) {
		try {
			long userId = 0;
			int phaseCode = 0;
			int subjectCode = 0;
			if (json.containsKey("userId")) {
				userId = json.getLongValue("userId");
				if (userId > 0) {
					User user = accountService.getUserByUserId(userId);
					if (user != null) {
						if (user.getUserType() == UserType.TEACHER) {
							Teacher teacher = (Teacher) teacherService.getUser(userId);
							phaseCode = teacher.getPhaseCode() == null ? 0 : teacher.getPhaseCode();
							subjectCode = teacher.getSubjectCode() == null ? 0 : teacher.getSubjectCode();
						} else if (user.getUserType() == UserType.STUDENT) {
							Student student = (Student) studentService.getUser(userId);
							phaseCode = student.getPhaseCode() == null ? 0 : student.getPhaseCode();
							// TODO 学科如何设置
						}
					}
				}
			}
			Set<String> types = Sets.newHashSet();
			if (json.containsKey("type")) {
				types.add(json.getString("type"));
			}
			if (json.containsKey("types")) {
				types.addAll(json.getObject("types", List.class));
			}
			if (types.size() == 0) {
				types.add(StringUtils.EMPTY);
			}
			String queryBuilder = StringUtils.EMPTY;
			if (json.containsKey("queryBuilder")) {
				queryBuilder = json.getString("queryBuilder");
			}
			if (StringUtils.isNotBlank(queryBuilder)) {
				Set<String> words = Sets.newHashSet();
				parseQueryKeyword(words, queryBuilder);
				if (words.size() > 0) {
					for (String word : words) {
						if (StringUtils.isNotBlank(word)) {
							for (String type : types) {
								keyWordService.save(userId, type, phaseCode, subjectCode, word);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("collect search word fail:", e);
		}
	}

	void parseQueryKeyword(Set<String> words, String word) {
		if (StringUtils.isNotBlank(word)) {
			JSONObject jsonObject = null;
			try {
				jsonObject = JSONObject.parseObject(word);
			} catch (Exception e) {
			}
			if (jsonObject != null) {
				if (jsonObject.containsKey(PARSE_QUERY)) {
					words.add(jsonObject.getString(PARSE_QUERY));
				}
				for (String key : jsonObject.keySet()) {
					if (IGNORE_PARSE_QUERY.equals(key)) {
						continue;
					}
					String w = jsonObject.getString(key);
					if (w.startsWith("[")) {
						for (Object arr : JSONArray.parseArray(w)) {
							parseQueryKeyword(words, arr.toString());
						}
					} else {
						parseQueryKeyword(words, w);
					}
				}
			}
		}
	}

}
