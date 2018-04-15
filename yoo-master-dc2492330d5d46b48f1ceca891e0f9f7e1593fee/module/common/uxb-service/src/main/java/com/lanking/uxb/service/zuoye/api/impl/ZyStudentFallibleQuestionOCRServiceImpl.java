package com.lanking.uxb.service.zuoye.api.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.base.file.File;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.FileService;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.zuoye.api.ZyStudentFallibleQuestionOCRService;

/**
 * @author xinyu.zhou
 * @since 2.1.0
 */
@Service
public class ZyStudentFallibleQuestionOCRServiceImpl implements ZyStudentFallibleQuestionOCRService {

	public static String OCR_URL = "http://139.196.35.206:8080/RecoServices/image/ql";
	public static String WYUN_USER = "wyunClient";
	public static String WYUN_PASSWORD = "lank1ng$";

	private static Logger logger = LoggerFactory.getLogger(ZyStudentFallibleQuestionOCRServiceImpl.class);

	@Autowired
	private HttpClient httpClient;
	@Autowired
	private FileService fileService;

	@PostConstruct
	public void init() {
		OCR_URL = Env.getString("yoomath.ocr.url");
		WYUN_USER = Env.getString("yoomath.ocr.wyun.user");
		WYUN_PASSWORD = Env.getString("yoomath.ocr.wyun.password");
	}

	@Override
	public List<Long> ocr(long fileId) {
		File file = fileService.getFile(fileId);

		if (file == null) {
			return Collections.EMPTY_LIST;
		}
		
		try {
			HttpPost post = new HttpPost(OCR_URL);
			post.setHeader("Content-Type", "application/json;charset=UTF-8");
			post.setHeader("accept", "application/json;charset=UTF-8");
			post.setHeader("wyun_user", WYUN_USER);
			post.setHeader("wyun_password", WYUN_PASSWORD);
			post.setHeader("x_meta", "");
			post.setHeader("x_protocolversion", "1");

			JSONObject object = new JSONObject();

			String fileName = file.getName();
			String type = fileName.substring(fileName.lastIndexOf(".") + 1);
			if (type.toLowerCase().startsWith("png")) {
				object.put("type", "PNG");
			} else {
				object.put("type", "JPEG");
			}
			object.put("encodedImage", getImageBase64(file));

			StringEntity entity = new StringEntity(object.toString(), "UTF-8");
			post.setEntity(entity);

			long start = System.currentTimeMillis();
			logger.info("ocr start time:{}", start);
			HttpResponse httpResponse = httpClient.execute(post);
			logger.info("ocr cost time:{}", System.currentTimeMillis() - start);
			int status = httpResponse.getStatusLine().getStatusCode();
			String response = EntityUtils.toString(httpResponse.getEntity());

			if (status == 200) {
				JSONObject result = JSONObject.parseObject(response);

				// TODO: 语义标签相关
				String content = result.getString("content");
				JSONArray array = result.getJSONArray("qlist");
				List<Long> questionIds = new ArrayList<Long>(array.size());
				for (Object o : array) {
					JSONObject jsonObject = JSONObject.parseObject(o.toString());
					questionIds.add(jsonObject.getLong("id"));
				}

				if (questionIds.size() > 3) {
					questionIds = questionIds.subList(0, 3);
				}

				return questionIds;
			} else {
				// 其他状态下为找不到题目进行处理
				return Collections.EMPTY_LIST;
			}

		} catch (Exception e) {
			logger.error("post data to ocr server has error: {}", e);
			return Collections.EMPTY_LIST;
		}

	}

	private String getImageBase64(File file) {
		byte[] data = null;

		try {
			InputStream in = new FileInputStream(FileUtil.getFilePath(file));
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new String(Base64.encodeBase64(data));

	}

}
