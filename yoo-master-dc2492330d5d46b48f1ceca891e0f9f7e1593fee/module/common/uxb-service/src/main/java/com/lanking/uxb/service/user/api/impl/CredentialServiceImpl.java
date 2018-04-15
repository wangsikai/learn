package com.lanking.uxb.service.user.api.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.component.mq.common.constants.MqHonorRegistryConstants;
import com.lanking.cloud.component.mq.producer.MQ;
import com.lanking.cloud.component.mq.producer.MqSender;
import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.type.Biz;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.honor.coins.CoinsAction;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.ex.core.IllegalArgException;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.service.honor.api.CoinsService;
import com.lanking.uxb.service.user.api.CredentialService;

@Service
@Transactional(readOnly = true)
public class CredentialServiceImpl implements CredentialService {
	@Autowired
	@Qualifier("CredentialRepo")
	private Repo<Credential, Long> credentialRepo;
	@Autowired
	private CoinsService coinsService;
	@Autowired
	private MqSender mqSender;

	@Transactional
	@Override
	public void save(Credential credential) {
		if (credential.getId() != null) {
			Credential oldCredential = credentialRepo.get(credential.getId());
			oldCredential.setEndAt(credential.getEndAt());
			oldCredential.setToken(credential.getToken());
			oldCredential.setUpdateAt(credential.getUpdateAt());
			credentialRepo.save(oldCredential);
		} else {
			if (credential.getProduct() == null) {
				throw new IllegalArgException();
			}
			credentialRepo.save(credential);
			if (credential.getType() == CredentialType.QQ && credential.getUserId() != null
					&& credential.getUserId() > 0) {
				coinsService.earn(CoinsAction.BIND_QQ, credential.getUserId(), -1, Biz.NULL, 0);
			} else if (credential.getType() == CredentialType.WEIXIN && credential.getUserId() != null
					&& credential.getUserId() > 0) {
				coinsService.earn(CoinsAction.BIND_WEIXIN, credential.getUserId(), -1, Biz.NULL, 0);
			}
		}
	}

	@Transactional
	@Override
	public void save(Credential credential, boolean isClient, UserType userType) {
		if (credential.getId() != null) {
			Credential oldCredential = credentialRepo.get(credential.getId());
			oldCredential.setEndAt(credential.getEndAt());
			oldCredential.setToken(credential.getToken());
			oldCredential.setUpdateAt(credential.getUpdateAt());
			credentialRepo.save(oldCredential);
		} else {
			if (credential.getProduct() == null) {
				throw new IllegalArgException();
			}
			credentialRepo.save(credential);

			if (credential.getUserId() != null && credential.getUserId() > 0 && userType == UserType.TEACHER) {
				// 教师暂时还是按原来的逻辑
				if (credential.getType() == CredentialType.QQ) {
					coinsService.earn(CoinsAction.BIND_QQ, credential.getUserId(), -1, Biz.NULL, 0);
				} else if (credential.getType() == CredentialType.WEIXIN) {
					coinsService.earn(CoinsAction.BIND_WEIXIN, credential.getUserId(), -1, Biz.NULL, 0);
				}
			} else if (credential.getUserId() != null && credential.getUserId() > 0 && userType == UserType.STUDENT) {
				// 学生走新的逻辑
				if (credential.getType() == CredentialType.QQ) {
					JSONObject messageObj = new JSONObject();
					messageObj.put("taskCode", 101000003);
					messageObj.put("userId", credential.getUserId());
					messageObj.put("isClient", isClient);
					mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
							MQ.builder().data(messageObj).build());
				} else if (credential.getType() == CredentialType.WEIXIN) {
					JSONObject messageObj = new JSONObject();
					messageObj.put("taskCode", 101000004);
					messageObj.put("userId", credential.getUserId());
					messageObj.put("isClient", isClient);
					mqSender.send(MqHonorRegistryConstants.EX_TASK, MqHonorRegistryConstants.RK_TASK_LOG,
							MQ.builder().data(messageObj).build());
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Credential getCredentialByPersonId(Product product, CredentialType type, String uid) {
		List<Credential> lists = credentialRepo
				.find("$getCredentialByPersonId",
						Params.param("uid", uid).put("type", type.getValue()).put("product", product.getValue()))
				.list();
		return lists.size() == 0 ? null : lists.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public Credential getCredentialByAccountId(Product product, CredentialType type, Long accountId) {
		List<Credential> lists = credentialRepo.find("$getCredentialByAccountId",
				Params.param("accountId", accountId).put("type", type.getValue()).put("product", product.getValue()))
				.list();
		return lists.size() == 0 ? null : lists.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public Credential getCredentialByUserId(Product product, CredentialType type, Long userId) {
		List<Credential> lists = credentialRepo
				.find("$getCredentialByUserId",
						Params.param("userId", userId).put("type", type.getValue()).put("product", product.getValue()))
				.list();

		return lists.size() == 0 ? null : lists.get(0);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Credential> listCredentials(Product product, Long accountId) {
		return credentialRepo.find("$getCredentialByAccountId",
				Params.param("accountId", accountId).put("product", product.getValue())).list();
	}

	@Override
	@Transactional
	public void deleteCredential(Long id) {
		credentialRepo.deleteById(id);
	}

	@Override
	@Transactional
	public void deleteCredential(Product product, CredentialType type, long accountId) {
		credentialRepo.execute("$deleteCredential",
				Params.param("accountId", accountId).put("type", type.getValue()).put("product", product.getValue()));
	}

	@Override
	@Transactional(readOnly = true)
	public Credential get(Long id) {
		return credentialRepo.get(id);
	}
}
