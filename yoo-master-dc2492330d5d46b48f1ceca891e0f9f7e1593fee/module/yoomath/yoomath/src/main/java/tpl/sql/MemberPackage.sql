##查询会员套餐
#macro($queryMemberPackage(userType, memberType))
select * from member_package where status=0
#if(userType)
 AND user_type=:userType
#end
#if(memberType)
 AND member_type=:memberType
#end
 order by sequence ASC
#end

##查询指定渠道对应的套餐组
#macro($queryMemberPackageGroupByAppointChannel(userType, memberType,channelCode,schoolId))
SELECT c.* FROM member_package_group a
INNER JOIN member_package_group_channel b ON a.id = b.member_package_group_id
INNER JOIN member_package c ON c.member_package_group_id = a.id
WHERE a.status = 0 AND a.type = 1 AND b.status = 0 AND c.status = 0
#if(userType)
	AND a.user_type = :userType
#end
#if(memberType)
	AND a.member_type = :memberType
#end
#if(channelCode)
	AND b.user_channel_code = :channelCode
#end
#if(schoolId)
	AND b.school_id = :schoolId
#elseif
	AND b.school_id = 0
#end
#end

##查询全部渠道对应的套餐组
#macro($queryMemberPackageGroupByAllChannel(userType, memberType))
SELECT c.* FROM member_package_group a 
INNER JOIN member_package c ON a.id = c.member_package_group_id
WHERE a.status = 0 AND a.type = 0  AND c.status = 0
#if(userType)
	AND a.user_type = :userType
#end
#if(memberType)
	AND a.member_type = :memberType
#end
#end

##查询注册用户对应的套餐组
#macro($queryMemberPackageByAutoRegister(userType, memberType))
SELECT c.* FROM member_package_group a 
INNER JOIN member_package c ON a.id = c.member_package_group_id
WHERE a.status = 0 AND a.type = 2  AND c.status = 0
#if(userType)
	AND a.user_type = :userType
#end
#if(memberType)
	AND a.member_type = :memberType
#end
#end
