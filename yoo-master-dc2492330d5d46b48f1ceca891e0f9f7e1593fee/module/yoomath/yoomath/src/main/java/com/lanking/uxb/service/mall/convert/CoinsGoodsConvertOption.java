package com.lanking.uxb.service.mall.convert;

/**
 * 金币商品转换选项
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月15日
 */
public class CoinsGoodsConvertOption {

	private boolean initDaySelledCount = false;

	public CoinsGoodsConvertOption() {
		super();
	}

	public CoinsGoodsConvertOption(boolean initDaySelledCount) {
		super();
		this.initDaySelledCount = initDaySelledCount;
	}

	public boolean isInitDaySelledCount() {
		return initDaySelledCount;
	}

	public void setInitDaySelledCount(boolean initDaySelledCount) {
		this.initDaySelledCount = initDaySelledCount;
	}

}
