package com.lanking.uxb.service.resources.api;

import java.util.List;

import com.lanking.cloud.domain.yoo.user.UserType;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessage;
import com.lanking.cloud.domain.yoomath.homework.HomeworkMessageType;
import com.lanking.cloud.sdk.bean.Status;

/**
 * 提供作业留言相关接口
 * 
 * @author <a href="mailto:qiuxue.jiang@elanking.com">qiuxue.jiang</a>
 * @version 2018年2月9日
 */
public interface HomeworkMessageService {
	
	/**
	 * 根据留言id获取留言信息
	 * 
	 * @param hkId
	 *            作业ID

	 */
	HomeworkMessage get(long id);
	
	/**
	 * 根据留言id更新留言状态
	 * 
	 * @param hkId
	 *            作业ID

	 */
	void updateStatus(long id, Status status);
	
	/**
	 * 根据作业ID获取作业留言信息
	 * 
	 * @param hkId
	 *            作业ID

	 */
	List<HomeworkMessage> findByHkId(long hkId);

	/**
	 * 根据学生作业ID获取学生作业留言信息
	 * 
	 * @param stuHkId
	 *            学生作业ID

	 */
	List<HomeworkMessage> findByStudentHkId(long stuHkId);
	
	/**
	 * 根据学生作业题目ID获取学生作业题目留言信息
	 * 
	 * @param stuHkQId
	 *            学生作业题目ID

	 */
	List<HomeworkMessage> findByStudentHkQId(long stuHkQId);
	
	
	/**
	 * 根据不同的场景和创建用户id查询消息
	 * 
	 * @param id
	 *            id,可能为作业id，学生作业id，学生作业题目id，场景不同id含义不同
	 * @param userId
	 *            创建者的用户id
	 * @param scene
	 *            场景，0 学生习题 1 学生作业  2 整份作业
	 */
	List<HomeworkMessage> findMessages(long id, long userId, Integer scene);
	
	
	/**
	 * 根据不同的场景和创建用户id查询消息的数量是否超出限制
	 * 
	 * @param id
	 *            id,可能为作业id，学生作业id，学生作业题目id，场景不同id含义不同
	 * @param userId
	 *            创建者的用户id
	 * @param scene
	 *            场景，0 学生习题 1 学生作业  2 整份作业
	 */
	Boolean isMessagesExceed(long id, long userId, Integer scene);
	
	
	/**
	 * 添加一条批注
	 * 
	 * @param voiceTime
	 *            语音时间
	 * @param fileKey
	 *            七牛上传后的唯一标识
	 * @param id
	 *            id,可能为作业id，学生作业id，学生作业题目id，场景不同id含义不同
	 * @param scene
	 *            场景，0 学生习题 1 学生作业  2 整份作业
	 * @param type
	 *            留言类型  TEXT 文本  AUDIO 语音  VIDEO 视频
	 * @param comment
	 *            文本留言，留言类型为 0 时有效
	 * @param iconKey
	 *            留言图标key属性
	 * @param userType
	 *            用户类型
	 */
	Long addComment(Integer voiceTime, String fileKey, Long id, HomeworkMessageType type, Integer scene, String comment, String iconKey,UserType userType);
	
}
