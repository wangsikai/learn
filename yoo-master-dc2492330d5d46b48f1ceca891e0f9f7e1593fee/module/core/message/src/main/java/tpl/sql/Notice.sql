#macro($getUnreadNoticeCount(uid,catgory,type))
SELECT count(id) FROM notice 
WHERE uid = :uid AND status = 0
#if(catgory)
AND catgory = :catgory
#end
#if(type)
AND type = :type
#end
#end

#macro($getUnreadNoticeCountByCatgories(uid,catgories))
SELECT 
count(id) AS cou,catgory
FROM notice 
WHERE uid = :uid AND status = 0 AND catgory IN :catgories GROUP BY catgory
#end

#macro($getUnreadNoticeCountByTypes(uid,types))
SELECT 
count(id) AS cou,type
FROM notice 
WHERE uid = :uid AND status = 0 AND type IN :types GROUP BY type
#end

#macro($updateNoticeStatus(uid,id,status,readAt))
UPDATE notice SET status = :status 
#if(status == 1) 
#if(readAt) 
,read_at = :readAt 
#end 
#end 
WHERE id = :id AND uid = :uid 
#end

#macro($updateNoticesStatus(uid,ids,status,readAt))
UPDATE notice SET status = :status 
#if(status == 1) 
#if(readAt) 
,read_at = :readAt 
#end 
#end 
WHERE id IN :ids AND uid = :uid
#end

#macro($queryNotices(uid,catgory,type,types,status))
SELECT * FROM notice 
WHERE uid = :uid AND status IN :status
#if(catgory)
AND catgory in :catgory
#end
#if(type)
AND type = :type
#end
#if(types)
AND type IN :types
#end
AND id < :next
ORDER BY create_at DESC
#end

#macro($queryNoticesByPage(uid,catgory,type,types,status))
SELECT * FROM notice 
WHERE uid = :uid AND status IN (:status) and type not in(2,3,4,7)
#if(catgory)
AND catgory in (:catgory)
#end
#if(type)
AND type = :type
#end
#if(types)
AND type IN (:types)
#end
ORDER BY status asc,create_at DESC
#end

#macro($updateNoticeStatusByCatgoryType(uid,catgory,catgories,type,types,status,readAt))
UPDATE notice SET status = :status 
#if(status == 1) 
#if(readAt) 
,read_at = :readAt 
#end 
#end 
WHERE uid = :uid 
#if(catgory)
AND catgory = :catgory
#end
#if(catgories)
AND catgory IN :catgories
#end
#if(type)
AND type = :type
#end
#if(types)
AND type IN :types
AND status != 2
#end
#end