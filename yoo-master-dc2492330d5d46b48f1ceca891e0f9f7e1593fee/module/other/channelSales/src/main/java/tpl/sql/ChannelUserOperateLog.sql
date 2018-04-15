## 查看操作日志
#macro($findLogList(userId,channelCode))
	select * from channel_user_operate_log where user_id =:userId and channel_code =:channelCode
#end


