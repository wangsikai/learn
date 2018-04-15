package com.lanking.cloud.domain.yoo.goods.coins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.lanking.cloud.sdk.util.StringUtils;

/**
 * 金币商品被兑换的数量计数
 * 
 * @since 3.9.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年3月21日
 */
@MappedSuperclass
public class CoinsGoodsDaySellCountKey implements Serializable {

	private static final long serialVersionUID = 5747983167945530629L;

	/**
	 * 金币商品ID
	 */
	@Id
	@Column(name = "coins_goods_id", nullable = false)
	private long coinsGoodsId;

	/**
	 * 日期,格式为:20160315
	 */
	@Id
	@Column(name = "date0", nullable = false)
	private long date0;

	public CoinsGoodsDaySellCountKey(long coinsGoodsId, long date0) {
		this.coinsGoodsId = coinsGoodsId;
		this.date0 = date0;
	}

	public CoinsGoodsDaySellCountKey() {
	}

	public long getCoinsGoodsId() {
		return coinsGoodsId;
	}

	public void setCoinsGoodsId(long coinsGoodsId) {
		this.coinsGoodsId = coinsGoodsId;
	}

	public long getDate0() {
		return date0;
	}

	public void setDate0(long date0) {
		this.date0 = date0;
	}

	@Override
	public int hashCode() {
		return (int) (coinsGoodsId * 37 + date0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CoinsGoodsDaySellCountKey) {
			CoinsGoodsDaySellCountKey other = (CoinsGoodsDaySellCountKey) obj;
			return coinsGoodsId == other.coinsGoodsId && date0 == other.date0;
		}
		return false;
	}

	@Override
	public String toString() {
		return StringUtils.join(new Object[] { coinsGoodsId, date0 }, '-');
	}
}
