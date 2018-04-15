package com.lanking.uxb.service.user.api;

import java.util.List;

import com.lanking.cloud.domain.frame.system.Product;
import com.lanking.cloud.domain.yoo.account.Credential;
import com.lanking.cloud.domain.yoo.account.CredentialType;
import com.lanking.cloud.domain.yoo.user.UserType;

public interface CredentialService {

	/**
	 * 保存凭证.
	 * <p>
	 * 当前仅教师使用此方法
	 * </p>
	 * 
	 * @since 学生端 v1.4.3 2017-6-03
	 * @param credential
	 */
	void save(Credential credential);

	/**
	 * 保存凭证（配合新的金币成长值任务）.
	 * <p>
	 * 学生调用此新的方法
	 * </p>
	 * 
	 * @since 学生端 v1.4.3 2017-6-03
	 * 
	 * @param credential
	 *            凭证
	 * @param isClient
	 *            是否是客户端调用
	 * @param userType
	 *            用户类型
	 */
	void save(Credential credential, boolean isClient, UserType userType);

	/**
	 * 根据第三方用户ID获取第三方凭证.
	 * 
	 * @param type
	 *            凭证类型
	 * @param uid
	 *            用户ID
	 * @return
	 */
	Credential getCredentialByPersonId(Product product, CredentialType type, String uid);

	/**
	 * 根据本地账户ID获取第三方凭证.
	 * 
	 * @param type
	 * @param accountId
	 * @return
	 */
	Credential getCredentialByAccountId(Product product, CredentialType type, Long accountId);

	/**
	 * 根据用户id获取第三方凭证
	 *
	 * @param product
	 *            {@link Product}
	 * @param type
	 *            {@link CredentialType}
	 * @param userId
	 *            用户id
	 * @return {@link Credential}
	 */
	Credential getCredentialByUserId(Product product, CredentialType type, Long userId);

	/**
	 * 获取用户的凭证集合.
	 * 
	 * @param accountId
	 *            账户ID
	 * @return
	 */
	List<Credential> listCredentials(Product product, Long accountId);

	/**
	 * 删除凭证.
	 * 
	 * @param id
	 */
	void deleteCredential(Long id);

	void deleteCredential(Product product, CredentialType type, long accountId);

	/**
	 * 获得凭证.
	 * 
	 * @param id
	 * @return
	 */
	Credential get(Long id);
}
