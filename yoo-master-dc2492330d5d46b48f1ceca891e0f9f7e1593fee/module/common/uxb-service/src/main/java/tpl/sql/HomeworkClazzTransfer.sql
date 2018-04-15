##获取最新的
#macro($getLastest(userId))
SELECT Max(id) FROM homework_class_transfer where to0 = :userId and read_at is null
#end

##读取转让记录
#macro($readTransferLog(userId,id,nowTime))
update homework_class_transfer set read_at = :nowTime where to0 = :userId and id <= :id
#end