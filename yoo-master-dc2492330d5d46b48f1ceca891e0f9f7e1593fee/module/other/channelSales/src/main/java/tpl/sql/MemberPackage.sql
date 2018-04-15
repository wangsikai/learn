##查询套餐信息
#macro($csGetMemberPackages(userType,memberType,oderByMonth))
select * from member_package where user_type =:userType and member_type=:memberType and status=0 
#if(oderByMonth==true)
ORDER BY month0,id ASC
#elseif(oderByMonth==false)
ORDER BY sequence ASC
#end
#end

##删除套餐
#macro($csDaleteMemberPackages(id))
update member_package set status=2 where id=:id
#end

##删除套餐组下所有数据
#macro($csDaleteMemberPackagesByGroupId(id))
update member_package set status=2 where member_package_group_id=:id 
#end

## 查询所有数据
#macro($csFindAll(mtype,utype))
SELECT t.* FROM member_package t WHERE t.status = 0
#if(mtype)
 and member_type =:mtype
#end
#if(utype)
 and user_type =:utype
#end
ORDER BY sequence ASC
#end

## 查询套餐
#macro($csFindPackage(utype, mtype, schoolId))
SELECT t.* FROM member_package t
#if(schoolId)
INNER JOIN member_package_group_channel mg ON t.member_package_group_id = mg.member_package_group_id
#end
WHERE t.status = 0
#if(mtype)
AND t.member_type = :mtype
#end
#if(utype)
AND t.user_type = :utype
#end
#if(schoolId)
AND mg.school_id = :schoolId
#end
#end

## 查询渠道商套餐列表
#macro($csFindChannelPackages(userType,memberType,schoolId,channelCode,schoolId,groupType))
SELECT DISTINCT t.* FROM member_package t
#if(userType==1)
WHERE t.user_type = 1 AND t.member_type = :memberType  AND t.status = 0
#end
#if(userType==2)
LEFT JOIN member_package_group g ON g.id = t.member_package_group_id
LEFT JOIN member_package_group_channel gc ON gc.member_package_group_id = g.id
#if(schoolId)
LEFT JOIN user_channel uc ON uc.code = gc.user_channel_code
LEFT JOIN channel_school cs ON cs.channel_code = uc.code
#end
WHERE
  t.status = 0
  #if(schoolId)
  AND uc.code = :channelCode AND gc.status = 0
  #end
  AND g.status = 0
  AND t.member_type = :memberType
  #if(groupType!=2)
  #if(schoolId)
  AND g.type = 1 AND gc.school_id = :schoolId AND gc.status = 0
  #else
  #if(channelCode)
    AND g.type = 1 AND gc.user_channel_code = :channelCode AND gc.school_id = 0 AND gc.status = 0
  #else
    AND g.type = 0
  #end
  #end
  #end
  #if(groupType==2)
  AND g.type = 2
  #end
#end
#end

## 查询非渠道商套餐
#macro($csFindNotChannelPackages(userType,memberType,schoolId,channelCode,schoolId,groupType))
SELECT DISTINCT t.* FROM member_package t
#if(userType==1)
WHERE t.user_type=1 AND t.member_type = :memberType AND t.status = 0
#end
#if(userType==2)
LEFT JOIN member_package_group g ON g.id = t.member_package_group_id
LEFT JOIN member_package_group_channel gc ON gc.member_package_group_id = g.id
#if(schoolId)
LEFT JOIN user_channel uc ON uc.code = gc.user_channel_code
LEFT JOIN channel_school cs ON cs.channel_code = uc.code
#end
WHERE t.status = 0 AND g.status = 0
#if(schoolId)
AND gc.status = 0
#end
AND t.member_type = :memberType
#if(groupType!=2)
  #if(schoolId)
  AND g.type = 1 AND gc.school_id = :schoolId AND gc.status = 0
  #else
  #if(channelCode)
    AND g.type = 1 AND gc.user_channel_code = :channelCode AND gc.school_id = 0 AND gc.status = 0
  #else
    AND g.type = 0
  #end
  #end
#end
#if(groupType==2)
AND g.type = 2
#end
#end
#end
