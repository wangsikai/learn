#macro($getRecentlyContacts(long userId))
SELECT other_id FROM MSG_TIMELINE WHERE main_id=:userId and status = 0 ORDER BY update_at DESC limit 0,15
#end

#macro($updateTimeLineByMainId(userId,count0,update_at))
UPDATE MSG_TIMELINE SET count0=:count0 and update_at=:update_at WHERE main_id=:userId
#end

#macro($getUnReadCount(mainId))
SELECT COUNT(1) FROM MSG_TIMELINE t WHERE t.main_id =:mainId AND t.count0 > 0
#end

#macro($getMsgTimeline(long mainId))
SELECT  a.* FROM MSG_TIMELINE a 
INNER JOIN MSG_INBOX b on a.inbox_msg_id = b.id
WHERE  a.main_id=:mainId and b.status = 0
ORDER BY CASE count0 WHEN 0 THEN 0 ELSE 1 END DESC, UPDATE_AT DESC
#end