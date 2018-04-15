package com.lanking.uxb.rescon.book.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 书本目录.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 * @version 2015年10月27日
 */
public class VBookCatalog implements Serializable {
	private static final long serialVersionUID = -7579795451074636168L;

	private long id;
	private long bookVersionId;
	private int level;
	private int sequence;
	private String name;
	private Long pid;
	private String index;
	private long resourceCount = 0;
	private List<VBookCatalog> children = Lists.newArrayList();

	/**
	 * 是否关联了章节（教辅图书使用）.
	 * 
	 * @since 中央资源库 v1.3.4 2017-7-20
	 */
	private boolean hasLinkSection = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBookVersionId() {
		return bookVersionId;
	}

	public void setBookVersionId(long bookVersionId) {
		this.bookVersionId = bookVersionId;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public long getResourceCount() {
		return resourceCount;
	}

	public void setResourceCount(long resourceCount) {
		this.resourceCount = resourceCount;
	}

	public List<VBookCatalog> getChildren() {
		return children;
	}

	public void setChildren(List<VBookCatalog> children) {
		this.children = children;
	}

	public boolean isHasLinkSection() {
		return hasLinkSection;
	}

	public void setHasLinkSection(boolean hasLinkSection) {
		this.hasLinkSection = hasLinkSection;
	}

}
