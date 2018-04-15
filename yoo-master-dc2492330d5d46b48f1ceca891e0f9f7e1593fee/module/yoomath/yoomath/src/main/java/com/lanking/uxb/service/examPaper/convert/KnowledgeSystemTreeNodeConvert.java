package com.lanking.uxb.service.examPaper.convert;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.baseData.KnowledgeSystem;
import com.lanking.cloud.sdk.bean.Converter;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.service.examPaper.value.VKnowledgeTreeNode;

/**
 * KnowledgeSystem -> VKnowledgeSystemTreeNode
 *
 * @author xinyu.zhou
 * @since 2.3.0
 */
@Component
public class KnowledgeSystemTreeNodeConvert extends Converter<VKnowledgeTreeNode, KnowledgeSystem, Long> {
	@Override
	protected Long getId(KnowledgeSystem knowledgeSystem) {
		return knowledgeSystem.getCode();
	}

	@Override
	protected VKnowledgeTreeNode convert(KnowledgeSystem knowledgeSystem) {
		VKnowledgeTreeNode v = new VKnowledgeTreeNode();
		v.setCode(knowledgeSystem.getCode());
		v.setLevel(knowledgeSystem.getLevel());
		v.setName(knowledgeSystem.getName());
		v.setPcode(knowledgeSystem.getPcode());
		v.setSequence(knowledgeSystem.getSequence());
		v.setSystem(true);
		v.getAllChild().add(knowledgeSystem.getCode());
		return v;
	}

	private void internalAssemblyPointTree(List<VKnowledgeTreeNode> dest, VKnowledgeTreeNode v) {
		if (v.getLevel() == 1) {
			dest.add(v);
		} else {
			for (VKnowledgeTreeNode pc : dest) {
				if (pc.getCode().equals(v.getPcode())) {
					pc.getChildren().add(v);
					break;
				} else {
					this.internalAssemblyPointTree(pc.getChildren(), v);
				}
			}
		}
	}

	private void assemblyCardCount(List<VKnowledgeTreeNode> dest) {
		for (VKnowledgeTreeNode v : dest) {
			assemblyCardCount(v);
		}
		for (VKnowledgeTreeNode v : dest) {
			assemblyCardCount(v);
		}
		for (VKnowledgeTreeNode v : dest) {
			assemblyCardCount(v);
		}
	}

	public List<VKnowledgeTreeNode> assemblyPointTree(List<VKnowledgeTreeNode> src) {
		List<VKnowledgeTreeNode> dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : src) {
			internalAssemblyPointTree(dest, v);
		}
		return dest;
	}

	public List<VKnowledgeTreeNode> assemblyPointTreeFilterNoCard(List<VKnowledgeTreeNode> src) {
		List<VKnowledgeTreeNode> dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : src) {
			internalAssemblyPointTree(dest, v);
		}
		assemblyCardCount(dest);
		return filterNoCard(dest);
	}

	public List<VKnowledgeTreeNode> assemblyPointTreeFilterNoFall(List<VKnowledgeTreeNode> src) {
		List<VKnowledgeTreeNode> dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : src) {
			internalAssemblyPointTree(dest, v);
		}
		assemblyCardCount(dest);
		return filterNoFall(dest);
	}

	public List<VKnowledgeTreeNode> assemblyPointTreeFilterNoCollect(List<VKnowledgeTreeNode> src) {
		List<VKnowledgeTreeNode> dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : src) {
			internalAssemblyPointTree(dest, v);
		}
		assemblyCardCount(dest);
		return filterNoCollect(dest);
	}

	public List<VKnowledgeTreeNode> assemblyPointTreeFilterNoSchoolQuestion(List<VKnowledgeTreeNode> src) {
		List<VKnowledgeTreeNode> dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : src) {
			internalAssemblyPointTree(dest, v);
		}
		assemblyCardCount(dest);
		return filterNoSchoolQuestion(dest);
	}

	private void assemblyCardCount(VKnowledgeTreeNode v) {
		List<VKnowledgeTreeNode> children = v.getChildren();
		if (children.size() > 0) {
			long knowCardCount = 0;
			long fallibleCount = 0;
			long collectCount = 0;
			long schoolQCount = 0;
			for (VKnowledgeTreeNode c : children) {
				v.getAllChild().addAll(c.getAllChild());
				knowCardCount += c.getKnowCardCount();
				fallibleCount += c.getFallibleCount();
				collectCount += c.getCollectCount();
				schoolQCount += c.getSchoolQCount();
				assemblyCardCount(c);
			}
			v.setKnowCardCount(knowCardCount);
			v.setFallibleCount(fallibleCount);
			v.setCollectCount(collectCount);
			v.setSchoolQCount(schoolQCount);
		}
	}

	private List<VKnowledgeTreeNode> filterNoCard(List<VKnowledgeTreeNode> dest) {
		List<VKnowledgeTreeNode> $dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : dest) {
			List<VKnowledgeTreeNode> cvs = v.getChildren();
			v.setChildren(new ArrayList<VKnowledgeTreeNode>());
			if (v.getKnowCardCount() != null && v.getKnowCardCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VKnowledgeTreeNode cv : cvs) {
						List<VKnowledgeTreeNode> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VKnowledgeTreeNode>());
						if (cv.getKnowCardCount() != null && cv.getKnowCardCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VKnowledgeTreeNode ccv : ccvs) {
									List<VKnowledgeTreeNode> cccvs = ccv.getChildren();
									ccv.setChildren(new ArrayList<VKnowledgeTreeNode>());
									if (ccv.getKnowCardCount() != null && ccv.getKnowCardCount().longValue() > 0) {
										$dest.add(ccv);
										if (CollectionUtils.isNotEmpty(cccvs)) {
											for (VKnowledgeTreeNode cccv : cccvs) {
												if (cccv.getKnowCardCount() != null
														&& cccv.getKnowCardCount().longValue() > 0) {
													$dest.add(cccv);

												}
											}
										}
									}
								}
							}
						}

					}
				}
			}

		}
		List<VKnowledgeTreeNode> $$desc = Lists.newArrayList();
		for (VKnowledgeTreeNode v : $dest) {
			internalAssemblyPointTree($$desc, v);
		}
		return $$desc;
	}

	private List<VKnowledgeTreeNode> filterNoFall(List<VKnowledgeTreeNode> dest) {
		List<VKnowledgeTreeNode> $dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : dest) {
			List<VKnowledgeTreeNode> cvs = v.getChildren();
			v.setChildren(new ArrayList<VKnowledgeTreeNode>());
			if (v.getFallibleCount() != null && v.getFallibleCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VKnowledgeTreeNode cv : cvs) {
						List<VKnowledgeTreeNode> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VKnowledgeTreeNode>());
						if (cv.getFallibleCount() != null && cv.getFallibleCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VKnowledgeTreeNode ccv : ccvs) {
									List<VKnowledgeTreeNode> cccvs = ccv.getChildren();
									ccv.setChildren(new ArrayList<VKnowledgeTreeNode>());
									if (ccv.getFallibleCount() != null && ccv.getFallibleCount().longValue() > 0) {
										$dest.add(ccv);
										if (CollectionUtils.isNotEmpty(cccvs)) {
											for (VKnowledgeTreeNode cccv : cccvs) {
												if (cccv.getFallibleCount() != null
														&& cccv.getFallibleCount().longValue() > 0) {
													$dest.add(cccv);

												}
											}
										}
									}
								}
							}
						}

					}
				}
			}

		}
		List<VKnowledgeTreeNode> $$desc = Lists.newArrayList();
		for (VKnowledgeTreeNode v : $dest) {
			internalAssemblyPointTree($$desc, v);
		}
		return $$desc;
	}

	private List<VKnowledgeTreeNode> filterNoCollect(List<VKnowledgeTreeNode> dest) {
		List<VKnowledgeTreeNode> $dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : dest) {
			List<VKnowledgeTreeNode> cvs = v.getChildren();
			v.setChildren(new ArrayList<VKnowledgeTreeNode>());
			if (v.getCollectCount() != null && v.getCollectCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VKnowledgeTreeNode cv : cvs) {
						List<VKnowledgeTreeNode> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VKnowledgeTreeNode>());
						if (cv.getCollectCount() != null && cv.getCollectCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VKnowledgeTreeNode ccv : ccvs) {
									List<VKnowledgeTreeNode> cccvs = ccv.getChildren();
									ccv.setChildren(new ArrayList<VKnowledgeTreeNode>());
									if (ccv.getCollectCount() != null && ccv.getCollectCount().longValue() > 0) {
										$dest.add(ccv);
										if (CollectionUtils.isNotEmpty(cccvs)) {
											for (VKnowledgeTreeNode cccv : cccvs) {
												if (cccv.getCollectCount() != null
														&& cccv.getCollectCount().longValue() > 0) {
													$dest.add(cccv);

												}
											}
										}
									}
								}
							}
						}

					}
				}
			}

		}
		List<VKnowledgeTreeNode> $$desc = Lists.newArrayList();
		for (VKnowledgeTreeNode v : $dest) {
			internalAssemblyPointTree($$desc, v);
		}
		return $$desc;
	}

	private List<VKnowledgeTreeNode> filterNoSchoolQuestion(List<VKnowledgeTreeNode> dest) {
		List<VKnowledgeTreeNode> $dest = Lists.newArrayList();
		for (VKnowledgeTreeNode v : dest) {
			List<VKnowledgeTreeNode> cvs = v.getChildren();
			v.setChildren(new ArrayList<VKnowledgeTreeNode>());
			if (v.getSchoolQCount() != null && v.getSchoolQCount().longValue() > 0) {
				$dest.add(v);
				if (CollectionUtils.isNotEmpty(cvs)) {
					for (VKnowledgeTreeNode cv : cvs) {
						List<VKnowledgeTreeNode> ccvs = cv.getChildren();
						cv.setChildren(new ArrayList<VKnowledgeTreeNode>());
						if (cv.getSchoolQCount() != null && cv.getSchoolQCount().longValue() > 0) {
							$dest.add(cv);
							if (CollectionUtils.isNotEmpty(ccvs)) {
								for (VKnowledgeTreeNode ccv : ccvs) {
									List<VKnowledgeTreeNode> cccvs = ccv.getChildren();
									ccv.setChildren(new ArrayList<VKnowledgeTreeNode>());
									if (ccv.getSchoolQCount() != null && ccv.getSchoolQCount().longValue() > 0) {
										$dest.add(ccv);
										if (CollectionUtils.isNotEmpty(cccvs)) {
											for (VKnowledgeTreeNode cccv : cccvs) {
												if (cccv.getSchoolQCount() != null
														&& cccv.getSchoolQCount().longValue() > 0) {
													$dest.add(cccv);

												}
											}
										}
									}
								}
							}
						}

					}
				}
			}

		}
		List<VKnowledgeTreeNode> $$desc = Lists.newArrayList();
		for (VKnowledgeTreeNode v : $dest) {
			internalAssemblyPointTree($$desc, v);
		}
		return $$desc;
	}
}
