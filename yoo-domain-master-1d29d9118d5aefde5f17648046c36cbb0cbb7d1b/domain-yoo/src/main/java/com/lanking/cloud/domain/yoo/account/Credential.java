package com.lanking.cloud.domain.yoo.account;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 第三方凭证
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月20日
 */
@Entity
@Table(name = "credential")
public class Credential implements Serializable {
	private static final long serialVersionUID = 7305618073616346103L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 关联账号ID
	 */
	@Column(name = "account_id")
	private Long accountId;

	/**
	 * 第三方用户标识
	 */
	@Column(name = "uid")
	private String uid;

	/**
	 * 凭证类型
	 */
	@Column(name = "type", precision = 3)
	private CredentialType type = CredentialType.DEFAULT;

	/**
	 * 第三方的名称
	 */
	@Column(name = "name", precision = 100)
	private String name;

	/**
	 * 第三方token
	 */
	@Column(name = "token", length = 200)
	private String token;

	/**
	 * 第三方secret
	 */
	@Column(name = "secret", length = 100)
	private String secret;

	/**
	 * 凭证有效时间
	 */
	@Column(name = "end_at", columnDefinition = "datetime(3)")
	private Date endAt;

	/**
	 * 创建时间
	 */
	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	/**
	 * 更新时间
	 */
	@Column(name = "update_at", columnDefinition = "datetime(3)")
	private Date updateAt;

	/**
	 * 产品
	 */
	@Column(name = "product", precision = 3, columnDefinition = "tinyint default 1")
	private Product product;

	@Transient
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public CredentialType getType() {
		return type;
	}

	public void setType(CredentialType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public Date getEndAt() {
		return endAt;
	}

	public void setEndAt(Date endAt) {
		this.endAt = endAt;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
