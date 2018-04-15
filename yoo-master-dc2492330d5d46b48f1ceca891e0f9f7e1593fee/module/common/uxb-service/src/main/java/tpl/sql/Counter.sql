#macro($getCounter(biz,bizId))
SELECT * FROM counter 
WHERE biz = :biz AND biz_id = :bizId
#end

#macro($getCounters(biz,bizIds))
SELECT * FROM counter 
WHERE biz = :biz AND biz_id IN :bizIds
#end
