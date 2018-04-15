##查询套餐信息
#macro($csGetMemberPackageGroups(userType,memberType))
select * from member_package_group where user_type =:userType and member_type=:memberType and status=0 ORDER BY id DESC
#end
