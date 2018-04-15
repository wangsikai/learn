package com.lanking.uxb.service.file.api.impl;

import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.cloud.springboot.environment.Env;
import com.lanking.uxb.service.file.api.QiNiuFileService;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author xinyu.zhou
 * @since 3.9.3
 */
@Service
public class QiNiuFileServiceImpl implements QiNiuFileService {

	// access key
	private String ak;
	// secret key
	private String sk;
	// 空间
	private String bucket;
	// 基础url
	private String domainOfBucket;
	// token失效时间
	private int expireSeconds;
	// 下载链接过期时间
	private long downloadExpireSeconds;

	@PostConstruct
	public void init() {
		ak = Env.getString("file.qn.ak");
		sk = Env.getString("file.qn.sk");
		bucket = Env.getString("file.qn.annotate.bucket");
		domainOfBucket = Env.getString("file.qn.annotate.bucket.domain");
		expireSeconds = Env.getInt("file.qn.expire.seconds");
		downloadExpireSeconds = Env.getLong("file.qn.download.expire.seconds");
	}

	@Override
	public String getUpToken(String scene) {
		if (StringUtils.isBlank(scene)) {
			return null;
		}
		if (scene.equals("ANNOTATE")) {
			StringMap putPolicy = new StringMap();
			putPolicy.put("returnBody",
					"{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize), \"avinfo\":$(avinfo)}");
			Auth auth = Auth.create(ak, sk);
			return auth.uploadToken(bucket, null, expireSeconds, putPolicy);
		} else {
			return null;
		}
	}

	@Override
	public String getBucket(String scene) {
		if (StringUtils.isBlank(scene)) {
			return null;
		}
		if (scene.equals("ANNOTATE")) {
			return bucket;
		} else {
			return null;
		}
	}

	@Override
	public String getDownloadUrl(String key) {
		String baseUrl = String.format("%s/%s", domainOfBucket, key);
		Auth auth = Auth.create(ak, sk);
		return auth.privateDownloadUrl(baseUrl);
	}

	@Override
	public List<String> getDownloadUrls(Collection<String> keys) {
		List<String> privateUrls = new ArrayList<String>(keys.size());
		Auth auth = Auth.create(ak, sk);

		for (String key : keys) {
			String baseUrl = String.format("%s/%s", domainOfBucket, key);
			privateUrls.add(auth.privateDownloadUrl(baseUrl, downloadExpireSeconds));
		}
		return privateUrls;
	}

}
