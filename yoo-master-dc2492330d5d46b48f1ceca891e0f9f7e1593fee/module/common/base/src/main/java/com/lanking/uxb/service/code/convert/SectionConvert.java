package com.lanking.uxb.service.code.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.Section;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.code.value.VSection;

/**
 * 章节的转换接口
 * 
 * @since 2.1
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2015年7月13日
 */
@Component
public class SectionConvert extends Converter<VSection, Section, Long> {

	@Override
	protected Long getId(Section s) {
		return s.getCode();
	}

	@Override
	protected VSection convert(Section s) {
		VSection v = new VSection();
		v.setCode(s.getCode());
		v.setLevel(s.getLevel());
		v.setName(s.getName());
		v.setPcode(s.getPcode() == null ? 0 : s.getPcode());
		v.setTextbookCode(s.getTextbookCode());
		v.getAllChild().add(s.getCode());
		return v;
	}

	private void internalAssemblySectionTree(List<VSection> dest, VSection v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VSection pc : dest) {
				if (pc.getCode() == v.getPcode()) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblySectionTree(pc.getChildren(), v);
				}
			}
		}
	}

	private void assemblyFallibleCount(VSection v) {
		List<VSection> children = v.getChildren();
		if (children.size() > 0) {
			long fallibleCount = 0;
			long collectCount = 0;
			long schoolQCount = 0;
			long questionCount = 0;
			for (VSection c : children) {
				v.getAllChild().addAll(c.getAllChild());
				fallibleCount += c.getFallibleCount();
				collectCount += c.getCollectCount();
				schoolQCount += c.getSchoolQCount();
				questionCount += c.getQuestionCount();
				assemblyFallibleCount(c);
			}
			v.setFallibleCount(fallibleCount);
			v.setCollectCount(collectCount);
			v.setSchoolQCount(schoolQCount);
			v.setQuestionCount(questionCount);
		}
	}

	private void assemblyFallibleCount(List<VSection> dest) {
		for (VSection v : dest) {
			assemblyFallibleCount(v);
		}
		for (VSection v : dest) {
			assemblyFallibleCount(v);
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
	public List<VSection> assemblySectionTree(List<VSection> src) {
		List<VSection> dest = Lists.newArrayList();
		for (VSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return dest;
	}

	public List<VSection> assemblySectionTreeFilterNoQuestion(List<VSection> src) {
		List<VSection> dest = Lists.newArrayList();
		for (VSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return filterNoQuestion(dest);
	}

	private List<VSection> filterNoQuestion(List<VSection> dest) {
		List<VSection> $dest = Lists.newArrayList();
		for (VSection v : dest) {
			List<VSection> cvs = v.getChildren();
			v.setChildren(new ArrayList<VSection>());
			if (v.getQuestionCount() != null && v.getQuestionCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VSection cv : cvs) {
						List<VSection> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VSection>());
						if (cv.getQuestionCount() != null && cv.getQuestionCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VSection ccv : ccvs) {
									if (ccv.getQuestionCount() != null && ccv.getQuestionCount().longValue() > 0) {
										$dest.add(ccv);
									}
								}
							}
						}

					}
				}
			}

		}
		List<VSection> $$desc = Lists.newArrayList();
		for (VSection v : $dest) {
			internalAssemblySectionTree($$desc, v);
		}
		return $$desc;
	}

	public List<VSection> assemblySectionTreeFilterNoFall(List<VSection> src) {
		List<VSection> dest = Lists.newArrayList();
		for (VSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return filterNoFall(dest);
	}

	private List<VSection> filterNoFall(List<VSection> dest) {
		List<VSection> $dest = Lists.newArrayList();
		for (VSection v : dest) {
			List<VSection> cvs = v.getChildren();
			v.setChildren(new ArrayList<VSection>());
			if (v.getFallibleCount() != null && v.getFallibleCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VSection cv : cvs) {
						List<VSection> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VSection>());
						if (cv.getFallibleCount() != null && cv.getFallibleCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VSection ccv : ccvs) {
									if (ccv.getFallibleCount() != null && ccv.getFallibleCount().longValue() > 0) {
										$dest.add(ccv);
									}
								}
							}
						}

					}
				}
			}

		}
		List<VSection> $$desc = Lists.newArrayList();
		for (VSection v : $dest) {
			internalAssemblySectionTree($$desc, v);
		}
		return $$desc;
	}

	public List<VSection> assemblySectionTreeFilterNoCollect(List<VSection> src) {
		List<VSection> dest = Lists.newArrayList();
		for (VSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return filterNoCollect(dest);
	}

	private List<VSection> filterNoCollect(List<VSection> dest) {
		List<VSection> $dest = Lists.newArrayList();
		for (VSection v : dest) {
			List<VSection> cvs = v.getChildren();
			v.setChildren(new ArrayList<VSection>());
			if (v.getCollectCount() != null && v.getCollectCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VSection cv : cvs) {
						List<VSection> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VSection>());
						if (cv.getCollectCount() != null && cv.getCollectCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VSection ccv : ccvs) {
									if (ccv.getCollectCount() != null && ccv.getCollectCount().longValue() > 0) {
										$dest.add(ccv);
									}
								}
							}
						}

					}
				}
			}

		}
		List<VSection> $$desc = Lists.newArrayList();
		for (VSection v : $dest) {
			internalAssemblySectionTree($$desc, v);
		}
		return $$desc;
	}

	public List<VSection> assemblySectionTreeFilterNoSchoolQuestion(List<VSection> src) {
		List<VSection> dest = Lists.newArrayList();
		for (VSection v : src) {
			internalAssemblySectionTree(dest, v);
		}
		assemblyFallibleCount(dest);
		return filterNoSchoolQuestion(dest);
	}

	private List<VSection> filterNoSchoolQuestion(List<VSection> dest) {
		List<VSection> $dest = Lists.newArrayList();
		for (VSection v : dest) {
			List<VSection> cvs = v.getChildren();
			v.setChildren(new ArrayList<VSection>());
			if (v.getSchoolQCount() != null && v.getSchoolQCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VSection cv : cvs) {
						List<VSection> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VSection>());
						if (cv.getSchoolQCount() != null && cv.getSchoolQCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VSection ccv : ccvs) {
									if (ccv.getSchoolQCount() != null && ccv.getSchoolQCount().longValue() > 0) {
										$dest.add(ccv);
									}
								}
							}
						}

					}
				}
			}

		}
		List<VSection> $$desc = Lists.newArrayList();
		for (VSection v : $dest) {
			internalAssemblySectionTree($$desc, v);
		}
		return $$desc;
	}
}
