## 查询所有总战力排行记录信息
#macro($getUserPowerInfos(code))
SELECT rank.id,user.user_id,rank.power,user.activity_user_code, (user.win + user.draw + user.lose) as total
FROM holiday_activity_02_power_rank rank join holiday_activity_02_user user
on user.user_id = rank.user_id and user.activity_code = rank.activity_code
WHERE user.activity_code = :code 
order by power desc,user.win/total desc,total desc 
#end

## 更新所有总战力排行记录信息
#macro($updateUserPowerRanks(id,rank0))
update holiday_activity_02_power_rank set rank=:rank0,rank0=:rank0 where id = :id
#end

