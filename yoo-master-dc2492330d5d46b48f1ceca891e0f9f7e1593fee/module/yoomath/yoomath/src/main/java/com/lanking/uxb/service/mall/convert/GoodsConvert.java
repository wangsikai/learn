package com.lanking.uxb.service.mall.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.yoo.goods.Goods;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.util.FileUtil;
import com.lanking.uxb.service.mall.value.VGoods;

/**
 * 商品convert
 * 
 * @since 2.0.3
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2016年3月14日
 */
@Component
public class GoodsConvert extends Converter<VGoods, Goods, Long> {

	@Override
	protected Long getId(Goods s) {
		return s.getId();
	}

	@Override
	protected VGoods convert(Goods s) {
		VGoods v = new VGoods();
		assemblerVO(s, v);
		return v;
	}

	void assemblerVO(Goods s, VGoods v) {
		v.setId(s.getId());
		v.setGoodsSnapshotId(s.getGoodsSnapshotId());
		v.setName(s.getName());
		v.setIntroduction(validBlank(s.getIntroduction()));
		v.setContent(validBlank(s.getContent()));
		if (s.getImage() != null) {
			v.setImage(s.getImage());
			v.setImageUrl(FileUtil.getUrl(s.getImage()));
			v.setImageMidUrl(FileUtil.getUrl(s.getImage()));
			v.setImageMinUrl(FileUtil.getUrl(s.getImage()));
		}
		v.setPrice(s.getPrice());
		v.setPrice(s.getPrice());
		v.setSalesTime(s.getSalesTime());
		v.setSoldOutTime(s.getSoldOutTime());
		v.setType(s.getType());
	}

}
