package com.lanking.uxb.rescon.teach.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.lanking.cloud.sdk.bean.Valueable;

/**
 * 教辅菜单.
 * 
 * @author wlche
 *
 */
public class TeachAssistCatalogForm implements Serializable {
	private static final long serialVersionUID = 771507428782947614L;

	private Long id;
	private Long teachassistVersionId;
	private Long pid;
	private int level;
	private int sequence;
	private String name;
	private CatalogTag catalogTag; // 菜单标签类型
	private Boolean newFlag = false; // 是否为新建的菜单，此时前台预置的ID无效
	private List<TeachAssistCatalogForm> children = new ArrayList<TeachAssistCatalogForm>(); // 子菜单

	public enum CatalogTag implements Valueable {
		/**
		 * 空白目录.
		 */
		BLANK(0),
		/**
		 * 新授课时.
		 */
		NEW(1),
		/**
		 * 训练课时
		 */
		TRAIN(2),
		/**
		 * 复习课时.
		 */
		REVIEW(3),
		/**
		 * 本章复习
		 */
		CHAPTER(4),

		// 二级菜单

		/**
		 * 新课导学
		 */
		L2_XKDX(5),

		/**
		 * 课堂学习
		 */
		L2_KTXX(6),

		/**
		 * 课后作业
		 */
		L2_KHZY(7),

		/**
		 * 知识回顾
		 */
		L2_ZSHG(8),

		/**
		 * 课堂练习
		 */
		L2_KTLX(9),

		/**
		 * 易错疑难
		 */
		L2_YCYN(10),

		/**
		 * 课堂小结
		 */
		L2_KTXJ(11),

		/**
		 * 专项训练
		 */
		L2_ZXXL(12),

		/**
		 * 知识拓扑图
		 */
		L2_ZSTPT(13),

		/**
		 * 知识专题总结
		 */
		L2_ZSZTZJ(14),

		/**
		 * 规律方法总结
		 */
		L2_GLFFZJ(15),

		/**
		 * 数学思想总结
		 */
		L2_SXSXZJ(16),

		/**
		 * 本章综合评价
		 */
		L2_BZZHPJ(17);

		private int value;

		CatalogTag(int value) {
			this.value = value;
		}

		@Override
		public int getValue() {
			return value;
		}

		public String getName() {
			switch (value) {
			case 1:
				return "新授课时";
			case 2:
				return "训练课时";
			case 3:
				return "复习课时";
			case 4:
				return "本章复习";
			default:
				return "";
			}
		}

		public static CatalogTag findByValue(int value) {
			switch (value) {
			case 0:
				return BLANK;
			case 1:
				return NEW;
			case 2:
				return TRAIN;
			case 3:
				return REVIEW;
			case 4:
				return CHAPTER;
			default:
				return BLANK;
			}
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTeachassistVersionId() {
		return teachassistVersionId;
	}

	public void setTeachassistVersionId(Long teachassistVersionId) {
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

	public CatalogTag getCatalogTag() {
		return catalogTag;
	}

	public void setCatalogTag(CatalogTag catalogTag) {
		this.catalogTag = catalogTag;
	}

	public Boolean getNewFlag() {
		return newFlag;
	}

	public void setNewFlag(Boolean newFlag) {
		this.newFlag = newFlag;
	}

	public List<TeachAssistCatalogForm> getChildren() {
		return children;
	}

	public void setChildren(List<TeachAssistCatalogForm> children) {
		this.children = children == null ? new ArrayList<TeachAssistCatalogForm>(0) : children;
	}
}
