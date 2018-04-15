##查找未删除的教辅
#macro($listTeachAssisVersions(teachAssisIds))
select * from teachassist_version where teachassist_id in (:teachAssisIds) AND del_status!=2 order by create_at ASC
#end

##查找未删除的教辅
#macro($listTeachVersion(teachId))
select * from teachassist_version where teachassist_id=:teachId AND del_status!=2 ORDER BY create_at DESC limit 0,2
#end

##获取最大的版本号
#macro($getMaxVersion(teachAssistId))
select max(version) from teachassist_version where teachassist_id=:teachAssistId AND del_status!=2
#end


##除当前版本，其他更新为不是主版本
#macro($updateMainFlag(teachAssistId,versionId))
update teachassist_version set main_flag = 0
where teachassist_id=:teachAssistId and id != :versionId
AND del_status!=2
#end


##获取教辅统计数据
#macro($getStat(vendorId))
SELECT COUNT(a.id) count,a.status FROM teachassist_version a 
INNER JOIN teachassist b ON a.teachassist_id = b.id AND b.vendor_id = :vendorId
WHERE a.del_status = 0 GROUP BY a.STATUS 
#end


