package com.lanking.uxb.rescon.book.api.impl;

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
import com.lanking.cloud.domain.common.resource.book.BookCatalog;
import com.lanking.cloud.domain.common.resource.book.BookCatalogSection;
import com.lanking.cloud.sdk.data.Params;
import com.lanking.cloud.sdk.util.CollectionUtils;
import com.lanking.uxb.rescon.book.api.ResconBookCatalogManage;
import com.lanking.uxb.rescon.book.api.ResconBookQuestionCategoryManage;
import com.lanking.uxb.service.counter.api.impl.BookCatalogCounterProvider;
import com.lanking.uxb.service.counter.api.impl.BookCounterProvider;

@Service("bookCatalogManage2")
@Transactional(readOnly = true)
public class ResconBookCatalogManageImpl2 implements ResconBookCatalogManage {
	@Autowired
	@Qualifier("BookCatalogRepo")
	Repo<BookCatalog, Long> bookcatalogRepo;

	@Autowired
	private BookCounterProvider bookCounterProvider;

	@Autowired
	private BookCatalogCounterProvider bookCatalogCounterProvider;

	@Autowired
	private ResconBookQuestionCategoryManage resconBookQuestionCategoryManage;

	@Override
	public BookCatalog get(Long bookCatalogId) {
		return bookcatalogRepo.get(bookCatalogId);
	}

	@Override
	public List<BookCatalog> listCatalogs(long bookVersionId, boolean isDesc) {
		return bookcatalogRepo
				.find("$listByBookVersion", Params.param("bookVersionId", bookVersionId).put("isDesc", isDesc)).list();
	}

	@Transactional(readOnly = true)
	@Override
	public List<BookCatalog> getBookCatalog(long bookVersionId) {
		return bookcatalogRepo.find("$getBookCatalog", Params.param("bookVersionId", bookVersionId)).list();
	}

	private void getAllChildren(long pid, List<Long> childrenIds) {
		List<BookCatalog> children = bookcatalogRepo.find("$getChildrenByPId", Params.param("pid", pid)).list();
		for (BookCatalog bookCatalog : children) {
			childrenIds.add(bookCatalog.getId());
			getAllChildren(bookCatalog.getId(), childrenIds);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Map<Long, BookCatalog> mgetBookCatalog(long bookVersionId) {
		List<BookCatalog> children = bookcatalogRepo
				.find("$getBookCatalog", Params.param("bookVersionId", bookVersionId)).list();
		Map<Long, BookCatalog> map = new HashMap<Long, BookCatalog>(children.size());
		for (BookCatalog bookCatalog : children) {
			map.put(bookCatalog.getId(), bookCatalog);
		}
		return map;
	}

	@Transactional
	@Override
	public void delete(long id) {
		BookCatalog p = bookcatalogRepo.get(id);
		if (p != null) {
			List<Long> childrenIds = Lists.newArrayList();
			childrenIds.add(p.getId());
			getAllChildren(p.getId(), childrenIds);
			this.move2NoCatalog(p.getBookVersionId(), childrenIds);
			bookcatalogRepo.execute("$deleteByIds", Params.param("ids", childrenIds));
		}
	}

	@Transactional
	@Override
	public BookCatalog create(long id, long bookVersionId, String name, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		BookCatalog np = new BookCatalog();
		np.setCreateAt(new Date());
		np.setCreateId(updateId);
		np.setUpdateAt(np.getCreateAt());
		np.setUpdateId(np.getCreateId());
		np.setName(name);
		if (p == null) {
			np.setPid(0L);
			np.setLevel(1);
			np.setBookVersionId(bookVersionId);
			Integer max = bookcatalogRepo.find("$getMaxSequenceSameParent",
					Params.param("pid", np.getPid()).put("bookVersionId", bookVersionId)).get(Integer.class);
			np.setSequence(max == null ? 1 : max + 1);
		} else {
			np.setPid(p.getPid());
			np.setLevel(p.getLevel());
			np.setBookVersionId(p.getBookVersionId());
			np.setSequence(p.getSequence() + 1);
			bookcatalogRepo.execute("$updateSequence", Params.param("bookVersionId", bookVersionId)
					.put("sequence", p.getSequence()).put("updateId", updateId).put("nowDate", new Date()));
		}
		return bookcatalogRepo.save(np);
	}

	@Transactional
	@Override
	public BookCatalog updateName(long id, String name, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		p.setName(name);
		p.setUpdateAt(new Date());
		p.setUpdateId(updateId);
		return bookcatalogRepo.save(p);
	}

	@Transactional
	@Override
	public void upLevel(long id, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		Params params = Params.param("sequence", p.getSequence()).put("bookVersionId", p.getBookVersionId())
				.put("level", p.getLevel()).put("pid", p.getPid());
		BookCatalog lastP = bookcatalogRepo.find("$findLastCatalog", params).get();
		if (lastP != null) {
			List<BookCatalog> lastPChildren = bookcatalogRepo
					.find("$getChildrenByPId", Params.param("pid", lastP.getId())).list();
			p.setLevel(lastP.getLevel() + 1);
			p.setPid(lastP.getId());
			if (CollectionUtils.isEmpty(lastPChildren)) {
				p.setSequence(1);
			} else {
				p.setSequence(lastPChildren.get(lastPChildren.size() - 1).getSequence() + 1);
			}
			p.setUpdateAt(new Date());
			p.setUpdateId(updateId);
			bookcatalogRepo.save(p);
			// 被移动的所有子目录的level都+1
			List<Long> childrenIds = Lists.newArrayList();
			getAllChildren(id, childrenIds);
			if (childrenIds.size() > 0) {
				bookcatalogRepo.execute("$updateLevel", Params.param("ids", childrenIds).put("delta", 1));
			}
			// 将同层上一个目录下的资源移动到当前目录下或者当前目录下的一个目录下
			if (CollectionUtils.isEmpty(lastPChildren)) {
				List<BookCatalog> moveCataChildren = bookcatalogRepo.find("$getChildrenByPId", Params.param("pid", id))
						.list();
				if (moveCataChildren.size() <= 0) {
					// 清除被移动的目录习题分类结构
					resconBookQuestionCategoryManage.delBookQuestionCategory(lastP.getId(), null);

					this.moveResource(p.getBookVersionId(), lastP.getId(), p.getId());
				} else {
					long targetId = moveCataChildren.get(0).getId();
					moveCataChildren = bookcatalogRepo.find("$getChildrenByPId", Params.param("pid", targetId)).list();
					if (moveCataChildren.size() > 0) {
						targetId = moveCataChildren.get(0).getId();
					}
					// 清除被移动的目录习题分类结构
					resconBookQuestionCategoryManage.delBookQuestionCategory(lastP.getId(), null);

					this.moveResource(p.getBookVersionId(), lastP.getId(), targetId);
				}
			}
		}
	}

	@Transactional
	private void moveResource(Long bookVersionId, Long srcCatalogId, Long destCatalogId) {
		bookcatalogRepo.execute("$moveResource", Params.param("bookVersionId", bookVersionId)
				.put("srcCatalogId", srcCatalogId).put("destCatalogId", destCatalogId));

		// 目录资源计数
		long count = bookCatalogCounterProvider.getResourceCount(srcCatalogId);
		bookCatalogCounterProvider.incrResourceCount(srcCatalogId, 0 - (int) count);
		bookCatalogCounterProvider.incrResourceCount(destCatalogId, (int) count);
	}

	@Transactional
	@Override
	public void downLevel(long id, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		if (p.getPid() != null && p.getPid() != 0) {
			BookCatalog parent = bookcatalogRepo.get(p.getPid());
			p.setPid(parent.getPid());
			p.setLevel(parent.getLevel());
			p.setSequence(parent.getSequence() + 1);
			p.setUpdateAt(new Date());
			p.setUpdateId(updateId);
			bookcatalogRepo.save(p);
			bookcatalogRepo.execute("$updateSequence", Params.param("bookVersionId", p.getBookVersionId())
					.put("sequence", p.getSequence()).put("updateId", updateId).put("nowDate", new Date()));
			// 被移动的所有子目录的level都-1
			List<Long> childrenIds = Lists.newArrayList();
			getAllChildren(id, childrenIds);
			if (childrenIds.size() > 0) {
				bookcatalogRepo.execute("$updateLevel", Params.param("ids", childrenIds).put("delta", -1));
			}
		}
	}

	@Transactional
	@Override
	public void down(long id, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		Params params = Params.param("sequence", p.getSequence()).put("bookVersionId", p.getBookVersionId())
				.put("level", p.getLevel()).put("pid", p.getPid());
		BookCatalog nextP = bookcatalogRepo.find("$findNextCatalog", params).get();
		if (nextP != null) {
			int nextPSequence = nextP.getSequence();
			nextP.setSequence(p.getSequence());
			nextP.setUpdateAt(new Date());
			nextP.setUpdateId(updateId);
			bookcatalogRepo.save(nextP);
			p.setSequence(nextPSequence);
			p.setUpdateAt(nextP.getUpdateAt());
			p.setUpdateId(updateId);
			bookcatalogRepo.save(p);
		}
	}

	@Transactional
	@Override
	public void up(long id, long updateId) {
		BookCatalog p = bookcatalogRepo.get(id);
		Params params = Params.param("sequence", p.getSequence()).put("bookVersionId", p.getBookVersionId())
				.put("level", p.getLevel()).put("pid", p.getPid());
		BookCatalog lastP = bookcatalogRepo.find("$findLastCatalog", params).get();
		if (lastP != null) {
			int lastPSequence = lastP.getSequence();
			lastP.setSequence(p.getSequence());
			lastP.setUpdateAt(new Date());
			lastP.setUpdateId(updateId);
			bookcatalogRepo.save(lastP);
			p.setSequence(lastPSequence);
			p.setUpdateAt(lastP.getUpdateAt());
			p.setUpdateId(updateId);
			bookcatalogRepo.save(p);
		}
	}

	@Transactional
	@Override
	public List<BookCatalog> save(Collection<BookCatalog> catalogs) {
		return bookcatalogRepo.save(catalogs);
	}

	@Transactional
	@Override
	public int move2NoCatalog(Long bookVersionId, List<Long> catalogIds) {
		Params params = Params.param("bookVersionId", bookVersionId);
		if (CollectionUtils.isNotEmpty(catalogIds)) {
			if (catalogIds.size() == 1) {
				params.put("catalogId", catalogIds.get(0));
			} else {
				params.put("catalogIds", catalogIds);
			}
		}
		int updateCount = bookcatalogRepo.execute("$move2NoCatalog", params);
		bookCounterProvider.incrNoCatalogResourceCount(bookVersionId, updateCount);
		return updateCount;
	}

	@Transactional
	@Override
	public Map<Long, BookCatalog> saveCopy(long bookVersionId, Collection<BookCatalog> catalogs, long createId,
			Map<Long, BookCatalogSection> bookCatalogSectionMap) {
		Map<Long, BookCatalog> map = new HashMap<Long, BookCatalog>(catalogs.size());
		Date date = new Date();
		for (BookCatalog bookCatalog : catalogs) {
			long oldId = bookCatalog.getId();
			bookCatalog.setId(null);
			if (bookCatalog.getPid() != null) {
				BookCatalog newBookCatalog = map.get(bookCatalog.getPid());
				if (null != newBookCatalog) {
					bookCatalog.setPid(newBookCatalog.getId());
				}
			}
			bookCatalog.setBookVersionId(bookVersionId);
			bookCatalog.setCreateAt(date);
			bookCatalog.setCreateId(createId);
			bookCatalog.setUpdateAt(date);
			bookcatalogRepo.save(bookCatalog);
			map.put(oldId, bookCatalog);
		}
		return map;
	}

}
