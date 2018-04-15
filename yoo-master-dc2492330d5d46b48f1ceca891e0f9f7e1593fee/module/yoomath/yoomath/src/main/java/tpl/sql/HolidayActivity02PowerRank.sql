## 查询总战力排行记录
#macro($getPowerRank(code,userId))
SELECT * FROM holiday_activity_02_power_rank WHERE activity_code = :code and user_id = :userId
#end


## 查询用户总战力排行记录信息
#macro($getUserPowerRank(code,userId))
SELECT user.user_id,user.power,user.activity_user_code,rank.rank FROM holiday_activity_02_user user left join holiday_activity_02_power_rank rank
on user.user_id = rank.user_id and user.activity_code = rank.activity_code
WHERE user.activity_code = :code and user.user_id = :userId
#end

## 查询所有总战力排行记录信息
#macro($getUserPowerRanks(code))
SELECT user.user_id,rank.power,user.activity_user_code, (user.win + user.draw + user.lose) as total,
rank.rank FROM holiday_activity_02_power_rank rank join holiday_activity_02_user user
on user.user_id = rank.user_id and user.activity_code = rank.activity_code
WHERE user.activity_code = :code 
and rank is not null
order by rank limit 100
#end

## 更新用户总战力
#macro($updateUserPower(code,userId, difference))
UPDATE holiday_activity_02_power_rank 
SET power = power + :difference,
real_power = real_power + :difference
WHERE activity_code = :code 
and user_id = :userId
#end