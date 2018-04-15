## 查询中奖榜单
#macro($findTeaAward())
SELECT *
FROM national_day_activity_01_tea_award
ORDER BY award ASC,rank ASC
#end