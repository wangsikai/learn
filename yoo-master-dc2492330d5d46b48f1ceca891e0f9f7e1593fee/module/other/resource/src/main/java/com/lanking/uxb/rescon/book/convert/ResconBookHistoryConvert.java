package com.lanking.uxb.rescon.book.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.book.BookHistory;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.convert.ResconVendorUserConvert;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.rescon.book.value.VBookHistory;

@Component
public class ResconBookHistoryConvert extends Converter<VBookHistory, BookHistory, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconVendorUserConvert vendorUserConvert;

	@Override
	protected Long getId(BookHistory s) {
		return s.getId();
	}

	@Override
	protected VBookHistory convert(BookHistory s) {
		if (null == s) {
			return null;
		}
		VBookHistory v = new VBookHistory();
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		v.setBookId(s.getBookId());
		v.setType(s.getType());
		v.setVersion(s.getVersion());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 创建人
		assemblers.add(new ConverterAssembler<VBookHistory, BookHistory, Long, VVendorUser>() {
			@Override
			public boolean accept(BookHistory s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(BookHistory s, VBookHistory d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(BookHistory s, VBookHistory d, VVendorUser value) {
				d.setCreator(value);
			}

			@Override
			public VVendorUser getValue(Long key) {
				return key == null ? null : vendorUserConvert.to(vendorUserManage.getVendorUser(key));
			}

			@Override
			public Map<Long, VVendorUser> mgetValue(Collection<Long> keys) {
				return keys == null ? null : vendorUserConvert.to(vendorUserManage.mgetVendorUser(keys));
			}
		});
	}
}
