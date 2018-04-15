package com.lanking.uxb.service.file.convert;

import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.base.file.Space;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.file.value.VSpace;

/**
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version V1.0.0,2014年11月17日
 *
 */
@Component
public class SpaceConverter extends Converter<VSpace, Space, Long> {

	@Override
	protected Long getId(Space s) {
		return s.getId();
	}

	@Override
	protected VSpace convert(Space s) {
		VSpace vspace = new VSpace();
		vspace.setId(s.getId());
		vspace.setName(s.getName());
		vspace.setFileCount(s.getFileCount());
		vspace.setMaxFileSize(s.getMaxFileSize());
		vspace.setUsedSize(s.getUsedSize());
		vspace.setSize(s.getSize());
		vspace.setAuthorization(s.isAuthorization());
		vspace.setPreview(s.isPreview());
		return vspace;
	}

}
