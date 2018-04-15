package com.lanking.cloud.domain.yoo.activity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 活动表（所有类型活动在此表中都有一条对应的记录）
 * 
 * @author <a href="mailto:sikai.wang@elanking.com">sikai.wang</a>
 * @version 2017年8月26日
 */
@Getter
@Setter
@Entity
@Table(name = "activity")
public class Activity implements Serializable {

	private static final long serialVersionUID = 1155699076176957330L;

	/**
	 * 活动标识
	 */
	@Id
	@Column(name = "code")
	private Long code;

	/**
	 * 活动名称
	 */
	@Column(name = "name", length = 128)
	private String name;

	/**
	 * 关联活动表
	 */
	@Column(name = "activity_table", length = 128)
	private String activityTable;

}
