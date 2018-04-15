## 通过套餐组查询学校
#macro($csGetSchoolByGroupIds(groupIds))
select * from member_package_group_channel t where t.member_package_group_id in (:groupIds) and t.status=0
#end

## 通过学校ID查询学校是否指定套餐组
#macro($csGetyGroupBySchoolIds(schoolIds))
select t.* from member_package_group_channel t where t.school_id in (:schoolIds) and t.status=0
#end

##删除套餐组下所有数据
#macro($csDaleteByGroupId(id))
update member_package_group_channel set status=2 where member_package_group_id=:id 
#end


## 通过渠道code查询套餐对应关系
#macro($csGetyGroupByChannelCodes(channelCodes,schoolId))
select t.* from member_package_group_channel t where t.user_channel_code in (:channelCodes)   
#if(schoolId)
and t.school_id=:schoolId
#end
and t.status=0
#end

