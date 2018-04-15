package com.lanking.uxb.service.examPaper.form;

import java.io.Serializable;
import java.util.List;

import com.beust.jcommander.internal.Lists;

/**
 * 精品试卷导出表单.
 * 
 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
 *
 * @version 2016年8月19日
 */
public class TeaExcellentExampaperExportForm implements Serializable {
	private static final long serialVersionUID = -4285726729005739624L;

	/**
	 * 教师资源试卷商品快照ID.
	 */
	private Long resourcesGoodsSnapshotID;

	/**
	 * 商品快照ID.
	 */
	private Long goodsSnapshotID;

	/**
	 * 资源组卷ID（goodsID或exampaperID至少要有一个）.
	 */
	private Long exampaperID;

	/**
	 * 导出文档范围.
	 */
	private List<Scope> scopes = Lists.newArrayList();

	/**
	 * 纸张设置.
	 */
	private Sets sets;

	private Type type;

	private String host;

	/**
	 * 类型.
	 * 
	 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
	 *
	 * @version 2016年8月19日
	 */
	public enum Scope {
		STUDENT(0), TEACHER(1), ANSWER(2);
		private int value;

		Scope(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public String getZhName() {
			switch (this.value) {
			case 0:
				return "学生卷";
			case 1:
				return "教师卷";
			case 2:
				return "答案";
			default:
				return this.name();
			}
		}
	}

	/**
	 * 纸张设置.
	 * 
	 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
	 *
	 * @version 2016年8月19日
	 */
	public enum Sets {
		A4(0), A3_2(1), K16(2), K8_2(3);
		private int value;

		Sets(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	/**
	 * 文档类型.
	 * 
	 * @author <a href="mailto:wanlong.che@elanking.com">wanlong.che</a>
	 *
	 * @version 2016年8月19日
	 */
	public enum Type {
		DOCX(0), PDF(1);
		private int value;

		Type(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	public Long getResourcesGoodsSnapshotID() {
		return resourcesGoodsSnapshotID;
	}

	public void setResourcesGoodsSnapshotID(Long resourcesGoodsSnapshotID) {
		this.resourcesGoodsSnapshotID = resourcesGoodsSnapshotID;
	}

	public Long getGoodsSnapshotID() {
		return goodsSnapshotID;
	}

	public void setGoodsSnapshotID(Long goodsSnapshotID) {
		this.goodsSnapshotID = goodsSnapshotID;
	}

	public Long getExampaperID() {
		return exampaperID;
	}

	public void setExampaperID(Long exampaperID) {
		this.exampaperID = exampaperID;
	}

	public List<Scope> getScopes() {
		return scopes;
	}

	public void setScopes(List<Scope> scopes) {
		this.scopes = scopes;
	}

	public Sets getSets() {
		return sets;
	}

	public void setSets(Sets sets) {
		this.sets = sets;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

}
