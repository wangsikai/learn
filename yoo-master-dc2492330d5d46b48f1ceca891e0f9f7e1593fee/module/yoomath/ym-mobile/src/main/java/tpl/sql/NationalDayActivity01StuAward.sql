## 查询中奖榜单
#macro($findStuAward())
SELECT *
FROM national_day_activity_01_stu_award
ORDER BY award ASC,rank ASC
#end