package com.lanking.cloud.domain.yoo.user;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.frame.system.Product;

/**
 * 通用用户参数<br>
 * 
 * <pre>
 * 使用说明:
 * {@link #product} 产品 {@link Product},由于历史缘故,Product目前只能用YOOSHARE和YOOMATH
 * {@link #type} 参数类型  {@link UserParameterType}
 * {@link #version} 表示参数有版本属性,如引导页是根据版本的
 * p参数根据上面三个参数定义。
 * 目前定义如下:
 * 1.product=YOOMATH type=GUIDE version=1.2,p0首页引导p1作业引导p2班级引导
 * 2.product=YOOMATH type=GUIDE version=1.3,p0首页引导p1作业引导p2班级引导
 * 3.product=YOOMATH type=STUDENT_APP_USE,p0客户端首页邀请提醒p1寒假作业提醒
 * 4.product=YOOMATH type=STUDENT_WEB_USE,p0给家长的一封信
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_parameter")
public class UserParameter implements Serializable {

	private static final long serialVersionUID = 8341643947340837216L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 所属产品
	 */
	@Column(name = "product", precision = 3)
	private Product product;

	/**
	 * 参数类型
	 */
	@Column(name = "type", precision = 3)
	private UserParameterType type;

	/**
	 * 用户ID
	 */
	@Column(name = "user_id")
	private long userId;

	/**
	 * 对应web或者客户端版本
	 */
	@Column(length = 32)
	private String version;

	/**
	 * 通用参数0
	 */
	@Column(length = 256)
	private String p0;

	/**
	 * 通用参数1
	 */
	@Column(length = 256)
	private String p1;

	/**
	 * 通用参数2
	 */
	@Column(length = 256)
	private String p2;

	/**
	 * 通用参数3
	 */
	@Column(length = 256)
	private String p3;

	/**
	 * 通用参数4
	 */
	@Column(length = 256)
	private String p4;

	/**
	 * 通用参数5
	 */
	@Column(length = 256)
	private String p5;

	/**
	 * 通用参数6
	 */
	@Column(length = 256)
	private String p6;

	/**
	 * 通用参数7
	 */
	@Column(length = 256)
	private String p7;

	/**
	 * 通用参数8
	 */
	@Column(length = 256)
	private String p8;

	/**
	 * 通用参数9
	 */
	@Column(length = 256)
	private String p9;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public UserParameterType getType() {
		return type;
	}

	public void setType(UserParameterType type) {
		this.type = type;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getP0() {
		return p0;
	}

	public void setP0(String p0) {
		this.p0 = p0;
	}

	public String getP1() {
		return p1;
	}

	public void setP1(String p1) {
		this.p1 = p1;
	}

	public String getP2() {
		return p2;
	}

	public void setP2(String p2) {
		this.p2 = p2;
	}

	public String getP3() {
		return p3;
	}

	public void setP3(String p3) {
		this.p3 = p3;
	}

	public String getP4() {
		return p4;
	}

	public void setP4(String p4) {
		this.p4 = p4;
	}

	public String getP5() {
		return p5;
	}

	public void setP5(String p5) {
		this.p5 = p5;
	}

	public String getP6() {
		return p6;
	}

	public void setP6(String p6) {
		this.p6 = p6;
	}

	public String getP7() {
		return p7;
	}

	public void setP7(String p7) {
		this.p7 = p7;
	}

	public String getP8() {
		return p8;
	}

	public void setP8(String p8) {
		this.p8 = p8;
	}

	public String getP9() {
		return p9;
	}

	public void setP9(String p9) {
		this.p9 = p9;
	}

}
