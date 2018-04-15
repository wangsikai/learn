#macro($findByCorrectUserId(correctUserId))
select * from trustrank_stat_log l where l.correct_user_id=:correctUserId
#end