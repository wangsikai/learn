##国庆活动统计学生排名
#macro($queryH5PVUV(h5,viewAt))
SELECT * FROM national_day_activity_01_h5pvuv 
WHERE h5 = :h5
AND view_at = :viewAt
#end