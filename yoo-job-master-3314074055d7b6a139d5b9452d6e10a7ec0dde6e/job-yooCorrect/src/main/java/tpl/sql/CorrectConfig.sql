##根据用户类型查询等待中的批改人员
#macro($queryCorrectConfigs())
SELECT * FROM correct_config
WHERE status = 0
ORDER BY ID DESC
#end

##更新
#macro($updateRewardCfg(id, rewardConfig))
UPDATE correct_config SET reward_cfg = :rewardConfig WHERE id = :id
#end