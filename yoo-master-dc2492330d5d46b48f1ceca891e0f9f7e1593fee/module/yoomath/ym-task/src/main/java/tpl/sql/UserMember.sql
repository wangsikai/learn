##获取用户会员信息记录
#macro($taskFindByUserId(userId))
SELECT * FROM user_member WHERE user_id = :userId 
#end

## 删除数据
#macro($taskDeleteById(id))
delete from user_member where id=:id
#end