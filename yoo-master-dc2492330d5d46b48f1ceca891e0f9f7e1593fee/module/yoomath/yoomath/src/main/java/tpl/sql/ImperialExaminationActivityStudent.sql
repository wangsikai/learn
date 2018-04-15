## 获取用户报名信息
#macro($getByUser(userId,code))
SELECT * FROM imperial_exam_activity_student WHERE user_id=:userId and activity_code =:code
#end

## 获取用户报名信息
#macro($getByUsers(userIds,code))
SELECT * FROM imperial_exam_activity_student 
WHERE user_id IN (:userIds) 
and activity_code =:code
#end