##根据针对性训练查询对应的操作集合
#macro($findLogList(specialTrainingId))
SELECT * FROM special_training_operate_log WHERE special_training_id = :specialTrainingId and type !=1
ORDER BY create_at DESC limit 0,11
#end