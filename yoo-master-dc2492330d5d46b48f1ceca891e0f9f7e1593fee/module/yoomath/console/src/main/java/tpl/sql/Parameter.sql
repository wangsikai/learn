## 查询所有
#macro($zycAllParameters(product))
SELECT * FROM parameter where status =0
#if(product)
AND product =:product
#end
#end

##删除配置
#macro($zycUpdateStatusById(id,status))
UPDATE parameter set status =:status where id=:id
#end

##查询Key不重复的包括已删除的
#macro($zycGetParameterCountByKey(key))
SELECT count(1) from parameter where key0 =:key and status=0
#end
