package com.lanking.uxb.service.thirdparty.api.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.sdk.util.UUID;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.session.api.impl.Security;
import com.lanking.uxb.service.thirdparty.api.ThirdpartyService;
import com.lanking.uxb.service.thirdparty.eduyun.Client;
import com.lanking.uxb.service.thirdparty.eduyun.response.YunUser;

/**
 * 第三方相关接口.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年7月8日
 */
@Service
public class ThirdpartyServiceImpl implements ThirdpartyService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Client eduyunClient;
	@Autowired
	private HttpClient httpClient;

	public void logout() {
		Integer credentialValue = Security.getSession().getAttrSession().getIntAttr("credentialType");
		if (credentialValue != null) {
			CredentialType credentialType = CredentialType.findByValue(credentialValue);
			if (credentialType == CredentialType.EDUYUN) {
				YunUser yunUser = Security.getSession().getAttrSession().getObject("yunuser", YunUser.class);
				if (yunUser != null) {
					try {
						eduyunClient.logout(yunUser.getTokenInfo().getToken(), yunUser.getUsessionid());
					} catch (IOException e) {
						logger.error(e.getMessage(), e);
					}
				}
			}
		}
	}

	@Override
	public long loadThirdImage(String url, String upurl, HttpServletRequest request) throws Exception {
		HttpGet httpget = new HttpGet(url);

		HttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();

		if (response.getStatusLine().getStatusCode() != 200) {
			throw new IOException(
					"Got bad response, error code = " + response.getStatusLine().getStatusCode() + " imageUrl: " + url);
		}
		if (entity != null) {
			FileOutputStream outputStream = null;
			InputStream inputStream = null;
			String tempFileName = Env.getString("file.store.path.tmp") + "/third/" + UUID.uuid() + ".jpg";
			File file = new File(tempFileName);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			try {
				outputStream = new FileOutputStream(file);
				inputStream = response.getEntity().getContent();
				byte b[] = new byte[32 * 1024];
				int j = 0;

				while ((j = inputStream.read(b)) != -1) {
					outputStream.write(b, 0, j);
				}
			} finally {
				if (outputStream != null) {
					outputStream.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
			}

			// 上传
			if (file.exists()) {
				HttpPost httppost = new HttpPost(upurl);
				FileBody filebody = new FileBody(file);
				MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
				multipartEntityBuilder.addPart("file", filebody);
				HttpEntity reqEntity = multipartEntityBuilder.build();

				Cookie[] cookies = request.getCookies();
				String cookiestr = "";
				for (Cookie cookie : cookies) {
					cookiestr += cookie.getName() + "=" + cookie.getValue() + ";";
				}
				httppost.setHeader("Cookie", cookiestr);
				httppost.setEntity(reqEntity);

				HttpResponse response2 = httpClient.execute(httppost);
				file.delete(); // 删除临时文件

				if (response2.getStatusLine().getStatusCode() != 200) {
					throw new IOException("Got bad response, error code = " + response2.getStatusLine().getStatusCode()
							+ " file: " + file.getPath());
				}
				HttpEntity httpEntity = response2.getEntity();
				String result = EntityUtils.toString(httpEntity, Charset.forName("UTF-8"));
				if (StringUtils.isNotBlank(result)) {
					JSONObject object = JSONObject.parseObject(result);
					return object.getJSONObject("ret").getLong("id");
				}
			}
		}
		response = null;
		return 0;
	}
}
