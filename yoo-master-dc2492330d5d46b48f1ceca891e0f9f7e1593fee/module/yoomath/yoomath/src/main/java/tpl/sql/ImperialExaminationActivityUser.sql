## 获取用户报名信息
#macro($getByUser(userId,code))
SELECT * FROM imperial_exam_activity_user WHERE user_id=:userId and activity_code =:code
#end