package com.lanking.uxb.rescon.teach.value;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;
import com.lanking.cloud.domain.common.resource.card.CardStatus;

/**
 * 教辅菜单VO.
 * 
 * @author wlche
 *
 */
public class VTeachAssistCatalog implements Serializable {
	private static final long serialVersionUID = -2178898578990030459L;

	private long id;
	private long teachassistVersionId;
	private Long pid;
	private int level;
	private int sequence;
	private String name;
	private String index = "";
	private CardStatus checkStatus;
	private long resourceCount = 0;
	private List<VTeachAssistCatalog> children = Lists.newArrayList();
	private boolean newFlag = false;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTeachassistVersionId() {
		return teachassistVersionId;
	}

	public void setTeachassistVersionId(long teachassistVersionId) {
		this.teachassistVersionId = teachassistVersionId;
	}

	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
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

	public CardStatus getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(CardStatus checkStatus) {
		this.checkStatus = checkStatus;
	}

	public List<VTeachAssistCatalog> getChildren() {
		return children;
	}

	public void setChildren(List<VTeachAssistCatalog> children) {
		this.children = children;
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

	public boolean isNewFlag() {
		return newFlag;
	}

	public void setNewFlag(boolean newFlag) {
		this.newFlag = newFlag;
	}
}
