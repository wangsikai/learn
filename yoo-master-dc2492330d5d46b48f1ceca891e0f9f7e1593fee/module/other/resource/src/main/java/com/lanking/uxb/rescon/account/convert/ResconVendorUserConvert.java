package com.lanking.uxb.rescon.account.convert;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lanking.cloud.domain.support.resources.vendor.VendorUser;
import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.StringUtils;
import com.lanking.uxb.rescon.account.type.FileStyle;
import com.lanking.uxb.rescon.account.value.VVendorUser;
import com.lanking.uxb.rescon.auth.api.DataPermission;
import com.lanking.uxb.service.file.util.FileUtil;

@Component
public class ResconVendorUserConvert extends Converter<VVendorUser, VendorUser, Long> {

	@Override
	protected Long getId(VendorUser s) {
		return s.getId();
	}

	@Override
	protected VVendorUser convert(VendorUser s) {
		VVendorUser v = new VVendorUser();
		v.setId(s.getId());
		v.setName(s.getName());
		v.setStatus(s.getStatus());
		v.setType(s.getType());
		v.setTypeName(s.getType().getCnName());
		if (StringUtils.isBlank(v.getTypeName())) {
			v.setTypeName("暂未分配");
		}
		v.setVendorId(s.getVendorId());
		v.setAvatarId(s.getAvatarId() == null ? 0 : s.getAvatarId());
		v.setAvatarUrl(FileUtil.getUrl(v.getAvatarId()));
		v.setMidAvatarUrl(FileUtil.getUrl(FileStyle.VENDOR_AVATAR_MID, v.getAvatarId()));
		if (s.getType() != UserType.VENDOR_ADMIN) {
			v.setPwd(s.getPassword2());
		}
		v.setRealName(s.getRealName());
		if (StringUtils.isNotBlank(s.getPermissions())) {
			v.setPermission((DataPermission) JSON.parseObject(s.getPermissions(), DataPermission.class));
		}
		return v;
	}
}
