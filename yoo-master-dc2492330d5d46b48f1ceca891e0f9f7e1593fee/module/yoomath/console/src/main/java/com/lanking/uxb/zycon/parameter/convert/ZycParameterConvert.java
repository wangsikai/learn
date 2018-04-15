package com.lanking.uxb.zycon.parameter.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.frame.config.Parameter;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.zycon.parameter.value.VParameter;

/**
 * 系统配置转换
 * 
 * @author zemin.song
 * @version 2016年12月9日
 */
@Component
public class ZycParameterConvert extends Converter<VParameter, Parameter, Long> {

	@Override
	protected Long getId(Parameter s) {
		return s.getId();
	}

	@Override
	protected VParameter convert(Parameter s) {
		VParameter vo = new VParameter();
		vo.setId(s.getId());
		vo.setKey(s.getKey());
		vo.setValue(s.getValue());
		vo.setNote(s.getNote());
		vo.setProduct(s.getProduct());
		vo.setStatus(s.getStatus());
		return vo;
	}

}
