## 通过用户id查询对应的学情分析周报告
#macro($getByUserId(userId))
SELECT * FROM student_week_report WHERE user_id = :userId order by create_at asc
#end

## 通过用户id和指定周时间范围查询唯一的周统计数据
#macro($findWeekReport(userId,startDate,endDate))
SELECT * FROM student_week_report WHERE user_id = :userId and start_date = :startDate and end_date = :endDate
#end
