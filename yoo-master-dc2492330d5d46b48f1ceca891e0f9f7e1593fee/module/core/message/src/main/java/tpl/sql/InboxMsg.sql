#macro($updateInboxMsg(userId,otherId,status))
UPDATE MSG_INBOX SET status=:status WHERE main_id=:userId and other_id =:otherId
#end

#macro($updateOneInboxMsg(userId,otherId,msgId,status))
UPDATE MSG_INBOX SET status=:status WHERE main_id=:userId and other_id =:otherId and msg_id =:msgId
#end

#macro($updateAllInboxMsg(userId,status))
UPDATE MSG_INBOX SET status=:status WHERE main_id=:userId
#end

#macro($getAllNewMessage(userId))
SELECT B.send_at,A.other_id ,B.BODY ,C.count0,b.send_id
FROM MSG_INBOX a 
INNER JOIN  MSG b ON A.MSG_ID= B.id 
INNER JOIN  MSG_TIMELINE C ON  A.MAIN_ID= C.MAIN_ID AND A.OTHER_ID = C.OTHER_ID
INNER JOIN (SELECT MAX(send_at) sendtime,other_id FROM MSG_INBOX e,MSG f
				 WHERE main_id = :userId 
				AND e.MSG_ID= f.id 
				GROUP BY other_id) d 
	ON b.send_at=d.sendtime
AND a.main_id = :userId
AND a.status = 0
ORDER BY B.send_at desc,c.count0 desc
#end