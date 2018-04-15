## 根据学生作业id查找该份作业的所有留言
#macro($findHomeworkMessages(id,scene,userId))
SELECT * FROM homework_message WHERE biz_id = :id 
#if(scene)
and scene = :scene
#end
and status = 0
#if(userId)
AND creator = :userId
#end
ORDER BY create_at ASC
#end


## 根据学生作业id查找该份作业的留言留言数量
#macro($findHomeworkMessagesCount(id,scene,userId))
SELECT count(*) FROM homework_message WHERE biz_id = :id and scene = :scene
and status = 0
#if(userId)
AND creator = :userId
#end
#end

