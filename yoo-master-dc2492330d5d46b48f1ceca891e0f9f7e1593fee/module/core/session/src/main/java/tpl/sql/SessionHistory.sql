#macro($delHistory(token))
DELETE FROM session_history WHERE token =:token
#end