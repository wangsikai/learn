package com.lanking.cloud.domain.yoo.goods.coins;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;

/**
 * 金币商品组-商品
 * 
 * <pre>
 * 1.当组里面的商品被下架再上架，此时应该还存在于此组中
 * </pre>
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@Entity
@Table(name = "coins_goods_group_goods")
public class CoinsGoodsGroupGoods implements Serializable {

	private static final long serialVersionUID = 2436314677524482410L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 组ID
	 */
	@Column(name = "group_id")
	private long groupId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id")
	private long goodsId;

	/**
	 * 序号1
	 */
	@Column(name = "sequence0")
	private Integer sequence0;

	/**
	 * 序号2
	 */
	@Column(name = "sequence1")
	private Integer sequence1;

	/**
	 * 序号3
	 */
	@Column(name = "sequence2")
	private Integer sequence2;

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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getSequence0() {
		return sequence0;
	}

	public void setSequence0(Integer sequence0) {
		this.sequence0 = sequence0;
	}

	public Integer getSequence1() {
		return sequence1;
	}

	public void setSequence1(Integer sequence1) {
		this.sequence1 = sequence1;
	}

	public Integer getSequence2() {
		return sequence2;
	}

	public void setSequence2(Integer sequence2) {
		this.sequence2 = sequence2;
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

}
