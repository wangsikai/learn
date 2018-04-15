#macro($listRulesByActivity(activityCode))
SELECT * FROM lottery_activity_rule WHERE activity_code=:activityCode
#end
