#macro($getUserMsgs(mainId,otherId))
SELECT a.main_id,a.other_id,b.body,b.send_at,a.msg_id,b.send_id FROM msg_inbox a,msg b
WHERE
  a.main_id = :mainId
  AND a.other_id = :otherId
  AND a.msg_id = b.id
  AND a.status = 0
  AND a.msg_id < :next
ORDER BY a.msg_id desc
#end

#macro($getMessageCount(mainId,otherId))
SELECT count(a.msg_id) FROM msg_inbox a,msg b
WHERE
  a.main_id = :mainId
  AND a.other_id = :otherId
  AND a.msg_id = b.id
  AND a.status = 0
ORDER BY a.msg_id desc
#end

#macro($getMsgsByUser(mainId,otherId))
SELECT b.* FROM msg_inbox a
INNER JOIN msg b on a.msg_id = b.id
WHERE a.main_id = :mainId
  AND a.other_id = :otherId
  AND a.status = 0
  AND a.msg_id < :next
ORDER BY b.id desc
#end