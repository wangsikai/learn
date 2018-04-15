package com.lanking.uxb.rescon.book.form;

import java.io.Serializable;
import java.util.List;

import com.lanking.cloud.domain.common.resource.question.Question;
import com.lanking.cloud.domain.type.CheckStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookQuestionQueryForm implements Serializable {
	private static final long serialVersionUID = -247097571034503414L;

	/**
	 * 书本版本.
	 */
	Long bookVersionId;

	/**
	 * 书本目录.
	 */
	List<Long> catalogIds;

	/**
	 * 查询关键字.
	 */
	private String key;

	/**
	 * 编码.
	 */
	private String code;

	/**
	 * 题目状态.
	 */
	private CheckStatus checkStatus;

	/**
	 * 题目类型.
	 */
	private Question.Type questionType;

	/**
	 * 是否查询未分组题目
	 */
	private Integer notag = 0;

	/**
	 * 题目标签.
	 */
	private List<Long> questionTags;

	/**
	 * 是否搜索需要v3转换的习题
	 */
	private Integer v3flag = 0;

	/**
	 * 图书目录习题结构分组ID.
	 */
	private Long bookQuestionCategoryId;

	private Integer pageSize = 20;
	private Integer page = 1;

}
