##查询
#macro($queryAllByUser(userId))
SELECT * FROM correct_user_month_stat
WHERE user_id = :userId
#end

##查询
#macro($queryByMonthDate(userId, monthDate))
SELECT * FROM correct_user_month_stat
WHERE user_id = :userId
AND month_date = :monthDate
LIMIT 1
#end