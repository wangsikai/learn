package com.lanking.uxb.service.file.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.FileStyle;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.value.VFileStyle;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月17日
 *
 */
@Component
public class FileStyleConverter extends Converter<VFileStyle, FileStyle, Long> {

	@Override
	protected Long getId(FileStyle s) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected VFileStyle convert(FileStyle s) {
		// TODO Auto-generated method stub
		return null;
	}
}
