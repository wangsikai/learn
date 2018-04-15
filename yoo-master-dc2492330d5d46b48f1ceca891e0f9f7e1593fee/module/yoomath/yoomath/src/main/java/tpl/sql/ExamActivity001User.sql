## 查找中奖用户资料
#macro($ymFindUser(userId,code))
SELECT * FROM exam_activity_001_user WHERE user_id=:userId and activity_code =:code
#end

## 修改用户资料
#macro($ymUpdateUser(id,grade,category))
UPDATE exam_activity_001_user set grade=:grade,textbook_category_code=:category where id=:id
#end

## 修改用户资料
#macro($ymUpdateUserQNum(id,qNum))
UPDATE exam_activity_001_user set q_num=:qNum where id=:id
#end

