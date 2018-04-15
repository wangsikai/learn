##获取用户会员信息记录
#macro($findByUserId(userId))
SELECT * FROM user_member WHERE user_id = :userId 
#end

## 根据用户id列表获得数据
#macro($findByUserIds(userIds))
SELECT t.* FROM user_member t WHERE t.user_id IN :userIds
#end

## 删除数据
#macro($deleteById(id))
delete from user_member where id=:id
#end