package com.lanking.uxb.service.zuoye.convert;

import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.uxb.service.code.value.VSection;
import com.lanking.uxb.service.zuoye.value.VExerciseSection;
import com.lanking.uxb.service.zuoye.value.VTextbookExercise;

@Component
public class ZyExerciseSectionConvert extends Converter<VExerciseSection, VSection, Long> {

	@Override
	protected Long getId(VSection s) {
		return s.getCode();
	}

	public void assemblyExercise(List<VExerciseSection> vs, List<VTextbookExercise> exercises,
			Long selectTextbookExerciseId) {
		for (VExerciseSection v : vs) {
			for (VTextbookExercise ve : exercises) {
				if (ve.getSectionCode() == v.getCode()) {
					v.getTextbookExercises().add(ve);
					if (selectTextbookExerciseId != null && ve.getId() == selectTextbookExerciseId.longValue()) {
						v.setSelected(true);
					}
				}
			}
		}
	}

	@Override
	protected VExerciseSection convert(VSection s) {
		VExerciseSection v = new VExerciseSection();
		v.setPcode(s.getPcode());
		v.setCode(s.getCode());
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.setTextbookCode(s.getTextbookCode());
		return v;
	}

	private void internalAssemblySectionTree(List<VExerciseSection> dest, VExerciseSection v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VExerciseSection pc : dest) {
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
	 * 组装树形结构的章节数据
	 * 
	 * @since 2.1
	 * @param src
	 *            section VO list
	 * @return 树形结构的章节数据
	 */
	public List<VExerciseSection> assemblySectionTree(List<VExerciseSection> src) {
		List<VExerciseSection> dest = Lists.newArrayList();
		for (VExerciseSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		return dest;
	}

}
