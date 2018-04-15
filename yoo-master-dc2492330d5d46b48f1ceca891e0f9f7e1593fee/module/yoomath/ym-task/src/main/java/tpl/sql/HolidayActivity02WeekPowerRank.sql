## 查询所有周战力排行记录信息
#macro($getUserWeekPowerInfos(code,time))
SELECT rank.id,user.user_id,rank.week_power power,user.activity_user_code, (user.win + user.draw + user.lose) as total
FROM holiday_activity_02_week_power_rank as rank join holiday_activity_02_user as user
on user.user_id = rank.user_id and user.activity_code = rank.activity_code
WHERE user.activity_code = :code and rank.start_time<=:time and rank.end_time >=:time
order by power desc,user.win/total desc,total desc 
#end

## 更新所有周战力排行记录信息
#macro($updateUserWeekPowerRanks(id,rank0))
update holiday_activity_02_week_power_rank set rank=:rank0,rank0=:rank0 where id = :id
#end


