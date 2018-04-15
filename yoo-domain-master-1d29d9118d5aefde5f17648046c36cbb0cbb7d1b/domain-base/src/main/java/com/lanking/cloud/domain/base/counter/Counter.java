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
 * 计数表
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月17日
 */
@Entity
@Table(name = "counter")
public class Counter implements Serializable {

	private static final long serialVersionUID = 1475058129915212633L;

	@Id
	@GeneratedValue(generator = "snowflake")
	@GenericGenerator(name = "snowflake", strategy = SnowflakeGenerator.TYPE)
	private Long id;

	/**
	 * 业务对象类型
	 * 
	 * @see Biz
	 */
	@Column(name = "biz", precision = 3)
	private int biz;

	/**
	 * 业务对象ID
	 */
	@Column(name = "biz_id")
	private long bizId;

	@Column(name = "count_1", columnDefinition = "bigint default 0")
	private long count1 = 0;

	@Column(name = "count_2", columnDefinition = "bigint default 0")
	private long count2 = 0;

	@Column(name = "count_3", columnDefinition = "bigint default 0")
	private long count3 = 0;

	@Column(name = "count_4", columnDefinition = "bigint default 0")
	private long count4 = 0;

	@Column(name = "count_5", columnDefinition = "bigint default 0")
	private long count5 = 0;

	@Column(name = "count_6", columnDefinition = "bigint default 0")
	private long count6 = 0;

	@Column(name = "count_7", columnDefinition = "bigint default 0")
	private long count7 = 0;

	@Column(name = "count_8", columnDefinition = "bigint default 0")
	private long count8 = 0;

	@Column(name = "count_9", columnDefinition = "bigint default 0")
	private long count9 = 0;

	@Column(name = "count_10", columnDefinition = "bigint default 0")
	private long count10 = 0;

	@Column(name = "count_11", columnDefinition = "bigint default 0")
	private long count11 = 0;

	@Column(name = "count_12", columnDefinition = "bigint default 0")
	private long count12 = 0;

	@Column(name = "count_13", columnDefinition = "bigint default 0")
	private long count13 = 0;

	@Column(name = "count_14", columnDefinition = "bigint default 0")
	private long count14 = 0;

	@Column(name = "count_15", columnDefinition = "bigint default 0")
	private long count15 = 0;

	@Column(name = "count_16", columnDefinition = "bigint default 0")
	private long count16 = 0;

	@Column(name = "count_17", columnDefinition = "bigint default 0")
	private long count17 = 0;

	@Column(name = "count_18", columnDefinition = "bigint default 0")
	private long count18 = 0;

	@Column(name = "count_19", columnDefinition = "bigint default 0")
	private long count19 = 0;

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

	public long getCount11() {
		return count11;
	}

	public void setCount11(long count11) {
		this.count11 = count11;
	}

	public long getCount12() {
		return count12;
	}

	public void setCount12(long count12) {
		this.count12 = count12;
	}

	public long getCount13() {
		return count13;
	}

	public void setCount13(long count13) {
		this.count13 = count13;
	}

	public long getCount14() {
		return count14;
	}

	public void setCount14(long count14) {
		this.count14 = count14;
	}

	public long getCount15() {
		return count15;
	}

	public void setCount15(long count15) {
		this.count15 = count15;
	}

	public long getCount16() {
		return count16;
	}

	public void setCount16(long count16) {
		this.count16 = count16;
	}

	public long getCount17() {
		return count17;
	}

	public void setCount17(long count17) {
		this.count17 = count17;
	}

	public long getCount18() {
		return count18;
	}

	public void setCount18(long count18) {
		this.count18 = count18;
	}

	public long getCount19() {
		return count19;
	}

	public void setCount19(long count19) {
		this.count19 = count19;
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

	public Date getUpdateAt() {
		return updateAt;
	}

	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}

}
