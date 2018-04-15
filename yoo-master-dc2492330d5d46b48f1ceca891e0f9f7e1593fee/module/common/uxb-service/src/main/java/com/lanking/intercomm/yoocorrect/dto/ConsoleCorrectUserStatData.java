package com.lanking.intercomm.yoocorrect.dto;

import java.util.List;

import com.beust.jcommander.internal.Lists;
import com.lanking.cloud.sdk.value.VPage;

import lombok.Data;

/**
 * 转换用用户统计数据对象.
 * 
 * @author peng.zhao
 * @version 2018-3-14
 */
@Data
public class ConsoleCorrectUserStatData {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private VPage<CorrectUserStatResponse> vStatPage = new VPage();
	
	// 所有用户id(在uxb中的id),查询实时在线用户用
	private List<Long> uxbUserIds = Lists.newArrayList();
	
	// 实时批改中用户
	private List<Long> correctingUxbUserIds = Lists.newArrayList();
	
	// 待批改题目数量
	private Long blankCount;
	private Long answerCount;
	private Long allQuestionCount;
}
