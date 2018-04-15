package com.lanking.uxb.service.zuoye.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VKnowpoint;
import com.lanking.uxb.service.zuoye.value.VLevelKnowpoint;

@Component
public class ZyLevelKnowpointConvert extends Converter<VLevelKnowpoint, VKnowpoint, Long> {

	@Override
	protected Long getId(VKnowpoint s) {
		return Long.valueOf(s.getCode());
	}

	@Override
	protected VLevelKnowpoint convert(VKnowpoint s) {
		VLevelKnowpoint v = new VLevelKnowpoint();
		v.setPcode(s.getPcode());
		v.setCode(Long.valueOf(s.getCode()));
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.getAllChild().add(Long.valueOf(s.getCode()));
		return v;
	}

	private void internalAssemblySectionTree(List<VLevelKnowpoint> dest, VLevelKnowpoint v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VLevelKnowpoint pc : dest) {
				if (pc.getCode() == v.getPcode()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblySectionTree(pc.getChildren(), v);
				}
			}
		}
	}

	/**
	 * 组装树形结构的知识点数据
	 * 
	 * @param src
	 * @return List<VLevelKnowpoint>
	 */
	public List<VLevelKnowpoint> assemblySectionTree(List<VLevelKnowpoint> src) {
		List<VLevelKnowpoint> dest = Lists.newArrayList();
		for (VLevelKnowpoint v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return dest;
	}

	private void assemblyFallibleCount(VLevelKnowpoint v) {
		List<VLevelKnowpoint> children = v.getChildren();
		if (children.size() > 0) {
			long fallibleCount = 0;
			long collectCount = 0;
			long schoolQCount = 0;
			for (VLevelKnowpoint c : children) {
				v.getAllChild().addAll(c.getAllChild());
				fallibleCount += c.getFallibleCount();
				collectCount += c.getCollectCount();
				schoolQCount += c.getSchoolQCount();
				assemblyFallibleCount(c);
			}
			v.setFallibleCount(fallibleCount);
			v.setCollectCount(collectCount);
			v.setSchoolQCount(schoolQCount);
		}
	}

	private void assemblyFallibleCount(List<VLevelKnowpoint> dest) {
		for (VLevelKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
		for (VLevelKnowpoint v : dest) {
			assemblyFallibleCount(v);
		}
	}

}
