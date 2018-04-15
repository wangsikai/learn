package com.lanking.cloud.domain.base.counter;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.lanking.cloud.component.db.support.hibernate.identifierGenerator.SnowflakeGenerator;
import com.lanking.cloud.domain.type.Biz;

/**
 * 计数明细表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "counter_detail")
public class CounterDetail implements Serializable {

	private static final long serialVersionUID = -1876530083482301149L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 主业务对象类型
	 * 
	 * @see Biz
	 */
	@Column(name = "biz", precision = 3)
	private int biz;

	/**
	 * 主业务对象ID
	 */
	@Column(name = "biz_id")
	private long bizId;

	/**
	 * 关联业务对象类型
	 * 
	 * @see Biz
	 */
	@Column(name = "other_biz", precision = 3)
	private int otherBiz;

	/**
	 * 关联业务对象ID
	 */
	@Column(name = "other_biz_id")
	private long otherBizId;

	@Column(name = "count_1")
	private long count1 = 0;

	@Column(name = "count_2")
	private long count2 = 0;

	@Column(name = "count_3")
	private long count3 = 0;

	@Column(name = "count_4")
	private long count4 = 0;

	@Column(name = "count_5")
	private long count5 = 0;

	@Column(name = "count_6")
	private long count6 = 0;

	@Column(name = "count_7")
	private long count7 = 0;

	@Column(name = "count_8")
	private long count8 = 0;

	@Column(name = "count_9")
	private long count9 = 0;

	@Column(name = "count_10")
	private long count10 = 0;

	@Column(name = "count_20")
	private Double count20 = 0.0;

	@Column(name = "count_21")
	private Double count21 = 0.0;

	@Column(name = "count_22")
	private Double count22 = 0.0;

	@Column(name = "count_23")
	private Double count23 = 0.0;

	@Column(name = "count_24")
	private Double count24 = 0.0;

	@Column(name = "create_at", columnDefinition = "datetime(3)")
	private Date createAt;

	@Column(name = "update_at_1", columnDefinition = "datetime(3)")
	private Date updateAt1;

	@Column(name = "update_at_2", columnDefinition = "datetime(3)")
	private Date updateAt2;

	@Column(name = "update_at_3", columnDefinition = "datetime(3)")
	private Date updateAt3;

	@Column(name = "update_at_4", columnDefinition = "datetime(3)")
	private Date updateAt4;

	@Column(name = "update_at_5", columnDefinition = "datetime(3)")
	private Date updateAt5;

	@Column(name = "update_at_6", columnDefinition = "datetime(3)")
	private Date updateAt6;

	@Column(name = "update_at_7", columnDefinition = "datetime(3)")
	private Date updateAt7;

	@Column(name = "update_at_8", columnDefinition = "datetime(3)")
	private Date updateAt8;

	@Column(name = "update_at_9", columnDefinition = "datetime(3)")
	private Date updateAt9;

	@Column(name = "update_at_10", columnDefinition = "datetime(3)")
	private Date updateAt10;

	@Column(name = "update_at_20", columnDefinition = "datetime(3)")
	private Date updateAt20;

	@Column(name = "update_at_21", columnDefinition = "datetime(3)")
	private Date updateAt21;

	@Column(name = "update_at_22", columnDefinition = "datetime(3)")
	private Date updateAt22;

	@Column(name = "update_at_23", columnDefinition = "datetime(3)")
	private Date updateAt23;

	@Column(name = "update_at_24", columnDefinition = "datetime(3)")
	private Date updateAt24;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getBiz() {
		return biz;
	}

	public void setBiz(int biz) {
		this.biz = biz;
	}

	public long getBizId() {
		return bizId;
	}

	public void setBizId(long bizId) {
		this.bizId = bizId;
	}

	public int getOtherBiz() {
		return otherBiz;
	}

	public void setOtherBiz(int otherBiz) {
		this.otherBiz = otherBiz;
	}

	public long getOtherBizId() {
		return otherBizId;
	}

	public void setOtherBizId(long otherBizId) {
		this.otherBizId = otherBizId;
	}

	public long getCount1() {
		return count1;
	}

	public void setCount1(long count1) {
		this.count1 = count1;
	}

	public long getCount2() {
		return count2;
	}

	public void setCount2(long count2) {
		this.count2 = count2;
	}

	public long getCount3() {
		return count3;
	}

	public void setCount3(long count3) {
		this.count3 = count3;
	}

	public long getCount4() {
		return count4;
	}

	public void setCount4(long count4) {
		this.count4 = count4;
	}

	public long getCount5() {
		return count5;
	}

	public void setCount5(long count5) {
		this.count5 = count5;
	}

	public long getCount6() {
		return count6;
	}

	public void setCount6(long count6) {
		this.count6 = count6;
	}

	public long getCount7() {
		return count7;
	}

	public void setCount7(long count7) {
		this.count7 = count7;
	}

	public long getCount8() {
		return count8;
	}

	public void setCount8(long count8) {
		this.count8 = count8;
	}

	public long getCount9() {
		return count9;
	}

	public void setCount9(long count9) {
		this.count9 = count9;
	}

	public long getCount10() {
		return count10;
	}

	public void setCount10(long count10) {
		this.count10 = count10;
	}

	public Double getCount20() {
		return count20;
	}

	public void setCount20(Double count20) {
		this.count20 = count20;
	}

	public Double getCount21() {
		return count21;
	}

	public void setCount21(Double count21) {
		this.count21 = count21;
	}

	public Double getCount22() {
		return count22;
	}

	public void setCount22(Double count22) {
		this.count22 = count22;
	}

	public Double getCount23() {
		return count23;
	}

	public void setCount23(Double count23) {
		this.count23 = count23;
	}

	public Double getCount24() {
		return count24;
	}

	public void setCount24(Double count24) {
		this.count24 = count24;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public Date getUpdateAt1() {
		return updateAt1;
	}

	public void setUpdateAt1(Date updateAt1) {
		this.updateAt1 = updateAt1;
	}

	public Date getUpdateAt2() {
		return updateAt2;
	}

	public void setUpdateAt2(Date updateAt2) {
		this.updateAt2 = updateAt2;
	}

	public Date getUpdateAt3() {
		return updateAt3;
	}

	public void setUpdateAt3(Date updateAt3) {
		this.updateAt3 = updateAt3;
	}

	public Date getUpdateAt4() {
		return updateAt4;
	}

	public void setUpdateAt4(Date updateAt4) {
		this.updateAt4 = updateAt4;
	}

	public Date getUpdateAt5() {
		return updateAt5;
	}

	public void setUpdateAt5(Date updateAt5) {
		this.updateAt5 = updateAt5;
	}

	public Date getUpdateAt6() {
		return updateAt6;
	}

	public void setUpdateAt6(Date updateAt6) {
		this.updateAt6 = updateAt6;
	}

	public Date getUpdateAt7() {
		return updateAt7;
	}

	public void setUpdateAt7(Date updateAt7) {
		this.updateAt7 = updateAt7;
	}

	public Date getUpdateAt8() {
		return updateAt8;
	}

	public void setUpdateAt8(Date updateAt8) {
		this.updateAt8 = updateAt8;
	}

	public Date getUpdateAt9() {
		return updateAt9;
	}

	public void setUpdateAt9(Date updateAt9) {
		this.updateAt9 = updateAt9;
	}

	public Date getUpdateAt10() {
		return updateAt10;
	}

	public void setUpdateAt10(Date updateAt10) {
		this.updateAt10 = updateAt10;
	}

	public Date getUpdateAt20() {
		return updateAt20;
	}

	public void setUpdateAt20(Date updateAt20) {
		this.updateAt20 = updateAt20;
	}

	public Date getUpdateAt21() {
		return updateAt21;
	}

	public void setUpdateAt21(Date updateAt21) {
		this.updateAt21 = updateAt21;
	}

	public Date getUpdateAt22() {
		return updateAt22;
	}

	public void setUpdateAt22(Date updateAt22) {
		this.updateAt22 = updateAt22;
	}

	public Date getUpdateAt23() {
		return updateAt23;
	}

	public void setUpdateAt23(Date updateAt23) {
		this.updateAt23 = updateAt23;
	}

	public Date getUpdateAt24() {
		return updateAt24;
	}

	public void setUpdateAt24(Date updateAt24) {
		this.updateAt24 = updateAt24;
	}
}
