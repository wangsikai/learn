#macro($getSessionByToken(token))
SELECT * FROM session WHERE token =:token
#end

#macro($findInvalidWebSession(timeOutTime,fetchCount))
SELECT * FROM session WHERE device_type = 7 AND (status = 2 OR status = 3 OR active_at < :timeOutTime) LIMIT :fetchCount
#end

#macro($findInvalidMobileSession(timeOutTime,fetchCount))
SELECT * FROM session WHERE device_type != 7 AND (status = 2 OR status = 3 OR active_at < :timeOutTime) LIMIT :fetchCount
#end

#macro($findSessionByUserId(userId))
SELECT * FROM session WHERE (status = 0 OR status = 1) AND user_id = :userId
#end