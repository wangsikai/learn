package com.lanking.uxb.service.thirdparty.api.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeUUID;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.thirdparty.ShareLog;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.service.thirdparty.api.ShareLogService;
import com.lanking.uxb.service.thirdparty.form.ShareForm;

@Transactional(readOnly = true)
@Service
public class ShareLogServiceImpl implements ShareLogService {

	@Autowired
	@Qualifier("ShareLogRepo")
	private Repo<ShareLog, Long> shareLogRepo;

	@Transactional
	@Override
	public ShareLog log(ShareForm form) {
		ShareLog log = new ShareLog();
		if (form.getId() != null) {
			log.setId(form.getId());
		} else {
			log.setId(SnowflakeUUID.next());
		}
		log.setUserId(form.getUserId());
		log.setBiz(form.getBiz());
		log.setBizId(form.getBizId());
		log.setType(form.getType());
		log.setTitle(form.getTitle());
		log.setBody(form.getBody());
		log.setUrl(form.getUrl());
		log.setContent(form.getContent());
		if (StringUtils.isNotBlank(form.getExtend())) {
			// 解析p0
			JSONObject jo = JSONObject.parseObject(form.getExtend());
			if (jo.containsKey("p0")) {
				log.setP0(jo.getString("p0"));
			}
		}
		if (form.getCreateAt() != null) {
			log.setCreateAt(form.getCreateAt());
		} else {
			log.setCreateAt(new Date());
		}
		return shareLogRepo.save(log);
	}

	@Override
	public ShareLog get(long id) {
		return shareLogRepo.get(id);
	}

	@Override
	public boolean isShare(Biz biz, long bizId) {
		return shareLogRepo.find("$getShareLogByBiz", Params.param("biz", biz.getBiz()).put("bizId", bizId)).get(
				Long.class) > 0;
	}

	@Override
	public boolean isShare(Biz biz, long bizId, Long userId, String p0) {
		Params params = Params.param("biz", biz.getBiz()).put("bizId", bizId);
		if (userId != null) {
			params.put("userId", userId);
		}
		if (p0 != null) {
			params.put("p0", p0);
		}
		return shareLogRepo.find("$getShareLogByBiz", params).get(Long.class) > 0;
	}

	@Transactional
	@Override
	public ShareLog log2(ShareForm form) {
		ShareLog log = new ShareLog();
		if (form.getId() != null) {
			log.setId(form.getId());
		} else {
			log.setId(SnowflakeUUID.next());
		}
		log.setUserId(form.getUserId());
		log.setBiz(Biz.ACTIVITY);
		log.setBizId(795646319942180865L);
		log.setType(form.getType());
		log.setTitle(form.getTitle());
		log.setBody(form.getBody());
		log.setUrl(form.getUrl());
		log.setContent(form.getContent());
		log.setP0(String.valueOf(form.getBizId()));
		if (form.getCreateAt() != null) {
			log.setCreateAt(form.getCreateAt());
		} else {
			log.setCreateAt(new Date());
		}
		return shareLogRepo.save(log);
	}
}
