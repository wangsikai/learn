#macro($listByActivityCode(activityCode))
SELECT * FROM lottery_activity_goods WHERE activity_code=:activityCode
#end

#macro($getNothingGoods(activityCode))
SELECT * FROM lottery_activity_goods WHERE activity_code=:activityCode AND activity_goods_type=3
#end