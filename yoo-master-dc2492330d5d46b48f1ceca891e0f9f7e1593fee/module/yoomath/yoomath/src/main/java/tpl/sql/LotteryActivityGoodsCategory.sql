#macro($listCategorysByActivity(activityCode))
SELECT * FROM lottery_activity_goods_category WHERE activity_code=:activityCode order by level ASC
#end