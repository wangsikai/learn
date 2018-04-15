package com.lanking.uxb.rescon.teach.api.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.lanking.cloud.component.db.support.hibernate.Repo;
import com.lanking.cloud.domain.common.resource.card.CardStatus;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalog;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistCatalogElement;
import com.lanking.cloud.domain.common.resource.teachAssist.TeachAssistElementType;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.uxb.rescon.common.ex.ResourceConsoleException;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistCatalogManage;
import com.lanking.uxb.rescon.teach.api.ResconTeachAssistElementService;
import com.lanking.uxb.rescon.teach.convert.ResconTeachAssistCatalogConvert;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForm;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForm.CatalogTag;
import com.lanking.uxb.rescon.teach.form.TeachAssistCatalogForms;
import com.lanking.uxb.rescon.teach.form.TeachAssistElementForm;
import com.lanking.uxb.rescon.teach.value.VTeachAssistCatalog;

@Transactional(readOnly = true)
@Service
public class ResconTeachAssistCatalogImpl implements ResconTeachAssistCatalogManage {
	@Autowired
	@Qualifier("TeachAssistCatalogRepo")
	Repo<TeachAssistCatalog, Long> teachAssistCatalogRepo;
	@Autowired
	@Qualifier("TeachAssistCatalogElementRepo")
	Repo<TeachAssistCatalogElement, Long> teachAssistCatalogElementRepo;

	@Autowired
	private ResconTeachAssistCatalogConvert teachAssistCatalogConvert;
	@Autowired
	private ResconTeachAssistElementService resconTeachAssistElementService;

	@Override
	public List<TeachAssistCatalog> listTeachAssistCatalogCatalog(long teachAssistVersionId) {
		return teachAssistCatalogRepo.find("$listTeachAssistCatalogCatalog",
				Params.param("teachAssistVersionId", teachAssistVersionId)).list();
	}

	@Override
	@Transactional
	public VTeachAssistCatalog addCatalog(TeachAssistCatalogForm catalogForm, long createId)
			throws ResourceConsoleException {
		Date date = new Date();
		TeachAssistCatalog catalog = new TeachAssistCatalog();
		catalog.setCreateAt(date);
		catalog.setCreateId(createId);
		if (catalogForm.getPid() == null || catalogForm.getPid() == 0) {
			catalog.setPid(0L);
			catalog.setLevel(1);
			long count = this.childrenCount(0L);
			catalog.setSequence((int) count + 1);
		} else {
			TeachAssistCatalog p = teachAssistCatalogRepo.get(catalogForm.getPid());
			catalog.setPid(catalogForm.getPid());
			catalog.setLevel(catalogForm.getLevel());
			catalog.setSequence(p.getSequence() + 1);

			// 更新新增菜单后面的目录顺序
			teachAssistCatalogRepo.execute(
					"$updateSequence",
					Params.param("teachassistVersionId", catalogForm.getTeachassistVersionId())
							.put("sequence", p.getSequence()).put("updateId", createId).put("incr", 1).put("nowDate", date));
		}
		catalog.setName(catalogForm.getName());
		catalog.setTeachassistVersionId(catalogForm.getTeachassistVersionId());
		catalog.setUpdateAt(date);
		catalog.setUpdateId(createId);
		if (catalogForm.getCatalogTag() != null && catalogForm.getCatalogTag() != CatalogTag.BLANK) {
			catalog.setName(catalogForm.getCatalogTag().getName());
		}
		teachAssistCatalogRepo.save(catalog);

		List<TeachAssistCatalog> catalogs = new ArrayList<TeachAssistCatalog>();
		if (catalogForm.getCatalogTag() != null) {
			// 预设菜单
			if (catalogForm.getCatalogTag() == CatalogTag.NEW) {
				// 新授课时
				catalogs.addAll(this.initChildTeachAssistCatalogs(catalog, "新课导学", "课堂学习", "课后作业"));
			} else if (catalogForm.getCatalogTag() == CatalogTag.TRAIN) {
				// 训练课时
				catalogs.addAll(this.initChildTeachAssistCatalogs(catalog, "知识回顾", "课堂练习"));
			} else if (catalogForm.getCatalogTag() == CatalogTag.REVIEW) {
				// 复习课时
				catalogs.addAll(this.initChildTeachAssistCatalogs(catalog, "知识回顾", "易错疑难", "课堂小结", "专项训练"));
			} else if (catalogForm.getCatalogTag() == CatalogTag.CHAPTER) {
				// 本章复习
				catalogs.addAll(this.initChildTeachAssistCatalogs(catalog, "知识拓扑图", "知识专题总结", "数学思想总结", "本章综合评价"));
			}
			teachAssistCatalogRepo.save(catalogs);

			// 预设模块 TODO
		}
		catalogs.add(catalog);
		return teachAssistCatalogConvert.assemblyCatalogTree(teachAssistCatalogConvert.to(catalogs)).get(0);
	}

	@Override
	@Transactional
	public void batchSaveCatalogs(TeachAssistCatalogForms catalogForms, long updateId) throws ResourceConsoleException {

		// 首先删除指定的菜单集合
		if (catalogForms.getDeleteCatalogs() != null && catalogForms.getDeleteCatalogs().size() != 0) {
			teachAssistCatalogRepo
					.execute("$deleteByIds", Params.param("catalogIds", catalogForms.getDeleteCatalogs()));

			// 删除子孙目录下的所有资源
			teachAssistCatalogElementRepo.execute("$deleteByCatalogIds",
					Params.param("catalogIds", catalogForms.getDeleteCatalogs()));
		}

		// 找到原版本下的所有菜单集合
		List<TeachAssistCatalog> oldCatalogs = this.listTeachAssistCatalogCatalog(catalogForms
				.getTeachassistVersionId());

		// 过滤菜单集
		Date date = new Date();
		for (int i = 0; i < catalogForms.getForms().size(); i++) {
			TeachAssistCatalogForm catalogForm = catalogForms.getForms().get(i);
			TeachAssistCatalog catalog = this.catalogFill(catalogForm, date, updateId, i, null);

			// 菜单模块个数，移动判断使用
			long count1 = 0;
			if (!catalogForm.getNewFlag() && catalogForm.getChildren().size() != 0) {
				count1 = teachAssistCatalogElementRepo.find("$getElementCount",
						Params.param("catalogIds", Lists.newArrayList(catalog.getId()))).count();
			}

			// 二级菜单
			for (int j = 0; j < catalogForm.getChildren().size(); j++) {
				TeachAssistCatalogForm catalogForm2 = catalogForm.getChildren().get(j);
				TeachAssistCatalog catalog2 = this.catalogFill(catalogForm2, date, updateId, j, catalog);

				// 需要移动模块的目录
				if (count1 > 0 && !catalogForm.getNewFlag() && catalogForm2.getChildren().size() == 0 && j == 0) {
					long count = teachAssistCatalogElementRepo.find("$getElementCount",
							Params.param("catalogIds", Lists.newArrayList(catalog2.getId()))).count();
					teachAssistCatalogElementRepo.execute("$moveElements", Params.param("oldCatalog", catalog.getId())
							.put("newCatalog", catalog2.getId()).put("newNum", count));
				}

				// 菜单模块个数，移动判断使用
				long count2 = 0;
				if (!catalogForm2.getNewFlag() && catalogForm2.getChildren().size() != 0) {
					count2 = teachAssistCatalogElementRepo.find("$getElementCount",
							Params.param("catalogIds", Lists.newArrayList(catalog2.getId()))).count();
				}

				// 三级菜单
				for (int k = 0; k < catalogForm2.getChildren().size(); k++) {
					TeachAssistCatalogForm catalogForm3 = catalogForm2.getChildren().get(k);
					TeachAssistCatalog catalog3 = this.catalogFill(catalogForm3, date, updateId, k, catalog2);

					// 需要移动模块的目录
					if (count1 > 0 && !catalogForm.getNewFlag() && j == 0 && k == 0) {
						long count = teachAssistCatalogElementRepo.find("$getElementCount",
								Params.param("catalogIds", Lists.newArrayList(catalog3.getId()))).count();
						teachAssistCatalogElementRepo.execute(
								"$moveElements",
								Params.param("oldCatalog", catalog.getId()).put("newCatalog", catalog3.getId())
										.put("newNum", count));
					}
					if (count2 > 0 && !catalogForm2.getNewFlag() && k == 0) {
						long count = teachAssistCatalogElementRepo.find("$getElementCount",
								Params.param("catalogIds", Lists.newArrayList(catalog3.getId()))).count();
						teachAssistCatalogElementRepo.execute(
								"$moveElements",
								Params.param("oldCatalog", catalog2.getId()).put("newCatalog", catalog3.getId())
										.put("newNum", count));
					}
				}
			}
		}
	}

	/**
	 * 菜单填充处理.
	 * 
	 * @param catalogForm
	 * @return
	 */
	private TeachAssistCatalog catalogFill(TeachAssistCatalogForm catalogForm, Date date, Long updateId, int index,
			TeachAssistCatalog parent) {
		TeachAssistCatalog catalog = null;
		if (catalogForm.getNewFlag()) {
			// 新加的菜单
			catalog = new TeachAssistCatalog();
			catalog.setCreateAt(date);
			catalog.setCreateId(updateId);
			catalog.setTeachassistVersionId(catalogForm.getTeachassistVersionId());
		} else {
			// 更新的菜单
			catalog = teachAssistCatalogRepo.get(catalogForm.getId());
		}
		catalog.setPid(parent == null ? 0L : parent.getId());
		catalog.setLevel(parent == null ? 1 : parent.getLevel() + 1);
		catalog.setSequence(index + 1);
		catalog.setName(catalogForm.getName());
		catalog.setUpdateAt(date);
		catalog.setUpdateId(updateId);

		teachAssistCatalogRepo.save(catalog);

		// 需要自动创建模块的目录
		if (catalogForm.getNewFlag() && catalogForm.getChildren().size() == 0
				&& catalogForm.getCatalogTag() != CatalogTag.BLANK && catalogForm.getCatalogTag() != CatalogTag.CHAPTER
				&& catalogForm.getCatalogTag() != CatalogTag.NEW && catalogForm.getCatalogTag() != CatalogTag.REVIEW
				&& catalogForm.getCatalogTag() != CatalogTag.TRAIN) {
			if (catalogForm.getCatalogTag() == CatalogTag.L2_XKDX) {
				// 新课导学
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_TITLE, 1, updateId); // 课时标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 2, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.KNOWLEDGE_SPEC, 3, updateId); // 知识说明
				this.saveElement(catalog.getId(), TeachAssistElementType.LEARN_GOAL, 4, updateId); // 学习目标模块
				this.saveElement(catalog.getId(), TeachAssistElementType.PREPARE_GOAL, 5, updateId); // 预习目标模块
				this.saveElement(catalog.getId(), TeachAssistElementType.PREPARE_COMMENT, 6, updateId); // 预习评价模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_KTXX) {
				// 课堂学习
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.LESSON_TEACH, 2, updateId); // 课内教学模块
				this.saveElement(catalog.getId(), TeachAssistElementType.REVIEW, 3, updateId); // 回顾反思模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_KHZY) {
				// 课后作业
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PRACTICE, 2, updateId); // 习题内容模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_ZSHG) {
				// 知识回顾
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_TITLE, 1, updateId); // 课时标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 2, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.KNOWLEDGE_SPEC, 3, updateId); // 知识说明
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_KTLX) {
				// 课堂练习
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PRACTICE, 2, updateId); // 习题内容模块
				this.saveElement(catalog.getId(), TeachAssistElementType.PRACTICE_COMMENT, 3, updateId); // 预习评价模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_YCYN) {
				// 易错疑难
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.FALLIBLE_POINT, 2, updateId); // 易错点模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_KTXJ) {
				// 课堂小结
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.POINT_STRUCTURE, 2, updateId); // 知识结构模块
				this.saveElement(catalog.getId(), TeachAssistElementType.PROBLEM_SOLVING, 3, updateId); // 解题方法及要点模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_ZXXL
					|| catalogForm.getCatalogTag() == CatalogTag.L2_BZZHPJ) {
				// 专项训练、本章综合评价
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PRACTICE, 2, updateId); // 习题内容模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_ZSTPT) {
				// 知识拓扑图
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_TITLE, 1, updateId); // 课时标题
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 2, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.POINT_MAP, 3, updateId); // 知识拓扑模块
			} else if (catalogForm.getCatalogTag() == CatalogTag.L2_ZSZTZJ
					|| catalogForm.getCatalogTag() == CatalogTag.L2_GLFFZJ
					|| catalogForm.getCatalogTag() == CatalogTag.L2_SXSXZJ) {
				// 知识专题、 规律方法、数学思想
				this.saveElement(catalog.getId(), TeachAssistElementType.PERIOD_CHILD_TITLE, 1, updateId); // 课时子标题
				this.saveElement(catalog.getId(), TeachAssistElementType.TOPIC, 2, updateId); // 专题模块
			}
		}

		return catalog;
	}

	private void saveElement(long catalogId, TeachAssistElementType type, int sequence, long userid) {
		TeachAssistElementForm form = new TeachAssistElementForm();
		form.setCatalogId(catalogId);
		form.setType(type);
		form.setSequence(sequence);
		form.setUserId(userid);
		resconTeachAssistElementService.save(form);
	}

	/**
	 * 初始化一级子菜单.
	 * 
	 * @param names
	 *            名称集合
	 * @param parentCatalog
	 *            一级菜单
	 * @return
	 */
	private List<TeachAssistCatalog> initChildTeachAssistCatalogs(TeachAssistCatalog parentCatalog, String... names) {
		List<TeachAssistCatalog> list = new ArrayList<TeachAssistCatalog>();
		for (int i = 0; i < names.length; i++) {
			TeachAssistCatalog catalog = new TeachAssistCatalog();
			catalog.setCreateAt(parentCatalog.getCreateAt());
			catalog.setCreateId(parentCatalog.getCreateId());
			catalog.setUpdateAt(parentCatalog.getCreateAt());
			catalog.setUpdateId(parentCatalog.getCreateId());
			catalog.setLevel(parentCatalog.getLevel() + 1);
			catalog.setName(names[i]);
			catalog.setPid(parentCatalog.getId());
			catalog.setSequence(i + 1);
			catalog.setTeachassistVersionId(parentCatalog.getTeachassistVersionId());
			list.add(catalog);
		}
		return list;
	}

	@Override
	@Transactional
	public TeachAssistCatalog updateName(long id, String name, long updateId) throws ResourceConsoleException {
		TeachAssistCatalog catalog = teachAssistCatalogRepo.get(id);
		if (catalog != null) {
			Date date = new Date();
			catalog.setName(name);
			catalog.setUpdateAt(date);
			catalog.setUpdateId(updateId);
			teachAssistCatalogRepo.save(catalog);
		}
		return catalog;
	}

	@Override
	@Transactional
	public void up(long id, long updateId) throws ResourceConsoleException {
		TeachAssistCatalog catalog = teachAssistCatalogRepo.get(id);
		if (catalog.getSequence() != 1) {
			Params params = Params.param("sequence", catalog.getSequence() - 1)
					.put("teachAssistVersionId", catalog.getTeachassistVersionId()).put("level", catalog.getLevel())
					.put("pid", catalog.getPid());
			TeachAssistCatalog specifyCatalog = teachAssistCatalogRepo.find("$findSpecifyCatalog", params).get();
			if (null != specifyCatalog) {
				Date updateAt = new Date();
				int specifySequence = specifyCatalog.getSequence();
				specifyCatalog.setSequence(catalog.getSequence());
				specifyCatalog.setUpdateAt(updateAt);
				specifyCatalog.setUpdateId(updateId);
				catalog.setSequence(specifySequence);
				catalog.setUpdateAt(updateAt);
				catalog.setUpdateId(updateId);
				teachAssistCatalogRepo.save(catalog);
				teachAssistCatalogRepo.save(specifyCatalog);
			}
		}
	}

	@Override
	@Transactional
	public void down(long id, long updateId) throws ResourceConsoleException {
		TeachAssistCatalog catalog = teachAssistCatalogRepo.get(id);
		Params params = Params.param("sequence", catalog.getSequence() + 1)
				.put("teachAssistVersionId", catalog.getTeachassistVersionId()).put("level", catalog.getLevel())
				.put("pid", catalog.getPid());
		TeachAssistCatalog specifyCatalog = teachAssistCatalogRepo.find("$findSpecifyCatalog", params).get();
		if (null != specifyCatalog) {
			Date updateAt = new Date();
			int specifySequence = specifyCatalog.getSequence();
			specifyCatalog.setSequence(catalog.getSequence());
			specifyCatalog.setUpdateAt(updateAt);
			specifyCatalog.setUpdateId(updateId);
			catalog.setSequence(specifySequence);
			catalog.setUpdateAt(updateAt);
			catalog.setUpdateId(updateId);
			teachAssistCatalogRepo.save(catalog);
			teachAssistCatalogRepo.save(specifyCatalog);
		}
	}

	@Override
	@Transactional
	public void downLevel(long id, long updateId) throws ResourceConsoleException {
		TeachAssistCatalog catalog = teachAssistCatalogRepo.get(id); // 要下移的目录
		if (catalog.getSequence() != 1) {
			Params params = Params.param("sequence", catalog.getSequence() - 1)
					.put("teachAssistVersionId", catalog.getTeachassistVersionId()).put("level", catalog.getLevel())
					.put("pid", catalog.getPid());
			// 被移入的目录，即将成为父级的目录
			TeachAssistCatalog specifyCatalog = teachAssistCatalogRepo.find("$findSpecifyCatalog", params).get();
			if (null != specifyCatalog) {
				Date date = new Date();
				long childrenCount = this.childrenCount(specifyCatalog.getId());
				catalog.setLevel(catalog.getLevel() + 1);
				catalog.setPid(specifyCatalog.getId());
				catalog.setSequence((int) childrenCount + 1);
				catalog.setUpdateAt(date);
				catalog.setUpdateId(updateId);
				teachAssistCatalogRepo.save(catalog);

				// 更新子目录层级 + 1
				teachAssistCatalogRepo.execute("$updateDownChildrensLevel", Params.param("pid", catalog.getId()));

				// 判断被移入的目录中是否还包含子目录，如果没有，则该目录下的资源需要挂在要下移的目录下
				if (childrenCount == 0) {
					List<TeachAssistCatalog> children = this.children(catalog.getId());
					if (children.size() == 0) {
						// 直接移动到该目录下
						this.moveResource(catalog.getTeachassistVersionId(), specifyCatalog.getId(), catalog.getId());
					} else {
						// 移动到该目录第一个子目录下
						long targetId = children.get(0).getId();
						children = this.children(targetId);
						if (children.size() > 0) {
							targetId = children.get(0).getId();
						}
						this.moveResource(catalog.getTeachassistVersionId(), specifyCatalog.getId(), targetId);
					}
				}
			}
		}
	}

	@Override
	public List<TeachAssistCatalog> children(long pid) {
		return teachAssistCatalogRepo.find("$getChildrenByPId", Params.param("pid", pid)).list();
	}

	@Override
	public long childrenCount(long pid) {
		return teachAssistCatalogRepo.find("$getChildrenCountByPId", Params.param("pid", pid)).count();
	}

	@Override
	public List<Long> allChildrenIds(long pid, int pidLevel) {
		return teachAssistCatalogRepo.find("$allChildren", Params.param("pid", pid).put("pidLevel", pidLevel)).list(
				Long.class);
	}

	/**
	 * 移动目录下的资源.
	 * 
	 * @param teachassistVersionId
	 *            教辅版本
	 * @param srcCatalogId
	 *            原目录
	 * @param destCatalogId
	 *            目标目录
	 */
	@Transactional
	private void moveResource(Long teachassistVersionId, Long srcCatalogId, Long destCatalogId) {
		teachAssistCatalogRepo.execute(
				"$moveResource",
				Params.param("teachassistVersionId", teachassistVersionId).put("srcCatalogId", srcCatalogId)
						.put("destCatalogId", destCatalogId));
	}

	@Override
	@Transactional
	public void delete(Collection<Long> catalogIds, boolean confirm, long updateId) throws ResourceConsoleException {
		// TeachAssistCatalog catalog = teachAssistCatalogRepo.get(catalogId);
		// List<Long> allChildrenIds = this.allChildrenIds(catalog.getId(),
		// catalog.getLevel()); // 子孙目录
		// allChildrenIds.add(catalog.getId());

		// 有内容异常提示
		if (confirm) {
			long count = teachAssistCatalogElementRepo.find("$getElementCount", Params.param("catalogIds", catalogIds))
					.count();
			if (count > 0) {
				throw new ResourceConsoleException(ResourceConsoleException.CATALOG_RESOURCE_NOT_NULL);
			}
		}

		// 删除子孙目录
		// teachAssistCatalogRepo.execute("$deleteByIds",
		// Params.param("catalogIds", catalogIds));
		//
		// // 删除子孙目录下的所有资源
		// teachAssistCatalogElementRepo.execute("$deleteByCatalogIds",
		// Params.param("catalogIds", catalogIds));
		//
		// // 更新删除菜单后面的目录顺序
		// teachAssistCatalogRepo.execute(
		// "$updateSequence",
		// Params.param("teachassistVersionId",
		// catalog.getTeachassistVersionId())
		// .put("sequence", catalog.getSequence()).put("updateId",
		// updateId).put("incr", -1));
	}

	@Override
	public List<TeachAssistCatalog> findLowestList(long teachAssistVersionId) {
		return teachAssistCatalogRepo.find("$findLowestList",
				Params.param("teachAssistVersionId", teachAssistVersionId)).list();
	}

	@Override
	@Transactional
	public void updateCheckStatus(CardStatus status, long id) {
		teachAssistCatalogRepo.execute("$updateCheckStatus", Params.param("status", status.getValue()).put("id", id));
	}

	@Override
	@Transactional
	public Map<Long, TeachAssistCatalog> copy(long versionId, Collection<TeachAssistCatalog> catalogs, long userId) {
		Date now = new Date();
		Map<Long, TeachAssistCatalog> retMap = new HashMap<Long, TeachAssistCatalog>(catalogs.size());
		for (TeachAssistCatalog catalog : catalogs) {
			TeachAssistCatalog newCatalog = new TeachAssistCatalog();
			newCatalog.setCheckStatus(CardStatus.EDITING);
			newCatalog.setCreateAt(now);
			newCatalog.setCreateId(userId);
			newCatalog.setLevel(catalog.getLevel());
			newCatalog.setName(catalog.getName());
			newCatalog.setPid(catalog.getPid());
			newCatalog.setSequence(catalog.getSequence());
			newCatalog.setTeachassistVersionId(versionId);
			newCatalog.setUpdateAt(now);
			newCatalog.setUpdateId(userId);

			teachAssistCatalogRepo.save(newCatalog);

			retMap.put(catalog.getId(), newCatalog);
		}
		for (Map.Entry<Long, TeachAssistCatalog> entry : retMap.entrySet()) {
			TeachAssistCatalog c = entry.getValue();
			if (c.getPid() != 0) {
				c.setPid(retMap.get(c.getPid()).getId());
			}
			teachAssistCatalogRepo.save(c);
		}

		return retMap;
	}

}
