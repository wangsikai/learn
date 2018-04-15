##分页查询
#macro($csQuery())
SELECT * FROM user_channel ORDER BY code DESC
#end

##检验帐号是否存在
#macro($csGetConUserByName(conName))
SELECT count(1) FROM con_user where name=:conName and system_id=5
#end

##绑定账户
#macro($csBindConName(code,conName))
update  user_channel set con_user_id =(select id from con_user where name =:conName and system_id=5)
where code=:code
#end

##解除绑定
#macro($csRemoveBind(code))
update  user_channel set con_user_id =null where code=:code
#end

## 根据学校查找其渠道商
#macro($csFindBySchool(schoolId))
SELECT t.* FROM user_channel t INNER JOIN channel_school c ON c.channel_code = t.code
WHERE c.school_id = :schoolId
#end

## 根据学校列表查找渠道商
#macro($csFindBySchools(schoolIds))
SELECT t.*, c.school_id FROM user_channel t INNER JOIN channel_school c ON c.channel_code = t.code
WHERE c.school_id IN :schoolIds
#end

##查询用户绑定的渠道
#macro($csGetChannel(userId))
select * from user_channel where con_user_id=:userId
#end

##查询用户绑定的渠道
#macro($csGetChannelByName(conName))
select * from user_channel uc INNER JOIN con_user cu on uc.con_user_id=cu.id and cu.name =:conName
#end

##获取当前最大的code
#macro($csgetMaxCode())
SELECT max(code) code FROM user_channel
#end


##恢复渠道商透支额度
#macro($csUpdateUserChannelLimit(channelCode, addLimit))
update user_channel set opened_member=opened_member-:addLimit where code=:channelCode
#end

##查询渠道订单首单开始时间
#macro($csGetFirstYearByChannelSettlement(code))
select LEFT(mocs.create_at,4) from user_channel t INNER JOIN  member_package_order_channel_settlement mocs on t.code = mocs.channel_code and mocs.status =0 where t.code=:code  ORDER BY mocs.create_at ASC LIMIT 1
#end







