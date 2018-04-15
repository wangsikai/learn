#macro($delGuestHistory(token))
DELETE FROM session_guest_history WHERE token =:token
#end