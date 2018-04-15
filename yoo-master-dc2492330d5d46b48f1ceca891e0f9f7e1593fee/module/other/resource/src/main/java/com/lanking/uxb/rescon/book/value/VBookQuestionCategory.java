package com.lanking.uxb.rescon.book.value;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * 习题结构分类.
 * 
 * @author wlche
 *
 */
@Getter
@Setter
public class VBookQuestionCategory implements Serializable {
	private static final long serialVersionUID = 9070545637510292398L;

	private Long id;

	/**
	 * 书本ID.
	 */
	private Long bookVersionId;

	/**
	 * 书本章节ID.
	 */
	private Long bookSectionId;

	/**
	 * 名称.
	 */
	private String name;

	/**
	 * 习题个数.
	 */
	private int count;
}
