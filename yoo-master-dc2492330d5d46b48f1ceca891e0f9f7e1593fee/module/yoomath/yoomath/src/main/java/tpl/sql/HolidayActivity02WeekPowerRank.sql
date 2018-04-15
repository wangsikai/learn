## 查询周战力排行记录
#macro($getWeekPowerRank(code,userId,time))
SELECT * FROM holiday_activity_02_week_power_rank WHERE activity_code = :code and user_id = :userId 
and start_time<=:time and end_time >=:time
#end

## 查询用户周战力排行记录信息
#macro($getUserWeekPowerRank(code,userId,time))
SELECT user.user_id,rank.week_power as power,user.activity_user_code,rank.rank FROM holiday_activity_02_user as user left join holiday_activity_02_week_power_rank as rank
on user.user_id = rank.user_id and user.activity_code = rank.activity_code and rank.start_time<=:time and rank.end_time >=:time
WHERE user.activity_code = :code and user.user_id = :userId 
#end

## 查询所有周战力排行记录信息
#macro($getUserWeekPowerRanks(code,time))
SELECT user.user_id,rank.week_power as power,user.activity_user_code, (user.win + user.draw + user.lose) as total,
rank.rank FROM holiday_activity_02_week_power_rank as rank join holiday_activity_02_user as user
on user.user_id = rank.user_id and user.activity_code = rank.activity_code
WHERE user.activity_code = :code and rank.start_time<=:time and rank.end_time >=:time
and rank is not null
order by rank limit 10
#end

## 更新用户周战力
#macro($updateUserWeekPower(code,userId,time,difference))
UPDATE holiday_activity_02_week_power_rank 
SET week_power = week_power + :difference,
real_week_power = real_week_power + :difference
WHERE activity_code = :code 
and user_id = :userId 
and start_time <= :time 
and end_time >= :time
#end
