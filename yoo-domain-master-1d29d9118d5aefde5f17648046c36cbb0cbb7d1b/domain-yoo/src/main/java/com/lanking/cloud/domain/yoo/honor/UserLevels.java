package com.lanking.cloud.domain.yoo.honor;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.lanking.cloud.domain.frame.system.Product;

/**
 * 用户级别
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "user_levels")
public class UserLevels implements Serializable {

	private static final long serialVersionUID = -584231251406319035L;

	/**
	 * 主键{1}{系统}+{级别}=>{1}{XXX}{XXX},不足三位补全
	 */
	@Id
	@Column(name = "code")
	private int code;

	/**
	 * 级别
	 */
	@Column(name = "level", precision = 10)
	private int level;

	/**
	 * 最小成长值(闭区间)
	 */
	@Column(name = "min_growth_value", precision = 10)
	private int minGrowthValue;

	/**
	 * 最大成长值(开区间)
	 */
	@Column(name = "max_growth_value", precision = 10)
	private int maxGrowthValue;

	/**
	 * 所属产品
	 */
	@Column(name = "product", precision = 3)
	private Product product;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getMinGrowthValue() {
		return minGrowthValue;
	}

	public void setMinGrowthValue(int minGrowthValue) {
		this.minGrowthValue = minGrowthValue;
	}

	public int getMaxGrowthValue() {
		return maxGrowthValue;
	}

	public void setMaxGrowthValue(int maxGrowthValue) {
		this.maxGrowthValue = maxGrowthValue;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

}
