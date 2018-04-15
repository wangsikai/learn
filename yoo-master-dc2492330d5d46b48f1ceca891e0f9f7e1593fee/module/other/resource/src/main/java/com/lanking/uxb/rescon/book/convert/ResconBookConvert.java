package com.lanking.uxb.rescon.book.convert;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lanking.cloud.domain.common.resource.book.Book;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.bean.ConverterAssembler;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.account.api.ResconVendorUserManage;
import com.lanking.uxb.rescon.account.convert.ResconVendorUserConvert;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.rescon.book.value.VBook;
import com.lanking.uxb.service.code.api.SchoolService;
import com.lanking.uxb.service.code.convert.SchoolConvert;
import com.lanking.uxb.service.code.value.VSchool;

/**
 * 书本转换.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年11月3日
 */
@Component
public class ResconBookConvert extends Converter<VBook, Book, Long> {
	@Autowired
	private ResconVendorUserManage vendorUserManage;
	@Autowired
	private ResconVendorUserConvert vendorUserConvert;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private SchoolConvert schoolConvert;

	@Override
	protected Long getId(Book s) {
		return s.getId();
	}

	@Override
	protected VBook convert(Book s) {
		VBook v = new VBook();
		v.setId(s.getId());
		v.setCreateAt(s.getCreateAt());
		v.setNum(0);
		v.setStatus(s.getStatus());
		return v;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected void initAssemblers(List<ConverterAssembler> assemblers) {
		// 创建人
		assemblers.add(new ConverterAssembler<VBook, Book, Long, VVendorUser>() {
			@Override
			public boolean accept(Book s) {
				return true;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Book s, VBook d) {
				return s.getCreateId();
			}

			@Override
			public void setValue(Book s, VBook d, VVendorUser value) {
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

		// 学校
		assemblers.add(new ConverterAssembler<VBook, Book, Long, VSchool>() {
			@Override
			public boolean accept(Book s) {
				return s.getSchoolId() != null;
			}

			@Override
			public boolean accept(Map<String, Object> hints) {
				return true;
			}

			@Override
			public Long getKey(Book s, VBook d) {
				return s.getSchoolId();
			}

			@Override
			public void setValue(Book s, VBook d, VSchool value) {
				d.setSchool(value);
			}

			@Override
			public VSchool getValue(Long key) {
				return key == null ? null : schoolConvert.to(schoolService.get(key));
			}

			@Override
			public Map<Long, VSchool> mgetValue(Collection<Long> keys) {
				if (CollectionUtils.isEmpty(keys)) {
					return null;
				}
				return schoolConvert.to(schoolService.mget(keys));
			}
		});
	}
}
